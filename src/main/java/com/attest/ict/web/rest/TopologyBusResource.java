package com.attest.ict.web.rest;

import com.attest.ict.helper.TopologyHelper;
import com.attest.ict.repository.TopologyBusRepository;
import com.attest.ict.service.TopologyBusQueryService;
import com.attest.ict.service.TopologyBusService;
import com.attest.ict.service.TopologyService;
import com.attest.ict.service.criteria.TopologyBusCriteria;
import com.attest.ict.service.dto.TopologyBusDTO;
import com.attest.ict.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.attest.ict.domain.TopologyBus}.
 */
@RestController
@RequestMapping("/api")
public class TopologyBusResource {

    private final Logger log = LoggerFactory.getLogger(TopologyBusResource.class);

    private static final String ENTITY_NAME = "topologyBus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TopologyBusService topologyBusService;

    private final TopologyBusRepository topologyBusRepository;

    private final TopologyBusQueryService topologyBusQueryService;

    private final TopologyService topologyService;

    public TopologyBusResource(
        TopologyBusService topologyBusService,
        TopologyBusRepository topologyBusRepository,
        TopologyBusQueryService topologyBusQueryService,
        TopologyService topologyService
    ) {
        this.topologyBusService = topologyBusService;
        this.topologyBusRepository = topologyBusRepository;
        this.topologyBusQueryService = topologyBusQueryService;
        this.topologyService = topologyService;
    }

    /**
     * {@code POST  /topology-buses} : Create a new topologyBus.
     *
     * @param topologyBusDTO the topologyBusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new topologyBusDTO, or with status {@code 400 (Bad Request)} if the topologyBus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/topology-buses")
    public ResponseEntity<TopologyBusDTO> createTopologyBus(@RequestBody TopologyBusDTO topologyBusDTO) throws URISyntaxException {
        log.debug("REST request to save TopologyBus : {}", topologyBusDTO);
        if (topologyBusDTO.getId() != null) {
            throw new BadRequestAlertException("A new topologyBus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TopologyBusDTO result = topologyBusService.save(topologyBusDTO);
        return ResponseEntity
            .created(new URI("/api/topology-buses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /topology-buses/:id} : Updates an existing topologyBus.
     *
     * @param id the id of the topologyBusDTO to save.
     * @param topologyBusDTO the topologyBusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated topologyBusDTO,
     * or with status {@code 400 (Bad Request)} if the topologyBusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the topologyBusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/topology-buses/{id}")
    public ResponseEntity<TopologyBusDTO> updateTopologyBus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TopologyBusDTO topologyBusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TopologyBus : {}, {}", id, topologyBusDTO);
        if (topologyBusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, topologyBusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!topologyBusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TopologyBusDTO result = topologyBusService.save(topologyBusDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, topologyBusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /topology-buses/:id} : Partial updates given fields of an existing topologyBus, field will ignore if it is null
     *
     * @param id the id of the topologyBusDTO to save.
     * @param topologyBusDTO the topologyBusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated topologyBusDTO,
     * or with status {@code 400 (Bad Request)} if the topologyBusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the topologyBusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the topologyBusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/topology-buses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TopologyBusDTO> partialUpdateTopologyBus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TopologyBusDTO topologyBusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TopologyBus partially : {}, {}", id, topologyBusDTO);
        if (topologyBusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, topologyBusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!topologyBusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TopologyBusDTO> result = topologyBusService.partialUpdate(topologyBusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, topologyBusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /topology-buses} : get all the topologyBuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of topologyBuses in body.
     */
    @GetMapping("/topology-buses")
    public ResponseEntity<List<TopologyBusDTO>> getAllTopologyBuses(TopologyBusCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TopologyBuses by criteria: {}", criteria);
        Page<TopologyBusDTO> page = topologyBusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /topology-buses/count} : count all the topologyBuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/topology-buses/count")
    public ResponseEntity<Long> countTopologyBuses(TopologyBusCriteria criteria) {
        log.debug("REST request to count TopologyBuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(topologyBusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /topology-buses/:id} : get the "id" topologyBus.
     *
     * @param id the id of the topologyBusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the topologyBusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/topology-buses/{id}")
    public ResponseEntity<TopologyBusDTO> getTopologyBus(@PathVariable Long id) {
        log.debug("REST request to get TopologyBus : {}", id);
        Optional<TopologyBusDTO> topologyBusDTO = topologyBusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(topologyBusDTO);
    }

    /**
     * {@code DELETE  /topology-buses/:id} : delete the "id" topologyBus.
     *
     * @param id the id of the topologyBusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/topology-buses/{id}")
    public ResponseEntity<Void> deleteTopologyBus(@PathVariable Long id) {
        log.debug("REST request to delete TopologyBus : {}", id);
        topologyBusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    //====== Start Custom Methods

    @GetMapping("/getAllPoints")
    public ResponseEntity<?> getAllPoints() {
        try {
            List<String> plbNames = topologyBusService.getPLBNames();

            if (plbNames.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            Map<String, List<Double[]>> points = new LinkedHashMap<>();
            for (String name : plbNames) {
                List<String> coords = topologyService.getPointsOfPLB(name);
                if (!coords.isEmpty()) points.put(name, TopologyHelper.stringToDoubleCoords(coords));
            }

            return new ResponseEntity<>(points, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Exception: " + e.getMessage());
            return new ResponseEntity<>("Exception: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getPoints/{networkId}")
    public ResponseEntity<?> getPointsByNetworkId(@PathVariable("networkId") String networkId) {
        try {
            List<String> plbNames = topologyBusService.getPLBNamesByNetworkId(networkId);

            if (plbNames.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Map<String, List<Double[]>> points = new LinkedHashMap<>();
            for (String name : plbNames) {
                List<String> coords = topologyService.getPointsOfPLB(name);
                if (!coords.isEmpty()) points.put(name, TopologyHelper.stringToDoubleCoords(coords));
            }

            return new ResponseEntity<>(points, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Exception: " + e.getMessage());
            return new ResponseEntity<>("Exception: " + e.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
