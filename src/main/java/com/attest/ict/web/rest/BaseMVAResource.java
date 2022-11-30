package com.attest.ict.web.rest;

import com.attest.ict.repository.BaseMVARepository;
import com.attest.ict.service.BaseMVAQueryService;
import com.attest.ict.service.BaseMVAService;
import com.attest.ict.service.criteria.BaseMVACriteria;
import com.attest.ict.service.dto.BaseMVADTO;
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
 * REST controller for managing {@link com.attest.ict.domain.BaseMVA}.
 */
@RestController
@RequestMapping("/api")
public class BaseMVAResource {

    private final Logger log = LoggerFactory.getLogger(BaseMVAResource.class);

    private static final String ENTITY_NAME = "baseMVA";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BaseMVAService baseMVAService;

    private final BaseMVARepository baseMVARepository;

    private final BaseMVAQueryService baseMVAQueryService;

    public BaseMVAResource(BaseMVAService baseMVAService, BaseMVARepository baseMVARepository, BaseMVAQueryService baseMVAQueryService) {
        this.baseMVAService = baseMVAService;
        this.baseMVARepository = baseMVARepository;
        this.baseMVAQueryService = baseMVAQueryService;
    }

    /**
     * {@code POST  /base-mvas} : Create a new baseMVA.
     *
     * @param baseMVADTO the baseMVADTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new baseMVADTO, or with status {@code 400 (Bad Request)} if the baseMVA has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/base-mvas")
    public ResponseEntity<BaseMVADTO> createBaseMVA(@RequestBody BaseMVADTO baseMVADTO) throws URISyntaxException {
        log.debug("REST request to save BaseMVA : {}", baseMVADTO);
        if (baseMVADTO.getId() != null) {
            throw new BadRequestAlertException("A new baseMVA cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BaseMVADTO result = baseMVAService.save(baseMVADTO);
        return ResponseEntity
            .created(new URI("/api/base-mvas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /base-mvas/:id} : Updates an existing baseMVA.
     *
     * @param id the id of the baseMVADTO to save.
     * @param baseMVADTO the baseMVADTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated baseMVADTO,
     * or with status {@code 400 (Bad Request)} if the baseMVADTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the baseMVADTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/base-mvas/{id}")
    public ResponseEntity<BaseMVADTO> updateBaseMVA(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BaseMVADTO baseMVADTO
    ) throws URISyntaxException {
        log.debug("REST request to update BaseMVA : {}, {}", id, baseMVADTO);
        if (baseMVADTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, baseMVADTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!baseMVARepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BaseMVADTO result = baseMVAService.save(baseMVADTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, baseMVADTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /base-mvas/:id} : Partial updates given fields of an existing baseMVA, field will ignore if it is null
     *
     * @param id the id of the baseMVADTO to save.
     * @param baseMVADTO the baseMVADTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated baseMVADTO,
     * or with status {@code 400 (Bad Request)} if the baseMVADTO is not valid,
     * or with status {@code 404 (Not Found)} if the baseMVADTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the baseMVADTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/base-mvas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BaseMVADTO> partialUpdateBaseMVA(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BaseMVADTO baseMVADTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BaseMVA partially : {}, {}", id, baseMVADTO);
        if (baseMVADTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, baseMVADTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!baseMVARepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BaseMVADTO> result = baseMVAService.partialUpdate(baseMVADTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, baseMVADTO.getId().toString())
        );
    }

    /**
     * {@code GET  /base-mvas} : get all the baseMVAS.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of baseMVAS in body.
     */
    @GetMapping("/base-mvas")
    public ResponseEntity<List<BaseMVADTO>> getAllBaseMVAS(BaseMVACriteria criteria, Pageable pageable) {
        log.debug("REST request to get BaseMVAS by criteria: {}", criteria);
        Page<BaseMVADTO> page = baseMVAQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /base-mvas/count} : count all the baseMVAS.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/base-mvas/count")
    public ResponseEntity<Long> countBaseMVAS(BaseMVACriteria criteria) {
        log.debug("REST request to count BaseMVAS by criteria: {}", criteria);
        return ResponseEntity.ok().body(baseMVAQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /base-mvas/:id} : get the "id" baseMVA.
     *
     * @param id the id of the baseMVADTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the baseMVADTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/base-mvas/{id}")
    public ResponseEntity<BaseMVADTO> getBaseMVA(@PathVariable Long id) {
        log.debug("REST request to get BaseMVA : {}", id);
        Optional<BaseMVADTO> baseMVADTO = baseMVAService.findOne(id);
        return ResponseUtil.wrapOrNotFound(baseMVADTO);
    }

    /**
     * {@code DELETE  /base-mvas/:id} : delete the "id" baseMVA.
     *
     * @param id the id of the baseMVADTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/base-mvas/{id}")
    public ResponseEntity<Void> deleteBaseMVA(@PathVariable Long id) {
        log.debug("REST request to delete BaseMVA : {}", id);
        baseMVAService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
