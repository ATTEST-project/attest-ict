package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Network;
import com.attest.ict.domain.VoltageLevel;
import com.attest.ict.repository.VoltageLevelRepository;
import com.attest.ict.service.criteria.VoltageLevelCriteria;
import com.attest.ict.service.dto.VoltageLevelDTO;
import com.attest.ict.service.mapper.VoltageLevelMapper;
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
 * Integration tests for the {@link VoltageLevelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VoltageLevelResourceIT {

    private static final Double DEFAULT_V_1 = 1D;
    private static final Double UPDATED_V_1 = 2D;
    private static final Double SMALLER_V_1 = 1D - 1D;

    private static final Double DEFAULT_V_2 = 1D;
    private static final Double UPDATED_V_2 = 2D;
    private static final Double SMALLER_V_2 = 1D - 1D;

    private static final Double DEFAULT_V_3 = 1D;
    private static final Double UPDATED_V_3 = 2D;
    private static final Double SMALLER_V_3 = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/voltage-levels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VoltageLevelRepository voltageLevelRepository;

    @Autowired
    private VoltageLevelMapper voltageLevelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVoltageLevelMockMvc;

    private VoltageLevel voltageLevel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VoltageLevel createEntity(EntityManager em) {
        VoltageLevel voltageLevel = new VoltageLevel().v1(DEFAULT_V_1).v2(DEFAULT_V_2).v3(DEFAULT_V_3);
        return voltageLevel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VoltageLevel createUpdatedEntity(EntityManager em) {
        VoltageLevel voltageLevel = new VoltageLevel().v1(UPDATED_V_1).v2(UPDATED_V_2).v3(UPDATED_V_3);
        return voltageLevel;
    }

    @BeforeEach
    public void initTest() {
        voltageLevel = createEntity(em);
    }

    @Test
    @Transactional
    void createVoltageLevel() throws Exception {
        int databaseSizeBeforeCreate = voltageLevelRepository.findAll().size();
        // Create the VoltageLevel
        VoltageLevelDTO voltageLevelDTO = voltageLevelMapper.toDto(voltageLevel);
        restVoltageLevelMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voltageLevelDTO))
            )
            .andExpect(status().isCreated());

        // Validate the VoltageLevel in the database
        List<VoltageLevel> voltageLevelList = voltageLevelRepository.findAll();
        assertThat(voltageLevelList).hasSize(databaseSizeBeforeCreate + 1);
        VoltageLevel testVoltageLevel = voltageLevelList.get(voltageLevelList.size() - 1);
        assertThat(testVoltageLevel.getv1()).isEqualTo(DEFAULT_V_1);
        assertThat(testVoltageLevel.getv2()).isEqualTo(DEFAULT_V_2);
        assertThat(testVoltageLevel.getv3()).isEqualTo(DEFAULT_V_3);
    }

    @Test
    @Transactional
    void createVoltageLevelWithExistingId() throws Exception {
        // Create the VoltageLevel with an existing ID
        voltageLevel.setId(1L);
        VoltageLevelDTO voltageLevelDTO = voltageLevelMapper.toDto(voltageLevel);

        int databaseSizeBeforeCreate = voltageLevelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoltageLevelMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voltageLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoltageLevel in the database
        List<VoltageLevel> voltageLevelList = voltageLevelRepository.findAll();
        assertThat(voltageLevelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVoltageLevels() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList
        restVoltageLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voltageLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].v1").value(hasItem(DEFAULT_V_1.doubleValue())))
            .andExpect(jsonPath("$.[*].v2").value(hasItem(DEFAULT_V_2.doubleValue())))
            .andExpect(jsonPath("$.[*].v3").value(hasItem(DEFAULT_V_3.doubleValue())));
    }

    @Test
    @Transactional
    void getVoltageLevel() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get the voltageLevel
        restVoltageLevelMockMvc
            .perform(get(ENTITY_API_URL_ID, voltageLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(voltageLevel.getId().intValue()))
            .andExpect(jsonPath("$.v1").value(DEFAULT_V_1.doubleValue()))
            .andExpect(jsonPath("$.v2").value(DEFAULT_V_2.doubleValue()))
            .andExpect(jsonPath("$.v3").value(DEFAULT_V_3.doubleValue()));
    }

    @Test
    @Transactional
    void getVoltageLevelsByIdFiltering() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        Long id = voltageLevel.getId();

        defaultVoltageLevelShouldBeFound("id.equals=" + id);
        defaultVoltageLevelShouldNotBeFound("id.notEquals=" + id);

        defaultVoltageLevelShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVoltageLevelShouldNotBeFound("id.greaterThan=" + id);

        defaultVoltageLevelShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVoltageLevelShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv1IsEqualToSomething() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v1 equals to DEFAULT_V_1
        defaultVoltageLevelShouldBeFound("v1.equals=" + DEFAULT_V_1);

        // Get all the voltageLevelList where v1 equals to UPDATED_V_1
        defaultVoltageLevelShouldNotBeFound("v1.equals=" + UPDATED_V_1);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v1 not equals to DEFAULT_V_1
        defaultVoltageLevelShouldNotBeFound("v1.notEquals=" + DEFAULT_V_1);

        // Get all the voltageLevelList where v1 not equals to UPDATED_V_1
        defaultVoltageLevelShouldBeFound("v1.notEquals=" + UPDATED_V_1);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv1IsInShouldWork() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v1 in DEFAULT_V_1 or UPDATED_V_1
        defaultVoltageLevelShouldBeFound("v1.in=" + DEFAULT_V_1 + "," + UPDATED_V_1);

        // Get all the voltageLevelList where v1 equals to UPDATED_V_1
        defaultVoltageLevelShouldNotBeFound("v1.in=" + UPDATED_V_1);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv1IsNullOrNotNull() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v1 is not null
        defaultVoltageLevelShouldBeFound("v1.specified=true");

        // Get all the voltageLevelList where v1 is null
        defaultVoltageLevelShouldNotBeFound("v1.specified=false");
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv1IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v1 is greater than or equal to DEFAULT_V_1
        defaultVoltageLevelShouldBeFound("v1.greaterThanOrEqual=" + DEFAULT_V_1);

        // Get all the voltageLevelList where v1 is greater than or equal to UPDATED_V_1
        defaultVoltageLevelShouldNotBeFound("v1.greaterThanOrEqual=" + UPDATED_V_1);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv1IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v1 is less than or equal to DEFAULT_V_1
        defaultVoltageLevelShouldBeFound("v1.lessThanOrEqual=" + DEFAULT_V_1);

        // Get all the voltageLevelList where v1 is less than or equal to SMALLER_V_1
        defaultVoltageLevelShouldNotBeFound("v1.lessThanOrEqual=" + SMALLER_V_1);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv1IsLessThanSomething() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v1 is less than DEFAULT_V_1
        defaultVoltageLevelShouldNotBeFound("v1.lessThan=" + DEFAULT_V_1);

        // Get all the voltageLevelList where v1 is less than UPDATED_V_1
        defaultVoltageLevelShouldBeFound("v1.lessThan=" + UPDATED_V_1);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv1IsGreaterThanSomething() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v1 is greater than DEFAULT_V_1
        defaultVoltageLevelShouldNotBeFound("v1.greaterThan=" + DEFAULT_V_1);

        // Get all the voltageLevelList where v1 is greater than SMALLER_V_1
        defaultVoltageLevelShouldBeFound("v1.greaterThan=" + SMALLER_V_1);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv2IsEqualToSomething() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v2 equals to DEFAULT_V_2
        defaultVoltageLevelShouldBeFound("v2.equals=" + DEFAULT_V_2);

        // Get all the voltageLevelList where v2 equals to UPDATED_V_2
        defaultVoltageLevelShouldNotBeFound("v2.equals=" + UPDATED_V_2);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v2 not equals to DEFAULT_V_2
        defaultVoltageLevelShouldNotBeFound("v2.notEquals=" + DEFAULT_V_2);

        // Get all the voltageLevelList where v2 not equals to UPDATED_V_2
        defaultVoltageLevelShouldBeFound("v2.notEquals=" + UPDATED_V_2);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv2IsInShouldWork() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v2 in DEFAULT_V_2 or UPDATED_V_2
        defaultVoltageLevelShouldBeFound("v2.in=" + DEFAULT_V_2 + "," + UPDATED_V_2);

        // Get all the voltageLevelList where v2 equals to UPDATED_V_2
        defaultVoltageLevelShouldNotBeFound("v2.in=" + UPDATED_V_2);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv2IsNullOrNotNull() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v2 is not null
        defaultVoltageLevelShouldBeFound("v2.specified=true");

        // Get all the voltageLevelList where v2 is null
        defaultVoltageLevelShouldNotBeFound("v2.specified=false");
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv2IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v2 is greater than or equal to DEFAULT_V_2
        defaultVoltageLevelShouldBeFound("v2.greaterThanOrEqual=" + DEFAULT_V_2);

        // Get all the voltageLevelList where v2 is greater than or equal to UPDATED_V_2
        defaultVoltageLevelShouldNotBeFound("v2.greaterThanOrEqual=" + UPDATED_V_2);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv2IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v2 is less than or equal to DEFAULT_V_2
        defaultVoltageLevelShouldBeFound("v2.lessThanOrEqual=" + DEFAULT_V_2);

        // Get all the voltageLevelList where v2 is less than or equal to SMALLER_V_2
        defaultVoltageLevelShouldNotBeFound("v2.lessThanOrEqual=" + SMALLER_V_2);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv2IsLessThanSomething() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v2 is less than DEFAULT_V_2
        defaultVoltageLevelShouldNotBeFound("v2.lessThan=" + DEFAULT_V_2);

        // Get all the voltageLevelList where v2 is less than UPDATED_V_2
        defaultVoltageLevelShouldBeFound("v2.lessThan=" + UPDATED_V_2);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv2IsGreaterThanSomething() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v2 is greater than DEFAULT_V_2
        defaultVoltageLevelShouldNotBeFound("v2.greaterThan=" + DEFAULT_V_2);

        // Get all the voltageLevelList where v2 is greater than SMALLER_V_2
        defaultVoltageLevelShouldBeFound("v2.greaterThan=" + SMALLER_V_2);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv3IsEqualToSomething() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v3 equals to DEFAULT_V_3
        defaultVoltageLevelShouldBeFound("v3.equals=" + DEFAULT_V_3);

        // Get all the voltageLevelList where v3 equals to UPDATED_V_3
        defaultVoltageLevelShouldNotBeFound("v3.equals=" + UPDATED_V_3);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv3IsNotEqualToSomething() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v3 not equals to DEFAULT_V_3
        defaultVoltageLevelShouldNotBeFound("v3.notEquals=" + DEFAULT_V_3);

        // Get all the voltageLevelList where v3 not equals to UPDATED_V_3
        defaultVoltageLevelShouldBeFound("v3.notEquals=" + UPDATED_V_3);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv3IsInShouldWork() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v3 in DEFAULT_V_3 or UPDATED_V_3
        defaultVoltageLevelShouldBeFound("v3.in=" + DEFAULT_V_3 + "," + UPDATED_V_3);

        // Get all the voltageLevelList where v3 equals to UPDATED_V_3
        defaultVoltageLevelShouldNotBeFound("v3.in=" + UPDATED_V_3);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv3IsNullOrNotNull() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v3 is not null
        defaultVoltageLevelShouldBeFound("v3.specified=true");

        // Get all the voltageLevelList where v3 is null
        defaultVoltageLevelShouldNotBeFound("v3.specified=false");
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv3IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v3 is greater than or equal to DEFAULT_V_3
        defaultVoltageLevelShouldBeFound("v3.greaterThanOrEqual=" + DEFAULT_V_3);

        // Get all the voltageLevelList where v3 is greater than or equal to UPDATED_V_3
        defaultVoltageLevelShouldNotBeFound("v3.greaterThanOrEqual=" + UPDATED_V_3);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv3IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v3 is less than or equal to DEFAULT_V_3
        defaultVoltageLevelShouldBeFound("v3.lessThanOrEqual=" + DEFAULT_V_3);

        // Get all the voltageLevelList where v3 is less than or equal to SMALLER_V_3
        defaultVoltageLevelShouldNotBeFound("v3.lessThanOrEqual=" + SMALLER_V_3);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv3IsLessThanSomething() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v3 is less than DEFAULT_V_3
        defaultVoltageLevelShouldNotBeFound("v3.lessThan=" + DEFAULT_V_3);

        // Get all the voltageLevelList where v3 is less than UPDATED_V_3
        defaultVoltageLevelShouldBeFound("v3.lessThan=" + UPDATED_V_3);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByv3IsGreaterThanSomething() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        // Get all the voltageLevelList where v3 is greater than DEFAULT_V_3
        defaultVoltageLevelShouldNotBeFound("v3.greaterThan=" + DEFAULT_V_3);

        // Get all the voltageLevelList where v3 is greater than SMALLER_V_3
        defaultVoltageLevelShouldBeFound("v3.greaterThan=" + SMALLER_V_3);
    }

    @Test
    @Transactional
    void getAllVoltageLevelsByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);
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
        voltageLevel.setNetwork(network);
        voltageLevelRepository.saveAndFlush(voltageLevel);
        Long networkId = network.getId();

        // Get all the voltageLevelList where network equals to networkId
        defaultVoltageLevelShouldBeFound("networkId.equals=" + networkId);

        // Get all the voltageLevelList where network equals to (networkId + 1)
        defaultVoltageLevelShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVoltageLevelShouldBeFound(String filter) throws Exception {
        restVoltageLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voltageLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].v1").value(hasItem(DEFAULT_V_1.doubleValue())))
            .andExpect(jsonPath("$.[*].v2").value(hasItem(DEFAULT_V_2.doubleValue())))
            .andExpect(jsonPath("$.[*].v3").value(hasItem(DEFAULT_V_3.doubleValue())));

        // Check, that the count call also returns 1
        restVoltageLevelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVoltageLevelShouldNotBeFound(String filter) throws Exception {
        restVoltageLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVoltageLevelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVoltageLevel() throws Exception {
        // Get the voltageLevel
        restVoltageLevelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVoltageLevel() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        int databaseSizeBeforeUpdate = voltageLevelRepository.findAll().size();

        // Update the voltageLevel
        VoltageLevel updatedVoltageLevel = voltageLevelRepository.findById(voltageLevel.getId()).get();
        // Disconnect from session so that the updates on updatedVoltageLevel are not directly saved in db
        em.detach(updatedVoltageLevel);
        updatedVoltageLevel.v1(UPDATED_V_1).v2(UPDATED_V_2).v3(UPDATED_V_3);
        VoltageLevelDTO voltageLevelDTO = voltageLevelMapper.toDto(updatedVoltageLevel);

        restVoltageLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, voltageLevelDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voltageLevelDTO))
            )
            .andExpect(status().isOk());

        // Validate the VoltageLevel in the database
        List<VoltageLevel> voltageLevelList = voltageLevelRepository.findAll();
        assertThat(voltageLevelList).hasSize(databaseSizeBeforeUpdate);
        VoltageLevel testVoltageLevel = voltageLevelList.get(voltageLevelList.size() - 1);
        assertThat(testVoltageLevel.getv1()).isEqualTo(UPDATED_V_1);
        assertThat(testVoltageLevel.getv2()).isEqualTo(UPDATED_V_2);
        assertThat(testVoltageLevel.getv3()).isEqualTo(UPDATED_V_3);
    }

    @Test
    @Transactional
    void putNonExistingVoltageLevel() throws Exception {
        int databaseSizeBeforeUpdate = voltageLevelRepository.findAll().size();
        voltageLevel.setId(count.incrementAndGet());

        // Create the VoltageLevel
        VoltageLevelDTO voltageLevelDTO = voltageLevelMapper.toDto(voltageLevel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoltageLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, voltageLevelDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voltageLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoltageLevel in the database
        List<VoltageLevel> voltageLevelList = voltageLevelRepository.findAll();
        assertThat(voltageLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVoltageLevel() throws Exception {
        int databaseSizeBeforeUpdate = voltageLevelRepository.findAll().size();
        voltageLevel.setId(count.incrementAndGet());

        // Create the VoltageLevel
        VoltageLevelDTO voltageLevelDTO = voltageLevelMapper.toDto(voltageLevel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoltageLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voltageLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoltageLevel in the database
        List<VoltageLevel> voltageLevelList = voltageLevelRepository.findAll();
        assertThat(voltageLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVoltageLevel() throws Exception {
        int databaseSizeBeforeUpdate = voltageLevelRepository.findAll().size();
        voltageLevel.setId(count.incrementAndGet());

        // Create the VoltageLevel
        VoltageLevelDTO voltageLevelDTO = voltageLevelMapper.toDto(voltageLevel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoltageLevelMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voltageLevelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VoltageLevel in the database
        List<VoltageLevel> voltageLevelList = voltageLevelRepository.findAll();
        assertThat(voltageLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVoltageLevelWithPatch() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        int databaseSizeBeforeUpdate = voltageLevelRepository.findAll().size();

        // Update the voltageLevel using partial update
        VoltageLevel partialUpdatedVoltageLevel = new VoltageLevel();
        partialUpdatedVoltageLevel.setId(voltageLevel.getId());

        partialUpdatedVoltageLevel.v2(UPDATED_V_2);

        restVoltageLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoltageLevel.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoltageLevel))
            )
            .andExpect(status().isOk());

        // Validate the VoltageLevel in the database
        List<VoltageLevel> voltageLevelList = voltageLevelRepository.findAll();
        assertThat(voltageLevelList).hasSize(databaseSizeBeforeUpdate);
        VoltageLevel testVoltageLevel = voltageLevelList.get(voltageLevelList.size() - 1);
        assertThat(testVoltageLevel.getv1()).isEqualTo(DEFAULT_V_1);
        assertThat(testVoltageLevel.getv2()).isEqualTo(UPDATED_V_2);
        assertThat(testVoltageLevel.getv3()).isEqualTo(DEFAULT_V_3);
    }

    @Test
    @Transactional
    void fullUpdateVoltageLevelWithPatch() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        int databaseSizeBeforeUpdate = voltageLevelRepository.findAll().size();

        // Update the voltageLevel using partial update
        VoltageLevel partialUpdatedVoltageLevel = new VoltageLevel();
        partialUpdatedVoltageLevel.setId(voltageLevel.getId());

        partialUpdatedVoltageLevel.v1(UPDATED_V_1).v2(UPDATED_V_2).v3(UPDATED_V_3);

        restVoltageLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoltageLevel.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoltageLevel))
            )
            .andExpect(status().isOk());

        // Validate the VoltageLevel in the database
        List<VoltageLevel> voltageLevelList = voltageLevelRepository.findAll();
        assertThat(voltageLevelList).hasSize(databaseSizeBeforeUpdate);
        VoltageLevel testVoltageLevel = voltageLevelList.get(voltageLevelList.size() - 1);
        assertThat(testVoltageLevel.getv1()).isEqualTo(UPDATED_V_1);
        assertThat(testVoltageLevel.getv2()).isEqualTo(UPDATED_V_2);
        assertThat(testVoltageLevel.getv3()).isEqualTo(UPDATED_V_3);
    }

    @Test
    @Transactional
    void patchNonExistingVoltageLevel() throws Exception {
        int databaseSizeBeforeUpdate = voltageLevelRepository.findAll().size();
        voltageLevel.setId(count.incrementAndGet());

        // Create the VoltageLevel
        VoltageLevelDTO voltageLevelDTO = voltageLevelMapper.toDto(voltageLevel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoltageLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, voltageLevelDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voltageLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoltageLevel in the database
        List<VoltageLevel> voltageLevelList = voltageLevelRepository.findAll();
        assertThat(voltageLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVoltageLevel() throws Exception {
        int databaseSizeBeforeUpdate = voltageLevelRepository.findAll().size();
        voltageLevel.setId(count.incrementAndGet());

        // Create the VoltageLevel
        VoltageLevelDTO voltageLevelDTO = voltageLevelMapper.toDto(voltageLevel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoltageLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voltageLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoltageLevel in the database
        List<VoltageLevel> voltageLevelList = voltageLevelRepository.findAll();
        assertThat(voltageLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVoltageLevel() throws Exception {
        int databaseSizeBeforeUpdate = voltageLevelRepository.findAll().size();
        voltageLevel.setId(count.incrementAndGet());

        // Create the VoltageLevel
        VoltageLevelDTO voltageLevelDTO = voltageLevelMapper.toDto(voltageLevel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoltageLevelMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voltageLevelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VoltageLevel in the database
        List<VoltageLevel> voltageLevelList = voltageLevelRepository.findAll();
        assertThat(voltageLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVoltageLevel() throws Exception {
        // Initialize the database
        voltageLevelRepository.saveAndFlush(voltageLevel);

        int databaseSizeBeforeDelete = voltageLevelRepository.findAll().size();

        // Delete the voltageLevel
        restVoltageLevelMockMvc
            .perform(delete(ENTITY_API_URL_ID, voltageLevel.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VoltageLevel> voltageLevelList = voltageLevelRepository.findAll();
        assertThat(voltageLevelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
