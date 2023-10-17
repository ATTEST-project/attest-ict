package com.attest.ict.web.rest;

import com.attest.ict.repository.BusRepository;
import com.attest.ict.service.BusQueryService;
import com.attest.ict.service.BusService;
import com.attest.ict.service.criteria.BusCriteria;
import com.attest.ict.service.dto.BusDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link com.attest.ict.domain.Bus}.
 */
@RestController
@RequestMapping("/api")
public class BusAdditionalResource {

    private final Logger log = LoggerFactory.getLogger(BusAdditionalResource.class);

    private static final String ENTITY_NAME = "bus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusService busService;

    private final BusRepository busRepository;

    private final BusQueryService busQueryService;

    public BusAdditionalResource(BusService busService, BusRepository busRepository, BusQueryService busQueryService) {
        this.busService = busService;
        this.busRepository = busRepository;
        this.busQueryService = busQueryService;
    }

    /**
     * {@code GET  /busesNoPagination} : Returns all filtered buses based on the criteria set, without using pagination
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of buses in body.
     */
    @GetMapping("/busesLessPagination")
    public ResponseEntity<List<BusDTO>> getAllBuses(BusCriteria criteria) {
        log.debug("REST request to get Buses by criteria: {}", criteria);
        List<BusDTO> results = busQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(results);
    }
}
