package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.GenCost;
import com.attest.ict.domain.Generator;
import com.attest.ict.repository.GenCostRepository;
import com.attest.ict.service.criteria.GenCostCriteria;
import com.attest.ict.service.dto.GenCostDTO;
import com.attest.ict.service.mapper.GenCostMapper;
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
 * Integration tests for the {@link GenCostResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GenCostResourceIT {

    private static final Integer DEFAULT_MODEL = 1;
    private static final Integer UPDATED_MODEL = 2;
    private static final Integer SMALLER_MODEL = 1 - 1;

    private static final Double DEFAULT_STARTUP = 1D;
    private static final Double UPDATED_STARTUP = 2D;
    private static final Double SMALLER_STARTUP = 1D - 1D;

    private static final Double DEFAULT_SHUTDOWN = 1D;
    private static final Double UPDATED_SHUTDOWN = 2D;
    private static final Double SMALLER_SHUTDOWN = 1D - 1D;

    private static final Long DEFAULT_N_COST = 1L;
    private static final Long UPDATED_N_COST = 2L;
    private static final Long SMALLER_N_COST = 1L - 1L;

    private static final String DEFAULT_COST_PF = "AAAAAAAAAA";
    private static final String UPDATED_COST_PF = "BBBBBBBBBB";

    private static final String DEFAULT_COST_QF = "AAAAAAAAAA";
    private static final String UPDATED_COST_QF = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/gen-costs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GenCostRepository genCostRepository;

    @Autowired
    private GenCostMapper genCostMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGenCostMockMvc;

    private GenCost genCost;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GenCost createEntity(EntityManager em) {
        GenCost genCost = new GenCost()
            .model(DEFAULT_MODEL)
            .startup(DEFAULT_STARTUP)
            .shutdown(DEFAULT_SHUTDOWN)
            .nCost(DEFAULT_N_COST)
            .costPF(DEFAULT_COST_PF)
            .costQF(DEFAULT_COST_QF);
        return genCost;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GenCost createUpdatedEntity(EntityManager em) {
        GenCost genCost = new GenCost()
            .model(UPDATED_MODEL)
            .startup(UPDATED_STARTUP)
            .shutdown(UPDATED_SHUTDOWN)
            .nCost(UPDATED_N_COST)
            .costPF(UPDATED_COST_PF)
            .costQF(UPDATED_COST_QF);
        return genCost;
    }

    @BeforeEach
    public void initTest() {
        genCost = createEntity(em);
    }

    @Test
    @Transactional
    void createGenCost() throws Exception {
        int databaseSizeBeforeCreate = genCostRepository.findAll().size();
        // Create the GenCost
        GenCostDTO genCostDTO = genCostMapper.toDto(genCost);
        restGenCostMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genCostDTO))
            )
            .andExpect(status().isCreated());

        // Validate the GenCost in the database
        List<GenCost> genCostList = genCostRepository.findAll();
        assertThat(genCostList).hasSize(databaseSizeBeforeCreate + 1);
        GenCost testGenCost = genCostList.get(genCostList.size() - 1);
        assertThat(testGenCost.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testGenCost.getStartup()).isEqualTo(DEFAULT_STARTUP);
        assertThat(testGenCost.getShutdown()).isEqualTo(DEFAULT_SHUTDOWN);
        assertThat(testGenCost.getnCost()).isEqualTo(DEFAULT_N_COST);
        assertThat(testGenCost.getCostPF()).isEqualTo(DEFAULT_COST_PF);
        assertThat(testGenCost.getCostQF()).isEqualTo(DEFAULT_COST_QF);
    }

    @Test
    @Transactional
    void createGenCostWithExistingId() throws Exception {
        // Create the GenCost with an existing ID
        genCost.setId(1L);
        GenCostDTO genCostDTO = genCostMapper.toDto(genCost);

        int databaseSizeBeforeCreate = genCostRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGenCostMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genCostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenCost in the database
        List<GenCost> genCostList = genCostRepository.findAll();
        assertThat(genCostList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGenCosts() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList
        restGenCostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genCost.getId().intValue())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].startup").value(hasItem(DEFAULT_STARTUP.doubleValue())))
            .andExpect(jsonPath("$.[*].shutdown").value(hasItem(DEFAULT_SHUTDOWN.doubleValue())))
            .andExpect(jsonPath("$.[*].nCost").value(hasItem(DEFAULT_N_COST.intValue())))
            .andExpect(jsonPath("$.[*].costPF").value(hasItem(DEFAULT_COST_PF)))
            .andExpect(jsonPath("$.[*].costQF").value(hasItem(DEFAULT_COST_QF)));
    }

    @Test
    @Transactional
    void getGenCost() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get the genCost
        restGenCostMockMvc
            .perform(get(ENTITY_API_URL_ID, genCost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(genCost.getId().intValue()))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.startup").value(DEFAULT_STARTUP.doubleValue()))
            .andExpect(jsonPath("$.shutdown").value(DEFAULT_SHUTDOWN.doubleValue()))
            .andExpect(jsonPath("$.nCost").value(DEFAULT_N_COST.intValue()))
            .andExpect(jsonPath("$.costPF").value(DEFAULT_COST_PF))
            .andExpect(jsonPath("$.costQF").value(DEFAULT_COST_QF));
    }

    @Test
    @Transactional
    void getGenCostsByIdFiltering() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        Long id = genCost.getId();

        defaultGenCostShouldBeFound("id.equals=" + id);
        defaultGenCostShouldNotBeFound("id.notEquals=" + id);

        defaultGenCostShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGenCostShouldNotBeFound("id.greaterThan=" + id);

        defaultGenCostShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGenCostShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGenCostsByModelIsEqualToSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where model equals to DEFAULT_MODEL
        defaultGenCostShouldBeFound("model.equals=" + DEFAULT_MODEL);

        // Get all the genCostList where model equals to UPDATED_MODEL
        defaultGenCostShouldNotBeFound("model.equals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllGenCostsByModelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where model not equals to DEFAULT_MODEL
        defaultGenCostShouldNotBeFound("model.notEquals=" + DEFAULT_MODEL);

        // Get all the genCostList where model not equals to UPDATED_MODEL
        defaultGenCostShouldBeFound("model.notEquals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllGenCostsByModelIsInShouldWork() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where model in DEFAULT_MODEL or UPDATED_MODEL
        defaultGenCostShouldBeFound("model.in=" + DEFAULT_MODEL + "," + UPDATED_MODEL);

        // Get all the genCostList where model equals to UPDATED_MODEL
        defaultGenCostShouldNotBeFound("model.in=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllGenCostsByModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where model is not null
        defaultGenCostShouldBeFound("model.specified=true");

        // Get all the genCostList where model is null
        defaultGenCostShouldNotBeFound("model.specified=false");
    }

    @Test
    @Transactional
    void getAllGenCostsByModelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where model is greater than or equal to DEFAULT_MODEL
        defaultGenCostShouldBeFound("model.greaterThanOrEqual=" + DEFAULT_MODEL);

        // Get all the genCostList where model is greater than or equal to UPDATED_MODEL
        defaultGenCostShouldNotBeFound("model.greaterThanOrEqual=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllGenCostsByModelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where model is less than or equal to DEFAULT_MODEL
        defaultGenCostShouldBeFound("model.lessThanOrEqual=" + DEFAULT_MODEL);

        // Get all the genCostList where model is less than or equal to SMALLER_MODEL
        defaultGenCostShouldNotBeFound("model.lessThanOrEqual=" + SMALLER_MODEL);
    }

    @Test
    @Transactional
    void getAllGenCostsByModelIsLessThanSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where model is less than DEFAULT_MODEL
        defaultGenCostShouldNotBeFound("model.lessThan=" + DEFAULT_MODEL);

        // Get all the genCostList where model is less than UPDATED_MODEL
        defaultGenCostShouldBeFound("model.lessThan=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllGenCostsByModelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where model is greater than DEFAULT_MODEL
        defaultGenCostShouldNotBeFound("model.greaterThan=" + DEFAULT_MODEL);

        // Get all the genCostList where model is greater than SMALLER_MODEL
        defaultGenCostShouldBeFound("model.greaterThan=" + SMALLER_MODEL);
    }

    @Test
    @Transactional
    void getAllGenCostsByStartupIsEqualToSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where startup equals to DEFAULT_STARTUP
        defaultGenCostShouldBeFound("startup.equals=" + DEFAULT_STARTUP);

        // Get all the genCostList where startup equals to UPDATED_STARTUP
        defaultGenCostShouldNotBeFound("startup.equals=" + UPDATED_STARTUP);
    }

    @Test
    @Transactional
    void getAllGenCostsByStartupIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where startup not equals to DEFAULT_STARTUP
        defaultGenCostShouldNotBeFound("startup.notEquals=" + DEFAULT_STARTUP);

        // Get all the genCostList where startup not equals to UPDATED_STARTUP
        defaultGenCostShouldBeFound("startup.notEquals=" + UPDATED_STARTUP);
    }

    @Test
    @Transactional
    void getAllGenCostsByStartupIsInShouldWork() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where startup in DEFAULT_STARTUP or UPDATED_STARTUP
        defaultGenCostShouldBeFound("startup.in=" + DEFAULT_STARTUP + "," + UPDATED_STARTUP);

        // Get all the genCostList where startup equals to UPDATED_STARTUP
        defaultGenCostShouldNotBeFound("startup.in=" + UPDATED_STARTUP);
    }

    @Test
    @Transactional
    void getAllGenCostsByStartupIsNullOrNotNull() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where startup is not null
        defaultGenCostShouldBeFound("startup.specified=true");

        // Get all the genCostList where startup is null
        defaultGenCostShouldNotBeFound("startup.specified=false");
    }

    @Test
    @Transactional
    void getAllGenCostsByStartupIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where startup is greater than or equal to DEFAULT_STARTUP
        defaultGenCostShouldBeFound("startup.greaterThanOrEqual=" + DEFAULT_STARTUP);

        // Get all the genCostList where startup is greater than or equal to UPDATED_STARTUP
        defaultGenCostShouldNotBeFound("startup.greaterThanOrEqual=" + UPDATED_STARTUP);
    }

    @Test
    @Transactional
    void getAllGenCostsByStartupIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where startup is less than or equal to DEFAULT_STARTUP
        defaultGenCostShouldBeFound("startup.lessThanOrEqual=" + DEFAULT_STARTUP);

        // Get all the genCostList where startup is less than or equal to SMALLER_STARTUP
        defaultGenCostShouldNotBeFound("startup.lessThanOrEqual=" + SMALLER_STARTUP);
    }

    @Test
    @Transactional
    void getAllGenCostsByStartupIsLessThanSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where startup is less than DEFAULT_STARTUP
        defaultGenCostShouldNotBeFound("startup.lessThan=" + DEFAULT_STARTUP);

        // Get all the genCostList where startup is less than UPDATED_STARTUP
        defaultGenCostShouldBeFound("startup.lessThan=" + UPDATED_STARTUP);
    }

    @Test
    @Transactional
    void getAllGenCostsByStartupIsGreaterThanSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where startup is greater than DEFAULT_STARTUP
        defaultGenCostShouldNotBeFound("startup.greaterThan=" + DEFAULT_STARTUP);

        // Get all the genCostList where startup is greater than SMALLER_STARTUP
        defaultGenCostShouldBeFound("startup.greaterThan=" + SMALLER_STARTUP);
    }

    @Test
    @Transactional
    void getAllGenCostsByShutdownIsEqualToSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where shutdown equals to DEFAULT_SHUTDOWN
        defaultGenCostShouldBeFound("shutdown.equals=" + DEFAULT_SHUTDOWN);

        // Get all the genCostList where shutdown equals to UPDATED_SHUTDOWN
        defaultGenCostShouldNotBeFound("shutdown.equals=" + UPDATED_SHUTDOWN);
    }

    @Test
    @Transactional
    void getAllGenCostsByShutdownIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where shutdown not equals to DEFAULT_SHUTDOWN
        defaultGenCostShouldNotBeFound("shutdown.notEquals=" + DEFAULT_SHUTDOWN);

        // Get all the genCostList where shutdown not equals to UPDATED_SHUTDOWN
        defaultGenCostShouldBeFound("shutdown.notEquals=" + UPDATED_SHUTDOWN);
    }

    @Test
    @Transactional
    void getAllGenCostsByShutdownIsInShouldWork() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where shutdown in DEFAULT_SHUTDOWN or UPDATED_SHUTDOWN
        defaultGenCostShouldBeFound("shutdown.in=" + DEFAULT_SHUTDOWN + "," + UPDATED_SHUTDOWN);

        // Get all the genCostList where shutdown equals to UPDATED_SHUTDOWN
        defaultGenCostShouldNotBeFound("shutdown.in=" + UPDATED_SHUTDOWN);
    }

    @Test
    @Transactional
    void getAllGenCostsByShutdownIsNullOrNotNull() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where shutdown is not null
        defaultGenCostShouldBeFound("shutdown.specified=true");

        // Get all the genCostList where shutdown is null
        defaultGenCostShouldNotBeFound("shutdown.specified=false");
    }

    @Test
    @Transactional
    void getAllGenCostsByShutdownIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where shutdown is greater than or equal to DEFAULT_SHUTDOWN
        defaultGenCostShouldBeFound("shutdown.greaterThanOrEqual=" + DEFAULT_SHUTDOWN);

        // Get all the genCostList where shutdown is greater than or equal to UPDATED_SHUTDOWN
        defaultGenCostShouldNotBeFound("shutdown.greaterThanOrEqual=" + UPDATED_SHUTDOWN);
    }

    @Test
    @Transactional
    void getAllGenCostsByShutdownIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where shutdown is less than or equal to DEFAULT_SHUTDOWN
        defaultGenCostShouldBeFound("shutdown.lessThanOrEqual=" + DEFAULT_SHUTDOWN);

        // Get all the genCostList where shutdown is less than or equal to SMALLER_SHUTDOWN
        defaultGenCostShouldNotBeFound("shutdown.lessThanOrEqual=" + SMALLER_SHUTDOWN);
    }

    @Test
    @Transactional
    void getAllGenCostsByShutdownIsLessThanSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where shutdown is less than DEFAULT_SHUTDOWN
        defaultGenCostShouldNotBeFound("shutdown.lessThan=" + DEFAULT_SHUTDOWN);

        // Get all the genCostList where shutdown is less than UPDATED_SHUTDOWN
        defaultGenCostShouldBeFound("shutdown.lessThan=" + UPDATED_SHUTDOWN);
    }

    @Test
    @Transactional
    void getAllGenCostsByShutdownIsGreaterThanSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where shutdown is greater than DEFAULT_SHUTDOWN
        defaultGenCostShouldNotBeFound("shutdown.greaterThan=" + DEFAULT_SHUTDOWN);

        // Get all the genCostList where shutdown is greater than SMALLER_SHUTDOWN
        defaultGenCostShouldBeFound("shutdown.greaterThan=" + SMALLER_SHUTDOWN);
    }

    @Test
    @Transactional
    void getAllGenCostsBynCostIsEqualToSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where nCost equals to DEFAULT_N_COST
        defaultGenCostShouldBeFound("nCost.equals=" + DEFAULT_N_COST);

        // Get all the genCostList where nCost equals to UPDATED_N_COST
        defaultGenCostShouldNotBeFound("nCost.equals=" + UPDATED_N_COST);
    }

    @Test
    @Transactional
    void getAllGenCostsBynCostIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where nCost not equals to DEFAULT_N_COST
        defaultGenCostShouldNotBeFound("nCost.notEquals=" + DEFAULT_N_COST);

        // Get all the genCostList where nCost not equals to UPDATED_N_COST
        defaultGenCostShouldBeFound("nCost.notEquals=" + UPDATED_N_COST);
    }

    @Test
    @Transactional
    void getAllGenCostsBynCostIsInShouldWork() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where nCost in DEFAULT_N_COST or UPDATED_N_COST
        defaultGenCostShouldBeFound("nCost.in=" + DEFAULT_N_COST + "," + UPDATED_N_COST);

        // Get all the genCostList where nCost equals to UPDATED_N_COST
        defaultGenCostShouldNotBeFound("nCost.in=" + UPDATED_N_COST);
    }

    @Test
    @Transactional
    void getAllGenCostsBynCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where nCost is not null
        defaultGenCostShouldBeFound("nCost.specified=true");

        // Get all the genCostList where nCost is null
        defaultGenCostShouldNotBeFound("nCost.specified=false");
    }

    @Test
    @Transactional
    void getAllGenCostsBynCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where nCost is greater than or equal to DEFAULT_N_COST
        defaultGenCostShouldBeFound("nCost.greaterThanOrEqual=" + DEFAULT_N_COST);

        // Get all the genCostList where nCost is greater than or equal to UPDATED_N_COST
        defaultGenCostShouldNotBeFound("nCost.greaterThanOrEqual=" + UPDATED_N_COST);
    }

    @Test
    @Transactional
    void getAllGenCostsBynCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where nCost is less than or equal to DEFAULT_N_COST
        defaultGenCostShouldBeFound("nCost.lessThanOrEqual=" + DEFAULT_N_COST);

        // Get all the genCostList where nCost is less than or equal to SMALLER_N_COST
        defaultGenCostShouldNotBeFound("nCost.lessThanOrEqual=" + SMALLER_N_COST);
    }

    @Test
    @Transactional
    void getAllGenCostsBynCostIsLessThanSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where nCost is less than DEFAULT_N_COST
        defaultGenCostShouldNotBeFound("nCost.lessThan=" + DEFAULT_N_COST);

        // Get all the genCostList where nCost is less than UPDATED_N_COST
        defaultGenCostShouldBeFound("nCost.lessThan=" + UPDATED_N_COST);
    }

    @Test
    @Transactional
    void getAllGenCostsBynCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where nCost is greater than DEFAULT_N_COST
        defaultGenCostShouldNotBeFound("nCost.greaterThan=" + DEFAULT_N_COST);

        // Get all the genCostList where nCost is greater than SMALLER_N_COST
        defaultGenCostShouldBeFound("nCost.greaterThan=" + SMALLER_N_COST);
    }

    @Test
    @Transactional
    void getAllGenCostsByCostPFIsEqualToSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where costPF equals to DEFAULT_COST_PF
        defaultGenCostShouldBeFound("costPF.equals=" + DEFAULT_COST_PF);

        // Get all the genCostList where costPF equals to UPDATED_COST_PF
        defaultGenCostShouldNotBeFound("costPF.equals=" + UPDATED_COST_PF);
    }

    @Test
    @Transactional
    void getAllGenCostsByCostPFIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where costPF not equals to DEFAULT_COST_PF
        defaultGenCostShouldNotBeFound("costPF.notEquals=" + DEFAULT_COST_PF);

        // Get all the genCostList where costPF not equals to UPDATED_COST_PF
        defaultGenCostShouldBeFound("costPF.notEquals=" + UPDATED_COST_PF);
    }

    @Test
    @Transactional
    void getAllGenCostsByCostPFIsInShouldWork() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where costPF in DEFAULT_COST_PF or UPDATED_COST_PF
        defaultGenCostShouldBeFound("costPF.in=" + DEFAULT_COST_PF + "," + UPDATED_COST_PF);

        // Get all the genCostList where costPF equals to UPDATED_COST_PF
        defaultGenCostShouldNotBeFound("costPF.in=" + UPDATED_COST_PF);
    }

    @Test
    @Transactional
    void getAllGenCostsByCostPFIsNullOrNotNull() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where costPF is not null
        defaultGenCostShouldBeFound("costPF.specified=true");

        // Get all the genCostList where costPF is null
        defaultGenCostShouldNotBeFound("costPF.specified=false");
    }

    @Test
    @Transactional
    void getAllGenCostsByCostPFContainsSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where costPF contains DEFAULT_COST_PF
        defaultGenCostShouldBeFound("costPF.contains=" + DEFAULT_COST_PF);

        // Get all the genCostList where costPF contains UPDATED_COST_PF
        defaultGenCostShouldNotBeFound("costPF.contains=" + UPDATED_COST_PF);
    }

    @Test
    @Transactional
    void getAllGenCostsByCostPFNotContainsSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where costPF does not contain DEFAULT_COST_PF
        defaultGenCostShouldNotBeFound("costPF.doesNotContain=" + DEFAULT_COST_PF);

        // Get all the genCostList where costPF does not contain UPDATED_COST_PF
        defaultGenCostShouldBeFound("costPF.doesNotContain=" + UPDATED_COST_PF);
    }

    @Test
    @Transactional
    void getAllGenCostsByCostQFIsEqualToSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where costQF equals to DEFAULT_COST_QF
        defaultGenCostShouldBeFound("costQF.equals=" + DEFAULT_COST_QF);

        // Get all the genCostList where costQF equals to UPDATED_COST_QF
        defaultGenCostShouldNotBeFound("costQF.equals=" + UPDATED_COST_QF);
    }

    @Test
    @Transactional
    void getAllGenCostsByCostQFIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where costQF not equals to DEFAULT_COST_QF
        defaultGenCostShouldNotBeFound("costQF.notEquals=" + DEFAULT_COST_QF);

        // Get all the genCostList where costQF not equals to UPDATED_COST_QF
        defaultGenCostShouldBeFound("costQF.notEquals=" + UPDATED_COST_QF);
    }

    @Test
    @Transactional
    void getAllGenCostsByCostQFIsInShouldWork() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where costQF in DEFAULT_COST_QF or UPDATED_COST_QF
        defaultGenCostShouldBeFound("costQF.in=" + DEFAULT_COST_QF + "," + UPDATED_COST_QF);

        // Get all the genCostList where costQF equals to UPDATED_COST_QF
        defaultGenCostShouldNotBeFound("costQF.in=" + UPDATED_COST_QF);
    }

    @Test
    @Transactional
    void getAllGenCostsByCostQFIsNullOrNotNull() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where costQF is not null
        defaultGenCostShouldBeFound("costQF.specified=true");

        // Get all the genCostList where costQF is null
        defaultGenCostShouldNotBeFound("costQF.specified=false");
    }

    @Test
    @Transactional
    void getAllGenCostsByCostQFContainsSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where costQF contains DEFAULT_COST_QF
        defaultGenCostShouldBeFound("costQF.contains=" + DEFAULT_COST_QF);

        // Get all the genCostList where costQF contains UPDATED_COST_QF
        defaultGenCostShouldNotBeFound("costQF.contains=" + UPDATED_COST_QF);
    }

    @Test
    @Transactional
    void getAllGenCostsByCostQFNotContainsSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        // Get all the genCostList where costQF does not contain DEFAULT_COST_QF
        defaultGenCostShouldNotBeFound("costQF.doesNotContain=" + DEFAULT_COST_QF);

        // Get all the genCostList where costQF does not contain UPDATED_COST_QF
        defaultGenCostShouldBeFound("costQF.doesNotContain=" + UPDATED_COST_QF);
    }

    @Test
    @Transactional
    void getAllGenCostsByGeneratorIsEqualToSomething() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);
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
        genCost.setGenerator(generator);
        genCostRepository.saveAndFlush(genCost);
        Long generatorId = generator.getId();

        // Get all the genCostList where generator equals to generatorId
        defaultGenCostShouldBeFound("generatorId.equals=" + generatorId);

        // Get all the genCostList where generator equals to (generatorId + 1)
        defaultGenCostShouldNotBeFound("generatorId.equals=" + (generatorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGenCostShouldBeFound(String filter) throws Exception {
        restGenCostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genCost.getId().intValue())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].startup").value(hasItem(DEFAULT_STARTUP.doubleValue())))
            .andExpect(jsonPath("$.[*].shutdown").value(hasItem(DEFAULT_SHUTDOWN.doubleValue())))
            .andExpect(jsonPath("$.[*].nCost").value(hasItem(DEFAULT_N_COST.intValue())))
            .andExpect(jsonPath("$.[*].costPF").value(hasItem(DEFAULT_COST_PF)))
            .andExpect(jsonPath("$.[*].costQF").value(hasItem(DEFAULT_COST_QF)));

        // Check, that the count call also returns 1
        restGenCostMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGenCostShouldNotBeFound(String filter) throws Exception {
        restGenCostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGenCostMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGenCost() throws Exception {
        // Get the genCost
        restGenCostMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGenCost() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        int databaseSizeBeforeUpdate = genCostRepository.findAll().size();

        // Update the genCost
        GenCost updatedGenCost = genCostRepository.findById(genCost.getId()).get();
        // Disconnect from session so that the updates on updatedGenCost are not directly saved in db
        em.detach(updatedGenCost);
        updatedGenCost
            .model(UPDATED_MODEL)
            .startup(UPDATED_STARTUP)
            .shutdown(UPDATED_SHUTDOWN)
            .nCost(UPDATED_N_COST)
            .costPF(UPDATED_COST_PF)
            .costQF(UPDATED_COST_QF);
        GenCostDTO genCostDTO = genCostMapper.toDto(updatedGenCost);

        restGenCostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, genCostDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genCostDTO))
            )
            .andExpect(status().isOk());

        // Validate the GenCost in the database
        List<GenCost> genCostList = genCostRepository.findAll();
        assertThat(genCostList).hasSize(databaseSizeBeforeUpdate);
        GenCost testGenCost = genCostList.get(genCostList.size() - 1);
        assertThat(testGenCost.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testGenCost.getStartup()).isEqualTo(UPDATED_STARTUP);
        assertThat(testGenCost.getShutdown()).isEqualTo(UPDATED_SHUTDOWN);
        assertThat(testGenCost.getnCost()).isEqualTo(UPDATED_N_COST);
        assertThat(testGenCost.getCostPF()).isEqualTo(UPDATED_COST_PF);
        assertThat(testGenCost.getCostQF()).isEqualTo(UPDATED_COST_QF);
    }

    @Test
    @Transactional
    void putNonExistingGenCost() throws Exception {
        int databaseSizeBeforeUpdate = genCostRepository.findAll().size();
        genCost.setId(count.incrementAndGet());

        // Create the GenCost
        GenCostDTO genCostDTO = genCostMapper.toDto(genCost);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenCostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, genCostDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genCostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenCost in the database
        List<GenCost> genCostList = genCostRepository.findAll();
        assertThat(genCostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGenCost() throws Exception {
        int databaseSizeBeforeUpdate = genCostRepository.findAll().size();
        genCost.setId(count.incrementAndGet());

        // Create the GenCost
        GenCostDTO genCostDTO = genCostMapper.toDto(genCost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenCostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genCostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenCost in the database
        List<GenCost> genCostList = genCostRepository.findAll();
        assertThat(genCostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGenCost() throws Exception {
        int databaseSizeBeforeUpdate = genCostRepository.findAll().size();
        genCost.setId(count.incrementAndGet());

        // Create the GenCost
        GenCostDTO genCostDTO = genCostMapper.toDto(genCost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenCostMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genCostDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GenCost in the database
        List<GenCost> genCostList = genCostRepository.findAll();
        assertThat(genCostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGenCostWithPatch() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        int databaseSizeBeforeUpdate = genCostRepository.findAll().size();

        // Update the genCost using partial update
        GenCost partialUpdatedGenCost = new GenCost();
        partialUpdatedGenCost.setId(genCost.getId());

        partialUpdatedGenCost.startup(UPDATED_STARTUP).costPF(UPDATED_COST_PF);

        restGenCostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenCost.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenCost))
            )
            .andExpect(status().isOk());

        // Validate the GenCost in the database
        List<GenCost> genCostList = genCostRepository.findAll();
        assertThat(genCostList).hasSize(databaseSizeBeforeUpdate);
        GenCost testGenCost = genCostList.get(genCostList.size() - 1);
        assertThat(testGenCost.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testGenCost.getStartup()).isEqualTo(UPDATED_STARTUP);
        assertThat(testGenCost.getShutdown()).isEqualTo(DEFAULT_SHUTDOWN);
        assertThat(testGenCost.getnCost()).isEqualTo(DEFAULT_N_COST);
        assertThat(testGenCost.getCostPF()).isEqualTo(UPDATED_COST_PF);
        assertThat(testGenCost.getCostQF()).isEqualTo(DEFAULT_COST_QF);
    }

    @Test
    @Transactional
    void fullUpdateGenCostWithPatch() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        int databaseSizeBeforeUpdate = genCostRepository.findAll().size();

        // Update the genCost using partial update
        GenCost partialUpdatedGenCost = new GenCost();
        partialUpdatedGenCost.setId(genCost.getId());

        partialUpdatedGenCost
            .model(UPDATED_MODEL)
            .startup(UPDATED_STARTUP)
            .shutdown(UPDATED_SHUTDOWN)
            .nCost(UPDATED_N_COST)
            .costPF(UPDATED_COST_PF)
            .costQF(UPDATED_COST_QF);

        restGenCostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenCost.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenCost))
            )
            .andExpect(status().isOk());

        // Validate the GenCost in the database
        List<GenCost> genCostList = genCostRepository.findAll();
        assertThat(genCostList).hasSize(databaseSizeBeforeUpdate);
        GenCost testGenCost = genCostList.get(genCostList.size() - 1);
        assertThat(testGenCost.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testGenCost.getStartup()).isEqualTo(UPDATED_STARTUP);
        assertThat(testGenCost.getShutdown()).isEqualTo(UPDATED_SHUTDOWN);
        assertThat(testGenCost.getnCost()).isEqualTo(UPDATED_N_COST);
        assertThat(testGenCost.getCostPF()).isEqualTo(UPDATED_COST_PF);
        assertThat(testGenCost.getCostQF()).isEqualTo(UPDATED_COST_QF);
    }

    @Test
    @Transactional
    void patchNonExistingGenCost() throws Exception {
        int databaseSizeBeforeUpdate = genCostRepository.findAll().size();
        genCost.setId(count.incrementAndGet());

        // Create the GenCost
        GenCostDTO genCostDTO = genCostMapper.toDto(genCost);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenCostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, genCostDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genCostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenCost in the database
        List<GenCost> genCostList = genCostRepository.findAll();
        assertThat(genCostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGenCost() throws Exception {
        int databaseSizeBeforeUpdate = genCostRepository.findAll().size();
        genCost.setId(count.incrementAndGet());

        // Create the GenCost
        GenCostDTO genCostDTO = genCostMapper.toDto(genCost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenCostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genCostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenCost in the database
        List<GenCost> genCostList = genCostRepository.findAll();
        assertThat(genCostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGenCost() throws Exception {
        int databaseSizeBeforeUpdate = genCostRepository.findAll().size();
        genCost.setId(count.incrementAndGet());

        // Create the GenCost
        GenCostDTO genCostDTO = genCostMapper.toDto(genCost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenCostMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genCostDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GenCost in the database
        List<GenCost> genCostList = genCostRepository.findAll();
        assertThat(genCostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGenCost() throws Exception {
        // Initialize the database
        genCostRepository.saveAndFlush(genCost);

        int databaseSizeBeforeDelete = genCostRepository.findAll().size();

        // Delete the genCost
        restGenCostMockMvc
            .perform(delete(ENTITY_API_URL_ID, genCost.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GenCost> genCostList = genCostRepository.findAll();
        assertThat(genCostList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
