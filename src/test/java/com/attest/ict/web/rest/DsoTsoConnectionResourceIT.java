package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.DsoTsoConnection;
import com.attest.ict.domain.Network;
import com.attest.ict.repository.DsoTsoConnectionRepository;
import com.attest.ict.service.criteria.DsoTsoConnectionCriteria;
import com.attest.ict.service.dto.DsoTsoConnectionDTO;
import com.attest.ict.service.mapper.DsoTsoConnectionMapper;
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
 * Integration tests for the {@link DsoTsoConnectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DsoTsoConnectionResourceIT {

    private static final String DEFAULT_TSO_NETWORK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TSO_NETWORK_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_DSO_BUS_NUM = 1L;
    private static final Long UPDATED_DSO_BUS_NUM = 2L;
    private static final Long SMALLER_DSO_BUS_NUM = 1L - 1L;

    private static final Long DEFAULT_TSO_BUS_NUM = 1L;
    private static final Long UPDATED_TSO_BUS_NUM = 2L;
    private static final Long SMALLER_TSO_BUS_NUM = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/dso-tso-connections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DsoTsoConnectionRepository dsoTsoConnectionRepository;

    @Autowired
    private DsoTsoConnectionMapper dsoTsoConnectionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDsoTsoConnectionMockMvc;

    private DsoTsoConnection dsoTsoConnection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DsoTsoConnection createEntity(EntityManager em) {
        DsoTsoConnection dsoTsoConnection = new DsoTsoConnection()
            .tsoNetworkName(DEFAULT_TSO_NETWORK_NAME)
            .dsoBusNum(DEFAULT_DSO_BUS_NUM)
            .tsoBusNum(DEFAULT_TSO_BUS_NUM);
        return dsoTsoConnection;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DsoTsoConnection createUpdatedEntity(EntityManager em) {
        DsoTsoConnection dsoTsoConnection = new DsoTsoConnection()
            .tsoNetworkName(UPDATED_TSO_NETWORK_NAME)
            .dsoBusNum(UPDATED_DSO_BUS_NUM)
            .tsoBusNum(UPDATED_TSO_BUS_NUM);
        return dsoTsoConnection;
    }

    @BeforeEach
    public void initTest() {
        dsoTsoConnection = createEntity(em);
    }

    @Test
    @Transactional
    void createDsoTsoConnection() throws Exception {
        int databaseSizeBeforeCreate = dsoTsoConnectionRepository.findAll().size();
        // Create the DsoTsoConnection
        DsoTsoConnectionDTO dsoTsoConnectionDTO = dsoTsoConnectionMapper.toDto(dsoTsoConnection);
        restDsoTsoConnectionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dsoTsoConnectionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DsoTsoConnection in the database
        List<DsoTsoConnection> dsoTsoConnectionList = dsoTsoConnectionRepository.findAll();
        assertThat(dsoTsoConnectionList).hasSize(databaseSizeBeforeCreate + 1);
        DsoTsoConnection testDsoTsoConnection = dsoTsoConnectionList.get(dsoTsoConnectionList.size() - 1);
        assertThat(testDsoTsoConnection.getTsoNetworkName()).isEqualTo(DEFAULT_TSO_NETWORK_NAME);
        assertThat(testDsoTsoConnection.getDsoBusNum()).isEqualTo(DEFAULT_DSO_BUS_NUM);
        assertThat(testDsoTsoConnection.getTsoBusNum()).isEqualTo(DEFAULT_TSO_BUS_NUM);
    }

    @Test
    @Transactional
    void createDsoTsoConnectionWithExistingId() throws Exception {
        // Create the DsoTsoConnection with an existing ID
        dsoTsoConnection.setId(1L);
        DsoTsoConnectionDTO dsoTsoConnectionDTO = dsoTsoConnectionMapper.toDto(dsoTsoConnection);

        int databaseSizeBeforeCreate = dsoTsoConnectionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDsoTsoConnectionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dsoTsoConnectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DsoTsoConnection in the database
        List<DsoTsoConnection> dsoTsoConnectionList = dsoTsoConnectionRepository.findAll();
        assertThat(dsoTsoConnectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDsoTsoConnections() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList
        restDsoTsoConnectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dsoTsoConnection.getId().intValue())))
            .andExpect(jsonPath("$.[*].tsoNetworkName").value(hasItem(DEFAULT_TSO_NETWORK_NAME)))
            .andExpect(jsonPath("$.[*].dsoBusNum").value(hasItem(DEFAULT_DSO_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].tsoBusNum").value(hasItem(DEFAULT_TSO_BUS_NUM.intValue())));
    }

    @Test
    @Transactional
    void getDsoTsoConnection() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get the dsoTsoConnection
        restDsoTsoConnectionMockMvc
            .perform(get(ENTITY_API_URL_ID, dsoTsoConnection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dsoTsoConnection.getId().intValue()))
            .andExpect(jsonPath("$.tsoNetworkName").value(DEFAULT_TSO_NETWORK_NAME))
            .andExpect(jsonPath("$.dsoBusNum").value(DEFAULT_DSO_BUS_NUM.intValue()))
            .andExpect(jsonPath("$.tsoBusNum").value(DEFAULT_TSO_BUS_NUM.intValue()));
    }

    @Test
    @Transactional
    void getDsoTsoConnectionsByIdFiltering() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        Long id = dsoTsoConnection.getId();

        defaultDsoTsoConnectionShouldBeFound("id.equals=" + id);
        defaultDsoTsoConnectionShouldNotBeFound("id.notEquals=" + id);

        defaultDsoTsoConnectionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDsoTsoConnectionShouldNotBeFound("id.greaterThan=" + id);

        defaultDsoTsoConnectionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDsoTsoConnectionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByTsoNetworkNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where tsoNetworkName equals to DEFAULT_TSO_NETWORK_NAME
        defaultDsoTsoConnectionShouldBeFound("tsoNetworkName.equals=" + DEFAULT_TSO_NETWORK_NAME);

        // Get all the dsoTsoConnectionList where tsoNetworkName equals to UPDATED_TSO_NETWORK_NAME
        defaultDsoTsoConnectionShouldNotBeFound("tsoNetworkName.equals=" + UPDATED_TSO_NETWORK_NAME);
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByTsoNetworkNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where tsoNetworkName not equals to DEFAULT_TSO_NETWORK_NAME
        defaultDsoTsoConnectionShouldNotBeFound("tsoNetworkName.notEquals=" + DEFAULT_TSO_NETWORK_NAME);

        // Get all the dsoTsoConnectionList where tsoNetworkName not equals to UPDATED_TSO_NETWORK_NAME
        defaultDsoTsoConnectionShouldBeFound("tsoNetworkName.notEquals=" + UPDATED_TSO_NETWORK_NAME);
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByTsoNetworkNameIsInShouldWork() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where tsoNetworkName in DEFAULT_TSO_NETWORK_NAME or UPDATED_TSO_NETWORK_NAME
        defaultDsoTsoConnectionShouldBeFound("tsoNetworkName.in=" + DEFAULT_TSO_NETWORK_NAME + "," + UPDATED_TSO_NETWORK_NAME);

        // Get all the dsoTsoConnectionList where tsoNetworkName equals to UPDATED_TSO_NETWORK_NAME
        defaultDsoTsoConnectionShouldNotBeFound("tsoNetworkName.in=" + UPDATED_TSO_NETWORK_NAME);
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByTsoNetworkNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where tsoNetworkName is not null
        defaultDsoTsoConnectionShouldBeFound("tsoNetworkName.specified=true");

        // Get all the dsoTsoConnectionList where tsoNetworkName is null
        defaultDsoTsoConnectionShouldNotBeFound("tsoNetworkName.specified=false");
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByTsoNetworkNameContainsSomething() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where tsoNetworkName contains DEFAULT_TSO_NETWORK_NAME
        defaultDsoTsoConnectionShouldBeFound("tsoNetworkName.contains=" + DEFAULT_TSO_NETWORK_NAME);

        // Get all the dsoTsoConnectionList where tsoNetworkName contains UPDATED_TSO_NETWORK_NAME
        defaultDsoTsoConnectionShouldNotBeFound("tsoNetworkName.contains=" + UPDATED_TSO_NETWORK_NAME);
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByTsoNetworkNameNotContainsSomething() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where tsoNetworkName does not contain DEFAULT_TSO_NETWORK_NAME
        defaultDsoTsoConnectionShouldNotBeFound("tsoNetworkName.doesNotContain=" + DEFAULT_TSO_NETWORK_NAME);

        // Get all the dsoTsoConnectionList where tsoNetworkName does not contain UPDATED_TSO_NETWORK_NAME
        defaultDsoTsoConnectionShouldBeFound("tsoNetworkName.doesNotContain=" + UPDATED_TSO_NETWORK_NAME);
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByDsoBusNumIsEqualToSomething() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where dsoBusNum equals to DEFAULT_DSO_BUS_NUM
        defaultDsoTsoConnectionShouldBeFound("dsoBusNum.equals=" + DEFAULT_DSO_BUS_NUM);

        // Get all the dsoTsoConnectionList where dsoBusNum equals to UPDATED_DSO_BUS_NUM
        defaultDsoTsoConnectionShouldNotBeFound("dsoBusNum.equals=" + UPDATED_DSO_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByDsoBusNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where dsoBusNum not equals to DEFAULT_DSO_BUS_NUM
        defaultDsoTsoConnectionShouldNotBeFound("dsoBusNum.notEquals=" + DEFAULT_DSO_BUS_NUM);

        // Get all the dsoTsoConnectionList where dsoBusNum not equals to UPDATED_DSO_BUS_NUM
        defaultDsoTsoConnectionShouldBeFound("dsoBusNum.notEquals=" + UPDATED_DSO_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByDsoBusNumIsInShouldWork() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where dsoBusNum in DEFAULT_DSO_BUS_NUM or UPDATED_DSO_BUS_NUM
        defaultDsoTsoConnectionShouldBeFound("dsoBusNum.in=" + DEFAULT_DSO_BUS_NUM + "," + UPDATED_DSO_BUS_NUM);

        // Get all the dsoTsoConnectionList where dsoBusNum equals to UPDATED_DSO_BUS_NUM
        defaultDsoTsoConnectionShouldNotBeFound("dsoBusNum.in=" + UPDATED_DSO_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByDsoBusNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where dsoBusNum is not null
        defaultDsoTsoConnectionShouldBeFound("dsoBusNum.specified=true");

        // Get all the dsoTsoConnectionList where dsoBusNum is null
        defaultDsoTsoConnectionShouldNotBeFound("dsoBusNum.specified=false");
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByDsoBusNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where dsoBusNum is greater than or equal to DEFAULT_DSO_BUS_NUM
        defaultDsoTsoConnectionShouldBeFound("dsoBusNum.greaterThanOrEqual=" + DEFAULT_DSO_BUS_NUM);

        // Get all the dsoTsoConnectionList where dsoBusNum is greater than or equal to UPDATED_DSO_BUS_NUM
        defaultDsoTsoConnectionShouldNotBeFound("dsoBusNum.greaterThanOrEqual=" + UPDATED_DSO_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByDsoBusNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where dsoBusNum is less than or equal to DEFAULT_DSO_BUS_NUM
        defaultDsoTsoConnectionShouldBeFound("dsoBusNum.lessThanOrEqual=" + DEFAULT_DSO_BUS_NUM);

        // Get all the dsoTsoConnectionList where dsoBusNum is less than or equal to SMALLER_DSO_BUS_NUM
        defaultDsoTsoConnectionShouldNotBeFound("dsoBusNum.lessThanOrEqual=" + SMALLER_DSO_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByDsoBusNumIsLessThanSomething() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where dsoBusNum is less than DEFAULT_DSO_BUS_NUM
        defaultDsoTsoConnectionShouldNotBeFound("dsoBusNum.lessThan=" + DEFAULT_DSO_BUS_NUM);

        // Get all the dsoTsoConnectionList where dsoBusNum is less than UPDATED_DSO_BUS_NUM
        defaultDsoTsoConnectionShouldBeFound("dsoBusNum.lessThan=" + UPDATED_DSO_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByDsoBusNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where dsoBusNum is greater than DEFAULT_DSO_BUS_NUM
        defaultDsoTsoConnectionShouldNotBeFound("dsoBusNum.greaterThan=" + DEFAULT_DSO_BUS_NUM);

        // Get all the dsoTsoConnectionList where dsoBusNum is greater than SMALLER_DSO_BUS_NUM
        defaultDsoTsoConnectionShouldBeFound("dsoBusNum.greaterThan=" + SMALLER_DSO_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByTsoBusNumIsEqualToSomething() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where tsoBusNum equals to DEFAULT_TSO_BUS_NUM
        defaultDsoTsoConnectionShouldBeFound("tsoBusNum.equals=" + DEFAULT_TSO_BUS_NUM);

        // Get all the dsoTsoConnectionList where tsoBusNum equals to UPDATED_TSO_BUS_NUM
        defaultDsoTsoConnectionShouldNotBeFound("tsoBusNum.equals=" + UPDATED_TSO_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByTsoBusNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where tsoBusNum not equals to DEFAULT_TSO_BUS_NUM
        defaultDsoTsoConnectionShouldNotBeFound("tsoBusNum.notEquals=" + DEFAULT_TSO_BUS_NUM);

        // Get all the dsoTsoConnectionList where tsoBusNum not equals to UPDATED_TSO_BUS_NUM
        defaultDsoTsoConnectionShouldBeFound("tsoBusNum.notEquals=" + UPDATED_TSO_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByTsoBusNumIsInShouldWork() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where tsoBusNum in DEFAULT_TSO_BUS_NUM or UPDATED_TSO_BUS_NUM
        defaultDsoTsoConnectionShouldBeFound("tsoBusNum.in=" + DEFAULT_TSO_BUS_NUM + "," + UPDATED_TSO_BUS_NUM);

        // Get all the dsoTsoConnectionList where tsoBusNum equals to UPDATED_TSO_BUS_NUM
        defaultDsoTsoConnectionShouldNotBeFound("tsoBusNum.in=" + UPDATED_TSO_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByTsoBusNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where tsoBusNum is not null
        defaultDsoTsoConnectionShouldBeFound("tsoBusNum.specified=true");

        // Get all the dsoTsoConnectionList where tsoBusNum is null
        defaultDsoTsoConnectionShouldNotBeFound("tsoBusNum.specified=false");
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByTsoBusNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where tsoBusNum is greater than or equal to DEFAULT_TSO_BUS_NUM
        defaultDsoTsoConnectionShouldBeFound("tsoBusNum.greaterThanOrEqual=" + DEFAULT_TSO_BUS_NUM);

        // Get all the dsoTsoConnectionList where tsoBusNum is greater than or equal to UPDATED_TSO_BUS_NUM
        defaultDsoTsoConnectionShouldNotBeFound("tsoBusNum.greaterThanOrEqual=" + UPDATED_TSO_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByTsoBusNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where tsoBusNum is less than or equal to DEFAULT_TSO_BUS_NUM
        defaultDsoTsoConnectionShouldBeFound("tsoBusNum.lessThanOrEqual=" + DEFAULT_TSO_BUS_NUM);

        // Get all the dsoTsoConnectionList where tsoBusNum is less than or equal to SMALLER_TSO_BUS_NUM
        defaultDsoTsoConnectionShouldNotBeFound("tsoBusNum.lessThanOrEqual=" + SMALLER_TSO_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByTsoBusNumIsLessThanSomething() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where tsoBusNum is less than DEFAULT_TSO_BUS_NUM
        defaultDsoTsoConnectionShouldNotBeFound("tsoBusNum.lessThan=" + DEFAULT_TSO_BUS_NUM);

        // Get all the dsoTsoConnectionList where tsoBusNum is less than UPDATED_TSO_BUS_NUM
        defaultDsoTsoConnectionShouldBeFound("tsoBusNum.lessThan=" + UPDATED_TSO_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByTsoBusNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        // Get all the dsoTsoConnectionList where tsoBusNum is greater than DEFAULT_TSO_BUS_NUM
        defaultDsoTsoConnectionShouldNotBeFound("tsoBusNum.greaterThan=" + DEFAULT_TSO_BUS_NUM);

        // Get all the dsoTsoConnectionList where tsoBusNum is greater than SMALLER_TSO_BUS_NUM
        defaultDsoTsoConnectionShouldBeFound("tsoBusNum.greaterThan=" + SMALLER_TSO_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllDsoTsoConnectionsByDsoNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);
        Network dsoNetwork;
        if (TestUtil.findAll(em, Network.class).isEmpty()) {
            dsoNetwork = NetworkResourceIT.createEntity(em);
            em.persist(dsoNetwork);
            em.flush();
        } else {
            dsoNetwork = TestUtil.findAll(em, Network.class).get(0);
        }
        em.persist(dsoNetwork);
        em.flush();
        dsoTsoConnection.setDsoNetwork(dsoNetwork);
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);
        Long dsoNetworkId = dsoNetwork.getId();

        // Get all the dsoTsoConnectionList where dsoNetwork equals to dsoNetworkId
        defaultDsoTsoConnectionShouldBeFound("dsoNetworkId.equals=" + dsoNetworkId);

        // Get all the dsoTsoConnectionList where dsoNetwork equals to (dsoNetworkId + 1)
        defaultDsoTsoConnectionShouldNotBeFound("dsoNetworkId.equals=" + (dsoNetworkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDsoTsoConnectionShouldBeFound(String filter) throws Exception {
        restDsoTsoConnectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dsoTsoConnection.getId().intValue())))
            .andExpect(jsonPath("$.[*].tsoNetworkName").value(hasItem(DEFAULT_TSO_NETWORK_NAME)))
            .andExpect(jsonPath("$.[*].dsoBusNum").value(hasItem(DEFAULT_DSO_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].tsoBusNum").value(hasItem(DEFAULT_TSO_BUS_NUM.intValue())));

        // Check, that the count call also returns 1
        restDsoTsoConnectionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDsoTsoConnectionShouldNotBeFound(String filter) throws Exception {
        restDsoTsoConnectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDsoTsoConnectionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDsoTsoConnection() throws Exception {
        // Get the dsoTsoConnection
        restDsoTsoConnectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDsoTsoConnection() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        int databaseSizeBeforeUpdate = dsoTsoConnectionRepository.findAll().size();

        // Update the dsoTsoConnection
        DsoTsoConnection updatedDsoTsoConnection = dsoTsoConnectionRepository.findById(dsoTsoConnection.getId()).get();
        // Disconnect from session so that the updates on updatedDsoTsoConnection are not directly saved in db
        em.detach(updatedDsoTsoConnection);
        updatedDsoTsoConnection.tsoNetworkName(UPDATED_TSO_NETWORK_NAME).dsoBusNum(UPDATED_DSO_BUS_NUM).tsoBusNum(UPDATED_TSO_BUS_NUM);
        DsoTsoConnectionDTO dsoTsoConnectionDTO = dsoTsoConnectionMapper.toDto(updatedDsoTsoConnection);

        restDsoTsoConnectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dsoTsoConnectionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dsoTsoConnectionDTO))
            )
            .andExpect(status().isOk());

        // Validate the DsoTsoConnection in the database
        List<DsoTsoConnection> dsoTsoConnectionList = dsoTsoConnectionRepository.findAll();
        assertThat(dsoTsoConnectionList).hasSize(databaseSizeBeforeUpdate);
        DsoTsoConnection testDsoTsoConnection = dsoTsoConnectionList.get(dsoTsoConnectionList.size() - 1);
        assertThat(testDsoTsoConnection.getTsoNetworkName()).isEqualTo(UPDATED_TSO_NETWORK_NAME);
        assertThat(testDsoTsoConnection.getDsoBusNum()).isEqualTo(UPDATED_DSO_BUS_NUM);
        assertThat(testDsoTsoConnection.getTsoBusNum()).isEqualTo(UPDATED_TSO_BUS_NUM);
    }

    @Test
    @Transactional
    void putNonExistingDsoTsoConnection() throws Exception {
        int databaseSizeBeforeUpdate = dsoTsoConnectionRepository.findAll().size();
        dsoTsoConnection.setId(count.incrementAndGet());

        // Create the DsoTsoConnection
        DsoTsoConnectionDTO dsoTsoConnectionDTO = dsoTsoConnectionMapper.toDto(dsoTsoConnection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDsoTsoConnectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dsoTsoConnectionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dsoTsoConnectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DsoTsoConnection in the database
        List<DsoTsoConnection> dsoTsoConnectionList = dsoTsoConnectionRepository.findAll();
        assertThat(dsoTsoConnectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDsoTsoConnection() throws Exception {
        int databaseSizeBeforeUpdate = dsoTsoConnectionRepository.findAll().size();
        dsoTsoConnection.setId(count.incrementAndGet());

        // Create the DsoTsoConnection
        DsoTsoConnectionDTO dsoTsoConnectionDTO = dsoTsoConnectionMapper.toDto(dsoTsoConnection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDsoTsoConnectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dsoTsoConnectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DsoTsoConnection in the database
        List<DsoTsoConnection> dsoTsoConnectionList = dsoTsoConnectionRepository.findAll();
        assertThat(dsoTsoConnectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDsoTsoConnection() throws Exception {
        int databaseSizeBeforeUpdate = dsoTsoConnectionRepository.findAll().size();
        dsoTsoConnection.setId(count.incrementAndGet());

        // Create the DsoTsoConnection
        DsoTsoConnectionDTO dsoTsoConnectionDTO = dsoTsoConnectionMapper.toDto(dsoTsoConnection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDsoTsoConnectionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dsoTsoConnectionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DsoTsoConnection in the database
        List<DsoTsoConnection> dsoTsoConnectionList = dsoTsoConnectionRepository.findAll();
        assertThat(dsoTsoConnectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDsoTsoConnectionWithPatch() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        int databaseSizeBeforeUpdate = dsoTsoConnectionRepository.findAll().size();

        // Update the dsoTsoConnection using partial update
        DsoTsoConnection partialUpdatedDsoTsoConnection = new DsoTsoConnection();
        partialUpdatedDsoTsoConnection.setId(dsoTsoConnection.getId());

        partialUpdatedDsoTsoConnection
            .tsoNetworkName(UPDATED_TSO_NETWORK_NAME)
            .dsoBusNum(UPDATED_DSO_BUS_NUM)
            .tsoBusNum(UPDATED_TSO_BUS_NUM);

        restDsoTsoConnectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDsoTsoConnection.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDsoTsoConnection))
            )
            .andExpect(status().isOk());

        // Validate the DsoTsoConnection in the database
        List<DsoTsoConnection> dsoTsoConnectionList = dsoTsoConnectionRepository.findAll();
        assertThat(dsoTsoConnectionList).hasSize(databaseSizeBeforeUpdate);
        DsoTsoConnection testDsoTsoConnection = dsoTsoConnectionList.get(dsoTsoConnectionList.size() - 1);
        assertThat(testDsoTsoConnection.getTsoNetworkName()).isEqualTo(UPDATED_TSO_NETWORK_NAME);
        assertThat(testDsoTsoConnection.getDsoBusNum()).isEqualTo(UPDATED_DSO_BUS_NUM);
        assertThat(testDsoTsoConnection.getTsoBusNum()).isEqualTo(UPDATED_TSO_BUS_NUM);
    }

    @Test
    @Transactional
    void fullUpdateDsoTsoConnectionWithPatch() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        int databaseSizeBeforeUpdate = dsoTsoConnectionRepository.findAll().size();

        // Update the dsoTsoConnection using partial update
        DsoTsoConnection partialUpdatedDsoTsoConnection = new DsoTsoConnection();
        partialUpdatedDsoTsoConnection.setId(dsoTsoConnection.getId());

        partialUpdatedDsoTsoConnection
            .tsoNetworkName(UPDATED_TSO_NETWORK_NAME)
            .dsoBusNum(UPDATED_DSO_BUS_NUM)
            .tsoBusNum(UPDATED_TSO_BUS_NUM);

        restDsoTsoConnectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDsoTsoConnection.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDsoTsoConnection))
            )
            .andExpect(status().isOk());

        // Validate the DsoTsoConnection in the database
        List<DsoTsoConnection> dsoTsoConnectionList = dsoTsoConnectionRepository.findAll();
        assertThat(dsoTsoConnectionList).hasSize(databaseSizeBeforeUpdate);
        DsoTsoConnection testDsoTsoConnection = dsoTsoConnectionList.get(dsoTsoConnectionList.size() - 1);
        assertThat(testDsoTsoConnection.getTsoNetworkName()).isEqualTo(UPDATED_TSO_NETWORK_NAME);
        assertThat(testDsoTsoConnection.getDsoBusNum()).isEqualTo(UPDATED_DSO_BUS_NUM);
        assertThat(testDsoTsoConnection.getTsoBusNum()).isEqualTo(UPDATED_TSO_BUS_NUM);
    }

    @Test
    @Transactional
    void patchNonExistingDsoTsoConnection() throws Exception {
        int databaseSizeBeforeUpdate = dsoTsoConnectionRepository.findAll().size();
        dsoTsoConnection.setId(count.incrementAndGet());

        // Create the DsoTsoConnection
        DsoTsoConnectionDTO dsoTsoConnectionDTO = dsoTsoConnectionMapper.toDto(dsoTsoConnection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDsoTsoConnectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dsoTsoConnectionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dsoTsoConnectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DsoTsoConnection in the database
        List<DsoTsoConnection> dsoTsoConnectionList = dsoTsoConnectionRepository.findAll();
        assertThat(dsoTsoConnectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDsoTsoConnection() throws Exception {
        int databaseSizeBeforeUpdate = dsoTsoConnectionRepository.findAll().size();
        dsoTsoConnection.setId(count.incrementAndGet());

        // Create the DsoTsoConnection
        DsoTsoConnectionDTO dsoTsoConnectionDTO = dsoTsoConnectionMapper.toDto(dsoTsoConnection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDsoTsoConnectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dsoTsoConnectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DsoTsoConnection in the database
        List<DsoTsoConnection> dsoTsoConnectionList = dsoTsoConnectionRepository.findAll();
        assertThat(dsoTsoConnectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDsoTsoConnection() throws Exception {
        int databaseSizeBeforeUpdate = dsoTsoConnectionRepository.findAll().size();
        dsoTsoConnection.setId(count.incrementAndGet());

        // Create the DsoTsoConnection
        DsoTsoConnectionDTO dsoTsoConnectionDTO = dsoTsoConnectionMapper.toDto(dsoTsoConnection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDsoTsoConnectionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dsoTsoConnectionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DsoTsoConnection in the database
        List<DsoTsoConnection> dsoTsoConnectionList = dsoTsoConnectionRepository.findAll();
        assertThat(dsoTsoConnectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDsoTsoConnection() throws Exception {
        // Initialize the database
        dsoTsoConnectionRepository.saveAndFlush(dsoTsoConnection);

        int databaseSizeBeforeDelete = dsoTsoConnectionRepository.findAll().size();

        // Delete the dsoTsoConnection
        restDsoTsoConnectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, dsoTsoConnection.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DsoTsoConnection> dsoTsoConnectionList = dsoTsoConnectionRepository.findAll();
        assertThat(dsoTsoConnectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
