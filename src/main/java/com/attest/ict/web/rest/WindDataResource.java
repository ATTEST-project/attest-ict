package com.attest.ict.web.rest;

import com.attest.ict.repository.WindDataRepository;
import com.attest.ict.service.WindDataQueryService;
import com.attest.ict.service.WindDataService;
import com.attest.ict.service.criteria.WindDataCriteria;
import com.attest.ict.service.dto.WindDataDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.WindData}.
 */
@RestController
@RequestMapping("/api")
public class WindDataResource {

    private final Logger log = LoggerFactory.getLogger(WindDataResource.class);

    private static final String ENTITY_NAME = "windData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WindDataService windDataService;

    private final WindDataRepository windDataRepository;

    private final WindDataQueryService windDataQueryService;

    public WindDataResource(
        WindDataService windDataService,
        WindDataRepository windDataRepository,
        WindDataQueryService windDataQueryService
    ) {
        this.windDataService = windDataService;
        this.windDataRepository = windDataRepository;
        this.windDataQueryService = windDataQueryService;
    }

    /**
     * {@code POST  /wind-data} : Create a new windData.
     *
     * @param windDataDTO the windDataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new windDataDTO, or with status {@code 400 (Bad Request)} if the windData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wind-data")
    public ResponseEntity<WindDataDTO> createWindData(@RequestBody WindDataDTO windDataDTO) throws URISyntaxException {
        log.debug("REST request to save WindData : {}", windDataDTO);
        if (windDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new windData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WindDataDTO result = windDataService.save(windDataDTO);
        return ResponseEntity
            .created(new URI("/api/wind-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wind-data/:id} : Updates an existing windData.
     *
     * @param id the id of the windDataDTO to save.
     * @param windDataDTO the windDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated windDataDTO,
     * or with status {@code 400 (Bad Request)} if the windDataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the windDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wind-data/{id}")
    public ResponseEntity<WindDataDTO> updateWindData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WindDataDTO windDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WindData : {}, {}", id, windDataDTO);
        if (windDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, windDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!windDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WindDataDTO result = windDataService.save(windDataDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, windDataDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /wind-data/:id} : Partial updates given fields of an existing windData, field will ignore if it is null
     *
     * @param id the id of the windDataDTO to save.
     * @param windDataDTO the windDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated windDataDTO,
     * or with status {@code 400 (Bad Request)} if the windDataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the windDataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the windDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/wind-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WindDataDTO> partialUpdateWindData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WindDataDTO windDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WindData partially : {}, {}", id, windDataDTO);
        if (windDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, windDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!windDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WindDataDTO> result = windDataService.partialUpdate(windDataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, windDataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /wind-data} : get all the windData.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of windData in body.
     */
    @GetMapping("/wind-data")
    public ResponseEntity<List<WindDataDTO>> getAllWindData(WindDataCriteria criteria, Pageable pageable) {
        log.debug("REST request to get WindData by criteria: {}", criteria);
        Page<WindDataDTO> page = windDataQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wind-data/count} : count all the windData.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/wind-data/count")
    public ResponseEntity<Long> countWindData(WindDataCriteria criteria) {
        log.debug("REST request to count WindData by criteria: {}", criteria);
        return ResponseEntity.ok().body(windDataQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /wind-data/:id} : get the "id" windData.
     *
     * @param id the id of the windDataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the windDataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wind-data/{id}")
    public ResponseEntity<WindDataDTO> getWindData(@PathVariable Long id) {
        log.debug("REST request to get WindData : {}", id);
        Optional<WindDataDTO> windDataDTO = windDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(windDataDTO);
    }

    /**
     * {@code DELETE  /wind-data/:id} : delete the "id" windData.
     *
     * @param id the id of the windDataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wind-data/{id}")
    public ResponseEntity<Void> deleteWindData(@PathVariable Long id) {
        log.debug("REST request to delete WindData : {}", id);
        windDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
