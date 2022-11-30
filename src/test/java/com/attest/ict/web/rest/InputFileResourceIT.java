package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.BranchProfile;
import com.attest.ict.domain.FlexProfile;
import com.attest.ict.domain.GenProfile;
import com.attest.ict.domain.InputFile;
import com.attest.ict.domain.LoadProfile;
import com.attest.ict.domain.Network;
import com.attest.ict.domain.Simulation;
import com.attest.ict.domain.Tool;
import com.attest.ict.domain.TransfProfile;
import com.attest.ict.repository.InputFileRepository;
import com.attest.ict.service.criteria.InputFileCriteria;
import com.attest.ict.service.dto.InputFileDTO;
import com.attest.ict.service.mapper.InputFileMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link InputFileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InputFileResourceIT {

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DATA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DATA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DATA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DATA_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_UPLOAD_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPLOAD_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/input-files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InputFileRepository inputFileRepository;

    @Autowired
    private InputFileMapper inputFileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInputFileMockMvc;

    private InputFile inputFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InputFile createEntity(EntityManager em) {
        InputFile inputFile = new InputFile()
            .fileName(DEFAULT_FILE_NAME)
            .description(DEFAULT_DESCRIPTION)
            .data(DEFAULT_DATA)
            .dataContentType(DEFAULT_DATA_CONTENT_TYPE)
            .uploadTime(DEFAULT_UPLOAD_TIME);
        return inputFile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InputFile createUpdatedEntity(EntityManager em) {
        InputFile inputFile = new InputFile()
            .fileName(UPDATED_FILE_NAME)
            .description(UPDATED_DESCRIPTION)
            .data(UPDATED_DATA)
            .dataContentType(UPDATED_DATA_CONTENT_TYPE)
            .uploadTime(UPDATED_UPLOAD_TIME);
        return inputFile;
    }

    @BeforeEach
    public void initTest() {
        inputFile = createEntity(em);
    }

    @Test
    @Transactional
    void createInputFile() throws Exception {
        int databaseSizeBeforeCreate = inputFileRepository.findAll().size();
        // Create the InputFile
        InputFileDTO inputFileDTO = inputFileMapper.toDto(inputFile);
        restInputFileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputFileDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InputFile in the database
        List<InputFile> inputFileList = inputFileRepository.findAll();
        assertThat(inputFileList).hasSize(databaseSizeBeforeCreate + 1);
        InputFile testInputFile = inputFileList.get(inputFileList.size() - 1);
        assertThat(testInputFile.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testInputFile.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInputFile.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testInputFile.getDataContentType()).isEqualTo(DEFAULT_DATA_CONTENT_TYPE);
        assertThat(testInputFile.getUploadTime()).isEqualTo(DEFAULT_UPLOAD_TIME);
    }

    @Test
    @Transactional
    void createInputFileWithExistingId() throws Exception {
        // Create the InputFile with an existing ID
        inputFile.setId(1L);
        InputFileDTO inputFileDTO = inputFileMapper.toDto(inputFile);

        int databaseSizeBeforeCreate = inputFileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInputFileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InputFile in the database
        List<InputFile> inputFileList = inputFileRepository.findAll();
        assertThat(inputFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInputFiles() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        // Get all the inputFileList
        restInputFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inputFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dataContentType").value(hasItem(DEFAULT_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].uploadTime").value(hasItem(DEFAULT_UPLOAD_TIME.toString())));
    }

    @Test
    @Transactional
    void getInputFile() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        // Get the inputFile
        restInputFileMockMvc
            .perform(get(ENTITY_API_URL_ID, inputFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inputFile.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dataContentType").value(DEFAULT_DATA_CONTENT_TYPE))
            .andExpect(jsonPath("$.data").value(Base64Utils.encodeToString(DEFAULT_DATA)))
            .andExpect(jsonPath("$.uploadTime").value(DEFAULT_UPLOAD_TIME.toString()));
    }

    @Test
    @Transactional
    void getInputFilesByIdFiltering() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        Long id = inputFile.getId();

        defaultInputFileShouldBeFound("id.equals=" + id);
        defaultInputFileShouldNotBeFound("id.notEquals=" + id);

        defaultInputFileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInputFileShouldNotBeFound("id.greaterThan=" + id);

        defaultInputFileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInputFileShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInputFilesByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        // Get all the inputFileList where fileName equals to DEFAULT_FILE_NAME
        defaultInputFileShouldBeFound("fileName.equals=" + DEFAULT_FILE_NAME);

        // Get all the inputFileList where fileName equals to UPDATED_FILE_NAME
        defaultInputFileShouldNotBeFound("fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllInputFilesByFileNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        // Get all the inputFileList where fileName not equals to DEFAULT_FILE_NAME
        defaultInputFileShouldNotBeFound("fileName.notEquals=" + DEFAULT_FILE_NAME);

        // Get all the inputFileList where fileName not equals to UPDATED_FILE_NAME
        defaultInputFileShouldBeFound("fileName.notEquals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllInputFilesByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        // Get all the inputFileList where fileName in DEFAULT_FILE_NAME or UPDATED_FILE_NAME
        defaultInputFileShouldBeFound("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME);

        // Get all the inputFileList where fileName equals to UPDATED_FILE_NAME
        defaultInputFileShouldNotBeFound("fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllInputFilesByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        // Get all the inputFileList where fileName is not null
        defaultInputFileShouldBeFound("fileName.specified=true");

        // Get all the inputFileList where fileName is null
        defaultInputFileShouldNotBeFound("fileName.specified=false");
    }

    @Test
    @Transactional
    void getAllInputFilesByFileNameContainsSomething() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        // Get all the inputFileList where fileName contains DEFAULT_FILE_NAME
        defaultInputFileShouldBeFound("fileName.contains=" + DEFAULT_FILE_NAME);

        // Get all the inputFileList where fileName contains UPDATED_FILE_NAME
        defaultInputFileShouldNotBeFound("fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllInputFilesByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        // Get all the inputFileList where fileName does not contain DEFAULT_FILE_NAME
        defaultInputFileShouldNotBeFound("fileName.doesNotContain=" + DEFAULT_FILE_NAME);

        // Get all the inputFileList where fileName does not contain UPDATED_FILE_NAME
        defaultInputFileShouldBeFound("fileName.doesNotContain=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllInputFilesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        // Get all the inputFileList where description equals to DEFAULT_DESCRIPTION
        defaultInputFileShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the inputFileList where description equals to UPDATED_DESCRIPTION
        defaultInputFileShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInputFilesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        // Get all the inputFileList where description not equals to DEFAULT_DESCRIPTION
        defaultInputFileShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the inputFileList where description not equals to UPDATED_DESCRIPTION
        defaultInputFileShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInputFilesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        // Get all the inputFileList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultInputFileShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the inputFileList where description equals to UPDATED_DESCRIPTION
        defaultInputFileShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInputFilesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        // Get all the inputFileList where description is not null
        defaultInputFileShouldBeFound("description.specified=true");

        // Get all the inputFileList where description is null
        defaultInputFileShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllInputFilesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        // Get all the inputFileList where description contains DEFAULT_DESCRIPTION
        defaultInputFileShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the inputFileList where description contains UPDATED_DESCRIPTION
        defaultInputFileShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInputFilesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        // Get all the inputFileList where description does not contain DEFAULT_DESCRIPTION
        defaultInputFileShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the inputFileList where description does not contain UPDATED_DESCRIPTION
        defaultInputFileShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInputFilesByUploadTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        // Get all the inputFileList where uploadTime equals to DEFAULT_UPLOAD_TIME
        defaultInputFileShouldBeFound("uploadTime.equals=" + DEFAULT_UPLOAD_TIME);

        // Get all the inputFileList where uploadTime equals to UPDATED_UPLOAD_TIME
        defaultInputFileShouldNotBeFound("uploadTime.equals=" + UPDATED_UPLOAD_TIME);
    }

    @Test
    @Transactional
    void getAllInputFilesByUploadTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        // Get all the inputFileList where uploadTime not equals to DEFAULT_UPLOAD_TIME
        defaultInputFileShouldNotBeFound("uploadTime.notEquals=" + DEFAULT_UPLOAD_TIME);

        // Get all the inputFileList where uploadTime not equals to UPDATED_UPLOAD_TIME
        defaultInputFileShouldBeFound("uploadTime.notEquals=" + UPDATED_UPLOAD_TIME);
    }

    @Test
    @Transactional
    void getAllInputFilesByUploadTimeIsInShouldWork() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        // Get all the inputFileList where uploadTime in DEFAULT_UPLOAD_TIME or UPDATED_UPLOAD_TIME
        defaultInputFileShouldBeFound("uploadTime.in=" + DEFAULT_UPLOAD_TIME + "," + UPDATED_UPLOAD_TIME);

        // Get all the inputFileList where uploadTime equals to UPDATED_UPLOAD_TIME
        defaultInputFileShouldNotBeFound("uploadTime.in=" + UPDATED_UPLOAD_TIME);
    }

    @Test
    @Transactional
    void getAllInputFilesByUploadTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        // Get all the inputFileList where uploadTime is not null
        defaultInputFileShouldBeFound("uploadTime.specified=true");

        // Get all the inputFileList where uploadTime is null
        defaultInputFileShouldNotBeFound("uploadTime.specified=false");
    }

    @Test
    @Transactional
    void getAllInputFilesByToolIsEqualToSomething() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);
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
        inputFile.setTool(tool);
        inputFileRepository.saveAndFlush(inputFile);
        Long toolId = tool.getId();

        // Get all the inputFileList where tool equals to toolId
        defaultInputFileShouldBeFound("toolId.equals=" + toolId);

        // Get all the inputFileList where tool equals to (toolId + 1)
        defaultInputFileShouldNotBeFound("toolId.equals=" + (toolId + 1));
    }

    @Test
    @Transactional
    void getAllInputFilesByGenProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);
        GenProfile genProfile;
        if (TestUtil.findAll(em, GenProfile.class).isEmpty()) {
            genProfile = GenProfileResourceIT.createEntity(em);
            em.persist(genProfile);
            em.flush();
        } else {
            genProfile = TestUtil.findAll(em, GenProfile.class).get(0);
        }
        em.persist(genProfile);
        em.flush();
        inputFile.setGenProfile(genProfile);
        genProfile.setInputFile(inputFile);
        inputFileRepository.saveAndFlush(inputFile);
        Long genProfileId = genProfile.getId();

        // Get all the inputFileList where genProfile equals to genProfileId
        defaultInputFileShouldBeFound("genProfileId.equals=" + genProfileId);

        // Get all the inputFileList where genProfile equals to (genProfileId + 1)
        defaultInputFileShouldNotBeFound("genProfileId.equals=" + (genProfileId + 1));
    }

    @Test
    @Transactional
    void getAllInputFilesByFlexProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);
        FlexProfile flexProfile;
        if (TestUtil.findAll(em, FlexProfile.class).isEmpty()) {
            flexProfile = FlexProfileResourceIT.createEntity(em);
            em.persist(flexProfile);
            em.flush();
        } else {
            flexProfile = TestUtil.findAll(em, FlexProfile.class).get(0);
        }
        em.persist(flexProfile);
        em.flush();
        inputFile.setFlexProfile(flexProfile);
        flexProfile.setInputFile(inputFile);
        inputFileRepository.saveAndFlush(inputFile);
        Long flexProfileId = flexProfile.getId();

        // Get all the inputFileList where flexProfile equals to flexProfileId
        defaultInputFileShouldBeFound("flexProfileId.equals=" + flexProfileId);

        // Get all the inputFileList where flexProfile equals to (flexProfileId + 1)
        defaultInputFileShouldNotBeFound("flexProfileId.equals=" + (flexProfileId + 1));
    }

    @Test
    @Transactional
    void getAllInputFilesByLoadProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);
        LoadProfile loadProfile;
        if (TestUtil.findAll(em, LoadProfile.class).isEmpty()) {
            loadProfile = LoadProfileResourceIT.createEntity(em);
            em.persist(loadProfile);
            em.flush();
        } else {
            loadProfile = TestUtil.findAll(em, LoadProfile.class).get(0);
        }
        em.persist(loadProfile);
        em.flush();
        inputFile.setLoadProfile(loadProfile);
        loadProfile.setInputFile(inputFile);
        inputFileRepository.saveAndFlush(inputFile);
        Long loadProfileId = loadProfile.getId();

        // Get all the inputFileList where loadProfile equals to loadProfileId
        defaultInputFileShouldBeFound("loadProfileId.equals=" + loadProfileId);

        // Get all the inputFileList where loadProfile equals to (loadProfileId + 1)
        defaultInputFileShouldNotBeFound("loadProfileId.equals=" + (loadProfileId + 1));
    }

    @Test
    @Transactional
    void getAllInputFilesByTransfProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);
        TransfProfile transfProfile;
        if (TestUtil.findAll(em, TransfProfile.class).isEmpty()) {
            transfProfile = TransfProfileResourceIT.createEntity(em);
            em.persist(transfProfile);
            em.flush();
        } else {
            transfProfile = TestUtil.findAll(em, TransfProfile.class).get(0);
        }
        em.persist(transfProfile);
        em.flush();
        inputFile.setTransfProfile(transfProfile);
        transfProfile.setInputFile(inputFile);
        inputFileRepository.saveAndFlush(inputFile);
        Long transfProfileId = transfProfile.getId();

        // Get all the inputFileList where transfProfile equals to transfProfileId
        defaultInputFileShouldBeFound("transfProfileId.equals=" + transfProfileId);

        // Get all the inputFileList where transfProfile equals to (transfProfileId + 1)
        defaultInputFileShouldNotBeFound("transfProfileId.equals=" + (transfProfileId + 1));
    }

    @Test
    @Transactional
    void getAllInputFilesByBranchProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);
        BranchProfile branchProfile;
        if (TestUtil.findAll(em, BranchProfile.class).isEmpty()) {
            branchProfile = BranchProfileResourceIT.createEntity(em);
            em.persist(branchProfile);
            em.flush();
        } else {
            branchProfile = TestUtil.findAll(em, BranchProfile.class).get(0);
        }
        em.persist(branchProfile);
        em.flush();
        inputFile.setBranchProfile(branchProfile);
        branchProfile.setInputFile(inputFile);
        inputFileRepository.saveAndFlush(inputFile);
        Long branchProfileId = branchProfile.getId();

        // Get all the inputFileList where branchProfile equals to branchProfileId
        defaultInputFileShouldBeFound("branchProfileId.equals=" + branchProfileId);

        // Get all the inputFileList where branchProfile equals to (branchProfileId + 1)
        defaultInputFileShouldNotBeFound("branchProfileId.equals=" + (branchProfileId + 1));
    }

    @Test
    @Transactional
    void getAllInputFilesByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);
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
        inputFile.setNetwork(network);
        inputFileRepository.saveAndFlush(inputFile);
        Long networkId = network.getId();

        // Get all the inputFileList where network equals to networkId
        defaultInputFileShouldBeFound("networkId.equals=" + networkId);

        // Get all the inputFileList where network equals to (networkId + 1)
        defaultInputFileShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    @Test
    @Transactional
    void getAllInputFilesBySimulationIsEqualToSomething() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);
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
        inputFile.addSimulation(simulation);
        inputFileRepository.saveAndFlush(inputFile);
        Long simulationId = simulation.getId();

        // Get all the inputFileList where simulation equals to simulationId
        defaultInputFileShouldBeFound("simulationId.equals=" + simulationId);

        // Get all the inputFileList where simulation equals to (simulationId + 1)
        defaultInputFileShouldNotBeFound("simulationId.equals=" + (simulationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInputFileShouldBeFound(String filter) throws Exception {
        restInputFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inputFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dataContentType").value(hasItem(DEFAULT_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].uploadTime").value(hasItem(DEFAULT_UPLOAD_TIME.toString())));

        // Check, that the count call also returns 1
        restInputFileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInputFileShouldNotBeFound(String filter) throws Exception {
        restInputFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInputFileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInputFile() throws Exception {
        // Get the inputFile
        restInputFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInputFile() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        int databaseSizeBeforeUpdate = inputFileRepository.findAll().size();

        // Update the inputFile
        InputFile updatedInputFile = inputFileRepository.findById(inputFile.getId()).get();
        // Disconnect from session so that the updates on updatedInputFile are not directly saved in db
        em.detach(updatedInputFile);
        updatedInputFile
            .fileName(UPDATED_FILE_NAME)
            .description(UPDATED_DESCRIPTION)
            .data(UPDATED_DATA)
            .dataContentType(UPDATED_DATA_CONTENT_TYPE)
            .uploadTime(UPDATED_UPLOAD_TIME);
        InputFileDTO inputFileDTO = inputFileMapper.toDto(updatedInputFile);

        restInputFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inputFileDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputFileDTO))
            )
            .andExpect(status().isOk());

        // Validate the InputFile in the database
        List<InputFile> inputFileList = inputFileRepository.findAll();
        assertThat(inputFileList).hasSize(databaseSizeBeforeUpdate);
        InputFile testInputFile = inputFileList.get(inputFileList.size() - 1);
        assertThat(testInputFile.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testInputFile.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInputFile.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testInputFile.getDataContentType()).isEqualTo(UPDATED_DATA_CONTENT_TYPE);
        assertThat(testInputFile.getUploadTime()).isEqualTo(UPDATED_UPLOAD_TIME);
    }

    @Test
    @Transactional
    void putNonExistingInputFile() throws Exception {
        int databaseSizeBeforeUpdate = inputFileRepository.findAll().size();
        inputFile.setId(count.incrementAndGet());

        // Create the InputFile
        InputFileDTO inputFileDTO = inputFileMapper.toDto(inputFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInputFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inputFileDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InputFile in the database
        List<InputFile> inputFileList = inputFileRepository.findAll();
        assertThat(inputFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInputFile() throws Exception {
        int databaseSizeBeforeUpdate = inputFileRepository.findAll().size();
        inputFile.setId(count.incrementAndGet());

        // Create the InputFile
        InputFileDTO inputFileDTO = inputFileMapper.toDto(inputFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInputFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InputFile in the database
        List<InputFile> inputFileList = inputFileRepository.findAll();
        assertThat(inputFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInputFile() throws Exception {
        int databaseSizeBeforeUpdate = inputFileRepository.findAll().size();
        inputFile.setId(count.incrementAndGet());

        // Create the InputFile
        InputFileDTO inputFileDTO = inputFileMapper.toDto(inputFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInputFileMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputFileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InputFile in the database
        List<InputFile> inputFileList = inputFileRepository.findAll();
        assertThat(inputFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInputFileWithPatch() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        int databaseSizeBeforeUpdate = inputFileRepository.findAll().size();

        // Update the inputFile using partial update
        InputFile partialUpdatedInputFile = new InputFile();
        partialUpdatedInputFile.setId(inputFile.getId());

        partialUpdatedInputFile.uploadTime(UPDATED_UPLOAD_TIME);

        restInputFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInputFile.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInputFile))
            )
            .andExpect(status().isOk());

        // Validate the InputFile in the database
        List<InputFile> inputFileList = inputFileRepository.findAll();
        assertThat(inputFileList).hasSize(databaseSizeBeforeUpdate);
        InputFile testInputFile = inputFileList.get(inputFileList.size() - 1);
        assertThat(testInputFile.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testInputFile.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInputFile.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testInputFile.getDataContentType()).isEqualTo(DEFAULT_DATA_CONTENT_TYPE);
        assertThat(testInputFile.getUploadTime()).isEqualTo(UPDATED_UPLOAD_TIME);
    }

    @Test
    @Transactional
    void fullUpdateInputFileWithPatch() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        int databaseSizeBeforeUpdate = inputFileRepository.findAll().size();

        // Update the inputFile using partial update
        InputFile partialUpdatedInputFile = new InputFile();
        partialUpdatedInputFile.setId(inputFile.getId());

        partialUpdatedInputFile
            .fileName(UPDATED_FILE_NAME)
            .description(UPDATED_DESCRIPTION)
            .data(UPDATED_DATA)
            .dataContentType(UPDATED_DATA_CONTENT_TYPE)
            .uploadTime(UPDATED_UPLOAD_TIME);

        restInputFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInputFile.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInputFile))
            )
            .andExpect(status().isOk());

        // Validate the InputFile in the database
        List<InputFile> inputFileList = inputFileRepository.findAll();
        assertThat(inputFileList).hasSize(databaseSizeBeforeUpdate);
        InputFile testInputFile = inputFileList.get(inputFileList.size() - 1);
        assertThat(testInputFile.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testInputFile.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInputFile.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testInputFile.getDataContentType()).isEqualTo(UPDATED_DATA_CONTENT_TYPE);
        assertThat(testInputFile.getUploadTime()).isEqualTo(UPDATED_UPLOAD_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingInputFile() throws Exception {
        int databaseSizeBeforeUpdate = inputFileRepository.findAll().size();
        inputFile.setId(count.incrementAndGet());

        // Create the InputFile
        InputFileDTO inputFileDTO = inputFileMapper.toDto(inputFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInputFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inputFileDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inputFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InputFile in the database
        List<InputFile> inputFileList = inputFileRepository.findAll();
        assertThat(inputFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInputFile() throws Exception {
        int databaseSizeBeforeUpdate = inputFileRepository.findAll().size();
        inputFile.setId(count.incrementAndGet());

        // Create the InputFile
        InputFileDTO inputFileDTO = inputFileMapper.toDto(inputFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInputFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inputFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InputFile in the database
        List<InputFile> inputFileList = inputFileRepository.findAll();
        assertThat(inputFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInputFile() throws Exception {
        int databaseSizeBeforeUpdate = inputFileRepository.findAll().size();
        inputFile.setId(count.incrementAndGet());

        // Create the InputFile
        InputFileDTO inputFileDTO = inputFileMapper.toDto(inputFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInputFileMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inputFileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InputFile in the database
        List<InputFile> inputFileList = inputFileRepository.findAll();
        assertThat(inputFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInputFile() throws Exception {
        // Initialize the database
        inputFileRepository.saveAndFlush(inputFile);

        int databaseSizeBeforeDelete = inputFileRepository.findAll().size();

        // Delete the inputFile
        restInputFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, inputFile.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InputFile> inputFileList = inputFileRepository.findAll();
        assertThat(inputFileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
