package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.BillingDer;
import com.attest.ict.domain.Network;
import com.attest.ict.repository.BillingDerRepository;
import com.attest.ict.service.criteria.BillingDerCriteria;
import com.attest.ict.service.dto.BillingDerDTO;
import com.attest.ict.service.mapper.BillingDerMapper;
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
 * Integration tests for the {@link BillingDerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BillingDerResourceIT {

    private static final Long DEFAULT_BUS_NUM = 1L;
    private static final Long UPDATED_BUS_NUM = 2L;
    private static final Long SMALLER_BUS_NUM = 1L - 1L;

    private static final Long DEFAULT_MAX_POWER_KW = 1L;
    private static final Long UPDATED_MAX_POWER_KW = 2L;
    private static final Long SMALLER_MAX_POWER_KW = 1L - 1L;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/billing-ders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BillingDerRepository billingDerRepository;

    @Autowired
    private BillingDerMapper billingDerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillingDerMockMvc;

    private BillingDer billingDer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillingDer createEntity(EntityManager em) {
        BillingDer billingDer = new BillingDer().busNum(DEFAULT_BUS_NUM).maxPowerKw(DEFAULT_MAX_POWER_KW).type(DEFAULT_TYPE);
        return billingDer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillingDer createUpdatedEntity(EntityManager em) {
        BillingDer billingDer = new BillingDer().busNum(UPDATED_BUS_NUM).maxPowerKw(UPDATED_MAX_POWER_KW).type(UPDATED_TYPE);
        return billingDer;
    }

    @BeforeEach
    public void initTest() {
        billingDer = createEntity(em);
    }

    @Test
    @Transactional
    void createBillingDer() throws Exception {
        int databaseSizeBeforeCreate = billingDerRepository.findAll().size();
        // Create the BillingDer
        BillingDerDTO billingDerDTO = billingDerMapper.toDto(billingDer);
        restBillingDerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billingDerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BillingDer in the database
        List<BillingDer> billingDerList = billingDerRepository.findAll();
        assertThat(billingDerList).hasSize(databaseSizeBeforeCreate + 1);
        BillingDer testBillingDer = billingDerList.get(billingDerList.size() - 1);
        assertThat(testBillingDer.getBusNum()).isEqualTo(DEFAULT_BUS_NUM);
        assertThat(testBillingDer.getMaxPowerKw()).isEqualTo(DEFAULT_MAX_POWER_KW);
        assertThat(testBillingDer.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createBillingDerWithExistingId() throws Exception {
        // Create the BillingDer with an existing ID
        billingDer.setId(1L);
        BillingDerDTO billingDerDTO = billingDerMapper.toDto(billingDer);

        int databaseSizeBeforeCreate = billingDerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillingDerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billingDerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillingDer in the database
        List<BillingDer> billingDerList = billingDerRepository.findAll();
        assertThat(billingDerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBillingDers() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList
        restBillingDerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billingDer.getId().intValue())))
            .andExpect(jsonPath("$.[*].busNum").value(hasItem(DEFAULT_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].maxPowerKw").value(hasItem(DEFAULT_MAX_POWER_KW.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getBillingDer() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get the billingDer
        restBillingDerMockMvc
            .perform(get(ENTITY_API_URL_ID, billingDer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(billingDer.getId().intValue()))
            .andExpect(jsonPath("$.busNum").value(DEFAULT_BUS_NUM.intValue()))
            .andExpect(jsonPath("$.maxPowerKw").value(DEFAULT_MAX_POWER_KW.intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getBillingDersByIdFiltering() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        Long id = billingDer.getId();

        defaultBillingDerShouldBeFound("id.equals=" + id);
        defaultBillingDerShouldNotBeFound("id.notEquals=" + id);

        defaultBillingDerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBillingDerShouldNotBeFound("id.greaterThan=" + id);

        defaultBillingDerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBillingDerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBillingDersByBusNumIsEqualToSomething() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where busNum equals to DEFAULT_BUS_NUM
        defaultBillingDerShouldBeFound("busNum.equals=" + DEFAULT_BUS_NUM);

        // Get all the billingDerList where busNum equals to UPDATED_BUS_NUM
        defaultBillingDerShouldNotBeFound("busNum.equals=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllBillingDersByBusNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where busNum not equals to DEFAULT_BUS_NUM
        defaultBillingDerShouldNotBeFound("busNum.notEquals=" + DEFAULT_BUS_NUM);

        // Get all the billingDerList where busNum not equals to UPDATED_BUS_NUM
        defaultBillingDerShouldBeFound("busNum.notEquals=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllBillingDersByBusNumIsInShouldWork() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where busNum in DEFAULT_BUS_NUM or UPDATED_BUS_NUM
        defaultBillingDerShouldBeFound("busNum.in=" + DEFAULT_BUS_NUM + "," + UPDATED_BUS_NUM);

        // Get all the billingDerList where busNum equals to UPDATED_BUS_NUM
        defaultBillingDerShouldNotBeFound("busNum.in=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllBillingDersByBusNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where busNum is not null
        defaultBillingDerShouldBeFound("busNum.specified=true");

        // Get all the billingDerList where busNum is null
        defaultBillingDerShouldNotBeFound("busNum.specified=false");
    }

    @Test
    @Transactional
    void getAllBillingDersByBusNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where busNum is greater than or equal to DEFAULT_BUS_NUM
        defaultBillingDerShouldBeFound("busNum.greaterThanOrEqual=" + DEFAULT_BUS_NUM);

        // Get all the billingDerList where busNum is greater than or equal to UPDATED_BUS_NUM
        defaultBillingDerShouldNotBeFound("busNum.greaterThanOrEqual=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllBillingDersByBusNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where busNum is less than or equal to DEFAULT_BUS_NUM
        defaultBillingDerShouldBeFound("busNum.lessThanOrEqual=" + DEFAULT_BUS_NUM);

        // Get all the billingDerList where busNum is less than or equal to SMALLER_BUS_NUM
        defaultBillingDerShouldNotBeFound("busNum.lessThanOrEqual=" + SMALLER_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllBillingDersByBusNumIsLessThanSomething() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where busNum is less than DEFAULT_BUS_NUM
        defaultBillingDerShouldNotBeFound("busNum.lessThan=" + DEFAULT_BUS_NUM);

        // Get all the billingDerList where busNum is less than UPDATED_BUS_NUM
        defaultBillingDerShouldBeFound("busNum.lessThan=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllBillingDersByBusNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where busNum is greater than DEFAULT_BUS_NUM
        defaultBillingDerShouldNotBeFound("busNum.greaterThan=" + DEFAULT_BUS_NUM);

        // Get all the billingDerList where busNum is greater than SMALLER_BUS_NUM
        defaultBillingDerShouldBeFound("busNum.greaterThan=" + SMALLER_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllBillingDersByMaxPowerKwIsEqualToSomething() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where maxPowerKw equals to DEFAULT_MAX_POWER_KW
        defaultBillingDerShouldBeFound("maxPowerKw.equals=" + DEFAULT_MAX_POWER_KW);

        // Get all the billingDerList where maxPowerKw equals to UPDATED_MAX_POWER_KW
        defaultBillingDerShouldNotBeFound("maxPowerKw.equals=" + UPDATED_MAX_POWER_KW);
    }

    @Test
    @Transactional
    void getAllBillingDersByMaxPowerKwIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where maxPowerKw not equals to DEFAULT_MAX_POWER_KW
        defaultBillingDerShouldNotBeFound("maxPowerKw.notEquals=" + DEFAULT_MAX_POWER_KW);

        // Get all the billingDerList where maxPowerKw not equals to UPDATED_MAX_POWER_KW
        defaultBillingDerShouldBeFound("maxPowerKw.notEquals=" + UPDATED_MAX_POWER_KW);
    }

    @Test
    @Transactional
    void getAllBillingDersByMaxPowerKwIsInShouldWork() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where maxPowerKw in DEFAULT_MAX_POWER_KW or UPDATED_MAX_POWER_KW
        defaultBillingDerShouldBeFound("maxPowerKw.in=" + DEFAULT_MAX_POWER_KW + "," + UPDATED_MAX_POWER_KW);

        // Get all the billingDerList where maxPowerKw equals to UPDATED_MAX_POWER_KW
        defaultBillingDerShouldNotBeFound("maxPowerKw.in=" + UPDATED_MAX_POWER_KW);
    }

    @Test
    @Transactional
    void getAllBillingDersByMaxPowerKwIsNullOrNotNull() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where maxPowerKw is not null
        defaultBillingDerShouldBeFound("maxPowerKw.specified=true");

        // Get all the billingDerList where maxPowerKw is null
        defaultBillingDerShouldNotBeFound("maxPowerKw.specified=false");
    }

    @Test
    @Transactional
    void getAllBillingDersByMaxPowerKwIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where maxPowerKw is greater than or equal to DEFAULT_MAX_POWER_KW
        defaultBillingDerShouldBeFound("maxPowerKw.greaterThanOrEqual=" + DEFAULT_MAX_POWER_KW);

        // Get all the billingDerList where maxPowerKw is greater than or equal to UPDATED_MAX_POWER_KW
        defaultBillingDerShouldNotBeFound("maxPowerKw.greaterThanOrEqual=" + UPDATED_MAX_POWER_KW);
    }

    @Test
    @Transactional
    void getAllBillingDersByMaxPowerKwIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where maxPowerKw is less than or equal to DEFAULT_MAX_POWER_KW
        defaultBillingDerShouldBeFound("maxPowerKw.lessThanOrEqual=" + DEFAULT_MAX_POWER_KW);

        // Get all the billingDerList where maxPowerKw is less than or equal to SMALLER_MAX_POWER_KW
        defaultBillingDerShouldNotBeFound("maxPowerKw.lessThanOrEqual=" + SMALLER_MAX_POWER_KW);
    }

    @Test
    @Transactional
    void getAllBillingDersByMaxPowerKwIsLessThanSomething() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where maxPowerKw is less than DEFAULT_MAX_POWER_KW
        defaultBillingDerShouldNotBeFound("maxPowerKw.lessThan=" + DEFAULT_MAX_POWER_KW);

        // Get all the billingDerList where maxPowerKw is less than UPDATED_MAX_POWER_KW
        defaultBillingDerShouldBeFound("maxPowerKw.lessThan=" + UPDATED_MAX_POWER_KW);
    }

    @Test
    @Transactional
    void getAllBillingDersByMaxPowerKwIsGreaterThanSomething() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where maxPowerKw is greater than DEFAULT_MAX_POWER_KW
        defaultBillingDerShouldNotBeFound("maxPowerKw.greaterThan=" + DEFAULT_MAX_POWER_KW);

        // Get all the billingDerList where maxPowerKw is greater than SMALLER_MAX_POWER_KW
        defaultBillingDerShouldBeFound("maxPowerKw.greaterThan=" + SMALLER_MAX_POWER_KW);
    }

    @Test
    @Transactional
    void getAllBillingDersByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where type equals to DEFAULT_TYPE
        defaultBillingDerShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the billingDerList where type equals to UPDATED_TYPE
        defaultBillingDerShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBillingDersByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where type not equals to DEFAULT_TYPE
        defaultBillingDerShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the billingDerList where type not equals to UPDATED_TYPE
        defaultBillingDerShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBillingDersByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultBillingDerShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the billingDerList where type equals to UPDATED_TYPE
        defaultBillingDerShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBillingDersByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where type is not null
        defaultBillingDerShouldBeFound("type.specified=true");

        // Get all the billingDerList where type is null
        defaultBillingDerShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllBillingDersByTypeContainsSomething() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where type contains DEFAULT_TYPE
        defaultBillingDerShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the billingDerList where type contains UPDATED_TYPE
        defaultBillingDerShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBillingDersByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        // Get all the billingDerList where type does not contain DEFAULT_TYPE
        defaultBillingDerShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the billingDerList where type does not contain UPDATED_TYPE
        defaultBillingDerShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBillingDersByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);
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
        billingDer.setNetwork(network);
        billingDerRepository.saveAndFlush(billingDer);
        Long networkId = network.getId();

        // Get all the billingDerList where network equals to networkId
        defaultBillingDerShouldBeFound("networkId.equals=" + networkId);

        // Get all the billingDerList where network equals to (networkId + 1)
        defaultBillingDerShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBillingDerShouldBeFound(String filter) throws Exception {
        restBillingDerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billingDer.getId().intValue())))
            .andExpect(jsonPath("$.[*].busNum").value(hasItem(DEFAULT_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].maxPowerKw").value(hasItem(DEFAULT_MAX_POWER_KW.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));

        // Check, that the count call also returns 1
        restBillingDerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBillingDerShouldNotBeFound(String filter) throws Exception {
        restBillingDerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBillingDerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBillingDer() throws Exception {
        // Get the billingDer
        restBillingDerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBillingDer() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        int databaseSizeBeforeUpdate = billingDerRepository.findAll().size();

        // Update the billingDer
        BillingDer updatedBillingDer = billingDerRepository.findById(billingDer.getId()).get();
        // Disconnect from session so that the updates on updatedBillingDer are not directly saved in db
        em.detach(updatedBillingDer);
        updatedBillingDer.busNum(UPDATED_BUS_NUM).maxPowerKw(UPDATED_MAX_POWER_KW).type(UPDATED_TYPE);
        BillingDerDTO billingDerDTO = billingDerMapper.toDto(updatedBillingDer);

        restBillingDerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billingDerDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billingDerDTO))
            )
            .andExpect(status().isOk());

        // Validate the BillingDer in the database
        List<BillingDer> billingDerList = billingDerRepository.findAll();
        assertThat(billingDerList).hasSize(databaseSizeBeforeUpdate);
        BillingDer testBillingDer = billingDerList.get(billingDerList.size() - 1);
        assertThat(testBillingDer.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testBillingDer.getMaxPowerKw()).isEqualTo(UPDATED_MAX_POWER_KW);
        assertThat(testBillingDer.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingBillingDer() throws Exception {
        int databaseSizeBeforeUpdate = billingDerRepository.findAll().size();
        billingDer.setId(count.incrementAndGet());

        // Create the BillingDer
        BillingDerDTO billingDerDTO = billingDerMapper.toDto(billingDer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillingDerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billingDerDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billingDerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillingDer in the database
        List<BillingDer> billingDerList = billingDerRepository.findAll();
        assertThat(billingDerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBillingDer() throws Exception {
        int databaseSizeBeforeUpdate = billingDerRepository.findAll().size();
        billingDer.setId(count.incrementAndGet());

        // Create the BillingDer
        BillingDerDTO billingDerDTO = billingDerMapper.toDto(billingDer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillingDerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billingDerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillingDer in the database
        List<BillingDer> billingDerList = billingDerRepository.findAll();
        assertThat(billingDerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBillingDer() throws Exception {
        int databaseSizeBeforeUpdate = billingDerRepository.findAll().size();
        billingDer.setId(count.incrementAndGet());

        // Create the BillingDer
        BillingDerDTO billingDerDTO = billingDerMapper.toDto(billingDer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillingDerMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billingDerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillingDer in the database
        List<BillingDer> billingDerList = billingDerRepository.findAll();
        assertThat(billingDerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBillingDerWithPatch() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        int databaseSizeBeforeUpdate = billingDerRepository.findAll().size();

        // Update the billingDer using partial update
        BillingDer partialUpdatedBillingDer = new BillingDer();
        partialUpdatedBillingDer.setId(billingDer.getId());

        partialUpdatedBillingDer.maxPowerKw(UPDATED_MAX_POWER_KW);

        restBillingDerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillingDer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBillingDer))
            )
            .andExpect(status().isOk());

        // Validate the BillingDer in the database
        List<BillingDer> billingDerList = billingDerRepository.findAll();
        assertThat(billingDerList).hasSize(databaseSizeBeforeUpdate);
        BillingDer testBillingDer = billingDerList.get(billingDerList.size() - 1);
        assertThat(testBillingDer.getBusNum()).isEqualTo(DEFAULT_BUS_NUM);
        assertThat(testBillingDer.getMaxPowerKw()).isEqualTo(UPDATED_MAX_POWER_KW);
        assertThat(testBillingDer.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateBillingDerWithPatch() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        int databaseSizeBeforeUpdate = billingDerRepository.findAll().size();

        // Update the billingDer using partial update
        BillingDer partialUpdatedBillingDer = new BillingDer();
        partialUpdatedBillingDer.setId(billingDer.getId());

        partialUpdatedBillingDer.busNum(UPDATED_BUS_NUM).maxPowerKw(UPDATED_MAX_POWER_KW).type(UPDATED_TYPE);

        restBillingDerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillingDer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBillingDer))
            )
            .andExpect(status().isOk());

        // Validate the BillingDer in the database
        List<BillingDer> billingDerList = billingDerRepository.findAll();
        assertThat(billingDerList).hasSize(databaseSizeBeforeUpdate);
        BillingDer testBillingDer = billingDerList.get(billingDerList.size() - 1);
        assertThat(testBillingDer.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testBillingDer.getMaxPowerKw()).isEqualTo(UPDATED_MAX_POWER_KW);
        assertThat(testBillingDer.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingBillingDer() throws Exception {
        int databaseSizeBeforeUpdate = billingDerRepository.findAll().size();
        billingDer.setId(count.incrementAndGet());

        // Create the BillingDer
        BillingDerDTO billingDerDTO = billingDerMapper.toDto(billingDer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillingDerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, billingDerDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billingDerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillingDer in the database
        List<BillingDer> billingDerList = billingDerRepository.findAll();
        assertThat(billingDerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBillingDer() throws Exception {
        int databaseSizeBeforeUpdate = billingDerRepository.findAll().size();
        billingDer.setId(count.incrementAndGet());

        // Create the BillingDer
        BillingDerDTO billingDerDTO = billingDerMapper.toDto(billingDer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillingDerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billingDerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillingDer in the database
        List<BillingDer> billingDerList = billingDerRepository.findAll();
        assertThat(billingDerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBillingDer() throws Exception {
        int databaseSizeBeforeUpdate = billingDerRepository.findAll().size();
        billingDer.setId(count.incrementAndGet());

        // Create the BillingDer
        BillingDerDTO billingDerDTO = billingDerMapper.toDto(billingDer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillingDerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billingDerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillingDer in the database
        List<BillingDer> billingDerList = billingDerRepository.findAll();
        assertThat(billingDerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBillingDer() throws Exception {
        // Initialize the database
        billingDerRepository.saveAndFlush(billingDer);

        int databaseSizeBeforeDelete = billingDerRepository.findAll().size();

        // Delete the billingDer
        restBillingDerMockMvc
            .perform(delete(ENTITY_API_URL_ID, billingDer.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BillingDer> billingDerList = billingDerRepository.findAll();
        assertThat(billingDerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
