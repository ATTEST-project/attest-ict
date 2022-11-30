package com.attest.ict.web.rest;

import com.attest.ict.repository.BranchProfileRepository;
import com.attest.ict.service.BranchProfileQueryService;
import com.attest.ict.service.BranchProfileService;
import com.attest.ict.service.criteria.BranchProfileCriteria;
import com.attest.ict.service.dto.BranchProfileDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.BranchProfile}.
 */
@RestController
@RequestMapping("/api")
public class BranchProfileResource {

    private final Logger log = LoggerFactory.getLogger(BranchProfileResource.class);

    private static final String ENTITY_NAME = "branchProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BranchProfileService branchProfileService;

    private final BranchProfileRepository branchProfileRepository;

    private final BranchProfileQueryService branchProfileQueryService;

    public BranchProfileResource(
        BranchProfileService branchProfileService,
        BranchProfileRepository branchProfileRepository,
        BranchProfileQueryService branchProfileQueryService
    ) {
        this.branchProfileService = branchProfileService;
        this.branchProfileRepository = branchProfileRepository;
        this.branchProfileQueryService = branchProfileQueryService;
    }

    /**
     * {@code POST  /branch-profiles} : Create a new branchProfile.
     *
     * @param branchProfileDTO the branchProfileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new branchProfileDTO, or with status {@code 400 (Bad Request)} if the branchProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/branch-profiles")
    public ResponseEntity<BranchProfileDTO> createBranchProfile(@RequestBody BranchProfileDTO branchProfileDTO) throws URISyntaxException {
        log.debug("REST request to save BranchProfile : {}", branchProfileDTO);
        if (branchProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new branchProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BranchProfileDTO result = branchProfileService.save(branchProfileDTO);
        return ResponseEntity
            .created(new URI("/api/branch-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /branch-profiles/:id} : Updates an existing branchProfile.
     *
     * @param id the id of the branchProfileDTO to save.
     * @param branchProfileDTO the branchProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated branchProfileDTO,
     * or with status {@code 400 (Bad Request)} if the branchProfileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the branchProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/branch-profiles/{id}")
    public ResponseEntity<BranchProfileDTO> updateBranchProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BranchProfileDTO branchProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BranchProfile : {}, {}", id, branchProfileDTO);
        if (branchProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, branchProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!branchProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BranchProfileDTO result = branchProfileService.save(branchProfileDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, branchProfileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /branch-profiles/:id} : Partial updates given fields of an existing branchProfile, field will ignore if it is null
     *
     * @param id the id of the branchProfileDTO to save.
     * @param branchProfileDTO the branchProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated branchProfileDTO,
     * or with status {@code 400 (Bad Request)} if the branchProfileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the branchProfileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the branchProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/branch-profiles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BranchProfileDTO> partialUpdateBranchProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BranchProfileDTO branchProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BranchProfile partially : {}, {}", id, branchProfileDTO);
        if (branchProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, branchProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!branchProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BranchProfileDTO> result = branchProfileService.partialUpdate(branchProfileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, branchProfileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /branch-profiles} : get all the branchProfiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of branchProfiles in body.
     */
    @GetMapping("/branch-profiles")
    public ResponseEntity<List<BranchProfileDTO>> getAllBranchProfiles(BranchProfileCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BranchProfiles by criteria: {}", criteria);
        Page<BranchProfileDTO> page = branchProfileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /branch-profiles/count} : count all the branchProfiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/branch-profiles/count")
    public ResponseEntity<Long> countBranchProfiles(BranchProfileCriteria criteria) {
        log.debug("REST request to count BranchProfiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(branchProfileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /branch-profiles/:id} : get the "id" branchProfile.
     *
     * @param id the id of the branchProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the branchProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/branch-profiles/{id}")
    public ResponseEntity<BranchProfileDTO> getBranchProfile(@PathVariable Long id) {
        log.debug("REST request to get BranchProfile : {}", id);
        Optional<BranchProfileDTO> branchProfileDTO = branchProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(branchProfileDTO);
    }

    /**
     * {@code DELETE  /branch-profiles/:id} : delete the "id" branchProfile.
     *
     * @param id the id of the branchProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/branch-profiles/{id}")
    public ResponseEntity<Void> deleteBranchProfile(@PathVariable Long id) {
        log.debug("REST request to delete BranchProfile : {}", id);
        branchProfileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
