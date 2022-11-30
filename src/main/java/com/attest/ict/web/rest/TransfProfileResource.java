package com.attest.ict.web.rest;

import com.attest.ict.repository.TransfProfileRepository;
import com.attest.ict.service.TransfProfileQueryService;
import com.attest.ict.service.TransfProfileService;
import com.attest.ict.service.criteria.TransfProfileCriteria;
import com.attest.ict.service.dto.TransfProfileDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.TransfProfile}.
 */
@RestController
@RequestMapping("/api")
public class TransfProfileResource {

    private final Logger log = LoggerFactory.getLogger(TransfProfileResource.class);

    private static final String ENTITY_NAME = "transfProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransfProfileService transfProfileService;

    private final TransfProfileRepository transfProfileRepository;

    private final TransfProfileQueryService transfProfileQueryService;

    public TransfProfileResource(
        TransfProfileService transfProfileService,
        TransfProfileRepository transfProfileRepository,
        TransfProfileQueryService transfProfileQueryService
    ) {
        this.transfProfileService = transfProfileService;
        this.transfProfileRepository = transfProfileRepository;
        this.transfProfileQueryService = transfProfileQueryService;
    }

    /**
     * {@code POST  /transf-profiles} : Create a new transfProfile.
     *
     * @param transfProfileDTO the transfProfileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transfProfileDTO, or with status {@code 400 (Bad Request)} if the transfProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transf-profiles")
    public ResponseEntity<TransfProfileDTO> createTransfProfile(@RequestBody TransfProfileDTO transfProfileDTO) throws URISyntaxException {
        log.debug("REST request to save TransfProfile : {}", transfProfileDTO);
        if (transfProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new transfProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransfProfileDTO result = transfProfileService.save(transfProfileDTO);
        return ResponseEntity
            .created(new URI("/api/transf-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transf-profiles/:id} : Updates an existing transfProfile.
     *
     * @param id the id of the transfProfileDTO to save.
     * @param transfProfileDTO the transfProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transfProfileDTO,
     * or with status {@code 400 (Bad Request)} if the transfProfileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transfProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transf-profiles/{id}")
    public ResponseEntity<TransfProfileDTO> updateTransfProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TransfProfileDTO transfProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TransfProfile : {}, {}", id, transfProfileDTO);
        if (transfProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transfProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transfProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransfProfileDTO result = transfProfileService.save(transfProfileDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transfProfileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transf-profiles/:id} : Partial updates given fields of an existing transfProfile, field will ignore if it is null
     *
     * @param id the id of the transfProfileDTO to save.
     * @param transfProfileDTO the transfProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transfProfileDTO,
     * or with status {@code 400 (Bad Request)} if the transfProfileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transfProfileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transfProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transf-profiles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransfProfileDTO> partialUpdateTransfProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TransfProfileDTO transfProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TransfProfile partially : {}, {}", id, transfProfileDTO);
        if (transfProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transfProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transfProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransfProfileDTO> result = transfProfileService.partialUpdate(transfProfileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transfProfileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transf-profiles} : get all the transfProfiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transfProfiles in body.
     */
    @GetMapping("/transf-profiles")
    public ResponseEntity<List<TransfProfileDTO>> getAllTransfProfiles(TransfProfileCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TransfProfiles by criteria: {}", criteria);
        Page<TransfProfileDTO> page = transfProfileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transf-profiles/count} : count all the transfProfiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/transf-profiles/count")
    public ResponseEntity<Long> countTransfProfiles(TransfProfileCriteria criteria) {
        log.debug("REST request to count TransfProfiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(transfProfileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transf-profiles/:id} : get the "id" transfProfile.
     *
     * @param id the id of the transfProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transfProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transf-profiles/{id}")
    public ResponseEntity<TransfProfileDTO> getTransfProfile(@PathVariable Long id) {
        log.debug("REST request to get TransfProfile : {}", id);
        Optional<TransfProfileDTO> transfProfileDTO = transfProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transfProfileDTO);
    }

    /**
     * {@code DELETE  /transf-profiles/:id} : delete the "id" transfProfile.
     *
     * @param id the id of the transfProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transf-profiles/{id}")
    public ResponseEntity<Void> deleteTransfProfile(@PathVariable Long id) {
        log.debug("REST request to delete TransfProfile : {}", id);
        transfProfileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
