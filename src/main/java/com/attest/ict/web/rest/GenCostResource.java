package com.attest.ict.web.rest;

import com.attest.ict.repository.GenCostRepository;
import com.attest.ict.service.GenCostQueryService;
import com.attest.ict.service.GenCostService;
import com.attest.ict.service.criteria.GenCostCriteria;
import com.attest.ict.service.dto.GenCostDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.GenCost}.
 */
@RestController
@RequestMapping("/api")
public class GenCostResource {

    private final Logger log = LoggerFactory.getLogger(GenCostResource.class);

    private static final String ENTITY_NAME = "genCost";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GenCostService genCostService;

    private final GenCostRepository genCostRepository;

    private final GenCostQueryService genCostQueryService;

    public GenCostResource(GenCostService genCostService, GenCostRepository genCostRepository, GenCostQueryService genCostQueryService) {
        this.genCostService = genCostService;
        this.genCostRepository = genCostRepository;
        this.genCostQueryService = genCostQueryService;
    }

    /**
     * {@code POST  /gen-costs} : Create a new genCost.
     *
     * @param genCostDTO the genCostDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new genCostDTO, or with status {@code 400 (Bad Request)} if the genCost has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gen-costs")
    public ResponseEntity<GenCostDTO> createGenCost(@RequestBody GenCostDTO genCostDTO) throws URISyntaxException {
        log.debug("REST request to save GenCost : {}", genCostDTO);
        if (genCostDTO.getId() != null) {
            throw new BadRequestAlertException("A new genCost cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GenCostDTO result = genCostService.save(genCostDTO);
        return ResponseEntity
            .created(new URI("/api/gen-costs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gen-costs/:id} : Updates an existing genCost.
     *
     * @param id the id of the genCostDTO to save.
     * @param genCostDTO the genCostDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated genCostDTO,
     * or with status {@code 400 (Bad Request)} if the genCostDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the genCostDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gen-costs/{id}")
    public ResponseEntity<GenCostDTO> updateGenCost(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GenCostDTO genCostDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GenCost : {}, {}", id, genCostDTO);
        if (genCostDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, genCostDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!genCostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GenCostDTO result = genCostService.save(genCostDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, genCostDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gen-costs/:id} : Partial updates given fields of an existing genCost, field will ignore if it is null
     *
     * @param id the id of the genCostDTO to save.
     * @param genCostDTO the genCostDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated genCostDTO,
     * or with status {@code 400 (Bad Request)} if the genCostDTO is not valid,
     * or with status {@code 404 (Not Found)} if the genCostDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the genCostDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gen-costs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GenCostDTO> partialUpdateGenCost(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GenCostDTO genCostDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GenCost partially : {}, {}", id, genCostDTO);
        if (genCostDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, genCostDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!genCostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GenCostDTO> result = genCostService.partialUpdate(genCostDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, genCostDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /gen-costs} : get all the genCosts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of genCosts in body.
     */
    @GetMapping("/gen-costs")
    public ResponseEntity<List<GenCostDTO>> getAllGenCosts(GenCostCriteria criteria, Pageable pageable) {
        log.debug("REST request to get GenCosts by criteria: {}", criteria);
        Page<GenCostDTO> page = genCostQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gen-costs/count} : count all the genCosts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/gen-costs/count")
    public ResponseEntity<Long> countGenCosts(GenCostCriteria criteria) {
        log.debug("REST request to count GenCosts by criteria: {}", criteria);
        return ResponseEntity.ok().body(genCostQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /gen-costs/:id} : get the "id" genCost.
     *
     * @param id the id of the genCostDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the genCostDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gen-costs/{id}")
    public ResponseEntity<GenCostDTO> getGenCost(@PathVariable Long id) {
        log.debug("REST request to get GenCost : {}", id);
        Optional<GenCostDTO> genCostDTO = genCostService.findOne(id);
        return ResponseUtil.wrapOrNotFound(genCostDTO);
    }

    /**
     * {@code DELETE  /gen-costs/:id} : delete the "id" genCost.
     *
     * @param id the id of the genCostDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gen-costs/{id}")
    public ResponseEntity<Void> deleteGenCost(@PathVariable Long id) {
        log.debug("REST request to delete GenCost : {}", id);
        genCostService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
