package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.AssetUGCable;
import com.attest.ict.domain.Network;
import com.attest.ict.repository.AssetUGCableRepository;
import com.attest.ict.service.criteria.AssetUGCableCriteria;
import com.attest.ict.service.dto.AssetUGCableDTO;
import com.attest.ict.service.mapper.AssetUGCableMapper;
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
 * Integration tests for the {@link AssetUGCableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AssetUGCableResourceIT {

    private static final String DEFAULT_SECTION_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_SECTION_LABEL = "BBBBBBBBBB";

    private static final Long DEFAULT_CIRCUIT_ID = 1L;
    private static final Long UPDATED_CIRCUIT_ID = 2L;
    private static final Long SMALLER_CIRCUIT_ID = 1L - 1L;

    private static final Integer DEFAULT_CONDUCTOR_CROSS_SECTIONAL_AREA = 1;
    private static final Integer UPDATED_CONDUCTOR_CROSS_SECTIONAL_AREA = 2;
    private static final Integer SMALLER_CONDUCTOR_CROSS_SECTIONAL_AREA = 1 - 1;

    private static final String DEFAULT_SHEATH_MATERIAL = "AAAAAAAAAA";
    private static final String UPDATED_SHEATH_MATERIAL = "BBBBBBBBBB";

    private static final String DEFAULT_DESIGN_VOLTAGE = "AAAAAAAAAA";
    private static final String UPDATED_DESIGN_VOLTAGE = "BBBBBBBBBB";

    private static final String DEFAULT_OPERATING_VOLTAGE = "AAAAAAAAAA";
    private static final String UPDATED_OPERATING_VOLTAGE = "BBBBBBBBBB";

    private static final String DEFAULT_INSULATION_TYPE_SHEATH = "AAAAAAAAAA";
    private static final String UPDATED_INSULATION_TYPE_SHEATH = "BBBBBBBBBB";

    private static final String DEFAULT_CONDUCTOR_MATERIAL = "AAAAAAAAAA";
    private static final String UPDATED_CONDUCTOR_MATERIAL = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;
    private static final Integer SMALLER_AGE = 1 - 1;

    private static final Integer DEFAULT_FAULT_HISTORY = 1;
    private static final Integer UPDATED_FAULT_HISTORY = 2;
    private static final Integer SMALLER_FAULT_HISTORY = 1 - 1;

    private static final Integer DEFAULT_LENGTH_OF_CABLE_SECTION_METERS = 1;
    private static final Integer UPDATED_LENGTH_OF_CABLE_SECTION_METERS = 2;
    private static final Integer SMALLER_LENGTH_OF_CABLE_SECTION_METERS = 1 - 1;

    private static final Integer DEFAULT_SECTION_RATING = 1;
    private static final Integer UPDATED_SECTION_RATING = 2;
    private static final Integer SMALLER_SECTION_RATING = 1 - 1;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_CORES = 1;
    private static final Integer UPDATED_NUMBER_OF_CORES = 2;
    private static final Integer SMALLER_NUMBER_OF_CORES = 1 - 1;

    private static final String DEFAULT_NET_PERFORMANCE_COST_OF_FAILURE_EURO = "AAAAAAAAAA";
    private static final String UPDATED_NET_PERFORMANCE_COST_OF_FAILURE_EURO = "BBBBBBBBBB";

    private static final Integer DEFAULT_REPAIR_TIME_HOUR = 1;
    private static final Integer UPDATED_REPAIR_TIME_HOUR = 2;
    private static final Integer SMALLER_REPAIR_TIME_HOUR = 1 - 1;

    private static final String ENTITY_API_URL = "/api/asset-ug-cables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssetUGCableRepository assetUGCableRepository;

    @Autowired
    private AssetUGCableMapper assetUGCableMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetUGCableMockMvc;

    private AssetUGCable assetUGCable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetUGCable createEntity(EntityManager em) {
        AssetUGCable assetUGCable = new AssetUGCable()
            .sectionLabel(DEFAULT_SECTION_LABEL)
            .circuitId(DEFAULT_CIRCUIT_ID)
            .conductorCrossSectionalArea(DEFAULT_CONDUCTOR_CROSS_SECTIONAL_AREA)
            .sheathMaterial(DEFAULT_SHEATH_MATERIAL)
            .designVoltage(DEFAULT_DESIGN_VOLTAGE)
            .operatingVoltage(DEFAULT_OPERATING_VOLTAGE)
            .insulationTypeSheath(DEFAULT_INSULATION_TYPE_SHEATH)
            .conductorMaterial(DEFAULT_CONDUCTOR_MATERIAL)
            .age(DEFAULT_AGE)
            .faultHistory(DEFAULT_FAULT_HISTORY)
            .lengthOfCableSectionMeters(DEFAULT_LENGTH_OF_CABLE_SECTION_METERS)
            .sectionRating(DEFAULT_SECTION_RATING)
            .type(DEFAULT_TYPE)
            .numberOfCores(DEFAULT_NUMBER_OF_CORES)
            .netPerformanceCostOfFailureEuro(DEFAULT_NET_PERFORMANCE_COST_OF_FAILURE_EURO)
            .repairTimeHour(DEFAULT_REPAIR_TIME_HOUR);
        return assetUGCable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetUGCable createUpdatedEntity(EntityManager em) {
        AssetUGCable assetUGCable = new AssetUGCable()
            .sectionLabel(UPDATED_SECTION_LABEL)
            .circuitId(UPDATED_CIRCUIT_ID)
            .conductorCrossSectionalArea(UPDATED_CONDUCTOR_CROSS_SECTIONAL_AREA)
            .sheathMaterial(UPDATED_SHEATH_MATERIAL)
            .designVoltage(UPDATED_DESIGN_VOLTAGE)
            .operatingVoltage(UPDATED_OPERATING_VOLTAGE)
            .insulationTypeSheath(UPDATED_INSULATION_TYPE_SHEATH)
            .conductorMaterial(UPDATED_CONDUCTOR_MATERIAL)
            .age(UPDATED_AGE)
            .faultHistory(UPDATED_FAULT_HISTORY)
            .lengthOfCableSectionMeters(UPDATED_LENGTH_OF_CABLE_SECTION_METERS)
            .sectionRating(UPDATED_SECTION_RATING)
            .type(UPDATED_TYPE)
            .numberOfCores(UPDATED_NUMBER_OF_CORES)
            .netPerformanceCostOfFailureEuro(UPDATED_NET_PERFORMANCE_COST_OF_FAILURE_EURO)
            .repairTimeHour(UPDATED_REPAIR_TIME_HOUR);
        return assetUGCable;
    }

    @BeforeEach
    public void initTest() {
        assetUGCable = createEntity(em);
    }

    @Test
    @Transactional
    void createAssetUGCable() throws Exception {
        int databaseSizeBeforeCreate = assetUGCableRepository.findAll().size();
        // Create the AssetUGCable
        AssetUGCableDTO assetUGCableDTO = assetUGCableMapper.toDto(assetUGCable);
        restAssetUGCableMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetUGCableDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AssetUGCable in the database
        List<AssetUGCable> assetUGCableList = assetUGCableRepository.findAll();
        assertThat(assetUGCableList).hasSize(databaseSizeBeforeCreate + 1);
        AssetUGCable testAssetUGCable = assetUGCableList.get(assetUGCableList.size() - 1);
        assertThat(testAssetUGCable.getSectionLabel()).isEqualTo(DEFAULT_SECTION_LABEL);
        assertThat(testAssetUGCable.getCircuitId()).isEqualTo(DEFAULT_CIRCUIT_ID);
        assertThat(testAssetUGCable.getConductorCrossSectionalArea()).isEqualTo(DEFAULT_CONDUCTOR_CROSS_SECTIONAL_AREA);
        assertThat(testAssetUGCable.getSheathMaterial()).isEqualTo(DEFAULT_SHEATH_MATERIAL);
        assertThat(testAssetUGCable.getDesignVoltage()).isEqualTo(DEFAULT_DESIGN_VOLTAGE);
        assertThat(testAssetUGCable.getOperatingVoltage()).isEqualTo(DEFAULT_OPERATING_VOLTAGE);
        assertThat(testAssetUGCable.getInsulationTypeSheath()).isEqualTo(DEFAULT_INSULATION_TYPE_SHEATH);
        assertThat(testAssetUGCable.getConductorMaterial()).isEqualTo(DEFAULT_CONDUCTOR_MATERIAL);
        assertThat(testAssetUGCable.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testAssetUGCable.getFaultHistory()).isEqualTo(DEFAULT_FAULT_HISTORY);
        assertThat(testAssetUGCable.getLengthOfCableSectionMeters()).isEqualTo(DEFAULT_LENGTH_OF_CABLE_SECTION_METERS);
        assertThat(testAssetUGCable.getSectionRating()).isEqualTo(DEFAULT_SECTION_RATING);
        assertThat(testAssetUGCable.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAssetUGCable.getNumberOfCores()).isEqualTo(DEFAULT_NUMBER_OF_CORES);
        assertThat(testAssetUGCable.getNetPerformanceCostOfFailureEuro()).isEqualTo(DEFAULT_NET_PERFORMANCE_COST_OF_FAILURE_EURO);
        assertThat(testAssetUGCable.getRepairTimeHour()).isEqualTo(DEFAULT_REPAIR_TIME_HOUR);
    }

    @Test
    @Transactional
    void createAssetUGCableWithExistingId() throws Exception {
        // Create the AssetUGCable with an existing ID
        assetUGCable.setId(1L);
        AssetUGCableDTO assetUGCableDTO = assetUGCableMapper.toDto(assetUGCable);

        int databaseSizeBeforeCreate = assetUGCableRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetUGCableMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetUGCableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetUGCable in the database
        List<AssetUGCable> assetUGCableList = assetUGCableRepository.findAll();
        assertThat(assetUGCableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAssetUGCables() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList
        restAssetUGCableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetUGCable.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectionLabel").value(hasItem(DEFAULT_SECTION_LABEL)))
            .andExpect(jsonPath("$.[*].circuitId").value(hasItem(DEFAULT_CIRCUIT_ID.intValue())))
            .andExpect(jsonPath("$.[*].conductorCrossSectionalArea").value(hasItem(DEFAULT_CONDUCTOR_CROSS_SECTIONAL_AREA)))
            .andExpect(jsonPath("$.[*].sheathMaterial").value(hasItem(DEFAULT_SHEATH_MATERIAL)))
            .andExpect(jsonPath("$.[*].designVoltage").value(hasItem(DEFAULT_DESIGN_VOLTAGE)))
            .andExpect(jsonPath("$.[*].operatingVoltage").value(hasItem(DEFAULT_OPERATING_VOLTAGE)))
            .andExpect(jsonPath("$.[*].insulationTypeSheath").value(hasItem(DEFAULT_INSULATION_TYPE_SHEATH)))
            .andExpect(jsonPath("$.[*].conductorMaterial").value(hasItem(DEFAULT_CONDUCTOR_MATERIAL)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].faultHistory").value(hasItem(DEFAULT_FAULT_HISTORY)))
            .andExpect(jsonPath("$.[*].lengthOfCableSectionMeters").value(hasItem(DEFAULT_LENGTH_OF_CABLE_SECTION_METERS)))
            .andExpect(jsonPath("$.[*].sectionRating").value(hasItem(DEFAULT_SECTION_RATING)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].numberOfCores").value(hasItem(DEFAULT_NUMBER_OF_CORES)))
            .andExpect(jsonPath("$.[*].netPerformanceCostOfFailureEuro").value(hasItem(DEFAULT_NET_PERFORMANCE_COST_OF_FAILURE_EURO)))
            .andExpect(jsonPath("$.[*].repairTimeHour").value(hasItem(DEFAULT_REPAIR_TIME_HOUR)));
    }

    @Test
    @Transactional
    void getAssetUGCable() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get the assetUGCable
        restAssetUGCableMockMvc
            .perform(get(ENTITY_API_URL_ID, assetUGCable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assetUGCable.getId().intValue()))
            .andExpect(jsonPath("$.sectionLabel").value(DEFAULT_SECTION_LABEL))
            .andExpect(jsonPath("$.circuitId").value(DEFAULT_CIRCUIT_ID.intValue()))
            .andExpect(jsonPath("$.conductorCrossSectionalArea").value(DEFAULT_CONDUCTOR_CROSS_SECTIONAL_AREA))
            .andExpect(jsonPath("$.sheathMaterial").value(DEFAULT_SHEATH_MATERIAL))
            .andExpect(jsonPath("$.designVoltage").value(DEFAULT_DESIGN_VOLTAGE))
            .andExpect(jsonPath("$.operatingVoltage").value(DEFAULT_OPERATING_VOLTAGE))
            .andExpect(jsonPath("$.insulationTypeSheath").value(DEFAULT_INSULATION_TYPE_SHEATH))
            .andExpect(jsonPath("$.conductorMaterial").value(DEFAULT_CONDUCTOR_MATERIAL))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.faultHistory").value(DEFAULT_FAULT_HISTORY))
            .andExpect(jsonPath("$.lengthOfCableSectionMeters").value(DEFAULT_LENGTH_OF_CABLE_SECTION_METERS))
            .andExpect(jsonPath("$.sectionRating").value(DEFAULT_SECTION_RATING))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.numberOfCores").value(DEFAULT_NUMBER_OF_CORES))
            .andExpect(jsonPath("$.netPerformanceCostOfFailureEuro").value(DEFAULT_NET_PERFORMANCE_COST_OF_FAILURE_EURO))
            .andExpect(jsonPath("$.repairTimeHour").value(DEFAULT_REPAIR_TIME_HOUR));
    }

    @Test
    @Transactional
    void getAssetUGCablesByIdFiltering() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        Long id = assetUGCable.getId();

        defaultAssetUGCableShouldBeFound("id.equals=" + id);
        defaultAssetUGCableShouldNotBeFound("id.notEquals=" + id);

        defaultAssetUGCableShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssetUGCableShouldNotBeFound("id.greaterThan=" + id);

        defaultAssetUGCableShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssetUGCableShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesBySectionLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where sectionLabel equals to DEFAULT_SECTION_LABEL
        defaultAssetUGCableShouldBeFound("sectionLabel.equals=" + DEFAULT_SECTION_LABEL);

        // Get all the assetUGCableList where sectionLabel equals to UPDATED_SECTION_LABEL
        defaultAssetUGCableShouldNotBeFound("sectionLabel.equals=" + UPDATED_SECTION_LABEL);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesBySectionLabelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where sectionLabel not equals to DEFAULT_SECTION_LABEL
        defaultAssetUGCableShouldNotBeFound("sectionLabel.notEquals=" + DEFAULT_SECTION_LABEL);

        // Get all the assetUGCableList where sectionLabel not equals to UPDATED_SECTION_LABEL
        defaultAssetUGCableShouldBeFound("sectionLabel.notEquals=" + UPDATED_SECTION_LABEL);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesBySectionLabelIsInShouldWork() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where sectionLabel in DEFAULT_SECTION_LABEL or UPDATED_SECTION_LABEL
        defaultAssetUGCableShouldBeFound("sectionLabel.in=" + DEFAULT_SECTION_LABEL + "," + UPDATED_SECTION_LABEL);

        // Get all the assetUGCableList where sectionLabel equals to UPDATED_SECTION_LABEL
        defaultAssetUGCableShouldNotBeFound("sectionLabel.in=" + UPDATED_SECTION_LABEL);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesBySectionLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where sectionLabel is not null
        defaultAssetUGCableShouldBeFound("sectionLabel.specified=true");

        // Get all the assetUGCableList where sectionLabel is null
        defaultAssetUGCableShouldNotBeFound("sectionLabel.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetUGCablesBySectionLabelContainsSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where sectionLabel contains DEFAULT_SECTION_LABEL
        defaultAssetUGCableShouldBeFound("sectionLabel.contains=" + DEFAULT_SECTION_LABEL);

        // Get all the assetUGCableList where sectionLabel contains UPDATED_SECTION_LABEL
        defaultAssetUGCableShouldNotBeFound("sectionLabel.contains=" + UPDATED_SECTION_LABEL);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesBySectionLabelNotContainsSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where sectionLabel does not contain DEFAULT_SECTION_LABEL
        defaultAssetUGCableShouldNotBeFound("sectionLabel.doesNotContain=" + DEFAULT_SECTION_LABEL);

        // Get all the assetUGCableList where sectionLabel does not contain UPDATED_SECTION_LABEL
        defaultAssetUGCableShouldBeFound("sectionLabel.doesNotContain=" + UPDATED_SECTION_LABEL);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByCircuitIdIsEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where circuitId equals to DEFAULT_CIRCUIT_ID
        defaultAssetUGCableShouldBeFound("circuitId.equals=" + DEFAULT_CIRCUIT_ID);

        // Get all the assetUGCableList where circuitId equals to UPDATED_CIRCUIT_ID
        defaultAssetUGCableShouldNotBeFound("circuitId.equals=" + UPDATED_CIRCUIT_ID);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByCircuitIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where circuitId not equals to DEFAULT_CIRCUIT_ID
        defaultAssetUGCableShouldNotBeFound("circuitId.notEquals=" + DEFAULT_CIRCUIT_ID);

        // Get all the assetUGCableList where circuitId not equals to UPDATED_CIRCUIT_ID
        defaultAssetUGCableShouldBeFound("circuitId.notEquals=" + UPDATED_CIRCUIT_ID);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByCircuitIdIsInShouldWork() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where circuitId in DEFAULT_CIRCUIT_ID or UPDATED_CIRCUIT_ID
        defaultAssetUGCableShouldBeFound("circuitId.in=" + DEFAULT_CIRCUIT_ID + "," + UPDATED_CIRCUIT_ID);

        // Get all the assetUGCableList where circuitId equals to UPDATED_CIRCUIT_ID
        defaultAssetUGCableShouldNotBeFound("circuitId.in=" + UPDATED_CIRCUIT_ID);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByCircuitIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where circuitId is not null
        defaultAssetUGCableShouldBeFound("circuitId.specified=true");

        // Get all the assetUGCableList where circuitId is null
        defaultAssetUGCableShouldNotBeFound("circuitId.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByCircuitIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where circuitId is greater than or equal to DEFAULT_CIRCUIT_ID
        defaultAssetUGCableShouldBeFound("circuitId.greaterThanOrEqual=" + DEFAULT_CIRCUIT_ID);

        // Get all the assetUGCableList where circuitId is greater than or equal to UPDATED_CIRCUIT_ID
        defaultAssetUGCableShouldNotBeFound("circuitId.greaterThanOrEqual=" + UPDATED_CIRCUIT_ID);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByCircuitIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where circuitId is less than or equal to DEFAULT_CIRCUIT_ID
        defaultAssetUGCableShouldBeFound("circuitId.lessThanOrEqual=" + DEFAULT_CIRCUIT_ID);

        // Get all the assetUGCableList where circuitId is less than or equal to SMALLER_CIRCUIT_ID
        defaultAssetUGCableShouldNotBeFound("circuitId.lessThanOrEqual=" + SMALLER_CIRCUIT_ID);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByCircuitIdIsLessThanSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where circuitId is less than DEFAULT_CIRCUIT_ID
        defaultAssetUGCableShouldNotBeFound("circuitId.lessThan=" + DEFAULT_CIRCUIT_ID);

        // Get all the assetUGCableList where circuitId is less than UPDATED_CIRCUIT_ID
        defaultAssetUGCableShouldBeFound("circuitId.lessThan=" + UPDATED_CIRCUIT_ID);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByCircuitIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where circuitId is greater than DEFAULT_CIRCUIT_ID
        defaultAssetUGCableShouldNotBeFound("circuitId.greaterThan=" + DEFAULT_CIRCUIT_ID);

        // Get all the assetUGCableList where circuitId is greater than SMALLER_CIRCUIT_ID
        defaultAssetUGCableShouldBeFound("circuitId.greaterThan=" + SMALLER_CIRCUIT_ID);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByConductorCrossSectionalAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where conductorCrossSectionalArea equals to DEFAULT_CONDUCTOR_CROSS_SECTIONAL_AREA
        defaultAssetUGCableShouldBeFound("conductorCrossSectionalArea.equals=" + DEFAULT_CONDUCTOR_CROSS_SECTIONAL_AREA);

        // Get all the assetUGCableList where conductorCrossSectionalArea equals to UPDATED_CONDUCTOR_CROSS_SECTIONAL_AREA
        defaultAssetUGCableShouldNotBeFound("conductorCrossSectionalArea.equals=" + UPDATED_CONDUCTOR_CROSS_SECTIONAL_AREA);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByConductorCrossSectionalAreaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where conductorCrossSectionalArea not equals to DEFAULT_CONDUCTOR_CROSS_SECTIONAL_AREA
        defaultAssetUGCableShouldNotBeFound("conductorCrossSectionalArea.notEquals=" + DEFAULT_CONDUCTOR_CROSS_SECTIONAL_AREA);

        // Get all the assetUGCableList where conductorCrossSectionalArea not equals to UPDATED_CONDUCTOR_CROSS_SECTIONAL_AREA
        defaultAssetUGCableShouldBeFound("conductorCrossSectionalArea.notEquals=" + UPDATED_CONDUCTOR_CROSS_SECTIONAL_AREA);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByConductorCrossSectionalAreaIsInShouldWork() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where conductorCrossSectionalArea in DEFAULT_CONDUCTOR_CROSS_SECTIONAL_AREA or UPDATED_CONDUCTOR_CROSS_SECTIONAL_AREA
        defaultAssetUGCableShouldBeFound(
            "conductorCrossSectionalArea.in=" + DEFAULT_CONDUCTOR_CROSS_SECTIONAL_AREA + "," + UPDATED_CONDUCTOR_CROSS_SECTIONAL_AREA
        );

        // Get all the assetUGCableList where conductorCrossSectionalArea equals to UPDATED_CONDUCTOR_CROSS_SECTIONAL_AREA
        defaultAssetUGCableShouldNotBeFound("conductorCrossSectionalArea.in=" + UPDATED_CONDUCTOR_CROSS_SECTIONAL_AREA);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByConductorCrossSectionalAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where conductorCrossSectionalArea is not null
        defaultAssetUGCableShouldBeFound("conductorCrossSectionalArea.specified=true");

        // Get all the assetUGCableList where conductorCrossSectionalArea is null
        defaultAssetUGCableShouldNotBeFound("conductorCrossSectionalArea.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByConductorCrossSectionalAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where conductorCrossSectionalArea is greater than or equal to DEFAULT_CONDUCTOR_CROSS_SECTIONAL_AREA
        defaultAssetUGCableShouldBeFound("conductorCrossSectionalArea.greaterThanOrEqual=" + DEFAULT_CONDUCTOR_CROSS_SECTIONAL_AREA);

        // Get all the assetUGCableList where conductorCrossSectionalArea is greater than or equal to UPDATED_CONDUCTOR_CROSS_SECTIONAL_AREA
        defaultAssetUGCableShouldNotBeFound("conductorCrossSectionalArea.greaterThanOrEqual=" + UPDATED_CONDUCTOR_CROSS_SECTIONAL_AREA);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByConductorCrossSectionalAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where conductorCrossSectionalArea is less than or equal to DEFAULT_CONDUCTOR_CROSS_SECTIONAL_AREA
        defaultAssetUGCableShouldBeFound("conductorCrossSectionalArea.lessThanOrEqual=" + DEFAULT_CONDUCTOR_CROSS_SECTIONAL_AREA);

        // Get all the assetUGCableList where conductorCrossSectionalArea is less than or equal to SMALLER_CONDUCTOR_CROSS_SECTIONAL_AREA
        defaultAssetUGCableShouldNotBeFound("conductorCrossSectionalArea.lessThanOrEqual=" + SMALLER_CONDUCTOR_CROSS_SECTIONAL_AREA);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByConductorCrossSectionalAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where conductorCrossSectionalArea is less than DEFAULT_CONDUCTOR_CROSS_SECTIONAL_AREA
        defaultAssetUGCableShouldNotBeFound("conductorCrossSectionalArea.lessThan=" + DEFAULT_CONDUCTOR_CROSS_SECTIONAL_AREA);

        // Get all the assetUGCableList where conductorCrossSectionalArea is less than UPDATED_CONDUCTOR_CROSS_SECTIONAL_AREA
        defaultAssetUGCableShouldBeFound("conductorCrossSectionalArea.lessThan=" + UPDATED_CONDUCTOR_CROSS_SECTIONAL_AREA);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByConductorCrossSectionalAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where conductorCrossSectionalArea is greater than DEFAULT_CONDUCTOR_CROSS_SECTIONAL_AREA
        defaultAssetUGCableShouldNotBeFound("conductorCrossSectionalArea.greaterThan=" + DEFAULT_CONDUCTOR_CROSS_SECTIONAL_AREA);

        // Get all the assetUGCableList where conductorCrossSectionalArea is greater than SMALLER_CONDUCTOR_CROSS_SECTIONAL_AREA
        defaultAssetUGCableShouldBeFound("conductorCrossSectionalArea.greaterThan=" + SMALLER_CONDUCTOR_CROSS_SECTIONAL_AREA);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesBySheathMaterialIsEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where sheathMaterial equals to DEFAULT_SHEATH_MATERIAL
        defaultAssetUGCableShouldBeFound("sheathMaterial.equals=" + DEFAULT_SHEATH_MATERIAL);

        // Get all the assetUGCableList where sheathMaterial equals to UPDATED_SHEATH_MATERIAL
        defaultAssetUGCableShouldNotBeFound("sheathMaterial.equals=" + UPDATED_SHEATH_MATERIAL);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesBySheathMaterialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where sheathMaterial not equals to DEFAULT_SHEATH_MATERIAL
        defaultAssetUGCableShouldNotBeFound("sheathMaterial.notEquals=" + DEFAULT_SHEATH_MATERIAL);

        // Get all the assetUGCableList where sheathMaterial not equals to UPDATED_SHEATH_MATERIAL
        defaultAssetUGCableShouldBeFound("sheathMaterial.notEquals=" + UPDATED_SHEATH_MATERIAL);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesBySheathMaterialIsInShouldWork() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where sheathMaterial in DEFAULT_SHEATH_MATERIAL or UPDATED_SHEATH_MATERIAL
        defaultAssetUGCableShouldBeFound("sheathMaterial.in=" + DEFAULT_SHEATH_MATERIAL + "," + UPDATED_SHEATH_MATERIAL);

        // Get all the assetUGCableList where sheathMaterial equals to UPDATED_SHEATH_MATERIAL
        defaultAssetUGCableShouldNotBeFound("sheathMaterial.in=" + UPDATED_SHEATH_MATERIAL);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesBySheathMaterialIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where sheathMaterial is not null
        defaultAssetUGCableShouldBeFound("sheathMaterial.specified=true");

        // Get all the assetUGCableList where sheathMaterial is null
        defaultAssetUGCableShouldNotBeFound("sheathMaterial.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetUGCablesBySheathMaterialContainsSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where sheathMaterial contains DEFAULT_SHEATH_MATERIAL
        defaultAssetUGCableShouldBeFound("sheathMaterial.contains=" + DEFAULT_SHEATH_MATERIAL);

        // Get all the assetUGCableList where sheathMaterial contains UPDATED_SHEATH_MATERIAL
        defaultAssetUGCableShouldNotBeFound("sheathMaterial.contains=" + UPDATED_SHEATH_MATERIAL);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesBySheathMaterialNotContainsSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where sheathMaterial does not contain DEFAULT_SHEATH_MATERIAL
        defaultAssetUGCableShouldNotBeFound("sheathMaterial.doesNotContain=" + DEFAULT_SHEATH_MATERIAL);

        // Get all the assetUGCableList where sheathMaterial does not contain UPDATED_SHEATH_MATERIAL
        defaultAssetUGCableShouldBeFound("sheathMaterial.doesNotContain=" + UPDATED_SHEATH_MATERIAL);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByDesignVoltageIsEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where designVoltage equals to DEFAULT_DESIGN_VOLTAGE
        defaultAssetUGCableShouldBeFound("designVoltage.equals=" + DEFAULT_DESIGN_VOLTAGE);

        // Get all the assetUGCableList where designVoltage equals to UPDATED_DESIGN_VOLTAGE
        defaultAssetUGCableShouldNotBeFound("designVoltage.equals=" + UPDATED_DESIGN_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByDesignVoltageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where designVoltage not equals to DEFAULT_DESIGN_VOLTAGE
        defaultAssetUGCableShouldNotBeFound("designVoltage.notEquals=" + DEFAULT_DESIGN_VOLTAGE);

        // Get all the assetUGCableList where designVoltage not equals to UPDATED_DESIGN_VOLTAGE
        defaultAssetUGCableShouldBeFound("designVoltage.notEquals=" + UPDATED_DESIGN_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByDesignVoltageIsInShouldWork() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where designVoltage in DEFAULT_DESIGN_VOLTAGE or UPDATED_DESIGN_VOLTAGE
        defaultAssetUGCableShouldBeFound("designVoltage.in=" + DEFAULT_DESIGN_VOLTAGE + "," + UPDATED_DESIGN_VOLTAGE);

        // Get all the assetUGCableList where designVoltage equals to UPDATED_DESIGN_VOLTAGE
        defaultAssetUGCableShouldNotBeFound("designVoltage.in=" + UPDATED_DESIGN_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByDesignVoltageIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where designVoltage is not null
        defaultAssetUGCableShouldBeFound("designVoltage.specified=true");

        // Get all the assetUGCableList where designVoltage is null
        defaultAssetUGCableShouldNotBeFound("designVoltage.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByDesignVoltageContainsSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where designVoltage contains DEFAULT_DESIGN_VOLTAGE
        defaultAssetUGCableShouldBeFound("designVoltage.contains=" + DEFAULT_DESIGN_VOLTAGE);

        // Get all the assetUGCableList where designVoltage contains UPDATED_DESIGN_VOLTAGE
        defaultAssetUGCableShouldNotBeFound("designVoltage.contains=" + UPDATED_DESIGN_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByDesignVoltageNotContainsSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where designVoltage does not contain DEFAULT_DESIGN_VOLTAGE
        defaultAssetUGCableShouldNotBeFound("designVoltage.doesNotContain=" + DEFAULT_DESIGN_VOLTAGE);

        // Get all the assetUGCableList where designVoltage does not contain UPDATED_DESIGN_VOLTAGE
        defaultAssetUGCableShouldBeFound("designVoltage.doesNotContain=" + UPDATED_DESIGN_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByOperatingVoltageIsEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where operatingVoltage equals to DEFAULT_OPERATING_VOLTAGE
        defaultAssetUGCableShouldBeFound("operatingVoltage.equals=" + DEFAULT_OPERATING_VOLTAGE);

        // Get all the assetUGCableList where operatingVoltage equals to UPDATED_OPERATING_VOLTAGE
        defaultAssetUGCableShouldNotBeFound("operatingVoltage.equals=" + UPDATED_OPERATING_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByOperatingVoltageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where operatingVoltage not equals to DEFAULT_OPERATING_VOLTAGE
        defaultAssetUGCableShouldNotBeFound("operatingVoltage.notEquals=" + DEFAULT_OPERATING_VOLTAGE);

        // Get all the assetUGCableList where operatingVoltage not equals to UPDATED_OPERATING_VOLTAGE
        defaultAssetUGCableShouldBeFound("operatingVoltage.notEquals=" + UPDATED_OPERATING_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByOperatingVoltageIsInShouldWork() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where operatingVoltage in DEFAULT_OPERATING_VOLTAGE or UPDATED_OPERATING_VOLTAGE
        defaultAssetUGCableShouldBeFound("operatingVoltage.in=" + DEFAULT_OPERATING_VOLTAGE + "," + UPDATED_OPERATING_VOLTAGE);

        // Get all the assetUGCableList where operatingVoltage equals to UPDATED_OPERATING_VOLTAGE
        defaultAssetUGCableShouldNotBeFound("operatingVoltage.in=" + UPDATED_OPERATING_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByOperatingVoltageIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where operatingVoltage is not null
        defaultAssetUGCableShouldBeFound("operatingVoltage.specified=true");

        // Get all the assetUGCableList where operatingVoltage is null
        defaultAssetUGCableShouldNotBeFound("operatingVoltage.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByOperatingVoltageContainsSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where operatingVoltage contains DEFAULT_OPERATING_VOLTAGE
        defaultAssetUGCableShouldBeFound("operatingVoltage.contains=" + DEFAULT_OPERATING_VOLTAGE);

        // Get all the assetUGCableList where operatingVoltage contains UPDATED_OPERATING_VOLTAGE
        defaultAssetUGCableShouldNotBeFound("operatingVoltage.contains=" + UPDATED_OPERATING_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByOperatingVoltageNotContainsSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where operatingVoltage does not contain DEFAULT_OPERATING_VOLTAGE
        defaultAssetUGCableShouldNotBeFound("operatingVoltage.doesNotContain=" + DEFAULT_OPERATING_VOLTAGE);

        // Get all the assetUGCableList where operatingVoltage does not contain UPDATED_OPERATING_VOLTAGE
        defaultAssetUGCableShouldBeFound("operatingVoltage.doesNotContain=" + UPDATED_OPERATING_VOLTAGE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByInsulationTypeSheathIsEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where insulationTypeSheath equals to DEFAULT_INSULATION_TYPE_SHEATH
        defaultAssetUGCableShouldBeFound("insulationTypeSheath.equals=" + DEFAULT_INSULATION_TYPE_SHEATH);

        // Get all the assetUGCableList where insulationTypeSheath equals to UPDATED_INSULATION_TYPE_SHEATH
        defaultAssetUGCableShouldNotBeFound("insulationTypeSheath.equals=" + UPDATED_INSULATION_TYPE_SHEATH);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByInsulationTypeSheathIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where insulationTypeSheath not equals to DEFAULT_INSULATION_TYPE_SHEATH
        defaultAssetUGCableShouldNotBeFound("insulationTypeSheath.notEquals=" + DEFAULT_INSULATION_TYPE_SHEATH);

        // Get all the assetUGCableList where insulationTypeSheath not equals to UPDATED_INSULATION_TYPE_SHEATH
        defaultAssetUGCableShouldBeFound("insulationTypeSheath.notEquals=" + UPDATED_INSULATION_TYPE_SHEATH);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByInsulationTypeSheathIsInShouldWork() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where insulationTypeSheath in DEFAULT_INSULATION_TYPE_SHEATH or UPDATED_INSULATION_TYPE_SHEATH
        defaultAssetUGCableShouldBeFound(
            "insulationTypeSheath.in=" + DEFAULT_INSULATION_TYPE_SHEATH + "," + UPDATED_INSULATION_TYPE_SHEATH
        );

        // Get all the assetUGCableList where insulationTypeSheath equals to UPDATED_INSULATION_TYPE_SHEATH
        defaultAssetUGCableShouldNotBeFound("insulationTypeSheath.in=" + UPDATED_INSULATION_TYPE_SHEATH);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByInsulationTypeSheathIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where insulationTypeSheath is not null
        defaultAssetUGCableShouldBeFound("insulationTypeSheath.specified=true");

        // Get all the assetUGCableList where insulationTypeSheath is null
        defaultAssetUGCableShouldNotBeFound("insulationTypeSheath.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByInsulationTypeSheathContainsSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where insulationTypeSheath contains DEFAULT_INSULATION_TYPE_SHEATH
        defaultAssetUGCableShouldBeFound("insulationTypeSheath.contains=" + DEFAULT_INSULATION_TYPE_SHEATH);

        // Get all the assetUGCableList where insulationTypeSheath contains UPDATED_INSULATION_TYPE_SHEATH
        defaultAssetUGCableShouldNotBeFound("insulationTypeSheath.contains=" + UPDATED_INSULATION_TYPE_SHEATH);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByInsulationTypeSheathNotContainsSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where insulationTypeSheath does not contain DEFAULT_INSULATION_TYPE_SHEATH
        defaultAssetUGCableShouldNotBeFound("insulationTypeSheath.doesNotContain=" + DEFAULT_INSULATION_TYPE_SHEATH);

        // Get all the assetUGCableList where insulationTypeSheath does not contain UPDATED_INSULATION_TYPE_SHEATH
        defaultAssetUGCableShouldBeFound("insulationTypeSheath.doesNotContain=" + UPDATED_INSULATION_TYPE_SHEATH);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByConductorMaterialIsEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where conductorMaterial equals to DEFAULT_CONDUCTOR_MATERIAL
        defaultAssetUGCableShouldBeFound("conductorMaterial.equals=" + DEFAULT_CONDUCTOR_MATERIAL);

        // Get all the assetUGCableList where conductorMaterial equals to UPDATED_CONDUCTOR_MATERIAL
        defaultAssetUGCableShouldNotBeFound("conductorMaterial.equals=" + UPDATED_CONDUCTOR_MATERIAL);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByConductorMaterialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where conductorMaterial not equals to DEFAULT_CONDUCTOR_MATERIAL
        defaultAssetUGCableShouldNotBeFound("conductorMaterial.notEquals=" + DEFAULT_CONDUCTOR_MATERIAL);

        // Get all the assetUGCableList where conductorMaterial not equals to UPDATED_CONDUCTOR_MATERIAL
        defaultAssetUGCableShouldBeFound("conductorMaterial.notEquals=" + UPDATED_CONDUCTOR_MATERIAL);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByConductorMaterialIsInShouldWork() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where conductorMaterial in DEFAULT_CONDUCTOR_MATERIAL or UPDATED_CONDUCTOR_MATERIAL
        defaultAssetUGCableShouldBeFound("conductorMaterial.in=" + DEFAULT_CONDUCTOR_MATERIAL + "," + UPDATED_CONDUCTOR_MATERIAL);

        // Get all the assetUGCableList where conductorMaterial equals to UPDATED_CONDUCTOR_MATERIAL
        defaultAssetUGCableShouldNotBeFound("conductorMaterial.in=" + UPDATED_CONDUCTOR_MATERIAL);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByConductorMaterialIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where conductorMaterial is not null
        defaultAssetUGCableShouldBeFound("conductorMaterial.specified=true");

        // Get all the assetUGCableList where conductorMaterial is null
        defaultAssetUGCableShouldNotBeFound("conductorMaterial.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByConductorMaterialContainsSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where conductorMaterial contains DEFAULT_CONDUCTOR_MATERIAL
        defaultAssetUGCableShouldBeFound("conductorMaterial.contains=" + DEFAULT_CONDUCTOR_MATERIAL);

        // Get all the assetUGCableList where conductorMaterial contains UPDATED_CONDUCTOR_MATERIAL
        defaultAssetUGCableShouldNotBeFound("conductorMaterial.contains=" + UPDATED_CONDUCTOR_MATERIAL);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByConductorMaterialNotContainsSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where conductorMaterial does not contain DEFAULT_CONDUCTOR_MATERIAL
        defaultAssetUGCableShouldNotBeFound("conductorMaterial.doesNotContain=" + DEFAULT_CONDUCTOR_MATERIAL);

        // Get all the assetUGCableList where conductorMaterial does not contain UPDATED_CONDUCTOR_MATERIAL
        defaultAssetUGCableShouldBeFound("conductorMaterial.doesNotContain=" + UPDATED_CONDUCTOR_MATERIAL);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByAgeIsEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where age equals to DEFAULT_AGE
        defaultAssetUGCableShouldBeFound("age.equals=" + DEFAULT_AGE);

        // Get all the assetUGCableList where age equals to UPDATED_AGE
        defaultAssetUGCableShouldNotBeFound("age.equals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByAgeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where age not equals to DEFAULT_AGE
        defaultAssetUGCableShouldNotBeFound("age.notEquals=" + DEFAULT_AGE);

        // Get all the assetUGCableList where age not equals to UPDATED_AGE
        defaultAssetUGCableShouldBeFound("age.notEquals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByAgeIsInShouldWork() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where age in DEFAULT_AGE or UPDATED_AGE
        defaultAssetUGCableShouldBeFound("age.in=" + DEFAULT_AGE + "," + UPDATED_AGE);

        // Get all the assetUGCableList where age equals to UPDATED_AGE
        defaultAssetUGCableShouldNotBeFound("age.in=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByAgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where age is not null
        defaultAssetUGCableShouldBeFound("age.specified=true");

        // Get all the assetUGCableList where age is null
        defaultAssetUGCableShouldNotBeFound("age.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByAgeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where age is greater than or equal to DEFAULT_AGE
        defaultAssetUGCableShouldBeFound("age.greaterThanOrEqual=" + DEFAULT_AGE);

        // Get all the assetUGCableList where age is greater than or equal to UPDATED_AGE
        defaultAssetUGCableShouldNotBeFound("age.greaterThanOrEqual=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByAgeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where age is less than or equal to DEFAULT_AGE
        defaultAssetUGCableShouldBeFound("age.lessThanOrEqual=" + DEFAULT_AGE);

        // Get all the assetUGCableList where age is less than or equal to SMALLER_AGE
        defaultAssetUGCableShouldNotBeFound("age.lessThanOrEqual=" + SMALLER_AGE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByAgeIsLessThanSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where age is less than DEFAULT_AGE
        defaultAssetUGCableShouldNotBeFound("age.lessThan=" + DEFAULT_AGE);

        // Get all the assetUGCableList where age is less than UPDATED_AGE
        defaultAssetUGCableShouldBeFound("age.lessThan=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByAgeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where age is greater than DEFAULT_AGE
        defaultAssetUGCableShouldNotBeFound("age.greaterThan=" + DEFAULT_AGE);

        // Get all the assetUGCableList where age is greater than SMALLER_AGE
        defaultAssetUGCableShouldBeFound("age.greaterThan=" + SMALLER_AGE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByFaultHistoryIsEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where faultHistory equals to DEFAULT_FAULT_HISTORY
        defaultAssetUGCableShouldBeFound("faultHistory.equals=" + DEFAULT_FAULT_HISTORY);

        // Get all the assetUGCableList where faultHistory equals to UPDATED_FAULT_HISTORY
        defaultAssetUGCableShouldNotBeFound("faultHistory.equals=" + UPDATED_FAULT_HISTORY);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByFaultHistoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where faultHistory not equals to DEFAULT_FAULT_HISTORY
        defaultAssetUGCableShouldNotBeFound("faultHistory.notEquals=" + DEFAULT_FAULT_HISTORY);

        // Get all the assetUGCableList where faultHistory not equals to UPDATED_FAULT_HISTORY
        defaultAssetUGCableShouldBeFound("faultHistory.notEquals=" + UPDATED_FAULT_HISTORY);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByFaultHistoryIsInShouldWork() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where faultHistory in DEFAULT_FAULT_HISTORY or UPDATED_FAULT_HISTORY
        defaultAssetUGCableShouldBeFound("faultHistory.in=" + DEFAULT_FAULT_HISTORY + "," + UPDATED_FAULT_HISTORY);

        // Get all the assetUGCableList where faultHistory equals to UPDATED_FAULT_HISTORY
        defaultAssetUGCableShouldNotBeFound("faultHistory.in=" + UPDATED_FAULT_HISTORY);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByFaultHistoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where faultHistory is not null
        defaultAssetUGCableShouldBeFound("faultHistory.specified=true");

        // Get all the assetUGCableList where faultHistory is null
        defaultAssetUGCableShouldNotBeFound("faultHistory.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByFaultHistoryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where faultHistory is greater than or equal to DEFAULT_FAULT_HISTORY
        defaultAssetUGCableShouldBeFound("faultHistory.greaterThanOrEqual=" + DEFAULT_FAULT_HISTORY);

        // Get all the assetUGCableList where faultHistory is greater than or equal to UPDATED_FAULT_HISTORY
        defaultAssetUGCableShouldNotBeFound("faultHistory.greaterThanOrEqual=" + UPDATED_FAULT_HISTORY);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByFaultHistoryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where faultHistory is less than or equal to DEFAULT_FAULT_HISTORY
        defaultAssetUGCableShouldBeFound("faultHistory.lessThanOrEqual=" + DEFAULT_FAULT_HISTORY);

        // Get all the assetUGCableList where faultHistory is less than or equal to SMALLER_FAULT_HISTORY
        defaultAssetUGCableShouldNotBeFound("faultHistory.lessThanOrEqual=" + SMALLER_FAULT_HISTORY);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByFaultHistoryIsLessThanSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where faultHistory is less than DEFAULT_FAULT_HISTORY
        defaultAssetUGCableShouldNotBeFound("faultHistory.lessThan=" + DEFAULT_FAULT_HISTORY);

        // Get all the assetUGCableList where faultHistory is less than UPDATED_FAULT_HISTORY
        defaultAssetUGCableShouldBeFound("faultHistory.lessThan=" + UPDATED_FAULT_HISTORY);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByFaultHistoryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where faultHistory is greater than DEFAULT_FAULT_HISTORY
        defaultAssetUGCableShouldNotBeFound("faultHistory.greaterThan=" + DEFAULT_FAULT_HISTORY);

        // Get all the assetUGCableList where faultHistory is greater than SMALLER_FAULT_HISTORY
        defaultAssetUGCableShouldBeFound("faultHistory.greaterThan=" + SMALLER_FAULT_HISTORY);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByLengthOfCableSectionMetersIsEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where lengthOfCableSectionMeters equals to DEFAULT_LENGTH_OF_CABLE_SECTION_METERS
        defaultAssetUGCableShouldBeFound("lengthOfCableSectionMeters.equals=" + DEFAULT_LENGTH_OF_CABLE_SECTION_METERS);

        // Get all the assetUGCableList where lengthOfCableSectionMeters equals to UPDATED_LENGTH_OF_CABLE_SECTION_METERS
        defaultAssetUGCableShouldNotBeFound("lengthOfCableSectionMeters.equals=" + UPDATED_LENGTH_OF_CABLE_SECTION_METERS);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByLengthOfCableSectionMetersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where lengthOfCableSectionMeters not equals to DEFAULT_LENGTH_OF_CABLE_SECTION_METERS
        defaultAssetUGCableShouldNotBeFound("lengthOfCableSectionMeters.notEquals=" + DEFAULT_LENGTH_OF_CABLE_SECTION_METERS);

        // Get all the assetUGCableList where lengthOfCableSectionMeters not equals to UPDATED_LENGTH_OF_CABLE_SECTION_METERS
        defaultAssetUGCableShouldBeFound("lengthOfCableSectionMeters.notEquals=" + UPDATED_LENGTH_OF_CABLE_SECTION_METERS);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByLengthOfCableSectionMetersIsInShouldWork() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where lengthOfCableSectionMeters in DEFAULT_LENGTH_OF_CABLE_SECTION_METERS or UPDATED_LENGTH_OF_CABLE_SECTION_METERS
        defaultAssetUGCableShouldBeFound(
            "lengthOfCableSectionMeters.in=" + DEFAULT_LENGTH_OF_CABLE_SECTION_METERS + "," + UPDATED_LENGTH_OF_CABLE_SECTION_METERS
        );

        // Get all the assetUGCableList where lengthOfCableSectionMeters equals to UPDATED_LENGTH_OF_CABLE_SECTION_METERS
        defaultAssetUGCableShouldNotBeFound("lengthOfCableSectionMeters.in=" + UPDATED_LENGTH_OF_CABLE_SECTION_METERS);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByLengthOfCableSectionMetersIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where lengthOfCableSectionMeters is not null
        defaultAssetUGCableShouldBeFound("lengthOfCableSectionMeters.specified=true");

        // Get all the assetUGCableList where lengthOfCableSectionMeters is null
        defaultAssetUGCableShouldNotBeFound("lengthOfCableSectionMeters.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByLengthOfCableSectionMetersIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where lengthOfCableSectionMeters is greater than or equal to DEFAULT_LENGTH_OF_CABLE_SECTION_METERS
        defaultAssetUGCableShouldBeFound("lengthOfCableSectionMeters.greaterThanOrEqual=" + DEFAULT_LENGTH_OF_CABLE_SECTION_METERS);

        // Get all the assetUGCableList where lengthOfCableSectionMeters is greater than or equal to UPDATED_LENGTH_OF_CABLE_SECTION_METERS
        defaultAssetUGCableShouldNotBeFound("lengthOfCableSectionMeters.greaterThanOrEqual=" + UPDATED_LENGTH_OF_CABLE_SECTION_METERS);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByLengthOfCableSectionMetersIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where lengthOfCableSectionMeters is less than or equal to DEFAULT_LENGTH_OF_CABLE_SECTION_METERS
        defaultAssetUGCableShouldBeFound("lengthOfCableSectionMeters.lessThanOrEqual=" + DEFAULT_LENGTH_OF_CABLE_SECTION_METERS);

        // Get all the assetUGCableList where lengthOfCableSectionMeters is less than or equal to SMALLER_LENGTH_OF_CABLE_SECTION_METERS
        defaultAssetUGCableShouldNotBeFound("lengthOfCableSectionMeters.lessThanOrEqual=" + SMALLER_LENGTH_OF_CABLE_SECTION_METERS);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByLengthOfCableSectionMetersIsLessThanSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where lengthOfCableSectionMeters is less than DEFAULT_LENGTH_OF_CABLE_SECTION_METERS
        defaultAssetUGCableShouldNotBeFound("lengthOfCableSectionMeters.lessThan=" + DEFAULT_LENGTH_OF_CABLE_SECTION_METERS);

        // Get all the assetUGCableList where lengthOfCableSectionMeters is less than UPDATED_LENGTH_OF_CABLE_SECTION_METERS
        defaultAssetUGCableShouldBeFound("lengthOfCableSectionMeters.lessThan=" + UPDATED_LENGTH_OF_CABLE_SECTION_METERS);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByLengthOfCableSectionMetersIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where lengthOfCableSectionMeters is greater than DEFAULT_LENGTH_OF_CABLE_SECTION_METERS
        defaultAssetUGCableShouldNotBeFound("lengthOfCableSectionMeters.greaterThan=" + DEFAULT_LENGTH_OF_CABLE_SECTION_METERS);

        // Get all the assetUGCableList where lengthOfCableSectionMeters is greater than SMALLER_LENGTH_OF_CABLE_SECTION_METERS
        defaultAssetUGCableShouldBeFound("lengthOfCableSectionMeters.greaterThan=" + SMALLER_LENGTH_OF_CABLE_SECTION_METERS);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesBySectionRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where sectionRating equals to DEFAULT_SECTION_RATING
        defaultAssetUGCableShouldBeFound("sectionRating.equals=" + DEFAULT_SECTION_RATING);

        // Get all the assetUGCableList where sectionRating equals to UPDATED_SECTION_RATING
        defaultAssetUGCableShouldNotBeFound("sectionRating.equals=" + UPDATED_SECTION_RATING);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesBySectionRatingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where sectionRating not equals to DEFAULT_SECTION_RATING
        defaultAssetUGCableShouldNotBeFound("sectionRating.notEquals=" + DEFAULT_SECTION_RATING);

        // Get all the assetUGCableList where sectionRating not equals to UPDATED_SECTION_RATING
        defaultAssetUGCableShouldBeFound("sectionRating.notEquals=" + UPDATED_SECTION_RATING);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesBySectionRatingIsInShouldWork() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where sectionRating in DEFAULT_SECTION_RATING or UPDATED_SECTION_RATING
        defaultAssetUGCableShouldBeFound("sectionRating.in=" + DEFAULT_SECTION_RATING + "," + UPDATED_SECTION_RATING);

        // Get all the assetUGCableList where sectionRating equals to UPDATED_SECTION_RATING
        defaultAssetUGCableShouldNotBeFound("sectionRating.in=" + UPDATED_SECTION_RATING);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesBySectionRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where sectionRating is not null
        defaultAssetUGCableShouldBeFound("sectionRating.specified=true");

        // Get all the assetUGCableList where sectionRating is null
        defaultAssetUGCableShouldNotBeFound("sectionRating.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetUGCablesBySectionRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where sectionRating is greater than or equal to DEFAULT_SECTION_RATING
        defaultAssetUGCableShouldBeFound("sectionRating.greaterThanOrEqual=" + DEFAULT_SECTION_RATING);

        // Get all the assetUGCableList where sectionRating is greater than or equal to UPDATED_SECTION_RATING
        defaultAssetUGCableShouldNotBeFound("sectionRating.greaterThanOrEqual=" + UPDATED_SECTION_RATING);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesBySectionRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where sectionRating is less than or equal to DEFAULT_SECTION_RATING
        defaultAssetUGCableShouldBeFound("sectionRating.lessThanOrEqual=" + DEFAULT_SECTION_RATING);

        // Get all the assetUGCableList where sectionRating is less than or equal to SMALLER_SECTION_RATING
        defaultAssetUGCableShouldNotBeFound("sectionRating.lessThanOrEqual=" + SMALLER_SECTION_RATING);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesBySectionRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where sectionRating is less than DEFAULT_SECTION_RATING
        defaultAssetUGCableShouldNotBeFound("sectionRating.lessThan=" + DEFAULT_SECTION_RATING);

        // Get all the assetUGCableList where sectionRating is less than UPDATED_SECTION_RATING
        defaultAssetUGCableShouldBeFound("sectionRating.lessThan=" + UPDATED_SECTION_RATING);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesBySectionRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where sectionRating is greater than DEFAULT_SECTION_RATING
        defaultAssetUGCableShouldNotBeFound("sectionRating.greaterThan=" + DEFAULT_SECTION_RATING);

        // Get all the assetUGCableList where sectionRating is greater than SMALLER_SECTION_RATING
        defaultAssetUGCableShouldBeFound("sectionRating.greaterThan=" + SMALLER_SECTION_RATING);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where type equals to DEFAULT_TYPE
        defaultAssetUGCableShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the assetUGCableList where type equals to UPDATED_TYPE
        defaultAssetUGCableShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where type not equals to DEFAULT_TYPE
        defaultAssetUGCableShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the assetUGCableList where type not equals to UPDATED_TYPE
        defaultAssetUGCableShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultAssetUGCableShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the assetUGCableList where type equals to UPDATED_TYPE
        defaultAssetUGCableShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where type is not null
        defaultAssetUGCableShouldBeFound("type.specified=true");

        // Get all the assetUGCableList where type is null
        defaultAssetUGCableShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByTypeContainsSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where type contains DEFAULT_TYPE
        defaultAssetUGCableShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the assetUGCableList where type contains UPDATED_TYPE
        defaultAssetUGCableShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where type does not contain DEFAULT_TYPE
        defaultAssetUGCableShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the assetUGCableList where type does not contain UPDATED_TYPE
        defaultAssetUGCableShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByNumberOfCoresIsEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where numberOfCores equals to DEFAULT_NUMBER_OF_CORES
        defaultAssetUGCableShouldBeFound("numberOfCores.equals=" + DEFAULT_NUMBER_OF_CORES);

        // Get all the assetUGCableList where numberOfCores equals to UPDATED_NUMBER_OF_CORES
        defaultAssetUGCableShouldNotBeFound("numberOfCores.equals=" + UPDATED_NUMBER_OF_CORES);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByNumberOfCoresIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where numberOfCores not equals to DEFAULT_NUMBER_OF_CORES
        defaultAssetUGCableShouldNotBeFound("numberOfCores.notEquals=" + DEFAULT_NUMBER_OF_CORES);

        // Get all the assetUGCableList where numberOfCores not equals to UPDATED_NUMBER_OF_CORES
        defaultAssetUGCableShouldBeFound("numberOfCores.notEquals=" + UPDATED_NUMBER_OF_CORES);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByNumberOfCoresIsInShouldWork() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where numberOfCores in DEFAULT_NUMBER_OF_CORES or UPDATED_NUMBER_OF_CORES
        defaultAssetUGCableShouldBeFound("numberOfCores.in=" + DEFAULT_NUMBER_OF_CORES + "," + UPDATED_NUMBER_OF_CORES);

        // Get all the assetUGCableList where numberOfCores equals to UPDATED_NUMBER_OF_CORES
        defaultAssetUGCableShouldNotBeFound("numberOfCores.in=" + UPDATED_NUMBER_OF_CORES);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByNumberOfCoresIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where numberOfCores is not null
        defaultAssetUGCableShouldBeFound("numberOfCores.specified=true");

        // Get all the assetUGCableList where numberOfCores is null
        defaultAssetUGCableShouldNotBeFound("numberOfCores.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByNumberOfCoresIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where numberOfCores is greater than or equal to DEFAULT_NUMBER_OF_CORES
        defaultAssetUGCableShouldBeFound("numberOfCores.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_CORES);

        // Get all the assetUGCableList where numberOfCores is greater than or equal to UPDATED_NUMBER_OF_CORES
        defaultAssetUGCableShouldNotBeFound("numberOfCores.greaterThanOrEqual=" + UPDATED_NUMBER_OF_CORES);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByNumberOfCoresIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where numberOfCores is less than or equal to DEFAULT_NUMBER_OF_CORES
        defaultAssetUGCableShouldBeFound("numberOfCores.lessThanOrEqual=" + DEFAULT_NUMBER_OF_CORES);

        // Get all the assetUGCableList where numberOfCores is less than or equal to SMALLER_NUMBER_OF_CORES
        defaultAssetUGCableShouldNotBeFound("numberOfCores.lessThanOrEqual=" + SMALLER_NUMBER_OF_CORES);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByNumberOfCoresIsLessThanSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where numberOfCores is less than DEFAULT_NUMBER_OF_CORES
        defaultAssetUGCableShouldNotBeFound("numberOfCores.lessThan=" + DEFAULT_NUMBER_OF_CORES);

        // Get all the assetUGCableList where numberOfCores is less than UPDATED_NUMBER_OF_CORES
        defaultAssetUGCableShouldBeFound("numberOfCores.lessThan=" + UPDATED_NUMBER_OF_CORES);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByNumberOfCoresIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where numberOfCores is greater than DEFAULT_NUMBER_OF_CORES
        defaultAssetUGCableShouldNotBeFound("numberOfCores.greaterThan=" + DEFAULT_NUMBER_OF_CORES);

        // Get all the assetUGCableList where numberOfCores is greater than SMALLER_NUMBER_OF_CORES
        defaultAssetUGCableShouldBeFound("numberOfCores.greaterThan=" + SMALLER_NUMBER_OF_CORES);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByNetPerformanceCostOfFailureEuroIsEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where netPerformanceCostOfFailureEuro equals to DEFAULT_NET_PERFORMANCE_COST_OF_FAILURE_EURO
        defaultAssetUGCableShouldBeFound("netPerformanceCostOfFailureEuro.equals=" + DEFAULT_NET_PERFORMANCE_COST_OF_FAILURE_EURO);

        // Get all the assetUGCableList where netPerformanceCostOfFailureEuro equals to UPDATED_NET_PERFORMANCE_COST_OF_FAILURE_EURO
        defaultAssetUGCableShouldNotBeFound("netPerformanceCostOfFailureEuro.equals=" + UPDATED_NET_PERFORMANCE_COST_OF_FAILURE_EURO);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByNetPerformanceCostOfFailureEuroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where netPerformanceCostOfFailureEuro not equals to DEFAULT_NET_PERFORMANCE_COST_OF_FAILURE_EURO
        defaultAssetUGCableShouldNotBeFound("netPerformanceCostOfFailureEuro.notEquals=" + DEFAULT_NET_PERFORMANCE_COST_OF_FAILURE_EURO);

        // Get all the assetUGCableList where netPerformanceCostOfFailureEuro not equals to UPDATED_NET_PERFORMANCE_COST_OF_FAILURE_EURO
        defaultAssetUGCableShouldBeFound("netPerformanceCostOfFailureEuro.notEquals=" + UPDATED_NET_PERFORMANCE_COST_OF_FAILURE_EURO);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByNetPerformanceCostOfFailureEuroIsInShouldWork() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where netPerformanceCostOfFailureEuro in DEFAULT_NET_PERFORMANCE_COST_OF_FAILURE_EURO or UPDATED_NET_PERFORMANCE_COST_OF_FAILURE_EURO
        defaultAssetUGCableShouldBeFound(
            "netPerformanceCostOfFailureEuro.in=" +
            DEFAULT_NET_PERFORMANCE_COST_OF_FAILURE_EURO +
            "," +
            UPDATED_NET_PERFORMANCE_COST_OF_FAILURE_EURO
        );

        // Get all the assetUGCableList where netPerformanceCostOfFailureEuro equals to UPDATED_NET_PERFORMANCE_COST_OF_FAILURE_EURO
        defaultAssetUGCableShouldNotBeFound("netPerformanceCostOfFailureEuro.in=" + UPDATED_NET_PERFORMANCE_COST_OF_FAILURE_EURO);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByNetPerformanceCostOfFailureEuroIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where netPerformanceCostOfFailureEuro is not null
        defaultAssetUGCableShouldBeFound("netPerformanceCostOfFailureEuro.specified=true");

        // Get all the assetUGCableList where netPerformanceCostOfFailureEuro is null
        defaultAssetUGCableShouldNotBeFound("netPerformanceCostOfFailureEuro.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByNetPerformanceCostOfFailureEuroContainsSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where netPerformanceCostOfFailureEuro contains DEFAULT_NET_PERFORMANCE_COST_OF_FAILURE_EURO
        defaultAssetUGCableShouldBeFound("netPerformanceCostOfFailureEuro.contains=" + DEFAULT_NET_PERFORMANCE_COST_OF_FAILURE_EURO);

        // Get all the assetUGCableList where netPerformanceCostOfFailureEuro contains UPDATED_NET_PERFORMANCE_COST_OF_FAILURE_EURO
        defaultAssetUGCableShouldNotBeFound("netPerformanceCostOfFailureEuro.contains=" + UPDATED_NET_PERFORMANCE_COST_OF_FAILURE_EURO);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByNetPerformanceCostOfFailureEuroNotContainsSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where netPerformanceCostOfFailureEuro does not contain DEFAULT_NET_PERFORMANCE_COST_OF_FAILURE_EURO
        defaultAssetUGCableShouldNotBeFound(
            "netPerformanceCostOfFailureEuro.doesNotContain=" + DEFAULT_NET_PERFORMANCE_COST_OF_FAILURE_EURO
        );

        // Get all the assetUGCableList where netPerformanceCostOfFailureEuro does not contain UPDATED_NET_PERFORMANCE_COST_OF_FAILURE_EURO
        defaultAssetUGCableShouldBeFound("netPerformanceCostOfFailureEuro.doesNotContain=" + UPDATED_NET_PERFORMANCE_COST_OF_FAILURE_EURO);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByRepairTimeHourIsEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where repairTimeHour equals to DEFAULT_REPAIR_TIME_HOUR
        defaultAssetUGCableShouldBeFound("repairTimeHour.equals=" + DEFAULT_REPAIR_TIME_HOUR);

        // Get all the assetUGCableList where repairTimeHour equals to UPDATED_REPAIR_TIME_HOUR
        defaultAssetUGCableShouldNotBeFound("repairTimeHour.equals=" + UPDATED_REPAIR_TIME_HOUR);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByRepairTimeHourIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where repairTimeHour not equals to DEFAULT_REPAIR_TIME_HOUR
        defaultAssetUGCableShouldNotBeFound("repairTimeHour.notEquals=" + DEFAULT_REPAIR_TIME_HOUR);

        // Get all the assetUGCableList where repairTimeHour not equals to UPDATED_REPAIR_TIME_HOUR
        defaultAssetUGCableShouldBeFound("repairTimeHour.notEquals=" + UPDATED_REPAIR_TIME_HOUR);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByRepairTimeHourIsInShouldWork() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where repairTimeHour in DEFAULT_REPAIR_TIME_HOUR or UPDATED_REPAIR_TIME_HOUR
        defaultAssetUGCableShouldBeFound("repairTimeHour.in=" + DEFAULT_REPAIR_TIME_HOUR + "," + UPDATED_REPAIR_TIME_HOUR);

        // Get all the assetUGCableList where repairTimeHour equals to UPDATED_REPAIR_TIME_HOUR
        defaultAssetUGCableShouldNotBeFound("repairTimeHour.in=" + UPDATED_REPAIR_TIME_HOUR);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByRepairTimeHourIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where repairTimeHour is not null
        defaultAssetUGCableShouldBeFound("repairTimeHour.specified=true");

        // Get all the assetUGCableList where repairTimeHour is null
        defaultAssetUGCableShouldNotBeFound("repairTimeHour.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByRepairTimeHourIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where repairTimeHour is greater than or equal to DEFAULT_REPAIR_TIME_HOUR
        defaultAssetUGCableShouldBeFound("repairTimeHour.greaterThanOrEqual=" + DEFAULT_REPAIR_TIME_HOUR);

        // Get all the assetUGCableList where repairTimeHour is greater than or equal to UPDATED_REPAIR_TIME_HOUR
        defaultAssetUGCableShouldNotBeFound("repairTimeHour.greaterThanOrEqual=" + UPDATED_REPAIR_TIME_HOUR);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByRepairTimeHourIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where repairTimeHour is less than or equal to DEFAULT_REPAIR_TIME_HOUR
        defaultAssetUGCableShouldBeFound("repairTimeHour.lessThanOrEqual=" + DEFAULT_REPAIR_TIME_HOUR);

        // Get all the assetUGCableList where repairTimeHour is less than or equal to SMALLER_REPAIR_TIME_HOUR
        defaultAssetUGCableShouldNotBeFound("repairTimeHour.lessThanOrEqual=" + SMALLER_REPAIR_TIME_HOUR);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByRepairTimeHourIsLessThanSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where repairTimeHour is less than DEFAULT_REPAIR_TIME_HOUR
        defaultAssetUGCableShouldNotBeFound("repairTimeHour.lessThan=" + DEFAULT_REPAIR_TIME_HOUR);

        // Get all the assetUGCableList where repairTimeHour is less than UPDATED_REPAIR_TIME_HOUR
        defaultAssetUGCableShouldBeFound("repairTimeHour.lessThan=" + UPDATED_REPAIR_TIME_HOUR);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByRepairTimeHourIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        // Get all the assetUGCableList where repairTimeHour is greater than DEFAULT_REPAIR_TIME_HOUR
        defaultAssetUGCableShouldNotBeFound("repairTimeHour.greaterThan=" + DEFAULT_REPAIR_TIME_HOUR);

        // Get all the assetUGCableList where repairTimeHour is greater than SMALLER_REPAIR_TIME_HOUR
        defaultAssetUGCableShouldBeFound("repairTimeHour.greaterThan=" + SMALLER_REPAIR_TIME_HOUR);
    }

    @Test
    @Transactional
    void getAllAssetUGCablesByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);
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
        assetUGCable.setNetwork(network);
        assetUGCableRepository.saveAndFlush(assetUGCable);
        Long networkId = network.getId();

        // Get all the assetUGCableList where network equals to networkId
        defaultAssetUGCableShouldBeFound("networkId.equals=" + networkId);

        // Get all the assetUGCableList where network equals to (networkId + 1)
        defaultAssetUGCableShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetUGCableShouldBeFound(String filter) throws Exception {
        restAssetUGCableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetUGCable.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectionLabel").value(hasItem(DEFAULT_SECTION_LABEL)))
            .andExpect(jsonPath("$.[*].circuitId").value(hasItem(DEFAULT_CIRCUIT_ID.intValue())))
            .andExpect(jsonPath("$.[*].conductorCrossSectionalArea").value(hasItem(DEFAULT_CONDUCTOR_CROSS_SECTIONAL_AREA)))
            .andExpect(jsonPath("$.[*].sheathMaterial").value(hasItem(DEFAULT_SHEATH_MATERIAL)))
            .andExpect(jsonPath("$.[*].designVoltage").value(hasItem(DEFAULT_DESIGN_VOLTAGE)))
            .andExpect(jsonPath("$.[*].operatingVoltage").value(hasItem(DEFAULT_OPERATING_VOLTAGE)))
            .andExpect(jsonPath("$.[*].insulationTypeSheath").value(hasItem(DEFAULT_INSULATION_TYPE_SHEATH)))
            .andExpect(jsonPath("$.[*].conductorMaterial").value(hasItem(DEFAULT_CONDUCTOR_MATERIAL)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].faultHistory").value(hasItem(DEFAULT_FAULT_HISTORY)))
            .andExpect(jsonPath("$.[*].lengthOfCableSectionMeters").value(hasItem(DEFAULT_LENGTH_OF_CABLE_SECTION_METERS)))
            .andExpect(jsonPath("$.[*].sectionRating").value(hasItem(DEFAULT_SECTION_RATING)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].numberOfCores").value(hasItem(DEFAULT_NUMBER_OF_CORES)))
            .andExpect(jsonPath("$.[*].netPerformanceCostOfFailureEuro").value(hasItem(DEFAULT_NET_PERFORMANCE_COST_OF_FAILURE_EURO)))
            .andExpect(jsonPath("$.[*].repairTimeHour").value(hasItem(DEFAULT_REPAIR_TIME_HOUR)));

        // Check, that the count call also returns 1
        restAssetUGCableMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetUGCableShouldNotBeFound(String filter) throws Exception {
        restAssetUGCableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetUGCableMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssetUGCable() throws Exception {
        // Get the assetUGCable
        restAssetUGCableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssetUGCable() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        int databaseSizeBeforeUpdate = assetUGCableRepository.findAll().size();

        // Update the assetUGCable
        AssetUGCable updatedAssetUGCable = assetUGCableRepository.findById(assetUGCable.getId()).get();
        // Disconnect from session so that the updates on updatedAssetUGCable are not directly saved in db
        em.detach(updatedAssetUGCable);
        updatedAssetUGCable
            .sectionLabel(UPDATED_SECTION_LABEL)
            .circuitId(UPDATED_CIRCUIT_ID)
            .conductorCrossSectionalArea(UPDATED_CONDUCTOR_CROSS_SECTIONAL_AREA)
            .sheathMaterial(UPDATED_SHEATH_MATERIAL)
            .designVoltage(UPDATED_DESIGN_VOLTAGE)
            .operatingVoltage(UPDATED_OPERATING_VOLTAGE)
            .insulationTypeSheath(UPDATED_INSULATION_TYPE_SHEATH)
            .conductorMaterial(UPDATED_CONDUCTOR_MATERIAL)
            .age(UPDATED_AGE)
            .faultHistory(UPDATED_FAULT_HISTORY)
            .lengthOfCableSectionMeters(UPDATED_LENGTH_OF_CABLE_SECTION_METERS)
            .sectionRating(UPDATED_SECTION_RATING)
            .type(UPDATED_TYPE)
            .numberOfCores(UPDATED_NUMBER_OF_CORES)
            .netPerformanceCostOfFailureEuro(UPDATED_NET_PERFORMANCE_COST_OF_FAILURE_EURO)
            .repairTimeHour(UPDATED_REPAIR_TIME_HOUR);
        AssetUGCableDTO assetUGCableDTO = assetUGCableMapper.toDto(updatedAssetUGCable);

        restAssetUGCableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetUGCableDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetUGCableDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssetUGCable in the database
        List<AssetUGCable> assetUGCableList = assetUGCableRepository.findAll();
        assertThat(assetUGCableList).hasSize(databaseSizeBeforeUpdate);
        AssetUGCable testAssetUGCable = assetUGCableList.get(assetUGCableList.size() - 1);
        assertThat(testAssetUGCable.getSectionLabel()).isEqualTo(UPDATED_SECTION_LABEL);
        assertThat(testAssetUGCable.getCircuitId()).isEqualTo(UPDATED_CIRCUIT_ID);
        assertThat(testAssetUGCable.getConductorCrossSectionalArea()).isEqualTo(UPDATED_CONDUCTOR_CROSS_SECTIONAL_AREA);
        assertThat(testAssetUGCable.getSheathMaterial()).isEqualTo(UPDATED_SHEATH_MATERIAL);
        assertThat(testAssetUGCable.getDesignVoltage()).isEqualTo(UPDATED_DESIGN_VOLTAGE);
        assertThat(testAssetUGCable.getOperatingVoltage()).isEqualTo(UPDATED_OPERATING_VOLTAGE);
        assertThat(testAssetUGCable.getInsulationTypeSheath()).isEqualTo(UPDATED_INSULATION_TYPE_SHEATH);
        assertThat(testAssetUGCable.getConductorMaterial()).isEqualTo(UPDATED_CONDUCTOR_MATERIAL);
        assertThat(testAssetUGCable.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testAssetUGCable.getFaultHistory()).isEqualTo(UPDATED_FAULT_HISTORY);
        assertThat(testAssetUGCable.getLengthOfCableSectionMeters()).isEqualTo(UPDATED_LENGTH_OF_CABLE_SECTION_METERS);
        assertThat(testAssetUGCable.getSectionRating()).isEqualTo(UPDATED_SECTION_RATING);
        assertThat(testAssetUGCable.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAssetUGCable.getNumberOfCores()).isEqualTo(UPDATED_NUMBER_OF_CORES);
        assertThat(testAssetUGCable.getNetPerformanceCostOfFailureEuro()).isEqualTo(UPDATED_NET_PERFORMANCE_COST_OF_FAILURE_EURO);
        assertThat(testAssetUGCable.getRepairTimeHour()).isEqualTo(UPDATED_REPAIR_TIME_HOUR);
    }

    @Test
    @Transactional
    void putNonExistingAssetUGCable() throws Exception {
        int databaseSizeBeforeUpdate = assetUGCableRepository.findAll().size();
        assetUGCable.setId(count.incrementAndGet());

        // Create the AssetUGCable
        AssetUGCableDTO assetUGCableDTO = assetUGCableMapper.toDto(assetUGCable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetUGCableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetUGCableDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetUGCableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetUGCable in the database
        List<AssetUGCable> assetUGCableList = assetUGCableRepository.findAll();
        assertThat(assetUGCableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssetUGCable() throws Exception {
        int databaseSizeBeforeUpdate = assetUGCableRepository.findAll().size();
        assetUGCable.setId(count.incrementAndGet());

        // Create the AssetUGCable
        AssetUGCableDTO assetUGCableDTO = assetUGCableMapper.toDto(assetUGCable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetUGCableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetUGCableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetUGCable in the database
        List<AssetUGCable> assetUGCableList = assetUGCableRepository.findAll();
        assertThat(assetUGCableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssetUGCable() throws Exception {
        int databaseSizeBeforeUpdate = assetUGCableRepository.findAll().size();
        assetUGCable.setId(count.incrementAndGet());

        // Create the AssetUGCable
        AssetUGCableDTO assetUGCableDTO = assetUGCableMapper.toDto(assetUGCable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetUGCableMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetUGCableDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetUGCable in the database
        List<AssetUGCable> assetUGCableList = assetUGCableRepository.findAll();
        assertThat(assetUGCableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssetUGCableWithPatch() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        int databaseSizeBeforeUpdate = assetUGCableRepository.findAll().size();

        // Update the assetUGCable using partial update
        AssetUGCable partialUpdatedAssetUGCable = new AssetUGCable();
        partialUpdatedAssetUGCable.setId(assetUGCable.getId());

        partialUpdatedAssetUGCable
            .conductorCrossSectionalArea(UPDATED_CONDUCTOR_CROSS_SECTIONAL_AREA)
            .sheathMaterial(UPDATED_SHEATH_MATERIAL)
            .operatingVoltage(UPDATED_OPERATING_VOLTAGE)
            .conductorMaterial(UPDATED_CONDUCTOR_MATERIAL)
            .age(UPDATED_AGE)
            .netPerformanceCostOfFailureEuro(UPDATED_NET_PERFORMANCE_COST_OF_FAILURE_EURO)
            .repairTimeHour(UPDATED_REPAIR_TIME_HOUR);

        restAssetUGCableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetUGCable.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetUGCable))
            )
            .andExpect(status().isOk());

        // Validate the AssetUGCable in the database
        List<AssetUGCable> assetUGCableList = assetUGCableRepository.findAll();
        assertThat(assetUGCableList).hasSize(databaseSizeBeforeUpdate);
        AssetUGCable testAssetUGCable = assetUGCableList.get(assetUGCableList.size() - 1);
        assertThat(testAssetUGCable.getSectionLabel()).isEqualTo(DEFAULT_SECTION_LABEL);
        assertThat(testAssetUGCable.getCircuitId()).isEqualTo(DEFAULT_CIRCUIT_ID);
        assertThat(testAssetUGCable.getConductorCrossSectionalArea()).isEqualTo(UPDATED_CONDUCTOR_CROSS_SECTIONAL_AREA);
        assertThat(testAssetUGCable.getSheathMaterial()).isEqualTo(UPDATED_SHEATH_MATERIAL);
        assertThat(testAssetUGCable.getDesignVoltage()).isEqualTo(DEFAULT_DESIGN_VOLTAGE);
        assertThat(testAssetUGCable.getOperatingVoltage()).isEqualTo(UPDATED_OPERATING_VOLTAGE);
        assertThat(testAssetUGCable.getInsulationTypeSheath()).isEqualTo(DEFAULT_INSULATION_TYPE_SHEATH);
        assertThat(testAssetUGCable.getConductorMaterial()).isEqualTo(UPDATED_CONDUCTOR_MATERIAL);
        assertThat(testAssetUGCable.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testAssetUGCable.getFaultHistory()).isEqualTo(DEFAULT_FAULT_HISTORY);
        assertThat(testAssetUGCable.getLengthOfCableSectionMeters()).isEqualTo(DEFAULT_LENGTH_OF_CABLE_SECTION_METERS);
        assertThat(testAssetUGCable.getSectionRating()).isEqualTo(DEFAULT_SECTION_RATING);
        assertThat(testAssetUGCable.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAssetUGCable.getNumberOfCores()).isEqualTo(DEFAULT_NUMBER_OF_CORES);
        assertThat(testAssetUGCable.getNetPerformanceCostOfFailureEuro()).isEqualTo(UPDATED_NET_PERFORMANCE_COST_OF_FAILURE_EURO);
        assertThat(testAssetUGCable.getRepairTimeHour()).isEqualTo(UPDATED_REPAIR_TIME_HOUR);
    }

    @Test
    @Transactional
    void fullUpdateAssetUGCableWithPatch() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        int databaseSizeBeforeUpdate = assetUGCableRepository.findAll().size();

        // Update the assetUGCable using partial update
        AssetUGCable partialUpdatedAssetUGCable = new AssetUGCable();
        partialUpdatedAssetUGCable.setId(assetUGCable.getId());

        partialUpdatedAssetUGCable
            .sectionLabel(UPDATED_SECTION_LABEL)
            .circuitId(UPDATED_CIRCUIT_ID)
            .conductorCrossSectionalArea(UPDATED_CONDUCTOR_CROSS_SECTIONAL_AREA)
            .sheathMaterial(UPDATED_SHEATH_MATERIAL)
            .designVoltage(UPDATED_DESIGN_VOLTAGE)
            .operatingVoltage(UPDATED_OPERATING_VOLTAGE)
            .insulationTypeSheath(UPDATED_INSULATION_TYPE_SHEATH)
            .conductorMaterial(UPDATED_CONDUCTOR_MATERIAL)
            .age(UPDATED_AGE)
            .faultHistory(UPDATED_FAULT_HISTORY)
            .lengthOfCableSectionMeters(UPDATED_LENGTH_OF_CABLE_SECTION_METERS)
            .sectionRating(UPDATED_SECTION_RATING)
            .type(UPDATED_TYPE)
            .numberOfCores(UPDATED_NUMBER_OF_CORES)
            .netPerformanceCostOfFailureEuro(UPDATED_NET_PERFORMANCE_COST_OF_FAILURE_EURO)
            .repairTimeHour(UPDATED_REPAIR_TIME_HOUR);

        restAssetUGCableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetUGCable.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetUGCable))
            )
            .andExpect(status().isOk());

        // Validate the AssetUGCable in the database
        List<AssetUGCable> assetUGCableList = assetUGCableRepository.findAll();
        assertThat(assetUGCableList).hasSize(databaseSizeBeforeUpdate);
        AssetUGCable testAssetUGCable = assetUGCableList.get(assetUGCableList.size() - 1);
        assertThat(testAssetUGCable.getSectionLabel()).isEqualTo(UPDATED_SECTION_LABEL);
        assertThat(testAssetUGCable.getCircuitId()).isEqualTo(UPDATED_CIRCUIT_ID);
        assertThat(testAssetUGCable.getConductorCrossSectionalArea()).isEqualTo(UPDATED_CONDUCTOR_CROSS_SECTIONAL_AREA);
        assertThat(testAssetUGCable.getSheathMaterial()).isEqualTo(UPDATED_SHEATH_MATERIAL);
        assertThat(testAssetUGCable.getDesignVoltage()).isEqualTo(UPDATED_DESIGN_VOLTAGE);
        assertThat(testAssetUGCable.getOperatingVoltage()).isEqualTo(UPDATED_OPERATING_VOLTAGE);
        assertThat(testAssetUGCable.getInsulationTypeSheath()).isEqualTo(UPDATED_INSULATION_TYPE_SHEATH);
        assertThat(testAssetUGCable.getConductorMaterial()).isEqualTo(UPDATED_CONDUCTOR_MATERIAL);
        assertThat(testAssetUGCable.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testAssetUGCable.getFaultHistory()).isEqualTo(UPDATED_FAULT_HISTORY);
        assertThat(testAssetUGCable.getLengthOfCableSectionMeters()).isEqualTo(UPDATED_LENGTH_OF_CABLE_SECTION_METERS);
        assertThat(testAssetUGCable.getSectionRating()).isEqualTo(UPDATED_SECTION_RATING);
        assertThat(testAssetUGCable.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAssetUGCable.getNumberOfCores()).isEqualTo(UPDATED_NUMBER_OF_CORES);
        assertThat(testAssetUGCable.getNetPerformanceCostOfFailureEuro()).isEqualTo(UPDATED_NET_PERFORMANCE_COST_OF_FAILURE_EURO);
        assertThat(testAssetUGCable.getRepairTimeHour()).isEqualTo(UPDATED_REPAIR_TIME_HOUR);
    }

    @Test
    @Transactional
    void patchNonExistingAssetUGCable() throws Exception {
        int databaseSizeBeforeUpdate = assetUGCableRepository.findAll().size();
        assetUGCable.setId(count.incrementAndGet());

        // Create the AssetUGCable
        AssetUGCableDTO assetUGCableDTO = assetUGCableMapper.toDto(assetUGCable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetUGCableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetUGCableDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetUGCableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetUGCable in the database
        List<AssetUGCable> assetUGCableList = assetUGCableRepository.findAll();
        assertThat(assetUGCableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssetUGCable() throws Exception {
        int databaseSizeBeforeUpdate = assetUGCableRepository.findAll().size();
        assetUGCable.setId(count.incrementAndGet());

        // Create the AssetUGCable
        AssetUGCableDTO assetUGCableDTO = assetUGCableMapper.toDto(assetUGCable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetUGCableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetUGCableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetUGCable in the database
        List<AssetUGCable> assetUGCableList = assetUGCableRepository.findAll();
        assertThat(assetUGCableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssetUGCable() throws Exception {
        int databaseSizeBeforeUpdate = assetUGCableRepository.findAll().size();
        assetUGCable.setId(count.incrementAndGet());

        // Create the AssetUGCable
        AssetUGCableDTO assetUGCableDTO = assetUGCableMapper.toDto(assetUGCable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetUGCableMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetUGCableDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetUGCable in the database
        List<AssetUGCable> assetUGCableList = assetUGCableRepository.findAll();
        assertThat(assetUGCableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssetUGCable() throws Exception {
        // Initialize the database
        assetUGCableRepository.saveAndFlush(assetUGCable);

        int databaseSizeBeforeDelete = assetUGCableRepository.findAll().size();

        // Delete the assetUGCable
        restAssetUGCableMockMvc
            .perform(delete(ENTITY_API_URL_ID, assetUGCable.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssetUGCable> assetUGCableList = assetUGCableRepository.findAll();
        assertThat(assetUGCableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
