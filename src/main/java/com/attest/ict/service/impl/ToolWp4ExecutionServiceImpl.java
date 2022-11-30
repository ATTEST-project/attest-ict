package com.attest.ict.service.impl;

import com.attest.ict.config.ToolsConfiguration;
import com.attest.ict.custom.tools.julia.JuliaScript;
import com.attest.ict.custom.tools.utils.ToolSimulationReferencies;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.domain.InputFile;
import com.attest.ict.helper.TaskStatus;
import com.attest.ict.helper.excel.reader.T41FileOutputFormat;
import com.attest.ict.helper.ods.utils.T41FileInputFormat;
import com.attest.ict.helper.ods.utils.TSGFileOutputFormat;
import com.attest.ict.service.InputFileService;
import com.attest.ict.service.TaskService;
import com.attest.ict.service.ToolWp4ExecutionService;
import com.attest.ict.service.UserService;
import com.attest.ict.service.dto.InputFileDTO;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.OutputFileDTO;
import com.attest.ict.service.dto.SimulationDTO;
import com.attest.ict.service.dto.TaskDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.custom.ToolConfigParameters;
import com.attest.ict.service.mapper.InputFileMapper;
import com.attest.ict.tools.constants.ToolFileFormat;
import com.attest.ict.tools.exception.RunningToolException;
import com.attest.ict.tools.parameter.constants.ToolWp4Parameters;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ToolWp4ExecutionServiceImpl implements ToolWp4ExecutionService {

    private final Logger log = LoggerFactory.getLogger(ToolWp4ExecutionServiceImpl.class);

    @Autowired
    private InputFileService inputFileServiceImpl;

    @Autowired
    ToolExecutionServiceImpl toolExecutionServiceImpl;

    @Autowired
    private InputFileMapper inputFileMapper;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    private String attestToolsDir;

    private String toolsPathSimulation;

    public ToolWp4ExecutionServiceImpl(ToolsConfiguration toolsConfig) {
        // ATTEST/tools
        this.attestToolsDir = toolsConfig.getPath();
        log.debug("attestToolsDir {}", attestToolsDir);

        // ATSIM
        this.toolsPathSimulation = toolsConfig.getPathSimulation();
        log.debug("toolsPathSimulation {}", toolsPathSimulation);
    }

    @Override
    public String windAndPV(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        String[] fileDesc,
        MultipartFile[] files,
        String[] parameterNames,
        String[] parameterValues
    ) throws RunningToolException, Exception {
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();

        // Create new Task
        TaskDTO taskSaved;
        try {
            taskSaved = toolExecutionServiceImpl.createTask(toolDto);
        } catch (Exception e) {
            String errMsg = "Error creating new task for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg);
        }

        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);

        // Create simulationWorkingDir
        // -- ATS/WPX/TXY/UUID
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid.toString());
        Files.createDirectories(Paths.get(simulationWorkingDir));

        // Create logs dir if not exists
        String logDirPath = simulationWorkingDir.concat(File.separator).concat(ToolFileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        String logFile = logDirPath.concat(File.separator).concat(ToolFileFormat.LOG_FILE_NAME);
        Path logFilePath = Paths.get(logFile);

        // Create outputDir
        String outputDir = simulationWorkingDir.concat(File.separator).concat(toolSimulationRef.getOutputData());
        Files.createDirectories(Paths.get(outputDir));

        // -- ATTEST/tools/TXY
        String toolWorkingDir = toolSimulationRef.getToolWorkingDir(toolDto);

        // Prepare JsonConfig
        LinkedHashMap<String, Object> paramsMap = new LinkedHashMap<String, Object>();

        // tool's parameters "nsc": 10
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                log.debug("Request Params: [{} : {}] ", parameterNames[i], parameterValues[i]);
                this.setParamFromRequest(paramsMap, parameterNames[i], parameterValues[i], ToolWp4Parameters.TSG_MAP_PARAM_TYPE);
            }
        }

        // prepare tool's input file name
        for (int i = 0; i < fileDesc.length; i++) {
            MultipartFile mpFile = files[i];
            String inputFile = simulationWorkingDir.concat(File.separator).concat(mpFile.getOriginalFilename());
            paramsMap.put("input_file", inputFile);
        }

        // prepare tool's output file
        String outputFilePath = outputDir.concat(File.separator).concat("scenario_gen.ods");
        paramsMap.put("output_file", outputFilePath);

        // set missing parameters with def values
        this.setDefaultValue(paramsMap, ToolWp4Parameters.TSG_MAP_PARAM_DEF_VALUE, ToolWp4Parameters.TSG_MAP_PARAM_TYPE);
        // tool's file json config parameters
        ToolConfigParameters toolConfigParameters = new ToolConfigParameters();
        toolConfigParameters.setParameters(paramsMap);
        String configJsonName = simulationWorkingDir.concat(File.separator).concat("launch.json");
        File fileConfigParametersJson = Paths.get(configJsonName).toFile();

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileConfigParametersJson, toolConfigParameters);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating launch.json file for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + e.getMessage());
        }

        // -- Save inputfile On DB and FS
        Set<String> expectedInputFiles = new HashSet<>(Arrays.asList("data_scenarios.ods"));
        List<InputFileDTO> inputFileSavedList = new ArrayList<InputFileDTO>();
        try {
            inputFileSavedList = saveInputFileOnDbAndFileSystem(networkDto, toolDto, files, expectedInputFiles, simulationWorkingDir);
        } catch (Exception e) {
            //toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            String errMsg = "Error saving inputFile for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + " " + e.getMessage());
        }

        int esito = runToolWrapper(toolWorkingDir, configJsonName, logFile);
        // check if exit value is 0 (OK) store output file on DB
        List<OutputFileDTO> outputFileSavedList = new ArrayList<OutputFileDTO>();
        if (esito == 0) {
            try {
                //saveResultsOnDB(outputFiles, networkDto, toolDto);
                outputFileSavedList =
                    toolExecutionServiceImpl.saveOutputFileOnDB(
                        new File(outputDir),
                        networkDto,
                        toolDto,
                        TSGFileOutputFormat.FILE_EXTENSION
                    );
                toolExecutionServiceImpl.uploadToolLogFileToDB(logFilePath, taskSaved, false);
            } catch (IOException ioex) {
                log.error("Error saving results for tool: {} ", toolDto.getName());
                toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
                throw ioex;
            }
            //toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.PASSED);
            log.info("Tool: {}, on Network: {}, running succesfully ", toolDto.getName(), networkDto.getName());
            try {
                SimulationDTO simulationDto = toolExecutionServiceImpl.createSimulation(
                    toolDto,
                    networkDto,
                    uuidGenerated,
                    fileConfigParametersJson,
                    taskSaved,
                    inputFileSavedList,
                    outputFileSavedList,
                    TaskStatus.Status.PASSED,
                    "" // description
                );
                log.info("simulationDto: {}, saved succesfully ", simulationDto.toString());
            } catch (Exception ex) {
                String errMsg = "Error saving simulation data for  tool: " + toolDto.getNum() + " uuid: " + uuid.toString();
                log.error(errMsg);
                //tool run succesfully so we set status PASSED but not refereces to simulation will be set
                toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.PASSED);
            }
            return uuid;
        } else {
            toolExecutionServiceImpl.uploadToolLogFileToDB(logFilePath, taskSaved, false);
            toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            String errMsg = "Error running tool: " + toolDto.getNum() + " uuid: " + uuid;
            log.error(errMsg);
            throw new RunningToolException(errMsg);
        }
    }

    @Override
    public String tractability(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        String[] fileDesc,
        MultipartFile[] files,
        String[] parameterNames,
        String[] parameterValues
    ) throws RunningToolException, Exception {
        // Create new Task
        TaskDTO taskSaved;
        try {
            taskSaved = toolExecutionServiceImpl.createTask(toolDto);
        } catch (Exception e) {
            String errMsg = "Error creating new task for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + " " + e.getMessage());
        }

        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();

        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);

        // Create simulationWorkingDir
        // -- ATS/WPX/TXY
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid.toString());
        Files.createDirectories(Paths.get(simulationWorkingDir));

        // Create logs dir if not exists
        String logDirPath = simulationWorkingDir.concat(File.separator).concat(ToolFileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        String logFile = logDirPath.concat(File.separator).concat(ToolFileFormat.LOG_FILE_NAME);
        Path logFilePath = Paths.get(logFile);

        // Create outputDir
        String outputDir = simulationWorkingDir.concat(File.separator).concat(toolSimulationRef.getOutputData());
        Files.createDirectories(Paths.get(outputDir));

        // -- ATTEST/tools/TXY
        String toolWorkingDir = toolSimulationRef.getToolWorkingDir(toolDto);

        // --- Prepare JsonConfig
        LinkedHashMap<String, Object> paramsMap = new LinkedHashMap<String, Object>();
        if (parameterNames != null) {
            // tool's parameters eg: "nsc": 10
            for (int i = 0; i < parameterNames.length; i++) {
                log.debug(" Request Params: [{} : {}] ", parameterNames[i], parameterValues[i]);
                this.setParamFromRequest(paramsMap, parameterNames[i], parameterValues[i], ToolWp4Parameters.T41_MAP_PARAM_TYPE);
            }
        }

        String outputFileName = "";
        int pos = 0;

        // --- Prepare parameters for input file used by the tool
        for (int i = 0; i < fileDesc.length; i++) {
            MultipartFile mpFile = files[i];
            String originalFileName = mpFile.getOriginalFilename();
            String inputFile = simulationWorkingDir.concat(File.separator).concat(originalFileName);
            switch (fileDesc[i]) {
                case "network":
                    paramsMap.put("network_file", inputFile);
                    pos = originalFileName.indexOf(".ods");
                    outputFileName = originalFileName.substring(0, pos) + "_output.xlsx";
                    break;
                case "flex":
                    paramsMap.put("auxiliary_file", inputFile);
                    if ("".equals(outputFileName)) {
                        pos = originalFileName.indexOf("flex.ods");
                        outputFileName = originalFileName.substring(0, pos) + "output.xlsx";
                    }
                    break;
                case "scenario":
                    paramsMap.put("scenario_file", inputFile);
                    break;
            }
        }

        // --- Prepare parameters for output file used by the tool:
        // ATSIM/WP4/T41/uuid/output_data/scenario_gen.ods"
        String outputFilePath = outputDir.concat(File.separator).concat(outputFileName);
        paramsMap.put("output_file", outputFilePath);

        // -- Save inputfile On DB and FS
        List<InputFileDTO> inputFileDtoSavedList = new ArrayList<InputFileDTO>();
        try {
            inputFileDtoSavedList = saveInputFileOnDbAndFileSystem(networkDto, toolDto, files, null, simulationWorkingDir);
        } catch (Exception e) {
            //toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            String errMsg = "Error saving inputFile for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + " " + e.getMessage());
        }

        // read network input file from DB and check if it is in ODS format
        InputFile testCasesNetwork = inputFileServiceImpl.findNetworkFileByNetworkId(networkDto.getId());

        Path pathNetworkDataFile = Paths.get(testCasesNetwork.getFileName());
        //String mimeType = Files.probeContentType(pathNetworkDataFile);
        String mimeType = FileUtils.probeContentType(pathNetworkDataFile);
        if (!T41FileInputFormat.ODS_CONTENT_MEDIA_TYPE.equals(mimeType)) {
            String errMsg =
                "Network input file " + testCasesNetwork.getFileName() + " for tool: " + toolDto.getName() + " must be in .ODS format!";
            log.error(errMsg);
            throw new Exception(errMsg);
        }

        // add network file into list of input file saved
        InputFileDTO inputFileTestCaseNetworkDTO = inputFileMapper.toDto(testCasesNetwork);
        inputFileDtoSavedList.add(inputFileTestCaseNetworkDTO);

        String inputNetworkFile = simulationWorkingDir.concat(File.separator).concat(testCasesNetwork.getFileName());
        try (FileOutputStream fos = new FileOutputStream(inputNetworkFile)) {
            fos.write(testCasesNetwork.getData());
            log.debug("Input File: {}, Saved on fileSystem!", inputNetworkFile);
        } catch (Exception e) {
            String errMsg = "Error saving file: " + inputNetworkFile;
            log.error(errMsg);
            throw new Exception(errMsg);
        }
        paramsMap.put("network_file", inputNetworkFile);

        // set missing parameters with def values
        setDefaultValue(paramsMap, ToolWp4Parameters.T41_MAP_PARAM_DEF_VALUE, ToolWp4Parameters.T41_MAP_PARAM_TYPE);

        // -- prepare tool's file json config parameters
        ToolConfigParameters toolConfigParameters = new ToolConfigParameters();
        toolConfigParameters.setParameters(paramsMap);
        String configJsonName = simulationWorkingDir.concat(File.separator).concat("launch.json");
        File fileConfigParametersJson = Paths.get(configJsonName).toFile();

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileConfigParametersJson, toolConfigParameters);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating launch.json file for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + " " + e.getMessage());
        }

        int esito = runToolWrapper(toolWorkingDir, configJsonName, logFile);
        // check if exit value is 0 (OK) store output file on DB
        List<OutputFileDTO> outputFileDtoSavedList = new ArrayList<OutputFileDTO>();
        if (esito == 0) {
            try {
                //File outputFileDir = new File(outputDir);
                //saveResultsOnDB(outputFileDir, networkDto, toolDto);
                outputFileDtoSavedList =
                    toolExecutionServiceImpl.saveOutputFileOnDB(
                        new File(outputDir),
                        networkDto,
                        toolDto,
                        T41FileOutputFormat.SUFFIX_OUTPUT_FILES_EXTENSION.concat(T41FileOutputFormat.FILE_EXTENSION)
                    );
                toolExecutionServiceImpl.uploadToolLogFileToDB(logFilePath, taskSaved, false);
            } catch (IOException ioex) {
                String errMsg = "Error saving results for tool: " + toolDto.getName();
                toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
                log.error(errMsg, ioex);
                throw new Exception(errMsg + " " + ioex.getMessage());
            }
            //toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.PASSED);
            log.info("Tool: {}, on Network: {}, running succesfully ", toolDto.getName(), networkDto.getName());
            try {
                SimulationDTO simulationDto = toolExecutionServiceImpl.createSimulation(
                    toolDto,
                    networkDto,
                    uuidGenerated,
                    fileConfigParametersJson,
                    taskSaved,
                    inputFileDtoSavedList,
                    outputFileDtoSavedList,
                    TaskStatus.Status.PASSED,
                    "" // description
                );
                log.info("Simulation {} saved succesfully ", simulationDto.toString());
            } catch (Exception ex) {
                String errMsg = "Error saving simulation data for  tool: " + toolDto.getNum() + " uuid: " + uuid.toString();
                log.error(errMsg);
                //tool run succesfully so we set status PASSED but not refereces to simulation will be set
                toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.PASSED);
            }
            return uuid;
        } else {
            toolExecutionServiceImpl.uploadToolLogFileToDB(logFilePath, taskSaved, false);
            toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            String errMsg = "Error running tool: " + toolDto.getNum() + " uuid: " + uuid;
            log.error(errMsg);
            throw new RunningToolException(errMsg);
        }
    }

    public String t44AsDayheadTx(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        String[] fileDesc,
        MultipartFile[] files,
        String[] parameterNames,
        String[] parameterValues
    ) throws RunningToolException, Exception {
        // Create new Task
        TaskDTO taskSaved;
        try {
            taskSaved = toolExecutionServiceImpl.createTask(toolDto);
        } catch (Exception e) {
            String errMsg = "Error creating new task for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + " " + e.getMessage());
        }

        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();

        String country = networkDto.getCountry();

        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);

        // Create simulationWorkingDir
        // -- ATS/WPX/TXY
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid.toString());
        Files.createDirectories(Paths.get(simulationWorkingDir));

        // Create logs dir if not exists
        String logDirPath = simulationWorkingDir.concat(File.separator).concat(ToolFileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        String logFile = logDirPath.concat(File.separator).concat(ToolFileFormat.LOG_FILE_NAME);
        Path logFilePath = Paths.get(logFile);

        // Create outputDir
        String outputDir = simulationWorkingDir.concat(File.separator).concat(toolSimulationRef.getOutputData());
        Files.createDirectories(Paths.get(outputDir));

        // -- ATTEST/tools/TXY
        String toolWorkingDir = toolSimulationRef.getToolWorkingDir(toolDto);

        // --- Prepare JsonConfig
        LinkedHashMap<String, Object> paramsMap = new LinkedHashMap<String, Object>();

        // tool's parameters
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                log.debug("Param: [{} : {}] ", parameterNames[i], parameterValues[i]);
                this.setParamFromRequest(paramsMap, parameterNames[i], parameterValues[i], ToolWp4Parameters.T44_MAP_PARAM_TYPE);
            }
        }

        String outputFilePrefix = "";
        int pos = 0;

        // --- Prepare parameters for input file used by the tool
        for (int i = 0; i < fileDesc.length; i++) {
            MultipartFile mpFile = files[i];
            String originalFileName = mpFile.getOriginalFilename();
            String inputFile = simulationWorkingDir.concat(File.separator).concat(originalFileName);

            log.debug("FileName: {} , FileDesc:{}, OutputFilePrefix: {} ", originalFileName, fileDesc[i], outputFilePrefix);

            switch (fileDesc[i]) {
                case "additionalNetwork":
                    paramsMap.put("network_file", inputFile);
                    pos = originalFileName.indexOf(".ods");
                    outputFilePrefix = originalFileName.substring(0, pos);
                    break;
                case "flex":
                    paramsMap.put("auxiliary_file", inputFile);
                    if ("".equals(outputFilePrefix)) {
                        pos = originalFileName.indexOf("_PROF.ods");
                        outputFilePrefix = originalFileName.substring(0, pos);
                    }
                    break;
                case "scenario":
                    paramsMap.put("scenario_file", inputFile);
                    break;
            }
        }

        log.debug("outputFilePrefix: {} ", outputFilePrefix);
        // --- Prepare parameters for output file used by the tool:
        // ATSIM/WP4/T44/uuid/output_data/"
        String outputFilePath = outputDir.concat(File.separator).concat(outputFilePrefix);
        paramsMap.put("output_files_prefix", outputFilePath);

        // -- Save inputfile On DB and FS
        List<InputFileDTO> inputFileDtoSavedList = new ArrayList<InputFileDTO>();
        try {
            inputFileDtoSavedList = saveInputFileOnDbAndFileSystem(networkDto, toolDto, files, null, simulationWorkingDir);
        } catch (Exception e) {
            //toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            String errMsg = "Error saving inputFile for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + " " + e.getMessage());
        }

        // if user doesn't select an additionalNetwork file (.ods ), take network file
        // loaded from testCases
        if (!Arrays.asList(fileDesc).contains("additionalNetwork")) {
            InputFile testCasesNetwork = inputFileServiceImpl.findNetworkFileByNetworkId(networkDto.getId());
            String inputNetworkFile = simulationWorkingDir.concat(File.separator).concat(testCasesNetwork.getFileName());
            try (FileOutputStream fos = new FileOutputStream(inputNetworkFile)) {
                fos.write(testCasesNetwork.getData());
                log.debug("Input File: {}, Saved on fileSystem!", inputNetworkFile);
            } catch (Exception e) {
                String errMsg = "Error saving file: " + inputNetworkFile;
                log.error(errMsg);
                throw new Exception(errMsg);
            }
            paramsMap.put("network_file", inputNetworkFile);
            // add network file into list of input file saved
            InputFileDTO inputFileTestCaseNetworkDTO = inputFileMapper.toDto(testCasesNetwork);
            inputFileDtoSavedList.add(inputFileTestCaseNetworkDTO);
        }

        // set missing parameters with def values
        setDefaultValue(paramsMap, ToolWp4Parameters.T44_MAP_PARAM_DEF_VALUE, ToolWp4Parameters.T44_MAP_PARAM_TYPE);
        // load_correction depends on country
        paramsMap.put("load_correction", this.getLoadCorrection(country));
        // -- prepare tool's file json config parameters
        ToolConfigParameters toolConfigParameters = new ToolConfigParameters();
        toolConfigParameters.setParameters(paramsMap);
        String configJsonName = simulationWorkingDir.concat(File.separator).concat("launch.json");
        File fileConfigParametersJson = Paths.get(configJsonName).toFile();

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileConfigParametersJson, toolConfigParameters);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating launch.json file for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + " " + e.getMessage());
        }

        int esito = runToolWrapper(toolWorkingDir, configJsonName, logFile);
        // check if exit value is 0 (OK) store output file on DB
        List<OutputFileDTO> outputFileDtoSavedList = new ArrayList<OutputFileDTO>();
        if (esito == 0) {
            try {
                outputFileDtoSavedList = toolExecutionServiceImpl.saveOutputFileOnDB(new File(outputDir), networkDto, toolDto, null);
                toolExecutionServiceImpl.uploadToolLogFileToDB(logFilePath, taskSaved, false);
            } catch (IOException ioex) {
                String errMsg = "Error saving results for tool: " + toolDto.getName();
                toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
                log.error(errMsg, ioex);
                throw new Exception(errMsg + " " + ioex.getMessage());
            }
            //toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.PASSED);
            log.info("Tool: {}, on Network: {}, running succesfully ", toolDto.getName(), networkDto.getName());
            try {
                SimulationDTO simulationDto = toolExecutionServiceImpl.createSimulation(
                    toolDto,
                    networkDto,
                    uuidGenerated,
                    fileConfigParametersJson,
                    taskSaved,
                    inputFileDtoSavedList,
                    outputFileDtoSavedList,
                    TaskStatus.Status.PASSED,
                    "" // description
                );
                log.info("Simulation {} saved succesfully ", simulationDto.toString());
            } catch (Exception ex) {
                String errMsg = "Error saving simulation data for  tool: " + toolDto.getNum() + " uuid: " + uuid.toString();
                log.error(errMsg);
                //tool run succesfully so we set status PASSED but not refereces to simulation will be set
                toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.PASSED);
            }

            return uuid;
        } else {
            toolExecutionServiceImpl.uploadToolLogFileToDB(logFilePath, taskSaved, false);
            toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            String errMsg = "Error running tool: " + toolDto.getNum() + " uuid: " + uuid;
            log.error(errMsg);
            throw new RunningToolException(errMsg);
        }
    }

    private List<InputFileDTO> saveInputFileOnDbAndFileSystem(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        MultipartFile[] files,
        Set<String> expectedInputFiles,
        String workingDir
    ) throws Exception {
        log.info("Store all Input files on DB and under tool's input_dir: {}", workingDir);
        List<InputFileDTO> inputFileSaved = new ArrayList<InputFileDTO>();
        // save input file in db and into the file system
        for (MultipartFile mpFile : files) {
            // check the content type
            if (FileUtils.checkContentType(mpFile)) {
                if (expectedInputFiles == null || expectedInputFiles.contains(mpFile.getOriginalFilename())) {
                    // -- save file ON DB input_file table
                    InputFileDTO fileSaved = inputFileServiceImpl.saveFileForNetwork(mpFile, networkDto, toolDto);
                    inputFileSaved.add(fileSaved);
                    String inputFileName = workingDir + File.separator + mpFile.getOriginalFilename();

                    try (FileOutputStream fos = new FileOutputStream(inputFileName)) {
                        fos.write(mpFile.getBytes());
                        log.debug("Input File: {}, Saved on fileSystem!", inputFileName);
                    } catch (Exception e) {
                        String errMsg = "Error saving file: " + inputFileName;
                        log.error(errMsg);
                        throw new Exception(errMsg);
                    }
                } else {
                    String msg = "Input fileName not valid ";
                    log.error(msg);
                    throw new Exception(msg);
                }
            } else {
                String msg = "InputFile content type not valid ";
                log.error(msg);
                throw new Exception(msg);
            }
        }
        return inputFileSaved;
    }

    private int runToolWrapper(String toolWorkingDir, String jsonConfig, String logFile) {
        Process process = JuliaScript.executeBatchFile(toolWorkingDir, jsonConfig);
        FileUtils.writeToolLogsToFile(new InputStreamReader(process.getInputStream()), logFile);
        int exitValue = process.exitValue();
        log.info("Tool:{} END with exit value: {} ", toolWorkingDir, exitValue);
        return exitValue;
    }

    private void setParamFromRequest(Map mapToSet, String paramName, String paramValue, Map<String, Object> paramsType) {
        Object type = paramsType.get(paramName);
        if (type.equals(String.class)) {
            mapToSet.put(paramName, paramValue);
        } else if (!"".equals(paramValue) && type.equals(Integer.class)) {
            mapToSet.put(paramName, Integer.parseInt(paramValue));
        }
        if (!"".equals(paramValue) && type.equals(Double.class)) {
            mapToSet.put(paramName, Double.parseDouble(paramValue));
        }
    }

    private Number getLoadCorrection(String country) {
        return country.equals("PT") ? 0.01 : 1.0;
    }

    private void setDefaultValue(Map mapToSet, Map<String, Object> paramsDefValue, Map<String, Object> paramsType) {
        for (String paramName : paramsDefValue.keySet()) {
            if (!mapToSet.containsKey(paramName)) {
                Object type = paramsType.get(paramName);
                if (type.equals(Integer.class)) {
                    Integer value = (Integer) paramsDefValue.get(paramName);
                    mapToSet.put(paramName, value);
                }
                if (type.equals(Double.class)) {
                    Double value = (Double) (paramsDefValue.get(paramName));
                    mapToSet.put(paramName, value);
                }
            }
        }
    }
}
