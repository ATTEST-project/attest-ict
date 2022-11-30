package com.attest.ict.web.rest;

import com.attest.ict.repository.TransformerRepository;
import com.attest.ict.service.TransformerQueryService;
import com.attest.ict.service.TransformerService;
import com.attest.ict.service.criteria.TransformerCriteria;
import com.attest.ict.service.dto.TransformerDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.Transformer}.
 */
@RestController
@RequestMapping("/api")
public class TransformerResource {

    private final Logger log = LoggerFactory.getLogger(TransformerResource.class);

    private static final String ENTITY_NAME = "transformer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransformerService transformerService;

    private final TransformerRepository transformerRepository;

    private final TransformerQueryService transformerQueryService;

    public TransformerResource(
        TransformerService transformerService,
        TransformerRepository transformerRepository,
        TransformerQueryService transformerQueryService
    ) {
        this.transformerService = transformerService;
        this.transformerRepository = transformerRepository;
        this.transformerQueryService = transformerQueryService;
    }

    /**
     * {@code POST  /transformers} : Create a new transformer.
     *
     * @param transformerDTO the transformerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transformerDTO, or with status {@code 400 (Bad Request)} if the transformer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transformers")
    public ResponseEntity<TransformerDTO> createTransformer(@RequestBody TransformerDTO transformerDTO) throws URISyntaxException {
        log.debug("REST request to save Transformer : {}", transformerDTO);
        if (transformerDTO.getId() != null) {
            throw new BadRequestAlertException("A new transformer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransformerDTO result = transformerService.save(transformerDTO);
        return ResponseEntity
            .created(new URI("/api/transformers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transformers/:id} : Updates an existing transformer.
     *
     * @param id the id of the transformerDTO to save.
     * @param transformerDTO the transformerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transformerDTO,
     * or with status {@code 400 (Bad Request)} if the transformerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transformerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transformers/{id}")
    public ResponseEntity<TransformerDTO> updateTransformer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TransformerDTO transformerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Transformer : {}, {}", id, transformerDTO);
        if (transformerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transformerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transformerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransformerDTO result = transformerService.save(transformerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transformerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transformers/:id} : Partial updates given fields of an existing transformer, field will ignore if it is null
     *
     * @param id the id of the transformerDTO to save.
     * @param transformerDTO the transformerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transformerDTO,
     * or with status {@code 400 (Bad Request)} if the transformerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transformerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transformerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transformers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransformerDTO> partialUpdateTransformer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TransformerDTO transformerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Transformer partially : {}, {}", id, transformerDTO);
        if (transformerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transformerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transformerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransformerDTO> result = transformerService.partialUpdate(transformerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transformerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transformers} : get all the transformers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transformers in body.
     */
    @GetMapping("/transformers")
    public ResponseEntity<List<TransformerDTO>> getAllTransformers(TransformerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Transformers by criteria: {}", criteria);
        Page<TransformerDTO> page = transformerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transformers/count} : count all the transformers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/transformers/count")
    public ResponseEntity<Long> countTransformers(TransformerCriteria criteria) {
        log.debug("REST request to count Transformers by criteria: {}", criteria);
        return ResponseEntity.ok().body(transformerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transformers/:id} : get the "id" transformer.
     *
     * @param id the id of the transformerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transformerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transformers/{id}")
    public ResponseEntity<TransformerDTO> getTransformer(@PathVariable Long id) {
        log.debug("REST request to get Transformer : {}", id);
        Optional<TransformerDTO> transformerDTO = transformerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transformerDTO);
    }

    /**
     * {@code DELETE  /transformers/:id} : delete the "id" transformer.
     *
     * @param id the id of the transformerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transformers/{id}")
    public ResponseEntity<Void> deleteTransformer(@PathVariable Long id) {
        log.debug("REST request to delete Transformer : {}", id);
        transformerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
