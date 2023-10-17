package com.attest.ict.web.rest;

import com.attest.ict.custom.exception.KillProcessException;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.domain.OutputFile;
import com.attest.ict.helper.TaskStatus;
import com.attest.ict.repository.TaskRepository;
import com.attest.ict.service.CommandExecutorService;
import com.attest.ict.service.OutputFileService;
import com.attest.ict.service.TaskQueryService;
import com.attest.ict.service.TaskService;
import com.attest.ict.service.dto.TaskDTO;
import com.attest.ict.tools.constants.ToolFileFormat;
import com.attest.ict.web.rest.errors.BadRequestAlertException;
import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.attest.ict.domain.Task}.
 */
@RestController
@RequestMapping("/api")
public class TaskAdditionalResource {

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(TaskAdditionalResource.class);
    private static final String ENTITY_NAME = "task";
    private final TaskService taskService;
    private final TaskRepository taskRepository;
    private final TaskQueryService taskQueryService;
    private final OutputFileService outputFileService;
    private final CommandExecutorService commandExecutionService;

    public TaskAdditionalResource(
        TaskService taskService,
        TaskRepository taskRepository,
        TaskQueryService taskQueryService,
        OutputFileService outputFileService,
        CommandExecutorService commandExecutionService
    ) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
        this.taskQueryService = taskQueryService;
        this.outputFileService = outputFileService;
        this.commandExecutionService = commandExecutionService;
    }

    /**
     * {@code GET  /tasks/tool-results/:id} : the "id" task.
     *
     * @param id the id of the taskDTO
     * @return File or Zip containing all output file provided during the execution
     * of the tool
     */
    @GetMapping("/tasks/tool-results/{id}")
    public ResponseEntity<?> downloadToolResults(@PathVariable Long id) {
        try {
            log.debug("Request to download tool's output files for task: {}", id);
            Optional<TaskDTO> taskDTO = taskService.findOne(id);
            if (!taskDTO.isPresent()) {
                throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
            }

            TaskDTO taskDto = taskDTO.get();
            Long simulationId = taskDto.getSimulationId();

            if (simulationId == null) {
                return new ResponseEntity<>("Simulation data not present for task " + id, HttpStatus.OK);
            }
            List<OutputFile> outputFileList = outputFileService.findFromSimulationId(simulationId);

            if (outputFileList.isEmpty()) {
                return new ResponseEntity<>("Tools's output file not present for task " + id, HttpStatus.OK);
            }

            String archiveFileName = taskDto.getSimulationUuid() + ToolFileFormat.ZIP_EXTENSION;
            ByteArrayOutputStream bos = FileUtils.zipOutputFiles(outputFileList);
            ByteArrayResource resource = new ByteArrayResource(bos.toByteArray());
            return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + archiveFileName)
                .contentType(MediaType.parseMediaType(FileUtils.CONTENT_TYPE.get("zip")))
                .body(resource);
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("downloadToolResults raise this exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * {@code PATCH  /tasks/kill/:id}: Kill process referenced by the task with id identifier, and set task status to killed
     *
     * @param id the id of the taskDTO
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the updated taskDTO,
     * or with status {@code 404 (Not Found)} if the taskDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the taskDTO
     * couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tasks/kill/{id}")
    public ResponseEntity<?> killProcess(@PathVariable(value = "id", required = true) final Long id) throws URISyntaxException {
        log.info("REST request to KILL the process launched by the Task id: {}", id);

        try {
            boolean isKilled = commandExecutionService.killProcessByTaskId(id);
            log.debug("Request Kill process Find Task, taskid: {}", id);
            Optional<TaskDTO> taskDtoOpt = taskService.findOne(id);
            if (!taskDtoOpt.isPresent()) {
                throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
            }
            if (isKilled) {
                TaskDTO taskDTO = taskDtoOpt.get();
                taskDTO.setInfo(TaskStatus.Status.KILLED.name());
                taskDTO.setTaskStatus(TaskStatus.Status.FAILED.name());
                Optional<TaskDTO> result = taskService.partialUpdate(taskDTO);
                log.info("Exit: Request Kill process task updated:{} ", result);
                return ResponseUtil.wrapOrNotFound(
                    result,
                    HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, taskDTO.getId().toString())
                );
            } else {
                log.info("Exit: Request Kill process with result: stop process not needed!");
                return ResponseUtil.wrapOrNotFound(
                    taskDtoOpt,
                    HeaderUtil.createAlert(applicationName, "Kill process not needed", taskDtoOpt.get().getId().toString())
                );
            }
        } catch (KillProcessException spe) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(spe.getMessage());
        }
    }
}
