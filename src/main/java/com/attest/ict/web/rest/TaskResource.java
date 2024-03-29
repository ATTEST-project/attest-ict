package com.attest.ict.web.rest;

import com.attest.ict.helper.TaskStatus;
import com.attest.ict.repository.TaskRepository;
import com.attest.ict.service.*;
import com.attest.ict.service.criteria.TaskCriteria;
import com.attest.ict.service.dto.TaskDTO;
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
 * REST controller for managing {@link com.attest.ict.domain.Task}.
 */
@RestController
@RequestMapping("/api")
public class TaskResource {

    private final Logger log = LoggerFactory.getLogger(TaskResource.class);

    private static final String ENTITY_NAME = "task";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaskService taskService;

    private final TaskRepository taskRepository;

    private final TaskQueryService taskQueryService;

    // custom
    private final OutputFileService outputFileService;

    private final ToolLogFileService toolLogFileService;

    private final SimulationService simulationService;

    public TaskResource(
        TaskService taskService,
        TaskRepository taskRepository,
        TaskQueryService taskQueryService,
        OutputFileService outputFileService,
        ToolLogFileService toolLogFileService,
        SimulationService simulationService
    ) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
        this.taskQueryService = taskQueryService;
        this.outputFileService = outputFileService;
        this.toolLogFileService = toolLogFileService;
        this.simulationService = simulationService;
    }

    /**
     * {@code POST  /tasks} : Create a new task.
     *
     * @param taskDTO the taskDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new taskDTO, or with status {@code 400 (Bad Request)} if the
     *         task has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tasks")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) throws URISyntaxException {
        log.debug("REST request to save Task : {}", taskDTO);
        if (taskDTO.getId() != null) {
            throw new BadRequestAlertException("A new task cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaskDTO result = taskService.save(taskDTO);
        return ResponseEntity
            .created(new URI("/api/tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tasks/:id} : Updates an existing task.
     *
     * @param id      the id of the taskDTO to save.
     * @param taskDTO the taskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated taskDTO,
     *         or with status {@code 400 (Bad Request)} if the taskDTO is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the taskDTO
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable(value = "id", required = false) final Long id, @RequestBody TaskDTO taskDTO)
        throws URISyntaxException {
        log.debug("REST request to update Task : {}, {}", id, taskDTO);
        if (taskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        // toolLogFileId=292, toolLogFileName=log.txt, networkId=95, simulationId=300
        if (taskDTO.getToolLogFileId() != null) {
            taskDTO.setToolLogFile(toolLogFileService.findOne(taskDTO.getToolLogFileId()).get());
        }

        if (taskDTO.getSimulationId() != null) {
            taskDTO.setSimulation(simulationService.findOne(taskDTO.getSimulationId()).get());
        }

        TaskDTO result = taskService.save(taskDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, taskDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tasks/:id} : Partial updates given fields of an existing task,
     * field will ignore if it is null
     *
     * @param id      the id of the taskDTO to save.
     * @param taskDTO the taskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated taskDTO,
     *         or with status {@code 400 (Bad Request)} if the taskDTO is not valid,
     *         or with status {@code 404 (Not Found)} if the taskDTO is not found,
     *         or with status {@code 500 (Internal Server Error)} if the taskDTO
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tasks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TaskDTO> partialUpdateTask(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TaskDTO taskDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Task partially : {}, {}", id, taskDTO);
        if (taskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaskDTO> result = taskService.partialUpdate(taskDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, taskDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tasks} : get all the tasks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of tasks in body.
     */
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getAllTasks(TaskCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Tasks by criteria: {}", criteria);
        Page<TaskDTO> page = taskQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tasks/count} : count all the tasks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/tasks/count")
    public ResponseEntity<Long> countTasks(TaskCriteria criteria) {
        log.debug("REST request to count Tasks by criteria: {}", criteria);
        return ResponseEntity.ok().body(taskQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tasks/:id} : get the "id" task.
     *
     * @param id the id of the taskDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the taskDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        log.debug("REST request to get Task : {}", id);
        Optional<TaskDTO> taskDTO = taskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taskDTO);
    }

    /**
     * {@code DELETE  /tasks/:id} : delete the "id" task.
     *
     * @param id the id of the taskDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.debug("REST request to delete Task by ID: {}", id);
        Optional<TaskDTO> taskDtoOpt = taskService.findOne(id);
        if (!taskDtoOpt.isPresent()) {
            log.warn("Task with ID {} not found.", id);
            return ResponseEntity.notFound().build();
        }

        TaskDTO task = taskDtoOpt.get();
        String status = taskDtoOpt.get().getTaskStatus();

        log.debug("REST request to delete Task: {}", task);

        if (TaskStatus.Status.ONGOING.name().equals(status)) {
            log.info(
                "Deletion of Task ID: {} for Simulation UUID: {} is forbidden, the task is in ONGOING status.",
                task.getId(),
                task.getSimulationUuid()
            );
            return ResponseEntity
                .noContent()
                .headers(
                    HeaderUtil.createAlert(
                        applicationName,
                        "Deletion is forbidden for the task in ONGOING status",
                        taskDtoOpt.get().getId().toString()
                    )
                )
                .build();
        } else {
            taskService.delete(id);
            log.info("Deleted Task ID: {} for Simulation UUID: {}", task.getId(), task.getSimulationUuid());
            return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                .build();
        }
    }
}
