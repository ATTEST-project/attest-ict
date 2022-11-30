package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Task;
import com.attest.ict.domain.ToolLogFile;
import com.attest.ict.repository.ToolLogFileRepository;
import com.attest.ict.service.criteria.ToolLogFileCriteria;
import com.attest.ict.service.dto.ToolLogFileDTO;
import com.attest.ict.service.mapper.ToolLogFileMapper;
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
 * Integration tests for the {@link ToolLogFileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ToolLogFileResourceIT {

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

    private static final String ENTITY_API_URL = "/api/tool-log-files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ToolLogFileRepository toolLogFileRepository;

    @Autowired
    private ToolLogFileMapper toolLogFileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restToolLogFileMockMvc;

    private ToolLogFile toolLogFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ToolLogFile createEntity(EntityManager em) {
        ToolLogFile toolLogFile = new ToolLogFile()
            .fileName(DEFAULT_FILE_NAME)
            .description(DEFAULT_DESCRIPTION)
            .data(DEFAULT_DATA)
            .dataContentType(DEFAULT_DATA_CONTENT_TYPE)
            .uploadTime(DEFAULT_UPLOAD_TIME);
        return toolLogFile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ToolLogFile createUpdatedEntity(EntityManager em) {
        ToolLogFile toolLogFile = new ToolLogFile()
            .fileName(UPDATED_FILE_NAME)
            .description(UPDATED_DESCRIPTION)
            .data(UPDATED_DATA)
            .dataContentType(UPDATED_DATA_CONTENT_TYPE)
            .uploadTime(UPDATED_UPLOAD_TIME);
        return toolLogFile;
    }

    @BeforeEach
    public void initTest() {
        toolLogFile = createEntity(em);
    }

    @Test
    @Transactional
    void createToolLogFile() throws Exception {
        int databaseSizeBeforeCreate = toolLogFileRepository.findAll().size();
        // Create the ToolLogFile
        ToolLogFileDTO toolLogFileDTO = toolLogFileMapper.toDto(toolLogFile);
        restToolLogFileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolLogFileDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ToolLogFile in the database
        List<ToolLogFile> toolLogFileList = toolLogFileRepository.findAll();
        assertThat(toolLogFileList).hasSize(databaseSizeBeforeCreate + 1);
        ToolLogFile testToolLogFile = toolLogFileList.get(toolLogFileList.size() - 1);
        assertThat(testToolLogFile.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testToolLogFile.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testToolLogFile.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testToolLogFile.getDataContentType()).isEqualTo(DEFAULT_DATA_CONTENT_TYPE);
        assertThat(testToolLogFile.getUploadTime()).isEqualTo(DEFAULT_UPLOAD_TIME);
    }

    @Test
    @Transactional
    void createToolLogFileWithExistingId() throws Exception {
        // Create the ToolLogFile with an existing ID
        toolLogFile.setId(1L);
        ToolLogFileDTO toolLogFileDTO = toolLogFileMapper.toDto(toolLogFile);

        int databaseSizeBeforeCreate = toolLogFileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restToolLogFileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolLogFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ToolLogFile in the database
        List<ToolLogFile> toolLogFileList = toolLogFileRepository.findAll();
        assertThat(toolLogFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllToolLogFiles() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        // Get all the toolLogFileList
        restToolLogFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(toolLogFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dataContentType").value(hasItem(DEFAULT_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].uploadTime").value(hasItem(DEFAULT_UPLOAD_TIME.toString())));
    }

    @Test
    @Transactional
    void getToolLogFile() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        // Get the toolLogFile
        restToolLogFileMockMvc
            .perform(get(ENTITY_API_URL_ID, toolLogFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(toolLogFile.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dataContentType").value(DEFAULT_DATA_CONTENT_TYPE))
            .andExpect(jsonPath("$.data").value(Base64Utils.encodeToString(DEFAULT_DATA)))
            .andExpect(jsonPath("$.uploadTime").value(DEFAULT_UPLOAD_TIME.toString()));
    }

    @Test
    @Transactional
    void getToolLogFilesByIdFiltering() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        Long id = toolLogFile.getId();

        defaultToolLogFileShouldBeFound("id.equals=" + id);
        defaultToolLogFileShouldNotBeFound("id.notEquals=" + id);

        defaultToolLogFileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultToolLogFileShouldNotBeFound("id.greaterThan=" + id);

        defaultToolLogFileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultToolLogFileShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllToolLogFilesByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        // Get all the toolLogFileList where fileName equals to DEFAULT_FILE_NAME
        defaultToolLogFileShouldBeFound("fileName.equals=" + DEFAULT_FILE_NAME);

        // Get all the toolLogFileList where fileName equals to UPDATED_FILE_NAME
        defaultToolLogFileShouldNotBeFound("fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllToolLogFilesByFileNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        // Get all the toolLogFileList where fileName not equals to DEFAULT_FILE_NAME
        defaultToolLogFileShouldNotBeFound("fileName.notEquals=" + DEFAULT_FILE_NAME);

        // Get all the toolLogFileList where fileName not equals to UPDATED_FILE_NAME
        defaultToolLogFileShouldBeFound("fileName.notEquals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllToolLogFilesByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        // Get all the toolLogFileList where fileName in DEFAULT_FILE_NAME or UPDATED_FILE_NAME
        defaultToolLogFileShouldBeFound("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME);

        // Get all the toolLogFileList where fileName equals to UPDATED_FILE_NAME
        defaultToolLogFileShouldNotBeFound("fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllToolLogFilesByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        // Get all the toolLogFileList where fileName is not null
        defaultToolLogFileShouldBeFound("fileName.specified=true");

        // Get all the toolLogFileList where fileName is null
        defaultToolLogFileShouldNotBeFound("fileName.specified=false");
    }

    @Test
    @Transactional
    void getAllToolLogFilesByFileNameContainsSomething() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        // Get all the toolLogFileList where fileName contains DEFAULT_FILE_NAME
        defaultToolLogFileShouldBeFound("fileName.contains=" + DEFAULT_FILE_NAME);

        // Get all the toolLogFileList where fileName contains UPDATED_FILE_NAME
        defaultToolLogFileShouldNotBeFound("fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllToolLogFilesByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        // Get all the toolLogFileList where fileName does not contain DEFAULT_FILE_NAME
        defaultToolLogFileShouldNotBeFound("fileName.doesNotContain=" + DEFAULT_FILE_NAME);

        // Get all the toolLogFileList where fileName does not contain UPDATED_FILE_NAME
        defaultToolLogFileShouldBeFound("fileName.doesNotContain=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllToolLogFilesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        // Get all the toolLogFileList where description equals to DEFAULT_DESCRIPTION
        defaultToolLogFileShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the toolLogFileList where description equals to UPDATED_DESCRIPTION
        defaultToolLogFileShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllToolLogFilesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        // Get all the toolLogFileList where description not equals to DEFAULT_DESCRIPTION
        defaultToolLogFileShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the toolLogFileList where description not equals to UPDATED_DESCRIPTION
        defaultToolLogFileShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllToolLogFilesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        // Get all the toolLogFileList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultToolLogFileShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the toolLogFileList where description equals to UPDATED_DESCRIPTION
        defaultToolLogFileShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllToolLogFilesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        // Get all the toolLogFileList where description is not null
        defaultToolLogFileShouldBeFound("description.specified=true");

        // Get all the toolLogFileList where description is null
        defaultToolLogFileShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllToolLogFilesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        // Get all the toolLogFileList where description contains DEFAULT_DESCRIPTION
        defaultToolLogFileShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the toolLogFileList where description contains UPDATED_DESCRIPTION
        defaultToolLogFileShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllToolLogFilesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        // Get all the toolLogFileList where description does not contain DEFAULT_DESCRIPTION
        defaultToolLogFileShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the toolLogFileList where description does not contain UPDATED_DESCRIPTION
        defaultToolLogFileShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllToolLogFilesByUploadTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        // Get all the toolLogFileList where uploadTime equals to DEFAULT_UPLOAD_TIME
        defaultToolLogFileShouldBeFound("uploadTime.equals=" + DEFAULT_UPLOAD_TIME);

        // Get all the toolLogFileList where uploadTime equals to UPDATED_UPLOAD_TIME
        defaultToolLogFileShouldNotBeFound("uploadTime.equals=" + UPDATED_UPLOAD_TIME);
    }

    @Test
    @Transactional
    void getAllToolLogFilesByUploadTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        // Get all the toolLogFileList where uploadTime not equals to DEFAULT_UPLOAD_TIME
        defaultToolLogFileShouldNotBeFound("uploadTime.notEquals=" + DEFAULT_UPLOAD_TIME);

        // Get all the toolLogFileList where uploadTime not equals to UPDATED_UPLOAD_TIME
        defaultToolLogFileShouldBeFound("uploadTime.notEquals=" + UPDATED_UPLOAD_TIME);
    }

    @Test
    @Transactional
    void getAllToolLogFilesByUploadTimeIsInShouldWork() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        // Get all the toolLogFileList where uploadTime in DEFAULT_UPLOAD_TIME or UPDATED_UPLOAD_TIME
        defaultToolLogFileShouldBeFound("uploadTime.in=" + DEFAULT_UPLOAD_TIME + "," + UPDATED_UPLOAD_TIME);

        // Get all the toolLogFileList where uploadTime equals to UPDATED_UPLOAD_TIME
        defaultToolLogFileShouldNotBeFound("uploadTime.in=" + UPDATED_UPLOAD_TIME);
    }

    @Test
    @Transactional
    void getAllToolLogFilesByUploadTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        // Get all the toolLogFileList where uploadTime is not null
        defaultToolLogFileShouldBeFound("uploadTime.specified=true");

        // Get all the toolLogFileList where uploadTime is null
        defaultToolLogFileShouldNotBeFound("uploadTime.specified=false");
    }

    @Test
    @Transactional
    void getAllToolLogFilesByTaskIsEqualToSomething() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);
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
        toolLogFile.setTask(task);
        task.setToolLogFile(toolLogFile);
        toolLogFileRepository.saveAndFlush(toolLogFile);
        Long taskId = task.getId();

        // Get all the toolLogFileList where task equals to taskId
        defaultToolLogFileShouldBeFound("taskId.equals=" + taskId);

        // Get all the toolLogFileList where task equals to (taskId + 1)
        defaultToolLogFileShouldNotBeFound("taskId.equals=" + (taskId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultToolLogFileShouldBeFound(String filter) throws Exception {
        restToolLogFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(toolLogFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dataContentType").value(hasItem(DEFAULT_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].uploadTime").value(hasItem(DEFAULT_UPLOAD_TIME.toString())));

        // Check, that the count call also returns 1
        restToolLogFileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultToolLogFileShouldNotBeFound(String filter) throws Exception {
        restToolLogFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restToolLogFileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingToolLogFile() throws Exception {
        // Get the toolLogFile
        restToolLogFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewToolLogFile() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        int databaseSizeBeforeUpdate = toolLogFileRepository.findAll().size();

        // Update the toolLogFile
        ToolLogFile updatedToolLogFile = toolLogFileRepository.findById(toolLogFile.getId()).get();
        // Disconnect from session so that the updates on updatedToolLogFile are not directly saved in db
        em.detach(updatedToolLogFile);
        updatedToolLogFile
            .fileName(UPDATED_FILE_NAME)
            .description(UPDATED_DESCRIPTION)
            .data(UPDATED_DATA)
            .dataContentType(UPDATED_DATA_CONTENT_TYPE)
            .uploadTime(UPDATED_UPLOAD_TIME);
        ToolLogFileDTO toolLogFileDTO = toolLogFileMapper.toDto(updatedToolLogFile);

        restToolLogFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, toolLogFileDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolLogFileDTO))
            )
            .andExpect(status().isOk());

        // Validate the ToolLogFile in the database
        List<ToolLogFile> toolLogFileList = toolLogFileRepository.findAll();
        assertThat(toolLogFileList).hasSize(databaseSizeBeforeUpdate);
        ToolLogFile testToolLogFile = toolLogFileList.get(toolLogFileList.size() - 1);
        assertThat(testToolLogFile.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testToolLogFile.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testToolLogFile.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testToolLogFile.getDataContentType()).isEqualTo(UPDATED_DATA_CONTENT_TYPE);
        assertThat(testToolLogFile.getUploadTime()).isEqualTo(UPDATED_UPLOAD_TIME);
    }

    @Test
    @Transactional
    void putNonExistingToolLogFile() throws Exception {
        int databaseSizeBeforeUpdate = toolLogFileRepository.findAll().size();
        toolLogFile.setId(count.incrementAndGet());

        // Create the ToolLogFile
        ToolLogFileDTO toolLogFileDTO = toolLogFileMapper.toDto(toolLogFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restToolLogFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, toolLogFileDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolLogFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ToolLogFile in the database
        List<ToolLogFile> toolLogFileList = toolLogFileRepository.findAll();
        assertThat(toolLogFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchToolLogFile() throws Exception {
        int databaseSizeBeforeUpdate = toolLogFileRepository.findAll().size();
        toolLogFile.setId(count.incrementAndGet());

        // Create the ToolLogFile
        ToolLogFileDTO toolLogFileDTO = toolLogFileMapper.toDto(toolLogFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToolLogFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolLogFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ToolLogFile in the database
        List<ToolLogFile> toolLogFileList = toolLogFileRepository.findAll();
        assertThat(toolLogFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamToolLogFile() throws Exception {
        int databaseSizeBeforeUpdate = toolLogFileRepository.findAll().size();
        toolLogFile.setId(count.incrementAndGet());

        // Create the ToolLogFile
        ToolLogFileDTO toolLogFileDTO = toolLogFileMapper.toDto(toolLogFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToolLogFileMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toolLogFileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ToolLogFile in the database
        List<ToolLogFile> toolLogFileList = toolLogFileRepository.findAll();
        assertThat(toolLogFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateToolLogFileWithPatch() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        int databaseSizeBeforeUpdate = toolLogFileRepository.findAll().size();

        // Update the toolLogFile using partial update
        ToolLogFile partialUpdatedToolLogFile = new ToolLogFile();
        partialUpdatedToolLogFile.setId(toolLogFile.getId());

        partialUpdatedToolLogFile.description(UPDATED_DESCRIPTION).uploadTime(UPDATED_UPLOAD_TIME);

        restToolLogFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedToolLogFile.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedToolLogFile))
            )
            .andExpect(status().isOk());

        // Validate the ToolLogFile in the database
        List<ToolLogFile> toolLogFileList = toolLogFileRepository.findAll();
        assertThat(toolLogFileList).hasSize(databaseSizeBeforeUpdate);
        ToolLogFile testToolLogFile = toolLogFileList.get(toolLogFileList.size() - 1);
        assertThat(testToolLogFile.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testToolLogFile.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testToolLogFile.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testToolLogFile.getDataContentType()).isEqualTo(DEFAULT_DATA_CONTENT_TYPE);
        assertThat(testToolLogFile.getUploadTime()).isEqualTo(UPDATED_UPLOAD_TIME);
    }

    @Test
    @Transactional
    void fullUpdateToolLogFileWithPatch() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        int databaseSizeBeforeUpdate = toolLogFileRepository.findAll().size();

        // Update the toolLogFile using partial update
        ToolLogFile partialUpdatedToolLogFile = new ToolLogFile();
        partialUpdatedToolLogFile.setId(toolLogFile.getId());

        partialUpdatedToolLogFile
            .fileName(UPDATED_FILE_NAME)
            .description(UPDATED_DESCRIPTION)
            .data(UPDATED_DATA)
            .dataContentType(UPDATED_DATA_CONTENT_TYPE)
            .uploadTime(UPDATED_UPLOAD_TIME);

        restToolLogFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedToolLogFile.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedToolLogFile))
            )
            .andExpect(status().isOk());

        // Validate the ToolLogFile in the database
        List<ToolLogFile> toolLogFileList = toolLogFileRepository.findAll();
        assertThat(toolLogFileList).hasSize(databaseSizeBeforeUpdate);
        ToolLogFile testToolLogFile = toolLogFileList.get(toolLogFileList.size() - 1);
        assertThat(testToolLogFile.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testToolLogFile.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testToolLogFile.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testToolLogFile.getDataContentType()).isEqualTo(UPDATED_DATA_CONTENT_TYPE);
        assertThat(testToolLogFile.getUploadTime()).isEqualTo(UPDATED_UPLOAD_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingToolLogFile() throws Exception {
        int databaseSizeBeforeUpdate = toolLogFileRepository.findAll().size();
        toolLogFile.setId(count.incrementAndGet());

        // Create the ToolLogFile
        ToolLogFileDTO toolLogFileDTO = toolLogFileMapper.toDto(toolLogFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restToolLogFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, toolLogFileDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(toolLogFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ToolLogFile in the database
        List<ToolLogFile> toolLogFileList = toolLogFileRepository.findAll();
        assertThat(toolLogFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchToolLogFile() throws Exception {
        int databaseSizeBeforeUpdate = toolLogFileRepository.findAll().size();
        toolLogFile.setId(count.incrementAndGet());

        // Create the ToolLogFile
        ToolLogFileDTO toolLogFileDTO = toolLogFileMapper.toDto(toolLogFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToolLogFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(toolLogFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ToolLogFile in the database
        List<ToolLogFile> toolLogFileList = toolLogFileRepository.findAll();
        assertThat(toolLogFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamToolLogFile() throws Exception {
        int databaseSizeBeforeUpdate = toolLogFileRepository.findAll().size();
        toolLogFile.setId(count.incrementAndGet());

        // Create the ToolLogFile
        ToolLogFileDTO toolLogFileDTO = toolLogFileMapper.toDto(toolLogFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToolLogFileMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(toolLogFileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ToolLogFile in the database
        List<ToolLogFile> toolLogFileList = toolLogFileRepository.findAll();
        assertThat(toolLogFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteToolLogFile() throws Exception {
        // Initialize the database
        toolLogFileRepository.saveAndFlush(toolLogFile);

        int databaseSizeBeforeDelete = toolLogFileRepository.findAll().size();

        // Delete the toolLogFile
        restToolLogFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, toolLogFile.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ToolLogFile> toolLogFileList = toolLogFileRepository.findAll();
        assertThat(toolLogFileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
