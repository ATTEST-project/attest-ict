package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.WindData;
import com.attest.ict.repository.WindDataRepository;
import com.attest.ict.service.criteria.WindDataCriteria;
import com.attest.ict.service.dto.WindDataDTO;
import com.attest.ict.service.mapper.WindDataMapper;
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
 * Integration tests for the {@link WindDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WindDataResourceIT {

    private static final Double DEFAULT_WIND_SPEED = 1D;
    private static final Double UPDATED_WIND_SPEED = 2D;
    private static final Double SMALLER_WIND_SPEED = 1D - 1D;

    private static final Integer DEFAULT_HOUR = 1;
    private static final Integer UPDATED_HOUR = 2;
    private static final Integer SMALLER_HOUR = 1 - 1;

    private static final String ENTITY_API_URL = "/api/wind-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WindDataRepository windDataRepository;

    @Autowired
    private WindDataMapper windDataMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWindDataMockMvc;

    private WindData windData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WindData createEntity(EntityManager em) {
        WindData windData = new WindData().windSpeed(DEFAULT_WIND_SPEED).hour(DEFAULT_HOUR);
        return windData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WindData createUpdatedEntity(EntityManager em) {
        WindData windData = new WindData().windSpeed(UPDATED_WIND_SPEED).hour(UPDATED_HOUR);
        return windData;
    }

    @BeforeEach
    public void initTest() {
        windData = createEntity(em);
    }

    @Test
    @Transactional
    void createWindData() throws Exception {
        int databaseSizeBeforeCreate = windDataRepository.findAll().size();
        // Create the WindData
        WindDataDTO windDataDTO = windDataMapper.toDto(windData);
        restWindDataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(windDataDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WindData in the database
        List<WindData> windDataList = windDataRepository.findAll();
        assertThat(windDataList).hasSize(databaseSizeBeforeCreate + 1);
        WindData testWindData = windDataList.get(windDataList.size() - 1);
        assertThat(testWindData.getWindSpeed()).isEqualTo(DEFAULT_WIND_SPEED);
        assertThat(testWindData.getHour()).isEqualTo(DEFAULT_HOUR);
    }

    @Test
    @Transactional
    void createWindDataWithExistingId() throws Exception {
        // Create the WindData with an existing ID
        windData.setId(1L);
        WindDataDTO windDataDTO = windDataMapper.toDto(windData);

        int databaseSizeBeforeCreate = windDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWindDataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(windDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WindData in the database
        List<WindData> windDataList = windDataRepository.findAll();
        assertThat(windDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWindData() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        // Get all the windDataList
        restWindDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(windData.getId().intValue())))
            .andExpect(jsonPath("$.[*].windSpeed").value(hasItem(DEFAULT_WIND_SPEED.doubleValue())))
            .andExpect(jsonPath("$.[*].hour").value(hasItem(DEFAULT_HOUR)));
    }

    @Test
    @Transactional
    void getWindData() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        // Get the windData
        restWindDataMockMvc
            .perform(get(ENTITY_API_URL_ID, windData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(windData.getId().intValue()))
            .andExpect(jsonPath("$.windSpeed").value(DEFAULT_WIND_SPEED.doubleValue()))
            .andExpect(jsonPath("$.hour").value(DEFAULT_HOUR));
    }

    @Test
    @Transactional
    void getWindDataByIdFiltering() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        Long id = windData.getId();

        defaultWindDataShouldBeFound("id.equals=" + id);
        defaultWindDataShouldNotBeFound("id.notEquals=" + id);

        defaultWindDataShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWindDataShouldNotBeFound("id.greaterThan=" + id);

        defaultWindDataShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWindDataShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWindDataByWindSpeedIsEqualToSomething() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        // Get all the windDataList where windSpeed equals to DEFAULT_WIND_SPEED
        defaultWindDataShouldBeFound("windSpeed.equals=" + DEFAULT_WIND_SPEED);

        // Get all the windDataList where windSpeed equals to UPDATED_WIND_SPEED
        defaultWindDataShouldNotBeFound("windSpeed.equals=" + UPDATED_WIND_SPEED);
    }

    @Test
    @Transactional
    void getAllWindDataByWindSpeedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        // Get all the windDataList where windSpeed not equals to DEFAULT_WIND_SPEED
        defaultWindDataShouldNotBeFound("windSpeed.notEquals=" + DEFAULT_WIND_SPEED);

        // Get all the windDataList where windSpeed not equals to UPDATED_WIND_SPEED
        defaultWindDataShouldBeFound("windSpeed.notEquals=" + UPDATED_WIND_SPEED);
    }

    @Test
    @Transactional
    void getAllWindDataByWindSpeedIsInShouldWork() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        // Get all the windDataList where windSpeed in DEFAULT_WIND_SPEED or UPDATED_WIND_SPEED
        defaultWindDataShouldBeFound("windSpeed.in=" + DEFAULT_WIND_SPEED + "," + UPDATED_WIND_SPEED);

        // Get all the windDataList where windSpeed equals to UPDATED_WIND_SPEED
        defaultWindDataShouldNotBeFound("windSpeed.in=" + UPDATED_WIND_SPEED);
    }

    @Test
    @Transactional
    void getAllWindDataByWindSpeedIsNullOrNotNull() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        // Get all the windDataList where windSpeed is not null
        defaultWindDataShouldBeFound("windSpeed.specified=true");

        // Get all the windDataList where windSpeed is null
        defaultWindDataShouldNotBeFound("windSpeed.specified=false");
    }

    @Test
    @Transactional
    void getAllWindDataByWindSpeedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        // Get all the windDataList where windSpeed is greater than or equal to DEFAULT_WIND_SPEED
        defaultWindDataShouldBeFound("windSpeed.greaterThanOrEqual=" + DEFAULT_WIND_SPEED);

        // Get all the windDataList where windSpeed is greater than or equal to UPDATED_WIND_SPEED
        defaultWindDataShouldNotBeFound("windSpeed.greaterThanOrEqual=" + UPDATED_WIND_SPEED);
    }

    @Test
    @Transactional
    void getAllWindDataByWindSpeedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        // Get all the windDataList where windSpeed is less than or equal to DEFAULT_WIND_SPEED
        defaultWindDataShouldBeFound("windSpeed.lessThanOrEqual=" + DEFAULT_WIND_SPEED);

        // Get all the windDataList where windSpeed is less than or equal to SMALLER_WIND_SPEED
        defaultWindDataShouldNotBeFound("windSpeed.lessThanOrEqual=" + SMALLER_WIND_SPEED);
    }

    @Test
    @Transactional
    void getAllWindDataByWindSpeedIsLessThanSomething() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        // Get all the windDataList where windSpeed is less than DEFAULT_WIND_SPEED
        defaultWindDataShouldNotBeFound("windSpeed.lessThan=" + DEFAULT_WIND_SPEED);

        // Get all the windDataList where windSpeed is less than UPDATED_WIND_SPEED
        defaultWindDataShouldBeFound("windSpeed.lessThan=" + UPDATED_WIND_SPEED);
    }

    @Test
    @Transactional
    void getAllWindDataByWindSpeedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        // Get all the windDataList where windSpeed is greater than DEFAULT_WIND_SPEED
        defaultWindDataShouldNotBeFound("windSpeed.greaterThan=" + DEFAULT_WIND_SPEED);

        // Get all the windDataList where windSpeed is greater than SMALLER_WIND_SPEED
        defaultWindDataShouldBeFound("windSpeed.greaterThan=" + SMALLER_WIND_SPEED);
    }

    @Test
    @Transactional
    void getAllWindDataByHourIsEqualToSomething() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        // Get all the windDataList where hour equals to DEFAULT_HOUR
        defaultWindDataShouldBeFound("hour.equals=" + DEFAULT_HOUR);

        // Get all the windDataList where hour equals to UPDATED_HOUR
        defaultWindDataShouldNotBeFound("hour.equals=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllWindDataByHourIsNotEqualToSomething() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        // Get all the windDataList where hour not equals to DEFAULT_HOUR
        defaultWindDataShouldNotBeFound("hour.notEquals=" + DEFAULT_HOUR);

        // Get all the windDataList where hour not equals to UPDATED_HOUR
        defaultWindDataShouldBeFound("hour.notEquals=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllWindDataByHourIsInShouldWork() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        // Get all the windDataList where hour in DEFAULT_HOUR or UPDATED_HOUR
        defaultWindDataShouldBeFound("hour.in=" + DEFAULT_HOUR + "," + UPDATED_HOUR);

        // Get all the windDataList where hour equals to UPDATED_HOUR
        defaultWindDataShouldNotBeFound("hour.in=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllWindDataByHourIsNullOrNotNull() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        // Get all the windDataList where hour is not null
        defaultWindDataShouldBeFound("hour.specified=true");

        // Get all the windDataList where hour is null
        defaultWindDataShouldNotBeFound("hour.specified=false");
    }

    @Test
    @Transactional
    void getAllWindDataByHourIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        // Get all the windDataList where hour is greater than or equal to DEFAULT_HOUR
        defaultWindDataShouldBeFound("hour.greaterThanOrEqual=" + DEFAULT_HOUR);

        // Get all the windDataList where hour is greater than or equal to UPDATED_HOUR
        defaultWindDataShouldNotBeFound("hour.greaterThanOrEqual=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllWindDataByHourIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        // Get all the windDataList where hour is less than or equal to DEFAULT_HOUR
        defaultWindDataShouldBeFound("hour.lessThanOrEqual=" + DEFAULT_HOUR);

        // Get all the windDataList where hour is less than or equal to SMALLER_HOUR
        defaultWindDataShouldNotBeFound("hour.lessThanOrEqual=" + SMALLER_HOUR);
    }

    @Test
    @Transactional
    void getAllWindDataByHourIsLessThanSomething() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        // Get all the windDataList where hour is less than DEFAULT_HOUR
        defaultWindDataShouldNotBeFound("hour.lessThan=" + DEFAULT_HOUR);

        // Get all the windDataList where hour is less than UPDATED_HOUR
        defaultWindDataShouldBeFound("hour.lessThan=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllWindDataByHourIsGreaterThanSomething() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        // Get all the windDataList where hour is greater than DEFAULT_HOUR
        defaultWindDataShouldNotBeFound("hour.greaterThan=" + DEFAULT_HOUR);

        // Get all the windDataList where hour is greater than SMALLER_HOUR
        defaultWindDataShouldBeFound("hour.greaterThan=" + SMALLER_HOUR);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWindDataShouldBeFound(String filter) throws Exception {
        restWindDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(windData.getId().intValue())))
            .andExpect(jsonPath("$.[*].windSpeed").value(hasItem(DEFAULT_WIND_SPEED.doubleValue())))
            .andExpect(jsonPath("$.[*].hour").value(hasItem(DEFAULT_HOUR)));

        // Check, that the count call also returns 1
        restWindDataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWindDataShouldNotBeFound(String filter) throws Exception {
        restWindDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWindDataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWindData() throws Exception {
        // Get the windData
        restWindDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWindData() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        int databaseSizeBeforeUpdate = windDataRepository.findAll().size();

        // Update the windData
        WindData updatedWindData = windDataRepository.findById(windData.getId()).get();
        // Disconnect from session so that the updates on updatedWindData are not directly saved in db
        em.detach(updatedWindData);
        updatedWindData.windSpeed(UPDATED_WIND_SPEED).hour(UPDATED_HOUR);
        WindDataDTO windDataDTO = windDataMapper.toDto(updatedWindData);

        restWindDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, windDataDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(windDataDTO))
            )
            .andExpect(status().isOk());

        // Validate the WindData in the database
        List<WindData> windDataList = windDataRepository.findAll();
        assertThat(windDataList).hasSize(databaseSizeBeforeUpdate);
        WindData testWindData = windDataList.get(windDataList.size() - 1);
        assertThat(testWindData.getWindSpeed()).isEqualTo(UPDATED_WIND_SPEED);
        assertThat(testWindData.getHour()).isEqualTo(UPDATED_HOUR);
    }

    @Test
    @Transactional
    void putNonExistingWindData() throws Exception {
        int databaseSizeBeforeUpdate = windDataRepository.findAll().size();
        windData.setId(count.incrementAndGet());

        // Create the WindData
        WindDataDTO windDataDTO = windDataMapper.toDto(windData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWindDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, windDataDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(windDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WindData in the database
        List<WindData> windDataList = windDataRepository.findAll();
        assertThat(windDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWindData() throws Exception {
        int databaseSizeBeforeUpdate = windDataRepository.findAll().size();
        windData.setId(count.incrementAndGet());

        // Create the WindData
        WindDataDTO windDataDTO = windDataMapper.toDto(windData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWindDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(windDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WindData in the database
        List<WindData> windDataList = windDataRepository.findAll();
        assertThat(windDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWindData() throws Exception {
        int databaseSizeBeforeUpdate = windDataRepository.findAll().size();
        windData.setId(count.incrementAndGet());

        // Create the WindData
        WindDataDTO windDataDTO = windDataMapper.toDto(windData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWindDataMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(windDataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WindData in the database
        List<WindData> windDataList = windDataRepository.findAll();
        assertThat(windDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWindDataWithPatch() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        int databaseSizeBeforeUpdate = windDataRepository.findAll().size();

        // Update the windData using partial update
        WindData partialUpdatedWindData = new WindData();
        partialUpdatedWindData.setId(windData.getId());

        partialUpdatedWindData.windSpeed(UPDATED_WIND_SPEED);

        restWindDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWindData.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWindData))
            )
            .andExpect(status().isOk());

        // Validate the WindData in the database
        List<WindData> windDataList = windDataRepository.findAll();
        assertThat(windDataList).hasSize(databaseSizeBeforeUpdate);
        WindData testWindData = windDataList.get(windDataList.size() - 1);
        assertThat(testWindData.getWindSpeed()).isEqualTo(UPDATED_WIND_SPEED);
        assertThat(testWindData.getHour()).isEqualTo(DEFAULT_HOUR);
    }

    @Test
    @Transactional
    void fullUpdateWindDataWithPatch() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        int databaseSizeBeforeUpdate = windDataRepository.findAll().size();

        // Update the windData using partial update
        WindData partialUpdatedWindData = new WindData();
        partialUpdatedWindData.setId(windData.getId());

        partialUpdatedWindData.windSpeed(UPDATED_WIND_SPEED).hour(UPDATED_HOUR);

        restWindDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWindData.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWindData))
            )
            .andExpect(status().isOk());

        // Validate the WindData in the database
        List<WindData> windDataList = windDataRepository.findAll();
        assertThat(windDataList).hasSize(databaseSizeBeforeUpdate);
        WindData testWindData = windDataList.get(windDataList.size() - 1);
        assertThat(testWindData.getWindSpeed()).isEqualTo(UPDATED_WIND_SPEED);
        assertThat(testWindData.getHour()).isEqualTo(UPDATED_HOUR);
    }

    @Test
    @Transactional
    void patchNonExistingWindData() throws Exception {
        int databaseSizeBeforeUpdate = windDataRepository.findAll().size();
        windData.setId(count.incrementAndGet());

        // Create the WindData
        WindDataDTO windDataDTO = windDataMapper.toDto(windData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWindDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, windDataDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(windDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WindData in the database
        List<WindData> windDataList = windDataRepository.findAll();
        assertThat(windDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWindData() throws Exception {
        int databaseSizeBeforeUpdate = windDataRepository.findAll().size();
        windData.setId(count.incrementAndGet());

        // Create the WindData
        WindDataDTO windDataDTO = windDataMapper.toDto(windData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWindDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(windDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WindData in the database
        List<WindData> windDataList = windDataRepository.findAll();
        assertThat(windDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWindData() throws Exception {
        int databaseSizeBeforeUpdate = windDataRepository.findAll().size();
        windData.setId(count.incrementAndGet());

        // Create the WindData
        WindDataDTO windDataDTO = windDataMapper.toDto(windData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWindDataMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(windDataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WindData in the database
        List<WindData> windDataList = windDataRepository.findAll();
        assertThat(windDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWindData() throws Exception {
        // Initialize the database
        windDataRepository.saveAndFlush(windData);

        int databaseSizeBeforeDelete = windDataRepository.findAll().size();

        // Delete the windData
        restWindDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, windData.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WindData> windDataList = windDataRepository.findAll();
        assertThat(windDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
