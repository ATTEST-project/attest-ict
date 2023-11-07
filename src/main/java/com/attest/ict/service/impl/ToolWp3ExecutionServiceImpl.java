package com.attest.ict.service.impl;

import static com.attest.ict.tools.constants.ToolsConstants.*;

import com.attest.ict.config.ToolsConfiguration;
import com.attest.ict.custom.tools.utils.ToolSimulationReferencies;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.domain.InputFile;
import com.attest.ict.service.CommandExecutorService;
import com.attest.ict.service.InputFileService;
import com.attest.ict.service.TaskService;
import com.attest.ict.service.ToolWp3ExecutionService;
import com.attest.ict.service.dto.InputFileDTO;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.SimulationDTO;
import com.attest.ict.service.dto.TaskDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.custom.ToolConfigParameters;
import com.attest.ict.service.mapper.InputFileMapper;
import com.attest.ict.tools.constants.T31FileFormat;
import com.attest.ict.tools.constants.T32FileFormat;
import com.attest.ict.tools.constants.T33FileFormat;
import com.attest.ict.tools.constants.ToolFileFormat;
import com.attest.ict.tools.parameter.constants.ToolWp3Parameters;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ToolWp3ExecutionServiceImpl implements ToolWp3ExecutionService {

    private final Logger log = LoggerFactory.getLogger(ToolWp3ExecutionServiceImpl.class);

    private String attestToolsDir;

    private String toolsPathSimulation;

    @Autowired
    private InputFileService inputFileServiceImpl;

    @Autowired
    ToolExecutionServiceImpl toolExecutionServiceImpl;

    @Autowired
    private InputFileMapper inputFileMapper;

    @Autowired
    private SimulationServiceImpl simulationServiceImpl;

    @Autowired
    CommandExecutorService commandExecutorServiceImpl;

    @Autowired
    private TaskService taskServiceImpl;

    public ToolWp3ExecutionServiceImpl(ToolsConfiguration toolsConfig) {
        // ATTEST/tools
        this.attestToolsDir = toolsConfig.getPath();
        log.debug("ToolWp3ExecutionServiceImpl() - attestToolsDir {}", attestToolsDir);
        // ATSIM
        this.toolsPathSimulation = toolsConfig.getPathSimulation();
        log.debug("ToolWp3ExecutionServiceImpl() - toolsPathSimulation {}", toolsPathSimulation);
    }

    @Override
    @Transactional
    public Map<String, String> prepareT31WorkingDir(NetworkDTO networkDto, ToolDTO toolDto, MultipartFile[] mpFiles, String jsonConfig)
        throws Exception {
        log.debug("prepareT31WorkingDir() - network:{},  tool: {}, jsonConfig: {}", networkDto, toolDto, jsonConfig);
        Map<String, String> configMap = new HashMap<String, String>();
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();

        // Simulation UUID
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();
        log.info("prepareT31WorkingDir() - uuid: {} ", uuid);
        configMap.put(UUID_KEY, uuid);

        // Tool's installation path
        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        String toolInstallationDir = toolSimulationRef.getToolWorkingDir(toolDto);
        log.info("prepareT31WorkingDir() - tool_installation_dir: {}", toolInstallationDir);
        configMap.put(TOOL_INSTALL_DIR, toolInstallationDir);
        configMap.put(LAUNCH_TOOL_FILE_KEY, ToolFileFormat.LAUNCH_FILE);

        // Create simulationWorkingDir
        // -- ATS/WPX/TXY
        String simulationWorkingDir = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid.toString());
        Files.createDirectories(Paths.get(simulationWorkingDir));
        log.info("prepareT31WorkingDir() - dir: {}, created ", simulationWorkingDir);
        configMap.put(WORKING_DIR_KEY, simulationWorkingDir);

        // Create logs dir if not exists
        String logDirPath = simulationWorkingDir.concat(File.separator).concat(T31FileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        log.info("prepareT31WorkingDir() - dir: {}, created ", logDirPath);

        String logFile = logDirPath.concat(File.separator).concat(T31FileFormat.LOG_FILE_NAME);
        Path logFilePath = Paths.get(logFile);
        log.info("prepareT31WorkingDir() - tool_log_file: {}", logFile);
        configMap.put(LOG_FILENAME_KEY, logFile);

        // Create outputDir
        String outputDir = simulationWorkingDir.concat(File.separator).concat(toolSimulationRef.getOutputData());
        Files.createDirectories(Paths.get(outputDir));
        configMap.put(OUTPUT_DIR_KEY, outputDir);
        log.info("prepareT31WorkingDir() - output_dir: {}, created ", outputDir);

        // --- Write network file  (Network file .m) in tool simulation directory
        InputFile testCasesNetwork = inputFileServiceImpl.findNetworkFileByNetworkId(networkDto.getId());
        String inputNetworkFile = simulationWorkingDir.concat(File.separator).concat(testCasesNetwork.getFileName());
        try (FileOutputStream fos = new FileOutputStream(inputNetworkFile)) {
            fos.write(testCasesNetwork.getData());
            log.debug("prepareT31WorkingDir() - Network_File: {}, Saved on fileSystem!", inputNetworkFile);
        } catch (Exception e) {
            String errMsg = "--- Error saving file: " + inputNetworkFile;
            log.error("prepareT31WorkingDir() - " + errMsg, e);
            throw new Exception(errMsg);
        }

        List<InputFileDTO> inputFileDtoSavedList = new ArrayList<InputFileDTO>();
        // add network file into list of input file saved (need for simulation)
        InputFileDTO inputFileTestCaseNetworkDTO = inputFileMapper.toDto(testCasesNetwork);
        inputFileDtoSavedList.add(inputFileTestCaseNetworkDTO);

        // Read jsonConfig input file
        ToolConfigParameters inputParam;
        ObjectMapper mapper = new ObjectMapper();
        try {
            inputParam = mapper.readValue(jsonConfig, ToolConfigParameters.class);
        } catch (JsonProcessingException e) {
            String errMsg = "--- Error converting jsonConfig to ToolConfigParameters";
            log.error("prepareT31WorkingDir() - " + errMsg, e);
            throw e;
        }

        LinkedHashMap<String, Object> parameters = inputParam.getParameters();
        log.debug("prepareT31WorkingDir() - Parameters from request: {}", parameters.toString());

        // -- Add parameter: case_path: "/ATSIM/WP3/T31/<uuid>/<NetworkFileName>.m,
        parameters.put(ToolWp3Parameters.T31_PARAM_CASE_PATH, inputNetworkFile);
        log.info("prepareT31WorkingDir() - Add parameter: {}, {}", ToolWp3Parameters.T31_PARAM_CASE_PATH, inputNetworkFile);

        // -- Add parameter /ATSIM/WP3/T31/<uuid>/output_data/<NetworkFileName>_outputs.json"
        String testCaseFileName = testCasesNetwork.getFileName();
        int pos = testCaseFileName.indexOf(T31FileFormat.CASE_INPUT_SUFFIX);
        String outputFileName = testCaseFileName.substring(0, pos) + T31FileFormat.OUTPUT_SUFFIX;
        String outputPath = outputDir.concat(File.separator).concat(outputFileName);
        parameters.put(ToolWp3Parameters.T31_PARAM_OUTPUT_PATH, outputPath);
        log.info("prepareT31WorkingDir() - Add parameter: {}, {}", ToolWp3Parameters.T31_PARAM_OUTPUT_PATH, outputPath);

        // -- Add parameter "attest_inputs_path": "/ATSIM/WP3/T31/<uuid>/NetworkFileName>_inputs.json",
        String attestInputJsonFile = testCaseFileName.substring(0, pos) + T31FileFormat.ATTEST_INPUT_SUFFIX;
        String attestInputPath = simulationWorkingDir.concat(File.separator).concat(attestInputJsonFile);
        parameters.put(ToolWp3Parameters.T31_PARAM_ATTEST_INPUTS_PATH, attestInputPath);
        log.info("prepareT31WorkingDir() - Add parameter: {}, {}", ToolWp3Parameters.T31_PARAM_ATTEST_INPUTS_PATH, attestInputPath);

        // -- Modify parameter: EV_data_file_path  "/ATSIM/WP3/T31/<uuid>/<EV_data_file_path>.xlsx
        String simulationEVStoreLoadFileName = (String) parameters.get(ToolWp3Parameters.T31_PARAM_EV_DATA_FILE_PATH);
        String evDataFilePath = "";
        if (simulationEVStoreLoadFileName != null && !simulationEVStoreLoadFileName.isBlank()) {
            evDataFilePath = simulationWorkingDir.concat(File.separator).concat(simulationEVStoreLoadFileName);
        } else {
            // -- if  EV-PV-Storage_Data_for_Simulations.xlsx is missing eg ES_DX_01 case
            parameters.put(ToolWp3Parameters.T31_PARAM_ADD_LOAD_DATA_CASE_NAME, "");
        }
        parameters.put(ToolWp3Parameters.T31_PARAM_EV_DATA_FILE_PATH, evDataFilePath);
        log.info("prepareT31WorkingDir() - Modify parameter: {}, {}", ToolWp3Parameters.T31_PARAM_EV_DATA_FILE_PATH, evDataFilePath);
        // -- Create launch.json file:
        ToolConfigParameters launchParams = new ToolConfigParameters();
        launchParams.setParameters(parameters);

        // -- save launch.json file on fileSystem
        String launchJsonFileName = simulationWorkingDir.concat(File.separator).concat(ToolFileFormat.CONFIG_FILE);
        File fileConfigParametersJson = Paths.get(launchJsonFileName).toFile();
        mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileConfigParametersJson, launchParams);
        } catch (JsonProcessingException e) {
            String errMsg = "--- Error creating " + ToolFileFormat.CONFIG_FILE + "  for tool: " + toolDto.getName();
            log.error("prepareT31WorkingDir() - " + errMsg, e);
            throw new Exception(errMsg + e.getMessage());
        }
        configMap.put(LAUNCH_JSON_FILE_KEY, launchJsonFileName);

        Map<String, String> mapFilesPath = new HashMap<>();
        if (mpFiles != null) {
            for (int i = 0; i < mpFiles.length; i++) {
                if (mpFiles[i].getOriginalFilename().equals(simulationEVStoreLoadFileName)) {
                    mapFilesPath.put(mpFiles[i].getOriginalFilename(), evDataFilePath);
                }
            }
            List<InputFileDTO> auxiliaryFileSaved = this.saveT31InputFileOnDbAndFileSystem(networkDto, toolDto, mpFiles, mapFilesPath);
            inputFileDtoSavedList.addAll(auxiliaryFileSaved);
        }
        // Init new Task
        TaskDTO taskDtoSaved;
        try {
            taskDtoSaved = toolExecutionServiceImpl.createTask(toolDto);
        } catch (Exception e) {
            String errMsg = "--- Error creating new task for tool: " + toolDto.getName();
            log.error("prepareT31WorkingDir() - " + errMsg, e);
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
            String errMsg = "--- Error saving simulation data for  tool: " + toolDto.getNum() + " uuid: " + uuid.toString();
            log.error("prepareT31WorkingDir() - " + errMsg, ex);
        }
        log.info("prepareT31WorkingDir() - return: {}", configMap.toString());
        return configMap;
    }

    @Override
    @Transactional
    public Map<String, String> prepareT32WorkingDir(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        Long profileId,
        MultipartFile[] mpFiles,
        String jsonConfig
    ) throws Exception {
        log.debug("prepareT32WorkingDir() - network:{},  tool: {}, jsonConfig: {}", networkDto, toolDto, jsonConfig);
        Map<String, String> configMap = new HashMap<String, String>();
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();

        // Simulation UUID
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();
        log.info("prepareT32WorkingDir() - uuid: {} ", uuid);
        configMap.put(UUID_KEY, uuid);

        // Tool's installation path
        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        String toolInstallationDir = toolSimulationRef.getToolWorkingDir(toolDto);
        configMap.put(TOOL_INSTALL_DIR, toolInstallationDir);
        configMap.put(LAUNCH_TOOL_FILE_KEY, ToolFileFormat.LAUNCH_FILE);

        //tests/json needed for save network file ( .m )
        String inputNetworkDir = T32FileFormat.TEST_DIR.concat(File.separator).concat("json");

        //tests/excel   add load profile in excel format
        String inputLoadProfileDir = T32FileFormat.TEST_DIR.concat(File.separator).concat("excel");

        //SCOPF_R5/input_data
        // add   _PROF from  T44 HR_Tx_03_2020_new_Zagreb_PROF_update_v2.ods  file is not mandatory
        // add  file: scenario_gen_ods from TSG
        String scopfInputDataDir = T32FileFormat.INPUT_SCOPF_R5_DIR.concat(File.separator).concat("input_data");

        //SCOPF_R5/data_preparation
        //during the execution of the investment model reads and writes into the folder: SCOPF_R5/data_preparation
        String scopfDataPreparationDir = T32FileFormat.INPUT_SCOPF_R5_DIR.concat(File.separator).concat("data_preparation");

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
        log.info("prepareT32WorkingDir() - DIR: {}, created ", logFile);
        configMap.put(LOG_FILENAME_KEY, logFile);

        // Create outputs dir if not exists
        // -- ATSIM/WP3/T32/UUID/outputs
        String outputDirPath = simulationWorkingPath.concat(File.separator).concat(T32FileFormat.OUTPUT_DIR);
        Files.createDirectories(Paths.get(outputDirPath));
        configMap.put(OUTPUT_DIR_KEY, outputDirPath);
        log.info("prepareT32WorkingDir() - DIR: {}, created ", outputDirPath);

        // Create input_dir for network file Dir
        // -- ATSIM/WP3/T32/UUID/inputs/tests/json
        String inputNetworkPath = inputDirPath.concat(File.separator).concat(inputNetworkDir);
        Files.createDirectories(Paths.get(inputNetworkPath));
        log.debug("prepareT32WorkingDir() - DIR: {}  created ", inputNetworkPath);

        //-- ATSIM/WP5/T32/UUID/inputs/tests/excel
        String inputLoadProfileDirPath = inputDirPath.concat(File.separator).concat(inputLoadProfileDir);
        Files.createDirectories(Paths.get(inputLoadProfileDirPath));
        log.debug("prepareT32WorkingDir() - DIR : {}  created ", inputLoadProfileDirPath);

        // Create /ATSIM/WP5/T32/UUID/inputs/SCOPF_R5/input_data   dir if not exists
        String scopfInputDataDirPath = inputDirPath.concat(File.separator).concat(scopfInputDataDir);
        Files.createDirectories(Paths.get(scopfInputDataDirPath));
        log.debug("prepareT32WorkingDir() - DIR: {}  created ", scopfInputDataDirPath);

        //Create //ATSIM/WP5/T32/UUID/SCOPF_R5/data_preparation dir not exists
        String scopfDataPreparationDirPath = inputDirPath.concat(File.separator).concat(scopfDataPreparationDir);
        Files.createDirectories(Paths.get(scopfDataPreparationDirPath));
        log.debug("prepareT32WorkingDir() - DIR: {}  created ", scopfDataPreparationDirPath);

        // Read jsonConfig input file
        ToolConfigParameters inputParam;
        ObjectMapper mapper = new ObjectMapper();
        try {
            inputParam = mapper.readValue(jsonConfig, ToolConfigParameters.class);
        } catch (JsonProcessingException e) {
            String errMsg = "Error converting jsonConfig to ToolConfigParameters";
            log.error("prepareT32WorkingDir() - " + errMsg, e);
            throw e;
        }

        LinkedHashMap<String, Object> parameters = inputParam.getParameters();
        // Tools need's input_dir path and output_dir path
        parameters.put(ToolWp3Parameters.T32_PARAM_INPUT_DIR, inputDirPath);
        parameters.put(ToolWp3Parameters.T32_PARAM_OUTPUT_DIR, outputDirPath);

        // Mapping between File and File System path where file will be saved
        Map<String, String> mapFileNamePath = new HashMap<String, String>();
        mapFileNamePath.put(ToolWp3Parameters.T32_PARAM_XLS_FILE_NAME, inputLoadProfileDirPath); //ATSIM/WP5/T32/UUID/inputs/tests/excel
        mapFileNamePath.put(ToolWp3Parameters.T32_PARAM_ODS_FILE_NAME, scopfInputDataDirPath); //ATSIM/WP5/T32/UUID/inputs/SCOPF_R5/input_data
        mapFileNamePath.put(ToolWp3Parameters.T32_SCENARIO_FILE_NAME, scopfInputDataDirPath); //ATSIM/WP5/T32/UUID/inputs/SCOPF_R5/input_data
        mapFileNamePath.put(ToolWp3Parameters.T32_PARAM_EV_DATA_FILE_NAME, inputDirPath); //ATSIM/WP3/T32/UUID/inputs

        // Save input file for T32 into the data base and  in the file system
        List<InputFileDTO> inputFileDtoSavedList = new ArrayList<InputFileDTO>();
        try {
            inputFileDtoSavedList = saveT32InputFileOnDbAndFileSystem(networkDto, toolDto, mpFiles, mapFileNamePath, parameters);
        } catch (Exception e) {
            String errMsg = "Error saving inputFile for tool: " + toolDto.getName();
            log.error("prepareT32WorkingDir() - " + errMsg, e);
            throw new Exception(errMsg + " " + e.getMessage());
        }

        //Load network file from db
        InputFile networkFile = inputFileServiceImpl.findNetworkFileByNetworkId(networkDto.getId());
        String networkFileName = networkFile.getFileName();
        log.debug("prepareT32WorkingDir() - Network's File: {} ", networkFileName);
        // add network file into list of input file saved (need for simulation)
        InputFileDTO inputFileTestCaseNetworkDTO = inputFileMapper.toDto(networkFile);
        inputFileDtoSavedList.add(inputFileTestCaseNetworkDTO);

        //-- Set test_case parameter = network file name, less extension
        String ext = FileUtils.getFileExtension(networkFileName);
        if (!ext.equals("m")) {
            String errMsg = "Error: network's file invalid file format: " + toolDto.getName();
            log.error("prepareT32WorkingDir() - " + errMsg);
            throw new Exception(errMsg);
        }

        String testCase = FileUtils.getFileLessExtension(networkFileName);
        if (!testCase.isEmpty()) {
            parameters.put(ToolWp3Parameters.T32_PARAM_TEST_CASE, testCase); // less extension
            String inputNetworkFile = inputNetworkPath.concat(File.separator).concat(networkFileName);
            try {
                //save network file on file system
                FileUtils.saveFileOnFs(inputNetworkFile, networkFile.getData());
                log.debug("prepareT32WorkingDir() - NetworkFile: {} saved in the File System FS  ", inputNetworkFile);
            } catch (Exception e) {
                throw new Exception(e);
            }
        }

        //Se parameter: xlsx_file_name (Auxiliary Load and Flexibility Profile Name)
        if (profileId != null) {
            Optional<InputFileDTO> inputProfileFileDto = inputFileServiceImpl.findOne(profileId);
            log.debug("prepareT32WorkingDir() - Selected profile id:" + ": {} ", profileId);
            if (inputProfileFileDto.isPresent()) {
                String xlsx_file_name = inputProfileFileDto.get().getFileName();
                log.info(ToolWp3Parameters.T32_PARAM_XLS_FILE_NAME + ": {} ", xlsx_file_name);
                parameters.put(ToolWp3Parameters.T32_PARAM_XLS_FILE_NAME, FileUtils.getFileLessExtension(xlsx_file_name));
                inputFileDtoSavedList.add(inputFileTestCaseNetworkDTO);
                try {
                    //save load Profile on file system
                    String xlsFileName = inputLoadProfileDirPath.concat(File.separator).concat(xlsx_file_name);
                    FileUtils.saveFileOnFs(xlsFileName, inputProfileFileDto.get().getData());
                    log.debug("prepareT32WorkingDir() - Selected Load Profile: {} saved on FS  ", xlsFileName);
                } catch (Exception e) {
                    throw new Exception(e);
                }
            }
        }
        // If the user hasn't uploaded the ev_pv_store file, it is still necessary to populate the 'EV_data_file_name' parameter by setting it to an empty string
        if (parameters.get(ToolWp3Parameters.T32_PARAM_EV_DATA_FILE_NAME) == null) {
            parameters.put(ToolWp3Parameters.T32_PARAM_EV_DATA_FILE_NAME, "");
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
            log.error("prepareT32WorkingDir() - " + errMsg, e);
            throw new Exception(errMsg + e.getMessage());
        }

        configMap.put(LAUNCH_JSON_FILE_KEY, launchJsonFileName);

        // Init new Task
        TaskDTO taskDtoSaved;
        try {
            taskDtoSaved = toolExecutionServiceImpl.createTask(toolDto);
        } catch (Exception e) {
            String errMsg = "Error creating new task for tool: " + toolDto.getName();
            log.error("prepareT32WorkingDir() - " + errMsg, e);
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
            log.error("prepareT32WorkingDir() - " + errMsg, ex);
        }
        log.debug("prepareT32WorkingDir() - return: {}", configMap.toString());
        return configMap;
    }

    @Override
    @Transactional
    public Map<String, String> prepareT33WorkingDir(NetworkDTO networkDto, ToolDTO toolDto, MultipartFile zipFile, String jsonConfig)
        throws Exception {
        log.debug("prepareT33WorkingDir() - network:{},  tool: {}, jsonConfig: {}", networkDto, toolDto, jsonConfig);
        Map<String, String> configMap = new HashMap<String, String>();
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();

        // Simulation UUID
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();
        log.info("prepareT33WorkingDir() - uuid: {}  ", uuid);
        configMap.put(UUID_KEY, uuid);

        // Tool's installation path
        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        String toolInstallationDir = toolSimulationRef.getToolWorkingDir(toolDto).concat(File.separator);
        log.info("prepareT33WorkingDir() - TOOL_INSTALL_DIR: {} created", toolInstallationDir);
        configMap.put(TOOL_INSTALL_DIR, toolInstallationDir);
        configMap.put(LAUNCH_TOOL_FILE_KEY, ToolFileFormat.LAUNCH_FILE);

        // Create simulationWorkingDir
        // -- ATS/WPX/TXY/uuid
        String simulationWorkingPath = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid.toString());
        Files.createDirectories(Paths.get(simulationWorkingPath));
        log.info("prepareT33WorkingDir() - WORKING DIR: {}, created ", simulationWorkingPath);
        configMap.put(WORKING_DIR_KEY, simulationWorkingPath);

        // Create logs dir if not exists
        String logDirPath = simulationWorkingPath.concat(File.separator).concat(T31FileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        log.info("prepareT33WorkingDir() - DIR: {}, created ", logDirPath);

        String logFile = logDirPath.concat(File.separator).concat(T31FileFormat.LOG_FILE_NAME);
        Path logFilePath = Paths.get(logFile);
        log.info("prepareT33WorkingDir() - DIR: {}, created ", logFile);
        configMap.put(LOG_FILENAME_KEY, logFile);

        //-- Prepare all input file needed by T33 unzip the .zipFile. Root case_dir is included in zipFile
        FileUtils.unzipFile(zipFile, simulationWorkingPath);

        // Read jsonConfig input file
        ToolConfigParameters inputParam;
        ObjectMapper mapper = new ObjectMapper();
        try {
            inputParam = mapper.readValue(jsonConfig, ToolConfigParameters.class);
        } catch (JsonProcessingException e) {
            String errMsg = "Error converting jsonConfig to ToolConfigParameters";
            log.error("prepareT33WorkingDir() - " + errMsg, e);
            throw e;
        }

        LinkedHashMap<String, Object> paramsMap = inputParam.getParameters();
        String caseDirParam = (String) paramsMap.get("case_dir");
        String inputSpecificationFileParam = (String) paramsMap.get("specification_file");

        String testCaseDirPath = simulationWorkingPath.concat(File.separator).concat(caseDirParam);
        File testCasePathFile = new File(testCaseDirPath);
        if (testCasePathFile.isDirectory()) {
            log.info("prepareT33WorkingDir() - CASE DIR: {}  unzipped", caseDirParam);
        } else {
            String errMsg = " CASE_Dir: " + testCaseDirPath + ", not found! ";
            log.error("prepareT33WorkingDir() - Problem preparing working dir for T33: " + errMsg);
            throw new Exception(errMsg);
        }

        File toolSpecificationFile = new File(testCaseDirPath.concat(File.separator).concat(inputSpecificationFileParam));
        if (!toolSpecificationFile.exists()) {
            String errMsg = " Specification File: " + toolSpecificationFile + ", not found! ";
            log.error("prepareT33WorkingDir() - Problem preparing working dir for T33: " + errMsg);
            throw new Exception(errMsg);
        } else {
            log.info("prepareT33WorkingDir() - SPECIFICATION FILE: {} ", toolSpecificationFile);
        }

        //-- Prepare params for launch.json file used for running the tool
        paramsMap.put("case_dir", testCaseDirPath);
        paramsMap.put("specification_file", inputSpecificationFileParam);

        //Set outputs dir
        //ATTEST\tools\<WPX>\<TNUM>\<TEST_CASE_DIR>\Results
        String outputDir = testCaseDirPath.concat(File.separator).concat(T33FileFormat.OUTPUT_RESULTS_DIR);
        configMap.put(OUTPUT_DIR_KEY, outputDir);
        log.info("prepareT33WorkingDir() - OUTPUT_DIR: {}  ", outputDir);

        //Set diagrams dir, containing all natwork diagrams produced by the tool
        String diagramDir = testCaseDirPath.concat(File.separator).concat(T33FileFormat.OUTPUT_DIAGRAM_DIR);
        configMap.put(DIAGRAMS_DIR_KEY, diagramDir);
        log.info("prepareT33WorkingDir() - DIAGRAMS_DIR_KEY: {}  ", diagramDir);

        //Save zipFile containing all inputFile used by the tool into Input_File DB table
        List<InputFileDTO> inputFileDtoSavedList = new ArrayList<InputFileDTO>();
        try {
            InputFileDTO fileSaved = inputFileServiceImpl.saveFileForNetworkAndTool(zipFile, networkDto, toolDto);
            inputFileDtoSavedList.add(fileSaved);
        } catch (Exception e) {
            String errMsg = "Error saving zipFile in InputFile table, for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + " " + e.getMessage());
        }

        ToolConfigParameters launchParams = new ToolConfigParameters();
        launchParams.setParameters(paramsMap);

        // -- save launch.json file on fileSystem
        String launchJsonFileName = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.CONFIG_FILE);
        File fileConfigParametersJson = Paths.get(launchJsonFileName).toFile();

        try {
            mapper.writeValue(fileConfigParametersJson, launchParams);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating " + ToolFileFormat.CONFIG_FILE + "  for tool: " + toolDto.getName();
            log.error("prepareT33WorkingDir() - " + errMsg, e);
            throw new Exception(errMsg + e.getMessage());
        }
        configMap.put(LAUNCH_JSON_FILE_KEY, launchJsonFileName);

        TaskDTO taskDtoSaved;
        try {
            taskDtoSaved = toolExecutionServiceImpl.createTask(toolDto);
        } catch (Exception e) {
            String errMsg = "Error creating new task for tool: " + toolDto.getName();
            log.error("prepareT33WorkingDir() - " + errMsg, e);
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
            log.error("prepareT33WorkingDir() - " + errMsg, ex);
        }
        log.info("prepareT33WorkingDir() return: {}", configMap.toString());
        return configMap;
    }

    /**
     * @param networkDto
     * @param toolDto
     * @param files
     * @param mapFilePath
     * @param parameters
     * @return the inputFileSaved list containing the saved input files.
     * @throws Exception
     */
    private List<InputFileDTO> saveT32InputFileOnDbAndFileSystem(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        MultipartFile[] files,
        Map<String, String> mapFilePath,
        LinkedHashMap<String, Object> parameters
    ) throws Exception {
        log.debug("saveT32InputFileOnDbAndFileSystem() - Network: {}, Tool:{}  ", networkDto, toolDto);
        List<InputFileDTO> inputFileSaved = new ArrayList<InputFileDTO>();

        for (MultipartFile mpFile : files) {
            String originalFileName = mpFile.getOriginalFilename();
            String name = mpFile.getName();
            log.debug("saveT32InputFileOnDbAndFileSystem() - OriginalFileName: {}", mpFile.getOriginalFilename());

            //scenario_gen.ods, must be saved into the directory:  <working_Dir>/SCOPF_R5/input_data
            String inputFileName = "";
            if (originalFileName.equals(ToolWp3Parameters.T32_SCENARIO_FILE_NAME)) {
                log.info(ToolWp3Parameters.T32_SCENARIO_FILE_NAME);
                String targetPath = mapFilePath.get(ToolWp3Parameters.T32_SCENARIO_FILE_NAME);
                inputFileName = targetPath + File.separator + originalFileName;
                log.info("saveT32InputFileOnDbAndFileSystem() - InputFileName to save: {} ", inputFileName);
            }

            //_PROF.ods file must be saved into the directory:  <working_Dir>/SCOPF_R5/input_data
            String odsFileParam = (String) parameters.get(ToolWp3Parameters.T32_PARAM_ODS_FILE_NAME); //_PROF.ods
            if (originalFileName.contains(odsFileParam)) {
                log.info("saveT32InputFileOnDbAndFileSystem() - ods_file_name parameter: {} ", odsFileParam);
                String targetPath = mapFilePath.get(ToolWp3Parameters.T32_PARAM_ODS_FILE_NAME);
                //inputFileName = scopfInputDataDirPath + File.separator + originalFileName;
                inputFileName = targetPath + File.separator + originalFileName;
                log.info("saveT32InputFileOnDbAndFileSystem() - InputFileName to save: {} ", inputFileName);
            }

            //LoadP and Load Q must be saved  into <working_Dir>/test/excel
            String xlsFileName = (String) parameters.get(ToolWp3Parameters.T32_PARAM_XLS_FILE_NAME); // loadProfile P and Q .xlsx
            if (xlsFileName != null && !xlsFileName.isBlank() && originalFileName.contains(xlsFileName)) {
                log.info("saveT32InputFileOnDbAndFileSystem() - xlsx_file_name parameter: {} ", xlsFileName);
                String targetPath = mapFilePath.get(ToolWp3Parameters.T32_PARAM_XLS_FILE_NAME);
                inputFileName = targetPath + File.separator + originalFileName;
                parameters.put(ToolWp3Parameters.T32_PARAM_XLS_FILE_NAME, FileUtils.getFileLessExtension(originalFileName));
                log.info("saveT32InputFileOnDbAndFileSystem() - InputFileName to save: {} ", inputFileName);
            }

            String evPvFileName = (String) parameters.get(ToolWp3Parameters.T32_PARAM_EV_DATA_FILE_NAME); // simulation
            if (evPvFileName != null && !evPvFileName.isBlank() && originalFileName.equals(evPvFileName)) {
                log.info("EV_data_file_name parameter: {} ", evPvFileName);
                String targetPath = mapFilePath.get(ToolWp3Parameters.T32_PARAM_EV_DATA_FILE_NAME);
                inputFileName = targetPath + File.separator + originalFileName;
                parameters.put(ToolWp3Parameters.T32_PARAM_EV_DATA_FILE_NAME, originalFileName);
                log.info("saveT32InputFileOnDbAndFileSystem() - InputFileName to save: {} ", inputFileName);
            }

            log.debug("saveT32InputFileOnDbAndFileSystem() - Ready to save InputFileName: {} ", inputFileName);
            try (FileOutputStream fos = new FileOutputStream(inputFileName)) {
                fos.write(mpFile.getBytes());
                log.info(
                    "saveT32InputFileOnDbAndFileSystem() - The file: {}, has been successfully saved in the FileSystem",
                    inputFileName
                );
            } catch (Exception e) {
                String errMsg = "Error saving file: " + inputFileName + ", in the FileSystem";
                log.error("saveT32InputFileOnDbAndFileSystem() - " + errMsg);
                e.printStackTrace();
                throw new Exception(errMsg);
            }

            // -- save input file selected by user into the  input_file table
            InputFileDTO fileSaved = inputFileServiceImpl.saveFileForNetworkAndTool(mpFile, networkDto, toolDto);
            inputFileSaved.add(fileSaved);
        }

        return inputFileSaved;
    }

    /**
     * Saving input files both in a database table (input_file) and on the file system
     *
     * @param networkDto
     * @param toolDto
     * @param files input files list
     * @param mapFilesPath map <originalFileName, path>  path correspond to the absolute path where saving file on file system
     * @return the inputFileSaved list containing the saved input files.
     */
    private List<InputFileDTO> saveT31InputFileOnDbAndFileSystem(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        MultipartFile[] files,
        Map<String, String> mapFilesPath
    ) throws Exception {
        log.debug("saveT31InputFileOnDbAndFileSystem() - network: {}, tool:{}  ", networkDto, toolDto);
        List<InputFileDTO> inputFileSaved = new ArrayList<InputFileDTO>();
        for (MultipartFile mpFile : files) {
            String originalFileName = mpFile.getOriginalFilename();
            log.debug("saveT31InputFileOnDbAndFileSystem() - OriginalFileName: {}", mpFile.getOriginalFilename());

            // -- save input file selected by user into the  input_file table
            InputFileDTO fileSaved = inputFileServiceImpl.saveFileForNetworkAndTool(mpFile, networkDto, toolDto);
            inputFileSaved.add(fileSaved);
            log.info("saveT31InputFileOnDbAndFileSystem() - Input File: {} saved in DB", mpFile.getName());

            // save file on  File System
            String filePath = mapFilesPath.get(originalFileName);
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(mpFile.getBytes());
                log.info("saveT31InputFileOnDbAndFileSystem() - Input File: {}, Saved on fileSystem!", filePath);
            } catch (Exception e) {
                String errMsg = "Error saving file: " + filePath;
                log.error("saveT31InputFileOnDbAndFileSystem() - " + errMsg, e);
                throw new Exception(errMsg);
            }
        }
        return inputFileSaved;
    }
}
