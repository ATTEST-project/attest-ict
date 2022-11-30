package com.attest.ict.web.rest;

import com.attest.ict.repository.InputFileRepository;
import com.attest.ict.service.InputFileQueryService;
import com.attest.ict.service.InputFileService;
import com.attest.ict.service.criteria.InputFileCriteria;
import com.attest.ict.service.dto.InputFileDTO;
import com.attest.ict.service.dto.custom.ProfileInputFileDTO;
import com.attest.ict.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.attest.ict.domain.InputFile}.
 */
@RestController
@RequestMapping("/api")
public class InputFileResource {

    private final Logger log = LoggerFactory.getLogger(InputFileResource.class);

    private static final String ENTITY_NAME = "inputFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InputFileService inputFileService;

    private final InputFileRepository inputFileRepository;

    private final InputFileQueryService inputFileQueryService;

    public InputFileResource(
        InputFileService inputFileService,
        InputFileRepository inputFileRepository,
        InputFileQueryService inputFileQueryService
    ) {
        this.inputFileService = inputFileService;
        this.inputFileRepository = inputFileRepository;
        this.inputFileQueryService = inputFileQueryService;
    }

    /**
     * {@code POST  /input-files} : Create a new inputFile.
     *
     * @param inputFileDTO the inputFileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inputFileDTO, or with status {@code 400 (Bad Request)} if the inputFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/input-files")
    public ResponseEntity<InputFileDTO> createInputFile(@RequestBody InputFileDTO inputFileDTO) throws URISyntaxException {
        log.debug("REST request to save InputFile : {}", inputFileDTO);
        if (inputFileDTO.getId() != null) {
            throw new BadRequestAlertException("A new inputFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InputFileDTO result = inputFileService.save(inputFileDTO);
        return ResponseEntity
            .created(new URI("/api/input-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /input-files/:id} : Updates an existing inputFile.
     *
     * @param id the id of the inputFileDTO to save.
     * @param inputFileDTO the inputFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inputFileDTO,
     * or with status {@code 400 (Bad Request)} if the inputFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inputFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/input-files/{id}")
    public ResponseEntity<InputFileDTO> updateInputFile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InputFileDTO inputFileDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InputFile : {}, {}", id, inputFileDTO);
        if (inputFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inputFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inputFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InputFileDTO result = inputFileService.save(inputFileDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, inputFileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /input-files/:id} : Partial updates given fields of an existing inputFile, field will ignore if it is null
     *
     * @param id the id of the inputFileDTO to save.
     * @param inputFileDTO the inputFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inputFileDTO,
     * or with status {@code 400 (Bad Request)} if the inputFileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inputFileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inputFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/input-files/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InputFileDTO> partialUpdateInputFile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InputFileDTO inputFileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InputFile partially : {}, {}", id, inputFileDTO);
        if (inputFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inputFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inputFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InputFileDTO> result = inputFileService.partialUpdate(inputFileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, inputFileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /input-files} : get all the inputFiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inputFiles in body.
     */
    @GetMapping("/input-files")
    public ResponseEntity<List<InputFileDTO>> getAllInputFiles(InputFileCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InputFiles by criteria: {}", criteria);
        Page<InputFileDTO> page = inputFileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /input-files/count} : count all the inputFiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/input-files/count")
    public ResponseEntity<Long> countInputFiles(InputFileCriteria criteria) {
        log.debug("REST request to count InputFiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(inputFileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /input-files/:id} : get the "id" inputFile.
     *
     * @param id the id of the inputFileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inputFileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/input-files/{id}")
    public ResponseEntity<InputFileDTO> getInputFile(@PathVariable Long id) {
        log.debug("REST request to get InputFile : {}", id);
        Optional<InputFileDTO> inputFileDTO = inputFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inputFileDTO);
    }

    /**
     * {@code DELETE  /input-files/:id} : delete the "id" inputFile.
     *
     * @param id the id of the inputFileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/input-files/{id}")
    public ResponseEntity<Void> deleteInputFile(@PathVariable Long id) {
        log.debug("REST request to delete InputFile : {}", id);
        inputFileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     *
     * @param networkId id of the network
     * @return list of input files loaded for networkId
     */
    @GetMapping("/input-files-network/{networkId}")
    public ResponseEntity<List<ProfileInputFileDTO>> getInputFilesLoadedForNetwork(@PathVariable Long networkId) {
        log.debug("REST request to get list of input file for NetworkId : {}", networkId);
        List<ProfileInputFileDTO> profileInputFileList = inputFileService.findFilesLoadedForNetwork(networkId);
        return new ResponseEntity(profileInputFileList, HttpStatus.OK);
    }
}
