package com.attest.ict.web.rest;

import com.attest.ict.repository.CapacitorBankDataRepository;
import com.attest.ict.service.CapacitorBankDataQueryService;
import com.attest.ict.service.CapacitorBankDataService;
import com.attest.ict.service.criteria.CapacitorBankDataCriteria;
import com.attest.ict.service.dto.CapacitorBankDataDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.CapacitorBankData}.
 */
@RestController
@RequestMapping("/api")
public class CapacitorBankDataResource {

    private final Logger log = LoggerFactory.getLogger(CapacitorBankDataResource.class);

    private static final String ENTITY_NAME = "capacitorBankData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CapacitorBankDataService capacitorBankDataService;

    private final CapacitorBankDataRepository capacitorBankDataRepository;

    private final CapacitorBankDataQueryService capacitorBankDataQueryService;

    public CapacitorBankDataResource(
        CapacitorBankDataService capacitorBankDataService,
        CapacitorBankDataRepository capacitorBankDataRepository,
        CapacitorBankDataQueryService capacitorBankDataQueryService
    ) {
        this.capacitorBankDataService = capacitorBankDataService;
        this.capacitorBankDataRepository = capacitorBankDataRepository;
        this.capacitorBankDataQueryService = capacitorBankDataQueryService;
    }

    /**
     * {@code POST  /capacitor-bank-data} : Create a new capacitorBankData.
     *
     * @param capacitorBankDataDTO the capacitorBankDataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new capacitorBankDataDTO, or with status {@code 400 (Bad Request)} if the capacitorBankData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/capacitor-bank-data")
    public ResponseEntity<CapacitorBankDataDTO> createCapacitorBankData(@RequestBody CapacitorBankDataDTO capacitorBankDataDTO)
        throws URISyntaxException {
        log.debug("REST request to save CapacitorBankData : {}", capacitorBankDataDTO);
        if (capacitorBankDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new capacitorBankData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CapacitorBankDataDTO result = capacitorBankDataService.save(capacitorBankDataDTO);
        return ResponseEntity
            .created(new URI("/api/capacitor-bank-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /capacitor-bank-data/:id} : Updates an existing capacitorBankData.
     *
     * @param id the id of the capacitorBankDataDTO to save.
     * @param capacitorBankDataDTO the capacitorBankDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capacitorBankDataDTO,
     * or with status {@code 400 (Bad Request)} if the capacitorBankDataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the capacitorBankDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/capacitor-bank-data/{id}")
    public ResponseEntity<CapacitorBankDataDTO> updateCapacitorBankData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CapacitorBankDataDTO capacitorBankDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CapacitorBankData : {}, {}", id, capacitorBankDataDTO);
        if (capacitorBankDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capacitorBankDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!capacitorBankDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CapacitorBankDataDTO result = capacitorBankDataService.save(capacitorBankDataDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, capacitorBankDataDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /capacitor-bank-data/:id} : Partial updates given fields of an existing capacitorBankData, field will ignore if it is null
     *
     * @param id the id of the capacitorBankDataDTO to save.
     * @param capacitorBankDataDTO the capacitorBankDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capacitorBankDataDTO,
     * or with status {@code 400 (Bad Request)} if the capacitorBankDataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the capacitorBankDataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the capacitorBankDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/capacitor-bank-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CapacitorBankDataDTO> partialUpdateCapacitorBankData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CapacitorBankDataDTO capacitorBankDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CapacitorBankData partially : {}, {}", id, capacitorBankDataDTO);
        if (capacitorBankDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capacitorBankDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!capacitorBankDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CapacitorBankDataDTO> result = capacitorBankDataService.partialUpdate(capacitorBankDataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, capacitorBankDataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /capacitor-bank-data} : get all the capacitorBankData.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of capacitorBankData in body.
     */
    @GetMapping("/capacitor-bank-data")
    public ResponseEntity<List<CapacitorBankDataDTO>> getAllCapacitorBankData(CapacitorBankDataCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CapacitorBankData by criteria: {}", criteria);
        Page<CapacitorBankDataDTO> page = capacitorBankDataQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /capacitor-bank-data/count} : count all the capacitorBankData.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/capacitor-bank-data/count")
    public ResponseEntity<Long> countCapacitorBankData(CapacitorBankDataCriteria criteria) {
        log.debug("REST request to count CapacitorBankData by criteria: {}", criteria);
        return ResponseEntity.ok().body(capacitorBankDataQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /capacitor-bank-data/:id} : get the "id" capacitorBankData.
     *
     * @param id the id of the capacitorBankDataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the capacitorBankDataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/capacitor-bank-data/{id}")
    public ResponseEntity<CapacitorBankDataDTO> getCapacitorBankData(@PathVariable Long id) {
        log.debug("REST request to get CapacitorBankData : {}", id);
        Optional<CapacitorBankDataDTO> capacitorBankDataDTO = capacitorBankDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(capacitorBankDataDTO);
    }

    /**
     * {@code DELETE  /capacitor-bank-data/:id} : delete the "id" capacitorBankData.
     *
     * @param id the id of the capacitorBankDataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/capacitor-bank-data/{id}")
    public ResponseEntity<Void> deleteCapacitorBankData(@PathVariable Long id) {
        log.debug("REST request to delete CapacitorBankData : {}", id);
        capacitorBankDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
