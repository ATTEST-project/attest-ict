package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.GenElVal;
import com.attest.ict.domain.GenProfile;
import com.attest.ict.domain.InputFile;
import com.attest.ict.domain.Network;
import com.attest.ict.repository.GenProfileRepository;
import com.attest.ict.service.criteria.GenProfileCriteria;
import com.attest.ict.service.dto.GenProfileDTO;
import com.attest.ict.service.mapper.GenProfileMapper;
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
 * Integration tests for the {@link GenProfileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GenProfileResourceIT {

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

    private static final String ENTITY_API_URL = "/api/gen-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GenProfileRepository genProfileRepository;

    @Autowired
    private GenProfileMapper genProfileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGenProfileMockMvc;

    private GenProfile genProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GenProfile createEntity(EntityManager em) {
        GenProfile genProfile = new GenProfile()
            .season(DEFAULT_SEASON)
            .typicalDay(DEFAULT_TYPICAL_DAY)
            .mode(DEFAULT_MODE)
            .timeInterval(DEFAULT_TIME_INTERVAL)
            .uploadDateTime(DEFAULT_UPLOAD_DATE_TIME);
        return genProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GenProfile createUpdatedEntity(EntityManager em) {
        GenProfile genProfile = new GenProfile()
            .season(UPDATED_SEASON)
            .typicalDay(UPDATED_TYPICAL_DAY)
            .mode(UPDATED_MODE)
            .timeInterval(UPDATED_TIME_INTERVAL)
            .uploadDateTime(UPDATED_UPLOAD_DATE_TIME);
        return genProfile;
    }

    @BeforeEach
    public void initTest() {
        genProfile = createEntity(em);
    }

    @Test
    @Transactional
    void createGenProfile() throws Exception {
        int databaseSizeBeforeCreate = genProfileRepository.findAll().size();
        // Create the GenProfile
        GenProfileDTO genProfileDTO = genProfileMapper.toDto(genProfile);
        restGenProfileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genProfileDTO))
            )
            .andExpect(status().isCreated());

        // Validate the GenProfile in the database
        List<GenProfile> genProfileList = genProfileRepository.findAll();
        assertThat(genProfileList).hasSize(databaseSizeBeforeCreate + 1);
        GenProfile testGenProfile = genProfileList.get(genProfileList.size() - 1);
        assertThat(testGenProfile.getSeason()).isEqualTo(DEFAULT_SEASON);
        assertThat(testGenProfile.getTypicalDay()).isEqualTo(DEFAULT_TYPICAL_DAY);
        assertThat(testGenProfile.getMode()).isEqualTo(DEFAULT_MODE);
        assertThat(testGenProfile.getTimeInterval()).isEqualTo(DEFAULT_TIME_INTERVAL);
        assertThat(testGenProfile.getUploadDateTime()).isEqualTo(DEFAULT_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void createGenProfileWithExistingId() throws Exception {
        // Create the GenProfile with an existing ID
        genProfile.setId(1L);
        GenProfileDTO genProfileDTO = genProfileMapper.toDto(genProfile);

        int databaseSizeBeforeCreate = genProfileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGenProfileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenProfile in the database
        List<GenProfile> genProfileList = genProfileRepository.findAll();
        assertThat(genProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGenProfiles() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList
        restGenProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].season").value(hasItem(DEFAULT_SEASON)))
            .andExpect(jsonPath("$.[*].typicalDay").value(hasItem(DEFAULT_TYPICAL_DAY)))
            .andExpect(jsonPath("$.[*].mode").value(hasItem(DEFAULT_MODE)))
            .andExpect(jsonPath("$.[*].timeInterval").value(hasItem(DEFAULT_TIME_INTERVAL.doubleValue())))
            .andExpect(jsonPath("$.[*].uploadDateTime").value(hasItem(DEFAULT_UPLOAD_DATE_TIME.toString())));
    }

    @Test
    @Transactional
    void getGenProfile() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get the genProfile
        restGenProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, genProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(genProfile.getId().intValue()))
            .andExpect(jsonPath("$.season").value(DEFAULT_SEASON))
            .andExpect(jsonPath("$.typicalDay").value(DEFAULT_TYPICAL_DAY))
            .andExpect(jsonPath("$.mode").value(DEFAULT_MODE))
            .andExpect(jsonPath("$.timeInterval").value(DEFAULT_TIME_INTERVAL.doubleValue()))
            .andExpect(jsonPath("$.uploadDateTime").value(DEFAULT_UPLOAD_DATE_TIME.toString()));
    }

    @Test
    @Transactional
    void getGenProfilesByIdFiltering() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        Long id = genProfile.getId();

        defaultGenProfileShouldBeFound("id.equals=" + id);
        defaultGenProfileShouldNotBeFound("id.notEquals=" + id);

        defaultGenProfileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGenProfileShouldNotBeFound("id.greaterThan=" + id);

        defaultGenProfileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGenProfileShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGenProfilesBySeasonIsEqualToSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where season equals to DEFAULT_SEASON
        defaultGenProfileShouldBeFound("season.equals=" + DEFAULT_SEASON);

        // Get all the genProfileList where season equals to UPDATED_SEASON
        defaultGenProfileShouldNotBeFound("season.equals=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllGenProfilesBySeasonIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where season not equals to DEFAULT_SEASON
        defaultGenProfileShouldNotBeFound("season.notEquals=" + DEFAULT_SEASON);

        // Get all the genProfileList where season not equals to UPDATED_SEASON
        defaultGenProfileShouldBeFound("season.notEquals=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllGenProfilesBySeasonIsInShouldWork() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where season in DEFAULT_SEASON or UPDATED_SEASON
        defaultGenProfileShouldBeFound("season.in=" + DEFAULT_SEASON + "," + UPDATED_SEASON);

        // Get all the genProfileList where season equals to UPDATED_SEASON
        defaultGenProfileShouldNotBeFound("season.in=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllGenProfilesBySeasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where season is not null
        defaultGenProfileShouldBeFound("season.specified=true");

        // Get all the genProfileList where season is null
        defaultGenProfileShouldNotBeFound("season.specified=false");
    }

    @Test
    @Transactional
    void getAllGenProfilesBySeasonContainsSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where season contains DEFAULT_SEASON
        defaultGenProfileShouldBeFound("season.contains=" + DEFAULT_SEASON);

        // Get all the genProfileList where season contains UPDATED_SEASON
        defaultGenProfileShouldNotBeFound("season.contains=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllGenProfilesBySeasonNotContainsSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where season does not contain DEFAULT_SEASON
        defaultGenProfileShouldNotBeFound("season.doesNotContain=" + DEFAULT_SEASON);

        // Get all the genProfileList where season does not contain UPDATED_SEASON
        defaultGenProfileShouldBeFound("season.doesNotContain=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllGenProfilesByTypicalDayIsEqualToSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where typicalDay equals to DEFAULT_TYPICAL_DAY
        defaultGenProfileShouldBeFound("typicalDay.equals=" + DEFAULT_TYPICAL_DAY);

        // Get all the genProfileList where typicalDay equals to UPDATED_TYPICAL_DAY
        defaultGenProfileShouldNotBeFound("typicalDay.equals=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllGenProfilesByTypicalDayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where typicalDay not equals to DEFAULT_TYPICAL_DAY
        defaultGenProfileShouldNotBeFound("typicalDay.notEquals=" + DEFAULT_TYPICAL_DAY);

        // Get all the genProfileList where typicalDay not equals to UPDATED_TYPICAL_DAY
        defaultGenProfileShouldBeFound("typicalDay.notEquals=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllGenProfilesByTypicalDayIsInShouldWork() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where typicalDay in DEFAULT_TYPICAL_DAY or UPDATED_TYPICAL_DAY
        defaultGenProfileShouldBeFound("typicalDay.in=" + DEFAULT_TYPICAL_DAY + "," + UPDATED_TYPICAL_DAY);

        // Get all the genProfileList where typicalDay equals to UPDATED_TYPICAL_DAY
        defaultGenProfileShouldNotBeFound("typicalDay.in=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllGenProfilesByTypicalDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where typicalDay is not null
        defaultGenProfileShouldBeFound("typicalDay.specified=true");

        // Get all the genProfileList where typicalDay is null
        defaultGenProfileShouldNotBeFound("typicalDay.specified=false");
    }

    @Test
    @Transactional
    void getAllGenProfilesByTypicalDayContainsSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where typicalDay contains DEFAULT_TYPICAL_DAY
        defaultGenProfileShouldBeFound("typicalDay.contains=" + DEFAULT_TYPICAL_DAY);

        // Get all the genProfileList where typicalDay contains UPDATED_TYPICAL_DAY
        defaultGenProfileShouldNotBeFound("typicalDay.contains=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllGenProfilesByTypicalDayNotContainsSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where typicalDay does not contain DEFAULT_TYPICAL_DAY
        defaultGenProfileShouldNotBeFound("typicalDay.doesNotContain=" + DEFAULT_TYPICAL_DAY);

        // Get all the genProfileList where typicalDay does not contain UPDATED_TYPICAL_DAY
        defaultGenProfileShouldBeFound("typicalDay.doesNotContain=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllGenProfilesByModeIsEqualToSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where mode equals to DEFAULT_MODE
        defaultGenProfileShouldBeFound("mode.equals=" + DEFAULT_MODE);

        // Get all the genProfileList where mode equals to UPDATED_MODE
        defaultGenProfileShouldNotBeFound("mode.equals=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllGenProfilesByModeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where mode not equals to DEFAULT_MODE
        defaultGenProfileShouldNotBeFound("mode.notEquals=" + DEFAULT_MODE);

        // Get all the genProfileList where mode not equals to UPDATED_MODE
        defaultGenProfileShouldBeFound("mode.notEquals=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllGenProfilesByModeIsInShouldWork() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where mode in DEFAULT_MODE or UPDATED_MODE
        defaultGenProfileShouldBeFound("mode.in=" + DEFAULT_MODE + "," + UPDATED_MODE);

        // Get all the genProfileList where mode equals to UPDATED_MODE
        defaultGenProfileShouldNotBeFound("mode.in=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllGenProfilesByModeIsNullOrNotNull() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where mode is not null
        defaultGenProfileShouldBeFound("mode.specified=true");

        // Get all the genProfileList where mode is null
        defaultGenProfileShouldNotBeFound("mode.specified=false");
    }

    @Test
    @Transactional
    void getAllGenProfilesByModeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where mode is greater than or equal to DEFAULT_MODE
        defaultGenProfileShouldBeFound("mode.greaterThanOrEqual=" + DEFAULT_MODE);

        // Get all the genProfileList where mode is greater than or equal to UPDATED_MODE
        defaultGenProfileShouldNotBeFound("mode.greaterThanOrEqual=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllGenProfilesByModeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where mode is less than or equal to DEFAULT_MODE
        defaultGenProfileShouldBeFound("mode.lessThanOrEqual=" + DEFAULT_MODE);

        // Get all the genProfileList where mode is less than or equal to SMALLER_MODE
        defaultGenProfileShouldNotBeFound("mode.lessThanOrEqual=" + SMALLER_MODE);
    }

    @Test
    @Transactional
    void getAllGenProfilesByModeIsLessThanSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where mode is less than DEFAULT_MODE
        defaultGenProfileShouldNotBeFound("mode.lessThan=" + DEFAULT_MODE);

        // Get all the genProfileList where mode is less than UPDATED_MODE
        defaultGenProfileShouldBeFound("mode.lessThan=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllGenProfilesByModeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where mode is greater than DEFAULT_MODE
        defaultGenProfileShouldNotBeFound("mode.greaterThan=" + DEFAULT_MODE);

        // Get all the genProfileList where mode is greater than SMALLER_MODE
        defaultGenProfileShouldBeFound("mode.greaterThan=" + SMALLER_MODE);
    }

    @Test
    @Transactional
    void getAllGenProfilesByTimeIntervalIsEqualToSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where timeInterval equals to DEFAULT_TIME_INTERVAL
        defaultGenProfileShouldBeFound("timeInterval.equals=" + DEFAULT_TIME_INTERVAL);

        // Get all the genProfileList where timeInterval equals to UPDATED_TIME_INTERVAL
        defaultGenProfileShouldNotBeFound("timeInterval.equals=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllGenProfilesByTimeIntervalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where timeInterval not equals to DEFAULT_TIME_INTERVAL
        defaultGenProfileShouldNotBeFound("timeInterval.notEquals=" + DEFAULT_TIME_INTERVAL);

        // Get all the genProfileList where timeInterval not equals to UPDATED_TIME_INTERVAL
        defaultGenProfileShouldBeFound("timeInterval.notEquals=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllGenProfilesByTimeIntervalIsInShouldWork() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where timeInterval in DEFAULT_TIME_INTERVAL or UPDATED_TIME_INTERVAL
        defaultGenProfileShouldBeFound("timeInterval.in=" + DEFAULT_TIME_INTERVAL + "," + UPDATED_TIME_INTERVAL);

        // Get all the genProfileList where timeInterval equals to UPDATED_TIME_INTERVAL
        defaultGenProfileShouldNotBeFound("timeInterval.in=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllGenProfilesByTimeIntervalIsNullOrNotNull() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where timeInterval is not null
        defaultGenProfileShouldBeFound("timeInterval.specified=true");

        // Get all the genProfileList where timeInterval is null
        defaultGenProfileShouldNotBeFound("timeInterval.specified=false");
    }

    @Test
    @Transactional
    void getAllGenProfilesByTimeIntervalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where timeInterval is greater than or equal to DEFAULT_TIME_INTERVAL
        defaultGenProfileShouldBeFound("timeInterval.greaterThanOrEqual=" + DEFAULT_TIME_INTERVAL);

        // Get all the genProfileList where timeInterval is greater than or equal to UPDATED_TIME_INTERVAL
        defaultGenProfileShouldNotBeFound("timeInterval.greaterThanOrEqual=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllGenProfilesByTimeIntervalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where timeInterval is less than or equal to DEFAULT_TIME_INTERVAL
        defaultGenProfileShouldBeFound("timeInterval.lessThanOrEqual=" + DEFAULT_TIME_INTERVAL);

        // Get all the genProfileList where timeInterval is less than or equal to SMALLER_TIME_INTERVAL
        defaultGenProfileShouldNotBeFound("timeInterval.lessThanOrEqual=" + SMALLER_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllGenProfilesByTimeIntervalIsLessThanSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where timeInterval is less than DEFAULT_TIME_INTERVAL
        defaultGenProfileShouldNotBeFound("timeInterval.lessThan=" + DEFAULT_TIME_INTERVAL);

        // Get all the genProfileList where timeInterval is less than UPDATED_TIME_INTERVAL
        defaultGenProfileShouldBeFound("timeInterval.lessThan=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllGenProfilesByTimeIntervalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where timeInterval is greater than DEFAULT_TIME_INTERVAL
        defaultGenProfileShouldNotBeFound("timeInterval.greaterThan=" + DEFAULT_TIME_INTERVAL);

        // Get all the genProfileList where timeInterval is greater than SMALLER_TIME_INTERVAL
        defaultGenProfileShouldBeFound("timeInterval.greaterThan=" + SMALLER_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllGenProfilesByUploadDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where uploadDateTime equals to DEFAULT_UPLOAD_DATE_TIME
        defaultGenProfileShouldBeFound("uploadDateTime.equals=" + DEFAULT_UPLOAD_DATE_TIME);

        // Get all the genProfileList where uploadDateTime equals to UPDATED_UPLOAD_DATE_TIME
        defaultGenProfileShouldNotBeFound("uploadDateTime.equals=" + UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllGenProfilesByUploadDateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where uploadDateTime not equals to DEFAULT_UPLOAD_DATE_TIME
        defaultGenProfileShouldNotBeFound("uploadDateTime.notEquals=" + DEFAULT_UPLOAD_DATE_TIME);

        // Get all the genProfileList where uploadDateTime not equals to UPDATED_UPLOAD_DATE_TIME
        defaultGenProfileShouldBeFound("uploadDateTime.notEquals=" + UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllGenProfilesByUploadDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where uploadDateTime in DEFAULT_UPLOAD_DATE_TIME or UPDATED_UPLOAD_DATE_TIME
        defaultGenProfileShouldBeFound("uploadDateTime.in=" + DEFAULT_UPLOAD_DATE_TIME + "," + UPDATED_UPLOAD_DATE_TIME);

        // Get all the genProfileList where uploadDateTime equals to UPDATED_UPLOAD_DATE_TIME
        defaultGenProfileShouldNotBeFound("uploadDateTime.in=" + UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllGenProfilesByUploadDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        // Get all the genProfileList where uploadDateTime is not null
        defaultGenProfileShouldBeFound("uploadDateTime.specified=true");

        // Get all the genProfileList where uploadDateTime is null
        defaultGenProfileShouldNotBeFound("uploadDateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllGenProfilesByInputFileIsEqualToSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);
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
        genProfile.setInputFile(inputFile);
        genProfileRepository.saveAndFlush(genProfile);
        Long inputFileId = inputFile.getId();

        // Get all the genProfileList where inputFile equals to inputFileId
        defaultGenProfileShouldBeFound("inputFileId.equals=" + inputFileId);

        // Get all the genProfileList where inputFile equals to (inputFileId + 1)
        defaultGenProfileShouldNotBeFound("inputFileId.equals=" + (inputFileId + 1));
    }

    @Test
    @Transactional
    void getAllGenProfilesByGenElValIsEqualToSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);
        GenElVal genElVal;
        if (TestUtil.findAll(em, GenElVal.class).isEmpty()) {
            genElVal = GenElValResourceIT.createEntity(em);
            em.persist(genElVal);
            em.flush();
        } else {
            genElVal = TestUtil.findAll(em, GenElVal.class).get(0);
        }
        em.persist(genElVal);
        em.flush();
        genProfile.addGenElVal(genElVal);
        genProfileRepository.saveAndFlush(genProfile);
        Long genElValId = genElVal.getId();

        // Get all the genProfileList where genElVal equals to genElValId
        defaultGenProfileShouldBeFound("genElValId.equals=" + genElValId);

        // Get all the genProfileList where genElVal equals to (genElValId + 1)
        defaultGenProfileShouldNotBeFound("genElValId.equals=" + (genElValId + 1));
    }

    @Test
    @Transactional
    void getAllGenProfilesByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);
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
        genProfile.setNetwork(network);
        genProfileRepository.saveAndFlush(genProfile);
        Long networkId = network.getId();

        // Get all the genProfileList where network equals to networkId
        defaultGenProfileShouldBeFound("networkId.equals=" + networkId);

        // Get all the genProfileList where network equals to (networkId + 1)
        defaultGenProfileShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGenProfileShouldBeFound(String filter) throws Exception {
        restGenProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].season").value(hasItem(DEFAULT_SEASON)))
            .andExpect(jsonPath("$.[*].typicalDay").value(hasItem(DEFAULT_TYPICAL_DAY)))
            .andExpect(jsonPath("$.[*].mode").value(hasItem(DEFAULT_MODE)))
            .andExpect(jsonPath("$.[*].timeInterval").value(hasItem(DEFAULT_TIME_INTERVAL.doubleValue())))
            .andExpect(jsonPath("$.[*].uploadDateTime").value(hasItem(DEFAULT_UPLOAD_DATE_TIME.toString())));

        // Check, that the count call also returns 1
        restGenProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGenProfileShouldNotBeFound(String filter) throws Exception {
        restGenProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGenProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGenProfile() throws Exception {
        // Get the genProfile
        restGenProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGenProfile() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        int databaseSizeBeforeUpdate = genProfileRepository.findAll().size();

        // Update the genProfile
        GenProfile updatedGenProfile = genProfileRepository.findById(genProfile.getId()).get();
        // Disconnect from session so that the updates on updatedGenProfile are not directly saved in db
        em.detach(updatedGenProfile);
        updatedGenProfile
            .season(UPDATED_SEASON)
            .typicalDay(UPDATED_TYPICAL_DAY)
            .mode(UPDATED_MODE)
            .timeInterval(UPDATED_TIME_INTERVAL)
            .uploadDateTime(UPDATED_UPLOAD_DATE_TIME);
        GenProfileDTO genProfileDTO = genProfileMapper.toDto(updatedGenProfile);

        restGenProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, genProfileDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genProfileDTO))
            )
            .andExpect(status().isOk());

        // Validate the GenProfile in the database
        List<GenProfile> genProfileList = genProfileRepository.findAll();
        assertThat(genProfileList).hasSize(databaseSizeBeforeUpdate);
        GenProfile testGenProfile = genProfileList.get(genProfileList.size() - 1);
        assertThat(testGenProfile.getSeason()).isEqualTo(UPDATED_SEASON);
        assertThat(testGenProfile.getTypicalDay()).isEqualTo(UPDATED_TYPICAL_DAY);
        assertThat(testGenProfile.getMode()).isEqualTo(UPDATED_MODE);
        assertThat(testGenProfile.getTimeInterval()).isEqualTo(UPDATED_TIME_INTERVAL);
        assertThat(testGenProfile.getUploadDateTime()).isEqualTo(UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void putNonExistingGenProfile() throws Exception {
        int databaseSizeBeforeUpdate = genProfileRepository.findAll().size();
        genProfile.setId(count.incrementAndGet());

        // Create the GenProfile
        GenProfileDTO genProfileDTO = genProfileMapper.toDto(genProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, genProfileDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenProfile in the database
        List<GenProfile> genProfileList = genProfileRepository.findAll();
        assertThat(genProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGenProfile() throws Exception {
        int databaseSizeBeforeUpdate = genProfileRepository.findAll().size();
        genProfile.setId(count.incrementAndGet());

        // Create the GenProfile
        GenProfileDTO genProfileDTO = genProfileMapper.toDto(genProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenProfile in the database
        List<GenProfile> genProfileList = genProfileRepository.findAll();
        assertThat(genProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGenProfile() throws Exception {
        int databaseSizeBeforeUpdate = genProfileRepository.findAll().size();
        genProfile.setId(count.incrementAndGet());

        // Create the GenProfile
        GenProfileDTO genProfileDTO = genProfileMapper.toDto(genProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenProfileMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GenProfile in the database
        List<GenProfile> genProfileList = genProfileRepository.findAll();
        assertThat(genProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGenProfileWithPatch() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        int databaseSizeBeforeUpdate = genProfileRepository.findAll().size();

        // Update the genProfile using partial update
        GenProfile partialUpdatedGenProfile = new GenProfile();
        partialUpdatedGenProfile.setId(genProfile.getId());

        partialUpdatedGenProfile.typicalDay(UPDATED_TYPICAL_DAY).mode(UPDATED_MODE).timeInterval(UPDATED_TIME_INTERVAL);

        restGenProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenProfile.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenProfile))
            )
            .andExpect(status().isOk());

        // Validate the GenProfile in the database
        List<GenProfile> genProfileList = genProfileRepository.findAll();
        assertThat(genProfileList).hasSize(databaseSizeBeforeUpdate);
        GenProfile testGenProfile = genProfileList.get(genProfileList.size() - 1);
        assertThat(testGenProfile.getSeason()).isEqualTo(DEFAULT_SEASON);
        assertThat(testGenProfile.getTypicalDay()).isEqualTo(UPDATED_TYPICAL_DAY);
        assertThat(testGenProfile.getMode()).isEqualTo(UPDATED_MODE);
        assertThat(testGenProfile.getTimeInterval()).isEqualTo(UPDATED_TIME_INTERVAL);
        assertThat(testGenProfile.getUploadDateTime()).isEqualTo(DEFAULT_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void fullUpdateGenProfileWithPatch() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        int databaseSizeBeforeUpdate = genProfileRepository.findAll().size();

        // Update the genProfile using partial update
        GenProfile partialUpdatedGenProfile = new GenProfile();
        partialUpdatedGenProfile.setId(genProfile.getId());

        partialUpdatedGenProfile
            .season(UPDATED_SEASON)
            .typicalDay(UPDATED_TYPICAL_DAY)
            .mode(UPDATED_MODE)
            .timeInterval(UPDATED_TIME_INTERVAL)
            .uploadDateTime(UPDATED_UPLOAD_DATE_TIME);

        restGenProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenProfile.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenProfile))
            )
            .andExpect(status().isOk());

        // Validate the GenProfile in the database
        List<GenProfile> genProfileList = genProfileRepository.findAll();
        assertThat(genProfileList).hasSize(databaseSizeBeforeUpdate);
        GenProfile testGenProfile = genProfileList.get(genProfileList.size() - 1);
        assertThat(testGenProfile.getSeason()).isEqualTo(UPDATED_SEASON);
        assertThat(testGenProfile.getTypicalDay()).isEqualTo(UPDATED_TYPICAL_DAY);
        assertThat(testGenProfile.getMode()).isEqualTo(UPDATED_MODE);
        assertThat(testGenProfile.getTimeInterval()).isEqualTo(UPDATED_TIME_INTERVAL);
        assertThat(testGenProfile.getUploadDateTime()).isEqualTo(UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingGenProfile() throws Exception {
        int databaseSizeBeforeUpdate = genProfileRepository.findAll().size();
        genProfile.setId(count.incrementAndGet());

        // Create the GenProfile
        GenProfileDTO genProfileDTO = genProfileMapper.toDto(genProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, genProfileDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenProfile in the database
        List<GenProfile> genProfileList = genProfileRepository.findAll();
        assertThat(genProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGenProfile() throws Exception {
        int databaseSizeBeforeUpdate = genProfileRepository.findAll().size();
        genProfile.setId(count.incrementAndGet());

        // Create the GenProfile
        GenProfileDTO genProfileDTO = genProfileMapper.toDto(genProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenProfile in the database
        List<GenProfile> genProfileList = genProfileRepository.findAll();
        assertThat(genProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGenProfile() throws Exception {
        int databaseSizeBeforeUpdate = genProfileRepository.findAll().size();
        genProfile.setId(count.incrementAndGet());

        // Create the GenProfile
        GenProfileDTO genProfileDTO = genProfileMapper.toDto(genProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenProfileMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GenProfile in the database
        List<GenProfile> genProfileList = genProfileRepository.findAll();
        assertThat(genProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGenProfile() throws Exception {
        // Initialize the database
        genProfileRepository.saveAndFlush(genProfile);

        int databaseSizeBeforeDelete = genProfileRepository.findAll().size();

        // Delete the genProfile
        restGenProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, genProfile.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GenProfile> genProfileList = genProfileRepository.findAll();
        assertThat(genProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
