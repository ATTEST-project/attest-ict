package com.attest.ict.service.impl;

import static com.attest.ict.tools.constants.ToolsConstants.*;

import com.attest.ict.config.ToolsConfiguration;
import com.attest.ict.custom.tools.utils.ToolSimulationReferencies;
import com.attest.ict.custom.utils.MimeUtils;
import com.attest.ict.helper.TaskStatus;
import com.attest.ict.service.*;
import com.attest.ict.service.dto.*;
import com.attest.ict.service.dto.custom.T26InputParamDTO;
import com.attest.ict.service.dto.custom.T26LaunchParamDTO;
import com.attest.ict.tools.constants.ToolFileFormat;
import com.attest.ict.tools.constants.WP2FileFormat;
import com.attest.ict.tools.exception.RunningToolException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ToolWp2ExecutionServiceImpl implements ToolWp2ExecutionService {

    private final Logger log = LoggerFactory.getLogger(ToolWp2ExecutionServiceImpl.class);
    private final String attestToolsDir;
    private final String toolsPathSimulation;

    @Autowired
    private InputFileService inputFileServiceImpl;

    @Autowired
    ToolExecutionServiceImpl toolExecutionServiceImpl;

    @Autowired
    CommandExecutorService commandExecutorServiceImpl;

    @Autowired
    private SimulationServiceImpl simulationServiceImpl;

    @Autowired
    private TaskService taskServiceImpl;

    public ToolWp2ExecutionServiceImpl(ToolsConfiguration toolsConfig) {
        // ATTEST/tools
        this.attestToolsDir = toolsConfig.getPath();
        log.debug("attestToolsDir {}", attestToolsDir);
        // ATSIM
        this.toolsPathSimulation = toolsConfig.getPathSimulation();
        log.debug("toolsPathSimulation {}", toolsPathSimulation);
    }

    @Override
    @Transactional
    public Map<String, String> prepareT25WorkingDir(NetworkDTO networkDto, ToolDTO toolDto, MultipartFile[] mpFiles, String jsonConfig)
        throws Exception {
        log.info("Enter prepareT25WorkingDir ");
        Map<String, String> configMap = new HashMap<String, String>();
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();
        configMap.put(UUID_KEY, uuid);

        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        // Tool's installation path
        String toolInstallationDir = toolSimulationRef.getToolWorkingDir(toolDto);
        configMap.put(TOOL_INSTALL_DIR, toolInstallationDir);
        configMap.put(LAUNCH_TOOL_FILE_KEY, ToolFileFormat.LAUNCH_FILE);

        // Create simulationWorkingDir
        // -- ATSIM/WP2/T251/UUID/
        String simulationWorkingPath = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);
        Files.createDirectories(Paths.get(simulationWorkingPath));
        log.info("DIR: {}, created ", simulationWorkingPath);
        configMap.put(WORKING_DIR_KEY, simulationWorkingPath);

        // Create logs dir if not exists
        // -- ATSIM/WP2/T251/UUID/logs
        // Create logs dir if not exists
        String logDirPath = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        String logFile = logDirPath.concat(File.separator).concat(ToolFileFormat.LOG_FILE_NAME);
        log.info("DIR:  {}, created ", logDirPath);
        configMap.put(LOG_FILENAME_KEY, logFile);

        //Create input_data dir if not exists
        // -- ATSIM/WP2/T251/UUID/input_data
        String inputDirPath = simulationWorkingPath.concat(File.separator).concat(WP2FileFormat.INPUT_DIR);
        Files.createDirectories(Paths.get(inputDirPath));
        log.info("DIR:  {}, created ", inputDirPath);
        configMap.put(INPUT_DIR_KEY, inputDirPath);

        // Create outputs dir if not exists
        // -- ATSIM/WP2/T251/UUID/output_data
        String outputDirPath = simulationWorkingPath.concat(File.separator).concat(WP2FileFormat.OUTPUT_DIR);
        Files.createDirectories(Paths.get(outputDirPath));
        log.info("DIR:  {}, created ", outputDirPath);
        configMap.put(OUTPUT_DIR_KEY, outputDirPath);

        // Read input  file jsonConfig
        Map<String, String> paramHashMap;
        ObjectMapper mapper = new ObjectMapper();
        try {
            paramHashMap = mapper.readValue(jsonConfig, HashMap.class);
        } catch (JsonProcessingException e) {
            String errMsg = "Error converting jsonConfig into ToolConfigParameters";
            log.error(errMsg, e);
            throw e;
        }

        updateJsonParamsMap(paramHashMap, inputDirPath);
        // Tools need's output_dir path
        paramHashMap.put("output_dir", outputDirPath);

        // -- save launch.json file on fileSystem
        String launchJsonFileName = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.CONFIG_FILE);
        File fileConfigParametersJson = Paths.get(launchJsonFileName).toFile();
        mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileConfigParametersJson, paramHashMap);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating " + ToolFileFormat.CONFIG_FILE + "  for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + e.getMessage());
        }
        configMap.put(LAUNCH_JSON_FILE_KEY, launchJsonFileName);

        // Save all uploaded file on fileSystem
        List<InputFileDTO> inputFileSavedList = new ArrayList<InputFileDTO>();
        List<String> inputFileContentType = new ArrayList<>();
        //- xlsx
        inputFileContentType.add(WP2FileFormat.INPUT_CONTENT_TYPE_LIST.get(0));
        try {
            inputFileSavedList =
                toolExecutionServiceImpl.saveInputFileOnDbAndFileSystem(networkDto, toolDto, mpFiles, inputDirPath, inputFileContentType);
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
                toolExecutionServiceImpl.getInputFilesDtoSetFromList(inputFileSavedList),
                null
            );
            taskDtoSaved.setSimulation(simulationDto);
            taskServiceImpl.partialUpdate(taskDtoSaved);
        } catch (Exception ex) {
            String errMsg = "Error saving simulation data for  tool: " + toolDto.getNum() + " uuid: " + uuid.toString();
            log.error(errMsg, ex);
        }

        log.info("Exit prepareT25WorkingDir() return: {}", configMap.toString());
        return configMap;
    }

    @Override
    @Transactional
    public Map<String, String> prepareT26WorkingDir(NetworkDTO networkDto, ToolDTO toolDto, MultipartFile[] mpFiles, String jsonConfig)
        throws Exception {
        log.info("Enter: prepareT26WorkingDir() ");
        Map<String, String> toolConfigMap = new HashMap<String, String>();

        // UUID
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        UUID uuidGenerated = UUID.randomUUID();
        String uuid = uuidGenerated.toString();
        toolConfigMap.put(UUID_KEY, uuid);

        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        // Tool's installation path
        String toolInstallationDir = toolSimulationRef.getToolWorkingDir(toolDto);
        toolConfigMap.put(TOOL_INSTALL_DIR, toolInstallationDir);
        toolConfigMap.put(LAUNCH_TOOL_FILE_KEY, ToolFileFormat.LAUNCH_FILE);

        // Create simulationWorkingDir
        // -- ATSIM/WP2/T26/<UUID>
        String simulationWorkingPath = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);
        Files.createDirectories(Paths.get(simulationWorkingPath));
        log.info("DIR: {}, created", simulationWorkingPath);
        toolConfigMap.put(WORKING_DIR_KEY, simulationWorkingPath);

        // Create logs dir if not exists
        // -- ATSIM/WP2/T26/<UUID>/logs
        // Create logs dir if not exists
        String logDirPath = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.LOG_DIR);
        Files.createDirectories(Paths.get(logDirPath));
        log.info("DIR: {}, created ", logDirPath);
        String logFile = logDirPath.concat(File.separator).concat(ToolFileFormat.LOG_FILE_NAME);
        log.info("LOG_FILE: {}", logFile);
        toolConfigMap.put(LOG_FILENAME_KEY, logFile);

        //Create input_data dir if not exists
        // -- ATSIM/WP2/T26/<UUID>//input_data
        String inputDirPath = simulationWorkingPath.concat(File.separator).concat(WP2FileFormat.INPUT_DIR);
        Files.createDirectories(Paths.get(inputDirPath));
        log.info("DIR: {} created", inputDirPath);
        toolConfigMap.put(INPUT_DIR_KEY, inputDirPath);

        //Create input_data/Energy dir if not exists
        // -- ATSIM/WP2/T26/<UUID>/input_data/Network
        String inputNetworkDirPath = inputDirPath.concat(File.separator).concat(WP2FileFormat.NETWORK_DIR);
        Files.createDirectories(Paths.get(inputNetworkDirPath));
        log.info("DIR: {} created ", inputNetworkDirPath);
        toolConfigMap.put(INPUT_NETWORK_DIR_KEY, inputNetworkDirPath);

        //Create input_data/Energy dir if not exists
        // -- ATSIM/WP2/T26/<UUID>/input_data/Energy
        String inputEnergyDirPath = inputDirPath.concat(File.separator).concat(WP2FileFormat.ENERGY_DIR);
        Files.createDirectories(Paths.get(inputEnergyDirPath));
        log.info("DIR: {} created ", inputEnergyDirPath);
        toolConfigMap.put(INPUT_ENERGY_DIR_KEY, inputEnergyDirPath);

        //Create input_data/Secondary dir if not exists
        // -- ATSIM/WP2/T26/<UUID>/input_data/Secondary
        String inputSecondaryDirPath = inputDirPath.concat(File.separator).concat(WP2FileFormat.SECONDARY_DIR);
        Files.createDirectories(Paths.get(inputSecondaryDirPath));
        log.info("DIR: {} created ", inputSecondaryDirPath);
        toolConfigMap.put(INPUT_SECONDARY_DIR_KEY, inputSecondaryDirPath);

        //Create input_data/Tertiary dir if not exists
        // -- ATSIM/WP2/T26/<UUID>/input_data/Tertiary
        String inputTertiaryDirPath = inputDirPath.concat(File.separator).concat(WP2FileFormat.TERTIARY_DIR);
        Files.createDirectories(Paths.get(inputTertiaryDirPath));
        log.info("DIR: {} created ", inputTertiaryDirPath);
        toolConfigMap.put(INPUT_TERTIARY_DIR_KEY, inputTertiaryDirPath);

        // Create outputs dir if not exists
        // -- ATSIM/WP2/T251/UUID/output_data
        String outputDirPath = simulationWorkingPath.concat(File.separator).concat(WP2FileFormat.OUTPUT_DIR);
        Files.createDirectories(Paths.get(outputDirPath));
        log.info("DIR: {} created ", outputDirPath);
        toolConfigMap.put(OUTPUT_DIR_KEY, outputDirPath);

        //Create output_data/Energy dir if not exists
        // -- ATSIM/WP2/T26/<UUID>/output_data/Energy
        String outputEnergyDirPath = outputDirPath.concat(File.separator).concat(WP2FileFormat.ENERGY_DIR);
        Files.createDirectories(Paths.get(outputEnergyDirPath));
        log.info("DIR: {} created ", outputEnergyDirPath);
        toolConfigMap.put(OUTPUT_ENERGY_DIR_KEY, outputEnergyDirPath);

        //Create output_data/Secondary dir if not exists
        // -- ATSIM/WP2/T26/<UUID>/output_data/Secondary
        String outputSecondaryDirPath = outputDirPath.concat(File.separator).concat(WP2FileFormat.SECONDARY_DIR);
        Files.createDirectories(Paths.get(outputSecondaryDirPath));
        log.info("DIR: {} created ", outputSecondaryDirPath);
        toolConfigMap.put(OUTPUT_SECONDARY_DIR_KEY, outputSecondaryDirPath);

        //Create output_data/Tertiary dir if not exists
        // -- ATSIM/WP2/T26/<UUID>/output_data/Tertiary
        String outputTertiaryDirPath = outputDirPath.concat(File.separator).concat(WP2FileFormat.TERTIARY_DIR);
        Files.createDirectories(Paths.get(outputTertiaryDirPath));
        log.info("DIR: {} created ", outputTertiaryDirPath);
        toolConfigMap.put(OUTPUT_TERTIARY_DIR_KEY, outputTertiaryDirPath);

        // Read jsonConfig input file
        T26InputParamDTO t26InputParamDTO;
        ObjectMapper mapper = new ObjectMapper();
        try {
            t26InputParamDTO = mapper.readValue(jsonConfig, T26InputParamDTO.class);
        } catch (JsonProcessingException e) {
            String errMsg = "Error converting jsonConfig into T26InputParamDTO";
            log.error(errMsg, e);
            throw e;
        }

        // Prepare launch.json file
        T26LaunchParamDTO launchParamDTO = new T26LaunchParamDTO();
        //-- Set input file Path
        List<Map<String, String>> networkFileNames = t26InputParamDTO.getNetworkFileNames();
        launchParamDTO.setNetwork(updateJsonParamsList(networkFileNames, inputNetworkDirPath));

        List<Map<String, String>> energyFileNames = t26InputParamDTO.getEnergyFileNames();
        launchParamDTO.setEnergy(updateJsonParamsList(energyFileNames, inputEnergyDirPath));

        List<Map<String, String>> secondaryFileNames = t26InputParamDTO.getSecondaryFileNames();
        if (!secondaryFileNames.isEmpty()) {
            launchParamDTO.setSecondary(updateJsonParamsList(secondaryFileNames, inputSecondaryDirPath));
        } else {
            log.debug("Secondary file name is empty");
        }

        List<Map<String, String>> tertiaryFileNames = t26InputParamDTO.getTertiaryFileNames();

        if (!tertiaryFileNames.isEmpty()) {
            launchParamDTO.setTertiary(updateJsonParamsList(tertiaryFileNames, inputTertiaryDirPath));
        } else {
            log.debug("TertiaryFileNames file name is empty");
        }

        //-- Set outputDir
        Map<String, String> outputDirMap = new HashMap<String, String>();
        outputDirMap.put(WP2FileFormat.ENERGY_DIR, outputEnergyDirPath);
        outputDirMap.put(WP2FileFormat.SECONDARY_DIR, outputSecondaryDirPath);
        outputDirMap.put(WP2FileFormat.TERTIARY_DIR, outputTertiaryDirPath);
        launchParamDTO.setOutputDir(outputDirMap);

        Map<String, Boolean> paramsEnergyToRun = t26InputParamDTO.getParameters();
        log.debug("FLAG: run_energy {} ", paramsEnergyToRun.get("run_energy"));
        launchParamDTO.setRunEnergy(paramsEnergyToRun.get("run_energy"));

        log.debug("FLAG: run_secondary {} ", paramsEnergyToRun.get("run_secondary"));
        launchParamDTO.setRunSecondary(paramsEnergyToRun.get("run_secondary"));

        log.debug("FLAG: run_secondary {} ", paramsEnergyToRun.get("run_tertiary"));
        launchParamDTO.setRunTertiary(paramsEnergyToRun.get("run_tertiary"));

        // -- save launch.json file on fileSystem
        String launchJsonFileName = simulationWorkingPath.concat(File.separator).concat(ToolFileFormat.CONFIG_FILE);
        File fileConfigParametersJson = Paths.get(launchJsonFileName).toFile();
        mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileConfigParametersJson, launchParamDTO);
        } catch (JsonProcessingException e) {
            String errMsg = "Error creating " + ToolFileFormat.CONFIG_FILE + "  for tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new Exception(errMsg + e.getMessage());
        }
        toolConfigMap.put(LAUNCH_JSON_FILE_KEY, launchJsonFileName);

        // Save input File on fileSystem and on DB
        List<InputFileDTO> inputFileSavedList = new ArrayList<InputFileDTO>();
        List<String> inputFileContentType = new ArrayList<>();
        inputFileContentType.add(WP2FileFormat.INPUT_CONTENT_TYPE_LIST.get(0)); // xlsx
        inputFileContentType.add(WP2FileFormat.INPUT_CONTENT_TYPE_LIST.get(2)); //csv
        try {
            this.saveT26InputFileOnDbAndFileSystem(
                    networkDto,
                    toolDto,
                    mpFiles,
                    inputFileContentType,
                    inputNetworkDirPath,
                    inputEnergyDirPath,
                    inputSecondaryDirPath,
                    inputTertiaryDirPath,
                    inputFileSavedList
                );
        } catch (Exception e) {
            String errMsg = "Error saving inputFiles for tool: " + toolDto.getName();
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
                toolExecutionServiceImpl.getInputFilesDtoSetFromList(inputFileSavedList),
                null
            );
            taskDtoSaved.setSimulation(simulationDto);
            taskServiceImpl.partialUpdate(taskDtoSaved);
        } catch (Exception ex) {
            String errMsg = "Error saving simulation data for  tool: " + toolDto.getNum() + " uuid: " + uuid.toString();
            log.error(errMsg, ex);
        }

        log.debug("Exit: prepareT26WorkingDir() return: {}", toolConfigMap.toString());
        return toolConfigMap;
    }

    private String getFilePath(
        String mpFileName,
        String inputNetworkDir,
        String inputEnergyDir,
        String inputSecondaryDir,
        String inputTertiaryDir
    ) {
        int pos = mpFileName.indexOf("_");
        String newFileName = mpFileName.substring(pos + 1);
        String group = mpFileName.substring(0, pos);
        switch (group) {
            case "network":
                return inputNetworkDir.concat(File.separator).concat(newFileName);
            case "energy":
                return inputEnergyDir.concat(File.separator).concat(newFileName);
            case "secondary":
                return inputSecondaryDir.concat(File.separator).concat(newFileName);
            case "tertiary":
                return inputTertiaryDir.concat(File.separator).concat(newFileName);
        }
        log.warn("mpFileName: {} name doesn't contain: '_network' or '_energy' or '_secondary' or '_tertiary' as expected ", mpFileName);
        return null;
    }

    private MultipartFile renameMultipartFile(MultipartFile mpFile) throws IOException {
        String mpFileName = mpFile.getOriginalFilename();
        int pos = mpFileName.indexOf("_");
        String fileName = mpFileName.substring(pos + 1);
        String contentType = MimeUtils.detect(mpFile);
        return new MockMultipartFile(fileName, fileName, contentType, mpFile.getBytes());
    }

    private Map<String, String> updateJsonParamsList(List<Map<String, String>> paramsList, String dirPath) {
        Map returnMapParam = new HashMap<String, String>();
        for (Map<String, String> mapParams : paramsList) {
            for (Map.Entry<String, String> entry : mapParams.entrySet()) {
                //log.debug("updateJsonParamsList "+entry.getKey() + " "+ dirPath);
                returnMapParam.put(entry.getKey(), dirPath.concat(File.separator).concat((String) entry.getValue()));
            }
        }
        //
        if (dirPath.contains("secondary") || dirPath.contains("tertiary")) {
            Optional genBidQnt = paramsList.stream().filter(m -> m.keySet().contains("gen_bid_qnt_path")).findFirst();
            if (!genBidQnt.isPresent()) {
                //"gen_bid_qnt_path":  "/ATSIM\\WP2\\T26\\b369efb3-3acf-4844-80e4-4fd3d8e2b6c6\\input_data\\secondary\\gen_bid_qnt.csv",
                returnMapParam.put("gen_bid_qnt_path", dirPath.concat(File.separator).concat("gen_bid_qnt.csv"));
            }

            Optional genBidPrices = paramsList.stream().filter(m -> m.keySet().contains("gen_bid_prices_path")).findFirst();
            if (!genBidPrices.isPresent()) {
                // "gen_bid_prices_path": "/ATSIM\\WP2\\T26\\b369efb3-3acf-4844-80e4-4fd3d8e2b6c6\\input_data\\secondary\\gen_bid_prices.csv"
                returnMapParam.put("gen_bid_prices_path", dirPath.concat(File.separator).concat("gen_bid_prices.csv"));
            }
        }

        return returnMapParam;
    }

    private void updateJsonParamsMap(Map<String, String> paramsMap, String dirPath) {
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            //log.debug("updateJsonParamsMap "+entry.getKey() + " "+ dirPath);
            paramsMap.put(entry.getKey(), dirPath.concat(File.separator).concat((String) entry.getValue()));
        }
    }

    private void saveT26InputFileOnDbAndFileSystem(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        MultipartFile[] files,
        List<String> inputFileContentType,
        String inputNetworkDir,
        String inputEnergyDir,
        String inputSecondaryDir,
        String inputTertiaryDir,
        List<InputFileDTO> inputFileSavedList
    ) throws Exception {
        for (MultipartFile mpFile : files) {
            String fileName = mpFile.getOriginalFilename();
            String contentType = MimeUtils.detect(mpFile);
            if (inputFileContentType.contains(contentType)) {
                String filePath = getFilePath(fileName, inputNetworkDir, inputEnergyDir, inputSecondaryDir, inputTertiaryDir);
                if (filePath == null) {
                    String errMsg = "filePath null for file:  " + fileName;
                    log.error(errMsg);
                    throw new Exception(errMsg);
                }

                MultipartFile newMpFile = renameMultipartFile(mpFile);
                InputFileDTO fileSaved = inputFileServiceImpl.saveFileForNetworkAndTool(newMpFile, networkDto, toolDto);
                inputFileSavedList.add(fileSaved);
                log.debug("Input File: {}, Saved on db!", fileSaved);

                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    fos.write(mpFile.getBytes());
                    log.debug("Input File: {}, Saved on fileSystem!", filePath);
                } catch (Exception e) {
                    String errMsg = "Error saving file: " + filePath;
                    log.error(errMsg);
                    throw new Exception(errMsg);
                }
            } else {
                String msg = "InputFile content type not valid  ";
                log.error(msg);
                throw new Exception(msg);
            }
        }
    }

    // Output file are splitted in different output_dir  for t26, it's impossible to use the same method used for other tools
    @Override
    @Async
    public CompletableFuture<String> t26Async(NetworkDTO networkDto, ToolDTO toolDto, Map<String, String> toolConfigMap) throws Exception {
        log.info(
            "Enter: t26Async() with args: [networkId: {}, toolNum: {}, configMap: {} ",
            networkDto.getName(),
            toolDto.getNum(),
            toolConfigMap.toString()
        );

        String uuid = toolConfigMap.get(UUID_KEY);
        log.info("UUID: {} ", uuid);

        Optional<SimulationDTO> simulationOpt = simulationServiceImpl.findByUuid(uuid);
        if (!simulationOpt.isPresent()) {
            throw new Exception(" Simulation doesn't exists for simulation.uuid: " + uuid);
        }
        SimulationDTO simulationDto = simulationOpt.get();
        log.debug("Simulation_id: {} ", simulationDto.getId());

        Optional<TaskDTO> taskOpt = taskServiceImpl.findBySimulationId(simulationDto.getId());

        if (!taskOpt.isPresent()) {
            throw new Exception(" Task doesn't exists for simulation.uuid: " + uuid);
        }

        TaskDTO taskDto = taskOpt.get();
        TaskDTO taskDtoUpdated = taskDto;
        // -- ATTEST/tools/T251
        int exitCode = -1;
        try {
            //NO timeout:  set -1
            exitCode =
                commandExecutorServiceImpl.launchTool(
                    toolConfigMap.get(TOOL_INSTALL_DIR),
                    toolConfigMap.get(LAUNCH_JSON_FILE_KEY),
                    toolConfigMap.get(LOG_FILENAME_KEY),
                    WP2FileFormat.LAUNCH_FILE,
                    -1L,
                    taskDto.getId()
                );
        } catch (IOException | InterruptedException e) {
            String errMsg = "An error occurred while running the batch script to execute tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new RunningToolException(errMsg, e);
        }
        log.debug("t26Async() exitCode: {}", exitCode);

        //--- Save log.txt file produce by the tool into TOOL_LOG_FILE table
        Path logFilePath = Paths.get(toolConfigMap.get(LOG_FILENAME_KEY));
        ToolLogFileDTO toolLogFileSaved = this.toolExecutionServiceImpl.uploadToolLogFileToDB(logFilePath, false);

        // Update task Status
        if (exitCode != 0) {
            String errMsg = "Error running tool: " + toolDto.getNum() + " uuid: " + uuid;
            log.error(errMsg);
            taskDtoUpdated = toolExecutionServiceImpl.updateTaskStatus(taskDto, TaskStatus.Status.FAILED);
        } else {
            taskDtoUpdated = toolExecutionServiceImpl.updateTaskStatus(taskDto, TaskStatus.Status.PASSED);
        }

        if (toolLogFileSaved != null) {
            toolExecutionServiceImpl.updateTaskToolLogFile(taskDtoUpdated, toolLogFileSaved);
        }

        List<OutputFileDTO> outputFileDtoSavedList = new ArrayList<OutputFileDTO>();
        try {
            File outputDir = new File(toolConfigMap.get(OUTPUT_DIR_KEY));
            // all T26 output files are located in subdirectories:(energy,secondary,tertiary)  under ATSIM/WP2/T251/UUID/output_data/
            List<File> allOutputDir = Arrays.stream(outputDir.listFiles()).filter(f -> f.isDirectory()).collect(Collectors.toList());
            for (File dir : allOutputDir) {
                toolExecutionServiceImpl.saveOutputFilesOnDB(
                    dir,
                    networkDto,
                    toolDto,
                    WP2FileFormat.T26_DOWNLOAD_FILES_EXTENSION,
                    outputFileDtoSavedList,
                    simulationDto
                );
            }
        } catch (IOException ioex) {
            String errMsg = "Error saving results for tool: " + toolDto.getName();
            log.error(errMsg, ioex);
        }
        log.info("Exit: t26Async() Tool: {}, on Network: {}, running successfully ", toolDto.getName(), networkDto.getName());
        return CompletableFuture.completedFuture(uuid);
    }
}
