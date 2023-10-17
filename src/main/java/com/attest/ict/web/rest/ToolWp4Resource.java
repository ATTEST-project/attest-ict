package com.attest.ict.web.rest;

import com.attest.ict.custom.message.ResponseMessage;
import com.attest.ict.custom.tools.utils.ToolVarName;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.helper.excel.reader.T41FileOutputFormat;
import com.attest.ict.helper.excel.reader.T44V3FileOutputFormat;
import com.attest.ict.repository.ToolRepository;
import com.attest.ict.service.InputFileService;
import com.attest.ict.service.NetworkService;
import com.attest.ict.service.OutputFileService;
import com.attest.ict.service.TaskService;
import com.attest.ict.service.ToolExecutionService;
import com.attest.ict.service.ToolWp4ShowResultsService;
import com.attest.ict.service.UserService;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.dto.custom.*;
import com.attest.ict.service.impl.ToolWp4ExecutionServiceImpl;
import com.attest.ict.tools.constants.T45FileFormat;
import com.attest.ict.tools.constants.TSGFileFormat;
import com.attest.ict.tools.constants.ToolFileFormat;
import com.attest.ict.tools.exception.RunningToolException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
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
public class ToolWp4Resource {

    private final Logger log = LoggerFactory.getLogger(ToolWp4Resource.class);

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
    ToolWp4ShowResultsService toolWp4ShowResultsImpl;

    @Autowired
    ToolWp4ExecutionServiceImpl toolWp4ExecutionServiceImpl;

    @Autowired
    ToolRepository toolRepository;

    /**
     * @param networkId
     * @param toolName
     * @param filesDesc
     * @param files
     * @param parameterNames
     * @param parameterValues
     * @param profilesId
     * @param otherToolOutputFileIds
     * @return
     */
    @PostMapping("/tools/wp4/run")
    public ResponseEntity<?> run(
        @RequestParam("networkId") Long networkId, // test case id selected by the user before running the tool
        @RequestParam("toolName") String toolName,
        @RequestParam(name = "filesDesc", required = false) String[] filesDesc,
        @RequestParam(name = "files", required = false) MultipartFile[] files,
        @RequestParam(name = "parameterNames", required = false) String[] parameterNames,
        @RequestParam(name = "parameterValues", required = false) String[] parameterValues,
        @RequestParam(name = "profilesId", required = false) String[] profilesId,
        @RequestParam(name = "otherToolOutputFileIds", required = false) Long[] otherToolOutputFileIds // t44OutputFileIds
    ) {
        log.info("REST Request to run tool: {}", toolName);

        final String SUCCESS = "ok";
        //final String SUCCESS = "Tool's is running";
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
                !toolName.equals(ToolVarName.T41_WIND_AND_PV) &&
                !toolName.equals(ToolVarName.T41_TRACTABILITY) &&
                !toolName.equals(ToolVarName.T44_AS_DAY_HEAD_TX) &&
                !toolName.equals(ToolVarName.T42_AS_REAL_TIME_DX) &&
                !toolName.equals(ToolVarName.T45_AS_REAL_TIME_TX)
            ) {
                return new ResponseEntity<>("Tool: " + toolName + " is not part of WP4!", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (toolName.equals(ToolVarName.T41_WIND_AND_PV)) {
                Map<String, String> configMap = toolWp4ExecutionServiceImpl.prepareTSGWorkingDir(
                    networkDtoOpt.get(),
                    toolDtoOpt.get(),
                    filesDesc,
                    files,
                    parameterNames,
                    parameterValues
                );
                CompletableFuture tsgRunAsync = toolExecutionServiceImpl.asyncRun(
                    networkDtoOpt.get(),
                    toolDtoOpt.get(),
                    configMap,
                    TSGFileFormat.OUTPUT_SUFFIX
                );
                log.info("END T41 WIND PV asyncRun: {}", tsgRunAsync);
                ToolExecutionResponseDTO runResponse = new ToolExecutionResponseDTO(SUCCESS, "");
                return new ResponseEntity<>(runResponse, HttpStatus.OK);
            }

            if (toolName.equals(ToolVarName.T41_TRACTABILITY)) {
                Map<String, String> configMap = toolWp4ExecutionServiceImpl.prepareT41WorkingDir(
                    networkDtoOpt.get(),
                    toolDtoOpt.get(),
                    filesDesc,
                    files,
                    parameterNames,
                    parameterValues
                );
                CompletableFuture t41RunAsync = toolExecutionServiceImpl.asyncRun(
                    networkDtoOpt.get(),
                    toolDtoOpt.get(),
                    configMap,
                    T41FileOutputFormat.OUTPUT_FILES_EXTENSION_LIST
                );
                log.info("END T41 asyncRun(): {}", t41RunAsync);
                ToolExecutionResponseDTO runResponse = new ToolExecutionResponseDTO(SUCCESS, "");
                return new ResponseEntity<>(runResponse, HttpStatus.OK);
            }

            if (toolName.equals(ToolVarName.T44_AS_DAY_HEAD_TX)) {
                Map<String, String> configMap = toolWp4ExecutionServiceImpl.prepareT44V3WorkingDir(
                    networkDtoOpt.get(),
                    toolDtoOpt.get(),
                    filesDesc,
                    files,
                    parameterNames,
                    parameterValues
                );
                CompletableFuture t44RunAsync = toolExecutionServiceImpl.asyncRun(
                    networkDtoOpt.get(),
                    toolDtoOpt.get(),
                    configMap,
                    T44V3FileOutputFormat.OUTPUT_FILE_EXTENSION
                );
                log.info("END T44 V3 asyncRun: {}", t44RunAsync);

                ToolExecutionResponseDTO runResponse = new ToolExecutionResponseDTO(SUCCESS, "");
                return new ResponseEntity<>(runResponse, HttpStatus.OK);
            }
            // -- T42
            if (toolName.equals(ToolVarName.T42_AS_REAL_TIME_DX)) {
                Map<String, String> configMap = toolWp4ExecutionServiceImpl.prepareT42WorkingDir(
                    networkDtoOpt.get(),
                    toolDtoOpt.get(),
                    filesDesc,
                    files,
                    parameterNames,
                    parameterValues,
                    otherToolOutputFileIds
                );

                CompletableFuture t45RunAsync = toolExecutionServiceImpl.asyncRun(
                    networkDtoOpt.get(),
                    toolDtoOpt.get(),
                    configMap,
                    T45FileFormat.OUTPUT_SUFFIX
                );
                log.info("END T42 asyncRun: {}", t45RunAsync);

                ToolExecutionResponseDTO runResponse = new ToolExecutionResponseDTO(SUCCESS, "");
                return new ResponseEntity<>(runResponse, HttpStatus.OK);
            }
            // t45
            else {
                Map<String, String> configMap = toolWp4ExecutionServiceImpl.prepareT45WorkingDir(
                    networkDtoOpt.get(),
                    toolDtoOpt.get(),
                    filesDesc,
                    files,
                    parameterNames,
                    parameterValues,
                    otherToolOutputFileIds
                );
                CompletableFuture t45RunAsync = toolExecutionServiceImpl.asyncRun(
                    networkDtoOpt.get(),
                    toolDtoOpt.get(),
                    configMap,
                    T45FileFormat.OUTPUT_SUFFIX
                );
                log.info("END T45 asyncRun: {}", t45RunAsync);

                ToolExecutionResponseDTO runResponse = new ToolExecutionResponseDTO(SUCCESS, "");
                return new ResponseEntity<>(runResponse, HttpStatus.OK);
            }
        } catch (RunningToolException rte) {
            ToolExecutionResponseDTO resp = new ToolExecutionResponseDTO(FAILURE, "");
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param networkId
     * @param toolName
     * @param uuid
     * @return
     */
    @GetMapping("/tools/wp4/show-table")
    public ResponseEntity<?> showTable(
        @RequestParam("networkId") Long networkId, // test case id selected by the user // before running the tool
        @RequestParam("toolName") String toolName,
        @RequestParam("uuid") String uuid
    ) {
        try {
            log.info("Request to show table results for tool: {}", toolName);

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

            switch (toolName) {
                case ToolVarName.T41_TRACTABILITY:
                    return t41PagesToShow(networkDtoOpt.get(), toolDtoOpt.get(), uuid);
                case ToolVarName.T44_AS_DAY_HEAD_TX:
                    return t44PagesToShow(networkDtoOpt.get(), toolDtoOpt.get(), uuid);
                case ToolVarName.T42_AS_REAL_TIME_DX:
                case ToolVarName.T45_AS_REAL_TIME_TX:
                    return t42T45PagesToShow(networkDtoOpt.get(), toolDtoOpt.get(), uuid);
            }
            return new ResponseEntity<>("Tool doesn't allow this action ", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tools/wp4/show-charts")
    public ResponseEntity<?> showCharts(
        @RequestParam("networkId") Long networkId,
        @RequestParam("toolName") String toolName,
        @RequestParam("uuid") String uuid,
        @RequestParam(name = "nConting", required = false) Integer nConting,
        @RequestParam(name = "nSc", required = false) Integer nSc,
        @RequestParam(name = "type", required = false) String type,
        @RequestParam(name = "title", required = false) String title, //used by T41 to identify which sheet's name visualize,
        @RequestParam(name = "timePeriod", required = false) String timePeriod // used by T45 in chart visualization
    ) {
        try {
            log.info(
                "Request to show chart results for tool: {}, Contingency: {}, Scenario {}, type: {}, timePeriod: {}",
                toolName,
                nConting,
                nSc,
                type,
                timePeriod
            );

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

            // Normal and _postConting is valid
            if (
                type != null &&
                !type.equals(T44V3FileOutputFormat.OUTPUT_FILES_SUFFIX.get(0)) &&
                !type.equals(T44V3FileOutputFormat.OUTPUT_FILES_SUFFIX.get(1))
            ) {
                String msgErr = "Type: " + type + " is not valid for tool: " + toolName;
                log.warn(msgErr);
                return new ResponseEntity<>(msgErr, HttpStatus.BAD_REQUEST);
            }

            if (nConting != null && nSc != null) {
                if (nConting < 1) {
                    String msgErr = "nConting: " + nConting + " is not valid for tool: " + toolName;
                    log.warn(msgErr);
                    return new ResponseEntity<>(msgErr, HttpStatus.BAD_REQUEST);
                }

                if (nSc < 1) {
                    String msgErr = "nSc: " + nSc + " is not valid for tool: " + toolName;
                    log.warn(msgErr);
                    return new ResponseEntity<>(msgErr, HttpStatus.BAD_REQUEST);
                }
            }

            if (toolName.equals(ToolVarName.T41_WIND_AND_PV)) {
                TSGResultsDTO charts = toolWp4ShowResultsImpl.windAndPVCharts(networkDtoOpt.get(), toolDtoOpt.get(), uuid);
                return new ResponseEntity<>(charts, HttpStatus.OK);
            }

            if (toolName.equals(ToolVarName.T44_AS_DAY_HEAD_TX)) {
                T44ResultsDTO chart = null;
                // Chart request for particular Contingency and Scenario
                if (nConting != null && nSc != null) {
                    chart = toolWp4ShowResultsImpl.t44V3ChartsByNContingAndNsc(networkDtoOpt.get(), toolDtoOpt.get(), uuid, nConting, nSc);
                }
                // Chart request for particular Normal or OPF output results. NB: OPF doesn't exist anymore in t44v3
                if (type != null) {
                    chart = toolWp4ShowResultsImpl.t44V3ChartsByType(networkDtoOpt.get(), toolDtoOpt.get(), uuid, type);
                }
                return new ResponseEntity<>(chart, HttpStatus.OK);
            }

            if (toolName.equals(ToolVarName.T41_TRACTABILITY)) {
                return this.t41showTableAndCharts(networkDtoOpt.get(), toolDtoOpt.get(), uuid, title, nSc);
            }

            if (toolName.equals(ToolVarName.T42_AS_REAL_TIME_DX) || toolName.equals(ToolVarName.T45_AS_REAL_TIME_TX)) {
                return this.t42T45showCharts(networkDtoOpt.get(), toolDtoOpt.get(), uuid, timePeriod);
            }

            return new ResponseEntity<>("ToolName is not valid ", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 2023/05 new service to show table and charts in T41

    public ResponseEntity<?> t41showTableAndCharts(NetworkDTO networkDto, ToolDTO toolDto, String uuid, String title, Integer nSc)
        throws Exception {
        log.info(
            "Request to show  results for tool: {}, networkId: {},  uuid: {},  title: {}, nSc: {}, isFlexData: {}",
            toolDto.getName(),
            networkDto.getId(),
            uuid,
            title,
            nSc,
            T41FileOutputFormat.FLEX_SHEETS_NAME.contains(title)
        );
        if (title != null && T41FileOutputFormat.SHEETS_NAME.contains(title)) {
            T41TableDataDTO resultsTableAndCharts = toolWp4ShowResultsImpl.t41TablesData(networkDto, toolDto, uuid, title, nSc);
            return new ResponseEntity<>(resultsTableAndCharts, HttpStatus.OK);
        } else {
            T41FlexResultsDTO chart = toolWp4ShowResultsImpl.t41Charts(networkDto, toolDto, uuid);
            return new ResponseEntity<>(chart, HttpStatus.OK);
        }
    }

    public ResponseEntity<?> t42T45showCharts(NetworkDTO networkDto, ToolDTO toolDto, String uuid, String timePeriod) throws Exception {
        T42T45FlexResultsDTO chart = toolWp4ShowResultsImpl.t42T45Charts(networkDto, toolDto, uuid, timePeriod);
        return new ResponseEntity<>(chart, HttpStatus.OK);
    }

    @GetMapping(value = "/tools/wp4/download")
    public ResponseEntity<?> downloadResult(
        @RequestParam("networkId") Long networkId,
        @RequestParam("toolName") String toolName,
        @RequestParam("uuid") String uuid
    ) {
        try {
            log.debug("Request output results file for tool: {}", toolName);

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

            File[] filesOutputResults = toolWp4ShowResultsImpl.getOutputFile(networkDtoOpt.get(), toolDtoOpt.get(), uuid);

            if (filesOutputResults.length == 1) {
                Path fileToDownloadPath = filesOutputResults[0].toPath();
                String mimeType = FileUtils.probeContentType(fileToDownloadPath);
                ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(fileToDownloadPath));
                return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileToDownloadPath.getFileName())
                    .contentType(MediaType.parseMediaType(mimeType))
                    .body(resource);
            } else {
                // build zip file
                String archiveFileName = uuid.concat(ToolFileFormat.ZIP_EXTENSION);
                //ByteArrayOutputStream bos = FileUtils.zipFiles(filesOutputResults);
                byte[] zipByteArray = toolWp4ShowResultsImpl.zipOutputDir(networkDtoOpt.get(), toolDtoOpt.get(), uuid);
                ByteArrayResource resource = new ByteArrayResource(zipByteArray);
                return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + archiveFileName)
                    .contentType(MediaType.parseMediaType(FileUtils.CONTENT_TYPE.get("zip")))
                    .body(resource);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<?> t41PagesToShow(NetworkDTO networkDTO, ToolDTO toolDTO, String uuid) {
        try {
            T41ResultsPagesDTO t41PagesToShow = toolWp4ShowResultsImpl.t41PagesToShow(networkDTO, toolDTO, uuid);
            return new ResponseEntity<>(t41PagesToShow, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<?> t44PagesToShow(NetworkDTO networkDTO, ToolDTO toolDTO, String uuid) {
        try {
            T44ResultsPagesDTO pagesToShow = toolWp4ShowResultsImpl.t44V3PagesToShow(networkDTO, toolDTO, uuid);
            return new ResponseEntity<>(pagesToShow, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<?> t42T45PagesToShow(NetworkDTO networkDTO, ToolDTO toolDTO, String uuid) {
        try {
            ToolResultsPagesDTO t45PagesToShow = toolWp4ShowResultsImpl.t42T45PagesToShow(networkDTO, toolDTO, uuid);
            return new ResponseEntity<>(t45PagesToShow, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
