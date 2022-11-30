package com.attest.ict.web.rest;

import com.attest.ict.repository.StorageCostRepository;
import com.attest.ict.service.StorageCostQueryService;
import com.attest.ict.service.StorageCostService;
import com.attest.ict.service.criteria.StorageCostCriteria;
import com.attest.ict.service.dto.StorageCostDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.StorageCost}.
 */
@RestController
@RequestMapping("/api")
public class StorageCostResource {

    private final Logger log = LoggerFactory.getLogger(StorageCostResource.class);

    private static final String ENTITY_NAME = "storageCost";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StorageCostService storageCostService;

    private final StorageCostRepository storageCostRepository;

    private final StorageCostQueryService storageCostQueryService;

    public StorageCostResource(
        StorageCostService storageCostService,
        StorageCostRepository storageCostRepository,
        StorageCostQueryService storageCostQueryService
    ) {
        this.storageCostService = storageCostService;
        this.storageCostRepository = storageCostRepository;
        this.storageCostQueryService = storageCostQueryService;
    }

    /**
     * {@code POST  /storage-costs} : Create a new storageCost.
     *
     * @param storageCostDTO the storageCostDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new storageCostDTO, or with status {@code 400 (Bad Request)} if the storageCost has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/storage-costs")
    public ResponseEntity<StorageCostDTO> createStorageCost(@RequestBody StorageCostDTO storageCostDTO) throws URISyntaxException {
        log.debug("REST request to save StorageCost : {}", storageCostDTO);
        if (storageCostDTO.getId() != null) {
            throw new BadRequestAlertException("A new storageCost cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StorageCostDTO result = storageCostService.save(storageCostDTO);
        return ResponseEntity
            .created(new URI("/api/storage-costs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /storage-costs/:id} : Updates an existing storageCost.
     *
     * @param id the id of the storageCostDTO to save.
     * @param storageCostDTO the storageCostDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storageCostDTO,
     * or with status {@code 400 (Bad Request)} if the storageCostDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the storageCostDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/storage-costs/{id}")
    public ResponseEntity<StorageCostDTO> updateStorageCost(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StorageCostDTO storageCostDTO
    ) throws URISyntaxException {
        log.debug("REST request to update StorageCost : {}, {}", id, storageCostDTO);
        if (storageCostDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storageCostDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storageCostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StorageCostDTO result = storageCostService.save(storageCostDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, storageCostDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /storage-costs/:id} : Partial updates given fields of an existing storageCost, field will ignore if it is null
     *
     * @param id the id of the storageCostDTO to save.
     * @param storageCostDTO the storageCostDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storageCostDTO,
     * or with status {@code 400 (Bad Request)} if the storageCostDTO is not valid,
     * or with status {@code 404 (Not Found)} if the storageCostDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the storageCostDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/storage-costs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StorageCostDTO> partialUpdateStorageCost(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StorageCostDTO storageCostDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update StorageCost partially : {}, {}", id, storageCostDTO);
        if (storageCostDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storageCostDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storageCostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StorageCostDTO> result = storageCostService.partialUpdate(storageCostDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, storageCostDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /storage-costs} : get all the storageCosts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of storageCosts in body.
     */
    @GetMapping("/storage-costs")
    public ResponseEntity<List<StorageCostDTO>> getAllStorageCosts(StorageCostCriteria criteria, Pageable pageable) {
        log.debug("REST request to get StorageCosts by criteria: {}", criteria);
        Page<StorageCostDTO> page = storageCostQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /storage-costs/count} : count all the storageCosts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/storage-costs/count")
    public ResponseEntity<Long> countStorageCosts(StorageCostCriteria criteria) {
        log.debug("REST request to count StorageCosts by criteria: {}", criteria);
        return ResponseEntity.ok().body(storageCostQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /storage-costs/:id} : get the "id" storageCost.
     *
     * @param id the id of the storageCostDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the storageCostDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/storage-costs/{id}")
    public ResponseEntity<StorageCostDTO> getStorageCost(@PathVariable Long id) {
        log.debug("REST request to get StorageCost : {}", id);
        Optional<StorageCostDTO> storageCostDTO = storageCostService.findOne(id);
        return ResponseUtil.wrapOrNotFound(storageCostDTO);
    }

    /**
     * {@code DELETE  /storage-costs/:id} : delete the "id" storageCost.
     *
     * @param id the id of the storageCostDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/storage-costs/{id}")
    public ResponseEntity<Void> deleteStorageCost(@PathVariable Long id) {
        log.debug("REST request to delete StorageCost : {}", id);
        storageCostService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
