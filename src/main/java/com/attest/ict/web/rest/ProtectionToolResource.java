package com.attest.ict.web.rest;

import com.attest.ict.repository.ProtectionToolRepository;
import com.attest.ict.service.ProtectionToolQueryService;
import com.attest.ict.service.ProtectionToolService;
import com.attest.ict.service.criteria.ProtectionToolCriteria;
import com.attest.ict.service.dto.ProtectionToolDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.ProtectionTool}.
 */
@RestController
@RequestMapping("/api")
public class ProtectionToolResource {

    private final Logger log = LoggerFactory.getLogger(ProtectionToolResource.class);

    private static final String ENTITY_NAME = "protectionTool";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProtectionToolService protectionToolService;

    private final ProtectionToolRepository protectionToolRepository;

    private final ProtectionToolQueryService protectionToolQueryService;

    public ProtectionToolResource(
        ProtectionToolService protectionToolService,
        ProtectionToolRepository protectionToolRepository,
        ProtectionToolQueryService protectionToolQueryService
    ) {
        this.protectionToolService = protectionToolService;
        this.protectionToolRepository = protectionToolRepository;
        this.protectionToolQueryService = protectionToolQueryService;
    }

    /**
     * {@code POST  /protection-tools} : Create a new protectionTool.
     *
     * @param protectionToolDTO the protectionToolDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new protectionToolDTO, or with status {@code 400 (Bad Request)} if the protectionTool has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/protection-tools")
    public ResponseEntity<ProtectionToolDTO> createProtectionTool(@RequestBody ProtectionToolDTO protectionToolDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProtectionTool : {}", protectionToolDTO);
        if (protectionToolDTO.getId() != null) {
            throw new BadRequestAlertException("A new protectionTool cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProtectionToolDTO result = protectionToolService.save(protectionToolDTO);
        return ResponseEntity
            .created(new URI("/api/protection-tools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /protection-tools/:id} : Updates an existing protectionTool.
     *
     * @param id the id of the protectionToolDTO to save.
     * @param protectionToolDTO the protectionToolDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated protectionToolDTO,
     * or with status {@code 400 (Bad Request)} if the protectionToolDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the protectionToolDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/protection-tools/{id}")
    public ResponseEntity<ProtectionToolDTO> updateProtectionTool(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProtectionToolDTO protectionToolDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProtectionTool : {}, {}", id, protectionToolDTO);
        if (protectionToolDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, protectionToolDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!protectionToolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProtectionToolDTO result = protectionToolService.save(protectionToolDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, protectionToolDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /protection-tools/:id} : Partial updates given fields of an existing protectionTool, field will ignore if it is null
     *
     * @param id the id of the protectionToolDTO to save.
     * @param protectionToolDTO the protectionToolDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated protectionToolDTO,
     * or with status {@code 400 (Bad Request)} if the protectionToolDTO is not valid,
     * or with status {@code 404 (Not Found)} if the protectionToolDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the protectionToolDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/protection-tools/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProtectionToolDTO> partialUpdateProtectionTool(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProtectionToolDTO protectionToolDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProtectionTool partially : {}, {}", id, protectionToolDTO);
        if (protectionToolDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, protectionToolDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!protectionToolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProtectionToolDTO> result = protectionToolService.partialUpdate(protectionToolDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, protectionToolDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /protection-tools} : get all the protectionTools.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of protectionTools in body.
     */
    @GetMapping("/protection-tools")
    public ResponseEntity<List<ProtectionToolDTO>> getAllProtectionTools(ProtectionToolCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProtectionTools by criteria: {}", criteria);
        Page<ProtectionToolDTO> page = protectionToolQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /protection-tools/count} : count all the protectionTools.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/protection-tools/count")
    public ResponseEntity<Long> countProtectionTools(ProtectionToolCriteria criteria) {
        log.debug("REST request to count ProtectionTools by criteria: {}", criteria);
        return ResponseEntity.ok().body(protectionToolQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /protection-tools/:id} : get the "id" protectionTool.
     *
     * @param id the id of the protectionToolDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the protectionToolDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/protection-tools/{id}")
    public ResponseEntity<ProtectionToolDTO> getProtectionTool(@PathVariable Long id) {
        log.debug("REST request to get ProtectionTool : {}", id);
        Optional<ProtectionToolDTO> protectionToolDTO = protectionToolService.findOne(id);
        return ResponseUtil.wrapOrNotFound(protectionToolDTO);
    }

    /**
     * {@code DELETE  /protection-tools/:id} : delete the "id" protectionTool.
     *
     * @param id the id of the protectionToolDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/protection-tools/{id}")
    public ResponseEntity<Void> deleteProtectionTool(@PathVariable Long id) {
        log.debug("REST request to delete ProtectionTool : {}", id);
        protectionToolService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
