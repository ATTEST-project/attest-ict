package com.attest.ict.service.impl;

import static com.attest.ict.tools.constants.ToolsConstants.*;

import com.attest.ict.config.ToolsConfiguration;
import com.attest.ict.custom.tools.utils.ToolSimulationReferencies;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.custom.utils.MimeUtils;
import com.attest.ict.helper.csv.reader.CsvFileReader;
import com.attest.ict.helper.excel.reader.ExcelReader;
import com.attest.ict.service.InputFileService;
import com.attest.ict.service.OutputFileService;
import com.attest.ict.service.TaskService;
import com.attest.ict.service.ToolWp5ExecutionService;
import com.attest.ict.service.dto.InputFileDTO;
import com.attest.ict.service.dto.NetworkDTO;
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
import com.attest.ict.tools.constants.T51FileFormat;
import com.attest.ict.tools.constants.T52FileFormat;
import com.attest.ict.tools.constants.T53FileFormat;
import com.attest.ict.tools.constants.ToolFileFormat;
import com.attest.ict.tools.exception.RunningToolException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ToolWp5ExecutionServiceImpl implements ToolWp5ExecutionService {

    private final Logger log = LoggerFactory.getLogger(ToolWp5ExecutionServiceImpl.class);

    @Autowired
    ToolExecutionServiceImpl toolExecutionServiceImpl;

    @Autowired
    private InputFileService inputFileServiceImpl;

    @Autowired
    private OutputFileService outputFileServiceImpl;

    private String attestToolsDir;

    private String toolsPathSimulation;

    @Autowired
    TaskService taskServiceImpl;

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
        int sheetIndex = 0;
        //String contentType = mpFile.getContentType();
        String contentType = MimeUtils.detect(mpFile);
        if (contentType.equals(FileUtils.CONTENT_TYPE.get("xlsx")) || contentType.equals(FileUtils.CONTENT_TYPE.get("xls"))) {
            ExcelReader readerExcel = new ExcelReader(mpFile);
            return readerExcel.parseHeaderBySheetIndex(sheetIndex);
        }
        //default CSV
        CsvFileReader readerCsv = new CsvFileReader();
        return readerCsv.parseCsvHeaderMultiPartFile(mpFile);
    }

    @Override
    public Map<String, String> prepareT511CharacterizationWorkingDir(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        String jsonConfig,
        MultipartFile[] files
    ) throws RunningToolException, Exception {
        log.info("Enter prepareT511CharacterizationWorkingDir() ");
        Map<String, String> configMap = new HashMap<String, String>();
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();
        log.info("UUID: {} ", uuid);
        configMap.put(UUID_KEY, uuid);

        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        // Tool's installation path
        String toolInstallationDir = toolSimulationRef.getToolWorkingDir(toolDto);
        log.info("TOOL_INSTALL_DIR: {} ", toolInstallationDir);
        configMap.put(TOOL_INSTALL_DIR, toolInstallationDir);
        configMap.put(LAUNCH_TOOL_FILE_KEY, T51FileFormat.CHARACTERIZATION_FILE_LAUNCH);

        // Create simulationWorkingDir
        // -- ATSIM/WP5/<ToolNum>/<UUID>/
        String simulationWorkingPath = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);
        Files.createDirectories(Paths.get(simulationWorkingPath));
        log.info("DIR: {}, created ", simulationWorkingPath);
        configMap.put(WORKING_DIR_KEY, simulationWorkingPath);

        // Create logs dir if not exists
        // -- ATSIM/WP5/<ToolNum>/<UUID>/logs
        // Create logs dir if not exists
        String logDirPath = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        String logFile = logDirPath.concat(File.separator).concat(ToolFileFormat.LOG_FILE_NAME);
        log.info("DIR:  {}, created ", logDirPath);
        configMap.put(LOG_FILENAME_KEY, logFile);

        // Create outputs dir if not exists
        // -- ATSIM/WP5/<ToolNum>/<UUID>/output_data
        String outputDirPath = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.OUTPUT_DIR);
        Files.createDirectories(Paths.get(outputDirPath));
        log.info("DIR:  {}, created ", outputDirPath);
        configMap.put(OUTPUT_DIR_KEY, outputDirPath);

        // Create restore_files dir
        // -- ATSIM/WP5/T51/UUID/output-data/restore_files
        String restoreFilesDir = outputDirPath.concat(File.separator).concat(T51FileFormat.CHARACTERIZATION_DIR);
        Files.createDirectories(Paths.get(restoreFilesDir));
        log.info("DIR:  {}, created ", outputDirPath);
        configMap.put(RESTORE_FILE_DIR_KEY, restoreFilesDir);

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
                String pathNew = this.replaceFilePath(configDto.getPath(), simulationWorkingPath);
                configDto.setPath(pathNew);
            } else {
                configDto.setPath("");
            }

            // Replace Path2
            if (configDto.getPath2() != null && !"".equals(configDto.getPath2())) {
                String pathNew = this.replaceFilePath(configDto.getPath2(), simulationWorkingPath);
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
        launchParam.setOutputDir(outputDirPath.concat(File.separator));

        // -- save launch_characterization.json file on fileSystem
        String configJsonName = simulationWorkingPath.concat(File.separator).concat(T51FileFormat.CHARACTERIZATION_FILE_CONFIG);
        File fileConfigParametersJson = Paths.get(configJsonName).toFile();
        mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileConfigParametersJson, launchParam);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating " + T51FileFormat.CHARACTERIZATION_FILE_CONFIG + " file for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + e.getMessage());
        }

        configMap.put(LAUNCH_JSON_FILE_KEY, configJsonName);

        List<InputFileDTO> inputFileDtoSavedList = new ArrayList<InputFileDTO>();
        try {
            inputFileDtoSavedList =
                toolExecutionServiceImpl.saveInputFileOnDbAndFileSystem(
                    networkDto,
                    toolDto,
                    files,
                    simulationWorkingPath,
                    T51FileFormat.CHARACT_INPUT_CONTENT_TYPE
                );
        } catch (Exception e) {
            // toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            String errMsg = "Error saving inputFile for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + " " + e.getMessage());
        }

        // Init new Task
        TaskDTO taskDtoSaved;
        try {
            taskDtoSaved = toolExecutionServiceImpl.createTask(toolDto);
        } catch (Exception e) {
            String errMsg = "Error creating new task for tool: " + toolDto.getName();
            log.error(errMsg, e);
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
                toolExecutionServiceImpl.getInputFilesDtoSetFromList(inputFileDtoSavedList),
                null
            );
            taskDtoSaved.setSimulation(simulationDto);
            taskServiceImpl.partialUpdate(taskDtoSaved);
        } catch (Exception ex) {
            String errMsg = "Error saving simulation data for  tool: " + toolDto.getNum() + " uuid: " + uuid.toString();
            log.error(errMsg, ex);
        }
        log.info("Exit prepareT511CharacterizationWorkingDir() ");
        return configMap;
    }

    @Override
    @Transactional
    public Map<String, String> prepareT512MonitoringWorkingDir(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        String jsonConfig,
        MultipartFile[] files
    ) throws Exception {
        log.info("Enter prepareT512MonitoringWorkingDir() ");
        Map<String, String> configMap = new HashMap<String, String>();
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();
        log.info("UUID: {} ", uuid);
        configMap.put(UUID_KEY, uuid);

        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        // Tool's installation path
        String toolInstallationDir = toolSimulationRef.getToolWorkingDir(toolDto);
        log.info("TOOL_INSTALL_DIR: {} ", toolInstallationDir);
        configMap.put(TOOL_INSTALL_DIR, toolInstallationDir);
        configMap.put(LAUNCH_TOOL_FILE_KEY, T51FileFormat.MONITORING_FILE_LAUNCH);

        // Create simulationWorkingDir
        // -- ATSIM/WP5/<ToolNum>/<UUID>/
        String simulationWorkingPath = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);
        Files.createDirectories(Paths.get(simulationWorkingPath));
        log.info("DIR: {}, created ", simulationWorkingPath);
        configMap.put(WORKING_DIR_KEY, simulationWorkingPath);

        // Create logs dir if not exists
        // -- ATSIM/WP5/<ToolNum>/<UUID>/logs
        // Create logs dir if not exists
        String logDirPath = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        String logFile = logDirPath.concat(File.separator).concat(ToolFileFormat.LOG_FILE_NAME);
        log.info("DIR:  {}, created ", logDirPath);
        configMap.put(LOG_FILENAME_KEY, logFile);

        // Create outputs dir if not exists
        // -- ATSIM/WP5/<ToolNum>/<UUID>/output_data
        String outputDirPath = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.OUTPUT_DIR);
        Files.createDirectories(Paths.get(outputDirPath));
        log.info("DIR:  {}, created ", outputDirPath);
        configMap.put(OUTPUT_DIR_KEY, outputDirPath);

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
            launchParam.setModelPath1(this.replaceFilePath(inputParam.getModelPath1(), simulationWorkingPath));
        }
        if (inputParam.getModelPath2() != null && !inputParam.getModelPath2().isEmpty()) {
            launchParam.setModelPath2(this.replaceFilePath(inputParam.getModelPath2(), simulationWorkingPath));
        }
        if (inputParam.getFilePath2() != null && !inputParam.getFilePath2().isEmpty()) {
            launchParam.setFilePath2(this.replaceFilePath(inputParam.getFilePath2(), simulationWorkingPath));
        }

        // set Item1, Item2, Item 3
        launchParam.setItem1(inputParam.getItem1());
        launchParam.setItem2(inputParam.getItem2());
        launchParam.setItem3(inputParam.getItem3());
        launchParam.setOutputDir(outputDirPath.concat(File.separator));

        // -- save launch_monitoring.json file on fileSystem
        String configJsonName = simulationWorkingPath.concat(File.separator).concat(T51FileFormat.MONITORING_FILE_CONFIG);
        File fileConfigParametersJson = Paths.get(configJsonName).toFile();
        mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileConfigParametersJson, launchParam);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating " + T51FileFormat.MONITORING_FILE_CONFIG + " file for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + e.getMessage());
        }
        configMap.put(LAUNCH_JSON_FILE_KEY, configJsonName);

        List<InputFileDTO> inputFileDtoSavedList = new ArrayList<InputFileDTO>();
        try {
            inputFileDtoSavedList =
                toolExecutionServiceImpl.saveInputFileOnDbAndFileSystem(
                    networkDto,
                    toolDto,
                    files,
                    simulationWorkingPath,
                    T51FileFormat.MONITORING_INPUT_CONTENT_TYPE
                );
        } catch (Exception e) {
            String errMsg = "Error saving input files for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + " " + e.getMessage());
        }

        // Init new Task
        TaskDTO taskDtoSaved;
        try {
            taskDtoSaved = toolExecutionServiceImpl.createTask(toolDto);
        } catch (Exception e) {
            String errMsg = "Error creating new task for tool: " + toolDto.getName();
            log.error(errMsg, e);
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
                toolExecutionServiceImpl.getInputFilesDtoSetFromList(inputFileDtoSavedList),
                null
            );
            taskDtoSaved.setSimulation(simulationDto);
            taskServiceImpl.partialUpdate(taskDtoSaved);
        } catch (Exception ex) {
            String errMsg = "Error saving simulation data for  tool: " + toolDto.getNum() + " uuid: " + uuid.toString();
            log.error(errMsg, ex);
        }

        log.info("Exit prepareT512MonitoringWorkingDir return: {}", configMap.toString());
        return configMap;
    }

    @Override
    @Transactional
    public Map<String, String> prepareT52WorkingDir(NetworkDTO networkDto, ToolDTO toolDto, String jsonConfig, MultipartFile[] files)
        throws RunningToolException, Exception {
        log.info("Enter prepareT52WorkingDir() ");
        Map<String, String> configMap = new HashMap<String, String>();
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();
        log.info("UUID: {} ", uuid);
        configMap.put(UUID_KEY, uuid);

        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        // Tool's installation path
        String toolInstallationDir = toolSimulationRef.getToolWorkingDir(toolDto);
        log.info("TOOL_INSTALL_DIR: {} ", toolInstallationDir);
        configMap.put(TOOL_INSTALL_DIR, toolInstallationDir);
        configMap.put(LAUNCH_TOOL_FILE_KEY, T52FileFormat.LAUNCH_FILE);

        // Create simulationWorkingDir
        // -- ATSIM/WP5/<ToolNum>/<UUID>/
        String simulationWorkingPath = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);
        Files.createDirectories(Paths.get(simulationWorkingPath));
        log.info("DIR: {}, created ", simulationWorkingPath);
        configMap.put(WORKING_DIR_KEY, simulationWorkingPath);

        // Create logs dir if not exists
        // -- ATSIM/WP5/<ToolNum>/<UUID>/logs
        // Create logs dir if not exists
        String logDirPath = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        String logFile = logDirPath.concat(File.separator).concat(ToolFileFormat.LOG_FILE_NAME);
        log.info("DIR:  {}, created ", logDirPath);
        configMap.put(LOG_FILENAME_KEY, logFile);

        // Create outputs dir if not exists
        // -- ATSIM/WP5/<ToolNum>/<UUID>/output_data
        String outputDirPath = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.OUTPUT_DIR);
        Files.createDirectories(Paths.get(outputDirPath));
        log.info("DIR:  {}, created ", outputDirPath);
        configMap.put(OUTPUT_DIR_KEY, outputDirPath);

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
            String absolutePath = simulationWorkingPath.concat(File.separator).concat(path);
            configDto.setPath(absolutePath);
            mainConfigList.set(i, configDto);
        }

        T52LaunchParamDTO launchParam = new T52LaunchParamDTO();
        launchParam.setOutputDir(outputDirPath.concat(File.separator));
        launchParam.setMainConfig(mainConfigList);

        // -- Replace path
        CoordinatesConfigDTO coordinateConfig = inputParam.getCoordinatesConfig();
        if (coordinateConfig != null) {
            String coordinatePath = coordinateConfig.getCoordinatesFilePath();
            String coordinateAbsolutePath = simulationWorkingPath.concat(File.separator).concat(coordinatePath);
            coordinateConfig.setCoordinatesFilePath(coordinateAbsolutePath);
            launchParam.setCoordinatesConfig(coordinateConfig);
        }

        // -- save launch.json file on fileSystem
        String configJsonName = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.CONFIG_FILE);
        File fileConfigParametersJson = Paths.get(configJsonName).toFile();
        mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileConfigParametersJson, launchParam);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating " + ToolFileFormat.CONFIG_FILE + " file for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + e.getMessage());
        }
        configMap.put(LAUNCH_JSON_FILE_KEY, configJsonName);

        List<InputFileDTO> inputFileDtoSavedList = new ArrayList<InputFileDTO>();
        try {
            inputFileDtoSavedList =
                toolExecutionServiceImpl.saveInputFileOnDbAndFileSystem(
                    networkDto,
                    toolDto,
                    files,
                    simulationWorkingPath,
                    T52FileFormat.INPUT_CONTENT_TYPE
                );
        } catch (Exception e) {
            String errMsg = "Error saving inputFile for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + " " + e.getMessage());
        }

        // Init new Task
        TaskDTO taskDtoSaved;
        try {
            taskDtoSaved = toolExecutionServiceImpl.createTask(toolDto);
        } catch (Exception e) {
            String errMsg = "Error creating new task for tool: " + toolDto.getName();
            log.error(errMsg, e);
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
                toolExecutionServiceImpl.getInputFilesDtoSetFromList(inputFileDtoSavedList),
                null
            );
            taskDtoSaved.setSimulation(simulationDto);
            taskServiceImpl.partialUpdate(taskDtoSaved);
        } catch (Exception ex) {
            String errMsg = "Error saving simulation data for  tool: " + toolDto.getNum() + " uuid: " + uuid.toString();
            log.error(errMsg, ex);
        }

        log.info("Exit prepareT52WorkingDir return: {}", configMap.toString());
        return configMap;
    }

    @Override
    @Transactional
    public Map<String, String> prepareT53WorkingDir(NetworkDTO networkDto, ToolDTO toolDto, String jsonConfig, MultipartFile[] files)
        throws RunningToolException, Exception {
        log.info("Enter prepareT53WorkingDir() ");
        String configDir = "Configs";
        String firstPartDir = "first_part";
        String t52PartDir = "t52_part";
        String actionPartDir = T53FileFormat.ACTION_PARTS_OUTPUT_DIR;
        String futureScenDir = "future_scen";
        String resultFromT52Dir = "Result_from_T52";

        Map<String, String> configMap = new HashMap<String, String>();
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();
        log.info("UUID: {} ", uuid);
        configMap.put(UUID_KEY, uuid);

        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        // Tool's installation path
        String toolInstallationDir = toolSimulationRef.getToolWorkingDir(toolDto);
        log.info("TOOL_INSTALL_DIR: {} ", toolInstallationDir);
        configMap.put(TOOL_INSTALL_DIR, toolInstallationDir);
        configMap.put(LAUNCH_TOOL_FILE_KEY, ToolFileFormat.LAUNCH_FILE);

        // Create simulationWorkingDir
        // -- ATSIM/WP5/<ToolNum>/<UUID>/
        String simulationWorkingPath = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);
        Files.createDirectories(Paths.get(simulationWorkingPath));
        log.info("DIR: {}, created ", simulationWorkingPath);
        configMap.put(WORKING_DIR_KEY, simulationWorkingPath);

        // Create logs dir if not exists
        // -- ATSIM/WP5/<ToolNum>/<UUID>/logs
        // Create logs dir if not exists
        String logDirPath = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        String logFile = logDirPath.concat(File.separator).concat(ToolFileFormat.LOG_FILE_NAME);
        log.info("DIR: {}, created ", logDirPath);
        configMap.put(LOG_FILENAME_KEY, logFile);

        // Create outputs dir if not exists
        // -- ATSIM/WP5/<ToolNum>/<UUID>/output_data
        String outputDirPath = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.OUTPUT_DIR);
        Files.createDirectories(Paths.get(outputDirPath));
        log.info("DIR: {}, created ", outputDirPath);

        // Create config Dir
        // -- ATSIM/WP5/T53/UUID/Configs
        String configDirPath = simulationWorkingPath.concat(File.separator).concat(configDir);
        Files.createDirectories(Paths.get(configDirPath));
        log.debug("DIR: {}  created ", configDirPath);

        //Create firstPath dir if not exists
        String firstPartPath = outputDirPath.concat(File.separator).concat(firstPartDir);
        Files.createDirectories(Paths.get(firstPartPath));
        log.debug("DIR: {}  created ", firstPartPath);

        //Create futureScen dir if not exists
        String futureScenPath = firstPartPath.concat(File.separator).concat(futureScenDir);
        Files.createDirectories(Paths.get(futureScenPath));
        log.debug("DIR: {}  created ", futureScenPath);

        //Create resultFromT52 dir if not exists
        String resultFromT52Path = firstPartPath.concat(File.separator).concat(resultFromT52Dir);
        Files.createDirectories(Paths.get(resultFromT52Path));
        log.debug("DIR: {}  created ", resultFromT52Path);

        //Create t52Part dir if not exists
        String t52PartPath = outputDirPath.concat(File.separator).concat(t52PartDir);
        Files.createDirectories(Paths.get(t52PartPath));
        log.debug("DIR: {}  created ", t52PartPath);

        //Create actionsPart dir if not exists
        String actionsPartPath = outputDirPath.concat(File.separator).concat(actionPartDir);
        Files.createDirectories(Paths.get(actionsPartPath));
        log.debug("DIR: {}  created ", actionsPartPath);
        // T53 output file are stored under C:\ATSIM\WP5\T53\<uuid>\output_data\actions_part
        configMap.put(OUTPUT_DIR_KEY, actionsPartPath);

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
            String absolutePath = simulationWorkingPath.concat(File.separator).concat(path);
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

        // -- save launch.json file on fileSysterm
        String configJsonName = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.CONFIG_FILE);
        File fileConfigParametersJson = Paths.get(configJsonName).toFile();
        mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileConfigParametersJson, launchParam);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating " + ToolFileFormat.CONFIG_FILE + " file for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + e.getMessage());
        }

        configMap.put(LAUNCH_JSON_FILE_KEY, configJsonName);

        List<InputFileDTO> inputFileDtoSavedList = new ArrayList<InputFileDTO>();
        try {
            inputFileDtoSavedList =
                toolExecutionServiceImpl.saveInputFileOnDbAndFileSystem(
                    networkDto,
                    toolDto,
                    files,
                    simulationWorkingPath,
                    T53FileFormat.INPUT_CONTENT_TYPE
                );
        } catch (Exception e) {
            String errMsg = "Error saving inputFile for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + " " + e.getMessage());
        }

        // Init new Task
        TaskDTO taskDtoSaved;
        try {
            taskDtoSaved = toolExecutionServiceImpl.createTask(toolDto);
        } catch (Exception e) {
            String errMsg = "Error creating new task for tool: " + toolDto.getName();
            log.error(errMsg, e);
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
                toolExecutionServiceImpl.getInputFilesDtoSetFromList(inputFileDtoSavedList),
                null
            );
            taskDtoSaved.setSimulation(simulationDto);
            taskServiceImpl.partialUpdate(taskDtoSaved);
        } catch (Exception ex) {
            String errMsg = "Error saving simulation data for  tool: " + toolDto.getNum() + " uuid: " + uuid.toString();
            log.error(errMsg, ex);
        }

        log.info("Exit prepareT53WorkingDir return: {}", configMap.toString());
        return configMap;
    }

    //-- Private Methods

    private String replaceFilePath(String origFileName, String inputDirPath) {
        return inputDirPath.concat(File.separator).concat(origFileName);
    }
}
