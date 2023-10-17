package com.attest.ict.web.rest;

import com.attest.ict.service.dto.custom.CimRepoNetworkDTO;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing network from cim repo.
 */
@RestController
@RequestMapping("/api")
public class CimRepoFakeResource {

    private final Logger log = LoggerFactory.getLogger(CimRepoFakeResource.class);

    public CimRepoFakeResource() {}

    /**
     * POST /fakeCimRepoNetworks : Import a network from cimRepo.
     * This endpoint is used to import a network from cimRepo into ATTEST DB. The network data is sent in the request body as an Object.
     * @param cimRepoNetworDTO The network data to be imported from cimRepo.
     * @return A ResponseEntity containing a List of Integer values as a sample response. The List contains example data with values 1 and 2.
     * @throws URISyntaxException If there is an issue with the URI syntax.
     */

    @PostMapping("/fakeCimRepoNetworks")
    public ResponseEntity<?> importNetwork(@Valid @RequestBody CimRepoNetworkDTO cimRepoNetworDTO) throws URISyntaxException {
        log.debug("REST request to import Network from cimRepo: {}", cimRepoNetworDTO);
        //TODO implement the logic and than return a list of the network's id created
        //-- the following values are a sample response
        List<Integer> sampleResponse = new ArrayList<>();
        sampleResponse.add(1);
        sampleResponse.add(2);

        return new ResponseEntity<>(sampleResponse, HttpStatus.OK);
    }

    /**
     * GET /fakeCimRepoNetworks : Retrieve all CimNetworks.
     * This endpoint is used to retrieve all CimNetworks. The method reads the CimNetwork data from a JSON file and returns it as a JSON response.
     * @return A ResponseEntity containing the JSON data of all CimNetworks read from the JSON file. The data is returned as a String in JSON format.
     */
    @GetMapping(value = "/fakeCimRepoNetworks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllCimNetworks() {
        //TODO implement the logic and than return a json file
        //--  sample response
        String json;
        log.debug("REST request to get all CimNetworks ");
        String sampleData = "src\\test\\resources\\json_file\\cim-network-sample-data.json";
        try (InputStream stream = new FileInputStream(sampleData)) {
            json = StreamUtils.copyToString(stream, StandardCharsets.UTF_8);
        } catch (IOException ioe) {
            log.error("Couldn't fetch JSON! Error: ", ioe);
            return new ResponseEntity<>(ioe.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}
