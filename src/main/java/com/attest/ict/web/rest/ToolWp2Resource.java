package com.attest.ict.web.rest;

import com.attest.ict.custom.message.ResponseMessage;
import com.attest.ict.custom.tools.utils.ToolVarName;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.repository.ToolRepository;
import com.attest.ict.service.*;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.custom.ToolExecutionResponseDTO;
import com.attest.ict.tools.constants.ToolFileFormat;
import com.attest.ict.tools.constants.WP2FileFormat;
import com.attest.ict.tools.exception.RunningToolException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ToolWp2Resource {

    private final Logger log = LoggerFactory.getLogger(ToolWp2Resource.class);

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
    ToolWp2ExecutionService toolWp2ExecutionServiceImpl;

    @Autowired
    ToolRepository toolRepository;

    /**
     *
     * @param networkId
     * @param toolName
     * @param files
     * @param jsonConfig
     *
     * @return
     */
    @PostMapping("/tools/wp2/run")
    public ResponseEntity<?> run(
        @RequestParam("networkId") Long networkId, // test case id selected by the user before running the tool
        @RequestParam("toolName") String toolName,
        @RequestParam(name = "files", required = false) MultipartFile[] files, // input files
        @RequestParam("jsonConfig") String jsonConfig
    ) {
        log.debug("REST Request to run tool: {}", toolName);

        final String SUCCESS = "ok";
        final String FAILURE = "ko";
        String uuid = "";

        try {
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

            if (files == null || files.length == 0) {
                String errMsg = "Please upload files needed by the tool!";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(errMsg));
            }

            if (
                !toolName.equals(ToolVarName.T25_DAY_AHEAD_TOOL) &&
                !toolName.equals(ToolVarName.T25_REAL_TIME_TOOL) &&
                !toolName.equals(ToolVarName.T26_MARKET_SIMUL)
            ) {
                return new ResponseEntity<>("Tool: " + toolName + " is not part of WP2!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Map<String, String> configMap = Collections.emptyMap();
            if (!toolName.equals(ToolVarName.T26_MARKET_SIMUL)) {
                configMap = toolWp2ExecutionServiceImpl.prepareT25WorkingDir(networkDtoOpt.get(), toolDtoOpt.get(), files, jsonConfig);
                CompletableFuture t25Async = toolExecutionServiceImpl.asyncRun(
                    networkDtoOpt.get(),
                    toolDtoOpt.get(),
                    configMap,
                    WP2FileFormat.DOWNLOAD_FILES_EXTENSION
                );
                log.info("END t25Async: {}", t25Async);
                //uuid = toolWp2ExecutionServiceImpl.t25(networkDtoOpt.get(), toolDtoOpt.get(), files, jsonConfig);
                //log.info("t25 end return uuid: {} ",uuid);
                ToolExecutionResponseDTO runResponse = new ToolExecutionResponseDTO(SUCCESS, "");
                return new ResponseEntity<>(runResponse, HttpStatus.OK);
            } else {
                configMap = toolWp2ExecutionServiceImpl.prepareT26WorkingDir(networkDtoOpt.get(), toolDtoOpt.get(), files, jsonConfig);
                CompletableFuture t26Async = toolWp2ExecutionServiceImpl.t26Async(networkDtoOpt.get(), toolDtoOpt.get(), configMap);
                log.info("END t26Async: {}", t26Async);
                ToolExecutionResponseDTO runResponse = new ToolExecutionResponseDTO(SUCCESS, uuid);
                return new ResponseEntity<>(runResponse, HttpStatus.OK);
            }
        } catch (RunningToolException rte) {
            ToolExecutionResponseDTO resp = new ToolExecutionResponseDTO(FAILURE, "");
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/tools/wp2/download")
    public ResponseEntity<?> downloadResult(
        @RequestParam("networkId") Long networkId,
        @RequestParam("toolName") String toolName,
        @RequestParam("uuid") String uuid
    ) {
        try {
            log.debug("Request to download output file for tool: {}", toolName);

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

            // build zip file
            ByteArrayOutputStream bos = null;
            try {
                bos = toolExecutionServiceImpl.zipOutputResults(networkDtoOpt.get(), toolDtoOpt.get(), uuid);
                ByteArrayResource resource = new ByteArrayResource(bos.toByteArray());
                String archiveFileName = uuid.concat(ToolFileFormat.ZIP_EXTENSION);
                return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + archiveFileName)
                    .contentType(MediaType.parseMediaType(FileUtils.CONTENT_TYPE.get("zip")))
                    .body(resource);
            } catch (IOException ex) {
                log.error("Error creating output (.zip) archive :" + ex.getCause());
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
