package com.attest.ict.web.rest;

import com.attest.ict.domain.BranchExtension;
import com.attest.ict.repository.BranchExtensionRepository;
import com.attest.ict.service.BranchExtensionQueryService;
import com.attest.ict.service.BranchExtensionService;
import com.attest.ict.service.NetworkService;
import com.attest.ict.service.criteria.BranchExtensionCriteria;
import com.attest.ict.service.dto.BranchExtensionDTO;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.dto.custom.BranchCustomDTO;
import com.attest.ict.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.attest.ict.domain.BranchExtension}.
 */
@RestController
@RequestMapping("/api")
public class BranchExtensionResource {

    private final Logger log = LoggerFactory.getLogger(BranchExtensionResource.class);

    private static final String ENTITY_NAME = "branchExtension";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BranchExtensionService branchExtensionService;

    private final BranchExtensionRepository branchExtensionRepository;

    private final BranchExtensionQueryService branchExtensionQueryService;

    @Autowired
    NetworkService networkService;

    public BranchExtensionResource(
        BranchExtensionService branchExtensionService,
        BranchExtensionRepository branchExtensionRepository,
        BranchExtensionQueryService branchExtensionQueryService
    ) {
        this.branchExtensionService = branchExtensionService;
        this.branchExtensionRepository = branchExtensionRepository;
        this.branchExtensionQueryService = branchExtensionQueryService;
    }

    /**
     * {@code POST  /branch-extensions} : Create a new branchExtension.
     *
     * @param branchExtensionDTO the branchExtensionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new branchExtensionDTO, or with status {@code 400 (Bad Request)} if the branchExtension has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/branch-extensions")
    public ResponseEntity<BranchExtensionDTO> createBranchExtension(@RequestBody BranchExtensionDTO branchExtensionDTO)
        throws URISyntaxException {
        log.debug("REST request to save BranchExtension : {}", branchExtensionDTO);
        if (branchExtensionDTO.getId() != null) {
            throw new BadRequestAlertException("A new branchExtension cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BranchExtensionDTO result = branchExtensionService.save(branchExtensionDTO);
        return ResponseEntity
            .created(new URI("/api/branch-extensions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /branch-extensions/:id} : Updates an existing branchExtension.
     *
     * @param id the id of the branchExtensionDTO to save.
     * @param branchExtensionDTO the branchExtensionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated branchExtensionDTO,
     * or with status {@code 400 (Bad Request)} if the branchExtensionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the branchExtensionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/branch-extensions/{id}")
    public ResponseEntity<BranchExtensionDTO> updateBranchExtension(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BranchExtensionDTO branchExtensionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BranchExtension : {}, {}", id, branchExtensionDTO);
        if (branchExtensionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, branchExtensionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!branchExtensionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BranchExtensionDTO result = branchExtensionService.save(branchExtensionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, branchExtensionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /branch-extensions/:id} : Partial updates given fields of an existing branchExtension, field will ignore if it is null
     *
     * @param id the id of the branchExtensionDTO to save.
     * @param branchExtensionDTO the branchExtensionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated branchExtensionDTO,
     * or with status {@code 400 (Bad Request)} if the branchExtensionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the branchExtensionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the branchExtensionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/branch-extensions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BranchExtensionDTO> partialUpdateBranchExtension(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BranchExtensionDTO branchExtensionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BranchExtension partially : {}, {}", id, branchExtensionDTO);
        if (branchExtensionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, branchExtensionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!branchExtensionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BranchExtensionDTO> result = branchExtensionService.partialUpdate(branchExtensionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, branchExtensionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /branch-extensions} : get all the branchExtensions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of branchExtensions in body.
     */
    @GetMapping("/branch-extensions")
    public ResponseEntity<List<BranchExtensionDTO>> getAllBranchExtensions(BranchExtensionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BranchExtensions by criteria: {}", criteria);
        Page<BranchExtensionDTO> page = branchExtensionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /branch-extensions/count} : count all the branchExtensions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/branch-extensions/count")
    public ResponseEntity<Long> countBranchExtensions(BranchExtensionCriteria criteria) {
        log.debug("REST request to count BranchExtensions by criteria: {}", criteria);
        return ResponseEntity.ok().body(branchExtensionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /branch-extensions/:id} : get the "id" branchExtension.
     *
     * @param id the id of the branchExtensionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the branchExtensionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/branch-extensions/{id}")
    public ResponseEntity<BranchExtensionDTO> getBranchExtension(@PathVariable Long id) {
        log.debug("REST request to get BranchExtension : {}", id);
        Optional<BranchExtensionDTO> branchExtensionDTO = branchExtensionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(branchExtensionDTO);
    }

    /**
     * {@code DELETE  /branch-extensions/:id} : delete the "id" branchExtension.
     *
     * @param id the id of the branchExtensionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/branch-extensions/{id}")
    public ResponseEntity<Void> deleteBranchExtension(@PathVariable Long id) {
        log.debug("REST request to delete BranchExtension : {}", id);
        branchExtensionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    // Start Custom Method

    /**
     * {@code GET  /branch-extensions/network} : get the all network's branches with length greater than param: length (if specified), all network's branches if length param is not present
     * @param networkId the id of the network  to retrieve.
     * @param length minLength
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the BranchCustomDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/branch-extensions/network")
    public ResponseEntity<?> getAllBranchExtensionByNetworkIdAndMinLength(
        @RequestParam(value = "networkId", required = true) Long networkId,
        @RequestParam(name = "length", required = false) Double minLength
    ) {
        log.debug("REST Request to obtain branch's length. Params NetworkId: {} minLength: {} ", networkId, minLength);
        try {
            List<BranchCustomDTO> branchExtensionList = new ArrayList<BranchCustomDTO>();

            // -- check if netwrok exists
            Optional<NetworkDTO> networkDtoOpt = networkService.findOne(networkId);
            if (!networkDtoOpt.isPresent()) return new ResponseEntity<>(
                "Network with id: " + networkId + " not found!",
                HttpStatus.NOT_FOUND
            );

            if (minLength == null) {
                branchExtensionList = branchExtensionService.findLengthByNetworkId(networkId);
            } else {
                branchExtensionList = branchExtensionService.findLengthByNetworkIdAndLengthGreatherThen(networkId, minLength);
            }

            return new ResponseEntity<>(branchExtensionList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error quering Branch_Extension table ");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
