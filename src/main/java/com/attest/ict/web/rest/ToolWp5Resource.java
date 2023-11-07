package com.attest.ict.web.rest;

import com.attest.ict.custom.message.ResponseMessage;
import com.attest.ict.custom.tools.utils.ToolVarName;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.custom.utils.MimeUtils;
import com.attest.ict.repository.ToolRepository;
import com.attest.ict.service.InputFileService;
import com.attest.ict.service.NetworkService;
import com.attest.ict.service.OutputFileService;
import com.attest.ict.service.TaskService;
import com.attest.ict.service.ToolExecutionService;
import com.attest.ict.service.ToolWp5ExecutionService;
import com.attest.ict.service.ToolWp5ShowResultsService;
import com.attest.ict.service.UserService;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.custom.ToolExecutionResponseDTO;
import com.attest.ict.tools.constants.*;
import com.attest.ict.tools.exception.RunningToolException;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ToolWp5Resource {

    private final Logger log = LoggerFactory.getLogger(ToolWp5Resource.class);

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

    @Autowired
    ToolWp5ExecutionService toolWp5ExecutionServiceImpl;

    @Autowired
    ToolWp5ShowResultsService toolWp5ShowResultsServiceImpl;

    @Autowired
    ToolExecutionService toolExecutionServiceImpl;

    @Autowired
    ToolRepository toolRepository;

    /**
     * @param mpFile
     * @return file's header
     */
    @PostMapping("/tools/wp5/file-headers")
    public ResponseEntity<?> fileHeaders(@RequestParam("file") MultipartFile mpFile) {
        try {
            if (mpFile == null) {
                String errMsg = "Please upload files needed by the tool!";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(errMsg));
            }

            log.debug("Request to get header of  the file: {}", mpFile.getOriginalFilename());
            String contentType = MimeUtils.detect(mpFile);
            if (
                !contentType.equals(FileUtils.CONTENT_TYPE.get("xlsx")) &&
                !contentType.equals(FileUtils.CONTENT_TYPE.get("csv")) && //CsvConstants.CONTENT_TYPE) &&
                !contentType.equals(FileUtils.CONTENT_TYPE.get("xls")) // "application/vnd.ms-excel")
            ) {
                String errMsg = "File not valid";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(errMsg));
            }

            String[] headers = toolWp5ExecutionServiceImpl.getFileHeaders(mpFile);

            return new ResponseEntity<>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/tools/wp5/run")
    public ResponseEntity<?> run(
        @RequestParam("networkId") Long networkId,
        @RequestParam("toolName") String toolName,
        @RequestParam("files") MultipartFile[] files,
        @RequestParam("jsonConfig") String jsonConfig
    ) {
        final String SUCCESS = "ok";
        final String FAILURE = "ko";
        String uuid = "";
        // -- check if the tool exists
        try {
            log.info("Request to run toolName: {} for networkId: {}, configFile: {}: " + toolName, networkId, jsonConfig);
            // -- check if the tool exists
            Optional<ToolDTO> toolDtoOpt = toolExecutionServiceImpl.findToolByName(toolName);
            if (!toolDtoOpt.isPresent()) {
                return new ResponseEntity<>("Tool: " + toolName + " not found!", HttpStatus.NOT_FOUND);
            }

            // -- check if netwrok exists
            Optional<NetworkDTO> networkDtoOpt = networkService.findOne(networkId);
            if (!networkDtoOpt.isPresent()) return new ResponseEntity<>(
                "Network with id: " + networkId + " not found!",
                HttpStatus.NOT_FOUND
            );

            if (
                !toolName.equals(ToolVarName.T52_INDICATOR) &&
                !toolName.equals(ToolVarName.T51_CHARACTERIZATION) &&
                !toolName.equals(ToolVarName.T51_MONITORING) &&
                !toolName.equals(ToolVarName.T53_MANAGEMENT)
            ) {
                return new ResponseEntity<>("Tool: " + toolName + " is not part of WP5!", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (toolName.equals(ToolVarName.T51_CHARACTERIZATION)) {
                try {
                    // uuid = toolWp5ExecutionServiceImpl.t511CharacterizationRun(networkDtoOpt.get(), toolDtoOpt.get(), jsonConfig, files);
                    //ToolExecutionResponseDTO runResponse = new ToolExecutionResponseDTO(SUCCESS, uuid);
                    Map<String, String> configMap = toolWp5ExecutionServiceImpl.prepareT511CharacterizationWorkingDir(
                        networkDtoOpt.get(),
                        toolDtoOpt.get(),
                        jsonConfig,
                        files
                    );
                    CompletableFuture t511CharacterizationRunAsync = toolExecutionServiceImpl.asyncRun(
                        networkDtoOpt.get(),
                        toolDtoOpt.get(),
                        configMap,
                        T51FileFormat.DOWNLOAD_FILES_EXTENSION
                    );
                    log.info("END t511CharacterizationRunAsync: {}", t511CharacterizationRunAsync);
                    ToolExecutionResponseDTO runResponse = new ToolExecutionResponseDTO(SUCCESS, "");
                    return new ResponseEntity<>(runResponse, HttpStatus.OK);
                } catch (JsonProcessingException e) {
                    String errMsg = "Error converting jsonConfig to T52InputParamDTO";
                    log.error(errMsg, e);
                    return new ResponseEntity<>("param jsonConfig param is not valid!", HttpStatus.BAD_REQUEST);
                }
            }

            if (toolName.equals(ToolVarName.T51_MONITORING)) {
                try {
                    Map<String, String> configMap = toolWp5ExecutionServiceImpl.prepareT512MonitoringWorkingDir(
                        networkDtoOpt.get(),
                        toolDtoOpt.get(),
                        jsonConfig,
                        files
                    );
                    CompletableFuture t512MonitoringRunAsync = toolExecutionServiceImpl.asyncRun(
                        networkDtoOpt.get(),
                        toolDtoOpt.get(),
                        configMap,
                        T52FileFormat.DOWNLOAD_FILES_EXTENSION
                    );
                    log.info("END t512MonitoringRunAsync: {}", t512MonitoringRunAsync);
                    ToolExecutionResponseDTO runResponse = new ToolExecutionResponseDTO(SUCCESS, "");
                    return new ResponseEntity<>(runResponse, HttpStatus.OK);
                } catch (JsonProcessingException e) {
                    String errMsg = "Error converting jsonConfig to T51MonitoringLaunchParamDTO";
                    log.error(errMsg, e);
                    return new ResponseEntity<>("param jsonConfig param is not valid!", HttpStatus.BAD_REQUEST);
                }
            } else if (toolName.equals(ToolVarName.T52_INDICATOR)) {
                try {
                    Map<String, String> configMap = toolWp5ExecutionServiceImpl.prepareT52WorkingDir(
                        networkDtoOpt.get(),
                        toolDtoOpt.get(),
                        jsonConfig,
                        files
                    );
                    CompletableFuture t52RunAsync = toolExecutionServiceImpl.asyncRun(
                        networkDtoOpt.get(),
                        toolDtoOpt.get(),
                        configMap,
                        T52FileFormat.DOWNLOAD_FILES_EXTENSION
                    );

                    log.info("END t52RunAsync: {}", t52RunAsync);
                    ToolExecutionResponseDTO runResponse = new ToolExecutionResponseDTO(SUCCESS, "");
                    return new ResponseEntity<>(runResponse, HttpStatus.OK);
                } catch (JsonProcessingException e) {
                    String errMsg = "Error converting jsonConfig to T52InputParamDTO";
                    log.error(errMsg, e);
                    return new ResponseEntity<>("param jsonConfig param is not valid!", HttpStatus.BAD_REQUEST);
                }
            }

            if (toolName.equals(ToolVarName.T53_MANAGEMENT)) {
                try {
                    Map<String, String> configMap = toolWp5ExecutionServiceImpl.prepareT53WorkingDir(
                        networkDtoOpt.get(),
                        toolDtoOpt.get(),
                        jsonConfig,
                        files
                    );
                    CompletableFuture t53RunAsync = toolExecutionServiceImpl.asyncRun(
                        networkDtoOpt.get(),
                        toolDtoOpt.get(),
                        configMap,
                        T53FileFormat.DOWNLOAD_FILES_EXTENSION
                    );

                    log.info("END t53RunAsync: {}", t53RunAsync);
                    ToolExecutionResponseDTO runResponse = new ToolExecutionResponseDTO(SUCCESS, "");
                    return new ResponseEntity<>(runResponse, HttpStatus.OK);
                } catch (JsonProcessingException e) {
                    String errMsg = "Error converting jsonConfig to T53LaunchParamDTO";
                    log.error(errMsg, e);
                    return new ResponseEntity<>("param jsonConfig param is not valid!", HttpStatus.BAD_REQUEST);
                }
            }

            return new ResponseEntity<>("Tool: " + toolName + " is not part of WP5!", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RunningToolException rte) {
            ToolExecutionResponseDTO resp = new ToolExecutionResponseDTO(FAILURE, "");
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/tools/wp5/show-table")
    public ResponseEntity<?> showTable(
        @RequestParam("networkId") Long networkId,
        @RequestParam("toolName") String toolName,
        @RequestParam("uuid") String uuid
    ) {
        try {
            log.info("Request to get results file for tool: {}", toolName);

            // -- check if the tool exists
            Optional<ToolDTO> toolDtoOpt = toolExecutionServiceImpl.findToolByName(toolName);

            if (!toolDtoOpt.isPresent()) {
                return new ResponseEntity<>("Tool: " + toolName + " not found!", HttpStatus.NOT_FOUND);
            }

            // -- check if netwrok exists
            Optional<NetworkDTO> networkDtoOpt = networkService.findOne(networkId);
            if (!networkDtoOpt.isPresent()) return new ResponseEntity<>(
                "Network with id: " + networkId + " not found!",
                HttpStatus.NOT_FOUND
            );

            List<String> resultsFileName = toolWp5ShowResultsServiceImpl.getResultsFileName(networkDtoOpt.get(), toolDtoOpt.get(), uuid);

            if (resultsFileName.isEmpty()) {
                return new ResponseEntity<>("Results for tool: " + toolName + " not found!", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(resultsFileName, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tools/wp5/show-charts")
    public ResponseEntity<?> showCharts(
        @RequestParam("networkId") Long networkId,
        @RequestParam("toolName") String toolName,
        @RequestParam("uuid") String uuid,
        @RequestParam("fileName") String fileName
    ) {
        try {
            log.info("Request to show chart's result for tool: {}, FileName: {}", toolName, fileName);

            // -- check if the tool exists
            Optional<ToolDTO> toolDtoOpt = toolExecutionServiceImpl.findToolByName(toolName);
            if (!toolDtoOpt.isPresent()) {
                return new ResponseEntity<>("Tool: " + toolName + " not found!", HttpStatus.NOT_FOUND);
            }

            // -- check if netwrok exists
            Optional<NetworkDTO> networkDtoOpt = networkService.findOne(networkId);
            if (!networkDtoOpt.isPresent()) return new ResponseEntity<>(
                "Network with id: " + networkId + " not found!",
                HttpStatus.NOT_FOUND
            );

            String html = toolWp5ShowResultsServiceImpl.readFile(networkDtoOpt.get(), toolDtoOpt.get(), uuid, fileName);
            return new ResponseEntity<>(html, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/tools/wp5/download")
    public ResponseEntity<?> downloadResult(
        @RequestParam("networkId") Long networkId,
        @RequestParam("toolName") String toolName,
        @RequestParam("uuid") String uuid
    ) {
        try {
            log.info("Request to download output file for tool: {}", toolName);

            // -- check if the tool exists
            Optional<ToolDTO> toolDtoOpt = toolExecutionServiceImpl.findToolByName(toolName);
            if (!toolDtoOpt.isPresent()) {
                return new ResponseEntity<>("Tool: " + toolName + " not found!", HttpStatus.NOT_FOUND);
            }

            // -- check if netwrok exists
            Optional<NetworkDTO> networkDtoOpt = networkService.findOne(networkId);
            if (!networkDtoOpt.isPresent()) return new ResponseEntity<>(
                "Network with id: " + networkId + " not found!",
                HttpStatus.NOT_FOUND
            );

            File[] filesOutputResults = toolWp5ShowResultsServiceImpl.downloadResultsFile(networkDtoOpt.get(), toolDtoOpt.get(), uuid);

            if (filesOutputResults == null) {
                return new ResponseEntity<>("output results for tool:  " + toolName + " not found!", HttpStatus.NOT_FOUND);
            }

            // build zip file
            String archiveFileName = uuid.concat(ToolFileFormat.ZIP_EXTENSION);
            ByteArrayOutputStream bos = FileUtils.zipFiles(filesOutputResults);
            ByteArrayResource resource = new ByteArrayResource(bos.toByteArray());

            return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + archiveFileName)
                .contentType(MediaType.parseMediaType(FileUtils.CONTENT_TYPE.get("zip")))
                .body(resource);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
