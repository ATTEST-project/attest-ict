package com.attest.ict.web.rest;

import com.attest.ict.repository.DsoTsoConnectionRepository;
import com.attest.ict.service.DsoTsoConnectionQueryService;
import com.attest.ict.service.DsoTsoConnectionService;
import com.attest.ict.service.criteria.DsoTsoConnectionCriteria;
import com.attest.ict.service.dto.DsoTsoConnectionDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.DsoTsoConnection}.
 */
@RestController
@RequestMapping("/api")
public class DsoTsoConnectionResource {

    private final Logger log = LoggerFactory.getLogger(DsoTsoConnectionResource.class);

    private static final String ENTITY_NAME = "dsoTsoConnection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DsoTsoConnectionService dsoTsoConnectionService;

    private final DsoTsoConnectionRepository dsoTsoConnectionRepository;

    private final DsoTsoConnectionQueryService dsoTsoConnectionQueryService;

    public DsoTsoConnectionResource(
        DsoTsoConnectionService dsoTsoConnectionService,
        DsoTsoConnectionRepository dsoTsoConnectionRepository,
        DsoTsoConnectionQueryService dsoTsoConnectionQueryService
    ) {
        this.dsoTsoConnectionService = dsoTsoConnectionService;
        this.dsoTsoConnectionRepository = dsoTsoConnectionRepository;
        this.dsoTsoConnectionQueryService = dsoTsoConnectionQueryService;
    }

    /**
     * {@code POST  /dso-tso-connections} : Create a new dsoTsoConnection.
     *
     * @param dsoTsoConnectionDTO the dsoTsoConnectionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dsoTsoConnectionDTO, or with status {@code 400 (Bad Request)} if the dsoTsoConnection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dso-tso-connections")
    public ResponseEntity<DsoTsoConnectionDTO> createDsoTsoConnection(@RequestBody DsoTsoConnectionDTO dsoTsoConnectionDTO)
        throws URISyntaxException {
        log.debug("REST request to save DsoTsoConnection : {}", dsoTsoConnectionDTO);
        if (dsoTsoConnectionDTO.getId() != null) {
            throw new BadRequestAlertException("A new dsoTsoConnection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DsoTsoConnectionDTO result = dsoTsoConnectionService.save(dsoTsoConnectionDTO);
        return ResponseEntity
            .created(new URI("/api/dso-tso-connections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dso-tso-connections/:id} : Updates an existing dsoTsoConnection.
     *
     * @param id the id of the dsoTsoConnectionDTO to save.
     * @param dsoTsoConnectionDTO the dsoTsoConnectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dsoTsoConnectionDTO,
     * or with status {@code 400 (Bad Request)} if the dsoTsoConnectionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dsoTsoConnectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dso-tso-connections/{id}")
    public ResponseEntity<DsoTsoConnectionDTO> updateDsoTsoConnection(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DsoTsoConnectionDTO dsoTsoConnectionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DsoTsoConnection : {}, {}", id, dsoTsoConnectionDTO);
        if (dsoTsoConnectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dsoTsoConnectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dsoTsoConnectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DsoTsoConnectionDTO result = dsoTsoConnectionService.save(dsoTsoConnectionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dsoTsoConnectionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dso-tso-connections/:id} : Partial updates given fields of an existing dsoTsoConnection, field will ignore if it is null
     *
     * @param id the id of the dsoTsoConnectionDTO to save.
     * @param dsoTsoConnectionDTO the dsoTsoConnectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dsoTsoConnectionDTO,
     * or with status {@code 400 (Bad Request)} if the dsoTsoConnectionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dsoTsoConnectionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dsoTsoConnectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dso-tso-connections/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DsoTsoConnectionDTO> partialUpdateDsoTsoConnection(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DsoTsoConnectionDTO dsoTsoConnectionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DsoTsoConnection partially : {}, {}", id, dsoTsoConnectionDTO);
        if (dsoTsoConnectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dsoTsoConnectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dsoTsoConnectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DsoTsoConnectionDTO> result = dsoTsoConnectionService.partialUpdate(dsoTsoConnectionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dsoTsoConnectionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dso-tso-connections} : get all the dsoTsoConnections.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dsoTsoConnections in body.
     */
    @GetMapping("/dso-tso-connections")
    public ResponseEntity<List<DsoTsoConnectionDTO>> getAllDsoTsoConnections(DsoTsoConnectionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DsoTsoConnections by criteria: {}", criteria);
        Page<DsoTsoConnectionDTO> page = dsoTsoConnectionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dso-tso-connections/count} : count all the dsoTsoConnections.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/dso-tso-connections/count")
    public ResponseEntity<Long> countDsoTsoConnections(DsoTsoConnectionCriteria criteria) {
        log.debug("REST request to count DsoTsoConnections by criteria: {}", criteria);
        return ResponseEntity.ok().body(dsoTsoConnectionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /dso-tso-connections/:id} : get the "id" dsoTsoConnection.
     *
     * @param id the id of the dsoTsoConnectionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dsoTsoConnectionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dso-tso-connections/{id}")
    public ResponseEntity<DsoTsoConnectionDTO> getDsoTsoConnection(@PathVariable Long id) {
        log.debug("REST request to get DsoTsoConnection : {}", id);
        Optional<DsoTsoConnectionDTO> dsoTsoConnectionDTO = dsoTsoConnectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dsoTsoConnectionDTO);
    }

    /**
     * {@code DELETE  /dso-tso-connections/:id} : delete the "id" dsoTsoConnection.
     *
     * @param id the id of the dsoTsoConnectionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dso-tso-connections/{id}")
    public ResponseEntity<Void> deleteDsoTsoConnection(@PathVariable Long id) {
        log.debug("REST request to delete DsoTsoConnection : {}", id);
        dsoTsoConnectionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
