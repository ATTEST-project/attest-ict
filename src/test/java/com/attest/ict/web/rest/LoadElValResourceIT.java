package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.LoadElVal;
import com.attest.ict.domain.LoadProfile;
import com.attest.ict.repository.LoadElValRepository;
import com.attest.ict.service.criteria.LoadElValCriteria;
import com.attest.ict.service.dto.LoadElValDTO;
import com.attest.ict.service.mapper.LoadElValMapper;
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
 * Integration tests for the {@link LoadElValResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LoadElValResourceIT {

    private static final Integer DEFAULT_HOUR = 1;
    private static final Integer UPDATED_HOUR = 2;
    private static final Integer SMALLER_HOUR = 1 - 1;

    private static final Integer DEFAULT_MIN = 1;
    private static final Integer UPDATED_MIN = 2;
    private static final Integer SMALLER_MIN = 1 - 1;

    private static final Double DEFAULT_P = 1D;
    private static final Double UPDATED_P = 2D;
    private static final Double SMALLER_P = 1D - 1D;

    private static final Double DEFAULT_Q = 1D;
    private static final Double UPDATED_Q = 2D;
    private static final Double SMALLER_Q = 1D - 1D;

    private static final Long DEFAULT_LOAD_ID_ON_SUBST = 1L;
    private static final Long UPDATED_LOAD_ID_ON_SUBST = 2L;
    private static final Long SMALLER_LOAD_ID_ON_SUBST = 1L - 1L;

    private static final String DEFAULT_NOMINAL_VOLTAGE = "AAAAAAAAAA";
    private static final String UPDATED_NOMINAL_VOLTAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/load-el-vals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LoadElValRepository loadElValRepository;

    @Autowired
    private LoadElValMapper loadElValMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoadElValMockMvc;

    private LoadElVal loadElVal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoadElVal createEntity(EntityManager em) {
        LoadElVal loadElVal = new LoadElVal()
            .hour(DEFAULT_HOUR)
            .min(DEFAULT_MIN)
            .p(DEFAULT_P)
            .q(DEFAULT_Q)
            .loadIdOnSubst(DEFAULT_LOAD_ID_ON_SUBST)
            .nominalVoltage(DEFAULT_NOMINAL_VOLTAGE);
        return loadElVal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoadElVal createUpdatedEntity(EntityManager em) {
        LoadElVal loadElVal = new LoadElVal()
            .hour(UPDATED_HOUR)
            .min(UPDATED_MIN)
            .p(UPDATED_P)
            .q(UPDATED_Q)
            .loadIdOnSubst(UPDATED_LOAD_ID_ON_SUBST)
            .nominalVoltage(UPDATED_NOMINAL_VOLTAGE);
        return loadElVal;
    }

    @BeforeEach
    public void initTest() {
        loadElVal = createEntity(em);
    }

    @Test
    @Transactional
    void createLoadElVal() throws Exception {
        int databaseSizeBeforeCreate = loadElValRepository.findAll().size();
        // Create the LoadElVal
        LoadElValDTO loadElValDTO = loadElValMapper.toDto(loadElVal);
        restLoadElValMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loadElValDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LoadElVal in the database
        List<LoadElVal> loadElValList = loadElValRepository.findAll();
        assertThat(loadElValList).hasSize(databaseSizeBeforeCreate + 1);
        LoadElVal testLoadElVal = loadElValList.get(loadElValList.size() - 1);
        assertThat(testLoadElVal.getHour()).isEqualTo(DEFAULT_HOUR);
        assertThat(testLoadElVal.getMin()).isEqualTo(DEFAULT_MIN);
        assertThat(testLoadElVal.getP()).isEqualTo(DEFAULT_P);
        assertThat(testLoadElVal.getQ()).isEqualTo(DEFAULT_Q);
        assertThat(testLoadElVal.getLoadIdOnSubst()).isEqualTo(DEFAULT_LOAD_ID_ON_SUBST);
        assertThat(testLoadElVal.getNominalVoltage()).isEqualTo(DEFAULT_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void createLoadElValWithExistingId() throws Exception {
        // Create the LoadElVal with an existing ID
        loadElVal.setId(1L);
        LoadElValDTO loadElValDTO = loadElValMapper.toDto(loadElVal);

        int databaseSizeBeforeCreate = loadElValRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoadElValMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loadElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoadElVal in the database
        List<LoadElVal> loadElValList = loadElValRepository.findAll();
        assertThat(loadElValList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLoadElVals() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList
        restLoadElValMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loadElVal.getId().intValue())))
            .andExpect(jsonPath("$.[*].hour").value(hasItem(DEFAULT_HOUR)))
            .andExpect(jsonPath("$.[*].min").value(hasItem(DEFAULT_MIN)))
            .andExpect(jsonPath("$.[*].p").value(hasItem(DEFAULT_P.doubleValue())))
            .andExpect(jsonPath("$.[*].q").value(hasItem(DEFAULT_Q.doubleValue())))
            .andExpect(jsonPath("$.[*].loadIdOnSubst").value(hasItem(DEFAULT_LOAD_ID_ON_SUBST.intValue())))
            .andExpect(jsonPath("$.[*].nominalVoltage").value(hasItem(DEFAULT_NOMINAL_VOLTAGE)));
    }

    @Test
    @Transactional
    void getLoadElVal() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get the loadElVal
        restLoadElValMockMvc
            .perform(get(ENTITY_API_URL_ID, loadElVal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loadElVal.getId().intValue()))
            .andExpect(jsonPath("$.hour").value(DEFAULT_HOUR))
            .andExpect(jsonPath("$.min").value(DEFAULT_MIN))
            .andExpect(jsonPath("$.p").value(DEFAULT_P.doubleValue()))
            .andExpect(jsonPath("$.q").value(DEFAULT_Q.doubleValue()))
            .andExpect(jsonPath("$.loadIdOnSubst").value(DEFAULT_LOAD_ID_ON_SUBST.intValue()))
            .andExpect(jsonPath("$.nominalVoltage").value(DEFAULT_NOMINAL_VOLTAGE));
    }

    @Test
    @Transactional
    void getLoadElValsByIdFiltering() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        Long id = loadElVal.getId();

        defaultLoadElValShouldBeFound("id.equals=" + id);
        defaultLoadElValShouldNotBeFound("id.notEquals=" + id);

        defaultLoadElValShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLoadElValShouldNotBeFound("id.greaterThan=" + id);

        defaultLoadElValShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLoadElValShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLoadElValsByHourIsEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where hour equals to DEFAULT_HOUR
        defaultLoadElValShouldBeFound("hour.equals=" + DEFAULT_HOUR);

        // Get all the loadElValList where hour equals to UPDATED_HOUR
        defaultLoadElValShouldNotBeFound("hour.equals=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllLoadElValsByHourIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where hour not equals to DEFAULT_HOUR
        defaultLoadElValShouldNotBeFound("hour.notEquals=" + DEFAULT_HOUR);

        // Get all the loadElValList where hour not equals to UPDATED_HOUR
        defaultLoadElValShouldBeFound("hour.notEquals=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllLoadElValsByHourIsInShouldWork() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where hour in DEFAULT_HOUR or UPDATED_HOUR
        defaultLoadElValShouldBeFound("hour.in=" + DEFAULT_HOUR + "," + UPDATED_HOUR);

        // Get all the loadElValList where hour equals to UPDATED_HOUR
        defaultLoadElValShouldNotBeFound("hour.in=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllLoadElValsByHourIsNullOrNotNull() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where hour is not null
        defaultLoadElValShouldBeFound("hour.specified=true");

        // Get all the loadElValList where hour is null
        defaultLoadElValShouldNotBeFound("hour.specified=false");
    }

    @Test
    @Transactional
    void getAllLoadElValsByHourIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where hour is greater than or equal to DEFAULT_HOUR
        defaultLoadElValShouldBeFound("hour.greaterThanOrEqual=" + DEFAULT_HOUR);

        // Get all the loadElValList where hour is greater than or equal to UPDATED_HOUR
        defaultLoadElValShouldNotBeFound("hour.greaterThanOrEqual=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllLoadElValsByHourIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where hour is less than or equal to DEFAULT_HOUR
        defaultLoadElValShouldBeFound("hour.lessThanOrEqual=" + DEFAULT_HOUR);

        // Get all the loadElValList where hour is less than or equal to SMALLER_HOUR
        defaultLoadElValShouldNotBeFound("hour.lessThanOrEqual=" + SMALLER_HOUR);
    }

    @Test
    @Transactional
    void getAllLoadElValsByHourIsLessThanSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where hour is less than DEFAULT_HOUR
        defaultLoadElValShouldNotBeFound("hour.lessThan=" + DEFAULT_HOUR);

        // Get all the loadElValList where hour is less than UPDATED_HOUR
        defaultLoadElValShouldBeFound("hour.lessThan=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllLoadElValsByHourIsGreaterThanSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where hour is greater than DEFAULT_HOUR
        defaultLoadElValShouldNotBeFound("hour.greaterThan=" + DEFAULT_HOUR);

        // Get all the loadElValList where hour is greater than SMALLER_HOUR
        defaultLoadElValShouldBeFound("hour.greaterThan=" + SMALLER_HOUR);
    }

    @Test
    @Transactional
    void getAllLoadElValsByMinIsEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where min equals to DEFAULT_MIN
        defaultLoadElValShouldBeFound("min.equals=" + DEFAULT_MIN);

        // Get all the loadElValList where min equals to UPDATED_MIN
        defaultLoadElValShouldNotBeFound("min.equals=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllLoadElValsByMinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where min not equals to DEFAULT_MIN
        defaultLoadElValShouldNotBeFound("min.notEquals=" + DEFAULT_MIN);

        // Get all the loadElValList where min not equals to UPDATED_MIN
        defaultLoadElValShouldBeFound("min.notEquals=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllLoadElValsByMinIsInShouldWork() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where min in DEFAULT_MIN or UPDATED_MIN
        defaultLoadElValShouldBeFound("min.in=" + DEFAULT_MIN + "," + UPDATED_MIN);

        // Get all the loadElValList where min equals to UPDATED_MIN
        defaultLoadElValShouldNotBeFound("min.in=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllLoadElValsByMinIsNullOrNotNull() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where min is not null
        defaultLoadElValShouldBeFound("min.specified=true");

        // Get all the loadElValList where min is null
        defaultLoadElValShouldNotBeFound("min.specified=false");
    }

    @Test
    @Transactional
    void getAllLoadElValsByMinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where min is greater than or equal to DEFAULT_MIN
        defaultLoadElValShouldBeFound("min.greaterThanOrEqual=" + DEFAULT_MIN);

        // Get all the loadElValList where min is greater than or equal to UPDATED_MIN
        defaultLoadElValShouldNotBeFound("min.greaterThanOrEqual=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllLoadElValsByMinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where min is less than or equal to DEFAULT_MIN
        defaultLoadElValShouldBeFound("min.lessThanOrEqual=" + DEFAULT_MIN);

        // Get all the loadElValList where min is less than or equal to SMALLER_MIN
        defaultLoadElValShouldNotBeFound("min.lessThanOrEqual=" + SMALLER_MIN);
    }

    @Test
    @Transactional
    void getAllLoadElValsByMinIsLessThanSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where min is less than DEFAULT_MIN
        defaultLoadElValShouldNotBeFound("min.lessThan=" + DEFAULT_MIN);

        // Get all the loadElValList where min is less than UPDATED_MIN
        defaultLoadElValShouldBeFound("min.lessThan=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllLoadElValsByMinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where min is greater than DEFAULT_MIN
        defaultLoadElValShouldNotBeFound("min.greaterThan=" + DEFAULT_MIN);

        // Get all the loadElValList where min is greater than SMALLER_MIN
        defaultLoadElValShouldBeFound("min.greaterThan=" + SMALLER_MIN);
    }

    @Test
    @Transactional
    void getAllLoadElValsByPIsEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where p equals to DEFAULT_P
        defaultLoadElValShouldBeFound("p.equals=" + DEFAULT_P);

        // Get all the loadElValList where p equals to UPDATED_P
        defaultLoadElValShouldNotBeFound("p.equals=" + UPDATED_P);
    }

    @Test
    @Transactional
    void getAllLoadElValsByPIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where p not equals to DEFAULT_P
        defaultLoadElValShouldNotBeFound("p.notEquals=" + DEFAULT_P);

        // Get all the loadElValList where p not equals to UPDATED_P
        defaultLoadElValShouldBeFound("p.notEquals=" + UPDATED_P);
    }

    @Test
    @Transactional
    void getAllLoadElValsByPIsInShouldWork() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where p in DEFAULT_P or UPDATED_P
        defaultLoadElValShouldBeFound("p.in=" + DEFAULT_P + "," + UPDATED_P);

        // Get all the loadElValList where p equals to UPDATED_P
        defaultLoadElValShouldNotBeFound("p.in=" + UPDATED_P);
    }

    @Test
    @Transactional
    void getAllLoadElValsByPIsNullOrNotNull() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where p is not null
        defaultLoadElValShouldBeFound("p.specified=true");

        // Get all the loadElValList where p is null
        defaultLoadElValShouldNotBeFound("p.specified=false");
    }

    @Test
    @Transactional
    void getAllLoadElValsByPIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where p is greater than or equal to DEFAULT_P
        defaultLoadElValShouldBeFound("p.greaterThanOrEqual=" + DEFAULT_P);

        // Get all the loadElValList where p is greater than or equal to UPDATED_P
        defaultLoadElValShouldNotBeFound("p.greaterThanOrEqual=" + UPDATED_P);
    }

    @Test
    @Transactional
    void getAllLoadElValsByPIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where p is less than or equal to DEFAULT_P
        defaultLoadElValShouldBeFound("p.lessThanOrEqual=" + DEFAULT_P);

        // Get all the loadElValList where p is less than or equal to SMALLER_P
        defaultLoadElValShouldNotBeFound("p.lessThanOrEqual=" + SMALLER_P);
    }

    @Test
    @Transactional
    void getAllLoadElValsByPIsLessThanSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where p is less than DEFAULT_P
        defaultLoadElValShouldNotBeFound("p.lessThan=" + DEFAULT_P);

        // Get all the loadElValList where p is less than UPDATED_P
        defaultLoadElValShouldBeFound("p.lessThan=" + UPDATED_P);
    }

    @Test
    @Transactional
    void getAllLoadElValsByPIsGreaterThanSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where p is greater than DEFAULT_P
        defaultLoadElValShouldNotBeFound("p.greaterThan=" + DEFAULT_P);

        // Get all the loadElValList where p is greater than SMALLER_P
        defaultLoadElValShouldBeFound("p.greaterThan=" + SMALLER_P);
    }

    @Test
    @Transactional
    void getAllLoadElValsByQIsEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where q equals to DEFAULT_Q
        defaultLoadElValShouldBeFound("q.equals=" + DEFAULT_Q);

        // Get all the loadElValList where q equals to UPDATED_Q
        defaultLoadElValShouldNotBeFound("q.equals=" + UPDATED_Q);
    }

    @Test
    @Transactional
    void getAllLoadElValsByQIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where q not equals to DEFAULT_Q
        defaultLoadElValShouldNotBeFound("q.notEquals=" + DEFAULT_Q);

        // Get all the loadElValList where q not equals to UPDATED_Q
        defaultLoadElValShouldBeFound("q.notEquals=" + UPDATED_Q);
    }

    @Test
    @Transactional
    void getAllLoadElValsByQIsInShouldWork() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where q in DEFAULT_Q or UPDATED_Q
        defaultLoadElValShouldBeFound("q.in=" + DEFAULT_Q + "," + UPDATED_Q);

        // Get all the loadElValList where q equals to UPDATED_Q
        defaultLoadElValShouldNotBeFound("q.in=" + UPDATED_Q);
    }

    @Test
    @Transactional
    void getAllLoadElValsByQIsNullOrNotNull() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where q is not null
        defaultLoadElValShouldBeFound("q.specified=true");

        // Get all the loadElValList where q is null
        defaultLoadElValShouldNotBeFound("q.specified=false");
    }

    @Test
    @Transactional
    void getAllLoadElValsByQIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where q is greater than or equal to DEFAULT_Q
        defaultLoadElValShouldBeFound("q.greaterThanOrEqual=" + DEFAULT_Q);

        // Get all the loadElValList where q is greater than or equal to UPDATED_Q
        defaultLoadElValShouldNotBeFound("q.greaterThanOrEqual=" + UPDATED_Q);
    }

    @Test
    @Transactional
    void getAllLoadElValsByQIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where q is less than or equal to DEFAULT_Q
        defaultLoadElValShouldBeFound("q.lessThanOrEqual=" + DEFAULT_Q);

        // Get all the loadElValList where q is less than or equal to SMALLER_Q
        defaultLoadElValShouldNotBeFound("q.lessThanOrEqual=" + SMALLER_Q);
    }

    @Test
    @Transactional
    void getAllLoadElValsByQIsLessThanSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where q is less than DEFAULT_Q
        defaultLoadElValShouldNotBeFound("q.lessThan=" + DEFAULT_Q);

        // Get all the loadElValList where q is less than UPDATED_Q
        defaultLoadElValShouldBeFound("q.lessThan=" + UPDATED_Q);
    }

    @Test
    @Transactional
    void getAllLoadElValsByQIsGreaterThanSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where q is greater than DEFAULT_Q
        defaultLoadElValShouldNotBeFound("q.greaterThan=" + DEFAULT_Q);

        // Get all the loadElValList where q is greater than SMALLER_Q
        defaultLoadElValShouldBeFound("q.greaterThan=" + SMALLER_Q);
    }

    @Test
    @Transactional
    void getAllLoadElValsByLoadIdOnSubstIsEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where loadIdOnSubst equals to DEFAULT_LOAD_ID_ON_SUBST
        defaultLoadElValShouldBeFound("loadIdOnSubst.equals=" + DEFAULT_LOAD_ID_ON_SUBST);

        // Get all the loadElValList where loadIdOnSubst equals to UPDATED_LOAD_ID_ON_SUBST
        defaultLoadElValShouldNotBeFound("loadIdOnSubst.equals=" + UPDATED_LOAD_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllLoadElValsByLoadIdOnSubstIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where loadIdOnSubst not equals to DEFAULT_LOAD_ID_ON_SUBST
        defaultLoadElValShouldNotBeFound("loadIdOnSubst.notEquals=" + DEFAULT_LOAD_ID_ON_SUBST);

        // Get all the loadElValList where loadIdOnSubst not equals to UPDATED_LOAD_ID_ON_SUBST
        defaultLoadElValShouldBeFound("loadIdOnSubst.notEquals=" + UPDATED_LOAD_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllLoadElValsByLoadIdOnSubstIsInShouldWork() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where loadIdOnSubst in DEFAULT_LOAD_ID_ON_SUBST or UPDATED_LOAD_ID_ON_SUBST
        defaultLoadElValShouldBeFound("loadIdOnSubst.in=" + DEFAULT_LOAD_ID_ON_SUBST + "," + UPDATED_LOAD_ID_ON_SUBST);

        // Get all the loadElValList where loadIdOnSubst equals to UPDATED_LOAD_ID_ON_SUBST
        defaultLoadElValShouldNotBeFound("loadIdOnSubst.in=" + UPDATED_LOAD_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllLoadElValsByLoadIdOnSubstIsNullOrNotNull() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where loadIdOnSubst is not null
        defaultLoadElValShouldBeFound("loadIdOnSubst.specified=true");

        // Get all the loadElValList where loadIdOnSubst is null
        defaultLoadElValShouldNotBeFound("loadIdOnSubst.specified=false");
    }

    @Test
    @Transactional
    void getAllLoadElValsByLoadIdOnSubstIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where loadIdOnSubst is greater than or equal to DEFAULT_LOAD_ID_ON_SUBST
        defaultLoadElValShouldBeFound("loadIdOnSubst.greaterThanOrEqual=" + DEFAULT_LOAD_ID_ON_SUBST);

        // Get all the loadElValList where loadIdOnSubst is greater than or equal to UPDATED_LOAD_ID_ON_SUBST
        defaultLoadElValShouldNotBeFound("loadIdOnSubst.greaterThanOrEqual=" + UPDATED_LOAD_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllLoadElValsByLoadIdOnSubstIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where loadIdOnSubst is less than or equal to DEFAULT_LOAD_ID_ON_SUBST
        defaultLoadElValShouldBeFound("loadIdOnSubst.lessThanOrEqual=" + DEFAULT_LOAD_ID_ON_SUBST);

        // Get all the loadElValList where loadIdOnSubst is less than or equal to SMALLER_LOAD_ID_ON_SUBST
        defaultLoadElValShouldNotBeFound("loadIdOnSubst.lessThanOrEqual=" + SMALLER_LOAD_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllLoadElValsByLoadIdOnSubstIsLessThanSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where loadIdOnSubst is less than DEFAULT_LOAD_ID_ON_SUBST
        defaultLoadElValShouldNotBeFound("loadIdOnSubst.lessThan=" + DEFAULT_LOAD_ID_ON_SUBST);

        // Get all the loadElValList where loadIdOnSubst is less than UPDATED_LOAD_ID_ON_SUBST
        defaultLoadElValShouldBeFound("loadIdOnSubst.lessThan=" + UPDATED_LOAD_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllLoadElValsByLoadIdOnSubstIsGreaterThanSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where loadIdOnSubst is greater than DEFAULT_LOAD_ID_ON_SUBST
        defaultLoadElValShouldNotBeFound("loadIdOnSubst.greaterThan=" + DEFAULT_LOAD_ID_ON_SUBST);

        // Get all the loadElValList where loadIdOnSubst is greater than SMALLER_LOAD_ID_ON_SUBST
        defaultLoadElValShouldBeFound("loadIdOnSubst.greaterThan=" + SMALLER_LOAD_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllLoadElValsByNominalVoltageIsEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where nominalVoltage equals to DEFAULT_NOMINAL_VOLTAGE
        defaultLoadElValShouldBeFound("nominalVoltage.equals=" + DEFAULT_NOMINAL_VOLTAGE);

        // Get all the loadElValList where nominalVoltage equals to UPDATED_NOMINAL_VOLTAGE
        defaultLoadElValShouldNotBeFound("nominalVoltage.equals=" + UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllLoadElValsByNominalVoltageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where nominalVoltage not equals to DEFAULT_NOMINAL_VOLTAGE
        defaultLoadElValShouldNotBeFound("nominalVoltage.notEquals=" + DEFAULT_NOMINAL_VOLTAGE);

        // Get all the loadElValList where nominalVoltage not equals to UPDATED_NOMINAL_VOLTAGE
        defaultLoadElValShouldBeFound("nominalVoltage.notEquals=" + UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllLoadElValsByNominalVoltageIsInShouldWork() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where nominalVoltage in DEFAULT_NOMINAL_VOLTAGE or UPDATED_NOMINAL_VOLTAGE
        defaultLoadElValShouldBeFound("nominalVoltage.in=" + DEFAULT_NOMINAL_VOLTAGE + "," + UPDATED_NOMINAL_VOLTAGE);

        // Get all the loadElValList where nominalVoltage equals to UPDATED_NOMINAL_VOLTAGE
        defaultLoadElValShouldNotBeFound("nominalVoltage.in=" + UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllLoadElValsByNominalVoltageIsNullOrNotNull() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where nominalVoltage is not null
        defaultLoadElValShouldBeFound("nominalVoltage.specified=true");

        // Get all the loadElValList where nominalVoltage is null
        defaultLoadElValShouldNotBeFound("nominalVoltage.specified=false");
    }

    @Test
    @Transactional
    void getAllLoadElValsByNominalVoltageContainsSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where nominalVoltage contains DEFAULT_NOMINAL_VOLTAGE
        defaultLoadElValShouldBeFound("nominalVoltage.contains=" + DEFAULT_NOMINAL_VOLTAGE);

        // Get all the loadElValList where nominalVoltage contains UPDATED_NOMINAL_VOLTAGE
        defaultLoadElValShouldNotBeFound("nominalVoltage.contains=" + UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllLoadElValsByNominalVoltageNotContainsSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        // Get all the loadElValList where nominalVoltage does not contain DEFAULT_NOMINAL_VOLTAGE
        defaultLoadElValShouldNotBeFound("nominalVoltage.doesNotContain=" + DEFAULT_NOMINAL_VOLTAGE);

        // Get all the loadElValList where nominalVoltage does not contain UPDATED_NOMINAL_VOLTAGE
        defaultLoadElValShouldBeFound("nominalVoltage.doesNotContain=" + UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllLoadElValsByLoadProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);
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
        loadElVal.setLoadProfile(loadProfile);
        loadElValRepository.saveAndFlush(loadElVal);
        Long loadProfileId = loadProfile.getId();

        // Get all the loadElValList where loadProfile equals to loadProfileId
        defaultLoadElValShouldBeFound("loadProfileId.equals=" + loadProfileId);

        // Get all the loadElValList where loadProfile equals to (loadProfileId + 1)
        defaultLoadElValShouldNotBeFound("loadProfileId.equals=" + (loadProfileId + 1));
    }

    @Test
    @Transactional
    void getAllLoadElValsByBusIsEqualToSomething() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);
        Bus bus;
        if (TestUtil.findAll(em, Bus.class).isEmpty()) {
            bus = BusResourceIT.createEntity(em);
            em.persist(bus);
            em.flush();
        } else {
            bus = TestUtil.findAll(em, Bus.class).get(0);
        }
        em.persist(bus);
        em.flush();
        loadElVal.setBus(bus);
        loadElValRepository.saveAndFlush(loadElVal);
        Long busId = bus.getId();

        // Get all the loadElValList where bus equals to busId
        defaultLoadElValShouldBeFound("busId.equals=" + busId);

        // Get all the loadElValList where bus equals to (busId + 1)
        defaultLoadElValShouldNotBeFound("busId.equals=" + (busId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLoadElValShouldBeFound(String filter) throws Exception {
        restLoadElValMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loadElVal.getId().intValue())))
            .andExpect(jsonPath("$.[*].hour").value(hasItem(DEFAULT_HOUR)))
            .andExpect(jsonPath("$.[*].min").value(hasItem(DEFAULT_MIN)))
            .andExpect(jsonPath("$.[*].p").value(hasItem(DEFAULT_P.doubleValue())))
            .andExpect(jsonPath("$.[*].q").value(hasItem(DEFAULT_Q.doubleValue())))
            .andExpect(jsonPath("$.[*].loadIdOnSubst").value(hasItem(DEFAULT_LOAD_ID_ON_SUBST.intValue())))
            .andExpect(jsonPath("$.[*].nominalVoltage").value(hasItem(DEFAULT_NOMINAL_VOLTAGE)));

        // Check, that the count call also returns 1
        restLoadElValMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLoadElValShouldNotBeFound(String filter) throws Exception {
        restLoadElValMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLoadElValMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLoadElVal() throws Exception {
        // Get the loadElVal
        restLoadElValMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLoadElVal() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        int databaseSizeBeforeUpdate = loadElValRepository.findAll().size();

        // Update the loadElVal
        LoadElVal updatedLoadElVal = loadElValRepository.findById(loadElVal.getId()).get();
        // Disconnect from session so that the updates on updatedLoadElVal are not directly saved in db
        em.detach(updatedLoadElVal);
        updatedLoadElVal
            .hour(UPDATED_HOUR)
            .min(UPDATED_MIN)
            .p(UPDATED_P)
            .q(UPDATED_Q)
            .loadIdOnSubst(UPDATED_LOAD_ID_ON_SUBST)
            .nominalVoltage(UPDATED_NOMINAL_VOLTAGE);
        LoadElValDTO loadElValDTO = loadElValMapper.toDto(updatedLoadElVal);

        restLoadElValMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loadElValDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loadElValDTO))
            )
            .andExpect(status().isOk());

        // Validate the LoadElVal in the database
        List<LoadElVal> loadElValList = loadElValRepository.findAll();
        assertThat(loadElValList).hasSize(databaseSizeBeforeUpdate);
        LoadElVal testLoadElVal = loadElValList.get(loadElValList.size() - 1);
        assertThat(testLoadElVal.getHour()).isEqualTo(UPDATED_HOUR);
        assertThat(testLoadElVal.getMin()).isEqualTo(UPDATED_MIN);
        assertThat(testLoadElVal.getP()).isEqualTo(UPDATED_P);
        assertThat(testLoadElVal.getQ()).isEqualTo(UPDATED_Q);
        assertThat(testLoadElVal.getLoadIdOnSubst()).isEqualTo(UPDATED_LOAD_ID_ON_SUBST);
        assertThat(testLoadElVal.getNominalVoltage()).isEqualTo(UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void putNonExistingLoadElVal() throws Exception {
        int databaseSizeBeforeUpdate = loadElValRepository.findAll().size();
        loadElVal.setId(count.incrementAndGet());

        // Create the LoadElVal
        LoadElValDTO loadElValDTO = loadElValMapper.toDto(loadElVal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoadElValMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loadElValDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loadElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoadElVal in the database
        List<LoadElVal> loadElValList = loadElValRepository.findAll();
        assertThat(loadElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoadElVal() throws Exception {
        int databaseSizeBeforeUpdate = loadElValRepository.findAll().size();
        loadElVal.setId(count.incrementAndGet());

        // Create the LoadElVal
        LoadElValDTO loadElValDTO = loadElValMapper.toDto(loadElVal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoadElValMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loadElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoadElVal in the database
        List<LoadElVal> loadElValList = loadElValRepository.findAll();
        assertThat(loadElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoadElVal() throws Exception {
        int databaseSizeBeforeUpdate = loadElValRepository.findAll().size();
        loadElVal.setId(count.incrementAndGet());

        // Create the LoadElVal
        LoadElValDTO loadElValDTO = loadElValMapper.toDto(loadElVal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoadElValMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loadElValDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoadElVal in the database
        List<LoadElVal> loadElValList = loadElValRepository.findAll();
        assertThat(loadElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLoadElValWithPatch() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        int databaseSizeBeforeUpdate = loadElValRepository.findAll().size();

        // Update the loadElVal using partial update
        LoadElVal partialUpdatedLoadElVal = new LoadElVal();
        partialUpdatedLoadElVal.setId(loadElVal.getId());

        partialUpdatedLoadElVal.hour(UPDATED_HOUR).min(UPDATED_MIN).q(UPDATED_Q).loadIdOnSubst(UPDATED_LOAD_ID_ON_SUBST);

        restLoadElValMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoadElVal.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoadElVal))
            )
            .andExpect(status().isOk());

        // Validate the LoadElVal in the database
        List<LoadElVal> loadElValList = loadElValRepository.findAll();
        assertThat(loadElValList).hasSize(databaseSizeBeforeUpdate);
        LoadElVal testLoadElVal = loadElValList.get(loadElValList.size() - 1);
        assertThat(testLoadElVal.getHour()).isEqualTo(UPDATED_HOUR);
        assertThat(testLoadElVal.getMin()).isEqualTo(UPDATED_MIN);
        assertThat(testLoadElVal.getP()).isEqualTo(DEFAULT_P);
        assertThat(testLoadElVal.getQ()).isEqualTo(UPDATED_Q);
        assertThat(testLoadElVal.getLoadIdOnSubst()).isEqualTo(UPDATED_LOAD_ID_ON_SUBST);
        assertThat(testLoadElVal.getNominalVoltage()).isEqualTo(DEFAULT_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void fullUpdateLoadElValWithPatch() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        int databaseSizeBeforeUpdate = loadElValRepository.findAll().size();

        // Update the loadElVal using partial update
        LoadElVal partialUpdatedLoadElVal = new LoadElVal();
        partialUpdatedLoadElVal.setId(loadElVal.getId());

        partialUpdatedLoadElVal
            .hour(UPDATED_HOUR)
            .min(UPDATED_MIN)
            .p(UPDATED_P)
            .q(UPDATED_Q)
            .loadIdOnSubst(UPDATED_LOAD_ID_ON_SUBST)
            .nominalVoltage(UPDATED_NOMINAL_VOLTAGE);

        restLoadElValMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoadElVal.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoadElVal))
            )
            .andExpect(status().isOk());

        // Validate the LoadElVal in the database
        List<LoadElVal> loadElValList = loadElValRepository.findAll();
        assertThat(loadElValList).hasSize(databaseSizeBeforeUpdate);
        LoadElVal testLoadElVal = loadElValList.get(loadElValList.size() - 1);
        assertThat(testLoadElVal.getHour()).isEqualTo(UPDATED_HOUR);
        assertThat(testLoadElVal.getMin()).isEqualTo(UPDATED_MIN);
        assertThat(testLoadElVal.getP()).isEqualTo(UPDATED_P);
        assertThat(testLoadElVal.getQ()).isEqualTo(UPDATED_Q);
        assertThat(testLoadElVal.getLoadIdOnSubst()).isEqualTo(UPDATED_LOAD_ID_ON_SUBST);
        assertThat(testLoadElVal.getNominalVoltage()).isEqualTo(UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void patchNonExistingLoadElVal() throws Exception {
        int databaseSizeBeforeUpdate = loadElValRepository.findAll().size();
        loadElVal.setId(count.incrementAndGet());

        // Create the LoadElVal
        LoadElValDTO loadElValDTO = loadElValMapper.toDto(loadElVal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoadElValMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loadElValDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loadElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoadElVal in the database
        List<LoadElVal> loadElValList = loadElValRepository.findAll();
        assertThat(loadElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoadElVal() throws Exception {
        int databaseSizeBeforeUpdate = loadElValRepository.findAll().size();
        loadElVal.setId(count.incrementAndGet());

        // Create the LoadElVal
        LoadElValDTO loadElValDTO = loadElValMapper.toDto(loadElVal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoadElValMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loadElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoadElVal in the database
        List<LoadElVal> loadElValList = loadElValRepository.findAll();
        assertThat(loadElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoadElVal() throws Exception {
        int databaseSizeBeforeUpdate = loadElValRepository.findAll().size();
        loadElVal.setId(count.incrementAndGet());

        // Create the LoadElVal
        LoadElValDTO loadElValDTO = loadElValMapper.toDto(loadElVal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoadElValMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loadElValDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoadElVal in the database
        List<LoadElVal> loadElValList = loadElValRepository.findAll();
        assertThat(loadElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLoadElVal() throws Exception {
        // Initialize the database
        loadElValRepository.saveAndFlush(loadElVal);

        int databaseSizeBeforeDelete = loadElValRepository.findAll().size();

        // Delete the loadElVal
        restLoadElValMockMvc
            .perform(delete(ENTITY_API_URL_ID, loadElVal.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LoadElVal> loadElValList = loadElValRepository.findAll();
        assertThat(loadElValList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
