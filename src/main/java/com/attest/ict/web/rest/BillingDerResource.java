package com.attest.ict.web.rest;

import com.attest.ict.repository.BillingDerRepository;
import com.attest.ict.service.BillingDerQueryService;
import com.attest.ict.service.BillingDerService;
import com.attest.ict.service.criteria.BillingDerCriteria;
import com.attest.ict.service.dto.BillingDerDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.BillingDer}.
 */
@RestController
@RequestMapping("/api")
public class BillingDerResource {

    private final Logger log = LoggerFactory.getLogger(BillingDerResource.class);

    private static final String ENTITY_NAME = "billingDer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillingDerService billingDerService;

    private final BillingDerRepository billingDerRepository;

    private final BillingDerQueryService billingDerQueryService;

    public BillingDerResource(
        BillingDerService billingDerService,
        BillingDerRepository billingDerRepository,
        BillingDerQueryService billingDerQueryService
    ) {
        this.billingDerService = billingDerService;
        this.billingDerRepository = billingDerRepository;
        this.billingDerQueryService = billingDerQueryService;
    }

    /**
     * {@code POST  /billing-ders} : Create a new billingDer.
     *
     * @param billingDerDTO the billingDerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billingDerDTO, or with status {@code 400 (Bad Request)} if the billingDer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/billing-ders")
    public ResponseEntity<BillingDerDTO> createBillingDer(@RequestBody BillingDerDTO billingDerDTO) throws URISyntaxException {
        log.debug("REST request to save BillingDer : {}", billingDerDTO);
        if (billingDerDTO.getId() != null) {
            throw new BadRequestAlertException("A new billingDer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BillingDerDTO result = billingDerService.save(billingDerDTO);
        return ResponseEntity
            .created(new URI("/api/billing-ders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /billing-ders/:id} : Updates an existing billingDer.
     *
     * @param id the id of the billingDerDTO to save.
     * @param billingDerDTO the billingDerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billingDerDTO,
     * or with status {@code 400 (Bad Request)} if the billingDerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billingDerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/billing-ders/{id}")
    public ResponseEntity<BillingDerDTO> updateBillingDer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BillingDerDTO billingDerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BillingDer : {}, {}", id, billingDerDTO);
        if (billingDerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billingDerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billingDerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BillingDerDTO result = billingDerService.save(billingDerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, billingDerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /billing-ders/:id} : Partial updates given fields of an existing billingDer, field will ignore if it is null
     *
     * @param id the id of the billingDerDTO to save.
     * @param billingDerDTO the billingDerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billingDerDTO,
     * or with status {@code 400 (Bad Request)} if the billingDerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the billingDerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the billingDerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/billing-ders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BillingDerDTO> partialUpdateBillingDer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BillingDerDTO billingDerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BillingDer partially : {}, {}", id, billingDerDTO);
        if (billingDerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billingDerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billingDerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BillingDerDTO> result = billingDerService.partialUpdate(billingDerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, billingDerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /billing-ders} : get all the billingDers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of billingDers in body.
     */
    @GetMapping("/billing-ders")
    public ResponseEntity<List<BillingDerDTO>> getAllBillingDers(BillingDerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BillingDers by criteria: {}", criteria);
        Page<BillingDerDTO> page = billingDerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /billing-ders/count} : count all the billingDers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/billing-ders/count")
    public ResponseEntity<Long> countBillingDers(BillingDerCriteria criteria) {
        log.debug("REST request to count BillingDers by criteria: {}", criteria);
        return ResponseEntity.ok().body(billingDerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /billing-ders/:id} : get the "id" billingDer.
     *
     * @param id the id of the billingDerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the billingDerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/billing-ders/{id}")
    public ResponseEntity<BillingDerDTO> getBillingDer(@PathVariable Long id) {
        log.debug("REST request to get BillingDer : {}", id);
        Optional<BillingDerDTO> billingDerDTO = billingDerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billingDerDTO);
    }

    /**
     * {@code DELETE  /billing-ders/:id} : delete the "id" billingDer.
     *
     * @param id the id of the billingDerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/billing-ders/{id}")
    public ResponseEntity<Void> deleteBillingDer(@PathVariable Long id) {
        log.debug("REST request to delete BillingDer : {}", id);
        billingDerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
