package com.attest.ict.web.rest;

import com.attest.ict.repository.BusExtensionRepository;
import com.attest.ict.service.BusExtensionQueryService;
import com.attest.ict.service.BusExtensionService;
import com.attest.ict.service.criteria.BusExtensionCriteria;
import com.attest.ict.service.dto.BusExtensionDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.BusExtension}.
 */
@RestController
@RequestMapping("/api")
public class BusExtensionResource {

    private final Logger log = LoggerFactory.getLogger(BusExtensionResource.class);

    private static final String ENTITY_NAME = "busExtension";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusExtensionService busExtensionService;

    private final BusExtensionRepository busExtensionRepository;

    private final BusExtensionQueryService busExtensionQueryService;

    public BusExtensionResource(
        BusExtensionService busExtensionService,
        BusExtensionRepository busExtensionRepository,
        BusExtensionQueryService busExtensionQueryService
    ) {
        this.busExtensionService = busExtensionService;
        this.busExtensionRepository = busExtensionRepository;
        this.busExtensionQueryService = busExtensionQueryService;
    }

    /**
     * {@code POST  /bus-extensions} : Create a new busExtension.
     *
     * @param busExtensionDTO the busExtensionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new busExtensionDTO, or with status {@code 400 (Bad Request)} if the busExtension has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bus-extensions")
    public ResponseEntity<BusExtensionDTO> createBusExtension(@RequestBody BusExtensionDTO busExtensionDTO) throws URISyntaxException {
        log.debug("REST request to save BusExtension : {}", busExtensionDTO);
        if (busExtensionDTO.getId() != null) {
            throw new BadRequestAlertException("A new busExtension cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusExtensionDTO result = busExtensionService.save(busExtensionDTO);
        return ResponseEntity
            .created(new URI("/api/bus-extensions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bus-extensions/:id} : Updates an existing busExtension.
     *
     * @param id the id of the busExtensionDTO to save.
     * @param busExtensionDTO the busExtensionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated busExtensionDTO,
     * or with status {@code 400 (Bad Request)} if the busExtensionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the busExtensionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bus-extensions/{id}")
    public ResponseEntity<BusExtensionDTO> updateBusExtension(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BusExtensionDTO busExtensionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BusExtension : {}, {}", id, busExtensionDTO);
        if (busExtensionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, busExtensionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!busExtensionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BusExtensionDTO result = busExtensionService.save(busExtensionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, busExtensionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bus-extensions/:id} : Partial updates given fields of an existing busExtension, field will ignore if it is null
     *
     * @param id the id of the busExtensionDTO to save.
     * @param busExtensionDTO the busExtensionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated busExtensionDTO,
     * or with status {@code 400 (Bad Request)} if the busExtensionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the busExtensionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the busExtensionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bus-extensions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BusExtensionDTO> partialUpdateBusExtension(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BusExtensionDTO busExtensionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BusExtension partially : {}, {}", id, busExtensionDTO);
        if (busExtensionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, busExtensionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!busExtensionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusExtensionDTO> result = busExtensionService.partialUpdate(busExtensionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, busExtensionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bus-extensions} : get all the busExtensions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of busExtensions in body.
     */
    @GetMapping("/bus-extensions")
    public ResponseEntity<List<BusExtensionDTO>> getAllBusExtensions(BusExtensionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BusExtensions by criteria: {}", criteria);
        Page<BusExtensionDTO> page = busExtensionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bus-extensions/count} : count all the busExtensions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/bus-extensions/count")
    public ResponseEntity<Long> countBusExtensions(BusExtensionCriteria criteria) {
        log.debug("REST request to count BusExtensions by criteria: {}", criteria);
        return ResponseEntity.ok().body(busExtensionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bus-extensions/:id} : get the "id" busExtension.
     *
     * @param id the id of the busExtensionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the busExtensionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bus-extensions/{id}")
    public ResponseEntity<BusExtensionDTO> getBusExtension(@PathVariable Long id) {
        log.debug("REST request to get BusExtension : {}", id);
        Optional<BusExtensionDTO> busExtensionDTO = busExtensionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(busExtensionDTO);
    }

    /**
     * {@code DELETE  /bus-extensions/:id} : delete the "id" busExtension.
     *
     * @param id the id of the busExtensionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bus-extensions/{id}")
    public ResponseEntity<Void> deleteBusExtension(@PathVariable Long id) {
        log.debug("REST request to delete BusExtension : {}", id);
        busExtensionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
