package com.attest.ict.web.rest;

import com.attest.ict.repository.AssetUGCableRepository;
import com.attest.ict.service.AssetUGCableQueryService;
import com.attest.ict.service.AssetUGCableService;
import com.attest.ict.service.criteria.AssetUGCableCriteria;
import com.attest.ict.service.dto.AssetUGCableDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.AssetUGCable}.
 */
@RestController
@RequestMapping("/api")
public class AssetUGCableResource {

    private final Logger log = LoggerFactory.getLogger(AssetUGCableResource.class);

    private static final String ENTITY_NAME = "assetUGCable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetUGCableService assetUGCableService;

    private final AssetUGCableRepository assetUGCableRepository;

    private final AssetUGCableQueryService assetUGCableQueryService;

    public AssetUGCableResource(
        AssetUGCableService assetUGCableService,
        AssetUGCableRepository assetUGCableRepository,
        AssetUGCableQueryService assetUGCableQueryService
    ) {
        this.assetUGCableService = assetUGCableService;
        this.assetUGCableRepository = assetUGCableRepository;
        this.assetUGCableQueryService = assetUGCableQueryService;
    }

    /**
     * {@code POST  /asset-ug-cables} : Create a new assetUGCable.
     *
     * @param assetUGCableDTO the assetUGCableDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetUGCableDTO, or with status {@code 400 (Bad Request)} if the assetUGCable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asset-ug-cables")
    public ResponseEntity<AssetUGCableDTO> createAssetUGCable(@RequestBody AssetUGCableDTO assetUGCableDTO) throws URISyntaxException {
        log.debug("REST request to save AssetUGCable : {}", assetUGCableDTO);
        if (assetUGCableDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetUGCable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssetUGCableDTO result = assetUGCableService.save(assetUGCableDTO);
        return ResponseEntity
            .created(new URI("/api/asset-ug-cables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asset-ug-cables/:id} : Updates an existing assetUGCable.
     *
     * @param id the id of the assetUGCableDTO to save.
     * @param assetUGCableDTO the assetUGCableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetUGCableDTO,
     * or with status {@code 400 (Bad Request)} if the assetUGCableDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetUGCableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asset-ug-cables/{id}")
    public ResponseEntity<AssetUGCableDTO> updateAssetUGCable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssetUGCableDTO assetUGCableDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AssetUGCable : {}, {}", id, assetUGCableDTO);
        if (assetUGCableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetUGCableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetUGCableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssetUGCableDTO result = assetUGCableService.save(assetUGCableDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assetUGCableDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /asset-ug-cables/:id} : Partial updates given fields of an existing assetUGCable, field will ignore if it is null
     *
     * @param id the id of the assetUGCableDTO to save.
     * @param assetUGCableDTO the assetUGCableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetUGCableDTO,
     * or with status {@code 400 (Bad Request)} if the assetUGCableDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetUGCableDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetUGCableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/asset-ug-cables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssetUGCableDTO> partialUpdateAssetUGCable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssetUGCableDTO assetUGCableDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AssetUGCable partially : {}, {}", id, assetUGCableDTO);
        if (assetUGCableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetUGCableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetUGCableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssetUGCableDTO> result = assetUGCableService.partialUpdate(assetUGCableDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assetUGCableDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asset-ug-cables} : get all the assetUGCables.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetUGCables in body.
     */
    @GetMapping("/asset-ug-cables")
    public ResponseEntity<List<AssetUGCableDTO>> getAllAssetUGCables(AssetUGCableCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AssetUGCables by criteria: {}", criteria);
        Page<AssetUGCableDTO> page = assetUGCableQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asset-ug-cables/count} : count all the assetUGCables.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/asset-ug-cables/count")
    public ResponseEntity<Long> countAssetUGCables(AssetUGCableCriteria criteria) {
        log.debug("REST request to count AssetUGCables by criteria: {}", criteria);
        return ResponseEntity.ok().body(assetUGCableQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /asset-ug-cables/:id} : get the "id" assetUGCable.
     *
     * @param id the id of the assetUGCableDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetUGCableDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asset-ug-cables/{id}")
    public ResponseEntity<AssetUGCableDTO> getAssetUGCable(@PathVariable Long id) {
        log.debug("REST request to get AssetUGCable : {}", id);
        Optional<AssetUGCableDTO> assetUGCableDTO = assetUGCableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetUGCableDTO);
    }

    /**
     * {@code DELETE  /asset-ug-cables/:id} : delete the "id" assetUGCable.
     *
     * @param id the id of the assetUGCableDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asset-ug-cables/{id}")
    public ResponseEntity<Void> deleteAssetUGCable(@PathVariable Long id) {
        log.debug("REST request to delete AssetUGCable : {}", id);
        assetUGCableService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
