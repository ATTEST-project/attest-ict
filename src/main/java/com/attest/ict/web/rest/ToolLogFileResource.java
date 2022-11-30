package com.attest.ict.web.rest;

import com.attest.ict.domain.ToolLogFile;
import com.attest.ict.repository.ToolLogFileRepository;
import com.attest.ict.service.ToolLogFileQueryService;
import com.attest.ict.service.ToolLogFileService;
import com.attest.ict.service.criteria.ToolLogFileCriteria;
import com.attest.ict.service.dto.ToolLogFileDTO;
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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.attest.ict.domain.ToolLogFile}.
 */
@RestController
@RequestMapping("/api")
public class ToolLogFileResource {

    private final Logger log = LoggerFactory.getLogger(ToolLogFileResource.class);

    private static final String ENTITY_NAME = "toolLogFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ToolLogFileService toolLogFileService;

    private final ToolLogFileRepository toolLogFileRepository;

    private final ToolLogFileQueryService toolLogFileQueryService;

    public ToolLogFileResource(
        ToolLogFileService toolLogFileService,
        ToolLogFileRepository toolLogFileRepository,
        ToolLogFileQueryService toolLogFileQueryService
    ) {
        this.toolLogFileService = toolLogFileService;
        this.toolLogFileRepository = toolLogFileRepository;
        this.toolLogFileQueryService = toolLogFileQueryService;
    }

    /**
     * {@code POST  /tool-log-files} : Create a new toolLogFile.
     *
     * @param toolLogFileDTO the toolLogFileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new toolLogFileDTO, or with status {@code 400 (Bad Request)} if the toolLogFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tool-log-files")
    public ResponseEntity<ToolLogFileDTO> createToolLogFile(@RequestBody ToolLogFileDTO toolLogFileDTO) throws URISyntaxException {
        log.debug("REST request to save ToolLogFile : {}", toolLogFileDTO);
        if (toolLogFileDTO.getId() != null) {
            throw new BadRequestAlertException("A new toolLogFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ToolLogFileDTO result = toolLogFileService.save(toolLogFileDTO);
        return ResponseEntity
            .created(new URI("/api/tool-log-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tool-log-files/:id} : Updates an existing toolLogFile.
     *
     * @param id the id of the toolLogFileDTO to save.
     * @param toolLogFileDTO the toolLogFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toolLogFileDTO,
     * or with status {@code 400 (Bad Request)} if the toolLogFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the toolLogFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tool-log-files/{id}")
    public ResponseEntity<ToolLogFileDTO> updateToolLogFile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ToolLogFileDTO toolLogFileDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ToolLogFile : {}, {}", id, toolLogFileDTO);
        if (toolLogFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, toolLogFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!toolLogFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ToolLogFileDTO result = toolLogFileService.save(toolLogFileDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, toolLogFileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tool-log-files/:id} : Partial updates given fields of an existing toolLogFile, field will ignore if it is null
     *
     * @param id the id of the toolLogFileDTO to save.
     * @param toolLogFileDTO the toolLogFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toolLogFileDTO,
     * or with status {@code 400 (Bad Request)} if the toolLogFileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the toolLogFileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the toolLogFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tool-log-files/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ToolLogFileDTO> partialUpdateToolLogFile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ToolLogFileDTO toolLogFileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ToolLogFile partially : {}, {}", id, toolLogFileDTO);
        if (toolLogFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, toolLogFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!toolLogFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ToolLogFileDTO> result = toolLogFileService.partialUpdate(toolLogFileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, toolLogFileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tool-log-files} : get all the toolLogFiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of toolLogFiles in body.
     */
    @GetMapping("/tool-log-files")
    public ResponseEntity<List<ToolLogFileDTO>> getAllToolLogFiles(ToolLogFileCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ToolLogFiles by criteria: {}", criteria);
        Page<ToolLogFileDTO> page = toolLogFileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tool-log-files/count} : count all the toolLogFiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tool-log-files/count")
    public ResponseEntity<Long> countToolLogFiles(ToolLogFileCriteria criteria) {
        log.debug("REST request to count ToolLogFiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(toolLogFileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tool-log-files/:id} : get the "id" toolLogFile.
     *
     * @param id the id of the toolLogFileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the toolLogFileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tool-log-files/{id}")
    public ResponseEntity<ToolLogFileDTO> getToolLogFile(@PathVariable Long id) {
        log.debug("REST request to get ToolLogFile : {}", id);
        Optional<ToolLogFileDTO> toolLogFileDTO = toolLogFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(toolLogFileDTO);
    }

    /**
     * {@code DELETE  /tool-log-files/:id} : delete the "id" toolLogFile.
     *
     * @param id the id of the toolLogFileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tool-log-files/{id}")
    public ResponseEntity<Void> deleteToolLogFile(@PathVariable Long id) {
        log.debug("REST request to delete ToolLogFile : {}", id);
        toolLogFileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    //===== Start  Custom Methods
    //EX @GetMapping("/getLogsFile/{taskId}")
    @GetMapping("/tool-log-files/taskId/{taskId}")
    public ResponseEntity<?> getToolLogsFileByTaskId(@PathVariable("taskId") long taskId) {
        log.debug("REST request to get ToolLogFile by taskId: {}", taskId);
        try {
            Optional<ToolLogFile> toolLogFileOpt = toolLogFileService.findByTaskId(taskId);
            if (!toolLogFileOpt.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            ToolLogFile toolLogFile = toolLogFileOpt.get();
            return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(toolLogFile.getDataContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + toolLogFile.getFileName() + "\"")
                .body(new ByteArrayResource(toolLogFile.getData()));
        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
