package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.BusCoordinate;
import com.attest.ict.repository.BusCoordinateRepository;
import com.attest.ict.service.criteria.BusCoordinateCriteria;
import com.attest.ict.service.dto.BusCoordinateDTO;
import com.attest.ict.service.mapper.BusCoordinateMapper;
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
 * Integration tests for the {@link BusCoordinateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BusCoordinateResourceIT {

    private static final Double DEFAULT_X = 1D;
    private static final Double UPDATED_X = 2D;
    private static final Double SMALLER_X = 1D - 1D;

    private static final Double DEFAULT_Y = 1D;
    private static final Double UPDATED_Y = 2D;
    private static final Double SMALLER_Y = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/bus-coordinates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BusCoordinateRepository busCoordinateRepository;

    @Autowired
    private BusCoordinateMapper busCoordinateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusCoordinateMockMvc;

    private BusCoordinate busCoordinate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusCoordinate createEntity(EntityManager em) {
        BusCoordinate busCoordinate = new BusCoordinate().x(DEFAULT_X).y(DEFAULT_Y);
        return busCoordinate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusCoordinate createUpdatedEntity(EntityManager em) {
        BusCoordinate busCoordinate = new BusCoordinate().x(UPDATED_X).y(UPDATED_Y);
        return busCoordinate;
    }

    @BeforeEach
    public void initTest() {
        busCoordinate = createEntity(em);
    }

    @Test
    @Transactional
    void createBusCoordinate() throws Exception {
        int databaseSizeBeforeCreate = busCoordinateRepository.findAll().size();
        // Create the BusCoordinate
        BusCoordinateDTO busCoordinateDTO = busCoordinateMapper.toDto(busCoordinate);
        restBusCoordinateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busCoordinateDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BusCoordinate in the database
        List<BusCoordinate> busCoordinateList = busCoordinateRepository.findAll();
        assertThat(busCoordinateList).hasSize(databaseSizeBeforeCreate + 1);
        BusCoordinate testBusCoordinate = busCoordinateList.get(busCoordinateList.size() - 1);
        assertThat(testBusCoordinate.getX()).isEqualTo(DEFAULT_X);
        assertThat(testBusCoordinate.getY()).isEqualTo(DEFAULT_Y);
    }

    @Test
    @Transactional
    void createBusCoordinateWithExistingId() throws Exception {
        // Create the BusCoordinate with an existing ID
        busCoordinate.setId(1L);
        BusCoordinateDTO busCoordinateDTO = busCoordinateMapper.toDto(busCoordinate);

        int databaseSizeBeforeCreate = busCoordinateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusCoordinateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busCoordinateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusCoordinate in the database
        List<BusCoordinate> busCoordinateList = busCoordinateRepository.findAll();
        assertThat(busCoordinateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBusCoordinates() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        // Get all the busCoordinateList
        restBusCoordinateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(busCoordinate.getId().intValue())))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X.doubleValue())))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y.doubleValue())));
    }

    @Test
    @Transactional
    void getBusCoordinate() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        // Get the busCoordinate
        restBusCoordinateMockMvc
            .perform(get(ENTITY_API_URL_ID, busCoordinate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(busCoordinate.getId().intValue()))
            .andExpect(jsonPath("$.x").value(DEFAULT_X.doubleValue()))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y.doubleValue()));
    }

    @Test
    @Transactional
    void getBusCoordinatesByIdFiltering() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        Long id = busCoordinate.getId();

        defaultBusCoordinateShouldBeFound("id.equals=" + id);
        defaultBusCoordinateShouldNotBeFound("id.notEquals=" + id);

        defaultBusCoordinateShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBusCoordinateShouldNotBeFound("id.greaterThan=" + id);

        defaultBusCoordinateShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBusCoordinateShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBusCoordinatesByXIsEqualToSomething() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        // Get all the busCoordinateList where x equals to DEFAULT_X
        defaultBusCoordinateShouldBeFound("x.equals=" + DEFAULT_X);

        // Get all the busCoordinateList where x equals to UPDATED_X
        defaultBusCoordinateShouldNotBeFound("x.equals=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllBusCoordinatesByXIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        // Get all the busCoordinateList where x not equals to DEFAULT_X
        defaultBusCoordinateShouldNotBeFound("x.notEquals=" + DEFAULT_X);

        // Get all the busCoordinateList where x not equals to UPDATED_X
        defaultBusCoordinateShouldBeFound("x.notEquals=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllBusCoordinatesByXIsInShouldWork() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        // Get all the busCoordinateList where x in DEFAULT_X or UPDATED_X
        defaultBusCoordinateShouldBeFound("x.in=" + DEFAULT_X + "," + UPDATED_X);

        // Get all the busCoordinateList where x equals to UPDATED_X
        defaultBusCoordinateShouldNotBeFound("x.in=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllBusCoordinatesByXIsNullOrNotNull() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        // Get all the busCoordinateList where x is not null
        defaultBusCoordinateShouldBeFound("x.specified=true");

        // Get all the busCoordinateList where x is null
        defaultBusCoordinateShouldNotBeFound("x.specified=false");
    }

    @Test
    @Transactional
    void getAllBusCoordinatesByXIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        // Get all the busCoordinateList where x is greater than or equal to DEFAULT_X
        defaultBusCoordinateShouldBeFound("x.greaterThanOrEqual=" + DEFAULT_X);

        // Get all the busCoordinateList where x is greater than or equal to UPDATED_X
        defaultBusCoordinateShouldNotBeFound("x.greaterThanOrEqual=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllBusCoordinatesByXIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        // Get all the busCoordinateList where x is less than or equal to DEFAULT_X
        defaultBusCoordinateShouldBeFound("x.lessThanOrEqual=" + DEFAULT_X);

        // Get all the busCoordinateList where x is less than or equal to SMALLER_X
        defaultBusCoordinateShouldNotBeFound("x.lessThanOrEqual=" + SMALLER_X);
    }

    @Test
    @Transactional
    void getAllBusCoordinatesByXIsLessThanSomething() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        // Get all the busCoordinateList where x is less than DEFAULT_X
        defaultBusCoordinateShouldNotBeFound("x.lessThan=" + DEFAULT_X);

        // Get all the busCoordinateList where x is less than UPDATED_X
        defaultBusCoordinateShouldBeFound("x.lessThan=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllBusCoordinatesByXIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        // Get all the busCoordinateList where x is greater than DEFAULT_X
        defaultBusCoordinateShouldNotBeFound("x.greaterThan=" + DEFAULT_X);

        // Get all the busCoordinateList where x is greater than SMALLER_X
        defaultBusCoordinateShouldBeFound("x.greaterThan=" + SMALLER_X);
    }

    @Test
    @Transactional
    void getAllBusCoordinatesByYIsEqualToSomething() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        // Get all the busCoordinateList where y equals to DEFAULT_Y
        defaultBusCoordinateShouldBeFound("y.equals=" + DEFAULT_Y);

        // Get all the busCoordinateList where y equals to UPDATED_Y
        defaultBusCoordinateShouldNotBeFound("y.equals=" + UPDATED_Y);
    }

    @Test
    @Transactional
    void getAllBusCoordinatesByYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        // Get all the busCoordinateList where y not equals to DEFAULT_Y
        defaultBusCoordinateShouldNotBeFound("y.notEquals=" + DEFAULT_Y);

        // Get all the busCoordinateList where y not equals to UPDATED_Y
        defaultBusCoordinateShouldBeFound("y.notEquals=" + UPDATED_Y);
    }

    @Test
    @Transactional
    void getAllBusCoordinatesByYIsInShouldWork() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        // Get all the busCoordinateList where y in DEFAULT_Y or UPDATED_Y
        defaultBusCoordinateShouldBeFound("y.in=" + DEFAULT_Y + "," + UPDATED_Y);

        // Get all the busCoordinateList where y equals to UPDATED_Y
        defaultBusCoordinateShouldNotBeFound("y.in=" + UPDATED_Y);
    }

    @Test
    @Transactional
    void getAllBusCoordinatesByYIsNullOrNotNull() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        // Get all the busCoordinateList where y is not null
        defaultBusCoordinateShouldBeFound("y.specified=true");

        // Get all the busCoordinateList where y is null
        defaultBusCoordinateShouldNotBeFound("y.specified=false");
    }

    @Test
    @Transactional
    void getAllBusCoordinatesByYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        // Get all the busCoordinateList where y is greater than or equal to DEFAULT_Y
        defaultBusCoordinateShouldBeFound("y.greaterThanOrEqual=" + DEFAULT_Y);

        // Get all the busCoordinateList where y is greater than or equal to UPDATED_Y
        defaultBusCoordinateShouldNotBeFound("y.greaterThanOrEqual=" + UPDATED_Y);
    }

    @Test
    @Transactional
    void getAllBusCoordinatesByYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        // Get all the busCoordinateList where y is less than or equal to DEFAULT_Y
        defaultBusCoordinateShouldBeFound("y.lessThanOrEqual=" + DEFAULT_Y);

        // Get all the busCoordinateList where y is less than or equal to SMALLER_Y
        defaultBusCoordinateShouldNotBeFound("y.lessThanOrEqual=" + SMALLER_Y);
    }

    @Test
    @Transactional
    void getAllBusCoordinatesByYIsLessThanSomething() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        // Get all the busCoordinateList where y is less than DEFAULT_Y
        defaultBusCoordinateShouldNotBeFound("y.lessThan=" + DEFAULT_Y);

        // Get all the busCoordinateList where y is less than UPDATED_Y
        defaultBusCoordinateShouldBeFound("y.lessThan=" + UPDATED_Y);
    }

    @Test
    @Transactional
    void getAllBusCoordinatesByYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        // Get all the busCoordinateList where y is greater than DEFAULT_Y
        defaultBusCoordinateShouldNotBeFound("y.greaterThan=" + DEFAULT_Y);

        // Get all the busCoordinateList where y is greater than SMALLER_Y
        defaultBusCoordinateShouldBeFound("y.greaterThan=" + SMALLER_Y);
    }

    @Test
    @Transactional
    void getAllBusCoordinatesByBusIsEqualToSomething() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);
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
        busCoordinate.setBus(bus);
        busCoordinateRepository.saveAndFlush(busCoordinate);
        Long busId = bus.getId();

        // Get all the busCoordinateList where bus equals to busId
        defaultBusCoordinateShouldBeFound("busId.equals=" + busId);

        // Get all the busCoordinateList where bus equals to (busId + 1)
        defaultBusCoordinateShouldNotBeFound("busId.equals=" + (busId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBusCoordinateShouldBeFound(String filter) throws Exception {
        restBusCoordinateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(busCoordinate.getId().intValue())))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X.doubleValue())))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y.doubleValue())));

        // Check, that the count call also returns 1
        restBusCoordinateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBusCoordinateShouldNotBeFound(String filter) throws Exception {
        restBusCoordinateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBusCoordinateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBusCoordinate() throws Exception {
        // Get the busCoordinate
        restBusCoordinateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBusCoordinate() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        int databaseSizeBeforeUpdate = busCoordinateRepository.findAll().size();

        // Update the busCoordinate
        BusCoordinate updatedBusCoordinate = busCoordinateRepository.findById(busCoordinate.getId()).get();
        // Disconnect from session so that the updates on updatedBusCoordinate are not directly saved in db
        em.detach(updatedBusCoordinate);
        updatedBusCoordinate.x(UPDATED_X).y(UPDATED_Y);
        BusCoordinateDTO busCoordinateDTO = busCoordinateMapper.toDto(updatedBusCoordinate);

        restBusCoordinateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, busCoordinateDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busCoordinateDTO))
            )
            .andExpect(status().isOk());

        // Validate the BusCoordinate in the database
        List<BusCoordinate> busCoordinateList = busCoordinateRepository.findAll();
        assertThat(busCoordinateList).hasSize(databaseSizeBeforeUpdate);
        BusCoordinate testBusCoordinate = busCoordinateList.get(busCoordinateList.size() - 1);
        assertThat(testBusCoordinate.getX()).isEqualTo(UPDATED_X);
        assertThat(testBusCoordinate.getY()).isEqualTo(UPDATED_Y);
    }

    @Test
    @Transactional
    void putNonExistingBusCoordinate() throws Exception {
        int databaseSizeBeforeUpdate = busCoordinateRepository.findAll().size();
        busCoordinate.setId(count.incrementAndGet());

        // Create the BusCoordinate
        BusCoordinateDTO busCoordinateDTO = busCoordinateMapper.toDto(busCoordinate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusCoordinateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, busCoordinateDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busCoordinateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusCoordinate in the database
        List<BusCoordinate> busCoordinateList = busCoordinateRepository.findAll();
        assertThat(busCoordinateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBusCoordinate() throws Exception {
        int databaseSizeBeforeUpdate = busCoordinateRepository.findAll().size();
        busCoordinate.setId(count.incrementAndGet());

        // Create the BusCoordinate
        BusCoordinateDTO busCoordinateDTO = busCoordinateMapper.toDto(busCoordinate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusCoordinateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busCoordinateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusCoordinate in the database
        List<BusCoordinate> busCoordinateList = busCoordinateRepository.findAll();
        assertThat(busCoordinateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBusCoordinate() throws Exception {
        int databaseSizeBeforeUpdate = busCoordinateRepository.findAll().size();
        busCoordinate.setId(count.incrementAndGet());

        // Create the BusCoordinate
        BusCoordinateDTO busCoordinateDTO = busCoordinateMapper.toDto(busCoordinate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusCoordinateMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busCoordinateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusCoordinate in the database
        List<BusCoordinate> busCoordinateList = busCoordinateRepository.findAll();
        assertThat(busCoordinateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBusCoordinateWithPatch() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        int databaseSizeBeforeUpdate = busCoordinateRepository.findAll().size();

        // Update the busCoordinate using partial update
        BusCoordinate partialUpdatedBusCoordinate = new BusCoordinate();
        partialUpdatedBusCoordinate.setId(busCoordinate.getId());

        partialUpdatedBusCoordinate.x(UPDATED_X);

        restBusCoordinateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusCoordinate.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusCoordinate))
            )
            .andExpect(status().isOk());

        // Validate the BusCoordinate in the database
        List<BusCoordinate> busCoordinateList = busCoordinateRepository.findAll();
        assertThat(busCoordinateList).hasSize(databaseSizeBeforeUpdate);
        BusCoordinate testBusCoordinate = busCoordinateList.get(busCoordinateList.size() - 1);
        assertThat(testBusCoordinate.getX()).isEqualTo(UPDATED_X);
        assertThat(testBusCoordinate.getY()).isEqualTo(DEFAULT_Y);
    }

    @Test
    @Transactional
    void fullUpdateBusCoordinateWithPatch() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        int databaseSizeBeforeUpdate = busCoordinateRepository.findAll().size();

        // Update the busCoordinate using partial update
        BusCoordinate partialUpdatedBusCoordinate = new BusCoordinate();
        partialUpdatedBusCoordinate.setId(busCoordinate.getId());

        partialUpdatedBusCoordinate.x(UPDATED_X).y(UPDATED_Y);

        restBusCoordinateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusCoordinate.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusCoordinate))
            )
            .andExpect(status().isOk());

        // Validate the BusCoordinate in the database
        List<BusCoordinate> busCoordinateList = busCoordinateRepository.findAll();
        assertThat(busCoordinateList).hasSize(databaseSizeBeforeUpdate);
        BusCoordinate testBusCoordinate = busCoordinateList.get(busCoordinateList.size() - 1);
        assertThat(testBusCoordinate.getX()).isEqualTo(UPDATED_X);
        assertThat(testBusCoordinate.getY()).isEqualTo(UPDATED_Y);
    }

    @Test
    @Transactional
    void patchNonExistingBusCoordinate() throws Exception {
        int databaseSizeBeforeUpdate = busCoordinateRepository.findAll().size();
        busCoordinate.setId(count.incrementAndGet());

        // Create the BusCoordinate
        BusCoordinateDTO busCoordinateDTO = busCoordinateMapper.toDto(busCoordinate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusCoordinateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, busCoordinateDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(busCoordinateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusCoordinate in the database
        List<BusCoordinate> busCoordinateList = busCoordinateRepository.findAll();
        assertThat(busCoordinateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBusCoordinate() throws Exception {
        int databaseSizeBeforeUpdate = busCoordinateRepository.findAll().size();
        busCoordinate.setId(count.incrementAndGet());

        // Create the BusCoordinate
        BusCoordinateDTO busCoordinateDTO = busCoordinateMapper.toDto(busCoordinate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusCoordinateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(busCoordinateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusCoordinate in the database
        List<BusCoordinate> busCoordinateList = busCoordinateRepository.findAll();
        assertThat(busCoordinateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBusCoordinate() throws Exception {
        int databaseSizeBeforeUpdate = busCoordinateRepository.findAll().size();
        busCoordinate.setId(count.incrementAndGet());

        // Create the BusCoordinate
        BusCoordinateDTO busCoordinateDTO = busCoordinateMapper.toDto(busCoordinate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusCoordinateMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(busCoordinateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusCoordinate in the database
        List<BusCoordinate> busCoordinateList = busCoordinateRepository.findAll();
        assertThat(busCoordinateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBusCoordinate() throws Exception {
        // Initialize the database
        busCoordinateRepository.saveAndFlush(busCoordinate);

        int databaseSizeBeforeDelete = busCoordinateRepository.findAll().size();

        // Delete the busCoordinate
        restBusCoordinateMockMvc
            .perform(delete(ENTITY_API_URL_ID, busCoordinate.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BusCoordinate> busCoordinateList = busCoordinateRepository.findAll();
        assertThat(busCoordinateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
