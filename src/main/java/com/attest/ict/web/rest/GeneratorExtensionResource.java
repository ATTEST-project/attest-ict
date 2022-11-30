package com.attest.ict.web.rest;

import com.attest.ict.repository.GeneratorExtensionRepository;
import com.attest.ict.service.GeneratorExtensionQueryService;
import com.attest.ict.service.GeneratorExtensionService;
import com.attest.ict.service.criteria.GeneratorExtensionCriteria;
import com.attest.ict.service.dto.GeneratorExtensionDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.GeneratorExtension}.
 */
@RestController
@RequestMapping("/api")
public class GeneratorExtensionResource {

    private final Logger log = LoggerFactory.getLogger(GeneratorExtensionResource.class);

    private static final String ENTITY_NAME = "generatorExtension";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GeneratorExtensionService generatorExtensionService;

    private final GeneratorExtensionRepository generatorExtensionRepository;

    private final GeneratorExtensionQueryService generatorExtensionQueryService;

    public GeneratorExtensionResource(
        GeneratorExtensionService generatorExtensionService,
        GeneratorExtensionRepository generatorExtensionRepository,
        GeneratorExtensionQueryService generatorExtensionQueryService
    ) {
        this.generatorExtensionService = generatorExtensionService;
        this.generatorExtensionRepository = generatorExtensionRepository;
        this.generatorExtensionQueryService = generatorExtensionQueryService;
    }

    /**
     * {@code POST  /generator-extensions} : Create a new generatorExtension.
     *
     * @param generatorExtensionDTO the generatorExtensionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new generatorExtensionDTO, or with status {@code 400 (Bad Request)} if the generatorExtension has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/generator-extensions")
    public ResponseEntity<GeneratorExtensionDTO> createGeneratorExtension(@RequestBody GeneratorExtensionDTO generatorExtensionDTO)
        throws URISyntaxException {
        log.debug("REST request to save GeneratorExtension : {}", generatorExtensionDTO);
        if (generatorExtensionDTO.getId() != null) {
            throw new BadRequestAlertException("A new generatorExtension cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GeneratorExtensionDTO result = generatorExtensionService.save(generatorExtensionDTO);
        return ResponseEntity
            .created(new URI("/api/generator-extensions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /generator-extensions/:id} : Updates an existing generatorExtension.
     *
     * @param id the id of the generatorExtensionDTO to save.
     * @param generatorExtensionDTO the generatorExtensionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated generatorExtensionDTO,
     * or with status {@code 400 (Bad Request)} if the generatorExtensionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the generatorExtensionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/generator-extensions/{id}")
    public ResponseEntity<GeneratorExtensionDTO> updateGeneratorExtension(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GeneratorExtensionDTO generatorExtensionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GeneratorExtension : {}, {}", id, generatorExtensionDTO);
        if (generatorExtensionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, generatorExtensionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!generatorExtensionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GeneratorExtensionDTO result = generatorExtensionService.save(generatorExtensionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, generatorExtensionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /generator-extensions/:id} : Partial updates given fields of an existing generatorExtension, field will ignore if it is null
     *
     * @param id the id of the generatorExtensionDTO to save.
     * @param generatorExtensionDTO the generatorExtensionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated generatorExtensionDTO,
     * or with status {@code 400 (Bad Request)} if the generatorExtensionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the generatorExtensionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the generatorExtensionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/generator-extensions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GeneratorExtensionDTO> partialUpdateGeneratorExtension(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GeneratorExtensionDTO generatorExtensionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GeneratorExtension partially : {}, {}", id, generatorExtensionDTO);
        if (generatorExtensionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, generatorExtensionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!generatorExtensionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GeneratorExtensionDTO> result = generatorExtensionService.partialUpdate(generatorExtensionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, generatorExtensionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /generator-extensions} : get all the generatorExtensions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of generatorExtensions in body.
     */
    @GetMapping("/generator-extensions")
    public ResponseEntity<List<GeneratorExtensionDTO>> getAllGeneratorExtensions(GeneratorExtensionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get GeneratorExtensions by criteria: {}", criteria);
        Page<GeneratorExtensionDTO> page = generatorExtensionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /generator-extensions/count} : count all the generatorExtensions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/generator-extensions/count")
    public ResponseEntity<Long> countGeneratorExtensions(GeneratorExtensionCriteria criteria) {
        log.debug("REST request to count GeneratorExtensions by criteria: {}", criteria);
        return ResponseEntity.ok().body(generatorExtensionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /generator-extensions/:id} : get the "id" generatorExtension.
     *
     * @param id the id of the generatorExtensionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the generatorExtensionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/generator-extensions/{id}")
    public ResponseEntity<GeneratorExtensionDTO> getGeneratorExtension(@PathVariable Long id) {
        log.debug("REST request to get GeneratorExtension : {}", id);
        Optional<GeneratorExtensionDTO> generatorExtensionDTO = generatorExtensionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(generatorExtensionDTO);
    }

    /**
     * {@code DELETE  /generator-extensions/:id} : delete the "id" generatorExtension.
     *
     * @param id the id of the generatorExtensionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/generator-extensions/{id}")
    public ResponseEntity<Void> deleteGeneratorExtension(@PathVariable Long id) {
        log.debug("REST request to delete GeneratorExtension : {}", id);
        generatorExtensionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
