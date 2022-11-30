package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.InputFile;
import com.attest.ict.domain.LoadElVal;
import com.attest.ict.domain.LoadProfile;
import com.attest.ict.domain.Network;
import com.attest.ict.repository.LoadProfileRepository;
import com.attest.ict.service.criteria.LoadProfileCriteria;
import com.attest.ict.service.dto.LoadProfileDTO;
import com.attest.ict.service.mapper.LoadProfileMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link LoadProfileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LoadProfileResourceIT {

    private static final String DEFAULT_SEASON = "AAAAAAAAAA";
    private static final String UPDATED_SEASON = "BBBBBBBBBB";

    private static final String DEFAULT_TYPICAL_DAY = "AAAAAAAAAA";
    private static final String UPDATED_TYPICAL_DAY = "BBBBBBBBBB";

    private static final Integer DEFAULT_MODE = 1;
    private static final Integer UPDATED_MODE = 2;
    private static final Integer SMALLER_MODE = 1 - 1;

    private static final Double DEFAULT_TIME_INTERVAL = 1D;
    private static final Double UPDATED_TIME_INTERVAL = 2D;
    private static final Double SMALLER_TIME_INTERVAL = 1D - 1D;

    private static final Instant DEFAULT_UPLOAD_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPLOAD_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/load-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LoadProfileRepository loadProfileRepository;

    @Autowired
    private LoadProfileMapper loadProfileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoadProfileMockMvc;

    private LoadProfile loadProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoadProfile createEntity(EntityManager em) {
        LoadProfile loadProfile = new LoadProfile()
            .season(DEFAULT_SEASON)
            .typicalDay(DEFAULT_TYPICAL_DAY)
            .mode(DEFAULT_MODE)
            .timeInterval(DEFAULT_TIME_INTERVAL)
            .uploadDateTime(DEFAULT_UPLOAD_DATE_TIME);
        return loadProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoadProfile createUpdatedEntity(EntityManager em) {
        LoadProfile loadProfile = new LoadProfile()
            .season(UPDATED_SEASON)
            .typicalDay(UPDATED_TYPICAL_DAY)
            .mode(UPDATED_MODE)
            .timeInterval(UPDATED_TIME_INTERVAL)
            .uploadDateTime(UPDATED_UPLOAD_DATE_TIME);
        return loadProfile;
    }

    @BeforeEach
    public void initTest() {
        loadProfile = createEntity(em);
    }

    @Test
    @Transactional
    void createLoadProfile() throws Exception {
        int databaseSizeBeforeCreate = loadProfileRepository.findAll().size();
        // Create the LoadProfile
        LoadProfileDTO loadProfileDTO = loadProfileMapper.toDto(loadProfile);
        restLoadProfileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loadProfileDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LoadProfile in the database
        List<LoadProfile> loadProfileList = loadProfileRepository.findAll();
        assertThat(loadProfileList).hasSize(databaseSizeBeforeCreate + 1);
        LoadProfile testLoadProfile = loadProfileList.get(loadProfileList.size() - 1);
        assertThat(testLoadProfile.getSeason()).isEqualTo(DEFAULT_SEASON);
        assertThat(testLoadProfile.getTypicalDay()).isEqualTo(DEFAULT_TYPICAL_DAY);
        assertThat(testLoadProfile.getMode()).isEqualTo(DEFAULT_MODE);
        assertThat(testLoadProfile.getTimeInterval()).isEqualTo(DEFAULT_TIME_INTERVAL);
        assertThat(testLoadProfile.getUploadDateTime()).isEqualTo(DEFAULT_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void createLoadProfileWithExistingId() throws Exception {
        // Create the LoadProfile with an existing ID
        loadProfile.setId(1L);
        LoadProfileDTO loadProfileDTO = loadProfileMapper.toDto(loadProfile);

        int databaseSizeBeforeCreate = loadProfileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoadProfileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loadProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoadProfile in the database
        List<LoadProfile> loadProfileList = loadProfileRepository.findAll();
        assertThat(loadProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLoadProfiles() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList
        restLoadProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loadProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].season").value(hasItem(DEFAULT_SEASON)))
            .andExpect(jsonPath("$.[*].typicalDay").value(hasItem(DEFAULT_TYPICAL_DAY)))
            .andExpect(jsonPath("$.[*].mode").value(hasItem(DEFAULT_MODE)))
            .andExpect(jsonPath("$.[*].timeInterval").value(hasItem(DEFAULT_TIME_INTERVAL.doubleValue())))
            .andExpect(jsonPath("$.[*].uploadDateTime").value(hasItem(DEFAULT_UPLOAD_DATE_TIME.toString())));
    }

    @Test
    @Transactional
    void getLoadProfile() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get the loadProfile
        restLoadProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, loadProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loadProfile.getId().intValue()))
            .andExpect(jsonPath("$.season").value(DEFAULT_SEASON))
            .andExpect(jsonPath("$.typicalDay").value(DEFAULT_TYPICAL_DAY))
            .andExpect(jsonPath("$.mode").value(DEFAULT_MODE))
            .andExpect(jsonPath("$.timeInterval").value(DEFAULT_TIME_INTERVAL.doubleValue()))
            .andExpect(jsonPath("$.uploadDateTime").value(DEFAULT_UPLOAD_DATE_TIME.toString()));
    }

    @Test
    @Transactional
    void getLoadProfilesByIdFiltering() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        Long id = loadProfile.getId();

        defaultLoadProfileShouldBeFound("id.equals=" + id);
        defaultLoadProfileShouldNotBeFound("id.notEquals=" + id);

        defaultLoadProfileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLoadProfileShouldNotBeFound("id.greaterThan=" + id);

        defaultLoadProfileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLoadProfileShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLoadProfilesBySeasonIsEqualToSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where season equals to DEFAULT_SEASON
        defaultLoadProfileShouldBeFound("season.equals=" + DEFAULT_SEASON);

        // Get all the loadProfileList where season equals to UPDATED_SEASON
        defaultLoadProfileShouldNotBeFound("season.equals=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllLoadProfilesBySeasonIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where season not equals to DEFAULT_SEASON
        defaultLoadProfileShouldNotBeFound("season.notEquals=" + DEFAULT_SEASON);

        // Get all the loadProfileList where season not equals to UPDATED_SEASON
        defaultLoadProfileShouldBeFound("season.notEquals=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllLoadProfilesBySeasonIsInShouldWork() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where season in DEFAULT_SEASON or UPDATED_SEASON
        defaultLoadProfileShouldBeFound("season.in=" + DEFAULT_SEASON + "," + UPDATED_SEASON);

        // Get all the loadProfileList where season equals to UPDATED_SEASON
        defaultLoadProfileShouldNotBeFound("season.in=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllLoadProfilesBySeasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where season is not null
        defaultLoadProfileShouldBeFound("season.specified=true");

        // Get all the loadProfileList where season is null
        defaultLoadProfileShouldNotBeFound("season.specified=false");
    }

    @Test
    @Transactional
    void getAllLoadProfilesBySeasonContainsSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where season contains DEFAULT_SEASON
        defaultLoadProfileShouldBeFound("season.contains=" + DEFAULT_SEASON);

        // Get all the loadProfileList where season contains UPDATED_SEASON
        defaultLoadProfileShouldNotBeFound("season.contains=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllLoadProfilesBySeasonNotContainsSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where season does not contain DEFAULT_SEASON
        defaultLoadProfileShouldNotBeFound("season.doesNotContain=" + DEFAULT_SEASON);

        // Get all the loadProfileList where season does not contain UPDATED_SEASON
        defaultLoadProfileShouldBeFound("season.doesNotContain=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByTypicalDayIsEqualToSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where typicalDay equals to DEFAULT_TYPICAL_DAY
        defaultLoadProfileShouldBeFound("typicalDay.equals=" + DEFAULT_TYPICAL_DAY);

        // Get all the loadProfileList where typicalDay equals to UPDATED_TYPICAL_DAY
        defaultLoadProfileShouldNotBeFound("typicalDay.equals=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByTypicalDayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where typicalDay not equals to DEFAULT_TYPICAL_DAY
        defaultLoadProfileShouldNotBeFound("typicalDay.notEquals=" + DEFAULT_TYPICAL_DAY);

        // Get all the loadProfileList where typicalDay not equals to UPDATED_TYPICAL_DAY
        defaultLoadProfileShouldBeFound("typicalDay.notEquals=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByTypicalDayIsInShouldWork() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where typicalDay in DEFAULT_TYPICAL_DAY or UPDATED_TYPICAL_DAY
        defaultLoadProfileShouldBeFound("typicalDay.in=" + DEFAULT_TYPICAL_DAY + "," + UPDATED_TYPICAL_DAY);

        // Get all the loadProfileList where typicalDay equals to UPDATED_TYPICAL_DAY
        defaultLoadProfileShouldNotBeFound("typicalDay.in=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByTypicalDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where typicalDay is not null
        defaultLoadProfileShouldBeFound("typicalDay.specified=true");

        // Get all the loadProfileList where typicalDay is null
        defaultLoadProfileShouldNotBeFound("typicalDay.specified=false");
    }

    @Test
    @Transactional
    void getAllLoadProfilesByTypicalDayContainsSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where typicalDay contains DEFAULT_TYPICAL_DAY
        defaultLoadProfileShouldBeFound("typicalDay.contains=" + DEFAULT_TYPICAL_DAY);

        // Get all the loadProfileList where typicalDay contains UPDATED_TYPICAL_DAY
        defaultLoadProfileShouldNotBeFound("typicalDay.contains=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByTypicalDayNotContainsSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where typicalDay does not contain DEFAULT_TYPICAL_DAY
        defaultLoadProfileShouldNotBeFound("typicalDay.doesNotContain=" + DEFAULT_TYPICAL_DAY);

        // Get all the loadProfileList where typicalDay does not contain UPDATED_TYPICAL_DAY
        defaultLoadProfileShouldBeFound("typicalDay.doesNotContain=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByModeIsEqualToSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where mode equals to DEFAULT_MODE
        defaultLoadProfileShouldBeFound("mode.equals=" + DEFAULT_MODE);

        // Get all the loadProfileList where mode equals to UPDATED_MODE
        defaultLoadProfileShouldNotBeFound("mode.equals=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByModeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where mode not equals to DEFAULT_MODE
        defaultLoadProfileShouldNotBeFound("mode.notEquals=" + DEFAULT_MODE);

        // Get all the loadProfileList where mode not equals to UPDATED_MODE
        defaultLoadProfileShouldBeFound("mode.notEquals=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByModeIsInShouldWork() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where mode in DEFAULT_MODE or UPDATED_MODE
        defaultLoadProfileShouldBeFound("mode.in=" + DEFAULT_MODE + "," + UPDATED_MODE);

        // Get all the loadProfileList where mode equals to UPDATED_MODE
        defaultLoadProfileShouldNotBeFound("mode.in=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByModeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where mode is not null
        defaultLoadProfileShouldBeFound("mode.specified=true");

        // Get all the loadProfileList where mode is null
        defaultLoadProfileShouldNotBeFound("mode.specified=false");
    }

    @Test
    @Transactional
    void getAllLoadProfilesByModeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where mode is greater than or equal to DEFAULT_MODE
        defaultLoadProfileShouldBeFound("mode.greaterThanOrEqual=" + DEFAULT_MODE);

        // Get all the loadProfileList where mode is greater than or equal to UPDATED_MODE
        defaultLoadProfileShouldNotBeFound("mode.greaterThanOrEqual=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByModeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where mode is less than or equal to DEFAULT_MODE
        defaultLoadProfileShouldBeFound("mode.lessThanOrEqual=" + DEFAULT_MODE);

        // Get all the loadProfileList where mode is less than or equal to SMALLER_MODE
        defaultLoadProfileShouldNotBeFound("mode.lessThanOrEqual=" + SMALLER_MODE);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByModeIsLessThanSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where mode is less than DEFAULT_MODE
        defaultLoadProfileShouldNotBeFound("mode.lessThan=" + DEFAULT_MODE);

        // Get all the loadProfileList where mode is less than UPDATED_MODE
        defaultLoadProfileShouldBeFound("mode.lessThan=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByModeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where mode is greater than DEFAULT_MODE
        defaultLoadProfileShouldNotBeFound("mode.greaterThan=" + DEFAULT_MODE);

        // Get all the loadProfileList where mode is greater than SMALLER_MODE
        defaultLoadProfileShouldBeFound("mode.greaterThan=" + SMALLER_MODE);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByTimeIntervalIsEqualToSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where timeInterval equals to DEFAULT_TIME_INTERVAL
        defaultLoadProfileShouldBeFound("timeInterval.equals=" + DEFAULT_TIME_INTERVAL);

        // Get all the loadProfileList where timeInterval equals to UPDATED_TIME_INTERVAL
        defaultLoadProfileShouldNotBeFound("timeInterval.equals=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByTimeIntervalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where timeInterval not equals to DEFAULT_TIME_INTERVAL
        defaultLoadProfileShouldNotBeFound("timeInterval.notEquals=" + DEFAULT_TIME_INTERVAL);

        // Get all the loadProfileList where timeInterval not equals to UPDATED_TIME_INTERVAL
        defaultLoadProfileShouldBeFound("timeInterval.notEquals=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByTimeIntervalIsInShouldWork() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where timeInterval in DEFAULT_TIME_INTERVAL or UPDATED_TIME_INTERVAL
        defaultLoadProfileShouldBeFound("timeInterval.in=" + DEFAULT_TIME_INTERVAL + "," + UPDATED_TIME_INTERVAL);

        // Get all the loadProfileList where timeInterval equals to UPDATED_TIME_INTERVAL
        defaultLoadProfileShouldNotBeFound("timeInterval.in=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByTimeIntervalIsNullOrNotNull() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where timeInterval is not null
        defaultLoadProfileShouldBeFound("timeInterval.specified=true");

        // Get all the loadProfileList where timeInterval is null
        defaultLoadProfileShouldNotBeFound("timeInterval.specified=false");
    }

    @Test
    @Transactional
    void getAllLoadProfilesByTimeIntervalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where timeInterval is greater than or equal to DEFAULT_TIME_INTERVAL
        defaultLoadProfileShouldBeFound("timeInterval.greaterThanOrEqual=" + DEFAULT_TIME_INTERVAL);

        // Get all the loadProfileList where timeInterval is greater than or equal to UPDATED_TIME_INTERVAL
        defaultLoadProfileShouldNotBeFound("timeInterval.greaterThanOrEqual=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByTimeIntervalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where timeInterval is less than or equal to DEFAULT_TIME_INTERVAL
        defaultLoadProfileShouldBeFound("timeInterval.lessThanOrEqual=" + DEFAULT_TIME_INTERVAL);

        // Get all the loadProfileList where timeInterval is less than or equal to SMALLER_TIME_INTERVAL
        defaultLoadProfileShouldNotBeFound("timeInterval.lessThanOrEqual=" + SMALLER_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByTimeIntervalIsLessThanSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where timeInterval is less than DEFAULT_TIME_INTERVAL
        defaultLoadProfileShouldNotBeFound("timeInterval.lessThan=" + DEFAULT_TIME_INTERVAL);

        // Get all the loadProfileList where timeInterval is less than UPDATED_TIME_INTERVAL
        defaultLoadProfileShouldBeFound("timeInterval.lessThan=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByTimeIntervalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where timeInterval is greater than DEFAULT_TIME_INTERVAL
        defaultLoadProfileShouldNotBeFound("timeInterval.greaterThan=" + DEFAULT_TIME_INTERVAL);

        // Get all the loadProfileList where timeInterval is greater than SMALLER_TIME_INTERVAL
        defaultLoadProfileShouldBeFound("timeInterval.greaterThan=" + SMALLER_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByUploadDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where uploadDateTime equals to DEFAULT_UPLOAD_DATE_TIME
        defaultLoadProfileShouldBeFound("uploadDateTime.equals=" + DEFAULT_UPLOAD_DATE_TIME);

        // Get all the loadProfileList where uploadDateTime equals to UPDATED_UPLOAD_DATE_TIME
        defaultLoadProfileShouldNotBeFound("uploadDateTime.equals=" + UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByUploadDateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where uploadDateTime not equals to DEFAULT_UPLOAD_DATE_TIME
        defaultLoadProfileShouldNotBeFound("uploadDateTime.notEquals=" + DEFAULT_UPLOAD_DATE_TIME);

        // Get all the loadProfileList where uploadDateTime not equals to UPDATED_UPLOAD_DATE_TIME
        defaultLoadProfileShouldBeFound("uploadDateTime.notEquals=" + UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByUploadDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where uploadDateTime in DEFAULT_UPLOAD_DATE_TIME or UPDATED_UPLOAD_DATE_TIME
        defaultLoadProfileShouldBeFound("uploadDateTime.in=" + DEFAULT_UPLOAD_DATE_TIME + "," + UPDATED_UPLOAD_DATE_TIME);

        // Get all the loadProfileList where uploadDateTime equals to UPDATED_UPLOAD_DATE_TIME
        defaultLoadProfileShouldNotBeFound("uploadDateTime.in=" + UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllLoadProfilesByUploadDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        // Get all the loadProfileList where uploadDateTime is not null
        defaultLoadProfileShouldBeFound("uploadDateTime.specified=true");

        // Get all the loadProfileList where uploadDateTime is null
        defaultLoadProfileShouldNotBeFound("uploadDateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllLoadProfilesByInputFileIsEqualToSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);
        InputFile inputFile;
        if (TestUtil.findAll(em, InputFile.class).isEmpty()) {
            inputFile = InputFileResourceIT.createEntity(em);
            em.persist(inputFile);
            em.flush();
        } else {
            inputFile = TestUtil.findAll(em, InputFile.class).get(0);
        }
        em.persist(inputFile);
        em.flush();
        loadProfile.setInputFile(inputFile);
        loadProfileRepository.saveAndFlush(loadProfile);
        Long inputFileId = inputFile.getId();

        // Get all the loadProfileList where inputFile equals to inputFileId
        defaultLoadProfileShouldBeFound("inputFileId.equals=" + inputFileId);

        // Get all the loadProfileList where inputFile equals to (inputFileId + 1)
        defaultLoadProfileShouldNotBeFound("inputFileId.equals=" + (inputFileId + 1));
    }

    @Test
    @Transactional
    void getAllLoadProfilesByLoadElValIsEqualToSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);
        LoadElVal loadElVal;
        if (TestUtil.findAll(em, LoadElVal.class).isEmpty()) {
            loadElVal = LoadElValResourceIT.createEntity(em);
            em.persist(loadElVal);
            em.flush();
        } else {
            loadElVal = TestUtil.findAll(em, LoadElVal.class).get(0);
        }
        em.persist(loadElVal);
        em.flush();
        loadProfile.addLoadElVal(loadElVal);
        loadProfileRepository.saveAndFlush(loadProfile);
        Long loadElValId = loadElVal.getId();

        // Get all the loadProfileList where loadElVal equals to loadElValId
        defaultLoadProfileShouldBeFound("loadElValId.equals=" + loadElValId);

        // Get all the loadProfileList where loadElVal equals to (loadElValId + 1)
        defaultLoadProfileShouldNotBeFound("loadElValId.equals=" + (loadElValId + 1));
    }

    @Test
    @Transactional
    void getAllLoadProfilesByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);
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
        loadProfile.setNetwork(network);
        loadProfileRepository.saveAndFlush(loadProfile);
        Long networkId = network.getId();

        // Get all the loadProfileList where network equals to networkId
        defaultLoadProfileShouldBeFound("networkId.equals=" + networkId);

        // Get all the loadProfileList where network equals to (networkId + 1)
        defaultLoadProfileShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLoadProfileShouldBeFound(String filter) throws Exception {
        restLoadProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loadProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].season").value(hasItem(DEFAULT_SEASON)))
            .andExpect(jsonPath("$.[*].typicalDay").value(hasItem(DEFAULT_TYPICAL_DAY)))
            .andExpect(jsonPath("$.[*].mode").value(hasItem(DEFAULT_MODE)))
            .andExpect(jsonPath("$.[*].timeInterval").value(hasItem(DEFAULT_TIME_INTERVAL.doubleValue())))
            .andExpect(jsonPath("$.[*].uploadDateTime").value(hasItem(DEFAULT_UPLOAD_DATE_TIME.toString())));

        // Check, that the count call also returns 1
        restLoadProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLoadProfileShouldNotBeFound(String filter) throws Exception {
        restLoadProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLoadProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLoadProfile() throws Exception {
        // Get the loadProfile
        restLoadProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLoadProfile() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        int databaseSizeBeforeUpdate = loadProfileRepository.findAll().size();

        // Update the loadProfile
        LoadProfile updatedLoadProfile = loadProfileRepository.findById(loadProfile.getId()).get();
        // Disconnect from session so that the updates on updatedLoadProfile are not directly saved in db
        em.detach(updatedLoadProfile);
        updatedLoadProfile
            .season(UPDATED_SEASON)
            .typicalDay(UPDATED_TYPICAL_DAY)
            .mode(UPDATED_MODE)
            .timeInterval(UPDATED_TIME_INTERVAL)
            .uploadDateTime(UPDATED_UPLOAD_DATE_TIME);
        LoadProfileDTO loadProfileDTO = loadProfileMapper.toDto(updatedLoadProfile);

        restLoadProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loadProfileDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loadProfileDTO))
            )
            .andExpect(status().isOk());

        // Validate the LoadProfile in the database
        List<LoadProfile> loadProfileList = loadProfileRepository.findAll();
        assertThat(loadProfileList).hasSize(databaseSizeBeforeUpdate);
        LoadProfile testLoadProfile = loadProfileList.get(loadProfileList.size() - 1);
        assertThat(testLoadProfile.getSeason()).isEqualTo(UPDATED_SEASON);
        assertThat(testLoadProfile.getTypicalDay()).isEqualTo(UPDATED_TYPICAL_DAY);
        assertThat(testLoadProfile.getMode()).isEqualTo(UPDATED_MODE);
        assertThat(testLoadProfile.getTimeInterval()).isEqualTo(UPDATED_TIME_INTERVAL);
        assertThat(testLoadProfile.getUploadDateTime()).isEqualTo(UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void putNonExistingLoadProfile() throws Exception {
        int databaseSizeBeforeUpdate = loadProfileRepository.findAll().size();
        loadProfile.setId(count.incrementAndGet());

        // Create the LoadProfile
        LoadProfileDTO loadProfileDTO = loadProfileMapper.toDto(loadProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoadProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loadProfileDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loadProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoadProfile in the database
        List<LoadProfile> loadProfileList = loadProfileRepository.findAll();
        assertThat(loadProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoadProfile() throws Exception {
        int databaseSizeBeforeUpdate = loadProfileRepository.findAll().size();
        loadProfile.setId(count.incrementAndGet());

        // Create the LoadProfile
        LoadProfileDTO loadProfileDTO = loadProfileMapper.toDto(loadProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoadProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loadProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoadProfile in the database
        List<LoadProfile> loadProfileList = loadProfileRepository.findAll();
        assertThat(loadProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoadProfile() throws Exception {
        int databaseSizeBeforeUpdate = loadProfileRepository.findAll().size();
        loadProfile.setId(count.incrementAndGet());

        // Create the LoadProfile
        LoadProfileDTO loadProfileDTO = loadProfileMapper.toDto(loadProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoadProfileMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loadProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoadProfile in the database
        List<LoadProfile> loadProfileList = loadProfileRepository.findAll();
        assertThat(loadProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLoadProfileWithPatch() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        int databaseSizeBeforeUpdate = loadProfileRepository.findAll().size();

        // Update the loadProfile using partial update
        LoadProfile partialUpdatedLoadProfile = new LoadProfile();
        partialUpdatedLoadProfile.setId(loadProfile.getId());

        partialUpdatedLoadProfile.typicalDay(UPDATED_TYPICAL_DAY);

        restLoadProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoadProfile.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoadProfile))
            )
            .andExpect(status().isOk());

        // Validate the LoadProfile in the database
        List<LoadProfile> loadProfileList = loadProfileRepository.findAll();
        assertThat(loadProfileList).hasSize(databaseSizeBeforeUpdate);
        LoadProfile testLoadProfile = loadProfileList.get(loadProfileList.size() - 1);
        assertThat(testLoadProfile.getSeason()).isEqualTo(DEFAULT_SEASON);
        assertThat(testLoadProfile.getTypicalDay()).isEqualTo(UPDATED_TYPICAL_DAY);
        assertThat(testLoadProfile.getMode()).isEqualTo(DEFAULT_MODE);
        assertThat(testLoadProfile.getTimeInterval()).isEqualTo(DEFAULT_TIME_INTERVAL);
        assertThat(testLoadProfile.getUploadDateTime()).isEqualTo(DEFAULT_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void fullUpdateLoadProfileWithPatch() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        int databaseSizeBeforeUpdate = loadProfileRepository.findAll().size();

        // Update the loadProfile using partial update
        LoadProfile partialUpdatedLoadProfile = new LoadProfile();
        partialUpdatedLoadProfile.setId(loadProfile.getId());

        partialUpdatedLoadProfile
            .season(UPDATED_SEASON)
            .typicalDay(UPDATED_TYPICAL_DAY)
            .mode(UPDATED_MODE)
            .timeInterval(UPDATED_TIME_INTERVAL)
            .uploadDateTime(UPDATED_UPLOAD_DATE_TIME);

        restLoadProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoadProfile.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoadProfile))
            )
            .andExpect(status().isOk());

        // Validate the LoadProfile in the database
        List<LoadProfile> loadProfileList = loadProfileRepository.findAll();
        assertThat(loadProfileList).hasSize(databaseSizeBeforeUpdate);
        LoadProfile testLoadProfile = loadProfileList.get(loadProfileList.size() - 1);
        assertThat(testLoadProfile.getSeason()).isEqualTo(UPDATED_SEASON);
        assertThat(testLoadProfile.getTypicalDay()).isEqualTo(UPDATED_TYPICAL_DAY);
        assertThat(testLoadProfile.getMode()).isEqualTo(UPDATED_MODE);
        assertThat(testLoadProfile.getTimeInterval()).isEqualTo(UPDATED_TIME_INTERVAL);
        assertThat(testLoadProfile.getUploadDateTime()).isEqualTo(UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingLoadProfile() throws Exception {
        int databaseSizeBeforeUpdate = loadProfileRepository.findAll().size();
        loadProfile.setId(count.incrementAndGet());

        // Create the LoadProfile
        LoadProfileDTO loadProfileDTO = loadProfileMapper.toDto(loadProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoadProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loadProfileDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loadProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoadProfile in the database
        List<LoadProfile> loadProfileList = loadProfileRepository.findAll();
        assertThat(loadProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoadProfile() throws Exception {
        int databaseSizeBeforeUpdate = loadProfileRepository.findAll().size();
        loadProfile.setId(count.incrementAndGet());

        // Create the LoadProfile
        LoadProfileDTO loadProfileDTO = loadProfileMapper.toDto(loadProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoadProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loadProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoadProfile in the database
        List<LoadProfile> loadProfileList = loadProfileRepository.findAll();
        assertThat(loadProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoadProfile() throws Exception {
        int databaseSizeBeforeUpdate = loadProfileRepository.findAll().size();
        loadProfile.setId(count.incrementAndGet());

        // Create the LoadProfile
        LoadProfileDTO loadProfileDTO = loadProfileMapper.toDto(loadProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoadProfileMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loadProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoadProfile in the database
        List<LoadProfile> loadProfileList = loadProfileRepository.findAll();
        assertThat(loadProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLoadProfile() throws Exception {
        // Initialize the database
        loadProfileRepository.saveAndFlush(loadProfile);

        int databaseSizeBeforeDelete = loadProfileRepository.findAll().size();

        // Delete the loadProfile
        restLoadProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, loadProfile.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LoadProfile> loadProfileList = loadProfileRepository.findAll();
        assertThat(loadProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
