package com.attest.ict.web.rest;

import com.attest.ict.repository.BranchRepository;
import com.attest.ict.service.BranchQueryService;
import com.attest.ict.service.BranchService;
import com.attest.ict.service.criteria.BranchCriteria;
import com.attest.ict.service.dto.BranchDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link com.attest.ict.domain.Bus}.
 */
@RestController
@RequestMapping("/api")
public class BranchAdditionalResource {

    private final Logger log = LoggerFactory.getLogger(BranchAdditionalResource.class);

    private static final String ENTITY_NAME = "branch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BranchService branchService;

    private final BranchRepository branchRepository;

    private final BranchQueryService branchQueryService;

    public BranchAdditionalResource(BranchService branchService, BranchRepository branchRepository, BranchQueryService branchQueryService) {
        this.branchService = branchService;
        this.branchRepository = branchRepository;
        this.branchQueryService = branchQueryService;
    }

    /**
     * {@code GET  /branchesNoPagination} : Returns all filtered branches based on the criteria set, without using pagination
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of buses in body.
     */
    @GetMapping("/branchesNoPagination")
    public ResponseEntity<List<BranchDTO>> getAllBranches(BranchCriteria criteria) {
        log.debug("REST request to get Branch by criteria: {} - no pagination", criteria);
        List<BranchDTO> results = branchQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(results);
    }
}
