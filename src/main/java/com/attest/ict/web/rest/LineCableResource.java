package com.attest.ict.web.rest;

import com.attest.ict.repository.LineCableRepository;
import com.attest.ict.service.LineCableQueryService;
import com.attest.ict.service.LineCableService;
import com.attest.ict.service.criteria.LineCableCriteria;
import com.attest.ict.service.dto.LineCableDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.LineCable}.
 */
@RestController
@RequestMapping("/api")
public class LineCableResource {

    private final Logger log = LoggerFactory.getLogger(LineCableResource.class);

    private static final String ENTITY_NAME = "lineCable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LineCableService lineCableService;

    private final LineCableRepository lineCableRepository;

    private final LineCableQueryService lineCableQueryService;

    public LineCableResource(
        LineCableService lineCableService,
        LineCableRepository lineCableRepository,
        LineCableQueryService lineCableQueryService
    ) {
        this.lineCableService = lineCableService;
        this.lineCableRepository = lineCableRepository;
        this.lineCableQueryService = lineCableQueryService;
    }

    /**
     * {@code POST  /line-cables} : Create a new lineCable.
     *
     * @param lineCableDTO the lineCableDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lineCableDTO, or with status {@code 400 (Bad Request)} if the lineCable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/line-cables")
    public ResponseEntity<LineCableDTO> createLineCable(@RequestBody LineCableDTO lineCableDTO) throws URISyntaxException {
        log.debug("REST request to save LineCable : {}", lineCableDTO);
        if (lineCableDTO.getId() != null) {
            throw new BadRequestAlertException("A new lineCable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LineCableDTO result = lineCableService.save(lineCableDTO);
        return ResponseEntity
            .created(new URI("/api/line-cables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /line-cables/:id} : Updates an existing lineCable.
     *
     * @param id the id of the lineCableDTO to save.
     * @param lineCableDTO the lineCableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lineCableDTO,
     * or with status {@code 400 (Bad Request)} if the lineCableDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lineCableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/line-cables/{id}")
    public ResponseEntity<LineCableDTO> updateLineCable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LineCableDTO lineCableDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LineCable : {}, {}", id, lineCableDTO);
        if (lineCableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lineCableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lineCableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LineCableDTO result = lineCableService.save(lineCableDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lineCableDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /line-cables/:id} : Partial updates given fields of an existing lineCable, field will ignore if it is null
     *
     * @param id the id of the lineCableDTO to save.
     * @param lineCableDTO the lineCableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lineCableDTO,
     * or with status {@code 400 (Bad Request)} if the lineCableDTO is not valid,
     * or with status {@code 404 (Not Found)} if the lineCableDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the lineCableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/line-cables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LineCableDTO> partialUpdateLineCable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LineCableDTO lineCableDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LineCable partially : {}, {}", id, lineCableDTO);
        if (lineCableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lineCableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lineCableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LineCableDTO> result = lineCableService.partialUpdate(lineCableDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lineCableDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /line-cables} : get all the lineCables.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lineCables in body.
     */
    @GetMapping("/line-cables")
    public ResponseEntity<List<LineCableDTO>> getAllLineCables(LineCableCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LineCables by criteria: {}", criteria);
        Page<LineCableDTO> page = lineCableQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /line-cables/count} : count all the lineCables.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/line-cables/count")
    public ResponseEntity<Long> countLineCables(LineCableCriteria criteria) {
        log.debug("REST request to count LineCables by criteria: {}", criteria);
        return ResponseEntity.ok().body(lineCableQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /line-cables/:id} : get the "id" lineCable.
     *
     * @param id the id of the lineCableDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lineCableDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/line-cables/{id}")
    public ResponseEntity<LineCableDTO> getLineCable(@PathVariable Long id) {
        log.debug("REST request to get LineCable : {}", id);
        Optional<LineCableDTO> lineCableDTO = lineCableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lineCableDTO);
    }

    /**
     * {@code DELETE  /line-cables/:id} : delete the "id" lineCable.
     *
     * @param id the id of the lineCableDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/line-cables/{id}")
    public ResponseEntity<Void> deleteLineCable(@PathVariable Long id) {
        log.debug("REST request to delete LineCable : {}", id);
        lineCableService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
