package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.FlexElVal;
import com.attest.ict.domain.FlexProfile;
import com.attest.ict.repository.FlexElValRepository;
import com.attest.ict.service.criteria.FlexElValCriteria;
import com.attest.ict.service.dto.FlexElValDTO;
import com.attest.ict.service.mapper.FlexElValMapper;
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
 * Integration tests for the {@link FlexElValResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FlexElValResourceIT {

    private static final Long DEFAULT_BUS_NUM = 1L;
    private static final Long UPDATED_BUS_NUM = 2L;
    private static final Long SMALLER_BUS_NUM = 1L - 1L;

    private static final Integer DEFAULT_HOUR = 1;
    private static final Integer UPDATED_HOUR = 2;
    private static final Integer SMALLER_HOUR = 1 - 1;

    private static final Integer DEFAULT_MIN = 1;
    private static final Integer UPDATED_MIN = 2;
    private static final Integer SMALLER_MIN = 1 - 1;

    private static final Double DEFAULT_PFMAX_UP = 1D;
    private static final Double UPDATED_PFMAX_UP = 2D;
    private static final Double SMALLER_PFMAX_UP = 1D - 1D;

    private static final Double DEFAULT_PFMAX_DN = 1D;
    private static final Double UPDATED_PFMAX_DN = 2D;
    private static final Double SMALLER_PFMAX_DN = 1D - 1D;

    private static final Double DEFAULT_QFMAX_UP = 1D;
    private static final Double UPDATED_QFMAX_UP = 2D;
    private static final Double SMALLER_QFMAX_UP = 1D - 1D;

    private static final Double DEFAULT_QFMAX_DN = 1D;
    private static final Double UPDATED_QFMAX_DN = 2D;
    private static final Double SMALLER_QFMAX_DN = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/flex-el-vals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FlexElValRepository flexElValRepository;

    @Autowired
    private FlexElValMapper flexElValMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFlexElValMockMvc;

    private FlexElVal flexElVal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlexElVal createEntity(EntityManager em) {
        FlexElVal flexElVal = new FlexElVal()
            .busNum(DEFAULT_BUS_NUM)
            .hour(DEFAULT_HOUR)
            .min(DEFAULT_MIN)
            .pfmaxUp(DEFAULT_PFMAX_UP)
            .pfmaxDn(DEFAULT_PFMAX_DN)
            .qfmaxUp(DEFAULT_QFMAX_UP)
            .qfmaxDn(DEFAULT_QFMAX_DN);
        return flexElVal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlexElVal createUpdatedEntity(EntityManager em) {
        FlexElVal flexElVal = new FlexElVal()
            .busNum(UPDATED_BUS_NUM)
            .hour(UPDATED_HOUR)
            .min(UPDATED_MIN)
            .pfmaxUp(UPDATED_PFMAX_UP)
            .pfmaxDn(UPDATED_PFMAX_DN)
            .qfmaxUp(UPDATED_QFMAX_UP)
            .qfmaxDn(UPDATED_QFMAX_DN);
        return flexElVal;
    }

    @BeforeEach
    public void initTest() {
        flexElVal = createEntity(em);
    }

    @Test
    @Transactional
    void createFlexElVal() throws Exception {
        int databaseSizeBeforeCreate = flexElValRepository.findAll().size();
        // Create the FlexElVal
        FlexElValDTO flexElValDTO = flexElValMapper.toDto(flexElVal);
        restFlexElValMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexElValDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FlexElVal in the database
        List<FlexElVal> flexElValList = flexElValRepository.findAll();
        assertThat(flexElValList).hasSize(databaseSizeBeforeCreate + 1);
        FlexElVal testFlexElVal = flexElValList.get(flexElValList.size() - 1);
        assertThat(testFlexElVal.getBusNum()).isEqualTo(DEFAULT_BUS_NUM);
        assertThat(testFlexElVal.getHour()).isEqualTo(DEFAULT_HOUR);
        assertThat(testFlexElVal.getMin()).isEqualTo(DEFAULT_MIN);
        assertThat(testFlexElVal.getPfmaxUp()).isEqualTo(DEFAULT_PFMAX_UP);
        assertThat(testFlexElVal.getPfmaxDn()).isEqualTo(DEFAULT_PFMAX_DN);
        assertThat(testFlexElVal.getQfmaxUp()).isEqualTo(DEFAULT_QFMAX_UP);
        assertThat(testFlexElVal.getQfmaxDn()).isEqualTo(DEFAULT_QFMAX_DN);
    }

    @Test
    @Transactional
    void createFlexElValWithExistingId() throws Exception {
        // Create the FlexElVal with an existing ID
        flexElVal.setId(1L);
        FlexElValDTO flexElValDTO = flexElValMapper.toDto(flexElVal);

        int databaseSizeBeforeCreate = flexElValRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlexElValMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexElVal in the database
        List<FlexElVal> flexElValList = flexElValRepository.findAll();
        assertThat(flexElValList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFlexElVals() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList
        restFlexElValMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flexElVal.getId().intValue())))
            .andExpect(jsonPath("$.[*].busNum").value(hasItem(DEFAULT_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].hour").value(hasItem(DEFAULT_HOUR)))
            .andExpect(jsonPath("$.[*].min").value(hasItem(DEFAULT_MIN)))
            .andExpect(jsonPath("$.[*].pfmaxUp").value(hasItem(DEFAULT_PFMAX_UP.doubleValue())))
            .andExpect(jsonPath("$.[*].pfmaxDn").value(hasItem(DEFAULT_PFMAX_DN.doubleValue())))
            .andExpect(jsonPath("$.[*].qfmaxUp").value(hasItem(DEFAULT_QFMAX_UP.doubleValue())))
            .andExpect(jsonPath("$.[*].qfmaxDn").value(hasItem(DEFAULT_QFMAX_DN.doubleValue())));
    }

    @Test
    @Transactional
    void getFlexElVal() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get the flexElVal
        restFlexElValMockMvc
            .perform(get(ENTITY_API_URL_ID, flexElVal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flexElVal.getId().intValue()))
            .andExpect(jsonPath("$.busNum").value(DEFAULT_BUS_NUM.intValue()))
            .andExpect(jsonPath("$.hour").value(DEFAULT_HOUR))
            .andExpect(jsonPath("$.min").value(DEFAULT_MIN))
            .andExpect(jsonPath("$.pfmaxUp").value(DEFAULT_PFMAX_UP.doubleValue()))
            .andExpect(jsonPath("$.pfmaxDn").value(DEFAULT_PFMAX_DN.doubleValue()))
            .andExpect(jsonPath("$.qfmaxUp").value(DEFAULT_QFMAX_UP.doubleValue()))
            .andExpect(jsonPath("$.qfmaxDn").value(DEFAULT_QFMAX_DN.doubleValue()));
    }

    @Test
    @Transactional
    void getFlexElValsByIdFiltering() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        Long id = flexElVal.getId();

        defaultFlexElValShouldBeFound("id.equals=" + id);
        defaultFlexElValShouldNotBeFound("id.notEquals=" + id);

        defaultFlexElValShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFlexElValShouldNotBeFound("id.greaterThan=" + id);

        defaultFlexElValShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFlexElValShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFlexElValsByBusNumIsEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where busNum equals to DEFAULT_BUS_NUM
        defaultFlexElValShouldBeFound("busNum.equals=" + DEFAULT_BUS_NUM);

        // Get all the flexElValList where busNum equals to UPDATED_BUS_NUM
        defaultFlexElValShouldNotBeFound("busNum.equals=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllFlexElValsByBusNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where busNum not equals to DEFAULT_BUS_NUM
        defaultFlexElValShouldNotBeFound("busNum.notEquals=" + DEFAULT_BUS_NUM);

        // Get all the flexElValList where busNum not equals to UPDATED_BUS_NUM
        defaultFlexElValShouldBeFound("busNum.notEquals=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllFlexElValsByBusNumIsInShouldWork() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where busNum in DEFAULT_BUS_NUM or UPDATED_BUS_NUM
        defaultFlexElValShouldBeFound("busNum.in=" + DEFAULT_BUS_NUM + "," + UPDATED_BUS_NUM);

        // Get all the flexElValList where busNum equals to UPDATED_BUS_NUM
        defaultFlexElValShouldNotBeFound("busNum.in=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllFlexElValsByBusNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where busNum is not null
        defaultFlexElValShouldBeFound("busNum.specified=true");

        // Get all the flexElValList where busNum is null
        defaultFlexElValShouldNotBeFound("busNum.specified=false");
    }

    @Test
    @Transactional
    void getAllFlexElValsByBusNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where busNum is greater than or equal to DEFAULT_BUS_NUM
        defaultFlexElValShouldBeFound("busNum.greaterThanOrEqual=" + DEFAULT_BUS_NUM);

        // Get all the flexElValList where busNum is greater than or equal to UPDATED_BUS_NUM
        defaultFlexElValShouldNotBeFound("busNum.greaterThanOrEqual=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllFlexElValsByBusNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where busNum is less than or equal to DEFAULT_BUS_NUM
        defaultFlexElValShouldBeFound("busNum.lessThanOrEqual=" + DEFAULT_BUS_NUM);

        // Get all the flexElValList where busNum is less than or equal to SMALLER_BUS_NUM
        defaultFlexElValShouldNotBeFound("busNum.lessThanOrEqual=" + SMALLER_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllFlexElValsByBusNumIsLessThanSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where busNum is less than DEFAULT_BUS_NUM
        defaultFlexElValShouldNotBeFound("busNum.lessThan=" + DEFAULT_BUS_NUM);

        // Get all the flexElValList where busNum is less than UPDATED_BUS_NUM
        defaultFlexElValShouldBeFound("busNum.lessThan=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllFlexElValsByBusNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where busNum is greater than DEFAULT_BUS_NUM
        defaultFlexElValShouldNotBeFound("busNum.greaterThan=" + DEFAULT_BUS_NUM);

        // Get all the flexElValList where busNum is greater than SMALLER_BUS_NUM
        defaultFlexElValShouldBeFound("busNum.greaterThan=" + SMALLER_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllFlexElValsByHourIsEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where hour equals to DEFAULT_HOUR
        defaultFlexElValShouldBeFound("hour.equals=" + DEFAULT_HOUR);

        // Get all the flexElValList where hour equals to UPDATED_HOUR
        defaultFlexElValShouldNotBeFound("hour.equals=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllFlexElValsByHourIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where hour not equals to DEFAULT_HOUR
        defaultFlexElValShouldNotBeFound("hour.notEquals=" + DEFAULT_HOUR);

        // Get all the flexElValList where hour not equals to UPDATED_HOUR
        defaultFlexElValShouldBeFound("hour.notEquals=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllFlexElValsByHourIsInShouldWork() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where hour in DEFAULT_HOUR or UPDATED_HOUR
        defaultFlexElValShouldBeFound("hour.in=" + DEFAULT_HOUR + "," + UPDATED_HOUR);

        // Get all the flexElValList where hour equals to UPDATED_HOUR
        defaultFlexElValShouldNotBeFound("hour.in=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllFlexElValsByHourIsNullOrNotNull() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where hour is not null
        defaultFlexElValShouldBeFound("hour.specified=true");

        // Get all the flexElValList where hour is null
        defaultFlexElValShouldNotBeFound("hour.specified=false");
    }

    @Test
    @Transactional
    void getAllFlexElValsByHourIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where hour is greater than or equal to DEFAULT_HOUR
        defaultFlexElValShouldBeFound("hour.greaterThanOrEqual=" + DEFAULT_HOUR);

        // Get all the flexElValList where hour is greater than or equal to UPDATED_HOUR
        defaultFlexElValShouldNotBeFound("hour.greaterThanOrEqual=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllFlexElValsByHourIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where hour is less than or equal to DEFAULT_HOUR
        defaultFlexElValShouldBeFound("hour.lessThanOrEqual=" + DEFAULT_HOUR);

        // Get all the flexElValList where hour is less than or equal to SMALLER_HOUR
        defaultFlexElValShouldNotBeFound("hour.lessThanOrEqual=" + SMALLER_HOUR);
    }

    @Test
    @Transactional
    void getAllFlexElValsByHourIsLessThanSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where hour is less than DEFAULT_HOUR
        defaultFlexElValShouldNotBeFound("hour.lessThan=" + DEFAULT_HOUR);

        // Get all the flexElValList where hour is less than UPDATED_HOUR
        defaultFlexElValShouldBeFound("hour.lessThan=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllFlexElValsByHourIsGreaterThanSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where hour is greater than DEFAULT_HOUR
        defaultFlexElValShouldNotBeFound("hour.greaterThan=" + DEFAULT_HOUR);

        // Get all the flexElValList where hour is greater than SMALLER_HOUR
        defaultFlexElValShouldBeFound("hour.greaterThan=" + SMALLER_HOUR);
    }

    @Test
    @Transactional
    void getAllFlexElValsByMinIsEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where min equals to DEFAULT_MIN
        defaultFlexElValShouldBeFound("min.equals=" + DEFAULT_MIN);

        // Get all the flexElValList where min equals to UPDATED_MIN
        defaultFlexElValShouldNotBeFound("min.equals=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllFlexElValsByMinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where min not equals to DEFAULT_MIN
        defaultFlexElValShouldNotBeFound("min.notEquals=" + DEFAULT_MIN);

        // Get all the flexElValList where min not equals to UPDATED_MIN
        defaultFlexElValShouldBeFound("min.notEquals=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllFlexElValsByMinIsInShouldWork() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where min in DEFAULT_MIN or UPDATED_MIN
        defaultFlexElValShouldBeFound("min.in=" + DEFAULT_MIN + "," + UPDATED_MIN);

        // Get all the flexElValList where min equals to UPDATED_MIN
        defaultFlexElValShouldNotBeFound("min.in=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllFlexElValsByMinIsNullOrNotNull() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where min is not null
        defaultFlexElValShouldBeFound("min.specified=true");

        // Get all the flexElValList where min is null
        defaultFlexElValShouldNotBeFound("min.specified=false");
    }

    @Test
    @Transactional
    void getAllFlexElValsByMinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where min is greater than or equal to DEFAULT_MIN
        defaultFlexElValShouldBeFound("min.greaterThanOrEqual=" + DEFAULT_MIN);

        // Get all the flexElValList where min is greater than or equal to UPDATED_MIN
        defaultFlexElValShouldNotBeFound("min.greaterThanOrEqual=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllFlexElValsByMinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where min is less than or equal to DEFAULT_MIN
        defaultFlexElValShouldBeFound("min.lessThanOrEqual=" + DEFAULT_MIN);

        // Get all the flexElValList where min is less than or equal to SMALLER_MIN
        defaultFlexElValShouldNotBeFound("min.lessThanOrEqual=" + SMALLER_MIN);
    }

    @Test
    @Transactional
    void getAllFlexElValsByMinIsLessThanSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where min is less than DEFAULT_MIN
        defaultFlexElValShouldNotBeFound("min.lessThan=" + DEFAULT_MIN);

        // Get all the flexElValList where min is less than UPDATED_MIN
        defaultFlexElValShouldBeFound("min.lessThan=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllFlexElValsByMinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where min is greater than DEFAULT_MIN
        defaultFlexElValShouldNotBeFound("min.greaterThan=" + DEFAULT_MIN);

        // Get all the flexElValList where min is greater than SMALLER_MIN
        defaultFlexElValShouldBeFound("min.greaterThan=" + SMALLER_MIN);
    }

    @Test
    @Transactional
    void getAllFlexElValsByPfmaxUpIsEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where pfmaxUp equals to DEFAULT_PFMAX_UP
        defaultFlexElValShouldBeFound("pfmaxUp.equals=" + DEFAULT_PFMAX_UP);

        // Get all the flexElValList where pfmaxUp equals to UPDATED_PFMAX_UP
        defaultFlexElValShouldNotBeFound("pfmaxUp.equals=" + UPDATED_PFMAX_UP);
    }

    @Test
    @Transactional
    void getAllFlexElValsByPfmaxUpIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where pfmaxUp not equals to DEFAULT_PFMAX_UP
        defaultFlexElValShouldNotBeFound("pfmaxUp.notEquals=" + DEFAULT_PFMAX_UP);

        // Get all the flexElValList where pfmaxUp not equals to UPDATED_PFMAX_UP
        defaultFlexElValShouldBeFound("pfmaxUp.notEquals=" + UPDATED_PFMAX_UP);
    }

    @Test
    @Transactional
    void getAllFlexElValsByPfmaxUpIsInShouldWork() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where pfmaxUp in DEFAULT_PFMAX_UP or UPDATED_PFMAX_UP
        defaultFlexElValShouldBeFound("pfmaxUp.in=" + DEFAULT_PFMAX_UP + "," + UPDATED_PFMAX_UP);

        // Get all the flexElValList where pfmaxUp equals to UPDATED_PFMAX_UP
        defaultFlexElValShouldNotBeFound("pfmaxUp.in=" + UPDATED_PFMAX_UP);
    }

    @Test
    @Transactional
    void getAllFlexElValsByPfmaxUpIsNullOrNotNull() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where pfmaxUp is not null
        defaultFlexElValShouldBeFound("pfmaxUp.specified=true");

        // Get all the flexElValList where pfmaxUp is null
        defaultFlexElValShouldNotBeFound("pfmaxUp.specified=false");
    }

    @Test
    @Transactional
    void getAllFlexElValsByPfmaxUpIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where pfmaxUp is greater than or equal to DEFAULT_PFMAX_UP
        defaultFlexElValShouldBeFound("pfmaxUp.greaterThanOrEqual=" + DEFAULT_PFMAX_UP);

        // Get all the flexElValList where pfmaxUp is greater than or equal to UPDATED_PFMAX_UP
        defaultFlexElValShouldNotBeFound("pfmaxUp.greaterThanOrEqual=" + UPDATED_PFMAX_UP);
    }

    @Test
    @Transactional
    void getAllFlexElValsByPfmaxUpIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where pfmaxUp is less than or equal to DEFAULT_PFMAX_UP
        defaultFlexElValShouldBeFound("pfmaxUp.lessThanOrEqual=" + DEFAULT_PFMAX_UP);

        // Get all the flexElValList where pfmaxUp is less than or equal to SMALLER_PFMAX_UP
        defaultFlexElValShouldNotBeFound("pfmaxUp.lessThanOrEqual=" + SMALLER_PFMAX_UP);
    }

    @Test
    @Transactional
    void getAllFlexElValsByPfmaxUpIsLessThanSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where pfmaxUp is less than DEFAULT_PFMAX_UP
        defaultFlexElValShouldNotBeFound("pfmaxUp.lessThan=" + DEFAULT_PFMAX_UP);

        // Get all the flexElValList where pfmaxUp is less than UPDATED_PFMAX_UP
        defaultFlexElValShouldBeFound("pfmaxUp.lessThan=" + UPDATED_PFMAX_UP);
    }

    @Test
    @Transactional
    void getAllFlexElValsByPfmaxUpIsGreaterThanSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where pfmaxUp is greater than DEFAULT_PFMAX_UP
        defaultFlexElValShouldNotBeFound("pfmaxUp.greaterThan=" + DEFAULT_PFMAX_UP);

        // Get all the flexElValList where pfmaxUp is greater than SMALLER_PFMAX_UP
        defaultFlexElValShouldBeFound("pfmaxUp.greaterThan=" + SMALLER_PFMAX_UP);
    }

    @Test
    @Transactional
    void getAllFlexElValsByPfmaxDnIsEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where pfmaxDn equals to DEFAULT_PFMAX_DN
        defaultFlexElValShouldBeFound("pfmaxDn.equals=" + DEFAULT_PFMAX_DN);

        // Get all the flexElValList where pfmaxDn equals to UPDATED_PFMAX_DN
        defaultFlexElValShouldNotBeFound("pfmaxDn.equals=" + UPDATED_PFMAX_DN);
    }

    @Test
    @Transactional
    void getAllFlexElValsByPfmaxDnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where pfmaxDn not equals to DEFAULT_PFMAX_DN
        defaultFlexElValShouldNotBeFound("pfmaxDn.notEquals=" + DEFAULT_PFMAX_DN);

        // Get all the flexElValList where pfmaxDn not equals to UPDATED_PFMAX_DN
        defaultFlexElValShouldBeFound("pfmaxDn.notEquals=" + UPDATED_PFMAX_DN);
    }

    @Test
    @Transactional
    void getAllFlexElValsByPfmaxDnIsInShouldWork() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where pfmaxDn in DEFAULT_PFMAX_DN or UPDATED_PFMAX_DN
        defaultFlexElValShouldBeFound("pfmaxDn.in=" + DEFAULT_PFMAX_DN + "," + UPDATED_PFMAX_DN);

        // Get all the flexElValList where pfmaxDn equals to UPDATED_PFMAX_DN
        defaultFlexElValShouldNotBeFound("pfmaxDn.in=" + UPDATED_PFMAX_DN);
    }

    @Test
    @Transactional
    void getAllFlexElValsByPfmaxDnIsNullOrNotNull() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where pfmaxDn is not null
        defaultFlexElValShouldBeFound("pfmaxDn.specified=true");

        // Get all the flexElValList where pfmaxDn is null
        defaultFlexElValShouldNotBeFound("pfmaxDn.specified=false");
    }

    @Test
    @Transactional
    void getAllFlexElValsByPfmaxDnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where pfmaxDn is greater than or equal to DEFAULT_PFMAX_DN
        defaultFlexElValShouldBeFound("pfmaxDn.greaterThanOrEqual=" + DEFAULT_PFMAX_DN);

        // Get all the flexElValList where pfmaxDn is greater than or equal to UPDATED_PFMAX_DN
        defaultFlexElValShouldNotBeFound("pfmaxDn.greaterThanOrEqual=" + UPDATED_PFMAX_DN);
    }

    @Test
    @Transactional
    void getAllFlexElValsByPfmaxDnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where pfmaxDn is less than or equal to DEFAULT_PFMAX_DN
        defaultFlexElValShouldBeFound("pfmaxDn.lessThanOrEqual=" + DEFAULT_PFMAX_DN);

        // Get all the flexElValList where pfmaxDn is less than or equal to SMALLER_PFMAX_DN
        defaultFlexElValShouldNotBeFound("pfmaxDn.lessThanOrEqual=" + SMALLER_PFMAX_DN);
    }

    @Test
    @Transactional
    void getAllFlexElValsByPfmaxDnIsLessThanSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where pfmaxDn is less than DEFAULT_PFMAX_DN
        defaultFlexElValShouldNotBeFound("pfmaxDn.lessThan=" + DEFAULT_PFMAX_DN);

        // Get all the flexElValList where pfmaxDn is less than UPDATED_PFMAX_DN
        defaultFlexElValShouldBeFound("pfmaxDn.lessThan=" + UPDATED_PFMAX_DN);
    }

    @Test
    @Transactional
    void getAllFlexElValsByPfmaxDnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where pfmaxDn is greater than DEFAULT_PFMAX_DN
        defaultFlexElValShouldNotBeFound("pfmaxDn.greaterThan=" + DEFAULT_PFMAX_DN);

        // Get all the flexElValList where pfmaxDn is greater than SMALLER_PFMAX_DN
        defaultFlexElValShouldBeFound("pfmaxDn.greaterThan=" + SMALLER_PFMAX_DN);
    }

    @Test
    @Transactional
    void getAllFlexElValsByQfmaxUpIsEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where qfmaxUp equals to DEFAULT_QFMAX_UP
        defaultFlexElValShouldBeFound("qfmaxUp.equals=" + DEFAULT_QFMAX_UP);

        // Get all the flexElValList where qfmaxUp equals to UPDATED_QFMAX_UP
        defaultFlexElValShouldNotBeFound("qfmaxUp.equals=" + UPDATED_QFMAX_UP);
    }

    @Test
    @Transactional
    void getAllFlexElValsByQfmaxUpIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where qfmaxUp not equals to DEFAULT_QFMAX_UP
        defaultFlexElValShouldNotBeFound("qfmaxUp.notEquals=" + DEFAULT_QFMAX_UP);

        // Get all the flexElValList where qfmaxUp not equals to UPDATED_QFMAX_UP
        defaultFlexElValShouldBeFound("qfmaxUp.notEquals=" + UPDATED_QFMAX_UP);
    }

    @Test
    @Transactional
    void getAllFlexElValsByQfmaxUpIsInShouldWork() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where qfmaxUp in DEFAULT_QFMAX_UP or UPDATED_QFMAX_UP
        defaultFlexElValShouldBeFound("qfmaxUp.in=" + DEFAULT_QFMAX_UP + "," + UPDATED_QFMAX_UP);

        // Get all the flexElValList where qfmaxUp equals to UPDATED_QFMAX_UP
        defaultFlexElValShouldNotBeFound("qfmaxUp.in=" + UPDATED_QFMAX_UP);
    }

    @Test
    @Transactional
    void getAllFlexElValsByQfmaxUpIsNullOrNotNull() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where qfmaxUp is not null
        defaultFlexElValShouldBeFound("qfmaxUp.specified=true");

        // Get all the flexElValList where qfmaxUp is null
        defaultFlexElValShouldNotBeFound("qfmaxUp.specified=false");
    }

    @Test
    @Transactional
    void getAllFlexElValsByQfmaxUpIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where qfmaxUp is greater than or equal to DEFAULT_QFMAX_UP
        defaultFlexElValShouldBeFound("qfmaxUp.greaterThanOrEqual=" + DEFAULT_QFMAX_UP);

        // Get all the flexElValList where qfmaxUp is greater than or equal to UPDATED_QFMAX_UP
        defaultFlexElValShouldNotBeFound("qfmaxUp.greaterThanOrEqual=" + UPDATED_QFMAX_UP);
    }

    @Test
    @Transactional
    void getAllFlexElValsByQfmaxUpIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where qfmaxUp is less than or equal to DEFAULT_QFMAX_UP
        defaultFlexElValShouldBeFound("qfmaxUp.lessThanOrEqual=" + DEFAULT_QFMAX_UP);

        // Get all the flexElValList where qfmaxUp is less than or equal to SMALLER_QFMAX_UP
        defaultFlexElValShouldNotBeFound("qfmaxUp.lessThanOrEqual=" + SMALLER_QFMAX_UP);
    }

    @Test
    @Transactional
    void getAllFlexElValsByQfmaxUpIsLessThanSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where qfmaxUp is less than DEFAULT_QFMAX_UP
        defaultFlexElValShouldNotBeFound("qfmaxUp.lessThan=" + DEFAULT_QFMAX_UP);

        // Get all the flexElValList where qfmaxUp is less than UPDATED_QFMAX_UP
        defaultFlexElValShouldBeFound("qfmaxUp.lessThan=" + UPDATED_QFMAX_UP);
    }

    @Test
    @Transactional
    void getAllFlexElValsByQfmaxUpIsGreaterThanSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where qfmaxUp is greater than DEFAULT_QFMAX_UP
        defaultFlexElValShouldNotBeFound("qfmaxUp.greaterThan=" + DEFAULT_QFMAX_UP);

        // Get all the flexElValList where qfmaxUp is greater than SMALLER_QFMAX_UP
        defaultFlexElValShouldBeFound("qfmaxUp.greaterThan=" + SMALLER_QFMAX_UP);
    }

    @Test
    @Transactional
    void getAllFlexElValsByQfmaxDnIsEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where qfmaxDn equals to DEFAULT_QFMAX_DN
        defaultFlexElValShouldBeFound("qfmaxDn.equals=" + DEFAULT_QFMAX_DN);

        // Get all the flexElValList where qfmaxDn equals to UPDATED_QFMAX_DN
        defaultFlexElValShouldNotBeFound("qfmaxDn.equals=" + UPDATED_QFMAX_DN);
    }

    @Test
    @Transactional
    void getAllFlexElValsByQfmaxDnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where qfmaxDn not equals to DEFAULT_QFMAX_DN
        defaultFlexElValShouldNotBeFound("qfmaxDn.notEquals=" + DEFAULT_QFMAX_DN);

        // Get all the flexElValList where qfmaxDn not equals to UPDATED_QFMAX_DN
        defaultFlexElValShouldBeFound("qfmaxDn.notEquals=" + UPDATED_QFMAX_DN);
    }

    @Test
    @Transactional
    void getAllFlexElValsByQfmaxDnIsInShouldWork() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where qfmaxDn in DEFAULT_QFMAX_DN or UPDATED_QFMAX_DN
        defaultFlexElValShouldBeFound("qfmaxDn.in=" + DEFAULT_QFMAX_DN + "," + UPDATED_QFMAX_DN);

        // Get all the flexElValList where qfmaxDn equals to UPDATED_QFMAX_DN
        defaultFlexElValShouldNotBeFound("qfmaxDn.in=" + UPDATED_QFMAX_DN);
    }

    @Test
    @Transactional
    void getAllFlexElValsByQfmaxDnIsNullOrNotNull() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where qfmaxDn is not null
        defaultFlexElValShouldBeFound("qfmaxDn.specified=true");

        // Get all the flexElValList where qfmaxDn is null
        defaultFlexElValShouldNotBeFound("qfmaxDn.specified=false");
    }

    @Test
    @Transactional
    void getAllFlexElValsByQfmaxDnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where qfmaxDn is greater than or equal to DEFAULT_QFMAX_DN
        defaultFlexElValShouldBeFound("qfmaxDn.greaterThanOrEqual=" + DEFAULT_QFMAX_DN);

        // Get all the flexElValList where qfmaxDn is greater than or equal to UPDATED_QFMAX_DN
        defaultFlexElValShouldNotBeFound("qfmaxDn.greaterThanOrEqual=" + UPDATED_QFMAX_DN);
    }

    @Test
    @Transactional
    void getAllFlexElValsByQfmaxDnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where qfmaxDn is less than or equal to DEFAULT_QFMAX_DN
        defaultFlexElValShouldBeFound("qfmaxDn.lessThanOrEqual=" + DEFAULT_QFMAX_DN);

        // Get all the flexElValList where qfmaxDn is less than or equal to SMALLER_QFMAX_DN
        defaultFlexElValShouldNotBeFound("qfmaxDn.lessThanOrEqual=" + SMALLER_QFMAX_DN);
    }

    @Test
    @Transactional
    void getAllFlexElValsByQfmaxDnIsLessThanSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where qfmaxDn is less than DEFAULT_QFMAX_DN
        defaultFlexElValShouldNotBeFound("qfmaxDn.lessThan=" + DEFAULT_QFMAX_DN);

        // Get all the flexElValList where qfmaxDn is less than UPDATED_QFMAX_DN
        defaultFlexElValShouldBeFound("qfmaxDn.lessThan=" + UPDATED_QFMAX_DN);
    }

    @Test
    @Transactional
    void getAllFlexElValsByQfmaxDnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        // Get all the flexElValList where qfmaxDn is greater than DEFAULT_QFMAX_DN
        defaultFlexElValShouldNotBeFound("qfmaxDn.greaterThan=" + DEFAULT_QFMAX_DN);

        // Get all the flexElValList where qfmaxDn is greater than SMALLER_QFMAX_DN
        defaultFlexElValShouldBeFound("qfmaxDn.greaterThan=" + SMALLER_QFMAX_DN);
    }

    @Test
    @Transactional
    void getAllFlexElValsByFlexProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);
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
        flexElVal.setFlexProfile(flexProfile);
        flexElValRepository.saveAndFlush(flexElVal);
        Long flexProfileId = flexProfile.getId();

        // Get all the flexElValList where flexProfile equals to flexProfileId
        defaultFlexElValShouldBeFound("flexProfileId.equals=" + flexProfileId);

        // Get all the flexElValList where flexProfile equals to (flexProfileId + 1)
        defaultFlexElValShouldNotBeFound("flexProfileId.equals=" + (flexProfileId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFlexElValShouldBeFound(String filter) throws Exception {
        restFlexElValMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flexElVal.getId().intValue())))
            .andExpect(jsonPath("$.[*].busNum").value(hasItem(DEFAULT_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].hour").value(hasItem(DEFAULT_HOUR)))
            .andExpect(jsonPath("$.[*].min").value(hasItem(DEFAULT_MIN)))
            .andExpect(jsonPath("$.[*].pfmaxUp").value(hasItem(DEFAULT_PFMAX_UP.doubleValue())))
            .andExpect(jsonPath("$.[*].pfmaxDn").value(hasItem(DEFAULT_PFMAX_DN.doubleValue())))
            .andExpect(jsonPath("$.[*].qfmaxUp").value(hasItem(DEFAULT_QFMAX_UP.doubleValue())))
            .andExpect(jsonPath("$.[*].qfmaxDn").value(hasItem(DEFAULT_QFMAX_DN.doubleValue())));

        // Check, that the count call also returns 1
        restFlexElValMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFlexElValShouldNotBeFound(String filter) throws Exception {
        restFlexElValMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFlexElValMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFlexElVal() throws Exception {
        // Get the flexElVal
        restFlexElValMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFlexElVal() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        int databaseSizeBeforeUpdate = flexElValRepository.findAll().size();

        // Update the flexElVal
        FlexElVal updatedFlexElVal = flexElValRepository.findById(flexElVal.getId()).get();
        // Disconnect from session so that the updates on updatedFlexElVal are not directly saved in db
        em.detach(updatedFlexElVal);
        updatedFlexElVal
            .busNum(UPDATED_BUS_NUM)
            .hour(UPDATED_HOUR)
            .min(UPDATED_MIN)
            .pfmaxUp(UPDATED_PFMAX_UP)
            .pfmaxDn(UPDATED_PFMAX_DN)
            .qfmaxUp(UPDATED_QFMAX_UP)
            .qfmaxDn(UPDATED_QFMAX_DN);
        FlexElValDTO flexElValDTO = flexElValMapper.toDto(updatedFlexElVal);

        restFlexElValMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flexElValDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexElValDTO))
            )
            .andExpect(status().isOk());

        // Validate the FlexElVal in the database
        List<FlexElVal> flexElValList = flexElValRepository.findAll();
        assertThat(flexElValList).hasSize(databaseSizeBeforeUpdate);
        FlexElVal testFlexElVal = flexElValList.get(flexElValList.size() - 1);
        assertThat(testFlexElVal.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testFlexElVal.getHour()).isEqualTo(UPDATED_HOUR);
        assertThat(testFlexElVal.getMin()).isEqualTo(UPDATED_MIN);
        assertThat(testFlexElVal.getPfmaxUp()).isEqualTo(UPDATED_PFMAX_UP);
        assertThat(testFlexElVal.getPfmaxDn()).isEqualTo(UPDATED_PFMAX_DN);
        assertThat(testFlexElVal.getQfmaxUp()).isEqualTo(UPDATED_QFMAX_UP);
        assertThat(testFlexElVal.getQfmaxDn()).isEqualTo(UPDATED_QFMAX_DN);
    }

    @Test
    @Transactional
    void putNonExistingFlexElVal() throws Exception {
        int databaseSizeBeforeUpdate = flexElValRepository.findAll().size();
        flexElVal.setId(count.incrementAndGet());

        // Create the FlexElVal
        FlexElValDTO flexElValDTO = flexElValMapper.toDto(flexElVal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlexElValMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flexElValDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexElVal in the database
        List<FlexElVal> flexElValList = flexElValRepository.findAll();
        assertThat(flexElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFlexElVal() throws Exception {
        int databaseSizeBeforeUpdate = flexElValRepository.findAll().size();
        flexElVal.setId(count.incrementAndGet());

        // Create the FlexElVal
        FlexElValDTO flexElValDTO = flexElValMapper.toDto(flexElVal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexElValMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexElVal in the database
        List<FlexElVal> flexElValList = flexElValRepository.findAll();
        assertThat(flexElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFlexElVal() throws Exception {
        int databaseSizeBeforeUpdate = flexElValRepository.findAll().size();
        flexElVal.setId(count.incrementAndGet());

        // Create the FlexElVal
        FlexElValDTO flexElValDTO = flexElValMapper.toDto(flexElVal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexElValMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexElValDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlexElVal in the database
        List<FlexElVal> flexElValList = flexElValRepository.findAll();
        assertThat(flexElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFlexElValWithPatch() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        int databaseSizeBeforeUpdate = flexElValRepository.findAll().size();

        // Update the flexElVal using partial update
        FlexElVal partialUpdatedFlexElVal = new FlexElVal();
        partialUpdatedFlexElVal.setId(flexElVal.getId());

        partialUpdatedFlexElVal
            .hour(UPDATED_HOUR)
            .min(UPDATED_MIN)
            .pfmaxUp(UPDATED_PFMAX_UP)
            .qfmaxUp(UPDATED_QFMAX_UP)
            .qfmaxDn(UPDATED_QFMAX_DN);

        restFlexElValMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlexElVal.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlexElVal))
            )
            .andExpect(status().isOk());

        // Validate the FlexElVal in the database
        List<FlexElVal> flexElValList = flexElValRepository.findAll();
        assertThat(flexElValList).hasSize(databaseSizeBeforeUpdate);
        FlexElVal testFlexElVal = flexElValList.get(flexElValList.size() - 1);
        assertThat(testFlexElVal.getBusNum()).isEqualTo(DEFAULT_BUS_NUM);
        assertThat(testFlexElVal.getHour()).isEqualTo(UPDATED_HOUR);
        assertThat(testFlexElVal.getMin()).isEqualTo(UPDATED_MIN);
        assertThat(testFlexElVal.getPfmaxUp()).isEqualTo(UPDATED_PFMAX_UP);
        assertThat(testFlexElVal.getPfmaxDn()).isEqualTo(DEFAULT_PFMAX_DN);
        assertThat(testFlexElVal.getQfmaxUp()).isEqualTo(UPDATED_QFMAX_UP);
        assertThat(testFlexElVal.getQfmaxDn()).isEqualTo(UPDATED_QFMAX_DN);
    }

    @Test
    @Transactional
    void fullUpdateFlexElValWithPatch() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        int databaseSizeBeforeUpdate = flexElValRepository.findAll().size();

        // Update the flexElVal using partial update
        FlexElVal partialUpdatedFlexElVal = new FlexElVal();
        partialUpdatedFlexElVal.setId(flexElVal.getId());

        partialUpdatedFlexElVal
            .busNum(UPDATED_BUS_NUM)
            .hour(UPDATED_HOUR)
            .min(UPDATED_MIN)
            .pfmaxUp(UPDATED_PFMAX_UP)
            .pfmaxDn(UPDATED_PFMAX_DN)
            .qfmaxUp(UPDATED_QFMAX_UP)
            .qfmaxDn(UPDATED_QFMAX_DN);

        restFlexElValMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlexElVal.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlexElVal))
            )
            .andExpect(status().isOk());

        // Validate the FlexElVal in the database
        List<FlexElVal> flexElValList = flexElValRepository.findAll();
        assertThat(flexElValList).hasSize(databaseSizeBeforeUpdate);
        FlexElVal testFlexElVal = flexElValList.get(flexElValList.size() - 1);
        assertThat(testFlexElVal.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testFlexElVal.getHour()).isEqualTo(UPDATED_HOUR);
        assertThat(testFlexElVal.getMin()).isEqualTo(UPDATED_MIN);
        assertThat(testFlexElVal.getPfmaxUp()).isEqualTo(UPDATED_PFMAX_UP);
        assertThat(testFlexElVal.getPfmaxDn()).isEqualTo(UPDATED_PFMAX_DN);
        assertThat(testFlexElVal.getQfmaxUp()).isEqualTo(UPDATED_QFMAX_UP);
        assertThat(testFlexElVal.getQfmaxDn()).isEqualTo(UPDATED_QFMAX_DN);
    }

    @Test
    @Transactional
    void patchNonExistingFlexElVal() throws Exception {
        int databaseSizeBeforeUpdate = flexElValRepository.findAll().size();
        flexElVal.setId(count.incrementAndGet());

        // Create the FlexElVal
        FlexElValDTO flexElValDTO = flexElValMapper.toDto(flexElVal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlexElValMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, flexElValDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flexElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexElVal in the database
        List<FlexElVal> flexElValList = flexElValRepository.findAll();
        assertThat(flexElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFlexElVal() throws Exception {
        int databaseSizeBeforeUpdate = flexElValRepository.findAll().size();
        flexElVal.setId(count.incrementAndGet());

        // Create the FlexElVal
        FlexElValDTO flexElValDTO = flexElValMapper.toDto(flexElVal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexElValMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flexElValDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexElVal in the database
        List<FlexElVal> flexElValList = flexElValRepository.findAll();
        assertThat(flexElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFlexElVal() throws Exception {
        int databaseSizeBeforeUpdate = flexElValRepository.findAll().size();
        flexElVal.setId(count.incrementAndGet());

        // Create the FlexElVal
        FlexElValDTO flexElValDTO = flexElValMapper.toDto(flexElVal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexElValMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flexElValDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlexElVal in the database
        List<FlexElVal> flexElValList = flexElValRepository.findAll();
        assertThat(flexElValList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFlexElVal() throws Exception {
        // Initialize the database
        flexElValRepository.saveAndFlush(flexElVal);

        int databaseSizeBeforeDelete = flexElValRepository.findAll().size();

        // Delete the flexElVal
        restFlexElValMockMvc
            .perform(delete(ENTITY_API_URL_ID, flexElVal.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FlexElVal> flexElValList = flexElValRepository.findAll();
        assertThat(flexElValList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
