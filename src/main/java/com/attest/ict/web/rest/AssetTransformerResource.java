package com.attest.ict.web.rest;

import com.attest.ict.repository.AssetTransformerRepository;
import com.attest.ict.service.AssetTransformerQueryService;
import com.attest.ict.service.AssetTransformerService;
import com.attest.ict.service.criteria.AssetTransformerCriteria;
import com.attest.ict.service.dto.AssetTransformerDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.AssetTransformer}.
 */
@RestController
@RequestMapping("/api")
public class AssetTransformerResource {

    private final Logger log = LoggerFactory.getLogger(AssetTransformerResource.class);

    private static final String ENTITY_NAME = "assetTransformer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetTransformerService assetTransformerService;

    private final AssetTransformerRepository assetTransformerRepository;

    private final AssetTransformerQueryService assetTransformerQueryService;

    public AssetTransformerResource(
        AssetTransformerService assetTransformerService,
        AssetTransformerRepository assetTransformerRepository,
        AssetTransformerQueryService assetTransformerQueryService
    ) {
        this.assetTransformerService = assetTransformerService;
        this.assetTransformerRepository = assetTransformerRepository;
        this.assetTransformerQueryService = assetTransformerQueryService;
    }

    /**
     * {@code POST  /asset-transformers} : Create a new assetTransformer.
     *
     * @param assetTransformerDTO the assetTransformerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetTransformerDTO, or with status {@code 400 (Bad Request)} if the assetTransformer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asset-transformers")
    public ResponseEntity<AssetTransformerDTO> createAssetTransformer(@RequestBody AssetTransformerDTO assetTransformerDTO)
        throws URISyntaxException {
        log.debug("REST request to save AssetTransformer : {}", assetTransformerDTO);
        if (assetTransformerDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetTransformer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssetTransformerDTO result = assetTransformerService.save(assetTransformerDTO);
        return ResponseEntity
            .created(new URI("/api/asset-transformers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asset-transformers/:id} : Updates an existing assetTransformer.
     *
     * @param id the id of the assetTransformerDTO to save.
     * @param assetTransformerDTO the assetTransformerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetTransformerDTO,
     * or with status {@code 400 (Bad Request)} if the assetTransformerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetTransformerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asset-transformers/{id}")
    public ResponseEntity<AssetTransformerDTO> updateAssetTransformer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssetTransformerDTO assetTransformerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AssetTransformer : {}, {}", id, assetTransformerDTO);
        if (assetTransformerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetTransformerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetTransformerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssetTransformerDTO result = assetTransformerService.save(assetTransformerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assetTransformerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /asset-transformers/:id} : Partial updates given fields of an existing assetTransformer, field will ignore if it is null
     *
     * @param id the id of the assetTransformerDTO to save.
     * @param assetTransformerDTO the assetTransformerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetTransformerDTO,
     * or with status {@code 400 (Bad Request)} if the assetTransformerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetTransformerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetTransformerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/asset-transformers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssetTransformerDTO> partialUpdateAssetTransformer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssetTransformerDTO assetTransformerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AssetTransformer partially : {}, {}", id, assetTransformerDTO);
        if (assetTransformerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetTransformerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetTransformerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssetTransformerDTO> result = assetTransformerService.partialUpdate(assetTransformerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assetTransformerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asset-transformers} : get all the assetTransformers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetTransformers in body.
     */
    @GetMapping("/asset-transformers")
    public ResponseEntity<List<AssetTransformerDTO>> getAllAssetTransformers(AssetTransformerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AssetTransformers by criteria: {}", criteria);
        Page<AssetTransformerDTO> page = assetTransformerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asset-transformers/count} : count all the assetTransformers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/asset-transformers/count")
    public ResponseEntity<Long> countAssetTransformers(AssetTransformerCriteria criteria) {
        log.debug("REST request to count AssetTransformers by criteria: {}", criteria);
        return ResponseEntity.ok().body(assetTransformerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /asset-transformers/:id} : get the "id" assetTransformer.
     *
     * @param id the id of the assetTransformerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetTransformerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asset-transformers/{id}")
    public ResponseEntity<AssetTransformerDTO> getAssetTransformer(@PathVariable Long id) {
        log.debug("REST request to get AssetTransformer : {}", id);
        Optional<AssetTransformerDTO> assetTransformerDTO = assetTransformerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetTransformerDTO);
    }

    /**
     * {@code DELETE  /asset-transformers/:id} : delete the "id" assetTransformer.
     *
     * @param id the id of the assetTransformerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asset-transformers/{id}")
    public ResponseEntity<Void> deleteAssetTransformer(@PathVariable Long id) {
        log.debug("REST request to delete AssetTransformer : {}", id);
        assetTransformerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
