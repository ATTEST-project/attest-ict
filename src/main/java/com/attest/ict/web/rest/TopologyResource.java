package com.attest.ict.web.rest;

import com.attest.ict.helper.TopologyHelper;
import com.attest.ict.repository.TopologyRepository;
import com.attest.ict.service.TopologyQueryService;
import com.attest.ict.service.TopologyService;
import com.attest.ict.service.criteria.TopologyCriteria;
import com.attest.ict.service.dto.TopologyDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.Topology}.
 */
@RestController
@RequestMapping("/api")
public class TopologyResource {

    private final Logger log = LoggerFactory.getLogger(TopologyResource.class);

    private static final String ENTITY_NAME = "topology";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TopologyService topologyService;

    private final TopologyRepository topologyRepository;

    private final TopologyQueryService topologyQueryService;

    public TopologyResource(
        TopologyService topologyService,
        TopologyRepository topologyRepository,
        TopologyQueryService topologyQueryService
    ) {
        this.topologyService = topologyService;
        this.topologyRepository = topologyRepository;
        this.topologyQueryService = topologyQueryService;
    }

    /**
     * {@code POST  /topologies} : Create a new topology.
     *
     * @param topologyDTO the topologyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new topologyDTO, or with status {@code 400 (Bad Request)} if the topology has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/topologies")
    public ResponseEntity<TopologyDTO> createTopology(@RequestBody TopologyDTO topologyDTO) throws URISyntaxException {
        log.debug("REST request to save Topology : {}", topologyDTO);
        if (topologyDTO.getId() != null) {
            throw new BadRequestAlertException("A new topology cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TopologyDTO result = topologyService.save(topologyDTO);
        return ResponseEntity
            .created(new URI("/api/topologies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /topologies/:id} : Updates an existing topology.
     *
     * @param id the id of the topologyDTO to save.
     * @param topologyDTO the topologyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated topologyDTO,
     * or with status {@code 400 (Bad Request)} if the topologyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the topologyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/topologies/{id}")
    public ResponseEntity<TopologyDTO> updateTopology(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TopologyDTO topologyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Topology : {}, {}", id, topologyDTO);
        if (topologyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, topologyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!topologyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TopologyDTO result = topologyService.save(topologyDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, topologyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /topologies/:id} : Partial updates given fields of an existing topology, field will ignore if it is null
     *
     * @param id the id of the topologyDTO to save.
     * @param topologyDTO the topologyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated topologyDTO,
     * or with status {@code 400 (Bad Request)} if the topologyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the topologyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the topologyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/topologies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TopologyDTO> partialUpdateTopology(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TopologyDTO topologyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Topology partially : {}, {}", id, topologyDTO);
        if (topologyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, topologyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!topologyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TopologyDTO> result = topologyService.partialUpdate(topologyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, topologyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /topologies} : get all the topologies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of topologies in body.
     */
    @GetMapping("/topologies")
    public ResponseEntity<List<TopologyDTO>> getAllTopologies(TopologyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Topologies by criteria: {}", criteria);
        Page<TopologyDTO> page = topologyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /topologies/count} : count all the topologies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/topologies/count")
    public ResponseEntity<Long> countTopologies(TopologyCriteria criteria) {
        log.debug("REST request to count Topologies by criteria: {}", criteria);
        return ResponseEntity.ok().body(topologyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /topologies/:id} : get the "id" topology.
     *
     * @param id the id of the topologyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the topologyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/topologies/{id}")
    public ResponseEntity<TopologyDTO> getTopology(@PathVariable Long id) {
        log.debug("REST request to get Topology : {}", id);
        Optional<TopologyDTO> topologyDTO = topologyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(topologyDTO);
    }

    /**
     * {@code DELETE  /topologies/:id} : delete the "id" topology.
     *
     * @param id the id of the topologyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/topologies/{id}")
    public ResponseEntity<Void> deleteTopology(@PathVariable Long id) {
        log.debug("REST request to delete Topology : {}", id);
        topologyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    //===== Start Custom Methods
    @GetMapping("/getPLBPoints/{plb}")
    public ResponseEntity<List<Double[]>> getPLBPoints(@PathVariable("plb") String plb) {
        try {
            List<String> points = topologyService.getPointsOfPLB(plb);

            if (points.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            List<Double[]> doubles_coords = TopologyHelper.stringToDoubleCoords(points);

            return new ResponseEntity<>(doubles_coords, HttpStatus.OK);
        } catch (Exception e) {
            log.debug(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
