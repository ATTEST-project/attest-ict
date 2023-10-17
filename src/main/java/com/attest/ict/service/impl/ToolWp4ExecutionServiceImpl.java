package com.attest.ict.service.impl;

import static com.attest.ict.tools.constants.ToolsConstants.*;

import com.attest.ict.config.ToolsConfiguration;
import com.attest.ict.custom.tools.utils.ToolSimulationReferencies;
import com.attest.ict.custom.tools.utils.ToolVarName;
import com.attest.ict.custom.utils.ConverterUtils;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.domain.InputFile;
import com.attest.ict.helper.ods.exception.OdsWriterFileException;
import com.attest.ict.service.*;
import com.attest.ict.service.dto.*;
import com.attest.ict.service.dto.custom.ToolConfigParameters;
import com.attest.ict.service.mapper.InputFileMapper;
import com.attest.ict.tools.constants.*;
import com.attest.ict.tools.parameter.constants.ToolWp4Parameters;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
//@Transactional
public class ToolWp4ExecutionServiceImpl implements ToolWp4ExecutionService {

    private final Logger log = LoggerFactory.getLogger(ToolWp4ExecutionServiceImpl.class);
    private String attestToolsDir;

    private String toolsPathSimulation;

    private String t44WithConverter;

    @Autowired
    private InputFileService inputFileServiceImpl;

    @Autowired
    ToolExecutionServiceImpl toolExecutionServiceImpl;

    @Autowired
    private InputFileMapper inputFileMapper;

    @Autowired
    CommandExecutorService commandExecutorServiceImpl;

    @Autowired
    private TaskService taskServiceImpl;

    @Autowired
    private SimulationServiceImpl simulationServiceImpl;

    @Autowired
    private OdsNetworkService odsNetworkService;

    @Autowired
    private OutputFileService outputFileServiceImpl;

    public ToolWp4ExecutionServiceImpl(ToolsConfiguration toolsConfig) {
        // ATTEST/tools
        this.attestToolsDir = toolsConfig.getPath();
        log.debug("attestToolsDir {}", attestToolsDir);

        // ATSIM
        this.toolsPathSimulation = toolsConfig.getPathSimulation();
        log.debug("toolsPathSimulation {}", toolsPathSimulation);

        //T44 With Converter
        this.t44WithConverter = toolsConfig.getT44WithConv();
        log.debug("t44WithConverter {}", t44WithConverter);
    }

    @Override
    @Transactional
    public Map<String, String> prepareTSGWorkingDir(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        String[] fileDesc,
        MultipartFile[] files,
        String[] parameterNames,
        String[] parameterValues
    ) throws Exception {
        Map<String, String> configMap = new HashMap<String, String>();
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();
        log.info("prepareTSGWorkingDir() - uuid: {} ", uuid);
        configMap.put(UUID_KEY, uuid);

        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        // Tool's installation path
        String toolInstallationDir = toolSimulationRef.getToolWorkingDir(toolDto);
        configMap.put(TOOL_INSTALL_DIR, toolInstallationDir);
        configMap.put(LAUNCH_TOOL_FILE_KEY, ToolFileFormat.LAUNCH_FILE);

        // Create simulationWorkingDir
        // -- ATSIM/WP4/<ToolNum>/<UUID>/
        String simulationWorkingPath = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);
        Files.createDirectories(Paths.get(simulationWorkingPath));
        log.info("prepareTSGWorkingDir() - DIR: {}, created ", simulationWorkingPath);
        configMap.put(WORKING_DIR_KEY, simulationWorkingPath);

        // Create logs dir if not exists
        // -- ATSIM/WP4/<ToolNum>/<UUID>/logs
        // Create logs dir if not exists
        String logDirPath = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        String logFile = logDirPath.concat(File.separator).concat(ToolFileFormat.LOG_FILE_NAME);
        log.info("prepareTSGWorkingDir() - DIR:  {}, created ", logDirPath);
        configMap.put(LOG_FILENAME_KEY, logFile);

        // Create outputs dir if not exists
        // -- ATSIM/WP4/<ToolNum>/<UUID>/output_data
        String outputDirPath = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.OUTPUT_DIR);
        Files.createDirectories(Paths.get(outputDirPath));
        log.info("prepareTSGWorkingDir() - DIR:  {}, created ", outputDirPath);
        configMap.put(OUTPUT_DIR_KEY, outputDirPath);

        //-- Prepare json configuration file used by the tool
        LinkedHashMap<String, Object> paramsMap = new LinkedHashMap<String, Object>();

        // tool's parameters "nsc": 10
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                log.debug("prepareTSGWorkingDir() - Request Params: [{} : {}] ", parameterNames[i], parameterValues[i]);
                this.setParamFromRequest(paramsMap, parameterNames[i], parameterValues[i], ToolWp4Parameters.TSG_MAP_PARAM_TYPE, toolDto);
            }
        }

        // prepare tool's input file name
        for (int i = 0; i < fileDesc.length; i++) {
            MultipartFile mpFile = files[i];
            String inputFile = simulationWorkingPath.concat(File.separator).concat(mpFile.getOriginalFilename());
            paramsMap.put("input_file", inputFile);
        }

        // prepare tool's output file
        String outputFilePath = outputDirPath.concat(File.separator).concat(TSGFileFormat.TSG_OUTPUT_FILE);
        paramsMap.put("output_file", outputFilePath);

        // set missing parameters with def values
        this.setDefaultValue(paramsMap, ToolWp4Parameters.TSG_MAP_PARAM_DEF_VALUE, ToolWp4Parameters.TSG_MAP_PARAM_TYPE);
        // tool's file json config parameters
        ToolConfigParameters toolConfigParameters = new ToolConfigParameters();
        toolConfigParameters.setParameters(paramsMap);

        String configJsonName = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.CONFIG_FILE);
        File fileConfigParametersJson = Paths.get(configJsonName).toFile();

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileConfigParametersJson, toolConfigParameters);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating " + ToolFileFormat.CONFIG_FILE + " file for tool: " + toolDto.getName();
            log.error("prepareTSGWorkingDir() - " + errMsg, e);
            throw new Exception(errMsg + e.getMessage());
        }
        configMap.put(LAUNCH_JSON_FILE_KEY, configJsonName);

        // -- Save inputfile On DB and FS
        Set<String> expectedInputFiles = new HashSet<>(Arrays.asList(TSGFileFormat.TSG_INPUT_FILE));
        List<InputFileDTO> inputFileSavedList = new ArrayList<InputFileDTO>();
        try {
            inputFileSavedList = saveInputFileOnDbAndFileSystem(networkDto, toolDto, files, expectedInputFiles, simulationWorkingPath);
        } catch (Exception e) {
            String errMsg = "Error saving inputFile for tool: " + toolDto.getName();
            log.error("prepareTSGWorkingDir() - " + errMsg, e);
            throw new Exception(errMsg + " " + e.getMessage());
        }

        // Init new Task
        TaskDTO taskDtoSaved;
        try {
            taskDtoSaved = toolExecutionServiceImpl.createTask(toolDto);
        } catch (Exception e) {
            String errMsg = "Error creating new task for tool: " + toolDto.getName();
            log.error("prepareTSGWorkingDir() - " + errMsg, e);
            throw new Exception(errMsg);
        }

        // Init Simulation
        try {
            SimulationDTO simulationDto = toolExecutionServiceImpl.initSimulation(
                toolDto,
                networkDto,
                uuidGenerated,
                fileConfigParametersJson,
                taskDtoSaved,
                toolExecutionServiceImpl.getInputFilesDtoSetFromList(inputFileSavedList),
                null
            );
            taskDtoSaved.setSimulation(simulationDto);
            taskServiceImpl.partialUpdate(taskDtoSaved);
        } catch (Exception ex) {
            String errMsg = "Error saving simulation data for  tool: " + toolDto.getNum() + " uuid: " + uuid.toString();
            log.error("prepareTSGWorkingDir() - " + errMsg, ex);
            throw new Exception(errMsg, ex);
        }

        log.info("prepareTSGWorkingDir() - return: {}", configMap.toString());
        return configMap;
    }

    @Override
    @Transactional
    public Map<String, String> prepareT41WorkingDir(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        String[] fileDesc,
        MultipartFile[] files,
        String[] parameterNames,
        String[] parameterValues
    ) throws Exception {
        Map<String, String> configMap = new HashMap<String, String>();
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();
        log.info("prepareT41WorkingDir() - uuid: {} ", uuid);
        configMap.put(UUID_KEY, uuid);

        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        // Tool's installation path
        String toolInstallationDir = toolSimulationRef.getToolWorkingDir(toolDto);
        configMap.put(TOOL_INSTALL_DIR, toolInstallationDir);
        configMap.put(LAUNCH_TOOL_FILE_KEY, ToolFileFormat.LAUNCH_FILE);

        // Create simulationWorkingDir
        // -- ATSIM/WP4/<ToolNum>/<UUID>/
        String simulationWorkingPath = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);
        Files.createDirectories(Paths.get(simulationWorkingPath));
        log.info("prepareT41WorkingDir() - DIR: {}, created ", simulationWorkingPath);
        configMap.put(WORKING_DIR_KEY, simulationWorkingPath);

        // Create logs dir if not exists
        // -- ATSIM/WP4/<ToolNum>/<UUID>/logs
        // Create logs dir if not exists
        String logDirPath = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        String logFile = logDirPath.concat(File.separator).concat(ToolFileFormat.LOG_FILE_NAME);
        log.info("prepareT41WorkingDir() - DIR:  {}, created ", logDirPath);
        configMap.put(LOG_FILENAME_KEY, logFile);

        // Create outputs dir if not exists
        // -- ATSIM/WP4/<ToolNum>/<UUID>/output_data
        String outputDirPath = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.OUTPUT_DIR);
        Files.createDirectories(Paths.get(outputDirPath));
        log.info("prepareT41WorkingDir() - DIR:  {}, created ", outputDirPath);
        configMap.put(OUTPUT_DIR_KEY, outputDirPath);

        // --- Prepare JsonConfig
        LinkedHashMap<String, Object> paramsMap = new LinkedHashMap<String, Object>();
        if (parameterNames != null) {
            // tool's parameters eg: "nsc": 10
            for (int i = 0; i < parameterNames.length; i++) {
                log.debug("Request Params: [{} : {}] ", parameterNames[i], parameterValues[i]);
                this.setParamFromRequest(paramsMap, parameterNames[i], parameterValues[i], ToolWp4Parameters.T41_MAP_PARAM_TYPE, toolDto);
            }
        }

        String outputFileName = "";
        int pos = 0;
        String caseName = "";
        //--- Prepare parameters for the additional input files used by the tool. (NB: doesn't contain network file)
        for (int i = 0; i < fileDesc.length; i++) {
            MultipartFile mpFile = files[i];
            String originalFileName = mpFile.getOriginalFilename();
            String inputFile = simulationWorkingPath.concat(File.separator).concat(originalFileName);
            switch (fileDesc[i]) {
                case "flex":
                    paramsMap.put("auxiliary_file", inputFile);
                    if ("".equals(outputFileName)) {
                        pos = originalFileName.lastIndexOf("_flex.ods");
                        if (pos > 0) {
                            outputFileName = originalFileName.substring(0, pos) + "_output.xlsx";
                            caseName = ("".equals(caseName)) ? originalFileName.substring(0, pos) : caseName;
                        } else {
                            String errMsg = "The Auxiliary file name is incorrect, it must end with '_flex.ods'.";
                            log.error("prepareT41WorkingDir() - " + errMsg);
                            throw new Exception(errMsg);
                        }
                    }
                    break;
                case "scenario":
                    paramsMap.put("scenario_file", inputFile);
                    break;
            }
        }

        //--- Since T41V2 version new parameter 'case_name' has been included, if not specified by the user, set it equals to networkFile or additional(_flex) uploaded files
        if (StringUtils.isBlank((String) paramsMap.get(ToolWp4Parameters.PARAM_CASE_NAME))) {
            paramsMap.put(ToolWp4Parameters.PARAM_CASE_NAME, caseName);
        }

        // --- Prepare parameters for output file used by the tool
        // ATSIM/WP4/T41/uuid/output_data/scenario_gen.ods
        String outputFilePath = outputDirPath.concat(File.separator).concat(outputFileName);
        paramsMap.put("output_file", outputFilePath);

        //Since T41V2 version new parameter 'outlog_file' has been included
        String paramOutlogFile = "outlog_file";
        paramsMap.put(
            paramOutlogFile,
            outputDirPath.concat(File.separator).concat((String) ToolWp4Parameters.T41_MAP_PARAM_DEF_VALUE.get(paramOutlogFile))
        );

        // -- Save inputfile On DB and FS
        List<InputFileDTO> inputFileDtoSavedList = new ArrayList<InputFileDTO>();
        try {
            inputFileDtoSavedList = saveInputFileOnDbAndFileSystem(networkDto, toolDto, files, null, simulationWorkingPath);
        } catch (Exception e) {
            //toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            String errMsg = "Error saving inputFile for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg, e);
        }

        //--- Read network input file from DB and check if it is in ODS format
        InputFile testCasesNetwork = inputFileServiceImpl.findNetworkFileByNetworkId(networkDto.getId());

        Path pathNetworkDataFile = Paths.get(testCasesNetwork.getFileName());
        String mimeType = FileUtils.probeContentType(pathNetworkDataFile);
        if (
            !FileUtils.CONTENT_TYPE.get("ods").equals(mimeType) && !FileUtils.getFileExtension(testCasesNetwork.getFileName()).equals("m")
        ) {
            String errMsg =
                "Network input file " +
                testCasesNetwork.getFileName() +
                " for tool: " +
                toolDto.getName() +
                " must be in .ODS or MATPOWER case file format!";
            log.error("prepareT41WorkingDir() - " + errMsg);
            throw new Exception(errMsg);
        }

        String inputNetworkFile = simulationWorkingPath.concat(File.separator).concat(testCasesNetwork.getFileName());
        //-- Save network's file to the fileSystem
        try (FileOutputStream fos = new FileOutputStream(inputNetworkFile)) {
            fos.write(testCasesNetwork.getData());
            log.debug("prepareT41WorkingDir() - Input File: {}, Saved to the fileSystem!", inputNetworkFile);
        } catch (Exception e) {
            String errMsg = "Error saving file: " + inputNetworkFile;
            log.error("prepareT41WorkingDir() - " + errMsg);
            throw new Exception(errMsg, e);
        }

        // add network file into list of input file saved
        InputFileDTO inputFileTestCaseNetworkDTO = inputFileMapper.toDto(testCasesNetwork);
        inputFileDtoSavedList.add(inputFileTestCaseNetworkDTO);

        if (!FileUtils.CONTENT_TYPE.get("ods").equals(mimeType)) {
            //-- Convert '.m' file in '.ods'
            String odsFileName = replaceFileNameExtension(testCasesNetwork.getFileName(), ".ods");
            inputNetworkFile = simulationWorkingPath.concat(File.separator).concat(odsFileName);
            try {
                log.debug("prepareT41WorkingDir() - Convert '.m' file in '.ods' ...");
                ByteArrayOutputStream byteArrayOutputStream = odsNetworkService.exportNetworkToOdsFile(networkDto.getId());
                try (FileOutputStream fileOutputStream = new FileOutputStream(inputNetworkFile)) {
                    byteArrayOutputStream.writeTo(fileOutputStream);
                    log.info("prepareT41WorkingDir() - Network data: {}, Saved to .ods file successfully.", inputNetworkFile);
                } catch (IOException e) {
                    String errMsg =
                        "Error saving: " + testCasesNetwork.getFileName() + " to the file system for tool: " + toolDto.getName();
                    log.error("prepareT41WorkingDir() - " + errMsg);
                    throw new Exception(errMsg, e);
                }

                //-- save .ods file in input_file db table
                try {
                    InputFileDTO inputFileNetOdsDto = inputFileServiceImpl.saveFileForNetworkAndTool(
                        byteArrayOutputStream.toByteArray(),
                        odsFileName,
                        FileUtils.CONTENT_TYPE.get("ods"),
                        networkDto,
                        toolDto
                    );
                    inputFileDtoSavedList.add(inputFileNetOdsDto);
                } catch (Exception ex) {
                    String errMsg =
                        "Error saving: " + testCasesNetwork.getFileName() + " in InputFile DB's table for tool: " + toolDto.getName();
                    log.error("prepareT41WorkingDir() - " + errMsg);
                    throw new Exception(errMsg, ex);
                }
            } catch (OdsWriterFileException owfe) {
                String errMsg = "Exception running tool: " + toolDto.getName();
                log.error("prepareT41WorkingDir() - " + errMsg);
                throw new Exception(errMsg, owfe);
            }
        }

        paramsMap.put("network_file", inputNetworkFile);

        // set missing parameters with def values
        setDefaultValue(paramsMap, ToolWp4Parameters.T41_MAP_PARAM_DEF_VALUE, ToolWp4Parameters.T41_MAP_PARAM_TYPE);

        // -- prepare tool's file json config parameters
        ToolConfigParameters toolConfigParameters = new ToolConfigParameters();
        toolConfigParameters.setParameters(paramsMap);
        String configJsonName = simulationWorkingPath.concat(File.separator).concat("launch.json");
        File fileConfigParametersJson = Paths.get(configJsonName).toFile();

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileConfigParametersJson, toolConfigParameters);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating launch.json file for tool: " + toolDto.getName();
            log.error("prepareT41WorkingDir() - " + errMsg, e);
            throw new Exception(errMsg, e);
        }
        configMap.put(LAUNCH_JSON_FILE_KEY, configJsonName);
        // If T41 raise the exception: ERROR: LoadError: No violations. This case is a waste of time,  this mustn't be considered a real ERROR. In fact the tool
        // ended successfully and produce also the output file with the set point obtained from the first part of the tool. This value are written in a output file
        // add this key to the list of the errors to skip
        configMap.put(ERROR_TO_SKIP, "No violations. This case is a waste of time");

        // Init new Task
        TaskDTO taskDtoSaved;
        try {
            taskDtoSaved = toolExecutionServiceImpl.createTask(toolDto);
        } catch (Exception e) {
            String errMsg = "Error creating new task for tool: " + toolDto.getName();
            log.error("prepareT41WorkingDir() - " + errMsg, e);
            throw new Exception(errMsg, e);
        }

        String simulationDescr = t41GetSimulationDescr(paramsMap);
        log.debug("prepareT41WorkingDir() - SimulationDescr: {} ", simulationDescr);
        // Init Simulation
        try {
            SimulationDTO simulationDto = toolExecutionServiceImpl.initSimulation(
                toolDto,
                networkDto,
                uuidGenerated,
                fileConfigParametersJson,
                taskDtoSaved,
                toolExecutionServiceImpl.getInputFilesDtoSetFromList(inputFileDtoSavedList),
                simulationDescr
            );
            taskDtoSaved.setSimulation(simulationDto);
            taskServiceImpl.partialUpdate(taskDtoSaved);
        } catch (Exception ex) {
            String errMsg = "Error saving simulation data for  tool: " + toolDto.getNum() + " uuid: " + uuid.toString();
            log.error("prepareT41WorkingDir() - " + errMsg, ex);
            throw new Exception(errMsg, ex);
        }

        log.info("prepareT41WorkingDir() - Return: {}", configMap.toString());
        return configMap;
    }

    private String replaceFileNameExtension(String originalFileName, String newExtension) {
        int pos = 0;
        pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(0, pos).concat(newExtension);
    }

    @Override
    @Transactional
    public Map<String, String> prepareT44V3WorkingDir(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        String[] fileDesc,
        MultipartFile[] files,
        String[] parameterNames,
        String[] parameterValues
    ) throws Exception {
        Arrays.stream(fileDesc).forEach(f -> log.debug("FileDesc: {}", f));

        Map<String, String> configMap = new HashMap<String, String>();
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();
        log.info("prepareT44V3WorkingDir() - uuid: {} ", uuid);
        configMap.put(UUID_KEY, uuid);

        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        configMap.put(LAUNCH_TOOL_FILE_KEY, ToolFileFormat.LAUNCH_FILE);

        // Create simulationWorkingDir
        // -- ATSIM/WP4/<ToolNum>/<UUID>/
        String simulationWorkingPath = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);
        Files.createDirectories(Paths.get(simulationWorkingPath));
        log.info("prepareT44V3WorkingDir() - DIR: {}, created ", simulationWorkingPath);
        configMap.put(WORKING_DIR_KEY, simulationWorkingPath);

        // Create logs dir if not exists
        // -- ATSIM/WP4/<ToolNum>/<UUID>/logs
        // Create logs dir if not exists
        String logDirPath = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        String logFile = logDirPath.concat(File.separator).concat(ToolFileFormat.LOG_FILE_NAME);
        log.info("prepareT44V3WorkingDir() - DIR:  {}, created ", logDirPath);
        configMap.put(LOG_FILENAME_KEY, logFile);

        // Create outputs dir if not exists
        // -- ATSIM/WP4/<ToolNum>/<UUID>/output_data
        String outputDirPath = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.OUTPUT_DIR);
        Files.createDirectories(Paths.get(outputDirPath));
        log.info("prepareT44V3WorkingDir() - DIR:  {}, created ", outputDirPath);
        configMap.put(OUTPUT_DIR_KEY, outputDirPath);

        // --- Prepare JsonConfig
        LinkedHashMap<String, Object> paramsMap = new LinkedHashMap<String, Object>();

        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                log.debug("prepareT44V3WorkingDir() - Param: [{} : {}] ", parameterNames[i], parameterValues[i]);
                this.setParamFromRequest(paramsMap, parameterNames[i], parameterValues[i], ToolWp4Parameters.T44_MAP_PARAM_TYPE, toolDto);
            }
        }

        String filePrefix = "";
        int pos = 0;

        // --- Prepare parameters for input file used by the tool
        for (int i = 0; i < fileDesc.length; i++) {
            MultipartFile mpFile = files[i];
            String originalFileName = mpFile.getOriginalFilename();
            String inputFile = simulationWorkingPath.concat(File.separator).concat(originalFileName);
            log.debug(
                "prepareT44V3WorkingDir() - FileName: {} , FileDesc:{}, OutputDir: {} ",
                originalFileName,
                fileDesc[i],
                outputDirPath
            );

            switch (fileDesc[i]) {
                case "additionalNetwork":
                    paramsMap.put("network_file", inputFile);
                    pos = originalFileName.indexOf(".ods");
                    if (pos == -1) {
                        String errMsg = "The additionalNetwork file format is incorrect, it must be in '.ODS' ";
                        log.error(errMsg);
                        throw new Exception(errMsg);
                    }
                    filePrefix = originalFileName.substring(0, pos);
                    break;
                case "flex":
                    paramsMap.put("auxiliary_file", inputFile);
                    String suffix = "_PROF.ods";
                    pos = originalFileName.indexOf(suffix);
                    if (pos == -1) {
                        String errMsg = "The Auxiliary file name is incorrect, it must end with: " + suffix;
                        log.error(errMsg);
                        throw new Exception(errMsg);
                    }
                    if ("".equals(filePrefix)) {
                        filePrefix = originalFileName.substring(0, pos);
                    }
                    break;
                case "scenario":
                    paramsMap.put("scenario_file", inputFile);
                    break;
            }
        }

        if (StringUtils.isBlank((String) paramsMap.get(ToolWp4Parameters.PARAM_CASE_NAME))) {
            paramsMap.put(ToolWp4Parameters.PARAM_CASE_NAME, filePrefix);
        }

        // --- Prepare parameters for output_dir used by the tool:
        // ATSIM/WP4/T44/uuid/output_data/"
        paramsMap.put("output_dir", outputDirPath);

        // -- Save inputfile On DB and FS
        List<InputFileDTO> inputFileDtoSavedList = new ArrayList<InputFileDTO>();
        try {
            inputFileDtoSavedList = saveInputFileOnDbAndFileSystem(networkDto, toolDto, files, null, simulationWorkingPath);
        } catch (Exception e) {
            //toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            String errMsg = "Error saving inputFile for tool: " + toolDto.getName();
            log.error("prepareT44V3WorkingDir() - " + errMsg, e);
            throw new Exception(errMsg, e);
        }

        // Tool's installation path
        String toolInstallationDir = toolSimulationRef.getToolWorkingDir(toolDto);

        // if user doesn't select an additionalNetwork file (.ods ), take network file
        // loaded from testCases
        if (!Arrays.asList(fileDesc).contains("additionalNetwork")) {
            log.debug("prepareT44V3WorkingDir() - Load network matpower file from DB");
            InputFile testCasesNetwork = inputFileServiceImpl.findNetworkFileByNetworkId(networkDto.getId());
            String inputNetworkFile = simulationWorkingPath.concat(File.separator).concat(testCasesNetwork.getFileName());
            try (FileOutputStream fos = new FileOutputStream(inputNetworkFile)) {
                fos.write(testCasesNetwork.getData());
                log.debug("prepareT44V3WorkingDir() - Input File: {}, Saved on fileSystem!", inputNetworkFile);
            } catch (Exception e) {
                String errMsg = "Error saving file: " + inputNetworkFile;
                log.error(errMsg);
                throw new Exception(errMsg, e);
            }
            paramsMap.put("network_file", inputNetworkFile);
            // add network file into list of input file saved
            InputFileDTO inputFileTestCaseNetworkDTO = inputFileMapper.toDto(testCasesNetwork);
            inputFileDtoSavedList.add(inputFileTestCaseNetworkDTO);
            //Cal tool with  converter included
            String ext = FileUtils.getFileExtension(testCasesNetwork.getFileName());
            if (ext.equals("m")) {
                toolInstallationDir = toolSimulationRef.getToolWorkingDir(toolDto, this.t44WithConverter);
            }
        }

        configMap.put(TOOL_INSTALL_DIR, toolInstallationDir);
        log.debug("prepareT44V3WorkingDir() - tool installation dir: {} ", toolInstallationDir);
        // set missing parameters with def values
        setDefaultValue(paramsMap, ToolWp4Parameters.T44_MAP_PARAM_DEF_VALUE, ToolWp4Parameters.T44_MAP_PARAM_TYPE);

        // -- prepare tool's file json config parameters
        ToolConfigParameters toolConfigParameters = new ToolConfigParameters();
        toolConfigParameters.setParameters(paramsMap);
        String configJsonName = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.CONFIG_FILE);
        File fileConfigParametersJson = Paths.get(configJsonName).toFile();

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileConfigParametersJson, toolConfigParameters);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating " + ToolFileFormat.CONFIG_FILE + " file for tool: " + toolDto.getName();
            log.error("prepareT44V3WorkingDir() - " + errMsg, e);
            throw new Exception(errMsg, e);
        }
        configMap.put(LAUNCH_JSON_FILE_KEY, configJsonName);

        // Init new Task
        TaskDTO taskDtoSaved;
        try {
            taskDtoSaved = toolExecutionServiceImpl.createTask(toolDto);
        } catch (Exception e) {
            String errMsg = "Error creating new task for tool: " + toolDto.getName();
            log.error("prepareT44V3WorkingDir() - " + errMsg, e);
            throw new Exception(errMsg, e);
        }

        // Init Simulation
        try {
            SimulationDTO simulationDto = toolExecutionServiceImpl.initSimulation(
                toolDto,
                networkDto,
                uuidGenerated,
                fileConfigParametersJson,
                taskDtoSaved,
                toolExecutionServiceImpl.getInputFilesDtoSetFromList(inputFileDtoSavedList),
                this.t44V3GetSimulationDescr(paramsMap)
            );
            taskDtoSaved.setSimulation(simulationDto);
            taskServiceImpl.partialUpdate(taskDtoSaved);
        } catch (Exception ex) {
            String errMsg = "Error saving simulation data for  tool: " + toolDto.getNum() + " uuid: " + uuid.toString();
            log.error("prepareT44V3WorkingDir() - " + errMsg, ex);
            throw new Exception(errMsg, ex);
        }

        log.info("prepareT44WorkingDir() - Return: {}", configMap.toString());
        return configMap;
    }

    @Override
    @Transactional
    public Map<String, String> prepareT42WorkingDir(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        String[] fileDesc,
        MultipartFile[] files,
        String[] parameterNames,
        String[] parameterValues,
        Long[] otherToolOutputFileIds
    ) throws Exception {
        Map<String, String> configMap = new HashMap<String, String>();
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();
        log.info("prepareT42WorkingDir() - uuid: {} ", uuid);
        configMap.put(UUID_KEY, uuid);

        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        // Tool's installation path
        String toolInstallationDir = toolSimulationRef.getToolWorkingDir(toolDto);
        configMap.put(TOOL_INSTALL_DIR, toolInstallationDir);
        configMap.put(LAUNCH_TOOL_FILE_KEY, ToolFileFormat.LAUNCH_FILE);

        // Create simulationWorkingDir
        // -- ATSIM/WP4/<ToolNum>/<UUID>/
        String simulationWorkingPath = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);
        Files.createDirectories(Paths.get(simulationWorkingPath));
        log.info("prepareT42WorkingDir() - DIR: {}, created ", simulationWorkingPath);
        configMap.put(WORKING_DIR_KEY, simulationWorkingPath);

        // Create logs dir if not exists
        // -- ATSIM/WP4/<ToolNum>/<UUID>/logs
        // Create logs dir if not exists
        String logDirPath = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        String logFile = logDirPath.concat(File.separator).concat(ToolFileFormat.LOG_FILE_NAME);
        log.info("prepareT42WorkingDir() - DIR:  {}, created ", logDirPath);
        configMap.put(LOG_FILENAME_KEY, logFile);

        // Create outputs dir if not exists
        // -- ATSIM/WP4/<ToolNum>/<UUID>/output_data
        String outputDirPath = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.OUTPUT_DIR);
        Files.createDirectories(Paths.get(outputDirPath));
        log.info("prepareT42WorkingDir() - DIR:  {}, created ", outputDirPath);
        configMap.put(OUTPUT_DIR_KEY, outputDirPath);

        // --- Prepare JsonConfig
        LinkedHashMap<String, Object> paramsMap = new LinkedHashMap<String, Object>();
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                log.info("prepareT42WorkingDir() - Params: [{} : {}] ", parameterNames[i], parameterValues[i]);
                this.setParamFromRequest(paramsMap, parameterNames[i], parameterValues[i], ToolWp4Parameters.T42_MAP_PARAM_TYPE, toolDto);
            }
        }

        // -- ATSIM/WP4/<ToolNum>/<UUID>/
        //name of the file used by the tool for saving the converted network file  from .m format into  .xlsx
        String networkFile = simulationWorkingPath.concat(File.separator).concat(T42FileFormat.T42_NETWORK_FILE_XLSX);
        paramsMap.put("network_file", networkFile);
        log.info("prepareT42WorkingDir() - param: network_file: {}, ", networkFile);

        String filePrefix = "";
        int pos = 0;

        // --- Prepare parameters for input file used by the tool
        for (int i = 0; i < fileDesc.length; i++) {
            MultipartFile mpFile = files[i];
            String originalFileName = mpFile.getOriginalFilename();
            String inputFile = simulationWorkingPath.concat(File.separator).concat(originalFileName);
            log.info("prepareT42WorkingDir() - FileName: {} , FileDesc:{}", originalFileName, fileDesc[i]);

            switch (fileDesc[i]) {
                case "PV_production_profile_file":
                    // PV_production_diagram.xlsx from simulation folder on cloud server
                    paramsMap.put("PV_production_profile_file", inputFile);
                    break;
                // From T41 results
                case "flexibity_devices_states_file":
                    // from T41 procured_flexibility_<case_name><yyyy>_<season>_<wf>.xlsx
                    paramsMap.put("flexibity_devices_states_file", inputFile);
                    break;
                case "state_estimation_csv_file":
                    paramsMap.put("state_estimation_csv_file", inputFile);
                    break;
                case "flex_devices_tech_char_file":
                    // from xlsx simulation xlsx with load profile estabilisched for PV, EV  eg: <Country>_tx_<yyyy>.xlsx
                    paramsMap.put("flex_devices_tech_char_file", inputFile);
                    break;
                case "trans_activation_file":
                    // output result from T45 eg.: Location1_output.xlsx
                    paramsMap.put("trans_activation_file", inputFile);
                    break;
            }
        }

        List<InputFileDTO> inputFileDtoSavedList = new ArrayList<InputFileDTO>();

        // read network input file from DB using selected network id as key
        InputFile testCasesNetwork = inputFileServiceImpl.findNetworkFileByNetworkId(networkDto.getId());
        Path pathNetworkDataFile = Paths.get(testCasesNetwork.getFileName());
        // add network file into list of input file saved
        InputFileDTO inputFileTestCaseNetworkDTO = inputFileMapper.toDto(testCasesNetwork);
        inputFileDtoSavedList.add(inputFileTestCaseNetworkDTO);

        String inputNetworkFile = simulationWorkingPath.concat(File.separator).concat(testCasesNetwork.getFileName());
        try (FileOutputStream fos = new FileOutputStream(inputNetworkFile)) {
            fos.write(testCasesNetwork.getData());
            log.info("prepareT42WorkingDir() - Matpower network File: {}, Saved on fileSystem!", inputNetworkFile);
        } catch (Exception e) {
            String errMsg = "Error saving file: " + inputNetworkFile;
            log.error("prepareT42WorkingDir() - " + errMsg);
            throw new Exception(errMsg, e);
        }
        paramsMap.put("matpower_network_file", inputNetworkFile);
        log.info("prepareT42WorkingDir() - Param: matpower_network_file: {}, ", inputNetworkFile);
        pos = testCasesNetwork.getFileName().indexOf(".m");
        filePrefix = testCasesNetwork.getFileName().substring(0, pos);

        if (StringUtils.isBlank((String) paramsMap.get(ToolWp4Parameters.PARAM_CASE_NAME))) {
            paramsMap.put(ToolWp4Parameters.PARAM_CASE_NAME, filePrefix);
        }

        // --- Prepare parameters for output_file used by the tool:
        // ATSIM/WP4/<T42/uuid/output_data/"
        String outputFile = (!filePrefix.equals(""))
            ? outputDirPath.concat(File.separator).concat(filePrefix).concat(T42FileFormat.OUTPUT_SUFFIX.get(0))
            : outputDirPath.concat(File.separator).concat(T42FileFormat.OUTPUT_SUFFIX.get(0));
        paramsMap.put("out_file", outputFile);
        log.info("prepareT42WorkingDir() - Param: out_file: {}, ", outputFile);

        // -- Save inputfile On DB and FS
        try {
            inputFileDtoSavedList = saveInputFileOnDbAndFileSystem(networkDto, toolDto, files, null, simulationWorkingPath);
        } catch (Exception e) {
            String errMsg = "Error saving inputFile for tool: " + toolDto.getName();
            log.error("prepareT42WorkingDir() - " + errMsg, e);
            throw new Exception(errMsg, e);
        }

        // set missing parameters with def values
        setDefaultValue(paramsMap, ToolWp4Parameters.T42_MAP_PARAM_DEF_VALUE, ToolWp4Parameters.T42_MAP_PARAM_TYPE);

        // -- prepare tool's file json config parameters
        ToolConfigParameters toolConfigParameters = new ToolConfigParameters();
        toolConfigParameters.setParameters(paramsMap);
        String configJsonName = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.CONFIG_FILE);
        File fileConfigParametersJson = Paths.get(configJsonName).toFile();

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileConfigParametersJson, toolConfigParameters);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating " + ToolFileFormat.CONFIG_FILE + " file for tool: " + toolDto.getName();
            log.error("prepareT42WorkingDir() - " + errMsg, e);
            throw new Exception(errMsg, e);
        }
        configMap.put(LAUNCH_JSON_FILE_KEY, configJsonName);

        // Init new Task
        TaskDTO taskDtoSaved;
        try {
            taskDtoSaved = toolExecutionServiceImpl.createTask(toolDto);
        } catch (Exception e) {
            String errMsg = "Error creating new task for tool: " + toolDto.getName();
            log.error("prepareT42WorkingDir() - " + errMsg, e);
            throw new Exception(errMsg, e);
        }

        // Init Simulation
        try {
            SimulationDTO simulationDto = toolExecutionServiceImpl.initSimulation(
                toolDto,
                networkDto,
                uuidGenerated,
                fileConfigParametersJson,
                taskDtoSaved,
                toolExecutionServiceImpl.getInputFilesDtoSetFromList(inputFileDtoSavedList),
                this.t45T42GetSimulationDescr(paramsMap)
            );
            taskDtoSaved.setSimulation(simulationDto);
            taskServiceImpl.partialUpdate(taskDtoSaved);
        } catch (Exception ex) {
            String errMsg = "Error saving simulation data for tool: " + toolDto.getNum() + " uuid: " + uuid.toString();
            log.error("prepareT42WorkingDir() - " + errMsg, ex);
            throw new Exception(errMsg, ex);
        }

        log.info("prepareT42WorkingDir() - EXIT Return: {}", configMap.toString());
        return configMap;
    }

    @Override
    @Transactional
    public Map<String, String> prepareT45WorkingDir(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        String[] fileDesc,
        MultipartFile[] files,
        String[] parameterNames,
        String[] parameterValues,
        Long[] otherToolOutputFileIds
    ) throws Exception {
        Map<String, String> configMap = new HashMap<String, String>();
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();
        log.info("prepareT45WorkingDir() - uuid: {} ", uuid);
        configMap.put(UUID_KEY, uuid);

        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        // Tool's installation path
        String toolInstallationDir = toolSimulationRef.getToolWorkingDir(toolDto);
        configMap.put(TOOL_INSTALL_DIR, toolInstallationDir);
        configMap.put(LAUNCH_TOOL_FILE_KEY, ToolFileFormat.LAUNCH_FILE);

        // Create simulationWorkingDir
        // -- ATSIM/WP4/<ToolNum>/<UUID>/
        String simulationWorkingPath = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);
        Files.createDirectories(Paths.get(simulationWorkingPath));
        log.info("prepareT45WorkingDir() - DIR: {}, created ", simulationWorkingPath);
        configMap.put(WORKING_DIR_KEY, simulationWorkingPath);

        // Create logs dir if not exists
        // -- ATSIM/WP4/<ToolNum>/<UUID>/logs
        // Create logs dir if not exists
        String logDirPath = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        String logFile = logDirPath.concat(File.separator).concat(ToolFileFormat.LOG_FILE_NAME);
        log.info("prepareT45WorkingDir() - DIR:  {}, created ", logDirPath);
        configMap.put(LOG_FILENAME_KEY, logFile);

        // Create outputs dir if not exists
        // -- ATSIM/WP4/<ToolNum>/<UUID>/output_data
        String outputDirPath = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.OUTPUT_DIR);
        Files.createDirectories(Paths.get(outputDirPath));
        log.info("prepareT45WorkingDir() - DIR:  {}, created ", outputDirPath);
        configMap.put(OUTPUT_DIR_KEY, outputDirPath);

        // --- Prepare JsonConfig
        LinkedHashMap<String, Object> paramsMap = new LinkedHashMap<String, Object>();
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                log.info("prepareT45WorkingDir() - Params: [{} : {}] ", parameterNames[i], parameterValues[i]);
                this.setParamFromRequest(paramsMap, parameterNames[i], parameterValues[i], ToolWp4Parameters.T45_MAP_PARAM_TYPE, toolDto);
            }
        }

        // -- ATSIM/WP4/<ToolNum>/<UUID>/
        //name of the file used by the tool for saving the converted network file  from .m format into  .xlsx
        String networkFile = simulationWorkingPath.concat(File.separator).concat(T45FileFormat.T45_NETWORK_FILE_XLSX);
        paramsMap.put("network_file", networkFile);
        log.info("prepareT45WorkingDir() - Param: network_file: {}, ", networkFile);

        String filePrefix = "";
        int pos = 0;

        // --- Prepare parameters for input file used by the tool
        for (int i = 0; i < fileDesc.length; i++) {
            MultipartFile mpFile = files[i];
            String originalFileName = mpFile.getOriginalFilename();
            String inputFile = simulationWorkingPath.concat(File.separator).concat(originalFileName);
            log.info("prepareT45WorkingDir() - FileName: {} , FileDesc:{}", originalFileName, fileDesc[i]);

            switch (fileDesc[i]) {
                case "PV_production_profile_file":
                    // PV_production_diagram.xlsx from simulation folder on cloud server
                    paramsMap.put("PV_production_profile_file", inputFile);
                    break;
                case "state_estimation_csv_file":
                    paramsMap.put("state_estimation_csv_file", inputFile);
                    break;
                case "flex_devices_tech_char_file":
                    // from xlsx simulation xlsx with load profile estabilisched for PV, EV  eg: <Country>_tx_<yyyy>.xlsx
                    paramsMap.put("flex_devices_tech_char_file", inputFile);
                    break;
                // From T44 results
                case "flexibity_devices_states_file":
                    // from T44 procured_flexibility_<case_name><yyyy>_<season>_<wf>.xlsx
                    paramsMap.put("flexibity_devices_states_file", inputFile);
                    break;
                case "DA_curtailment_file":
                    // from T44 <yyyy>_<season>_<wforWof>_Normal.xlsx
                    paramsMap.put("DA_curtailment_file", inputFile);
                    break;
            }
        }

        List<InputFileDTO> inputFileDtoSavedList = new ArrayList<InputFileDTO>();

        if (otherToolOutputFileIds != null && otherToolOutputFileIds.length > 0) {
            addT44ToolResults(otherToolOutputFileIds, simulationWorkingPath, networkDto, toolDto, inputFileDtoSavedList, paramsMap);
        }

        // read network input file from DB using selected network id as key
        InputFile testCasesNetwork = inputFileServiceImpl.findNetworkFileByNetworkId(networkDto.getId());
        Path pathNetworkDataFile = Paths.get(testCasesNetwork.getFileName());
        // add network file into list of input file saved
        InputFileDTO inputFileTestCaseNetworkDTO = inputFileMapper.toDto(testCasesNetwork);
        inputFileDtoSavedList.add(inputFileTestCaseNetworkDTO);

        String inputNetworkFile = simulationWorkingPath.concat(File.separator).concat(testCasesNetwork.getFileName());
        try (FileOutputStream fos = new FileOutputStream(inputNetworkFile)) {
            fos.write(testCasesNetwork.getData());
            log.info("prepareT45WorkingDir() - Matpower network File: {}, Saved on fileSystem!", inputNetworkFile);
        } catch (Exception e) {
            String errMsg = "Error saving file: " + inputNetworkFile;
            log.error("prepareT45WorkingDir() - " + errMsg);
            throw new Exception(errMsg, e);
        }
        paramsMap.put("matpower_network_file", inputNetworkFile);
        log.info("prepareT45WorkingDir() - Param: matpower_network_file: {}, ", inputNetworkFile);
        pos = testCasesNetwork.getFileName().indexOf(".m");
        filePrefix = testCasesNetwork.getFileName().substring(0, pos);

        if (StringUtils.isBlank((String) paramsMap.get(ToolWp4Parameters.PARAM_CASE_NAME))) {
            paramsMap.put(ToolWp4Parameters.PARAM_CASE_NAME, filePrefix);
        }

        // --- Prepare parameters for output_file used by the tool:
        // ATSIM/WP4/<T45/uuid/output_data/"
        String outputFile = (!filePrefix.equals(""))
            ? outputDirPath.concat(File.separator).concat(filePrefix).concat(T45FileFormat.OUTPUT_SUFFIX.get(0))
            : outputDirPath.concat(File.separator).concat(T45FileFormat.OUTPUT_SUFFIX.get(0));
        paramsMap.put("out_file", outputFile);
        log.info("prepareT45WorkingDir() - Param: out_file: {}, ", outputFile);

        // -- Save inputfile On DB and FS
        try {
            inputFileDtoSavedList = saveInputFileOnDbAndFileSystem(networkDto, toolDto, files, null, simulationWorkingPath);
        } catch (Exception e) {
            String errMsg = "Error saving inputFile for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg, e);
        }

        // set missing parameters with def values
        setDefaultValue(paramsMap, ToolWp4Parameters.T45_MAP_PARAM_DEF_VALUE, ToolWp4Parameters.T45_MAP_PARAM_TYPE);

        // -- prepare tool's file json config parameters
        ToolConfigParameters toolConfigParameters = new ToolConfigParameters();
        toolConfigParameters.setParameters(paramsMap);
        String configJsonName = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.CONFIG_FILE);
        File fileConfigParametersJson = Paths.get(configJsonName).toFile();

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileConfigParametersJson, toolConfigParameters);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating " + ToolFileFormat.CONFIG_FILE + " file for tool: " + toolDto.getName();
            log.error("prepareT45WorkingDir() - " + errMsg, e);
            throw new Exception(errMsg, e);
        }
        configMap.put(LAUNCH_JSON_FILE_KEY, configJsonName);

        // Init new Task
        TaskDTO taskDtoSaved;
        try {
            taskDtoSaved = toolExecutionServiceImpl.createTask(toolDto);
        } catch (Exception e) {
            String errMsg = "Error creating new task for tool: " + toolDto.getName();
            log.error("prepareT45WorkingDir() - " + errMsg, e);
            throw new Exception(errMsg, e);
        }

        // Init Simulation
        try {
            SimulationDTO simulationDto = toolExecutionServiceImpl.initSimulation(
                toolDto,
                networkDto,
                uuidGenerated,
                fileConfigParametersJson,
                taskDtoSaved,
                toolExecutionServiceImpl.getInputFilesDtoSetFromList(inputFileDtoSavedList),
                this.t45T42GetSimulationDescr(paramsMap)
            );
            taskDtoSaved.setSimulation(simulationDto);
            taskServiceImpl.partialUpdate(taskDtoSaved);
        } catch (Exception ex) {
            String errMsg = "Error saving simulation data for  tool: " + toolDto.getNum() + " uuid: " + uuid.toString();
            log.error("prepareT45WorkingDir() - " + errMsg, ex);
            throw new Exception(errMsg, ex);
        }

        log.info("prepareT45WorkingDir() - EXIT Return: {}", configMap.toString());
        return configMap;
    }

    private void addT44ToolResults(
        Long[] otherToolOutputFileIds,
        String workingDir,
        NetworkDTO networkDto,
        ToolDTO toolDto,
        List<InputFileDTO> inputFileDtoSavedList,
        LinkedHashMap<String, Object> paramsMap
    ) throws Exception {
        for (Long id : otherToolOutputFileIds) {
            log.debug("addT44ToolResults() -  T44 outputFile's Id - id:{} ", id);

            Optional<OutputFileDTO> outputFileDTO = outputFileServiceImpl.findOne(id);
            if (outputFileDTO.isPresent()) {
                OutputFileDTO outputFileDto = outputFileDTO.get();
                String fileName = outputFileDto.getFileName();
                byte[] data = outputFileDto.getData();
                String contentType = outputFileDto.getDataContentType();

                String inputFileName = workingDir + File.separator + fileName;
                log.debug("addT44ToolResults() - Saving file in the File System:{} ", inputFileName);
                try (FileOutputStream fos = new FileOutputStream(inputFileName)) {
                    fos.write(data);
                    log.debug("addT44ToolResults() - Input File: {}, Saved on fileSystem!", inputFileName);
                } catch (Exception e) {
                    String errMsg = "Error saving file: " + inputFileName;
                    log.error("addT44ToolResults() - " + errMsg);
                    throw new Exception(errMsg);
                }
                // save file as input file for T45
                log.debug("addT44ToolResults() - Saving file: {}, in the INPUT_FILE DB table ", fileName);
                InputFileDTO inputFileDTO = inputFileServiceImpl.saveFileForNetworkAndTool(
                    data,
                    fileName,
                    contentType,
                    networkDto,
                    toolDto
                );
                inputFileDtoSavedList.add(inputFileDTO);

                if (fileName.contains("procured_flexibility")) {
                    paramsMap.put("flexibity_devices_states_file", inputFileName);
                }

                if (fileName.contains("_Normal") || fileName.contains("_normal")) {
                    paramsMap.put("DA_curtailment_file", inputFileName);
                }
            }
        }
        log.debug("addT44ToolResults() - EXIT");
    }

    private List<InputFileDTO> saveInputFileOnDbAndFileSystem(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        MultipartFile[] files,
        Set<String> expectedInputFiles,
        String workingDir
    ) throws Exception {
        log.info("saveInputFileOnDbAndFileSystem() - Store all Input files on DB and under tool's input_dir: {}", workingDir);
        List<InputFileDTO> inputFileSaved = new ArrayList<InputFileDTO>();
        // save input file in db and into the file system
        for (MultipartFile mpFile : files) {
            // check the content type
            log.debug("Tool: {}", toolDto);
            log.debug(
                "saveInputFileOnDbAndFileSystem() - File - originalFileName: {}, name: {}",
                mpFile.getOriginalFilename(),
                mpFile.getName()
            );
            if (expectedInputFiles != null) {
                log.debug("saveInputFileOnDbAndFileSystem() - Tool's expected tool input files: {}", expectedInputFiles.toString());
            }

            if (expectedInputFiles == null || expectedInputFiles.contains(mpFile.getOriginalFilename())) {
                // -- save file ON DB input_file table
                InputFileDTO fileSaved = inputFileServiceImpl.saveFileForNetworkAndTool(mpFile, networkDto, toolDto);
                inputFileSaved.add(fileSaved);
                String inputFileName = workingDir + File.separator + mpFile.getOriginalFilename();

                try (FileOutputStream fos = new FileOutputStream(inputFileName)) {
                    fos.write(mpFile.getBytes());
                    log.info("saveInputFileOnDbAndFileSystem() - Input File: {}, Saved on fileSystem!", inputFileName);
                } catch (Exception e) {
                    String errMsg = "Error saving file: " + inputFileName;
                    log.error("saveInputFileOnDbAndFileSystem() - " + errMsg);
                    throw new Exception(errMsg);
                }
            } else {
                String msg = "File: " + mpFile.getOriginalFilename() + ", is not expected among tools input!";
                log.error("saveInputFileOnDbAndFileSystem() - " + msg);
                throw new Exception(msg);
            }
        }
        return inputFileSaved;
    }

    private void setParamFromRequest(Map mapToSet, String paramName, String paramValue, Map<String, Object> paramsType, ToolDTO toolDto) {
        if (paramName.equals(ToolWp4Parameters.PARAM_CASE_NAME)) {
            paramValue = ConverterUtils.replaceAllSpaceWithUnderscore(paramValue);
        }
        if (paramName.equals(ToolWp4Parameters.PARAM_FLAG_WITH_FLEX) && toolDto.getName().equals(ToolVarName.T41_TRACTABILITY)) {
            this.setT41FlexibilityFlag(mapToSet, paramValue);
        }
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

    private void setT41FlexibilityFlag(Map mapToSet, String paramValue) {
        Integer withFlexibility = Integer.parseInt(paramValue);
        mapToSet.put("flex_fl", withFlexibility);
        mapToSet.put("fl_bin", withFlexibility);
        mapToSet.put("flex_str", withFlexibility);
        mapToSet.put("str_bin", withFlexibility);
        mapToSet.put("oltc_bin", withFlexibility);
        mapToSet.put("flex_oltc", withFlexibility);
    }

    private void setDefaultValue(Map mapToSet, Map<String, Object> paramsDefValue, Map<String, Object> paramsType) {
        for (String paramName : paramsDefValue.keySet()) {
            if (!mapToSet.containsKey(paramName)) {
                Object type = paramsType.get(paramName);
                if (type != null && type.equals(Integer.class)) {
                    Integer value = (Integer) paramsDefValue.get(paramName);
                    mapToSet.put(paramName, value);
                }
                if (type != null && type.equals(Double.class)) {
                    Double value = (Double) (paramsDefValue.get(paramName));
                    mapToSet.put(paramName, value);
                }
                if (type != null && type.equals(String.class)) {
                    mapToSet.put(paramName, paramsDefValue.get(paramName));
                }
            }
        }
    }

    private String prepareSimulationDescr(String caseName, String year, String season, String withFlex) throws Exception {
        try {
            log.debug(
                "prepareSimulationDescr() - ENTER caseName: {}, year: {}, season: {}, withFlex: {}",
                caseName,
                year,
                season,
                withFlex
            );
            String valueSeparator = ":";
            String labelSeparator = ", ";
            StringBuffer simulDescr = new StringBuffer();

            if (caseName != null && !caseName.isEmpty()) {
                simulDescr.append(ToolWp4Parameters.PARAM_CASE_NAME).append(valueSeparator).append(caseName);
            }
            if (year != null && !year.isEmpty()) {
                if (simulDescr.length() > 0) simulDescr.append(labelSeparator);
                simulDescr.append(ToolWp4Parameters.PARAM_YEAR).append(valueSeparator).append(year);
            }

            if (season != null && !season.isEmpty()) {
                if (simulDescr.length() > 0) simulDescr.append(labelSeparator);
                simulDescr.append(ToolWp4Parameters.PARAM_SEASON).append(valueSeparator).append(season);
            }
            if (withFlex != null) {
                if (simulDescr.length() > 0) simulDescr.append(labelSeparator);
                simulDescr.append(ToolWp4Parameters.PARAM_FLAG_WITH_FLEX).append(valueSeparator).append(withFlex);
            }
            log.debug("prepareSimulationDescr() - EXIT Return " + simulDescr.toString());
            return simulDescr.toString();
        } catch (Exception ex) {
            log.error("prepareSimulationDescr() - Error: ", ex);
            throw new Exception(ex);
        }
    }

    private String t44V3GetSimulationDescr(Map paramsMap) throws Exception {
        String caseName = (paramsMap.get(ToolWp4Parameters.PARAM_CASE_NAME) != null)
            ? (String) paramsMap.get(ToolWp4Parameters.PARAM_CASE_NAME)
            : "";
        String season = "";
        if (paramsMap.get("profile") != null) {
            season = ((Integer) paramsMap.get("profile")).equals(new Integer(1)) ? "Summer" : "Winter ";
        }
        String withFlex = "";
        if (paramsMap.get(ToolWp4Parameters.PARAM_FLEXIBILITY) != null) {
            withFlex = withFlex + (Integer) (paramsMap.get(ToolWp4Parameters.PARAM_FLEXIBILITY));
        }

        String year = (paramsMap.get(ToolWp4Parameters.PARAM_YEAR) != null)
            ? String.valueOf((Integer) paramsMap.get(ToolWp4Parameters.PARAM_YEAR))
            : "";

        return prepareSimulationDescr(caseName, year, season, withFlex);
    }

    private String t45T42GetSimulationDescr(Map paramsMap) throws Exception {
        String caseName = (paramsMap.get(ToolWp4Parameters.PARAM_CASE_NAME) != null)
            ? (String) paramsMap.get(ToolWp4Parameters.PARAM_CASE_NAME)
            : "";

        String paramSeason = ToolWp4Parameters.PARAM_SEASON;
        String season = (paramsMap.get(paramSeason) != null && !((String) paramsMap.get(paramSeason)).isEmpty())
            ? ToolWp4Parameters.MAP_PARAM_SEASON.get(paramsMap.get(paramSeason))
            : "";

        String withFlex = "";
        if (paramsMap.get(ToolWp4Parameters.PARAM_FLAG_WITH_FLEX) != null) {
            withFlex = withFlex + (Integer) (paramsMap.get(ToolWp4Parameters.PARAM_FLAG_WITH_FLEX));
        }
        String year = (paramsMap.get(ToolWp4Parameters.PARAM_YEAR) != null)
            ? String.valueOf((Integer) paramsMap.get(ToolWp4Parameters.PARAM_YEAR))
            : "";
        return prepareSimulationDescr(caseName, year, season, withFlex);
    }

    private String t41GetSimulationDescr(Map paramsMap) throws Exception {
        String caseName = (paramsMap.get(ToolWp4Parameters.PARAM_CASE_NAME) != null)
            ? (String) paramsMap.get(ToolWp4Parameters.PARAM_CASE_NAME)
            : "";
        String paramSeason = ToolWp4Parameters.PARAM_SEASON;
        String season = (paramsMap.get(paramSeason) != null && !((String) paramsMap.get(paramSeason)).isEmpty())
            ? ToolWp4Parameters.MAP_PARAM_SEASON.get(paramsMap.get(ToolWp4Parameters.PARAM_SEASON))
            : "";
        int countFlex =
            ((Integer) paramsMap.get("flex_fl")).intValue() +
            ((Integer) paramsMap.get("fl_bin")).intValue() +
            ((Integer) paramsMap.get("flex_str")).intValue() +
            ((Integer) paramsMap.get("str_bin")).intValue() +
            ((Integer) paramsMap.get("flex_oltc")).intValue() +
            ((Integer) paramsMap.get("oltc_bin")).intValue();

        String withFlex = (countFlex > 0) ? "1" : "0";
        String year = (paramsMap.get(ToolWp4Parameters.PARAM_YEAR) != null)
            ? String.valueOf((Integer) paramsMap.get(ToolWp4Parameters.PARAM_YEAR))
            : "";
        return prepareSimulationDescr(caseName, year, season, withFlex);
    }
}
