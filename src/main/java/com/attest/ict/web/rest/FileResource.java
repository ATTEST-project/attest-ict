package com.attest.ict.web.rest;

import com.attest.ict.custom.message.ResponseMessage;
import com.attest.ict.domain.Network;
import com.attest.ict.helper.ProtectionToolsHelper;
import com.attest.ict.helper.TopologyBusesHelper;
import com.attest.ict.helper.TopologyHelper;
import com.attest.ict.helper.matpower.common.reader.MatpowerReader;
import com.attest.ict.helper.ods.reader.OdsFileReader;
import com.attest.ict.repository.NetworkRepository;
import com.attest.ict.service.*;
import com.attest.ict.service.dto.InputFileDTO;
import com.attest.ict.service.mapper.NetworkMapper;
import com.attest.ict.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

//@Validated
//@CrossOrigin(origins = "*", allowedHeaders = "*")
//@Controller
@RestController
@RequestMapping("/api")
public class FileResource {

    private final Logger log = LoggerFactory.getLogger(FileResource.class);

    @Autowired
    InputFileService inputFileInputServiceImpl;

    @Autowired
    NetworkService networkServiceImpl;

    @Autowired
    NetworkRepository networkRepository;

    @Autowired
    NetworkMapper networkMapper;

    @Autowired
    TopologyService topologyServiceImpl;

    @Autowired
    TopologyBusService topologyBusServiceImpl;

    @Autowired
    ProtectionToolService protectionToolServiceImpl;

    @Autowired
    FileService fileServiceImpl;

    @Autowired
    OdsNetworkService odsNetworkService;

    @Autowired
    MatpowerNetworkService matpowerNetworkService;

    /**
     * Export network data to a Matpower file and provide it as a downloadable resource.
     *
     * @param networkName The name of the network to export.
     * @return A ResponseEntity containing the Matpower file as a downloadable resource.
     * @throws IOException If an error occurs during the export process or resource creation.
     */
    @GetMapping("/export-data/{networkName}")
    public ResponseEntity<Resource> exportData(@PathVariable("networkName") String networkName) throws IOException {
        log.info("REST request to export network data to a Matpower file, name of the network: {} ", networkName);
        InputStreamResource file = new InputStreamResource(matpowerNetworkService.exportToMatpowerFile(networkName));
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + networkName + ".m")
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .body(file);
    }

    @GetMapping("/download-file/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws URISyntaxException {
        log.info("REST request to download file id: {}", id);
        // Load file from database
        Optional<InputFileDTO> inputFileOpt = inputFileInputServiceImpl.findOne(id);
        if (!inputFileOpt.isPresent()) {
            throw new BadRequestAlertException("File not found", "Input_File", "idnotfound");
        }
        InputFileDTO inputFileDto = inputFileOpt.get();
        return ResponseEntity
            .ok()
            .contentType(MediaType.parseMediaType(inputFileDto.getDataContentType()))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + inputFileDto.getFileName() + "\"")
            .body(new ByteArrayResource(inputFileDto.getData()));
    }

    /**
     * Handles the import of a network from a file and associates it with a specified network name.
     *
     * @param mpFile The multipart file to be uploaded.
     * @param networkName The name of the network to associate the file with.
     * @return A ResponseEntity containing a ResponseMessage indicating the result of the upload operation.
     */
    @PostMapping("/upload-network")
    public ResponseEntity<ResponseMessage> importNetworkFromFile(@RequestParam("file") MultipartFile mpFile, String networkName) {
        log.info("REST request to import a network data from a file. NetworkName: {}", networkName);
        String message = "";
        if (mpFile.isEmpty()) {
            message = "Please select the file to upload!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        String fileName = StringUtils.cleanPath(mpFile.getOriginalFilename());
        //   Check if the file's name contains invalid characters
        if (fileName.contains("..")) {
            message = "Filename contains invalid path sequence!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        Optional<Network> networkOpt = networkRepository.findByName(networkName);
        if (!networkOpt.isPresent()) {
            message = "Network: " + networkName + " not found!";
            log.error(message);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
        }

        Boolean networkFileExists = inputFileInputServiceImpl.isNetworkFileAvailable(networkOpt.get().getId());
        if (networkFileExists) {
            message = "Sorry! There is already a file uploded for the network selected!";
            log.error(message);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage(message));
        }

        // Import from matpower file
        if (MatpowerReader.isDotMFile(mpFile)) {
            try {
                matpowerNetworkService.importFromMatpowerFile(mpFile, networkOpt.get().getId());
                message = "File : " + mpFile.getOriginalFilename() + " Uploaded Successfully!";
                log.info(message);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Error uploading file: " + mpFile.getOriginalFilename() + "!";
                log.error(message, e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
            }
        }
        //20220823 For t4.1 V2 network data test cases are generated by developper in .ods format
        // Import from Ods
        if (OdsFileReader.hasOdsFormat(mpFile)) {
            try {
                odsNetworkService.importNetworkFromOdsFile(networkOpt.get().getId(), mpFile);
                message = "File : " + mpFile.getOriginalFilename() + " Uploaded Successfully!";
                log.info(message);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Error uploading file: " + mpFile.getOriginalFilename() + "!";
                log.error(message, e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
            }
        }
        message = "File format not allowed!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

    @PostMapping("/upload-topology")
    public ResponseEntity<String> uploadTopology(@RequestParam("file") MultipartFile file) {
        try {
            List<String[]> fileParsed = TopologyHelper.readTopology(file.getInputStream());
            topologyServiceImpl.saveAllTopologies(TopologyHelper.createTopologies(fileParsed));

            log.info("Uploaded topology file successfully!");
            return new ResponseEntity<>("Uploaded topology file successfully!", HttpStatus.OK);
        } catch (Exception e) {
            log.debug(e.getMessage());
            return new ResponseEntity<>("Topology API error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload-topology-buses/{networkId}")
    public ResponseEntity<String> uploadTopologyBuses(@RequestParam("file") MultipartFile file, @PathVariable("networkId") Long networkId) {
        try {
            List<String[]> fileParsed = TopologyBusesHelper.readTopBuses(file.getInputStream());
            topologyBusServiceImpl.saveAllTopologyBuses(TopologyBusesHelper.createTopologyBuses(fileParsed, networkId));

            log.info("Uploaded topology file successfully!");
            return new ResponseEntity<>("Uploaded topology buses file successfully!", HttpStatus.OK);
        } catch (Exception e) {
            log.debug(e.getMessage());
            return new ResponseEntity<>("Topology API error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload-protection")
    public ResponseEntity<String> uploadProt(@RequestParam("file") MultipartFile file) {
        try {
            List<String[]> prots = ProtectionToolsHelper.readProt(file.getInputStream());
            protectionToolServiceImpl.saveAllProtectionTools(ProtectionToolsHelper.createProtTools(prots));

            return new ResponseEntity<>("Saved protection tools successfully!", HttpStatus.OK);
        } catch (Exception e) {
            log.info("Exception: " + e.getMessage());
            return new ResponseEntity<>("Error saving protection tools", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* getFile method
     * return an .xlsx file with data from a particular range of time (startDate and endDate)
     */
    @GetMapping("/download/{startDate}/{endDate}")
    public ResponseEntity<Resource> getFile(
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
    ) {
        String filename = "testcase.xlsx";
        InputStreamResource file = new InputStreamResource(fileServiceImpl.load(startDate, endDate));

        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
            .body(file);
    }
}
