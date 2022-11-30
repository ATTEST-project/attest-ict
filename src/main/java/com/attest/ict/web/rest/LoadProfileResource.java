package com.attest.ict.web.rest;

import com.attest.ict.repository.LoadProfileRepository;
import com.attest.ict.service.LoadProfileQueryService;
import com.attest.ict.service.LoadProfileService;
import com.attest.ict.service.criteria.LoadProfileCriteria;
import com.attest.ict.service.dto.LoadProfileDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.LoadProfile}.
 */
@RestController
@RequestMapping("/api")
public class LoadProfileResource {

    private final Logger log = LoggerFactory.getLogger(LoadProfileResource.class);

    private static final String ENTITY_NAME = "loadProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoadProfileService loadProfileService;

    private final LoadProfileRepository loadProfileRepository;

    private final LoadProfileQueryService loadProfileQueryService;

    public LoadProfileResource(
        LoadProfileService loadProfileService,
        LoadProfileRepository loadProfileRepository,
        LoadProfileQueryService loadProfileQueryService
    ) {
        this.loadProfileService = loadProfileService;
        this.loadProfileRepository = loadProfileRepository;
        this.loadProfileQueryService = loadProfileQueryService;
    }

    /**
     * {@code POST  /load-profiles} : Create a new loadProfile.
     *
     * @param loadProfileDTO the loadProfileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loadProfileDTO, or with status {@code 400 (Bad Request)} if the loadProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/load-profiles")
    public ResponseEntity<LoadProfileDTO> createLoadProfile(@RequestBody LoadProfileDTO loadProfileDTO) throws URISyntaxException {
        log.debug("REST request to save LoadProfile : {}", loadProfileDTO);
        if (loadProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new loadProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoadProfileDTO result = loadProfileService.save(loadProfileDTO);
        return ResponseEntity
            .created(new URI("/api/load-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /load-profiles/:id} : Updates an existing loadProfile.
     *
     * @param id the id of the loadProfileDTO to save.
     * @param loadProfileDTO the loadProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loadProfileDTO,
     * or with status {@code 400 (Bad Request)} if the loadProfileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loadProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/load-profiles/{id}")
    public ResponseEntity<LoadProfileDTO> updateLoadProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LoadProfileDTO loadProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LoadProfile : {}, {}", id, loadProfileDTO);
        if (loadProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loadProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loadProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LoadProfileDTO result = loadProfileService.save(loadProfileDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, loadProfileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /load-profiles/:id} : Partial updates given fields of an existing loadProfile, field will ignore if it is null
     *
     * @param id the id of the loadProfileDTO to save.
     * @param loadProfileDTO the loadProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loadProfileDTO,
     * or with status {@code 400 (Bad Request)} if the loadProfileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loadProfileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loadProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/load-profiles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LoadProfileDTO> partialUpdateLoadProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LoadProfileDTO loadProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LoadProfile partially : {}, {}", id, loadProfileDTO);
        if (loadProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loadProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loadProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoadProfileDTO> result = loadProfileService.partialUpdate(loadProfileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, loadProfileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /load-profiles} : get all the loadProfiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loadProfiles in body.
     */
    @GetMapping("/load-profiles")
    public ResponseEntity<List<LoadProfileDTO>> getAllLoadProfiles(LoadProfileCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LoadProfiles by criteria: {}", criteria);
        Page<LoadProfileDTO> page = loadProfileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /load-profiles/count} : count all the loadProfiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/load-profiles/count")
    public ResponseEntity<Long> countLoadProfiles(LoadProfileCriteria criteria) {
        log.debug("REST request to count LoadProfiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(loadProfileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /load-profiles/:id} : get the "id" loadProfile.
     *
     * @param id the id of the loadProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loadProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/load-profiles/{id}")
    public ResponseEntity<LoadProfileDTO> getLoadProfile(@PathVariable Long id) {
        log.debug("REST request to get LoadProfile : {}", id);
        Optional<LoadProfileDTO> loadProfileDTO = loadProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loadProfileDTO);
    }

    /**
     * {@code DELETE  /load-profiles/:id} : delete the "id" loadProfile.
     *
     * @param id the id of the loadProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/load-profiles/{id}")
    public ResponseEntity<Void> deleteLoadProfile(@PathVariable Long id) {
        log.debug("REST request to delete LoadProfile : {}", id);
        loadProfileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
