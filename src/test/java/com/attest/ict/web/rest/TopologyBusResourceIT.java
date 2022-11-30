package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Network;
import com.attest.ict.domain.Topology;
import com.attest.ict.domain.TopologyBus;
import com.attest.ict.repository.TopologyBusRepository;
import com.attest.ict.service.criteria.TopologyBusCriteria;
import com.attest.ict.service.dto.TopologyBusDTO;
import com.attest.ict.service.mapper.TopologyBusMapper;
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
 * Integration tests for the {@link TopologyBusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TopologyBusResourceIT {

    private static final String DEFAULT_POWER_LINE_BRANCH = "AAAAAAAAAA";
    private static final String UPDATED_POWER_LINE_BRANCH = "BBBBBBBBBB";

    private static final String DEFAULT_BUS_NAME_1 = "AAAAAAAAAA";
    private static final String UPDATED_BUS_NAME_1 = "BBBBBBBBBB";

    private static final String DEFAULT_BUS_NAME_2 = "AAAAAAAAAA";
    private static final String UPDATED_BUS_NAME_2 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/topology-buses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TopologyBusRepository topologyBusRepository;

    @Autowired
    private TopologyBusMapper topologyBusMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTopologyBusMockMvc;

    private TopologyBus topologyBus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TopologyBus createEntity(EntityManager em) {
        TopologyBus topologyBus = new TopologyBus()
            .powerLineBranch(DEFAULT_POWER_LINE_BRANCH)
            .busName1(DEFAULT_BUS_NAME_1)
            .busName2(DEFAULT_BUS_NAME_2);
        return topologyBus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TopologyBus createUpdatedEntity(EntityManager em) {
        TopologyBus topologyBus = new TopologyBus()
            .powerLineBranch(UPDATED_POWER_LINE_BRANCH)
            .busName1(UPDATED_BUS_NAME_1)
            .busName2(UPDATED_BUS_NAME_2);
        return topologyBus;
    }

    @BeforeEach
    public void initTest() {
        topologyBus = createEntity(em);
    }

    @Test
    @Transactional
    void createTopologyBus() throws Exception {
        int databaseSizeBeforeCreate = topologyBusRepository.findAll().size();
        // Create the TopologyBus
        TopologyBusDTO topologyBusDTO = topologyBusMapper.toDto(topologyBus);
        restTopologyBusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(topologyBusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TopologyBus in the database
        List<TopologyBus> topologyBusList = topologyBusRepository.findAll();
        assertThat(topologyBusList).hasSize(databaseSizeBeforeCreate + 1);
        TopologyBus testTopologyBus = topologyBusList.get(topologyBusList.size() - 1);
        assertThat(testTopologyBus.getPowerLineBranch()).isEqualTo(DEFAULT_POWER_LINE_BRANCH);
        assertThat(testTopologyBus.getBusName1()).isEqualTo(DEFAULT_BUS_NAME_1);
        assertThat(testTopologyBus.getBusName2()).isEqualTo(DEFAULT_BUS_NAME_2);
    }

    @Test
    @Transactional
    void createTopologyBusWithExistingId() throws Exception {
        // Create the TopologyBus with an existing ID
        topologyBus.setId(1L);
        TopologyBusDTO topologyBusDTO = topologyBusMapper.toDto(topologyBus);

        int databaseSizeBeforeCreate = topologyBusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTopologyBusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(topologyBusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TopologyBus in the database
        List<TopologyBus> topologyBusList = topologyBusRepository.findAll();
        assertThat(topologyBusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTopologyBuses() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        // Get all the topologyBusList
        restTopologyBusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topologyBus.getId().intValue())))
            .andExpect(jsonPath("$.[*].powerLineBranch").value(hasItem(DEFAULT_POWER_LINE_BRANCH)))
            .andExpect(jsonPath("$.[*].busName1").value(hasItem(DEFAULT_BUS_NAME_1)))
            .andExpect(jsonPath("$.[*].busName2").value(hasItem(DEFAULT_BUS_NAME_2)));
    }

    @Test
    @Transactional
    void getTopologyBus() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        // Get the topologyBus
        restTopologyBusMockMvc
            .perform(get(ENTITY_API_URL_ID, topologyBus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(topologyBus.getId().intValue()))
            .andExpect(jsonPath("$.powerLineBranch").value(DEFAULT_POWER_LINE_BRANCH))
            .andExpect(jsonPath("$.busName1").value(DEFAULT_BUS_NAME_1))
            .andExpect(jsonPath("$.busName2").value(DEFAULT_BUS_NAME_2));
    }

    @Test
    @Transactional
    void getTopologyBusesByIdFiltering() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        Long id = topologyBus.getId();

        defaultTopologyBusShouldBeFound("id.equals=" + id);
        defaultTopologyBusShouldNotBeFound("id.notEquals=" + id);

        defaultTopologyBusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTopologyBusShouldNotBeFound("id.greaterThan=" + id);

        defaultTopologyBusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTopologyBusShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTopologyBusesByPowerLineBranchIsEqualToSomething() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        // Get all the topologyBusList where powerLineBranch equals to DEFAULT_POWER_LINE_BRANCH
        defaultTopologyBusShouldBeFound("powerLineBranch.equals=" + DEFAULT_POWER_LINE_BRANCH);

        // Get all the topologyBusList where powerLineBranch equals to UPDATED_POWER_LINE_BRANCH
        defaultTopologyBusShouldNotBeFound("powerLineBranch.equals=" + UPDATED_POWER_LINE_BRANCH);
    }

    @Test
    @Transactional
    void getAllTopologyBusesByPowerLineBranchIsNotEqualToSomething() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        // Get all the topologyBusList where powerLineBranch not equals to DEFAULT_POWER_LINE_BRANCH
        defaultTopologyBusShouldNotBeFound("powerLineBranch.notEquals=" + DEFAULT_POWER_LINE_BRANCH);

        // Get all the topologyBusList where powerLineBranch not equals to UPDATED_POWER_LINE_BRANCH
        defaultTopologyBusShouldBeFound("powerLineBranch.notEquals=" + UPDATED_POWER_LINE_BRANCH);
    }

    @Test
    @Transactional
    void getAllTopologyBusesByPowerLineBranchIsInShouldWork() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        // Get all the topologyBusList where powerLineBranch in DEFAULT_POWER_LINE_BRANCH or UPDATED_POWER_LINE_BRANCH
        defaultTopologyBusShouldBeFound("powerLineBranch.in=" + DEFAULT_POWER_LINE_BRANCH + "," + UPDATED_POWER_LINE_BRANCH);

        // Get all the topologyBusList where powerLineBranch equals to UPDATED_POWER_LINE_BRANCH
        defaultTopologyBusShouldNotBeFound("powerLineBranch.in=" + UPDATED_POWER_LINE_BRANCH);
    }

    @Test
    @Transactional
    void getAllTopologyBusesByPowerLineBranchIsNullOrNotNull() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        // Get all the topologyBusList where powerLineBranch is not null
        defaultTopologyBusShouldBeFound("powerLineBranch.specified=true");

        // Get all the topologyBusList where powerLineBranch is null
        defaultTopologyBusShouldNotBeFound("powerLineBranch.specified=false");
    }

    @Test
    @Transactional
    void getAllTopologyBusesByPowerLineBranchContainsSomething() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        // Get all the topologyBusList where powerLineBranch contains DEFAULT_POWER_LINE_BRANCH
        defaultTopologyBusShouldBeFound("powerLineBranch.contains=" + DEFAULT_POWER_LINE_BRANCH);

        // Get all the topologyBusList where powerLineBranch contains UPDATED_POWER_LINE_BRANCH
        defaultTopologyBusShouldNotBeFound("powerLineBranch.contains=" + UPDATED_POWER_LINE_BRANCH);
    }

    @Test
    @Transactional
    void getAllTopologyBusesByPowerLineBranchNotContainsSomething() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        // Get all the topologyBusList where powerLineBranch does not contain DEFAULT_POWER_LINE_BRANCH
        defaultTopologyBusShouldNotBeFound("powerLineBranch.doesNotContain=" + DEFAULT_POWER_LINE_BRANCH);

        // Get all the topologyBusList where powerLineBranch does not contain UPDATED_POWER_LINE_BRANCH
        defaultTopologyBusShouldBeFound("powerLineBranch.doesNotContain=" + UPDATED_POWER_LINE_BRANCH);
    }

    @Test
    @Transactional
    void getAllTopologyBusesByBusName1IsEqualToSomething() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        // Get all the topologyBusList where busName1 equals to DEFAULT_BUS_NAME_1
        defaultTopologyBusShouldBeFound("busName1.equals=" + DEFAULT_BUS_NAME_1);

        // Get all the topologyBusList where busName1 equals to UPDATED_BUS_NAME_1
        defaultTopologyBusShouldNotBeFound("busName1.equals=" + UPDATED_BUS_NAME_1);
    }

    @Test
    @Transactional
    void getAllTopologyBusesByBusName1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        // Get all the topologyBusList where busName1 not equals to DEFAULT_BUS_NAME_1
        defaultTopologyBusShouldNotBeFound("busName1.notEquals=" + DEFAULT_BUS_NAME_1);

        // Get all the topologyBusList where busName1 not equals to UPDATED_BUS_NAME_1
        defaultTopologyBusShouldBeFound("busName1.notEquals=" + UPDATED_BUS_NAME_1);
    }

    @Test
    @Transactional
    void getAllTopologyBusesByBusName1IsInShouldWork() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        // Get all the topologyBusList where busName1 in DEFAULT_BUS_NAME_1 or UPDATED_BUS_NAME_1
        defaultTopologyBusShouldBeFound("busName1.in=" + DEFAULT_BUS_NAME_1 + "," + UPDATED_BUS_NAME_1);

        // Get all the topologyBusList where busName1 equals to UPDATED_BUS_NAME_1
        defaultTopologyBusShouldNotBeFound("busName1.in=" + UPDATED_BUS_NAME_1);
    }

    @Test
    @Transactional
    void getAllTopologyBusesByBusName1IsNullOrNotNull() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        // Get all the topologyBusList where busName1 is not null
        defaultTopologyBusShouldBeFound("busName1.specified=true");

        // Get all the topologyBusList where busName1 is null
        defaultTopologyBusShouldNotBeFound("busName1.specified=false");
    }

    @Test
    @Transactional
    void getAllTopologyBusesByBusName1ContainsSomething() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        // Get all the topologyBusList where busName1 contains DEFAULT_BUS_NAME_1
        defaultTopologyBusShouldBeFound("busName1.contains=" + DEFAULT_BUS_NAME_1);

        // Get all the topologyBusList where busName1 contains UPDATED_BUS_NAME_1
        defaultTopologyBusShouldNotBeFound("busName1.contains=" + UPDATED_BUS_NAME_1);
    }

    @Test
    @Transactional
    void getAllTopologyBusesByBusName1NotContainsSomething() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        // Get all the topologyBusList where busName1 does not contain DEFAULT_BUS_NAME_1
        defaultTopologyBusShouldNotBeFound("busName1.doesNotContain=" + DEFAULT_BUS_NAME_1);

        // Get all the topologyBusList where busName1 does not contain UPDATED_BUS_NAME_1
        defaultTopologyBusShouldBeFound("busName1.doesNotContain=" + UPDATED_BUS_NAME_1);
    }

    @Test
    @Transactional
    void getAllTopologyBusesByBusName2IsEqualToSomething() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        // Get all the topologyBusList where busName2 equals to DEFAULT_BUS_NAME_2
        defaultTopologyBusShouldBeFound("busName2.equals=" + DEFAULT_BUS_NAME_2);

        // Get all the topologyBusList where busName2 equals to UPDATED_BUS_NAME_2
        defaultTopologyBusShouldNotBeFound("busName2.equals=" + UPDATED_BUS_NAME_2);
    }

    @Test
    @Transactional
    void getAllTopologyBusesByBusName2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        // Get all the topologyBusList where busName2 not equals to DEFAULT_BUS_NAME_2
        defaultTopologyBusShouldNotBeFound("busName2.notEquals=" + DEFAULT_BUS_NAME_2);

        // Get all the topologyBusList where busName2 not equals to UPDATED_BUS_NAME_2
        defaultTopologyBusShouldBeFound("busName2.notEquals=" + UPDATED_BUS_NAME_2);
    }

    @Test
    @Transactional
    void getAllTopologyBusesByBusName2IsInShouldWork() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        // Get all the topologyBusList where busName2 in DEFAULT_BUS_NAME_2 or UPDATED_BUS_NAME_2
        defaultTopologyBusShouldBeFound("busName2.in=" + DEFAULT_BUS_NAME_2 + "," + UPDATED_BUS_NAME_2);

        // Get all the topologyBusList where busName2 equals to UPDATED_BUS_NAME_2
        defaultTopologyBusShouldNotBeFound("busName2.in=" + UPDATED_BUS_NAME_2);
    }

    @Test
    @Transactional
    void getAllTopologyBusesByBusName2IsNullOrNotNull() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        // Get all the topologyBusList where busName2 is not null
        defaultTopologyBusShouldBeFound("busName2.specified=true");

        // Get all the topologyBusList where busName2 is null
        defaultTopologyBusShouldNotBeFound("busName2.specified=false");
    }

    @Test
    @Transactional
    void getAllTopologyBusesByBusName2ContainsSomething() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        // Get all the topologyBusList where busName2 contains DEFAULT_BUS_NAME_2
        defaultTopologyBusShouldBeFound("busName2.contains=" + DEFAULT_BUS_NAME_2);

        // Get all the topologyBusList where busName2 contains UPDATED_BUS_NAME_2
        defaultTopologyBusShouldNotBeFound("busName2.contains=" + UPDATED_BUS_NAME_2);
    }

    @Test
    @Transactional
    void getAllTopologyBusesByBusName2NotContainsSomething() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        // Get all the topologyBusList where busName2 does not contain DEFAULT_BUS_NAME_2
        defaultTopologyBusShouldNotBeFound("busName2.doesNotContain=" + DEFAULT_BUS_NAME_2);

        // Get all the topologyBusList where busName2 does not contain UPDATED_BUS_NAME_2
        defaultTopologyBusShouldBeFound("busName2.doesNotContain=" + UPDATED_BUS_NAME_2);
    }

    @Test
    @Transactional
    void getAllTopologyBusesByTopologyIsEqualToSomething() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);
        Topology topology;
        if (TestUtil.findAll(em, Topology.class).isEmpty()) {
            topology = TopologyResourceIT.createEntity(em);
            em.persist(topology);
            em.flush();
        } else {
            topology = TestUtil.findAll(em, Topology.class).get(0);
        }
        em.persist(topology);
        em.flush();
        topologyBus.addTopology(topology);
        topologyBusRepository.saveAndFlush(topologyBus);
        Long topologyId = topology.getId();

        // Get all the topologyBusList where topology equals to topologyId
        defaultTopologyBusShouldBeFound("topologyId.equals=" + topologyId);

        // Get all the topologyBusList where topology equals to (topologyId + 1)
        defaultTopologyBusShouldNotBeFound("topologyId.equals=" + (topologyId + 1));
    }

    @Test
    @Transactional
    void getAllTopologyBusesByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);
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
        topologyBus.setNetwork(network);
        topologyBusRepository.saveAndFlush(topologyBus);
        Long networkId = network.getId();

        // Get all the topologyBusList where network equals to networkId
        defaultTopologyBusShouldBeFound("networkId.equals=" + networkId);

        // Get all the topologyBusList where network equals to (networkId + 1)
        defaultTopologyBusShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTopologyBusShouldBeFound(String filter) throws Exception {
        restTopologyBusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topologyBus.getId().intValue())))
            .andExpect(jsonPath("$.[*].powerLineBranch").value(hasItem(DEFAULT_POWER_LINE_BRANCH)))
            .andExpect(jsonPath("$.[*].busName1").value(hasItem(DEFAULT_BUS_NAME_1)))
            .andExpect(jsonPath("$.[*].busName2").value(hasItem(DEFAULT_BUS_NAME_2)));

        // Check, that the count call also returns 1
        restTopologyBusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTopologyBusShouldNotBeFound(String filter) throws Exception {
        restTopologyBusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTopologyBusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTopologyBus() throws Exception {
        // Get the topologyBus
        restTopologyBusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTopologyBus() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        int databaseSizeBeforeUpdate = topologyBusRepository.findAll().size();

        // Update the topologyBus
        TopologyBus updatedTopologyBus = topologyBusRepository.findById(topologyBus.getId()).get();
        // Disconnect from session so that the updates on updatedTopologyBus are not directly saved in db
        em.detach(updatedTopologyBus);
        updatedTopologyBus.powerLineBranch(UPDATED_POWER_LINE_BRANCH).busName1(UPDATED_BUS_NAME_1).busName2(UPDATED_BUS_NAME_2);
        TopologyBusDTO topologyBusDTO = topologyBusMapper.toDto(updatedTopologyBus);

        restTopologyBusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, topologyBusDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(topologyBusDTO))
            )
            .andExpect(status().isOk());

        // Validate the TopologyBus in the database
        List<TopologyBus> topologyBusList = topologyBusRepository.findAll();
        assertThat(topologyBusList).hasSize(databaseSizeBeforeUpdate);
        TopologyBus testTopologyBus = topologyBusList.get(topologyBusList.size() - 1);
        assertThat(testTopologyBus.getPowerLineBranch()).isEqualTo(UPDATED_POWER_LINE_BRANCH);
        assertThat(testTopologyBus.getBusName1()).isEqualTo(UPDATED_BUS_NAME_1);
        assertThat(testTopologyBus.getBusName2()).isEqualTo(UPDATED_BUS_NAME_2);
    }

    @Test
    @Transactional
    void putNonExistingTopologyBus() throws Exception {
        int databaseSizeBeforeUpdate = topologyBusRepository.findAll().size();
        topologyBus.setId(count.incrementAndGet());

        // Create the TopologyBus
        TopologyBusDTO topologyBusDTO = topologyBusMapper.toDto(topologyBus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTopologyBusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, topologyBusDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(topologyBusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TopologyBus in the database
        List<TopologyBus> topologyBusList = topologyBusRepository.findAll();
        assertThat(topologyBusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTopologyBus() throws Exception {
        int databaseSizeBeforeUpdate = topologyBusRepository.findAll().size();
        topologyBus.setId(count.incrementAndGet());

        // Create the TopologyBus
        TopologyBusDTO topologyBusDTO = topologyBusMapper.toDto(topologyBus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopologyBusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(topologyBusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TopologyBus in the database
        List<TopologyBus> topologyBusList = topologyBusRepository.findAll();
        assertThat(topologyBusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTopologyBus() throws Exception {
        int databaseSizeBeforeUpdate = topologyBusRepository.findAll().size();
        topologyBus.setId(count.incrementAndGet());

        // Create the TopologyBus
        TopologyBusDTO topologyBusDTO = topologyBusMapper.toDto(topologyBus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopologyBusMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(topologyBusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TopologyBus in the database
        List<TopologyBus> topologyBusList = topologyBusRepository.findAll();
        assertThat(topologyBusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTopologyBusWithPatch() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        int databaseSizeBeforeUpdate = topologyBusRepository.findAll().size();

        // Update the topologyBus using partial update
        TopologyBus partialUpdatedTopologyBus = new TopologyBus();
        partialUpdatedTopologyBus.setId(topologyBus.getId());

        partialUpdatedTopologyBus.powerLineBranch(UPDATED_POWER_LINE_BRANCH).busName1(UPDATED_BUS_NAME_1);

        restTopologyBusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTopologyBus.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTopologyBus))
            )
            .andExpect(status().isOk());

        // Validate the TopologyBus in the database
        List<TopologyBus> topologyBusList = topologyBusRepository.findAll();
        assertThat(topologyBusList).hasSize(databaseSizeBeforeUpdate);
        TopologyBus testTopologyBus = topologyBusList.get(topologyBusList.size() - 1);
        assertThat(testTopologyBus.getPowerLineBranch()).isEqualTo(UPDATED_POWER_LINE_BRANCH);
        assertThat(testTopologyBus.getBusName1()).isEqualTo(UPDATED_BUS_NAME_1);
        assertThat(testTopologyBus.getBusName2()).isEqualTo(DEFAULT_BUS_NAME_2);
    }

    @Test
    @Transactional
    void fullUpdateTopologyBusWithPatch() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        int databaseSizeBeforeUpdate = topologyBusRepository.findAll().size();

        // Update the topologyBus using partial update
        TopologyBus partialUpdatedTopologyBus = new TopologyBus();
        partialUpdatedTopologyBus.setId(topologyBus.getId());

        partialUpdatedTopologyBus.powerLineBranch(UPDATED_POWER_LINE_BRANCH).busName1(UPDATED_BUS_NAME_1).busName2(UPDATED_BUS_NAME_2);

        restTopologyBusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTopologyBus.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTopologyBus))
            )
            .andExpect(status().isOk());

        // Validate the TopologyBus in the database
        List<TopologyBus> topologyBusList = topologyBusRepository.findAll();
        assertThat(topologyBusList).hasSize(databaseSizeBeforeUpdate);
        TopologyBus testTopologyBus = topologyBusList.get(topologyBusList.size() - 1);
        assertThat(testTopologyBus.getPowerLineBranch()).isEqualTo(UPDATED_POWER_LINE_BRANCH);
        assertThat(testTopologyBus.getBusName1()).isEqualTo(UPDATED_BUS_NAME_1);
        assertThat(testTopologyBus.getBusName2()).isEqualTo(UPDATED_BUS_NAME_2);
    }

    @Test
    @Transactional
    void patchNonExistingTopologyBus() throws Exception {
        int databaseSizeBeforeUpdate = topologyBusRepository.findAll().size();
        topologyBus.setId(count.incrementAndGet());

        // Create the TopologyBus
        TopologyBusDTO topologyBusDTO = topologyBusMapper.toDto(topologyBus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTopologyBusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, topologyBusDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(topologyBusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TopologyBus in the database
        List<TopologyBus> topologyBusList = topologyBusRepository.findAll();
        assertThat(topologyBusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTopologyBus() throws Exception {
        int databaseSizeBeforeUpdate = topologyBusRepository.findAll().size();
        topologyBus.setId(count.incrementAndGet());

        // Create the TopologyBus
        TopologyBusDTO topologyBusDTO = topologyBusMapper.toDto(topologyBus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopologyBusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(topologyBusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TopologyBus in the database
        List<TopologyBus> topologyBusList = topologyBusRepository.findAll();
        assertThat(topologyBusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTopologyBus() throws Exception {
        int databaseSizeBeforeUpdate = topologyBusRepository.findAll().size();
        topologyBus.setId(count.incrementAndGet());

        // Create the TopologyBus
        TopologyBusDTO topologyBusDTO = topologyBusMapper.toDto(topologyBus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopologyBusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(topologyBusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TopologyBus in the database
        List<TopologyBus> topologyBusList = topologyBusRepository.findAll();
        assertThat(topologyBusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTopologyBus() throws Exception {
        // Initialize the database
        topologyBusRepository.saveAndFlush(topologyBus);

        int databaseSizeBeforeDelete = topologyBusRepository.findAll().size();

        // Delete the topologyBus
        restTopologyBusMockMvc
            .perform(delete(ENTITY_API_URL_ID, topologyBus.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TopologyBus> topologyBusList = topologyBusRepository.findAll();
        assertThat(topologyBusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
