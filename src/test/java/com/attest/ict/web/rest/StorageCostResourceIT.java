package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.StorageCost;
import com.attest.ict.repository.StorageCostRepository;
import com.attest.ict.service.criteria.StorageCostCriteria;
import com.attest.ict.service.dto.StorageCostDTO;
import com.attest.ict.service.mapper.StorageCostMapper;
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
 * Integration tests for the {@link StorageCostResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StorageCostResourceIT {

    private static final Long DEFAULT_BUS_NUM = 1L;
    private static final Long UPDATED_BUS_NUM = 2L;
    private static final Long SMALLER_BUS_NUM = 1L - 1L;

    private static final Double DEFAULT_COST_A = 1D;
    private static final Double UPDATED_COST_A = 2D;
    private static final Double SMALLER_COST_A = 1D - 1D;

    private static final Double DEFAULT_COST_B = 1D;
    private static final Double UPDATED_COST_B = 2D;
    private static final Double SMALLER_COST_B = 1D - 1D;

    private static final Double DEFAULT_COST_C = 1D;
    private static final Double UPDATED_COST_C = 2D;
    private static final Double SMALLER_COST_C = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/storage-costs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StorageCostRepository storageCostRepository;

    @Autowired
    private StorageCostMapper storageCostMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStorageCostMockMvc;

    private StorageCost storageCost;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StorageCost createEntity(EntityManager em) {
        StorageCost storageCost = new StorageCost()
            .busNum(DEFAULT_BUS_NUM)
            .costA(DEFAULT_COST_A)
            .costB(DEFAULT_COST_B)
            .costC(DEFAULT_COST_C);
        return storageCost;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StorageCost createUpdatedEntity(EntityManager em) {
        StorageCost storageCost = new StorageCost()
            .busNum(UPDATED_BUS_NUM)
            .costA(UPDATED_COST_A)
            .costB(UPDATED_COST_B)
            .costC(UPDATED_COST_C);
        return storageCost;
    }

    @BeforeEach
    public void initTest() {
        storageCost = createEntity(em);
    }

    @Test
    @Transactional
    void createStorageCost() throws Exception {
        int databaseSizeBeforeCreate = storageCostRepository.findAll().size();
        // Create the StorageCost
        StorageCostDTO storageCostDTO = storageCostMapper.toDto(storageCost);
        restStorageCostMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storageCostDTO))
            )
            .andExpect(status().isCreated());

        // Validate the StorageCost in the database
        List<StorageCost> storageCostList = storageCostRepository.findAll();
        assertThat(storageCostList).hasSize(databaseSizeBeforeCreate + 1);
        StorageCost testStorageCost = storageCostList.get(storageCostList.size() - 1);
        assertThat(testStorageCost.getBusNum()).isEqualTo(DEFAULT_BUS_NUM);
        assertThat(testStorageCost.getCostA()).isEqualTo(DEFAULT_COST_A);
        assertThat(testStorageCost.getCostB()).isEqualTo(DEFAULT_COST_B);
        assertThat(testStorageCost.getCostC()).isEqualTo(DEFAULT_COST_C);
    }

    @Test
    @Transactional
    void createStorageCostWithExistingId() throws Exception {
        // Create the StorageCost with an existing ID
        storageCost.setId(1L);
        StorageCostDTO storageCostDTO = storageCostMapper.toDto(storageCost);

        int databaseSizeBeforeCreate = storageCostRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStorageCostMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storageCostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageCost in the database
        List<StorageCost> storageCostList = storageCostRepository.findAll();
        assertThat(storageCostList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStorageCosts() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList
        restStorageCostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storageCost.getId().intValue())))
            .andExpect(jsonPath("$.[*].busNum").value(hasItem(DEFAULT_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].costA").value(hasItem(DEFAULT_COST_A.doubleValue())))
            .andExpect(jsonPath("$.[*].costB").value(hasItem(DEFAULT_COST_B.doubleValue())))
            .andExpect(jsonPath("$.[*].costC").value(hasItem(DEFAULT_COST_C.doubleValue())));
    }

    @Test
    @Transactional
    void getStorageCost() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get the storageCost
        restStorageCostMockMvc
            .perform(get(ENTITY_API_URL_ID, storageCost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(storageCost.getId().intValue()))
            .andExpect(jsonPath("$.busNum").value(DEFAULT_BUS_NUM.intValue()))
            .andExpect(jsonPath("$.costA").value(DEFAULT_COST_A.doubleValue()))
            .andExpect(jsonPath("$.costB").value(DEFAULT_COST_B.doubleValue()))
            .andExpect(jsonPath("$.costC").value(DEFAULT_COST_C.doubleValue()));
    }

    @Test
    @Transactional
    void getStorageCostsByIdFiltering() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        Long id = storageCost.getId();

        defaultStorageCostShouldBeFound("id.equals=" + id);
        defaultStorageCostShouldNotBeFound("id.notEquals=" + id);

        defaultStorageCostShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStorageCostShouldNotBeFound("id.greaterThan=" + id);

        defaultStorageCostShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStorageCostShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStorageCostsByBusNumIsEqualToSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where busNum equals to DEFAULT_BUS_NUM
        defaultStorageCostShouldBeFound("busNum.equals=" + DEFAULT_BUS_NUM);

        // Get all the storageCostList where busNum equals to UPDATED_BUS_NUM
        defaultStorageCostShouldNotBeFound("busNum.equals=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllStorageCostsByBusNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where busNum not equals to DEFAULT_BUS_NUM
        defaultStorageCostShouldNotBeFound("busNum.notEquals=" + DEFAULT_BUS_NUM);

        // Get all the storageCostList where busNum not equals to UPDATED_BUS_NUM
        defaultStorageCostShouldBeFound("busNum.notEquals=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllStorageCostsByBusNumIsInShouldWork() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where busNum in DEFAULT_BUS_NUM or UPDATED_BUS_NUM
        defaultStorageCostShouldBeFound("busNum.in=" + DEFAULT_BUS_NUM + "," + UPDATED_BUS_NUM);

        // Get all the storageCostList where busNum equals to UPDATED_BUS_NUM
        defaultStorageCostShouldNotBeFound("busNum.in=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllStorageCostsByBusNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where busNum is not null
        defaultStorageCostShouldBeFound("busNum.specified=true");

        // Get all the storageCostList where busNum is null
        defaultStorageCostShouldNotBeFound("busNum.specified=false");
    }

    @Test
    @Transactional
    void getAllStorageCostsByBusNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where busNum is greater than or equal to DEFAULT_BUS_NUM
        defaultStorageCostShouldBeFound("busNum.greaterThanOrEqual=" + DEFAULT_BUS_NUM);

        // Get all the storageCostList where busNum is greater than or equal to UPDATED_BUS_NUM
        defaultStorageCostShouldNotBeFound("busNum.greaterThanOrEqual=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllStorageCostsByBusNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where busNum is less than or equal to DEFAULT_BUS_NUM
        defaultStorageCostShouldBeFound("busNum.lessThanOrEqual=" + DEFAULT_BUS_NUM);

        // Get all the storageCostList where busNum is less than or equal to SMALLER_BUS_NUM
        defaultStorageCostShouldNotBeFound("busNum.lessThanOrEqual=" + SMALLER_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllStorageCostsByBusNumIsLessThanSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where busNum is less than DEFAULT_BUS_NUM
        defaultStorageCostShouldNotBeFound("busNum.lessThan=" + DEFAULT_BUS_NUM);

        // Get all the storageCostList where busNum is less than UPDATED_BUS_NUM
        defaultStorageCostShouldBeFound("busNum.lessThan=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllStorageCostsByBusNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where busNum is greater than DEFAULT_BUS_NUM
        defaultStorageCostShouldNotBeFound("busNum.greaterThan=" + DEFAULT_BUS_NUM);

        // Get all the storageCostList where busNum is greater than SMALLER_BUS_NUM
        defaultStorageCostShouldBeFound("busNum.greaterThan=" + SMALLER_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostAIsEqualToSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costA equals to DEFAULT_COST_A
        defaultStorageCostShouldBeFound("costA.equals=" + DEFAULT_COST_A);

        // Get all the storageCostList where costA equals to UPDATED_COST_A
        defaultStorageCostShouldNotBeFound("costA.equals=" + UPDATED_COST_A);
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostAIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costA not equals to DEFAULT_COST_A
        defaultStorageCostShouldNotBeFound("costA.notEquals=" + DEFAULT_COST_A);

        // Get all the storageCostList where costA not equals to UPDATED_COST_A
        defaultStorageCostShouldBeFound("costA.notEquals=" + UPDATED_COST_A);
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostAIsInShouldWork() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costA in DEFAULT_COST_A or UPDATED_COST_A
        defaultStorageCostShouldBeFound("costA.in=" + DEFAULT_COST_A + "," + UPDATED_COST_A);

        // Get all the storageCostList where costA equals to UPDATED_COST_A
        defaultStorageCostShouldNotBeFound("costA.in=" + UPDATED_COST_A);
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostAIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costA is not null
        defaultStorageCostShouldBeFound("costA.specified=true");

        // Get all the storageCostList where costA is null
        defaultStorageCostShouldNotBeFound("costA.specified=false");
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostAIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costA is greater than or equal to DEFAULT_COST_A
        defaultStorageCostShouldBeFound("costA.greaterThanOrEqual=" + DEFAULT_COST_A);

        // Get all the storageCostList where costA is greater than or equal to UPDATED_COST_A
        defaultStorageCostShouldNotBeFound("costA.greaterThanOrEqual=" + UPDATED_COST_A);
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostAIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costA is less than or equal to DEFAULT_COST_A
        defaultStorageCostShouldBeFound("costA.lessThanOrEqual=" + DEFAULT_COST_A);

        // Get all the storageCostList where costA is less than or equal to SMALLER_COST_A
        defaultStorageCostShouldNotBeFound("costA.lessThanOrEqual=" + SMALLER_COST_A);
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostAIsLessThanSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costA is less than DEFAULT_COST_A
        defaultStorageCostShouldNotBeFound("costA.lessThan=" + DEFAULT_COST_A);

        // Get all the storageCostList where costA is less than UPDATED_COST_A
        defaultStorageCostShouldBeFound("costA.lessThan=" + UPDATED_COST_A);
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostAIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costA is greater than DEFAULT_COST_A
        defaultStorageCostShouldNotBeFound("costA.greaterThan=" + DEFAULT_COST_A);

        // Get all the storageCostList where costA is greater than SMALLER_COST_A
        defaultStorageCostShouldBeFound("costA.greaterThan=" + SMALLER_COST_A);
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostBIsEqualToSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costB equals to DEFAULT_COST_B
        defaultStorageCostShouldBeFound("costB.equals=" + DEFAULT_COST_B);

        // Get all the storageCostList where costB equals to UPDATED_COST_B
        defaultStorageCostShouldNotBeFound("costB.equals=" + UPDATED_COST_B);
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostBIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costB not equals to DEFAULT_COST_B
        defaultStorageCostShouldNotBeFound("costB.notEquals=" + DEFAULT_COST_B);

        // Get all the storageCostList where costB not equals to UPDATED_COST_B
        defaultStorageCostShouldBeFound("costB.notEquals=" + UPDATED_COST_B);
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostBIsInShouldWork() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costB in DEFAULT_COST_B or UPDATED_COST_B
        defaultStorageCostShouldBeFound("costB.in=" + DEFAULT_COST_B + "," + UPDATED_COST_B);

        // Get all the storageCostList where costB equals to UPDATED_COST_B
        defaultStorageCostShouldNotBeFound("costB.in=" + UPDATED_COST_B);
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostBIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costB is not null
        defaultStorageCostShouldBeFound("costB.specified=true");

        // Get all the storageCostList where costB is null
        defaultStorageCostShouldNotBeFound("costB.specified=false");
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostBIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costB is greater than or equal to DEFAULT_COST_B
        defaultStorageCostShouldBeFound("costB.greaterThanOrEqual=" + DEFAULT_COST_B);

        // Get all the storageCostList where costB is greater than or equal to UPDATED_COST_B
        defaultStorageCostShouldNotBeFound("costB.greaterThanOrEqual=" + UPDATED_COST_B);
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostBIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costB is less than or equal to DEFAULT_COST_B
        defaultStorageCostShouldBeFound("costB.lessThanOrEqual=" + DEFAULT_COST_B);

        // Get all the storageCostList where costB is less than or equal to SMALLER_COST_B
        defaultStorageCostShouldNotBeFound("costB.lessThanOrEqual=" + SMALLER_COST_B);
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostBIsLessThanSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costB is less than DEFAULT_COST_B
        defaultStorageCostShouldNotBeFound("costB.lessThan=" + DEFAULT_COST_B);

        // Get all the storageCostList where costB is less than UPDATED_COST_B
        defaultStorageCostShouldBeFound("costB.lessThan=" + UPDATED_COST_B);
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostBIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costB is greater than DEFAULT_COST_B
        defaultStorageCostShouldNotBeFound("costB.greaterThan=" + DEFAULT_COST_B);

        // Get all the storageCostList where costB is greater than SMALLER_COST_B
        defaultStorageCostShouldBeFound("costB.greaterThan=" + SMALLER_COST_B);
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostCIsEqualToSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costC equals to DEFAULT_COST_C
        defaultStorageCostShouldBeFound("costC.equals=" + DEFAULT_COST_C);

        // Get all the storageCostList where costC equals to UPDATED_COST_C
        defaultStorageCostShouldNotBeFound("costC.equals=" + UPDATED_COST_C);
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostCIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costC not equals to DEFAULT_COST_C
        defaultStorageCostShouldNotBeFound("costC.notEquals=" + DEFAULT_COST_C);

        // Get all the storageCostList where costC not equals to UPDATED_COST_C
        defaultStorageCostShouldBeFound("costC.notEquals=" + UPDATED_COST_C);
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostCIsInShouldWork() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costC in DEFAULT_COST_C or UPDATED_COST_C
        defaultStorageCostShouldBeFound("costC.in=" + DEFAULT_COST_C + "," + UPDATED_COST_C);

        // Get all the storageCostList where costC equals to UPDATED_COST_C
        defaultStorageCostShouldNotBeFound("costC.in=" + UPDATED_COST_C);
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostCIsNullOrNotNull() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costC is not null
        defaultStorageCostShouldBeFound("costC.specified=true");

        // Get all the storageCostList where costC is null
        defaultStorageCostShouldNotBeFound("costC.specified=false");
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostCIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costC is greater than or equal to DEFAULT_COST_C
        defaultStorageCostShouldBeFound("costC.greaterThanOrEqual=" + DEFAULT_COST_C);

        // Get all the storageCostList where costC is greater than or equal to UPDATED_COST_C
        defaultStorageCostShouldNotBeFound("costC.greaterThanOrEqual=" + UPDATED_COST_C);
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostCIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costC is less than or equal to DEFAULT_COST_C
        defaultStorageCostShouldBeFound("costC.lessThanOrEqual=" + DEFAULT_COST_C);

        // Get all the storageCostList where costC is less than or equal to SMALLER_COST_C
        defaultStorageCostShouldNotBeFound("costC.lessThanOrEqual=" + SMALLER_COST_C);
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostCIsLessThanSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costC is less than DEFAULT_COST_C
        defaultStorageCostShouldNotBeFound("costC.lessThan=" + DEFAULT_COST_C);

        // Get all the storageCostList where costC is less than UPDATED_COST_C
        defaultStorageCostShouldBeFound("costC.lessThan=" + UPDATED_COST_C);
    }

    @Test
    @Transactional
    void getAllStorageCostsByCostCIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        // Get all the storageCostList where costC is greater than DEFAULT_COST_C
        defaultStorageCostShouldNotBeFound("costC.greaterThan=" + DEFAULT_COST_C);

        // Get all the storageCostList where costC is greater than SMALLER_COST_C
        defaultStorageCostShouldBeFound("costC.greaterThan=" + SMALLER_COST_C);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStorageCostShouldBeFound(String filter) throws Exception {
        restStorageCostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storageCost.getId().intValue())))
            .andExpect(jsonPath("$.[*].busNum").value(hasItem(DEFAULT_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].costA").value(hasItem(DEFAULT_COST_A.doubleValue())))
            .andExpect(jsonPath("$.[*].costB").value(hasItem(DEFAULT_COST_B.doubleValue())))
            .andExpect(jsonPath("$.[*].costC").value(hasItem(DEFAULT_COST_C.doubleValue())));

        // Check, that the count call also returns 1
        restStorageCostMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStorageCostShouldNotBeFound(String filter) throws Exception {
        restStorageCostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStorageCostMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStorageCost() throws Exception {
        // Get the storageCost
        restStorageCostMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStorageCost() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        int databaseSizeBeforeUpdate = storageCostRepository.findAll().size();

        // Update the storageCost
        StorageCost updatedStorageCost = storageCostRepository.findById(storageCost.getId()).get();
        // Disconnect from session so that the updates on updatedStorageCost are not directly saved in db
        em.detach(updatedStorageCost);
        updatedStorageCost.busNum(UPDATED_BUS_NUM).costA(UPDATED_COST_A).costB(UPDATED_COST_B).costC(UPDATED_COST_C);
        StorageCostDTO storageCostDTO = storageCostMapper.toDto(updatedStorageCost);

        restStorageCostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storageCostDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storageCostDTO))
            )
            .andExpect(status().isOk());

        // Validate the StorageCost in the database
        List<StorageCost> storageCostList = storageCostRepository.findAll();
        assertThat(storageCostList).hasSize(databaseSizeBeforeUpdate);
        StorageCost testStorageCost = storageCostList.get(storageCostList.size() - 1);
        assertThat(testStorageCost.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testStorageCost.getCostA()).isEqualTo(UPDATED_COST_A);
        assertThat(testStorageCost.getCostB()).isEqualTo(UPDATED_COST_B);
        assertThat(testStorageCost.getCostC()).isEqualTo(UPDATED_COST_C);
    }

    @Test
    @Transactional
    void putNonExistingStorageCost() throws Exception {
        int databaseSizeBeforeUpdate = storageCostRepository.findAll().size();
        storageCost.setId(count.incrementAndGet());

        // Create the StorageCost
        StorageCostDTO storageCostDTO = storageCostMapper.toDto(storageCost);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStorageCostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storageCostDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storageCostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageCost in the database
        List<StorageCost> storageCostList = storageCostRepository.findAll();
        assertThat(storageCostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStorageCost() throws Exception {
        int databaseSizeBeforeUpdate = storageCostRepository.findAll().size();
        storageCost.setId(count.incrementAndGet());

        // Create the StorageCost
        StorageCostDTO storageCostDTO = storageCostMapper.toDto(storageCost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageCostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storageCostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageCost in the database
        List<StorageCost> storageCostList = storageCostRepository.findAll();
        assertThat(storageCostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStorageCost() throws Exception {
        int databaseSizeBeforeUpdate = storageCostRepository.findAll().size();
        storageCost.setId(count.incrementAndGet());

        // Create the StorageCost
        StorageCostDTO storageCostDTO = storageCostMapper.toDto(storageCost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageCostMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storageCostDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StorageCost in the database
        List<StorageCost> storageCostList = storageCostRepository.findAll();
        assertThat(storageCostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStorageCostWithPatch() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        int databaseSizeBeforeUpdate = storageCostRepository.findAll().size();

        // Update the storageCost using partial update
        StorageCost partialUpdatedStorageCost = new StorageCost();
        partialUpdatedStorageCost.setId(storageCost.getId());

        partialUpdatedStorageCost.busNum(UPDATED_BUS_NUM).costA(UPDATED_COST_A).costB(UPDATED_COST_B);

        restStorageCostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStorageCost.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStorageCost))
            )
            .andExpect(status().isOk());

        // Validate the StorageCost in the database
        List<StorageCost> storageCostList = storageCostRepository.findAll();
        assertThat(storageCostList).hasSize(databaseSizeBeforeUpdate);
        StorageCost testStorageCost = storageCostList.get(storageCostList.size() - 1);
        assertThat(testStorageCost.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testStorageCost.getCostA()).isEqualTo(UPDATED_COST_A);
        assertThat(testStorageCost.getCostB()).isEqualTo(UPDATED_COST_B);
        assertThat(testStorageCost.getCostC()).isEqualTo(DEFAULT_COST_C);
    }

    @Test
    @Transactional
    void fullUpdateStorageCostWithPatch() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        int databaseSizeBeforeUpdate = storageCostRepository.findAll().size();

        // Update the storageCost using partial update
        StorageCost partialUpdatedStorageCost = new StorageCost();
        partialUpdatedStorageCost.setId(storageCost.getId());

        partialUpdatedStorageCost.busNum(UPDATED_BUS_NUM).costA(UPDATED_COST_A).costB(UPDATED_COST_B).costC(UPDATED_COST_C);

        restStorageCostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStorageCost.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStorageCost))
            )
            .andExpect(status().isOk());

        // Validate the StorageCost in the database
        List<StorageCost> storageCostList = storageCostRepository.findAll();
        assertThat(storageCostList).hasSize(databaseSizeBeforeUpdate);
        StorageCost testStorageCost = storageCostList.get(storageCostList.size() - 1);
        assertThat(testStorageCost.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testStorageCost.getCostA()).isEqualTo(UPDATED_COST_A);
        assertThat(testStorageCost.getCostB()).isEqualTo(UPDATED_COST_B);
        assertThat(testStorageCost.getCostC()).isEqualTo(UPDATED_COST_C);
    }

    @Test
    @Transactional
    void patchNonExistingStorageCost() throws Exception {
        int databaseSizeBeforeUpdate = storageCostRepository.findAll().size();
        storageCost.setId(count.incrementAndGet());

        // Create the StorageCost
        StorageCostDTO storageCostDTO = storageCostMapper.toDto(storageCost);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStorageCostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, storageCostDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storageCostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageCost in the database
        List<StorageCost> storageCostList = storageCostRepository.findAll();
        assertThat(storageCostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStorageCost() throws Exception {
        int databaseSizeBeforeUpdate = storageCostRepository.findAll().size();
        storageCost.setId(count.incrementAndGet());

        // Create the StorageCost
        StorageCostDTO storageCostDTO = storageCostMapper.toDto(storageCost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageCostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storageCostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageCost in the database
        List<StorageCost> storageCostList = storageCostRepository.findAll();
        assertThat(storageCostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStorageCost() throws Exception {
        int databaseSizeBeforeUpdate = storageCostRepository.findAll().size();
        storageCost.setId(count.incrementAndGet());

        // Create the StorageCost
        StorageCostDTO storageCostDTO = storageCostMapper.toDto(storageCost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageCostMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storageCostDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StorageCost in the database
        List<StorageCost> storageCostList = storageCostRepository.findAll();
        assertThat(storageCostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStorageCost() throws Exception {
        // Initialize the database
        storageCostRepository.saveAndFlush(storageCost);

        int databaseSizeBeforeDelete = storageCostRepository.findAll().size();

        // Delete the storageCost
        restStorageCostMockMvc
            .perform(delete(ENTITY_API_URL_ID, storageCost.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StorageCost> storageCostList = storageCostRepository.findAll();
        assertThat(storageCostList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
