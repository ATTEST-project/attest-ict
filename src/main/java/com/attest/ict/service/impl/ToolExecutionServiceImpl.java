package com.attest.ict.service.impl;

import static com.attest.ict.tools.constants.ToolsConstants.*;

import com.attest.ict.config.ToolsConfiguration;
import com.attest.ict.custom.tools.utils.ToolSimulationReferencies;
import com.attest.ict.custom.tools.utils.ToolVarName;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.custom.utils.MimeUtils;
import com.attest.ict.domain.User;
import com.attest.ict.helper.TaskStatus;
import com.attest.ict.helper.txt.FileReader;
import com.attest.ict.repository.OutputFileRepository;
import com.attest.ict.repository.ToolRepository;
import com.attest.ict.security.SecurityUtils;
import com.attest.ict.service.*;
import com.attest.ict.service.dto.*;
import com.attest.ict.service.mapper.ToolMapper;
import com.attest.ict.tools.constants.WP2FileFormat;
import com.attest.ict.tools.exception.RunningToolException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ToolExecutionServiceImpl implements ToolExecutionService {

    private final Logger log = LoggerFactory.getLogger(ToolExecutionServiceImpl.class);

    private final String attestToolsDir;

    private final String toolsPathSimulation;

    @Autowired
    private InputFileService inputFileServiceImpl;

    @Autowired
    private OutputFileService outputFileServiceImpl;

    @Autowired
    private ToolLogFileServiceImpl toolLogFileServiceImpl;

    @Autowired
    private OutputFileRepository outputFileRepository;

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private ToolMapper toolMapper;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskServiceImpl;

    @Autowired
    CommandExecutorService commandExecutorServiceImpl;

    @Autowired
    private SimulationServiceImpl simulationServiceImpl;

    public ToolExecutionServiceImpl(ToolsConfiguration toolsConfig) {
        // ATTEST/tools
        this.attestToolsDir = toolsConfig.getPath();
        log.debug("attestToolsDir {}", attestToolsDir);
        // ATSIM
        this.toolsPathSimulation = toolsConfig.getPathSimulation();
        log.debug("toolsPathSimulation {}", toolsPathSimulation);
    }

    @Override
    public Optional<ToolDTO> findToolByName(String name) {
        return toolRepository.findByName(name).map(toolMapper::toDto);
    }

    @Override
    public String getToolPath(String toolRootDir, ToolDTO toolDto) {
        StringBuffer toolPathSB = new StringBuffer();
        log.debug("toolRootDir {}", toolRootDir);
        toolPathSB
            .append(toolRootDir)
            .append(File.separator)
            .append(toolDto.getWorkPackage())
            .append(File.separator)
            .append(toolDto.getNum())
            .append(File.separator)
            .append(toolDto.getPath());
        return toolPathSB.toString();
    }

    @Override
    public TaskDTO createTask(ToolDTO toolDto) throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTool(toolDto);
        taskDTO.setDateTimeStart(Instant.now());
        taskDTO.setTaskStatus(TaskStatus.Status.ONGOING.name());
        Optional<String> currentUser = SecurityUtils.getCurrentUserLogin();
        String userLogin = currentUser.get();
        if (!userService.getUserWithAuthoritiesByLogin(userLogin).isPresent()) {
            log.error("Missing UserAuthorities for user {}", userLogin);
            throw new Exception("Missing UserAuthorities for user: " + userLogin);
        }
        User userLogged = userService.getUserWithAuthoritiesByLogin(userLogin).get();
        taskDTO.setUser(new UserDTO(userLogged));
        TaskDTO taskSaved = taskServiceImpl.save(taskDTO);
        log.info("New Task: {}, Saved ", taskSaved);
        return taskSaved;
    }

    @Override
    public TaskDTO updateTaskStatus(TaskDTO taskDto, TaskStatus.Status status) {
        taskDto.setDateTimeEnd(Instant.now());
        taskDto.setTaskStatus(status.name());
        Optional<TaskDTO> taskDtoOpt = taskServiceImpl.partialUpdate(taskDto);
        return taskDtoOpt.get();
    }

    @Override
    public TaskDTO updateTaskToolLogFile(TaskDTO taskDto, ToolLogFileDTO toolLogFileDTO) {
        taskDto.setToolLogFile(toolLogFileDTO);
        Optional<TaskDTO> taskDtoOpt = taskServiceImpl.partialUpdate(taskDto);
        return taskDtoOpt.get();
    }

    @Override
    public TaskDTO updateTaskSimulation(TaskDTO taskDto, SimulationDTO simulationDto, TaskStatus.Status status) {
        taskDto.setSimulation(simulationDto);
        taskDto.setTaskStatus(status.name());
        taskDto.setDateTimeEnd(Instant.now());
        Optional<TaskDTO> taskDtoOpt = taskServiceImpl.partialUpdate(taskDto);
        return taskDtoOpt.get();
    }

    @Override
    public void uploadToolLogFileToDB(Path f, TaskDTO taskDto, boolean delete) {
        String fileName = f.getFileName().toString();
        MultipartFile multipartFile = null;
        try {
            multipartFile = new MockMultipartFile(fileName, fileName, FileUtils.probeContentType(f), Files.readAllBytes(f));
            ToolLogFileDTO toolLogFileDTOSaved = toolLogFileServiceImpl.saveFileByTask(multipartFile, taskDto);
            taskDto.setToolLogFile(toolLogFileDTOSaved);
            log.info(" Tool log File saved succesfully: {}", toolLogFileDTOSaved);
            if (delete) {
                Files.delete(f);
                log.debug("Tool log File deleted, succesfully from: {}", f);
            }
        } catch (IOException e) {
            log.error("Exception saving tool log file: ", e);
        }
    }

    @Override
    public ToolLogFileDTO uploadToolLogFileToDB(Path f, boolean delete) {
        String fileName = f.getFileName().toString();

        MultipartFile multipartFile = null;
        try {
            multipartFile = new MockMultipartFile(fileName, fileName, FileUtils.probeContentType(f), Files.readAllBytes(f));
            ToolLogFileDTO toolLogFileDTOSaved = toolLogFileServiceImpl.saveFile(multipartFile);
            log.info("Tool log File saved successfully: {}", toolLogFileDTOSaved);
            if (delete) {
                Files.delete(f);
                log.info("Tool log File deleted, successfully from: {}", f);
            }
            return toolLogFileDTOSaved;
        } catch (IOException e) {
            log.error("Exception saving file in TOOL_LOG_FILE table: ", e);
            return null;
        }
    }

    @Override
    public SimulationDTO createSimulation(
        ToolDTO toolDto,
        NetworkDTO networkDto,
        UUID uuid,
        File configFile,
        TaskDTO taskDto,
        List<InputFileDTO> inputFileTDOList,
        List<OutputFileDTO> outputFileDTOList,
        TaskStatus.Status status,
        String description
    ) throws IOException {
        Set<InputFileDTO> inputFileDtoSet = new HashSet<InputFileDTO>() {
            {
                addAll(inputFileTDOList);
            }
        };

        SimulationDTO simulationDto = new SimulationDTO();
        simulationDto.setUuid(uuid);

        if (!"".equals(description)) {
            simulationDto.setDescription(description);
        }

        simulationDto.setNetwork(networkDto);
        simulationDto.setInputFiles(inputFileDtoSet);

        MultipartFile mockMultipartFile = new MockMultipartFile(
            configFile.getName(),
            configFile.getName(),
            FileUtils.probeContentType(configFile.toPath()),
            Files.readAllBytes(configFile.toPath())
        );
        simulationDto.setConfigFile(mockMultipartFile.getBytes());
        simulationDto.setConfigFileContentType(mockMultipartFile.getContentType());
        SimulationDTO simulationDtoSaved = simulationServiceImpl.save(simulationDto);
        log.debug("Simulation: {} ", simulationDtoSaved);

        // add simulation reference to outputfile
        for (OutputFileDTO outputFileDto : outputFileDTOList) {
            outputFileDto.setSimulation(simulationDtoSaved);
            outputFileServiceImpl.partialUpdate(outputFileDto);
        }
        // add simulation reference to task and update task status
        this.updateTaskSimulation(taskDto, simulationDtoSaved, status);
        return simulationDto;
    }

    @Override
    public SimulationDTO initSimulation(
        ToolDTO toolDto,
        NetworkDTO networkDto,
        UUID uuid,
        File jsonConfigFile,
        TaskDTO taskDto,
        Set<InputFileDTO> inputFileDtoSet,
        String description
    ) throws IOException {
        SimulationDTO simulationDto = new SimulationDTO();
        simulationDto.setUuid(uuid);
        simulationDto.setNetwork(networkDto);
        if (!inputFileDtoSet.isEmpty()) {
            simulationDto.setInputFiles(inputFileDtoSet);
        }
        String contentType = FileUtils.probeContentType(jsonConfigFile.toPath());
        byte[] fileByteArray = Files.readAllBytes(jsonConfigFile.toPath());
        simulationDto.setConfigFile(fileByteArray);
        simulationDto.setConfigFileContentType(contentType);
        if (!StringUtils.isBlank(description)) {
            simulationDto.setDescription(description);
        }
        SimulationDTO simulationDtoSaved = simulationServiceImpl.save(simulationDto);
        log.debug("New Simulation saved: {} ", simulationDtoSaved);
        return simulationDtoSaved;
    }

    // add 05/01
    @Override
    public List<InputFileDTO> saveInputFileOnDbAndFileSystem(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        MultipartFile[] files,
        String inputDir,
        List<String> fileContentTypes
    ) throws Exception {
        log.debug("Enter: saveInputFileOnDbAndFileSystem() ");
        log.debug("InputDir: {}", inputDir);
        List<InputFileDTO> inputFileSaved = new ArrayList<InputFileDTO>();
        // save input file in db and into the file system
        for (MultipartFile mpFile : files) {
            String contentType = MimeUtils.detect(mpFile);
            log.debug("Tool: {}", toolDto);
            log.debug(
                "Input File - originalFileName: {}, name: {}, contentType: {}",
                mpFile.getOriginalFilename(),
                mpFile.getName(),
                contentType
            );
            log.debug("Input file ContentTypes accepted by the tool: {}", fileContentTypes.toString());
            //20230825 contentTypeCheck disabled to avoid problem with T51Monitoring file .h5. (on INESCTEC contentType is: application/octet-stream. IN our dev environment ContentType is text/x-hdf5
            // if (fileContentTypes.contains(mpFile.getContentType())) {
            // -- save file ON DB input_file table
            InputFileDTO fileSaved = inputFileServiceImpl.saveFileForNetworkAndTool(mpFile, networkDto, toolDto);
            inputFileSaved.add(fileSaved);
            String inputFileName = inputDir + File.separator + mpFile.getOriginalFilename();

            try (FileOutputStream fos = new FileOutputStream(inputFileName)) {
                fos.write(mpFile.getBytes());
                log.info("Input File: {}, Saved on fileSystem!", inputFileName);
            } catch (Exception e) {
                String errMsg = "Error saving file: " + inputFileName;
                log.error(errMsg);
                throw new Exception(errMsg, e);
            }
            // } else {
            // String msg = "Input file: "+ mpFile.getOriginalFilename() +" ContentType: "+ mpFile.getContentType() + ", is not valid";
            // log.error(msg);
            // throw new Exception(msg);
            // }
        }
        log.debug("Exit: saveInputFileOnDbAndFileSystem() ");
        return inputFileSaved;
    }

    @Override
    public void saveOutputFilesOnDB(
        File outputPath,
        NetworkDTO networkDto,
        ToolDTO toolDto,
        List<String> extensions,
        List<OutputFileDTO> outputFileDtoSavedList,
        SimulationDTO simulationDto
    ) throws IOException {
        if (!outputPath.isDirectory()) {
            throw new IOException(outputPath.getPath() + " is not a Directory ");
        }

        log.info("Save tool's output file with extension: {} ", extensions.toString());
        File[] files = outputPath.listFiles(
            new FilenameFilter() {
                @Override
                public boolean accept(File directory, String fileName) {
                    for (String ext : extensions) {
                        if (fileName.endsWith(ext)) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        );
        for (File file : files) {
            OutputFileDTO fileDtoSaved = outputFileServiceImpl.saveFileForNetworkAndToolAndSimulation(
                file,
                networkDto,
                toolDto,
                simulationDto,
                file.getName()
            );
            log.info("Output File: {}, Saved on db!", fileDtoSaved);
            outputFileDtoSavedList.add(fileDtoSaved);
        }
    }

    @Override
    public void saveAllOutputDirFilesOnDB(
        File outputPath,
        NetworkDTO networkDto,
        ToolDTO toolDto,
        List<String> extensions,
        List<OutputFileDTO> outputFileDtoSavedList,
        SimulationDTO simulationDto
    ) throws IOException {
        if (!outputPath.isDirectory()) {
            throw new IOException(outputPath.getPath() + " is not a Directory ");
        }

        Map<String, File> mapAllFileInDir = new HashMap<String, File>();
        // -- read all file in oututPath and all subDirs
        FileUtils.readContentDir(outputPath, 0, mapAllFileInDir);

        // include only file with extension
        Map<String, File> mapFilteredFiles = mapAllFileInDir
            .entrySet()
            .stream()
            .filter(f -> FileUtils.filterFileByNameExtension(f.getValue(), extensions))
            .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

        for (Map.Entry<String, File> entry : mapFilteredFiles.entrySet()) {
            String key = entry.getKey();
            File file = entry.getValue();
            OutputFileDTO fileDtoSaved = outputFileServiceImpl.saveFileForNetworkAndToolAndSimulation(
                file,
                networkDto,
                toolDto,
                simulationDto,
                key
            );
            outputFileDtoSavedList.add(fileDtoSaved);
        }
    }

    @Override
    public ByteArrayOutputStream zipOutputResults(NetworkDTO networkDto, ToolDTO toolDto, String uuid) throws IOException {
        String toolWorkPackage = toolDto.getWorkPackage();
        String toolNum = toolDto.getNum();
        // eg: /ATSIM
        ToolSimulationReferencies toolSimulationRef = new ToolSimulationReferencies(this.attestToolsDir, this.toolsPathSimulation);
        // eg: /ATSIM/WP2/<ToolNUM>/<uuid>
        String simulationWorkingPath = toolSimulationRef.getSimulationWorkingDir(toolWorkPackage, toolNum, uuid);
        // eg: /ATSIM/WP2/<ToolNUM>/<uuid>/output_data
        String outputDir = simulationWorkingPath.concat(File.separator).concat(WP2FileFormat.OUTPUT_DIR);

        File dir = new File(outputDir);
        if (!dir.isDirectory()) {
            throw new FileNotFoundException("Directory: " + outputDir + " doesn't exist! ");
        }
        return FileUtils.zipDir(dir);
    }

    @Override
    public Set<InputFileDTO> getInputFilesDtoSetFromList(List<InputFileDTO> filesList) {
        Set<InputFileDTO> inputFileDtoSet = new HashSet<InputFileDTO>();
        inputFileDtoSet.addAll(filesList);
        return inputFileDtoSet;
    }

    @Override
    @Async
    public CompletableFuture<String> asyncRun(
        NetworkDTO networkDto,
        ToolDTO toolDto,
        Map<String, String> toolConfigMap,
        List<String> suffixList
    ) throws Exception {
        log.info(
            "Enter: asyncRun() with arguments: [networkId: {}, toolNum: {}, configMap: {}] ",
            networkDto.getName(),
            toolDto.getNum(),
            toolConfigMap.toString()
        );
        String uuid = toolConfigMap.get(UUID_KEY);
        log.info("uuid: {} ", uuid);

        Optional<SimulationDTO> simulationOpt = simulationServiceImpl.findByUuid(uuid);
        if (!simulationOpt.isPresent()) {
            throw new Exception("Simulation doesn't exists for simulation.uuid: " + uuid);
        }
        SimulationDTO simulationDto = simulationOpt.get();
        Optional<TaskDTO> taskOpt = taskServiceImpl.findBySimulationId(simulationDto.getId());

        if (!taskOpt.isPresent()) {
            throw new Exception("Task doesn't exists for simulation.uuid: " + uuid);
        }

        TaskDTO taskDto = taskOpt.get();
        TaskDTO taskDtoUpdated = taskDto;
        int exitCode = -1;

        try {
            exitCode =
                commandExecutorServiceImpl.launchTool(
                    toolConfigMap.get(TOOL_INSTALL_DIR),
                    toolConfigMap.get(LAUNCH_JSON_FILE_KEY),
                    toolConfigMap.get(LOG_FILENAME_KEY),
                    toolConfigMap.get(LAUNCH_TOOL_FILE_KEY),
                    -1L, //timeout In sec
                    taskDto.getId()
                );
        } catch (IOException | InterruptedException e) {
            String errMsg = "An error occurred while running the batch script to execute tool: " + toolDto.getName();
            log.error(errMsg, e);
            throw new RunningToolException(errMsg, e);
        }

        //--- Save log.txt file produce by the tool into TOOL_LOG_FILE table
        Path logFilePath = Paths.get(toolConfigMap.get(LOG_FILENAME_KEY));
        ToolLogFileDTO toolLogFileSaved = this.uploadToolLogFileToDB(logFilePath, false);

        if (exitCode != 0) {
            taskDtoUpdated = this.manageErrorExitCode(toolConfigMap.get(LOG_FILENAME_KEY), taskDto, toolConfigMap.get(ERROR_TO_SKIP));
        } else {
            taskDtoUpdated = this.updateTaskStatus(taskDto, TaskStatus.Status.PASSED);
        }

        if (toolLogFileSaved != null) this.updateTaskToolLogFile(taskDtoUpdated, toolLogFileSaved);

        // check if exit value is 0 (OK) store output file on DB
        List<OutputFileDTO> outputFileDtoSavedList = new ArrayList<OutputFileDTO>();
        try {
            if (toolDto.getName().equals(ToolVarName.T44_AS_DAY_HEAD_TX)) {
                // save all file present under output_dir and all sub dir
                this.saveAllOutputDirFilesOnDB(
                        new File(toolConfigMap.get(OUTPUT_DIR_KEY)),
                        networkDto,
                        toolDto,
                        suffixList,
                        outputFileDtoSavedList,
                        simulationDto
                    );
            } else {
                this.saveOutputFilesOnDB(
                        new File(toolConfigMap.get(OUTPUT_DIR_KEY)),
                        networkDto,
                        toolDto,
                        suffixList,
                        outputFileDtoSavedList,
                        simulationDto
                    );
            }
        } catch (IOException ioex) {
            String errMsg = "Error saving results on DB for tool: " + toolDto.getName();
            log.error(errMsg, ioex);
        }

        log.info("Exit: asyncRun() uuid: {}", uuid);
        return CompletableFuture.completedFuture(uuid);
    }

    private TaskDTO manageErrorExitCode(String logFilePath, TaskDTO taskDto, String errorToSkip) {
        log.debug("Enter: manageErrorExitCode() ");

        Optional<TaskDTO> taskDtoUpdatedOpt = taskServiceImpl.findOne(taskDto.getId());
        log.info("Task: {} " + taskDtoUpdatedOpt.get());

        // Verify if Tool raise an exception but it's not a real exception e.g T41:  run this exception "ERROR: LoadError: LoadError: No violations. This case is a waste of time"
        boolean isErrorToSkipPresent = false;
        if (errorToSkip != null && !errorToSkip.isBlank()) {
            FileReader reader = new FileReader();
            isErrorToSkipPresent = reader.searchString(logFilePath, errorToSkip);
            if (isErrorToSkipPresent) {
                log.warn("Tool raises this exception: " + errorToSkip + ", but it mustn't be considered an error!");
                TaskDTO taskResult = this.updateTaskStatus(taskDto, TaskStatus.Status.PASSED);
                log.info("Exit:  manageErrorExitCode()  with results: " + taskResult);
                return taskResult;
            }
        }
        TaskDTO taskResult = this.updateTaskStatus(taskDto, TaskStatus.Status.FAILED);
        log.info("Exit:  manageErrorExitCode()  with results: " + taskResult);
        return taskResult;
    }
}
