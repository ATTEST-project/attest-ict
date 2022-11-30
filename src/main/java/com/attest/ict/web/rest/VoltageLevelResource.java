package com.attest.ict.web.rest;

import com.attest.ict.repository.VoltageLevelRepository;
import com.attest.ict.service.VoltageLevelQueryService;
import com.attest.ict.service.VoltageLevelService;
import com.attest.ict.service.criteria.VoltageLevelCriteria;
import com.attest.ict.service.dto.VoltageLevelDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.VoltageLevel}.
 */
@RestController
@RequestMapping("/api")
public class VoltageLevelResource {

    private final Logger log = LoggerFactory.getLogger(VoltageLevelResource.class);

    private static final String ENTITY_NAME = "voltageLevel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VoltageLevelService voltageLevelService;

    private final VoltageLevelRepository voltageLevelRepository;

    private final VoltageLevelQueryService voltageLevelQueryService;

    public VoltageLevelResource(
        VoltageLevelService voltageLevelService,
        VoltageLevelRepository voltageLevelRepository,
        VoltageLevelQueryService voltageLevelQueryService
    ) {
        this.voltageLevelService = voltageLevelService;
        this.voltageLevelRepository = voltageLevelRepository;
        this.voltageLevelQueryService = voltageLevelQueryService;
    }

    /**
     * {@code POST  /voltage-levels} : Create a new voltageLevel.
     *
     * @param voltageLevelDTO the voltageLevelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new voltageLevelDTO, or with status {@code 400 (Bad Request)} if the voltageLevel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/voltage-levels")
    public ResponseEntity<VoltageLevelDTO> createVoltageLevel(@RequestBody VoltageLevelDTO voltageLevelDTO) throws URISyntaxException {
        log.debug("REST request to save VoltageLevel : {}", voltageLevelDTO);
        if (voltageLevelDTO.getId() != null) {
            throw new BadRequestAlertException("A new voltageLevel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VoltageLevelDTO result = voltageLevelService.save(voltageLevelDTO);
        return ResponseEntity
            .created(new URI("/api/voltage-levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /voltage-levels/:id} : Updates an existing voltageLevel.
     *
     * @param id the id of the voltageLevelDTO to save.
     * @param voltageLevelDTO the voltageLevelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voltageLevelDTO,
     * or with status {@code 400 (Bad Request)} if the voltageLevelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the voltageLevelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/voltage-levels/{id}")
    public ResponseEntity<VoltageLevelDTO> updateVoltageLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VoltageLevelDTO voltageLevelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VoltageLevel : {}, {}", id, voltageLevelDTO);
        if (voltageLevelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voltageLevelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voltageLevelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VoltageLevelDTO result = voltageLevelService.save(voltageLevelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, voltageLevelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /voltage-levels/:id} : Partial updates given fields of an existing voltageLevel, field will ignore if it is null
     *
     * @param id the id of the voltageLevelDTO to save.
     * @param voltageLevelDTO the voltageLevelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voltageLevelDTO,
     * or with status {@code 400 (Bad Request)} if the voltageLevelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the voltageLevelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the voltageLevelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/voltage-levels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VoltageLevelDTO> partialUpdateVoltageLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VoltageLevelDTO voltageLevelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VoltageLevel partially : {}, {}", id, voltageLevelDTO);
        if (voltageLevelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voltageLevelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voltageLevelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VoltageLevelDTO> result = voltageLevelService.partialUpdate(voltageLevelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, voltageLevelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /voltage-levels} : get all the voltageLevels.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of voltageLevels in body.
     */
    @GetMapping("/voltage-levels")
    public ResponseEntity<List<VoltageLevelDTO>> getAllVoltageLevels(VoltageLevelCriteria criteria, Pageable pageable) {
        log.debug("REST request to get VoltageLevels by criteria: {}", criteria);
        Page<VoltageLevelDTO> page = voltageLevelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /voltage-levels/count} : count all the voltageLevels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/voltage-levels/count")
    public ResponseEntity<Long> countVoltageLevels(VoltageLevelCriteria criteria) {
        log.debug("REST request to count VoltageLevels by criteria: {}", criteria);
        return ResponseEntity.ok().body(voltageLevelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /voltage-levels/:id} : get the "id" voltageLevel.
     *
     * @param id the id of the voltageLevelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the voltageLevelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/voltage-levels/{id}")
    public ResponseEntity<VoltageLevelDTO> getVoltageLevel(@PathVariable Long id) {
        log.debug("REST request to get VoltageLevel : {}", id);
        Optional<VoltageLevelDTO> voltageLevelDTO = voltageLevelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(voltageLevelDTO);
    }

    /**
     * {@code DELETE  /voltage-levels/:id} : delete the "id" voltageLevel.
     *
     * @param id the id of the voltageLevelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/voltage-levels/{id}")
    public ResponseEntity<Void> deleteVoltageLevel(@PathVariable Long id) {
        log.debug("REST request to delete VoltageLevel : {}", id);
        voltageLevelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
