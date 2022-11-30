package com.attest.ict.web.rest;

import com.attest.ict.repository.BillingConsumptionRepository;
import com.attest.ict.service.BillingConsumptionQueryService;
import com.attest.ict.service.BillingConsumptionService;
import com.attest.ict.service.criteria.BillingConsumptionCriteria;
import com.attest.ict.service.dto.BillingConsumptionDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.BillingConsumption}.
 */
@RestController
@RequestMapping("/api")
public class BillingConsumptionResource {

    private final Logger log = LoggerFactory.getLogger(BillingConsumptionResource.class);

    private static final String ENTITY_NAME = "billingConsumption";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillingConsumptionService billingConsumptionService;

    private final BillingConsumptionRepository billingConsumptionRepository;

    private final BillingConsumptionQueryService billingConsumptionQueryService;

    public BillingConsumptionResource(
        BillingConsumptionService billingConsumptionService,
        BillingConsumptionRepository billingConsumptionRepository,
        BillingConsumptionQueryService billingConsumptionQueryService
    ) {
        this.billingConsumptionService = billingConsumptionService;
        this.billingConsumptionRepository = billingConsumptionRepository;
        this.billingConsumptionQueryService = billingConsumptionQueryService;
    }

    /**
     * {@code POST  /billing-consumptions} : Create a new billingConsumption.
     *
     * @param billingConsumptionDTO the billingConsumptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billingConsumptionDTO, or with status {@code 400 (Bad Request)} if the billingConsumption has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/billing-consumptions")
    public ResponseEntity<BillingConsumptionDTO> createBillingConsumption(@RequestBody BillingConsumptionDTO billingConsumptionDTO)
        throws URISyntaxException {
        log.debug("REST request to save BillingConsumption : {}", billingConsumptionDTO);
        if (billingConsumptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new billingConsumption cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BillingConsumptionDTO result = billingConsumptionService.save(billingConsumptionDTO);
        return ResponseEntity
            .created(new URI("/api/billing-consumptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /billing-consumptions/:id} : Updates an existing billingConsumption.
     *
     * @param id the id of the billingConsumptionDTO to save.
     * @param billingConsumptionDTO the billingConsumptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billingConsumptionDTO,
     * or with status {@code 400 (Bad Request)} if the billingConsumptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billingConsumptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/billing-consumptions/{id}")
    public ResponseEntity<BillingConsumptionDTO> updateBillingConsumption(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BillingConsumptionDTO billingConsumptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BillingConsumption : {}, {}", id, billingConsumptionDTO);
        if (billingConsumptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billingConsumptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billingConsumptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BillingConsumptionDTO result = billingConsumptionService.save(billingConsumptionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, billingConsumptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /billing-consumptions/:id} : Partial updates given fields of an existing billingConsumption, field will ignore if it is null
     *
     * @param id the id of the billingConsumptionDTO to save.
     * @param billingConsumptionDTO the billingConsumptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billingConsumptionDTO,
     * or with status {@code 400 (Bad Request)} if the billingConsumptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the billingConsumptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the billingConsumptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/billing-consumptions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BillingConsumptionDTO> partialUpdateBillingConsumption(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BillingConsumptionDTO billingConsumptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BillingConsumption partially : {}, {}", id, billingConsumptionDTO);
        if (billingConsumptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billingConsumptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billingConsumptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BillingConsumptionDTO> result = billingConsumptionService.partialUpdate(billingConsumptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, billingConsumptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /billing-consumptions} : get all the billingConsumptions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of billingConsumptions in body.
     */
    @GetMapping("/billing-consumptions")
    public ResponseEntity<List<BillingConsumptionDTO>> getAllBillingConsumptions(BillingConsumptionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BillingConsumptions by criteria: {}", criteria);
        Page<BillingConsumptionDTO> page = billingConsumptionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /billing-consumptions/count} : count all the billingConsumptions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/billing-consumptions/count")
    public ResponseEntity<Long> countBillingConsumptions(BillingConsumptionCriteria criteria) {
        log.debug("REST request to count BillingConsumptions by criteria: {}", criteria);
        return ResponseEntity.ok().body(billingConsumptionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /billing-consumptions/:id} : get the "id" billingConsumption.
     *
     * @param id the id of the billingConsumptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the billingConsumptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/billing-consumptions/{id}")
    public ResponseEntity<BillingConsumptionDTO> getBillingConsumption(@PathVariable Long id) {
        log.debug("REST request to get BillingConsumption : {}", id);
        Optional<BillingConsumptionDTO> billingConsumptionDTO = billingConsumptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billingConsumptionDTO);
    }

    /**
     * {@code DELETE  /billing-consumptions/:id} : delete the "id" billingConsumption.
     *
     * @param id the id of the billingConsumptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/billing-consumptions/{id}")
    public ResponseEntity<Void> deleteBillingConsumption(@PathVariable Long id) {
        log.debug("REST request to delete BillingConsumption : {}", id);
        billingConsumptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
