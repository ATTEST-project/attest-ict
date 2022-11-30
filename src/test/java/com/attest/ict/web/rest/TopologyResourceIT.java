package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Topology;
import com.attest.ict.domain.TopologyBus;
import com.attest.ict.repository.TopologyRepository;
import com.attest.ict.service.criteria.TopologyCriteria;
import com.attest.ict.service.dto.TopologyDTO;
import com.attest.ict.service.mapper.TopologyMapper;
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
 * Integration tests for the {@link TopologyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TopologyResourceIT {

    private static final String DEFAULT_POWER_LINE_BRANCH = "AAAAAAAAAA";
    private static final String UPDATED_POWER_LINE_BRANCH = "BBBBBBBBBB";

    private static final String DEFAULT_P_1 = "AAAAAAAAAA";
    private static final String UPDATED_P_1 = "BBBBBBBBBB";

    private static final String DEFAULT_P_2 = "AAAAAAAAAA";
    private static final String UPDATED_P_2 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/topologies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TopologyRepository topologyRepository;

    @Autowired
    private TopologyMapper topologyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTopologyMockMvc;

    private Topology topology;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Topology createEntity(EntityManager em) {
        Topology topology = new Topology().powerLineBranch(DEFAULT_POWER_LINE_BRANCH).p1(DEFAULT_P_1).p2(DEFAULT_P_2);
        return topology;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Topology createUpdatedEntity(EntityManager em) {
        Topology topology = new Topology().powerLineBranch(UPDATED_POWER_LINE_BRANCH).p1(UPDATED_P_1).p2(UPDATED_P_2);
        return topology;
    }

    @BeforeEach
    public void initTest() {
        topology = createEntity(em);
    }

    @Test
    @Transactional
    void createTopology() throws Exception {
        int databaseSizeBeforeCreate = topologyRepository.findAll().size();
        // Create the Topology
        TopologyDTO topologyDTO = topologyMapper.toDto(topology);
        restTopologyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(topologyDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Topology in the database
        List<Topology> topologyList = topologyRepository.findAll();
        assertThat(topologyList).hasSize(databaseSizeBeforeCreate + 1);
        Topology testTopology = topologyList.get(topologyList.size() - 1);
        assertThat(testTopology.getPowerLineBranch()).isEqualTo(DEFAULT_POWER_LINE_BRANCH);
        assertThat(testTopology.getp1()).isEqualTo(DEFAULT_P_1);
        assertThat(testTopology.getp2()).isEqualTo(DEFAULT_P_2);
    }

    @Test
    @Transactional
    void createTopologyWithExistingId() throws Exception {
        // Create the Topology with an existing ID
        topology.setId(1L);
        TopologyDTO topologyDTO = topologyMapper.toDto(topology);

        int databaseSizeBeforeCreate = topologyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTopologyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(topologyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Topology in the database
        List<Topology> topologyList = topologyRepository.findAll();
        assertThat(topologyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTopologies() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        // Get all the topologyList
        restTopologyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topology.getId().intValue())))
            .andExpect(jsonPath("$.[*].powerLineBranch").value(hasItem(DEFAULT_POWER_LINE_BRANCH)))
            .andExpect(jsonPath("$.[*].p1").value(hasItem(DEFAULT_P_1)))
            .andExpect(jsonPath("$.[*].p2").value(hasItem(DEFAULT_P_2)));
    }

    @Test
    @Transactional
    void getTopology() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        // Get the topology
        restTopologyMockMvc
            .perform(get(ENTITY_API_URL_ID, topology.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(topology.getId().intValue()))
            .andExpect(jsonPath("$.powerLineBranch").value(DEFAULT_POWER_LINE_BRANCH))
            .andExpect(jsonPath("$.p1").value(DEFAULT_P_1))
            .andExpect(jsonPath("$.p2").value(DEFAULT_P_2));
    }

    @Test
    @Transactional
    void getTopologiesByIdFiltering() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        Long id = topology.getId();

        defaultTopologyShouldBeFound("id.equals=" + id);
        defaultTopologyShouldNotBeFound("id.notEquals=" + id);

        defaultTopologyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTopologyShouldNotBeFound("id.greaterThan=" + id);

        defaultTopologyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTopologyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTopologiesByPowerLineBranchIsEqualToSomething() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        // Get all the topologyList where powerLineBranch equals to DEFAULT_POWER_LINE_BRANCH
        defaultTopologyShouldBeFound("powerLineBranch.equals=" + DEFAULT_POWER_LINE_BRANCH);

        // Get all the topologyList where powerLineBranch equals to UPDATED_POWER_LINE_BRANCH
        defaultTopologyShouldNotBeFound("powerLineBranch.equals=" + UPDATED_POWER_LINE_BRANCH);
    }

    @Test
    @Transactional
    void getAllTopologiesByPowerLineBranchIsNotEqualToSomething() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        // Get all the topologyList where powerLineBranch not equals to DEFAULT_POWER_LINE_BRANCH
        defaultTopologyShouldNotBeFound("powerLineBranch.notEquals=" + DEFAULT_POWER_LINE_BRANCH);

        // Get all the topologyList where powerLineBranch not equals to UPDATED_POWER_LINE_BRANCH
        defaultTopologyShouldBeFound("powerLineBranch.notEquals=" + UPDATED_POWER_LINE_BRANCH);
    }

    @Test
    @Transactional
    void getAllTopologiesByPowerLineBranchIsInShouldWork() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        // Get all the topologyList where powerLineBranch in DEFAULT_POWER_LINE_BRANCH or UPDATED_POWER_LINE_BRANCH
        defaultTopologyShouldBeFound("powerLineBranch.in=" + DEFAULT_POWER_LINE_BRANCH + "," + UPDATED_POWER_LINE_BRANCH);

        // Get all the topologyList where powerLineBranch equals to UPDATED_POWER_LINE_BRANCH
        defaultTopologyShouldNotBeFound("powerLineBranch.in=" + UPDATED_POWER_LINE_BRANCH);
    }

    @Test
    @Transactional
    void getAllTopologiesByPowerLineBranchIsNullOrNotNull() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        // Get all the topologyList where powerLineBranch is not null
        defaultTopologyShouldBeFound("powerLineBranch.specified=true");

        // Get all the topologyList where powerLineBranch is null
        defaultTopologyShouldNotBeFound("powerLineBranch.specified=false");
    }

    @Test
    @Transactional
    void getAllTopologiesByPowerLineBranchContainsSomething() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        // Get all the topologyList where powerLineBranch contains DEFAULT_POWER_LINE_BRANCH
        defaultTopologyShouldBeFound("powerLineBranch.contains=" + DEFAULT_POWER_LINE_BRANCH);

        // Get all the topologyList where powerLineBranch contains UPDATED_POWER_LINE_BRANCH
        defaultTopologyShouldNotBeFound("powerLineBranch.contains=" + UPDATED_POWER_LINE_BRANCH);
    }

    @Test
    @Transactional
    void getAllTopologiesByPowerLineBranchNotContainsSomething() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        // Get all the topologyList where powerLineBranch does not contain DEFAULT_POWER_LINE_BRANCH
        defaultTopologyShouldNotBeFound("powerLineBranch.doesNotContain=" + DEFAULT_POWER_LINE_BRANCH);

        // Get all the topologyList where powerLineBranch does not contain UPDATED_POWER_LINE_BRANCH
        defaultTopologyShouldBeFound("powerLineBranch.doesNotContain=" + UPDATED_POWER_LINE_BRANCH);
    }

    @Test
    @Transactional
    void getAllTopologiesByp1IsEqualToSomething() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        // Get all the topologyList where p1 equals to DEFAULT_P_1
        defaultTopologyShouldBeFound("p1.equals=" + DEFAULT_P_1);

        // Get all the topologyList where p1 equals to UPDATED_P_1
        defaultTopologyShouldNotBeFound("p1.equals=" + UPDATED_P_1);
    }

    @Test
    @Transactional
    void getAllTopologiesByp1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        // Get all the topologyList where p1 not equals to DEFAULT_P_1
        defaultTopologyShouldNotBeFound("p1.notEquals=" + DEFAULT_P_1);

        // Get all the topologyList where p1 not equals to UPDATED_P_1
        defaultTopologyShouldBeFound("p1.notEquals=" + UPDATED_P_1);
    }

    @Test
    @Transactional
    void getAllTopologiesByp1IsInShouldWork() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        // Get all the topologyList where p1 in DEFAULT_P_1 or UPDATED_P_1
        defaultTopologyShouldBeFound("p1.in=" + DEFAULT_P_1 + "," + UPDATED_P_1);

        // Get all the topologyList where p1 equals to UPDATED_P_1
        defaultTopologyShouldNotBeFound("p1.in=" + UPDATED_P_1);
    }

    @Test
    @Transactional
    void getAllTopologiesByp1IsNullOrNotNull() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        // Get all the topologyList where p1 is not null
        defaultTopologyShouldBeFound("p1.specified=true");

        // Get all the topologyList where p1 is null
        defaultTopologyShouldNotBeFound("p1.specified=false");
    }

    @Test
    @Transactional
    void getAllTopologiesByp1ContainsSomething() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        // Get all the topologyList where p1 contains DEFAULT_P_1
        defaultTopologyShouldBeFound("p1.contains=" + DEFAULT_P_1);

        // Get all the topologyList where p1 contains UPDATED_P_1
        defaultTopologyShouldNotBeFound("p1.contains=" + UPDATED_P_1);
    }

    @Test
    @Transactional
    void getAllTopologiesByp1NotContainsSomething() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        // Get all the topologyList where p1 does not contain DEFAULT_P_1
        defaultTopologyShouldNotBeFound("p1.doesNotContain=" + DEFAULT_P_1);

        // Get all the topologyList where p1 does not contain UPDATED_P_1
        defaultTopologyShouldBeFound("p1.doesNotContain=" + UPDATED_P_1);
    }

    @Test
    @Transactional
    void getAllTopologiesByp2IsEqualToSomething() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        // Get all the topologyList where p2 equals to DEFAULT_P_2
        defaultTopologyShouldBeFound("p2.equals=" + DEFAULT_P_2);

        // Get all the topologyList where p2 equals to UPDATED_P_2
        defaultTopologyShouldNotBeFound("p2.equals=" + UPDATED_P_2);
    }

    @Test
    @Transactional
    void getAllTopologiesByp2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        // Get all the topologyList where p2 not equals to DEFAULT_P_2
        defaultTopologyShouldNotBeFound("p2.notEquals=" + DEFAULT_P_2);

        // Get all the topologyList where p2 not equals to UPDATED_P_2
        defaultTopologyShouldBeFound("p2.notEquals=" + UPDATED_P_2);
    }

    @Test
    @Transactional
    void getAllTopologiesByp2IsInShouldWork() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        // Get all the topologyList where p2 in DEFAULT_P_2 or UPDATED_P_2
        defaultTopologyShouldBeFound("p2.in=" + DEFAULT_P_2 + "," + UPDATED_P_2);

        // Get all the topologyList where p2 equals to UPDATED_P_2
        defaultTopologyShouldNotBeFound("p2.in=" + UPDATED_P_2);
    }

    @Test
    @Transactional
    void getAllTopologiesByp2IsNullOrNotNull() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        // Get all the topologyList where p2 is not null
        defaultTopologyShouldBeFound("p2.specified=true");

        // Get all the topologyList where p2 is null
        defaultTopologyShouldNotBeFound("p2.specified=false");
    }

    @Test
    @Transactional
    void getAllTopologiesByp2ContainsSomething() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        // Get all the topologyList where p2 contains DEFAULT_P_2
        defaultTopologyShouldBeFound("p2.contains=" + DEFAULT_P_2);

        // Get all the topologyList where p2 contains UPDATED_P_2
        defaultTopologyShouldNotBeFound("p2.contains=" + UPDATED_P_2);
    }

    @Test
    @Transactional
    void getAllTopologiesByp2NotContainsSomething() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        // Get all the topologyList where p2 does not contain DEFAULT_P_2
        defaultTopologyShouldNotBeFound("p2.doesNotContain=" + DEFAULT_P_2);

        // Get all the topologyList where p2 does not contain UPDATED_P_2
        defaultTopologyShouldBeFound("p2.doesNotContain=" + UPDATED_P_2);
    }

    @Test
    @Transactional
    void getAllTopologiesByPowerLineBranchParentIsEqualToSomething() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);
        TopologyBus powerLineBranchParent;
        if (TestUtil.findAll(em, TopologyBus.class).isEmpty()) {
            powerLineBranchParent = TopologyBusResourceIT.createEntity(em);
            em.persist(powerLineBranchParent);
            em.flush();
        } else {
            powerLineBranchParent = TestUtil.findAll(em, TopologyBus.class).get(0);
        }
        em.persist(powerLineBranchParent);
        em.flush();
        topology.setPowerLineBranchParent(powerLineBranchParent);
        topologyRepository.saveAndFlush(topology);
        Long powerLineBranchParentId = powerLineBranchParent.getId();

        // Get all the topologyList where powerLineBranchParent equals to powerLineBranchParentId
        defaultTopologyShouldBeFound("powerLineBranchParentId.equals=" + powerLineBranchParentId);

        // Get all the topologyList where powerLineBranchParent equals to (powerLineBranchParentId + 1)
        defaultTopologyShouldNotBeFound("powerLineBranchParentId.equals=" + (powerLineBranchParentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTopologyShouldBeFound(String filter) throws Exception {
        restTopologyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topology.getId().intValue())))
            .andExpect(jsonPath("$.[*].powerLineBranch").value(hasItem(DEFAULT_POWER_LINE_BRANCH)))
            .andExpect(jsonPath("$.[*].p1").value(hasItem(DEFAULT_P_1)))
            .andExpect(jsonPath("$.[*].p2").value(hasItem(DEFAULT_P_2)));

        // Check, that the count call also returns 1
        restTopologyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTopologyShouldNotBeFound(String filter) throws Exception {
        restTopologyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTopologyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTopology() throws Exception {
        // Get the topology
        restTopologyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTopology() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        int databaseSizeBeforeUpdate = topologyRepository.findAll().size();

        // Update the topology
        Topology updatedTopology = topologyRepository.findById(topology.getId()).get();
        // Disconnect from session so that the updates on updatedTopology are not directly saved in db
        em.detach(updatedTopology);
        updatedTopology.powerLineBranch(UPDATED_POWER_LINE_BRANCH).p1(UPDATED_P_1).p2(UPDATED_P_2);
        TopologyDTO topologyDTO = topologyMapper.toDto(updatedTopology);

        restTopologyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, topologyDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(topologyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Topology in the database
        List<Topology> topologyList = topologyRepository.findAll();
        assertThat(topologyList).hasSize(databaseSizeBeforeUpdate);
        Topology testTopology = topologyList.get(topologyList.size() - 1);
        assertThat(testTopology.getPowerLineBranch()).isEqualTo(UPDATED_POWER_LINE_BRANCH);
        assertThat(testTopology.getp1()).isEqualTo(UPDATED_P_1);
        assertThat(testTopology.getp2()).isEqualTo(UPDATED_P_2);
    }

    @Test
    @Transactional
    void putNonExistingTopology() throws Exception {
        int databaseSizeBeforeUpdate = topologyRepository.findAll().size();
        topology.setId(count.incrementAndGet());

        // Create the Topology
        TopologyDTO topologyDTO = topologyMapper.toDto(topology);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTopologyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, topologyDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(topologyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Topology in the database
        List<Topology> topologyList = topologyRepository.findAll();
        assertThat(topologyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTopology() throws Exception {
        int databaseSizeBeforeUpdate = topologyRepository.findAll().size();
        topology.setId(count.incrementAndGet());

        // Create the Topology
        TopologyDTO topologyDTO = topologyMapper.toDto(topology);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopologyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(topologyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Topology in the database
        List<Topology> topologyList = topologyRepository.findAll();
        assertThat(topologyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTopology() throws Exception {
        int databaseSizeBeforeUpdate = topologyRepository.findAll().size();
        topology.setId(count.incrementAndGet());

        // Create the Topology
        TopologyDTO topologyDTO = topologyMapper.toDto(topology);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopologyMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(topologyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Topology in the database
        List<Topology> topologyList = topologyRepository.findAll();
        assertThat(topologyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTopologyWithPatch() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        int databaseSizeBeforeUpdate = topologyRepository.findAll().size();

        // Update the topology using partial update
        Topology partialUpdatedTopology = new Topology();
        partialUpdatedTopology.setId(topology.getId());

        restTopologyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTopology.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTopology))
            )
            .andExpect(status().isOk());

        // Validate the Topology in the database
        List<Topology> topologyList = topologyRepository.findAll();
        assertThat(topologyList).hasSize(databaseSizeBeforeUpdate);
        Topology testTopology = topologyList.get(topologyList.size() - 1);
        assertThat(testTopology.getPowerLineBranch()).isEqualTo(DEFAULT_POWER_LINE_BRANCH);
        assertThat(testTopology.getp1()).isEqualTo(DEFAULT_P_1);
        assertThat(testTopology.getp2()).isEqualTo(DEFAULT_P_2);
    }

    @Test
    @Transactional
    void fullUpdateTopologyWithPatch() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        int databaseSizeBeforeUpdate = topologyRepository.findAll().size();

        // Update the topology using partial update
        Topology partialUpdatedTopology = new Topology();
        partialUpdatedTopology.setId(topology.getId());

        partialUpdatedTopology.powerLineBranch(UPDATED_POWER_LINE_BRANCH).p1(UPDATED_P_1).p2(UPDATED_P_2);

        restTopologyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTopology.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTopology))
            )
            .andExpect(status().isOk());

        // Validate the Topology in the database
        List<Topology> topologyList = topologyRepository.findAll();
        assertThat(topologyList).hasSize(databaseSizeBeforeUpdate);
        Topology testTopology = topologyList.get(topologyList.size() - 1);
        assertThat(testTopology.getPowerLineBranch()).isEqualTo(UPDATED_POWER_LINE_BRANCH);
        assertThat(testTopology.getp1()).isEqualTo(UPDATED_P_1);
        assertThat(testTopology.getp2()).isEqualTo(UPDATED_P_2);
    }

    @Test
    @Transactional
    void patchNonExistingTopology() throws Exception {
        int databaseSizeBeforeUpdate = topologyRepository.findAll().size();
        topology.setId(count.incrementAndGet());

        // Create the Topology
        TopologyDTO topologyDTO = topologyMapper.toDto(topology);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTopologyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, topologyDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(topologyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Topology in the database
        List<Topology> topologyList = topologyRepository.findAll();
        assertThat(topologyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTopology() throws Exception {
        int databaseSizeBeforeUpdate = topologyRepository.findAll().size();
        topology.setId(count.incrementAndGet());

        // Create the Topology
        TopologyDTO topologyDTO = topologyMapper.toDto(topology);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopologyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(topologyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Topology in the database
        List<Topology> topologyList = topologyRepository.findAll();
        assertThat(topologyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTopology() throws Exception {
        int databaseSizeBeforeUpdate = topologyRepository.findAll().size();
        topology.setId(count.incrementAndGet());

        // Create the Topology
        TopologyDTO topologyDTO = topologyMapper.toDto(topology);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopologyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(topologyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Topology in the database
        List<Topology> topologyList = topologyRepository.findAll();
        assertThat(topologyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTopology() throws Exception {
        // Initialize the database
        topologyRepository.saveAndFlush(topology);

        int databaseSizeBeforeDelete = topologyRepository.findAll().size();

        // Delete the topology
        restTopologyMockMvc
            .perform(delete(ENTITY_API_URL_ID, topology.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Topology> topologyList = topologyRepository.findAll();
        assertThat(topologyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
