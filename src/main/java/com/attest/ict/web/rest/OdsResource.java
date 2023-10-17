package com.attest.ict.web.rest;

import com.attest.ict.config.ToolsConfiguration;
import com.attest.ict.custom.utils.DateFormatter;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.domain.Network;
import com.attest.ict.helper.ods.utils.OdsFileFormat;
import com.attest.ict.service.NetworkService;
import com.attest.ict.service.OdsNetworkService;
import com.attest.ict.web.rest.errors.BadRequestAlertException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ods")
public class OdsResource {

    private final Logger log = LoggerFactory.getLogger(OdsResource.class);

    @Autowired
    OdsNetworkService odsNetworkService;

    @Autowired
    NetworkService networkService;

    @Autowired
    ToolsConfiguration toolsConfiguration;

    @GetMapping("/export/{networkId}")
    public ResponseEntity<?> exportNetworkFile(@PathVariable("networkId") Long networkId) {
        log.debug("REST request to export '.ods' file  for networkId: {}", networkId);
        Optional<Network> networkOpt = networkService.findById(networkId);
        if (networkId == null) {
            throw new BadRequestAlertException("Invalid id", "exportOds", "idnull");
        }

        if (!networkOpt.isPresent()) {
            log.debug("Network not found: {}", networkId);
            throw new BadRequestAlertException("Network not found", "exportOds", "idnotfound");
        }

        try {
            Network network = networkOpt.get();
            String toolsRootDir = toolsConfiguration.getPath(); // eg C:\\ATTEST\\Tools
            log.debug("toolsRootDir {} ", toolsRootDir);

            StringBuffer sb = new StringBuffer();
            sb
                .append(toolsRootDir)
                .append(DateFormatter.getCurrentDateTimeFormatted())
                .append("_")
                .append(network.getName().trim())
                .append(OdsFileFormat.FILE_EXTENSION);
            String fileName = sb.toString();

            log.debug("File name to create: {}", fileName);
            ByteArrayResource resource = new ByteArrayResource(odsNetworkService.exportNetworkToOdsFile(network.getId()).toByteArray());

            log.debug("File: {} created succesfully ", fileName);
            return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType(FileUtils.CONTENT_TYPE.get("ods")))
                .body(resource);
        } catch (Exception ioe) {
            log.info("Exception: " + ioe.getMessage());
            return new ResponseEntity<>("Error export ods file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
