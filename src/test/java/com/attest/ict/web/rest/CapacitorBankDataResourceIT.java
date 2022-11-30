package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.CapacitorBankData;
import com.attest.ict.domain.Network;
import com.attest.ict.repository.CapacitorBankDataRepository;
import com.attest.ict.service.criteria.CapacitorBankDataCriteria;
import com.attest.ict.service.dto.CapacitorBankDataDTO;
import com.attest.ict.service.mapper.CapacitorBankDataMapper;
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
 * Integration tests for the {@link CapacitorBankDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CapacitorBankDataResourceIT {

    private static final Long DEFAULT_BUS_NUM = 1L;
    private static final Long UPDATED_BUS_NUM = 2L;
    private static final Long SMALLER_BUS_NUM = 1L - 1L;

    private static final String DEFAULT_NODE_ID = "AAAAAAAAAA";
    private static final String UPDATED_NODE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_ID = "AAAAAAAAAA";
    private static final String UPDATED_BANK_ID = "BBBBBBBBBB";

    private static final Double DEFAULT_QNOM = 1D;
    private static final Double UPDATED_QNOM = 2D;
    private static final Double SMALLER_QNOM = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/capacitor-bank-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CapacitorBankDataRepository capacitorBankDataRepository;

    @Autowired
    private CapacitorBankDataMapper capacitorBankDataMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCapacitorBankDataMockMvc;

    private CapacitorBankData capacitorBankData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CapacitorBankData createEntity(EntityManager em) {
        CapacitorBankData capacitorBankData = new CapacitorBankData()
            .busNum(DEFAULT_BUS_NUM)
            .nodeId(DEFAULT_NODE_ID)
            .bankId(DEFAULT_BANK_ID)
            .qnom(DEFAULT_QNOM);
        return capacitorBankData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CapacitorBankData createUpdatedEntity(EntityManager em) {
        CapacitorBankData capacitorBankData = new CapacitorBankData()
            .busNum(UPDATED_BUS_NUM)
            .nodeId(UPDATED_NODE_ID)
            .bankId(UPDATED_BANK_ID)
            .qnom(UPDATED_QNOM);
        return capacitorBankData;
    }

    @BeforeEach
    public void initTest() {
        capacitorBankData = createEntity(em);
    }

    @Test
    @Transactional
    void createCapacitorBankData() throws Exception {
        int databaseSizeBeforeCreate = capacitorBankDataRepository.findAll().size();
        // Create the CapacitorBankData
        CapacitorBankDataDTO capacitorBankDataDTO = capacitorBankDataMapper.toDto(capacitorBankData);
        restCapacitorBankDataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capacitorBankDataDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CapacitorBankData in the database
        List<CapacitorBankData> capacitorBankDataList = capacitorBankDataRepository.findAll();
        assertThat(capacitorBankDataList).hasSize(databaseSizeBeforeCreate + 1);
        CapacitorBankData testCapacitorBankData = capacitorBankDataList.get(capacitorBankDataList.size() - 1);
        assertThat(testCapacitorBankData.getBusNum()).isEqualTo(DEFAULT_BUS_NUM);
        assertThat(testCapacitorBankData.getNodeId()).isEqualTo(DEFAULT_NODE_ID);
        assertThat(testCapacitorBankData.getBankId()).isEqualTo(DEFAULT_BANK_ID);
        assertThat(testCapacitorBankData.getQnom()).isEqualTo(DEFAULT_QNOM);
    }

    @Test
    @Transactional
    void createCapacitorBankDataWithExistingId() throws Exception {
        // Create the CapacitorBankData with an existing ID
        capacitorBankData.setId(1L);
        CapacitorBankDataDTO capacitorBankDataDTO = capacitorBankDataMapper.toDto(capacitorBankData);

        int databaseSizeBeforeCreate = capacitorBankDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCapacitorBankDataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capacitorBankDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CapacitorBankData in the database
        List<CapacitorBankData> capacitorBankDataList = capacitorBankDataRepository.findAll();
        assertThat(capacitorBankDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCapacitorBankData() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList
        restCapacitorBankDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(capacitorBankData.getId().intValue())))
            .andExpect(jsonPath("$.[*].busNum").value(hasItem(DEFAULT_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].nodeId").value(hasItem(DEFAULT_NODE_ID)))
            .andExpect(jsonPath("$.[*].bankId").value(hasItem(DEFAULT_BANK_ID)))
            .andExpect(jsonPath("$.[*].qnom").value(hasItem(DEFAULT_QNOM.doubleValue())));
    }

    @Test
    @Transactional
    void getCapacitorBankData() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get the capacitorBankData
        restCapacitorBankDataMockMvc
            .perform(get(ENTITY_API_URL_ID, capacitorBankData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(capacitorBankData.getId().intValue()))
            .andExpect(jsonPath("$.busNum").value(DEFAULT_BUS_NUM.intValue()))
            .andExpect(jsonPath("$.nodeId").value(DEFAULT_NODE_ID))
            .andExpect(jsonPath("$.bankId").value(DEFAULT_BANK_ID))
            .andExpect(jsonPath("$.qnom").value(DEFAULT_QNOM.doubleValue()));
    }

    @Test
    @Transactional
    void getCapacitorBankDataByIdFiltering() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        Long id = capacitorBankData.getId();

        defaultCapacitorBankDataShouldBeFound("id.equals=" + id);
        defaultCapacitorBankDataShouldNotBeFound("id.notEquals=" + id);

        defaultCapacitorBankDataShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCapacitorBankDataShouldNotBeFound("id.greaterThan=" + id);

        defaultCapacitorBankDataShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCapacitorBankDataShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByBusNumIsEqualToSomething() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where busNum equals to DEFAULT_BUS_NUM
        defaultCapacitorBankDataShouldBeFound("busNum.equals=" + DEFAULT_BUS_NUM);

        // Get all the capacitorBankDataList where busNum equals to UPDATED_BUS_NUM
        defaultCapacitorBankDataShouldNotBeFound("busNum.equals=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByBusNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where busNum not equals to DEFAULT_BUS_NUM
        defaultCapacitorBankDataShouldNotBeFound("busNum.notEquals=" + DEFAULT_BUS_NUM);

        // Get all the capacitorBankDataList where busNum not equals to UPDATED_BUS_NUM
        defaultCapacitorBankDataShouldBeFound("busNum.notEquals=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByBusNumIsInShouldWork() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where busNum in DEFAULT_BUS_NUM or UPDATED_BUS_NUM
        defaultCapacitorBankDataShouldBeFound("busNum.in=" + DEFAULT_BUS_NUM + "," + UPDATED_BUS_NUM);

        // Get all the capacitorBankDataList where busNum equals to UPDATED_BUS_NUM
        defaultCapacitorBankDataShouldNotBeFound("busNum.in=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByBusNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where busNum is not null
        defaultCapacitorBankDataShouldBeFound("busNum.specified=true");

        // Get all the capacitorBankDataList where busNum is null
        defaultCapacitorBankDataShouldNotBeFound("busNum.specified=false");
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByBusNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where busNum is greater than or equal to DEFAULT_BUS_NUM
        defaultCapacitorBankDataShouldBeFound("busNum.greaterThanOrEqual=" + DEFAULT_BUS_NUM);

        // Get all the capacitorBankDataList where busNum is greater than or equal to UPDATED_BUS_NUM
        defaultCapacitorBankDataShouldNotBeFound("busNum.greaterThanOrEqual=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByBusNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where busNum is less than or equal to DEFAULT_BUS_NUM
        defaultCapacitorBankDataShouldBeFound("busNum.lessThanOrEqual=" + DEFAULT_BUS_NUM);

        // Get all the capacitorBankDataList where busNum is less than or equal to SMALLER_BUS_NUM
        defaultCapacitorBankDataShouldNotBeFound("busNum.lessThanOrEqual=" + SMALLER_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByBusNumIsLessThanSomething() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where busNum is less than DEFAULT_BUS_NUM
        defaultCapacitorBankDataShouldNotBeFound("busNum.lessThan=" + DEFAULT_BUS_NUM);

        // Get all the capacitorBankDataList where busNum is less than UPDATED_BUS_NUM
        defaultCapacitorBankDataShouldBeFound("busNum.lessThan=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByBusNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where busNum is greater than DEFAULT_BUS_NUM
        defaultCapacitorBankDataShouldNotBeFound("busNum.greaterThan=" + DEFAULT_BUS_NUM);

        // Get all the capacitorBankDataList where busNum is greater than SMALLER_BUS_NUM
        defaultCapacitorBankDataShouldBeFound("busNum.greaterThan=" + SMALLER_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByNodeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where nodeId equals to DEFAULT_NODE_ID
        defaultCapacitorBankDataShouldBeFound("nodeId.equals=" + DEFAULT_NODE_ID);

        // Get all the capacitorBankDataList where nodeId equals to UPDATED_NODE_ID
        defaultCapacitorBankDataShouldNotBeFound("nodeId.equals=" + UPDATED_NODE_ID);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByNodeIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where nodeId not equals to DEFAULT_NODE_ID
        defaultCapacitorBankDataShouldNotBeFound("nodeId.notEquals=" + DEFAULT_NODE_ID);

        // Get all the capacitorBankDataList where nodeId not equals to UPDATED_NODE_ID
        defaultCapacitorBankDataShouldBeFound("nodeId.notEquals=" + UPDATED_NODE_ID);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByNodeIdIsInShouldWork() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where nodeId in DEFAULT_NODE_ID or UPDATED_NODE_ID
        defaultCapacitorBankDataShouldBeFound("nodeId.in=" + DEFAULT_NODE_ID + "," + UPDATED_NODE_ID);

        // Get all the capacitorBankDataList where nodeId equals to UPDATED_NODE_ID
        defaultCapacitorBankDataShouldNotBeFound("nodeId.in=" + UPDATED_NODE_ID);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByNodeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where nodeId is not null
        defaultCapacitorBankDataShouldBeFound("nodeId.specified=true");

        // Get all the capacitorBankDataList where nodeId is null
        defaultCapacitorBankDataShouldNotBeFound("nodeId.specified=false");
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByNodeIdContainsSomething() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where nodeId contains DEFAULT_NODE_ID
        defaultCapacitorBankDataShouldBeFound("nodeId.contains=" + DEFAULT_NODE_ID);

        // Get all the capacitorBankDataList where nodeId contains UPDATED_NODE_ID
        defaultCapacitorBankDataShouldNotBeFound("nodeId.contains=" + UPDATED_NODE_ID);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByNodeIdNotContainsSomething() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where nodeId does not contain DEFAULT_NODE_ID
        defaultCapacitorBankDataShouldNotBeFound("nodeId.doesNotContain=" + DEFAULT_NODE_ID);

        // Get all the capacitorBankDataList where nodeId does not contain UPDATED_NODE_ID
        defaultCapacitorBankDataShouldBeFound("nodeId.doesNotContain=" + UPDATED_NODE_ID);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByBankIdIsEqualToSomething() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where bankId equals to DEFAULT_BANK_ID
        defaultCapacitorBankDataShouldBeFound("bankId.equals=" + DEFAULT_BANK_ID);

        // Get all the capacitorBankDataList where bankId equals to UPDATED_BANK_ID
        defaultCapacitorBankDataShouldNotBeFound("bankId.equals=" + UPDATED_BANK_ID);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByBankIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where bankId not equals to DEFAULT_BANK_ID
        defaultCapacitorBankDataShouldNotBeFound("bankId.notEquals=" + DEFAULT_BANK_ID);

        // Get all the capacitorBankDataList where bankId not equals to UPDATED_BANK_ID
        defaultCapacitorBankDataShouldBeFound("bankId.notEquals=" + UPDATED_BANK_ID);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByBankIdIsInShouldWork() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where bankId in DEFAULT_BANK_ID or UPDATED_BANK_ID
        defaultCapacitorBankDataShouldBeFound("bankId.in=" + DEFAULT_BANK_ID + "," + UPDATED_BANK_ID);

        // Get all the capacitorBankDataList where bankId equals to UPDATED_BANK_ID
        defaultCapacitorBankDataShouldNotBeFound("bankId.in=" + UPDATED_BANK_ID);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByBankIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where bankId is not null
        defaultCapacitorBankDataShouldBeFound("bankId.specified=true");

        // Get all the capacitorBankDataList where bankId is null
        defaultCapacitorBankDataShouldNotBeFound("bankId.specified=false");
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByBankIdContainsSomething() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where bankId contains DEFAULT_BANK_ID
        defaultCapacitorBankDataShouldBeFound("bankId.contains=" + DEFAULT_BANK_ID);

        // Get all the capacitorBankDataList where bankId contains UPDATED_BANK_ID
        defaultCapacitorBankDataShouldNotBeFound("bankId.contains=" + UPDATED_BANK_ID);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByBankIdNotContainsSomething() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where bankId does not contain DEFAULT_BANK_ID
        defaultCapacitorBankDataShouldNotBeFound("bankId.doesNotContain=" + DEFAULT_BANK_ID);

        // Get all the capacitorBankDataList where bankId does not contain UPDATED_BANK_ID
        defaultCapacitorBankDataShouldBeFound("bankId.doesNotContain=" + UPDATED_BANK_ID);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByQnomIsEqualToSomething() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where qnom equals to DEFAULT_QNOM
        defaultCapacitorBankDataShouldBeFound("qnom.equals=" + DEFAULT_QNOM);

        // Get all the capacitorBankDataList where qnom equals to UPDATED_QNOM
        defaultCapacitorBankDataShouldNotBeFound("qnom.equals=" + UPDATED_QNOM);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByQnomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where qnom not equals to DEFAULT_QNOM
        defaultCapacitorBankDataShouldNotBeFound("qnom.notEquals=" + DEFAULT_QNOM);

        // Get all the capacitorBankDataList where qnom not equals to UPDATED_QNOM
        defaultCapacitorBankDataShouldBeFound("qnom.notEquals=" + UPDATED_QNOM);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByQnomIsInShouldWork() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where qnom in DEFAULT_QNOM or UPDATED_QNOM
        defaultCapacitorBankDataShouldBeFound("qnom.in=" + DEFAULT_QNOM + "," + UPDATED_QNOM);

        // Get all the capacitorBankDataList where qnom equals to UPDATED_QNOM
        defaultCapacitorBankDataShouldNotBeFound("qnom.in=" + UPDATED_QNOM);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByQnomIsNullOrNotNull() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where qnom is not null
        defaultCapacitorBankDataShouldBeFound("qnom.specified=true");

        // Get all the capacitorBankDataList where qnom is null
        defaultCapacitorBankDataShouldNotBeFound("qnom.specified=false");
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByQnomIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where qnom is greater than or equal to DEFAULT_QNOM
        defaultCapacitorBankDataShouldBeFound("qnom.greaterThanOrEqual=" + DEFAULT_QNOM);

        // Get all the capacitorBankDataList where qnom is greater than or equal to UPDATED_QNOM
        defaultCapacitorBankDataShouldNotBeFound("qnom.greaterThanOrEqual=" + UPDATED_QNOM);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByQnomIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where qnom is less than or equal to DEFAULT_QNOM
        defaultCapacitorBankDataShouldBeFound("qnom.lessThanOrEqual=" + DEFAULT_QNOM);

        // Get all the capacitorBankDataList where qnom is less than or equal to SMALLER_QNOM
        defaultCapacitorBankDataShouldNotBeFound("qnom.lessThanOrEqual=" + SMALLER_QNOM);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByQnomIsLessThanSomething() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where qnom is less than DEFAULT_QNOM
        defaultCapacitorBankDataShouldNotBeFound("qnom.lessThan=" + DEFAULT_QNOM);

        // Get all the capacitorBankDataList where qnom is less than UPDATED_QNOM
        defaultCapacitorBankDataShouldBeFound("qnom.lessThan=" + UPDATED_QNOM);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByQnomIsGreaterThanSomething() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        // Get all the capacitorBankDataList where qnom is greater than DEFAULT_QNOM
        defaultCapacitorBankDataShouldNotBeFound("qnom.greaterThan=" + DEFAULT_QNOM);

        // Get all the capacitorBankDataList where qnom is greater than SMALLER_QNOM
        defaultCapacitorBankDataShouldBeFound("qnom.greaterThan=" + SMALLER_QNOM);
    }

    @Test
    @Transactional
    void getAllCapacitorBankDataByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);
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
        capacitorBankData.setNetwork(network);
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);
        Long networkId = network.getId();

        // Get all the capacitorBankDataList where network equals to networkId
        defaultCapacitorBankDataShouldBeFound("networkId.equals=" + networkId);

        // Get all the capacitorBankDataList where network equals to (networkId + 1)
        defaultCapacitorBankDataShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCapacitorBankDataShouldBeFound(String filter) throws Exception {
        restCapacitorBankDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(capacitorBankData.getId().intValue())))
            .andExpect(jsonPath("$.[*].busNum").value(hasItem(DEFAULT_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].nodeId").value(hasItem(DEFAULT_NODE_ID)))
            .andExpect(jsonPath("$.[*].bankId").value(hasItem(DEFAULT_BANK_ID)))
            .andExpect(jsonPath("$.[*].qnom").value(hasItem(DEFAULT_QNOM.doubleValue())));

        // Check, that the count call also returns 1
        restCapacitorBankDataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCapacitorBankDataShouldNotBeFound(String filter) throws Exception {
        restCapacitorBankDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCapacitorBankDataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCapacitorBankData() throws Exception {
        // Get the capacitorBankData
        restCapacitorBankDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCapacitorBankData() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        int databaseSizeBeforeUpdate = capacitorBankDataRepository.findAll().size();

        // Update the capacitorBankData
        CapacitorBankData updatedCapacitorBankData = capacitorBankDataRepository.findById(capacitorBankData.getId()).get();
        // Disconnect from session so that the updates on updatedCapacitorBankData are not directly saved in db
        em.detach(updatedCapacitorBankData);
        updatedCapacitorBankData.busNum(UPDATED_BUS_NUM).nodeId(UPDATED_NODE_ID).bankId(UPDATED_BANK_ID).qnom(UPDATED_QNOM);
        CapacitorBankDataDTO capacitorBankDataDTO = capacitorBankDataMapper.toDto(updatedCapacitorBankData);

        restCapacitorBankDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, capacitorBankDataDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capacitorBankDataDTO))
            )
            .andExpect(status().isOk());

        // Validate the CapacitorBankData in the database
        List<CapacitorBankData> capacitorBankDataList = capacitorBankDataRepository.findAll();
        assertThat(capacitorBankDataList).hasSize(databaseSizeBeforeUpdate);
        CapacitorBankData testCapacitorBankData = capacitorBankDataList.get(capacitorBankDataList.size() - 1);
        assertThat(testCapacitorBankData.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testCapacitorBankData.getNodeId()).isEqualTo(UPDATED_NODE_ID);
        assertThat(testCapacitorBankData.getBankId()).isEqualTo(UPDATED_BANK_ID);
        assertThat(testCapacitorBankData.getQnom()).isEqualTo(UPDATED_QNOM);
    }

    @Test
    @Transactional
    void putNonExistingCapacitorBankData() throws Exception {
        int databaseSizeBeforeUpdate = capacitorBankDataRepository.findAll().size();
        capacitorBankData.setId(count.incrementAndGet());

        // Create the CapacitorBankData
        CapacitorBankDataDTO capacitorBankDataDTO = capacitorBankDataMapper.toDto(capacitorBankData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCapacitorBankDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, capacitorBankDataDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capacitorBankDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CapacitorBankData in the database
        List<CapacitorBankData> capacitorBankDataList = capacitorBankDataRepository.findAll();
        assertThat(capacitorBankDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCapacitorBankData() throws Exception {
        int databaseSizeBeforeUpdate = capacitorBankDataRepository.findAll().size();
        capacitorBankData.setId(count.incrementAndGet());

        // Create the CapacitorBankData
        CapacitorBankDataDTO capacitorBankDataDTO = capacitorBankDataMapper.toDto(capacitorBankData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapacitorBankDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capacitorBankDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CapacitorBankData in the database
        List<CapacitorBankData> capacitorBankDataList = capacitorBankDataRepository.findAll();
        assertThat(capacitorBankDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCapacitorBankData() throws Exception {
        int databaseSizeBeforeUpdate = capacitorBankDataRepository.findAll().size();
        capacitorBankData.setId(count.incrementAndGet());

        // Create the CapacitorBankData
        CapacitorBankDataDTO capacitorBankDataDTO = capacitorBankDataMapper.toDto(capacitorBankData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapacitorBankDataMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capacitorBankDataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CapacitorBankData in the database
        List<CapacitorBankData> capacitorBankDataList = capacitorBankDataRepository.findAll();
        assertThat(capacitorBankDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCapacitorBankDataWithPatch() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        int databaseSizeBeforeUpdate = capacitorBankDataRepository.findAll().size();

        // Update the capacitorBankData using partial update
        CapacitorBankData partialUpdatedCapacitorBankData = new CapacitorBankData();
        partialUpdatedCapacitorBankData.setId(capacitorBankData.getId());

        partialUpdatedCapacitorBankData.busNum(UPDATED_BUS_NUM).nodeId(UPDATED_NODE_ID).bankId(UPDATED_BANK_ID);

        restCapacitorBankDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCapacitorBankData.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCapacitorBankData))
            )
            .andExpect(status().isOk());

        // Validate the CapacitorBankData in the database
        List<CapacitorBankData> capacitorBankDataList = capacitorBankDataRepository.findAll();
        assertThat(capacitorBankDataList).hasSize(databaseSizeBeforeUpdate);
        CapacitorBankData testCapacitorBankData = capacitorBankDataList.get(capacitorBankDataList.size() - 1);
        assertThat(testCapacitorBankData.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testCapacitorBankData.getNodeId()).isEqualTo(UPDATED_NODE_ID);
        assertThat(testCapacitorBankData.getBankId()).isEqualTo(UPDATED_BANK_ID);
        assertThat(testCapacitorBankData.getQnom()).isEqualTo(DEFAULT_QNOM);
    }

    @Test
    @Transactional
    void fullUpdateCapacitorBankDataWithPatch() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        int databaseSizeBeforeUpdate = capacitorBankDataRepository.findAll().size();

        // Update the capacitorBankData using partial update
        CapacitorBankData partialUpdatedCapacitorBankData = new CapacitorBankData();
        partialUpdatedCapacitorBankData.setId(capacitorBankData.getId());

        partialUpdatedCapacitorBankData.busNum(UPDATED_BUS_NUM).nodeId(UPDATED_NODE_ID).bankId(UPDATED_BANK_ID).qnom(UPDATED_QNOM);

        restCapacitorBankDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCapacitorBankData.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCapacitorBankData))
            )
            .andExpect(status().isOk());

        // Validate the CapacitorBankData in the database
        List<CapacitorBankData> capacitorBankDataList = capacitorBankDataRepository.findAll();
        assertThat(capacitorBankDataList).hasSize(databaseSizeBeforeUpdate);
        CapacitorBankData testCapacitorBankData = capacitorBankDataList.get(capacitorBankDataList.size() - 1);
        assertThat(testCapacitorBankData.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testCapacitorBankData.getNodeId()).isEqualTo(UPDATED_NODE_ID);
        assertThat(testCapacitorBankData.getBankId()).isEqualTo(UPDATED_BANK_ID);
        assertThat(testCapacitorBankData.getQnom()).isEqualTo(UPDATED_QNOM);
    }

    @Test
    @Transactional
    void patchNonExistingCapacitorBankData() throws Exception {
        int databaseSizeBeforeUpdate = capacitorBankDataRepository.findAll().size();
        capacitorBankData.setId(count.incrementAndGet());

        // Create the CapacitorBankData
        CapacitorBankDataDTO capacitorBankDataDTO = capacitorBankDataMapper.toDto(capacitorBankData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCapacitorBankDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, capacitorBankDataDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(capacitorBankDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CapacitorBankData in the database
        List<CapacitorBankData> capacitorBankDataList = capacitorBankDataRepository.findAll();
        assertThat(capacitorBankDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCapacitorBankData() throws Exception {
        int databaseSizeBeforeUpdate = capacitorBankDataRepository.findAll().size();
        capacitorBankData.setId(count.incrementAndGet());

        // Create the CapacitorBankData
        CapacitorBankDataDTO capacitorBankDataDTO = capacitorBankDataMapper.toDto(capacitorBankData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapacitorBankDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(capacitorBankDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CapacitorBankData in the database
        List<CapacitorBankData> capacitorBankDataList = capacitorBankDataRepository.findAll();
        assertThat(capacitorBankDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCapacitorBankData() throws Exception {
        int databaseSizeBeforeUpdate = capacitorBankDataRepository.findAll().size();
        capacitorBankData.setId(count.incrementAndGet());

        // Create the CapacitorBankData
        CapacitorBankDataDTO capacitorBankDataDTO = capacitorBankDataMapper.toDto(capacitorBankData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapacitorBankDataMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(capacitorBankDataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CapacitorBankData in the database
        List<CapacitorBankData> capacitorBankDataList = capacitorBankDataRepository.findAll();
        assertThat(capacitorBankDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCapacitorBankData() throws Exception {
        // Initialize the database
        capacitorBankDataRepository.saveAndFlush(capacitorBankData);

        int databaseSizeBeforeDelete = capacitorBankDataRepository.findAll().size();

        // Delete the capacitorBankData
        restCapacitorBankDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, capacitorBankData.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CapacitorBankData> capacitorBankDataList = capacitorBankDataRepository.findAll();
        assertThat(capacitorBankDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
