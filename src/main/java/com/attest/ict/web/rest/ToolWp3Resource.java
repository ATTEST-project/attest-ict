package com.attest.ict.web.rest;

import com.attest.ict.custom.tools.utils.ToolVarName;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.helper.excel.reader.T41FileOutputFormat;
import com.attest.ict.repository.ToolRepository;
import com.attest.ict.service.InputFileService;
import com.attest.ict.service.NetworkService;
import com.attest.ict.service.OutputFileService;
import com.attest.ict.service.TaskService;
import com.attest.ict.service.ToolExecutionService;
import com.attest.ict.service.ToolWp3ExecutionService;
import com.attest.ict.service.ToolWp3ShowResultsService;
import com.attest.ict.service.UserService;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.custom.T31InputParamDTO;
import com.attest.ict.service.dto.custom.ToolExecutionResponseDTO;
import com.attest.ict.tools.exception.RunningToolException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
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
    public ResponseEntity<?> run(@RequestBody T31InputParamDTO t31TestCaseParam) throws URISyntaxException {
        log.debug("REST request for tool running : {}", t31TestCaseParam.toString());

        String toolName = t31TestCaseParam.getToolName();
        Long networkId = t31TestCaseParam.getNetworkId();

        final String SUCCESS = "ok";
        final String FAILURE = "ko";
        String uuid = "";

        try {
            // -- chech if the tool is T31
            if (!toolName.equals(ToolVarName.T31_OPT_TOOL_DX)) {
                return new ResponseEntity<>("Tool: " + toolName + " is not valid!", HttpStatus.BAD_REQUEST);
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

            uuid = toolWp3ExecutionServiceImpl.t31Run(networkDtoOpt.get(), toolDtoOpt.get(), t31TestCaseParam);
            ToolExecutionResponseDTO runResponse = new ToolExecutionResponseDTO(SUCCESS, uuid);
            return new ResponseEntity<>(runResponse, HttpStatus.OK);
        } catch (RunningToolException rte) {
            ToolExecutionResponseDTO resp = new ToolExecutionResponseDTO(FAILURE, "");
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //TODO
    @PostMapping("/tools/wp3/t32/run")
    public ResponseEntity<?> run(
        @RequestParam("networkId") Long networkId,
        @RequestParam(name = "profileId", required = false) Long profileId,
        @RequestParam("toolName") String toolName,
        @RequestParam("files") MultipartFile[] files,
        @RequestParam("jsonConfig") String jsonConfig
    ) throws URISyntaxException {
        log.debug("REST request for tool running : {}");

        final String SUCCESS = "ok";
        final String FAILURE = "ko";
        String uuid = "";

        // -- check if the tool exists
        Optional<ToolDTO> toolDtoOpt = toolExecutionServiceImpl.findToolByName(toolName);
        if (!toolDtoOpt.isPresent()) {
            return new ResponseEntity<>("Tool: " + toolName + " not found!", HttpStatus.NOT_FOUND);
        }

        // -- check if netwrok exists
        Optional<NetworkDTO> networkDtoOpt = networkService.findOne(networkId);
        if (!networkDtoOpt.isPresent()) return new ResponseEntity<>("Network with id: " + networkId + " not found!", HttpStatus.NOT_FOUND);

        try {
            uuid = toolWp3ExecutionServiceImpl.t32Run(networkDtoOpt.get(), toolDtoOpt.get(), profileId, files, jsonConfig);
            ToolExecutionResponseDTO runResponse = new ToolExecutionResponseDTO(SUCCESS, uuid);
            return new ResponseEntity<>(runResponse, HttpStatus.OK);
        } catch (RunningToolException rte) {
            ToolExecutionResponseDTO resp = new ToolExecutionResponseDTO(FAILURE, "");
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/tools/wp3/show-charts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> showCharts(
        @RequestParam("networkId") Long networkId,
        @RequestParam("toolName") String toolName,
        @RequestParam("uuid") String uuid
    ) {
        try {
            log.debug("Request to show-charts for tool: {} , networkId {} ", toolName, networkId);

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
                return new ResponseEntity<>("Output results for tool:  " + toolName + " not found!", HttpStatus.NOT_FOUND);
            }

            String json = "";
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
}
