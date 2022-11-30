package com.attest.ict.web.rest;

import com.attest.ict.repository.FlexProfileRepository;
import com.attest.ict.service.FlexProfileQueryService;
import com.attest.ict.service.FlexProfileService;
import com.attest.ict.service.criteria.FlexProfileCriteria;
import com.attest.ict.service.dto.FlexProfileDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.FlexProfile}.
 */
@RestController
@RequestMapping("/api")
public class FlexProfileResource {

    private final Logger log = LoggerFactory.getLogger(FlexProfileResource.class);

    private static final String ENTITY_NAME = "flexProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FlexProfileService flexProfileService;

    private final FlexProfileRepository flexProfileRepository;

    private final FlexProfileQueryService flexProfileQueryService;

    public FlexProfileResource(
        FlexProfileService flexProfileService,
        FlexProfileRepository flexProfileRepository,
        FlexProfileQueryService flexProfileQueryService
    ) {
        this.flexProfileService = flexProfileService;
        this.flexProfileRepository = flexProfileRepository;
        this.flexProfileQueryService = flexProfileQueryService;
    }

    /**
     * {@code POST  /flex-profiles} : Create a new flexProfile.
     *
     * @param flexProfileDTO the flexProfileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new flexProfileDTO, or with status {@code 400 (Bad Request)} if the flexProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/flex-profiles")
    public ResponseEntity<FlexProfileDTO> createFlexProfile(@RequestBody FlexProfileDTO flexProfileDTO) throws URISyntaxException {
        log.debug("REST request to save FlexProfile : {}", flexProfileDTO);
        if (flexProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new flexProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FlexProfileDTO result = flexProfileService.save(flexProfileDTO);
        return ResponseEntity
            .created(new URI("/api/flex-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /flex-profiles/:id} : Updates an existing flexProfile.
     *
     * @param id the id of the flexProfileDTO to save.
     * @param flexProfileDTO the flexProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flexProfileDTO,
     * or with status {@code 400 (Bad Request)} if the flexProfileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the flexProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/flex-profiles/{id}")
    public ResponseEntity<FlexProfileDTO> updateFlexProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FlexProfileDTO flexProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FlexProfile : {}, {}", id, flexProfileDTO);
        if (flexProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flexProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flexProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FlexProfileDTO result = flexProfileService.save(flexProfileDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, flexProfileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /flex-profiles/:id} : Partial updates given fields of an existing flexProfile, field will ignore if it is null
     *
     * @param id the id of the flexProfileDTO to save.
     * @param flexProfileDTO the flexProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flexProfileDTO,
     * or with status {@code 400 (Bad Request)} if the flexProfileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the flexProfileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the flexProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/flex-profiles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FlexProfileDTO> partialUpdateFlexProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FlexProfileDTO flexProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FlexProfile partially : {}, {}", id, flexProfileDTO);
        if (flexProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flexProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flexProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FlexProfileDTO> result = flexProfileService.partialUpdate(flexProfileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, flexProfileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /flex-profiles} : get all the flexProfiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of flexProfiles in body.
     */
    @GetMapping("/flex-profiles")
    public ResponseEntity<List<FlexProfileDTO>> getAllFlexProfiles(FlexProfileCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FlexProfiles by criteria: {}", criteria);
        Page<FlexProfileDTO> page = flexProfileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /flex-profiles/count} : count all the flexProfiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/flex-profiles/count")
    public ResponseEntity<Long> countFlexProfiles(FlexProfileCriteria criteria) {
        log.debug("REST request to count FlexProfiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(flexProfileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /flex-profiles/:id} : get the "id" flexProfile.
     *
     * @param id the id of the flexProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the flexProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/flex-profiles/{id}")
    public ResponseEntity<FlexProfileDTO> getFlexProfile(@PathVariable Long id) {
        log.debug("REST request to get FlexProfile : {}", id);
        Optional<FlexProfileDTO> flexProfileDTO = flexProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(flexProfileDTO);
    }

    /**
     * {@code DELETE  /flex-profiles/:id} : delete the "id" flexProfile.
     *
     * @param id the id of the flexProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/flex-profiles/{id}")
    public ResponseEntity<Void> deleteFlexProfile(@PathVariable Long id) {
        log.debug("REST request to delete FlexProfile : {}", id);
        flexProfileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
