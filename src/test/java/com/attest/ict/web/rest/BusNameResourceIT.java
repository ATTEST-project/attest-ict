package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.BusName;
import com.attest.ict.repository.BusNameRepository;
import com.attest.ict.service.criteria.BusNameCriteria;
import com.attest.ict.service.dto.BusNameDTO;
import com.attest.ict.service.mapper.BusNameMapper;
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
 * Integration tests for the {@link BusNameResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BusNameResourceIT {

    private static final String DEFAULT_BUS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BUS_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bus-names";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BusNameRepository busNameRepository;

    @Autowired
    private BusNameMapper busNameMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusNameMockMvc;

    private BusName busName;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusName createEntity(EntityManager em) {
        BusName busName = new BusName().busName(DEFAULT_BUS_NAME);
        return busName;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusName createUpdatedEntity(EntityManager em) {
        BusName busName = new BusName().busName(UPDATED_BUS_NAME);
        return busName;
    }

    @BeforeEach
    public void initTest() {
        busName = createEntity(em);
    }

    @Test
    @Transactional
    void createBusName() throws Exception {
        int databaseSizeBeforeCreate = busNameRepository.findAll().size();
        // Create the BusName
        BusNameDTO busNameDTO = busNameMapper.toDto(busName);
        restBusNameMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busNameDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BusName in the database
        List<BusName> busNameList = busNameRepository.findAll();
        assertThat(busNameList).hasSize(databaseSizeBeforeCreate + 1);
        BusName testBusName = busNameList.get(busNameList.size() - 1);
        assertThat(testBusName.getBusName()).isEqualTo(DEFAULT_BUS_NAME);
    }

    @Test
    @Transactional
    void createBusNameWithExistingId() throws Exception {
        // Create the BusName with an existing ID
        busName.setId(1L);
        BusNameDTO busNameDTO = busNameMapper.toDto(busName);

        int databaseSizeBeforeCreate = busNameRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusNameMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busNameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusName in the database
        List<BusName> busNameList = busNameRepository.findAll();
        assertThat(busNameList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBusNames() throws Exception {
        // Initialize the database
        busNameRepository.saveAndFlush(busName);

        // Get all the busNameList
        restBusNameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(busName.getId().intValue())))
            .andExpect(jsonPath("$.[*].busName").value(hasItem(DEFAULT_BUS_NAME)));
    }

    @Test
    @Transactional
    void getBusName() throws Exception {
        // Initialize the database
        busNameRepository.saveAndFlush(busName);

        // Get the busName
        restBusNameMockMvc
            .perform(get(ENTITY_API_URL_ID, busName.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(busName.getId().intValue()))
            .andExpect(jsonPath("$.busName").value(DEFAULT_BUS_NAME));
    }

    @Test
    @Transactional
    void getBusNamesByIdFiltering() throws Exception {
        // Initialize the database
        busNameRepository.saveAndFlush(busName);

        Long id = busName.getId();

        defaultBusNameShouldBeFound("id.equals=" + id);
        defaultBusNameShouldNotBeFound("id.notEquals=" + id);

        defaultBusNameShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBusNameShouldNotBeFound("id.greaterThan=" + id);

        defaultBusNameShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBusNameShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBusNamesByBusNameIsEqualToSomething() throws Exception {
        // Initialize the database
        busNameRepository.saveAndFlush(busName);

        // Get all the busNameList where busName equals to DEFAULT_BUS_NAME
        defaultBusNameShouldBeFound("busName.equals=" + DEFAULT_BUS_NAME);

        // Get all the busNameList where busName equals to UPDATED_BUS_NAME
        defaultBusNameShouldNotBeFound("busName.equals=" + UPDATED_BUS_NAME);
    }

    @Test
    @Transactional
    void getAllBusNamesByBusNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busNameRepository.saveAndFlush(busName);

        // Get all the busNameList where busName not equals to DEFAULT_BUS_NAME
        defaultBusNameShouldNotBeFound("busName.notEquals=" + DEFAULT_BUS_NAME);

        // Get all the busNameList where busName not equals to UPDATED_BUS_NAME
        defaultBusNameShouldBeFound("busName.notEquals=" + UPDATED_BUS_NAME);
    }

    @Test
    @Transactional
    void getAllBusNamesByBusNameIsInShouldWork() throws Exception {
        // Initialize the database
        busNameRepository.saveAndFlush(busName);

        // Get all the busNameList where busName in DEFAULT_BUS_NAME or UPDATED_BUS_NAME
        defaultBusNameShouldBeFound("busName.in=" + DEFAULT_BUS_NAME + "," + UPDATED_BUS_NAME);

        // Get all the busNameList where busName equals to UPDATED_BUS_NAME
        defaultBusNameShouldNotBeFound("busName.in=" + UPDATED_BUS_NAME);
    }

    @Test
    @Transactional
    void getAllBusNamesByBusNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        busNameRepository.saveAndFlush(busName);

        // Get all the busNameList where busName is not null
        defaultBusNameShouldBeFound("busName.specified=true");

        // Get all the busNameList where busName is null
        defaultBusNameShouldNotBeFound("busName.specified=false");
    }

    @Test
    @Transactional
    void getAllBusNamesByBusNameContainsSomething() throws Exception {
        // Initialize the database
        busNameRepository.saveAndFlush(busName);

        // Get all the busNameList where busName contains DEFAULT_BUS_NAME
        defaultBusNameShouldBeFound("busName.contains=" + DEFAULT_BUS_NAME);

        // Get all the busNameList where busName contains UPDATED_BUS_NAME
        defaultBusNameShouldNotBeFound("busName.contains=" + UPDATED_BUS_NAME);
    }

    @Test
    @Transactional
    void getAllBusNamesByBusNameNotContainsSomething() throws Exception {
        // Initialize the database
        busNameRepository.saveAndFlush(busName);

        // Get all the busNameList where busName does not contain DEFAULT_BUS_NAME
        defaultBusNameShouldNotBeFound("busName.doesNotContain=" + DEFAULT_BUS_NAME);

        // Get all the busNameList where busName does not contain UPDATED_BUS_NAME
        defaultBusNameShouldBeFound("busName.doesNotContain=" + UPDATED_BUS_NAME);
    }

    @Test
    @Transactional
    void getAllBusNamesByBusIsEqualToSomething() throws Exception {
        // Initialize the database
        busNameRepository.saveAndFlush(busName);
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
        busName.setBus(bus);
        busNameRepository.saveAndFlush(busName);
        Long busId = bus.getId();

        // Get all the busNameList where bus equals to busId
        defaultBusNameShouldBeFound("busId.equals=" + busId);

        // Get all the busNameList where bus equals to (busId + 1)
        defaultBusNameShouldNotBeFound("busId.equals=" + (busId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBusNameShouldBeFound(String filter) throws Exception {
        restBusNameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(busName.getId().intValue())))
            .andExpect(jsonPath("$.[*].busName").value(hasItem(DEFAULT_BUS_NAME)));

        // Check, that the count call also returns 1
        restBusNameMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBusNameShouldNotBeFound(String filter) throws Exception {
        restBusNameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBusNameMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBusName() throws Exception {
        // Get the busName
        restBusNameMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBusName() throws Exception {
        // Initialize the database
        busNameRepository.saveAndFlush(busName);

        int databaseSizeBeforeUpdate = busNameRepository.findAll().size();

        // Update the busName
        BusName updatedBusName = busNameRepository.findById(busName.getId()).get();
        // Disconnect from session so that the updates on updatedBusName are not directly saved in db
        em.detach(updatedBusName);
        updatedBusName.busName(UPDATED_BUS_NAME);
        BusNameDTO busNameDTO = busNameMapper.toDto(updatedBusName);

        restBusNameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, busNameDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busNameDTO))
            )
            .andExpect(status().isOk());

        // Validate the BusName in the database
        List<BusName> busNameList = busNameRepository.findAll();
        assertThat(busNameList).hasSize(databaseSizeBeforeUpdate);
        BusName testBusName = busNameList.get(busNameList.size() - 1);
        assertThat(testBusName.getBusName()).isEqualTo(UPDATED_BUS_NAME);
    }

    @Test
    @Transactional
    void putNonExistingBusName() throws Exception {
        int databaseSizeBeforeUpdate = busNameRepository.findAll().size();
        busName.setId(count.incrementAndGet());

        // Create the BusName
        BusNameDTO busNameDTO = busNameMapper.toDto(busName);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusNameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, busNameDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busNameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusName in the database
        List<BusName> busNameList = busNameRepository.findAll();
        assertThat(busNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBusName() throws Exception {
        int databaseSizeBeforeUpdate = busNameRepository.findAll().size();
        busName.setId(count.incrementAndGet());

        // Create the BusName
        BusNameDTO busNameDTO = busNameMapper.toDto(busName);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusNameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busNameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusName in the database
        List<BusName> busNameList = busNameRepository.findAll();
        assertThat(busNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBusName() throws Exception {
        int databaseSizeBeforeUpdate = busNameRepository.findAll().size();
        busName.setId(count.incrementAndGet());

        // Create the BusName
        BusNameDTO busNameDTO = busNameMapper.toDto(busName);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusNameMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busNameDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusName in the database
        List<BusName> busNameList = busNameRepository.findAll();
        assertThat(busNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBusNameWithPatch() throws Exception {
        // Initialize the database
        busNameRepository.saveAndFlush(busName);

        int databaseSizeBeforeUpdate = busNameRepository.findAll().size();

        // Update the busName using partial update
        BusName partialUpdatedBusName = new BusName();
        partialUpdatedBusName.setId(busName.getId());

        partialUpdatedBusName.busName(UPDATED_BUS_NAME);

        restBusNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusName.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusName))
            )
            .andExpect(status().isOk());

        // Validate the BusName in the database
        List<BusName> busNameList = busNameRepository.findAll();
        assertThat(busNameList).hasSize(databaseSizeBeforeUpdate);
        BusName testBusName = busNameList.get(busNameList.size() - 1);
        assertThat(testBusName.getBusName()).isEqualTo(UPDATED_BUS_NAME);
    }

    @Test
    @Transactional
    void fullUpdateBusNameWithPatch() throws Exception {
        // Initialize the database
        busNameRepository.saveAndFlush(busName);

        int databaseSizeBeforeUpdate = busNameRepository.findAll().size();

        // Update the busName using partial update
        BusName partialUpdatedBusName = new BusName();
        partialUpdatedBusName.setId(busName.getId());

        partialUpdatedBusName.busName(UPDATED_BUS_NAME);

        restBusNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusName.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusName))
            )
            .andExpect(status().isOk());

        // Validate the BusName in the database
        List<BusName> busNameList = busNameRepository.findAll();
        assertThat(busNameList).hasSize(databaseSizeBeforeUpdate);
        BusName testBusName = busNameList.get(busNameList.size() - 1);
        assertThat(testBusName.getBusName()).isEqualTo(UPDATED_BUS_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingBusName() throws Exception {
        int databaseSizeBeforeUpdate = busNameRepository.findAll().size();
        busName.setId(count.incrementAndGet());

        // Create the BusName
        BusNameDTO busNameDTO = busNameMapper.toDto(busName);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, busNameDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(busNameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusName in the database
        List<BusName> busNameList = busNameRepository.findAll();
        assertThat(busNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBusName() throws Exception {
        int databaseSizeBeforeUpdate = busNameRepository.findAll().size();
        busName.setId(count.incrementAndGet());

        // Create the BusName
        BusNameDTO busNameDTO = busNameMapper.toDto(busName);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(busNameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusName in the database
        List<BusName> busNameList = busNameRepository.findAll();
        assertThat(busNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBusName() throws Exception {
        int databaseSizeBeforeUpdate = busNameRepository.findAll().size();
        busName.setId(count.incrementAndGet());

        // Create the BusName
        BusNameDTO busNameDTO = busNameMapper.toDto(busName);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusNameMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(busNameDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusName in the database
        List<BusName> busNameList = busNameRepository.findAll();
        assertThat(busNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBusName() throws Exception {
        // Initialize the database
        busNameRepository.saveAndFlush(busName);

        int databaseSizeBeforeDelete = busNameRepository.findAll().size();

        // Delete the busName
        restBusNameMockMvc
            .perform(delete(ENTITY_API_URL_ID, busName.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BusName> busNameList = busNameRepository.findAll();
        assertThat(busNameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
