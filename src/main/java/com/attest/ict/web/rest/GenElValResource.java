package com.attest.ict.web.rest;

import com.attest.ict.repository.GenElValRepository;
import com.attest.ict.service.GenElValQueryService;
import com.attest.ict.service.GenElValService;
import com.attest.ict.service.criteria.GenElValCriteria;
import com.attest.ict.service.dto.GenElValDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.GenElVal}.
 */
@RestController
@RequestMapping("/api")
public class GenElValResource {

    private final Logger log = LoggerFactory.getLogger(GenElValResource.class);

    private static final String ENTITY_NAME = "genElVal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GenElValService genElValService;

    private final GenElValRepository genElValRepository;

    private final GenElValQueryService genElValQueryService;

    public GenElValResource(
        GenElValService genElValService,
        GenElValRepository genElValRepository,
        GenElValQueryService genElValQueryService
    ) {
        this.genElValService = genElValService;
        this.genElValRepository = genElValRepository;
        this.genElValQueryService = genElValQueryService;
    }

    /**
     * {@code POST  /gen-el-vals} : Create a new genElVal.
     *
     * @param genElValDTO the genElValDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new genElValDTO, or with status {@code 400 (Bad Request)} if the genElVal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gen-el-vals")
    public ResponseEntity<GenElValDTO> createGenElVal(@RequestBody GenElValDTO genElValDTO) throws URISyntaxException {
        log.debug("REST request to save GenElVal : {}", genElValDTO);
        if (genElValDTO.getId() != null) {
            throw new BadRequestAlertException("A new genElVal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GenElValDTO result = genElValService.save(genElValDTO);
        return ResponseEntity
            .created(new URI("/api/gen-el-vals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gen-el-vals/:id} : Updates an existing genElVal.
     *
     * @param id the id of the genElValDTO to save.
     * @param genElValDTO the genElValDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated genElValDTO,
     * or with status {@code 400 (Bad Request)} if the genElValDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the genElValDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gen-el-vals/{id}")
    public ResponseEntity<GenElValDTO> updateGenElVal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GenElValDTO genElValDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GenElVal : {}, {}", id, genElValDTO);
        if (genElValDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, genElValDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!genElValRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GenElValDTO result = genElValService.save(genElValDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, genElValDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gen-el-vals/:id} : Partial updates given fields of an existing genElVal, field will ignore if it is null
     *
     * @param id the id of the genElValDTO to save.
     * @param genElValDTO the genElValDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated genElValDTO,
     * or with status {@code 400 (Bad Request)} if the genElValDTO is not valid,
     * or with status {@code 404 (Not Found)} if the genElValDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the genElValDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gen-el-vals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GenElValDTO> partialUpdateGenElVal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GenElValDTO genElValDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GenElVal partially : {}, {}", id, genElValDTO);
        if (genElValDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, genElValDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!genElValRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GenElValDTO> result = genElValService.partialUpdate(genElValDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, genElValDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /gen-el-vals} : get all the genElVals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of genElVals in body.
     */
    @GetMapping("/gen-el-vals")
    public ResponseEntity<List<GenElValDTO>> getAllGenElVals(GenElValCriteria criteria, Pageable pageable) {
        log.debug("REST request to get GenElVals by criteria: {}", criteria);
        Page<GenElValDTO> page = genElValQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gen-el-vals/count} : count all the genElVals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/gen-el-vals/count")
    public ResponseEntity<Long> countGenElVals(GenElValCriteria criteria) {
        log.debug("REST request to count GenElVals by criteria: {}", criteria);
        return ResponseEntity.ok().body(genElValQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /gen-el-vals/:id} : get the "id" genElVal.
     *
     * @param id the id of the genElValDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the genElValDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gen-el-vals/{id}")
    public ResponseEntity<GenElValDTO> getGenElVal(@PathVariable Long id) {
        log.debug("REST request to get GenElVal : {}", id);
        Optional<GenElValDTO> genElValDTO = genElValService.findOne(id);
        return ResponseUtil.wrapOrNotFound(genElValDTO);
    }

    /**
     * {@code DELETE  /gen-el-vals/:id} : delete the "id" genElVal.
     *
     * @param id the id of the genElValDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gen-el-vals/{id}")
    public ResponseEntity<Void> deleteGenElVal(@PathVariable Long id) {
        log.debug("REST request to delete GenElVal : {}", id);
        genElValService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
