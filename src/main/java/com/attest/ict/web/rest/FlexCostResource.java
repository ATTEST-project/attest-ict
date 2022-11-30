package com.attest.ict.web.rest;

import com.attest.ict.repository.FlexCostRepository;
import com.attest.ict.service.FlexCostQueryService;
import com.attest.ict.service.FlexCostService;
import com.attest.ict.service.criteria.FlexCostCriteria;
import com.attest.ict.service.dto.FlexCostDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.FlexCost}.
 */
@RestController
@RequestMapping("/api")
public class FlexCostResource {

    private final Logger log = LoggerFactory.getLogger(FlexCostResource.class);

    private static final String ENTITY_NAME = "flexCost";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FlexCostService flexCostService;

    private final FlexCostRepository flexCostRepository;

    private final FlexCostQueryService flexCostQueryService;

    public FlexCostResource(
        FlexCostService flexCostService,
        FlexCostRepository flexCostRepository,
        FlexCostQueryService flexCostQueryService
    ) {
        this.flexCostService = flexCostService;
        this.flexCostRepository = flexCostRepository;
        this.flexCostQueryService = flexCostQueryService;
    }

    /**
     * {@code POST  /flex-costs} : Create a new flexCost.
     *
     * @param flexCostDTO the flexCostDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new flexCostDTO, or with status {@code 400 (Bad Request)} if the flexCost has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/flex-costs")
    public ResponseEntity<FlexCostDTO> createFlexCost(@RequestBody FlexCostDTO flexCostDTO) throws URISyntaxException {
        log.debug("REST request to save FlexCost : {}", flexCostDTO);
        if (flexCostDTO.getId() != null) {
            throw new BadRequestAlertException("A new flexCost cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FlexCostDTO result = flexCostService.save(flexCostDTO);
        return ResponseEntity
            .created(new URI("/api/flex-costs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /flex-costs/:id} : Updates an existing flexCost.
     *
     * @param id the id of the flexCostDTO to save.
     * @param flexCostDTO the flexCostDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flexCostDTO,
     * or with status {@code 400 (Bad Request)} if the flexCostDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the flexCostDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/flex-costs/{id}")
    public ResponseEntity<FlexCostDTO> updateFlexCost(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FlexCostDTO flexCostDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FlexCost : {}, {}", id, flexCostDTO);
        if (flexCostDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flexCostDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flexCostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FlexCostDTO result = flexCostService.save(flexCostDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, flexCostDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /flex-costs/:id} : Partial updates given fields of an existing flexCost, field will ignore if it is null
     *
     * @param id the id of the flexCostDTO to save.
     * @param flexCostDTO the flexCostDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flexCostDTO,
     * or with status {@code 400 (Bad Request)} if the flexCostDTO is not valid,
     * or with status {@code 404 (Not Found)} if the flexCostDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the flexCostDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/flex-costs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FlexCostDTO> partialUpdateFlexCost(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FlexCostDTO flexCostDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FlexCost partially : {}, {}", id, flexCostDTO);
        if (flexCostDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flexCostDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flexCostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FlexCostDTO> result = flexCostService.partialUpdate(flexCostDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, flexCostDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /flex-costs} : get all the flexCosts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of flexCosts in body.
     */
    @GetMapping("/flex-costs")
    public ResponseEntity<List<FlexCostDTO>> getAllFlexCosts(FlexCostCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FlexCosts by criteria: {}", criteria);
        Page<FlexCostDTO> page = flexCostQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /flex-costs/count} : count all the flexCosts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/flex-costs/count")
    public ResponseEntity<Long> countFlexCosts(FlexCostCriteria criteria) {
        log.debug("REST request to count FlexCosts by criteria: {}", criteria);
        return ResponseEntity.ok().body(flexCostQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /flex-costs/:id} : get the "id" flexCost.
     *
     * @param id the id of the flexCostDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the flexCostDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/flex-costs/{id}")
    public ResponseEntity<FlexCostDTO> getFlexCost(@PathVariable Long id) {
        log.debug("REST request to get FlexCost : {}", id);
        Optional<FlexCostDTO> flexCostDTO = flexCostService.findOne(id);
        return ResponseUtil.wrapOrNotFound(flexCostDTO);
    }

    /**
     * {@code DELETE  /flex-costs/:id} : delete the "id" flexCost.
     *
     * @param id the id of the flexCostDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/flex-costs/{id}")
    public ResponseEntity<Void> deleteFlexCost(@PathVariable Long id) {
        log.debug("REST request to delete FlexCost : {}", id);
        flexCostService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
