package com.attest.ict.web.rest;

import com.attest.ict.repository.SimulationRepository;
import com.attest.ict.service.SimulationQueryService;
import com.attest.ict.service.SimulationService;
import com.attest.ict.service.criteria.SimulationCriteria;
import com.attest.ict.service.dto.SimulationDTO;
import com.attest.ict.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.attest.ict.domain.Simulation}.
 */
@RestController
@RequestMapping("/api")
public class SimulationResource {

    private final Logger log = LoggerFactory.getLogger(SimulationResource.class);

    private static final String ENTITY_NAME = "simulation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SimulationService simulationService;

    private final SimulationRepository simulationRepository;

    private final SimulationQueryService simulationQueryService;

    public SimulationResource(
        SimulationService simulationService,
        SimulationRepository simulationRepository,
        SimulationQueryService simulationQueryService
    ) {
        this.simulationService = simulationService;
        this.simulationRepository = simulationRepository;
        this.simulationQueryService = simulationQueryService;
    }

    /**
     * {@code POST  /simulations} : Create a new simulation.
     *
     * @param simulationDTO the simulationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new simulationDTO, or with status {@code 400 (Bad Request)} if the simulation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/simulations")
    public ResponseEntity<SimulationDTO> createSimulation(@Valid @RequestBody SimulationDTO simulationDTO) throws URISyntaxException {
        log.debug("REST request to save Simulation : {}", simulationDTO);
        if (simulationDTO.getId() != null) {
            throw new BadRequestAlertException("A new simulation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SimulationDTO result = simulationService.save(simulationDTO);
        return ResponseEntity
            .created(new URI("/api/simulations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /simulations/:id} : Updates an existing simulation.
     *
     * @param id the id of the simulationDTO to save.
     * @param simulationDTO the simulationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated simulationDTO,
     * or with status {@code 400 (Bad Request)} if the simulationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the simulationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/simulations/{id}")
    public ResponseEntity<SimulationDTO> updateSimulation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SimulationDTO simulationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Simulation : {}, {}", id, simulationDTO);
        if (simulationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, simulationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!simulationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SimulationDTO result = simulationService.save(simulationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, simulationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /simulations/:id} : Partial updates given fields of an existing simulation, field will ignore if it is null
     *
     * @param id the id of the simulationDTO to save.
     * @param simulationDTO the simulationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated simulationDTO,
     * or with status {@code 400 (Bad Request)} if the simulationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the simulationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the simulationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/simulations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SimulationDTO> partialUpdateSimulation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SimulationDTO simulationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Simulation partially : {}, {}", id, simulationDTO);
        if (simulationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, simulationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!simulationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SimulationDTO> result = simulationService.partialUpdate(simulationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, simulationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /simulations} : get all the simulations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of simulations in body.
     */
    @GetMapping("/simulations")
    public ResponseEntity<List<SimulationDTO>> getAllSimulations(SimulationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Simulations by criteria: {}", criteria);
        Page<SimulationDTO> page = simulationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /simulations/count} : count all the simulations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/simulations/count")
    public ResponseEntity<Long> countSimulations(SimulationCriteria criteria) {
        log.debug("REST request to count Simulations by criteria: {}", criteria);
        return ResponseEntity.ok().body(simulationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /simulations/:id} : get the "id" simulation.
     *
     * @param id the id of the simulationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the simulationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/simulations/{id}")
    public ResponseEntity<SimulationDTO> getSimulation(@PathVariable Long id) {
        log.debug("REST request to get Simulation : {}", id);
        Optional<SimulationDTO> simulationDTO = simulationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(simulationDTO);
    }

    /**
     * {@code DELETE  /simulations/:id} : delete the "id" simulation.
     *
     * @param id the id of the simulationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/simulations/{id}")
    public ResponseEntity<Void> deleteSimulation(@PathVariable Long id) {
        log.debug("REST request to delete Simulation : {}", id);
        simulationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
