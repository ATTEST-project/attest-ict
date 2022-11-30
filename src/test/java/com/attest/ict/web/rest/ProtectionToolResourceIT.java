package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Branch;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.ProtectionTool;
import com.attest.ict.repository.ProtectionToolRepository;
import com.attest.ict.service.criteria.ProtectionToolCriteria;
import com.attest.ict.service.dto.ProtectionToolDTO;
import com.attest.ict.service.mapper.ProtectionToolMapper;
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
 * Integration tests for the {@link ProtectionToolResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProtectionToolResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/protection-tools";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProtectionToolRepository protectionToolRepository;

    @Autowired
    private ProtectionToolMapper protectionToolMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProtectionToolMockMvc;

    private ProtectionTool protectionTool;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProtectionTool createEntity(EntityManager em) {
        ProtectionTool protectionTool = new ProtectionTool().type(DEFAULT_TYPE);
        return protectionTool;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProtectionTool createUpdatedEntity(EntityManager em) {
        ProtectionTool protectionTool = new ProtectionTool().type(UPDATED_TYPE);
        return protectionTool;
    }

    @BeforeEach
    public void initTest() {
        protectionTool = createEntity(em);
    }

    @Test
    @Transactional
    void createProtectionTool() throws Exception {
        int databaseSizeBeforeCreate = protectionToolRepository.findAll().size();
        // Create the ProtectionTool
        ProtectionToolDTO protectionToolDTO = protectionToolMapper.toDto(protectionTool);
        restProtectionToolMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(protectionToolDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProtectionTool in the database
        List<ProtectionTool> protectionToolList = protectionToolRepository.findAll();
        assertThat(protectionToolList).hasSize(databaseSizeBeforeCreate + 1);
        ProtectionTool testProtectionTool = protectionToolList.get(protectionToolList.size() - 1);
        assertThat(testProtectionTool.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createProtectionToolWithExistingId() throws Exception {
        // Create the ProtectionTool with an existing ID
        protectionTool.setId(1L);
        ProtectionToolDTO protectionToolDTO = protectionToolMapper.toDto(protectionTool);

        int databaseSizeBeforeCreate = protectionToolRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProtectionToolMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(protectionToolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProtectionTool in the database
        List<ProtectionTool> protectionToolList = protectionToolRepository.findAll();
        assertThat(protectionToolList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProtectionTools() throws Exception {
        // Initialize the database
        protectionToolRepository.saveAndFlush(protectionTool);

        // Get all the protectionToolList
        restProtectionToolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(protectionTool.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getProtectionTool() throws Exception {
        // Initialize the database
        protectionToolRepository.saveAndFlush(protectionTool);

        // Get the protectionTool
        restProtectionToolMockMvc
            .perform(get(ENTITY_API_URL_ID, protectionTool.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(protectionTool.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getProtectionToolsByIdFiltering() throws Exception {
        // Initialize the database
        protectionToolRepository.saveAndFlush(protectionTool);

        Long id = protectionTool.getId();

        defaultProtectionToolShouldBeFound("id.equals=" + id);
        defaultProtectionToolShouldNotBeFound("id.notEquals=" + id);

        defaultProtectionToolShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProtectionToolShouldNotBeFound("id.greaterThan=" + id);

        defaultProtectionToolShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProtectionToolShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProtectionToolsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        protectionToolRepository.saveAndFlush(protectionTool);

        // Get all the protectionToolList where type equals to DEFAULT_TYPE
        defaultProtectionToolShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the protectionToolList where type equals to UPDATED_TYPE
        defaultProtectionToolShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllProtectionToolsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        protectionToolRepository.saveAndFlush(protectionTool);

        // Get all the protectionToolList where type not equals to DEFAULT_TYPE
        defaultProtectionToolShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the protectionToolList where type not equals to UPDATED_TYPE
        defaultProtectionToolShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllProtectionToolsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        protectionToolRepository.saveAndFlush(protectionTool);

        // Get all the protectionToolList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultProtectionToolShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the protectionToolList where type equals to UPDATED_TYPE
        defaultProtectionToolShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllProtectionToolsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        protectionToolRepository.saveAndFlush(protectionTool);

        // Get all the protectionToolList where type is not null
        defaultProtectionToolShouldBeFound("type.specified=true");

        // Get all the protectionToolList where type is null
        defaultProtectionToolShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllProtectionToolsByTypeContainsSomething() throws Exception {
        // Initialize the database
        protectionToolRepository.saveAndFlush(protectionTool);

        // Get all the protectionToolList where type contains DEFAULT_TYPE
        defaultProtectionToolShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the protectionToolList where type contains UPDATED_TYPE
        defaultProtectionToolShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllProtectionToolsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        protectionToolRepository.saveAndFlush(protectionTool);

        // Get all the protectionToolList where type does not contain DEFAULT_TYPE
        defaultProtectionToolShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the protectionToolList where type does not contain UPDATED_TYPE
        defaultProtectionToolShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllProtectionToolsByBranchIsEqualToSomething() throws Exception {
        // Initialize the database
        protectionToolRepository.saveAndFlush(protectionTool);
        Branch branch;
        if (TestUtil.findAll(em, Branch.class).isEmpty()) {
            branch = BranchResourceIT.createEntity(em);
            em.persist(branch);
            em.flush();
        } else {
            branch = TestUtil.findAll(em, Branch.class).get(0);
        }
        em.persist(branch);
        em.flush();
        protectionTool.setBranch(branch);
        protectionToolRepository.saveAndFlush(protectionTool);
        Long branchId = branch.getId();

        // Get all the protectionToolList where branch equals to branchId
        defaultProtectionToolShouldBeFound("branchId.equals=" + branchId);

        // Get all the protectionToolList where branch equals to (branchId + 1)
        defaultProtectionToolShouldNotBeFound("branchId.equals=" + (branchId + 1));
    }

    @Test
    @Transactional
    void getAllProtectionToolsByBusIsEqualToSomething() throws Exception {
        // Initialize the database
        protectionToolRepository.saveAndFlush(protectionTool);
        Bus bus;
        if (TestUtil.findAll(em, Bus.class).isEmpty()) {
            bus = BusResourceIT.createEntity(em);
            em.persist(bus);
            em.flush();
        } else {
            bus = TestUtil.findAll(em, Bus.class).get(0);
        }
        em.persist(bus);
        em.flush();
        protectionTool.setBus(bus);
        protectionToolRepository.saveAndFlush(protectionTool);
        Long busId = bus.getId();

        // Get all the protectionToolList where bus equals to busId
        defaultProtectionToolShouldBeFound("busId.equals=" + busId);

        // Get all the protectionToolList where bus equals to (busId + 1)
        defaultProtectionToolShouldNotBeFound("busId.equals=" + (busId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProtectionToolShouldBeFound(String filter) throws Exception {
        restProtectionToolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(protectionTool.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));

        // Check, that the count call also returns 1
        restProtectionToolMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProtectionToolShouldNotBeFound(String filter) throws Exception {
        restProtectionToolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProtectionToolMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProtectionTool() throws Exception {
        // Get the protectionTool
        restProtectionToolMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProtectionTool() throws Exception {
        // Initialize the database
        protectionToolRepository.saveAndFlush(protectionTool);

        int databaseSizeBeforeUpdate = protectionToolRepository.findAll().size();

        // Update the protectionTool
        ProtectionTool updatedProtectionTool = protectionToolRepository.findById(protectionTool.getId()).get();
        // Disconnect from session so that the updates on updatedProtectionTool are not directly saved in db
        em.detach(updatedProtectionTool);
        updatedProtectionTool.type(UPDATED_TYPE);
        ProtectionToolDTO protectionToolDTO = protectionToolMapper.toDto(updatedProtectionTool);

        restProtectionToolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, protectionToolDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(protectionToolDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProtectionTool in the database
        List<ProtectionTool> protectionToolList = protectionToolRepository.findAll();
        assertThat(protectionToolList).hasSize(databaseSizeBeforeUpdate);
        ProtectionTool testProtectionTool = protectionToolList.get(protectionToolList.size() - 1);
        assertThat(testProtectionTool.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingProtectionTool() throws Exception {
        int databaseSizeBeforeUpdate = protectionToolRepository.findAll().size();
        protectionTool.setId(count.incrementAndGet());

        // Create the ProtectionTool
        ProtectionToolDTO protectionToolDTO = protectionToolMapper.toDto(protectionTool);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProtectionToolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, protectionToolDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(protectionToolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProtectionTool in the database
        List<ProtectionTool> protectionToolList = protectionToolRepository.findAll();
        assertThat(protectionToolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProtectionTool() throws Exception {
        int databaseSizeBeforeUpdate = protectionToolRepository.findAll().size();
        protectionTool.setId(count.incrementAndGet());

        // Create the ProtectionTool
        ProtectionToolDTO protectionToolDTO = protectionToolMapper.toDto(protectionTool);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProtectionToolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(protectionToolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProtectionTool in the database
        List<ProtectionTool> protectionToolList = protectionToolRepository.findAll();
        assertThat(protectionToolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProtectionTool() throws Exception {
        int databaseSizeBeforeUpdate = protectionToolRepository.findAll().size();
        protectionTool.setId(count.incrementAndGet());

        // Create the ProtectionTool
        ProtectionToolDTO protectionToolDTO = protectionToolMapper.toDto(protectionTool);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProtectionToolMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(protectionToolDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProtectionTool in the database
        List<ProtectionTool> protectionToolList = protectionToolRepository.findAll();
        assertThat(protectionToolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProtectionToolWithPatch() throws Exception {
        // Initialize the database
        protectionToolRepository.saveAndFlush(protectionTool);

        int databaseSizeBeforeUpdate = protectionToolRepository.findAll().size();

        // Update the protectionTool using partial update
        ProtectionTool partialUpdatedProtectionTool = new ProtectionTool();
        partialUpdatedProtectionTool.setId(protectionTool.getId());

        restProtectionToolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProtectionTool.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProtectionTool))
            )
            .andExpect(status().isOk());

        // Validate the ProtectionTool in the database
        List<ProtectionTool> protectionToolList = protectionToolRepository.findAll();
        assertThat(protectionToolList).hasSize(databaseSizeBeforeUpdate);
        ProtectionTool testProtectionTool = protectionToolList.get(protectionToolList.size() - 1);
        assertThat(testProtectionTool.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateProtectionToolWithPatch() throws Exception {
        // Initialize the database
        protectionToolRepository.saveAndFlush(protectionTool);

        int databaseSizeBeforeUpdate = protectionToolRepository.findAll().size();

        // Update the protectionTool using partial update
        ProtectionTool partialUpdatedProtectionTool = new ProtectionTool();
        partialUpdatedProtectionTool.setId(protectionTool.getId());

        partialUpdatedProtectionTool.type(UPDATED_TYPE);

        restProtectionToolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProtectionTool.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProtectionTool))
            )
            .andExpect(status().isOk());

        // Validate the ProtectionTool in the database
        List<ProtectionTool> protectionToolList = protectionToolRepository.findAll();
        assertThat(protectionToolList).hasSize(databaseSizeBeforeUpdate);
        ProtectionTool testProtectionTool = protectionToolList.get(protectionToolList.size() - 1);
        assertThat(testProtectionTool.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingProtectionTool() throws Exception {
        int databaseSizeBeforeUpdate = protectionToolRepository.findAll().size();
        protectionTool.setId(count.incrementAndGet());

        // Create the ProtectionTool
        ProtectionToolDTO protectionToolDTO = protectionToolMapper.toDto(protectionTool);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProtectionToolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, protectionToolDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(protectionToolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProtectionTool in the database
        List<ProtectionTool> protectionToolList = protectionToolRepository.findAll();
        assertThat(protectionToolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProtectionTool() throws Exception {
        int databaseSizeBeforeUpdate = protectionToolRepository.findAll().size();
        protectionTool.setId(count.incrementAndGet());

        // Create the ProtectionTool
        ProtectionToolDTO protectionToolDTO = protectionToolMapper.toDto(protectionTool);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProtectionToolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(protectionToolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProtectionTool in the database
        List<ProtectionTool> protectionToolList = protectionToolRepository.findAll();
        assertThat(protectionToolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProtectionTool() throws Exception {
        int databaseSizeBeforeUpdate = protectionToolRepository.findAll().size();
        protectionTool.setId(count.incrementAndGet());

        // Create the ProtectionTool
        ProtectionToolDTO protectionToolDTO = protectionToolMapper.toDto(protectionTool);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProtectionToolMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(protectionToolDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProtectionTool in the database
        List<ProtectionTool> protectionToolList = protectionToolRepository.findAll();
        assertThat(protectionToolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProtectionTool() throws Exception {
        // Initialize the database
        protectionToolRepository.saveAndFlush(protectionTool);

        int databaseSizeBeforeDelete = protectionToolRepository.findAll().size();

        // Delete the protectionTool
        restProtectionToolMockMvc
            .perform(delete(ENTITY_API_URL_ID, protectionTool.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProtectionTool> protectionToolList = protectionToolRepository.findAll();
        assertThat(protectionToolList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
