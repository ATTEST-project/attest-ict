package com.attest.ict.web.rest;

import com.attest.ict.custom.tools.utils.ToolVarName;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.repository.ToolRepository;
import com.attest.ict.service.*;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.custom.*;
import com.attest.ict.tools.constants.T31FileFormat;
import com.attest.ict.tools.constants.T32FileFormat;
import com.attest.ict.tools.constants.T33FileFormat;
import com.attest.ict.tools.exception.RunningToolException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
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
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ToolWp3Resource {

    private final Logger log = LoggerFactory.getLogger(ToolWp3Resource.class);

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
    ToolWp3ExecutionService toolWp3ExecutionServiceImpl;

    @Autowired
    ToolWp3ShowResultsService toolWp3ShowResultsImpl;

    @Autowired
    ToolRepository toolRepository;

    @PostMapping("/tools/wp3/run")
    public ResponseEntity<?> run(
        @RequestParam("networkId") Long networkId,
        @RequestParam("toolName") String toolName,
        @RequestParam(name = "files", required = false) MultipartFile[] files,
        @RequestParam("jsonConfig") String jsonConfig
    ) throws URISyntaxException {
        log.info(
            "REST request for running T31 : networkId: {}, toolName: {}, files: {},  jsonConfig: {}",
            networkId,
            toolName,
            files,
            jsonConfig
        );
        final String SUCCESS = "ok";
        final String FAILURE = "ko";
        try {
            // -- verify if toolName is compliant to the tool's numm (T31)
            if (!toolName.equals(ToolVarName.T31_OPT_TOOL_DX)) {
                return new ResponseEntity<>("Tool: " + toolName + " is not valid!", HttpStatus.BAD_REQUEST);
            }
            // -- check if the tool exists
            Optional<ToolDTO> toolDtoOpt = toolExecutionServiceImpl.findToolByName(toolName);
            if (!toolDtoOpt.isPresent()) {
                return new ResponseEntity<>("Tool: " + toolName + " not found!", HttpStatus.NOT_FOUND);
            }
            // -- check if network exists
            Optional<NetworkDTO> networkDtoOpt = networkService.findOne(networkId);
            if (!networkDtoOpt.isPresent()) return new ResponseEntity<>(
                "Network with id: " + networkId + " not found!",
                HttpStatus.NOT_FOUND
            );
            Map<String, String> configMap = toolWp3ExecutionServiceImpl.prepareT31WorkingDir(
                networkDtoOpt.get(),
                toolDtoOpt.get(),
                files,
                jsonConfig
            );

            CompletableFuture t31RunAsync = toolExecutionServiceImpl.asyncRun(
                networkDtoOpt.get(),
                toolDtoOpt.get(),
                configMap,
                Arrays.asList(T31FileFormat.ALL_OUTPUT_SUFFIX)
            );
            log.info("END request for running T31 : {}", t31RunAsync);
            ToolExecutionResponseDTO runResponse = new ToolExecutionResponseDTO(SUCCESS, "");
            return new ResponseEntity<>(runResponse, HttpStatus.OK);
        } catch (RunningToolException rte) {
            ToolExecutionResponseDTO resp = new ToolExecutionResponseDTO(FAILURE, "");
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/tools/wp3/t32/run")
    public ResponseEntity<?> runT32(
        @RequestParam("networkId") Long networkId,
        @RequestParam(name = "profileId", required = false) Long profileId,
        @RequestParam("toolName") String toolName,
        @RequestParam("files") MultipartFile[] files,
        @RequestParam("jsonConfig") String jsonConfig
    ) throws URISyntaxException {
        log.debug("REST request for running toolName: {}", toolName);

        final String SUCCESS = "ok";
        final String FAILURE = "ko";

        // -- check if the tool exists
        Optional<ToolDTO> toolDtoOpt = toolExecutionServiceImpl.findToolByName(toolName);
        if (!toolDtoOpt.isPresent()) {
            return new ResponseEntity<>("Tool: " + toolName + " not found!", HttpStatus.NOT_FOUND);
        }

        // -- check if network exists
        Optional<NetworkDTO> networkDtoOpt = networkService.findOne(networkId);
        if (!networkDtoOpt.isPresent()) return new ResponseEntity<>("Network with id: " + networkId + " not found!", HttpStatus.NOT_FOUND);

        try {
            Map<String, String> configMap = toolWp3ExecutionServiceImpl.prepareT32WorkingDir(
                networkDtoOpt.get(),
                toolDtoOpt.get(),
                profileId,
                files,
                jsonConfig
            );
            CompletableFuture t32RunAsync = toolExecutionServiceImpl.asyncRun(
                networkDtoOpt.get(),
                toolDtoOpt.get(),
                configMap,
                T32FileFormat.OUTPUT_SUFFIX
            );
            log.info("END t32RunAsync: {}", t32RunAsync);
            ToolExecutionResponseDTO runResponse = new ToolExecutionResponseDTO(SUCCESS, "");
            return new ResponseEntity<>(runResponse, HttpStatus.OK);
        } catch (RunningToolException rte) {
            ToolExecutionResponseDTO resp = new ToolExecutionResponseDTO(FAILURE, "");
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param networkId  Transmission network's id
     * @param toolName   Name of the tool
     * @param files    Zip file <test_cases>.zip (Test cases provided by t33 developper)
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/tools/wp3/t33/run")
    public ResponseEntity<?> runT33(
        @RequestParam("networkId") Long networkId,
        @RequestParam("toolName") String toolName,
        @RequestParam("files") MultipartFile[] files,
        @RequestParam("jsonConfig") String jsonConfig
    ) throws URISyntaxException {
        log.debug("REST request for tool T33, params: {}, {}, {}, {} ", networkId, toolName, files[0], jsonConfig);
        final String SUCCESS = "ok";
        final String FAILURE = "ko";

        // -- check if the tool exists
        Optional<ToolDTO> toolDtoOpt = toolExecutionServiceImpl.findToolByName(toolName);
        if (!toolDtoOpt.isPresent()) {
            return new ResponseEntity<>("Tool: " + toolName + " not found!", HttpStatus.NOT_FOUND);
        }

        // -- check if netwrok exists
        Optional<NetworkDTO> networkDtoOpt = networkService.findOne(networkId);
        if (!networkDtoOpt.isPresent()) return new ResponseEntity<>("Network with id: " + networkId + " not found!", HttpStatus.NOT_FOUND);

        MultipartFile zipFile = files[0];
        try {
            Map<String, String> configMap = toolWp3ExecutionServiceImpl.prepareT33WorkingDir(
                networkDtoOpt.get(),
                toolDtoOpt.get(),
                zipFile,
                jsonConfig
            );

            for (Map.Entry<String, String> entry : configMap.entrySet()) {
                log.debug(" ConfigMap: " + entry.getKey() + ":" + entry.getValue());
            }
            CompletableFuture t33RunAsync = toolExecutionServiceImpl.asyncRun(
                networkDtoOpt.get(),
                toolDtoOpt.get(),
                configMap,
                T33FileFormat.FILE_OUTPUT_SUFFIX
            );
            log.info("END t33RunAsync: {}", t33RunAsync);

            ToolExecutionResponseDTO runResponse = new ToolExecutionResponseDTO(SUCCESS, "");
            return new ResponseEntity<>(runResponse, HttpStatus.OK);
        } catch (RunningToolException rte) {
            ToolExecutionResponseDTO resp = new ToolExecutionResponseDTO(FAILURE, "");
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tools/wp3/download")
    public ResponseEntity<?> downloadResult(
        @RequestParam("networkId") Long networkId,
        @RequestParam("toolName") String toolName,
        @RequestParam("uuid") String uuid
    ) {
        try {
            log.debug("Request to download output file  for tool: {}", toolName);

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

            File fileToDownload = toolWp3ShowResultsImpl.getOutputFile(networkDtoOpt.get(), toolDtoOpt.get(), uuid);
            if (fileToDownload == null) {
                return new ResponseEntity<>("output results for tool:  " + toolName + " not found!", HttpStatus.NOT_FOUND);
            }

            Path fileToDownloadPath = fileToDownload.toPath();
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(fileToDownloadPath));
            //String mimeType = Files.probeContentType(fileToDownloadPath);
            String mimeType = FileUtils.probeContentType(fileToDownloadPath);

            return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileToDownload.getName())
                .contentType(MediaType.parseMediaType(mimeType))
                .body(resource);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/tools/wp3/show-table")
    public ResponseEntity<?> showTable(
        @RequestParam("networkId") Long networkId,
        @RequestParam("toolName") String toolName,
        @RequestParam("uuid") String uuid
    ) {
        try {
            log.debug("Request to prepare list of results produced by the tool: {}", toolName);
            // -- check if the tool exists
            Optional<ToolDTO> toolDtoOpt = toolExecutionServiceImpl.findToolByName(toolName);
            if (!toolDtoOpt.isPresent()) {
                return new ResponseEntity<>("Tool: " + toolName + " not found!", HttpStatus.NOT_FOUND);
            }

            if (!toolName.equals(ToolVarName.T33_OPT_TOOL_PLAN_TSO_DSO)) {
                return new ResponseEntity<>(" Service not available for Tool: " + toolName, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // -- check if network exists
            Optional<NetworkDTO> networkDtoOpt = networkService.findOne(networkId);
            if (!networkDtoOpt.isPresent()) return new ResponseEntity<>(
                "Network with id: " + networkId + " not found!",
                HttpStatus.NOT_FOUND
            );

            T33ResultsPagesDTO results = toolWp3ShowResultsImpl.getPagesToShow(networkDtoOpt.get(), toolDtoOpt.get(), uuid);
            return new ResponseEntity<>(results, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/tools/wp3/show-charts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> showTableAndCharts(
        @RequestParam("networkId") Long networkId,
        @RequestParam("toolName") String toolName,
        @RequestParam("uuid") String uuid
    ) {
        try {
            log.debug("Request to show-charts for tool: {} , networkId {},  uuid {} ", toolName, networkId, uuid);

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

            if (!toolName.equals(ToolVarName.T31_OPT_TOOL_DX) && !toolName.equals(ToolVarName.T32_OPT_TOOL_TX)) {
                return new ResponseEntity<>("Tool: " + toolName + " is not part of WP3!", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            File fileToDownload = toolWp3ShowResultsImpl.getOutputFile(networkDtoOpt.get(), toolDtoOpt.get(), uuid);
            String json = "";
            if (fileToDownload == null) {
                return new ResponseEntity<>("Output results for tool:  " + toolName + " not found!", HttpStatus.NOT_FOUND);
            }

            try (InputStream stream = new FileInputStream(fileToDownload)) {
                json = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
            } catch (IOException ioe) {
                log.error("Couldn't fetch JSON! Error: ", ioe.getMessage());
                return new ResponseEntity<>(ioe.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(json, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/tools/wp3/T33/show-charts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> showTableAndCharts(
        @RequestParam("networkId") Long networkId,
        @RequestParam("toolName") String toolName,
        @RequestParam("uuid") String uuid,
        @RequestParam(name = "node", required = false) String node,
        @RequestParam(name = "day", required = false) String day,
        @RequestParam(name = "title", required = false) String title // required for T33
    ) {
        try {
            log.debug(
                "Request to show-charts for tool: {} , networkId {},  uuid {}, node {}, day {}, title {}",
                toolName,
                networkId,
                uuid,
                node,
                day,
                title
            );

            if (!toolName.equals(ToolVarName.T33_OPT_TOOL_PLAN_TSO_DSO)) {
                return new ResponseEntity<>("Tool: " + toolName + " is not part of WP3!", HttpStatus.INTERNAL_SERVER_ERROR);
            }

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

            TableDataDTO resultsDTO = toolWp3ShowResultsImpl.getTablesData(networkDtoOpt.get(), toolDtoOpt.get(), uuid, node, day, title);

            return new ResponseEntity<>(resultsDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
