package com.attest.ict.web.rest;

import com.attest.ict.repository.OutputFileRepository;
import com.attest.ict.service.OutputFileQueryService;
import com.attest.ict.service.OutputFileService;
import com.attest.ict.service.criteria.OutputFileCriteria;
import com.attest.ict.service.dto.OutputFileDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.OutputFile}.
 */
@RestController
@RequestMapping("/api")
public class OutputFileResource {

    private final Logger log = LoggerFactory.getLogger(OutputFileResource.class);

    private static final String ENTITY_NAME = "outputFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OutputFileService outputFileService;

    private final OutputFileRepository outputFileRepository;

    private final OutputFileQueryService outputFileQueryService;

    public OutputFileResource(
        OutputFileService outputFileService,
        OutputFileRepository outputFileRepository,
        OutputFileQueryService outputFileQueryService
    ) {
        this.outputFileService = outputFileService;
        this.outputFileRepository = outputFileRepository;
        this.outputFileQueryService = outputFileQueryService;
    }

    /**
     * {@code POST  /output-files} : Create a new outputFile.
     *
     * @param outputFileDTO the outputFileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new outputFileDTO, or with status {@code 400 (Bad Request)} if the outputFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/output-files")
    public ResponseEntity<OutputFileDTO> createOutputFile(@RequestBody OutputFileDTO outputFileDTO) throws URISyntaxException {
        log.debug("REST request to save OutputFile : {}", outputFileDTO);
        if (outputFileDTO.getId() != null) {
            throw new BadRequestAlertException("A new outputFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OutputFileDTO result = outputFileService.save(outputFileDTO);
        return ResponseEntity
            .created(new URI("/api/output-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /output-files/:id} : Updates an existing outputFile.
     *
     * @param id the id of the outputFileDTO to save.
     * @param outputFileDTO the outputFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated outputFileDTO,
     * or with status {@code 400 (Bad Request)} if the outputFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the outputFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/output-files/{id}")
    public ResponseEntity<OutputFileDTO> updateOutputFile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OutputFileDTO outputFileDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OutputFile : {}, {}", id, outputFileDTO);
        if (outputFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, outputFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!outputFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OutputFileDTO result = outputFileService.save(outputFileDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, outputFileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /output-files/:id} : Partial updates given fields of an existing outputFile, field will ignore if it is null
     *
     * @param id the id of the outputFileDTO to save.
     * @param outputFileDTO the outputFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated outputFileDTO,
     * or with status {@code 400 (Bad Request)} if the outputFileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the outputFileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the outputFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/output-files/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OutputFileDTO> partialUpdateOutputFile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OutputFileDTO outputFileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OutputFile partially : {}, {}", id, outputFileDTO);
        if (outputFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, outputFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!outputFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OutputFileDTO> result = outputFileService.partialUpdate(outputFileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, outputFileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /output-files} : get all the outputFiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of outputFiles in body.
     */
    @GetMapping("/output-files")
    public ResponseEntity<List<OutputFileDTO>> getAllOutputFiles(OutputFileCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OutputFiles by criteria: {}", criteria);
        Page<OutputFileDTO> page = outputFileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /output-files/count} : count all the outputFiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/output-files/count")
    public ResponseEntity<Long> countOutputFiles(OutputFileCriteria criteria) {
        log.debug("REST request to count OutputFiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(outputFileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /output-files/:id} : get the "id" outputFile.
     *
     * @param id the id of the outputFileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the outputFileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/output-files/{id}")
    public ResponseEntity<OutputFileDTO> getOutputFile(@PathVariable Long id) {
        log.debug("REST request to get OutputFile : {}", id);
        Optional<OutputFileDTO> outputFileDTO = outputFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(outputFileDTO);
    }

    /**
     * {@code DELETE  /output-files/:id} : delete the "id" outputFile.
     *
     * @param id the id of the outputFileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/output-files/{id}")
    public ResponseEntity<Void> deleteOutputFile(@PathVariable Long id) {
        log.debug("REST request to delete OutputFile : {}", id);
        outputFileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
