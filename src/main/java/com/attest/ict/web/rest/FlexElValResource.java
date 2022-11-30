package com.attest.ict.web.rest;

import com.attest.ict.repository.FlexElValRepository;
import com.attest.ict.service.FlexElValQueryService;
import com.attest.ict.service.FlexElValService;
import com.attest.ict.service.criteria.FlexElValCriteria;
import com.attest.ict.service.dto.FlexElValDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.FlexElVal}.
 */
@RestController
@RequestMapping("/api")
public class FlexElValResource {

    private final Logger log = LoggerFactory.getLogger(FlexElValResource.class);

    private static final String ENTITY_NAME = "flexElVal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FlexElValService flexElValService;

    private final FlexElValRepository flexElValRepository;

    private final FlexElValQueryService flexElValQueryService;

    public FlexElValResource(
        FlexElValService flexElValService,
        FlexElValRepository flexElValRepository,
        FlexElValQueryService flexElValQueryService
    ) {
        this.flexElValService = flexElValService;
        this.flexElValRepository = flexElValRepository;
        this.flexElValQueryService = flexElValQueryService;
    }

    /**
     * {@code POST  /flex-el-vals} : Create a new flexElVal.
     *
     * @param flexElValDTO the flexElValDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new flexElValDTO, or with status {@code 400 (Bad Request)} if the flexElVal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/flex-el-vals")
    public ResponseEntity<FlexElValDTO> createFlexElVal(@RequestBody FlexElValDTO flexElValDTO) throws URISyntaxException {
        log.debug("REST request to save FlexElVal : {}", flexElValDTO);
        if (flexElValDTO.getId() != null) {
            throw new BadRequestAlertException("A new flexElVal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FlexElValDTO result = flexElValService.save(flexElValDTO);
        return ResponseEntity
            .created(new URI("/api/flex-el-vals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /flex-el-vals/:id} : Updates an existing flexElVal.
     *
     * @param id the id of the flexElValDTO to save.
     * @param flexElValDTO the flexElValDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flexElValDTO,
     * or with status {@code 400 (Bad Request)} if the flexElValDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the flexElValDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/flex-el-vals/{id}")
    public ResponseEntity<FlexElValDTO> updateFlexElVal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FlexElValDTO flexElValDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FlexElVal : {}, {}", id, flexElValDTO);
        if (flexElValDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flexElValDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flexElValRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FlexElValDTO result = flexElValService.save(flexElValDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, flexElValDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /flex-el-vals/:id} : Partial updates given fields of an existing flexElVal, field will ignore if it is null
     *
     * @param id the id of the flexElValDTO to save.
     * @param flexElValDTO the flexElValDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flexElValDTO,
     * or with status {@code 400 (Bad Request)} if the flexElValDTO is not valid,
     * or with status {@code 404 (Not Found)} if the flexElValDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the flexElValDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/flex-el-vals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FlexElValDTO> partialUpdateFlexElVal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FlexElValDTO flexElValDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FlexElVal partially : {}, {}", id, flexElValDTO);
        if (flexElValDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flexElValDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flexElValRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FlexElValDTO> result = flexElValService.partialUpdate(flexElValDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, flexElValDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /flex-el-vals} : get all the flexElVals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of flexElVals in body.
     */
    @GetMapping("/flex-el-vals")
    public ResponseEntity<List<FlexElValDTO>> getAllFlexElVals(FlexElValCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FlexElVals by criteria: {}", criteria);
        Page<FlexElValDTO> page = flexElValQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /flex-el-vals/count} : count all the flexElVals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/flex-el-vals/count")
    public ResponseEntity<Long> countFlexElVals(FlexElValCriteria criteria) {
        log.debug("REST request to count FlexElVals by criteria: {}", criteria);
        return ResponseEntity.ok().body(flexElValQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /flex-el-vals/:id} : get the "id" flexElVal.
     *
     * @param id the id of the flexElValDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the flexElValDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/flex-el-vals/{id}")
    public ResponseEntity<FlexElValDTO> getFlexElVal(@PathVariable Long id) {
        log.debug("REST request to get FlexElVal : {}", id);
        Optional<FlexElValDTO> flexElValDTO = flexElValService.findOne(id);
        return ResponseUtil.wrapOrNotFound(flexElValDTO);
    }

    /**
     * {@code DELETE  /flex-el-vals/:id} : delete the "id" flexElVal.
     *
     * @param id the id of the flexElValDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/flex-el-vals/{id}")
    public ResponseEntity<Void> deleteFlexElVal(@PathVariable Long id) {
        log.debug("REST request to delete FlexElVal : {}", id);
        flexElValService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
