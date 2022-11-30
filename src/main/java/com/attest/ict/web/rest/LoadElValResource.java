package com.attest.ict.web.rest;

import com.attest.ict.repository.LoadElValRepository;
import com.attest.ict.service.LoadElValQueryService;
import com.attest.ict.service.LoadElValService;
import com.attest.ict.service.criteria.LoadElValCriteria;
import com.attest.ict.service.dto.LoadElValDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.LoadElVal}.
 */
@RestController
@RequestMapping("/api")
public class LoadElValResource {

    private final Logger log = LoggerFactory.getLogger(LoadElValResource.class);

    private static final String ENTITY_NAME = "loadElVal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoadElValService loadElValService;

    private final LoadElValRepository loadElValRepository;

    private final LoadElValQueryService loadElValQueryService;

    public LoadElValResource(
        LoadElValService loadElValService,
        LoadElValRepository loadElValRepository,
        LoadElValQueryService loadElValQueryService
    ) {
        this.loadElValService = loadElValService;
        this.loadElValRepository = loadElValRepository;
        this.loadElValQueryService = loadElValQueryService;
    }

    /**
     * {@code POST  /load-el-vals} : Create a new loadElVal.
     *
     * @param loadElValDTO the loadElValDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loadElValDTO, or with status {@code 400 (Bad Request)} if the loadElVal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/load-el-vals")
    public ResponseEntity<LoadElValDTO> createLoadElVal(@RequestBody LoadElValDTO loadElValDTO) throws URISyntaxException {
        log.debug("REST request to save LoadElVal : {}", loadElValDTO);
        if (loadElValDTO.getId() != null) {
            throw new BadRequestAlertException("A new loadElVal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoadElValDTO result = loadElValService.save(loadElValDTO);
        return ResponseEntity
            .created(new URI("/api/load-el-vals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /load-el-vals/:id} : Updates an existing loadElVal.
     *
     * @param id the id of the loadElValDTO to save.
     * @param loadElValDTO the loadElValDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loadElValDTO,
     * or with status {@code 400 (Bad Request)} if the loadElValDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loadElValDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/load-el-vals/{id}")
    public ResponseEntity<LoadElValDTO> updateLoadElVal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LoadElValDTO loadElValDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LoadElVal : {}, {}", id, loadElValDTO);
        if (loadElValDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loadElValDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loadElValRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LoadElValDTO result = loadElValService.save(loadElValDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, loadElValDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /load-el-vals/:id} : Partial updates given fields of an existing loadElVal, field will ignore if it is null
     *
     * @param id the id of the loadElValDTO to save.
     * @param loadElValDTO the loadElValDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loadElValDTO,
     * or with status {@code 400 (Bad Request)} if the loadElValDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loadElValDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loadElValDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/load-el-vals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LoadElValDTO> partialUpdateLoadElVal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LoadElValDTO loadElValDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LoadElVal partially : {}, {}", id, loadElValDTO);
        if (loadElValDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loadElValDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loadElValRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoadElValDTO> result = loadElValService.partialUpdate(loadElValDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, loadElValDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /load-el-vals} : get all the loadElVals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loadElVals in body.
     */
    @GetMapping("/load-el-vals")
    public ResponseEntity<List<LoadElValDTO>> getAllLoadElVals(LoadElValCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LoadElVals by criteria: {}", criteria);
        Page<LoadElValDTO> page = loadElValQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /load-el-vals/count} : count all the loadElVals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/load-el-vals/count")
    public ResponseEntity<Long> countLoadElVals(LoadElValCriteria criteria) {
        log.debug("REST request to count LoadElVals by criteria: {}", criteria);
        return ResponseEntity.ok().body(loadElValQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /load-el-vals/:id} : get the "id" loadElVal.
     *
     * @param id the id of the loadElValDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loadElValDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/load-el-vals/{id}")
    public ResponseEntity<LoadElValDTO> getLoadElVal(@PathVariable Long id) {
        log.debug("REST request to get LoadElVal : {}", id);
        Optional<LoadElValDTO> loadElValDTO = loadElValService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loadElValDTO);
    }

    /**
     * {@code DELETE  /load-el-vals/:id} : delete the "id" loadElVal.
     *
     * @param id the id of the loadElValDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/load-el-vals/{id}")
    public ResponseEntity<Void> deleteLoadElVal(@PathVariable Long id) {
        log.debug("REST request to delete LoadElVal : {}", id);
        loadElValService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
