package com.attest.ict.web.rest;

import com.attest.ict.custom.message.ResponseMessage;
import com.attest.ict.custom.tools.utils.ToolVarName;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.helper.excel.reader.T44FileOutputFormat;
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
import com.attest.ict.service.dto.custom.T41ResultsDTO;
import com.attest.ict.service.dto.custom.T44ResultsDTO;
import com.attest.ict.service.dto.custom.TSGResultsDTO;
import com.attest.ict.service.dto.custom.ToolExecutionResponseDTO;
import com.attest.ict.service.impl.ToolWp4ExecutionServiceImpl;
import com.attest.ict.tools.constants.ToolFileFormat;
import com.attest.ict.tools.exception.RunningToolException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
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
     *
     * @param networkId
     * @param toolName
     * @param filesDesc
     * @param files
     * @param parameterNames
     * @param parameterValues
     * @param profilesId
     * @return
     */
    @PostMapping("/tools/wp4/run")
    public ResponseEntity<?> run(
        @RequestParam("networkId") Long networkId, // test case id selected by the user before running the tool
        @RequestParam("toolName") String toolName,
        @RequestParam("filesDesc") String[] filesDesc,
        // @RequestParam("files") MultipartFile[] files, // input files
        @RequestParam(name = "files", required = false) MultipartFile[] files,
        @RequestParam(name = "parameterNames", required = false) String[] parameterNames,
        @RequestParam(name = "parameterValues", required = false) String[] parameterValues,
        @RequestParam(name = "profilesId", required = false) String[] profilesId
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
                !toolName.equals(ToolVarName.T41_WIND_AND_PV) &&
                !toolName.equals(ToolVarName.T41_TRACTABILITY) &&
                !toolName.equals(ToolVarName.T44_AS_DAY_HEAD_TX)
            ) {
                return new ResponseEntity<>("Tool: " + toolName + " is not part of WP4!", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (toolName.equals(ToolVarName.T41_WIND_AND_PV)) {
                uuid =
                    toolWp4ExecutionServiceImpl.windAndPV(
                        networkDtoOpt.get(),
                        toolDtoOpt.get(),
                        filesDesc,
                        files,
                        parameterNames,
                        parameterValues
                    );
                ToolExecutionResponseDTO runResponse = new ToolExecutionResponseDTO(SUCCESS, uuid);
                return new ResponseEntity<>(runResponse, HttpStatus.OK);
            }

            if (toolName.equals(ToolVarName.T41_TRACTABILITY)) {
                uuid =
                    toolWp4ExecutionServiceImpl.tractability(
                        networkDtoOpt.get(),
                        toolDtoOpt.get(),
                        filesDesc,
                        files,
                        parameterNames,
                        parameterValues
                    );
                ToolExecutionResponseDTO runResponse = new ToolExecutionResponseDTO(SUCCESS, uuid);
                return new ResponseEntity<>(runResponse, HttpStatus.OK);
            } else {
                uuid =
                    toolWp4ExecutionServiceImpl.t44AsDayheadTx(
                        networkDtoOpt.get(),
                        toolDtoOpt.get(),
                        filesDesc,
                        files,
                        parameterNames,
                        parameterValues
                    );
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

    /**
     *
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
            log.debug("Request to show table results for tool: {}", toolName);

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

            Map<String, Integer> countMap = new HashMap<String, Integer>();
            if (toolName.equals(ToolVarName.T44_AS_DAY_HEAD_TX)) {
                countMap = toolWp4ShowResultsImpl.t44TableResults(networkDtoOpt.get(), toolDtoOpt.get(), uuid);
            }
            if (countMap == null) {
                return new ResponseEntity<>("Tool ends without generate output file ", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(countMap, HttpStatus.OK);
            }
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
        @RequestParam(name = "type", required = false) String type
    ) {
        try {
            log.debug("Request to show chart results for tool: {}, Contingency: {}, Scenario {}, type: {}", toolName, nConting, nSc, type);

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

            // Normal/OPF are valid
            if (
                type != null &&
                !type.equals(T44FileOutputFormat.OUTPUT_FILES_EXTENSION.get(1)) &&
                !type.equals(T44FileOutputFormat.OUTPUT_FILES_EXTENSION.get(2))
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
                    chart = toolWp4ShowResultsImpl.t44ChartsByNContingAndNsc(networkDtoOpt.get(), toolDtoOpt.get(), uuid, nConting, nSc);
                }
                // Chart request for particular Normal or OPF output results
                if (type != null) {
                    chart = toolWp4ShowResultsImpl.t44ChartsByType(networkDtoOpt.get(), toolDtoOpt.get(), uuid, type);
                }
                return new ResponseEntity<>(chart, HttpStatus.OK);
            }

            if (toolName.equals(ToolVarName.T41_TRACTABILITY)) {
                T41ResultsDTO chart = toolWp4ShowResultsImpl.t41Charts(networkDtoOpt.get(), toolDtoOpt.get(), uuid);
                return new ResponseEntity<>(chart, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("ToolName is not valid ", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/tools/wp4/download")
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

            File[] filesOutputResults = toolWp4ShowResultsImpl.getOutputFile(networkDtoOpt.get(), toolDtoOpt.get(), uuid);

            if (filesOutputResults == null) {
                return new ResponseEntity<>("output results for tool:  " + toolName + " not found!", HttpStatus.NOT_FOUND);
            }

            if (filesOutputResults.length == 1) {
                Path fileToDownloadPath = filesOutputResults[0].toPath();
                //String mimeType = Files.probeContentType(fileToDownloadPath);
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
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ZipOutputStream zipOutputStream = new ZipOutputStream(bos);
                for (File file : filesOutputResults) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zipEntry.setSize(file.length());
                    zipOutputStream.putNextEntry(zipEntry);
                    StreamUtils.copy(new FileInputStream(file), zipOutputStream);
                    zipOutputStream.closeEntry();
                }
                zipOutputStream.finish();
                zipOutputStream.close();

                ByteArrayResource resource = new ByteArrayResource(bos.toByteArray());

                return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + archiveFileName)
                    .contentType(MediaType.parseMediaType(ToolFileFormat.ZIP_CONTENT_MEDIA_TYPE))
                    .body(resource);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
