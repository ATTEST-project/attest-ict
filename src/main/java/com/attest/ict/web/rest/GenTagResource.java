package com.attest.ict.web.rest;

import com.attest.ict.repository.GenTagRepository;
import com.attest.ict.service.GenTagQueryService;
import com.attest.ict.service.GenTagService;
import com.attest.ict.service.criteria.GenTagCriteria;
import com.attest.ict.service.dto.GenTagDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.GenTag}.
 */
@RestController
@RequestMapping("/api")
public class GenTagResource {

    private final Logger log = LoggerFactory.getLogger(GenTagResource.class);

    private static final String ENTITY_NAME = "genTag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GenTagService genTagService;

    private final GenTagRepository genTagRepository;

    private final GenTagQueryService genTagQueryService;

    public GenTagResource(GenTagService genTagService, GenTagRepository genTagRepository, GenTagQueryService genTagQueryService) {
        this.genTagService = genTagService;
        this.genTagRepository = genTagRepository;
        this.genTagQueryService = genTagQueryService;
    }

    /**
     * {@code POST  /gen-tags} : Create a new genTag.
     *
     * @param genTagDTO the genTagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new genTagDTO, or with status {@code 400 (Bad Request)} if the genTag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gen-tags")
    public ResponseEntity<GenTagDTO> createGenTag(@RequestBody GenTagDTO genTagDTO) throws URISyntaxException {
        log.debug("REST request to save GenTag : {}", genTagDTO);
        if (genTagDTO.getId() != null) {
            throw new BadRequestAlertException("A new genTag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GenTagDTO result = genTagService.save(genTagDTO);
        return ResponseEntity
            .created(new URI("/api/gen-tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gen-tags/:id} : Updates an existing genTag.
     *
     * @param id the id of the genTagDTO to save.
     * @param genTagDTO the genTagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated genTagDTO,
     * or with status {@code 400 (Bad Request)} if the genTagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the genTagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gen-tags/{id}")
    public ResponseEntity<GenTagDTO> updateGenTag(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GenTagDTO genTagDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GenTag : {}, {}", id, genTagDTO);
        if (genTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, genTagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!genTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GenTagDTO result = genTagService.save(genTagDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, genTagDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gen-tags/:id} : Partial updates given fields of an existing genTag, field will ignore if it is null
     *
     * @param id the id of the genTagDTO to save.
     * @param genTagDTO the genTagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated genTagDTO,
     * or with status {@code 400 (Bad Request)} if the genTagDTO is not valid,
     * or with status {@code 404 (Not Found)} if the genTagDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the genTagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gen-tags/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GenTagDTO> partialUpdateGenTag(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GenTagDTO genTagDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GenTag partially : {}, {}", id, genTagDTO);
        if (genTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, genTagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!genTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GenTagDTO> result = genTagService.partialUpdate(genTagDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, genTagDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /gen-tags} : get all the genTags.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of genTags in body.
     */
    @GetMapping("/gen-tags")
    public ResponseEntity<List<GenTagDTO>> getAllGenTags(GenTagCriteria criteria, Pageable pageable) {
        log.debug("REST request to get GenTags by criteria: {}", criteria);
        Page<GenTagDTO> page = genTagQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gen-tags/count} : count all the genTags.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/gen-tags/count")
    public ResponseEntity<Long> countGenTags(GenTagCriteria criteria) {
        log.debug("REST request to count GenTags by criteria: {}", criteria);
        return ResponseEntity.ok().body(genTagQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /gen-tags/:id} : get the "id" genTag.
     *
     * @param id the id of the genTagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the genTagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gen-tags/{id}")
    public ResponseEntity<GenTagDTO> getGenTag(@PathVariable Long id) {
        log.debug("REST request to get GenTag : {}", id);
        Optional<GenTagDTO> genTagDTO = genTagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(genTagDTO);
    }

    /**
     * {@code DELETE  /gen-tags/:id} : delete the "id" genTag.
     *
     * @param id the id of the genTagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gen-tags/{id}")
    public ResponseEntity<Void> deleteGenTag(@PathVariable Long id) {
        log.debug("REST request to delete GenTag : {}", id);
        genTagService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
