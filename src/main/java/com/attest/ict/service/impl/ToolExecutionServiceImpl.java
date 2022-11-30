package com.attest.ict.service.impl;

import com.attest.ict.config.ToolsConfiguration;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.domain.OutputFile;
import com.attest.ict.domain.User;
import com.attest.ict.helper.TaskStatus;
import com.attest.ict.repository.OutputFileRepository;
import com.attest.ict.repository.ToolRepository;
import com.attest.ict.security.SecurityUtils;
import com.attest.ict.service.InputFileService;
import com.attest.ict.service.OutputFileService;
import com.attest.ict.service.SimulationService;
import com.attest.ict.service.TaskService;
import com.attest.ict.service.ToolExecutionService;
import com.attest.ict.service.UserService;
import com.attest.ict.service.dto.InputFileDTO;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.OutputFileDTO;
import com.attest.ict.service.dto.SimulationDTO;
import com.attest.ict.service.dto.TaskDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.ToolLogFileDTO;
import com.attest.ict.service.dto.UserDTO;
import com.attest.ict.service.mapper.ToolMapper;
import com.attest.ict.tools.RunWrapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ToolExecutionServiceImpl implements ToolExecutionService {

    private final Logger log = LoggerFactory.getLogger(ToolExecutionServiceImpl.class);

    //Override by toolsRootPath
    //private static final String CURRENT_DIR = System.getProperty("user.dir");

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
    TaskService taskService;

    @Autowired
    SimulationService simulationService;

    private String toolsRootPath;

    public ToolExecutionServiceImpl(ToolsConfiguration toolsConfig) {
        this.toolsRootPath = toolsConfig.getPath();
        log.debug("toolsRootPath {}", toolsRootPath);
    }

    @Override
    /*
     * useful method for jl/py tools to run two functions at once:
     * 1) upload file to db
     * 2) copy file to destination folder
     */
    public void uploadAndMove(MultipartFile file, NetworkDTO networkDto, String folderPath, ToolDTO toolDto) throws IOException {
        inputFileServiceImpl.saveFileForNetwork(file, networkDto, toolDto);
        FileUtils.moveFileTo(file, folderPath);
    }

    @Override
    public void uploadDATInputs(MultipartFile[] files, NetworkDTO networkDto, ToolDTO toolDto) throws Exception {
        log.info("Uploading input file for {}. Num Files: {}, Network: {} " + toolDto.getName(), files.length, networkDto.getName());
        for (MultipartFile file : files) {
            // check if file is .ods or .xlsx
            if (FileUtils.checkContentType(file)) {
                if (checkInputFileNameDAT(file.getOriginalFilename())) {
                    inputFileServiceImpl.saveFileForNetwork(file, networkDto, toolDto);
                } else { // file input not valid
                    String msg = "Exception uploading inputFile for: " + toolDto.getName() + ". Bad Input File Name";
                    log.error(msg);
                    throw new Exception(msg);
                }
            }
            // otherwise file content type is not valid
            else {
                String msg = "Exception uploading InputFile for: " + toolDto.getName() + " .Content type not valid";
                log.error(msg);
                throw new Exception(msg);
            }
        }
    }

    @Override
    public void uploadRTTInputs(MultipartFile[] files, NetworkDTO networkDto, ToolDTO toolDto) throws Exception {
        log.info("Uploading input file for {}. Num Files: {}, Network: {} " + toolDto.getName(), files.length, networkDto.getName());
        for (MultipartFile file : files) {
            if (FileUtils.checkContentType(file)) {
                if (checkInputFileNameRTT(file.getOriginalFilename())) {
                    inputFileServiceImpl.saveFileForNetwork(file, networkDto, toolDto);
                } else {
                    String msg = "Exception uploading inputFile for for:" + toolDto.getName() + ". Bad Input File Name";
                    log.error(msg);
                    throw new Exception(msg);
                }
            } else {
                String msg = "Exception uploading InputFile for: " + toolDto.getName() + " .Content type not valid";
                log.error(msg);
                throw new Exception(msg);
            }
        }
    }

    @Override
    public void uploadWindPVInput(MultipartFile file, NetworkDTO networkDto, ToolDTO toolDto) throws Exception {
        log.info("Uploading input file for {}. File: {}, Network: {} " + toolDto.getName(), file.getName(), networkDto.getName());
        String fileInputName = "data_scenarios.ods";
        if (FileUtils.checkContentType(file)) {
            if (file.getOriginalFilename().equals(fileInputName)) {
                inputFileServiceImpl.saveFileForNetwork(file, networkDto, toolDto);
            } else {
                String msg = "Exception uploading inputFile for for:" + toolDto.getName() + ". Bad Input File Name";
                log.error(msg);
                throw new Exception(msg);
            }
        } else {
            String msg = "Exception uploading InputFile for: " + toolDto.getName() + " .Content type not valid";
            log.error(msg);
            throw new Exception(msg);
        }
    }

    @Override
    public void uploadStochasticMPInput(MultipartFile file, NetworkDTO networkDto, ToolDTO toolDto) throws Exception {
        log.info("Uploading input file for {}. File: {}, Network: {} " + toolDto.getName(), file.getName(), networkDto.getName());
        String fileInputName = "case_34_baran.ods";

        if (FileUtils.checkContentType(file)) {
            if (file.getOriginalFilename().equals(fileInputName)) {
                inputFileServiceImpl.saveFileForNetwork(file, networkDto, toolDto);
            } else {
                String msg = "Exception uploading inputFile for for:" + toolDto.getName() + ". Bad Input File Name";
                log.error(msg);
                throw new Exception(msg);
            }
        } else {
            String msg = "Exception uploading InputFile for T2.5 Real Time: Content type not valid";
            log.error(msg);
            throw new Exception(msg);
        }
    }

    @Override
    public String uploadAssetInput(MultipartFile file, NetworkDTO networkDTO, ToolDTO toolDTO) throws Exception {
        log.info("Uploading input file for  Tool: {}, File: {}, Network: {} ", toolDTO.getName(), file.getName(), networkDTO.getName());
        if (FileUtils.checkContentTypeT51(file)) {
            String variables = t51GetVariablesFromInput(file, toolDTO);
            if (variables.isEmpty()) {
                String msg = "Variabiles are empty";
                log.error(msg);
                throw new Exception(msg);
            }
            inputFileServiceImpl.saveFileForNetwork(file, networkDTO, toolDTO);
            return variables;
        } else {
            String msg = "Exception uploading InputFile for T5.1 Real Time: Content type not valid";
            log.error(msg);
            throw new Exception(msg);
        }
    }

    private String t51GetVariablesFromInput(MultipartFile file, ToolDTO toolDTO) {
        String toolPath = getToolPath(toolsRootPath, toolDTO);
        String codePath = toolPath + File.separator + "Software" + File.separator + "main";
        String inputsPath = toolPath + File.separator + "input_files";
        String mainPath = "get_variables.cmd";

        try {
            Path filePath = Paths.get(inputsPath, file.getOriginalFilename());
            file.transferTo(filePath);

            String jsonPath = "\"{\"\"path\"\": \"\"" + filePath.toString().replace("\\", "/") + "\"\"}\"";

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(new File(codePath));
            processBuilder.command(Arrays.asList(codePath + "/" + mainPath, jsonPath));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("{\"variables")) {
                    builder.append(line);
                    builder.append(System.getProperty("line.separator"));
                }
            }

            return builder.toString();
        } catch (Exception e) {
            log.error("Exception: ", e);
            return "";
        }
    }

    @Override
    public void uploadAssetMonitoringInput(MultipartFile file, NetworkDTO networkDTO, ToolDTO toolDTO) throws Exception {
        log.info("Uploading input file for {}. File: {}, Network: {} " + toolDTO.getName(), file.getName(), networkDTO.getName());
        String toolPath = getToolPath(toolsRootPath, toolDTO);
        String inputsPath = toolPath + File.separator + "input_files";
        if (FileUtils.checkContentTypeT51Monitoring(file) || FileUtils.isDotH5File(file)) {
            Path filePath = Paths.get(inputsPath, file.getOriginalFilename());
            file.transferTo(filePath);
            inputFileServiceImpl.saveFileForNetwork(file, networkDTO, toolDTO);
        } else {
            String msg = "Exception uploading InputFile for: " + toolDTO.getName() + " .Content type not valid";
            log.error(msg);
            throw new Exception(msg);
        }
    }

    @Override
    public void uploadToolLogFileToDB(Path f, TaskDTO taskDto, boolean delete) {
        String fileName = f.getFileName().toString();

        MultipartFile multipartFile = null;
        try {
            //multipartFile = new MockMultipartFile(fileName, fileName, Files.probeContentType(f), Files.readAllBytes(f));
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
    public void uploadOutputFilesToDB(Path f, NetworkDTO networkDto, ToolDTO toolDto) {
        String fileName = "";
        if (f.getFileName().toString().startsWith("fdr")) {
            fileName = f.getFileName().toString().replace("_", "_sc");
        } else {
            fileName = f.getFileName().toString();
        }
        MultipartFile multipartFile = null;
        try {
            //multipartFile = new MockMultipartFile(fileName, fileName, Files.probeContentType(f), Files.readAllBytes(f));
            multipartFile = new MockMultipartFile(fileName, fileName, FileUtils.probeContentType(f), Files.readAllBytes(f));
            outputFileServiceImpl.saveFileForNetworkAndTool(multipartFile, networkDto, toolDto);
            Files.delete(f);
        } catch (IOException e) {
            log.error("Exception: ", e);
        }
    }

    @Override
    public boolean checkInputFileNameDAT(String fileName) {
        switch (fileName) {
            case "Input Data - Networks.xlsx":
            case "Input Data - Resources.xlsx":
            case "Input Data - Other.xlsx":
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean checkInputFileNameRTT(String fileName) {
        switch (fileName) {
            case "Input Data - Networks.xlsx":
            case "Input Data - Resources.xlsx":
            case "Input Data - Other.xlsx":
            case "Input Data - AGC.xlsx":
            case "Input Data - DA bids.xlsx":
                return true;
            default:
                return false;
        }
    }

    @Override
    public void createTmpInputsForTools(List<String> fileNames, Long networkId, String path, String toolName) throws IOException {
        for (String fileName : fileNames) {
            Optional<InputFileDTO> inputFileDtoOpt = inputFileServiceImpl.findLastFileByNetworkIdAndFileNameAndToolName(
                networkId,
                fileName,
                toolName
            );
            if (inputFileDtoOpt.isPresent()) {
                InputFileDTO inputFileDto = inputFileDtoOpt.get();
                log.debug("Last Input File Loaded id:{} for NetworkId: {}  ", inputFileDto.getId(), networkId);
                try (FileOutputStream fos = new FileOutputStream(path + "/" + fileName)) {
                    fos.write(inputFileDto.getData());
                }
            }
        }
    }

    @Override
    public List<OutputFile> getAllPlots(Long networkId) {
        return outputFileRepository.getPlotsPNGByNetworkId(networkId);
    }

    @Override
    public OutputFile getFdrPlot(Long networkId, String feeder, String scenario) {
        return outputFileRepository.getFdrPlotOfNetworkByFdrAndSc(networkId, feeder, scenario);
    }

    @Override
    public OutputFile getVPlot(Long networkId, String scenario) {
        return outputFileRepository.getVPlotOfNetworkBySc(networkId, scenario);
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
            log.error(" Missing UserAuthorities for user {}", userLogin);
            throw new Exception("Missing UserAuthorities for user: " + userLogin);
        }
        User userLogged = userService.getUserWithAuthoritiesByLogin(userLogin).get();
        taskDTO.setUser(new UserDTO(userLogged));
        TaskDTO taskSaved = taskService.save(taskDTO);
        log.debug("Task: {}, Saved ", taskSaved);
        return taskSaved;
    }

    @Override
    public TaskDTO updateTask(TaskDTO taskDto, TaskStatus.Status status) {
        taskDto.setDateTimeEnd(Instant.now());
        taskDto.setTaskStatus(status.name());
        Optional<TaskDTO> taskDtoOpt = taskService.partialUpdate(taskDto);
        return taskDtoOpt.get();
    }

    @Override
    public TaskDTO updateTaskSimulation(TaskDTO taskDto, SimulationDTO simulationDto, TaskStatus.Status status) {
        taskDto.setSimulation(simulationDto);
        taskDto.setTaskStatus(status.name());
        taskDto.setDateTimeEnd(Instant.now());
        Optional<TaskDTO> taskDtoOpt = taskService.partialUpdate(taskDto);
        return taskDtoOpt.get();
    }

    //    @Override
    //    public SimulationDTO createSimulation(
    //        ToolDTO toolDto,
    //        NetworkDTO networkDto,
    //        UUID uuid,
    //        String description,
    //        File configFile,
    //        Set<InputFileDTO> inputFiles
    //    ) throws IOException {
    //        SimulationDTO simulationDto = new SimulationDTO();
    //
    //        simulationDto.setUuid(uuid);
    //
    //        if ("".equals(description)) {
    //            simulationDto.setDescription(description);
    //        }
    //
    //        simulationDto.setNetwork(networkDto);
    //        simulationDto.setInputFiles(inputFiles);
    //
    //        MultipartFile multipartFile = new MockMultipartFile(
    //            configFile.getName(),
    //            configFile.getName(),
    //            Files.probeContentType(configFile.toPath()),
    //            Files.readAllBytes(configFile.toPath())
    //        );
    //        simulationDto.setConfigFile(multipartFile.getBytes());
    //        simulationDto.setConfigFileContentType(multipartFile.getContentType());
    //        SimulationDTO simulationDtoSaved = simulationService.save(simulationDto);
    //        log.debug("Simulation: {} ", simulationDtoSaved);
    //
    //        return simulationDtoSaved;
    //    }

    @Override
    /**
     * @param outputPath: directory containing output file generated by the tool
     * @param networkDto
     * @param toolDto
     * @throws IOException
     */
    public List<OutputFileDTO> saveOutputFileOnDB(File outputPath, NetworkDTO networkDto, ToolDTO toolDto, String outputSuffix)
        throws IOException {
        log.info("Save tool's output file on DB..");
        List<OutputFileDTO> outputFileDtoSaved = new ArrayList<OutputFileDTO>();

        File[] files = outputPath.listFiles(
            new FilenameFilter() {
                @Override
                public boolean accept(File directory, String fileName) {
                    if (outputSuffix == null || fileName.endsWith(outputSuffix)) {
                        return true;
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

        MultipartFile multipartFile = new MockMultipartFile(
            configFile.getName(),
            configFile.getName(),
            //Files.probeContentType(configFile.toPath()),
            FileUtils.probeContentType(configFile.toPath()),
            Files.readAllBytes(configFile.toPath())
        );
        simulationDto.setConfigFile(multipartFile.getBytes());
        simulationDto.setConfigFileContentType(multipartFile.getContentType());
        SimulationDTO simulationDtoSaved = simulationService.save(simulationDto);
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
}
