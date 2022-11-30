package com.attest.ict.web.rest;

import com.attest.ict.repository.StorageRepository;
import com.attest.ict.service.StorageQueryService;
import com.attest.ict.service.StorageService;
import com.attest.ict.service.criteria.StorageCriteria;
import com.attest.ict.service.dto.StorageDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.Storage}.
 */
@RestController
@RequestMapping("/api")
public class StorageResource {

    private final Logger log = LoggerFactory.getLogger(StorageResource.class);

    private static final String ENTITY_NAME = "storage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StorageService storageService;

    private final StorageRepository storageRepository;

    private final StorageQueryService storageQueryService;

    public StorageResource(StorageService storageService, StorageRepository storageRepository, StorageQueryService storageQueryService) {
        this.storageService = storageService;
        this.storageRepository = storageRepository;
        this.storageQueryService = storageQueryService;
    }

    /**
     * {@code POST  /storages} : Create a new storage.
     *
     * @param storageDTO the storageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new storageDTO, or with status {@code 400 (Bad Request)} if the storage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/storages")
    public ResponseEntity<StorageDTO> createStorage(@RequestBody StorageDTO storageDTO) throws URISyntaxException {
        log.debug("REST request to save Storage : {}", storageDTO);
        if (storageDTO.getId() != null) {
            throw new BadRequestAlertException("A new storage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StorageDTO result = storageService.save(storageDTO);
        return ResponseEntity
            .created(new URI("/api/storages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /storages/:id} : Updates an existing storage.
     *
     * @param id the id of the storageDTO to save.
     * @param storageDTO the storageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storageDTO,
     * or with status {@code 400 (Bad Request)} if the storageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the storageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/storages/{id}")
    public ResponseEntity<StorageDTO> updateStorage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StorageDTO storageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Storage : {}, {}", id, storageDTO);
        if (storageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StorageDTO result = storageService.save(storageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, storageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /storages/:id} : Partial updates given fields of an existing storage, field will ignore if it is null
     *
     * @param id the id of the storageDTO to save.
     * @param storageDTO the storageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storageDTO,
     * or with status {@code 400 (Bad Request)} if the storageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the storageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the storageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/storages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StorageDTO> partialUpdateStorage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StorageDTO storageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Storage partially : {}, {}", id, storageDTO);
        if (storageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StorageDTO> result = storageService.partialUpdate(storageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, storageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /storages} : get all the storages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of storages in body.
     */
    @GetMapping("/storages")
    public ResponseEntity<List<StorageDTO>> getAllStorages(StorageCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Storages by criteria: {}", criteria);
        Page<StorageDTO> page = storageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /storages/count} : count all the storages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/storages/count")
    public ResponseEntity<Long> countStorages(StorageCriteria criteria) {
        log.debug("REST request to count Storages by criteria: {}", criteria);
        return ResponseEntity.ok().body(storageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /storages/:id} : get the "id" storage.
     *
     * @param id the id of the storageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the storageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/storages/{id}")
    public ResponseEntity<StorageDTO> getStorage(@PathVariable Long id) {
        log.debug("REST request to get Storage : {}", id);
        Optional<StorageDTO> storageDTO = storageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(storageDTO);
    }

    /**
     * {@code DELETE  /storages/:id} : delete the "id" storage.
     *
     * @param id the id of the storageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/storages/{id}")
    public ResponseEntity<Void> deleteStorage(@PathVariable Long id) {
        log.debug("REST request to delete Storage : {}", id);
        storageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
