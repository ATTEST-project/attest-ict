package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Simulation;
import com.attest.ict.domain.Task;
import com.attest.ict.domain.Tool;
import com.attest.ict.domain.ToolLogFile;
import com.attest.ict.domain.User;
import com.attest.ict.repository.TaskRepository;
import com.attest.ict.service.criteria.TaskCriteria;
import com.attest.ict.service.dto.TaskDTO;
import com.attest.ict.service.mapper.TaskMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaskResourceIT {

    private static final String DEFAULT_TASK_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_TASK_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_INFO = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_TIME_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TIME_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_TIME_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TIME_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskMockMvc;

    private Task task;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createEntity(EntityManager em) {
        Task task = new Task()
            .taskStatus(DEFAULT_TASK_STATUS)
            .info(DEFAULT_INFO)
            .dateTimeStart(DEFAULT_DATE_TIME_START)
            .dateTimeEnd(DEFAULT_DATE_TIME_END);
        return task;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createUpdatedEntity(EntityManager em) {
        Task task = new Task()
            .taskStatus(UPDATED_TASK_STATUS)
            .info(UPDATED_INFO)
            .dateTimeStart(UPDATED_DATE_TIME_START)
            .dateTimeEnd(UPDATED_DATE_TIME_END);
        return task;
    }

    @BeforeEach
    public void initTest() {
        task = createEntity(em);
    }

    @Test
    @Transactional
    void createTask() throws Exception {
        int databaseSizeBeforeCreate = taskRepository.findAll().size();
        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);
        restTaskMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate + 1);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getTaskStatus()).isEqualTo(DEFAULT_TASK_STATUS);
        assertThat(testTask.getInfo()).isEqualTo(DEFAULT_INFO);
        assertThat(testTask.getDateTimeStart()).isEqualTo(DEFAULT_DATE_TIME_START);
        assertThat(testTask.getDateTimeEnd()).isEqualTo(DEFAULT_DATE_TIME_END);
    }

    @Test
    @Transactional
    void createTaskWithExistingId() throws Exception {
        // Create the Task with an existing ID
        task.setId(1L);
        TaskDTO taskDTO = taskMapper.toDto(task);

        int databaseSizeBeforeCreate = taskRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTasks() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskStatus").value(hasItem(DEFAULT_TASK_STATUS)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)))
            .andExpect(jsonPath("$.[*].dateTimeStart").value(hasItem(DEFAULT_DATE_TIME_START.toString())))
            .andExpect(jsonPath("$.[*].dateTimeEnd").value(hasItem(DEFAULT_DATE_TIME_END.toString())));
    }

    @Test
    @Transactional
    void getTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get the task
        restTaskMockMvc
            .perform(get(ENTITY_API_URL_ID, task.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(task.getId().intValue()))
            .andExpect(jsonPath("$.taskStatus").value(DEFAULT_TASK_STATUS))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO))
            .andExpect(jsonPath("$.dateTimeStart").value(DEFAULT_DATE_TIME_START.toString()))
            .andExpect(jsonPath("$.dateTimeEnd").value(DEFAULT_DATE_TIME_END.toString()));
    }

    @Test
    @Transactional
    void getTasksByIdFiltering() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        Long id = task.getId();

        defaultTaskShouldBeFound("id.equals=" + id);
        defaultTaskShouldNotBeFound("id.notEquals=" + id);

        defaultTaskShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTaskShouldNotBeFound("id.greaterThan=" + id);

        defaultTaskShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTaskShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTasksByTaskStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskStatus equals to DEFAULT_TASK_STATUS
        defaultTaskShouldBeFound("taskStatus.equals=" + DEFAULT_TASK_STATUS);

        // Get all the taskList where taskStatus equals to UPDATED_TASK_STATUS
        defaultTaskShouldNotBeFound("taskStatus.equals=" + UPDATED_TASK_STATUS);
    }

    @Test
    @Transactional
    void getAllTasksByTaskStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskStatus not equals to DEFAULT_TASK_STATUS
        defaultTaskShouldNotBeFound("taskStatus.notEquals=" + DEFAULT_TASK_STATUS);

        // Get all the taskList where taskStatus not equals to UPDATED_TASK_STATUS
        defaultTaskShouldBeFound("taskStatus.notEquals=" + UPDATED_TASK_STATUS);
    }

    @Test
    @Transactional
    void getAllTasksByTaskStatusIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskStatus in DEFAULT_TASK_STATUS or UPDATED_TASK_STATUS
        defaultTaskShouldBeFound("taskStatus.in=" + DEFAULT_TASK_STATUS + "," + UPDATED_TASK_STATUS);

        // Get all the taskList where taskStatus equals to UPDATED_TASK_STATUS
        defaultTaskShouldNotBeFound("taskStatus.in=" + UPDATED_TASK_STATUS);
    }

    @Test
    @Transactional
    void getAllTasksByTaskStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskStatus is not null
        defaultTaskShouldBeFound("taskStatus.specified=true");

        // Get all the taskList where taskStatus is null
        defaultTaskShouldNotBeFound("taskStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByTaskStatusContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskStatus contains DEFAULT_TASK_STATUS
        defaultTaskShouldBeFound("taskStatus.contains=" + DEFAULT_TASK_STATUS);

        // Get all the taskList where taskStatus contains UPDATED_TASK_STATUS
        defaultTaskShouldNotBeFound("taskStatus.contains=" + UPDATED_TASK_STATUS);
    }

    @Test
    @Transactional
    void getAllTasksByTaskStatusNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskStatus does not contain DEFAULT_TASK_STATUS
        defaultTaskShouldNotBeFound("taskStatus.doesNotContain=" + DEFAULT_TASK_STATUS);

        // Get all the taskList where taskStatus does not contain UPDATED_TASK_STATUS
        defaultTaskShouldBeFound("taskStatus.doesNotContain=" + UPDATED_TASK_STATUS);
    }

    @Test
    @Transactional
    void getAllTasksByInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where info equals to DEFAULT_INFO
        defaultTaskShouldBeFound("info.equals=" + DEFAULT_INFO);

        // Get all the taskList where info equals to UPDATED_INFO
        defaultTaskShouldNotBeFound("info.equals=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    void getAllTasksByInfoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where info not equals to DEFAULT_INFO
        defaultTaskShouldNotBeFound("info.notEquals=" + DEFAULT_INFO);

        // Get all the taskList where info not equals to UPDATED_INFO
        defaultTaskShouldBeFound("info.notEquals=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    void getAllTasksByInfoIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where info in DEFAULT_INFO or UPDATED_INFO
        defaultTaskShouldBeFound("info.in=" + DEFAULT_INFO + "," + UPDATED_INFO);

        // Get all the taskList where info equals to UPDATED_INFO
        defaultTaskShouldNotBeFound("info.in=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    void getAllTasksByInfoIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where info is not null
        defaultTaskShouldBeFound("info.specified=true");

        // Get all the taskList where info is null
        defaultTaskShouldNotBeFound("info.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByInfoContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where info contains DEFAULT_INFO
        defaultTaskShouldBeFound("info.contains=" + DEFAULT_INFO);

        // Get all the taskList where info contains UPDATED_INFO
        defaultTaskShouldNotBeFound("info.contains=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    void getAllTasksByInfoNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where info does not contain DEFAULT_INFO
        defaultTaskShouldNotBeFound("info.doesNotContain=" + DEFAULT_INFO);

        // Get all the taskList where info does not contain UPDATED_INFO
        defaultTaskShouldBeFound("info.doesNotContain=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    void getAllTasksByDateTimeStartIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateTimeStart equals to DEFAULT_DATE_TIME_START
        defaultTaskShouldBeFound("dateTimeStart.equals=" + DEFAULT_DATE_TIME_START);

        // Get all the taskList where dateTimeStart equals to UPDATED_DATE_TIME_START
        defaultTaskShouldNotBeFound("dateTimeStart.equals=" + UPDATED_DATE_TIME_START);
    }

    @Test
    @Transactional
    void getAllTasksByDateTimeStartIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateTimeStart not equals to DEFAULT_DATE_TIME_START
        defaultTaskShouldNotBeFound("dateTimeStart.notEquals=" + DEFAULT_DATE_TIME_START);

        // Get all the taskList where dateTimeStart not equals to UPDATED_DATE_TIME_START
        defaultTaskShouldBeFound("dateTimeStart.notEquals=" + UPDATED_DATE_TIME_START);
    }

    @Test
    @Transactional
    void getAllTasksByDateTimeStartIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateTimeStart in DEFAULT_DATE_TIME_START or UPDATED_DATE_TIME_START
        defaultTaskShouldBeFound("dateTimeStart.in=" + DEFAULT_DATE_TIME_START + "," + UPDATED_DATE_TIME_START);

        // Get all the taskList where dateTimeStart equals to UPDATED_DATE_TIME_START
        defaultTaskShouldNotBeFound("dateTimeStart.in=" + UPDATED_DATE_TIME_START);
    }

    @Test
    @Transactional
    void getAllTasksByDateTimeStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateTimeStart is not null
        defaultTaskShouldBeFound("dateTimeStart.specified=true");

        // Get all the taskList where dateTimeStart is null
        defaultTaskShouldNotBeFound("dateTimeStart.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByDateTimeEndIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateTimeEnd equals to DEFAULT_DATE_TIME_END
        defaultTaskShouldBeFound("dateTimeEnd.equals=" + DEFAULT_DATE_TIME_END);

        // Get all the taskList where dateTimeEnd equals to UPDATED_DATE_TIME_END
        defaultTaskShouldNotBeFound("dateTimeEnd.equals=" + UPDATED_DATE_TIME_END);
    }

    @Test
    @Transactional
    void getAllTasksByDateTimeEndIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateTimeEnd not equals to DEFAULT_DATE_TIME_END
        defaultTaskShouldNotBeFound("dateTimeEnd.notEquals=" + DEFAULT_DATE_TIME_END);

        // Get all the taskList where dateTimeEnd not equals to UPDATED_DATE_TIME_END
        defaultTaskShouldBeFound("dateTimeEnd.notEquals=" + UPDATED_DATE_TIME_END);
    }

    @Test
    @Transactional
    void getAllTasksByDateTimeEndIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateTimeEnd in DEFAULT_DATE_TIME_END or UPDATED_DATE_TIME_END
        defaultTaskShouldBeFound("dateTimeEnd.in=" + DEFAULT_DATE_TIME_END + "," + UPDATED_DATE_TIME_END);

        // Get all the taskList where dateTimeEnd equals to UPDATED_DATE_TIME_END
        defaultTaskShouldNotBeFound("dateTimeEnd.in=" + UPDATED_DATE_TIME_END);
    }

    @Test
    @Transactional
    void getAllTasksByDateTimeEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateTimeEnd is not null
        defaultTaskShouldBeFound("dateTimeEnd.specified=true");

        // Get all the taskList where dateTimeEnd is null
        defaultTaskShouldNotBeFound("dateTimeEnd.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByToolLogFileIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);
        ToolLogFile toolLogFile;
        if (TestUtil.findAll(em, ToolLogFile.class).isEmpty()) {
            toolLogFile = ToolLogFileResourceIT.createEntity(em);
            em.persist(toolLogFile);
            em.flush();
        } else {
            toolLogFile = TestUtil.findAll(em, ToolLogFile.class).get(0);
        }
        em.persist(toolLogFile);
        em.flush();
        task.setToolLogFile(toolLogFile);
        taskRepository.saveAndFlush(task);
        Long toolLogFileId = toolLogFile.getId();

        // Get all the taskList where toolLogFile equals to toolLogFileId
        defaultTaskShouldBeFound("toolLogFileId.equals=" + toolLogFileId);

        // Get all the taskList where toolLogFile equals to (toolLogFileId + 1)
        defaultTaskShouldNotBeFound("toolLogFileId.equals=" + (toolLogFileId + 1));
    }

    @Test
    @Transactional
    void getAllTasksBySimulationIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);
        Simulation simulation;
        if (TestUtil.findAll(em, Simulation.class).isEmpty()) {
            simulation = SimulationResourceIT.createEntity(em);
            em.persist(simulation);
            em.flush();
        } else {
            simulation = TestUtil.findAll(em, Simulation.class).get(0);
        }
        em.persist(simulation);
        em.flush();
        task.setSimulation(simulation);
        taskRepository.saveAndFlush(task);
        Long simulationId = simulation.getId();

        // Get all the taskList where simulation equals to simulationId
        defaultTaskShouldBeFound("simulationId.equals=" + simulationId);

        // Get all the taskList where simulation equals to (simulationId + 1)
        defaultTaskShouldNotBeFound("simulationId.equals=" + (simulationId + 1));
    }

    @Test
    @Transactional
    void getAllTasksByToolIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);
        Tool tool;
        if (TestUtil.findAll(em, Tool.class).isEmpty()) {
            tool = ToolResourceIT.createEntity(em);
            em.persist(tool);
            em.flush();
        } else {
            tool = TestUtil.findAll(em, Tool.class).get(0);
        }
        em.persist(tool);
        em.flush();
        task.setTool(tool);
        taskRepository.saveAndFlush(task);
        Long toolId = tool.getId();

        // Get all the taskList where tool equals to toolId
        defaultTaskShouldBeFound("toolId.equals=" + toolId);

        // Get all the taskList where tool equals to (toolId + 1)
        defaultTaskShouldNotBeFound("toolId.equals=" + (toolId + 1));
    }

    @Test
    @Transactional
    void getAllTasksByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            user = UserResourceIT.createEntity(em);
            em.persist(user);
            em.flush();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        task.setUser(user);
        taskRepository.saveAndFlush(task);
        String userId = user.getId();

        // Get all the taskList where user equals to userId
        defaultTaskShouldBeFound("userId.equals=" + userId);

        // Get all the taskList where user equals to "invalid-id"
        defaultTaskShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaskShouldBeFound(String filter) throws Exception {
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskStatus").value(hasItem(DEFAULT_TASK_STATUS)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)))
            .andExpect(jsonPath("$.[*].dateTimeStart").value(hasItem(DEFAULT_DATE_TIME_START.toString())))
            .andExpect(jsonPath("$.[*].dateTimeEnd").value(hasItem(DEFAULT_DATE_TIME_END.toString())));

        // Check, that the count call also returns 1
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaskShouldNotBeFound(String filter) throws Exception {
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTask() throws Exception {
        // Get the task
        restTaskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task
        Task updatedTask = taskRepository.findById(task.getId()).get();
        // Disconnect from session so that the updates on updatedTask are not directly saved in db
        em.detach(updatedTask);
        updatedTask
            .taskStatus(UPDATED_TASK_STATUS)
            .info(UPDATED_INFO)
            .dateTimeStart(UPDATED_DATE_TIME_START)
            .dateTimeEnd(UPDATED_DATE_TIME_END);
        TaskDTO taskDTO = taskMapper.toDto(updatedTask);

        restTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getTaskStatus()).isEqualTo(UPDATED_TASK_STATUS);
        assertThat(testTask.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testTask.getDateTimeStart()).isEqualTo(UPDATED_DATE_TIME_START);
        assertThat(testTask.getDateTimeEnd()).isEqualTo(UPDATED_DATE_TIME_END);
    }

    @Test
    @Transactional
    void putNonExistingTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(count.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(count.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(count.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaskWithPatch() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task using partial update
        Task partialUpdatedTask = new Task();
        partialUpdatedTask.setId(task.getId());

        partialUpdatedTask.info(UPDATED_INFO).dateTimeEnd(UPDATED_DATE_TIME_END);

        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTask.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTask))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getTaskStatus()).isEqualTo(DEFAULT_TASK_STATUS);
        assertThat(testTask.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testTask.getDateTimeStart()).isEqualTo(DEFAULT_DATE_TIME_START);
        assertThat(testTask.getDateTimeEnd()).isEqualTo(UPDATED_DATE_TIME_END);
    }

    @Test
    @Transactional
    void fullUpdateTaskWithPatch() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task using partial update
        Task partialUpdatedTask = new Task();
        partialUpdatedTask.setId(task.getId());

        partialUpdatedTask
            .taskStatus(UPDATED_TASK_STATUS)
            .info(UPDATED_INFO)
            .dateTimeStart(UPDATED_DATE_TIME_START)
            .dateTimeEnd(UPDATED_DATE_TIME_END);

        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTask.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTask))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getTaskStatus()).isEqualTo(UPDATED_TASK_STATUS);
        assertThat(testTask.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testTask.getDateTimeStart()).isEqualTo(UPDATED_DATE_TIME_START);
        assertThat(testTask.getDateTimeEnd()).isEqualTo(UPDATED_DATE_TIME_END);
    }

    @Test
    @Transactional
    void patchNonExistingTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(count.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taskDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(count.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(count.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        int databaseSizeBeforeDelete = taskRepository.findAll().size();

        // Delete the task
        restTaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, task.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
