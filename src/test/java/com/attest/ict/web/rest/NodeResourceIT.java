package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Node;
import com.attest.ict.repository.NodeRepository;
import com.attest.ict.service.criteria.NodeCriteria;
import com.attest.ict.service.dto.NodeDTO;
import com.attest.ict.service.mapper.NodeMapper;
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
 * Integration tests for the {@link NodeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NodeResourceIT {

    private static final String DEFAULT_NETWORK_ID = "AAAAAAAAAA";
    private static final String UPDATED_NETWORK_ID = "BBBBBBBBBB";

    private static final Long DEFAULT_LOAD_ID = 1L;
    private static final Long UPDATED_LOAD_ID = 2L;
    private static final Long SMALLER_LOAD_ID = 1L - 1L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/nodes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private NodeMapper nodeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNodeMockMvc;

    private Node node;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Node createEntity(EntityManager em) {
        Node node = new Node().networkId(DEFAULT_NETWORK_ID).loadId(DEFAULT_LOAD_ID).name(DEFAULT_NAME);
        return node;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Node createUpdatedEntity(EntityManager em) {
        Node node = new Node().networkId(UPDATED_NETWORK_ID).loadId(UPDATED_LOAD_ID).name(UPDATED_NAME);
        return node;
    }

    @BeforeEach
    public void initTest() {
        node = createEntity(em);
    }

    @Test
    @Transactional
    void createNode() throws Exception {
        int databaseSizeBeforeCreate = nodeRepository.findAll().size();
        // Create the Node
        NodeDTO nodeDTO = nodeMapper.toDto(node);
        restNodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nodeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Node in the database
        List<Node> nodeList = nodeRepository.findAll();
        assertThat(nodeList).hasSize(databaseSizeBeforeCreate + 1);
        Node testNode = nodeList.get(nodeList.size() - 1);
        assertThat(testNode.getNetworkId()).isEqualTo(DEFAULT_NETWORK_ID);
        assertThat(testNode.getLoadId()).isEqualTo(DEFAULT_LOAD_ID);
        assertThat(testNode.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createNodeWithExistingId() throws Exception {
        // Create the Node with an existing ID
        node.setId(1L);
        NodeDTO nodeDTO = nodeMapper.toDto(node);

        int databaseSizeBeforeCreate = nodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Node in the database
        List<Node> nodeList = nodeRepository.findAll();
        assertThat(nodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNodes() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList
        restNodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(node.getId().intValue())))
            .andExpect(jsonPath("$.[*].networkId").value(hasItem(DEFAULT_NETWORK_ID)))
            .andExpect(jsonPath("$.[*].loadId").value(hasItem(DEFAULT_LOAD_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getNode() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get the node
        restNodeMockMvc
            .perform(get(ENTITY_API_URL_ID, node.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(node.getId().intValue()))
            .andExpect(jsonPath("$.networkId").value(DEFAULT_NETWORK_ID))
            .andExpect(jsonPath("$.loadId").value(DEFAULT_LOAD_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNodesByIdFiltering() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        Long id = node.getId();

        defaultNodeShouldBeFound("id.equals=" + id);
        defaultNodeShouldNotBeFound("id.notEquals=" + id);

        defaultNodeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNodeShouldNotBeFound("id.greaterThan=" + id);

        defaultNodeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNodeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNodesByNetworkIdIsEqualToSomething() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList where networkId equals to DEFAULT_NETWORK_ID
        defaultNodeShouldBeFound("networkId.equals=" + DEFAULT_NETWORK_ID);

        // Get all the nodeList where networkId equals to UPDATED_NETWORK_ID
        defaultNodeShouldNotBeFound("networkId.equals=" + UPDATED_NETWORK_ID);
    }

    @Test
    @Transactional
    void getAllNodesByNetworkIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList where networkId not equals to DEFAULT_NETWORK_ID
        defaultNodeShouldNotBeFound("networkId.notEquals=" + DEFAULT_NETWORK_ID);

        // Get all the nodeList where networkId not equals to UPDATED_NETWORK_ID
        defaultNodeShouldBeFound("networkId.notEquals=" + UPDATED_NETWORK_ID);
    }

    @Test
    @Transactional
    void getAllNodesByNetworkIdIsInShouldWork() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList where networkId in DEFAULT_NETWORK_ID or UPDATED_NETWORK_ID
        defaultNodeShouldBeFound("networkId.in=" + DEFAULT_NETWORK_ID + "," + UPDATED_NETWORK_ID);

        // Get all the nodeList where networkId equals to UPDATED_NETWORK_ID
        defaultNodeShouldNotBeFound("networkId.in=" + UPDATED_NETWORK_ID);
    }

    @Test
    @Transactional
    void getAllNodesByNetworkIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList where networkId is not null
        defaultNodeShouldBeFound("networkId.specified=true");

        // Get all the nodeList where networkId is null
        defaultNodeShouldNotBeFound("networkId.specified=false");
    }

    @Test
    @Transactional
    void getAllNodesByNetworkIdContainsSomething() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList where networkId contains DEFAULT_NETWORK_ID
        defaultNodeShouldBeFound("networkId.contains=" + DEFAULT_NETWORK_ID);

        // Get all the nodeList where networkId contains UPDATED_NETWORK_ID
        defaultNodeShouldNotBeFound("networkId.contains=" + UPDATED_NETWORK_ID);
    }

    @Test
    @Transactional
    void getAllNodesByNetworkIdNotContainsSomething() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList where networkId does not contain DEFAULT_NETWORK_ID
        defaultNodeShouldNotBeFound("networkId.doesNotContain=" + DEFAULT_NETWORK_ID);

        // Get all the nodeList where networkId does not contain UPDATED_NETWORK_ID
        defaultNodeShouldBeFound("networkId.doesNotContain=" + UPDATED_NETWORK_ID);
    }

    @Test
    @Transactional
    void getAllNodesByLoadIdIsEqualToSomething() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList where loadId equals to DEFAULT_LOAD_ID
        defaultNodeShouldBeFound("loadId.equals=" + DEFAULT_LOAD_ID);

        // Get all the nodeList where loadId equals to UPDATED_LOAD_ID
        defaultNodeShouldNotBeFound("loadId.equals=" + UPDATED_LOAD_ID);
    }

    @Test
    @Transactional
    void getAllNodesByLoadIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList where loadId not equals to DEFAULT_LOAD_ID
        defaultNodeShouldNotBeFound("loadId.notEquals=" + DEFAULT_LOAD_ID);

        // Get all the nodeList where loadId not equals to UPDATED_LOAD_ID
        defaultNodeShouldBeFound("loadId.notEquals=" + UPDATED_LOAD_ID);
    }

    @Test
    @Transactional
    void getAllNodesByLoadIdIsInShouldWork() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList where loadId in DEFAULT_LOAD_ID or UPDATED_LOAD_ID
        defaultNodeShouldBeFound("loadId.in=" + DEFAULT_LOAD_ID + "," + UPDATED_LOAD_ID);

        // Get all the nodeList where loadId equals to UPDATED_LOAD_ID
        defaultNodeShouldNotBeFound("loadId.in=" + UPDATED_LOAD_ID);
    }

    @Test
    @Transactional
    void getAllNodesByLoadIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList where loadId is not null
        defaultNodeShouldBeFound("loadId.specified=true");

        // Get all the nodeList where loadId is null
        defaultNodeShouldNotBeFound("loadId.specified=false");
    }

    @Test
    @Transactional
    void getAllNodesByLoadIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList where loadId is greater than or equal to DEFAULT_LOAD_ID
        defaultNodeShouldBeFound("loadId.greaterThanOrEqual=" + DEFAULT_LOAD_ID);

        // Get all the nodeList where loadId is greater than or equal to UPDATED_LOAD_ID
        defaultNodeShouldNotBeFound("loadId.greaterThanOrEqual=" + UPDATED_LOAD_ID);
    }

    @Test
    @Transactional
    void getAllNodesByLoadIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList where loadId is less than or equal to DEFAULT_LOAD_ID
        defaultNodeShouldBeFound("loadId.lessThanOrEqual=" + DEFAULT_LOAD_ID);

        // Get all the nodeList where loadId is less than or equal to SMALLER_LOAD_ID
        defaultNodeShouldNotBeFound("loadId.lessThanOrEqual=" + SMALLER_LOAD_ID);
    }

    @Test
    @Transactional
    void getAllNodesByLoadIdIsLessThanSomething() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList where loadId is less than DEFAULT_LOAD_ID
        defaultNodeShouldNotBeFound("loadId.lessThan=" + DEFAULT_LOAD_ID);

        // Get all the nodeList where loadId is less than UPDATED_LOAD_ID
        defaultNodeShouldBeFound("loadId.lessThan=" + UPDATED_LOAD_ID);
    }

    @Test
    @Transactional
    void getAllNodesByLoadIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList where loadId is greater than DEFAULT_LOAD_ID
        defaultNodeShouldNotBeFound("loadId.greaterThan=" + DEFAULT_LOAD_ID);

        // Get all the nodeList where loadId is greater than SMALLER_LOAD_ID
        defaultNodeShouldBeFound("loadId.greaterThan=" + SMALLER_LOAD_ID);
    }

    @Test
    @Transactional
    void getAllNodesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList where name equals to DEFAULT_NAME
        defaultNodeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the nodeList where name equals to UPDATED_NAME
        defaultNodeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNodesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList where name not equals to DEFAULT_NAME
        defaultNodeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the nodeList where name not equals to UPDATED_NAME
        defaultNodeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNodesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultNodeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the nodeList where name equals to UPDATED_NAME
        defaultNodeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNodesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList where name is not null
        defaultNodeShouldBeFound("name.specified=true");

        // Get all the nodeList where name is null
        defaultNodeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllNodesByNameContainsSomething() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList where name contains DEFAULT_NAME
        defaultNodeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the nodeList where name contains UPDATED_NAME
        defaultNodeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNodesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList where name does not contain DEFAULT_NAME
        defaultNodeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the nodeList where name does not contain UPDATED_NAME
        defaultNodeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNodeShouldBeFound(String filter) throws Exception {
        restNodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(node.getId().intValue())))
            .andExpect(jsonPath("$.[*].networkId").value(hasItem(DEFAULT_NETWORK_ID)))
            .andExpect(jsonPath("$.[*].loadId").value(hasItem(DEFAULT_LOAD_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restNodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNodeShouldNotBeFound(String filter) throws Exception {
        restNodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNode() throws Exception {
        // Get the node
        restNodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNode() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        int databaseSizeBeforeUpdate = nodeRepository.findAll().size();

        // Update the node
        Node updatedNode = nodeRepository.findById(node.getId()).get();
        // Disconnect from session so that the updates on updatedNode are not directly saved in db
        em.detach(updatedNode);
        updatedNode.networkId(UPDATED_NETWORK_ID).loadId(UPDATED_LOAD_ID).name(UPDATED_NAME);
        NodeDTO nodeDTO = nodeMapper.toDto(updatedNode);

        restNodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nodeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Node in the database
        List<Node> nodeList = nodeRepository.findAll();
        assertThat(nodeList).hasSize(databaseSizeBeforeUpdate);
        Node testNode = nodeList.get(nodeList.size() - 1);
        assertThat(testNode.getNetworkId()).isEqualTo(UPDATED_NETWORK_ID);
        assertThat(testNode.getLoadId()).isEqualTo(UPDATED_LOAD_ID);
        assertThat(testNode.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingNode() throws Exception {
        int databaseSizeBeforeUpdate = nodeRepository.findAll().size();
        node.setId(count.incrementAndGet());

        // Create the Node
        NodeDTO nodeDTO = nodeMapper.toDto(node);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nodeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Node in the database
        List<Node> nodeList = nodeRepository.findAll();
        assertThat(nodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNode() throws Exception {
        int databaseSizeBeforeUpdate = nodeRepository.findAll().size();
        node.setId(count.incrementAndGet());

        // Create the Node
        NodeDTO nodeDTO = nodeMapper.toDto(node);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Node in the database
        List<Node> nodeList = nodeRepository.findAll();
        assertThat(nodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNode() throws Exception {
        int databaseSizeBeforeUpdate = nodeRepository.findAll().size();
        node.setId(count.incrementAndGet());

        // Create the Node
        NodeDTO nodeDTO = nodeMapper.toDto(node);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNodeMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Node in the database
        List<Node> nodeList = nodeRepository.findAll();
        assertThat(nodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNodeWithPatch() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        int databaseSizeBeforeUpdate = nodeRepository.findAll().size();

        // Update the node using partial update
        Node partialUpdatedNode = new Node();
        partialUpdatedNode.setId(node.getId());

        restNodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNode.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNode))
            )
            .andExpect(status().isOk());

        // Validate the Node in the database
        List<Node> nodeList = nodeRepository.findAll();
        assertThat(nodeList).hasSize(databaseSizeBeforeUpdate);
        Node testNode = nodeList.get(nodeList.size() - 1);
        assertThat(testNode.getNetworkId()).isEqualTo(DEFAULT_NETWORK_ID);
        assertThat(testNode.getLoadId()).isEqualTo(DEFAULT_LOAD_ID);
        assertThat(testNode.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateNodeWithPatch() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        int databaseSizeBeforeUpdate = nodeRepository.findAll().size();

        // Update the node using partial update
        Node partialUpdatedNode = new Node();
        partialUpdatedNode.setId(node.getId());

        partialUpdatedNode.networkId(UPDATED_NETWORK_ID).loadId(UPDATED_LOAD_ID).name(UPDATED_NAME);

        restNodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNode.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNode))
            )
            .andExpect(status().isOk());

        // Validate the Node in the database
        List<Node> nodeList = nodeRepository.findAll();
        assertThat(nodeList).hasSize(databaseSizeBeforeUpdate);
        Node testNode = nodeList.get(nodeList.size() - 1);
        assertThat(testNode.getNetworkId()).isEqualTo(UPDATED_NETWORK_ID);
        assertThat(testNode.getLoadId()).isEqualTo(UPDATED_LOAD_ID);
        assertThat(testNode.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingNode() throws Exception {
        int databaseSizeBeforeUpdate = nodeRepository.findAll().size();
        node.setId(count.incrementAndGet());

        // Create the Node
        NodeDTO nodeDTO = nodeMapper.toDto(node);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nodeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Node in the database
        List<Node> nodeList = nodeRepository.findAll();
        assertThat(nodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNode() throws Exception {
        int databaseSizeBeforeUpdate = nodeRepository.findAll().size();
        node.setId(count.incrementAndGet());

        // Create the Node
        NodeDTO nodeDTO = nodeMapper.toDto(node);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Node in the database
        List<Node> nodeList = nodeRepository.findAll();
        assertThat(nodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNode() throws Exception {
        int databaseSizeBeforeUpdate = nodeRepository.findAll().size();
        node.setId(count.incrementAndGet());

        // Create the Node
        NodeDTO nodeDTO = nodeMapper.toDto(node);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNodeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Node in the database
        List<Node> nodeList = nodeRepository.findAll();
        assertThat(nodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNode() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        int databaseSizeBeforeDelete = nodeRepository.findAll().size();

        // Delete the node
        restNodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, node.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Node> nodeList = nodeRepository.findAll();
        assertThat(nodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
