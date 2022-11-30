package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Branch;
import com.attest.ict.domain.TransfElVal;
import com.attest.ict.domain.TransfProfile;
import com.attest.ict.repository.TransfElValRepository;
import com.attest.ict.service.criteria.TransfElValCriteria;
import com.attest.ict.service.dto.TransfElValDTO;
import com.attest.ict.service.mapper.TransfElValMapper;
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
 * Integration tests for the {@link TransfElValResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransfElValResourceIT {

    private static final Integer DEFAULT_HOUR = 1;
    private static final Integer UPDATED_HOUR = 2;
    private static final Integer SMALLER_HOUR = 1 - 1;

    private static final Integer DEFAULT_MIN = 1;
    private static final Integer UPDATED_MIN = 2;
    private static final Integer SMALLER_MIN = 1 - 1;

    private static final Double DEFAULT_TAP_RATIO = 1D;
    private static final Double UPDATED_TAP_RATIO = 2D;
    private static final Double SMALLER_TAP_RATIO = 1D - 1D;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;
    private static final Integer SMALLER_STATUS = 1 - 1;

    private static final Long DEFAULT_TRASF_ID_ON_SUBST = 1L;
    private static final Long UPDATED_TRASF_ID_ON_SUBST = 2L;
    private static final Long SMALLER_TRASF_ID_ON_SUBST = 1L - 1L;

    private static final String DEFAULT_NOMINAL_VOLTAGE = "AAAAAAAAAA";
    private static final String UPDATED_NOMINAL_VOLTAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/transf-el-vals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransfElValRepository transfElValRepository;

    @Autowired
    private TransfElValMapper transfElValMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransfElValMockMvc;

    private TransfElVal transfElVal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransfElVal createEntity(EntityManager em) {
        TransfElVal transfElVal = new TransfElVal()
            .hour(DEFAULT_HOUR)
            .min(DEFAULT_MIN)
            .tapRatio(DEFAULT_TAP_RATIO)
            .status(DEFAULT_STATUS)
            .trasfIdOnSubst(DEFAULT_TRASF_ID_ON_SUBST)
            .nominalVoltage(DEFAULT_NOMINAL_VOLTAGE);
        return transfElVal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransfElVal createUpdatedEntity(EntityManager em) {
        TransfElVal transfElVal = new TransfElVal()
            .hour(UPDATED_HOUR)
            .min(UPDATED_MIN)
            .tapRatio(UPDATED_TAP_RATIO)
            .status(UPDATED_STATUS)
            .trasfIdOnSubst(UPDATED_TRASF_ID_ON_SUBST)
            .nominalVoltage(UPDATED_NOMINAL_VOLTAGE);
        return transfElVal;
    }

    @BeforeEach
    public void initTest() {
        transfElVal = createEntity(em);
    }

    @Test
    @Transactional
    void createTransfElVal() throws Exception {
        int databaseSizeBeforeCreate = transfElValRepository.findAll().size();
        // Create the TransfElVal
        TransfElValDTO transfElValDTO = transfElValMapper.toDto(transfElVal);
        restTransfElValMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transfElValDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TransfElVal in the database
        List<TransfElVal> transfElValList = transfElValRepository.findAll();
        assertThat(transfElValList).hasSize(databaseSizeBeforeCreate + 1);
        TransfElVal testTransfElVal = transfElValList.get(transfElValList.size() - 1);
        assertThat(testTransfElVal.getHour()).isEqualTo(DEFAULT_HOUR);
        assertThat(testTransfElVal.getMin()).isEqualTo(DEFAULT_MIN);
        assertThat(testTransfElVal.getTapRatio()).isEqualTo(DEFAULT_TAP_RATIO);
        assertThat(testTransfElVal.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTransfElVal.getTrasfIdOnSubst()).isEqualTo(DEFAULT_TRASF_ID_ON_SUBST);
        assertThat(testTransfElVal.getNominalVoltage()).isEqualTo(DEFAULT_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void createTransfElValWithExistingId() throws Exception {
        // Create the TransfElVal with an existing ID
        transfElVal.setId(1L);
        TransfElValDTO transfElValDTO = transfElValMapper.toDto(transfElVal);

        int databaseSizeBeforeCreate = transfElValRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransfElValMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transfElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransfElVal in the database
        List<TransfElVal> transfElValList = transfElValRepository.findAll();
        assertThat(transfElValList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTransfElVals() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList
        restTransfElValMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transfElVal.getId().intValue())))
            .andExpect(jsonPath("$.[*].hour").value(hasItem(DEFAULT_HOUR)))
            .andExpect(jsonPath("$.[*].min").value(hasItem(DEFAULT_MIN)))
            .andExpect(jsonPath("$.[*].tapRatio").value(hasItem(DEFAULT_TAP_RATIO.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].trasfIdOnSubst").value(hasItem(DEFAULT_TRASF_ID_ON_SUBST.intValue())))
            .andExpect(jsonPath("$.[*].nominalVoltage").value(hasItem(DEFAULT_NOMINAL_VOLTAGE)));
    }

    @Test
    @Transactional
    void getTransfElVal() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get the transfElVal
        restTransfElValMockMvc
            .perform(get(ENTITY_API_URL_ID, transfElVal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transfElVal.getId().intValue()))
            .andExpect(jsonPath("$.hour").value(DEFAULT_HOUR))
            .andExpect(jsonPath("$.min").value(DEFAULT_MIN))
            .andExpect(jsonPath("$.tapRatio").value(DEFAULT_TAP_RATIO.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.trasfIdOnSubst").value(DEFAULT_TRASF_ID_ON_SUBST.intValue()))
            .andExpect(jsonPath("$.nominalVoltage").value(DEFAULT_NOMINAL_VOLTAGE));
    }

    @Test
    @Transactional
    void getTransfElValsByIdFiltering() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        Long id = transfElVal.getId();

        defaultTransfElValShouldBeFound("id.equals=" + id);
        defaultTransfElValShouldNotBeFound("id.notEquals=" + id);

        defaultTransfElValShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTransfElValShouldNotBeFound("id.greaterThan=" + id);

        defaultTransfElValShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTransfElValShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransfElValsByHourIsEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where hour equals to DEFAULT_HOUR
        defaultTransfElValShouldBeFound("hour.equals=" + DEFAULT_HOUR);

        // Get all the transfElValList where hour equals to UPDATED_HOUR
        defaultTransfElValShouldNotBeFound("hour.equals=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllTransfElValsByHourIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where hour not equals to DEFAULT_HOUR
        defaultTransfElValShouldNotBeFound("hour.notEquals=" + DEFAULT_HOUR);

        // Get all the transfElValList where hour not equals to UPDATED_HOUR
        defaultTransfElValShouldBeFound("hour.notEquals=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllTransfElValsByHourIsInShouldWork() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where hour in DEFAULT_HOUR or UPDATED_HOUR
        defaultTransfElValShouldBeFound("hour.in=" + DEFAULT_HOUR + "," + UPDATED_HOUR);

        // Get all the transfElValList where hour equals to UPDATED_HOUR
        defaultTransfElValShouldNotBeFound("hour.in=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllTransfElValsByHourIsNullOrNotNull() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where hour is not null
        defaultTransfElValShouldBeFound("hour.specified=true");

        // Get all the transfElValList where hour is null
        defaultTransfElValShouldNotBeFound("hour.specified=false");
    }

    @Test
    @Transactional
    void getAllTransfElValsByHourIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where hour is greater than or equal to DEFAULT_HOUR
        defaultTransfElValShouldBeFound("hour.greaterThanOrEqual=" + DEFAULT_HOUR);

        // Get all the transfElValList where hour is greater than or equal to UPDATED_HOUR
        defaultTransfElValShouldNotBeFound("hour.greaterThanOrEqual=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllTransfElValsByHourIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where hour is less than or equal to DEFAULT_HOUR
        defaultTransfElValShouldBeFound("hour.lessThanOrEqual=" + DEFAULT_HOUR);

        // Get all the transfElValList where hour is less than or equal to SMALLER_HOUR
        defaultTransfElValShouldNotBeFound("hour.lessThanOrEqual=" + SMALLER_HOUR);
    }

    @Test
    @Transactional
    void getAllTransfElValsByHourIsLessThanSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where hour is less than DEFAULT_HOUR
        defaultTransfElValShouldNotBeFound("hour.lessThan=" + DEFAULT_HOUR);

        // Get all the transfElValList where hour is less than UPDATED_HOUR
        defaultTransfElValShouldBeFound("hour.lessThan=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllTransfElValsByHourIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where hour is greater than DEFAULT_HOUR
        defaultTransfElValShouldNotBeFound("hour.greaterThan=" + DEFAULT_HOUR);

        // Get all the transfElValList where hour is greater than SMALLER_HOUR
        defaultTransfElValShouldBeFound("hour.greaterThan=" + SMALLER_HOUR);
    }

    @Test
    @Transactional
    void getAllTransfElValsByMinIsEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where min equals to DEFAULT_MIN
        defaultTransfElValShouldBeFound("min.equals=" + DEFAULT_MIN);

        // Get all the transfElValList where min equals to UPDATED_MIN
        defaultTransfElValShouldNotBeFound("min.equals=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllTransfElValsByMinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where min not equals to DEFAULT_MIN
        defaultTransfElValShouldNotBeFound("min.notEquals=" + DEFAULT_MIN);

        // Get all the transfElValList where min not equals to UPDATED_MIN
        defaultTransfElValShouldBeFound("min.notEquals=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllTransfElValsByMinIsInShouldWork() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where min in DEFAULT_MIN or UPDATED_MIN
        defaultTransfElValShouldBeFound("min.in=" + DEFAULT_MIN + "," + UPDATED_MIN);

        // Get all the transfElValList where min equals to UPDATED_MIN
        defaultTransfElValShouldNotBeFound("min.in=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllTransfElValsByMinIsNullOrNotNull() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where min is not null
        defaultTransfElValShouldBeFound("min.specified=true");

        // Get all the transfElValList where min is null
        defaultTransfElValShouldNotBeFound("min.specified=false");
    }

    @Test
    @Transactional
    void getAllTransfElValsByMinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where min is greater than or equal to DEFAULT_MIN
        defaultTransfElValShouldBeFound("min.greaterThanOrEqual=" + DEFAULT_MIN);

        // Get all the transfElValList where min is greater than or equal to UPDATED_MIN
        defaultTransfElValShouldNotBeFound("min.greaterThanOrEqual=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllTransfElValsByMinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where min is less than or equal to DEFAULT_MIN
        defaultTransfElValShouldBeFound("min.lessThanOrEqual=" + DEFAULT_MIN);

        // Get all the transfElValList where min is less than or equal to SMALLER_MIN
        defaultTransfElValShouldNotBeFound("min.lessThanOrEqual=" + SMALLER_MIN);
    }

    @Test
    @Transactional
    void getAllTransfElValsByMinIsLessThanSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where min is less than DEFAULT_MIN
        defaultTransfElValShouldNotBeFound("min.lessThan=" + DEFAULT_MIN);

        // Get all the transfElValList where min is less than UPDATED_MIN
        defaultTransfElValShouldBeFound("min.lessThan=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllTransfElValsByMinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where min is greater than DEFAULT_MIN
        defaultTransfElValShouldNotBeFound("min.greaterThan=" + DEFAULT_MIN);

        // Get all the transfElValList where min is greater than SMALLER_MIN
        defaultTransfElValShouldBeFound("min.greaterThan=" + SMALLER_MIN);
    }

    @Test
    @Transactional
    void getAllTransfElValsByTapRatioIsEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where tapRatio equals to DEFAULT_TAP_RATIO
        defaultTransfElValShouldBeFound("tapRatio.equals=" + DEFAULT_TAP_RATIO);

        // Get all the transfElValList where tapRatio equals to UPDATED_TAP_RATIO
        defaultTransfElValShouldNotBeFound("tapRatio.equals=" + UPDATED_TAP_RATIO);
    }

    @Test
    @Transactional
    void getAllTransfElValsByTapRatioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where tapRatio not equals to DEFAULT_TAP_RATIO
        defaultTransfElValShouldNotBeFound("tapRatio.notEquals=" + DEFAULT_TAP_RATIO);

        // Get all the transfElValList where tapRatio not equals to UPDATED_TAP_RATIO
        defaultTransfElValShouldBeFound("tapRatio.notEquals=" + UPDATED_TAP_RATIO);
    }

    @Test
    @Transactional
    void getAllTransfElValsByTapRatioIsInShouldWork() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where tapRatio in DEFAULT_TAP_RATIO or UPDATED_TAP_RATIO
        defaultTransfElValShouldBeFound("tapRatio.in=" + DEFAULT_TAP_RATIO + "," + UPDATED_TAP_RATIO);

        // Get all the transfElValList where tapRatio equals to UPDATED_TAP_RATIO
        defaultTransfElValShouldNotBeFound("tapRatio.in=" + UPDATED_TAP_RATIO);
    }

    @Test
    @Transactional
    void getAllTransfElValsByTapRatioIsNullOrNotNull() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where tapRatio is not null
        defaultTransfElValShouldBeFound("tapRatio.specified=true");

        // Get all the transfElValList where tapRatio is null
        defaultTransfElValShouldNotBeFound("tapRatio.specified=false");
    }

    @Test
    @Transactional
    void getAllTransfElValsByTapRatioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where tapRatio is greater than or equal to DEFAULT_TAP_RATIO
        defaultTransfElValShouldBeFound("tapRatio.greaterThanOrEqual=" + DEFAULT_TAP_RATIO);

        // Get all the transfElValList where tapRatio is greater than or equal to UPDATED_TAP_RATIO
        defaultTransfElValShouldNotBeFound("tapRatio.greaterThanOrEqual=" + UPDATED_TAP_RATIO);
    }

    @Test
    @Transactional
    void getAllTransfElValsByTapRatioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where tapRatio is less than or equal to DEFAULT_TAP_RATIO
        defaultTransfElValShouldBeFound("tapRatio.lessThanOrEqual=" + DEFAULT_TAP_RATIO);

        // Get all the transfElValList where tapRatio is less than or equal to SMALLER_TAP_RATIO
        defaultTransfElValShouldNotBeFound("tapRatio.lessThanOrEqual=" + SMALLER_TAP_RATIO);
    }

    @Test
    @Transactional
    void getAllTransfElValsByTapRatioIsLessThanSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where tapRatio is less than DEFAULT_TAP_RATIO
        defaultTransfElValShouldNotBeFound("tapRatio.lessThan=" + DEFAULT_TAP_RATIO);

        // Get all the transfElValList where tapRatio is less than UPDATED_TAP_RATIO
        defaultTransfElValShouldBeFound("tapRatio.lessThan=" + UPDATED_TAP_RATIO);
    }

    @Test
    @Transactional
    void getAllTransfElValsByTapRatioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where tapRatio is greater than DEFAULT_TAP_RATIO
        defaultTransfElValShouldNotBeFound("tapRatio.greaterThan=" + DEFAULT_TAP_RATIO);

        // Get all the transfElValList where tapRatio is greater than SMALLER_TAP_RATIO
        defaultTransfElValShouldBeFound("tapRatio.greaterThan=" + SMALLER_TAP_RATIO);
    }

    @Test
    @Transactional
    void getAllTransfElValsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where status equals to DEFAULT_STATUS
        defaultTransfElValShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the transfElValList where status equals to UPDATED_STATUS
        defaultTransfElValShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTransfElValsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where status not equals to DEFAULT_STATUS
        defaultTransfElValShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the transfElValList where status not equals to UPDATED_STATUS
        defaultTransfElValShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTransfElValsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultTransfElValShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the transfElValList where status equals to UPDATED_STATUS
        defaultTransfElValShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTransfElValsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where status is not null
        defaultTransfElValShouldBeFound("status.specified=true");

        // Get all the transfElValList where status is null
        defaultTransfElValShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllTransfElValsByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where status is greater than or equal to DEFAULT_STATUS
        defaultTransfElValShouldBeFound("status.greaterThanOrEqual=" + DEFAULT_STATUS);

        // Get all the transfElValList where status is greater than or equal to UPDATED_STATUS
        defaultTransfElValShouldNotBeFound("status.greaterThanOrEqual=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTransfElValsByStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where status is less than or equal to DEFAULT_STATUS
        defaultTransfElValShouldBeFound("status.lessThanOrEqual=" + DEFAULT_STATUS);

        // Get all the transfElValList where status is less than or equal to SMALLER_STATUS
        defaultTransfElValShouldNotBeFound("status.lessThanOrEqual=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllTransfElValsByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where status is less than DEFAULT_STATUS
        defaultTransfElValShouldNotBeFound("status.lessThan=" + DEFAULT_STATUS);

        // Get all the transfElValList where status is less than UPDATED_STATUS
        defaultTransfElValShouldBeFound("status.lessThan=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTransfElValsByStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where status is greater than DEFAULT_STATUS
        defaultTransfElValShouldNotBeFound("status.greaterThan=" + DEFAULT_STATUS);

        // Get all the transfElValList where status is greater than SMALLER_STATUS
        defaultTransfElValShouldBeFound("status.greaterThan=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllTransfElValsByTrasfIdOnSubstIsEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where trasfIdOnSubst equals to DEFAULT_TRASF_ID_ON_SUBST
        defaultTransfElValShouldBeFound("trasfIdOnSubst.equals=" + DEFAULT_TRASF_ID_ON_SUBST);

        // Get all the transfElValList where trasfIdOnSubst equals to UPDATED_TRASF_ID_ON_SUBST
        defaultTransfElValShouldNotBeFound("trasfIdOnSubst.equals=" + UPDATED_TRASF_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllTransfElValsByTrasfIdOnSubstIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where trasfIdOnSubst not equals to DEFAULT_TRASF_ID_ON_SUBST
        defaultTransfElValShouldNotBeFound("trasfIdOnSubst.notEquals=" + DEFAULT_TRASF_ID_ON_SUBST);

        // Get all the transfElValList where trasfIdOnSubst not equals to UPDATED_TRASF_ID_ON_SUBST
        defaultTransfElValShouldBeFound("trasfIdOnSubst.notEquals=" + UPDATED_TRASF_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllTransfElValsByTrasfIdOnSubstIsInShouldWork() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where trasfIdOnSubst in DEFAULT_TRASF_ID_ON_SUBST or UPDATED_TRASF_ID_ON_SUBST
        defaultTransfElValShouldBeFound("trasfIdOnSubst.in=" + DEFAULT_TRASF_ID_ON_SUBST + "," + UPDATED_TRASF_ID_ON_SUBST);

        // Get all the transfElValList where trasfIdOnSubst equals to UPDATED_TRASF_ID_ON_SUBST
        defaultTransfElValShouldNotBeFound("trasfIdOnSubst.in=" + UPDATED_TRASF_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllTransfElValsByTrasfIdOnSubstIsNullOrNotNull() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where trasfIdOnSubst is not null
        defaultTransfElValShouldBeFound("trasfIdOnSubst.specified=true");

        // Get all the transfElValList where trasfIdOnSubst is null
        defaultTransfElValShouldNotBeFound("trasfIdOnSubst.specified=false");
    }

    @Test
    @Transactional
    void getAllTransfElValsByTrasfIdOnSubstIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where trasfIdOnSubst is greater than or equal to DEFAULT_TRASF_ID_ON_SUBST
        defaultTransfElValShouldBeFound("trasfIdOnSubst.greaterThanOrEqual=" + DEFAULT_TRASF_ID_ON_SUBST);

        // Get all the transfElValList where trasfIdOnSubst is greater than or equal to UPDATED_TRASF_ID_ON_SUBST
        defaultTransfElValShouldNotBeFound("trasfIdOnSubst.greaterThanOrEqual=" + UPDATED_TRASF_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllTransfElValsByTrasfIdOnSubstIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where trasfIdOnSubst is less than or equal to DEFAULT_TRASF_ID_ON_SUBST
        defaultTransfElValShouldBeFound("trasfIdOnSubst.lessThanOrEqual=" + DEFAULT_TRASF_ID_ON_SUBST);

        // Get all the transfElValList where trasfIdOnSubst is less than or equal to SMALLER_TRASF_ID_ON_SUBST
        defaultTransfElValShouldNotBeFound("trasfIdOnSubst.lessThanOrEqual=" + SMALLER_TRASF_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllTransfElValsByTrasfIdOnSubstIsLessThanSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where trasfIdOnSubst is less than DEFAULT_TRASF_ID_ON_SUBST
        defaultTransfElValShouldNotBeFound("trasfIdOnSubst.lessThan=" + DEFAULT_TRASF_ID_ON_SUBST);

        // Get all the transfElValList where trasfIdOnSubst is less than UPDATED_TRASF_ID_ON_SUBST
        defaultTransfElValShouldBeFound("trasfIdOnSubst.lessThan=" + UPDATED_TRASF_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllTransfElValsByTrasfIdOnSubstIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where trasfIdOnSubst is greater than DEFAULT_TRASF_ID_ON_SUBST
        defaultTransfElValShouldNotBeFound("trasfIdOnSubst.greaterThan=" + DEFAULT_TRASF_ID_ON_SUBST);

        // Get all the transfElValList where trasfIdOnSubst is greater than SMALLER_TRASF_ID_ON_SUBST
        defaultTransfElValShouldBeFound("trasfIdOnSubst.greaterThan=" + SMALLER_TRASF_ID_ON_SUBST);
    }

    @Test
    @Transactional
    void getAllTransfElValsByNominalVoltageIsEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where nominalVoltage equals to DEFAULT_NOMINAL_VOLTAGE
        defaultTransfElValShouldBeFound("nominalVoltage.equals=" + DEFAULT_NOMINAL_VOLTAGE);

        // Get all the transfElValList where nominalVoltage equals to UPDATED_NOMINAL_VOLTAGE
        defaultTransfElValShouldNotBeFound("nominalVoltage.equals=" + UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllTransfElValsByNominalVoltageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where nominalVoltage not equals to DEFAULT_NOMINAL_VOLTAGE
        defaultTransfElValShouldNotBeFound("nominalVoltage.notEquals=" + DEFAULT_NOMINAL_VOLTAGE);

        // Get all the transfElValList where nominalVoltage not equals to UPDATED_NOMINAL_VOLTAGE
        defaultTransfElValShouldBeFound("nominalVoltage.notEquals=" + UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllTransfElValsByNominalVoltageIsInShouldWork() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where nominalVoltage in DEFAULT_NOMINAL_VOLTAGE or UPDATED_NOMINAL_VOLTAGE
        defaultTransfElValShouldBeFound("nominalVoltage.in=" + DEFAULT_NOMINAL_VOLTAGE + "," + UPDATED_NOMINAL_VOLTAGE);

        // Get all the transfElValList where nominalVoltage equals to UPDATED_NOMINAL_VOLTAGE
        defaultTransfElValShouldNotBeFound("nominalVoltage.in=" + UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllTransfElValsByNominalVoltageIsNullOrNotNull() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where nominalVoltage is not null
        defaultTransfElValShouldBeFound("nominalVoltage.specified=true");

        // Get all the transfElValList where nominalVoltage is null
        defaultTransfElValShouldNotBeFound("nominalVoltage.specified=false");
    }

    @Test
    @Transactional
    void getAllTransfElValsByNominalVoltageContainsSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where nominalVoltage contains DEFAULT_NOMINAL_VOLTAGE
        defaultTransfElValShouldBeFound("nominalVoltage.contains=" + DEFAULT_NOMINAL_VOLTAGE);

        // Get all the transfElValList where nominalVoltage contains UPDATED_NOMINAL_VOLTAGE
        defaultTransfElValShouldNotBeFound("nominalVoltage.contains=" + UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllTransfElValsByNominalVoltageNotContainsSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        // Get all the transfElValList where nominalVoltage does not contain DEFAULT_NOMINAL_VOLTAGE
        defaultTransfElValShouldNotBeFound("nominalVoltage.doesNotContain=" + DEFAULT_NOMINAL_VOLTAGE);

        // Get all the transfElValList where nominalVoltage does not contain UPDATED_NOMINAL_VOLTAGE
        defaultTransfElValShouldBeFound("nominalVoltage.doesNotContain=" + UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllTransfElValsByTransfProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);
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
        transfElVal.setTransfProfile(transfProfile);
        transfElValRepository.saveAndFlush(transfElVal);
        Long transfProfileId = transfProfile.getId();

        // Get all the transfElValList where transfProfile equals to transfProfileId
        defaultTransfElValShouldBeFound("transfProfileId.equals=" + transfProfileId);

        // Get all the transfElValList where transfProfile equals to (transfProfileId + 1)
        defaultTransfElValShouldNotBeFound("transfProfileId.equals=" + (transfProfileId + 1));
    }

    @Test
    @Transactional
    void getAllTransfElValsByBranchIsEqualToSomething() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);
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
        transfElVal.setBranch(branch);
        transfElValRepository.saveAndFlush(transfElVal);
        Long branchId = branch.getId();

        // Get all the transfElValList where branch equals to branchId
        defaultTransfElValShouldBeFound("branchId.equals=" + branchId);

        // Get all the transfElValList where branch equals to (branchId + 1)
        defaultTransfElValShouldNotBeFound("branchId.equals=" + (branchId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransfElValShouldBeFound(String filter) throws Exception {
        restTransfElValMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transfElVal.getId().intValue())))
            .andExpect(jsonPath("$.[*].hour").value(hasItem(DEFAULT_HOUR)))
            .andExpect(jsonPath("$.[*].min").value(hasItem(DEFAULT_MIN)))
            .andExpect(jsonPath("$.[*].tapRatio").value(hasItem(DEFAULT_TAP_RATIO.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].trasfIdOnSubst").value(hasItem(DEFAULT_TRASF_ID_ON_SUBST.intValue())))
            .andExpect(jsonPath("$.[*].nominalVoltage").value(hasItem(DEFAULT_NOMINAL_VOLTAGE)));

        // Check, that the count call also returns 1
        restTransfElValMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransfElValShouldNotBeFound(String filter) throws Exception {
        restTransfElValMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransfElValMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransfElVal() throws Exception {
        // Get the transfElVal
        restTransfElValMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTransfElVal() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        int databaseSizeBeforeUpdate = transfElValRepository.findAll().size();

        // Update the transfElVal
        TransfElVal updatedTransfElVal = transfElValRepository.findById(transfElVal.getId()).get();
        // Disconnect from session so that the updates on updatedTransfElVal are not directly saved in db
        em.detach(updatedTransfElVal);
        updatedTransfElVal
            .hour(UPDATED_HOUR)
            .min(UPDATED_MIN)
            .tapRatio(UPDATED_TAP_RATIO)
            .status(UPDATED_STATUS)
            .trasfIdOnSubst(UPDATED_TRASF_ID_ON_SUBST)
            .nominalVoltage(UPDATED_NOMINAL_VOLTAGE);
        TransfElValDTO transfElValDTO = transfElValMapper.toDto(updatedTransfElVal);

        restTransfElValMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transfElValDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transfElValDTO))
            )
            .andExpect(status().isOk());

        // Validate the TransfElVal in the database
        List<TransfElVal> transfElValList = transfElValRepository.findAll();
        assertThat(transfElValList).hasSize(databaseSizeBeforeUpdate);
        TransfElVal testTransfElVal = transfElValList.get(transfElValList.size() - 1);
        assertThat(testTransfElVal.getHour()).isEqualTo(UPDATED_HOUR);
        assertThat(testTransfElVal.getMin()).isEqualTo(UPDATED_MIN);
        assertThat(testTransfElVal.getTapRatio()).isEqualTo(UPDATED_TAP_RATIO);
        assertThat(testTransfElVal.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTransfElVal.getTrasfIdOnSubst()).isEqualTo(UPDATED_TRASF_ID_ON_SUBST);
        assertThat(testTransfElVal.getNominalVoltage()).isEqualTo(UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void putNonExistingTransfElVal() throws Exception {
        int databaseSizeBeforeUpdate = transfElValRepository.findAll().size();
        transfElVal.setId(count.incrementAndGet());

        // Create the TransfElVal
        TransfElValDTO transfElValDTO = transfElValMapper.toDto(transfElVal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransfElValMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transfElValDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transfElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransfElVal in the database
        List<TransfElVal> transfElValList = transfElValRepository.findAll();
        assertThat(transfElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransfElVal() throws Exception {
        int databaseSizeBeforeUpdate = transfElValRepository.findAll().size();
        transfElVal.setId(count.incrementAndGet());

        // Create the TransfElVal
        TransfElValDTO transfElValDTO = transfElValMapper.toDto(transfElVal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransfElValMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transfElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransfElVal in the database
        List<TransfElVal> transfElValList = transfElValRepository.findAll();
        assertThat(transfElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransfElVal() throws Exception {
        int databaseSizeBeforeUpdate = transfElValRepository.findAll().size();
        transfElVal.setId(count.incrementAndGet());

        // Create the TransfElVal
        TransfElValDTO transfElValDTO = transfElValMapper.toDto(transfElVal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransfElValMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transfElValDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransfElVal in the database
        List<TransfElVal> transfElValList = transfElValRepository.findAll();
        assertThat(transfElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransfElValWithPatch() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        int databaseSizeBeforeUpdate = transfElValRepository.findAll().size();

        // Update the transfElVal using partial update
        TransfElVal partialUpdatedTransfElVal = new TransfElVal();
        partialUpdatedTransfElVal.setId(transfElVal.getId());

        partialUpdatedTransfElVal.tapRatio(UPDATED_TAP_RATIO).status(UPDATED_STATUS).nominalVoltage(UPDATED_NOMINAL_VOLTAGE);

        restTransfElValMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransfElVal.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransfElVal))
            )
            .andExpect(status().isOk());

        // Validate the TransfElVal in the database
        List<TransfElVal> transfElValList = transfElValRepository.findAll();
        assertThat(transfElValList).hasSize(databaseSizeBeforeUpdate);
        TransfElVal testTransfElVal = transfElValList.get(transfElValList.size() - 1);
        assertThat(testTransfElVal.getHour()).isEqualTo(DEFAULT_HOUR);
        assertThat(testTransfElVal.getMin()).isEqualTo(DEFAULT_MIN);
        assertThat(testTransfElVal.getTapRatio()).isEqualTo(UPDATED_TAP_RATIO);
        assertThat(testTransfElVal.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTransfElVal.getTrasfIdOnSubst()).isEqualTo(DEFAULT_TRASF_ID_ON_SUBST);
        assertThat(testTransfElVal.getNominalVoltage()).isEqualTo(UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void fullUpdateTransfElValWithPatch() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        int databaseSizeBeforeUpdate = transfElValRepository.findAll().size();

        // Update the transfElVal using partial update
        TransfElVal partialUpdatedTransfElVal = new TransfElVal();
        partialUpdatedTransfElVal.setId(transfElVal.getId());

        partialUpdatedTransfElVal
            .hour(UPDATED_HOUR)
            .min(UPDATED_MIN)
            .tapRatio(UPDATED_TAP_RATIO)
            .status(UPDATED_STATUS)
            .trasfIdOnSubst(UPDATED_TRASF_ID_ON_SUBST)
            .nominalVoltage(UPDATED_NOMINAL_VOLTAGE);

        restTransfElValMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransfElVal.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransfElVal))
            )
            .andExpect(status().isOk());

        // Validate the TransfElVal in the database
        List<TransfElVal> transfElValList = transfElValRepository.findAll();
        assertThat(transfElValList).hasSize(databaseSizeBeforeUpdate);
        TransfElVal testTransfElVal = transfElValList.get(transfElValList.size() - 1);
        assertThat(testTransfElVal.getHour()).isEqualTo(UPDATED_HOUR);
        assertThat(testTransfElVal.getMin()).isEqualTo(UPDATED_MIN);
        assertThat(testTransfElVal.getTapRatio()).isEqualTo(UPDATED_TAP_RATIO);
        assertThat(testTransfElVal.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTransfElVal.getTrasfIdOnSubst()).isEqualTo(UPDATED_TRASF_ID_ON_SUBST);
        assertThat(testTransfElVal.getNominalVoltage()).isEqualTo(UPDATED_NOMINAL_VOLTAGE);
    }

    @Test
    @Transactional
    void patchNonExistingTransfElVal() throws Exception {
        int databaseSizeBeforeUpdate = transfElValRepository.findAll().size();
        transfElVal.setId(count.incrementAndGet());

        // Create the TransfElVal
        TransfElValDTO transfElValDTO = transfElValMapper.toDto(transfElVal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransfElValMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transfElValDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transfElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransfElVal in the database
        List<TransfElVal> transfElValList = transfElValRepository.findAll();
        assertThat(transfElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransfElVal() throws Exception {
        int databaseSizeBeforeUpdate = transfElValRepository.findAll().size();
        transfElVal.setId(count.incrementAndGet());

        // Create the TransfElVal
        TransfElValDTO transfElValDTO = transfElValMapper.toDto(transfElVal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransfElValMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transfElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransfElVal in the database
        List<TransfElVal> transfElValList = transfElValRepository.findAll();
        assertThat(transfElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransfElVal() throws Exception {
        int databaseSizeBeforeUpdate = transfElValRepository.findAll().size();
        transfElVal.setId(count.incrementAndGet());

        // Create the TransfElVal
        TransfElValDTO transfElValDTO = transfElValMapper.toDto(transfElVal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransfElValMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transfElValDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransfElVal in the database
        List<TransfElVal> transfElValList = transfElValRepository.findAll();
        assertThat(transfElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransfElVal() throws Exception {
        // Initialize the database
        transfElValRepository.saveAndFlush(transfElVal);

        int databaseSizeBeforeDelete = transfElValRepository.findAll().size();

        // Delete the transfElVal
        restTransfElValMockMvc
            .perform(delete(ENTITY_API_URL_ID, transfElVal.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TransfElVal> transfElValList = transfElValRepository.findAll();
        assertThat(transfElValList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
