package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.GenElVal;
import com.attest.ict.domain.GenProfile;
import com.attest.ict.domain.Generator;
import com.attest.ict.repository.GenElValRepository;
import com.attest.ict.service.criteria.GenElValCriteria;
import com.attest.ict.service.dto.GenElValDTO;
import com.attest.ict.service.mapper.GenElValMapper;
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
 * Integration tests for the {@link GenElValResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GenElValResourceIT {

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

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;
    private static final Integer SMALLER_STATUS = 1 - 1;

    private static final Double DEFAULT_VOLTAGE_MAGNITUDE = 1D;
    private static final Double UPDATED_VOLTAGE_MAGNITUDE = 2D;
    private static final Double SMALLER_VOLTAGE_MAGNITUDE = 1D - 1D;

    private static final Long DEFAULT_GEN_ID_ON_SUBST = 1L;
    private static final Long UPDATED_GEN_ID_ON_SUBST = 2L;
    private static final Long SMALLER_GEN_ID_ON_SUBST = 1L - 1L;

    private static final String DEFAULT_NOMINAL_VOLTAGE = "AAAAAAAAAA";
    private static final String UPDATED_NOMINAL_VOLTAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/gen-el-vals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GenElValRepository genElValRepository;

    @Autowired
    private GenElValMapper genElValMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGenElValMockMvc;

    private GenElVal genElVal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GenElVal createEntity(EntityManager em) {
        GenElVal genElVal = new GenElVal()
            .hour(DEFAULT_HOUR)
            .min(DEFAULT_MIN)
            .p(DEFAULT_P)
            .q(DEFAULT_Q)
            .status(DEFAULT_STATUS)
            .voltageMagnitude(DEFAULT_VOLTAGE_MAGNITUDE)
            .genIdOnSubst(DEFAULT_GEN_ID_ON_SUBST)
            .nominalVoltage(DEFAULT_NOMINAL_VOLTAGE);
        return genElVal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GenElVal createUpdatedEntity(EntityManager em) {
        GenElVal genElVal = new GenElVal()
            .hour(UPDATED_HOUR)
            .min(UPDATED_MIN)
            .p(UPDATED_P)
            .q(UPDATED_Q)
            .status(UPDATED_STATUS)
            .voltageMagnitude(UPDATED_VOLTAGE_MAGNITUDE)
            .genIdOnSubst(UPDATED_GEN_ID_ON_SUBST)
            .nominalVoltage(UPDATED_NOMINAL_VOLTAGE);
        return genElVal;
    }

    @BeforeEach
    public void initTest() {
        genElVal = createEntity(em);
    }

    @Test
    @Transactional
    void createGenElVal() throws Exception {
        int databaseSizeBeforeCreate = genElValRepository.findAll().size();
        // Create the GenElVal
        GenElValDTO genElValDTO = genElValMapper.toDto(genElVal);
        restGenElValMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genElValDTO))
            )
            .andExpect(status().isCreated());

        // Validate the GenElVal in the database
        List<GenElVal> genElValList = genElValRepository.findAll();
        assertThat(genElValList).hasSize(databaseSizeBeforeCreate + 1);
        GenElVal testGenElVal = genElValList.get(genElValList.size() - 1);
        assertThat(testGenElVal.getHour()).isEqualTo(DEFAULT_HOUR);
        assertThat(testGenElVal.getMin()).isEqualTo(DEFAULT_MIN);
        assertThat(testGenElVal.getP()).isEqualTo(DEFAULT_P);
        assertThat(testGenElVal.getQ()).isEqualTo(DEFAULT_Q);
        assertThat(testGenElVal.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testGenElVal.getVoltageMagnitude()).isEqualTo(DEFAULT_VOLTAGE_MAGNITUDE);
        assertThat(testGenElVal.getGenIdOnSubst()).isEqualTo(DEFAULT_GEN_ID_ON_SUBST);
        assertThat(testGenElVal.getNominalVoltage()).isEqualTo(DEFAULT_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void createGenElValWithExistingId() throws Exception {
        // Create the GenElVal with an existing ID
        genElVal.setId(1L);
        GenElValDTO genElValDTO = genElValMapper.toDto(genElVal);

        int databaseSizeBeforeCreate = genElValRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGenElValMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenElVal in the database
        List<GenElVal> genElValList = genElValRepository.findAll();
        assertThat(genElValList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGenElVals() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList
        restGenElValMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genElVal.getId().intValue())))
            .andExpect(jsonPath("$.[*].hour").value(hasItem(DEFAULT_HOUR)))
            .andExpect(jsonPath("$.[*].min").value(hasItem(DEFAULT_MIN)))
            .andExpect(jsonPath("$.[*].p").value(hasItem(DEFAULT_P.doubleValue())))
            .andExpect(jsonPath("$.[*].q").value(hasItem(DEFAULT_Q.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].voltageMagnitude").value(hasItem(DEFAULT_VOLTAGE_MAGNITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].genIdOnSubst").value(hasItem(DEFAULT_GEN_ID_ON_SUBST.intValue())))
            .andExpect(jsonPath("$.[*].nominalVoltage").value(hasItem(DEFAULT_NOMINAL_VOLTAGE)));
    }

    @Test
    @Transactional
    void getGenElVal() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get the genElVal
        restGenElValMockMvc
            .perform(get(ENTITY_API_URL_ID, genElVal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(genElVal.getId().intValue()))
            .andExpect(jsonPath("$.hour").value(DEFAULT_HOUR))
            .andExpect(jsonPath("$.min").value(DEFAULT_MIN))
            .andExpect(jsonPath("$.p").value(DEFAULT_P.doubleValue()))
            .andExpect(jsonPath("$.q").value(DEFAULT_Q.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.voltageMagnitude").value(DEFAULT_VOLTAGE_MAGNITUDE.doubleValue()))
            .andExpect(jsonPath("$.genIdOnSubst").value(DEFAULT_GEN_ID_ON_SUBST.intValue()))
            .andExpect(jsonPath("$.nominalVoltage").value(DEFAULT_NOMINAL_VOLTAGE));
    }

    @Test
    @Transactional
    void getGenElValsByIdFiltering() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        Long id = genElVal.getId();

        defaultGenElValShouldBeFound("id.equals=" + id);
        defaultGenElValShouldNotBeFound("id.notEquals=" + id);

        defaultGenElValShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGenElValShouldNotBeFound("id.greaterThan=" + id);

        defaultGenElValShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGenElValShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGenElValsByHourIsEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where hour equals to DEFAULT_HOUR
        defaultGenElValShouldBeFound("hour.equals=" + DEFAULT_HOUR);

        // Get all the genElValList where hour equals to UPDATED_HOUR
        defaultGenElValShouldNotBeFound("hour.equals=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllGenElValsByHourIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where hour not equals to DEFAULT_HOUR
        defaultGenElValShouldNotBeFound("hour.notEquals=" + DEFAULT_HOUR);

        // Get all the genElValList where hour not equals to UPDATED_HOUR
        defaultGenElValShouldBeFound("hour.notEquals=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllGenElValsByHourIsInShouldWork() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where hour in DEFAULT_HOUR or UPDATED_HOUR
        defaultGenElValShouldBeFound("hour.in=" + DEFAULT_HOUR + "," + UPDATED_HOUR);

        // Get all the genElValList where hour equals to UPDATED_HOUR
        defaultGenElValShouldNotBeFound("hour.in=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllGenElValsByHourIsNullOrNotNull() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where hour is not null
        defaultGenElValShouldBeFound("hour.specified=true");

        // Get all the genElValList where hour is null
        defaultGenElValShouldNotBeFound("hour.specified=false");
    }

    @Test
    @Transactional
    void getAllGenElValsByHourIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where hour is greater than or equal to DEFAULT_HOUR
        defaultGenElValShouldBeFound("hour.greaterThanOrEqual=" + DEFAULT_HOUR);

        // Get all the genElValList where hour is greater than or equal to UPDATED_HOUR
        defaultGenElValShouldNotBeFound("hour.greaterThanOrEqual=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllGenElValsByHourIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where hour is less than or equal to DEFAULT_HOUR
        defaultGenElValShouldBeFound("hour.lessThanOrEqual=" + DEFAULT_HOUR);

        // Get all the genElValList where hour is less than or equal to SMALLER_HOUR
        defaultGenElValShouldNotBeFound("hour.lessThanOrEqual=" + SMALLER_HOUR);
    }

    @Test
    @Transactional
    void getAllGenElValsByHourIsLessThanSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where hour is less than DEFAULT_HOUR
        defaultGenElValShouldNotBeFound("hour.lessThan=" + DEFAULT_HOUR);

        // Get all the genElValList where hour is less than UPDATED_HOUR
        defaultGenElValShouldBeFound("hour.lessThan=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllGenElValsByHourIsGreaterThanSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where hour is greater than DEFAULT_HOUR
        defaultGenElValShouldNotBeFound("hour.greaterThan=" + DEFAULT_HOUR);

        // Get all the genElValList where hour is greater than SMALLER_HOUR
        defaultGenElValShouldBeFound("hour.greaterThan=" + SMALLER_HOUR);
    }

    @Test
    @Transactional
    void getAllGenElValsByMinIsEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where min equals to DEFAULT_MIN
        defaultGenElValShouldBeFound("min.equals=" + DEFAULT_MIN);

        // Get all the genElValList where min equals to UPDATED_MIN
        defaultGenElValShouldNotBeFound("min.equals=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllGenElValsByMinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where min not equals to DEFAULT_MIN
        defaultGenElValShouldNotBeFound("min.notEquals=" + DEFAULT_MIN);

        // Get all the genElValList where min not equals to UPDATED_MIN
        defaultGenElValShouldBeFound("min.notEquals=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllGenElValsByMinIsInShouldWork() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where min in DEFAULT_MIN or UPDATED_MIN
        defaultGenElValShouldBeFound("min.in=" + DEFAULT_MIN + "," + UPDATED_MIN);

        // Get all the genElValList where min equals to UPDATED_MIN
        defaultGenElValShouldNotBeFound("min.in=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllGenElValsByMinIsNullOrNotNull() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where min is not null
        defaultGenElValShouldBeFound("min.specified=true");

        // Get all the genElValList where min is null
        defaultGenElValShouldNotBeFound("min.specified=false");
    }

    @Test
    @Transactional
    void getAllGenElValsByMinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where min is greater than or equal to DEFAULT_MIN
        defaultGenElValShouldBeFound("min.greaterThanOrEqual=" + DEFAULT_MIN);

        // Get all the genElValList where min is greater than or equal to UPDATED_MIN
        defaultGenElValShouldNotBeFound("min.greaterThanOrEqual=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllGenElValsByMinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where min is less than or equal to DEFAULT_MIN
        defaultGenElValShouldBeFound("min.lessThanOrEqual=" + DEFAULT_MIN);

        // Get all the genElValList where min is less than or equal to SMALLER_MIN
        defaultGenElValShouldNotBeFound("min.lessThanOrEqual=" + SMALLER_MIN);
    }

    @Test
    @Transactional
    void getAllGenElValsByMinIsLessThanSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where min is less than DEFAULT_MIN
        defaultGenElValShouldNotBeFound("min.lessThan=" + DEFAULT_MIN);

        // Get all the genElValList where min is less than UPDATED_MIN
        defaultGenElValShouldBeFound("min.lessThan=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllGenElValsByMinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where min is greater than DEFAULT_MIN
        defaultGenElValShouldNotBeFound("min.greaterThan=" + DEFAULT_MIN);

        // Get all the genElValList where min is greater than SMALLER_MIN
        defaultGenElValShouldBeFound("min.greaterThan=" + SMALLER_MIN);
    }

    @Test
    @Transactional
    void getAllGenElValsByPIsEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where p equals to DEFAULT_P
        defaultGenElValShouldBeFound("p.equals=" + DEFAULT_P);

        // Get all the genElValList where p equals to UPDATED_P
        defaultGenElValShouldNotBeFound("p.equals=" + UPDATED_P);
    }

    @Test
    @Transactional
    void getAllGenElValsByPIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where p not equals to DEFAULT_P
        defaultGenElValShouldNotBeFound("p.notEquals=" + DEFAULT_P);

        // Get all the genElValList where p not equals to UPDATED_P
        defaultGenElValShouldBeFound("p.notEquals=" + UPDATED_P);
    }

    @Test
    @Transactional
    void getAllGenElValsByPIsInShouldWork() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where p in DEFAULT_P or UPDATED_P
        defaultGenElValShouldBeFound("p.in=" + DEFAULT_P + "," + UPDATED_P);

        // Get all the genElValList where p equals to UPDATED_P
        defaultGenElValShouldNotBeFound("p.in=" + UPDATED_P);
    }

    @Test
    @Transactional
    void getAllGenElValsByPIsNullOrNotNull() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where p is not null
        defaultGenElValShouldBeFound("p.specified=true");

        // Get all the genElValList where p is null
        defaultGenElValShouldNotBeFound("p.specified=false");
    }

    @Test
    @Transactional
    void getAllGenElValsByPIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where p is greater than or equal to DEFAULT_P
        defaultGenElValShouldBeFound("p.greaterThanOrEqual=" + DEFAULT_P);

        // Get all the genElValList where p is greater than or equal to UPDATED_P
        defaultGenElValShouldNotBeFound("p.greaterThanOrEqual=" + UPDATED_P);
    }

    @Test
    @Transactional
    void getAllGenElValsByPIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where p is less than or equal to DEFAULT_P
        defaultGenElValShouldBeFound("p.lessThanOrEqual=" + DEFAULT_P);

        // Get all the genElValList where p is less than or equal to SMALLER_P
        defaultGenElValShouldNotBeFound("p.lessThanOrEqual=" + SMALLER_P);
    }

    @Test
    @Transactional
    void getAllGenElValsByPIsLessThanSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where p is less than DEFAULT_P
        defaultGenElValShouldNotBeFound("p.lessThan=" + DEFAULT_P);

        // Get all the genElValList where p is less than UPDATED_P
        defaultGenElValShouldBeFound("p.lessThan=" + UPDATED_P);
    }

    @Test
    @Transactional
    void getAllGenElValsByPIsGreaterThanSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where p is greater than DEFAULT_P
        defaultGenElValShouldNotBeFound("p.greaterThan=" + DEFAULT_P);

        // Get all the genElValList where p is greater than SMALLER_P
        defaultGenElValShouldBeFound("p.greaterThan=" + SMALLER_P);
    }

    @Test
    @Transactional
    void getAllGenElValsByQIsEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where q equals to DEFAULT_Q
        defaultGenElValShouldBeFound("q.equals=" + DEFAULT_Q);

        // Get all the genElValList where q equals to UPDATED_Q
        defaultGenElValShouldNotBeFound("q.equals=" + UPDATED_Q);
    }

    @Test
    @Transactional
    void getAllGenElValsByQIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where q not equals to DEFAULT_Q
        defaultGenElValShouldNotBeFound("q.notEquals=" + DEFAULT_Q);

        // Get all the genElValList where q not equals to UPDATED_Q
        defaultGenElValShouldBeFound("q.notEquals=" + UPDATED_Q);
    }

    @Test
    @Transactional
    void getAllGenElValsByQIsInShouldWork() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where q in DEFAULT_Q or UPDATED_Q
        defaultGenElValShouldBeFound("q.in=" + DEFAULT_Q + "," + UPDATED_Q);

        // Get all the genElValList where q equals to UPDATED_Q
        defaultGenElValShouldNotBeFound("q.in=" + UPDATED_Q);
    }

    @Test
    @Transactional
    void getAllGenElValsByQIsNullOrNotNull() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where q is not null
        defaultGenElValShouldBeFound("q.specified=true");

        // Get all the genElValList where q is null
        defaultGenElValShouldNotBeFound("q.specified=false");
    }

    @Test
    @Transactional
    void getAllGenElValsByQIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where q is greater than or equal to DEFAULT_Q
        defaultGenElValShouldBeFound("q.greaterThanOrEqual=" + DEFAULT_Q);

        // Get all the genElValList where q is greater than or equal to UPDATED_Q
        defaultGenElValShouldNotBeFound("q.greaterThanOrEqual=" + UPDATED_Q);
    }

    @Test
    @Transactional
    void getAllGenElValsByQIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where q is less than or equal to DEFAULT_Q
        defaultGenElValShouldBeFound("q.lessThanOrEqual=" + DEFAULT_Q);

        // Get all the genElValList where q is less than or equal to SMALLER_Q
        defaultGenElValShouldNotBeFound("q.lessThanOrEqual=" + SMALLER_Q);
    }

    @Test
    @Transactional
    void getAllGenElValsByQIsLessThanSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where q is less than DEFAULT_Q
        defaultGenElValShouldNotBeFound("q.lessThan=" + DEFAULT_Q);

        // Get all the genElValList where q is less than UPDATED_Q
        defaultGenElValShouldBeFound("q.lessThan=" + UPDATED_Q);
    }

    @Test
    @Transactional
    void getAllGenElValsByQIsGreaterThanSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where q is greater than DEFAULT_Q
        defaultGenElValShouldNotBeFound("q.greaterThan=" + DEFAULT_Q);

        // Get all the genElValList where q is greater than SMALLER_Q
        defaultGenElValShouldBeFound("q.greaterThan=" + SMALLER_Q);
    }

    @Test
    @Transactional
    void getAllGenElValsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where status equals to DEFAULT_STATUS
        defaultGenElValShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the genElValList where status equals to UPDATED_STATUS
        defaultGenElValShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGenElValsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where status not equals to DEFAULT_STATUS
        defaultGenElValShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the genElValList where status not equals to UPDATED_STATUS
        defaultGenElValShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGenElValsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultGenElValShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the genElValList where status equals to UPDATED_STATUS
        defaultGenElValShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGenElValsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where status is not null
        defaultGenElValShouldBeFound("status.specified=true");

        // Get all the genElValList where status is null
        defaultGenElValShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllGenElValsByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where status is greater than or equal to DEFAULT_STATUS
        defaultGenElValShouldBeFound("status.greaterThanOrEqual=" + DEFAULT_STATUS);

        // Get all the genElValList where status is greater than or equal to UPDATED_STATUS
        defaultGenElValShouldNotBeFound("status.greaterThanOrEqual=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGenElValsByStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where status is less than or equal to DEFAULT_STATUS
        defaultGenElValShouldBeFound("status.lessThanOrEqual=" + DEFAULT_STATUS);

        // Get all the genElValList where status is less than or equal to SMALLER_STATUS
        defaultGenElValShouldNotBeFound("status.lessThanOrEqual=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllGenElValsByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where status is less than DEFAULT_STATUS
        defaultGenElValShouldNotBeFound("status.lessThan=" + DEFAULT_STATUS);

        // Get all the genElValList where status is less than UPDATED_STATUS
        defaultGenElValShouldBeFound("status.lessThan=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGenElValsByStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where status is greater than DEFAULT_STATUS
        defaultGenElValShouldNotBeFound("status.greaterThan=" + DEFAULT_STATUS);

        // Get all the genElValList where status is greater than SMALLER_STATUS
        defaultGenElValShouldBeFound("status.greaterThan=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllGenElValsByVoltageMagnitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where voltageMagnitude equals to DEFAULT_VOLTAGE_MAGNITUDE
        defaultGenElValShouldBeFound("voltageMagnitude.equals=" + DEFAULT_VOLTAGE_MAGNITUDE);

        // Get all the genElValList where voltageMagnitude equals to UPDATED_VOLTAGE_MAGNITUDE
        defaultGenElValShouldNotBeFound("voltageMagnitude.equals=" + UPDATED_VOLTAGE_MAGNITUDE);
    }

    @Test
    @Transactional
    void getAllGenElValsByVoltageMagnitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where voltageMagnitude not equals to DEFAULT_VOLTAGE_MAGNITUDE
        defaultGenElValShouldNotBeFound("voltageMagnitude.notEquals=" + DEFAULT_VOLTAGE_MAGNITUDE);

        // Get all the genElValList where voltageMagnitude not equals to UPDATED_VOLTAGE_MAGNITUDE
        defaultGenElValShouldBeFound("voltageMagnitude.notEquals=" + UPDATED_VOLTAGE_MAGNITUDE);
    }

    @Test
    @Transactional
    void getAllGenElValsByVoltageMagnitudeIsInShouldWork() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where voltageMagnitude in DEFAULT_VOLTAGE_MAGNITUDE or UPDATED_VOLTAGE_MAGNITUDE
        defaultGenElValShouldBeFound("voltageMagnitude.in=" + DEFAULT_VOLTAGE_MAGNITUDE + "," + UPDATED_VOLTAGE_MAGNITUDE);

        // Get all the genElValList where voltageMagnitude equals to UPDATED_VOLTAGE_MAGNITUDE
        defaultGenElValShouldNotBeFound("voltageMagnitude.in=" + UPDATED_VOLTAGE_MAGNITUDE);
    }

    @Test
    @Transactional
    void getAllGenElValsByVoltageMagnitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where voltageMagnitude is not null
        defaultGenElValShouldBeFound("voltageMagnitude.specified=true");

        // Get all the genElValList where voltageMagnitude is null
        defaultGenElValShouldNotBeFound("voltageMagnitude.specified=false");
    }

    @Test
    @Transactional
    void getAllGenElValsByVoltageMagnitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where voltageMagnitude is greater than or equal to DEFAULT_VOLTAGE_MAGNITUDE
        defaultGenElValShouldBeFound("voltageMagnitude.greaterThanOrEqual=" + DEFAULT_VOLTAGE_MAGNITUDE);

        // Get all the genElValList where voltageMagnitude is greater than or equal to UPDATED_VOLTAGE_MAGNITUDE
        defaultGenElValShouldNotBeFound("voltageMagnitude.greaterThanOrEqual=" + UPDATED_VOLTAGE_MAGNITUDE);
    }

    @Test
    @Transactional
    void getAllGenElValsByVoltageMagnitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where voltageMagnitude is less than or equal to DEFAULT_VOLTAGE_MAGNITUDE
        defaultGenElValShouldBeFound("voltageMagnitude.lessThanOrEqual=" + DEFAULT_VOLTAGE_MAGNITUDE);

        // Get all the genElValList where voltageMagnitude is less than or equal to SMALLER_VOLTAGE_MAGNITUDE
        defaultGenElValShouldNotBeFound("voltageMagnitude.lessThanOrEqual=" + SMALLER_VOLTAGE_MAGNITUDE);
    }

    @Test
    @Transactional
    void getAllGenElValsByVoltageMagnitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where voltageMagnitude is less than DEFAULT_VOLTAGE_MAGNITUDE
        defaultGenElValShouldNotBeFound("voltageMagnitude.lessThan=" + DEFAULT_VOLTAGE_MAGNITUDE);

        // Get all the genElValList where voltageMagnitude is less than UPDATED_VOLTAGE_MAGNITUDE
        defaultGenElValShouldBeFound("voltageMagnitude.lessThan=" + UPDATED_VOLTAGE_MAGNITUDE);
    }

    @Test
    @Transactional
    void getAllGenElValsByVoltageMagnitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where voltageMagnitude is greater than DEFAULT_VOLTAGE_MAGNITUDE
        defaultGenElValShouldNotBeFound("voltageMagnitude.greaterThan=" + DEFAULT_VOLTAGE_MAGNITUDE);

        // Get all the genElValList where voltageMagnitude is greater than SMALLER_VOLTAGE_MAGNITUDE
        defaultGenElValShouldBeFound("voltageMagnitude.greaterThan=" + SMALLER_VOLTAGE_MAGNITUDE);
    }

    @Test
    @Transactional
    void getAllGenElValsByGenIdOnSubstIsEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where genIdOnSubst equals to DEFAULT_GEN_ID_ON_SUBST
        defaultGenElValShouldBeFound("genIdOnSubst.equals=" + DEFAULT_GEN_ID_ON_SUBST);

        // Get all the genElValList where genIdOnSubst equals to UPDATED_GEN_ID_ON_SUBST
        defaultGenElValShouldNotBeFound("genIdOnSubst.equals=" + UPDATED_GEN_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllGenElValsByGenIdOnSubstIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where genIdOnSubst not equals to DEFAULT_GEN_ID_ON_SUBST
        defaultGenElValShouldNotBeFound("genIdOnSubst.notEquals=" + DEFAULT_GEN_ID_ON_SUBST);

        // Get all the genElValList where genIdOnSubst not equals to UPDATED_GEN_ID_ON_SUBST
        defaultGenElValShouldBeFound("genIdOnSubst.notEquals=" + UPDATED_GEN_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllGenElValsByGenIdOnSubstIsInShouldWork() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where genIdOnSubst in DEFAULT_GEN_ID_ON_SUBST or UPDATED_GEN_ID_ON_SUBST
        defaultGenElValShouldBeFound("genIdOnSubst.in=" + DEFAULT_GEN_ID_ON_SUBST + "," + UPDATED_GEN_ID_ON_SUBST);

        // Get all the genElValList where genIdOnSubst equals to UPDATED_GEN_ID_ON_SUBST
        defaultGenElValShouldNotBeFound("genIdOnSubst.in=" + UPDATED_GEN_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllGenElValsByGenIdOnSubstIsNullOrNotNull() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where genIdOnSubst is not null
        defaultGenElValShouldBeFound("genIdOnSubst.specified=true");

        // Get all the genElValList where genIdOnSubst is null
        defaultGenElValShouldNotBeFound("genIdOnSubst.specified=false");
    }

    @Test
    @Transactional
    void getAllGenElValsByGenIdOnSubstIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where genIdOnSubst is greater than or equal to DEFAULT_GEN_ID_ON_SUBST
        defaultGenElValShouldBeFound("genIdOnSubst.greaterThanOrEqual=" + DEFAULT_GEN_ID_ON_SUBST);

        // Get all the genElValList where genIdOnSubst is greater than or equal to UPDATED_GEN_ID_ON_SUBST
        defaultGenElValShouldNotBeFound("genIdOnSubst.greaterThanOrEqual=" + UPDATED_GEN_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllGenElValsByGenIdOnSubstIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where genIdOnSubst is less than or equal to DEFAULT_GEN_ID_ON_SUBST
        defaultGenElValShouldBeFound("genIdOnSubst.lessThanOrEqual=" + DEFAULT_GEN_ID_ON_SUBST);

        // Get all the genElValList where genIdOnSubst is less than or equal to SMALLER_GEN_ID_ON_SUBST
        defaultGenElValShouldNotBeFound("genIdOnSubst.lessThanOrEqual=" + SMALLER_GEN_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllGenElValsByGenIdOnSubstIsLessThanSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where genIdOnSubst is less than DEFAULT_GEN_ID_ON_SUBST
        defaultGenElValShouldNotBeFound("genIdOnSubst.lessThan=" + DEFAULT_GEN_ID_ON_SUBST);

        // Get all the genElValList where genIdOnSubst is less than UPDATED_GEN_ID_ON_SUBST
        defaultGenElValShouldBeFound("genIdOnSubst.lessThan=" + UPDATED_GEN_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllGenElValsByGenIdOnSubstIsGreaterThanSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where genIdOnSubst is greater than DEFAULT_GEN_ID_ON_SUBST
        defaultGenElValShouldNotBeFound("genIdOnSubst.greaterThan=" + DEFAULT_GEN_ID_ON_SUBST);

        // Get all the genElValList where genIdOnSubst is greater than SMALLER_GEN_ID_ON_SUBST
        defaultGenElValShouldBeFound("genIdOnSubst.greaterThan=" + SMALLER_GEN_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllGenElValsByNominalVoltageIsEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where nominalVoltage equals to DEFAULT_NOMINAL_VOLTAGE
        defaultGenElValShouldBeFound("nominalVoltage.equals=" + DEFAULT_NOMINAL_VOLTAGE);

        // Get all the genElValList where nominalVoltage equals to UPDATED_NOMINAL_VOLTAGE
        defaultGenElValShouldNotBeFound("nominalVoltage.equals=" + UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllGenElValsByNominalVoltageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where nominalVoltage not equals to DEFAULT_NOMINAL_VOLTAGE
        defaultGenElValShouldNotBeFound("nominalVoltage.notEquals=" + DEFAULT_NOMINAL_VOLTAGE);

        // Get all the genElValList where nominalVoltage not equals to UPDATED_NOMINAL_VOLTAGE
        defaultGenElValShouldBeFound("nominalVoltage.notEquals=" + UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllGenElValsByNominalVoltageIsInShouldWork() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where nominalVoltage in DEFAULT_NOMINAL_VOLTAGE or UPDATED_NOMINAL_VOLTAGE
        defaultGenElValShouldBeFound("nominalVoltage.in=" + DEFAULT_NOMINAL_VOLTAGE + "," + UPDATED_NOMINAL_VOLTAGE);

        // Get all the genElValList where nominalVoltage equals to UPDATED_NOMINAL_VOLTAGE
        defaultGenElValShouldNotBeFound("nominalVoltage.in=" + UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllGenElValsByNominalVoltageIsNullOrNotNull() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where nominalVoltage is not null
        defaultGenElValShouldBeFound("nominalVoltage.specified=true");

        // Get all the genElValList where nominalVoltage is null
        defaultGenElValShouldNotBeFound("nominalVoltage.specified=false");
    }

    @Test
    @Transactional
    void getAllGenElValsByNominalVoltageContainsSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where nominalVoltage contains DEFAULT_NOMINAL_VOLTAGE
        defaultGenElValShouldBeFound("nominalVoltage.contains=" + DEFAULT_NOMINAL_VOLTAGE);

        // Get all the genElValList where nominalVoltage contains UPDATED_NOMINAL_VOLTAGE
        defaultGenElValShouldNotBeFound("nominalVoltage.contains=" + UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllGenElValsByNominalVoltageNotContainsSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        // Get all the genElValList where nominalVoltage does not contain DEFAULT_NOMINAL_VOLTAGE
        defaultGenElValShouldNotBeFound("nominalVoltage.doesNotContain=" + DEFAULT_NOMINAL_VOLTAGE);

        // Get all the genElValList where nominalVoltage does not contain UPDATED_NOMINAL_VOLTAGE
        defaultGenElValShouldBeFound("nominalVoltage.doesNotContain=" + UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllGenElValsByGenProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);
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
        genElVal.setGenProfile(genProfile);
        genElValRepository.saveAndFlush(genElVal);
        Long genProfileId = genProfile.getId();

        // Get all the genElValList where genProfile equals to genProfileId
        defaultGenElValShouldBeFound("genProfileId.equals=" + genProfileId);

        // Get all the genElValList where genProfile equals to (genProfileId + 1)
        defaultGenElValShouldNotBeFound("genProfileId.equals=" + (genProfileId + 1));
    }

    @Test
    @Transactional
    void getAllGenElValsByGeneratorIsEqualToSomething() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);
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
        genElVal.setGenerator(generator);
        genElValRepository.saveAndFlush(genElVal);
        Long generatorId = generator.getId();

        // Get all the genElValList where generator equals to generatorId
        defaultGenElValShouldBeFound("generatorId.equals=" + generatorId);

        // Get all the genElValList where generator equals to (generatorId + 1)
        defaultGenElValShouldNotBeFound("generatorId.equals=" + (generatorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGenElValShouldBeFound(String filter) throws Exception {
        restGenElValMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genElVal.getId().intValue())))
            .andExpect(jsonPath("$.[*].hour").value(hasItem(DEFAULT_HOUR)))
            .andExpect(jsonPath("$.[*].min").value(hasItem(DEFAULT_MIN)))
            .andExpect(jsonPath("$.[*].p").value(hasItem(DEFAULT_P.doubleValue())))
            .andExpect(jsonPath("$.[*].q").value(hasItem(DEFAULT_Q.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].voltageMagnitude").value(hasItem(DEFAULT_VOLTAGE_MAGNITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].genIdOnSubst").value(hasItem(DEFAULT_GEN_ID_ON_SUBST.intValue())))
            .andExpect(jsonPath("$.[*].nominalVoltage").value(hasItem(DEFAULT_NOMINAL_VOLTAGE)));

        // Check, that the count call also returns 1
        restGenElValMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGenElValShouldNotBeFound(String filter) throws Exception {
        restGenElValMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGenElValMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGenElVal() throws Exception {
        // Get the genElVal
        restGenElValMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGenElVal() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        int databaseSizeBeforeUpdate = genElValRepository.findAll().size();

        // Update the genElVal
        GenElVal updatedGenElVal = genElValRepository.findById(genElVal.getId()).get();
        // Disconnect from session so that the updates on updatedGenElVal are not directly saved in db
        em.detach(updatedGenElVal);
        updatedGenElVal
            .hour(UPDATED_HOUR)
            .min(UPDATED_MIN)
            .p(UPDATED_P)
            .q(UPDATED_Q)
            .status(UPDATED_STATUS)
            .voltageMagnitude(UPDATED_VOLTAGE_MAGNITUDE)
            .genIdOnSubst(UPDATED_GEN_ID_ON_SUBST)
            .nominalVoltage(UPDATED_NOMINAL_VOLTAGE);
        GenElValDTO genElValDTO = genElValMapper.toDto(updatedGenElVal);

        restGenElValMockMvc
            .perform(
                put(ENTITY_API_URL_ID, genElValDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genElValDTO))
            )
            .andExpect(status().isOk());

        // Validate the GenElVal in the database
        List<GenElVal> genElValList = genElValRepository.findAll();
        assertThat(genElValList).hasSize(databaseSizeBeforeUpdate);
        GenElVal testGenElVal = genElValList.get(genElValList.size() - 1);
        assertThat(testGenElVal.getHour()).isEqualTo(UPDATED_HOUR);
        assertThat(testGenElVal.getMin()).isEqualTo(UPDATED_MIN);
        assertThat(testGenElVal.getP()).isEqualTo(UPDATED_P);
        assertThat(testGenElVal.getQ()).isEqualTo(UPDATED_Q);
        assertThat(testGenElVal.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testGenElVal.getVoltageMagnitude()).isEqualTo(UPDATED_VOLTAGE_MAGNITUDE);
        assertThat(testGenElVal.getGenIdOnSubst()).isEqualTo(UPDATED_GEN_ID_ON_SUBST);
        assertThat(testGenElVal.getNominalVoltage()).isEqualTo(UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void putNonExistingGenElVal() throws Exception {
        int databaseSizeBeforeUpdate = genElValRepository.findAll().size();
        genElVal.setId(count.incrementAndGet());

        // Create the GenElVal
        GenElValDTO genElValDTO = genElValMapper.toDto(genElVal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenElValMockMvc
            .perform(
                put(ENTITY_API_URL_ID, genElValDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenElVal in the database
        List<GenElVal> genElValList = genElValRepository.findAll();
        assertThat(genElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGenElVal() throws Exception {
        int databaseSizeBeforeUpdate = genElValRepository.findAll().size();
        genElVal.setId(count.incrementAndGet());

        // Create the GenElVal
        GenElValDTO genElValDTO = genElValMapper.toDto(genElVal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenElValMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenElVal in the database
        List<GenElVal> genElValList = genElValRepository.findAll();
        assertThat(genElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGenElVal() throws Exception {
        int databaseSizeBeforeUpdate = genElValRepository.findAll().size();
        genElVal.setId(count.incrementAndGet());

        // Create the GenElVal
        GenElValDTO genElValDTO = genElValMapper.toDto(genElVal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenElValMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genElValDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GenElVal in the database
        List<GenElVal> genElValList = genElValRepository.findAll();
        assertThat(genElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGenElValWithPatch() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        int databaseSizeBeforeUpdate = genElValRepository.findAll().size();

        // Update the genElVal using partial update
        GenElVal partialUpdatedGenElVal = new GenElVal();
        partialUpdatedGenElVal.setId(genElVal.getId());

        partialUpdatedGenElVal
            .hour(UPDATED_HOUR)
            .min(UPDATED_MIN)
            .genIdOnSubst(UPDATED_GEN_ID_ON_SUBST)
            .nominalVoltage(UPDATED_NOMINAL_VOLTAGE);

        restGenElValMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenElVal.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenElVal))
            )
            .andExpect(status().isOk());

        // Validate the GenElVal in the database
        List<GenElVal> genElValList = genElValRepository.findAll();
        assertThat(genElValList).hasSize(databaseSizeBeforeUpdate);
        GenElVal testGenElVal = genElValList.get(genElValList.size() - 1);
        assertThat(testGenElVal.getHour()).isEqualTo(UPDATED_HOUR);
        assertThat(testGenElVal.getMin()).isEqualTo(UPDATED_MIN);
        assertThat(testGenElVal.getP()).isEqualTo(DEFAULT_P);
        assertThat(testGenElVal.getQ()).isEqualTo(DEFAULT_Q);
        assertThat(testGenElVal.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testGenElVal.getVoltageMagnitude()).isEqualTo(DEFAULT_VOLTAGE_MAGNITUDE);
        assertThat(testGenElVal.getGenIdOnSubst()).isEqualTo(UPDATED_GEN_ID_ON_SUBST);
        assertThat(testGenElVal.getNominalVoltage()).isEqualTo(UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void fullUpdateGenElValWithPatch() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        int databaseSizeBeforeUpdate = genElValRepository.findAll().size();

        // Update the genElVal using partial update
        GenElVal partialUpdatedGenElVal = new GenElVal();
        partialUpdatedGenElVal.setId(genElVal.getId());

        partialUpdatedGenElVal
            .hour(UPDATED_HOUR)
            .min(UPDATED_MIN)
            .p(UPDATED_P)
            .q(UPDATED_Q)
            .status(UPDATED_STATUS)
            .voltageMagnitude(UPDATED_VOLTAGE_MAGNITUDE)
            .genIdOnSubst(UPDATED_GEN_ID_ON_SUBST)
            .nominalVoltage(UPDATED_NOMINAL_VOLTAGE);

        restGenElValMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenElVal.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenElVal))
            )
            .andExpect(status().isOk());

        // Validate the GenElVal in the database
        List<GenElVal> genElValList = genElValRepository.findAll();
        assertThat(genElValList).hasSize(databaseSizeBeforeUpdate);
        GenElVal testGenElVal = genElValList.get(genElValList.size() - 1);
        assertThat(testGenElVal.getHour()).isEqualTo(UPDATED_HOUR);
        assertThat(testGenElVal.getMin()).isEqualTo(UPDATED_MIN);
        assertThat(testGenElVal.getP()).isEqualTo(UPDATED_P);
        assertThat(testGenElVal.getQ()).isEqualTo(UPDATED_Q);
        assertThat(testGenElVal.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testGenElVal.getVoltageMagnitude()).isEqualTo(UPDATED_VOLTAGE_MAGNITUDE);
        assertThat(testGenElVal.getGenIdOnSubst()).isEqualTo(UPDATED_GEN_ID_ON_SUBST);
        assertThat(testGenElVal.getNominalVoltage()).isEqualTo(UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void patchNonExistingGenElVal() throws Exception {
        int databaseSizeBeforeUpdate = genElValRepository.findAll().size();
        genElVal.setId(count.incrementAndGet());

        // Create the GenElVal
        GenElValDTO genElValDTO = genElValMapper.toDto(genElVal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenElValMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, genElValDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenElVal in the database
        List<GenElVal> genElValList = genElValRepository.findAll();
        assertThat(genElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGenElVal() throws Exception {
        int databaseSizeBeforeUpdate = genElValRepository.findAll().size();
        genElVal.setId(count.incrementAndGet());

        // Create the GenElVal
        GenElValDTO genElValDTO = genElValMapper.toDto(genElVal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenElValMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenElVal in the database
        List<GenElVal> genElValList = genElValRepository.findAll();
        assertThat(genElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGenElVal() throws Exception {
        int databaseSizeBeforeUpdate = genElValRepository.findAll().size();
        genElVal.setId(count.incrementAndGet());

        // Create the GenElVal
        GenElValDTO genElValDTO = genElValMapper.toDto(genElVal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenElValMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genElValDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GenElVal in the database
        List<GenElVal> genElValList = genElValRepository.findAll();
        assertThat(genElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGenElVal() throws Exception {
        // Initialize the database
        genElValRepository.saveAndFlush(genElVal);

        int databaseSizeBeforeDelete = genElValRepository.findAll().size();

        // Delete the genElVal
        restGenElValMockMvc
            .perform(delete(ENTITY_API_URL_ID, genElVal.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GenElVal> genElValList = genElValRepository.findAll();
        assertThat(genElValList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
