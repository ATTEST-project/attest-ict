package com.attest.ict.web.rest;

import com.attest.ict.repository.NodeRepository;
import com.attest.ict.service.NodeQueryService;
import com.attest.ict.service.NodeService;
import com.attest.ict.service.criteria.NodeCriteria;
import com.attest.ict.service.dto.NodeDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.Node}.
 */
@RestController
@RequestMapping("/api")
public class NodeResource {

    private final Logger log = LoggerFactory.getLogger(NodeResource.class);

    private static final String ENTITY_NAME = "node";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NodeService nodeService;

    private final NodeRepository nodeRepository;

    private final NodeQueryService nodeQueryService;

    public NodeResource(NodeService nodeService, NodeRepository nodeRepository, NodeQueryService nodeQueryService) {
        this.nodeService = nodeService;
        this.nodeRepository = nodeRepository;
        this.nodeQueryService = nodeQueryService;
    }

    /**
     * {@code POST  /nodes} : Create a new node.
     *
     * @param nodeDTO the nodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nodeDTO, or with status {@code 400 (Bad Request)} if the node has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nodes")
    public ResponseEntity<NodeDTO> createNode(@RequestBody NodeDTO nodeDTO) throws URISyntaxException {
        log.debug("REST request to save Node : {}", nodeDTO);
        if (nodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new node cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NodeDTO result = nodeService.save(nodeDTO);
        return ResponseEntity
            .created(new URI("/api/nodes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nodes/:id} : Updates an existing node.
     *
     * @param id the id of the nodeDTO to save.
     * @param nodeDTO the nodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nodeDTO,
     * or with status {@code 400 (Bad Request)} if the nodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nodes/{id}")
    public ResponseEntity<NodeDTO> updateNode(@PathVariable(value = "id", required = false) final Long id, @RequestBody NodeDTO nodeDTO)
        throws URISyntaxException {
        log.debug("REST request to update Node : {}, {}", id, nodeDTO);
        if (nodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NodeDTO result = nodeService.save(nodeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, nodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /nodes/:id} : Partial updates given fields of an existing node, field will ignore if it is null
     *
     * @param id the id of the nodeDTO to save.
     * @param nodeDTO the nodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nodeDTO,
     * or with status {@code 400 (Bad Request)} if the nodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/nodes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NodeDTO> partialUpdateNode(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NodeDTO nodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Node partially : {}, {}", id, nodeDTO);
        if (nodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NodeDTO> result = nodeService.partialUpdate(nodeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, nodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /nodes} : get all the nodes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nodes in body.
     */
    @GetMapping("/nodes")
    public ResponseEntity<List<NodeDTO>> getAllNodes(NodeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Nodes by criteria: {}", criteria);
        Page<NodeDTO> page = nodeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /nodes/count} : count all the nodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/nodes/count")
    public ResponseEntity<Long> countNodes(NodeCriteria criteria) {
        log.debug("REST request to count Nodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(nodeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /nodes/:id} : get the "id" node.
     *
     * @param id the id of the nodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nodes/{id}")
    public ResponseEntity<NodeDTO> getNode(@PathVariable Long id) {
        log.debug("REST request to get Node : {}", id);
        Optional<NodeDTO> nodeDTO = nodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nodeDTO);
    }

    /**
     * {@code DELETE  /nodes/:id} : delete the "id" node.
     *
     * @param id the id of the nodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nodes/{id}")
    public ResponseEntity<Void> deleteNode(@PathVariable Long id) {
        log.debug("REST request to delete Node : {}", id);
        nodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
