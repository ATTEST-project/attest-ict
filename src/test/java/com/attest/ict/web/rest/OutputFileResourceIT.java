package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Network;
import com.attest.ict.domain.OutputFile;
import com.attest.ict.domain.Simulation;
import com.attest.ict.domain.Tool;
import com.attest.ict.repository.OutputFileRepository;
import com.attest.ict.service.criteria.OutputFileCriteria;
import com.attest.ict.service.dto.OutputFileDTO;
import com.attest.ict.service.mapper.OutputFileMapper;
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
 * Integration tests for the {@link OutputFileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OutputFileResourceIT {

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

    private static final String ENTITY_API_URL = "/api/output-files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OutputFileRepository outputFileRepository;

    @Autowired
    private OutputFileMapper outputFileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOutputFileMockMvc;

    private OutputFile outputFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OutputFile createEntity(EntityManager em) {
        OutputFile outputFile = new OutputFile()
            .fileName(DEFAULT_FILE_NAME)
            .description(DEFAULT_DESCRIPTION)
            .data(DEFAULT_DATA)
            .dataContentType(DEFAULT_DATA_CONTENT_TYPE)
            .uploadTime(DEFAULT_UPLOAD_TIME);
        return outputFile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OutputFile createUpdatedEntity(EntityManager em) {
        OutputFile outputFile = new OutputFile()
            .fileName(UPDATED_FILE_NAME)
            .description(UPDATED_DESCRIPTION)
            .data(UPDATED_DATA)
            .dataContentType(UPDATED_DATA_CONTENT_TYPE)
            .uploadTime(UPDATED_UPLOAD_TIME);
        return outputFile;
    }

    @BeforeEach
    public void initTest() {
        outputFile = createEntity(em);
    }

    @Test
    @Transactional
    void createOutputFile() throws Exception {
        int databaseSizeBeforeCreate = outputFileRepository.findAll().size();
        // Create the OutputFile
        OutputFileDTO outputFileDTO = outputFileMapper.toDto(outputFile);
        restOutputFileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outputFileDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OutputFile in the database
        List<OutputFile> outputFileList = outputFileRepository.findAll();
        assertThat(outputFileList).hasSize(databaseSizeBeforeCreate + 1);
        OutputFile testOutputFile = outputFileList.get(outputFileList.size() - 1);
        assertThat(testOutputFile.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testOutputFile.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOutputFile.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testOutputFile.getDataContentType()).isEqualTo(DEFAULT_DATA_CONTENT_TYPE);
        assertThat(testOutputFile.getUploadTime()).isEqualTo(DEFAULT_UPLOAD_TIME);
    }

    @Test
    @Transactional
    void createOutputFileWithExistingId() throws Exception {
        // Create the OutputFile with an existing ID
        outputFile.setId(1L);
        OutputFileDTO outputFileDTO = outputFileMapper.toDto(outputFile);

        int databaseSizeBeforeCreate = outputFileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOutputFileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outputFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutputFile in the database
        List<OutputFile> outputFileList = outputFileRepository.findAll();
        assertThat(outputFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOutputFiles() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        // Get all the outputFileList
        restOutputFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outputFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dataContentType").value(hasItem(DEFAULT_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].uploadTime").value(hasItem(DEFAULT_UPLOAD_TIME.toString())));
    }

    @Test
    @Transactional
    void getOutputFile() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        // Get the outputFile
        restOutputFileMockMvc
            .perform(get(ENTITY_API_URL_ID, outputFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(outputFile.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dataContentType").value(DEFAULT_DATA_CONTENT_TYPE))
            .andExpect(jsonPath("$.data").value(Base64Utils.encodeToString(DEFAULT_DATA)))
            .andExpect(jsonPath("$.uploadTime").value(DEFAULT_UPLOAD_TIME.toString()));
    }

    @Test
    @Transactional
    void getOutputFilesByIdFiltering() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        Long id = outputFile.getId();

        defaultOutputFileShouldBeFound("id.equals=" + id);
        defaultOutputFileShouldNotBeFound("id.notEquals=" + id);

        defaultOutputFileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOutputFileShouldNotBeFound("id.greaterThan=" + id);

        defaultOutputFileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOutputFileShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOutputFilesByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        // Get all the outputFileList where fileName equals to DEFAULT_FILE_NAME
        defaultOutputFileShouldBeFound("fileName.equals=" + DEFAULT_FILE_NAME);

        // Get all the outputFileList where fileName equals to UPDATED_FILE_NAME
        defaultOutputFileShouldNotBeFound("fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllOutputFilesByFileNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        // Get all the outputFileList where fileName not equals to DEFAULT_FILE_NAME
        defaultOutputFileShouldNotBeFound("fileName.notEquals=" + DEFAULT_FILE_NAME);

        // Get all the outputFileList where fileName not equals to UPDATED_FILE_NAME
        defaultOutputFileShouldBeFound("fileName.notEquals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllOutputFilesByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        // Get all the outputFileList where fileName in DEFAULT_FILE_NAME or UPDATED_FILE_NAME
        defaultOutputFileShouldBeFound("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME);

        // Get all the outputFileList where fileName equals to UPDATED_FILE_NAME
        defaultOutputFileShouldNotBeFound("fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllOutputFilesByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        // Get all the outputFileList where fileName is not null
        defaultOutputFileShouldBeFound("fileName.specified=true");

        // Get all the outputFileList where fileName is null
        defaultOutputFileShouldNotBeFound("fileName.specified=false");
    }

    @Test
    @Transactional
    void getAllOutputFilesByFileNameContainsSomething() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        // Get all the outputFileList where fileName contains DEFAULT_FILE_NAME
        defaultOutputFileShouldBeFound("fileName.contains=" + DEFAULT_FILE_NAME);

        // Get all the outputFileList where fileName contains UPDATED_FILE_NAME
        defaultOutputFileShouldNotBeFound("fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllOutputFilesByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        // Get all the outputFileList where fileName does not contain DEFAULT_FILE_NAME
        defaultOutputFileShouldNotBeFound("fileName.doesNotContain=" + DEFAULT_FILE_NAME);

        // Get all the outputFileList where fileName does not contain UPDATED_FILE_NAME
        defaultOutputFileShouldBeFound("fileName.doesNotContain=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllOutputFilesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        // Get all the outputFileList where description equals to DEFAULT_DESCRIPTION
        defaultOutputFileShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the outputFileList where description equals to UPDATED_DESCRIPTION
        defaultOutputFileShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOutputFilesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        // Get all the outputFileList where description not equals to DEFAULT_DESCRIPTION
        defaultOutputFileShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the outputFileList where description not equals to UPDATED_DESCRIPTION
        defaultOutputFileShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOutputFilesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        // Get all the outputFileList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultOutputFileShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the outputFileList where description equals to UPDATED_DESCRIPTION
        defaultOutputFileShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOutputFilesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        // Get all the outputFileList where description is not null
        defaultOutputFileShouldBeFound("description.specified=true");

        // Get all the outputFileList where description is null
        defaultOutputFileShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllOutputFilesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        // Get all the outputFileList where description contains DEFAULT_DESCRIPTION
        defaultOutputFileShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the outputFileList where description contains UPDATED_DESCRIPTION
        defaultOutputFileShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOutputFilesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        // Get all the outputFileList where description does not contain DEFAULT_DESCRIPTION
        defaultOutputFileShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the outputFileList where description does not contain UPDATED_DESCRIPTION
        defaultOutputFileShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOutputFilesByUploadTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        // Get all the outputFileList where uploadTime equals to DEFAULT_UPLOAD_TIME
        defaultOutputFileShouldBeFound("uploadTime.equals=" + DEFAULT_UPLOAD_TIME);

        // Get all the outputFileList where uploadTime equals to UPDATED_UPLOAD_TIME
        defaultOutputFileShouldNotBeFound("uploadTime.equals=" + UPDATED_UPLOAD_TIME);
    }

    @Test
    @Transactional
    void getAllOutputFilesByUploadTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        // Get all the outputFileList where uploadTime not equals to DEFAULT_UPLOAD_TIME
        defaultOutputFileShouldNotBeFound("uploadTime.notEquals=" + DEFAULT_UPLOAD_TIME);

        // Get all the outputFileList where uploadTime not equals to UPDATED_UPLOAD_TIME
        defaultOutputFileShouldBeFound("uploadTime.notEquals=" + UPDATED_UPLOAD_TIME);
    }

    @Test
    @Transactional
    void getAllOutputFilesByUploadTimeIsInShouldWork() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        // Get all the outputFileList where uploadTime in DEFAULT_UPLOAD_TIME or UPDATED_UPLOAD_TIME
        defaultOutputFileShouldBeFound("uploadTime.in=" + DEFAULT_UPLOAD_TIME + "," + UPDATED_UPLOAD_TIME);

        // Get all the outputFileList where uploadTime equals to UPDATED_UPLOAD_TIME
        defaultOutputFileShouldNotBeFound("uploadTime.in=" + UPDATED_UPLOAD_TIME);
    }

    @Test
    @Transactional
    void getAllOutputFilesByUploadTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        // Get all the outputFileList where uploadTime is not null
        defaultOutputFileShouldBeFound("uploadTime.specified=true");

        // Get all the outputFileList where uploadTime is null
        defaultOutputFileShouldNotBeFound("uploadTime.specified=false");
    }

    @Test
    @Transactional
    void getAllOutputFilesByToolIsEqualToSomething() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);
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
        outputFile.setTool(tool);
        outputFileRepository.saveAndFlush(outputFile);
        Long toolId = tool.getId();

        // Get all the outputFileList where tool equals to toolId
        defaultOutputFileShouldBeFound("toolId.equals=" + toolId);

        // Get all the outputFileList where tool equals to (toolId + 1)
        defaultOutputFileShouldNotBeFound("toolId.equals=" + (toolId + 1));
    }

    @Test
    @Transactional
    void getAllOutputFilesByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);
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
        outputFile.setNetwork(network);
        outputFileRepository.saveAndFlush(outputFile);
        Long networkId = network.getId();

        // Get all the outputFileList where network equals to networkId
        defaultOutputFileShouldBeFound("networkId.equals=" + networkId);

        // Get all the outputFileList where network equals to (networkId + 1)
        defaultOutputFileShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    @Test
    @Transactional
    void getAllOutputFilesBySimulationIsEqualToSomething() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);
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
        outputFile.setSimulation(simulation);
        outputFileRepository.saveAndFlush(outputFile);
        Long simulationId = simulation.getId();

        // Get all the outputFileList where simulation equals to simulationId
        defaultOutputFileShouldBeFound("simulationId.equals=" + simulationId);

        // Get all the outputFileList where simulation equals to (simulationId + 1)
        defaultOutputFileShouldNotBeFound("simulationId.equals=" + (simulationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOutputFileShouldBeFound(String filter) throws Exception {
        restOutputFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outputFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dataContentType").value(hasItem(DEFAULT_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].uploadTime").value(hasItem(DEFAULT_UPLOAD_TIME.toString())));

        // Check, that the count call also returns 1
        restOutputFileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOutputFileShouldNotBeFound(String filter) throws Exception {
        restOutputFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOutputFileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOutputFile() throws Exception {
        // Get the outputFile
        restOutputFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOutputFile() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        int databaseSizeBeforeUpdate = outputFileRepository.findAll().size();

        // Update the outputFile
        OutputFile updatedOutputFile = outputFileRepository.findById(outputFile.getId()).get();
        // Disconnect from session so that the updates on updatedOutputFile are not directly saved in db
        em.detach(updatedOutputFile);
        updatedOutputFile
            .fileName(UPDATED_FILE_NAME)
            .description(UPDATED_DESCRIPTION)
            .data(UPDATED_DATA)
            .dataContentType(UPDATED_DATA_CONTENT_TYPE)
            .uploadTime(UPDATED_UPLOAD_TIME);
        OutputFileDTO outputFileDTO = outputFileMapper.toDto(updatedOutputFile);

        restOutputFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, outputFileDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outputFileDTO))
            )
            .andExpect(status().isOk());

        // Validate the OutputFile in the database
        List<OutputFile> outputFileList = outputFileRepository.findAll();
        assertThat(outputFileList).hasSize(databaseSizeBeforeUpdate);
        OutputFile testOutputFile = outputFileList.get(outputFileList.size() - 1);
        assertThat(testOutputFile.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testOutputFile.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOutputFile.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testOutputFile.getDataContentType()).isEqualTo(UPDATED_DATA_CONTENT_TYPE);
        assertThat(testOutputFile.getUploadTime()).isEqualTo(UPDATED_UPLOAD_TIME);
    }

    @Test
    @Transactional
    void putNonExistingOutputFile() throws Exception {
        int databaseSizeBeforeUpdate = outputFileRepository.findAll().size();
        outputFile.setId(count.incrementAndGet());

        // Create the OutputFile
        OutputFileDTO outputFileDTO = outputFileMapper.toDto(outputFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOutputFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, outputFileDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outputFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutputFile in the database
        List<OutputFile> outputFileList = outputFileRepository.findAll();
        assertThat(outputFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOutputFile() throws Exception {
        int databaseSizeBeforeUpdate = outputFileRepository.findAll().size();
        outputFile.setId(count.incrementAndGet());

        // Create the OutputFile
        OutputFileDTO outputFileDTO = outputFileMapper.toDto(outputFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutputFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outputFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutputFile in the database
        List<OutputFile> outputFileList = outputFileRepository.findAll();
        assertThat(outputFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOutputFile() throws Exception {
        int databaseSizeBeforeUpdate = outputFileRepository.findAll().size();
        outputFile.setId(count.incrementAndGet());

        // Create the OutputFile
        OutputFileDTO outputFileDTO = outputFileMapper.toDto(outputFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutputFileMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outputFileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OutputFile in the database
        List<OutputFile> outputFileList = outputFileRepository.findAll();
        assertThat(outputFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOutputFileWithPatch() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        int databaseSizeBeforeUpdate = outputFileRepository.findAll().size();

        // Update the outputFile using partial update
        OutputFile partialUpdatedOutputFile = new OutputFile();
        partialUpdatedOutputFile.setId(outputFile.getId());

        partialUpdatedOutputFile
            .fileName(UPDATED_FILE_NAME)
            .description(UPDATED_DESCRIPTION)
            .data(UPDATED_DATA)
            .dataContentType(UPDATED_DATA_CONTENT_TYPE);

        restOutputFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOutputFile.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOutputFile))
            )
            .andExpect(status().isOk());

        // Validate the OutputFile in the database
        List<OutputFile> outputFileList = outputFileRepository.findAll();
        assertThat(outputFileList).hasSize(databaseSizeBeforeUpdate);
        OutputFile testOutputFile = outputFileList.get(outputFileList.size() - 1);
        assertThat(testOutputFile.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testOutputFile.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOutputFile.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testOutputFile.getDataContentType()).isEqualTo(UPDATED_DATA_CONTENT_TYPE);
        assertThat(testOutputFile.getUploadTime()).isEqualTo(DEFAULT_UPLOAD_TIME);
    }

    @Test
    @Transactional
    void fullUpdateOutputFileWithPatch() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        int databaseSizeBeforeUpdate = outputFileRepository.findAll().size();

        // Update the outputFile using partial update
        OutputFile partialUpdatedOutputFile = new OutputFile();
        partialUpdatedOutputFile.setId(outputFile.getId());

        partialUpdatedOutputFile
            .fileName(UPDATED_FILE_NAME)
            .description(UPDATED_DESCRIPTION)
            .data(UPDATED_DATA)
            .dataContentType(UPDATED_DATA_CONTENT_TYPE)
            .uploadTime(UPDATED_UPLOAD_TIME);

        restOutputFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOutputFile.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOutputFile))
            )
            .andExpect(status().isOk());

        // Validate the OutputFile in the database
        List<OutputFile> outputFileList = outputFileRepository.findAll();
        assertThat(outputFileList).hasSize(databaseSizeBeforeUpdate);
        OutputFile testOutputFile = outputFileList.get(outputFileList.size() - 1);
        assertThat(testOutputFile.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testOutputFile.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOutputFile.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testOutputFile.getDataContentType()).isEqualTo(UPDATED_DATA_CONTENT_TYPE);
        assertThat(testOutputFile.getUploadTime()).isEqualTo(UPDATED_UPLOAD_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingOutputFile() throws Exception {
        int databaseSizeBeforeUpdate = outputFileRepository.findAll().size();
        outputFile.setId(count.incrementAndGet());

        // Create the OutputFile
        OutputFileDTO outputFileDTO = outputFileMapper.toDto(outputFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOutputFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, outputFileDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(outputFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutputFile in the database
        List<OutputFile> outputFileList = outputFileRepository.findAll();
        assertThat(outputFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOutputFile() throws Exception {
        int databaseSizeBeforeUpdate = outputFileRepository.findAll().size();
        outputFile.setId(count.incrementAndGet());

        // Create the OutputFile
        OutputFileDTO outputFileDTO = outputFileMapper.toDto(outputFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutputFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(outputFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutputFile in the database
        List<OutputFile> outputFileList = outputFileRepository.findAll();
        assertThat(outputFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOutputFile() throws Exception {
        int databaseSizeBeforeUpdate = outputFileRepository.findAll().size();
        outputFile.setId(count.incrementAndGet());

        // Create the OutputFile
        OutputFileDTO outputFileDTO = outputFileMapper.toDto(outputFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutputFileMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(outputFileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OutputFile in the database
        List<OutputFile> outputFileList = outputFileRepository.findAll();
        assertThat(outputFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOutputFile() throws Exception {
        // Initialize the database
        outputFileRepository.saveAndFlush(outputFile);

        int databaseSizeBeforeDelete = outputFileRepository.findAll().size();

        // Delete the outputFile
        restOutputFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, outputFile.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OutputFile> outputFileList = outputFileRepository.findAll();
        assertThat(outputFileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
