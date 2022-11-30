package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Network;
import com.attest.ict.domain.Storage;
import com.attest.ict.repository.StorageRepository;
import com.attest.ict.service.criteria.StorageCriteria;
import com.attest.ict.service.dto.StorageDTO;
import com.attest.ict.service.mapper.StorageMapper;
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
 * Integration tests for the {@link StorageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StorageResourceIT {

    private static final Long DEFAULT_BUS_NUM = 1L;
    private static final Long UPDATED_BUS_NUM = 2L;
    private static final Long SMALLER_BUS_NUM = 1L - 1L;

    private static final Double DEFAULT_PS = 1D;
    private static final Double UPDATED_PS = 2D;
    private static final Double SMALLER_PS = 1D - 1D;

    private static final Double DEFAULT_QS = 1D;
    private static final Double UPDATED_QS = 2D;
    private static final Double SMALLER_QS = 1D - 1D;

    private static final Double DEFAULT_ENERGY = 1D;
    private static final Double UPDATED_ENERGY = 2D;
    private static final Double SMALLER_ENERGY = 1D - 1D;

    private static final Double DEFAULT_E_RATING = 1D;
    private static final Double UPDATED_E_RATING = 2D;
    private static final Double SMALLER_E_RATING = 1D - 1D;

    private static final Double DEFAULT_CHARGE_RATING = 1D;
    private static final Double UPDATED_CHARGE_RATING = 2D;
    private static final Double SMALLER_CHARGE_RATING = 1D - 1D;

    private static final Double DEFAULT_DISCHARGE_RATING = 1D;
    private static final Double UPDATED_DISCHARGE_RATING = 2D;
    private static final Double SMALLER_DISCHARGE_RATING = 1D - 1D;

    private static final Double DEFAULT_CHARGE_EFFICIENCY = 1D;
    private static final Double UPDATED_CHARGE_EFFICIENCY = 2D;
    private static final Double SMALLER_CHARGE_EFFICIENCY = 1D - 1D;

    private static final Double DEFAULT_THERMAL_RATING = 1D;
    private static final Double UPDATED_THERMAL_RATING = 2D;
    private static final Double SMALLER_THERMAL_RATING = 1D - 1D;

    private static final Double DEFAULT_QMIN = 1D;
    private static final Double UPDATED_QMIN = 2D;
    private static final Double SMALLER_QMIN = 1D - 1D;

    private static final Double DEFAULT_QMAX = 1D;
    private static final Double UPDATED_QMAX = 2D;
    private static final Double SMALLER_QMAX = 1D - 1D;

    private static final Double DEFAULT_R = 1D;
    private static final Double UPDATED_R = 2D;
    private static final Double SMALLER_R = 1D - 1D;

    private static final Double DEFAULT_X = 1D;
    private static final Double UPDATED_X = 2D;
    private static final Double SMALLER_X = 1D - 1D;

    private static final Double DEFAULT_P_LOSS = 1D;
    private static final Double UPDATED_P_LOSS = 2D;
    private static final Double SMALLER_P_LOSS = 1D - 1D;

    private static final Double DEFAULT_Q_LOSS = 1D;
    private static final Double UPDATED_Q_LOSS = 2D;
    private static final Double SMALLER_Q_LOSS = 1D - 1D;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;
    private static final Integer SMALLER_STATUS = 1 - 1;

    private static final Double DEFAULT_SOC_INITIAL = 1D;
    private static final Double UPDATED_SOC_INITIAL = 2D;
    private static final Double SMALLER_SOC_INITIAL = 1D - 1D;

    private static final Double DEFAULT_SOC_MIN = 1D;
    private static final Double UPDATED_SOC_MIN = 2D;
    private static final Double SMALLER_SOC_MIN = 1D - 1D;

    private static final Double DEFAULT_SOC_MAX = 1D;
    private static final Double UPDATED_SOC_MAX = 2D;
    private static final Double SMALLER_SOC_MAX = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/storages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private StorageMapper storageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStorageMockMvc;

    private Storage storage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Storage createEntity(EntityManager em) {
        Storage storage = new Storage()
            .busNum(DEFAULT_BUS_NUM)
            .ps(DEFAULT_PS)
            .qs(DEFAULT_QS)
            .energy(DEFAULT_ENERGY)
            .eRating(DEFAULT_E_RATING)
            .chargeRating(DEFAULT_CHARGE_RATING)
            .dischargeRating(DEFAULT_DISCHARGE_RATING)
            .chargeEfficiency(DEFAULT_CHARGE_EFFICIENCY)
            .thermalRating(DEFAULT_THERMAL_RATING)
            .qmin(DEFAULT_QMIN)
            .qmax(DEFAULT_QMAX)
            .r(DEFAULT_R)
            .x(DEFAULT_X)
            .pLoss(DEFAULT_P_LOSS)
            .qLoss(DEFAULT_Q_LOSS)
            .status(DEFAULT_STATUS)
            .socInitial(DEFAULT_SOC_INITIAL)
            .socMin(DEFAULT_SOC_MIN)
            .socMax(DEFAULT_SOC_MAX);
        return storage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Storage createUpdatedEntity(EntityManager em) {
        Storage storage = new Storage()
            .busNum(UPDATED_BUS_NUM)
            .ps(UPDATED_PS)
            .qs(UPDATED_QS)
            .energy(UPDATED_ENERGY)
            .eRating(UPDATED_E_RATING)
            .chargeRating(UPDATED_CHARGE_RATING)
            .dischargeRating(UPDATED_DISCHARGE_RATING)
            .chargeEfficiency(UPDATED_CHARGE_EFFICIENCY)
            .thermalRating(UPDATED_THERMAL_RATING)
            .qmin(UPDATED_QMIN)
            .qmax(UPDATED_QMAX)
            .r(UPDATED_R)
            .x(UPDATED_X)
            .pLoss(UPDATED_P_LOSS)
            .qLoss(UPDATED_Q_LOSS)
            .status(UPDATED_STATUS)
            .socInitial(UPDATED_SOC_INITIAL)
            .socMin(UPDATED_SOC_MIN)
            .socMax(UPDATED_SOC_MAX);
        return storage;
    }

    @BeforeEach
    public void initTest() {
        storage = createEntity(em);
    }

    @Test
    @Transactional
    void createStorage() throws Exception {
        int databaseSizeBeforeCreate = storageRepository.findAll().size();
        // Create the Storage
        StorageDTO storageDTO = storageMapper.toDto(storage);
        restStorageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Storage in the database
        List<Storage> storageList = storageRepository.findAll();
        assertThat(storageList).hasSize(databaseSizeBeforeCreate + 1);
        Storage testStorage = storageList.get(storageList.size() - 1);
        assertThat(testStorage.getBusNum()).isEqualTo(DEFAULT_BUS_NUM);
        assertThat(testStorage.getPs()).isEqualTo(DEFAULT_PS);
        assertThat(testStorage.getQs()).isEqualTo(DEFAULT_QS);
        assertThat(testStorage.getEnergy()).isEqualTo(DEFAULT_ENERGY);
        assertThat(testStorage.geteRating()).isEqualTo(DEFAULT_E_RATING);
        assertThat(testStorage.getChargeRating()).isEqualTo(DEFAULT_CHARGE_RATING);
        assertThat(testStorage.getDischargeRating()).isEqualTo(DEFAULT_DISCHARGE_RATING);
        assertThat(testStorage.getChargeEfficiency()).isEqualTo(DEFAULT_CHARGE_EFFICIENCY);
        assertThat(testStorage.getThermalRating()).isEqualTo(DEFAULT_THERMAL_RATING);
        assertThat(testStorage.getQmin()).isEqualTo(DEFAULT_QMIN);
        assertThat(testStorage.getQmax()).isEqualTo(DEFAULT_QMAX);
        assertThat(testStorage.getR()).isEqualTo(DEFAULT_R);
        assertThat(testStorage.getX()).isEqualTo(DEFAULT_X);
        assertThat(testStorage.getpLoss()).isEqualTo(DEFAULT_P_LOSS);
        assertThat(testStorage.getqLoss()).isEqualTo(DEFAULT_Q_LOSS);
        assertThat(testStorage.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStorage.getSocInitial()).isEqualTo(DEFAULT_SOC_INITIAL);
        assertThat(testStorage.getSocMin()).isEqualTo(DEFAULT_SOC_MIN);
        assertThat(testStorage.getSocMax()).isEqualTo(DEFAULT_SOC_MAX);
    }

    @Test
    @Transactional
    void createStorageWithExistingId() throws Exception {
        // Create the Storage with an existing ID
        storage.setId(1L);
        StorageDTO storageDTO = storageMapper.toDto(storage);

        int databaseSizeBeforeCreate = storageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStorageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Storage in the database
        List<Storage> storageList = storageRepository.findAll();
        assertThat(storageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStorages() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList
        restStorageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storage.getId().intValue())))
            .andExpect(jsonPath("$.[*].busNum").value(hasItem(DEFAULT_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].ps").value(hasItem(DEFAULT_PS.doubleValue())))
            .andExpect(jsonPath("$.[*].qs").value(hasItem(DEFAULT_QS.doubleValue())))
            .andExpect(jsonPath("$.[*].energy").value(hasItem(DEFAULT_ENERGY.doubleValue())))
            .andExpect(jsonPath("$.[*].eRating").value(hasItem(DEFAULT_E_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].chargeRating").value(hasItem(DEFAULT_CHARGE_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].dischargeRating").value(hasItem(DEFAULT_DISCHARGE_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].chargeEfficiency").value(hasItem(DEFAULT_CHARGE_EFFICIENCY.doubleValue())))
            .andExpect(jsonPath("$.[*].thermalRating").value(hasItem(DEFAULT_THERMAL_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].qmin").value(hasItem(DEFAULT_QMIN.doubleValue())))
            .andExpect(jsonPath("$.[*].qmax").value(hasItem(DEFAULT_QMAX.doubleValue())))
            .andExpect(jsonPath("$.[*].r").value(hasItem(DEFAULT_R.doubleValue())))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X.doubleValue())))
            .andExpect(jsonPath("$.[*].pLoss").value(hasItem(DEFAULT_P_LOSS.doubleValue())))
            .andExpect(jsonPath("$.[*].qLoss").value(hasItem(DEFAULT_Q_LOSS.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].socInitial").value(hasItem(DEFAULT_SOC_INITIAL.doubleValue())))
            .andExpect(jsonPath("$.[*].socMin").value(hasItem(DEFAULT_SOC_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].socMax").value(hasItem(DEFAULT_SOC_MAX.doubleValue())));
    }

    @Test
    @Transactional
    void getStorage() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get the storage
        restStorageMockMvc
            .perform(get(ENTITY_API_URL_ID, storage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(storage.getId().intValue()))
            .andExpect(jsonPath("$.busNum").value(DEFAULT_BUS_NUM.intValue()))
            .andExpect(jsonPath("$.ps").value(DEFAULT_PS.doubleValue()))
            .andExpect(jsonPath("$.qs").value(DEFAULT_QS.doubleValue()))
            .andExpect(jsonPath("$.energy").value(DEFAULT_ENERGY.doubleValue()))
            .andExpect(jsonPath("$.eRating").value(DEFAULT_E_RATING.doubleValue()))
            .andExpect(jsonPath("$.chargeRating").value(DEFAULT_CHARGE_RATING.doubleValue()))
            .andExpect(jsonPath("$.dischargeRating").value(DEFAULT_DISCHARGE_RATING.doubleValue()))
            .andExpect(jsonPath("$.chargeEfficiency").value(DEFAULT_CHARGE_EFFICIENCY.doubleValue()))
            .andExpect(jsonPath("$.thermalRating").value(DEFAULT_THERMAL_RATING.doubleValue()))
            .andExpect(jsonPath("$.qmin").value(DEFAULT_QMIN.doubleValue()))
            .andExpect(jsonPath("$.qmax").value(DEFAULT_QMAX.doubleValue()))
            .andExpect(jsonPath("$.r").value(DEFAULT_R.doubleValue()))
            .andExpect(jsonPath("$.x").value(DEFAULT_X.doubleValue()))
            .andExpect(jsonPath("$.pLoss").value(DEFAULT_P_LOSS.doubleValue()))
            .andExpect(jsonPath("$.qLoss").value(DEFAULT_Q_LOSS.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.socInitial").value(DEFAULT_SOC_INITIAL.doubleValue()))
            .andExpect(jsonPath("$.socMin").value(DEFAULT_SOC_MIN.doubleValue()))
            .andExpect(jsonPath("$.socMax").value(DEFAULT_SOC_MAX.doubleValue()));
    }

    @Test
    @Transactional
    void getStoragesByIdFiltering() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        Long id = storage.getId();

        defaultStorageShouldBeFound("id.equals=" + id);
        defaultStorageShouldNotBeFound("id.notEquals=" + id);

        defaultStorageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStorageShouldNotBeFound("id.greaterThan=" + id);

        defaultStorageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStorageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStoragesByBusNumIsEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where busNum equals to DEFAULT_BUS_NUM
        defaultStorageShouldBeFound("busNum.equals=" + DEFAULT_BUS_NUM);

        // Get all the storageList where busNum equals to UPDATED_BUS_NUM
        defaultStorageShouldNotBeFound("busNum.equals=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllStoragesByBusNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where busNum not equals to DEFAULT_BUS_NUM
        defaultStorageShouldNotBeFound("busNum.notEquals=" + DEFAULT_BUS_NUM);

        // Get all the storageList where busNum not equals to UPDATED_BUS_NUM
        defaultStorageShouldBeFound("busNum.notEquals=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllStoragesByBusNumIsInShouldWork() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where busNum in DEFAULT_BUS_NUM or UPDATED_BUS_NUM
        defaultStorageShouldBeFound("busNum.in=" + DEFAULT_BUS_NUM + "," + UPDATED_BUS_NUM);

        // Get all the storageList where busNum equals to UPDATED_BUS_NUM
        defaultStorageShouldNotBeFound("busNum.in=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllStoragesByBusNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where busNum is not null
        defaultStorageShouldBeFound("busNum.specified=true");

        // Get all the storageList where busNum is null
        defaultStorageShouldNotBeFound("busNum.specified=false");
    }

    @Test
    @Transactional
    void getAllStoragesByBusNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where busNum is greater than or equal to DEFAULT_BUS_NUM
        defaultStorageShouldBeFound("busNum.greaterThanOrEqual=" + DEFAULT_BUS_NUM);

        // Get all the storageList where busNum is greater than or equal to UPDATED_BUS_NUM
        defaultStorageShouldNotBeFound("busNum.greaterThanOrEqual=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllStoragesByBusNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where busNum is less than or equal to DEFAULT_BUS_NUM
        defaultStorageShouldBeFound("busNum.lessThanOrEqual=" + DEFAULT_BUS_NUM);

        // Get all the storageList where busNum is less than or equal to SMALLER_BUS_NUM
        defaultStorageShouldNotBeFound("busNum.lessThanOrEqual=" + SMALLER_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllStoragesByBusNumIsLessThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where busNum is less than DEFAULT_BUS_NUM
        defaultStorageShouldNotBeFound("busNum.lessThan=" + DEFAULT_BUS_NUM);

        // Get all the storageList where busNum is less than UPDATED_BUS_NUM
        defaultStorageShouldBeFound("busNum.lessThan=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllStoragesByBusNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where busNum is greater than DEFAULT_BUS_NUM
        defaultStorageShouldNotBeFound("busNum.greaterThan=" + DEFAULT_BUS_NUM);

        // Get all the storageList where busNum is greater than SMALLER_BUS_NUM
        defaultStorageShouldBeFound("busNum.greaterThan=" + SMALLER_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllStoragesByPsIsEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where ps equals to DEFAULT_PS
        defaultStorageShouldBeFound("ps.equals=" + DEFAULT_PS);

        // Get all the storageList where ps equals to UPDATED_PS
        defaultStorageShouldNotBeFound("ps.equals=" + UPDATED_PS);
    }

    @Test
    @Transactional
    void getAllStoragesByPsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where ps not equals to DEFAULT_PS
        defaultStorageShouldNotBeFound("ps.notEquals=" + DEFAULT_PS);

        // Get all the storageList where ps not equals to UPDATED_PS
        defaultStorageShouldBeFound("ps.notEquals=" + UPDATED_PS);
    }

    @Test
    @Transactional
    void getAllStoragesByPsIsInShouldWork() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where ps in DEFAULT_PS or UPDATED_PS
        defaultStorageShouldBeFound("ps.in=" + DEFAULT_PS + "," + UPDATED_PS);

        // Get all the storageList where ps equals to UPDATED_PS
        defaultStorageShouldNotBeFound("ps.in=" + UPDATED_PS);
    }

    @Test
    @Transactional
    void getAllStoragesByPsIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where ps is not null
        defaultStorageShouldBeFound("ps.specified=true");

        // Get all the storageList where ps is null
        defaultStorageShouldNotBeFound("ps.specified=false");
    }

    @Test
    @Transactional
    void getAllStoragesByPsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where ps is greater than or equal to DEFAULT_PS
        defaultStorageShouldBeFound("ps.greaterThanOrEqual=" + DEFAULT_PS);

        // Get all the storageList where ps is greater than or equal to UPDATED_PS
        defaultStorageShouldNotBeFound("ps.greaterThanOrEqual=" + UPDATED_PS);
    }

    @Test
    @Transactional
    void getAllStoragesByPsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where ps is less than or equal to DEFAULT_PS
        defaultStorageShouldBeFound("ps.lessThanOrEqual=" + DEFAULT_PS);

        // Get all the storageList where ps is less than or equal to SMALLER_PS
        defaultStorageShouldNotBeFound("ps.lessThanOrEqual=" + SMALLER_PS);
    }

    @Test
    @Transactional
    void getAllStoragesByPsIsLessThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where ps is less than DEFAULT_PS
        defaultStorageShouldNotBeFound("ps.lessThan=" + DEFAULT_PS);

        // Get all the storageList where ps is less than UPDATED_PS
        defaultStorageShouldBeFound("ps.lessThan=" + UPDATED_PS);
    }

    @Test
    @Transactional
    void getAllStoragesByPsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where ps is greater than DEFAULT_PS
        defaultStorageShouldNotBeFound("ps.greaterThan=" + DEFAULT_PS);

        // Get all the storageList where ps is greater than SMALLER_PS
        defaultStorageShouldBeFound("ps.greaterThan=" + SMALLER_PS);
    }

    @Test
    @Transactional
    void getAllStoragesByQsIsEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qs equals to DEFAULT_QS
        defaultStorageShouldBeFound("qs.equals=" + DEFAULT_QS);

        // Get all the storageList where qs equals to UPDATED_QS
        defaultStorageShouldNotBeFound("qs.equals=" + UPDATED_QS);
    }

    @Test
    @Transactional
    void getAllStoragesByQsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qs not equals to DEFAULT_QS
        defaultStorageShouldNotBeFound("qs.notEquals=" + DEFAULT_QS);

        // Get all the storageList where qs not equals to UPDATED_QS
        defaultStorageShouldBeFound("qs.notEquals=" + UPDATED_QS);
    }

    @Test
    @Transactional
    void getAllStoragesByQsIsInShouldWork() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qs in DEFAULT_QS or UPDATED_QS
        defaultStorageShouldBeFound("qs.in=" + DEFAULT_QS + "," + UPDATED_QS);

        // Get all the storageList where qs equals to UPDATED_QS
        defaultStorageShouldNotBeFound("qs.in=" + UPDATED_QS);
    }

    @Test
    @Transactional
    void getAllStoragesByQsIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qs is not null
        defaultStorageShouldBeFound("qs.specified=true");

        // Get all the storageList where qs is null
        defaultStorageShouldNotBeFound("qs.specified=false");
    }

    @Test
    @Transactional
    void getAllStoragesByQsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qs is greater than or equal to DEFAULT_QS
        defaultStorageShouldBeFound("qs.greaterThanOrEqual=" + DEFAULT_QS);

        // Get all the storageList where qs is greater than or equal to UPDATED_QS
        defaultStorageShouldNotBeFound("qs.greaterThanOrEqual=" + UPDATED_QS);
    }

    @Test
    @Transactional
    void getAllStoragesByQsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qs is less than or equal to DEFAULT_QS
        defaultStorageShouldBeFound("qs.lessThanOrEqual=" + DEFAULT_QS);

        // Get all the storageList where qs is less than or equal to SMALLER_QS
        defaultStorageShouldNotBeFound("qs.lessThanOrEqual=" + SMALLER_QS);
    }

    @Test
    @Transactional
    void getAllStoragesByQsIsLessThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qs is less than DEFAULT_QS
        defaultStorageShouldNotBeFound("qs.lessThan=" + DEFAULT_QS);

        // Get all the storageList where qs is less than UPDATED_QS
        defaultStorageShouldBeFound("qs.lessThan=" + UPDATED_QS);
    }

    @Test
    @Transactional
    void getAllStoragesByQsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qs is greater than DEFAULT_QS
        defaultStorageShouldNotBeFound("qs.greaterThan=" + DEFAULT_QS);

        // Get all the storageList where qs is greater than SMALLER_QS
        defaultStorageShouldBeFound("qs.greaterThan=" + SMALLER_QS);
    }

    @Test
    @Transactional
    void getAllStoragesByEnergyIsEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where energy equals to DEFAULT_ENERGY
        defaultStorageShouldBeFound("energy.equals=" + DEFAULT_ENERGY);

        // Get all the storageList where energy equals to UPDATED_ENERGY
        defaultStorageShouldNotBeFound("energy.equals=" + UPDATED_ENERGY);
    }

    @Test
    @Transactional
    void getAllStoragesByEnergyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where energy not equals to DEFAULT_ENERGY
        defaultStorageShouldNotBeFound("energy.notEquals=" + DEFAULT_ENERGY);

        // Get all the storageList where energy not equals to UPDATED_ENERGY
        defaultStorageShouldBeFound("energy.notEquals=" + UPDATED_ENERGY);
    }

    @Test
    @Transactional
    void getAllStoragesByEnergyIsInShouldWork() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where energy in DEFAULT_ENERGY or UPDATED_ENERGY
        defaultStorageShouldBeFound("energy.in=" + DEFAULT_ENERGY + "," + UPDATED_ENERGY);

        // Get all the storageList where energy equals to UPDATED_ENERGY
        defaultStorageShouldNotBeFound("energy.in=" + UPDATED_ENERGY);
    }

    @Test
    @Transactional
    void getAllStoragesByEnergyIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where energy is not null
        defaultStorageShouldBeFound("energy.specified=true");

        // Get all the storageList where energy is null
        defaultStorageShouldNotBeFound("energy.specified=false");
    }

    @Test
    @Transactional
    void getAllStoragesByEnergyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where energy is greater than or equal to DEFAULT_ENERGY
        defaultStorageShouldBeFound("energy.greaterThanOrEqual=" + DEFAULT_ENERGY);

        // Get all the storageList where energy is greater than or equal to UPDATED_ENERGY
        defaultStorageShouldNotBeFound("energy.greaterThanOrEqual=" + UPDATED_ENERGY);
    }

    @Test
    @Transactional
    void getAllStoragesByEnergyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where energy is less than or equal to DEFAULT_ENERGY
        defaultStorageShouldBeFound("energy.lessThanOrEqual=" + DEFAULT_ENERGY);

        // Get all the storageList where energy is less than or equal to SMALLER_ENERGY
        defaultStorageShouldNotBeFound("energy.lessThanOrEqual=" + SMALLER_ENERGY);
    }

    @Test
    @Transactional
    void getAllStoragesByEnergyIsLessThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where energy is less than DEFAULT_ENERGY
        defaultStorageShouldNotBeFound("energy.lessThan=" + DEFAULT_ENERGY);

        // Get all the storageList where energy is less than UPDATED_ENERGY
        defaultStorageShouldBeFound("energy.lessThan=" + UPDATED_ENERGY);
    }

    @Test
    @Transactional
    void getAllStoragesByEnergyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where energy is greater than DEFAULT_ENERGY
        defaultStorageShouldNotBeFound("energy.greaterThan=" + DEFAULT_ENERGY);

        // Get all the storageList where energy is greater than SMALLER_ENERGY
        defaultStorageShouldBeFound("energy.greaterThan=" + SMALLER_ENERGY);
    }

    @Test
    @Transactional
    void getAllStoragesByeRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where eRating equals to DEFAULT_E_RATING
        defaultStorageShouldBeFound("eRating.equals=" + DEFAULT_E_RATING);

        // Get all the storageList where eRating equals to UPDATED_E_RATING
        defaultStorageShouldNotBeFound("eRating.equals=" + UPDATED_E_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByeRatingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where eRating not equals to DEFAULT_E_RATING
        defaultStorageShouldNotBeFound("eRating.notEquals=" + DEFAULT_E_RATING);

        // Get all the storageList where eRating not equals to UPDATED_E_RATING
        defaultStorageShouldBeFound("eRating.notEquals=" + UPDATED_E_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByeRatingIsInShouldWork() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where eRating in DEFAULT_E_RATING or UPDATED_E_RATING
        defaultStorageShouldBeFound("eRating.in=" + DEFAULT_E_RATING + "," + UPDATED_E_RATING);

        // Get all the storageList where eRating equals to UPDATED_E_RATING
        defaultStorageShouldNotBeFound("eRating.in=" + UPDATED_E_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByeRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where eRating is not null
        defaultStorageShouldBeFound("eRating.specified=true");

        // Get all the storageList where eRating is null
        defaultStorageShouldNotBeFound("eRating.specified=false");
    }

    @Test
    @Transactional
    void getAllStoragesByeRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where eRating is greater than or equal to DEFAULT_E_RATING
        defaultStorageShouldBeFound("eRating.greaterThanOrEqual=" + DEFAULT_E_RATING);

        // Get all the storageList where eRating is greater than or equal to UPDATED_E_RATING
        defaultStorageShouldNotBeFound("eRating.greaterThanOrEqual=" + UPDATED_E_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByeRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where eRating is less than or equal to DEFAULT_E_RATING
        defaultStorageShouldBeFound("eRating.lessThanOrEqual=" + DEFAULT_E_RATING);

        // Get all the storageList where eRating is less than or equal to SMALLER_E_RATING
        defaultStorageShouldNotBeFound("eRating.lessThanOrEqual=" + SMALLER_E_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByeRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where eRating is less than DEFAULT_E_RATING
        defaultStorageShouldNotBeFound("eRating.lessThan=" + DEFAULT_E_RATING);

        // Get all the storageList where eRating is less than UPDATED_E_RATING
        defaultStorageShouldBeFound("eRating.lessThan=" + UPDATED_E_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByeRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where eRating is greater than DEFAULT_E_RATING
        defaultStorageShouldNotBeFound("eRating.greaterThan=" + DEFAULT_E_RATING);

        // Get all the storageList where eRating is greater than SMALLER_E_RATING
        defaultStorageShouldBeFound("eRating.greaterThan=" + SMALLER_E_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByChargeRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where chargeRating equals to DEFAULT_CHARGE_RATING
        defaultStorageShouldBeFound("chargeRating.equals=" + DEFAULT_CHARGE_RATING);

        // Get all the storageList where chargeRating equals to UPDATED_CHARGE_RATING
        defaultStorageShouldNotBeFound("chargeRating.equals=" + UPDATED_CHARGE_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByChargeRatingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where chargeRating not equals to DEFAULT_CHARGE_RATING
        defaultStorageShouldNotBeFound("chargeRating.notEquals=" + DEFAULT_CHARGE_RATING);

        // Get all the storageList where chargeRating not equals to UPDATED_CHARGE_RATING
        defaultStorageShouldBeFound("chargeRating.notEquals=" + UPDATED_CHARGE_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByChargeRatingIsInShouldWork() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where chargeRating in DEFAULT_CHARGE_RATING or UPDATED_CHARGE_RATING
        defaultStorageShouldBeFound("chargeRating.in=" + DEFAULT_CHARGE_RATING + "," + UPDATED_CHARGE_RATING);

        // Get all the storageList where chargeRating equals to UPDATED_CHARGE_RATING
        defaultStorageShouldNotBeFound("chargeRating.in=" + UPDATED_CHARGE_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByChargeRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where chargeRating is not null
        defaultStorageShouldBeFound("chargeRating.specified=true");

        // Get all the storageList where chargeRating is null
        defaultStorageShouldNotBeFound("chargeRating.specified=false");
    }

    @Test
    @Transactional
    void getAllStoragesByChargeRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where chargeRating is greater than or equal to DEFAULT_CHARGE_RATING
        defaultStorageShouldBeFound("chargeRating.greaterThanOrEqual=" + DEFAULT_CHARGE_RATING);

        // Get all the storageList where chargeRating is greater than or equal to UPDATED_CHARGE_RATING
        defaultStorageShouldNotBeFound("chargeRating.greaterThanOrEqual=" + UPDATED_CHARGE_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByChargeRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where chargeRating is less than or equal to DEFAULT_CHARGE_RATING
        defaultStorageShouldBeFound("chargeRating.lessThanOrEqual=" + DEFAULT_CHARGE_RATING);

        // Get all the storageList where chargeRating is less than or equal to SMALLER_CHARGE_RATING
        defaultStorageShouldNotBeFound("chargeRating.lessThanOrEqual=" + SMALLER_CHARGE_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByChargeRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where chargeRating is less than DEFAULT_CHARGE_RATING
        defaultStorageShouldNotBeFound("chargeRating.lessThan=" + DEFAULT_CHARGE_RATING);

        // Get all the storageList where chargeRating is less than UPDATED_CHARGE_RATING
        defaultStorageShouldBeFound("chargeRating.lessThan=" + UPDATED_CHARGE_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByChargeRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where chargeRating is greater than DEFAULT_CHARGE_RATING
        defaultStorageShouldNotBeFound("chargeRating.greaterThan=" + DEFAULT_CHARGE_RATING);

        // Get all the storageList where chargeRating is greater than SMALLER_CHARGE_RATING
        defaultStorageShouldBeFound("chargeRating.greaterThan=" + SMALLER_CHARGE_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByDischargeRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where dischargeRating equals to DEFAULT_DISCHARGE_RATING
        defaultStorageShouldBeFound("dischargeRating.equals=" + DEFAULT_DISCHARGE_RATING);

        // Get all the storageList where dischargeRating equals to UPDATED_DISCHARGE_RATING
        defaultStorageShouldNotBeFound("dischargeRating.equals=" + UPDATED_DISCHARGE_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByDischargeRatingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where dischargeRating not equals to DEFAULT_DISCHARGE_RATING
        defaultStorageShouldNotBeFound("dischargeRating.notEquals=" + DEFAULT_DISCHARGE_RATING);

        // Get all the storageList where dischargeRating not equals to UPDATED_DISCHARGE_RATING
        defaultStorageShouldBeFound("dischargeRating.notEquals=" + UPDATED_DISCHARGE_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByDischargeRatingIsInShouldWork() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where dischargeRating in DEFAULT_DISCHARGE_RATING or UPDATED_DISCHARGE_RATING
        defaultStorageShouldBeFound("dischargeRating.in=" + DEFAULT_DISCHARGE_RATING + "," + UPDATED_DISCHARGE_RATING);

        // Get all the storageList where dischargeRating equals to UPDATED_DISCHARGE_RATING
        defaultStorageShouldNotBeFound("dischargeRating.in=" + UPDATED_DISCHARGE_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByDischargeRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where dischargeRating is not null
        defaultStorageShouldBeFound("dischargeRating.specified=true");

        // Get all the storageList where dischargeRating is null
        defaultStorageShouldNotBeFound("dischargeRating.specified=false");
    }

    @Test
    @Transactional
    void getAllStoragesByDischargeRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where dischargeRating is greater than or equal to DEFAULT_DISCHARGE_RATING
        defaultStorageShouldBeFound("dischargeRating.greaterThanOrEqual=" + DEFAULT_DISCHARGE_RATING);

        // Get all the storageList where dischargeRating is greater than or equal to UPDATED_DISCHARGE_RATING
        defaultStorageShouldNotBeFound("dischargeRating.greaterThanOrEqual=" + UPDATED_DISCHARGE_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByDischargeRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where dischargeRating is less than or equal to DEFAULT_DISCHARGE_RATING
        defaultStorageShouldBeFound("dischargeRating.lessThanOrEqual=" + DEFAULT_DISCHARGE_RATING);

        // Get all the storageList where dischargeRating is less than or equal to SMALLER_DISCHARGE_RATING
        defaultStorageShouldNotBeFound("dischargeRating.lessThanOrEqual=" + SMALLER_DISCHARGE_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByDischargeRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where dischargeRating is less than DEFAULT_DISCHARGE_RATING
        defaultStorageShouldNotBeFound("dischargeRating.lessThan=" + DEFAULT_DISCHARGE_RATING);

        // Get all the storageList where dischargeRating is less than UPDATED_DISCHARGE_RATING
        defaultStorageShouldBeFound("dischargeRating.lessThan=" + UPDATED_DISCHARGE_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByDischargeRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where dischargeRating is greater than DEFAULT_DISCHARGE_RATING
        defaultStorageShouldNotBeFound("dischargeRating.greaterThan=" + DEFAULT_DISCHARGE_RATING);

        // Get all the storageList where dischargeRating is greater than SMALLER_DISCHARGE_RATING
        defaultStorageShouldBeFound("dischargeRating.greaterThan=" + SMALLER_DISCHARGE_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByChargeEfficiencyIsEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where chargeEfficiency equals to DEFAULT_CHARGE_EFFICIENCY
        defaultStorageShouldBeFound("chargeEfficiency.equals=" + DEFAULT_CHARGE_EFFICIENCY);

        // Get all the storageList where chargeEfficiency equals to UPDATED_CHARGE_EFFICIENCY
        defaultStorageShouldNotBeFound("chargeEfficiency.equals=" + UPDATED_CHARGE_EFFICIENCY);
    }

    @Test
    @Transactional
    void getAllStoragesByChargeEfficiencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where chargeEfficiency not equals to DEFAULT_CHARGE_EFFICIENCY
        defaultStorageShouldNotBeFound("chargeEfficiency.notEquals=" + DEFAULT_CHARGE_EFFICIENCY);

        // Get all the storageList where chargeEfficiency not equals to UPDATED_CHARGE_EFFICIENCY
        defaultStorageShouldBeFound("chargeEfficiency.notEquals=" + UPDATED_CHARGE_EFFICIENCY);
    }

    @Test
    @Transactional
    void getAllStoragesByChargeEfficiencyIsInShouldWork() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where chargeEfficiency in DEFAULT_CHARGE_EFFICIENCY or UPDATED_CHARGE_EFFICIENCY
        defaultStorageShouldBeFound("chargeEfficiency.in=" + DEFAULT_CHARGE_EFFICIENCY + "," + UPDATED_CHARGE_EFFICIENCY);

        // Get all the storageList where chargeEfficiency equals to UPDATED_CHARGE_EFFICIENCY
        defaultStorageShouldNotBeFound("chargeEfficiency.in=" + UPDATED_CHARGE_EFFICIENCY);
    }

    @Test
    @Transactional
    void getAllStoragesByChargeEfficiencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where chargeEfficiency is not null
        defaultStorageShouldBeFound("chargeEfficiency.specified=true");

        // Get all the storageList where chargeEfficiency is null
        defaultStorageShouldNotBeFound("chargeEfficiency.specified=false");
    }

    @Test
    @Transactional
    void getAllStoragesByChargeEfficiencyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where chargeEfficiency is greater than or equal to DEFAULT_CHARGE_EFFICIENCY
        defaultStorageShouldBeFound("chargeEfficiency.greaterThanOrEqual=" + DEFAULT_CHARGE_EFFICIENCY);

        // Get all the storageList where chargeEfficiency is greater than or equal to UPDATED_CHARGE_EFFICIENCY
        defaultStorageShouldNotBeFound("chargeEfficiency.greaterThanOrEqual=" + UPDATED_CHARGE_EFFICIENCY);
    }

    @Test
    @Transactional
    void getAllStoragesByChargeEfficiencyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where chargeEfficiency is less than or equal to DEFAULT_CHARGE_EFFICIENCY
        defaultStorageShouldBeFound("chargeEfficiency.lessThanOrEqual=" + DEFAULT_CHARGE_EFFICIENCY);

        // Get all the storageList where chargeEfficiency is less than or equal to SMALLER_CHARGE_EFFICIENCY
        defaultStorageShouldNotBeFound("chargeEfficiency.lessThanOrEqual=" + SMALLER_CHARGE_EFFICIENCY);
    }

    @Test
    @Transactional
    void getAllStoragesByChargeEfficiencyIsLessThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where chargeEfficiency is less than DEFAULT_CHARGE_EFFICIENCY
        defaultStorageShouldNotBeFound("chargeEfficiency.lessThan=" + DEFAULT_CHARGE_EFFICIENCY);

        // Get all the storageList where chargeEfficiency is less than UPDATED_CHARGE_EFFICIENCY
        defaultStorageShouldBeFound("chargeEfficiency.lessThan=" + UPDATED_CHARGE_EFFICIENCY);
    }

    @Test
    @Transactional
    void getAllStoragesByChargeEfficiencyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where chargeEfficiency is greater than DEFAULT_CHARGE_EFFICIENCY
        defaultStorageShouldNotBeFound("chargeEfficiency.greaterThan=" + DEFAULT_CHARGE_EFFICIENCY);

        // Get all the storageList where chargeEfficiency is greater than SMALLER_CHARGE_EFFICIENCY
        defaultStorageShouldBeFound("chargeEfficiency.greaterThan=" + SMALLER_CHARGE_EFFICIENCY);
    }

    @Test
    @Transactional
    void getAllStoragesByThermalRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where thermalRating equals to DEFAULT_THERMAL_RATING
        defaultStorageShouldBeFound("thermalRating.equals=" + DEFAULT_THERMAL_RATING);

        // Get all the storageList where thermalRating equals to UPDATED_THERMAL_RATING
        defaultStorageShouldNotBeFound("thermalRating.equals=" + UPDATED_THERMAL_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByThermalRatingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where thermalRating not equals to DEFAULT_THERMAL_RATING
        defaultStorageShouldNotBeFound("thermalRating.notEquals=" + DEFAULT_THERMAL_RATING);

        // Get all the storageList where thermalRating not equals to UPDATED_THERMAL_RATING
        defaultStorageShouldBeFound("thermalRating.notEquals=" + UPDATED_THERMAL_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByThermalRatingIsInShouldWork() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where thermalRating in DEFAULT_THERMAL_RATING or UPDATED_THERMAL_RATING
        defaultStorageShouldBeFound("thermalRating.in=" + DEFAULT_THERMAL_RATING + "," + UPDATED_THERMAL_RATING);

        // Get all the storageList where thermalRating equals to UPDATED_THERMAL_RATING
        defaultStorageShouldNotBeFound("thermalRating.in=" + UPDATED_THERMAL_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByThermalRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where thermalRating is not null
        defaultStorageShouldBeFound("thermalRating.specified=true");

        // Get all the storageList where thermalRating is null
        defaultStorageShouldNotBeFound("thermalRating.specified=false");
    }

    @Test
    @Transactional
    void getAllStoragesByThermalRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where thermalRating is greater than or equal to DEFAULT_THERMAL_RATING
        defaultStorageShouldBeFound("thermalRating.greaterThanOrEqual=" + DEFAULT_THERMAL_RATING);

        // Get all the storageList where thermalRating is greater than or equal to UPDATED_THERMAL_RATING
        defaultStorageShouldNotBeFound("thermalRating.greaterThanOrEqual=" + UPDATED_THERMAL_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByThermalRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where thermalRating is less than or equal to DEFAULT_THERMAL_RATING
        defaultStorageShouldBeFound("thermalRating.lessThanOrEqual=" + DEFAULT_THERMAL_RATING);

        // Get all the storageList where thermalRating is less than or equal to SMALLER_THERMAL_RATING
        defaultStorageShouldNotBeFound("thermalRating.lessThanOrEqual=" + SMALLER_THERMAL_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByThermalRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where thermalRating is less than DEFAULT_THERMAL_RATING
        defaultStorageShouldNotBeFound("thermalRating.lessThan=" + DEFAULT_THERMAL_RATING);

        // Get all the storageList where thermalRating is less than UPDATED_THERMAL_RATING
        defaultStorageShouldBeFound("thermalRating.lessThan=" + UPDATED_THERMAL_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByThermalRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where thermalRating is greater than DEFAULT_THERMAL_RATING
        defaultStorageShouldNotBeFound("thermalRating.greaterThan=" + DEFAULT_THERMAL_RATING);

        // Get all the storageList where thermalRating is greater than SMALLER_THERMAL_RATING
        defaultStorageShouldBeFound("thermalRating.greaterThan=" + SMALLER_THERMAL_RATING);
    }

    @Test
    @Transactional
    void getAllStoragesByQminIsEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qmin equals to DEFAULT_QMIN
        defaultStorageShouldBeFound("qmin.equals=" + DEFAULT_QMIN);

        // Get all the storageList where qmin equals to UPDATED_QMIN
        defaultStorageShouldNotBeFound("qmin.equals=" + UPDATED_QMIN);
    }

    @Test
    @Transactional
    void getAllStoragesByQminIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qmin not equals to DEFAULT_QMIN
        defaultStorageShouldNotBeFound("qmin.notEquals=" + DEFAULT_QMIN);

        // Get all the storageList where qmin not equals to UPDATED_QMIN
        defaultStorageShouldBeFound("qmin.notEquals=" + UPDATED_QMIN);
    }

    @Test
    @Transactional
    void getAllStoragesByQminIsInShouldWork() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qmin in DEFAULT_QMIN or UPDATED_QMIN
        defaultStorageShouldBeFound("qmin.in=" + DEFAULT_QMIN + "," + UPDATED_QMIN);

        // Get all the storageList where qmin equals to UPDATED_QMIN
        defaultStorageShouldNotBeFound("qmin.in=" + UPDATED_QMIN);
    }

    @Test
    @Transactional
    void getAllStoragesByQminIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qmin is not null
        defaultStorageShouldBeFound("qmin.specified=true");

        // Get all the storageList where qmin is null
        defaultStorageShouldNotBeFound("qmin.specified=false");
    }

    @Test
    @Transactional
    void getAllStoragesByQminIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qmin is greater than or equal to DEFAULT_QMIN
        defaultStorageShouldBeFound("qmin.greaterThanOrEqual=" + DEFAULT_QMIN);

        // Get all the storageList where qmin is greater than or equal to UPDATED_QMIN
        defaultStorageShouldNotBeFound("qmin.greaterThanOrEqual=" + UPDATED_QMIN);
    }

    @Test
    @Transactional
    void getAllStoragesByQminIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qmin is less than or equal to DEFAULT_QMIN
        defaultStorageShouldBeFound("qmin.lessThanOrEqual=" + DEFAULT_QMIN);

        // Get all the storageList where qmin is less than or equal to SMALLER_QMIN
        defaultStorageShouldNotBeFound("qmin.lessThanOrEqual=" + SMALLER_QMIN);
    }

    @Test
    @Transactional
    void getAllStoragesByQminIsLessThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qmin is less than DEFAULT_QMIN
        defaultStorageShouldNotBeFound("qmin.lessThan=" + DEFAULT_QMIN);

        // Get all the storageList where qmin is less than UPDATED_QMIN
        defaultStorageShouldBeFound("qmin.lessThan=" + UPDATED_QMIN);
    }

    @Test
    @Transactional
    void getAllStoragesByQminIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qmin is greater than DEFAULT_QMIN
        defaultStorageShouldNotBeFound("qmin.greaterThan=" + DEFAULT_QMIN);

        // Get all the storageList where qmin is greater than SMALLER_QMIN
        defaultStorageShouldBeFound("qmin.greaterThan=" + SMALLER_QMIN);
    }

    @Test
    @Transactional
    void getAllStoragesByQmaxIsEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qmax equals to DEFAULT_QMAX
        defaultStorageShouldBeFound("qmax.equals=" + DEFAULT_QMAX);

        // Get all the storageList where qmax equals to UPDATED_QMAX
        defaultStorageShouldNotBeFound("qmax.equals=" + UPDATED_QMAX);
    }

    @Test
    @Transactional
    void getAllStoragesByQmaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qmax not equals to DEFAULT_QMAX
        defaultStorageShouldNotBeFound("qmax.notEquals=" + DEFAULT_QMAX);

        // Get all the storageList where qmax not equals to UPDATED_QMAX
        defaultStorageShouldBeFound("qmax.notEquals=" + UPDATED_QMAX);
    }

    @Test
    @Transactional
    void getAllStoragesByQmaxIsInShouldWork() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qmax in DEFAULT_QMAX or UPDATED_QMAX
        defaultStorageShouldBeFound("qmax.in=" + DEFAULT_QMAX + "," + UPDATED_QMAX);

        // Get all the storageList where qmax equals to UPDATED_QMAX
        defaultStorageShouldNotBeFound("qmax.in=" + UPDATED_QMAX);
    }

    @Test
    @Transactional
    void getAllStoragesByQmaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qmax is not null
        defaultStorageShouldBeFound("qmax.specified=true");

        // Get all the storageList where qmax is null
        defaultStorageShouldNotBeFound("qmax.specified=false");
    }

    @Test
    @Transactional
    void getAllStoragesByQmaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qmax is greater than or equal to DEFAULT_QMAX
        defaultStorageShouldBeFound("qmax.greaterThanOrEqual=" + DEFAULT_QMAX);

        // Get all the storageList where qmax is greater than or equal to UPDATED_QMAX
        defaultStorageShouldNotBeFound("qmax.greaterThanOrEqual=" + UPDATED_QMAX);
    }

    @Test
    @Transactional
    void getAllStoragesByQmaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qmax is less than or equal to DEFAULT_QMAX
        defaultStorageShouldBeFound("qmax.lessThanOrEqual=" + DEFAULT_QMAX);

        // Get all the storageList where qmax is less than or equal to SMALLER_QMAX
        defaultStorageShouldNotBeFound("qmax.lessThanOrEqual=" + SMALLER_QMAX);
    }

    @Test
    @Transactional
    void getAllStoragesByQmaxIsLessThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qmax is less than DEFAULT_QMAX
        defaultStorageShouldNotBeFound("qmax.lessThan=" + DEFAULT_QMAX);

        // Get all the storageList where qmax is less than UPDATED_QMAX
        defaultStorageShouldBeFound("qmax.lessThan=" + UPDATED_QMAX);
    }

    @Test
    @Transactional
    void getAllStoragesByQmaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qmax is greater than DEFAULT_QMAX
        defaultStorageShouldNotBeFound("qmax.greaterThan=" + DEFAULT_QMAX);

        // Get all the storageList where qmax is greater than SMALLER_QMAX
        defaultStorageShouldBeFound("qmax.greaterThan=" + SMALLER_QMAX);
    }

    @Test
    @Transactional
    void getAllStoragesByRIsEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where r equals to DEFAULT_R
        defaultStorageShouldBeFound("r.equals=" + DEFAULT_R);

        // Get all the storageList where r equals to UPDATED_R
        defaultStorageShouldNotBeFound("r.equals=" + UPDATED_R);
    }

    @Test
    @Transactional
    void getAllStoragesByRIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where r not equals to DEFAULT_R
        defaultStorageShouldNotBeFound("r.notEquals=" + DEFAULT_R);

        // Get all the storageList where r not equals to UPDATED_R
        defaultStorageShouldBeFound("r.notEquals=" + UPDATED_R);
    }

    @Test
    @Transactional
    void getAllStoragesByRIsInShouldWork() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where r in DEFAULT_R or UPDATED_R
        defaultStorageShouldBeFound("r.in=" + DEFAULT_R + "," + UPDATED_R);

        // Get all the storageList where r equals to UPDATED_R
        defaultStorageShouldNotBeFound("r.in=" + UPDATED_R);
    }

    @Test
    @Transactional
    void getAllStoragesByRIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where r is not null
        defaultStorageShouldBeFound("r.specified=true");

        // Get all the storageList where r is null
        defaultStorageShouldNotBeFound("r.specified=false");
    }

    @Test
    @Transactional
    void getAllStoragesByRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where r is greater than or equal to DEFAULT_R
        defaultStorageShouldBeFound("r.greaterThanOrEqual=" + DEFAULT_R);

        // Get all the storageList where r is greater than or equal to UPDATED_R
        defaultStorageShouldNotBeFound("r.greaterThanOrEqual=" + UPDATED_R);
    }

    @Test
    @Transactional
    void getAllStoragesByRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where r is less than or equal to DEFAULT_R
        defaultStorageShouldBeFound("r.lessThanOrEqual=" + DEFAULT_R);

        // Get all the storageList where r is less than or equal to SMALLER_R
        defaultStorageShouldNotBeFound("r.lessThanOrEqual=" + SMALLER_R);
    }

    @Test
    @Transactional
    void getAllStoragesByRIsLessThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where r is less than DEFAULT_R
        defaultStorageShouldNotBeFound("r.lessThan=" + DEFAULT_R);

        // Get all the storageList where r is less than UPDATED_R
        defaultStorageShouldBeFound("r.lessThan=" + UPDATED_R);
    }

    @Test
    @Transactional
    void getAllStoragesByRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where r is greater than DEFAULT_R
        defaultStorageShouldNotBeFound("r.greaterThan=" + DEFAULT_R);

        // Get all the storageList where r is greater than SMALLER_R
        defaultStorageShouldBeFound("r.greaterThan=" + SMALLER_R);
    }

    @Test
    @Transactional
    void getAllStoragesByXIsEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where x equals to DEFAULT_X
        defaultStorageShouldBeFound("x.equals=" + DEFAULT_X);

        // Get all the storageList where x equals to UPDATED_X
        defaultStorageShouldNotBeFound("x.equals=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllStoragesByXIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where x not equals to DEFAULT_X
        defaultStorageShouldNotBeFound("x.notEquals=" + DEFAULT_X);

        // Get all the storageList where x not equals to UPDATED_X
        defaultStorageShouldBeFound("x.notEquals=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllStoragesByXIsInShouldWork() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where x in DEFAULT_X or UPDATED_X
        defaultStorageShouldBeFound("x.in=" + DEFAULT_X + "," + UPDATED_X);

        // Get all the storageList where x equals to UPDATED_X
        defaultStorageShouldNotBeFound("x.in=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllStoragesByXIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where x is not null
        defaultStorageShouldBeFound("x.specified=true");

        // Get all the storageList where x is null
        defaultStorageShouldNotBeFound("x.specified=false");
    }

    @Test
    @Transactional
    void getAllStoragesByXIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where x is greater than or equal to DEFAULT_X
        defaultStorageShouldBeFound("x.greaterThanOrEqual=" + DEFAULT_X);

        // Get all the storageList where x is greater than or equal to UPDATED_X
        defaultStorageShouldNotBeFound("x.greaterThanOrEqual=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllStoragesByXIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where x is less than or equal to DEFAULT_X
        defaultStorageShouldBeFound("x.lessThanOrEqual=" + DEFAULT_X);

        // Get all the storageList where x is less than or equal to SMALLER_X
        defaultStorageShouldNotBeFound("x.lessThanOrEqual=" + SMALLER_X);
    }

    @Test
    @Transactional
    void getAllStoragesByXIsLessThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where x is less than DEFAULT_X
        defaultStorageShouldNotBeFound("x.lessThan=" + DEFAULT_X);

        // Get all the storageList where x is less than UPDATED_X
        defaultStorageShouldBeFound("x.lessThan=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllStoragesByXIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where x is greater than DEFAULT_X
        defaultStorageShouldNotBeFound("x.greaterThan=" + DEFAULT_X);

        // Get all the storageList where x is greater than SMALLER_X
        defaultStorageShouldBeFound("x.greaterThan=" + SMALLER_X);
    }

    @Test
    @Transactional
    void getAllStoragesBypLossIsEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where pLoss equals to DEFAULT_P_LOSS
        defaultStorageShouldBeFound("pLoss.equals=" + DEFAULT_P_LOSS);

        // Get all the storageList where pLoss equals to UPDATED_P_LOSS
        defaultStorageShouldNotBeFound("pLoss.equals=" + UPDATED_P_LOSS);
    }

    @Test
    @Transactional
    void getAllStoragesBypLossIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where pLoss not equals to DEFAULT_P_LOSS
        defaultStorageShouldNotBeFound("pLoss.notEquals=" + DEFAULT_P_LOSS);

        // Get all the storageList where pLoss not equals to UPDATED_P_LOSS
        defaultStorageShouldBeFound("pLoss.notEquals=" + UPDATED_P_LOSS);
    }

    @Test
    @Transactional
    void getAllStoragesBypLossIsInShouldWork() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where pLoss in DEFAULT_P_LOSS or UPDATED_P_LOSS
        defaultStorageShouldBeFound("pLoss.in=" + DEFAULT_P_LOSS + "," + UPDATED_P_LOSS);

        // Get all the storageList where pLoss equals to UPDATED_P_LOSS
        defaultStorageShouldNotBeFound("pLoss.in=" + UPDATED_P_LOSS);
    }

    @Test
    @Transactional
    void getAllStoragesBypLossIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where pLoss is not null
        defaultStorageShouldBeFound("pLoss.specified=true");

        // Get all the storageList where pLoss is null
        defaultStorageShouldNotBeFound("pLoss.specified=false");
    }

    @Test
    @Transactional
    void getAllStoragesBypLossIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where pLoss is greater than or equal to DEFAULT_P_LOSS
        defaultStorageShouldBeFound("pLoss.greaterThanOrEqual=" + DEFAULT_P_LOSS);

        // Get all the storageList where pLoss is greater than or equal to UPDATED_P_LOSS
        defaultStorageShouldNotBeFound("pLoss.greaterThanOrEqual=" + UPDATED_P_LOSS);
    }

    @Test
    @Transactional
    void getAllStoragesBypLossIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where pLoss is less than or equal to DEFAULT_P_LOSS
        defaultStorageShouldBeFound("pLoss.lessThanOrEqual=" + DEFAULT_P_LOSS);

        // Get all the storageList where pLoss is less than or equal to SMALLER_P_LOSS
        defaultStorageShouldNotBeFound("pLoss.lessThanOrEqual=" + SMALLER_P_LOSS);
    }

    @Test
    @Transactional
    void getAllStoragesBypLossIsLessThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where pLoss is less than DEFAULT_P_LOSS
        defaultStorageShouldNotBeFound("pLoss.lessThan=" + DEFAULT_P_LOSS);

        // Get all the storageList where pLoss is less than UPDATED_P_LOSS
        defaultStorageShouldBeFound("pLoss.lessThan=" + UPDATED_P_LOSS);
    }

    @Test
    @Transactional
    void getAllStoragesBypLossIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where pLoss is greater than DEFAULT_P_LOSS
        defaultStorageShouldNotBeFound("pLoss.greaterThan=" + DEFAULT_P_LOSS);

        // Get all the storageList where pLoss is greater than SMALLER_P_LOSS
        defaultStorageShouldBeFound("pLoss.greaterThan=" + SMALLER_P_LOSS);
    }

    @Test
    @Transactional
    void getAllStoragesByqLossIsEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qLoss equals to DEFAULT_Q_LOSS
        defaultStorageShouldBeFound("qLoss.equals=" + DEFAULT_Q_LOSS);

        // Get all the storageList where qLoss equals to UPDATED_Q_LOSS
        defaultStorageShouldNotBeFound("qLoss.equals=" + UPDATED_Q_LOSS);
    }

    @Test
    @Transactional
    void getAllStoragesByqLossIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qLoss not equals to DEFAULT_Q_LOSS
        defaultStorageShouldNotBeFound("qLoss.notEquals=" + DEFAULT_Q_LOSS);

        // Get all the storageList where qLoss not equals to UPDATED_Q_LOSS
        defaultStorageShouldBeFound("qLoss.notEquals=" + UPDATED_Q_LOSS);
    }

    @Test
    @Transactional
    void getAllStoragesByqLossIsInShouldWork() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qLoss in DEFAULT_Q_LOSS or UPDATED_Q_LOSS
        defaultStorageShouldBeFound("qLoss.in=" + DEFAULT_Q_LOSS + "," + UPDATED_Q_LOSS);

        // Get all the storageList where qLoss equals to UPDATED_Q_LOSS
        defaultStorageShouldNotBeFound("qLoss.in=" + UPDATED_Q_LOSS);
    }

    @Test
    @Transactional
    void getAllStoragesByqLossIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qLoss is not null
        defaultStorageShouldBeFound("qLoss.specified=true");

        // Get all the storageList where qLoss is null
        defaultStorageShouldNotBeFound("qLoss.specified=false");
    }

    @Test
    @Transactional
    void getAllStoragesByqLossIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qLoss is greater than or equal to DEFAULT_Q_LOSS
        defaultStorageShouldBeFound("qLoss.greaterThanOrEqual=" + DEFAULT_Q_LOSS);

        // Get all the storageList where qLoss is greater than or equal to UPDATED_Q_LOSS
        defaultStorageShouldNotBeFound("qLoss.greaterThanOrEqual=" + UPDATED_Q_LOSS);
    }

    @Test
    @Transactional
    void getAllStoragesByqLossIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qLoss is less than or equal to DEFAULT_Q_LOSS
        defaultStorageShouldBeFound("qLoss.lessThanOrEqual=" + DEFAULT_Q_LOSS);

        // Get all the storageList where qLoss is less than or equal to SMALLER_Q_LOSS
        defaultStorageShouldNotBeFound("qLoss.lessThanOrEqual=" + SMALLER_Q_LOSS);
    }

    @Test
    @Transactional
    void getAllStoragesByqLossIsLessThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qLoss is less than DEFAULT_Q_LOSS
        defaultStorageShouldNotBeFound("qLoss.lessThan=" + DEFAULT_Q_LOSS);

        // Get all the storageList where qLoss is less than UPDATED_Q_LOSS
        defaultStorageShouldBeFound("qLoss.lessThan=" + UPDATED_Q_LOSS);
    }

    @Test
    @Transactional
    void getAllStoragesByqLossIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where qLoss is greater than DEFAULT_Q_LOSS
        defaultStorageShouldNotBeFound("qLoss.greaterThan=" + DEFAULT_Q_LOSS);

        // Get all the storageList where qLoss is greater than SMALLER_Q_LOSS
        defaultStorageShouldBeFound("qLoss.greaterThan=" + SMALLER_Q_LOSS);
    }

    @Test
    @Transactional
    void getAllStoragesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where status equals to DEFAULT_STATUS
        defaultStorageShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the storageList where status equals to UPDATED_STATUS
        defaultStorageShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllStoragesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where status not equals to DEFAULT_STATUS
        defaultStorageShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the storageList where status not equals to UPDATED_STATUS
        defaultStorageShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllStoragesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultStorageShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the storageList where status equals to UPDATED_STATUS
        defaultStorageShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllStoragesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where status is not null
        defaultStorageShouldBeFound("status.specified=true");

        // Get all the storageList where status is null
        defaultStorageShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllStoragesByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where status is greater than or equal to DEFAULT_STATUS
        defaultStorageShouldBeFound("status.greaterThanOrEqual=" + DEFAULT_STATUS);

        // Get all the storageList where status is greater than or equal to UPDATED_STATUS
        defaultStorageShouldNotBeFound("status.greaterThanOrEqual=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllStoragesByStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where status is less than or equal to DEFAULT_STATUS
        defaultStorageShouldBeFound("status.lessThanOrEqual=" + DEFAULT_STATUS);

        // Get all the storageList where status is less than or equal to SMALLER_STATUS
        defaultStorageShouldNotBeFound("status.lessThanOrEqual=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllStoragesByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where status is less than DEFAULT_STATUS
        defaultStorageShouldNotBeFound("status.lessThan=" + DEFAULT_STATUS);

        // Get all the storageList where status is less than UPDATED_STATUS
        defaultStorageShouldBeFound("status.lessThan=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllStoragesByStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where status is greater than DEFAULT_STATUS
        defaultStorageShouldNotBeFound("status.greaterThan=" + DEFAULT_STATUS);

        // Get all the storageList where status is greater than SMALLER_STATUS
        defaultStorageShouldBeFound("status.greaterThan=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllStoragesBySocInitialIsEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socInitial equals to DEFAULT_SOC_INITIAL
        defaultStorageShouldBeFound("socInitial.equals=" + DEFAULT_SOC_INITIAL);

        // Get all the storageList where socInitial equals to UPDATED_SOC_INITIAL
        defaultStorageShouldNotBeFound("socInitial.equals=" + UPDATED_SOC_INITIAL);
    }

    @Test
    @Transactional
    void getAllStoragesBySocInitialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socInitial not equals to DEFAULT_SOC_INITIAL
        defaultStorageShouldNotBeFound("socInitial.notEquals=" + DEFAULT_SOC_INITIAL);

        // Get all the storageList where socInitial not equals to UPDATED_SOC_INITIAL
        defaultStorageShouldBeFound("socInitial.notEquals=" + UPDATED_SOC_INITIAL);
    }

    @Test
    @Transactional
    void getAllStoragesBySocInitialIsInShouldWork() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socInitial in DEFAULT_SOC_INITIAL or UPDATED_SOC_INITIAL
        defaultStorageShouldBeFound("socInitial.in=" + DEFAULT_SOC_INITIAL + "," + UPDATED_SOC_INITIAL);

        // Get all the storageList where socInitial equals to UPDATED_SOC_INITIAL
        defaultStorageShouldNotBeFound("socInitial.in=" + UPDATED_SOC_INITIAL);
    }

    @Test
    @Transactional
    void getAllStoragesBySocInitialIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socInitial is not null
        defaultStorageShouldBeFound("socInitial.specified=true");

        // Get all the storageList where socInitial is null
        defaultStorageShouldNotBeFound("socInitial.specified=false");
    }

    @Test
    @Transactional
    void getAllStoragesBySocInitialIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socInitial is greater than or equal to DEFAULT_SOC_INITIAL
        defaultStorageShouldBeFound("socInitial.greaterThanOrEqual=" + DEFAULT_SOC_INITIAL);

        // Get all the storageList where socInitial is greater than or equal to UPDATED_SOC_INITIAL
        defaultStorageShouldNotBeFound("socInitial.greaterThanOrEqual=" + UPDATED_SOC_INITIAL);
    }

    @Test
    @Transactional
    void getAllStoragesBySocInitialIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socInitial is less than or equal to DEFAULT_SOC_INITIAL
        defaultStorageShouldBeFound("socInitial.lessThanOrEqual=" + DEFAULT_SOC_INITIAL);

        // Get all the storageList where socInitial is less than or equal to SMALLER_SOC_INITIAL
        defaultStorageShouldNotBeFound("socInitial.lessThanOrEqual=" + SMALLER_SOC_INITIAL);
    }

    @Test
    @Transactional
    void getAllStoragesBySocInitialIsLessThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socInitial is less than DEFAULT_SOC_INITIAL
        defaultStorageShouldNotBeFound("socInitial.lessThan=" + DEFAULT_SOC_INITIAL);

        // Get all the storageList where socInitial is less than UPDATED_SOC_INITIAL
        defaultStorageShouldBeFound("socInitial.lessThan=" + UPDATED_SOC_INITIAL);
    }

    @Test
    @Transactional
    void getAllStoragesBySocInitialIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socInitial is greater than DEFAULT_SOC_INITIAL
        defaultStorageShouldNotBeFound("socInitial.greaterThan=" + DEFAULT_SOC_INITIAL);

        // Get all the storageList where socInitial is greater than SMALLER_SOC_INITIAL
        defaultStorageShouldBeFound("socInitial.greaterThan=" + SMALLER_SOC_INITIAL);
    }

    @Test
    @Transactional
    void getAllStoragesBySocMinIsEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socMin equals to DEFAULT_SOC_MIN
        defaultStorageShouldBeFound("socMin.equals=" + DEFAULT_SOC_MIN);

        // Get all the storageList where socMin equals to UPDATED_SOC_MIN
        defaultStorageShouldNotBeFound("socMin.equals=" + UPDATED_SOC_MIN);
    }

    @Test
    @Transactional
    void getAllStoragesBySocMinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socMin not equals to DEFAULT_SOC_MIN
        defaultStorageShouldNotBeFound("socMin.notEquals=" + DEFAULT_SOC_MIN);

        // Get all the storageList where socMin not equals to UPDATED_SOC_MIN
        defaultStorageShouldBeFound("socMin.notEquals=" + UPDATED_SOC_MIN);
    }

    @Test
    @Transactional
    void getAllStoragesBySocMinIsInShouldWork() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socMin in DEFAULT_SOC_MIN or UPDATED_SOC_MIN
        defaultStorageShouldBeFound("socMin.in=" + DEFAULT_SOC_MIN + "," + UPDATED_SOC_MIN);

        // Get all the storageList where socMin equals to UPDATED_SOC_MIN
        defaultStorageShouldNotBeFound("socMin.in=" + UPDATED_SOC_MIN);
    }

    @Test
    @Transactional
    void getAllStoragesBySocMinIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socMin is not null
        defaultStorageShouldBeFound("socMin.specified=true");

        // Get all the storageList where socMin is null
        defaultStorageShouldNotBeFound("socMin.specified=false");
    }

    @Test
    @Transactional
    void getAllStoragesBySocMinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socMin is greater than or equal to DEFAULT_SOC_MIN
        defaultStorageShouldBeFound("socMin.greaterThanOrEqual=" + DEFAULT_SOC_MIN);

        // Get all the storageList where socMin is greater than or equal to UPDATED_SOC_MIN
        defaultStorageShouldNotBeFound("socMin.greaterThanOrEqual=" + UPDATED_SOC_MIN);
    }

    @Test
    @Transactional
    void getAllStoragesBySocMinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socMin is less than or equal to DEFAULT_SOC_MIN
        defaultStorageShouldBeFound("socMin.lessThanOrEqual=" + DEFAULT_SOC_MIN);

        // Get all the storageList where socMin is less than or equal to SMALLER_SOC_MIN
        defaultStorageShouldNotBeFound("socMin.lessThanOrEqual=" + SMALLER_SOC_MIN);
    }

    @Test
    @Transactional
    void getAllStoragesBySocMinIsLessThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socMin is less than DEFAULT_SOC_MIN
        defaultStorageShouldNotBeFound("socMin.lessThan=" + DEFAULT_SOC_MIN);

        // Get all the storageList where socMin is less than UPDATED_SOC_MIN
        defaultStorageShouldBeFound("socMin.lessThan=" + UPDATED_SOC_MIN);
    }

    @Test
    @Transactional
    void getAllStoragesBySocMinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socMin is greater than DEFAULT_SOC_MIN
        defaultStorageShouldNotBeFound("socMin.greaterThan=" + DEFAULT_SOC_MIN);

        // Get all the storageList where socMin is greater than SMALLER_SOC_MIN
        defaultStorageShouldBeFound("socMin.greaterThan=" + SMALLER_SOC_MIN);
    }

    @Test
    @Transactional
    void getAllStoragesBySocMaxIsEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socMax equals to DEFAULT_SOC_MAX
        defaultStorageShouldBeFound("socMax.equals=" + DEFAULT_SOC_MAX);

        // Get all the storageList where socMax equals to UPDATED_SOC_MAX
        defaultStorageShouldNotBeFound("socMax.equals=" + UPDATED_SOC_MAX);
    }

    @Test
    @Transactional
    void getAllStoragesBySocMaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socMax not equals to DEFAULT_SOC_MAX
        defaultStorageShouldNotBeFound("socMax.notEquals=" + DEFAULT_SOC_MAX);

        // Get all the storageList where socMax not equals to UPDATED_SOC_MAX
        defaultStorageShouldBeFound("socMax.notEquals=" + UPDATED_SOC_MAX);
    }

    @Test
    @Transactional
    void getAllStoragesBySocMaxIsInShouldWork() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socMax in DEFAULT_SOC_MAX or UPDATED_SOC_MAX
        defaultStorageShouldBeFound("socMax.in=" + DEFAULT_SOC_MAX + "," + UPDATED_SOC_MAX);

        // Get all the storageList where socMax equals to UPDATED_SOC_MAX
        defaultStorageShouldNotBeFound("socMax.in=" + UPDATED_SOC_MAX);
    }

    @Test
    @Transactional
    void getAllStoragesBySocMaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socMax is not null
        defaultStorageShouldBeFound("socMax.specified=true");

        // Get all the storageList where socMax is null
        defaultStorageShouldNotBeFound("socMax.specified=false");
    }

    @Test
    @Transactional
    void getAllStoragesBySocMaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socMax is greater than or equal to DEFAULT_SOC_MAX
        defaultStorageShouldBeFound("socMax.greaterThanOrEqual=" + DEFAULT_SOC_MAX);

        // Get all the storageList where socMax is greater than or equal to UPDATED_SOC_MAX
        defaultStorageShouldNotBeFound("socMax.greaterThanOrEqual=" + UPDATED_SOC_MAX);
    }

    @Test
    @Transactional
    void getAllStoragesBySocMaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socMax is less than or equal to DEFAULT_SOC_MAX
        defaultStorageShouldBeFound("socMax.lessThanOrEqual=" + DEFAULT_SOC_MAX);

        // Get all the storageList where socMax is less than or equal to SMALLER_SOC_MAX
        defaultStorageShouldNotBeFound("socMax.lessThanOrEqual=" + SMALLER_SOC_MAX);
    }

    @Test
    @Transactional
    void getAllStoragesBySocMaxIsLessThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socMax is less than DEFAULT_SOC_MAX
        defaultStorageShouldNotBeFound("socMax.lessThan=" + DEFAULT_SOC_MAX);

        // Get all the storageList where socMax is less than UPDATED_SOC_MAX
        defaultStorageShouldBeFound("socMax.lessThan=" + UPDATED_SOC_MAX);
    }

    @Test
    @Transactional
    void getAllStoragesBySocMaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        // Get all the storageList where socMax is greater than DEFAULT_SOC_MAX
        defaultStorageShouldNotBeFound("socMax.greaterThan=" + DEFAULT_SOC_MAX);

        // Get all the storageList where socMax is greater than SMALLER_SOC_MAX
        defaultStorageShouldBeFound("socMax.greaterThan=" + SMALLER_SOC_MAX);
    }

    @Test
    @Transactional
    void getAllStoragesByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);
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
        storage.setNetwork(network);
        storageRepository.saveAndFlush(storage);
        Long networkId = network.getId();

        // Get all the storageList where network equals to networkId
        defaultStorageShouldBeFound("networkId.equals=" + networkId);

        // Get all the storageList where network equals to (networkId + 1)
        defaultStorageShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStorageShouldBeFound(String filter) throws Exception {
        restStorageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storage.getId().intValue())))
            .andExpect(jsonPath("$.[*].busNum").value(hasItem(DEFAULT_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].ps").value(hasItem(DEFAULT_PS.doubleValue())))
            .andExpect(jsonPath("$.[*].qs").value(hasItem(DEFAULT_QS.doubleValue())))
            .andExpect(jsonPath("$.[*].energy").value(hasItem(DEFAULT_ENERGY.doubleValue())))
            .andExpect(jsonPath("$.[*].eRating").value(hasItem(DEFAULT_E_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].chargeRating").value(hasItem(DEFAULT_CHARGE_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].dischargeRating").value(hasItem(DEFAULT_DISCHARGE_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].chargeEfficiency").value(hasItem(DEFAULT_CHARGE_EFFICIENCY.doubleValue())))
            .andExpect(jsonPath("$.[*].thermalRating").value(hasItem(DEFAULT_THERMAL_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].qmin").value(hasItem(DEFAULT_QMIN.doubleValue())))
            .andExpect(jsonPath("$.[*].qmax").value(hasItem(DEFAULT_QMAX.doubleValue())))
            .andExpect(jsonPath("$.[*].r").value(hasItem(DEFAULT_R.doubleValue())))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X.doubleValue())))
            .andExpect(jsonPath("$.[*].pLoss").value(hasItem(DEFAULT_P_LOSS.doubleValue())))
            .andExpect(jsonPath("$.[*].qLoss").value(hasItem(DEFAULT_Q_LOSS.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].socInitial").value(hasItem(DEFAULT_SOC_INITIAL.doubleValue())))
            .andExpect(jsonPath("$.[*].socMin").value(hasItem(DEFAULT_SOC_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].socMax").value(hasItem(DEFAULT_SOC_MAX.doubleValue())));

        // Check, that the count call also returns 1
        restStorageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStorageShouldNotBeFound(String filter) throws Exception {
        restStorageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStorageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStorage() throws Exception {
        // Get the storage
        restStorageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStorage() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        int databaseSizeBeforeUpdate = storageRepository.findAll().size();

        // Update the storage
        Storage updatedStorage = storageRepository.findById(storage.getId()).get();
        // Disconnect from session so that the updates on updatedStorage are not directly saved in db
        em.detach(updatedStorage);
        updatedStorage
            .busNum(UPDATED_BUS_NUM)
            .ps(UPDATED_PS)
            .qs(UPDATED_QS)
            .energy(UPDATED_ENERGY)
            .eRating(UPDATED_E_RATING)
            .chargeRating(UPDATED_CHARGE_RATING)
            .dischargeRating(UPDATED_DISCHARGE_RATING)
            .chargeEfficiency(UPDATED_CHARGE_EFFICIENCY)
            .thermalRating(UPDATED_THERMAL_RATING)
            .qmin(UPDATED_QMIN)
            .qmax(UPDATED_QMAX)
            .r(UPDATED_R)
            .x(UPDATED_X)
            .pLoss(UPDATED_P_LOSS)
            .qLoss(UPDATED_Q_LOSS)
            .status(UPDATED_STATUS)
            .socInitial(UPDATED_SOC_INITIAL)
            .socMin(UPDATED_SOC_MIN)
            .socMax(UPDATED_SOC_MAX);
        StorageDTO storageDTO = storageMapper.toDto(updatedStorage);

        restStorageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storageDTO))
            )
            .andExpect(status().isOk());

        // Validate the Storage in the database
        List<Storage> storageList = storageRepository.findAll();
        assertThat(storageList).hasSize(databaseSizeBeforeUpdate);
        Storage testStorage = storageList.get(storageList.size() - 1);
        assertThat(testStorage.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testStorage.getPs()).isEqualTo(UPDATED_PS);
        assertThat(testStorage.getQs()).isEqualTo(UPDATED_QS);
        assertThat(testStorage.getEnergy()).isEqualTo(UPDATED_ENERGY);
        assertThat(testStorage.geteRating()).isEqualTo(UPDATED_E_RATING);
        assertThat(testStorage.getChargeRating()).isEqualTo(UPDATED_CHARGE_RATING);
        assertThat(testStorage.getDischargeRating()).isEqualTo(UPDATED_DISCHARGE_RATING);
        assertThat(testStorage.getChargeEfficiency()).isEqualTo(UPDATED_CHARGE_EFFICIENCY);
        assertThat(testStorage.getThermalRating()).isEqualTo(UPDATED_THERMAL_RATING);
        assertThat(testStorage.getQmin()).isEqualTo(UPDATED_QMIN);
        assertThat(testStorage.getQmax()).isEqualTo(UPDATED_QMAX);
        assertThat(testStorage.getR()).isEqualTo(UPDATED_R);
        assertThat(testStorage.getX()).isEqualTo(UPDATED_X);
        assertThat(testStorage.getpLoss()).isEqualTo(UPDATED_P_LOSS);
        assertThat(testStorage.getqLoss()).isEqualTo(UPDATED_Q_LOSS);
        assertThat(testStorage.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStorage.getSocInitial()).isEqualTo(UPDATED_SOC_INITIAL);
        assertThat(testStorage.getSocMin()).isEqualTo(UPDATED_SOC_MIN);
        assertThat(testStorage.getSocMax()).isEqualTo(UPDATED_SOC_MAX);
    }

    @Test
    @Transactional
    void putNonExistingStorage() throws Exception {
        int databaseSizeBeforeUpdate = storageRepository.findAll().size();
        storage.setId(count.incrementAndGet());

        // Create the Storage
        StorageDTO storageDTO = storageMapper.toDto(storage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStorageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Storage in the database
        List<Storage> storageList = storageRepository.findAll();
        assertThat(storageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStorage() throws Exception {
        int databaseSizeBeforeUpdate = storageRepository.findAll().size();
        storage.setId(count.incrementAndGet());

        // Create the Storage
        StorageDTO storageDTO = storageMapper.toDto(storage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Storage in the database
        List<Storage> storageList = storageRepository.findAll();
        assertThat(storageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStorage() throws Exception {
        int databaseSizeBeforeUpdate = storageRepository.findAll().size();
        storage.setId(count.incrementAndGet());

        // Create the Storage
        StorageDTO storageDTO = storageMapper.toDto(storage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Storage in the database
        List<Storage> storageList = storageRepository.findAll();
        assertThat(storageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStorageWithPatch() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        int databaseSizeBeforeUpdate = storageRepository.findAll().size();

        // Update the storage using partial update
        Storage partialUpdatedStorage = new Storage();
        partialUpdatedStorage.setId(storage.getId());

        partialUpdatedStorage
            .ps(UPDATED_PS)
            .energy(UPDATED_ENERGY)
            .dischargeRating(UPDATED_DISCHARGE_RATING)
            .chargeEfficiency(UPDATED_CHARGE_EFFICIENCY)
            .pLoss(UPDATED_P_LOSS)
            .qLoss(UPDATED_Q_LOSS)
            .socInitial(UPDATED_SOC_INITIAL)
            .socMin(UPDATED_SOC_MIN);

        restStorageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStorage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStorage))
            )
            .andExpect(status().isOk());

        // Validate the Storage in the database
        List<Storage> storageList = storageRepository.findAll();
        assertThat(storageList).hasSize(databaseSizeBeforeUpdate);
        Storage testStorage = storageList.get(storageList.size() - 1);
        assertThat(testStorage.getBusNum()).isEqualTo(DEFAULT_BUS_NUM);
        assertThat(testStorage.getPs()).isEqualTo(UPDATED_PS);
        assertThat(testStorage.getQs()).isEqualTo(DEFAULT_QS);
        assertThat(testStorage.getEnergy()).isEqualTo(UPDATED_ENERGY);
        assertThat(testStorage.geteRating()).isEqualTo(DEFAULT_E_RATING);
        assertThat(testStorage.getChargeRating()).isEqualTo(DEFAULT_CHARGE_RATING);
        assertThat(testStorage.getDischargeRating()).isEqualTo(UPDATED_DISCHARGE_RATING);
        assertThat(testStorage.getChargeEfficiency()).isEqualTo(UPDATED_CHARGE_EFFICIENCY);
        assertThat(testStorage.getThermalRating()).isEqualTo(DEFAULT_THERMAL_RATING);
        assertThat(testStorage.getQmin()).isEqualTo(DEFAULT_QMIN);
        assertThat(testStorage.getQmax()).isEqualTo(DEFAULT_QMAX);
        assertThat(testStorage.getR()).isEqualTo(DEFAULT_R);
        assertThat(testStorage.getX()).isEqualTo(DEFAULT_X);
        assertThat(testStorage.getpLoss()).isEqualTo(UPDATED_P_LOSS);
        assertThat(testStorage.getqLoss()).isEqualTo(UPDATED_Q_LOSS);
        assertThat(testStorage.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStorage.getSocInitial()).isEqualTo(UPDATED_SOC_INITIAL);
        assertThat(testStorage.getSocMin()).isEqualTo(UPDATED_SOC_MIN);
        assertThat(testStorage.getSocMax()).isEqualTo(DEFAULT_SOC_MAX);
    }

    @Test
    @Transactional
    void fullUpdateStorageWithPatch() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        int databaseSizeBeforeUpdate = storageRepository.findAll().size();

        // Update the storage using partial update
        Storage partialUpdatedStorage = new Storage();
        partialUpdatedStorage.setId(storage.getId());

        partialUpdatedStorage
            .busNum(UPDATED_BUS_NUM)
            .ps(UPDATED_PS)
            .qs(UPDATED_QS)
            .energy(UPDATED_ENERGY)
            .eRating(UPDATED_E_RATING)
            .chargeRating(UPDATED_CHARGE_RATING)
            .dischargeRating(UPDATED_DISCHARGE_RATING)
            .chargeEfficiency(UPDATED_CHARGE_EFFICIENCY)
            .thermalRating(UPDATED_THERMAL_RATING)
            .qmin(UPDATED_QMIN)
            .qmax(UPDATED_QMAX)
            .r(UPDATED_R)
            .x(UPDATED_X)
            .pLoss(UPDATED_P_LOSS)
            .qLoss(UPDATED_Q_LOSS)
            .status(UPDATED_STATUS)
            .socInitial(UPDATED_SOC_INITIAL)
            .socMin(UPDATED_SOC_MIN)
            .socMax(UPDATED_SOC_MAX);

        restStorageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStorage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStorage))
            )
            .andExpect(status().isOk());

        // Validate the Storage in the database
        List<Storage> storageList = storageRepository.findAll();
        assertThat(storageList).hasSize(databaseSizeBeforeUpdate);
        Storage testStorage = storageList.get(storageList.size() - 1);
        assertThat(testStorage.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testStorage.getPs()).isEqualTo(UPDATED_PS);
        assertThat(testStorage.getQs()).isEqualTo(UPDATED_QS);
        assertThat(testStorage.getEnergy()).isEqualTo(UPDATED_ENERGY);
        assertThat(testStorage.geteRating()).isEqualTo(UPDATED_E_RATING);
        assertThat(testStorage.getChargeRating()).isEqualTo(UPDATED_CHARGE_RATING);
        assertThat(testStorage.getDischargeRating()).isEqualTo(UPDATED_DISCHARGE_RATING);
        assertThat(testStorage.getChargeEfficiency()).isEqualTo(UPDATED_CHARGE_EFFICIENCY);
        assertThat(testStorage.getThermalRating()).isEqualTo(UPDATED_THERMAL_RATING);
        assertThat(testStorage.getQmin()).isEqualTo(UPDATED_QMIN);
        assertThat(testStorage.getQmax()).isEqualTo(UPDATED_QMAX);
        assertThat(testStorage.getR()).isEqualTo(UPDATED_R);
        assertThat(testStorage.getX()).isEqualTo(UPDATED_X);
        assertThat(testStorage.getpLoss()).isEqualTo(UPDATED_P_LOSS);
        assertThat(testStorage.getqLoss()).isEqualTo(UPDATED_Q_LOSS);
        assertThat(testStorage.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStorage.getSocInitial()).isEqualTo(UPDATED_SOC_INITIAL);
        assertThat(testStorage.getSocMin()).isEqualTo(UPDATED_SOC_MIN);
        assertThat(testStorage.getSocMax()).isEqualTo(UPDATED_SOC_MAX);
    }

    @Test
    @Transactional
    void patchNonExistingStorage() throws Exception {
        int databaseSizeBeforeUpdate = storageRepository.findAll().size();
        storage.setId(count.incrementAndGet());

        // Create the Storage
        StorageDTO storageDTO = storageMapper.toDto(storage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStorageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, storageDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Storage in the database
        List<Storage> storageList = storageRepository.findAll();
        assertThat(storageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStorage() throws Exception {
        int databaseSizeBeforeUpdate = storageRepository.findAll().size();
        storage.setId(count.incrementAndGet());

        // Create the Storage
        StorageDTO storageDTO = storageMapper.toDto(storage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Storage in the database
        List<Storage> storageList = storageRepository.findAll();
        assertThat(storageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStorage() throws Exception {
        int databaseSizeBeforeUpdate = storageRepository.findAll().size();
        storage.setId(count.incrementAndGet());

        // Create the Storage
        StorageDTO storageDTO = storageMapper.toDto(storage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Storage in the database
        List<Storage> storageList = storageRepository.findAll();
        assertThat(storageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStorage() throws Exception {
        // Initialize the database
        storageRepository.saveAndFlush(storage);

        int databaseSizeBeforeDelete = storageRepository.findAll().size();

        // Delete the storage
        restStorageMockMvc
            .perform(delete(ENTITY_API_URL_ID, storage.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Storage> storageList = storageRepository.findAll();
        assertThat(storageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
