package com.attest.ict.web.rest;

import com.attest.ict.config.ToolsConfiguration;
import com.attest.ict.custom.message.ResponseMessage;
import com.attest.ict.custom.tools.python.PythonScript;
import com.attest.ict.custom.tools.utils.ToolVarName;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.domain.OutputFile;
import com.attest.ict.helper.TaskStatus;
import com.attest.ict.repository.ToolRepository;
import com.attest.ict.service.InputFileService;
import com.attest.ict.service.NetworkService;
import com.attest.ict.service.OutputFileService;
import com.attest.ict.service.TaskService;
import com.attest.ict.service.ToolExecutionService;
import com.attest.ict.service.ToolWp3ExecutionService;
import com.attest.ict.service.ToolWp4ExecutionService;
import com.attest.ict.service.UserService;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.TaskDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.custom.ToolExecutionResponseDTO;
import com.attest.ict.tools.exception.RunningToolException;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//LP =======
//DAVE
import org.springframework.beans.factory.annotation.Value;
//DAVE >>>>>>> jhi-frontend
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class ToolExecutionResource {

    private final Logger log = LoggerFactory.getLogger(ToolExecutionResource.class);

    private ToolsConfiguration toolsConfig;

    //Override by toolsRootPath
    // private static final String CURRENT_DIR = System.getProperty("user.dir");

    // set into application application-*.yml  actually is ATTEST/TOOL
    private String toolsRootPath;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Autowired
    NetworkService networkService;

    @Autowired
    InputFileService inputFileService;

    @Autowired
    OutputFileService outputFileService;

    // CUSTOM service
    @Autowired
    ToolExecutionService toolExecutionServiceImpl;

    @Autowired
    ToolWp4ExecutionService toolWp4ExecutionServiceImpl;

    @Autowired
    ToolWp3ExecutionService toolWp3ExecutionServiceImpl;

    @Autowired
    ToolRepository toolRepository;

    public ToolExecutionResource(ToolsConfiguration toolsConfig) {
        this.toolsRootPath = toolsConfig.getPath();
        log.debug("toolsRootPath {}", toolsRootPath);
    }

    /*
     * 1) create a .zip file with folders containing source code (.jl or .py)
     * if possible, use this naming convention for zip and folders
     * julia_tools.zip
     * |_ Wind and PV Scenario Gen Tool_v1.0
     * |_ Stochastic MP-ACOPF benchmark tool_v1.0
     * dayahead_tool.zip
     * |_ T2.5 - Day-ahead aggregator tool
     * 2) then use this api to upload .zip file and unzip automatically under
     * 'fileName' folder
     * or make it manually copying the tools always in 'fileName' folder
     * 3) now you can run other julia/python apis
     */
    // EX @PostMapping("/uploadSourcesZip")
    @PostMapping("/tools/upload-source")
    public ResponseEntity<String> uploadSourcesZip(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf('.'));

        try {
            // check if .zip file has been already extracted
            File path = new File(fileName);
            if (path.exists()) {
                return new ResponseEntity<>("File already extracted!", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // extract under 'fileName' folder
            String dir = "./" + fileName;
            FileUtils.unzipFile(file, dir);
            return new ResponseEntity<>("Extracted sources of " + fileName + " successfully!", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>("Error extracting sources of " + fileName, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // LP da tenere per jhipster
    // EX @PostMapping("/uploadInputs")
    @PostMapping("/tools/upload-inputs")
    public ResponseEntity<String> uploadInputs(
        @RequestParam("networkName") String networkName,
        @RequestParam("tool") String toolName,
        @RequestParam("files") MultipartFile[] files
    ) {
        log.debug("Request to upload Input file for tool: {}", toolName);

        Optional<ToolDTO> toolOpt = toolExecutionServiceImpl.findToolByName(toolName);
        if (!toolOpt.isPresent()) return new ResponseEntity<>("Tool not found!", HttpStatus.NOT_FOUND);

        Optional<NetworkDTO> networkOpt = networkService.findByName(networkName);
        if (!networkOpt.isPresent()) return new ResponseEntity<>("Network not found!", HttpStatus.NOT_FOUND);

        String response = "Input files uploaded successfully";

        try {
            switch (toolName) {
                case ToolVarName.T25_DAY_AHEAD_TOOL:
                    toolExecutionServiceImpl.uploadDATInputs(files, networkOpt.get(), toolOpt.get());
                    break;
                case ToolVarName.T25_REAL_TIME_TOOL:
                    toolExecutionServiceImpl.uploadRTTInputs(files, networkOpt.get(), toolOpt.get());
                    break;
                case ToolVarName.T41_WIND_AND_PV:
                    toolExecutionServiceImpl.uploadWindPVInput(files[0], networkOpt.get(), toolOpt.get());
                    break;
                case ToolVarName.T41_STOCHASTIC_MP_ACOPF:
                    toolExecutionServiceImpl.uploadStochasticMPInput(files[0], networkOpt.get(), toolOpt.get());
                    break;
                case ToolVarName.T51_CHARACTERIZATION:
                    response = toolExecutionServiceImpl.uploadAssetInput(files[0], networkOpt.get(), toolOpt.get());
                    break;
                case ToolVarName.T51_MONITORING:
                    toolExecutionServiceImpl.uploadAssetMonitoringInput(files[0], networkOpt.get(), toolOpt.get());
                    break;
                default:
                    return new ResponseEntity<>("Tool does not exist", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>("Error uploading files", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * this api is to run day ahead tool, upload three input files required with /uploadInputs API:
     * Input Data - Networks.xlsx
     * Input Data - Resources.xlsx
     * Input Data - Other.xlsx
     */

    //@PostMapping("/dayAheadTool")
    @PostMapping("/tools/run/T25-day-ahead")
    public ResponseEntity<String> execDayAheadTool(@RequestParam("networkName") String networkName) {
        String toolName = ToolVarName.T25_DAY_AHEAD_TOOL; // "T25_dayahead";

        Optional<ToolDTO> toolDtoOpt = toolExecutionServiceImpl.findToolByName(toolName);
        String toolPath = toolExecutionServiceImpl.getToolPath(toolsRootPath, toolDtoOpt.get());
        List<String> inputFileNames = Arrays.asList("Input Data - Networks.xlsx", "Input Data - Resources.xlsx", "Input Data - Other.xlsx");
        String mainToolFile = "main.py";

        log.debug("Request to run tool: {}, on Network: {} ", toolName, networkName);

        Optional<NetworkDTO> networkOpt = networkService.findByName(networkName);
        if (!networkOpt.isPresent()) {
            log.error("Network: {} non found! ", networkName);
            return new ResponseEntity<>("Network not found in database", HttpStatus.BAD_REQUEST);
        }

        TaskDTO taskSaved;
        try {
            taskSaved = toolExecutionServiceImpl.createTask(toolDtoOpt.get());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            Long networkId = networkOpt.get().getId();
            toolExecutionServiceImpl.createTmpInputsForTools(inputFileNames, networkId, toolPath, toolName);
            List<Path> fileList = Files
                .walk(Paths.get(toolPath + File.separator))
                .filter(Files::isRegularFile)
                .filter(f -> toolExecutionServiceImpl.checkInputFileNameDAT(f.getFileName().toString()))
                .collect(Collectors.toList());

            if (fileList.size() < 3) {
                toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
                log.error("Error executing tool: {}, on Network: {}, missing one or more required input files ", toolName, networkName);
                return new ResponseEntity<>("One or more input files not present, please upload", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // run python 'main.py' file
            Process process = PythonScript.executeProcess(toolPath + "/" + mainToolFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            FileUtils.writeLogsToFile(reader, toolPath + "/" + toolName + "_log.txt");
            if (process.exitValue() == 0) {
                Files
                    .walk(Paths.get(toolPath + File.separator))
                    .filter(Files::isRegularFile)
                    .filter(f -> toolExecutionServiceImpl.checkInputFileNameDAT(f.getFileName().toString()))
                    .forEach(f -> FileUtils.removeFileFromPath(f.toString()));

                toolExecutionServiceImpl.uploadOutputFilesToDB(
                    Paths.get(toolPath + "/Bids_aggregator.xls"),
                    networkOpt.get(),
                    toolDtoOpt.get()
                );
            } else {
                toolExecutionServiceImpl.uploadToolLogFileToDB(Paths.get(toolPath + "/" + toolName + "_log.txt"), taskSaved, true);
                toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
                log.error("Tool: {}, on Network: {}, FAILED ", toolName, networkName);
                return new ResponseEntity<>("Error executing " + toolName, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            toolExecutionServiceImpl.uploadToolLogFileToDB(Paths.get(toolPath + "/" + toolName + "_log.txt"), taskSaved, true);
            toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.PASSED);
            log.info("Tool: {}, on Network: {}, end successfully ", toolName, networkName);
            return new ResponseEntity<>(toolName + " run successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception running the tool : " + toolName, e.getMessage());
            toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            return new ResponseEntity<>(
                "Request to run tool:" + toolName + " on Network:  " + networkName + " API Error",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /*
     * this api is to run real time tool, upload inputs required using /uploadInputs API:
     * Input Data - Networks.xlsx
     * Input Data - Resources.xlsx
     * Input Data - Other.xlsx
     * Input Data - AGC.xlsx
     * Input Data - DA bids.xlsx
     */
    //LP da tenere con jhipster
    //EX @PostMapping("/realTimeTool")
    @PostMapping("/tools/run/T25-real-time")
    public ResponseEntity<String> execRealTimeTool(@RequestParam("networkName") String networkName) {
        String toolName = ToolVarName.T25_REAL_TIME_TOOL; //  "T2.5 Real-time";
        Optional<ToolDTO> toolDtoOpt = toolExecutionServiceImpl.findToolByName(toolName);
        String toolPath = toolExecutionServiceImpl.getToolPath(toolsRootPath, toolDtoOpt.get());

        log.debug("Request to run tool: {}, on Network: {} ", toolName, networkName);

        List<String> inputFileNames = Arrays.asList(
            "Input Data - Networks.xlsx",
            "Input Data - Resources.xlsx",
            "Input Data - Other.xlsx",
            "Input Data - AGC.xlsx",
            "Input Data - DA bids.xlsx"
        );
        String mainToolFile = "main.py";

        //-- Find network by Name
        Optional<NetworkDTO> networkOpt = networkService.findByName(networkName);
        if (!networkOpt.isPresent()) {
            log.error("Network: {} non found! ", networkName);
            return new ResponseEntity<>("Network not found!", HttpStatus.NOT_FOUND);
        }

        // long taskId = taskServiceImpl.createTask(toolName, info);
        TaskDTO taskSaved;
        try {
            taskSaved = toolExecutionServiceImpl.createTask(toolDtoOpt.get());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            Long networkId = networkOpt.get().getId();
            toolExecutionServiceImpl.createTmpInputsForTools(inputFileNames, networkId, toolPath, toolName);

            List<Path> fileList = Files
                .walk(Paths.get(toolPath + File.separator))
                .filter(Files::isRegularFile)
                .filter(f -> toolExecutionServiceImpl.checkInputFileNameRTT(f.getFileName().toString()))
                .collect(Collectors.toList());

            if (fileList.size() < inputFileNames.size()) {
                toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
                log.error("Error executing tool: {}, on Network: {}, missing one or more required input files ", toolName, networkName);
                return new ResponseEntity<>("One or more input files not present, please upload", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // run python 'main.py' file
            Process process = PythonScript.executeProcess(toolPath + "/" + mainToolFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            FileUtils.writeLogsToFile(reader, toolPath + "/" + toolName + "_log.txt");
            if (process.exitValue() == 0) {
                log.debug("Tool: {} end succesfully! Starting to remove input file from: {}", toolName, toolPath);
                Files
                    .walk(Paths.get(toolPath + File.separator))
                    .filter(Files::isRegularFile)
                    .filter(f -> toolExecutionServiceImpl.checkInputFileNameRTT(f.getFileName().toString()))
                    .forEach(f -> FileUtils.removeFileFromPath(f.toString()));

                toolExecutionServiceImpl.uploadOutputFilesToDB(
                    Paths.get(toolPath + "/Bids_aggregator.xls"),
                    networkOpt.get(),
                    toolDtoOpt.get()
                );
                toolExecutionServiceImpl.uploadOutputFilesToDB(
                    Paths.get(toolPath + "/P_control_day.csv"),
                    networkOpt.get(),
                    toolDtoOpt.get()
                );
            } else {
                toolExecutionServiceImpl.uploadToolLogFileToDB(Paths.get(toolPath + "/" + toolName + "_log.txt"), taskSaved, true);
                toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
                log.error("Tool: {}, on Network: {}, FAILED ", toolName, networkName);
                return new ResponseEntity<>("Error executing " + toolName, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            toolExecutionServiceImpl.uploadToolLogFileToDB(Paths.get(toolPath + "/" + toolName + "_log.txt"), taskSaved, true);
            toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.PASSED);
            log.info("Tool: {}, on Network: {}, end successfully ", toolName, networkName);
            return new ResponseEntity<>(toolName + " run successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception running the tool : " + toolName, e.getMessage());
            toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            return new ResponseEntity<>(
                "Request to run tool:" + toolName + " on Network: {} " + networkName + " API Error",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    //-- Tool Result

    //EX @GetMapping("/getFdrPlot/{networkName}/{feeder}/{scenario}")
    @GetMapping("/tools/results/T41-fdr/{networkName}/{fdrNum}/{scNum}")
    public ResponseEntity<?> getFdrPlot(
        @PathVariable("networkName") String networkName,
        @PathVariable("fdrNum") String feeder,
        @PathVariable("scNum") String scenario
    ) {
        log.debug("Request to get T41 result Plot for Feeder: {} and scenario {}  for Network: {} ", feeder, scenario, networkName);
        //-- Find network by Name
        Optional<NetworkDTO> networkOpt = networkService.findByName(networkName);
        Long networkId = networkOpt.get().getId();

        if (!networkOpt.isPresent()) {
            log.error("Network: {} non found! ", networkName);
            return new ResponseEntity<>("Network: " + networkName + " not found in database", HttpStatus.BAD_REQUEST);
        }

        try {
            OutputFile outputFile = toolExecutionServiceImpl.getFdrPlot(networkId, feeder, scenario);
            if (outputFile == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(outputFile.getDataContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + outputFile.getFileName() + "\"")
                .body(new ByteArrayResource(outputFile.getData()));
        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // EX @GetMapping("/getVPlot/{networkName}/{scenario}")
    @GetMapping("/tools/results/T41-volt/{networkName}/{scNum}")
    public ResponseEntity<?> getVPlot(@PathVariable("networkName") String networkName, @PathVariable("scNum") String scenario) {
        log.debug("Request to get T41 result Plot for scenario {}  for Network: {} ", scenario, networkName);
        Optional<NetworkDTO> networkOpt = networkService.findByName(networkName);
        Long networkId = networkOpt.get().getId();
        if (!networkOpt.isPresent()) {
            log.error("Network: {} non found! ", networkName);
            return new ResponseEntity<>("Network: " + networkName + " not found in database", HttpStatus.BAD_REQUEST);
        }

        try {
            OutputFile outputFile = toolExecutionServiceImpl.getVPlot(networkId, scenario);

            if (outputFile == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(outputFile.getDataContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + outputFile.getFileName() + "\"")
                .body(new ByteArrayResource(outputFile.getData()));
        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/tools/t51/v1/upload-input")
    public ResponseEntity<?> uploadInputForT51V1(
        @RequestParam("networkName") String networkName,
        @RequestParam("file") MultipartFile file
    ) {
        //Dave String inputsPath = CURRENT_DIR + "/tools/WP5/T5.1/input_files";
        //Dave String toolPath = CURRENT_DIR + "/tools/WP5/T5.1/Software/main";

        String toolName = ToolVarName.T51_CHARACTERIZATION;
        Optional<ToolDTO> toolDtoOpt = toolExecutionServiceImpl.findToolByName(toolName);
        String toolPath = toolExecutionServiceImpl.getToolPath(toolsRootPath, toolDtoOpt.get());

        log.debug("ToolPath: {}", toolPath);
        log.debug("Network: {}", networkName);

        String inputsPath = toolPath + File.separator + "input_files";
        String mainFilePath = toolPath + File.separator + "Software" + File.separator + "main";

        String mainPath = "get_variables.cmd";

        try {
            Path filePath = Paths.get(inputsPath, file.getOriginalFilename());
            file.transferTo(filePath);
            String jsonPath = "\"{\"\"path\"\": \"\"" + filePath.toString().replace("\\", "/") + "\"\"}\"";
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(new File(mainFilePath));
            processBuilder.command(Arrays.asList(mainFilePath + "/" + mainPath, jsonPath));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            // Process process = PythonScript.executeProcessWithArgs(toolPath + "/" + mainPath, Arrays.asList("--json", jsonPath));
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("{\"variables")) {
                    builder.append(line);
                    builder.append(System.getProperty("line.separator"));
                }
            }
            String result = builder.toString();
            return ResponseEntity.ok().headers(HeaderUtil.createAlert(applicationName, "Uploaded file successfully!", "")).body(result);
        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>("Error uploading input file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/tools/run/v1/t51")
    public ResponseEntity<?> execT51ToolV1(@RequestParam("networkName") String networkName, @RequestParam("jsonConfig") String jsonConfig) {
        String toolName = ToolVarName.T51_CHARACTERIZATION; //  "T51_asset"
        Optional<ToolDTO> toolDtoOpt = toolExecutionServiceImpl.findToolByName(toolName);
        String toolPath = toolExecutionServiceImpl.getToolPath(toolsRootPath, toolDtoOpt.get());
        String codePath = toolPath + "/Software/main";
        String inputsPath = toolPath + "/input_files";

        log.debug("Request to run tool: {}, on Network: {} ", toolName, networkName);

        TaskDTO taskSaved;
        try {
            taskSaved = toolExecutionServiceImpl.createTask(toolDtoOpt.get());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String mainPath = "advanced_tool_environment_setup_1.cmd";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(new File(codePath));
            processBuilder.command(Arrays.asList(codePath + "/" + mainPath, jsonConfig));
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            FileUtils.writeLogsToFile(reader, toolPath + "/" + toolName + "_log.txt");
            Files.walk(Paths.get(inputsPath)).filter(Files::isRegularFile).forEach(file -> FileUtils.removeFileFromPath(file.toString()));
            toolExecutionServiceImpl.uploadToolLogFileToDB(Paths.get(toolPath + "/" + toolName + "_log.txt"), taskSaved, true);
            if (process.exitValue() == 0) {
                toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.PASSED);
                return new ResponseEntity<>(toolName + " run successfully", HttpStatus.OK);
            }
            toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            return new ResponseEntity<>("Error running " + toolName, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Exception: ", e);
            toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            return new ResponseEntity<>("Error running " + toolName, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/tools/run/v1/t51-monitoring")
    public ResponseEntity<?> execT51MonitoringToolV1(
        @RequestParam("networkName") String networkName,
        @RequestParam("jsonConfig") String jsonConfig
    ) {
        String toolName = ToolVarName.T51_MONITORING; //  "T51_asset_monitoring"
        Optional<ToolDTO> toolDtoOpt = toolExecutionServiceImpl.findToolByName(toolName);
        String toolPath = toolExecutionServiceImpl.getToolPath(toolsRootPath, toolDtoOpt.get());
        String codePath = toolPath + "/Software/main";
        String inputsPath = toolPath + "/input_files";

        log.debug("Request to run tool: {}, on Network: {} ", toolName, networkName);

        TaskDTO taskSaved;
        try {
            taskSaved = toolExecutionServiceImpl.createTask(toolDtoOpt.get());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String mainPath = "advanced_tool_environment_setup_2.cmd";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(new File(codePath));
            processBuilder.command(Arrays.asList(codePath + "/" + mainPath, "\"" + jsonConfig + "\""));
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            FileUtils.writeLogsToFile(reader, toolPath + "/" + toolName + "_log.txt");
            toolExecutionServiceImpl.uploadToolLogFileToDB(Paths.get(toolPath + "/" + toolName + "_log.txt"), taskSaved, true);
            Files.walk(Paths.get(inputsPath)).filter(Files::isRegularFile).forEach(file -> FileUtils.removeFileFromPath(file.toString()));
            if (process.exitValue() == 0) {
                toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.PASSED);
                return new ResponseEntity<>(toolName + " run successfully", HttpStatus.OK);
            }
            toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            return new ResponseEntity<>("Error running " + toolName, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Exception: ", e);
            toolExecutionServiceImpl.updateTask(taskSaved, TaskStatus.Status.FAILED);
            return new ResponseEntity<>("Error running " + toolName, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
