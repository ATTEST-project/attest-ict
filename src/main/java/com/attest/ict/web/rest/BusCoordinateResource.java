package com.attest.ict.web.rest;

import com.attest.ict.repository.BusCoordinateRepository;
import com.attest.ict.service.BusCoordinateQueryService;
import com.attest.ict.service.BusCoordinateService;
import com.attest.ict.service.criteria.BusCoordinateCriteria;
import com.attest.ict.service.dto.BusCoordinateDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.BusCoordinate}.
 */
@RestController
@RequestMapping("/api")
public class BusCoordinateResource {

    private final Logger log = LoggerFactory.getLogger(BusCoordinateResource.class);

    private static final String ENTITY_NAME = "busCoordinate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusCoordinateService busCoordinateService;

    private final BusCoordinateRepository busCoordinateRepository;

    private final BusCoordinateQueryService busCoordinateQueryService;

    public BusCoordinateResource(
        BusCoordinateService busCoordinateService,
        BusCoordinateRepository busCoordinateRepository,
        BusCoordinateQueryService busCoordinateQueryService
    ) {
        this.busCoordinateService = busCoordinateService;
        this.busCoordinateRepository = busCoordinateRepository;
        this.busCoordinateQueryService = busCoordinateQueryService;
    }

    /**
     * {@code POST  /bus-coordinates} : Create a new busCoordinate.
     *
     * @param busCoordinateDTO the busCoordinateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new busCoordinateDTO, or with status {@code 400 (Bad Request)} if the busCoordinate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bus-coordinates")
    public ResponseEntity<BusCoordinateDTO> createBusCoordinate(@RequestBody BusCoordinateDTO busCoordinateDTO) throws URISyntaxException {
        log.debug("REST request to save BusCoordinate : {}", busCoordinateDTO);
        if (busCoordinateDTO.getId() != null) {
            throw new BadRequestAlertException("A new busCoordinate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusCoordinateDTO result = busCoordinateService.save(busCoordinateDTO);
        return ResponseEntity
            .created(new URI("/api/bus-coordinates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bus-coordinates/:id} : Updates an existing busCoordinate.
     *
     * @param id the id of the busCoordinateDTO to save.
     * @param busCoordinateDTO the busCoordinateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated busCoordinateDTO,
     * or with status {@code 400 (Bad Request)} if the busCoordinateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the busCoordinateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bus-coordinates/{id}")
    public ResponseEntity<BusCoordinateDTO> updateBusCoordinate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BusCoordinateDTO busCoordinateDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BusCoordinate : {}, {}", id, busCoordinateDTO);
        if (busCoordinateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, busCoordinateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!busCoordinateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BusCoordinateDTO result = busCoordinateService.save(busCoordinateDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, busCoordinateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bus-coordinates/:id} : Partial updates given fields of an existing busCoordinate, field will ignore if it is null
     *
     * @param id the id of the busCoordinateDTO to save.
     * @param busCoordinateDTO the busCoordinateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated busCoordinateDTO,
     * or with status {@code 400 (Bad Request)} if the busCoordinateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the busCoordinateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the busCoordinateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bus-coordinates/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BusCoordinateDTO> partialUpdateBusCoordinate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BusCoordinateDTO busCoordinateDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BusCoordinate partially : {}, {}", id, busCoordinateDTO);
        if (busCoordinateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, busCoordinateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!busCoordinateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusCoordinateDTO> result = busCoordinateService.partialUpdate(busCoordinateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, busCoordinateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bus-coordinates} : get all the busCoordinates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of busCoordinates in body.
     */
    @GetMapping("/bus-coordinates")
    public ResponseEntity<List<BusCoordinateDTO>> getAllBusCoordinates(BusCoordinateCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BusCoordinates by criteria: {}", criteria);
        Page<BusCoordinateDTO> page = busCoordinateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bus-coordinates/count} : count all the busCoordinates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/bus-coordinates/count")
    public ResponseEntity<Long> countBusCoordinates(BusCoordinateCriteria criteria) {
        log.debug("REST request to count BusCoordinates by criteria: {}", criteria);
        return ResponseEntity.ok().body(busCoordinateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bus-coordinates/:id} : get the "id" busCoordinate.
     *
     * @param id the id of the busCoordinateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the busCoordinateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bus-coordinates/{id}")
    public ResponseEntity<BusCoordinateDTO> getBusCoordinate(@PathVariable Long id) {
        log.debug("REST request to get BusCoordinate : {}", id);
        Optional<BusCoordinateDTO> busCoordinateDTO = busCoordinateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(busCoordinateDTO);
    }

    /**
     * {@code DELETE  /bus-coordinates/:id} : delete the "id" busCoordinate.
     *
     * @param id the id of the busCoordinateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bus-coordinates/{id}")
    public ResponseEntity<Void> deleteBusCoordinate(@PathVariable Long id) {
        log.debug("REST request to delete BusCoordinate : {}", id);
        busCoordinateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
