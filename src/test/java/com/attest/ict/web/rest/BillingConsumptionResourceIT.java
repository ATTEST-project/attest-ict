package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.BillingConsumption;
import com.attest.ict.domain.Network;
import com.attest.ict.repository.BillingConsumptionRepository;
import com.attest.ict.service.criteria.BillingConsumptionCriteria;
import com.attest.ict.service.dto.BillingConsumptionDTO;
import com.attest.ict.service.mapper.BillingConsumptionMapper;
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
 * Integration tests for the {@link BillingConsumptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BillingConsumptionResourceIT {

    private static final Long DEFAULT_BUS_NUM = 1L;
    private static final Long UPDATED_BUS_NUM = 2L;
    private static final Long SMALLER_BUS_NUM = 1L - 1L;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_TOTAL_ENERGY_CONSUMPTION = 1L;
    private static final Long UPDATED_TOTAL_ENERGY_CONSUMPTION = 2L;
    private static final Long SMALLER_TOTAL_ENERGY_CONSUMPTION = 1L - 1L;

    private static final String DEFAULT_UNIT_OF_MEASURE = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_OF_MEASURE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/billing-consumptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BillingConsumptionRepository billingConsumptionRepository;

    @Autowired
    private BillingConsumptionMapper billingConsumptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillingConsumptionMockMvc;

    private BillingConsumption billingConsumption;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillingConsumption createEntity(EntityManager em) {
        BillingConsumption billingConsumption = new BillingConsumption()
            .busNum(DEFAULT_BUS_NUM)
            .type(DEFAULT_TYPE)
            .totalEnergyConsumption(DEFAULT_TOTAL_ENERGY_CONSUMPTION)
            .unitOfMeasure(DEFAULT_UNIT_OF_MEASURE);
        return billingConsumption;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillingConsumption createUpdatedEntity(EntityManager em) {
        BillingConsumption billingConsumption = new BillingConsumption()
            .busNum(UPDATED_BUS_NUM)
            .type(UPDATED_TYPE)
            .totalEnergyConsumption(UPDATED_TOTAL_ENERGY_CONSUMPTION)
            .unitOfMeasure(UPDATED_UNIT_OF_MEASURE);
        return billingConsumption;
    }

    @BeforeEach
    public void initTest() {
        billingConsumption = createEntity(em);
    }

    @Test
    @Transactional
    void createBillingConsumption() throws Exception {
        int databaseSizeBeforeCreate = billingConsumptionRepository.findAll().size();
        // Create the BillingConsumption
        BillingConsumptionDTO billingConsumptionDTO = billingConsumptionMapper.toDto(billingConsumption);
        restBillingConsumptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billingConsumptionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BillingConsumption in the database
        List<BillingConsumption> billingConsumptionList = billingConsumptionRepository.findAll();
        assertThat(billingConsumptionList).hasSize(databaseSizeBeforeCreate + 1);
        BillingConsumption testBillingConsumption = billingConsumptionList.get(billingConsumptionList.size() - 1);
        assertThat(testBillingConsumption.getBusNum()).isEqualTo(DEFAULT_BUS_NUM);
        assertThat(testBillingConsumption.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testBillingConsumption.getTotalEnergyConsumption()).isEqualTo(DEFAULT_TOTAL_ENERGY_CONSUMPTION);
        assertThat(testBillingConsumption.getUnitOfMeasure()).isEqualTo(DEFAULT_UNIT_OF_MEASURE);
    }

    @Test
    @Transactional
    void createBillingConsumptionWithExistingId() throws Exception {
        // Create the BillingConsumption with an existing ID
        billingConsumption.setId(1L);
        BillingConsumptionDTO billingConsumptionDTO = billingConsumptionMapper.toDto(billingConsumption);

        int databaseSizeBeforeCreate = billingConsumptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillingConsumptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billingConsumptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillingConsumption in the database
        List<BillingConsumption> billingConsumptionList = billingConsumptionRepository.findAll();
        assertThat(billingConsumptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBillingConsumptions() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList
        restBillingConsumptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billingConsumption.getId().intValue())))
            .andExpect(jsonPath("$.[*].busNum").value(hasItem(DEFAULT_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].totalEnergyConsumption").value(hasItem(DEFAULT_TOTAL_ENERGY_CONSUMPTION.intValue())))
            .andExpect(jsonPath("$.[*].unitOfMeasure").value(hasItem(DEFAULT_UNIT_OF_MEASURE)));
    }

    @Test
    @Transactional
    void getBillingConsumption() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get the billingConsumption
        restBillingConsumptionMockMvc
            .perform(get(ENTITY_API_URL_ID, billingConsumption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(billingConsumption.getId().intValue()))
            .andExpect(jsonPath("$.busNum").value(DEFAULT_BUS_NUM.intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.totalEnergyConsumption").value(DEFAULT_TOTAL_ENERGY_CONSUMPTION.intValue()))
            .andExpect(jsonPath("$.unitOfMeasure").value(DEFAULT_UNIT_OF_MEASURE));
    }

    @Test
    @Transactional
    void getBillingConsumptionsByIdFiltering() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        Long id = billingConsumption.getId();

        defaultBillingConsumptionShouldBeFound("id.equals=" + id);
        defaultBillingConsumptionShouldNotBeFound("id.notEquals=" + id);

        defaultBillingConsumptionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBillingConsumptionShouldNotBeFound("id.greaterThan=" + id);

        defaultBillingConsumptionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBillingConsumptionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByBusNumIsEqualToSomething() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where busNum equals to DEFAULT_BUS_NUM
        defaultBillingConsumptionShouldBeFound("busNum.equals=" + DEFAULT_BUS_NUM);

        // Get all the billingConsumptionList where busNum equals to UPDATED_BUS_NUM
        defaultBillingConsumptionShouldNotBeFound("busNum.equals=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByBusNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where busNum not equals to DEFAULT_BUS_NUM
        defaultBillingConsumptionShouldNotBeFound("busNum.notEquals=" + DEFAULT_BUS_NUM);

        // Get all the billingConsumptionList where busNum not equals to UPDATED_BUS_NUM
        defaultBillingConsumptionShouldBeFound("busNum.notEquals=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByBusNumIsInShouldWork() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where busNum in DEFAULT_BUS_NUM or UPDATED_BUS_NUM
        defaultBillingConsumptionShouldBeFound("busNum.in=" + DEFAULT_BUS_NUM + "," + UPDATED_BUS_NUM);

        // Get all the billingConsumptionList where busNum equals to UPDATED_BUS_NUM
        defaultBillingConsumptionShouldNotBeFound("busNum.in=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByBusNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where busNum is not null
        defaultBillingConsumptionShouldBeFound("busNum.specified=true");

        // Get all the billingConsumptionList where busNum is null
        defaultBillingConsumptionShouldNotBeFound("busNum.specified=false");
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByBusNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where busNum is greater than or equal to DEFAULT_BUS_NUM
        defaultBillingConsumptionShouldBeFound("busNum.greaterThanOrEqual=" + DEFAULT_BUS_NUM);

        // Get all the billingConsumptionList where busNum is greater than or equal to UPDATED_BUS_NUM
        defaultBillingConsumptionShouldNotBeFound("busNum.greaterThanOrEqual=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByBusNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where busNum is less than or equal to DEFAULT_BUS_NUM
        defaultBillingConsumptionShouldBeFound("busNum.lessThanOrEqual=" + DEFAULT_BUS_NUM);

        // Get all the billingConsumptionList where busNum is less than or equal to SMALLER_BUS_NUM
        defaultBillingConsumptionShouldNotBeFound("busNum.lessThanOrEqual=" + SMALLER_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByBusNumIsLessThanSomething() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where busNum is less than DEFAULT_BUS_NUM
        defaultBillingConsumptionShouldNotBeFound("busNum.lessThan=" + DEFAULT_BUS_NUM);

        // Get all the billingConsumptionList where busNum is less than UPDATED_BUS_NUM
        defaultBillingConsumptionShouldBeFound("busNum.lessThan=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByBusNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where busNum is greater than DEFAULT_BUS_NUM
        defaultBillingConsumptionShouldNotBeFound("busNum.greaterThan=" + DEFAULT_BUS_NUM);

        // Get all the billingConsumptionList where busNum is greater than SMALLER_BUS_NUM
        defaultBillingConsumptionShouldBeFound("busNum.greaterThan=" + SMALLER_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where type equals to DEFAULT_TYPE
        defaultBillingConsumptionShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the billingConsumptionList where type equals to UPDATED_TYPE
        defaultBillingConsumptionShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where type not equals to DEFAULT_TYPE
        defaultBillingConsumptionShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the billingConsumptionList where type not equals to UPDATED_TYPE
        defaultBillingConsumptionShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultBillingConsumptionShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the billingConsumptionList where type equals to UPDATED_TYPE
        defaultBillingConsumptionShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where type is not null
        defaultBillingConsumptionShouldBeFound("type.specified=true");

        // Get all the billingConsumptionList where type is null
        defaultBillingConsumptionShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByTypeContainsSomething() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where type contains DEFAULT_TYPE
        defaultBillingConsumptionShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the billingConsumptionList where type contains UPDATED_TYPE
        defaultBillingConsumptionShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where type does not contain DEFAULT_TYPE
        defaultBillingConsumptionShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the billingConsumptionList where type does not contain UPDATED_TYPE
        defaultBillingConsumptionShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByTotalEnergyConsumptionIsEqualToSomething() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where totalEnergyConsumption equals to DEFAULT_TOTAL_ENERGY_CONSUMPTION
        defaultBillingConsumptionShouldBeFound("totalEnergyConsumption.equals=" + DEFAULT_TOTAL_ENERGY_CONSUMPTION);

        // Get all the billingConsumptionList where totalEnergyConsumption equals to UPDATED_TOTAL_ENERGY_CONSUMPTION
        defaultBillingConsumptionShouldNotBeFound("totalEnergyConsumption.equals=" + UPDATED_TOTAL_ENERGY_CONSUMPTION);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByTotalEnergyConsumptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where totalEnergyConsumption not equals to DEFAULT_TOTAL_ENERGY_CONSUMPTION
        defaultBillingConsumptionShouldNotBeFound("totalEnergyConsumption.notEquals=" + DEFAULT_TOTAL_ENERGY_CONSUMPTION);

        // Get all the billingConsumptionList where totalEnergyConsumption not equals to UPDATED_TOTAL_ENERGY_CONSUMPTION
        defaultBillingConsumptionShouldBeFound("totalEnergyConsumption.notEquals=" + UPDATED_TOTAL_ENERGY_CONSUMPTION);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByTotalEnergyConsumptionIsInShouldWork() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where totalEnergyConsumption in DEFAULT_TOTAL_ENERGY_CONSUMPTION or UPDATED_TOTAL_ENERGY_CONSUMPTION
        defaultBillingConsumptionShouldBeFound(
            "totalEnergyConsumption.in=" + DEFAULT_TOTAL_ENERGY_CONSUMPTION + "," + UPDATED_TOTAL_ENERGY_CONSUMPTION
        );

        // Get all the billingConsumptionList where totalEnergyConsumption equals to UPDATED_TOTAL_ENERGY_CONSUMPTION
        defaultBillingConsumptionShouldNotBeFound("totalEnergyConsumption.in=" + UPDATED_TOTAL_ENERGY_CONSUMPTION);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByTotalEnergyConsumptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where totalEnergyConsumption is not null
        defaultBillingConsumptionShouldBeFound("totalEnergyConsumption.specified=true");

        // Get all the billingConsumptionList where totalEnergyConsumption is null
        defaultBillingConsumptionShouldNotBeFound("totalEnergyConsumption.specified=false");
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByTotalEnergyConsumptionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where totalEnergyConsumption is greater than or equal to DEFAULT_TOTAL_ENERGY_CONSUMPTION
        defaultBillingConsumptionShouldBeFound("totalEnergyConsumption.greaterThanOrEqual=" + DEFAULT_TOTAL_ENERGY_CONSUMPTION);

        // Get all the billingConsumptionList where totalEnergyConsumption is greater than or equal to UPDATED_TOTAL_ENERGY_CONSUMPTION
        defaultBillingConsumptionShouldNotBeFound("totalEnergyConsumption.greaterThanOrEqual=" + UPDATED_TOTAL_ENERGY_CONSUMPTION);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByTotalEnergyConsumptionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where totalEnergyConsumption is less than or equal to DEFAULT_TOTAL_ENERGY_CONSUMPTION
        defaultBillingConsumptionShouldBeFound("totalEnergyConsumption.lessThanOrEqual=" + DEFAULT_TOTAL_ENERGY_CONSUMPTION);

        // Get all the billingConsumptionList where totalEnergyConsumption is less than or equal to SMALLER_TOTAL_ENERGY_CONSUMPTION
        defaultBillingConsumptionShouldNotBeFound("totalEnergyConsumption.lessThanOrEqual=" + SMALLER_TOTAL_ENERGY_CONSUMPTION);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByTotalEnergyConsumptionIsLessThanSomething() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where totalEnergyConsumption is less than DEFAULT_TOTAL_ENERGY_CONSUMPTION
        defaultBillingConsumptionShouldNotBeFound("totalEnergyConsumption.lessThan=" + DEFAULT_TOTAL_ENERGY_CONSUMPTION);

        // Get all the billingConsumptionList where totalEnergyConsumption is less than UPDATED_TOTAL_ENERGY_CONSUMPTION
        defaultBillingConsumptionShouldBeFound("totalEnergyConsumption.lessThan=" + UPDATED_TOTAL_ENERGY_CONSUMPTION);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByTotalEnergyConsumptionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where totalEnergyConsumption is greater than DEFAULT_TOTAL_ENERGY_CONSUMPTION
        defaultBillingConsumptionShouldNotBeFound("totalEnergyConsumption.greaterThan=" + DEFAULT_TOTAL_ENERGY_CONSUMPTION);

        // Get all the billingConsumptionList where totalEnergyConsumption is greater than SMALLER_TOTAL_ENERGY_CONSUMPTION
        defaultBillingConsumptionShouldBeFound("totalEnergyConsumption.greaterThan=" + SMALLER_TOTAL_ENERGY_CONSUMPTION);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByUnitOfMeasureIsEqualToSomething() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where unitOfMeasure equals to DEFAULT_UNIT_OF_MEASURE
        defaultBillingConsumptionShouldBeFound("unitOfMeasure.equals=" + DEFAULT_UNIT_OF_MEASURE);

        // Get all the billingConsumptionList where unitOfMeasure equals to UPDATED_UNIT_OF_MEASURE
        defaultBillingConsumptionShouldNotBeFound("unitOfMeasure.equals=" + UPDATED_UNIT_OF_MEASURE);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByUnitOfMeasureIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where unitOfMeasure not equals to DEFAULT_UNIT_OF_MEASURE
        defaultBillingConsumptionShouldNotBeFound("unitOfMeasure.notEquals=" + DEFAULT_UNIT_OF_MEASURE);

        // Get all the billingConsumptionList where unitOfMeasure not equals to UPDATED_UNIT_OF_MEASURE
        defaultBillingConsumptionShouldBeFound("unitOfMeasure.notEquals=" + UPDATED_UNIT_OF_MEASURE);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByUnitOfMeasureIsInShouldWork() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where unitOfMeasure in DEFAULT_UNIT_OF_MEASURE or UPDATED_UNIT_OF_MEASURE
        defaultBillingConsumptionShouldBeFound("unitOfMeasure.in=" + DEFAULT_UNIT_OF_MEASURE + "," + UPDATED_UNIT_OF_MEASURE);

        // Get all the billingConsumptionList where unitOfMeasure equals to UPDATED_UNIT_OF_MEASURE
        defaultBillingConsumptionShouldNotBeFound("unitOfMeasure.in=" + UPDATED_UNIT_OF_MEASURE);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByUnitOfMeasureIsNullOrNotNull() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where unitOfMeasure is not null
        defaultBillingConsumptionShouldBeFound("unitOfMeasure.specified=true");

        // Get all the billingConsumptionList where unitOfMeasure is null
        defaultBillingConsumptionShouldNotBeFound("unitOfMeasure.specified=false");
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByUnitOfMeasureContainsSomething() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where unitOfMeasure contains DEFAULT_UNIT_OF_MEASURE
        defaultBillingConsumptionShouldBeFound("unitOfMeasure.contains=" + DEFAULT_UNIT_OF_MEASURE);

        // Get all the billingConsumptionList where unitOfMeasure contains UPDATED_UNIT_OF_MEASURE
        defaultBillingConsumptionShouldNotBeFound("unitOfMeasure.contains=" + UPDATED_UNIT_OF_MEASURE);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByUnitOfMeasureNotContainsSomething() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        // Get all the billingConsumptionList where unitOfMeasure does not contain DEFAULT_UNIT_OF_MEASURE
        defaultBillingConsumptionShouldNotBeFound("unitOfMeasure.doesNotContain=" + DEFAULT_UNIT_OF_MEASURE);

        // Get all the billingConsumptionList where unitOfMeasure does not contain UPDATED_UNIT_OF_MEASURE
        defaultBillingConsumptionShouldBeFound("unitOfMeasure.doesNotContain=" + UPDATED_UNIT_OF_MEASURE);
    }

    @Test
    @Transactional
    void getAllBillingConsumptionsByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);
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
        billingConsumption.setNetwork(network);
        billingConsumptionRepository.saveAndFlush(billingConsumption);
        Long networkId = network.getId();

        // Get all the billingConsumptionList where network equals to networkId
        defaultBillingConsumptionShouldBeFound("networkId.equals=" + networkId);

        // Get all the billingConsumptionList where network equals to (networkId + 1)
        defaultBillingConsumptionShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBillingConsumptionShouldBeFound(String filter) throws Exception {
        restBillingConsumptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billingConsumption.getId().intValue())))
            .andExpect(jsonPath("$.[*].busNum").value(hasItem(DEFAULT_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].totalEnergyConsumption").value(hasItem(DEFAULT_TOTAL_ENERGY_CONSUMPTION.intValue())))
            .andExpect(jsonPath("$.[*].unitOfMeasure").value(hasItem(DEFAULT_UNIT_OF_MEASURE)));

        // Check, that the count call also returns 1
        restBillingConsumptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBillingConsumptionShouldNotBeFound(String filter) throws Exception {
        restBillingConsumptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBillingConsumptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBillingConsumption() throws Exception {
        // Get the billingConsumption
        restBillingConsumptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBillingConsumption() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        int databaseSizeBeforeUpdate = billingConsumptionRepository.findAll().size();

        // Update the billingConsumption
        BillingConsumption updatedBillingConsumption = billingConsumptionRepository.findById(billingConsumption.getId()).get();
        // Disconnect from session so that the updates on updatedBillingConsumption are not directly saved in db
        em.detach(updatedBillingConsumption);
        updatedBillingConsumption
            .busNum(UPDATED_BUS_NUM)
            .type(UPDATED_TYPE)
            .totalEnergyConsumption(UPDATED_TOTAL_ENERGY_CONSUMPTION)
            .unitOfMeasure(UPDATED_UNIT_OF_MEASURE);
        BillingConsumptionDTO billingConsumptionDTO = billingConsumptionMapper.toDto(updatedBillingConsumption);

        restBillingConsumptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billingConsumptionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billingConsumptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the BillingConsumption in the database
        List<BillingConsumption> billingConsumptionList = billingConsumptionRepository.findAll();
        assertThat(billingConsumptionList).hasSize(databaseSizeBeforeUpdate);
        BillingConsumption testBillingConsumption = billingConsumptionList.get(billingConsumptionList.size() - 1);
        assertThat(testBillingConsumption.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testBillingConsumption.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBillingConsumption.getTotalEnergyConsumption()).isEqualTo(UPDATED_TOTAL_ENERGY_CONSUMPTION);
        assertThat(testBillingConsumption.getUnitOfMeasure()).isEqualTo(UPDATED_UNIT_OF_MEASURE);
    }

    @Test
    @Transactional
    void putNonExistingBillingConsumption() throws Exception {
        int databaseSizeBeforeUpdate = billingConsumptionRepository.findAll().size();
        billingConsumption.setId(count.incrementAndGet());

        // Create the BillingConsumption
        BillingConsumptionDTO billingConsumptionDTO = billingConsumptionMapper.toDto(billingConsumption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillingConsumptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billingConsumptionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billingConsumptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillingConsumption in the database
        List<BillingConsumption> billingConsumptionList = billingConsumptionRepository.findAll();
        assertThat(billingConsumptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBillingConsumption() throws Exception {
        int databaseSizeBeforeUpdate = billingConsumptionRepository.findAll().size();
        billingConsumption.setId(count.incrementAndGet());

        // Create the BillingConsumption
        BillingConsumptionDTO billingConsumptionDTO = billingConsumptionMapper.toDto(billingConsumption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillingConsumptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billingConsumptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillingConsumption in the database
        List<BillingConsumption> billingConsumptionList = billingConsumptionRepository.findAll();
        assertThat(billingConsumptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBillingConsumption() throws Exception {
        int databaseSizeBeforeUpdate = billingConsumptionRepository.findAll().size();
        billingConsumption.setId(count.incrementAndGet());

        // Create the BillingConsumption
        BillingConsumptionDTO billingConsumptionDTO = billingConsumptionMapper.toDto(billingConsumption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillingConsumptionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billingConsumptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillingConsumption in the database
        List<BillingConsumption> billingConsumptionList = billingConsumptionRepository.findAll();
        assertThat(billingConsumptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBillingConsumptionWithPatch() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        int databaseSizeBeforeUpdate = billingConsumptionRepository.findAll().size();

        // Update the billingConsumption using partial update
        BillingConsumption partialUpdatedBillingConsumption = new BillingConsumption();
        partialUpdatedBillingConsumption.setId(billingConsumption.getId());

        partialUpdatedBillingConsumption.totalEnergyConsumption(UPDATED_TOTAL_ENERGY_CONSUMPTION);

        restBillingConsumptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillingConsumption.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBillingConsumption))
            )
            .andExpect(status().isOk());

        // Validate the BillingConsumption in the database
        List<BillingConsumption> billingConsumptionList = billingConsumptionRepository.findAll();
        assertThat(billingConsumptionList).hasSize(databaseSizeBeforeUpdate);
        BillingConsumption testBillingConsumption = billingConsumptionList.get(billingConsumptionList.size() - 1);
        assertThat(testBillingConsumption.getBusNum()).isEqualTo(DEFAULT_BUS_NUM);
        assertThat(testBillingConsumption.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testBillingConsumption.getTotalEnergyConsumption()).isEqualTo(UPDATED_TOTAL_ENERGY_CONSUMPTION);
        assertThat(testBillingConsumption.getUnitOfMeasure()).isEqualTo(DEFAULT_UNIT_OF_MEASURE);
    }

    @Test
    @Transactional
    void fullUpdateBillingConsumptionWithPatch() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        int databaseSizeBeforeUpdate = billingConsumptionRepository.findAll().size();

        // Update the billingConsumption using partial update
        BillingConsumption partialUpdatedBillingConsumption = new BillingConsumption();
        partialUpdatedBillingConsumption.setId(billingConsumption.getId());

        partialUpdatedBillingConsumption
            .busNum(UPDATED_BUS_NUM)
            .type(UPDATED_TYPE)
            .totalEnergyConsumption(UPDATED_TOTAL_ENERGY_CONSUMPTION)
            .unitOfMeasure(UPDATED_UNIT_OF_MEASURE);

        restBillingConsumptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillingConsumption.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBillingConsumption))
            )
            .andExpect(status().isOk());

        // Validate the BillingConsumption in the database
        List<BillingConsumption> billingConsumptionList = billingConsumptionRepository.findAll();
        assertThat(billingConsumptionList).hasSize(databaseSizeBeforeUpdate);
        BillingConsumption testBillingConsumption = billingConsumptionList.get(billingConsumptionList.size() - 1);
        assertThat(testBillingConsumption.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testBillingConsumption.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBillingConsumption.getTotalEnergyConsumption()).isEqualTo(UPDATED_TOTAL_ENERGY_CONSUMPTION);
        assertThat(testBillingConsumption.getUnitOfMeasure()).isEqualTo(UPDATED_UNIT_OF_MEASURE);
    }

    @Test
    @Transactional
    void patchNonExistingBillingConsumption() throws Exception {
        int databaseSizeBeforeUpdate = billingConsumptionRepository.findAll().size();
        billingConsumption.setId(count.incrementAndGet());

        // Create the BillingConsumption
        BillingConsumptionDTO billingConsumptionDTO = billingConsumptionMapper.toDto(billingConsumption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillingConsumptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, billingConsumptionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billingConsumptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillingConsumption in the database
        List<BillingConsumption> billingConsumptionList = billingConsumptionRepository.findAll();
        assertThat(billingConsumptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBillingConsumption() throws Exception {
        int databaseSizeBeforeUpdate = billingConsumptionRepository.findAll().size();
        billingConsumption.setId(count.incrementAndGet());

        // Create the BillingConsumption
        BillingConsumptionDTO billingConsumptionDTO = billingConsumptionMapper.toDto(billingConsumption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillingConsumptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billingConsumptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillingConsumption in the database
        List<BillingConsumption> billingConsumptionList = billingConsumptionRepository.findAll();
        assertThat(billingConsumptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBillingConsumption() throws Exception {
        int databaseSizeBeforeUpdate = billingConsumptionRepository.findAll().size();
        billingConsumption.setId(count.incrementAndGet());

        // Create the BillingConsumption
        BillingConsumptionDTO billingConsumptionDTO = billingConsumptionMapper.toDto(billingConsumption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillingConsumptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billingConsumptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillingConsumption in the database
        List<BillingConsumption> billingConsumptionList = billingConsumptionRepository.findAll();
        assertThat(billingConsumptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBillingConsumption() throws Exception {
        // Initialize the database
        billingConsumptionRepository.saveAndFlush(billingConsumption);

        int databaseSizeBeforeDelete = billingConsumptionRepository.findAll().size();

        // Delete the billingConsumption
        restBillingConsumptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, billingConsumption.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BillingConsumption> billingConsumptionList = billingConsumptionRepository.findAll();
        assertThat(billingConsumptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
