package com.attest.ict.web.rest;

import com.attest.ict.constants.AttestConstants;
import com.attest.ict.custom.message.ResponseMessage;
//import com.attest.ict.custom.service.NetworkService;
import com.attest.ict.domain.Network;
import com.attest.ict.helper.ExcelHelper;
import com.attest.ict.helper.MHelper;
import com.attest.ict.helper.MatHelper;
import com.attest.ict.helper.ProtectionToolsHelper;
import com.attest.ict.helper.TopologyBusesHelper;
import com.attest.ict.helper.TopologyHelper;
import com.attest.ict.helper.ods.reader.OdsFileReader;
import com.attest.ict.repository.NetworkRepository;
import com.attest.ict.service.FileService;
import com.attest.ict.service.InputFileService;
import com.attest.ict.service.NetworkService;
import com.attest.ict.service.ProtectionToolService;
import com.attest.ict.service.TopologyBusService;
import com.attest.ict.service.TopologyService;
import com.attest.ict.service.dto.InputFileDTO;
import com.attest.ict.service.mapper.NetworkMapper;
import com.attest.ict.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
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

    @GetMapping("/export-data/{networkName}")
    public ResponseEntity<Resource> exportData(@PathVariable("networkName") String networkName) throws IOException {
        InputStreamResource file = new InputStreamResource(fileServiceImpl.getNetworkData(networkName));
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + networkName + ".m")
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .body(file);
    }

    @GetMapping("/download-file/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws URISyntaxException {
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
     *  uploads the file to backend: excel and/or matpower, matlab, ods files formates are allowed at the moment
     */
    @PostMapping("/upload-network")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file, String networkName) throws ParseException {
        log.debug("Request to upload Network File for network: {}", networkName);

        String message = "";
        if (file.isEmpty()) {
            message = "Please upload a file!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        Optional<Network> networkOpt = networkRepository.findByName(networkName);
        if (!networkOpt.isPresent()) {
            message = "Sorry! Network with name: " + networkName + " not found!";
            log.error(message);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
        }

        Boolean networkFileExists = inputFileInputServiceImpl.isNetworkFileAvailable(networkOpt.get().getId());
        if (networkFileExists) {
            message = "Sorry! There is already a file uploded for the network selected!";
            log.error(message);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }

        // save  file into inputFile Table
        inputFileInputServiceImpl.saveFileForNetworkWithDescr(
            file,
            networkMapper.toDto(networkOpt.get()),
            AttestConstants.INPUT_FILE_NETWORK_DESCR
        );

        //20220823 add possibility to upload network dataset from '.ods' file format
        if (
            //   ExcelHelper.hasExcelFormat(file) ||
            //  MatHelper.hasMatlabFormat(file) ||
            MHelper.isDotMFile(file) || OdsFileReader.hasOdsFormat(file)
        ) {
            try {
                fileServiceImpl.save(file, networkOpt.get());
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                log.error(message, e);
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }

        if (file.isEmpty()) {
            message = "Please upload a file!";
        } else {
            message = "Please upload  .m or .ods file!";
        }

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
