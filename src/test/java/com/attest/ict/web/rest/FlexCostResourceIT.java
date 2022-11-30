package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.FlexCost;
import com.attest.ict.domain.FlexProfile;
import com.attest.ict.repository.FlexCostRepository;
import com.attest.ict.service.criteria.FlexCostCriteria;
import com.attest.ict.service.dto.FlexCostDTO;
import com.attest.ict.service.mapper.FlexCostMapper;
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
 * Integration tests for the {@link FlexCostResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FlexCostResourceIT {

    private static final Long DEFAULT_BUS_NUM = 1L;
    private static final Long UPDATED_BUS_NUM = 2L;
    private static final Long SMALLER_BUS_NUM = 1L - 1L;

    private static final Integer DEFAULT_MODEL = 1;
    private static final Integer UPDATED_MODEL = 2;
    private static final Integer SMALLER_MODEL = 1 - 1;

    private static final Long DEFAULT_N_COST = 1L;
    private static final Long UPDATED_N_COST = 2L;
    private static final Long SMALLER_N_COST = 1L - 1L;

    private static final Double DEFAULT_COST_PR = 1D;
    private static final Double UPDATED_COST_PR = 2D;
    private static final Double SMALLER_COST_PR = 1D - 1D;

    private static final Double DEFAULT_COST_QR = 1D;
    private static final Double UPDATED_COST_QR = 2D;
    private static final Double SMALLER_COST_QR = 1D - 1D;

    private static final String DEFAULT_COST_PF = "AAAAAAAAAA";
    private static final String UPDATED_COST_PF = "BBBBBBBBBB";

    private static final String DEFAULT_COST_QF = "AAAAAAAAAA";
    private static final String UPDATED_COST_QF = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/flex-costs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FlexCostRepository flexCostRepository;

    @Autowired
    private FlexCostMapper flexCostMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFlexCostMockMvc;

    private FlexCost flexCost;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlexCost createEntity(EntityManager em) {
        FlexCost flexCost = new FlexCost()
            .busNum(DEFAULT_BUS_NUM)
            .model(DEFAULT_MODEL)
            .nCost(DEFAULT_N_COST)
            .costPr(DEFAULT_COST_PR)
            .costQr(DEFAULT_COST_QR)
            .costPf(DEFAULT_COST_PF)
            .costQf(DEFAULT_COST_QF);
        return flexCost;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlexCost createUpdatedEntity(EntityManager em) {
        FlexCost flexCost = new FlexCost()
            .busNum(UPDATED_BUS_NUM)
            .model(UPDATED_MODEL)
            .nCost(UPDATED_N_COST)
            .costPr(UPDATED_COST_PR)
            .costQr(UPDATED_COST_QR)
            .costPf(UPDATED_COST_PF)
            .costQf(UPDATED_COST_QF);
        return flexCost;
    }

    @BeforeEach
    public void initTest() {
        flexCost = createEntity(em);
    }

    @Test
    @Transactional
    void createFlexCost() throws Exception {
        int databaseSizeBeforeCreate = flexCostRepository.findAll().size();
        // Create the FlexCost
        FlexCostDTO flexCostDTO = flexCostMapper.toDto(flexCost);
        restFlexCostMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexCostDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FlexCost in the database
        List<FlexCost> flexCostList = flexCostRepository.findAll();
        assertThat(flexCostList).hasSize(databaseSizeBeforeCreate + 1);
        FlexCost testFlexCost = flexCostList.get(flexCostList.size() - 1);
        assertThat(testFlexCost.getBusNum()).isEqualTo(DEFAULT_BUS_NUM);
        assertThat(testFlexCost.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testFlexCost.getnCost()).isEqualTo(DEFAULT_N_COST);
        assertThat(testFlexCost.getCostPr()).isEqualTo(DEFAULT_COST_PR);
        assertThat(testFlexCost.getCostQr()).isEqualTo(DEFAULT_COST_QR);
        assertThat(testFlexCost.getCostPf()).isEqualTo(DEFAULT_COST_PF);
        assertThat(testFlexCost.getCostQf()).isEqualTo(DEFAULT_COST_QF);
    }

    @Test
    @Transactional
    void createFlexCostWithExistingId() throws Exception {
        // Create the FlexCost with an existing ID
        flexCost.setId(1L);
        FlexCostDTO flexCostDTO = flexCostMapper.toDto(flexCost);

        int databaseSizeBeforeCreate = flexCostRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlexCostMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexCostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexCost in the database
        List<FlexCost> flexCostList = flexCostRepository.findAll();
        assertThat(flexCostList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFlexCosts() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList
        restFlexCostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flexCost.getId().intValue())))
            .andExpect(jsonPath("$.[*].busNum").value(hasItem(DEFAULT_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].nCost").value(hasItem(DEFAULT_N_COST.intValue())))
            .andExpect(jsonPath("$.[*].costPr").value(hasItem(DEFAULT_COST_PR.doubleValue())))
            .andExpect(jsonPath("$.[*].costQr").value(hasItem(DEFAULT_COST_QR.doubleValue())))
            .andExpect(jsonPath("$.[*].costPf").value(hasItem(DEFAULT_COST_PF)))
            .andExpect(jsonPath("$.[*].costQf").value(hasItem(DEFAULT_COST_QF)));
    }

    @Test
    @Transactional
    void getFlexCost() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get the flexCost
        restFlexCostMockMvc
            .perform(get(ENTITY_API_URL_ID, flexCost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flexCost.getId().intValue()))
            .andExpect(jsonPath("$.busNum").value(DEFAULT_BUS_NUM.intValue()))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.nCost").value(DEFAULT_N_COST.intValue()))
            .andExpect(jsonPath("$.costPr").value(DEFAULT_COST_PR.doubleValue()))
            .andExpect(jsonPath("$.costQr").value(DEFAULT_COST_QR.doubleValue()))
            .andExpect(jsonPath("$.costPf").value(DEFAULT_COST_PF))
            .andExpect(jsonPath("$.costQf").value(DEFAULT_COST_QF));
    }

    @Test
    @Transactional
    void getFlexCostsByIdFiltering() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        Long id = flexCost.getId();

        defaultFlexCostShouldBeFound("id.equals=" + id);
        defaultFlexCostShouldNotBeFound("id.notEquals=" + id);

        defaultFlexCostShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFlexCostShouldNotBeFound("id.greaterThan=" + id);

        defaultFlexCostShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFlexCostShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFlexCostsByBusNumIsEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where busNum equals to DEFAULT_BUS_NUM
        defaultFlexCostShouldBeFound("busNum.equals=" + DEFAULT_BUS_NUM);

        // Get all the flexCostList where busNum equals to UPDATED_BUS_NUM
        defaultFlexCostShouldNotBeFound("busNum.equals=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllFlexCostsByBusNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where busNum not equals to DEFAULT_BUS_NUM
        defaultFlexCostShouldNotBeFound("busNum.notEquals=" + DEFAULT_BUS_NUM);

        // Get all the flexCostList where busNum not equals to UPDATED_BUS_NUM
        defaultFlexCostShouldBeFound("busNum.notEquals=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllFlexCostsByBusNumIsInShouldWork() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where busNum in DEFAULT_BUS_NUM or UPDATED_BUS_NUM
        defaultFlexCostShouldBeFound("busNum.in=" + DEFAULT_BUS_NUM + "," + UPDATED_BUS_NUM);

        // Get all the flexCostList where busNum equals to UPDATED_BUS_NUM
        defaultFlexCostShouldNotBeFound("busNum.in=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllFlexCostsByBusNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where busNum is not null
        defaultFlexCostShouldBeFound("busNum.specified=true");

        // Get all the flexCostList where busNum is null
        defaultFlexCostShouldNotBeFound("busNum.specified=false");
    }

    @Test
    @Transactional
    void getAllFlexCostsByBusNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where busNum is greater than or equal to DEFAULT_BUS_NUM
        defaultFlexCostShouldBeFound("busNum.greaterThanOrEqual=" + DEFAULT_BUS_NUM);

        // Get all the flexCostList where busNum is greater than or equal to UPDATED_BUS_NUM
        defaultFlexCostShouldNotBeFound("busNum.greaterThanOrEqual=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllFlexCostsByBusNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where busNum is less than or equal to DEFAULT_BUS_NUM
        defaultFlexCostShouldBeFound("busNum.lessThanOrEqual=" + DEFAULT_BUS_NUM);

        // Get all the flexCostList where busNum is less than or equal to SMALLER_BUS_NUM
        defaultFlexCostShouldNotBeFound("busNum.lessThanOrEqual=" + SMALLER_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllFlexCostsByBusNumIsLessThanSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where busNum is less than DEFAULT_BUS_NUM
        defaultFlexCostShouldNotBeFound("busNum.lessThan=" + DEFAULT_BUS_NUM);

        // Get all the flexCostList where busNum is less than UPDATED_BUS_NUM
        defaultFlexCostShouldBeFound("busNum.lessThan=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllFlexCostsByBusNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where busNum is greater than DEFAULT_BUS_NUM
        defaultFlexCostShouldNotBeFound("busNum.greaterThan=" + DEFAULT_BUS_NUM);

        // Get all the flexCostList where busNum is greater than SMALLER_BUS_NUM
        defaultFlexCostShouldBeFound("busNum.greaterThan=" + SMALLER_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllFlexCostsByModelIsEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where model equals to DEFAULT_MODEL
        defaultFlexCostShouldBeFound("model.equals=" + DEFAULT_MODEL);

        // Get all the flexCostList where model equals to UPDATED_MODEL
        defaultFlexCostShouldNotBeFound("model.equals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllFlexCostsByModelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where model not equals to DEFAULT_MODEL
        defaultFlexCostShouldNotBeFound("model.notEquals=" + DEFAULT_MODEL);

        // Get all the flexCostList where model not equals to UPDATED_MODEL
        defaultFlexCostShouldBeFound("model.notEquals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllFlexCostsByModelIsInShouldWork() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where model in DEFAULT_MODEL or UPDATED_MODEL
        defaultFlexCostShouldBeFound("model.in=" + DEFAULT_MODEL + "," + UPDATED_MODEL);

        // Get all the flexCostList where model equals to UPDATED_MODEL
        defaultFlexCostShouldNotBeFound("model.in=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllFlexCostsByModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where model is not null
        defaultFlexCostShouldBeFound("model.specified=true");

        // Get all the flexCostList where model is null
        defaultFlexCostShouldNotBeFound("model.specified=false");
    }

    @Test
    @Transactional
    void getAllFlexCostsByModelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where model is greater than or equal to DEFAULT_MODEL
        defaultFlexCostShouldBeFound("model.greaterThanOrEqual=" + DEFAULT_MODEL);

        // Get all the flexCostList where model is greater than or equal to UPDATED_MODEL
        defaultFlexCostShouldNotBeFound("model.greaterThanOrEqual=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllFlexCostsByModelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where model is less than or equal to DEFAULT_MODEL
        defaultFlexCostShouldBeFound("model.lessThanOrEqual=" + DEFAULT_MODEL);

        // Get all the flexCostList where model is less than or equal to SMALLER_MODEL
        defaultFlexCostShouldNotBeFound("model.lessThanOrEqual=" + SMALLER_MODEL);
    }

    @Test
    @Transactional
    void getAllFlexCostsByModelIsLessThanSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where model is less than DEFAULT_MODEL
        defaultFlexCostShouldNotBeFound("model.lessThan=" + DEFAULT_MODEL);

        // Get all the flexCostList where model is less than UPDATED_MODEL
        defaultFlexCostShouldBeFound("model.lessThan=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllFlexCostsByModelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where model is greater than DEFAULT_MODEL
        defaultFlexCostShouldNotBeFound("model.greaterThan=" + DEFAULT_MODEL);

        // Get all the flexCostList where model is greater than SMALLER_MODEL
        defaultFlexCostShouldBeFound("model.greaterThan=" + SMALLER_MODEL);
    }

    @Test
    @Transactional
    void getAllFlexCostsBynCostIsEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where nCost equals to DEFAULT_N_COST
        defaultFlexCostShouldBeFound("nCost.equals=" + DEFAULT_N_COST);

        // Get all the flexCostList where nCost equals to UPDATED_N_COST
        defaultFlexCostShouldNotBeFound("nCost.equals=" + UPDATED_N_COST);
    }

    @Test
    @Transactional
    void getAllFlexCostsBynCostIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where nCost not equals to DEFAULT_N_COST
        defaultFlexCostShouldNotBeFound("nCost.notEquals=" + DEFAULT_N_COST);

        // Get all the flexCostList where nCost not equals to UPDATED_N_COST
        defaultFlexCostShouldBeFound("nCost.notEquals=" + UPDATED_N_COST);
    }

    @Test
    @Transactional
    void getAllFlexCostsBynCostIsInShouldWork() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where nCost in DEFAULT_N_COST or UPDATED_N_COST
        defaultFlexCostShouldBeFound("nCost.in=" + DEFAULT_N_COST + "," + UPDATED_N_COST);

        // Get all the flexCostList where nCost equals to UPDATED_N_COST
        defaultFlexCostShouldNotBeFound("nCost.in=" + UPDATED_N_COST);
    }

    @Test
    @Transactional
    void getAllFlexCostsBynCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where nCost is not null
        defaultFlexCostShouldBeFound("nCost.specified=true");

        // Get all the flexCostList where nCost is null
        defaultFlexCostShouldNotBeFound("nCost.specified=false");
    }

    @Test
    @Transactional
    void getAllFlexCostsBynCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where nCost is greater than or equal to DEFAULT_N_COST
        defaultFlexCostShouldBeFound("nCost.greaterThanOrEqual=" + DEFAULT_N_COST);

        // Get all the flexCostList where nCost is greater than or equal to UPDATED_N_COST
        defaultFlexCostShouldNotBeFound("nCost.greaterThanOrEqual=" + UPDATED_N_COST);
    }

    @Test
    @Transactional
    void getAllFlexCostsBynCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where nCost is less than or equal to DEFAULT_N_COST
        defaultFlexCostShouldBeFound("nCost.lessThanOrEqual=" + DEFAULT_N_COST);

        // Get all the flexCostList where nCost is less than or equal to SMALLER_N_COST
        defaultFlexCostShouldNotBeFound("nCost.lessThanOrEqual=" + SMALLER_N_COST);
    }

    @Test
    @Transactional
    void getAllFlexCostsBynCostIsLessThanSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where nCost is less than DEFAULT_N_COST
        defaultFlexCostShouldNotBeFound("nCost.lessThan=" + DEFAULT_N_COST);

        // Get all the flexCostList where nCost is less than UPDATED_N_COST
        defaultFlexCostShouldBeFound("nCost.lessThan=" + UPDATED_N_COST);
    }

    @Test
    @Transactional
    void getAllFlexCostsBynCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where nCost is greater than DEFAULT_N_COST
        defaultFlexCostShouldNotBeFound("nCost.greaterThan=" + DEFAULT_N_COST);

        // Get all the flexCostList where nCost is greater than SMALLER_N_COST
        defaultFlexCostShouldBeFound("nCost.greaterThan=" + SMALLER_N_COST);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostPrIsEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costPr equals to DEFAULT_COST_PR
        defaultFlexCostShouldBeFound("costPr.equals=" + DEFAULT_COST_PR);

        // Get all the flexCostList where costPr equals to UPDATED_COST_PR
        defaultFlexCostShouldNotBeFound("costPr.equals=" + UPDATED_COST_PR);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostPrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costPr not equals to DEFAULT_COST_PR
        defaultFlexCostShouldNotBeFound("costPr.notEquals=" + DEFAULT_COST_PR);

        // Get all the flexCostList where costPr not equals to UPDATED_COST_PR
        defaultFlexCostShouldBeFound("costPr.notEquals=" + UPDATED_COST_PR);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostPrIsInShouldWork() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costPr in DEFAULT_COST_PR or UPDATED_COST_PR
        defaultFlexCostShouldBeFound("costPr.in=" + DEFAULT_COST_PR + "," + UPDATED_COST_PR);

        // Get all the flexCostList where costPr equals to UPDATED_COST_PR
        defaultFlexCostShouldNotBeFound("costPr.in=" + UPDATED_COST_PR);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostPrIsNullOrNotNull() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costPr is not null
        defaultFlexCostShouldBeFound("costPr.specified=true");

        // Get all the flexCostList where costPr is null
        defaultFlexCostShouldNotBeFound("costPr.specified=false");
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostPrIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costPr is greater than or equal to DEFAULT_COST_PR
        defaultFlexCostShouldBeFound("costPr.greaterThanOrEqual=" + DEFAULT_COST_PR);

        // Get all the flexCostList where costPr is greater than or equal to UPDATED_COST_PR
        defaultFlexCostShouldNotBeFound("costPr.greaterThanOrEqual=" + UPDATED_COST_PR);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostPrIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costPr is less than or equal to DEFAULT_COST_PR
        defaultFlexCostShouldBeFound("costPr.lessThanOrEqual=" + DEFAULT_COST_PR);

        // Get all the flexCostList where costPr is less than or equal to SMALLER_COST_PR
        defaultFlexCostShouldNotBeFound("costPr.lessThanOrEqual=" + SMALLER_COST_PR);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostPrIsLessThanSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costPr is less than DEFAULT_COST_PR
        defaultFlexCostShouldNotBeFound("costPr.lessThan=" + DEFAULT_COST_PR);

        // Get all the flexCostList where costPr is less than UPDATED_COST_PR
        defaultFlexCostShouldBeFound("costPr.lessThan=" + UPDATED_COST_PR);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostPrIsGreaterThanSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costPr is greater than DEFAULT_COST_PR
        defaultFlexCostShouldNotBeFound("costPr.greaterThan=" + DEFAULT_COST_PR);

        // Get all the flexCostList where costPr is greater than SMALLER_COST_PR
        defaultFlexCostShouldBeFound("costPr.greaterThan=" + SMALLER_COST_PR);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostQrIsEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costQr equals to DEFAULT_COST_QR
        defaultFlexCostShouldBeFound("costQr.equals=" + DEFAULT_COST_QR);

        // Get all the flexCostList where costQr equals to UPDATED_COST_QR
        defaultFlexCostShouldNotBeFound("costQr.equals=" + UPDATED_COST_QR);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostQrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costQr not equals to DEFAULT_COST_QR
        defaultFlexCostShouldNotBeFound("costQr.notEquals=" + DEFAULT_COST_QR);

        // Get all the flexCostList where costQr not equals to UPDATED_COST_QR
        defaultFlexCostShouldBeFound("costQr.notEquals=" + UPDATED_COST_QR);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostQrIsInShouldWork() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costQr in DEFAULT_COST_QR or UPDATED_COST_QR
        defaultFlexCostShouldBeFound("costQr.in=" + DEFAULT_COST_QR + "," + UPDATED_COST_QR);

        // Get all the flexCostList where costQr equals to UPDATED_COST_QR
        defaultFlexCostShouldNotBeFound("costQr.in=" + UPDATED_COST_QR);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostQrIsNullOrNotNull() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costQr is not null
        defaultFlexCostShouldBeFound("costQr.specified=true");

        // Get all the flexCostList where costQr is null
        defaultFlexCostShouldNotBeFound("costQr.specified=false");
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostQrIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costQr is greater than or equal to DEFAULT_COST_QR
        defaultFlexCostShouldBeFound("costQr.greaterThanOrEqual=" + DEFAULT_COST_QR);

        // Get all the flexCostList where costQr is greater than or equal to UPDATED_COST_QR
        defaultFlexCostShouldNotBeFound("costQr.greaterThanOrEqual=" + UPDATED_COST_QR);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostQrIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costQr is less than or equal to DEFAULT_COST_QR
        defaultFlexCostShouldBeFound("costQr.lessThanOrEqual=" + DEFAULT_COST_QR);

        // Get all the flexCostList where costQr is less than or equal to SMALLER_COST_QR
        defaultFlexCostShouldNotBeFound("costQr.lessThanOrEqual=" + SMALLER_COST_QR);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostQrIsLessThanSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costQr is less than DEFAULT_COST_QR
        defaultFlexCostShouldNotBeFound("costQr.lessThan=" + DEFAULT_COST_QR);

        // Get all the flexCostList where costQr is less than UPDATED_COST_QR
        defaultFlexCostShouldBeFound("costQr.lessThan=" + UPDATED_COST_QR);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostQrIsGreaterThanSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costQr is greater than DEFAULT_COST_QR
        defaultFlexCostShouldNotBeFound("costQr.greaterThan=" + DEFAULT_COST_QR);

        // Get all the flexCostList where costQr is greater than SMALLER_COST_QR
        defaultFlexCostShouldBeFound("costQr.greaterThan=" + SMALLER_COST_QR);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostPfIsEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costPf equals to DEFAULT_COST_PF
        defaultFlexCostShouldBeFound("costPf.equals=" + DEFAULT_COST_PF);

        // Get all the flexCostList where costPf equals to UPDATED_COST_PF
        defaultFlexCostShouldNotBeFound("costPf.equals=" + UPDATED_COST_PF);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostPfIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costPf not equals to DEFAULT_COST_PF
        defaultFlexCostShouldNotBeFound("costPf.notEquals=" + DEFAULT_COST_PF);

        // Get all the flexCostList where costPf not equals to UPDATED_COST_PF
        defaultFlexCostShouldBeFound("costPf.notEquals=" + UPDATED_COST_PF);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostPfIsInShouldWork() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costPf in DEFAULT_COST_PF or UPDATED_COST_PF
        defaultFlexCostShouldBeFound("costPf.in=" + DEFAULT_COST_PF + "," + UPDATED_COST_PF);

        // Get all the flexCostList where costPf equals to UPDATED_COST_PF
        defaultFlexCostShouldNotBeFound("costPf.in=" + UPDATED_COST_PF);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostPfIsNullOrNotNull() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costPf is not null
        defaultFlexCostShouldBeFound("costPf.specified=true");

        // Get all the flexCostList where costPf is null
        defaultFlexCostShouldNotBeFound("costPf.specified=false");
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostPfContainsSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costPf contains DEFAULT_COST_PF
        defaultFlexCostShouldBeFound("costPf.contains=" + DEFAULT_COST_PF);

        // Get all the flexCostList where costPf contains UPDATED_COST_PF
        defaultFlexCostShouldNotBeFound("costPf.contains=" + UPDATED_COST_PF);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostPfNotContainsSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costPf does not contain DEFAULT_COST_PF
        defaultFlexCostShouldNotBeFound("costPf.doesNotContain=" + DEFAULT_COST_PF);

        // Get all the flexCostList where costPf does not contain UPDATED_COST_PF
        defaultFlexCostShouldBeFound("costPf.doesNotContain=" + UPDATED_COST_PF);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostQfIsEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costQf equals to DEFAULT_COST_QF
        defaultFlexCostShouldBeFound("costQf.equals=" + DEFAULT_COST_QF);

        // Get all the flexCostList where costQf equals to UPDATED_COST_QF
        defaultFlexCostShouldNotBeFound("costQf.equals=" + UPDATED_COST_QF);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostQfIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costQf not equals to DEFAULT_COST_QF
        defaultFlexCostShouldNotBeFound("costQf.notEquals=" + DEFAULT_COST_QF);

        // Get all the flexCostList where costQf not equals to UPDATED_COST_QF
        defaultFlexCostShouldBeFound("costQf.notEquals=" + UPDATED_COST_QF);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostQfIsInShouldWork() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costQf in DEFAULT_COST_QF or UPDATED_COST_QF
        defaultFlexCostShouldBeFound("costQf.in=" + DEFAULT_COST_QF + "," + UPDATED_COST_QF);

        // Get all the flexCostList where costQf equals to UPDATED_COST_QF
        defaultFlexCostShouldNotBeFound("costQf.in=" + UPDATED_COST_QF);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostQfIsNullOrNotNull() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costQf is not null
        defaultFlexCostShouldBeFound("costQf.specified=true");

        // Get all the flexCostList where costQf is null
        defaultFlexCostShouldNotBeFound("costQf.specified=false");
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostQfContainsSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costQf contains DEFAULT_COST_QF
        defaultFlexCostShouldBeFound("costQf.contains=" + DEFAULT_COST_QF);

        // Get all the flexCostList where costQf contains UPDATED_COST_QF
        defaultFlexCostShouldNotBeFound("costQf.contains=" + UPDATED_COST_QF);
    }

    @Test
    @Transactional
    void getAllFlexCostsByCostQfNotContainsSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        // Get all the flexCostList where costQf does not contain DEFAULT_COST_QF
        defaultFlexCostShouldNotBeFound("costQf.doesNotContain=" + DEFAULT_COST_QF);

        // Get all the flexCostList where costQf does not contain UPDATED_COST_QF
        defaultFlexCostShouldBeFound("costQf.doesNotContain=" + UPDATED_COST_QF);
    }

    @Test
    @Transactional
    void getAllFlexCostsByFlexProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);
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
        flexCost.setFlexProfile(flexProfile);
        flexCostRepository.saveAndFlush(flexCost);
        Long flexProfileId = flexProfile.getId();

        // Get all the flexCostList where flexProfile equals to flexProfileId
        defaultFlexCostShouldBeFound("flexProfileId.equals=" + flexProfileId);

        // Get all the flexCostList where flexProfile equals to (flexProfileId + 1)
        defaultFlexCostShouldNotBeFound("flexProfileId.equals=" + (flexProfileId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFlexCostShouldBeFound(String filter) throws Exception {
        restFlexCostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flexCost.getId().intValue())))
            .andExpect(jsonPath("$.[*].busNum").value(hasItem(DEFAULT_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].nCost").value(hasItem(DEFAULT_N_COST.intValue())))
            .andExpect(jsonPath("$.[*].costPr").value(hasItem(DEFAULT_COST_PR.doubleValue())))
            .andExpect(jsonPath("$.[*].costQr").value(hasItem(DEFAULT_COST_QR.doubleValue())))
            .andExpect(jsonPath("$.[*].costPf").value(hasItem(DEFAULT_COST_PF)))
            .andExpect(jsonPath("$.[*].costQf").value(hasItem(DEFAULT_COST_QF)));

        // Check, that the count call also returns 1
        restFlexCostMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFlexCostShouldNotBeFound(String filter) throws Exception {
        restFlexCostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFlexCostMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFlexCost() throws Exception {
        // Get the flexCost
        restFlexCostMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFlexCost() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        int databaseSizeBeforeUpdate = flexCostRepository.findAll().size();

        // Update the flexCost
        FlexCost updatedFlexCost = flexCostRepository.findById(flexCost.getId()).get();
        // Disconnect from session so that the updates on updatedFlexCost are not directly saved in db
        em.detach(updatedFlexCost);
        updatedFlexCost
            .busNum(UPDATED_BUS_NUM)
            .model(UPDATED_MODEL)
            .nCost(UPDATED_N_COST)
            .costPr(UPDATED_COST_PR)
            .costQr(UPDATED_COST_QR)
            .costPf(UPDATED_COST_PF)
            .costQf(UPDATED_COST_QF);
        FlexCostDTO flexCostDTO = flexCostMapper.toDto(updatedFlexCost);

        restFlexCostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flexCostDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexCostDTO))
            )
            .andExpect(status().isOk());

        // Validate the FlexCost in the database
        List<FlexCost> flexCostList = flexCostRepository.findAll();
        assertThat(flexCostList).hasSize(databaseSizeBeforeUpdate);
        FlexCost testFlexCost = flexCostList.get(flexCostList.size() - 1);
        assertThat(testFlexCost.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testFlexCost.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testFlexCost.getnCost()).isEqualTo(UPDATED_N_COST);
        assertThat(testFlexCost.getCostPr()).isEqualTo(UPDATED_COST_PR);
        assertThat(testFlexCost.getCostQr()).isEqualTo(UPDATED_COST_QR);
        assertThat(testFlexCost.getCostPf()).isEqualTo(UPDATED_COST_PF);
        assertThat(testFlexCost.getCostQf()).isEqualTo(UPDATED_COST_QF);
    }

    @Test
    @Transactional
    void putNonExistingFlexCost() throws Exception {
        int databaseSizeBeforeUpdate = flexCostRepository.findAll().size();
        flexCost.setId(count.incrementAndGet());

        // Create the FlexCost
        FlexCostDTO flexCostDTO = flexCostMapper.toDto(flexCost);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlexCostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flexCostDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexCostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexCost in the database
        List<FlexCost> flexCostList = flexCostRepository.findAll();
        assertThat(flexCostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFlexCost() throws Exception {
        int databaseSizeBeforeUpdate = flexCostRepository.findAll().size();
        flexCost.setId(count.incrementAndGet());

        // Create the FlexCost
        FlexCostDTO flexCostDTO = flexCostMapper.toDto(flexCost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexCostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexCostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexCost in the database
        List<FlexCost> flexCostList = flexCostRepository.findAll();
        assertThat(flexCostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFlexCost() throws Exception {
        int databaseSizeBeforeUpdate = flexCostRepository.findAll().size();
        flexCost.setId(count.incrementAndGet());

        // Create the FlexCost
        FlexCostDTO flexCostDTO = flexCostMapper.toDto(flexCost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexCostMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexCostDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlexCost in the database
        List<FlexCost> flexCostList = flexCostRepository.findAll();
        assertThat(flexCostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFlexCostWithPatch() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        int databaseSizeBeforeUpdate = flexCostRepository.findAll().size();

        // Update the flexCost using partial update
        FlexCost partialUpdatedFlexCost = new FlexCost();
        partialUpdatedFlexCost.setId(flexCost.getId());

        partialUpdatedFlexCost.busNum(UPDATED_BUS_NUM).model(UPDATED_MODEL).nCost(UPDATED_N_COST).costPf(UPDATED_COST_PF);

        restFlexCostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlexCost.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlexCost))
            )
            .andExpect(status().isOk());

        // Validate the FlexCost in the database
        List<FlexCost> flexCostList = flexCostRepository.findAll();
        assertThat(flexCostList).hasSize(databaseSizeBeforeUpdate);
        FlexCost testFlexCost = flexCostList.get(flexCostList.size() - 1);
        assertThat(testFlexCost.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testFlexCost.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testFlexCost.getnCost()).isEqualTo(UPDATED_N_COST);
        assertThat(testFlexCost.getCostPr()).isEqualTo(DEFAULT_COST_PR);
        assertThat(testFlexCost.getCostQr()).isEqualTo(DEFAULT_COST_QR);
        assertThat(testFlexCost.getCostPf()).isEqualTo(UPDATED_COST_PF);
        assertThat(testFlexCost.getCostQf()).isEqualTo(DEFAULT_COST_QF);
    }

    @Test
    @Transactional
    void fullUpdateFlexCostWithPatch() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        int databaseSizeBeforeUpdate = flexCostRepository.findAll().size();

        // Update the flexCost using partial update
        FlexCost partialUpdatedFlexCost = new FlexCost();
        partialUpdatedFlexCost.setId(flexCost.getId());

        partialUpdatedFlexCost
            .busNum(UPDATED_BUS_NUM)
            .model(UPDATED_MODEL)
            .nCost(UPDATED_N_COST)
            .costPr(UPDATED_COST_PR)
            .costQr(UPDATED_COST_QR)
            .costPf(UPDATED_COST_PF)
            .costQf(UPDATED_COST_QF);

        restFlexCostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlexCost.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlexCost))
            )
            .andExpect(status().isOk());

        // Validate the FlexCost in the database
        List<FlexCost> flexCostList = flexCostRepository.findAll();
        assertThat(flexCostList).hasSize(databaseSizeBeforeUpdate);
        FlexCost testFlexCost = flexCostList.get(flexCostList.size() - 1);
        assertThat(testFlexCost.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testFlexCost.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testFlexCost.getnCost()).isEqualTo(UPDATED_N_COST);
        assertThat(testFlexCost.getCostPr()).isEqualTo(UPDATED_COST_PR);
        assertThat(testFlexCost.getCostQr()).isEqualTo(UPDATED_COST_QR);
        assertThat(testFlexCost.getCostPf()).isEqualTo(UPDATED_COST_PF);
        assertThat(testFlexCost.getCostQf()).isEqualTo(UPDATED_COST_QF);
    }

    @Test
    @Transactional
    void patchNonExistingFlexCost() throws Exception {
        int databaseSizeBeforeUpdate = flexCostRepository.findAll().size();
        flexCost.setId(count.incrementAndGet());

        // Create the FlexCost
        FlexCostDTO flexCostDTO = flexCostMapper.toDto(flexCost);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlexCostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, flexCostDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flexCostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexCost in the database
        List<FlexCost> flexCostList = flexCostRepository.findAll();
        assertThat(flexCostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFlexCost() throws Exception {
        int databaseSizeBeforeUpdate = flexCostRepository.findAll().size();
        flexCost.setId(count.incrementAndGet());

        // Create the FlexCost
        FlexCostDTO flexCostDTO = flexCostMapper.toDto(flexCost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexCostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flexCostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexCost in the database
        List<FlexCost> flexCostList = flexCostRepository.findAll();
        assertThat(flexCostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFlexCost() throws Exception {
        int databaseSizeBeforeUpdate = flexCostRepository.findAll().size();
        flexCost.setId(count.incrementAndGet());

        // Create the FlexCost
        FlexCostDTO flexCostDTO = flexCostMapper.toDto(flexCost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexCostMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flexCostDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlexCost in the database
        List<FlexCost> flexCostList = flexCostRepository.findAll();
        assertThat(flexCostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFlexCost() throws Exception {
        // Initialize the database
        flexCostRepository.saveAndFlush(flexCost);

        int databaseSizeBeforeDelete = flexCostRepository.findAll().size();

        // Delete the flexCost
        restFlexCostMockMvc
            .perform(delete(ENTITY_API_URL_ID, flexCost.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FlexCost> flexCostList = flexCostRepository.findAll();
        assertThat(flexCostList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
