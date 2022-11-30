package com.attest.ict.web.rest;

import com.attest.ict.repository.TransfElValRepository;
import com.attest.ict.service.TransfElValQueryService;
import com.attest.ict.service.TransfElValService;
import com.attest.ict.service.criteria.TransfElValCriteria;
import com.attest.ict.service.dto.TransfElValDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.TransfElVal}.
 */
@RestController
@RequestMapping("/api")
public class TransfElValResource {

    private final Logger log = LoggerFactory.getLogger(TransfElValResource.class);

    private static final String ENTITY_NAME = "transfElVal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransfElValService transfElValService;

    private final TransfElValRepository transfElValRepository;

    private final TransfElValQueryService transfElValQueryService;

    public TransfElValResource(
        TransfElValService transfElValService,
        TransfElValRepository transfElValRepository,
        TransfElValQueryService transfElValQueryService
    ) {
        this.transfElValService = transfElValService;
        this.transfElValRepository = transfElValRepository;
        this.transfElValQueryService = transfElValQueryService;
    }

    /**
     * {@code POST  /transf-el-vals} : Create a new transfElVal.
     *
     * @param transfElValDTO the transfElValDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transfElValDTO, or with status {@code 400 (Bad Request)} if the transfElVal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transf-el-vals")
    public ResponseEntity<TransfElValDTO> createTransfElVal(@RequestBody TransfElValDTO transfElValDTO) throws URISyntaxException {
        log.debug("REST request to save TransfElVal : {}", transfElValDTO);
        if (transfElValDTO.getId() != null) {
            throw new BadRequestAlertException("A new transfElVal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransfElValDTO result = transfElValService.save(transfElValDTO);
        return ResponseEntity
            .created(new URI("/api/transf-el-vals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transf-el-vals/:id} : Updates an existing transfElVal.
     *
     * @param id the id of the transfElValDTO to save.
     * @param transfElValDTO the transfElValDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transfElValDTO,
     * or with status {@code 400 (Bad Request)} if the transfElValDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transfElValDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transf-el-vals/{id}")
    public ResponseEntity<TransfElValDTO> updateTransfElVal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TransfElValDTO transfElValDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TransfElVal : {}, {}", id, transfElValDTO);
        if (transfElValDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transfElValDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transfElValRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransfElValDTO result = transfElValService.save(transfElValDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transfElValDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transf-el-vals/:id} : Partial updates given fields of an existing transfElVal, field will ignore if it is null
     *
     * @param id the id of the transfElValDTO to save.
     * @param transfElValDTO the transfElValDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transfElValDTO,
     * or with status {@code 400 (Bad Request)} if the transfElValDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transfElValDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transfElValDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transf-el-vals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransfElValDTO> partialUpdateTransfElVal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TransfElValDTO transfElValDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TransfElVal partially : {}, {}", id, transfElValDTO);
        if (transfElValDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transfElValDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transfElValRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransfElValDTO> result = transfElValService.partialUpdate(transfElValDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transfElValDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transf-el-vals} : get all the transfElVals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transfElVals in body.
     */
    @GetMapping("/transf-el-vals")
    public ResponseEntity<List<TransfElValDTO>> getAllTransfElVals(TransfElValCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TransfElVals by criteria: {}", criteria);
        Page<TransfElValDTO> page = transfElValQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transf-el-vals/count} : count all the transfElVals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/transf-el-vals/count")
    public ResponseEntity<Long> countTransfElVals(TransfElValCriteria criteria) {
        log.debug("REST request to count TransfElVals by criteria: {}", criteria);
        return ResponseEntity.ok().body(transfElValQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transf-el-vals/:id} : get the "id" transfElVal.
     *
     * @param id the id of the transfElValDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transfElValDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transf-el-vals/{id}")
    public ResponseEntity<TransfElValDTO> getTransfElVal(@PathVariable Long id) {
        log.debug("REST request to get TransfElVal : {}", id);
        Optional<TransfElValDTO> transfElValDTO = transfElValService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transfElValDTO);
    }

    /**
     * {@code DELETE  /transf-el-vals/:id} : delete the "id" transfElVal.
     *
     * @param id the id of the transfElValDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transf-el-vals/{id}")
    public ResponseEntity<Void> deleteTransfElVal(@PathVariable Long id) {
        log.debug("REST request to delete TransfElVal : {}", id);
        transfElValService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
