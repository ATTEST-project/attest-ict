package com.attest.ict.web.rest;

import com.attest.ict.repository.SolarDataRepository;
import com.attest.ict.service.SolarDataQueryService;
import com.attest.ict.service.SolarDataService;
import com.attest.ict.service.criteria.SolarDataCriteria;
import com.attest.ict.service.dto.SolarDataDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.SolarData}.
 */
@RestController
@RequestMapping("/api")
public class SolarDataResource {

    private final Logger log = LoggerFactory.getLogger(SolarDataResource.class);

    private static final String ENTITY_NAME = "solarData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SolarDataService solarDataService;

    private final SolarDataRepository solarDataRepository;

    private final SolarDataQueryService solarDataQueryService;

    public SolarDataResource(
        SolarDataService solarDataService,
        SolarDataRepository solarDataRepository,
        SolarDataQueryService solarDataQueryService
    ) {
        this.solarDataService = solarDataService;
        this.solarDataRepository = solarDataRepository;
        this.solarDataQueryService = solarDataQueryService;
    }

    /**
     * {@code POST  /solar-data} : Create a new solarData.
     *
     * @param solarDataDTO the solarDataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new solarDataDTO, or with status {@code 400 (Bad Request)} if the solarData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/solar-data")
    public ResponseEntity<SolarDataDTO> createSolarData(@RequestBody SolarDataDTO solarDataDTO) throws URISyntaxException {
        log.debug("REST request to save SolarData : {}", solarDataDTO);
        if (solarDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new solarData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SolarDataDTO result = solarDataService.save(solarDataDTO);
        return ResponseEntity
            .created(new URI("/api/solar-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /solar-data/:id} : Updates an existing solarData.
     *
     * @param id the id of the solarDataDTO to save.
     * @param solarDataDTO the solarDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated solarDataDTO,
     * or with status {@code 400 (Bad Request)} if the solarDataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the solarDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/solar-data/{id}")
    public ResponseEntity<SolarDataDTO> updateSolarData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SolarDataDTO solarDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SolarData : {}, {}", id, solarDataDTO);
        if (solarDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, solarDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!solarDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SolarDataDTO result = solarDataService.save(solarDataDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, solarDataDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /solar-data/:id} : Partial updates given fields of an existing solarData, field will ignore if it is null
     *
     * @param id the id of the solarDataDTO to save.
     * @param solarDataDTO the solarDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated solarDataDTO,
     * or with status {@code 400 (Bad Request)} if the solarDataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the solarDataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the solarDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/solar-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SolarDataDTO> partialUpdateSolarData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SolarDataDTO solarDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SolarData partially : {}, {}", id, solarDataDTO);
        if (solarDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, solarDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!solarDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SolarDataDTO> result = solarDataService.partialUpdate(solarDataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, solarDataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /solar-data} : get all the solarData.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of solarData in body.
     */
    @GetMapping("/solar-data")
    public ResponseEntity<List<SolarDataDTO>> getAllSolarData(SolarDataCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SolarData by criteria: {}", criteria);
        Page<SolarDataDTO> page = solarDataQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /solar-data/count} : count all the solarData.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/solar-data/count")
    public ResponseEntity<Long> countSolarData(SolarDataCriteria criteria) {
        log.debug("REST request to count SolarData by criteria: {}", criteria);
        return ResponseEntity.ok().body(solarDataQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /solar-data/:id} : get the "id" solarData.
     *
     * @param id the id of the solarDataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the solarDataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/solar-data/{id}")
    public ResponseEntity<SolarDataDTO> getSolarData(@PathVariable Long id) {
        log.debug("REST request to get SolarData : {}", id);
        Optional<SolarDataDTO> solarDataDTO = solarDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(solarDataDTO);
    }

    /**
     * {@code DELETE  /solar-data/:id} : delete the "id" solarData.
     *
     * @param id the id of the solarDataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/solar-data/{id}")
    public ResponseEntity<Void> deleteSolarData(@PathVariable Long id) {
        log.debug("REST request to delete SolarData : {}", id);
        solarDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
