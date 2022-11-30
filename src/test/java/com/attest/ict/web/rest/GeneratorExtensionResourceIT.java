package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Generator;
import com.attest.ict.domain.GeneratorExtension;
import com.attest.ict.repository.GeneratorExtensionRepository;
import com.attest.ict.service.criteria.GeneratorExtensionCriteria;
import com.attest.ict.service.dto.GeneratorExtensionDTO;
import com.attest.ict.service.mapper.GeneratorExtensionMapper;
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
 * Integration tests for the {@link GeneratorExtensionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GeneratorExtensionResourceIT {

    private static final Integer DEFAULT_ID_GEN = 1;
    private static final Integer UPDATED_ID_GEN = 2;
    private static final Integer SMALLER_ID_GEN = 1 - 1;

    private static final Integer DEFAULT_STATUS_CURT = 1;
    private static final Integer UPDATED_STATUS_CURT = 2;
    private static final Integer SMALLER_STATUS_CURT = 1 - 1;

    private static final Integer DEFAULT_DG_TYPE = 1;
    private static final Integer UPDATED_DG_TYPE = 2;
    private static final Integer SMALLER_DG_TYPE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/generator-extensions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GeneratorExtensionRepository generatorExtensionRepository;

    @Autowired
    private GeneratorExtensionMapper generatorExtensionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGeneratorExtensionMockMvc;

    private GeneratorExtension generatorExtension;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeneratorExtension createEntity(EntityManager em) {
        GeneratorExtension generatorExtension = new GeneratorExtension()
            .idGen(DEFAULT_ID_GEN)
            .statusCurt(DEFAULT_STATUS_CURT)
            .dgType(DEFAULT_DG_TYPE);
        return generatorExtension;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeneratorExtension createUpdatedEntity(EntityManager em) {
        GeneratorExtension generatorExtension = new GeneratorExtension()
            .idGen(UPDATED_ID_GEN)
            .statusCurt(UPDATED_STATUS_CURT)
            .dgType(UPDATED_DG_TYPE);
        return generatorExtension;
    }

    @BeforeEach
    public void initTest() {
        generatorExtension = createEntity(em);
    }

    @Test
    @Transactional
    void createGeneratorExtension() throws Exception {
        int databaseSizeBeforeCreate = generatorExtensionRepository.findAll().size();
        // Create the GeneratorExtension
        GeneratorExtensionDTO generatorExtensionDTO = generatorExtensionMapper.toDto(generatorExtension);
        restGeneratorExtensionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generatorExtensionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the GeneratorExtension in the database
        List<GeneratorExtension> generatorExtensionList = generatorExtensionRepository.findAll();
        assertThat(generatorExtensionList).hasSize(databaseSizeBeforeCreate + 1);
        GeneratorExtension testGeneratorExtension = generatorExtensionList.get(generatorExtensionList.size() - 1);
        assertThat(testGeneratorExtension.getIdGen()).isEqualTo(DEFAULT_ID_GEN);
        assertThat(testGeneratorExtension.getStatusCurt()).isEqualTo(DEFAULT_STATUS_CURT);
        assertThat(testGeneratorExtension.getDgType()).isEqualTo(DEFAULT_DG_TYPE);
    }

    @Test
    @Transactional
    void createGeneratorExtensionWithExistingId() throws Exception {
        // Create the GeneratorExtension with an existing ID
        generatorExtension.setId(1L);
        GeneratorExtensionDTO generatorExtensionDTO = generatorExtensionMapper.toDto(generatorExtension);

        int databaseSizeBeforeCreate = generatorExtensionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeneratorExtensionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generatorExtensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeneratorExtension in the database
        List<GeneratorExtension> generatorExtensionList = generatorExtensionRepository.findAll();
        assertThat(generatorExtensionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensions() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList
        restGeneratorExtensionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(generatorExtension.getId().intValue())))
            .andExpect(jsonPath("$.[*].idGen").value(hasItem(DEFAULT_ID_GEN)))
            .andExpect(jsonPath("$.[*].statusCurt").value(hasItem(DEFAULT_STATUS_CURT)))
            .andExpect(jsonPath("$.[*].dgType").value(hasItem(DEFAULT_DG_TYPE)));
    }

    @Test
    @Transactional
    void getGeneratorExtension() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get the generatorExtension
        restGeneratorExtensionMockMvc
            .perform(get(ENTITY_API_URL_ID, generatorExtension.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(generatorExtension.getId().intValue()))
            .andExpect(jsonPath("$.idGen").value(DEFAULT_ID_GEN))
            .andExpect(jsonPath("$.statusCurt").value(DEFAULT_STATUS_CURT))
            .andExpect(jsonPath("$.dgType").value(DEFAULT_DG_TYPE));
    }

    @Test
    @Transactional
    void getGeneratorExtensionsByIdFiltering() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        Long id = generatorExtension.getId();

        defaultGeneratorExtensionShouldBeFound("id.equals=" + id);
        defaultGeneratorExtensionShouldNotBeFound("id.notEquals=" + id);

        defaultGeneratorExtensionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGeneratorExtensionShouldNotBeFound("id.greaterThan=" + id);

        defaultGeneratorExtensionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGeneratorExtensionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByIdGenIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where idGen equals to DEFAULT_ID_GEN
        defaultGeneratorExtensionShouldBeFound("idGen.equals=" + DEFAULT_ID_GEN);

        // Get all the generatorExtensionList where idGen equals to UPDATED_ID_GEN
        defaultGeneratorExtensionShouldNotBeFound("idGen.equals=" + UPDATED_ID_GEN);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByIdGenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where idGen not equals to DEFAULT_ID_GEN
        defaultGeneratorExtensionShouldNotBeFound("idGen.notEquals=" + DEFAULT_ID_GEN);

        // Get all the generatorExtensionList where idGen not equals to UPDATED_ID_GEN
        defaultGeneratorExtensionShouldBeFound("idGen.notEquals=" + UPDATED_ID_GEN);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByIdGenIsInShouldWork() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where idGen in DEFAULT_ID_GEN or UPDATED_ID_GEN
        defaultGeneratorExtensionShouldBeFound("idGen.in=" + DEFAULT_ID_GEN + "," + UPDATED_ID_GEN);

        // Get all the generatorExtensionList where idGen equals to UPDATED_ID_GEN
        defaultGeneratorExtensionShouldNotBeFound("idGen.in=" + UPDATED_ID_GEN);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByIdGenIsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where idGen is not null
        defaultGeneratorExtensionShouldBeFound("idGen.specified=true");

        // Get all the generatorExtensionList where idGen is null
        defaultGeneratorExtensionShouldNotBeFound("idGen.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByIdGenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where idGen is greater than or equal to DEFAULT_ID_GEN
        defaultGeneratorExtensionShouldBeFound("idGen.greaterThanOrEqual=" + DEFAULT_ID_GEN);

        // Get all the generatorExtensionList where idGen is greater than or equal to UPDATED_ID_GEN
        defaultGeneratorExtensionShouldNotBeFound("idGen.greaterThanOrEqual=" + UPDATED_ID_GEN);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByIdGenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where idGen is less than or equal to DEFAULT_ID_GEN
        defaultGeneratorExtensionShouldBeFound("idGen.lessThanOrEqual=" + DEFAULT_ID_GEN);

        // Get all the generatorExtensionList where idGen is less than or equal to SMALLER_ID_GEN
        defaultGeneratorExtensionShouldNotBeFound("idGen.lessThanOrEqual=" + SMALLER_ID_GEN);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByIdGenIsLessThanSomething() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where idGen is less than DEFAULT_ID_GEN
        defaultGeneratorExtensionShouldNotBeFound("idGen.lessThan=" + DEFAULT_ID_GEN);

        // Get all the generatorExtensionList where idGen is less than UPDATED_ID_GEN
        defaultGeneratorExtensionShouldBeFound("idGen.lessThan=" + UPDATED_ID_GEN);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByIdGenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where idGen is greater than DEFAULT_ID_GEN
        defaultGeneratorExtensionShouldNotBeFound("idGen.greaterThan=" + DEFAULT_ID_GEN);

        // Get all the generatorExtensionList where idGen is greater than SMALLER_ID_GEN
        defaultGeneratorExtensionShouldBeFound("idGen.greaterThan=" + SMALLER_ID_GEN);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByStatusCurtIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where statusCurt equals to DEFAULT_STATUS_CURT
        defaultGeneratorExtensionShouldBeFound("statusCurt.equals=" + DEFAULT_STATUS_CURT);

        // Get all the generatorExtensionList where statusCurt equals to UPDATED_STATUS_CURT
        defaultGeneratorExtensionShouldNotBeFound("statusCurt.equals=" + UPDATED_STATUS_CURT);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByStatusCurtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where statusCurt not equals to DEFAULT_STATUS_CURT
        defaultGeneratorExtensionShouldNotBeFound("statusCurt.notEquals=" + DEFAULT_STATUS_CURT);

        // Get all the generatorExtensionList where statusCurt not equals to UPDATED_STATUS_CURT
        defaultGeneratorExtensionShouldBeFound("statusCurt.notEquals=" + UPDATED_STATUS_CURT);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByStatusCurtIsInShouldWork() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where statusCurt in DEFAULT_STATUS_CURT or UPDATED_STATUS_CURT
        defaultGeneratorExtensionShouldBeFound("statusCurt.in=" + DEFAULT_STATUS_CURT + "," + UPDATED_STATUS_CURT);

        // Get all the generatorExtensionList where statusCurt equals to UPDATED_STATUS_CURT
        defaultGeneratorExtensionShouldNotBeFound("statusCurt.in=" + UPDATED_STATUS_CURT);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByStatusCurtIsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where statusCurt is not null
        defaultGeneratorExtensionShouldBeFound("statusCurt.specified=true");

        // Get all the generatorExtensionList where statusCurt is null
        defaultGeneratorExtensionShouldNotBeFound("statusCurt.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByStatusCurtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where statusCurt is greater than or equal to DEFAULT_STATUS_CURT
        defaultGeneratorExtensionShouldBeFound("statusCurt.greaterThanOrEqual=" + DEFAULT_STATUS_CURT);

        // Get all the generatorExtensionList where statusCurt is greater than or equal to UPDATED_STATUS_CURT
        defaultGeneratorExtensionShouldNotBeFound("statusCurt.greaterThanOrEqual=" + UPDATED_STATUS_CURT);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByStatusCurtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where statusCurt is less than or equal to DEFAULT_STATUS_CURT
        defaultGeneratorExtensionShouldBeFound("statusCurt.lessThanOrEqual=" + DEFAULT_STATUS_CURT);

        // Get all the generatorExtensionList where statusCurt is less than or equal to SMALLER_STATUS_CURT
        defaultGeneratorExtensionShouldNotBeFound("statusCurt.lessThanOrEqual=" + SMALLER_STATUS_CURT);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByStatusCurtIsLessThanSomething() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where statusCurt is less than DEFAULT_STATUS_CURT
        defaultGeneratorExtensionShouldNotBeFound("statusCurt.lessThan=" + DEFAULT_STATUS_CURT);

        // Get all the generatorExtensionList where statusCurt is less than UPDATED_STATUS_CURT
        defaultGeneratorExtensionShouldBeFound("statusCurt.lessThan=" + UPDATED_STATUS_CURT);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByStatusCurtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where statusCurt is greater than DEFAULT_STATUS_CURT
        defaultGeneratorExtensionShouldNotBeFound("statusCurt.greaterThan=" + DEFAULT_STATUS_CURT);

        // Get all the generatorExtensionList where statusCurt is greater than SMALLER_STATUS_CURT
        defaultGeneratorExtensionShouldBeFound("statusCurt.greaterThan=" + SMALLER_STATUS_CURT);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByDgTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where dgType equals to DEFAULT_DG_TYPE
        defaultGeneratorExtensionShouldBeFound("dgType.equals=" + DEFAULT_DG_TYPE);

        // Get all the generatorExtensionList where dgType equals to UPDATED_DG_TYPE
        defaultGeneratorExtensionShouldNotBeFound("dgType.equals=" + UPDATED_DG_TYPE);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByDgTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where dgType not equals to DEFAULT_DG_TYPE
        defaultGeneratorExtensionShouldNotBeFound("dgType.notEquals=" + DEFAULT_DG_TYPE);

        // Get all the generatorExtensionList where dgType not equals to UPDATED_DG_TYPE
        defaultGeneratorExtensionShouldBeFound("dgType.notEquals=" + UPDATED_DG_TYPE);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByDgTypeIsInShouldWork() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where dgType in DEFAULT_DG_TYPE or UPDATED_DG_TYPE
        defaultGeneratorExtensionShouldBeFound("dgType.in=" + DEFAULT_DG_TYPE + "," + UPDATED_DG_TYPE);

        // Get all the generatorExtensionList where dgType equals to UPDATED_DG_TYPE
        defaultGeneratorExtensionShouldNotBeFound("dgType.in=" + UPDATED_DG_TYPE);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByDgTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where dgType is not null
        defaultGeneratorExtensionShouldBeFound("dgType.specified=true");

        // Get all the generatorExtensionList where dgType is null
        defaultGeneratorExtensionShouldNotBeFound("dgType.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByDgTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where dgType is greater than or equal to DEFAULT_DG_TYPE
        defaultGeneratorExtensionShouldBeFound("dgType.greaterThanOrEqual=" + DEFAULT_DG_TYPE);

        // Get all the generatorExtensionList where dgType is greater than or equal to UPDATED_DG_TYPE
        defaultGeneratorExtensionShouldNotBeFound("dgType.greaterThanOrEqual=" + UPDATED_DG_TYPE);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByDgTypeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where dgType is less than or equal to DEFAULT_DG_TYPE
        defaultGeneratorExtensionShouldBeFound("dgType.lessThanOrEqual=" + DEFAULT_DG_TYPE);

        // Get all the generatorExtensionList where dgType is less than or equal to SMALLER_DG_TYPE
        defaultGeneratorExtensionShouldNotBeFound("dgType.lessThanOrEqual=" + SMALLER_DG_TYPE);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByDgTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where dgType is less than DEFAULT_DG_TYPE
        defaultGeneratorExtensionShouldNotBeFound("dgType.lessThan=" + DEFAULT_DG_TYPE);

        // Get all the generatorExtensionList where dgType is less than UPDATED_DG_TYPE
        defaultGeneratorExtensionShouldBeFound("dgType.lessThan=" + UPDATED_DG_TYPE);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByDgTypeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        // Get all the generatorExtensionList where dgType is greater than DEFAULT_DG_TYPE
        defaultGeneratorExtensionShouldNotBeFound("dgType.greaterThan=" + DEFAULT_DG_TYPE);

        // Get all the generatorExtensionList where dgType is greater than SMALLER_DG_TYPE
        defaultGeneratorExtensionShouldBeFound("dgType.greaterThan=" + SMALLER_DG_TYPE);
    }

    @Test
    @Transactional
    void getAllGeneratorExtensionsByGeneratorIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);
        Generator generator;
        if (TestUtil.findAll(em, Generator.class).isEmpty()) {
            generator = GeneratorResourceIT.createEntity(em);
            em.persist(generator);
            em.flush();
        } else {
            generator = TestUtil.findAll(em, Generator.class).get(0);
        }
        em.persist(generator);
        em.flush();
        generatorExtension.setGenerator(generator);
        generatorExtensionRepository.saveAndFlush(generatorExtension);
        Long generatorId = generator.getId();

        // Get all the generatorExtensionList where generator equals to generatorId
        defaultGeneratorExtensionShouldBeFound("generatorId.equals=" + generatorId);

        // Get all the generatorExtensionList where generator equals to (generatorId + 1)
        defaultGeneratorExtensionShouldNotBeFound("generatorId.equals=" + (generatorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGeneratorExtensionShouldBeFound(String filter) throws Exception {
        restGeneratorExtensionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(generatorExtension.getId().intValue())))
            .andExpect(jsonPath("$.[*].idGen").value(hasItem(DEFAULT_ID_GEN)))
            .andExpect(jsonPath("$.[*].statusCurt").value(hasItem(DEFAULT_STATUS_CURT)))
            .andExpect(jsonPath("$.[*].dgType").value(hasItem(DEFAULT_DG_TYPE)));

        // Check, that the count call also returns 1
        restGeneratorExtensionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGeneratorExtensionShouldNotBeFound(String filter) throws Exception {
        restGeneratorExtensionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGeneratorExtensionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGeneratorExtension() throws Exception {
        // Get the generatorExtension
        restGeneratorExtensionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGeneratorExtension() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        int databaseSizeBeforeUpdate = generatorExtensionRepository.findAll().size();

        // Update the generatorExtension
        GeneratorExtension updatedGeneratorExtension = generatorExtensionRepository.findById(generatorExtension.getId()).get();
        // Disconnect from session so that the updates on updatedGeneratorExtension are not directly saved in db
        em.detach(updatedGeneratorExtension);
        updatedGeneratorExtension.idGen(UPDATED_ID_GEN).statusCurt(UPDATED_STATUS_CURT).dgType(UPDATED_DG_TYPE);
        GeneratorExtensionDTO generatorExtensionDTO = generatorExtensionMapper.toDto(updatedGeneratorExtension);

        restGeneratorExtensionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, generatorExtensionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generatorExtensionDTO))
            )
            .andExpect(status().isOk());

        // Validate the GeneratorExtension in the database
        List<GeneratorExtension> generatorExtensionList = generatorExtensionRepository.findAll();
        assertThat(generatorExtensionList).hasSize(databaseSizeBeforeUpdate);
        GeneratorExtension testGeneratorExtension = generatorExtensionList.get(generatorExtensionList.size() - 1);
        assertThat(testGeneratorExtension.getIdGen()).isEqualTo(UPDATED_ID_GEN);
        assertThat(testGeneratorExtension.getStatusCurt()).isEqualTo(UPDATED_STATUS_CURT);
        assertThat(testGeneratorExtension.getDgType()).isEqualTo(UPDATED_DG_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingGeneratorExtension() throws Exception {
        int databaseSizeBeforeUpdate = generatorExtensionRepository.findAll().size();
        generatorExtension.setId(count.incrementAndGet());

        // Create the GeneratorExtension
        GeneratorExtensionDTO generatorExtensionDTO = generatorExtensionMapper.toDto(generatorExtension);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeneratorExtensionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, generatorExtensionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generatorExtensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeneratorExtension in the database
        List<GeneratorExtension> generatorExtensionList = generatorExtensionRepository.findAll();
        assertThat(generatorExtensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGeneratorExtension() throws Exception {
        int databaseSizeBeforeUpdate = generatorExtensionRepository.findAll().size();
        generatorExtension.setId(count.incrementAndGet());

        // Create the GeneratorExtension
        GeneratorExtensionDTO generatorExtensionDTO = generatorExtensionMapper.toDto(generatorExtension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneratorExtensionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generatorExtensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeneratorExtension in the database
        List<GeneratorExtension> generatorExtensionList = generatorExtensionRepository.findAll();
        assertThat(generatorExtensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGeneratorExtension() throws Exception {
        int databaseSizeBeforeUpdate = generatorExtensionRepository.findAll().size();
        generatorExtension.setId(count.incrementAndGet());

        // Create the GeneratorExtension
        GeneratorExtensionDTO generatorExtensionDTO = generatorExtensionMapper.toDto(generatorExtension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneratorExtensionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generatorExtensionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GeneratorExtension in the database
        List<GeneratorExtension> generatorExtensionList = generatorExtensionRepository.findAll();
        assertThat(generatorExtensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGeneratorExtensionWithPatch() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        int databaseSizeBeforeUpdate = generatorExtensionRepository.findAll().size();

        // Update the generatorExtension using partial update
        GeneratorExtension partialUpdatedGeneratorExtension = new GeneratorExtension();
        partialUpdatedGeneratorExtension.setId(generatorExtension.getId());

        partialUpdatedGeneratorExtension.statusCurt(UPDATED_STATUS_CURT).dgType(UPDATED_DG_TYPE);

        restGeneratorExtensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeneratorExtension.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGeneratorExtension))
            )
            .andExpect(status().isOk());

        // Validate the GeneratorExtension in the database
        List<GeneratorExtension> generatorExtensionList = generatorExtensionRepository.findAll();
        assertThat(generatorExtensionList).hasSize(databaseSizeBeforeUpdate);
        GeneratorExtension testGeneratorExtension = generatorExtensionList.get(generatorExtensionList.size() - 1);
        assertThat(testGeneratorExtension.getIdGen()).isEqualTo(DEFAULT_ID_GEN);
        assertThat(testGeneratorExtension.getStatusCurt()).isEqualTo(UPDATED_STATUS_CURT);
        assertThat(testGeneratorExtension.getDgType()).isEqualTo(UPDATED_DG_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateGeneratorExtensionWithPatch() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        int databaseSizeBeforeUpdate = generatorExtensionRepository.findAll().size();

        // Update the generatorExtension using partial update
        GeneratorExtension partialUpdatedGeneratorExtension = new GeneratorExtension();
        partialUpdatedGeneratorExtension.setId(generatorExtension.getId());

        partialUpdatedGeneratorExtension.idGen(UPDATED_ID_GEN).statusCurt(UPDATED_STATUS_CURT).dgType(UPDATED_DG_TYPE);

        restGeneratorExtensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeneratorExtension.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGeneratorExtension))
            )
            .andExpect(status().isOk());

        // Validate the GeneratorExtension in the database
        List<GeneratorExtension> generatorExtensionList = generatorExtensionRepository.findAll();
        assertThat(generatorExtensionList).hasSize(databaseSizeBeforeUpdate);
        GeneratorExtension testGeneratorExtension = generatorExtensionList.get(generatorExtensionList.size() - 1);
        assertThat(testGeneratorExtension.getIdGen()).isEqualTo(UPDATED_ID_GEN);
        assertThat(testGeneratorExtension.getStatusCurt()).isEqualTo(UPDATED_STATUS_CURT);
        assertThat(testGeneratorExtension.getDgType()).isEqualTo(UPDATED_DG_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingGeneratorExtension() throws Exception {
        int databaseSizeBeforeUpdate = generatorExtensionRepository.findAll().size();
        generatorExtension.setId(count.incrementAndGet());

        // Create the GeneratorExtension
        GeneratorExtensionDTO generatorExtensionDTO = generatorExtensionMapper.toDto(generatorExtension);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeneratorExtensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, generatorExtensionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(generatorExtensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeneratorExtension in the database
        List<GeneratorExtension> generatorExtensionList = generatorExtensionRepository.findAll();
        assertThat(generatorExtensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGeneratorExtension() throws Exception {
        int databaseSizeBeforeUpdate = generatorExtensionRepository.findAll().size();
        generatorExtension.setId(count.incrementAndGet());

        // Create the GeneratorExtension
        GeneratorExtensionDTO generatorExtensionDTO = generatorExtensionMapper.toDto(generatorExtension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneratorExtensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(generatorExtensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeneratorExtension in the database
        List<GeneratorExtension> generatorExtensionList = generatorExtensionRepository.findAll();
        assertThat(generatorExtensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGeneratorExtension() throws Exception {
        int databaseSizeBeforeUpdate = generatorExtensionRepository.findAll().size();
        generatorExtension.setId(count.incrementAndGet());

        // Create the GeneratorExtension
        GeneratorExtensionDTO generatorExtensionDTO = generatorExtensionMapper.toDto(generatorExtension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneratorExtensionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(generatorExtensionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GeneratorExtension in the database
        List<GeneratorExtension> generatorExtensionList = generatorExtensionRepository.findAll();
        assertThat(generatorExtensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGeneratorExtension() throws Exception {
        // Initialize the database
        generatorExtensionRepository.saveAndFlush(generatorExtension);

        int databaseSizeBeforeDelete = generatorExtensionRepository.findAll().size();

        // Delete the generatorExtension
        restGeneratorExtensionMockMvc
            .perform(delete(ENTITY_API_URL_ID, generatorExtension.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GeneratorExtension> generatorExtensionList = generatorExtensionRepository.findAll();
        assertThat(generatorExtensionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
