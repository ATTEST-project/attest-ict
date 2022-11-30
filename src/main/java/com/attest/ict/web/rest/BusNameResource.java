package com.attest.ict.web.rest;

import com.attest.ict.repository.BusNameRepository;
import com.attest.ict.service.BusNameQueryService;
import com.attest.ict.service.BusNameService;
import com.attest.ict.service.criteria.BusNameCriteria;
import com.attest.ict.service.dto.BusNameDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.BusName}.
 */
@RestController
@RequestMapping("/api")
public class BusNameResource {

    private final Logger log = LoggerFactory.getLogger(BusNameResource.class);

    private static final String ENTITY_NAME = "busName";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusNameService busNameService;

    private final BusNameRepository busNameRepository;

    private final BusNameQueryService busNameQueryService;

    public BusNameResource(BusNameService busNameService, BusNameRepository busNameRepository, BusNameQueryService busNameQueryService) {
        this.busNameService = busNameService;
        this.busNameRepository = busNameRepository;
        this.busNameQueryService = busNameQueryService;
    }

    /**
     * {@code POST  /bus-names} : Create a new busName.
     *
     * @param busNameDTO the busNameDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new busNameDTO, or with status {@code 400 (Bad Request)} if the busName has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bus-names")
    public ResponseEntity<BusNameDTO> createBusName(@RequestBody BusNameDTO busNameDTO) throws URISyntaxException {
        log.debug("REST request to save BusName : {}", busNameDTO);
        if (busNameDTO.getId() != null) {
            throw new BadRequestAlertException("A new busName cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusNameDTO result = busNameService.save(busNameDTO);
        return ResponseEntity
            .created(new URI("/api/bus-names/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bus-names/:id} : Updates an existing busName.
     *
     * @param id the id of the busNameDTO to save.
     * @param busNameDTO the busNameDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated busNameDTO,
     * or with status {@code 400 (Bad Request)} if the busNameDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the busNameDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bus-names/{id}")
    public ResponseEntity<BusNameDTO> updateBusName(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BusNameDTO busNameDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BusName : {}, {}", id, busNameDTO);
        if (busNameDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, busNameDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!busNameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BusNameDTO result = busNameService.save(busNameDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, busNameDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bus-names/:id} : Partial updates given fields of an existing busName, field will ignore if it is null
     *
     * @param id the id of the busNameDTO to save.
     * @param busNameDTO the busNameDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated busNameDTO,
     * or with status {@code 400 (Bad Request)} if the busNameDTO is not valid,
     * or with status {@code 404 (Not Found)} if the busNameDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the busNameDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bus-names/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BusNameDTO> partialUpdateBusName(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BusNameDTO busNameDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BusName partially : {}, {}", id, busNameDTO);
        if (busNameDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, busNameDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!busNameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusNameDTO> result = busNameService.partialUpdate(busNameDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, busNameDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bus-names} : get all the busNames.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of busNames in body.
     */
    @GetMapping("/bus-names")
    public ResponseEntity<List<BusNameDTO>> getAllBusNames(BusNameCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BusNames by criteria: {}", criteria);
        Page<BusNameDTO> page = busNameQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bus-names/count} : count all the busNames.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/bus-names/count")
    public ResponseEntity<Long> countBusNames(BusNameCriteria criteria) {
        log.debug("REST request to count BusNames by criteria: {}", criteria);
        return ResponseEntity.ok().body(busNameQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bus-names/:id} : get the "id" busName.
     *
     * @param id the id of the busNameDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the busNameDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bus-names/{id}")
    public ResponseEntity<BusNameDTO> getBusName(@PathVariable Long id) {
        log.debug("REST request to get BusName : {}", id);
        Optional<BusNameDTO> busNameDTO = busNameService.findOne(id);
        return ResponseUtil.wrapOrNotFound(busNameDTO);
    }

    /**
     * {@code DELETE  /bus-names/:id} : delete the "id" busName.
     *
     * @param id the id of the busNameDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bus-names/{id}")
    public ResponseEntity<Void> deleteBusName(@PathVariable Long id) {
        log.debug("REST request to delete BusName : {}", id);
        busNameService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
