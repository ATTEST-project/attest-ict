package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.SolarData;
import com.attest.ict.repository.SolarDataRepository;
import com.attest.ict.service.criteria.SolarDataCriteria;
import com.attest.ict.service.dto.SolarDataDTO;
import com.attest.ict.service.mapper.SolarDataMapper;
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
 * Integration tests for the {@link SolarDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SolarDataResourceIT {

    private static final Double DEFAULT_P = 1D;
    private static final Double UPDATED_P = 2D;
    private static final Double SMALLER_P = 1D - 1D;

    private static final Integer DEFAULT_HOUR = 1;
    private static final Integer UPDATED_HOUR = 2;
    private static final Integer SMALLER_HOUR = 1 - 1;

    private static final String ENTITY_API_URL = "/api/solar-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SolarDataRepository solarDataRepository;

    @Autowired
    private SolarDataMapper solarDataMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSolarDataMockMvc;

    private SolarData solarData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SolarData createEntity(EntityManager em) {
        SolarData solarData = new SolarData().p(DEFAULT_P).hour(DEFAULT_HOUR);
        return solarData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SolarData createUpdatedEntity(EntityManager em) {
        SolarData solarData = new SolarData().p(UPDATED_P).hour(UPDATED_HOUR);
        return solarData;
    }

    @BeforeEach
    public void initTest() {
        solarData = createEntity(em);
    }

    @Test
    @Transactional
    void createSolarData() throws Exception {
        int databaseSizeBeforeCreate = solarDataRepository.findAll().size();
        // Create the SolarData
        SolarDataDTO solarDataDTO = solarDataMapper.toDto(solarData);
        restSolarDataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solarDataDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SolarData in the database
        List<SolarData> solarDataList = solarDataRepository.findAll();
        assertThat(solarDataList).hasSize(databaseSizeBeforeCreate + 1);
        SolarData testSolarData = solarDataList.get(solarDataList.size() - 1);
        assertThat(testSolarData.getP()).isEqualTo(DEFAULT_P);
        assertThat(testSolarData.getHour()).isEqualTo(DEFAULT_HOUR);
    }

    @Test
    @Transactional
    void createSolarDataWithExistingId() throws Exception {
        // Create the SolarData with an existing ID
        solarData.setId(1L);
        SolarDataDTO solarDataDTO = solarDataMapper.toDto(solarData);

        int databaseSizeBeforeCreate = solarDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSolarDataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solarDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SolarData in the database
        List<SolarData> solarDataList = solarDataRepository.findAll();
        assertThat(solarDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSolarData() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        // Get all the solarDataList
        restSolarDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(solarData.getId().intValue())))
            .andExpect(jsonPath("$.[*].p").value(hasItem(DEFAULT_P.doubleValue())))
            .andExpect(jsonPath("$.[*].hour").value(hasItem(DEFAULT_HOUR)));
    }

    @Test
    @Transactional
    void getSolarData() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        // Get the solarData
        restSolarDataMockMvc
            .perform(get(ENTITY_API_URL_ID, solarData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(solarData.getId().intValue()))
            .andExpect(jsonPath("$.p").value(DEFAULT_P.doubleValue()))
            .andExpect(jsonPath("$.hour").value(DEFAULT_HOUR));
    }

    @Test
    @Transactional
    void getSolarDataByIdFiltering() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        Long id = solarData.getId();

        defaultSolarDataShouldBeFound("id.equals=" + id);
        defaultSolarDataShouldNotBeFound("id.notEquals=" + id);

        defaultSolarDataShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSolarDataShouldNotBeFound("id.greaterThan=" + id);

        defaultSolarDataShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSolarDataShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSolarDataByPIsEqualToSomething() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        // Get all the solarDataList where p equals to DEFAULT_P
        defaultSolarDataShouldBeFound("p.equals=" + DEFAULT_P);

        // Get all the solarDataList where p equals to UPDATED_P
        defaultSolarDataShouldNotBeFound("p.equals=" + UPDATED_P);
    }

    @Test
    @Transactional
    void getAllSolarDataByPIsNotEqualToSomething() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        // Get all the solarDataList where p not equals to DEFAULT_P
        defaultSolarDataShouldNotBeFound("p.notEquals=" + DEFAULT_P);

        // Get all the solarDataList where p not equals to UPDATED_P
        defaultSolarDataShouldBeFound("p.notEquals=" + UPDATED_P);
    }

    @Test
    @Transactional
    void getAllSolarDataByPIsInShouldWork() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        // Get all the solarDataList where p in DEFAULT_P or UPDATED_P
        defaultSolarDataShouldBeFound("p.in=" + DEFAULT_P + "," + UPDATED_P);

        // Get all the solarDataList where p equals to UPDATED_P
        defaultSolarDataShouldNotBeFound("p.in=" + UPDATED_P);
    }

    @Test
    @Transactional
    void getAllSolarDataByPIsNullOrNotNull() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        // Get all the solarDataList where p is not null
        defaultSolarDataShouldBeFound("p.specified=true");

        // Get all the solarDataList where p is null
        defaultSolarDataShouldNotBeFound("p.specified=false");
    }

    @Test
    @Transactional
    void getAllSolarDataByPIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        // Get all the solarDataList where p is greater than or equal to DEFAULT_P
        defaultSolarDataShouldBeFound("p.greaterThanOrEqual=" + DEFAULT_P);

        // Get all the solarDataList where p is greater than or equal to UPDATED_P
        defaultSolarDataShouldNotBeFound("p.greaterThanOrEqual=" + UPDATED_P);
    }

    @Test
    @Transactional
    void getAllSolarDataByPIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        // Get all the solarDataList where p is less than or equal to DEFAULT_P
        defaultSolarDataShouldBeFound("p.lessThanOrEqual=" + DEFAULT_P);

        // Get all the solarDataList where p is less than or equal to SMALLER_P
        defaultSolarDataShouldNotBeFound("p.lessThanOrEqual=" + SMALLER_P);
    }

    @Test
    @Transactional
    void getAllSolarDataByPIsLessThanSomething() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        // Get all the solarDataList where p is less than DEFAULT_P
        defaultSolarDataShouldNotBeFound("p.lessThan=" + DEFAULT_P);

        // Get all the solarDataList where p is less than UPDATED_P
        defaultSolarDataShouldBeFound("p.lessThan=" + UPDATED_P);
    }

    @Test
    @Transactional
    void getAllSolarDataByPIsGreaterThanSomething() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        // Get all the solarDataList where p is greater than DEFAULT_P
        defaultSolarDataShouldNotBeFound("p.greaterThan=" + DEFAULT_P);

        // Get all the solarDataList where p is greater than SMALLER_P
        defaultSolarDataShouldBeFound("p.greaterThan=" + SMALLER_P);
    }

    @Test
    @Transactional
    void getAllSolarDataByHourIsEqualToSomething() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        // Get all the solarDataList where hour equals to DEFAULT_HOUR
        defaultSolarDataShouldBeFound("hour.equals=" + DEFAULT_HOUR);

        // Get all the solarDataList where hour equals to UPDATED_HOUR
        defaultSolarDataShouldNotBeFound("hour.equals=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllSolarDataByHourIsNotEqualToSomething() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        // Get all the solarDataList where hour not equals to DEFAULT_HOUR
        defaultSolarDataShouldNotBeFound("hour.notEquals=" + DEFAULT_HOUR);

        // Get all the solarDataList where hour not equals to UPDATED_HOUR
        defaultSolarDataShouldBeFound("hour.notEquals=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllSolarDataByHourIsInShouldWork() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        // Get all the solarDataList where hour in DEFAULT_HOUR or UPDATED_HOUR
        defaultSolarDataShouldBeFound("hour.in=" + DEFAULT_HOUR + "," + UPDATED_HOUR);

        // Get all the solarDataList where hour equals to UPDATED_HOUR
        defaultSolarDataShouldNotBeFound("hour.in=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllSolarDataByHourIsNullOrNotNull() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        // Get all the solarDataList where hour is not null
        defaultSolarDataShouldBeFound("hour.specified=true");

        // Get all the solarDataList where hour is null
        defaultSolarDataShouldNotBeFound("hour.specified=false");
    }

    @Test
    @Transactional
    void getAllSolarDataByHourIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        // Get all the solarDataList where hour is greater than or equal to DEFAULT_HOUR
        defaultSolarDataShouldBeFound("hour.greaterThanOrEqual=" + DEFAULT_HOUR);

        // Get all the solarDataList where hour is greater than or equal to UPDATED_HOUR
        defaultSolarDataShouldNotBeFound("hour.greaterThanOrEqual=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllSolarDataByHourIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        // Get all the solarDataList where hour is less than or equal to DEFAULT_HOUR
        defaultSolarDataShouldBeFound("hour.lessThanOrEqual=" + DEFAULT_HOUR);

        // Get all the solarDataList where hour is less than or equal to SMALLER_HOUR
        defaultSolarDataShouldNotBeFound("hour.lessThanOrEqual=" + SMALLER_HOUR);
    }

    @Test
    @Transactional
    void getAllSolarDataByHourIsLessThanSomething() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        // Get all the solarDataList where hour is less than DEFAULT_HOUR
        defaultSolarDataShouldNotBeFound("hour.lessThan=" + DEFAULT_HOUR);

        // Get all the solarDataList where hour is less than UPDATED_HOUR
        defaultSolarDataShouldBeFound("hour.lessThan=" + UPDATED_HOUR);
    }

    @Test
    @Transactional
    void getAllSolarDataByHourIsGreaterThanSomething() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        // Get all the solarDataList where hour is greater than DEFAULT_HOUR
        defaultSolarDataShouldNotBeFound("hour.greaterThan=" + DEFAULT_HOUR);

        // Get all the solarDataList where hour is greater than SMALLER_HOUR
        defaultSolarDataShouldBeFound("hour.greaterThan=" + SMALLER_HOUR);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSolarDataShouldBeFound(String filter) throws Exception {
        restSolarDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(solarData.getId().intValue())))
            .andExpect(jsonPath("$.[*].p").value(hasItem(DEFAULT_P.doubleValue())))
            .andExpect(jsonPath("$.[*].hour").value(hasItem(DEFAULT_HOUR)));

        // Check, that the count call also returns 1
        restSolarDataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSolarDataShouldNotBeFound(String filter) throws Exception {
        restSolarDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSolarDataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSolarData() throws Exception {
        // Get the solarData
        restSolarDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSolarData() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        int databaseSizeBeforeUpdate = solarDataRepository.findAll().size();

        // Update the solarData
        SolarData updatedSolarData = solarDataRepository.findById(solarData.getId()).get();
        // Disconnect from session so that the updates on updatedSolarData are not directly saved in db
        em.detach(updatedSolarData);
        updatedSolarData.p(UPDATED_P).hour(UPDATED_HOUR);
        SolarDataDTO solarDataDTO = solarDataMapper.toDto(updatedSolarData);

        restSolarDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, solarDataDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solarDataDTO))
            )
            .andExpect(status().isOk());

        // Validate the SolarData in the database
        List<SolarData> solarDataList = solarDataRepository.findAll();
        assertThat(solarDataList).hasSize(databaseSizeBeforeUpdate);
        SolarData testSolarData = solarDataList.get(solarDataList.size() - 1);
        assertThat(testSolarData.getP()).isEqualTo(UPDATED_P);
        assertThat(testSolarData.getHour()).isEqualTo(UPDATED_HOUR);
    }

    @Test
    @Transactional
    void putNonExistingSolarData() throws Exception {
        int databaseSizeBeforeUpdate = solarDataRepository.findAll().size();
        solarData.setId(count.incrementAndGet());

        // Create the SolarData
        SolarDataDTO solarDataDTO = solarDataMapper.toDto(solarData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSolarDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, solarDataDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solarDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SolarData in the database
        List<SolarData> solarDataList = solarDataRepository.findAll();
        assertThat(solarDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSolarData() throws Exception {
        int databaseSizeBeforeUpdate = solarDataRepository.findAll().size();
        solarData.setId(count.incrementAndGet());

        // Create the SolarData
        SolarDataDTO solarDataDTO = solarDataMapper.toDto(solarData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSolarDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solarDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SolarData in the database
        List<SolarData> solarDataList = solarDataRepository.findAll();
        assertThat(solarDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSolarData() throws Exception {
        int databaseSizeBeforeUpdate = solarDataRepository.findAll().size();
        solarData.setId(count.incrementAndGet());

        // Create the SolarData
        SolarDataDTO solarDataDTO = solarDataMapper.toDto(solarData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSolarDataMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solarDataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SolarData in the database
        List<SolarData> solarDataList = solarDataRepository.findAll();
        assertThat(solarDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSolarDataWithPatch() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        int databaseSizeBeforeUpdate = solarDataRepository.findAll().size();

        // Update the solarData using partial update
        SolarData partialUpdatedSolarData = new SolarData();
        partialUpdatedSolarData.setId(solarData.getId());

        partialUpdatedSolarData.hour(UPDATED_HOUR);

        restSolarDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSolarData.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSolarData))
            )
            .andExpect(status().isOk());

        // Validate the SolarData in the database
        List<SolarData> solarDataList = solarDataRepository.findAll();
        assertThat(solarDataList).hasSize(databaseSizeBeforeUpdate);
        SolarData testSolarData = solarDataList.get(solarDataList.size() - 1);
        assertThat(testSolarData.getP()).isEqualTo(DEFAULT_P);
        assertThat(testSolarData.getHour()).isEqualTo(UPDATED_HOUR);
    }

    @Test
    @Transactional
    void fullUpdateSolarDataWithPatch() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        int databaseSizeBeforeUpdate = solarDataRepository.findAll().size();

        // Update the solarData using partial update
        SolarData partialUpdatedSolarData = new SolarData();
        partialUpdatedSolarData.setId(solarData.getId());

        partialUpdatedSolarData.p(UPDATED_P).hour(UPDATED_HOUR);

        restSolarDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSolarData.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSolarData))
            )
            .andExpect(status().isOk());

        // Validate the SolarData in the database
        List<SolarData> solarDataList = solarDataRepository.findAll();
        assertThat(solarDataList).hasSize(databaseSizeBeforeUpdate);
        SolarData testSolarData = solarDataList.get(solarDataList.size() - 1);
        assertThat(testSolarData.getP()).isEqualTo(UPDATED_P);
        assertThat(testSolarData.getHour()).isEqualTo(UPDATED_HOUR);
    }

    @Test
    @Transactional
    void patchNonExistingSolarData() throws Exception {
        int databaseSizeBeforeUpdate = solarDataRepository.findAll().size();
        solarData.setId(count.incrementAndGet());

        // Create the SolarData
        SolarDataDTO solarDataDTO = solarDataMapper.toDto(solarData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSolarDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, solarDataDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(solarDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SolarData in the database
        List<SolarData> solarDataList = solarDataRepository.findAll();
        assertThat(solarDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSolarData() throws Exception {
        int databaseSizeBeforeUpdate = solarDataRepository.findAll().size();
        solarData.setId(count.incrementAndGet());

        // Create the SolarData
        SolarDataDTO solarDataDTO = solarDataMapper.toDto(solarData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSolarDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(solarDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SolarData in the database
        List<SolarData> solarDataList = solarDataRepository.findAll();
        assertThat(solarDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSolarData() throws Exception {
        int databaseSizeBeforeUpdate = solarDataRepository.findAll().size();
        solarData.setId(count.incrementAndGet());

        // Create the SolarData
        SolarDataDTO solarDataDTO = solarDataMapper.toDto(solarData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSolarDataMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(solarDataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SolarData in the database
        List<SolarData> solarDataList = solarDataRepository.findAll();
        assertThat(solarDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSolarData() throws Exception {
        // Initialize the database
        solarDataRepository.saveAndFlush(solarData);

        int databaseSizeBeforeDelete = solarDataRepository.findAll().size();

        // Delete the solarData
        restSolarDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, solarData.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SolarData> solarDataList = solarDataRepository.findAll();
        assertThat(solarDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
