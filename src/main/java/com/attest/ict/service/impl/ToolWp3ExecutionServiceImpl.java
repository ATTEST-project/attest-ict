package com.attest.ict.service.impl;

import com.attest.ict.config.ToolsConfiguration;
import com.attest.ict.custom.tools.utils.ToolSimulationReferencies;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.domain.InputFile;
import com.attest.ict.helper.TaskStatus;
import com.attest.ict.service.InputFileService;
import com.attest.ict.service.LoadProfileService;
import com.attest.ict.service.OutputFileService;
import com.attest.ict.service.TaskService;
import com.attest.ict.service.ToolWp3ExecutionService;
import com.attest.ict.service.UserService;
import com.attest.ict.service.dto.InputFileDTO;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.OutputFileDTO;
import com.attest.ict.service.dto.SimulationDTO;
import com.attest.ict.service.dto.TaskDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.custom.T31InputParamDTO;
import com.attest.ict.service.dto.custom.T31LaunchParamDTO;
import com.attest.ict.service.dto.custom.T31ParamsDTO;
import com.attest.ict.service.dto.custom.ToolConfigParameters;
import com.attest.ict.service.mapper.InputFileMapper;
import com.attest.ict.tools.RunWrapper;
import com.attest.ict.tools.constants.T31FileFormat;
import com.attest.ict.tools.constants.T32FileFormat;
import com.attest.ict.tools.constants.ToolFileFormat;
import com.attest.ict.tools.exception.RunningToolException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ToolWp3ExecutionServiceImpl implements ToolWp3ExecutionService {

    private final Logger log = LoggerFactory.getLogger(ToolWp3ExecutionServiceImpl.class);

    @Autowired
    private OutputFileService outputFileServiceImpl;

    @Autowired
    private InputFileService inputFileServiceImpl;

    @Autowired
    private LoadProfileService loadProfileServiceImpl;

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

    public ToolWp3ExecutionServiceImpl(ToolsConfiguration toolsConfig) {
        // ATTEST/tools
        this.attestToolsDir = toolsConfig.getPath();
        log.debug("attestToolsDir {}", attestToolsDir);
        // ATSIM
        this.toolsPathSimulation = toolsConfig.getPathSimulation();
        log.debug("toolsPathSimulation {}", toolsPathSimulation);
    }

    @Override
    public String t31Run(NetworkDTO networkDto, ToolDTO toolDto, T31InputParamDTO params) throws RunningToolException, Exception {
        //-- Save new Task
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
        String logDirPath = simulationWorkingDir.concat(File.separator).concat(T31FileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        String logFile = logDirPath.concat(File.separator).concat(T31FileFormat.LOG_FILE_NAME);
        Path logFilePath = Paths.get(logFile);

        // Create outputDir
        String outputDir = simulationWorkingDir.concat(File.separator).concat(toolSimulationRef.getOutputData());
        Files.createDirectories(Paths.get(outputDir));

        // -- ATTEST/tools/TXY
        String toolWorkingDir = toolSimulationRef.getToolWorkingDir(toolDto).concat(File.separator).concat("pyensys");

        // --- Write network file  (Network file .m) in tool simulation directory
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

        //-- Add network file to the list of input file used by the tool
        InputFileDTO inputFileTestCaseNetworkDTO = inputFileMapper.toDto(testCasesNetwork);
        List<InputFileDTO> inputFileDtoSavedList = new ArrayList<InputFileDTO>();
        inputFileDtoSavedList.add(inputFileTestCaseNetworkDTO);

        T31LaunchParamDTO parameters = new T31LaunchParamDTO();
        // -- Set tool's parameters
        T31ParamsDTO configParams = new T31ParamsDTO();
        configParams.setLineCapacities(params.getLineCapacities());
        configParams.setTrsCapacities(params.getTrsCapacities());
        configParams.setLineCosts(params.getLineCosts());
        configParams.setTrsCosts(params.getTrsCosts());
        configParams.setContList(params.getContList());
        configParams.setLineLength(params.getLineLength());
        configParams.setGrowth(params.getGrowth());
        configParams.setDsr(params.getDsr());
        configParams.setCluster(params.getCluster());
        configParams.setOversize(params.getOversize());
        configParams.setMaxClusters(params.getMaxClusters());
        configParams.setScenarios(params.getScenarios());
        // -- Set parameter: case_path
        configParams.setCasePath(inputNetworkFile);
        // --- Set parameter: output_path:
        String testCaseFileName = testCasesNetwork.getFileName();
        int pos = testCaseFileName.indexOf(T31FileFormat.CASE_INPUT_SUFFIX);
        String outputFileName = testCaseFileName.substring(0, pos) + T31FileFormat.OUTPUT_SUFFIX;
        // --- Prepare parameters for output file used by the tool:
        // ATSIM/WP3/T31/uuid/output_data/case4_outputs.json"
        String outputPath = outputDir.concat(File.separator).concat(outputFileName);
        configParams.setOutputPath(outputPath);
        // -- Prepare parameter "attest_inputs_path": "./tmp/case3_inputs.json",
        String attestInputJsonFile = testCaseFileName.substring(0, pos) + T31FileFormat.ATTEST_INPUT_SUFFIX;
        String attestInputPath = outputDir.concat(File.separator).concat(attestInputJsonFile);
        configParams.setAttestInputsPath(attestInputPath);
        parameters.setParameters(configParams);

        //-- Prepare json configuration file used by the tool
        String configJsonName = simulationWorkingDir.concat(File.separator).concat(T31FileFormat.CONFIG_FILE);
        File fileConfigParametersJson = Paths.get(configJsonName).toFile();

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileConfigParametersJson, parameters);
            log.debug("Tool's config file: {} saved ", parameters);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating JSON: " + configJsonName + "  for tool: " + toolDto.getName();
            throw new Exception(errMsg + " " + e.getMessage());
        }

        List<OutputFileDTO> outputFileSavedList = new ArrayList<OutputFileDTO>();
        // run tool
        int esito = this.runToolWrapper(toolWorkingDir, configJsonName, logFile, T31FileFormat.LAUNCH_FILE);
        // check if exit value is 0 (OK) store output file on DB
        if (esito == 0) {
            try {
                File outputFileDir = new File(outputDir);
                outputFileSavedList =
                    toolExecutionServiceImpl.saveOutputFileOnDB(outputFileDir, networkDto, toolDto, T31FileFormat.OUTPUT_SUFFIX);
                toolExecutionServiceImpl.uploadToolLogFileToDB(logFilePath, taskSaved, false);
            } catch (IOException ioex) {
                String errMsg = "Error saving results for tool: " + toolDto.getName();
                toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
                log.error(errMsg, ioex);
                throw new Exception(errMsg + " " + ioex.getMessage());
            }

            try {
                //-- Save new Simulation
                SimulationDTO simulationDto = toolExecutionServiceImpl.createSimulation(
                    toolDto,
                    networkDto,
                    uuidGenerated,
                    fileConfigParametersJson,
                    taskSaved,
                    inputFileDtoSavedList,
                    outputFileSavedList,
                    TaskStatus.Status.PASSED,
                    "" // description
                );
                log.info("Simulation: {}, saved succesfully ", simulationDto);
            } catch (Exception ex) {
                String errMsg = "Error saving simulation data for  tool: " + toolDto.getNum() + " uuid: " + uuid.toString();
                log.error(errMsg, ex.getMessage());
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

    public String t32Run(NetworkDTO networkDto, ToolDTO toolDto, Long profileId, MultipartFile[] files, String jsonConfig)
        throws RunningToolException, Exception {
        //tests/json  copiarci la cartella della rete .m
        String inputNetworkDir = T32FileFormat.TEST_DIR.concat(File.separator).concat("json");

        //tests/excel   add load profile in excel format
        String inputLoadProfileDir = T32FileFormat.TEST_DIR.concat(File.separator).concat("excel");

        //SCOPF_R5/input_data
        // add   _PROF from  T44 HR_Tx_03_2020_new_Zagreb_PROF_update_v2.ods  file is not mandatory
        // add  file: scenario_gen_ods from TSG
        String scopfInputDataDir = T32FileFormat.INPUT_SCOPF_R5_DIR.concat(File.separator).concat("input_data");

        //SCOPF_R5/data_preparation
        //durante l'esecuzione del modello investment legge e scrive nella cartella
        String scopfDataPreparationDir = T32FileFormat.INPUT_SCOPF_R5_DIR.concat(File.separator).concat("data_preparation");

        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();

        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);

        // Create simulationWorkingDir
        // -- ATSIM/WP3/T32/UUID/
        String simulationWorkingPath = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid.toString());
        Files.createDirectories(Paths.get(simulationWorkingPath));

        // -- ATSIM/WP3/T32/UUID/inputs
        String inputDirPath = simulationWorkingPath.concat(File.separator).concat(T32FileFormat.INPUT_DIR);
        Files.createDirectories(Paths.get(inputDirPath));

        // Create logs dir if not exists
        // -- ATSIM/WP3/T32/UUID/logs
        String logDirPath = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        String logFile = logDirPath.concat(File.separator).concat(ToolFileFormat.LOG_FILE_NAME);
        Path logFilePath = Paths.get(logFile);

        // Create outputs dir if not exists
        // -- ATSIM/WP3/T32/UUID/outputs
        String outputDirPath = simulationWorkingPath.concat(File.separator).concat(T32FileFormat.OUTPUT_DIR);
        Files.createDirectories(Paths.get(outputDirPath));

        // Create input_dir for network file Dir
        // -- ATSIM/WP3/T32/UUID/inputs/tests/json
        String inputNetworkPath = inputDirPath.concat(File.separator).concat(inputNetworkDir);
        Files.createDirectories(Paths.get(inputNetworkPath));
        log.debug("inputNetworkPath : {}  created ", inputNetworkPath);

        //-- ATSIM/WP5/T32/UUID/inputs/tests/excel
        String inputLoadProfileDirPath = inputDirPath.concat(File.separator).concat(inputLoadProfileDir);
        Files.createDirectories(Paths.get(inputLoadProfileDirPath));
        log.debug("inputLoadProfileDirPath : {}  created ", inputLoadProfileDirPath);

        // Create /ATSIM/WP5/T53/UUID/inputs/SCOPF_R5/input_data   dir if not exists
        String scopfInputDataDirPath = inputDirPath.concat(File.separator).concat(scopfInputDataDir);
        Files.createDirectories(Paths.get(scopfInputDataDirPath));
        log.debug("scopfInputDataDirPath : {}  created ", scopfInputDataDirPath);

        //Create //ATSIM/WP5/T53/UUID/SCOPF_R5/data_preparation dir not exists
        String scopfDataPreparationDirPath = inputDirPath.concat(File.separator).concat(scopfDataPreparationDir);
        Files.createDirectories(Paths.get(scopfDataPreparationDirPath));
        log.debug("scopfDataPreparationDirPath : {}  created ", scopfDataPreparationDirPath);

        // Read jsonConfig input file
        ToolConfigParameters inputParam;
        ObjectMapper mapper = new ObjectMapper();
        try {
            inputParam = mapper.readValue(jsonConfig, ToolConfigParameters.class);
        } catch (JsonProcessingException e) {
            String errMsg = "Error converting jsonConfig to ToolConfigParameters";
            log.error(errMsg, e);
            throw e;
        }

        LinkedHashMap<String, Object> parameters = inputParam.getParameters();

        // Tools need's input_dir path and output_dir path
        parameters.put("input_dir", inputDirPath);
        parameters.put("output_dir", outputDirPath);

        //Save input file for T32 into filesystem and DB
        List<InputFileDTO> inputFileDtoSavedList = new ArrayList<InputFileDTO>();
        try {
            inputFileDtoSavedList =
                this.saveInputFileOnDbAndFileSystem(networkDto, toolDto, files, scopfInputDataDirPath, inputLoadProfileDirPath, parameters);
        } catch (Exception e) {
            String errMsg = "Error saving inputFile for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + " " + e.getMessage());
        }

        //Load network file from db
        InputFile networkFile = inputFileServiceImpl.findNetworkFileByNetworkId(networkDto.getId());
        String networkFileName = networkFile.getFileName();
        log.debug("networkFileName : {} ", networkFileName);
        // add network file into list of input file saved (need for simulation)
        InputFileDTO inputFileTestCaseNetworkDTO = inputFileMapper.toDto(networkFile);
        inputFileDtoSavedList.add(inputFileTestCaseNetworkDTO);

        //-- Set test_case parameter = network file name, less extension
        String ext = FileUtils.getFileExtension(networkFileName);
        if (!ext.equals("m")) {
            String errMsg = "Error network file invalid file format: " + toolDto.getName();
            log.error(errMsg);
            throw new Exception(errMsg);
        }

        String testCase = FileUtils.getFileLessExtension(networkFileName);
        if (!testCase.isEmpty()) {
            parameters.put("test_case", testCase); // less extension
            String inputNetworkFile = inputNetworkPath.concat(File.separator).concat(networkFileName);
            try {
                //save network file on file system
                saveFileOnFs(inputNetworkFile, networkFile.getData());
                log.debug("NetworkFile: {} saved on FS  ", inputNetworkFile);
            } catch (Exception e) {
                throw new Exception(e);
            }
        }

        //Se parameter: xlsx_file_name (Auxiliary Load and Flexibility Profile Name)
        if (profileId != null) {
            Optional<InputFileDTO> inputProfileFileDto = inputFileServiceImpl.findOne(profileId);
            if (inputProfileFileDto.isPresent()) {
                String xlsx_file_name = inputProfileFileDto.get().getFileName();
                log.debug("xlsx_file_name : {} ", xlsx_file_name);
                parameters.put("xlsx_file_name", FileUtils.getFileLessExtension(xlsx_file_name));
                inputFileDtoSavedList.add(inputFileTestCaseNetworkDTO);
                try {
                    //save load Profile on file system
                    String xlsFileName = inputLoadProfileDirPath.concat(File.separator).concat(xlsx_file_name);
                    saveFileOnFs(xlsFileName, inputProfileFileDto.get().getData());
                    log.debug("Load Profile: {} saved on FS  ", xlsFileName);
                } catch (Exception e) {
                    throw new Exception(e);
                }
            }
        }

        //Create launch.json file:
        ToolConfigParameters launchParams = new ToolConfigParameters();
        launchParams.setParameters(parameters);

        // -- save launch.json file on fileSystem
        String launchJsonFileName = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.CONFIG_FILE);
        File fileConfigParametersJson = Paths.get(launchJsonFileName).toFile();
        mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileConfigParametersJson, launchParams);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating " + ToolFileFormat.CONFIG_FILE + "  for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + e.getMessage());
        }

        // -- ATTEST/tools/T32
        String toolWorkingDir = toolSimulationRef.getToolWorkingDir(toolDto);
        log.debug("toolWorkingDir : {} ", toolWorkingDir);

        // Create new Task
        TaskDTO taskSaved;
        try {
            taskSaved = toolExecutionServiceImpl.createTask(toolDto);
        } catch (Exception e) {
            String errMsg = "Error creating new task for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg);
        }

        int esito = runToolWrapper(toolWorkingDir, launchJsonFileName, logFile, ToolFileFormat.LAUNCH_FILE);

        // check if exit value is 0 (OK) store output file on DB
        List<OutputFileDTO> outputFileDtoSavedList = new ArrayList<OutputFileDTO>();
        if (esito == 0) {
            try {
                outputFileDtoSavedList =
                    toolExecutionServiceImpl.saveOutputFileOnDB(
                        new File(outputDirPath),
                        networkDto,
                        toolDto,
                        T32FileFormat.RESULTS_FILES_EXTENSION
                    );
                toolExecutionServiceImpl.uploadToolLogFileToDB(logFilePath, taskSaved, false);
            } catch (IOException ioex) {
                String errMsg = "Error saving results for tool: " + toolDto.getName();
                toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
                log.error(errMsg, ioex);
                throw new Exception(errMsg + " " + ioex.getMessage());
            }

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
                // tool run succesfully so we set status PASSED but not refereces to simulation
                // will be set
                toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.PASSED);
            }

            return uuid;
        } else {
            toolExecutionServiceImpl.uploadToolLogFileToDB(logFilePath, taskSaved, false);
            //   toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            String errMsg = "Error running tool: " + toolDto.getNum() + " uuid: " + uuid;
            log.error(errMsg);
            throw new RunningToolException(errMsg);
        }
    }

    /**
     *
     * @param networkDto
     * @param toolDto
     * @param files
     * @param scopfInputDataDirPath
     * @param inputLoadProfileDirPath
     * @param parameters
     * @return
     * @throws Exception
     */
    private List<InputFileDTO> saveInputFileOnDbAndFileSystem(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        MultipartFile[] files,
        String scopfInputDataDirPath,
        String inputLoadProfileDirPath,
        LinkedHashMap<String, Object> parameters
    ) throws Exception {
        log.info("Store all loaded Input files in input_file DB table  and under tool's working directory");
        String profFileName = (String) parameters.get("ods_file_name");
        log.info("Contingencies file _PROF name {} ", profFileName);

        List<InputFileDTO> inputFileSaved = new ArrayList<InputFileDTO>();
        for (MultipartFile mpFile : files) {
            String originalFileName = mpFile.getOriginalFilename();
            String mpFileContentType = mpFile.getContentType();

            log.debug("Input File: {} , Content Type:{} ", mpFile.getOriginalFilename(), mpFile.getContentType());
            if (T32FileFormat.MAP_INPUT_CONTENT_TYPE.values().contains(mpFileContentType)) {
                // -- save input file selected by user into the  input_file table
                InputFileDTO fileSaved = inputFileServiceImpl.saveFileForNetwork(mpFile, networkDto, toolDto);
                inputFileSaved.add(fileSaved);
                log.info("Input File: {}  saved in DB", mpFile.getName());

                // save into <working_Dir>/SCOPF_R5/input_data
                String inputFileName = "";
                if (originalFileName.equals("scenario_gen.ods") || originalFileName.contains(profFileName)) {
                    inputFileName = scopfInputDataDirPath + File.separator + originalFileName;
                }

                // save into <working_Dir>/test/excel
                //Auxiliary Load Profile
                //
                if (mpFileContentType.equals(T32FileFormat.MAP_INPUT_CONTENT_TYPE.get("xlsx"))) {
                    inputFileName = inputLoadProfileDirPath + File.separator + originalFileName;
                    parameters.put("xlsx_file_name", FileUtils.getFileLessExtension(originalFileName));
                }

                try (FileOutputStream fos = new FileOutputStream(inputFileName)) {
                    fos.write(mpFile.getBytes());
                    log.debug("Input File: {}, Saved on fileSystem!", inputFileName);
                } catch (Exception e) {
                    String errMsg = "Error saving file: " + inputFileName;
                    log.error(errMsg);
                    throw new Exception(errMsg);
                }
            } else {
                String msg = "InputFile content type not valid ";
                log.error(msg);
                throw new Exception(msg);
            }
        }

        return inputFileSaved;
    }

    private int runToolWrapper(String toolWorkingDir, String jsonConfig, String logFile, String launchFile) {
        Process process = RunWrapper.executeBatchFile(toolWorkingDir, jsonConfig, launchFile);
        FileUtils.writeToolLogsToFile(new InputStreamReader(process.getInputStream()), logFile);
        //TODO check if process is terminated
        int exitValue = process.exitValue();

        log.info("Tool:{} END with exit value: {} ", toolWorkingDir, exitValue);
        return exitValue;
    }

    private void saveFileOnFs(String inputFileName, byte[] data) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(inputFileName)) {
            fos.write(data);
            log.debug("Input File: {}, Saved on fileSystem!", inputFileName);
        } catch (Exception e) {
            String errMsg = "Error saving file: " + inputFileName;
            log.error(errMsg);
            throw new Exception(errMsg);
        }
    }
}
