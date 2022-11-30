package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.WeatherForecast;
import com.attest.ict.repository.WeatherForecastRepository;
import com.attest.ict.service.criteria.WeatherForecastCriteria;
import com.attest.ict.service.dto.WeatherForecastDTO;
import com.attest.ict.service.mapper.WeatherForecastMapper;
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
 * Integration tests for the {@link WeatherForecastResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WeatherForecastResourceIT {

    private static final Double DEFAULT_SOLAR_PROFILE = 1D;
    private static final Double UPDATED_SOLAR_PROFILE = 2D;
    private static final Double SMALLER_SOLAR_PROFILE = 1D - 1D;

    private static final Double DEFAULT_OUTSIDE_TEMP = 1D;
    private static final Double UPDATED_OUTSIDE_TEMP = 2D;
    private static final Double SMALLER_OUTSIDE_TEMP = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/weather-forecasts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WeatherForecastRepository weatherForecastRepository;

    @Autowired
    private WeatherForecastMapper weatherForecastMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWeatherForecastMockMvc;

    private WeatherForecast weatherForecast;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WeatherForecast createEntity(EntityManager em) {
        WeatherForecast weatherForecast = new WeatherForecast().solarProfile(DEFAULT_SOLAR_PROFILE).outsideTemp(DEFAULT_OUTSIDE_TEMP);
        return weatherForecast;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WeatherForecast createUpdatedEntity(EntityManager em) {
        WeatherForecast weatherForecast = new WeatherForecast().solarProfile(UPDATED_SOLAR_PROFILE).outsideTemp(UPDATED_OUTSIDE_TEMP);
        return weatherForecast;
    }

    @BeforeEach
    public void initTest() {
        weatherForecast = createEntity(em);
    }

    @Test
    @Transactional
    void createWeatherForecast() throws Exception {
        int databaseSizeBeforeCreate = weatherForecastRepository.findAll().size();
        // Create the WeatherForecast
        WeatherForecastDTO weatherForecastDTO = weatherForecastMapper.toDto(weatherForecast);
        restWeatherForecastMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weatherForecastDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WeatherForecast in the database
        List<WeatherForecast> weatherForecastList = weatherForecastRepository.findAll();
        assertThat(weatherForecastList).hasSize(databaseSizeBeforeCreate + 1);
        WeatherForecast testWeatherForecast = weatherForecastList.get(weatherForecastList.size() - 1);
        assertThat(testWeatherForecast.getSolarProfile()).isEqualTo(DEFAULT_SOLAR_PROFILE);
        assertThat(testWeatherForecast.getOutsideTemp()).isEqualTo(DEFAULT_OUTSIDE_TEMP);
    }

    @Test
    @Transactional
    void createWeatherForecastWithExistingId() throws Exception {
        // Create the WeatherForecast with an existing ID
        weatherForecast.setId(1L);
        WeatherForecastDTO weatherForecastDTO = weatherForecastMapper.toDto(weatherForecast);

        int databaseSizeBeforeCreate = weatherForecastRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeatherForecastMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weatherForecastDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeatherForecast in the database
        List<WeatherForecast> weatherForecastList = weatherForecastRepository.findAll();
        assertThat(weatherForecastList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWeatherForecasts() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        // Get all the weatherForecastList
        restWeatherForecastMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weatherForecast.getId().intValue())))
            .andExpect(jsonPath("$.[*].solarProfile").value(hasItem(DEFAULT_SOLAR_PROFILE.doubleValue())))
            .andExpect(jsonPath("$.[*].outsideTemp").value(hasItem(DEFAULT_OUTSIDE_TEMP.doubleValue())));
    }

    @Test
    @Transactional
    void getWeatherForecast() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        // Get the weatherForecast
        restWeatherForecastMockMvc
            .perform(get(ENTITY_API_URL_ID, weatherForecast.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(weatherForecast.getId().intValue()))
            .andExpect(jsonPath("$.solarProfile").value(DEFAULT_SOLAR_PROFILE.doubleValue()))
            .andExpect(jsonPath("$.outsideTemp").value(DEFAULT_OUTSIDE_TEMP.doubleValue()));
    }

    @Test
    @Transactional
    void getWeatherForecastsByIdFiltering() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        Long id = weatherForecast.getId();

        defaultWeatherForecastShouldBeFound("id.equals=" + id);
        defaultWeatherForecastShouldNotBeFound("id.notEquals=" + id);

        defaultWeatherForecastShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWeatherForecastShouldNotBeFound("id.greaterThan=" + id);

        defaultWeatherForecastShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWeatherForecastShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWeatherForecastsBySolarProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        // Get all the weatherForecastList where solarProfile equals to DEFAULT_SOLAR_PROFILE
        defaultWeatherForecastShouldBeFound("solarProfile.equals=" + DEFAULT_SOLAR_PROFILE);

        // Get all the weatherForecastList where solarProfile equals to UPDATED_SOLAR_PROFILE
        defaultWeatherForecastShouldNotBeFound("solarProfile.equals=" + UPDATED_SOLAR_PROFILE);
    }

    @Test
    @Transactional
    void getAllWeatherForecastsBySolarProfileIsNotEqualToSomething() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        // Get all the weatherForecastList where solarProfile not equals to DEFAULT_SOLAR_PROFILE
        defaultWeatherForecastShouldNotBeFound("solarProfile.notEquals=" + DEFAULT_SOLAR_PROFILE);

        // Get all the weatherForecastList where solarProfile not equals to UPDATED_SOLAR_PROFILE
        defaultWeatherForecastShouldBeFound("solarProfile.notEquals=" + UPDATED_SOLAR_PROFILE);
    }

    @Test
    @Transactional
    void getAllWeatherForecastsBySolarProfileIsInShouldWork() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        // Get all the weatherForecastList where solarProfile in DEFAULT_SOLAR_PROFILE or UPDATED_SOLAR_PROFILE
        defaultWeatherForecastShouldBeFound("solarProfile.in=" + DEFAULT_SOLAR_PROFILE + "," + UPDATED_SOLAR_PROFILE);

        // Get all the weatherForecastList where solarProfile equals to UPDATED_SOLAR_PROFILE
        defaultWeatherForecastShouldNotBeFound("solarProfile.in=" + UPDATED_SOLAR_PROFILE);
    }

    @Test
    @Transactional
    void getAllWeatherForecastsBySolarProfileIsNullOrNotNull() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        // Get all the weatherForecastList where solarProfile is not null
        defaultWeatherForecastShouldBeFound("solarProfile.specified=true");

        // Get all the weatherForecastList where solarProfile is null
        defaultWeatherForecastShouldNotBeFound("solarProfile.specified=false");
    }

    @Test
    @Transactional
    void getAllWeatherForecastsBySolarProfileIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        // Get all the weatherForecastList where solarProfile is greater than or equal to DEFAULT_SOLAR_PROFILE
        defaultWeatherForecastShouldBeFound("solarProfile.greaterThanOrEqual=" + DEFAULT_SOLAR_PROFILE);

        // Get all the weatherForecastList where solarProfile is greater than or equal to UPDATED_SOLAR_PROFILE
        defaultWeatherForecastShouldNotBeFound("solarProfile.greaterThanOrEqual=" + UPDATED_SOLAR_PROFILE);
    }

    @Test
    @Transactional
    void getAllWeatherForecastsBySolarProfileIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        // Get all the weatherForecastList where solarProfile is less than or equal to DEFAULT_SOLAR_PROFILE
        defaultWeatherForecastShouldBeFound("solarProfile.lessThanOrEqual=" + DEFAULT_SOLAR_PROFILE);

        // Get all the weatherForecastList where solarProfile is less than or equal to SMALLER_SOLAR_PROFILE
        defaultWeatherForecastShouldNotBeFound("solarProfile.lessThanOrEqual=" + SMALLER_SOLAR_PROFILE);
    }

    @Test
    @Transactional
    void getAllWeatherForecastsBySolarProfileIsLessThanSomething() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        // Get all the weatherForecastList where solarProfile is less than DEFAULT_SOLAR_PROFILE
        defaultWeatherForecastShouldNotBeFound("solarProfile.lessThan=" + DEFAULT_SOLAR_PROFILE);

        // Get all the weatherForecastList where solarProfile is less than UPDATED_SOLAR_PROFILE
        defaultWeatherForecastShouldBeFound("solarProfile.lessThan=" + UPDATED_SOLAR_PROFILE);
    }

    @Test
    @Transactional
    void getAllWeatherForecastsBySolarProfileIsGreaterThanSomething() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        // Get all the weatherForecastList where solarProfile is greater than DEFAULT_SOLAR_PROFILE
        defaultWeatherForecastShouldNotBeFound("solarProfile.greaterThan=" + DEFAULT_SOLAR_PROFILE);

        // Get all the weatherForecastList where solarProfile is greater than SMALLER_SOLAR_PROFILE
        defaultWeatherForecastShouldBeFound("solarProfile.greaterThan=" + SMALLER_SOLAR_PROFILE);
    }

    @Test
    @Transactional
    void getAllWeatherForecastsByOutsideTempIsEqualToSomething() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        // Get all the weatherForecastList where outsideTemp equals to DEFAULT_OUTSIDE_TEMP
        defaultWeatherForecastShouldBeFound("outsideTemp.equals=" + DEFAULT_OUTSIDE_TEMP);

        // Get all the weatherForecastList where outsideTemp equals to UPDATED_OUTSIDE_TEMP
        defaultWeatherForecastShouldNotBeFound("outsideTemp.equals=" + UPDATED_OUTSIDE_TEMP);
    }

    @Test
    @Transactional
    void getAllWeatherForecastsByOutsideTempIsNotEqualToSomething() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        // Get all the weatherForecastList where outsideTemp not equals to DEFAULT_OUTSIDE_TEMP
        defaultWeatherForecastShouldNotBeFound("outsideTemp.notEquals=" + DEFAULT_OUTSIDE_TEMP);

        // Get all the weatherForecastList where outsideTemp not equals to UPDATED_OUTSIDE_TEMP
        defaultWeatherForecastShouldBeFound("outsideTemp.notEquals=" + UPDATED_OUTSIDE_TEMP);
    }

    @Test
    @Transactional
    void getAllWeatherForecastsByOutsideTempIsInShouldWork() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        // Get all the weatherForecastList where outsideTemp in DEFAULT_OUTSIDE_TEMP or UPDATED_OUTSIDE_TEMP
        defaultWeatherForecastShouldBeFound("outsideTemp.in=" + DEFAULT_OUTSIDE_TEMP + "," + UPDATED_OUTSIDE_TEMP);

        // Get all the weatherForecastList where outsideTemp equals to UPDATED_OUTSIDE_TEMP
        defaultWeatherForecastShouldNotBeFound("outsideTemp.in=" + UPDATED_OUTSIDE_TEMP);
    }

    @Test
    @Transactional
    void getAllWeatherForecastsByOutsideTempIsNullOrNotNull() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        // Get all the weatherForecastList where outsideTemp is not null
        defaultWeatherForecastShouldBeFound("outsideTemp.specified=true");

        // Get all the weatherForecastList where outsideTemp is null
        defaultWeatherForecastShouldNotBeFound("outsideTemp.specified=false");
    }

    @Test
    @Transactional
    void getAllWeatherForecastsByOutsideTempIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        // Get all the weatherForecastList where outsideTemp is greater than or equal to DEFAULT_OUTSIDE_TEMP
        defaultWeatherForecastShouldBeFound("outsideTemp.greaterThanOrEqual=" + DEFAULT_OUTSIDE_TEMP);

        // Get all the weatherForecastList where outsideTemp is greater than or equal to UPDATED_OUTSIDE_TEMP
        defaultWeatherForecastShouldNotBeFound("outsideTemp.greaterThanOrEqual=" + UPDATED_OUTSIDE_TEMP);
    }

    @Test
    @Transactional
    void getAllWeatherForecastsByOutsideTempIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        // Get all the weatherForecastList where outsideTemp is less than or equal to DEFAULT_OUTSIDE_TEMP
        defaultWeatherForecastShouldBeFound("outsideTemp.lessThanOrEqual=" + DEFAULT_OUTSIDE_TEMP);

        // Get all the weatherForecastList where outsideTemp is less than or equal to SMALLER_OUTSIDE_TEMP
        defaultWeatherForecastShouldNotBeFound("outsideTemp.lessThanOrEqual=" + SMALLER_OUTSIDE_TEMP);
    }

    @Test
    @Transactional
    void getAllWeatherForecastsByOutsideTempIsLessThanSomething() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        // Get all the weatherForecastList where outsideTemp is less than DEFAULT_OUTSIDE_TEMP
        defaultWeatherForecastShouldNotBeFound("outsideTemp.lessThan=" + DEFAULT_OUTSIDE_TEMP);

        // Get all the weatherForecastList where outsideTemp is less than UPDATED_OUTSIDE_TEMP
        defaultWeatherForecastShouldBeFound("outsideTemp.lessThan=" + UPDATED_OUTSIDE_TEMP);
    }

    @Test
    @Transactional
    void getAllWeatherForecastsByOutsideTempIsGreaterThanSomething() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        // Get all the weatherForecastList where outsideTemp is greater than DEFAULT_OUTSIDE_TEMP
        defaultWeatherForecastShouldNotBeFound("outsideTemp.greaterThan=" + DEFAULT_OUTSIDE_TEMP);

        // Get all the weatherForecastList where outsideTemp is greater than SMALLER_OUTSIDE_TEMP
        defaultWeatherForecastShouldBeFound("outsideTemp.greaterThan=" + SMALLER_OUTSIDE_TEMP);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWeatherForecastShouldBeFound(String filter) throws Exception {
        restWeatherForecastMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weatherForecast.getId().intValue())))
            .andExpect(jsonPath("$.[*].solarProfile").value(hasItem(DEFAULT_SOLAR_PROFILE.doubleValue())))
            .andExpect(jsonPath("$.[*].outsideTemp").value(hasItem(DEFAULT_OUTSIDE_TEMP.doubleValue())));

        // Check, that the count call also returns 1
        restWeatherForecastMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWeatherForecastShouldNotBeFound(String filter) throws Exception {
        restWeatherForecastMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWeatherForecastMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWeatherForecast() throws Exception {
        // Get the weatherForecast
        restWeatherForecastMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWeatherForecast() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        int databaseSizeBeforeUpdate = weatherForecastRepository.findAll().size();

        // Update the weatherForecast
        WeatherForecast updatedWeatherForecast = weatherForecastRepository.findById(weatherForecast.getId()).get();
        // Disconnect from session so that the updates on updatedWeatherForecast are not directly saved in db
        em.detach(updatedWeatherForecast);
        updatedWeatherForecast.solarProfile(UPDATED_SOLAR_PROFILE).outsideTemp(UPDATED_OUTSIDE_TEMP);
        WeatherForecastDTO weatherForecastDTO = weatherForecastMapper.toDto(updatedWeatherForecast);

        restWeatherForecastMockMvc
            .perform(
                put(ENTITY_API_URL_ID, weatherForecastDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weatherForecastDTO))
            )
            .andExpect(status().isOk());

        // Validate the WeatherForecast in the database
        List<WeatherForecast> weatherForecastList = weatherForecastRepository.findAll();
        assertThat(weatherForecastList).hasSize(databaseSizeBeforeUpdate);
        WeatherForecast testWeatherForecast = weatherForecastList.get(weatherForecastList.size() - 1);
        assertThat(testWeatherForecast.getSolarProfile()).isEqualTo(UPDATED_SOLAR_PROFILE);
        assertThat(testWeatherForecast.getOutsideTemp()).isEqualTo(UPDATED_OUTSIDE_TEMP);
    }

    @Test
    @Transactional
    void putNonExistingWeatherForecast() throws Exception {
        int databaseSizeBeforeUpdate = weatherForecastRepository.findAll().size();
        weatherForecast.setId(count.incrementAndGet());

        // Create the WeatherForecast
        WeatherForecastDTO weatherForecastDTO = weatherForecastMapper.toDto(weatherForecast);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeatherForecastMockMvc
            .perform(
                put(ENTITY_API_URL_ID, weatherForecastDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weatherForecastDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeatherForecast in the database
        List<WeatherForecast> weatherForecastList = weatherForecastRepository.findAll();
        assertThat(weatherForecastList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWeatherForecast() throws Exception {
        int databaseSizeBeforeUpdate = weatherForecastRepository.findAll().size();
        weatherForecast.setId(count.incrementAndGet());

        // Create the WeatherForecast
        WeatherForecastDTO weatherForecastDTO = weatherForecastMapper.toDto(weatherForecast);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeatherForecastMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weatherForecastDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeatherForecast in the database
        List<WeatherForecast> weatherForecastList = weatherForecastRepository.findAll();
        assertThat(weatherForecastList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWeatherForecast() throws Exception {
        int databaseSizeBeforeUpdate = weatherForecastRepository.findAll().size();
        weatherForecast.setId(count.incrementAndGet());

        // Create the WeatherForecast
        WeatherForecastDTO weatherForecastDTO = weatherForecastMapper.toDto(weatherForecast);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeatherForecastMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weatherForecastDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WeatherForecast in the database
        List<WeatherForecast> weatherForecastList = weatherForecastRepository.findAll();
        assertThat(weatherForecastList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWeatherForecastWithPatch() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        int databaseSizeBeforeUpdate = weatherForecastRepository.findAll().size();

        // Update the weatherForecast using partial update
        WeatherForecast partialUpdatedWeatherForecast = new WeatherForecast();
        partialUpdatedWeatherForecast.setId(weatherForecast.getId());

        partialUpdatedWeatherForecast.solarProfile(UPDATED_SOLAR_PROFILE).outsideTemp(UPDATED_OUTSIDE_TEMP);

        restWeatherForecastMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeatherForecast.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWeatherForecast))
            )
            .andExpect(status().isOk());

        // Validate the WeatherForecast in the database
        List<WeatherForecast> weatherForecastList = weatherForecastRepository.findAll();
        assertThat(weatherForecastList).hasSize(databaseSizeBeforeUpdate);
        WeatherForecast testWeatherForecast = weatherForecastList.get(weatherForecastList.size() - 1);
        assertThat(testWeatherForecast.getSolarProfile()).isEqualTo(UPDATED_SOLAR_PROFILE);
        assertThat(testWeatherForecast.getOutsideTemp()).isEqualTo(UPDATED_OUTSIDE_TEMP);
    }

    @Test
    @Transactional
    void fullUpdateWeatherForecastWithPatch() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        int databaseSizeBeforeUpdate = weatherForecastRepository.findAll().size();

        // Update the weatherForecast using partial update
        WeatherForecast partialUpdatedWeatherForecast = new WeatherForecast();
        partialUpdatedWeatherForecast.setId(weatherForecast.getId());

        partialUpdatedWeatherForecast.solarProfile(UPDATED_SOLAR_PROFILE).outsideTemp(UPDATED_OUTSIDE_TEMP);

        restWeatherForecastMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeatherForecast.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWeatherForecast))
            )
            .andExpect(status().isOk());

        // Validate the WeatherForecast in the database
        List<WeatherForecast> weatherForecastList = weatherForecastRepository.findAll();
        assertThat(weatherForecastList).hasSize(databaseSizeBeforeUpdate);
        WeatherForecast testWeatherForecast = weatherForecastList.get(weatherForecastList.size() - 1);
        assertThat(testWeatherForecast.getSolarProfile()).isEqualTo(UPDATED_SOLAR_PROFILE);
        assertThat(testWeatherForecast.getOutsideTemp()).isEqualTo(UPDATED_OUTSIDE_TEMP);
    }

    @Test
    @Transactional
    void patchNonExistingWeatherForecast() throws Exception {
        int databaseSizeBeforeUpdate = weatherForecastRepository.findAll().size();
        weatherForecast.setId(count.incrementAndGet());

        // Create the WeatherForecast
        WeatherForecastDTO weatherForecastDTO = weatherForecastMapper.toDto(weatherForecast);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeatherForecastMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, weatherForecastDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weatherForecastDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeatherForecast in the database
        List<WeatherForecast> weatherForecastList = weatherForecastRepository.findAll();
        assertThat(weatherForecastList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWeatherForecast() throws Exception {
        int databaseSizeBeforeUpdate = weatherForecastRepository.findAll().size();
        weatherForecast.setId(count.incrementAndGet());

        // Create the WeatherForecast
        WeatherForecastDTO weatherForecastDTO = weatherForecastMapper.toDto(weatherForecast);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeatherForecastMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weatherForecastDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeatherForecast in the database
        List<WeatherForecast> weatherForecastList = weatherForecastRepository.findAll();
        assertThat(weatherForecastList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWeatherForecast() throws Exception {
        int databaseSizeBeforeUpdate = weatherForecastRepository.findAll().size();
        weatherForecast.setId(count.incrementAndGet());

        // Create the WeatherForecast
        WeatherForecastDTO weatherForecastDTO = weatherForecastMapper.toDto(weatherForecast);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeatherForecastMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weatherForecastDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WeatherForecast in the database
        List<WeatherForecast> weatherForecastList = weatherForecastRepository.findAll();
        assertThat(weatherForecastList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWeatherForecast() throws Exception {
        // Initialize the database
        weatherForecastRepository.saveAndFlush(weatherForecast);

        int databaseSizeBeforeDelete = weatherForecastRepository.findAll().size();

        // Delete the weatherForecast
        restWeatherForecastMockMvc
            .perform(delete(ENTITY_API_URL_ID, weatherForecast.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WeatherForecast> weatherForecastList = weatherForecastRepository.findAll();
        assertThat(weatherForecastList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
