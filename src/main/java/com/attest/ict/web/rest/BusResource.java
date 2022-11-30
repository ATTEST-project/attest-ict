package com.attest.ict.web.rest;

import com.attest.ict.repository.BusRepository;
import com.attest.ict.service.BusQueryService;
import com.attest.ict.service.BusService;
import com.attest.ict.service.criteria.BusCriteria;
import com.attest.ict.service.dto.BusDTO;
import com.attest.ict.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.attest.ict.domain.Bus}.
 */
@RestController
@RequestMapping("/api")
public class BusResource {

    private final Logger log = LoggerFactory.getLogger(BusResource.class);

    private static final String ENTITY_NAME = "bus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusService busService;

    private final BusRepository busRepository;

    private final BusQueryService busQueryService;

    public BusResource(BusService busService, BusRepository busRepository, BusQueryService busQueryService) {
        this.busService = busService;
        this.busRepository = busRepository;
        this.busQueryService = busQueryService;
    }

    /**
     * {@code POST  /buses} : Create a new bus.
     *
     * @param busDTO the busDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new busDTO, or with status {@code 400 (Bad Request)} if the bus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/buses")
    public ResponseEntity<BusDTO> createBus(@RequestBody BusDTO busDTO) throws URISyntaxException {
        log.debug("REST request to save Bus : {}", busDTO);
        if (busDTO.getId() != null) {
            throw new BadRequestAlertException("A new bus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusDTO result = busService.save(busDTO);
        return ResponseEntity
            .created(new URI("/api/buses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /buses/:id} : Updates an existing bus.
     *
     * @param id the id of the busDTO to save.
     * @param busDTO the busDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated busDTO,
     * or with status {@code 400 (Bad Request)} if the busDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the busDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/buses/{id}")
    public ResponseEntity<BusDTO> updateBus(@PathVariable(value = "id", required = false) final Long id, @RequestBody BusDTO busDTO)
        throws URISyntaxException {
        log.debug("REST request to update Bus : {}, {}", id, busDTO);
        if (busDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, busDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!busRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BusDTO result = busService.save(busDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, busDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /buses/:id} : Partial updates given fields of an existing bus, field will ignore if it is null
     *
     * @param id the id of the busDTO to save.
     * @param busDTO the busDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated busDTO,
     * or with status {@code 400 (Bad Request)} if the busDTO is not valid,
     * or with status {@code 404 (Not Found)} if the busDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the busDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/buses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BusDTO> partialUpdateBus(@PathVariable(value = "id", required = false) final Long id, @RequestBody BusDTO busDTO)
        throws URISyntaxException {
        log.debug("REST request to partial update Bus partially : {}, {}", id, busDTO);
        if (busDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, busDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!busRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusDTO> result = busService.partialUpdate(busDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, busDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /buses} : get all the buses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of buses in body.
     */
    @GetMapping("/buses")
    public ResponseEntity<List<BusDTO>> getAllBuses(BusCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Buses by criteria: {}", criteria);
        Page<BusDTO> page = busQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /buses/count} : count all the buses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/buses/count")
    public ResponseEntity<Long> countBuses(BusCriteria criteria) {
        log.debug("REST request to count Buses by criteria: {}", criteria);
        return ResponseEntity.ok().body(busQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /buses/:id} : get the "id" bus.
     *
     * @param id the id of the busDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the busDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/buses/{id}")
    public ResponseEntity<BusDTO> getBus(@PathVariable Long id) {
        log.debug("REST request to get Bus : {}", id);
        Optional<BusDTO> busDTO = busService.findOne(id);
        return ResponseUtil.wrapOrNotFound(busDTO);
    }

    /**
     * {@code DELETE  /buses/:id} : delete the "id" bus.
     *
     * @param id the id of the busDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/buses/{id}")
    public ResponseEntity<Void> deleteBus(@PathVariable Long id) {
        log.debug("REST request to delete Bus : {}", id);
        busService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
