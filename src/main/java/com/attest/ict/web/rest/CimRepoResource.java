package com.attest.ict.web.rest;

import com.attest.ict.service.CimRepoService;
import com.attest.ict.service.dto.custom.CimRepoNetworkDTO;
import com.attest.ict.service.dto.custom.CimRepositoryNetworkDTO;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing CIM repository networks and data.
 */
@RestController
@RequestMapping("/api")
public class CimRepoResource {

    private final Logger log = LoggerFactory.getLogger(CimRepoResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CimRepoService cimRepoService;

    public CimRepoResource(CimRepoService cimRepoService) {
        this.cimRepoService = cimRepoService;
    }

    /**
     * {@code GET  /cimRepoNetworks} : get networks stored in CIM repository.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of networks in body.
     * @throws IOException
     */
    @GetMapping("/cimRepoNetworks")
    public ResponseEntity<List<CimRepositoryNetworkDTO>> getNetworks() throws IOException {
        log.debug("REST request to get networks stored in CIM repository");
        List<CimRepositoryNetworkDTO> entityList = cimRepoService.getNetworks();
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code POS  /cimRepoNetworks} : import network from CIM repository to ATTEST database.
     *
     * @param cimRepoNetworkDTO the network to import
     * @return the list of ATTEST database ids of networks imported
     * @throws IOException
     */
    @PostMapping("/cimRepoNetworks")
    public ResponseEntity<List<Integer>> importNetwork(@Valid @RequestBody CimRepoNetworkDTO cimRepoNetworkDTO) throws IOException {
        log.debug("REST request to import network from CIM repository: {}", cimRepoNetworkDTO);
        List<Integer> networkIds = cimRepoService.importNetwork(cimRepoNetworkDTO);
        return new ResponseEntity<>(networkIds, HttpStatus.OK);
    }

    /**
     * Exception handler for handling ConstraintViolationException from Hibernate.
     * @param e The ConstraintViolationException that was thrown.
     * @return A ResponseEntity containing an error message and HTTP status code.
     */
    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(org.hibernate.exception.ConstraintViolationException e) {
        String constraintName = e.getConstraintName();
        String errorMessage = "Constraint '" + constraintName + "' violation: ";
        if (constraintName.contains("network.UC_NETWORKNAME_COL")) {
            errorMessage = "A network with the same name already exists!";
        }
        log.debug("handleConstraintViolationException() return message: {}", errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(java.net.ConnectException.class)
    public ResponseEntity<String> handleCimRepoConnectionException(java.net.ConnectException e) {
        String errorMessage = "Unable to connect to CIM_REPO: " + e.getMessage();
        log.debug("handleCimRepoConnectionException() return message: {}", errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
