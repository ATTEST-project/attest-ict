package com.attest.ict.web.rest;

import com.attest.ict.repository.BranchRepository;
import com.attest.ict.service.BranchQueryService;
import com.attest.ict.service.BranchService;
import com.attest.ict.service.criteria.BranchCriteria;
import com.attest.ict.service.dto.BranchDTO;
import com.attest.ict.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.attest.ict.domain.Branch}.
 */
@RestController
@RequestMapping("/api")
public class BranchResource {

    private final Logger log = LoggerFactory.getLogger(BranchResource.class);

    private static final String ENTITY_NAME = "branch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BranchService branchService;

    private final BranchRepository branchRepository;

    private final BranchQueryService branchQueryService;

    public BranchResource(BranchService branchService, BranchRepository branchRepository, BranchQueryService branchQueryService) {
        this.branchService = branchService;
        this.branchRepository = branchRepository;
        this.branchQueryService = branchQueryService;
    }

    /**
     * {@code POST  /branches} : Create a new branch.
     *
     * @param branchDTO the branchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new branchDTO, or with status {@code 400 (Bad Request)} if the branch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/branches")
    public ResponseEntity<BranchDTO> createBranch(@RequestBody BranchDTO branchDTO) throws URISyntaxException {
        log.debug("REST request to save Branch : {}", branchDTO);
        if (branchDTO.getId() != null) {
            throw new BadRequestAlertException("A new branch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BranchDTO result = branchService.save(branchDTO);
        return ResponseEntity
            .created(new URI("/api/branches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /branches/:id} : Updates an existing branch.
     *
     * @param id the id of the branchDTO to save.
     * @param branchDTO the branchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated branchDTO,
     * or with status {@code 400 (Bad Request)} if the branchDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the branchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/branches/{id}")
    public ResponseEntity<BranchDTO> updateBranch(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BranchDTO branchDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Branch : {}, {}", id, branchDTO);
        if (branchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, branchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!branchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BranchDTO result = branchService.save(branchDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, branchDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /branches/:id} : Partial updates given fields of an existing branch, field will ignore if it is null
     *
     * @param id the id of the branchDTO to save.
     * @param branchDTO the branchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated branchDTO,
     * or with status {@code 400 (Bad Request)} if the branchDTO is not valid,
     * or with status {@code 404 (Not Found)} if the branchDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the branchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/branches/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BranchDTO> partialUpdateBranch(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BranchDTO branchDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Branch partially : {}, {}", id, branchDTO);
        if (branchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, branchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!branchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BranchDTO> result = branchService.partialUpdate(branchDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, branchDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /branches} : get all the branches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of branches in body.
     */
    @GetMapping("/branches")
    public ResponseEntity<List<BranchDTO>> getAllBranches(BranchCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Branches by criteria: {}", criteria);
        Page<BranchDTO> page = branchQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /branches/count} : count all the branches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/branches/count")
    public ResponseEntity<Long> countBranches(BranchCriteria criteria) {
        log.debug("REST request to count Branches by criteria: {}", criteria);
        return ResponseEntity.ok().body(branchQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /branches/:id} : get the "id" branch.
     *
     * @param id the id of the branchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the branchDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/branches/{id}")
    public ResponseEntity<BranchDTO> getBranch(@PathVariable Long id) {
        log.debug("REST request to get Branch : {}", id);
        Optional<BranchDTO> branchDTO = branchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(branchDTO);
    }

    /**
     * {@code DELETE  /branches/:id} : delete the "id" branch.
     *
     * @param id the id of the branchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/branches/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        log.debug("REST request to delete Branch : {}", id);
        branchService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
