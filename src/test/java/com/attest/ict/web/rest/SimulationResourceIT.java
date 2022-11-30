package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.InputFile;
import com.attest.ict.domain.Network;
import com.attest.ict.domain.OutputFile;
import com.attest.ict.domain.Simulation;
import com.attest.ict.domain.Task;
import com.attest.ict.repository.SimulationRepository;
import com.attest.ict.service.SimulationService;
import com.attest.ict.service.criteria.SimulationCriteria;
import com.attest.ict.service.dto.SimulationDTO;
import com.attest.ict.service.mapper.SimulationMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link SimulationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SimulationResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONFIG_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONFIG_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONFIG_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONFIG_FILE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/simulations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SimulationRepository simulationRepository;

    @Mock
    private SimulationRepository simulationRepositoryMock;

    @Autowired
    private SimulationMapper simulationMapper;

    @Mock
    private SimulationService simulationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSimulationMockMvc;

    private Simulation simulation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Simulation createEntity(EntityManager em) {
        Simulation simulation = new Simulation()
            .uuid(DEFAULT_UUID)
            .description(DEFAULT_DESCRIPTION)
            .configFile(DEFAULT_CONFIG_FILE)
            .configFileContentType(DEFAULT_CONFIG_FILE_CONTENT_TYPE);
        return simulation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Simulation createUpdatedEntity(EntityManager em) {
        Simulation simulation = new Simulation()
            .uuid(UPDATED_UUID)
            .description(UPDATED_DESCRIPTION)
            .configFile(UPDATED_CONFIG_FILE)
            .configFileContentType(UPDATED_CONFIG_FILE_CONTENT_TYPE);
        return simulation;
    }

    @BeforeEach
    public void initTest() {
        simulation = createEntity(em);
    }

    @Test
    @Transactional
    void createSimulation() throws Exception {
        int databaseSizeBeforeCreate = simulationRepository.findAll().size();
        // Create the Simulation
        SimulationDTO simulationDTO = simulationMapper.toDto(simulation);
        restSimulationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(simulationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Simulation in the database
        List<Simulation> simulationList = simulationRepository.findAll();
        assertThat(simulationList).hasSize(databaseSizeBeforeCreate + 1);
        Simulation testSimulation = simulationList.get(simulationList.size() - 1);
        assertThat(testSimulation.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testSimulation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSimulation.getConfigFile()).isEqualTo(DEFAULT_CONFIG_FILE);
        assertThat(testSimulation.getConfigFileContentType()).isEqualTo(DEFAULT_CONFIG_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createSimulationWithExistingId() throws Exception {
        // Create the Simulation with an existing ID
        simulation.setId(1L);
        SimulationDTO simulationDTO = simulationMapper.toDto(simulation);

        int databaseSizeBeforeCreate = simulationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSimulationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(simulationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Simulation in the database
        List<Simulation> simulationList = simulationRepository.findAll();
        assertThat(simulationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = simulationRepository.findAll().size();
        // set the field null
        simulation.setUuid(null);

        // Create the Simulation, which fails.
        SimulationDTO simulationDTO = simulationMapper.toDto(simulation);

        restSimulationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(simulationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Simulation> simulationList = simulationRepository.findAll();
        assertThat(simulationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSimulations() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);

        // Get all the simulationList
        restSimulationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(simulation.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].configFileContentType").value(hasItem(DEFAULT_CONFIG_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].configFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONFIG_FILE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSimulationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(simulationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSimulationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(simulationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSimulationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(simulationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSimulationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(simulationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSimulation() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);

        // Get the simulation
        restSimulationMockMvc
            .perform(get(ENTITY_API_URL_ID, simulation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(simulation.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.configFileContentType").value(DEFAULT_CONFIG_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.configFile").value(Base64Utils.encodeToString(DEFAULT_CONFIG_FILE)));
    }

    @Test
    @Transactional
    void getSimulationsByIdFiltering() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);

        Long id = simulation.getId();

        defaultSimulationShouldBeFound("id.equals=" + id);
        defaultSimulationShouldNotBeFound("id.notEquals=" + id);

        defaultSimulationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSimulationShouldNotBeFound("id.greaterThan=" + id);

        defaultSimulationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSimulationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSimulationsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);

        // Get all the simulationList where uuid equals to DEFAULT_UUID
        defaultSimulationShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the simulationList where uuid equals to UPDATED_UUID
        defaultSimulationShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllSimulationsByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);

        // Get all the simulationList where uuid not equals to DEFAULT_UUID
        defaultSimulationShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the simulationList where uuid not equals to UPDATED_UUID
        defaultSimulationShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllSimulationsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);

        // Get all the simulationList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultSimulationShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the simulationList where uuid equals to UPDATED_UUID
        defaultSimulationShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllSimulationsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);

        // Get all the simulationList where uuid is not null
        defaultSimulationShouldBeFound("uuid.specified=true");

        // Get all the simulationList where uuid is null
        defaultSimulationShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllSimulationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);

        // Get all the simulationList where description equals to DEFAULT_DESCRIPTION
        defaultSimulationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the simulationList where description equals to UPDATED_DESCRIPTION
        defaultSimulationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSimulationsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);

        // Get all the simulationList where description not equals to DEFAULT_DESCRIPTION
        defaultSimulationShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the simulationList where description not equals to UPDATED_DESCRIPTION
        defaultSimulationShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSimulationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);

        // Get all the simulationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSimulationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the simulationList where description equals to UPDATED_DESCRIPTION
        defaultSimulationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSimulationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);

        // Get all the simulationList where description is not null
        defaultSimulationShouldBeFound("description.specified=true");

        // Get all the simulationList where description is null
        defaultSimulationShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllSimulationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);

        // Get all the simulationList where description contains DEFAULT_DESCRIPTION
        defaultSimulationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the simulationList where description contains UPDATED_DESCRIPTION
        defaultSimulationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSimulationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);

        // Get all the simulationList where description does not contain DEFAULT_DESCRIPTION
        defaultSimulationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the simulationList where description does not contain UPDATED_DESCRIPTION
        defaultSimulationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSimulationsByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);
        Network network;
        if (TestUtil.findAll(em, Network.class).isEmpty()) {
            network = NetworkResourceIT.createEntity(em);
            em.persist(network);
            em.flush();
        } else {
            network = TestUtil.findAll(em, Network.class).get(0);
        }
        em.persist(network);
        em.flush();
        simulation.setNetwork(network);
        simulationRepository.saveAndFlush(simulation);
        Long networkId = network.getId();

        // Get all the simulationList where network equals to networkId
        defaultSimulationShouldBeFound("networkId.equals=" + networkId);

        // Get all the simulationList where network equals to (networkId + 1)
        defaultSimulationShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    @Test
    @Transactional
    void getAllSimulationsByInputFileIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);
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
        simulation.addInputFile(inputFile);
        simulationRepository.saveAndFlush(simulation);
        Long inputFileId = inputFile.getId();

        // Get all the simulationList where inputFile equals to inputFileId
        defaultSimulationShouldBeFound("inputFileId.equals=" + inputFileId);

        // Get all the simulationList where inputFile equals to (inputFileId + 1)
        defaultSimulationShouldNotBeFound("inputFileId.equals=" + (inputFileId + 1));
    }

    @Test
    @Transactional
    void getAllSimulationsByTaskIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);
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
        simulation.setTask(task);
        task.setSimulation(simulation);
        simulationRepository.saveAndFlush(simulation);
        Long taskId = task.getId();

        // Get all the simulationList where task equals to taskId
        defaultSimulationShouldBeFound("taskId.equals=" + taskId);

        // Get all the simulationList where task equals to (taskId + 1)
        defaultSimulationShouldNotBeFound("taskId.equals=" + (taskId + 1));
    }

    @Test
    @Transactional
    void getAllSimulationsByOutputFileIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);
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
        simulation.addOutputFile(outputFile);
        simulationRepository.saveAndFlush(simulation);
        Long outputFileId = outputFile.getId();

        // Get all the simulationList where outputFile equals to outputFileId
        defaultSimulationShouldBeFound("outputFileId.equals=" + outputFileId);

        // Get all the simulationList where outputFile equals to (outputFileId + 1)
        defaultSimulationShouldNotBeFound("outputFileId.equals=" + (outputFileId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSimulationShouldBeFound(String filter) throws Exception {
        restSimulationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(simulation.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].configFileContentType").value(hasItem(DEFAULT_CONFIG_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].configFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONFIG_FILE))));

        // Check, that the count call also returns 1
        restSimulationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSimulationShouldNotBeFound(String filter) throws Exception {
        restSimulationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSimulationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSimulation() throws Exception {
        // Get the simulation
        restSimulationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSimulation() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);

        int databaseSizeBeforeUpdate = simulationRepository.findAll().size();

        // Update the simulation
        Simulation updatedSimulation = simulationRepository.findById(simulation.getId()).get();
        // Disconnect from session so that the updates on updatedSimulation are not directly saved in db
        em.detach(updatedSimulation);
        updatedSimulation
            .uuid(UPDATED_UUID)
            .description(UPDATED_DESCRIPTION)
            .configFile(UPDATED_CONFIG_FILE)
            .configFileContentType(UPDATED_CONFIG_FILE_CONTENT_TYPE);
        SimulationDTO simulationDTO = simulationMapper.toDto(updatedSimulation);

        restSimulationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, simulationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(simulationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Simulation in the database
        List<Simulation> simulationList = simulationRepository.findAll();
        assertThat(simulationList).hasSize(databaseSizeBeforeUpdate);
        Simulation testSimulation = simulationList.get(simulationList.size() - 1);
        assertThat(testSimulation.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testSimulation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSimulation.getConfigFile()).isEqualTo(UPDATED_CONFIG_FILE);
        assertThat(testSimulation.getConfigFileContentType()).isEqualTo(UPDATED_CONFIG_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingSimulation() throws Exception {
        int databaseSizeBeforeUpdate = simulationRepository.findAll().size();
        simulation.setId(count.incrementAndGet());

        // Create the Simulation
        SimulationDTO simulationDTO = simulationMapper.toDto(simulation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSimulationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, simulationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(simulationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Simulation in the database
        List<Simulation> simulationList = simulationRepository.findAll();
        assertThat(simulationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSimulation() throws Exception {
        int databaseSizeBeforeUpdate = simulationRepository.findAll().size();
        simulation.setId(count.incrementAndGet());

        // Create the Simulation
        SimulationDTO simulationDTO = simulationMapper.toDto(simulation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimulationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(simulationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Simulation in the database
        List<Simulation> simulationList = simulationRepository.findAll();
        assertThat(simulationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSimulation() throws Exception {
        int databaseSizeBeforeUpdate = simulationRepository.findAll().size();
        simulation.setId(count.incrementAndGet());

        // Create the Simulation
        SimulationDTO simulationDTO = simulationMapper.toDto(simulation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimulationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(simulationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Simulation in the database
        List<Simulation> simulationList = simulationRepository.findAll();
        assertThat(simulationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSimulationWithPatch() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);

        int databaseSizeBeforeUpdate = simulationRepository.findAll().size();

        // Update the simulation using partial update
        Simulation partialUpdatedSimulation = new Simulation();
        partialUpdatedSimulation.setId(simulation.getId());

        partialUpdatedSimulation
            .description(UPDATED_DESCRIPTION)
            .configFile(UPDATED_CONFIG_FILE)
            .configFileContentType(UPDATED_CONFIG_FILE_CONTENT_TYPE);

        restSimulationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSimulation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSimulation))
            )
            .andExpect(status().isOk());

        // Validate the Simulation in the database
        List<Simulation> simulationList = simulationRepository.findAll();
        assertThat(simulationList).hasSize(databaseSizeBeforeUpdate);
        Simulation testSimulation = simulationList.get(simulationList.size() - 1);
        assertThat(testSimulation.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testSimulation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSimulation.getConfigFile()).isEqualTo(UPDATED_CONFIG_FILE);
        assertThat(testSimulation.getConfigFileContentType()).isEqualTo(UPDATED_CONFIG_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateSimulationWithPatch() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);

        int databaseSizeBeforeUpdate = simulationRepository.findAll().size();

        // Update the simulation using partial update
        Simulation partialUpdatedSimulation = new Simulation();
        partialUpdatedSimulation.setId(simulation.getId());

        partialUpdatedSimulation
            .uuid(UPDATED_UUID)
            .description(UPDATED_DESCRIPTION)
            .configFile(UPDATED_CONFIG_FILE)
            .configFileContentType(UPDATED_CONFIG_FILE_CONTENT_TYPE);

        restSimulationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSimulation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSimulation))
            )
            .andExpect(status().isOk());

        // Validate the Simulation in the database
        List<Simulation> simulationList = simulationRepository.findAll();
        assertThat(simulationList).hasSize(databaseSizeBeforeUpdate);
        Simulation testSimulation = simulationList.get(simulationList.size() - 1);
        assertThat(testSimulation.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testSimulation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSimulation.getConfigFile()).isEqualTo(UPDATED_CONFIG_FILE);
        assertThat(testSimulation.getConfigFileContentType()).isEqualTo(UPDATED_CONFIG_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingSimulation() throws Exception {
        int databaseSizeBeforeUpdate = simulationRepository.findAll().size();
        simulation.setId(count.incrementAndGet());

        // Create the Simulation
        SimulationDTO simulationDTO = simulationMapper.toDto(simulation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSimulationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, simulationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(simulationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Simulation in the database
        List<Simulation> simulationList = simulationRepository.findAll();
        assertThat(simulationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSimulation() throws Exception {
        int databaseSizeBeforeUpdate = simulationRepository.findAll().size();
        simulation.setId(count.incrementAndGet());

        // Create the Simulation
        SimulationDTO simulationDTO = simulationMapper.toDto(simulation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimulationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(simulationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Simulation in the database
        List<Simulation> simulationList = simulationRepository.findAll();
        assertThat(simulationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSimulation() throws Exception {
        int databaseSizeBeforeUpdate = simulationRepository.findAll().size();
        simulation.setId(count.incrementAndGet());

        // Create the Simulation
        SimulationDTO simulationDTO = simulationMapper.toDto(simulation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimulationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(simulationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Simulation in the database
        List<Simulation> simulationList = simulationRepository.findAll();
        assertThat(simulationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSimulation() throws Exception {
        // Initialize the database
        simulationRepository.saveAndFlush(simulation);

        int databaseSizeBeforeDelete = simulationRepository.findAll().size();

        // Delete the simulation
        restSimulationMockMvc
            .perform(delete(ENTITY_API_URL_ID, simulation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Simulation> simulationList = simulationRepository.findAll();
        assertThat(simulationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
