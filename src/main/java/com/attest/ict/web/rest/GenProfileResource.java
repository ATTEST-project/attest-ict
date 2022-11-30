package com.attest.ict.web.rest;

import com.attest.ict.repository.GenProfileRepository;
import com.attest.ict.service.GenProfileQueryService;
import com.attest.ict.service.GenProfileService;
import com.attest.ict.service.criteria.GenProfileCriteria;
import com.attest.ict.service.dto.GenProfileDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.GenProfile}.
 */
@RestController
@RequestMapping("/api")
public class GenProfileResource {

    private final Logger log = LoggerFactory.getLogger(GenProfileResource.class);

    private static final String ENTITY_NAME = "genProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GenProfileService genProfileService;

    private final GenProfileRepository genProfileRepository;

    private final GenProfileQueryService genProfileQueryService;

    public GenProfileResource(
        GenProfileService genProfileService,
        GenProfileRepository genProfileRepository,
        GenProfileQueryService genProfileQueryService
    ) {
        this.genProfileService = genProfileService;
        this.genProfileRepository = genProfileRepository;
        this.genProfileQueryService = genProfileQueryService;
    }

    /**
     * {@code POST  /gen-profiles} : Create a new genProfile.
     *
     * @param genProfileDTO the genProfileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new genProfileDTO, or with status {@code 400 (Bad Request)} if the genProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gen-profiles")
    public ResponseEntity<GenProfileDTO> createGenProfile(@RequestBody GenProfileDTO genProfileDTO) throws URISyntaxException {
        log.debug("REST request to save GenProfile : {}", genProfileDTO);
        if (genProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new genProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GenProfileDTO result = genProfileService.save(genProfileDTO);
        return ResponseEntity
            .created(new URI("/api/gen-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gen-profiles/:id} : Updates an existing genProfile.
     *
     * @param id the id of the genProfileDTO to save.
     * @param genProfileDTO the genProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated genProfileDTO,
     * or with status {@code 400 (Bad Request)} if the genProfileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the genProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gen-profiles/{id}")
    public ResponseEntity<GenProfileDTO> updateGenProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GenProfileDTO genProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GenProfile : {}, {}", id, genProfileDTO);
        if (genProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, genProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!genProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GenProfileDTO result = genProfileService.save(genProfileDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, genProfileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gen-profiles/:id} : Partial updates given fields of an existing genProfile, field will ignore if it is null
     *
     * @param id the id of the genProfileDTO to save.
     * @param genProfileDTO the genProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated genProfileDTO,
     * or with status {@code 400 (Bad Request)} if the genProfileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the genProfileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the genProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gen-profiles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GenProfileDTO> partialUpdateGenProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GenProfileDTO genProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GenProfile partially : {}, {}", id, genProfileDTO);
        if (genProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, genProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!genProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GenProfileDTO> result = genProfileService.partialUpdate(genProfileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, genProfileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /gen-profiles} : get all the genProfiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of genProfiles in body.
     */
    @GetMapping("/gen-profiles")
    public ResponseEntity<List<GenProfileDTO>> getAllGenProfiles(GenProfileCriteria criteria, Pageable pageable) {
        log.debug("REST request to get GenProfiles by criteria: {}", criteria);
        Page<GenProfileDTO> page = genProfileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gen-profiles/count} : count all the genProfiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/gen-profiles/count")
    public ResponseEntity<Long> countGenProfiles(GenProfileCriteria criteria) {
        log.debug("REST request to count GenProfiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(genProfileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /gen-profiles/:id} : get the "id" genProfile.
     *
     * @param id the id of the genProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the genProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gen-profiles/{id}")
    public ResponseEntity<GenProfileDTO> getGenProfile(@PathVariable Long id) {
        log.debug("REST request to get GenProfile : {}", id);
        Optional<GenProfileDTO> genProfileDTO = genProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(genProfileDTO);
    }

    /**
     * {@code DELETE  /gen-profiles/:id} : delete the "id" genProfile.
     *
     * @param id the id of the genProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gen-profiles/{id}")
    public ResponseEntity<Void> deleteGenProfile(@PathVariable Long id) {
        log.debug("REST request to delete GenProfile : {}", id);
        genProfileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
