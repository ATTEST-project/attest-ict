package com.attest.ict.web.rest;

import com.attest.ict.repository.ToolParameterRepository;
import com.attest.ict.service.ToolParameterQueryService;
import com.attest.ict.service.ToolParameterService;
import com.attest.ict.service.criteria.ToolParameterCriteria;
import com.attest.ict.service.dto.ToolParameterDTO;
import com.attest.ict.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.attest.ict.domain.ToolParameter}.
 */
@RestController
@RequestMapping("/api")
public class ToolParameterResource {

    private final Logger log = LoggerFactory.getLogger(ToolParameterResource.class);

    private static final String ENTITY_NAME = "toolParameter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ToolParameterService toolParameterService;

    private final ToolParameterRepository toolParameterRepository;

    private final ToolParameterQueryService toolParameterQueryService;

    public ToolParameterResource(
        ToolParameterService toolParameterService,
        ToolParameterRepository toolParameterRepository,
        ToolParameterQueryService toolParameterQueryService
    ) {
        this.toolParameterService = toolParameterService;
        this.toolParameterRepository = toolParameterRepository;
        this.toolParameterQueryService = toolParameterQueryService;
    }

    /**
     * {@code POST  /tool-parameters} : Create a new toolParameter.
     *
     * @param toolParameterDTO the toolParameterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new toolParameterDTO, or with status {@code 400 (Bad Request)} if the toolParameter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tool-parameters")
    public ResponseEntity<ToolParameterDTO> createToolParameter(@Valid @RequestBody ToolParameterDTO toolParameterDTO)
        throws URISyntaxException {
        log.debug("REST request to save ToolParameter : {}", toolParameterDTO);
        if (toolParameterDTO.getId() != null) {
            throw new BadRequestAlertException("A new toolParameter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ToolParameterDTO result = toolParameterService.save(toolParameterDTO);
        return ResponseEntity
            .created(new URI("/api/tool-parameters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tool-parameters/:id} : Updates an existing toolParameter.
     *
     * @param id the id of the toolParameterDTO to save.
     * @param toolParameterDTO the toolParameterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toolParameterDTO,
     * or with status {@code 400 (Bad Request)} if the toolParameterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the toolParameterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tool-parameters/{id}")
    public ResponseEntity<ToolParameterDTO> updateToolParameter(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ToolParameterDTO toolParameterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ToolParameter : {}, {}", id, toolParameterDTO);
        if (toolParameterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, toolParameterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!toolParameterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ToolParameterDTO result = toolParameterService.save(toolParameterDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, toolParameterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tool-parameters/:id} : Partial updates given fields of an existing toolParameter, field will ignore if it is null
     *
     * @param id the id of the toolParameterDTO to save.
     * @param toolParameterDTO the toolParameterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toolParameterDTO,
     * or with status {@code 400 (Bad Request)} if the toolParameterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the toolParameterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the toolParameterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tool-parameters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ToolParameterDTO> partialUpdateToolParameter(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ToolParameterDTO toolParameterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ToolParameter partially : {}, {}", id, toolParameterDTO);
        if (toolParameterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, toolParameterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!toolParameterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ToolParameterDTO> result = toolParameterService.partialUpdate(toolParameterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, toolParameterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tool-parameters} : get all the toolParameters.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of toolParameters in body.
     */
    @GetMapping("/tool-parameters")
    public ResponseEntity<List<ToolParameterDTO>> getAllToolParameters(ToolParameterCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ToolParameters by criteria: {}", criteria);
        Page<ToolParameterDTO> page = toolParameterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tool-parameters/count} : count all the toolParameters.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tool-parameters/count")
    public ResponseEntity<Long> countToolParameters(ToolParameterCriteria criteria) {
        log.debug("REST request to count ToolParameters by criteria: {}", criteria);
        return ResponseEntity.ok().body(toolParameterQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tool-parameters/:id} : get the "id" toolParameter.
     *
     * @param id the id of the toolParameterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the toolParameterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tool-parameters/{id}")
    public ResponseEntity<ToolParameterDTO> getToolParameter(@PathVariable Long id) {
        log.debug("REST request to get ToolParameter : {}", id);
        Optional<ToolParameterDTO> toolParameterDTO = toolParameterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(toolParameterDTO);
    }

    /**
     * {@code DELETE  /tool-parameters/:id} : delete the "id" toolParameter.
     *
     * @param id the id of the toolParameterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tool-parameters/{id}")
    public ResponseEntity<Void> deleteToolParameter(@PathVariable Long id) {
        log.debug("REST request to delete ToolParameter : {}", id);
        toolParameterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
