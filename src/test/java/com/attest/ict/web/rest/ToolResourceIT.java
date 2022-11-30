package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.InputFile;
import com.attest.ict.domain.OutputFile;
import com.attest.ict.domain.Task;
import com.attest.ict.domain.Tool;
import com.attest.ict.domain.ToolParameter;
import com.attest.ict.repository.ToolRepository;
import com.attest.ict.service.criteria.ToolCriteria;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.mapper.ToolMapper;
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
 * Integration tests for the {@link ToolResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ToolResourceIT {

    private static final String DEFAULT_WORK_PACKAGE = "AAAAAAAAAA";
    private static final String UPDATED_WORK_PACKAGE = "BBBBBBBBBB";

    private static final String DEFAULT_NUM = "AAAAAAAAAA";
    private static final String UPDATED_NUM = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tools";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private ToolMapper toolMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restToolMockMvc;

    private Tool tool;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tool createEntity(EntityManager em) {
        Tool tool = new Tool()
            .workPackage(DEFAULT_WORK_PACKAGE)
            .num(DEFAULT_NUM)
            .name(DEFAULT_NAME)
            .path(DEFAULT_PATH)
            .description(DEFAULT_DESCRIPTION);
        return tool;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tool createUpdatedEntity(EntityManager em) {
        Tool tool = new Tool()
            .workPackage(UPDATED_WORK_PACKAGE)
            .num(UPDATED_NUM)
            .name(UPDATED_NAME)
            .path(UPDATED_PATH)
            .description(UPDATED_DESCRIPTION);
        return tool;
    }

    @BeforeEach
    public void initTest() {
        tool = createEntity(em);
    }

    @Test
    @Transactional
    void createTool() throws Exception {
        int databaseSizeBeforeCreate = toolRepository.findAll().size();
        // Create the Tool
        ToolDTO toolDTO = toolMapper.toDto(tool);
        restToolMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Tool in the database
        List<Tool> toolList = toolRepository.findAll();
        assertThat(toolList).hasSize(databaseSizeBeforeCreate + 1);
        Tool testTool = toolList.get(toolList.size() - 1);
        assertThat(testTool.getWorkPackage()).isEqualTo(DEFAULT_WORK_PACKAGE);
        assertThat(testTool.getNum()).isEqualTo(DEFAULT_NUM);
        assertThat(testTool.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTool.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testTool.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createToolWithExistingId() throws Exception {
        // Create the Tool with an existing ID
        tool.setId(1L);
        ToolDTO toolDTO = toolMapper.toDto(tool);

        int databaseSizeBeforeCreate = toolRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restToolMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tool in the database
        List<Tool> toolList = toolRepository.findAll();
        assertThat(toolList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkWorkPackageIsRequired() throws Exception {
        int databaseSizeBeforeTest = toolRepository.findAll().size();
        // set the field null
        tool.setWorkPackage(null);

        // Create the Tool, which fails.
        ToolDTO toolDTO = toolMapper.toDto(tool);

        restToolMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolDTO))
            )
            .andExpect(status().isBadRequest());

        List<Tool> toolList = toolRepository.findAll();
        assertThat(toolList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumIsRequired() throws Exception {
        int databaseSizeBeforeTest = toolRepository.findAll().size();
        // set the field null
        tool.setNum(null);

        // Create the Tool, which fails.
        ToolDTO toolDTO = toolMapper.toDto(tool);

        restToolMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolDTO))
            )
            .andExpect(status().isBadRequest());

        List<Tool> toolList = toolRepository.findAll();
        assertThat(toolList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = toolRepository.findAll().size();
        // set the field null
        tool.setName(null);

        // Create the Tool, which fails.
        ToolDTO toolDTO = toolMapper.toDto(tool);

        restToolMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolDTO))
            )
            .andExpect(status().isBadRequest());

        List<Tool> toolList = toolRepository.findAll();
        assertThat(toolList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTools() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList
        restToolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tool.getId().intValue())))
            .andExpect(jsonPath("$.[*].workPackage").value(hasItem(DEFAULT_WORK_PACKAGE)))
            .andExpect(jsonPath("$.[*].num").value(hasItem(DEFAULT_NUM)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getTool() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get the tool
        restToolMockMvc
            .perform(get(ENTITY_API_URL_ID, tool.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tool.getId().intValue()))
            .andExpect(jsonPath("$.workPackage").value(DEFAULT_WORK_PACKAGE))
            .andExpect(jsonPath("$.num").value(DEFAULT_NUM))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getToolsByIdFiltering() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        Long id = tool.getId();

        defaultToolShouldBeFound("id.equals=" + id);
        defaultToolShouldNotBeFound("id.notEquals=" + id);

        defaultToolShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultToolShouldNotBeFound("id.greaterThan=" + id);

        defaultToolShouldBeFound("id.lessThanOrEqual=" + id);
        defaultToolShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllToolsByWorkPackageIsEqualToSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where workPackage equals to DEFAULT_WORK_PACKAGE
        defaultToolShouldBeFound("workPackage.equals=" + DEFAULT_WORK_PACKAGE);

        // Get all the toolList where workPackage equals to UPDATED_WORK_PACKAGE
        defaultToolShouldNotBeFound("workPackage.equals=" + UPDATED_WORK_PACKAGE);
    }

    @Test
    @Transactional
    void getAllToolsByWorkPackageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where workPackage not equals to DEFAULT_WORK_PACKAGE
        defaultToolShouldNotBeFound("workPackage.notEquals=" + DEFAULT_WORK_PACKAGE);

        // Get all the toolList where workPackage not equals to UPDATED_WORK_PACKAGE
        defaultToolShouldBeFound("workPackage.notEquals=" + UPDATED_WORK_PACKAGE);
    }

    @Test
    @Transactional
    void getAllToolsByWorkPackageIsInShouldWork() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where workPackage in DEFAULT_WORK_PACKAGE or UPDATED_WORK_PACKAGE
        defaultToolShouldBeFound("workPackage.in=" + DEFAULT_WORK_PACKAGE + "," + UPDATED_WORK_PACKAGE);

        // Get all the toolList where workPackage equals to UPDATED_WORK_PACKAGE
        defaultToolShouldNotBeFound("workPackage.in=" + UPDATED_WORK_PACKAGE);
    }

    @Test
    @Transactional
    void getAllToolsByWorkPackageIsNullOrNotNull() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where workPackage is not null
        defaultToolShouldBeFound("workPackage.specified=true");

        // Get all the toolList where workPackage is null
        defaultToolShouldNotBeFound("workPackage.specified=false");
    }

    @Test
    @Transactional
    void getAllToolsByWorkPackageContainsSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where workPackage contains DEFAULT_WORK_PACKAGE
        defaultToolShouldBeFound("workPackage.contains=" + DEFAULT_WORK_PACKAGE);

        // Get all the toolList where workPackage contains UPDATED_WORK_PACKAGE
        defaultToolShouldNotBeFound("workPackage.contains=" + UPDATED_WORK_PACKAGE);
    }

    @Test
    @Transactional
    void getAllToolsByWorkPackageNotContainsSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where workPackage does not contain DEFAULT_WORK_PACKAGE
        defaultToolShouldNotBeFound("workPackage.doesNotContain=" + DEFAULT_WORK_PACKAGE);

        // Get all the toolList where workPackage does not contain UPDATED_WORK_PACKAGE
        defaultToolShouldBeFound("workPackage.doesNotContain=" + UPDATED_WORK_PACKAGE);
    }

    @Test
    @Transactional
    void getAllToolsByNumIsEqualToSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where num equals to DEFAULT_NUM
        defaultToolShouldBeFound("num.equals=" + DEFAULT_NUM);

        // Get all the toolList where num equals to UPDATED_NUM
        defaultToolShouldNotBeFound("num.equals=" + UPDATED_NUM);
    }

    @Test
    @Transactional
    void getAllToolsByNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where num not equals to DEFAULT_NUM
        defaultToolShouldNotBeFound("num.notEquals=" + DEFAULT_NUM);

        // Get all the toolList where num not equals to UPDATED_NUM
        defaultToolShouldBeFound("num.notEquals=" + UPDATED_NUM);
    }

    @Test
    @Transactional
    void getAllToolsByNumIsInShouldWork() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where num in DEFAULT_NUM or UPDATED_NUM
        defaultToolShouldBeFound("num.in=" + DEFAULT_NUM + "," + UPDATED_NUM);

        // Get all the toolList where num equals to UPDATED_NUM
        defaultToolShouldNotBeFound("num.in=" + UPDATED_NUM);
    }

    @Test
    @Transactional
    void getAllToolsByNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where num is not null
        defaultToolShouldBeFound("num.specified=true");

        // Get all the toolList where num is null
        defaultToolShouldNotBeFound("num.specified=false");
    }

    @Test
    @Transactional
    void getAllToolsByNumContainsSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where num contains DEFAULT_NUM
        defaultToolShouldBeFound("num.contains=" + DEFAULT_NUM);

        // Get all the toolList where num contains UPDATED_NUM
        defaultToolShouldNotBeFound("num.contains=" + UPDATED_NUM);
    }

    @Test
    @Transactional
    void getAllToolsByNumNotContainsSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where num does not contain DEFAULT_NUM
        defaultToolShouldNotBeFound("num.doesNotContain=" + DEFAULT_NUM);

        // Get all the toolList where num does not contain UPDATED_NUM
        defaultToolShouldBeFound("num.doesNotContain=" + UPDATED_NUM);
    }

    @Test
    @Transactional
    void getAllToolsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where name equals to DEFAULT_NAME
        defaultToolShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the toolList where name equals to UPDATED_NAME
        defaultToolShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllToolsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where name not equals to DEFAULT_NAME
        defaultToolShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the toolList where name not equals to UPDATED_NAME
        defaultToolShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllToolsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where name in DEFAULT_NAME or UPDATED_NAME
        defaultToolShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the toolList where name equals to UPDATED_NAME
        defaultToolShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllToolsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where name is not null
        defaultToolShouldBeFound("name.specified=true");

        // Get all the toolList where name is null
        defaultToolShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllToolsByNameContainsSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where name contains DEFAULT_NAME
        defaultToolShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the toolList where name contains UPDATED_NAME
        defaultToolShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllToolsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where name does not contain DEFAULT_NAME
        defaultToolShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the toolList where name does not contain UPDATED_NAME
        defaultToolShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllToolsByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where path equals to DEFAULT_PATH
        defaultToolShouldBeFound("path.equals=" + DEFAULT_PATH);

        // Get all the toolList where path equals to UPDATED_PATH
        defaultToolShouldNotBeFound("path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllToolsByPathIsNotEqualToSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where path not equals to DEFAULT_PATH
        defaultToolShouldNotBeFound("path.notEquals=" + DEFAULT_PATH);

        // Get all the toolList where path not equals to UPDATED_PATH
        defaultToolShouldBeFound("path.notEquals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllToolsByPathIsInShouldWork() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where path in DEFAULT_PATH or UPDATED_PATH
        defaultToolShouldBeFound("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH);

        // Get all the toolList where path equals to UPDATED_PATH
        defaultToolShouldNotBeFound("path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllToolsByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where path is not null
        defaultToolShouldBeFound("path.specified=true");

        // Get all the toolList where path is null
        defaultToolShouldNotBeFound("path.specified=false");
    }

    @Test
    @Transactional
    void getAllToolsByPathContainsSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where path contains DEFAULT_PATH
        defaultToolShouldBeFound("path.contains=" + DEFAULT_PATH);

        // Get all the toolList where path contains UPDATED_PATH
        defaultToolShouldNotBeFound("path.contains=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllToolsByPathNotContainsSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where path does not contain DEFAULT_PATH
        defaultToolShouldNotBeFound("path.doesNotContain=" + DEFAULT_PATH);

        // Get all the toolList where path does not contain UPDATED_PATH
        defaultToolShouldBeFound("path.doesNotContain=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllToolsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where description equals to DEFAULT_DESCRIPTION
        defaultToolShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the toolList where description equals to UPDATED_DESCRIPTION
        defaultToolShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllToolsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where description not equals to DEFAULT_DESCRIPTION
        defaultToolShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the toolList where description not equals to UPDATED_DESCRIPTION
        defaultToolShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllToolsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultToolShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the toolList where description equals to UPDATED_DESCRIPTION
        defaultToolShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllToolsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where description is not null
        defaultToolShouldBeFound("description.specified=true");

        // Get all the toolList where description is null
        defaultToolShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllToolsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where description contains DEFAULT_DESCRIPTION
        defaultToolShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the toolList where description contains UPDATED_DESCRIPTION
        defaultToolShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllToolsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where description does not contain DEFAULT_DESCRIPTION
        defaultToolShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the toolList where description does not contain UPDATED_DESCRIPTION
        defaultToolShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllToolsByInputFileIsEqualToSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);
        InputFile inputFile;
        if (TestUtil.findAll(em, InputFile.class).isEmpty()) {
            inputFile = InputFileResourceIT.createEntity(em);
            em.persist(inputFile);
            em.flush();
        } else {
            inputFile = TestUtil.findAll(em, InputFile.class).get(0);
        }
        em.persist(inputFile);
        em.flush();
        tool.addInputFile(inputFile);
        toolRepository.saveAndFlush(tool);
        Long inputFileId = inputFile.getId();

        // Get all the toolList where inputFile equals to inputFileId
        defaultToolShouldBeFound("inputFileId.equals=" + inputFileId);

        // Get all the toolList where inputFile equals to (inputFileId + 1)
        defaultToolShouldNotBeFound("inputFileId.equals=" + (inputFileId + 1));
    }

    @Test
    @Transactional
    void getAllToolsByOutputFileIsEqualToSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);
        OutputFile outputFile;
        if (TestUtil.findAll(em, OutputFile.class).isEmpty()) {
            outputFile = OutputFileResourceIT.createEntity(em);
            em.persist(outputFile);
            em.flush();
        } else {
            outputFile = TestUtil.findAll(em, OutputFile.class).get(0);
        }
        em.persist(outputFile);
        em.flush();
        tool.addOutputFile(outputFile);
        toolRepository.saveAndFlush(tool);
        Long outputFileId = outputFile.getId();

        // Get all the toolList where outputFile equals to outputFileId
        defaultToolShouldBeFound("outputFileId.equals=" + outputFileId);

        // Get all the toolList where outputFile equals to (outputFileId + 1)
        defaultToolShouldNotBeFound("outputFileId.equals=" + (outputFileId + 1));
    }

    @Test
    @Transactional
    void getAllToolsByTaskIsEqualToSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);
        Task task;
        if (TestUtil.findAll(em, Task.class).isEmpty()) {
            task = TaskResourceIT.createEntity(em);
            em.persist(task);
            em.flush();
        } else {
            task = TestUtil.findAll(em, Task.class).get(0);
        }
        em.persist(task);
        em.flush();
        tool.addTask(task);
        toolRepository.saveAndFlush(tool);
        Long taskId = task.getId();

        // Get all the toolList where task equals to taskId
        defaultToolShouldBeFound("taskId.equals=" + taskId);

        // Get all the toolList where task equals to (taskId + 1)
        defaultToolShouldNotBeFound("taskId.equals=" + (taskId + 1));
    }

    @Test
    @Transactional
    void getAllToolsByParameterIsEqualToSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);
        ToolParameter parameter;
        if (TestUtil.findAll(em, ToolParameter.class).isEmpty()) {
            parameter = ToolParameterResourceIT.createEntity(em);
            em.persist(parameter);
            em.flush();
        } else {
            parameter = TestUtil.findAll(em, ToolParameter.class).get(0);
        }
        em.persist(parameter);
        em.flush();
        tool.addParameter(parameter);
        toolRepository.saveAndFlush(tool);
        Long parameterId = parameter.getId();

        // Get all the toolList where parameter equals to parameterId
        defaultToolShouldBeFound("parameterId.equals=" + parameterId);

        // Get all the toolList where parameter equals to (parameterId + 1)
        defaultToolShouldNotBeFound("parameterId.equals=" + (parameterId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultToolShouldBeFound(String filter) throws Exception {
        restToolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tool.getId().intValue())))
            .andExpect(jsonPath("$.[*].workPackage").value(hasItem(DEFAULT_WORK_PACKAGE)))
            .andExpect(jsonPath("$.[*].num").value(hasItem(DEFAULT_NUM)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restToolMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultToolShouldNotBeFound(String filter) throws Exception {
        restToolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restToolMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTool() throws Exception {
        // Get the tool
        restToolMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTool() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        int databaseSizeBeforeUpdate = toolRepository.findAll().size();

        // Update the tool
        Tool updatedTool = toolRepository.findById(tool.getId()).get();
        // Disconnect from session so that the updates on updatedTool are not directly saved in db
        em.detach(updatedTool);
        updatedTool
            .workPackage(UPDATED_WORK_PACKAGE)
            .num(UPDATED_NUM)
            .name(UPDATED_NAME)
            .path(UPDATED_PATH)
            .description(UPDATED_DESCRIPTION);
        ToolDTO toolDTO = toolMapper.toDto(updatedTool);

        restToolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, toolDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tool in the database
        List<Tool> toolList = toolRepository.findAll();
        assertThat(toolList).hasSize(databaseSizeBeforeUpdate);
        Tool testTool = toolList.get(toolList.size() - 1);
        assertThat(testTool.getWorkPackage()).isEqualTo(UPDATED_WORK_PACKAGE);
        assertThat(testTool.getNum()).isEqualTo(UPDATED_NUM);
        assertThat(testTool.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTool.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testTool.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingTool() throws Exception {
        int databaseSizeBeforeUpdate = toolRepository.findAll().size();
        tool.setId(count.incrementAndGet());

        // Create the Tool
        ToolDTO toolDTO = toolMapper.toDto(tool);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restToolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, toolDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tool in the database
        List<Tool> toolList = toolRepository.findAll();
        assertThat(toolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTool() throws Exception {
        int databaseSizeBeforeUpdate = toolRepository.findAll().size();
        tool.setId(count.incrementAndGet());

        // Create the Tool
        ToolDTO toolDTO = toolMapper.toDto(tool);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tool in the database
        List<Tool> toolList = toolRepository.findAll();
        assertThat(toolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTool() throws Exception {
        int databaseSizeBeforeUpdate = toolRepository.findAll().size();
        tool.setId(count.incrementAndGet());

        // Create the Tool
        ToolDTO toolDTO = toolMapper.toDto(tool);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToolMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(toolDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tool in the database
        List<Tool> toolList = toolRepository.findAll();
        assertThat(toolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateToolWithPatch() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        int databaseSizeBeforeUpdate = toolRepository.findAll().size();

        // Update the tool using partial update
        Tool partialUpdatedTool = new Tool();
        partialUpdatedTool.setId(tool.getId());

        partialUpdatedTool.workPackage(UPDATED_WORK_PACKAGE).name(UPDATED_NAME);

        restToolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTool.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTool))
            )
            .andExpect(status().isOk());

        // Validate the Tool in the database
        List<Tool> toolList = toolRepository.findAll();
        assertThat(toolList).hasSize(databaseSizeBeforeUpdate);
        Tool testTool = toolList.get(toolList.size() - 1);
        assertThat(testTool.getWorkPackage()).isEqualTo(UPDATED_WORK_PACKAGE);
        assertThat(testTool.getNum()).isEqualTo(DEFAULT_NUM);
        assertThat(testTool.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTool.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testTool.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateToolWithPatch() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        int databaseSizeBeforeUpdate = toolRepository.findAll().size();

        // Update the tool using partial update
        Tool partialUpdatedTool = new Tool();
        partialUpdatedTool.setId(tool.getId());

        partialUpdatedTool
            .workPackage(UPDATED_WORK_PACKAGE)
            .num(UPDATED_NUM)
            .name(UPDATED_NAME)
            .path(UPDATED_PATH)
            .description(UPDATED_DESCRIPTION);

        restToolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTool.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTool))
            )
            .andExpect(status().isOk());

        // Validate the Tool in the database
        List<Tool> toolList = toolRepository.findAll();
        assertThat(toolList).hasSize(databaseSizeBeforeUpdate);
        Tool testTool = toolList.get(toolList.size() - 1);
        assertThat(testTool.getWorkPackage()).isEqualTo(UPDATED_WORK_PACKAGE);
        assertThat(testTool.getNum()).isEqualTo(UPDATED_NUM);
        assertThat(testTool.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTool.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testTool.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingTool() throws Exception {
        int databaseSizeBeforeUpdate = toolRepository.findAll().size();
        tool.setId(count.incrementAndGet());

        // Create the Tool
        ToolDTO toolDTO = toolMapper.toDto(tool);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restToolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, toolDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(toolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tool in the database
        List<Tool> toolList = toolRepository.findAll();
        assertThat(toolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTool() throws Exception {
        int databaseSizeBeforeUpdate = toolRepository.findAll().size();
        tool.setId(count.incrementAndGet());

        // Create the Tool
        ToolDTO toolDTO = toolMapper.toDto(tool);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(toolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tool in the database
        List<Tool> toolList = toolRepository.findAll();
        assertThat(toolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTool() throws Exception {
        int databaseSizeBeforeUpdate = toolRepository.findAll().size();
        tool.setId(count.incrementAndGet());

        // Create the Tool
        ToolDTO toolDTO = toolMapper.toDto(tool);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToolMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(toolDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tool in the database
        List<Tool> toolList = toolRepository.findAll();
        assertThat(toolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTool() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        int databaseSizeBeforeDelete = toolRepository.findAll().size();

        // Delete the tool
        restToolMockMvc
            .perform(delete(ENTITY_API_URL_ID, tool.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tool> toolList = toolRepository.findAll();
        assertThat(toolList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
