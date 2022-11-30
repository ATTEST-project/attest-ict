package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.AssetTransformer;
import com.attest.ict.domain.Network;
import com.attest.ict.repository.AssetTransformerRepository;
import com.attest.ict.service.criteria.AssetTransformerCriteria;
import com.attest.ict.service.dto.AssetTransformerDTO;
import com.attest.ict.service.mapper.AssetTransformerMapper;
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
 * Integration tests for the {@link AssetTransformerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AssetTransformerResourceIT {

    private static final Long DEFAULT_BUS_NUM = 1L;
    private static final Long UPDATED_BUS_NUM = 2L;
    private static final Long SMALLER_BUS_NUM = 1L - 1L;

    private static final String DEFAULT_VOLTAGE_RATIO = "AAAAAAAAAA";
    private static final String UPDATED_VOLTAGE_RATIO = "BBBBBBBBBB";

    private static final String DEFAULT_INSULATION_MEDIUM = "AAAAAAAAAA";
    private static final String UPDATED_INSULATION_MEDIUM = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_INDOOR_OUTDOOR = "AAAAAAAAAA";
    private static final String UPDATED_INDOOR_OUTDOOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANNUAL_MAX_LOAD_KVA = 1;
    private static final Integer UPDATED_ANNUAL_MAX_LOAD_KVA = 2;
    private static final Integer SMALLER_ANNUAL_MAX_LOAD_KVA = 1 - 1;

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;
    private static final Integer SMALLER_AGE = 1 - 1;

    private static final String DEFAULT_EXTERNAL_CONDITION = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_CONDITION = "BBBBBBBBBB";

    private static final Integer DEFAULT_RATING_KVA = 1;
    private static final Integer UPDATED_RATING_KVA = 2;
    private static final Integer SMALLER_RATING_KVA = 1 - 1;

    private static final Integer DEFAULT_NUM_CONNECTED_CUSTOMERS = 1;
    private static final Integer UPDATED_NUM_CONNECTED_CUSTOMERS = 2;
    private static final Integer SMALLER_NUM_CONNECTED_CUSTOMERS = 1 - 1;

    private static final Integer DEFAULT_NUM_SENSITIVE_CUSTOMERS = 1;
    private static final Integer UPDATED_NUM_SENSITIVE_CUSTOMERS = 2;
    private static final Integer SMALLER_NUM_SENSITIVE_CUSTOMERS = 1 - 1;

    private static final String DEFAULT_BACKUP_SUPPLY = "AAAAAAAAAA";
    private static final String UPDATED_BACKUP_SUPPLY = "BBBBBBBBBB";

    private static final Long DEFAULT_COST_OF_FAILURE_EURO = 1L;
    private static final Long UPDATED_COST_OF_FAILURE_EURO = 2L;
    private static final Long SMALLER_COST_OF_FAILURE_EURO = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/asset-transformers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssetTransformerRepository assetTransformerRepository;

    @Autowired
    private AssetTransformerMapper assetTransformerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetTransformerMockMvc;

    private AssetTransformer assetTransformer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetTransformer createEntity(EntityManager em) {
        AssetTransformer assetTransformer = new AssetTransformer()
            .busNum(DEFAULT_BUS_NUM)
            .voltageRatio(DEFAULT_VOLTAGE_RATIO)
            .insulationMedium(DEFAULT_INSULATION_MEDIUM)
            .type(DEFAULT_TYPE)
            .indoorOutdoor(DEFAULT_INDOOR_OUTDOOR)
            .annualMaxLoadKva(DEFAULT_ANNUAL_MAX_LOAD_KVA)
            .age(DEFAULT_AGE)
            .externalCondition(DEFAULT_EXTERNAL_CONDITION)
            .ratingKva(DEFAULT_RATING_KVA)
            .numConnectedCustomers(DEFAULT_NUM_CONNECTED_CUSTOMERS)
            .numSensitiveCustomers(DEFAULT_NUM_SENSITIVE_CUSTOMERS)
            .backupSupply(DEFAULT_BACKUP_SUPPLY)
            .costOfFailureEuro(DEFAULT_COST_OF_FAILURE_EURO);
        return assetTransformer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetTransformer createUpdatedEntity(EntityManager em) {
        AssetTransformer assetTransformer = new AssetTransformer()
            .busNum(UPDATED_BUS_NUM)
            .voltageRatio(UPDATED_VOLTAGE_RATIO)
            .insulationMedium(UPDATED_INSULATION_MEDIUM)
            .type(UPDATED_TYPE)
            .indoorOutdoor(UPDATED_INDOOR_OUTDOOR)
            .annualMaxLoadKva(UPDATED_ANNUAL_MAX_LOAD_KVA)
            .age(UPDATED_AGE)
            .externalCondition(UPDATED_EXTERNAL_CONDITION)
            .ratingKva(UPDATED_RATING_KVA)
            .numConnectedCustomers(UPDATED_NUM_CONNECTED_CUSTOMERS)
            .numSensitiveCustomers(UPDATED_NUM_SENSITIVE_CUSTOMERS)
            .backupSupply(UPDATED_BACKUP_SUPPLY)
            .costOfFailureEuro(UPDATED_COST_OF_FAILURE_EURO);
        return assetTransformer;
    }

    @BeforeEach
    public void initTest() {
        assetTransformer = createEntity(em);
    }

    @Test
    @Transactional
    void createAssetTransformer() throws Exception {
        int databaseSizeBeforeCreate = assetTransformerRepository.findAll().size();
        // Create the AssetTransformer
        AssetTransformerDTO assetTransformerDTO = assetTransformerMapper.toDto(assetTransformer);
        restAssetTransformerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetTransformerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AssetTransformer in the database
        List<AssetTransformer> assetTransformerList = assetTransformerRepository.findAll();
        assertThat(assetTransformerList).hasSize(databaseSizeBeforeCreate + 1);
        AssetTransformer testAssetTransformer = assetTransformerList.get(assetTransformerList.size() - 1);
        assertThat(testAssetTransformer.getBusNum()).isEqualTo(DEFAULT_BUS_NUM);
        assertThat(testAssetTransformer.getVoltageRatio()).isEqualTo(DEFAULT_VOLTAGE_RATIO);
        assertThat(testAssetTransformer.getInsulationMedium()).isEqualTo(DEFAULT_INSULATION_MEDIUM);
        assertThat(testAssetTransformer.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAssetTransformer.getIndoorOutdoor()).isEqualTo(DEFAULT_INDOOR_OUTDOOR);
        assertThat(testAssetTransformer.getAnnualMaxLoadKva()).isEqualTo(DEFAULT_ANNUAL_MAX_LOAD_KVA);
        assertThat(testAssetTransformer.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testAssetTransformer.getExternalCondition()).isEqualTo(DEFAULT_EXTERNAL_CONDITION);
        assertThat(testAssetTransformer.getRatingKva()).isEqualTo(DEFAULT_RATING_KVA);
        assertThat(testAssetTransformer.getNumConnectedCustomers()).isEqualTo(DEFAULT_NUM_CONNECTED_CUSTOMERS);
        assertThat(testAssetTransformer.getNumSensitiveCustomers()).isEqualTo(DEFAULT_NUM_SENSITIVE_CUSTOMERS);
        assertThat(testAssetTransformer.getBackupSupply()).isEqualTo(DEFAULT_BACKUP_SUPPLY);
        assertThat(testAssetTransformer.getCostOfFailureEuro()).isEqualTo(DEFAULT_COST_OF_FAILURE_EURO);
    }

    @Test
    @Transactional
    void createAssetTransformerWithExistingId() throws Exception {
        // Create the AssetTransformer with an existing ID
        assetTransformer.setId(1L);
        AssetTransformerDTO assetTransformerDTO = assetTransformerMapper.toDto(assetTransformer);

        int databaseSizeBeforeCreate = assetTransformerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetTransformerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetTransformerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetTransformer in the database
        List<AssetTransformer> assetTransformerList = assetTransformerRepository.findAll();
        assertThat(assetTransformerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAssetTransformers() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList
        restAssetTransformerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetTransformer.getId().intValue())))
            .andExpect(jsonPath("$.[*].busNum").value(hasItem(DEFAULT_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].voltageRatio").value(hasItem(DEFAULT_VOLTAGE_RATIO)))
            .andExpect(jsonPath("$.[*].insulationMedium").value(hasItem(DEFAULT_INSULATION_MEDIUM)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].indoorOutdoor").value(hasItem(DEFAULT_INDOOR_OUTDOOR)))
            .andExpect(jsonPath("$.[*].annualMaxLoadKva").value(hasItem(DEFAULT_ANNUAL_MAX_LOAD_KVA)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].externalCondition").value(hasItem(DEFAULT_EXTERNAL_CONDITION)))
            .andExpect(jsonPath("$.[*].ratingKva").value(hasItem(DEFAULT_RATING_KVA)))
            .andExpect(jsonPath("$.[*].numConnectedCustomers").value(hasItem(DEFAULT_NUM_CONNECTED_CUSTOMERS)))
            .andExpect(jsonPath("$.[*].numSensitiveCustomers").value(hasItem(DEFAULT_NUM_SENSITIVE_CUSTOMERS)))
            .andExpect(jsonPath("$.[*].backupSupply").value(hasItem(DEFAULT_BACKUP_SUPPLY)))
            .andExpect(jsonPath("$.[*].costOfFailureEuro").value(hasItem(DEFAULT_COST_OF_FAILURE_EURO.intValue())));
    }

    @Test
    @Transactional
    void getAssetTransformer() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get the assetTransformer
        restAssetTransformerMockMvc
            .perform(get(ENTITY_API_URL_ID, assetTransformer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assetTransformer.getId().intValue()))
            .andExpect(jsonPath("$.busNum").value(DEFAULT_BUS_NUM.intValue()))
            .andExpect(jsonPath("$.voltageRatio").value(DEFAULT_VOLTAGE_RATIO))
            .andExpect(jsonPath("$.insulationMedium").value(DEFAULT_INSULATION_MEDIUM))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.indoorOutdoor").value(DEFAULT_INDOOR_OUTDOOR))
            .andExpect(jsonPath("$.annualMaxLoadKva").value(DEFAULT_ANNUAL_MAX_LOAD_KVA))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.externalCondition").value(DEFAULT_EXTERNAL_CONDITION))
            .andExpect(jsonPath("$.ratingKva").value(DEFAULT_RATING_KVA))
            .andExpect(jsonPath("$.numConnectedCustomers").value(DEFAULT_NUM_CONNECTED_CUSTOMERS))
            .andExpect(jsonPath("$.numSensitiveCustomers").value(DEFAULT_NUM_SENSITIVE_CUSTOMERS))
            .andExpect(jsonPath("$.backupSupply").value(DEFAULT_BACKUP_SUPPLY))
            .andExpect(jsonPath("$.costOfFailureEuro").value(DEFAULT_COST_OF_FAILURE_EURO.intValue()));
    }

    @Test
    @Transactional
    void getAssetTransformersByIdFiltering() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        Long id = assetTransformer.getId();

        defaultAssetTransformerShouldBeFound("id.equals=" + id);
        defaultAssetTransformerShouldNotBeFound("id.notEquals=" + id);

        defaultAssetTransformerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssetTransformerShouldNotBeFound("id.greaterThan=" + id);

        defaultAssetTransformerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssetTransformerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByBusNumIsEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where busNum equals to DEFAULT_BUS_NUM
        defaultAssetTransformerShouldBeFound("busNum.equals=" + DEFAULT_BUS_NUM);

        // Get all the assetTransformerList where busNum equals to UPDATED_BUS_NUM
        defaultAssetTransformerShouldNotBeFound("busNum.equals=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByBusNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where busNum not equals to DEFAULT_BUS_NUM
        defaultAssetTransformerShouldNotBeFound("busNum.notEquals=" + DEFAULT_BUS_NUM);

        // Get all the assetTransformerList where busNum not equals to UPDATED_BUS_NUM
        defaultAssetTransformerShouldBeFound("busNum.notEquals=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByBusNumIsInShouldWork() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where busNum in DEFAULT_BUS_NUM or UPDATED_BUS_NUM
        defaultAssetTransformerShouldBeFound("busNum.in=" + DEFAULT_BUS_NUM + "," + UPDATED_BUS_NUM);

        // Get all the assetTransformerList where busNum equals to UPDATED_BUS_NUM
        defaultAssetTransformerShouldNotBeFound("busNum.in=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByBusNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where busNum is not null
        defaultAssetTransformerShouldBeFound("busNum.specified=true");

        // Get all the assetTransformerList where busNum is null
        defaultAssetTransformerShouldNotBeFound("busNum.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetTransformersByBusNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where busNum is greater than or equal to DEFAULT_BUS_NUM
        defaultAssetTransformerShouldBeFound("busNum.greaterThanOrEqual=" + DEFAULT_BUS_NUM);

        // Get all the assetTransformerList where busNum is greater than or equal to UPDATED_BUS_NUM
        defaultAssetTransformerShouldNotBeFound("busNum.greaterThanOrEqual=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByBusNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where busNum is less than or equal to DEFAULT_BUS_NUM
        defaultAssetTransformerShouldBeFound("busNum.lessThanOrEqual=" + DEFAULT_BUS_NUM);

        // Get all the assetTransformerList where busNum is less than or equal to SMALLER_BUS_NUM
        defaultAssetTransformerShouldNotBeFound("busNum.lessThanOrEqual=" + SMALLER_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByBusNumIsLessThanSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where busNum is less than DEFAULT_BUS_NUM
        defaultAssetTransformerShouldNotBeFound("busNum.lessThan=" + DEFAULT_BUS_NUM);

        // Get all the assetTransformerList where busNum is less than UPDATED_BUS_NUM
        defaultAssetTransformerShouldBeFound("busNum.lessThan=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByBusNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where busNum is greater than DEFAULT_BUS_NUM
        defaultAssetTransformerShouldNotBeFound("busNum.greaterThan=" + DEFAULT_BUS_NUM);

        // Get all the assetTransformerList where busNum is greater than SMALLER_BUS_NUM
        defaultAssetTransformerShouldBeFound("busNum.greaterThan=" + SMALLER_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByVoltageRatioIsEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where voltageRatio equals to DEFAULT_VOLTAGE_RATIO
        defaultAssetTransformerShouldBeFound("voltageRatio.equals=" + DEFAULT_VOLTAGE_RATIO);

        // Get all the assetTransformerList where voltageRatio equals to UPDATED_VOLTAGE_RATIO
        defaultAssetTransformerShouldNotBeFound("voltageRatio.equals=" + UPDATED_VOLTAGE_RATIO);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByVoltageRatioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where voltageRatio not equals to DEFAULT_VOLTAGE_RATIO
        defaultAssetTransformerShouldNotBeFound("voltageRatio.notEquals=" + DEFAULT_VOLTAGE_RATIO);

        // Get all the assetTransformerList where voltageRatio not equals to UPDATED_VOLTAGE_RATIO
        defaultAssetTransformerShouldBeFound("voltageRatio.notEquals=" + UPDATED_VOLTAGE_RATIO);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByVoltageRatioIsInShouldWork() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where voltageRatio in DEFAULT_VOLTAGE_RATIO or UPDATED_VOLTAGE_RATIO
        defaultAssetTransformerShouldBeFound("voltageRatio.in=" + DEFAULT_VOLTAGE_RATIO + "," + UPDATED_VOLTAGE_RATIO);

        // Get all the assetTransformerList where voltageRatio equals to UPDATED_VOLTAGE_RATIO
        defaultAssetTransformerShouldNotBeFound("voltageRatio.in=" + UPDATED_VOLTAGE_RATIO);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByVoltageRatioIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where voltageRatio is not null
        defaultAssetTransformerShouldBeFound("voltageRatio.specified=true");

        // Get all the assetTransformerList where voltageRatio is null
        defaultAssetTransformerShouldNotBeFound("voltageRatio.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetTransformersByVoltageRatioContainsSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where voltageRatio contains DEFAULT_VOLTAGE_RATIO
        defaultAssetTransformerShouldBeFound("voltageRatio.contains=" + DEFAULT_VOLTAGE_RATIO);

        // Get all the assetTransformerList where voltageRatio contains UPDATED_VOLTAGE_RATIO
        defaultAssetTransformerShouldNotBeFound("voltageRatio.contains=" + UPDATED_VOLTAGE_RATIO);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByVoltageRatioNotContainsSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where voltageRatio does not contain DEFAULT_VOLTAGE_RATIO
        defaultAssetTransformerShouldNotBeFound("voltageRatio.doesNotContain=" + DEFAULT_VOLTAGE_RATIO);

        // Get all the assetTransformerList where voltageRatio does not contain UPDATED_VOLTAGE_RATIO
        defaultAssetTransformerShouldBeFound("voltageRatio.doesNotContain=" + UPDATED_VOLTAGE_RATIO);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByInsulationMediumIsEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where insulationMedium equals to DEFAULT_INSULATION_MEDIUM
        defaultAssetTransformerShouldBeFound("insulationMedium.equals=" + DEFAULT_INSULATION_MEDIUM);

        // Get all the assetTransformerList where insulationMedium equals to UPDATED_INSULATION_MEDIUM
        defaultAssetTransformerShouldNotBeFound("insulationMedium.equals=" + UPDATED_INSULATION_MEDIUM);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByInsulationMediumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where insulationMedium not equals to DEFAULT_INSULATION_MEDIUM
        defaultAssetTransformerShouldNotBeFound("insulationMedium.notEquals=" + DEFAULT_INSULATION_MEDIUM);

        // Get all the assetTransformerList where insulationMedium not equals to UPDATED_INSULATION_MEDIUM
        defaultAssetTransformerShouldBeFound("insulationMedium.notEquals=" + UPDATED_INSULATION_MEDIUM);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByInsulationMediumIsInShouldWork() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where insulationMedium in DEFAULT_INSULATION_MEDIUM or UPDATED_INSULATION_MEDIUM
        defaultAssetTransformerShouldBeFound("insulationMedium.in=" + DEFAULT_INSULATION_MEDIUM + "," + UPDATED_INSULATION_MEDIUM);

        // Get all the assetTransformerList where insulationMedium equals to UPDATED_INSULATION_MEDIUM
        defaultAssetTransformerShouldNotBeFound("insulationMedium.in=" + UPDATED_INSULATION_MEDIUM);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByInsulationMediumIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where insulationMedium is not null
        defaultAssetTransformerShouldBeFound("insulationMedium.specified=true");

        // Get all the assetTransformerList where insulationMedium is null
        defaultAssetTransformerShouldNotBeFound("insulationMedium.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetTransformersByInsulationMediumContainsSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where insulationMedium contains DEFAULT_INSULATION_MEDIUM
        defaultAssetTransformerShouldBeFound("insulationMedium.contains=" + DEFAULT_INSULATION_MEDIUM);

        // Get all the assetTransformerList where insulationMedium contains UPDATED_INSULATION_MEDIUM
        defaultAssetTransformerShouldNotBeFound("insulationMedium.contains=" + UPDATED_INSULATION_MEDIUM);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByInsulationMediumNotContainsSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where insulationMedium does not contain DEFAULT_INSULATION_MEDIUM
        defaultAssetTransformerShouldNotBeFound("insulationMedium.doesNotContain=" + DEFAULT_INSULATION_MEDIUM);

        // Get all the assetTransformerList where insulationMedium does not contain UPDATED_INSULATION_MEDIUM
        defaultAssetTransformerShouldBeFound("insulationMedium.doesNotContain=" + UPDATED_INSULATION_MEDIUM);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where type equals to DEFAULT_TYPE
        defaultAssetTransformerShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the assetTransformerList where type equals to UPDATED_TYPE
        defaultAssetTransformerShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where type not equals to DEFAULT_TYPE
        defaultAssetTransformerShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the assetTransformerList where type not equals to UPDATED_TYPE
        defaultAssetTransformerShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultAssetTransformerShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the assetTransformerList where type equals to UPDATED_TYPE
        defaultAssetTransformerShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where type is not null
        defaultAssetTransformerShouldBeFound("type.specified=true");

        // Get all the assetTransformerList where type is null
        defaultAssetTransformerShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetTransformersByTypeContainsSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where type contains DEFAULT_TYPE
        defaultAssetTransformerShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the assetTransformerList where type contains UPDATED_TYPE
        defaultAssetTransformerShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where type does not contain DEFAULT_TYPE
        defaultAssetTransformerShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the assetTransformerList where type does not contain UPDATED_TYPE
        defaultAssetTransformerShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByIndoorOutdoorIsEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where indoorOutdoor equals to DEFAULT_INDOOR_OUTDOOR
        defaultAssetTransformerShouldBeFound("indoorOutdoor.equals=" + DEFAULT_INDOOR_OUTDOOR);

        // Get all the assetTransformerList where indoorOutdoor equals to UPDATED_INDOOR_OUTDOOR
        defaultAssetTransformerShouldNotBeFound("indoorOutdoor.equals=" + UPDATED_INDOOR_OUTDOOR);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByIndoorOutdoorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where indoorOutdoor not equals to DEFAULT_INDOOR_OUTDOOR
        defaultAssetTransformerShouldNotBeFound("indoorOutdoor.notEquals=" + DEFAULT_INDOOR_OUTDOOR);

        // Get all the assetTransformerList where indoorOutdoor not equals to UPDATED_INDOOR_OUTDOOR
        defaultAssetTransformerShouldBeFound("indoorOutdoor.notEquals=" + UPDATED_INDOOR_OUTDOOR);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByIndoorOutdoorIsInShouldWork() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where indoorOutdoor in DEFAULT_INDOOR_OUTDOOR or UPDATED_INDOOR_OUTDOOR
        defaultAssetTransformerShouldBeFound("indoorOutdoor.in=" + DEFAULT_INDOOR_OUTDOOR + "," + UPDATED_INDOOR_OUTDOOR);

        // Get all the assetTransformerList where indoorOutdoor equals to UPDATED_INDOOR_OUTDOOR
        defaultAssetTransformerShouldNotBeFound("indoorOutdoor.in=" + UPDATED_INDOOR_OUTDOOR);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByIndoorOutdoorIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where indoorOutdoor is not null
        defaultAssetTransformerShouldBeFound("indoorOutdoor.specified=true");

        // Get all the assetTransformerList where indoorOutdoor is null
        defaultAssetTransformerShouldNotBeFound("indoorOutdoor.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetTransformersByIndoorOutdoorContainsSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where indoorOutdoor contains DEFAULT_INDOOR_OUTDOOR
        defaultAssetTransformerShouldBeFound("indoorOutdoor.contains=" + DEFAULT_INDOOR_OUTDOOR);

        // Get all the assetTransformerList where indoorOutdoor contains UPDATED_INDOOR_OUTDOOR
        defaultAssetTransformerShouldNotBeFound("indoorOutdoor.contains=" + UPDATED_INDOOR_OUTDOOR);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByIndoorOutdoorNotContainsSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where indoorOutdoor does not contain DEFAULT_INDOOR_OUTDOOR
        defaultAssetTransformerShouldNotBeFound("indoorOutdoor.doesNotContain=" + DEFAULT_INDOOR_OUTDOOR);

        // Get all the assetTransformerList where indoorOutdoor does not contain UPDATED_INDOOR_OUTDOOR
        defaultAssetTransformerShouldBeFound("indoorOutdoor.doesNotContain=" + UPDATED_INDOOR_OUTDOOR);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByAnnualMaxLoadKvaIsEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where annualMaxLoadKva equals to DEFAULT_ANNUAL_MAX_LOAD_KVA
        defaultAssetTransformerShouldBeFound("annualMaxLoadKva.equals=" + DEFAULT_ANNUAL_MAX_LOAD_KVA);

        // Get all the assetTransformerList where annualMaxLoadKva equals to UPDATED_ANNUAL_MAX_LOAD_KVA
        defaultAssetTransformerShouldNotBeFound("annualMaxLoadKva.equals=" + UPDATED_ANNUAL_MAX_LOAD_KVA);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByAnnualMaxLoadKvaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where annualMaxLoadKva not equals to DEFAULT_ANNUAL_MAX_LOAD_KVA
        defaultAssetTransformerShouldNotBeFound("annualMaxLoadKva.notEquals=" + DEFAULT_ANNUAL_MAX_LOAD_KVA);

        // Get all the assetTransformerList where annualMaxLoadKva not equals to UPDATED_ANNUAL_MAX_LOAD_KVA
        defaultAssetTransformerShouldBeFound("annualMaxLoadKva.notEquals=" + UPDATED_ANNUAL_MAX_LOAD_KVA);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByAnnualMaxLoadKvaIsInShouldWork() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where annualMaxLoadKva in DEFAULT_ANNUAL_MAX_LOAD_KVA or UPDATED_ANNUAL_MAX_LOAD_KVA
        defaultAssetTransformerShouldBeFound("annualMaxLoadKva.in=" + DEFAULT_ANNUAL_MAX_LOAD_KVA + "," + UPDATED_ANNUAL_MAX_LOAD_KVA);

        // Get all the assetTransformerList where annualMaxLoadKva equals to UPDATED_ANNUAL_MAX_LOAD_KVA
        defaultAssetTransformerShouldNotBeFound("annualMaxLoadKva.in=" + UPDATED_ANNUAL_MAX_LOAD_KVA);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByAnnualMaxLoadKvaIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where annualMaxLoadKva is not null
        defaultAssetTransformerShouldBeFound("annualMaxLoadKva.specified=true");

        // Get all the assetTransformerList where annualMaxLoadKva is null
        defaultAssetTransformerShouldNotBeFound("annualMaxLoadKva.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetTransformersByAnnualMaxLoadKvaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where annualMaxLoadKva is greater than or equal to DEFAULT_ANNUAL_MAX_LOAD_KVA
        defaultAssetTransformerShouldBeFound("annualMaxLoadKva.greaterThanOrEqual=" + DEFAULT_ANNUAL_MAX_LOAD_KVA);

        // Get all the assetTransformerList where annualMaxLoadKva is greater than or equal to UPDATED_ANNUAL_MAX_LOAD_KVA
        defaultAssetTransformerShouldNotBeFound("annualMaxLoadKva.greaterThanOrEqual=" + UPDATED_ANNUAL_MAX_LOAD_KVA);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByAnnualMaxLoadKvaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where annualMaxLoadKva is less than or equal to DEFAULT_ANNUAL_MAX_LOAD_KVA
        defaultAssetTransformerShouldBeFound("annualMaxLoadKva.lessThanOrEqual=" + DEFAULT_ANNUAL_MAX_LOAD_KVA);

        // Get all the assetTransformerList where annualMaxLoadKva is less than or equal to SMALLER_ANNUAL_MAX_LOAD_KVA
        defaultAssetTransformerShouldNotBeFound("annualMaxLoadKva.lessThanOrEqual=" + SMALLER_ANNUAL_MAX_LOAD_KVA);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByAnnualMaxLoadKvaIsLessThanSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where annualMaxLoadKva is less than DEFAULT_ANNUAL_MAX_LOAD_KVA
        defaultAssetTransformerShouldNotBeFound("annualMaxLoadKva.lessThan=" + DEFAULT_ANNUAL_MAX_LOAD_KVA);

        // Get all the assetTransformerList where annualMaxLoadKva is less than UPDATED_ANNUAL_MAX_LOAD_KVA
        defaultAssetTransformerShouldBeFound("annualMaxLoadKva.lessThan=" + UPDATED_ANNUAL_MAX_LOAD_KVA);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByAnnualMaxLoadKvaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where annualMaxLoadKva is greater than DEFAULT_ANNUAL_MAX_LOAD_KVA
        defaultAssetTransformerShouldNotBeFound("annualMaxLoadKva.greaterThan=" + DEFAULT_ANNUAL_MAX_LOAD_KVA);

        // Get all the assetTransformerList where annualMaxLoadKva is greater than SMALLER_ANNUAL_MAX_LOAD_KVA
        defaultAssetTransformerShouldBeFound("annualMaxLoadKva.greaterThan=" + SMALLER_ANNUAL_MAX_LOAD_KVA);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByAgeIsEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where age equals to DEFAULT_AGE
        defaultAssetTransformerShouldBeFound("age.equals=" + DEFAULT_AGE);

        // Get all the assetTransformerList where age equals to UPDATED_AGE
        defaultAssetTransformerShouldNotBeFound("age.equals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByAgeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where age not equals to DEFAULT_AGE
        defaultAssetTransformerShouldNotBeFound("age.notEquals=" + DEFAULT_AGE);

        // Get all the assetTransformerList where age not equals to UPDATED_AGE
        defaultAssetTransformerShouldBeFound("age.notEquals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByAgeIsInShouldWork() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where age in DEFAULT_AGE or UPDATED_AGE
        defaultAssetTransformerShouldBeFound("age.in=" + DEFAULT_AGE + "," + UPDATED_AGE);

        // Get all the assetTransformerList where age equals to UPDATED_AGE
        defaultAssetTransformerShouldNotBeFound("age.in=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByAgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where age is not null
        defaultAssetTransformerShouldBeFound("age.specified=true");

        // Get all the assetTransformerList where age is null
        defaultAssetTransformerShouldNotBeFound("age.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetTransformersByAgeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where age is greater than or equal to DEFAULT_AGE
        defaultAssetTransformerShouldBeFound("age.greaterThanOrEqual=" + DEFAULT_AGE);

        // Get all the assetTransformerList where age is greater than or equal to UPDATED_AGE
        defaultAssetTransformerShouldNotBeFound("age.greaterThanOrEqual=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByAgeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where age is less than or equal to DEFAULT_AGE
        defaultAssetTransformerShouldBeFound("age.lessThanOrEqual=" + DEFAULT_AGE);

        // Get all the assetTransformerList where age is less than or equal to SMALLER_AGE
        defaultAssetTransformerShouldNotBeFound("age.lessThanOrEqual=" + SMALLER_AGE);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByAgeIsLessThanSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where age is less than DEFAULT_AGE
        defaultAssetTransformerShouldNotBeFound("age.lessThan=" + DEFAULT_AGE);

        // Get all the assetTransformerList where age is less than UPDATED_AGE
        defaultAssetTransformerShouldBeFound("age.lessThan=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByAgeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where age is greater than DEFAULT_AGE
        defaultAssetTransformerShouldNotBeFound("age.greaterThan=" + DEFAULT_AGE);

        // Get all the assetTransformerList where age is greater than SMALLER_AGE
        defaultAssetTransformerShouldBeFound("age.greaterThan=" + SMALLER_AGE);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByExternalConditionIsEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where externalCondition equals to DEFAULT_EXTERNAL_CONDITION
        defaultAssetTransformerShouldBeFound("externalCondition.equals=" + DEFAULT_EXTERNAL_CONDITION);

        // Get all the assetTransformerList where externalCondition equals to UPDATED_EXTERNAL_CONDITION
        defaultAssetTransformerShouldNotBeFound("externalCondition.equals=" + UPDATED_EXTERNAL_CONDITION);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByExternalConditionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where externalCondition not equals to DEFAULT_EXTERNAL_CONDITION
        defaultAssetTransformerShouldNotBeFound("externalCondition.notEquals=" + DEFAULT_EXTERNAL_CONDITION);

        // Get all the assetTransformerList where externalCondition not equals to UPDATED_EXTERNAL_CONDITION
        defaultAssetTransformerShouldBeFound("externalCondition.notEquals=" + UPDATED_EXTERNAL_CONDITION);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByExternalConditionIsInShouldWork() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where externalCondition in DEFAULT_EXTERNAL_CONDITION or UPDATED_EXTERNAL_CONDITION
        defaultAssetTransformerShouldBeFound("externalCondition.in=" + DEFAULT_EXTERNAL_CONDITION + "," + UPDATED_EXTERNAL_CONDITION);

        // Get all the assetTransformerList where externalCondition equals to UPDATED_EXTERNAL_CONDITION
        defaultAssetTransformerShouldNotBeFound("externalCondition.in=" + UPDATED_EXTERNAL_CONDITION);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByExternalConditionIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where externalCondition is not null
        defaultAssetTransformerShouldBeFound("externalCondition.specified=true");

        // Get all the assetTransformerList where externalCondition is null
        defaultAssetTransformerShouldNotBeFound("externalCondition.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetTransformersByExternalConditionContainsSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where externalCondition contains DEFAULT_EXTERNAL_CONDITION
        defaultAssetTransformerShouldBeFound("externalCondition.contains=" + DEFAULT_EXTERNAL_CONDITION);

        // Get all the assetTransformerList where externalCondition contains UPDATED_EXTERNAL_CONDITION
        defaultAssetTransformerShouldNotBeFound("externalCondition.contains=" + UPDATED_EXTERNAL_CONDITION);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByExternalConditionNotContainsSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where externalCondition does not contain DEFAULT_EXTERNAL_CONDITION
        defaultAssetTransformerShouldNotBeFound("externalCondition.doesNotContain=" + DEFAULT_EXTERNAL_CONDITION);

        // Get all the assetTransformerList where externalCondition does not contain UPDATED_EXTERNAL_CONDITION
        defaultAssetTransformerShouldBeFound("externalCondition.doesNotContain=" + UPDATED_EXTERNAL_CONDITION);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByRatingKvaIsEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where ratingKva equals to DEFAULT_RATING_KVA
        defaultAssetTransformerShouldBeFound("ratingKva.equals=" + DEFAULT_RATING_KVA);

        // Get all the assetTransformerList where ratingKva equals to UPDATED_RATING_KVA
        defaultAssetTransformerShouldNotBeFound("ratingKva.equals=" + UPDATED_RATING_KVA);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByRatingKvaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where ratingKva not equals to DEFAULT_RATING_KVA
        defaultAssetTransformerShouldNotBeFound("ratingKva.notEquals=" + DEFAULT_RATING_KVA);

        // Get all the assetTransformerList where ratingKva not equals to UPDATED_RATING_KVA
        defaultAssetTransformerShouldBeFound("ratingKva.notEquals=" + UPDATED_RATING_KVA);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByRatingKvaIsInShouldWork() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where ratingKva in DEFAULT_RATING_KVA or UPDATED_RATING_KVA
        defaultAssetTransformerShouldBeFound("ratingKva.in=" + DEFAULT_RATING_KVA + "," + UPDATED_RATING_KVA);

        // Get all the assetTransformerList where ratingKva equals to UPDATED_RATING_KVA
        defaultAssetTransformerShouldNotBeFound("ratingKva.in=" + UPDATED_RATING_KVA);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByRatingKvaIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where ratingKva is not null
        defaultAssetTransformerShouldBeFound("ratingKva.specified=true");

        // Get all the assetTransformerList where ratingKva is null
        defaultAssetTransformerShouldNotBeFound("ratingKva.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetTransformersByRatingKvaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where ratingKva is greater than or equal to DEFAULT_RATING_KVA
        defaultAssetTransformerShouldBeFound("ratingKva.greaterThanOrEqual=" + DEFAULT_RATING_KVA);

        // Get all the assetTransformerList where ratingKva is greater than or equal to UPDATED_RATING_KVA
        defaultAssetTransformerShouldNotBeFound("ratingKva.greaterThanOrEqual=" + UPDATED_RATING_KVA);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByRatingKvaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where ratingKva is less than or equal to DEFAULT_RATING_KVA
        defaultAssetTransformerShouldBeFound("ratingKva.lessThanOrEqual=" + DEFAULT_RATING_KVA);

        // Get all the assetTransformerList where ratingKva is less than or equal to SMALLER_RATING_KVA
        defaultAssetTransformerShouldNotBeFound("ratingKva.lessThanOrEqual=" + SMALLER_RATING_KVA);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByRatingKvaIsLessThanSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where ratingKva is less than DEFAULT_RATING_KVA
        defaultAssetTransformerShouldNotBeFound("ratingKva.lessThan=" + DEFAULT_RATING_KVA);

        // Get all the assetTransformerList where ratingKva is less than UPDATED_RATING_KVA
        defaultAssetTransformerShouldBeFound("ratingKva.lessThan=" + UPDATED_RATING_KVA);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByRatingKvaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where ratingKva is greater than DEFAULT_RATING_KVA
        defaultAssetTransformerShouldNotBeFound("ratingKva.greaterThan=" + DEFAULT_RATING_KVA);

        // Get all the assetTransformerList where ratingKva is greater than SMALLER_RATING_KVA
        defaultAssetTransformerShouldBeFound("ratingKva.greaterThan=" + SMALLER_RATING_KVA);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByNumConnectedCustomersIsEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where numConnectedCustomers equals to DEFAULT_NUM_CONNECTED_CUSTOMERS
        defaultAssetTransformerShouldBeFound("numConnectedCustomers.equals=" + DEFAULT_NUM_CONNECTED_CUSTOMERS);

        // Get all the assetTransformerList where numConnectedCustomers equals to UPDATED_NUM_CONNECTED_CUSTOMERS
        defaultAssetTransformerShouldNotBeFound("numConnectedCustomers.equals=" + UPDATED_NUM_CONNECTED_CUSTOMERS);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByNumConnectedCustomersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where numConnectedCustomers not equals to DEFAULT_NUM_CONNECTED_CUSTOMERS
        defaultAssetTransformerShouldNotBeFound("numConnectedCustomers.notEquals=" + DEFAULT_NUM_CONNECTED_CUSTOMERS);

        // Get all the assetTransformerList where numConnectedCustomers not equals to UPDATED_NUM_CONNECTED_CUSTOMERS
        defaultAssetTransformerShouldBeFound("numConnectedCustomers.notEquals=" + UPDATED_NUM_CONNECTED_CUSTOMERS);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByNumConnectedCustomersIsInShouldWork() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where numConnectedCustomers in DEFAULT_NUM_CONNECTED_CUSTOMERS or UPDATED_NUM_CONNECTED_CUSTOMERS
        defaultAssetTransformerShouldBeFound(
            "numConnectedCustomers.in=" + DEFAULT_NUM_CONNECTED_CUSTOMERS + "," + UPDATED_NUM_CONNECTED_CUSTOMERS
        );

        // Get all the assetTransformerList where numConnectedCustomers equals to UPDATED_NUM_CONNECTED_CUSTOMERS
        defaultAssetTransformerShouldNotBeFound("numConnectedCustomers.in=" + UPDATED_NUM_CONNECTED_CUSTOMERS);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByNumConnectedCustomersIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where numConnectedCustomers is not null
        defaultAssetTransformerShouldBeFound("numConnectedCustomers.specified=true");

        // Get all the assetTransformerList where numConnectedCustomers is null
        defaultAssetTransformerShouldNotBeFound("numConnectedCustomers.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetTransformersByNumConnectedCustomersIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where numConnectedCustomers is greater than or equal to DEFAULT_NUM_CONNECTED_CUSTOMERS
        defaultAssetTransformerShouldBeFound("numConnectedCustomers.greaterThanOrEqual=" + DEFAULT_NUM_CONNECTED_CUSTOMERS);

        // Get all the assetTransformerList where numConnectedCustomers is greater than or equal to UPDATED_NUM_CONNECTED_CUSTOMERS
        defaultAssetTransformerShouldNotBeFound("numConnectedCustomers.greaterThanOrEqual=" + UPDATED_NUM_CONNECTED_CUSTOMERS);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByNumConnectedCustomersIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where numConnectedCustomers is less than or equal to DEFAULT_NUM_CONNECTED_CUSTOMERS
        defaultAssetTransformerShouldBeFound("numConnectedCustomers.lessThanOrEqual=" + DEFAULT_NUM_CONNECTED_CUSTOMERS);

        // Get all the assetTransformerList where numConnectedCustomers is less than or equal to SMALLER_NUM_CONNECTED_CUSTOMERS
        defaultAssetTransformerShouldNotBeFound("numConnectedCustomers.lessThanOrEqual=" + SMALLER_NUM_CONNECTED_CUSTOMERS);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByNumConnectedCustomersIsLessThanSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where numConnectedCustomers is less than DEFAULT_NUM_CONNECTED_CUSTOMERS
        defaultAssetTransformerShouldNotBeFound("numConnectedCustomers.lessThan=" + DEFAULT_NUM_CONNECTED_CUSTOMERS);

        // Get all the assetTransformerList where numConnectedCustomers is less than UPDATED_NUM_CONNECTED_CUSTOMERS
        defaultAssetTransformerShouldBeFound("numConnectedCustomers.lessThan=" + UPDATED_NUM_CONNECTED_CUSTOMERS);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByNumConnectedCustomersIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where numConnectedCustomers is greater than DEFAULT_NUM_CONNECTED_CUSTOMERS
        defaultAssetTransformerShouldNotBeFound("numConnectedCustomers.greaterThan=" + DEFAULT_NUM_CONNECTED_CUSTOMERS);

        // Get all the assetTransformerList where numConnectedCustomers is greater than SMALLER_NUM_CONNECTED_CUSTOMERS
        defaultAssetTransformerShouldBeFound("numConnectedCustomers.greaterThan=" + SMALLER_NUM_CONNECTED_CUSTOMERS);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByNumSensitiveCustomersIsEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where numSensitiveCustomers equals to DEFAULT_NUM_SENSITIVE_CUSTOMERS
        defaultAssetTransformerShouldBeFound("numSensitiveCustomers.equals=" + DEFAULT_NUM_SENSITIVE_CUSTOMERS);

        // Get all the assetTransformerList where numSensitiveCustomers equals to UPDATED_NUM_SENSITIVE_CUSTOMERS
        defaultAssetTransformerShouldNotBeFound("numSensitiveCustomers.equals=" + UPDATED_NUM_SENSITIVE_CUSTOMERS);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByNumSensitiveCustomersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where numSensitiveCustomers not equals to DEFAULT_NUM_SENSITIVE_CUSTOMERS
        defaultAssetTransformerShouldNotBeFound("numSensitiveCustomers.notEquals=" + DEFAULT_NUM_SENSITIVE_CUSTOMERS);

        // Get all the assetTransformerList where numSensitiveCustomers not equals to UPDATED_NUM_SENSITIVE_CUSTOMERS
        defaultAssetTransformerShouldBeFound("numSensitiveCustomers.notEquals=" + UPDATED_NUM_SENSITIVE_CUSTOMERS);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByNumSensitiveCustomersIsInShouldWork() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where numSensitiveCustomers in DEFAULT_NUM_SENSITIVE_CUSTOMERS or UPDATED_NUM_SENSITIVE_CUSTOMERS
        defaultAssetTransformerShouldBeFound(
            "numSensitiveCustomers.in=" + DEFAULT_NUM_SENSITIVE_CUSTOMERS + "," + UPDATED_NUM_SENSITIVE_CUSTOMERS
        );

        // Get all the assetTransformerList where numSensitiveCustomers equals to UPDATED_NUM_SENSITIVE_CUSTOMERS
        defaultAssetTransformerShouldNotBeFound("numSensitiveCustomers.in=" + UPDATED_NUM_SENSITIVE_CUSTOMERS);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByNumSensitiveCustomersIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where numSensitiveCustomers is not null
        defaultAssetTransformerShouldBeFound("numSensitiveCustomers.specified=true");

        // Get all the assetTransformerList where numSensitiveCustomers is null
        defaultAssetTransformerShouldNotBeFound("numSensitiveCustomers.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetTransformersByNumSensitiveCustomersIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where numSensitiveCustomers is greater than or equal to DEFAULT_NUM_SENSITIVE_CUSTOMERS
        defaultAssetTransformerShouldBeFound("numSensitiveCustomers.greaterThanOrEqual=" + DEFAULT_NUM_SENSITIVE_CUSTOMERS);

        // Get all the assetTransformerList where numSensitiveCustomers is greater than or equal to UPDATED_NUM_SENSITIVE_CUSTOMERS
        defaultAssetTransformerShouldNotBeFound("numSensitiveCustomers.greaterThanOrEqual=" + UPDATED_NUM_SENSITIVE_CUSTOMERS);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByNumSensitiveCustomersIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where numSensitiveCustomers is less than or equal to DEFAULT_NUM_SENSITIVE_CUSTOMERS
        defaultAssetTransformerShouldBeFound("numSensitiveCustomers.lessThanOrEqual=" + DEFAULT_NUM_SENSITIVE_CUSTOMERS);

        // Get all the assetTransformerList where numSensitiveCustomers is less than or equal to SMALLER_NUM_SENSITIVE_CUSTOMERS
        defaultAssetTransformerShouldNotBeFound("numSensitiveCustomers.lessThanOrEqual=" + SMALLER_NUM_SENSITIVE_CUSTOMERS);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByNumSensitiveCustomersIsLessThanSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where numSensitiveCustomers is less than DEFAULT_NUM_SENSITIVE_CUSTOMERS
        defaultAssetTransformerShouldNotBeFound("numSensitiveCustomers.lessThan=" + DEFAULT_NUM_SENSITIVE_CUSTOMERS);

        // Get all the assetTransformerList where numSensitiveCustomers is less than UPDATED_NUM_SENSITIVE_CUSTOMERS
        defaultAssetTransformerShouldBeFound("numSensitiveCustomers.lessThan=" + UPDATED_NUM_SENSITIVE_CUSTOMERS);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByNumSensitiveCustomersIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where numSensitiveCustomers is greater than DEFAULT_NUM_SENSITIVE_CUSTOMERS
        defaultAssetTransformerShouldNotBeFound("numSensitiveCustomers.greaterThan=" + DEFAULT_NUM_SENSITIVE_CUSTOMERS);

        // Get all the assetTransformerList where numSensitiveCustomers is greater than SMALLER_NUM_SENSITIVE_CUSTOMERS
        defaultAssetTransformerShouldBeFound("numSensitiveCustomers.greaterThan=" + SMALLER_NUM_SENSITIVE_CUSTOMERS);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByBackupSupplyIsEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where backupSupply equals to DEFAULT_BACKUP_SUPPLY
        defaultAssetTransformerShouldBeFound("backupSupply.equals=" + DEFAULT_BACKUP_SUPPLY);

        // Get all the assetTransformerList where backupSupply equals to UPDATED_BACKUP_SUPPLY
        defaultAssetTransformerShouldNotBeFound("backupSupply.equals=" + UPDATED_BACKUP_SUPPLY);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByBackupSupplyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where backupSupply not equals to DEFAULT_BACKUP_SUPPLY
        defaultAssetTransformerShouldNotBeFound("backupSupply.notEquals=" + DEFAULT_BACKUP_SUPPLY);

        // Get all the assetTransformerList where backupSupply not equals to UPDATED_BACKUP_SUPPLY
        defaultAssetTransformerShouldBeFound("backupSupply.notEquals=" + UPDATED_BACKUP_SUPPLY);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByBackupSupplyIsInShouldWork() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where backupSupply in DEFAULT_BACKUP_SUPPLY or UPDATED_BACKUP_SUPPLY
        defaultAssetTransformerShouldBeFound("backupSupply.in=" + DEFAULT_BACKUP_SUPPLY + "," + UPDATED_BACKUP_SUPPLY);

        // Get all the assetTransformerList where backupSupply equals to UPDATED_BACKUP_SUPPLY
        defaultAssetTransformerShouldNotBeFound("backupSupply.in=" + UPDATED_BACKUP_SUPPLY);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByBackupSupplyIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where backupSupply is not null
        defaultAssetTransformerShouldBeFound("backupSupply.specified=true");

        // Get all the assetTransformerList where backupSupply is null
        defaultAssetTransformerShouldNotBeFound("backupSupply.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetTransformersByBackupSupplyContainsSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where backupSupply contains DEFAULT_BACKUP_SUPPLY
        defaultAssetTransformerShouldBeFound("backupSupply.contains=" + DEFAULT_BACKUP_SUPPLY);

        // Get all the assetTransformerList where backupSupply contains UPDATED_BACKUP_SUPPLY
        defaultAssetTransformerShouldNotBeFound("backupSupply.contains=" + UPDATED_BACKUP_SUPPLY);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByBackupSupplyNotContainsSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where backupSupply does not contain DEFAULT_BACKUP_SUPPLY
        defaultAssetTransformerShouldNotBeFound("backupSupply.doesNotContain=" + DEFAULT_BACKUP_SUPPLY);

        // Get all the assetTransformerList where backupSupply does not contain UPDATED_BACKUP_SUPPLY
        defaultAssetTransformerShouldBeFound("backupSupply.doesNotContain=" + UPDATED_BACKUP_SUPPLY);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByCostOfFailureEuroIsEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where costOfFailureEuro equals to DEFAULT_COST_OF_FAILURE_EURO
        defaultAssetTransformerShouldBeFound("costOfFailureEuro.equals=" + DEFAULT_COST_OF_FAILURE_EURO);

        // Get all the assetTransformerList where costOfFailureEuro equals to UPDATED_COST_OF_FAILURE_EURO
        defaultAssetTransformerShouldNotBeFound("costOfFailureEuro.equals=" + UPDATED_COST_OF_FAILURE_EURO);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByCostOfFailureEuroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where costOfFailureEuro not equals to DEFAULT_COST_OF_FAILURE_EURO
        defaultAssetTransformerShouldNotBeFound("costOfFailureEuro.notEquals=" + DEFAULT_COST_OF_FAILURE_EURO);

        // Get all the assetTransformerList where costOfFailureEuro not equals to UPDATED_COST_OF_FAILURE_EURO
        defaultAssetTransformerShouldBeFound("costOfFailureEuro.notEquals=" + UPDATED_COST_OF_FAILURE_EURO);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByCostOfFailureEuroIsInShouldWork() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where costOfFailureEuro in DEFAULT_COST_OF_FAILURE_EURO or UPDATED_COST_OF_FAILURE_EURO
        defaultAssetTransformerShouldBeFound("costOfFailureEuro.in=" + DEFAULT_COST_OF_FAILURE_EURO + "," + UPDATED_COST_OF_FAILURE_EURO);

        // Get all the assetTransformerList where costOfFailureEuro equals to UPDATED_COST_OF_FAILURE_EURO
        defaultAssetTransformerShouldNotBeFound("costOfFailureEuro.in=" + UPDATED_COST_OF_FAILURE_EURO);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByCostOfFailureEuroIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where costOfFailureEuro is not null
        defaultAssetTransformerShouldBeFound("costOfFailureEuro.specified=true");

        // Get all the assetTransformerList where costOfFailureEuro is null
        defaultAssetTransformerShouldNotBeFound("costOfFailureEuro.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetTransformersByCostOfFailureEuroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where costOfFailureEuro is greater than or equal to DEFAULT_COST_OF_FAILURE_EURO
        defaultAssetTransformerShouldBeFound("costOfFailureEuro.greaterThanOrEqual=" + DEFAULT_COST_OF_FAILURE_EURO);

        // Get all the assetTransformerList where costOfFailureEuro is greater than or equal to UPDATED_COST_OF_FAILURE_EURO
        defaultAssetTransformerShouldNotBeFound("costOfFailureEuro.greaterThanOrEqual=" + UPDATED_COST_OF_FAILURE_EURO);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByCostOfFailureEuroIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where costOfFailureEuro is less than or equal to DEFAULT_COST_OF_FAILURE_EURO
        defaultAssetTransformerShouldBeFound("costOfFailureEuro.lessThanOrEqual=" + DEFAULT_COST_OF_FAILURE_EURO);

        // Get all the assetTransformerList where costOfFailureEuro is less than or equal to SMALLER_COST_OF_FAILURE_EURO
        defaultAssetTransformerShouldNotBeFound("costOfFailureEuro.lessThanOrEqual=" + SMALLER_COST_OF_FAILURE_EURO);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByCostOfFailureEuroIsLessThanSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where costOfFailureEuro is less than DEFAULT_COST_OF_FAILURE_EURO
        defaultAssetTransformerShouldNotBeFound("costOfFailureEuro.lessThan=" + DEFAULT_COST_OF_FAILURE_EURO);

        // Get all the assetTransformerList where costOfFailureEuro is less than UPDATED_COST_OF_FAILURE_EURO
        defaultAssetTransformerShouldBeFound("costOfFailureEuro.lessThan=" + UPDATED_COST_OF_FAILURE_EURO);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByCostOfFailureEuroIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        // Get all the assetTransformerList where costOfFailureEuro is greater than DEFAULT_COST_OF_FAILURE_EURO
        defaultAssetTransformerShouldNotBeFound("costOfFailureEuro.greaterThan=" + DEFAULT_COST_OF_FAILURE_EURO);

        // Get all the assetTransformerList where costOfFailureEuro is greater than SMALLER_COST_OF_FAILURE_EURO
        defaultAssetTransformerShouldBeFound("costOfFailureEuro.greaterThan=" + SMALLER_COST_OF_FAILURE_EURO);
    }

    @Test
    @Transactional
    void getAllAssetTransformersByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);
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
        assetTransformer.setNetwork(network);
        assetTransformerRepository.saveAndFlush(assetTransformer);
        Long networkId = network.getId();

        // Get all the assetTransformerList where network equals to networkId
        defaultAssetTransformerShouldBeFound("networkId.equals=" + networkId);

        // Get all the assetTransformerList where network equals to (networkId + 1)
        defaultAssetTransformerShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetTransformerShouldBeFound(String filter) throws Exception {
        restAssetTransformerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetTransformer.getId().intValue())))
            .andExpect(jsonPath("$.[*].busNum").value(hasItem(DEFAULT_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].voltageRatio").value(hasItem(DEFAULT_VOLTAGE_RATIO)))
            .andExpect(jsonPath("$.[*].insulationMedium").value(hasItem(DEFAULT_INSULATION_MEDIUM)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].indoorOutdoor").value(hasItem(DEFAULT_INDOOR_OUTDOOR)))
            .andExpect(jsonPath("$.[*].annualMaxLoadKva").value(hasItem(DEFAULT_ANNUAL_MAX_LOAD_KVA)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].externalCondition").value(hasItem(DEFAULT_EXTERNAL_CONDITION)))
            .andExpect(jsonPath("$.[*].ratingKva").value(hasItem(DEFAULT_RATING_KVA)))
            .andExpect(jsonPath("$.[*].numConnectedCustomers").value(hasItem(DEFAULT_NUM_CONNECTED_CUSTOMERS)))
            .andExpect(jsonPath("$.[*].numSensitiveCustomers").value(hasItem(DEFAULT_NUM_SENSITIVE_CUSTOMERS)))
            .andExpect(jsonPath("$.[*].backupSupply").value(hasItem(DEFAULT_BACKUP_SUPPLY)))
            .andExpect(jsonPath("$.[*].costOfFailureEuro").value(hasItem(DEFAULT_COST_OF_FAILURE_EURO.intValue())));

        // Check, that the count call also returns 1
        restAssetTransformerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetTransformerShouldNotBeFound(String filter) throws Exception {
        restAssetTransformerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetTransformerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssetTransformer() throws Exception {
        // Get the assetTransformer
        restAssetTransformerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssetTransformer() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        int databaseSizeBeforeUpdate = assetTransformerRepository.findAll().size();

        // Update the assetTransformer
        AssetTransformer updatedAssetTransformer = assetTransformerRepository.findById(assetTransformer.getId()).get();
        // Disconnect from session so that the updates on updatedAssetTransformer are not directly saved in db
        em.detach(updatedAssetTransformer);
        updatedAssetTransformer
            .busNum(UPDATED_BUS_NUM)
            .voltageRatio(UPDATED_VOLTAGE_RATIO)
            .insulationMedium(UPDATED_INSULATION_MEDIUM)
            .type(UPDATED_TYPE)
            .indoorOutdoor(UPDATED_INDOOR_OUTDOOR)
            .annualMaxLoadKva(UPDATED_ANNUAL_MAX_LOAD_KVA)
            .age(UPDATED_AGE)
            .externalCondition(UPDATED_EXTERNAL_CONDITION)
            .ratingKva(UPDATED_RATING_KVA)
            .numConnectedCustomers(UPDATED_NUM_CONNECTED_CUSTOMERS)
            .numSensitiveCustomers(UPDATED_NUM_SENSITIVE_CUSTOMERS)
            .backupSupply(UPDATED_BACKUP_SUPPLY)
            .costOfFailureEuro(UPDATED_COST_OF_FAILURE_EURO);
        AssetTransformerDTO assetTransformerDTO = assetTransformerMapper.toDto(updatedAssetTransformer);

        restAssetTransformerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetTransformerDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetTransformerDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssetTransformer in the database
        List<AssetTransformer> assetTransformerList = assetTransformerRepository.findAll();
        assertThat(assetTransformerList).hasSize(databaseSizeBeforeUpdate);
        AssetTransformer testAssetTransformer = assetTransformerList.get(assetTransformerList.size() - 1);
        assertThat(testAssetTransformer.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testAssetTransformer.getVoltageRatio()).isEqualTo(UPDATED_VOLTAGE_RATIO);
        assertThat(testAssetTransformer.getInsulationMedium()).isEqualTo(UPDATED_INSULATION_MEDIUM);
        assertThat(testAssetTransformer.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAssetTransformer.getIndoorOutdoor()).isEqualTo(UPDATED_INDOOR_OUTDOOR);
        assertThat(testAssetTransformer.getAnnualMaxLoadKva()).isEqualTo(UPDATED_ANNUAL_MAX_LOAD_KVA);
        assertThat(testAssetTransformer.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testAssetTransformer.getExternalCondition()).isEqualTo(UPDATED_EXTERNAL_CONDITION);
        assertThat(testAssetTransformer.getRatingKva()).isEqualTo(UPDATED_RATING_KVA);
        assertThat(testAssetTransformer.getNumConnectedCustomers()).isEqualTo(UPDATED_NUM_CONNECTED_CUSTOMERS);
        assertThat(testAssetTransformer.getNumSensitiveCustomers()).isEqualTo(UPDATED_NUM_SENSITIVE_CUSTOMERS);
        assertThat(testAssetTransformer.getBackupSupply()).isEqualTo(UPDATED_BACKUP_SUPPLY);
        assertThat(testAssetTransformer.getCostOfFailureEuro()).isEqualTo(UPDATED_COST_OF_FAILURE_EURO);
    }

    @Test
    @Transactional
    void putNonExistingAssetTransformer() throws Exception {
        int databaseSizeBeforeUpdate = assetTransformerRepository.findAll().size();
        assetTransformer.setId(count.incrementAndGet());

        // Create the AssetTransformer
        AssetTransformerDTO assetTransformerDTO = assetTransformerMapper.toDto(assetTransformer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetTransformerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetTransformerDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetTransformerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetTransformer in the database
        List<AssetTransformer> assetTransformerList = assetTransformerRepository.findAll();
        assertThat(assetTransformerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssetTransformer() throws Exception {
        int databaseSizeBeforeUpdate = assetTransformerRepository.findAll().size();
        assetTransformer.setId(count.incrementAndGet());

        // Create the AssetTransformer
        AssetTransformerDTO assetTransformerDTO = assetTransformerMapper.toDto(assetTransformer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetTransformerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetTransformerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetTransformer in the database
        List<AssetTransformer> assetTransformerList = assetTransformerRepository.findAll();
        assertThat(assetTransformerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssetTransformer() throws Exception {
        int databaseSizeBeforeUpdate = assetTransformerRepository.findAll().size();
        assetTransformer.setId(count.incrementAndGet());

        // Create the AssetTransformer
        AssetTransformerDTO assetTransformerDTO = assetTransformerMapper.toDto(assetTransformer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetTransformerMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetTransformerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetTransformer in the database
        List<AssetTransformer> assetTransformerList = assetTransformerRepository.findAll();
        assertThat(assetTransformerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssetTransformerWithPatch() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        int databaseSizeBeforeUpdate = assetTransformerRepository.findAll().size();

        // Update the assetTransformer using partial update
        AssetTransformer partialUpdatedAssetTransformer = new AssetTransformer();
        partialUpdatedAssetTransformer.setId(assetTransformer.getId());

        partialUpdatedAssetTransformer
            .busNum(UPDATED_BUS_NUM)
            .ratingKva(UPDATED_RATING_KVA)
            .numConnectedCustomers(UPDATED_NUM_CONNECTED_CUSTOMERS)
            .numSensitiveCustomers(UPDATED_NUM_SENSITIVE_CUSTOMERS)
            .backupSupply(UPDATED_BACKUP_SUPPLY);

        restAssetTransformerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetTransformer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetTransformer))
            )
            .andExpect(status().isOk());

        // Validate the AssetTransformer in the database
        List<AssetTransformer> assetTransformerList = assetTransformerRepository.findAll();
        assertThat(assetTransformerList).hasSize(databaseSizeBeforeUpdate);
        AssetTransformer testAssetTransformer = assetTransformerList.get(assetTransformerList.size() - 1);
        assertThat(testAssetTransformer.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testAssetTransformer.getVoltageRatio()).isEqualTo(DEFAULT_VOLTAGE_RATIO);
        assertThat(testAssetTransformer.getInsulationMedium()).isEqualTo(DEFAULT_INSULATION_MEDIUM);
        assertThat(testAssetTransformer.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAssetTransformer.getIndoorOutdoor()).isEqualTo(DEFAULT_INDOOR_OUTDOOR);
        assertThat(testAssetTransformer.getAnnualMaxLoadKva()).isEqualTo(DEFAULT_ANNUAL_MAX_LOAD_KVA);
        assertThat(testAssetTransformer.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testAssetTransformer.getExternalCondition()).isEqualTo(DEFAULT_EXTERNAL_CONDITION);
        assertThat(testAssetTransformer.getRatingKva()).isEqualTo(UPDATED_RATING_KVA);
        assertThat(testAssetTransformer.getNumConnectedCustomers()).isEqualTo(UPDATED_NUM_CONNECTED_CUSTOMERS);
        assertThat(testAssetTransformer.getNumSensitiveCustomers()).isEqualTo(UPDATED_NUM_SENSITIVE_CUSTOMERS);
        assertThat(testAssetTransformer.getBackupSupply()).isEqualTo(UPDATED_BACKUP_SUPPLY);
        assertThat(testAssetTransformer.getCostOfFailureEuro()).isEqualTo(DEFAULT_COST_OF_FAILURE_EURO);
    }

    @Test
    @Transactional
    void fullUpdateAssetTransformerWithPatch() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        int databaseSizeBeforeUpdate = assetTransformerRepository.findAll().size();

        // Update the assetTransformer using partial update
        AssetTransformer partialUpdatedAssetTransformer = new AssetTransformer();
        partialUpdatedAssetTransformer.setId(assetTransformer.getId());

        partialUpdatedAssetTransformer
            .busNum(UPDATED_BUS_NUM)
            .voltageRatio(UPDATED_VOLTAGE_RATIO)
            .insulationMedium(UPDATED_INSULATION_MEDIUM)
            .type(UPDATED_TYPE)
            .indoorOutdoor(UPDATED_INDOOR_OUTDOOR)
            .annualMaxLoadKva(UPDATED_ANNUAL_MAX_LOAD_KVA)
            .age(UPDATED_AGE)
            .externalCondition(UPDATED_EXTERNAL_CONDITION)
            .ratingKva(UPDATED_RATING_KVA)
            .numConnectedCustomers(UPDATED_NUM_CONNECTED_CUSTOMERS)
            .numSensitiveCustomers(UPDATED_NUM_SENSITIVE_CUSTOMERS)
            .backupSupply(UPDATED_BACKUP_SUPPLY)
            .costOfFailureEuro(UPDATED_COST_OF_FAILURE_EURO);

        restAssetTransformerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetTransformer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetTransformer))
            )
            .andExpect(status().isOk());

        // Validate the AssetTransformer in the database
        List<AssetTransformer> assetTransformerList = assetTransformerRepository.findAll();
        assertThat(assetTransformerList).hasSize(databaseSizeBeforeUpdate);
        AssetTransformer testAssetTransformer = assetTransformerList.get(assetTransformerList.size() - 1);
        assertThat(testAssetTransformer.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testAssetTransformer.getVoltageRatio()).isEqualTo(UPDATED_VOLTAGE_RATIO);
        assertThat(testAssetTransformer.getInsulationMedium()).isEqualTo(UPDATED_INSULATION_MEDIUM);
        assertThat(testAssetTransformer.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAssetTransformer.getIndoorOutdoor()).isEqualTo(UPDATED_INDOOR_OUTDOOR);
        assertThat(testAssetTransformer.getAnnualMaxLoadKva()).isEqualTo(UPDATED_ANNUAL_MAX_LOAD_KVA);
        assertThat(testAssetTransformer.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testAssetTransformer.getExternalCondition()).isEqualTo(UPDATED_EXTERNAL_CONDITION);
        assertThat(testAssetTransformer.getRatingKva()).isEqualTo(UPDATED_RATING_KVA);
        assertThat(testAssetTransformer.getNumConnectedCustomers()).isEqualTo(UPDATED_NUM_CONNECTED_CUSTOMERS);
        assertThat(testAssetTransformer.getNumSensitiveCustomers()).isEqualTo(UPDATED_NUM_SENSITIVE_CUSTOMERS);
        assertThat(testAssetTransformer.getBackupSupply()).isEqualTo(UPDATED_BACKUP_SUPPLY);
        assertThat(testAssetTransformer.getCostOfFailureEuro()).isEqualTo(UPDATED_COST_OF_FAILURE_EURO);
    }

    @Test
    @Transactional
    void patchNonExistingAssetTransformer() throws Exception {
        int databaseSizeBeforeUpdate = assetTransformerRepository.findAll().size();
        assetTransformer.setId(count.incrementAndGet());

        // Create the AssetTransformer
        AssetTransformerDTO assetTransformerDTO = assetTransformerMapper.toDto(assetTransformer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetTransformerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetTransformerDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetTransformerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetTransformer in the database
        List<AssetTransformer> assetTransformerList = assetTransformerRepository.findAll();
        assertThat(assetTransformerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssetTransformer() throws Exception {
        int databaseSizeBeforeUpdate = assetTransformerRepository.findAll().size();
        assetTransformer.setId(count.incrementAndGet());

        // Create the AssetTransformer
        AssetTransformerDTO assetTransformerDTO = assetTransformerMapper.toDto(assetTransformer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetTransformerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetTransformerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetTransformer in the database
        List<AssetTransformer> assetTransformerList = assetTransformerRepository.findAll();
        assertThat(assetTransformerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssetTransformer() throws Exception {
        int databaseSizeBeforeUpdate = assetTransformerRepository.findAll().size();
        assetTransformer.setId(count.incrementAndGet());

        // Create the AssetTransformer
        AssetTransformerDTO assetTransformerDTO = assetTransformerMapper.toDto(assetTransformer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetTransformerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetTransformerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetTransformer in the database
        List<AssetTransformer> assetTransformerList = assetTransformerRepository.findAll();
        assertThat(assetTransformerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssetTransformer() throws Exception {
        // Initialize the database
        assetTransformerRepository.saveAndFlush(assetTransformer);

        int databaseSizeBeforeDelete = assetTransformerRepository.findAll().size();

        // Delete the assetTransformer
        restAssetTransformerMockMvc
            .perform(delete(ENTITY_API_URL_ID, assetTransformer.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssetTransformer> assetTransformerList = assetTransformerRepository.findAll();
        assertThat(assetTransformerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
