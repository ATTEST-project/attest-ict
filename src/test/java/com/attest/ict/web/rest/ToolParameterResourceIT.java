package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Tool;
import com.attest.ict.domain.ToolParameter;
import com.attest.ict.repository.ToolParameterRepository;
import com.attest.ict.service.criteria.ToolParameterCriteria;
import com.attest.ict.service.dto.ToolParameterDTO;
import com.attest.ict.service.mapper.ToolParameterMapper;
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
 * Integration tests for the {@link ToolParameterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ToolParameterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_VALUE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ENABLED = false;
    private static final Boolean UPDATED_IS_ENABLED = true;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/tool-parameters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ToolParameterRepository toolParameterRepository;

    @Autowired
    private ToolParameterMapper toolParameterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restToolParameterMockMvc;

    private ToolParameter toolParameter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ToolParameter createEntity(EntityManager em) {
        ToolParameter toolParameter = new ToolParameter()
            .name(DEFAULT_NAME)
            .defaultValue(DEFAULT_DEFAULT_VALUE)
            .isEnabled(DEFAULT_IS_ENABLED)
            .description(DEFAULT_DESCRIPTION)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        return toolParameter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ToolParameter createUpdatedEntity(EntityManager em) {
        ToolParameter toolParameter = new ToolParameter()
            .name(UPDATED_NAME)
            .defaultValue(UPDATED_DEFAULT_VALUE)
            .isEnabled(UPDATED_IS_ENABLED)
            .description(UPDATED_DESCRIPTION)
            .lastUpdate(UPDATED_LAST_UPDATE);
        return toolParameter;
    }

    @BeforeEach
    public void initTest() {
        toolParameter = createEntity(em);
    }

    @Test
    @Transactional
    void createToolParameter() throws Exception {
        int databaseSizeBeforeCreate = toolParameterRepository.findAll().size();
        // Create the ToolParameter
        ToolParameterDTO toolParameterDTO = toolParameterMapper.toDto(toolParameter);
        restToolParameterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolParameterDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ToolParameter in the database
        List<ToolParameter> toolParameterList = toolParameterRepository.findAll();
        assertThat(toolParameterList).hasSize(databaseSizeBeforeCreate + 1);
        ToolParameter testToolParameter = toolParameterList.get(toolParameterList.size() - 1);
        assertThat(testToolParameter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testToolParameter.getDefaultValue()).isEqualTo(DEFAULT_DEFAULT_VALUE);
        assertThat(testToolParameter.getIsEnabled()).isEqualTo(DEFAULT_IS_ENABLED);
        assertThat(testToolParameter.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testToolParameter.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
    }

    @Test
    @Transactional
    void createToolParameterWithExistingId() throws Exception {
        // Create the ToolParameter with an existing ID
        toolParameter.setId(1L);
        ToolParameterDTO toolParameterDTO = toolParameterMapper.toDto(toolParameter);

        int databaseSizeBeforeCreate = toolParameterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restToolParameterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolParameterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ToolParameter in the database
        List<ToolParameter> toolParameterList = toolParameterRepository.findAll();
        assertThat(toolParameterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = toolParameterRepository.findAll().size();
        // set the field null
        toolParameter.setName(null);

        // Create the ToolParameter, which fails.
        ToolParameterDTO toolParameterDTO = toolParameterMapper.toDto(toolParameter);

        restToolParameterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolParameterDTO))
            )
            .andExpect(status().isBadRequest());

        List<ToolParameter> toolParameterList = toolParameterRepository.findAll();
        assertThat(toolParameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDefaultValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = toolParameterRepository.findAll().size();
        // set the field null
        toolParameter.setDefaultValue(null);

        // Create the ToolParameter, which fails.
        ToolParameterDTO toolParameterDTO = toolParameterMapper.toDto(toolParameter);

        restToolParameterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolParameterDTO))
            )
            .andExpect(status().isBadRequest());

        List<ToolParameter> toolParameterList = toolParameterRepository.findAll();
        assertThat(toolParameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = toolParameterRepository.findAll().size();
        // set the field null
        toolParameter.setIsEnabled(null);

        // Create the ToolParameter, which fails.
        ToolParameterDTO toolParameterDTO = toolParameterMapper.toDto(toolParameter);

        restToolParameterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolParameterDTO))
            )
            .andExpect(status().isBadRequest());

        List<ToolParameter> toolParameterList = toolParameterRepository.findAll();
        assertThat(toolParameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllToolParameters() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList
        restToolParameterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(toolParameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].defaultValue").value(hasItem(DEFAULT_DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }

    @Test
    @Transactional
    void getToolParameter() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get the toolParameter
        restToolParameterMockMvc
            .perform(get(ENTITY_API_URL_ID, toolParameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(toolParameter.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.defaultValue").value(DEFAULT_DEFAULT_VALUE))
            .andExpect(jsonPath("$.isEnabled").value(DEFAULT_IS_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()));
    }

    @Test
    @Transactional
    void getToolParametersByIdFiltering() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        Long id = toolParameter.getId();

        defaultToolParameterShouldBeFound("id.equals=" + id);
        defaultToolParameterShouldNotBeFound("id.notEquals=" + id);

        defaultToolParameterShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultToolParameterShouldNotBeFound("id.greaterThan=" + id);

        defaultToolParameterShouldBeFound("id.lessThanOrEqual=" + id);
        defaultToolParameterShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllToolParametersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where name equals to DEFAULT_NAME
        defaultToolParameterShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the toolParameterList where name equals to UPDATED_NAME
        defaultToolParameterShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllToolParametersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where name not equals to DEFAULT_NAME
        defaultToolParameterShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the toolParameterList where name not equals to UPDATED_NAME
        defaultToolParameterShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllToolParametersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where name in DEFAULT_NAME or UPDATED_NAME
        defaultToolParameterShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the toolParameterList where name equals to UPDATED_NAME
        defaultToolParameterShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllToolParametersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where name is not null
        defaultToolParameterShouldBeFound("name.specified=true");

        // Get all the toolParameterList where name is null
        defaultToolParameterShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllToolParametersByNameContainsSomething() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where name contains DEFAULT_NAME
        defaultToolParameterShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the toolParameterList where name contains UPDATED_NAME
        defaultToolParameterShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllToolParametersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where name does not contain DEFAULT_NAME
        defaultToolParameterShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the toolParameterList where name does not contain UPDATED_NAME
        defaultToolParameterShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllToolParametersByDefaultValueIsEqualToSomething() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where defaultValue equals to DEFAULT_DEFAULT_VALUE
        defaultToolParameterShouldBeFound("defaultValue.equals=" + DEFAULT_DEFAULT_VALUE);

        // Get all the toolParameterList where defaultValue equals to UPDATED_DEFAULT_VALUE
        defaultToolParameterShouldNotBeFound("defaultValue.equals=" + UPDATED_DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void getAllToolParametersByDefaultValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where defaultValue not equals to DEFAULT_DEFAULT_VALUE
        defaultToolParameterShouldNotBeFound("defaultValue.notEquals=" + DEFAULT_DEFAULT_VALUE);

        // Get all the toolParameterList where defaultValue not equals to UPDATED_DEFAULT_VALUE
        defaultToolParameterShouldBeFound("defaultValue.notEquals=" + UPDATED_DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void getAllToolParametersByDefaultValueIsInShouldWork() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where defaultValue in DEFAULT_DEFAULT_VALUE or UPDATED_DEFAULT_VALUE
        defaultToolParameterShouldBeFound("defaultValue.in=" + DEFAULT_DEFAULT_VALUE + "," + UPDATED_DEFAULT_VALUE);

        // Get all the toolParameterList where defaultValue equals to UPDATED_DEFAULT_VALUE
        defaultToolParameterShouldNotBeFound("defaultValue.in=" + UPDATED_DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void getAllToolParametersByDefaultValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where defaultValue is not null
        defaultToolParameterShouldBeFound("defaultValue.specified=true");

        // Get all the toolParameterList where defaultValue is null
        defaultToolParameterShouldNotBeFound("defaultValue.specified=false");
    }

    @Test
    @Transactional
    void getAllToolParametersByDefaultValueContainsSomething() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where defaultValue contains DEFAULT_DEFAULT_VALUE
        defaultToolParameterShouldBeFound("defaultValue.contains=" + DEFAULT_DEFAULT_VALUE);

        // Get all the toolParameterList where defaultValue contains UPDATED_DEFAULT_VALUE
        defaultToolParameterShouldNotBeFound("defaultValue.contains=" + UPDATED_DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void getAllToolParametersByDefaultValueNotContainsSomething() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where defaultValue does not contain DEFAULT_DEFAULT_VALUE
        defaultToolParameterShouldNotBeFound("defaultValue.doesNotContain=" + DEFAULT_DEFAULT_VALUE);

        // Get all the toolParameterList where defaultValue does not contain UPDATED_DEFAULT_VALUE
        defaultToolParameterShouldBeFound("defaultValue.doesNotContain=" + UPDATED_DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void getAllToolParametersByIsEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where isEnabled equals to DEFAULT_IS_ENABLED
        defaultToolParameterShouldBeFound("isEnabled.equals=" + DEFAULT_IS_ENABLED);

        // Get all the toolParameterList where isEnabled equals to UPDATED_IS_ENABLED
        defaultToolParameterShouldNotBeFound("isEnabled.equals=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllToolParametersByIsEnabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where isEnabled not equals to DEFAULT_IS_ENABLED
        defaultToolParameterShouldNotBeFound("isEnabled.notEquals=" + DEFAULT_IS_ENABLED);

        // Get all the toolParameterList where isEnabled not equals to UPDATED_IS_ENABLED
        defaultToolParameterShouldBeFound("isEnabled.notEquals=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllToolParametersByIsEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where isEnabled in DEFAULT_IS_ENABLED or UPDATED_IS_ENABLED
        defaultToolParameterShouldBeFound("isEnabled.in=" + DEFAULT_IS_ENABLED + "," + UPDATED_IS_ENABLED);

        // Get all the toolParameterList where isEnabled equals to UPDATED_IS_ENABLED
        defaultToolParameterShouldNotBeFound("isEnabled.in=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllToolParametersByIsEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where isEnabled is not null
        defaultToolParameterShouldBeFound("isEnabled.specified=true");

        // Get all the toolParameterList where isEnabled is null
        defaultToolParameterShouldNotBeFound("isEnabled.specified=false");
    }

    @Test
    @Transactional
    void getAllToolParametersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where description equals to DEFAULT_DESCRIPTION
        defaultToolParameterShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the toolParameterList where description equals to UPDATED_DESCRIPTION
        defaultToolParameterShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllToolParametersByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where description not equals to DEFAULT_DESCRIPTION
        defaultToolParameterShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the toolParameterList where description not equals to UPDATED_DESCRIPTION
        defaultToolParameterShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllToolParametersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultToolParameterShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the toolParameterList where description equals to UPDATED_DESCRIPTION
        defaultToolParameterShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllToolParametersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where description is not null
        defaultToolParameterShouldBeFound("description.specified=true");

        // Get all the toolParameterList where description is null
        defaultToolParameterShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllToolParametersByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where description contains DEFAULT_DESCRIPTION
        defaultToolParameterShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the toolParameterList where description contains UPDATED_DESCRIPTION
        defaultToolParameterShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllToolParametersByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where description does not contain DEFAULT_DESCRIPTION
        defaultToolParameterShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the toolParameterList where description does not contain UPDATED_DESCRIPTION
        defaultToolParameterShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllToolParametersByLastUpdateIsEqualToSomething() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where lastUpdate equals to DEFAULT_LAST_UPDATE
        defaultToolParameterShouldBeFound("lastUpdate.equals=" + DEFAULT_LAST_UPDATE);

        // Get all the toolParameterList where lastUpdate equals to UPDATED_LAST_UPDATE
        defaultToolParameterShouldNotBeFound("lastUpdate.equals=" + UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    void getAllToolParametersByLastUpdateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where lastUpdate not equals to DEFAULT_LAST_UPDATE
        defaultToolParameterShouldNotBeFound("lastUpdate.notEquals=" + DEFAULT_LAST_UPDATE);

        // Get all the toolParameterList where lastUpdate not equals to UPDATED_LAST_UPDATE
        defaultToolParameterShouldBeFound("lastUpdate.notEquals=" + UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    void getAllToolParametersByLastUpdateIsInShouldWork() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where lastUpdate in DEFAULT_LAST_UPDATE or UPDATED_LAST_UPDATE
        defaultToolParameterShouldBeFound("lastUpdate.in=" + DEFAULT_LAST_UPDATE + "," + UPDATED_LAST_UPDATE);

        // Get all the toolParameterList where lastUpdate equals to UPDATED_LAST_UPDATE
        defaultToolParameterShouldNotBeFound("lastUpdate.in=" + UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    void getAllToolParametersByLastUpdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        // Get all the toolParameterList where lastUpdate is not null
        defaultToolParameterShouldBeFound("lastUpdate.specified=true");

        // Get all the toolParameterList where lastUpdate is null
        defaultToolParameterShouldNotBeFound("lastUpdate.specified=false");
    }

    @Test
    @Transactional
    void getAllToolParametersByToolIsEqualToSomething() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);
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
        toolParameter.setTool(tool);
        toolParameterRepository.saveAndFlush(toolParameter);
        Long toolId = tool.getId();

        // Get all the toolParameterList where tool equals to toolId
        defaultToolParameterShouldBeFound("toolId.equals=" + toolId);

        // Get all the toolParameterList where tool equals to (toolId + 1)
        defaultToolParameterShouldNotBeFound("toolId.equals=" + (toolId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultToolParameterShouldBeFound(String filter) throws Exception {
        restToolParameterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(toolParameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].defaultValue").value(hasItem(DEFAULT_DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));

        // Check, that the count call also returns 1
        restToolParameterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultToolParameterShouldNotBeFound(String filter) throws Exception {
        restToolParameterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restToolParameterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingToolParameter() throws Exception {
        // Get the toolParameter
        restToolParameterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewToolParameter() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        int databaseSizeBeforeUpdate = toolParameterRepository.findAll().size();

        // Update the toolParameter
        ToolParameter updatedToolParameter = toolParameterRepository.findById(toolParameter.getId()).get();
        // Disconnect from session so that the updates on updatedToolParameter are not directly saved in db
        em.detach(updatedToolParameter);
        updatedToolParameter
            .name(UPDATED_NAME)
            .defaultValue(UPDATED_DEFAULT_VALUE)
            .isEnabled(UPDATED_IS_ENABLED)
            .description(UPDATED_DESCRIPTION)
            .lastUpdate(UPDATED_LAST_UPDATE);
        ToolParameterDTO toolParameterDTO = toolParameterMapper.toDto(updatedToolParameter);

        restToolParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, toolParameterDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolParameterDTO))
            )
            .andExpect(status().isOk());

        // Validate the ToolParameter in the database
        List<ToolParameter> toolParameterList = toolParameterRepository.findAll();
        assertThat(toolParameterList).hasSize(databaseSizeBeforeUpdate);
        ToolParameter testToolParameter = toolParameterList.get(toolParameterList.size() - 1);
        assertThat(testToolParameter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testToolParameter.getDefaultValue()).isEqualTo(UPDATED_DEFAULT_VALUE);
        assertThat(testToolParameter.getIsEnabled()).isEqualTo(UPDATED_IS_ENABLED);
        assertThat(testToolParameter.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testToolParameter.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    void putNonExistingToolParameter() throws Exception {
        int databaseSizeBeforeUpdate = toolParameterRepository.findAll().size();
        toolParameter.setId(count.incrementAndGet());

        // Create the ToolParameter
        ToolParameterDTO toolParameterDTO = toolParameterMapper.toDto(toolParameter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restToolParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, toolParameterDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolParameterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ToolParameter in the database
        List<ToolParameter> toolParameterList = toolParameterRepository.findAll();
        assertThat(toolParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchToolParameter() throws Exception {
        int databaseSizeBeforeUpdate = toolParameterRepository.findAll().size();
        toolParameter.setId(count.incrementAndGet());

        // Create the ToolParameter
        ToolParameterDTO toolParameterDTO = toolParameterMapper.toDto(toolParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToolParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolParameterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ToolParameter in the database
        List<ToolParameter> toolParameterList = toolParameterRepository.findAll();
        assertThat(toolParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamToolParameter() throws Exception {
        int databaseSizeBeforeUpdate = toolParameterRepository.findAll().size();
        toolParameter.setId(count.incrementAndGet());

        // Create the ToolParameter
        ToolParameterDTO toolParameterDTO = toolParameterMapper.toDto(toolParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToolParameterMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolParameterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ToolParameter in the database
        List<ToolParameter> toolParameterList = toolParameterRepository.findAll();
        assertThat(toolParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateToolParameterWithPatch() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        int databaseSizeBeforeUpdate = toolParameterRepository.findAll().size();

        // Update the toolParameter using partial update
        ToolParameter partialUpdatedToolParameter = new ToolParameter();
        partialUpdatedToolParameter.setId(toolParameter.getId());

        partialUpdatedToolParameter
            .defaultValue(UPDATED_DEFAULT_VALUE)
            .isEnabled(UPDATED_IS_ENABLED)
            .description(UPDATED_DESCRIPTION)
            .lastUpdate(UPDATED_LAST_UPDATE);

        restToolParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedToolParameter.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedToolParameter))
            )
            .andExpect(status().isOk());

        // Validate the ToolParameter in the database
        List<ToolParameter> toolParameterList = toolParameterRepository.findAll();
        assertThat(toolParameterList).hasSize(databaseSizeBeforeUpdate);
        ToolParameter testToolParameter = toolParameterList.get(toolParameterList.size() - 1);
        assertThat(testToolParameter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testToolParameter.getDefaultValue()).isEqualTo(UPDATED_DEFAULT_VALUE);
        assertThat(testToolParameter.getIsEnabled()).isEqualTo(UPDATED_IS_ENABLED);
        assertThat(testToolParameter.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testToolParameter.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    void fullUpdateToolParameterWithPatch() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        int databaseSizeBeforeUpdate = toolParameterRepository.findAll().size();

        // Update the toolParameter using partial update
        ToolParameter partialUpdatedToolParameter = new ToolParameter();
        partialUpdatedToolParameter.setId(toolParameter.getId());

        partialUpdatedToolParameter
            .name(UPDATED_NAME)
            .defaultValue(UPDATED_DEFAULT_VALUE)
            .isEnabled(UPDATED_IS_ENABLED)
            .description(UPDATED_DESCRIPTION)
            .lastUpdate(UPDATED_LAST_UPDATE);

        restToolParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedToolParameter.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedToolParameter))
            )
            .andExpect(status().isOk());

        // Validate the ToolParameter in the database
        List<ToolParameter> toolParameterList = toolParameterRepository.findAll();
        assertThat(toolParameterList).hasSize(databaseSizeBeforeUpdate);
        ToolParameter testToolParameter = toolParameterList.get(toolParameterList.size() - 1);
        assertThat(testToolParameter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testToolParameter.getDefaultValue()).isEqualTo(UPDATED_DEFAULT_VALUE);
        assertThat(testToolParameter.getIsEnabled()).isEqualTo(UPDATED_IS_ENABLED);
        assertThat(testToolParameter.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testToolParameter.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    void patchNonExistingToolParameter() throws Exception {
        int databaseSizeBeforeUpdate = toolParameterRepository.findAll().size();
        toolParameter.setId(count.incrementAndGet());

        // Create the ToolParameter
        ToolParameterDTO toolParameterDTO = toolParameterMapper.toDto(toolParameter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restToolParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, toolParameterDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(toolParameterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ToolParameter in the database
        List<ToolParameter> toolParameterList = toolParameterRepository.findAll();
        assertThat(toolParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchToolParameter() throws Exception {
        int databaseSizeBeforeUpdate = toolParameterRepository.findAll().size();
        toolParameter.setId(count.incrementAndGet());

        // Create the ToolParameter
        ToolParameterDTO toolParameterDTO = toolParameterMapper.toDto(toolParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToolParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(toolParameterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ToolParameter in the database
        List<ToolParameter> toolParameterList = toolParameterRepository.findAll();
        assertThat(toolParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamToolParameter() throws Exception {
        int databaseSizeBeforeUpdate = toolParameterRepository.findAll().size();
        toolParameter.setId(count.incrementAndGet());

        // Create the ToolParameter
        ToolParameterDTO toolParameterDTO = toolParameterMapper.toDto(toolParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToolParameterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(toolParameterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ToolParameter in the database
        List<ToolParameter> toolParameterList = toolParameterRepository.findAll();
        assertThat(toolParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteToolParameter() throws Exception {
        // Initialize the database
        toolParameterRepository.saveAndFlush(toolParameter);

        int databaseSizeBeforeDelete = toolParameterRepository.findAll().size();

        // Delete the toolParameter
        restToolParameterMockMvc
            .perform(delete(ENTITY_API_URL_ID, toolParameter.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ToolParameter> toolParameterList = toolParameterRepository.findAll();
        assertThat(toolParameterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
