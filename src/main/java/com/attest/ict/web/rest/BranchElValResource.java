package com.attest.ict.web.rest;

import com.attest.ict.repository.BranchElValRepository;
import com.attest.ict.service.BranchElValQueryService;
import com.attest.ict.service.BranchElValService;
import com.attest.ict.service.criteria.BranchElValCriteria;
import com.attest.ict.service.dto.BranchElValDTO;
import com.attest.ict.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.attest.ict.domain.BranchElVal}.
 */
@RestController
@RequestMapping("/api")
public class BranchElValResource {

    private final Logger log = LoggerFactory.getLogger(BranchElValResource.class);

    private static final String ENTITY_NAME = "branchElVal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BranchElValService branchElValService;

    private final BranchElValRepository branchElValRepository;

    private final BranchElValQueryService branchElValQueryService;

    public BranchElValResource(
        BranchElValService branchElValService,
        BranchElValRepository branchElValRepository,
        BranchElValQueryService branchElValQueryService
    ) {
        this.branchElValService = branchElValService;
        this.branchElValRepository = branchElValRepository;
        this.branchElValQueryService = branchElValQueryService;
    }

    /**
     * {@code POST  /branch-el-vals} : Create a new branchElVal.
     *
     * @param branchElValDTO the branchElValDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new branchElValDTO, or with status {@code 400 (Bad Request)} if the branchElVal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/branch-el-vals")
    public ResponseEntity<BranchElValDTO> createBranchElVal(@RequestBody BranchElValDTO branchElValDTO) throws URISyntaxException {
        log.debug("REST request to save BranchElVal : {}", branchElValDTO);
        if (branchElValDTO.getId() != null) {
            throw new BadRequestAlertException("A new branchElVal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BranchElValDTO result = branchElValService.save(branchElValDTO);
        return ResponseEntity
            .created(new URI("/api/branch-el-vals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /branch-el-vals/:id} : Updates an existing branchElVal.
     *
     * @param id the id of the branchElValDTO to save.
     * @param branchElValDTO the branchElValDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated branchElValDTO,
     * or with status {@code 400 (Bad Request)} if the branchElValDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the branchElValDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/branch-el-vals/{id}")
    public ResponseEntity<BranchElValDTO> updateBranchElVal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BranchElValDTO branchElValDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BranchElVal : {}, {}", id, branchElValDTO);
        if (branchElValDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, branchElValDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!branchElValRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BranchElValDTO result = branchElValService.save(branchElValDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, branchElValDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /branch-el-vals/:id} : Partial updates given fields of an existing branchElVal, field will ignore if it is null
     *
     * @param id the id of the branchElValDTO to save.
     * @param branchElValDTO the branchElValDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated branchElValDTO,
     * or with status {@code 400 (Bad Request)} if the branchElValDTO is not valid,
     * or with status {@code 404 (Not Found)} if the branchElValDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the branchElValDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/branch-el-vals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BranchElValDTO> partialUpdateBranchElVal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BranchElValDTO branchElValDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BranchElVal partially : {}, {}", id, branchElValDTO);
        if (branchElValDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, branchElValDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!branchElValRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BranchElValDTO> result = branchElValService.partialUpdate(branchElValDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, branchElValDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /branch-el-vals} : get all the branchElVals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of branchElVals in body.
     */
    @GetMapping("/branch-el-vals")
    public ResponseEntity<List<BranchElValDTO>> getAllBranchElVals(BranchElValCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BranchElVals by criteria: {}", criteria);
        Page<BranchElValDTO> page = branchElValQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /branch-el-vals/count} : count all the branchElVals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/branch-el-vals/count")
    public ResponseEntity<Long> countBranchElVals(BranchElValCriteria criteria) {
        log.debug("REST request to count BranchElVals by criteria: {}", criteria);
        return ResponseEntity.ok().body(branchElValQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /branch-el-vals/:id} : get the "id" branchElVal.
     *
     * @param id the id of the branchElValDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the branchElValDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/branch-el-vals/{id}")
    public ResponseEntity<BranchElValDTO> getBranchElVal(@PathVariable Long id) {
        log.debug("REST request to get BranchElVal : {}", id);
        Optional<BranchElValDTO> branchElValDTO = branchElValService.findOne(id);
        return ResponseUtil.wrapOrNotFound(branchElValDTO);
    }

    /**
     * {@code DELETE  /branch-el-vals/:id} : delete the "id" branchElVal.
     *
     * @param id the id of the branchElValDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/branch-el-vals/{id}")
    public ResponseEntity<Void> deleteBranchElVal(@PathVariable Long id) {
        log.debug("REST request to delete BranchElVal : {}", id);
        branchElValService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
