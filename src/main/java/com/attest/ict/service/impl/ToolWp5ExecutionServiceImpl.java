package com.attest.ict.service.impl;

import com.attest.ict.config.ToolsConfiguration;
import com.attest.ict.custom.tools.utils.ToolSimulationReferencies;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.helper.TaskStatus;
import com.attest.ict.helper.csv.reader.CsvFileReader;
import com.attest.ict.helper.excel.reader.ExcelReader;
import com.attest.ict.helper.excel.util.ExcelFileFormat;
import com.attest.ict.service.InputFileService;
import com.attest.ict.service.OutputFileService;
import com.attest.ict.service.TaskService;
import com.attest.ict.service.ToolWp5ExecutionService;
import com.attest.ict.service.UserService;
import com.attest.ict.service.dto.InputFileDTO;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.OutputFileDTO;
import com.attest.ict.service.dto.SimulationDTO;
import com.attest.ict.service.dto.TaskDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.custom.CoordinatesConfigDTO;
import com.attest.ict.service.dto.custom.T51CharacterizationConfigDTO;
import com.attest.ict.service.dto.custom.T51CharacterizationInputDTO;
import com.attest.ict.service.dto.custom.T51CharacterizationLaunchParamDTO;
import com.attest.ict.service.dto.custom.T51MonitoringInputDTO;
import com.attest.ict.service.dto.custom.T51MonitoringLaunchParamDTO;
import com.attest.ict.service.dto.custom.T52InputParamDTO;
import com.attest.ict.service.dto.custom.T52LaunchParamDTO;
import com.attest.ict.service.dto.custom.T53AllConfigDTO;
import com.attest.ict.service.dto.custom.T53FirstConfigDTO;
import com.attest.ict.service.dto.custom.T53InputParamDTO;
import com.attest.ict.service.dto.custom.T53LaunchParamDTO;
import com.attest.ict.service.dto.custom.T5ConfigDTO;
import com.attest.ict.tools.RunWrapper;
import com.attest.ict.tools.constants.T51FileFormat;
import com.attest.ict.tools.constants.T52FileFormat;
import com.attest.ict.tools.constants.T53FileFormat;
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
import java.util.List;
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
public class ToolWp5ExecutionServiceImpl implements ToolWp5ExecutionService {

    private final Logger log = LoggerFactory.getLogger(ToolWp5ExecutionServiceImpl.class);

    @Autowired
    ToolExecutionServiceImpl toolExecutionServiceImpl;

    @Autowired
    private InputFileService inputFileServiceImpl;

    @Autowired
    private OutputFileService outputFileServiceImpl;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    private String attestToolsDir;

    private String toolsPathSimulation;

    public ToolWp5ExecutionServiceImpl(ToolsConfiguration toolsConfig) {
        // ATTEST/tools
        this.attestToolsDir = toolsConfig.getPath();
        log.debug("attestToolsDir {}", attestToolsDir);
        // ATSIM
        this.toolsPathSimulation = toolsConfig.getPathSimulation();
        log.debug("toolsPathSimulation {}", toolsPathSimulation);
    }

    @Override
    public String[] getFileHeaders(MultipartFile mpFile) {
        String[] headers;
        String contentType = mpFile.getContentType();
        switch (contentType) {
            case ExcelFileFormat.CONTENT_TYPE:
            case "application/vnd.ms-excel":
                ExcelReader readerExcel = new ExcelReader();
                headers = readerExcel.parseHeaderFile(mpFile, 0);
                break;
            default:
                CsvFileReader readerCsv = new CsvFileReader();
                headers = readerCsv.parseCsvHeaderMultiPartFile(mpFile);
                break;
        }

        return headers;
    }

    @Override
    public String t52Run(NetworkDTO networkDto, ToolDTO toolDto, String jsonConfig, MultipartFile[] files)
        throws RunningToolException, Exception {
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();

        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);

        // Create simulationWorkingDir
        // -- ATSIM/WP5/T52/UUID/
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid.toString());
        Files.createDirectories(Paths.get(simulationWorkingDir));

        // Create input dir if not exists
        // -- ATSIM/WP5/T52/UUID/dataset
        // String inputDirPath =
        // simulationWorkingDir.concat(File.separator).concat(T52FileFormat.INPUT_DIR);
        // Files.createDirectories(Paths.get(inputDirPath));

        // Create logs dir if not exists
        // -- ATSIM/WP5/T52/UUID/logs
        String logDirPath = simulationWorkingDir.concat(File.separator).concat(ToolFileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        String logFile = logDirPath.concat(File.separator).concat(ToolFileFormat.LOG_FILE_NAME);
        Path logFilePath = Paths.get(logFile);

        // Create
        // -- ATSIM/WP5/T52/UUID/output-data
        String outputData = simulationWorkingDir.concat(File.separator).concat(toolSimulationRef.getOutputData());
        Files.createDirectories(Paths.get(outputData));

        // jsonConfig String to jsonFile
        T52InputParamDTO inputParam;
        ObjectMapper mapper = new ObjectMapper();
        try {
            inputParam = mapper.readValue(jsonConfig, T52InputParamDTO.class);
        } catch (JsonProcessingException e) {
            String errMsg = "Error converting jsonConfig to T52InputParamDTO";
            log.error(errMsg, e);
            throw e;
        }

        List<T5ConfigDTO> mainConfigList = inputParam.getMainConfig();

        // -- Replace path
        for (int i = 0; i < mainConfigList.size(); i++) {
            T5ConfigDTO configDto = mainConfigList.get(i);
            String path = configDto.getPath();
            String absolutePath = simulationWorkingDir.concat(File.separator).concat(path);
            configDto.setPath(absolutePath);
            mainConfigList.set(i, configDto);
        }

        T52LaunchParamDTO launchParam = new T52LaunchParamDTO();
        launchParam.setOutputDir(outputData.concat(File.separator));
        launchParam.setMainConfig(mainConfigList);

        // -- Replace path
        CoordinatesConfigDTO coordinateConfig = inputParam.getCoordinatesConfig();
        if (coordinateConfig != null) {
            String coordinatePath = coordinateConfig.getCoordinatesFilePath();
            String coordinateAbsolutePath = simulationWorkingDir.concat(File.separator).concat(coordinatePath);
            coordinateConfig.setCoordinatesFilePath(coordinateAbsolutePath);
            launchParam.setCoordinatesConfig(coordinateConfig);
        }

        // -- ATTEST/tools/T52
        String toolWorkingDir = toolSimulationRef.getToolWorkingDir(toolDto);

        // -- save launch.json file on fileSysterm
        String launchJsonFileName = simulationWorkingDir.concat(File.separator).concat(ToolFileFormat.CONFIG_FILE);
        File fileConfigParametersJson = Paths.get(launchJsonFileName).toFile();
        mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileConfigParametersJson, launchParam);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating launch.json file for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + e.getMessage());
        }

        List<InputFileDTO> inputFileSavedList = new ArrayList<InputFileDTO>();
        try {
            inputFileSavedList =
                saveInputFileOnDbAndFileSystem(networkDto, toolDto, files, simulationWorkingDir, T52FileFormat.INPUT_CONTENT_TYPE);
        } catch (Exception e) {
            // toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            String errMsg = "Error saving inputFile for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + " " + e.getMessage());
        }

        // Create new Task
        TaskDTO taskSaved;
        try {
            taskSaved = toolExecutionServiceImpl.createTask(toolDto);
        } catch (Exception e) {
            String errMsg = "Error creating new task for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg);
        }

        int esito = runToolWrapper(toolWorkingDir, launchJsonFileName, logFile, T52FileFormat.LAUNCH_FILE);
        // check if exit value is 0 (OK) store output file on DB
        List<OutputFileDTO> outputFileDtoSavedList = new ArrayList<OutputFileDTO>();
        if (esito == 0) {
            try {
                outputFileDtoSavedList =
                    this.saveOutputFileOnDB(new File(outputData), networkDto, toolDto, T52FileFormat.DOWNLOAD_FILES_EXTENSION);

                toolExecutionServiceImpl.uploadToolLogFileToDB(logFilePath, taskSaved, false);
            } catch (IOException ioex) {
                String errMsg = "Error saving results for tool: " + toolDto.getName();
                toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
                log.error(errMsg, ioex);
                throw new Exception(errMsg + " " + ioex.getMessage());
            }
            // toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.PASSED);
            log.info("Tool: {}, on Network: {}, running succesfully ", toolDto.getName(), networkDto.getName());
            try {
                SimulationDTO simulationDto = toolExecutionServiceImpl.createSimulation(
                    toolDto,
                    networkDto,
                    uuidGenerated,
                    fileConfigParametersJson,
                    taskSaved,
                    inputFileSavedList,
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
            toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            String errMsg = "Error running tool: " + toolDto.getNum() + " uuid: " + uuid;
            log.error(errMsg);
            throw new RunningToolException(errMsg);
        }
    }

    @Override
    public String t512MonitoringRun(NetworkDTO networkDto, ToolDTO toolDto, String jsonConfig, MultipartFile[] files)
        throws RunningToolException, Exception {
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();

        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);

        // Create simulationWorkingDir
        // -- ATSIM/WP5/T51/UUID/
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid.toString());
        Files.createDirectories(Paths.get(simulationWorkingDir));

        // Create logs dir if not exists
        // -- ATSIM/WP5/T51/UUID/logs
        String logDirPath = simulationWorkingDir.concat(File.separator).concat(ToolFileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        String logFile = logDirPath.concat(File.separator).concat(ToolFileFormat.LOG_FILE_NAME);
        Path logFilePath = Paths.get(logFile);

        // Create output data dir
        // -- ATSIM/WP5/T51/UUID/output-data
        String outputData = simulationWorkingDir.concat(File.separator).concat(toolSimulationRef.getOutputData());
        Files.createDirectories(Paths.get(outputData));

        // jsonConfig String to jsonFile
        T51MonitoringInputDTO inputParam;
        ObjectMapper mapper = new ObjectMapper();
        try {
            inputParam = mapper.readValue(jsonConfig, T51MonitoringInputDTO.class);
        } catch (JsonProcessingException e) {
            String errMsg = "Error converting jsonConfig to T52InputParamDTO";
            log.error(errMsg, e);
            throw new Exception(errMsg + e.getMessage());
        }

        // Replace path
        T51MonitoringLaunchParamDTO launchParam = new T51MonitoringLaunchParamDTO();
        if (inputParam.getModelPath1() != null && !inputParam.getModelPath1().isEmpty()) {
            launchParam.setModelPath1(this.replaceFilePath(inputParam.getModelPath1(), simulationWorkingDir));
        }
        if (inputParam.getModelPath2() != null && !inputParam.getModelPath2().isEmpty()) {
            launchParam.setModelPath2(this.replaceFilePath(inputParam.getModelPath2(), simulationWorkingDir));
        }
        if (inputParam.getFilePath2() != null && !inputParam.getFilePath2().isEmpty()) {
            launchParam.setFilePath2(this.replaceFilePath(inputParam.getFilePath2(), simulationWorkingDir));
        }

        // set Item1, Item2, Item 3
        launchParam.setItem1(inputParam.getItem1());
        launchParam.setItem2(inputParam.getItem2());
        launchParam.setItem3(inputParam.getItem3());
        launchParam.setOutputDir(outputData.concat(File.separator));

        // -- ATTEST/tools/T51
        String toolWorkingDir = toolSimulationRef.getToolWorkingDir(toolDto);

        // -- save launch_monitoring.json file on fileSysterm
        String launchJsonFileName = simulationWorkingDir.concat(File.separator).concat(T51FileFormat.MONITORING_FILE_CONFIG);
        File fileConfigParametersJson = Paths.get(launchJsonFileName).toFile();
        mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileConfigParametersJson, launchParam);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating launch.json file for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + e.getMessage());
        }

        List<InputFileDTO> inputFileSavedList = new ArrayList<InputFileDTO>();
        try {
            inputFileSavedList =
                saveInputFileOnDbAndFileSystem(
                    networkDto,
                    toolDto,
                    files,
                    simulationWorkingDir,
                    T51FileFormat.MONITORING_INPUT_CONTENT_TYPE
                );
        } catch (Exception e) {
            // toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            String errMsg = "Error saving inputFile for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + " " + e.getMessage());
        }

        // Create new Task
        TaskDTO taskSaved;
        try {
            taskSaved = toolExecutionServiceImpl.createTask(toolDto);
        } catch (Exception e) {
            String errMsg = "Error creating new task for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg);
        }

        int esito = runToolWrapper(toolWorkingDir, launchJsonFileName, logFile, T51FileFormat.MONITORING_FILE_LAUNCH);

        // check if exit value is 0 (OK) store output file on DB
        List<OutputFileDTO> outputFileDtoSavedList = new ArrayList<OutputFileDTO>();
        if (esito == 0) {
            try {
                outputFileDtoSavedList =
                    this.saveOutputFileOnDB(new File(outputData), networkDto, toolDto, T52FileFormat.DOWNLOAD_FILES_EXTENSION);

                toolExecutionServiceImpl.uploadToolLogFileToDB(logFilePath, taskSaved, false);
            } catch (IOException ioex) {
                String errMsg = "Error saving results for tool: " + toolDto.getName();
                toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
                log.error(errMsg, ioex);
                throw new Exception(errMsg + " " + ioex.getMessage());
            }
            // toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.PASSED);
            log.info("Tool: {}, on Network: {}, running succesfully ", toolDto.getName(), networkDto.getName());
            try {
                SimulationDTO simulationDto = toolExecutionServiceImpl.createSimulation(
                    toolDto,
                    networkDto,
                    uuidGenerated,
                    fileConfigParametersJson,
                    taskSaved,
                    inputFileSavedList,
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
            toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            String errMsg = "Error running tool: " + toolDto.getNum() + " uuid: " + uuid;
            log.error(errMsg);
            throw new RunningToolException(errMsg);
        }
    }

    @Override
    public String t511CharacterizationRun(NetworkDTO networkDto, ToolDTO toolDto, String jsonConfig, MultipartFile[] files)
        throws RunningToolException, Exception {
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();

        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);

        // Create simulationWorkingDir
        // -- ATSIM/WP5/T51/UUID/
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid.toString());
        Files.createDirectories(Paths.get(simulationWorkingDir));

        // Create logs dir if not exists
        // -- ATSIM/WP5/T51/UUID/logs
        String logDirPath = simulationWorkingDir.concat(File.separator).concat(ToolFileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        String logFile = logDirPath.concat(File.separator).concat(ToolFileFormat.LOG_FILE_NAME);
        Path logFilePath = Paths.get(logFile);

        // Create output data dir
        // -- ATSIM/WP5/T51/UUID/output-data
        String outputData = simulationWorkingDir.concat(File.separator).concat(toolSimulationRef.getOutputData());
        Files.createDirectories(Paths.get(outputData));

        // Create restore_files dir
        // -- ATSIM/WP5/T51/UUID/output-data/restore_files
        String restoreFilesDir = outputData.concat(File.separator).concat(T51FileFormat.CHARACTERIZATION_DIR);
        Files.createDirectories(Paths.get(restoreFilesDir));

        // jsonConfig String to jsonFile
        T51CharacterizationInputDTO inputParam;
        ObjectMapper mapper = new ObjectMapper();
        try {
            inputParam = mapper.readValue(jsonConfig, T51CharacterizationInputDTO.class);
        } catch (JsonProcessingException e) {
            String errMsg = "Error converting jsonConfig to T51CharacterizationInputDTO";
            log.error(errMsg, e);
            throw new Exception(errMsg + e.getMessage());
        }

        T51CharacterizationLaunchParamDTO launchParam = new T51CharacterizationLaunchParamDTO();

        List<T51CharacterizationConfigDTO> configs = inputParam.getConfigs();

        for (T51CharacterizationConfigDTO configDto : configs) {
            // Replace Path
            if (configDto.getPath() != null && !"".equals(configDto.getPath())) {
                String pathNew = this.replaceFilePath(configDto.getPath(), simulationWorkingDir);
                configDto.setPath(pathNew);
            } else {
                configDto.setPath("");
            }

            // Replace Path2
            if (configDto.getPath2() != null && !"".equals(configDto.getPath2())) {
                String pathNew = this.replaceFilePath(configDto.getPath2(), simulationWorkingDir);
                configDto.setPath2(pathNew);
            } else {
                configDto.setPath2("");
            }

            // set empty values for optional params
            String emptyValue = "";
            if (configDto.getComponent2Field() == null) {
                configDto.setComponent2Field(emptyValue);
            }

            if (configDto.getVariables2() == null) {
                configDto.setVariables2(emptyValue);
            }

            if (configDto.getComponents2() == null) {
                configDto.setComponents2(new ArrayList<String>());
            }

            if (configDto.getAssestsType() == null) {
                configDto.setAssestsType(emptyValue);
            }
        }

        launchParam.setConfigs(configs);
        launchParam.setOutputDir(outputData.concat(File.separator));

        // -- ATTEST/tools/T51
        String toolWorkingDir = toolSimulationRef.getToolWorkingDir(toolDto);

        // -- save launch_characterization.json file on fileSystem
        String launchJsonFileName = simulationWorkingDir.concat(File.separator).concat(T51FileFormat.CHARACTERIZATION_FILE_CONFIG);
        File fileConfigParametersJson = Paths.get(launchJsonFileName).toFile();
        mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileConfigParametersJson, launchParam);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating launch_characterization.json file for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + e.getMessage());
        }

        List<InputFileDTO> inputFileSavedList = new ArrayList<InputFileDTO>();
        try {
            inputFileSavedList =
                saveInputFileOnDbAndFileSystem(networkDto, toolDto, files, simulationWorkingDir, T51FileFormat.CHARACT_INPUT_CONTENT_TYPE);
        } catch (Exception e) {
            // toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            String errMsg = "Error saving inputFile for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + " " + e.getMessage());
        }

        // Create new Task
        TaskDTO taskSaved;
        try {
            taskSaved = toolExecutionServiceImpl.createTask(toolDto);
        } catch (Exception e) {
            String errMsg = "Error creating new task for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg);
        }

        int esito = runToolWrapper(toolWorkingDir, launchJsonFileName, logFile, T51FileFormat.CHARACTERIZATION_FILE_LAUNCH);
        // check if exit value is 0 (OK) store output file on DB
        List<OutputFileDTO> outputFileDtoSavedList = new ArrayList<OutputFileDTO>();
        if (esito == 0) {
            try {
                outputFileDtoSavedList =
                    this.saveOutputFileOnDB(new File(outputData), networkDto, toolDto, T51FileFormat.DOWNLOAD_FILES_EXTENSION);

                toolExecutionServiceImpl.uploadToolLogFileToDB(logFilePath, taskSaved, false);
            } catch (IOException ioex) {
                String errMsg = "Error saving results for tool: " + toolDto.getName();
                toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
                log.error(errMsg, ioex);
                throw new Exception(errMsg + " " + ioex.getMessage());
            }
            // toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.PASSED);
            log.info("Tool: {}, on Network: {}, running succesfully ", toolDto.getName(), networkDto.getName());
            try {
                SimulationDTO simulationDto = toolExecutionServiceImpl.createSimulation(
                    toolDto,
                    networkDto,
                    uuidGenerated,
                    fileConfigParametersJson,
                    taskSaved,
                    inputFileSavedList,
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
            toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            String errMsg = "Error running tool: " + toolDto.getNum() + " uuid: " + uuid;
            log.error(errMsg);
            throw new RunningToolException(errMsg);
        }
    }

    @Override
    public String t53Run(NetworkDTO networkDto, ToolDTO toolDto, String jsonConfig, MultipartFile[] files)
        throws RunningToolException, Exception {
        String configDir = "Configs";
        String firstPartDir = "first_part";
        String t52PartDir = "t52_part";
        String actionPartDir = T53FileFormat.ACTION_PARTS_OUTPUT_DIR;
        String futureScenDir = "future_scen";
        String resultFromT52Dir = "Result_from_T52";

        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();

        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);

        // Create simulationWorkingDir
        // -- ATSIM/WP5/T53/UUID/
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid.toString());
        Files.createDirectories(Paths.get(simulationWorkingDir));

        // Create input dir if not exists
        // -- ATSIM/WP5/T53/UUID/dataset
        // String inputDirPath =
        // simulationWorkingDir.concat(File.separator).concat(T52FileFormat.INPUT_DIR);
        // Files.createDirectories(Paths.get(inputDirPath));

        // Create logs dir if not exists
        // -- ATSIM/WP5/T53/UUID/logs
        String logDirPath = simulationWorkingDir.concat(File.separator).concat(ToolFileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        String logFile = logDirPath.concat(File.separator).concat(ToolFileFormat.LOG_FILE_NAME);
        Path logFilePath = Paths.get(logFile);

        // Create output-data dir if not exists
        // -- ATSIM/WP5/T53/UUID/output-data
        String outputData = simulationWorkingDir.concat(File.separator).concat(toolSimulationRef.getOutputData());
        Files.createDirectories(Paths.get(outputData));

        // Create config Dir
        // -- ATSIM/WP5/T53/UUID/Configs
        String configDirPath = simulationWorkingDir.concat(File.separator).concat(configDir);
        Files.createDirectories(Paths.get(configDirPath));
        log.debug("configDir : {}  created ", configDirPath);

        //Create firstPath dir if not exists
        String firstPartPath = outputData.concat(File.separator).concat(firstPartDir);
        Files.createDirectories(Paths.get(firstPartPath));
        log.debug("firstPartPath : {}  created ", firstPartPath);

        //Create futureScen dir if not exists
        String futureScenPath = firstPartPath.concat(File.separator).concat(futureScenDir);
        Files.createDirectories(Paths.get(futureScenPath));
        log.debug("futureScenPath : {}  created ", futureScenPath);

        //Create resultFromT52 dir if not exists
        String resultFromT52Path = firstPartPath.concat(File.separator).concat(resultFromT52Dir);
        Files.createDirectories(Paths.get(resultFromT52Path));
        log.debug("resultFromT52Path : {}  created ", resultFromT52Path);

        //Create t52Part dir if not exists
        String t52PartPath = outputData.concat(File.separator).concat(t52PartDir);
        Files.createDirectories(Paths.get(t52PartPath));
        log.debug("t52PartPath : {}  created ", t52PartPath);

        //Create actionsPart dir if not exists
        String actionsPartPath = outputData.concat(File.separator).concat(actionPartDir);
        Files.createDirectories(Paths.get(actionsPartPath));
        log.debug("actionsPartPath : {}  created ", actionsPartPath);

        // jsonConfig String to jsonFile
        T53InputParamDTO inputParam;
        ObjectMapper mapper = new ObjectMapper();
        try {
            inputParam = mapper.readValue(jsonConfig, T53InputParamDTO.class);
        } catch (JsonProcessingException e) {
            String errMsg = "Error converting jsonConfig to T53InputParamDTO";
            log.error(errMsg, e);
            throw e;
        }

        List<T5ConfigDTO> mainConfigList = inputParam.getMainConfig();

        // -- Replace path
        for (int i = 0; i < mainConfigList.size(); i++) {
            T5ConfigDTO configDto = mainConfigList.get(i);
            String path = configDto.getPath();
            String absolutePath = simulationWorkingDir.concat(File.separator).concat(path);
            configDto.setPath(absolutePath);
            mainConfigList.set(i, configDto);
        }

        String assetName = inputParam.getAssetsName();
        Integer nScenarios = inputParam.getnScenarios();
        log.debug("assetName: {} ", assetName);
        log.debug("nScenarios: {} ", nScenarios);

        //Create All_config file
        String allCfgPath = (assetName != null && !assetName.isEmpty())
            ? configDirPath.concat(File.separator).concat(assetName).concat("_").concat(T53FileFormat.CONFIG_FILE_SUFFIX_EXTENSION)
            : configDir.concat(File.separator).concat("_").concat(T53FileFormat.CONFIG_FILE_SUFFIX_EXTENSION);
        log.debug("allCfgPath: {} ", allCfgPath);
        File allCfgFile = Paths.get(allCfgPath).toFile();
        mapper = new ObjectMapper();
        try {
            mapper.writeValue(allCfgFile, mainConfigList);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating file: " + allCfgPath + " for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + e.getMessage());
        }

        //Create launch.json file:

        T53LaunchParamDTO launchParam = new T53LaunchParamDTO();

        T53FirstConfigDTO firstConfig = new T53FirstConfigDTO();
        firstConfig.setPath(allCfgPath);
        firstConfig.setnScenarios(nScenarios);
        firstConfig.setAssetsName(assetName);
        firstConfig.setOutputDir(firstPartPath);

        T53AllConfigDTO t52Config = new T53AllConfigDTO();
        t52Config.setInputDir(futureScenPath);
        t52Config.setOutputDir(t52PartPath);

        T53AllConfigDTO actionsConfig = new T53AllConfigDTO();
        actionsConfig.setInputDir(resultFromT52Path);
        actionsConfig.setOutputDir(actionsPartPath);

        launchParam.setFirstConfig(firstConfig);
        launchParam.setT52Config(t52Config);
        launchParam.setActionsConfig(actionsConfig);

        // -- ATTEST/tools/T53
        String toolWorkingDir = toolSimulationRef.getToolWorkingDir(toolDto);

        // -- save launch.json file on fileSysterm
        String launchJsonFileName = simulationWorkingDir.concat(File.separator).concat(ToolFileFormat.CONFIG_FILE);
        File fileConfigParametersJson = Paths.get(launchJsonFileName).toFile();
        mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileConfigParametersJson, launchParam);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating " + ToolFileFormat.CONFIG_FILE + " file for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + e.getMessage());
        }

        List<InputFileDTO> inputFileSavedList = new ArrayList<InputFileDTO>();
        try {
            inputFileSavedList =
                saveInputFileOnDbAndFileSystem(networkDto, toolDto, files, simulationWorkingDir, T53FileFormat.INPUT_CONTENT_TYPE);
        } catch (Exception e) {
            // toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            String errMsg = "Error saving inputFile for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + " " + e.getMessage());
        }

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
                    this.saveOutputFileOnDB(new File(actionsPartPath), networkDto, toolDto, T53FileFormat.DOWNLOAD_FILES_EXTENSION);

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
                    inputFileSavedList,
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
            toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            String errMsg = "Error running tool: " + toolDto.getNum() + " uuid: " + uuid;
            log.error(errMsg);
            throw new RunningToolException(errMsg);
        }
    }

    private String replaceFilePath(String origFileName, String inputDirPath) {
        return inputDirPath.concat(File.separator).concat(origFileName);
    }

    private int runToolWrapper(String toolWorkingDir, String jsonConfig, String logFile, String launchFile) {
        Process process = RunWrapper.executeBatchFile(toolWorkingDir, jsonConfig, launchFile);
        FileUtils.writeToolLogsToFile(new InputStreamReader(process.getInputStream()), logFile);
        // TODO check if process is terminated
        int exitValue = process.exitValue();
        log.info("Tool:{} END with exit value: {} ", toolWorkingDir, exitValue);
        return exitValue;
    }

    private List<InputFileDTO> saveInputFileOnDbAndFileSystem(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        MultipartFile[] files,
        String inputDir,
        List<String> fileContentTypes
    ) throws Exception {
        log.info("Store all Input files on DB and under tool's input_dir: {}", inputDir);
        List<InputFileDTO> inputFileSaved = new ArrayList<InputFileDTO>();
        // save input file in db and into the file system
        for (MultipartFile mpFile : files) {
            log.debug("Input File: {} , Content Type:{} ", mpFile.getName(), mpFile.getContentType());
            // check the content type
            if (fileContentTypes.contains(mpFile.getContentType())) {
                // -- save file ON DB input_file table

                InputFileDTO fileSaved = inputFileServiceImpl.saveFileForNetwork(mpFile, networkDto, toolDto);
                inputFileSaved.add(fileSaved);
                String inputFileName = inputDir + File.separator + mpFile.getOriginalFilename();

                try (FileOutputStream fos = new FileOutputStream(inputFileName)) {
                    fos.write(mpFile.getBytes());
                    log.debug("Input File: {}, Saved on fileSystem!", inputFileName);
                } catch (Exception e) {
                    String errMsg = "Error saving file: " + inputFileName;
                    log.error(errMsg);
                    throw new Exception(errMsg);
                }
            } else {
                String msg = "InputFile content type not valid  ";
                log.error(msg);
                throw new Exception(msg);
            }
        }

        return inputFileSaved;
    }

    private List<OutputFileDTO> saveOutputFileOnDB(File outputPath, NetworkDTO networkDto, ToolDTO toolDto, List<String> extension)
        throws IOException {
        log.debug("Save tool's output file on DB..");
        List<OutputFileDTO> outputFileDtoSaved = new ArrayList<OutputFileDTO>();

        File[] files = outputPath.listFiles(
            new FilenameFilter() {
                @Override
                public boolean accept(File directory, String fileName) {
                    for (String ext : extension) {
                        if (fileName.endsWith(ext)) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        );

        if (files != null) {
            for (File file : files) {
                MultipartFile multipartFile = null;

                multipartFile =
                    new MockMultipartFile(
                        file.getName(),
                        file.getName(),
                        //Files.probeContentType(file.toPath()),
                        FileUtils.probeContentType(file.toPath()),
                        Files.readAllBytes(file.toPath())
                    );
                outputFileDtoSaved.add(outputFileServiceImpl.saveFileForNetworkAndTool(multipartFile, networkDto, toolDto));
            }
        }
        return outputFileDtoSaved;
    }
}
