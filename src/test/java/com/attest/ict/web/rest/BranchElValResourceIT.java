package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Branch;
import com.attest.ict.domain.BranchElVal;
import com.attest.ict.domain.BranchProfile;
import com.attest.ict.repository.BranchElValRepository;
import com.attest.ict.service.criteria.BranchElValCriteria;
import com.attest.ict.service.dto.BranchElValDTO;
import com.attest.ict.service.mapper.BranchElValMapper;
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
 * Integration tests for the {@link BranchElValResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BranchElValResourceIT {

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

    private static final Long DEFAULT_BRANCH_ID_ON_SUBST = 1L;
    private static final Long UPDATED_BRANCH_ID_ON_SUBST = 2L;
    private static final Long SMALLER_BRANCH_ID_ON_SUBST = 1L - 1L;

    private static final String DEFAULT_NOMINAL_VOLTAGE = "AAAAAAAAAA";
    private static final String UPDATED_NOMINAL_VOLTAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/branch-el-vals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BranchElValRepository branchElValRepository;

    @Autowired
    private BranchElValMapper branchElValMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBranchElValMockMvc;

    private BranchElVal branchElVal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BranchElVal createEntity(EntityManager em) {
        BranchElVal branchElVal = new BranchElVal()
            .hour(DEFAULT_HOUR)
            .min(DEFAULT_MIN)
            .p(DEFAULT_P)
            .q(DEFAULT_Q)
            .status(DEFAULT_STATUS)
            .branchIdOnSubst(DEFAULT_BRANCH_ID_ON_SUBST)
            .nominalVoltage(DEFAULT_NOMINAL_VOLTAGE);
        return branchElVal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BranchElVal createUpdatedEntity(EntityManager em) {
        BranchElVal branchElVal = new BranchElVal()
            .hour(UPDATED_HOUR)
            .min(UPDATED_MIN)
            .p(UPDATED_P)
            .q(UPDATED_Q)
            .status(UPDATED_STATUS)
            .branchIdOnSubst(UPDATED_BRANCH_ID_ON_SUBST)
            .nominalVoltage(UPDATED_NOMINAL_VOLTAGE);
        return branchElVal;
    }

    @BeforeEach
    public void initTest() {
        branchElVal = createEntity(em);
    }

    @Test
    @Transactional
    void createBranchElVal() throws Exception {
        int databaseSizeBeforeCreate = branchElValRepository.findAll().size();
        // Create the BranchElVal
        BranchElValDTO branchElValDTO = branchElValMapper.toDto(branchElVal);
        restBranchElValMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchElValDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BranchElVal in the database
        List<BranchElVal> branchElValList = branchElValRepository.findAll();
        assertThat(branchElValList).hasSize(databaseSizeBeforeCreate + 1);
        BranchElVal testBranchElVal = branchElValList.get(branchElValList.size() - 1);
        assertThat(testBranchElVal.getHour()).isEqualTo(DEFAULT_HOUR);
        assertThat(testBranchElVal.getMin()).isEqualTo(DEFAULT_MIN);
        assertThat(testBranchElVal.getP()).isEqualTo(DEFAULT_P);
        assertThat(testBranchElVal.getQ()).isEqualTo(DEFAULT_Q);
        assertThat(testBranchElVal.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBranchElVal.getBranchIdOnSubst()).isEqualTo(DEFAULT_BRANCH_ID_ON_SUBST);
        assertThat(testBranchElVal.getNominalVoltage()).isEqualTo(DEFAULT_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void createBranchElValWithExistingId() throws Exception {
        // Create the BranchElVal with an existing ID
        branchElVal.setId(1L);
        BranchElValDTO branchElValDTO = branchElValMapper.toDto(branchElVal);

        int databaseSizeBeforeCreate = branchElValRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBranchElValMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchElVal in the database
        List<BranchElVal> branchElValList = branchElValRepository.findAll();
        assertThat(branchElValList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBranchElVals() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList
        restBranchElValMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branchElVal.getId().intValue())))
            .andExpect(jsonPath("$.[*].hour").value(hasItem(DEFAULT_HOUR)))
            .andExpect(jsonPath("$.[*].min").value(hasItem(DEFAULT_MIN)))
            .andExpect(jsonPath("$.[*].p").value(hasItem(DEFAULT_P.doubleValue())))
            .andExpect(jsonPath("$.[*].q").value(hasItem(DEFAULT_Q.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].branchIdOnSubst").value(hasItem(DEFAULT_BRANCH_ID_ON_SUBST.intValue())))
            .andExpect(jsonPath("$.[*].nominalVoltage").value(hasItem(DEFAULT_NOMINAL_VOLTAGE)));
    }

    @Test
    @Transactional
    void getBranchElVal() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get the branchElVal
        restBranchElValMockMvc
            .perform(get(ENTITY_API_URL_ID, branchElVal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(branchElVal.getId().intValue()))
            .andExpect(jsonPath("$.hour").value(DEFAULT_HOUR))
            .andExpect(jsonPath("$.min").value(DEFAULT_MIN))
            .andExpect(jsonPath("$.p").value(DEFAULT_P.doubleValue()))
            .andExpect(jsonPath("$.q").value(DEFAULT_Q.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.branchIdOnSubst").value(DEFAULT_BRANCH_ID_ON_SUBST.intValue()))
            .andExpect(jsonPath("$.nominalVoltage").value(DEFAULT_NOMINAL_VOLTAGE));
    }

    @Test
    @Transactional
    void getBranchElValsByIdFiltering() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        Long id = branchElVal.getId();

        defaultBranchElValShouldBeFound("id.equals=" + id);
        defaultBranchElValShouldNotBeFound("id.notEquals=" + id);

        defaultBranchElValShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBranchElValShouldNotBeFound("id.greaterThan=" + id);

        defaultBranchElValShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBranchElValShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBranchElValsByHourIsEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where hour equals to DEFAULT_HOUR
        defaultBranchElValShouldBeFound("hour.equals=" + DEFAULT_HOUR);

        // Get all the branchElValList where hour equals to UPDATED_HOUR
        defaultBranchElValShouldNotBeFound("hour.equals=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllBranchElValsByHourIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where hour not equals to DEFAULT_HOUR
        defaultBranchElValShouldNotBeFound("hour.notEquals=" + DEFAULT_HOUR);

        // Get all the branchElValList where hour not equals to UPDATED_HOUR
        defaultBranchElValShouldBeFound("hour.notEquals=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllBranchElValsByHourIsInShouldWork() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where hour in DEFAULT_HOUR or UPDATED_HOUR
        defaultBranchElValShouldBeFound("hour.in=" + DEFAULT_HOUR + "," + UPDATED_HOUR);

        // Get all the branchElValList where hour equals to UPDATED_HOUR
        defaultBranchElValShouldNotBeFound("hour.in=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllBranchElValsByHourIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where hour is not null
        defaultBranchElValShouldBeFound("hour.specified=true");

        // Get all the branchElValList where hour is null
        defaultBranchElValShouldNotBeFound("hour.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchElValsByHourIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where hour is greater than or equal to DEFAULT_HOUR
        defaultBranchElValShouldBeFound("hour.greaterThanOrEqual=" + DEFAULT_HOUR);

        // Get all the branchElValList where hour is greater than or equal to UPDATED_HOUR
        defaultBranchElValShouldNotBeFound("hour.greaterThanOrEqual=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllBranchElValsByHourIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where hour is less than or equal to DEFAULT_HOUR
        defaultBranchElValShouldBeFound("hour.lessThanOrEqual=" + DEFAULT_HOUR);

        // Get all the branchElValList where hour is less than or equal to SMALLER_HOUR
        defaultBranchElValShouldNotBeFound("hour.lessThanOrEqual=" + SMALLER_HOUR);
    }

    @Test
    @Transactional
    void getAllBranchElValsByHourIsLessThanSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where hour is less than DEFAULT_HOUR
        defaultBranchElValShouldNotBeFound("hour.lessThan=" + DEFAULT_HOUR);

        // Get all the branchElValList where hour is less than UPDATED_HOUR
        defaultBranchElValShouldBeFound("hour.lessThan=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllBranchElValsByHourIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where hour is greater than DEFAULT_HOUR
        defaultBranchElValShouldNotBeFound("hour.greaterThan=" + DEFAULT_HOUR);

        // Get all the branchElValList where hour is greater than SMALLER_HOUR
        defaultBranchElValShouldBeFound("hour.greaterThan=" + SMALLER_HOUR);
    }

    @Test
    @Transactional
    void getAllBranchElValsByMinIsEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where min equals to DEFAULT_MIN
        defaultBranchElValShouldBeFound("min.equals=" + DEFAULT_MIN);

        // Get all the branchElValList where min equals to UPDATED_MIN
        defaultBranchElValShouldNotBeFound("min.equals=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllBranchElValsByMinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where min not equals to DEFAULT_MIN
        defaultBranchElValShouldNotBeFound("min.notEquals=" + DEFAULT_MIN);

        // Get all the branchElValList where min not equals to UPDATED_MIN
        defaultBranchElValShouldBeFound("min.notEquals=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllBranchElValsByMinIsInShouldWork() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where min in DEFAULT_MIN or UPDATED_MIN
        defaultBranchElValShouldBeFound("min.in=" + DEFAULT_MIN + "," + UPDATED_MIN);

        // Get all the branchElValList where min equals to UPDATED_MIN
        defaultBranchElValShouldNotBeFound("min.in=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllBranchElValsByMinIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where min is not null
        defaultBranchElValShouldBeFound("min.specified=true");

        // Get all the branchElValList where min is null
        defaultBranchElValShouldNotBeFound("min.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchElValsByMinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where min is greater than or equal to DEFAULT_MIN
        defaultBranchElValShouldBeFound("min.greaterThanOrEqual=" + DEFAULT_MIN);

        // Get all the branchElValList where min is greater than or equal to UPDATED_MIN
        defaultBranchElValShouldNotBeFound("min.greaterThanOrEqual=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllBranchElValsByMinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where min is less than or equal to DEFAULT_MIN
        defaultBranchElValShouldBeFound("min.lessThanOrEqual=" + DEFAULT_MIN);

        // Get all the branchElValList where min is less than or equal to SMALLER_MIN
        defaultBranchElValShouldNotBeFound("min.lessThanOrEqual=" + SMALLER_MIN);
    }

    @Test
    @Transactional
    void getAllBranchElValsByMinIsLessThanSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where min is less than DEFAULT_MIN
        defaultBranchElValShouldNotBeFound("min.lessThan=" + DEFAULT_MIN);

        // Get all the branchElValList where min is less than UPDATED_MIN
        defaultBranchElValShouldBeFound("min.lessThan=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllBranchElValsByMinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where min is greater than DEFAULT_MIN
        defaultBranchElValShouldNotBeFound("min.greaterThan=" + DEFAULT_MIN);

        // Get all the branchElValList where min is greater than SMALLER_MIN
        defaultBranchElValShouldBeFound("min.greaterThan=" + SMALLER_MIN);
    }

    @Test
    @Transactional
    void getAllBranchElValsByPIsEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where p equals to DEFAULT_P
        defaultBranchElValShouldBeFound("p.equals=" + DEFAULT_P);

        // Get all the branchElValList where p equals to UPDATED_P
        defaultBranchElValShouldNotBeFound("p.equals=" + UPDATED_P);
    }

    @Test
    @Transactional
    void getAllBranchElValsByPIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where p not equals to DEFAULT_P
        defaultBranchElValShouldNotBeFound("p.notEquals=" + DEFAULT_P);

        // Get all the branchElValList where p not equals to UPDATED_P
        defaultBranchElValShouldBeFound("p.notEquals=" + UPDATED_P);
    }

    @Test
    @Transactional
    void getAllBranchElValsByPIsInShouldWork() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where p in DEFAULT_P or UPDATED_P
        defaultBranchElValShouldBeFound("p.in=" + DEFAULT_P + "," + UPDATED_P);

        // Get all the branchElValList where p equals to UPDATED_P
        defaultBranchElValShouldNotBeFound("p.in=" + UPDATED_P);
    }

    @Test
    @Transactional
    void getAllBranchElValsByPIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where p is not null
        defaultBranchElValShouldBeFound("p.specified=true");

        // Get all the branchElValList where p is null
        defaultBranchElValShouldNotBeFound("p.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchElValsByPIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where p is greater than or equal to DEFAULT_P
        defaultBranchElValShouldBeFound("p.greaterThanOrEqual=" + DEFAULT_P);

        // Get all the branchElValList where p is greater than or equal to UPDATED_P
        defaultBranchElValShouldNotBeFound("p.greaterThanOrEqual=" + UPDATED_P);
    }

    @Test
    @Transactional
    void getAllBranchElValsByPIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where p is less than or equal to DEFAULT_P
        defaultBranchElValShouldBeFound("p.lessThanOrEqual=" + DEFAULT_P);

        // Get all the branchElValList where p is less than or equal to SMALLER_P
        defaultBranchElValShouldNotBeFound("p.lessThanOrEqual=" + SMALLER_P);
    }

    @Test
    @Transactional
    void getAllBranchElValsByPIsLessThanSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where p is less than DEFAULT_P
        defaultBranchElValShouldNotBeFound("p.lessThan=" + DEFAULT_P);

        // Get all the branchElValList where p is less than UPDATED_P
        defaultBranchElValShouldBeFound("p.lessThan=" + UPDATED_P);
    }

    @Test
    @Transactional
    void getAllBranchElValsByPIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where p is greater than DEFAULT_P
        defaultBranchElValShouldNotBeFound("p.greaterThan=" + DEFAULT_P);

        // Get all the branchElValList where p is greater than SMALLER_P
        defaultBranchElValShouldBeFound("p.greaterThan=" + SMALLER_P);
    }

    @Test
    @Transactional
    void getAllBranchElValsByQIsEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where q equals to DEFAULT_Q
        defaultBranchElValShouldBeFound("q.equals=" + DEFAULT_Q);

        // Get all the branchElValList where q equals to UPDATED_Q
        defaultBranchElValShouldNotBeFound("q.equals=" + UPDATED_Q);
    }

    @Test
    @Transactional
    void getAllBranchElValsByQIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where q not equals to DEFAULT_Q
        defaultBranchElValShouldNotBeFound("q.notEquals=" + DEFAULT_Q);

        // Get all the branchElValList where q not equals to UPDATED_Q
        defaultBranchElValShouldBeFound("q.notEquals=" + UPDATED_Q);
    }

    @Test
    @Transactional
    void getAllBranchElValsByQIsInShouldWork() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where q in DEFAULT_Q or UPDATED_Q
        defaultBranchElValShouldBeFound("q.in=" + DEFAULT_Q + "," + UPDATED_Q);

        // Get all the branchElValList where q equals to UPDATED_Q
        defaultBranchElValShouldNotBeFound("q.in=" + UPDATED_Q);
    }

    @Test
    @Transactional
    void getAllBranchElValsByQIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where q is not null
        defaultBranchElValShouldBeFound("q.specified=true");

        // Get all the branchElValList where q is null
        defaultBranchElValShouldNotBeFound("q.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchElValsByQIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where q is greater than or equal to DEFAULT_Q
        defaultBranchElValShouldBeFound("q.greaterThanOrEqual=" + DEFAULT_Q);

        // Get all the branchElValList where q is greater than or equal to UPDATED_Q
        defaultBranchElValShouldNotBeFound("q.greaterThanOrEqual=" + UPDATED_Q);
    }

    @Test
    @Transactional
    void getAllBranchElValsByQIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where q is less than or equal to DEFAULT_Q
        defaultBranchElValShouldBeFound("q.lessThanOrEqual=" + DEFAULT_Q);

        // Get all the branchElValList where q is less than or equal to SMALLER_Q
        defaultBranchElValShouldNotBeFound("q.lessThanOrEqual=" + SMALLER_Q);
    }

    @Test
    @Transactional
    void getAllBranchElValsByQIsLessThanSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where q is less than DEFAULT_Q
        defaultBranchElValShouldNotBeFound("q.lessThan=" + DEFAULT_Q);

        // Get all the branchElValList where q is less than UPDATED_Q
        defaultBranchElValShouldBeFound("q.lessThan=" + UPDATED_Q);
    }

    @Test
    @Transactional
    void getAllBranchElValsByQIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where q is greater than DEFAULT_Q
        defaultBranchElValShouldNotBeFound("q.greaterThan=" + DEFAULT_Q);

        // Get all the branchElValList where q is greater than SMALLER_Q
        defaultBranchElValShouldBeFound("q.greaterThan=" + SMALLER_Q);
    }

    @Test
    @Transactional
    void getAllBranchElValsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where status equals to DEFAULT_STATUS
        defaultBranchElValShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the branchElValList where status equals to UPDATED_STATUS
        defaultBranchElValShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBranchElValsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where status not equals to DEFAULT_STATUS
        defaultBranchElValShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the branchElValList where status not equals to UPDATED_STATUS
        defaultBranchElValShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBranchElValsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultBranchElValShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the branchElValList where status equals to UPDATED_STATUS
        defaultBranchElValShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBranchElValsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where status is not null
        defaultBranchElValShouldBeFound("status.specified=true");

        // Get all the branchElValList where status is null
        defaultBranchElValShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchElValsByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where status is greater than or equal to DEFAULT_STATUS
        defaultBranchElValShouldBeFound("status.greaterThanOrEqual=" + DEFAULT_STATUS);

        // Get all the branchElValList where status is greater than or equal to UPDATED_STATUS
        defaultBranchElValShouldNotBeFound("status.greaterThanOrEqual=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBranchElValsByStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where status is less than or equal to DEFAULT_STATUS
        defaultBranchElValShouldBeFound("status.lessThanOrEqual=" + DEFAULT_STATUS);

        // Get all the branchElValList where status is less than or equal to SMALLER_STATUS
        defaultBranchElValShouldNotBeFound("status.lessThanOrEqual=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllBranchElValsByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where status is less than DEFAULT_STATUS
        defaultBranchElValShouldNotBeFound("status.lessThan=" + DEFAULT_STATUS);

        // Get all the branchElValList where status is less than UPDATED_STATUS
        defaultBranchElValShouldBeFound("status.lessThan=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBranchElValsByStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where status is greater than DEFAULT_STATUS
        defaultBranchElValShouldNotBeFound("status.greaterThan=" + DEFAULT_STATUS);

        // Get all the branchElValList where status is greater than SMALLER_STATUS
        defaultBranchElValShouldBeFound("status.greaterThan=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllBranchElValsByBranchIdOnSubstIsEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where branchIdOnSubst equals to DEFAULT_BRANCH_ID_ON_SUBST
        defaultBranchElValShouldBeFound("branchIdOnSubst.equals=" + DEFAULT_BRANCH_ID_ON_SUBST);

        // Get all the branchElValList where branchIdOnSubst equals to UPDATED_BRANCH_ID_ON_SUBST
        defaultBranchElValShouldNotBeFound("branchIdOnSubst.equals=" + UPDATED_BRANCH_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllBranchElValsByBranchIdOnSubstIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where branchIdOnSubst not equals to DEFAULT_BRANCH_ID_ON_SUBST
        defaultBranchElValShouldNotBeFound("branchIdOnSubst.notEquals=" + DEFAULT_BRANCH_ID_ON_SUBST);

        // Get all the branchElValList where branchIdOnSubst not equals to UPDATED_BRANCH_ID_ON_SUBST
        defaultBranchElValShouldBeFound("branchIdOnSubst.notEquals=" + UPDATED_BRANCH_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllBranchElValsByBranchIdOnSubstIsInShouldWork() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where branchIdOnSubst in DEFAULT_BRANCH_ID_ON_SUBST or UPDATED_BRANCH_ID_ON_SUBST
        defaultBranchElValShouldBeFound("branchIdOnSubst.in=" + DEFAULT_BRANCH_ID_ON_SUBST + "," + UPDATED_BRANCH_ID_ON_SUBST);

        // Get all the branchElValList where branchIdOnSubst equals to UPDATED_BRANCH_ID_ON_SUBST
        defaultBranchElValShouldNotBeFound("branchIdOnSubst.in=" + UPDATED_BRANCH_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllBranchElValsByBranchIdOnSubstIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where branchIdOnSubst is not null
        defaultBranchElValShouldBeFound("branchIdOnSubst.specified=true");

        // Get all the branchElValList where branchIdOnSubst is null
        defaultBranchElValShouldNotBeFound("branchIdOnSubst.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchElValsByBranchIdOnSubstIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where branchIdOnSubst is greater than or equal to DEFAULT_BRANCH_ID_ON_SUBST
        defaultBranchElValShouldBeFound("branchIdOnSubst.greaterThanOrEqual=" + DEFAULT_BRANCH_ID_ON_SUBST);

        // Get all the branchElValList where branchIdOnSubst is greater than or equal to UPDATED_BRANCH_ID_ON_SUBST
        defaultBranchElValShouldNotBeFound("branchIdOnSubst.greaterThanOrEqual=" + UPDATED_BRANCH_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllBranchElValsByBranchIdOnSubstIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where branchIdOnSubst is less than or equal to DEFAULT_BRANCH_ID_ON_SUBST
        defaultBranchElValShouldBeFound("branchIdOnSubst.lessThanOrEqual=" + DEFAULT_BRANCH_ID_ON_SUBST);

        // Get all the branchElValList where branchIdOnSubst is less than or equal to SMALLER_BRANCH_ID_ON_SUBST
        defaultBranchElValShouldNotBeFound("branchIdOnSubst.lessThanOrEqual=" + SMALLER_BRANCH_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllBranchElValsByBranchIdOnSubstIsLessThanSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where branchIdOnSubst is less than DEFAULT_BRANCH_ID_ON_SUBST
        defaultBranchElValShouldNotBeFound("branchIdOnSubst.lessThan=" + DEFAULT_BRANCH_ID_ON_SUBST);

        // Get all the branchElValList where branchIdOnSubst is less than UPDATED_BRANCH_ID_ON_SUBST
        defaultBranchElValShouldBeFound("branchIdOnSubst.lessThan=" + UPDATED_BRANCH_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllBranchElValsByBranchIdOnSubstIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where branchIdOnSubst is greater than DEFAULT_BRANCH_ID_ON_SUBST
        defaultBranchElValShouldNotBeFound("branchIdOnSubst.greaterThan=" + DEFAULT_BRANCH_ID_ON_SUBST);

        // Get all the branchElValList where branchIdOnSubst is greater than SMALLER_BRANCH_ID_ON_SUBST
        defaultBranchElValShouldBeFound("branchIdOnSubst.greaterThan=" + SMALLER_BRANCH_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllBranchElValsByNominalVoltageIsEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where nominalVoltage equals to DEFAULT_NOMINAL_VOLTAGE
        defaultBranchElValShouldBeFound("nominalVoltage.equals=" + DEFAULT_NOMINAL_VOLTAGE);

        // Get all the branchElValList where nominalVoltage equals to UPDATED_NOMINAL_VOLTAGE
        defaultBranchElValShouldNotBeFound("nominalVoltage.equals=" + UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllBranchElValsByNominalVoltageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where nominalVoltage not equals to DEFAULT_NOMINAL_VOLTAGE
        defaultBranchElValShouldNotBeFound("nominalVoltage.notEquals=" + DEFAULT_NOMINAL_VOLTAGE);

        // Get all the branchElValList where nominalVoltage not equals to UPDATED_NOMINAL_VOLTAGE
        defaultBranchElValShouldBeFound("nominalVoltage.notEquals=" + UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllBranchElValsByNominalVoltageIsInShouldWork() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where nominalVoltage in DEFAULT_NOMINAL_VOLTAGE or UPDATED_NOMINAL_VOLTAGE
        defaultBranchElValShouldBeFound("nominalVoltage.in=" + DEFAULT_NOMINAL_VOLTAGE + "," + UPDATED_NOMINAL_VOLTAGE);

        // Get all the branchElValList where nominalVoltage equals to UPDATED_NOMINAL_VOLTAGE
        defaultBranchElValShouldNotBeFound("nominalVoltage.in=" + UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllBranchElValsByNominalVoltageIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where nominalVoltage is not null
        defaultBranchElValShouldBeFound("nominalVoltage.specified=true");

        // Get all the branchElValList where nominalVoltage is null
        defaultBranchElValShouldNotBeFound("nominalVoltage.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchElValsByNominalVoltageContainsSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where nominalVoltage contains DEFAULT_NOMINAL_VOLTAGE
        defaultBranchElValShouldBeFound("nominalVoltage.contains=" + DEFAULT_NOMINAL_VOLTAGE);

        // Get all the branchElValList where nominalVoltage contains UPDATED_NOMINAL_VOLTAGE
        defaultBranchElValShouldNotBeFound("nominalVoltage.contains=" + UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllBranchElValsByNominalVoltageNotContainsSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        // Get all the branchElValList where nominalVoltage does not contain DEFAULT_NOMINAL_VOLTAGE
        defaultBranchElValShouldNotBeFound("nominalVoltage.doesNotContain=" + DEFAULT_NOMINAL_VOLTAGE);

        // Get all the branchElValList where nominalVoltage does not contain UPDATED_NOMINAL_VOLTAGE
        defaultBranchElValShouldBeFound("nominalVoltage.doesNotContain=" + UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllBranchElValsByBranchIsEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);
        Branch branch;
        if (TestUtil.findAll(em, Branch.class).isEmpty()) {
            branch = BranchResourceIT.createEntity(em);
            em.persist(branch);
            em.flush();
        } else {
            branch = TestUtil.findAll(em, Branch.class).get(0);
        }
        em.persist(branch);
        em.flush();
        branchElVal.setBranch(branch);
        branchElValRepository.saveAndFlush(branchElVal);
        Long branchId = branch.getId();

        // Get all the branchElValList where branch equals to branchId
        defaultBranchElValShouldBeFound("branchId.equals=" + branchId);

        // Get all the branchElValList where branch equals to (branchId + 1)
        defaultBranchElValShouldNotBeFound("branchId.equals=" + (branchId + 1));
    }

    @Test
    @Transactional
    void getAllBranchElValsByBranchProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);
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
        branchElVal.setBranchProfile(branchProfile);
        branchElValRepository.saveAndFlush(branchElVal);
        Long branchProfileId = branchProfile.getId();

        // Get all the branchElValList where branchProfile equals to branchProfileId
        defaultBranchElValShouldBeFound("branchProfileId.equals=" + branchProfileId);

        // Get all the branchElValList where branchProfile equals to (branchProfileId + 1)
        defaultBranchElValShouldNotBeFound("branchProfileId.equals=" + (branchProfileId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBranchElValShouldBeFound(String filter) throws Exception {
        restBranchElValMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branchElVal.getId().intValue())))
            .andExpect(jsonPath("$.[*].hour").value(hasItem(DEFAULT_HOUR)))
            .andExpect(jsonPath("$.[*].min").value(hasItem(DEFAULT_MIN)))
            .andExpect(jsonPath("$.[*].p").value(hasItem(DEFAULT_P.doubleValue())))
            .andExpect(jsonPath("$.[*].q").value(hasItem(DEFAULT_Q.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].branchIdOnSubst").value(hasItem(DEFAULT_BRANCH_ID_ON_SUBST.intValue())))
            .andExpect(jsonPath("$.[*].nominalVoltage").value(hasItem(DEFAULT_NOMINAL_VOLTAGE)));

        // Check, that the count call also returns 1
        restBranchElValMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBranchElValShouldNotBeFound(String filter) throws Exception {
        restBranchElValMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBranchElValMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBranchElVal() throws Exception {
        // Get the branchElVal
        restBranchElValMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBranchElVal() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        int databaseSizeBeforeUpdate = branchElValRepository.findAll().size();

        // Update the branchElVal
        BranchElVal updatedBranchElVal = branchElValRepository.findById(branchElVal.getId()).get();
        // Disconnect from session so that the updates on updatedBranchElVal are not directly saved in db
        em.detach(updatedBranchElVal);
        updatedBranchElVal
            .hour(UPDATED_HOUR)
            .min(UPDATED_MIN)
            .p(UPDATED_P)
            .q(UPDATED_Q)
            .status(UPDATED_STATUS)
            .branchIdOnSubst(UPDATED_BRANCH_ID_ON_SUBST)
            .nominalVoltage(UPDATED_NOMINAL_VOLTAGE);
        BranchElValDTO branchElValDTO = branchElValMapper.toDto(updatedBranchElVal);

        restBranchElValMockMvc
            .perform(
                put(ENTITY_API_URL_ID, branchElValDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchElValDTO))
            )
            .andExpect(status().isOk());

        // Validate the BranchElVal in the database
        List<BranchElVal> branchElValList = branchElValRepository.findAll();
        assertThat(branchElValList).hasSize(databaseSizeBeforeUpdate);
        BranchElVal testBranchElVal = branchElValList.get(branchElValList.size() - 1);
        assertThat(testBranchElVal.getHour()).isEqualTo(UPDATED_HOUR);
        assertThat(testBranchElVal.getMin()).isEqualTo(UPDATED_MIN);
        assertThat(testBranchElVal.getP()).isEqualTo(UPDATED_P);
        assertThat(testBranchElVal.getQ()).isEqualTo(UPDATED_Q);
        assertThat(testBranchElVal.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBranchElVal.getBranchIdOnSubst()).isEqualTo(UPDATED_BRANCH_ID_ON_SUBST);
        assertThat(testBranchElVal.getNominalVoltage()).isEqualTo(UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void putNonExistingBranchElVal() throws Exception {
        int databaseSizeBeforeUpdate = branchElValRepository.findAll().size();
        branchElVal.setId(count.incrementAndGet());

        // Create the BranchElVal
        BranchElValDTO branchElValDTO = branchElValMapper.toDto(branchElVal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchElValMockMvc
            .perform(
                put(ENTITY_API_URL_ID, branchElValDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchElVal in the database
        List<BranchElVal> branchElValList = branchElValRepository.findAll();
        assertThat(branchElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBranchElVal() throws Exception {
        int databaseSizeBeforeUpdate = branchElValRepository.findAll().size();
        branchElVal.setId(count.incrementAndGet());

        // Create the BranchElVal
        BranchElValDTO branchElValDTO = branchElValMapper.toDto(branchElVal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchElValMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchElVal in the database
        List<BranchElVal> branchElValList = branchElValRepository.findAll();
        assertThat(branchElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBranchElVal() throws Exception {
        int databaseSizeBeforeUpdate = branchElValRepository.findAll().size();
        branchElVal.setId(count.incrementAndGet());

        // Create the BranchElVal
        BranchElValDTO branchElValDTO = branchElValMapper.toDto(branchElVal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchElValMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchElValDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BranchElVal in the database
        List<BranchElVal> branchElValList = branchElValRepository.findAll();
        assertThat(branchElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBranchElValWithPatch() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        int databaseSizeBeforeUpdate = branchElValRepository.findAll().size();

        // Update the branchElVal using partial update
        BranchElVal partialUpdatedBranchElVal = new BranchElVal();
        partialUpdatedBranchElVal.setId(branchElVal.getId());

        partialUpdatedBranchElVal.hour(UPDATED_HOUR).min(UPDATED_MIN).p(UPDATED_P).q(UPDATED_Q).status(UPDATED_STATUS);

        restBranchElValMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBranchElVal.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBranchElVal))
            )
            .andExpect(status().isOk());

        // Validate the BranchElVal in the database
        List<BranchElVal> branchElValList = branchElValRepository.findAll();
        assertThat(branchElValList).hasSize(databaseSizeBeforeUpdate);
        BranchElVal testBranchElVal = branchElValList.get(branchElValList.size() - 1);
        assertThat(testBranchElVal.getHour()).isEqualTo(UPDATED_HOUR);
        assertThat(testBranchElVal.getMin()).isEqualTo(UPDATED_MIN);
        assertThat(testBranchElVal.getP()).isEqualTo(UPDATED_P);
        assertThat(testBranchElVal.getQ()).isEqualTo(UPDATED_Q);
        assertThat(testBranchElVal.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBranchElVal.getBranchIdOnSubst()).isEqualTo(DEFAULT_BRANCH_ID_ON_SUBST);
        assertThat(testBranchElVal.getNominalVoltage()).isEqualTo(DEFAULT_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void fullUpdateBranchElValWithPatch() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        int databaseSizeBeforeUpdate = branchElValRepository.findAll().size();

        // Update the branchElVal using partial update
        BranchElVal partialUpdatedBranchElVal = new BranchElVal();
        partialUpdatedBranchElVal.setId(branchElVal.getId());

        partialUpdatedBranchElVal
            .hour(UPDATED_HOUR)
            .min(UPDATED_MIN)
            .p(UPDATED_P)
            .q(UPDATED_Q)
            .status(UPDATED_STATUS)
            .branchIdOnSubst(UPDATED_BRANCH_ID_ON_SUBST)
            .nominalVoltage(UPDATED_NOMINAL_VOLTAGE);

        restBranchElValMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBranchElVal.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBranchElVal))
            )
            .andExpect(status().isOk());

        // Validate the BranchElVal in the database
        List<BranchElVal> branchElValList = branchElValRepository.findAll();
        assertThat(branchElValList).hasSize(databaseSizeBeforeUpdate);
        BranchElVal testBranchElVal = branchElValList.get(branchElValList.size() - 1);
        assertThat(testBranchElVal.getHour()).isEqualTo(UPDATED_HOUR);
        assertThat(testBranchElVal.getMin()).isEqualTo(UPDATED_MIN);
        assertThat(testBranchElVal.getP()).isEqualTo(UPDATED_P);
        assertThat(testBranchElVal.getQ()).isEqualTo(UPDATED_Q);
        assertThat(testBranchElVal.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBranchElVal.getBranchIdOnSubst()).isEqualTo(UPDATED_BRANCH_ID_ON_SUBST);
        assertThat(testBranchElVal.getNominalVoltage()).isEqualTo(UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void patchNonExistingBranchElVal() throws Exception {
        int databaseSizeBeforeUpdate = branchElValRepository.findAll().size();
        branchElVal.setId(count.incrementAndGet());

        // Create the BranchElVal
        BranchElValDTO branchElValDTO = branchElValMapper.toDto(branchElVal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchElValMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, branchElValDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(branchElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchElVal in the database
        List<BranchElVal> branchElValList = branchElValRepository.findAll();
        assertThat(branchElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBranchElVal() throws Exception {
        int databaseSizeBeforeUpdate = branchElValRepository.findAll().size();
        branchElVal.setId(count.incrementAndGet());

        // Create the BranchElVal
        BranchElValDTO branchElValDTO = branchElValMapper.toDto(branchElVal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchElValMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(branchElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchElVal in the database
        List<BranchElVal> branchElValList = branchElValRepository.findAll();
        assertThat(branchElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBranchElVal() throws Exception {
        int databaseSizeBeforeUpdate = branchElValRepository.findAll().size();
        branchElVal.setId(count.incrementAndGet());

        // Create the BranchElVal
        BranchElValDTO branchElValDTO = branchElValMapper.toDto(branchElVal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchElValMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(branchElValDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BranchElVal in the database
        List<BranchElVal> branchElValList = branchElValRepository.findAll();
        assertThat(branchElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBranchElVal() throws Exception {
        // Initialize the database
        branchElValRepository.saveAndFlush(branchElVal);

        int databaseSizeBeforeDelete = branchElValRepository.findAll().size();

        // Delete the branchElVal
        restBranchElValMockMvc
            .perform(delete(ENTITY_API_URL_ID, branchElVal.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BranchElVal> branchElValList = branchElValRepository.findAll();
        assertThat(branchElValList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
