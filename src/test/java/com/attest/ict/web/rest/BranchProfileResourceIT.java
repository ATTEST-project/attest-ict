package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.BranchElVal;
import com.attest.ict.domain.BranchProfile;
import com.attest.ict.domain.InputFile;
import com.attest.ict.domain.Network;
import com.attest.ict.repository.BranchProfileRepository;
import com.attest.ict.service.criteria.BranchProfileCriteria;
import com.attest.ict.service.dto.BranchProfileDTO;
import com.attest.ict.service.mapper.BranchProfileMapper;
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
 * Integration tests for the {@link BranchProfileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BranchProfileResourceIT {

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

    private static final String ENTITY_API_URL = "/api/branch-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BranchProfileRepository branchProfileRepository;

    @Autowired
    private BranchProfileMapper branchProfileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBranchProfileMockMvc;

    private BranchProfile branchProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BranchProfile createEntity(EntityManager em) {
        BranchProfile branchProfile = new BranchProfile()
            .season(DEFAULT_SEASON)
            .typicalDay(DEFAULT_TYPICAL_DAY)
            .mode(DEFAULT_MODE)
            .timeInterval(DEFAULT_TIME_INTERVAL)
            .uploadDateTime(DEFAULT_UPLOAD_DATE_TIME);
        return branchProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BranchProfile createUpdatedEntity(EntityManager em) {
        BranchProfile branchProfile = new BranchProfile()
            .season(UPDATED_SEASON)
            .typicalDay(UPDATED_TYPICAL_DAY)
            .mode(UPDATED_MODE)
            .timeInterval(UPDATED_TIME_INTERVAL)
            .uploadDateTime(UPDATED_UPLOAD_DATE_TIME);
        return branchProfile;
    }

    @BeforeEach
    public void initTest() {
        branchProfile = createEntity(em);
    }

    @Test
    @Transactional
    void createBranchProfile() throws Exception {
        int databaseSizeBeforeCreate = branchProfileRepository.findAll().size();
        // Create the BranchProfile
        BranchProfileDTO branchProfileDTO = branchProfileMapper.toDto(branchProfile);
        restBranchProfileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchProfileDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BranchProfile in the database
        List<BranchProfile> branchProfileList = branchProfileRepository.findAll();
        assertThat(branchProfileList).hasSize(databaseSizeBeforeCreate + 1);
        BranchProfile testBranchProfile = branchProfileList.get(branchProfileList.size() - 1);
        assertThat(testBranchProfile.getSeason()).isEqualTo(DEFAULT_SEASON);
        assertThat(testBranchProfile.getTypicalDay()).isEqualTo(DEFAULT_TYPICAL_DAY);
        assertThat(testBranchProfile.getMode()).isEqualTo(DEFAULT_MODE);
        assertThat(testBranchProfile.getTimeInterval()).isEqualTo(DEFAULT_TIME_INTERVAL);
        assertThat(testBranchProfile.getUploadDateTime()).isEqualTo(DEFAULT_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void createBranchProfileWithExistingId() throws Exception {
        // Create the BranchProfile with an existing ID
        branchProfile.setId(1L);
        BranchProfileDTO branchProfileDTO = branchProfileMapper.toDto(branchProfile);

        int databaseSizeBeforeCreate = branchProfileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBranchProfileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchProfile in the database
        List<BranchProfile> branchProfileList = branchProfileRepository.findAll();
        assertThat(branchProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBranchProfiles() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList
        restBranchProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branchProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].season").value(hasItem(DEFAULT_SEASON)))
            .andExpect(jsonPath("$.[*].typicalDay").value(hasItem(DEFAULT_TYPICAL_DAY)))
            .andExpect(jsonPath("$.[*].mode").value(hasItem(DEFAULT_MODE)))
            .andExpect(jsonPath("$.[*].timeInterval").value(hasItem(DEFAULT_TIME_INTERVAL.doubleValue())))
            .andExpect(jsonPath("$.[*].uploadDateTime").value(hasItem(DEFAULT_UPLOAD_DATE_TIME.toString())));
    }

    @Test
    @Transactional
    void getBranchProfile() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get the branchProfile
        restBranchProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, branchProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(branchProfile.getId().intValue()))
            .andExpect(jsonPath("$.season").value(DEFAULT_SEASON))
            .andExpect(jsonPath("$.typicalDay").value(DEFAULT_TYPICAL_DAY))
            .andExpect(jsonPath("$.mode").value(DEFAULT_MODE))
            .andExpect(jsonPath("$.timeInterval").value(DEFAULT_TIME_INTERVAL.doubleValue()))
            .andExpect(jsonPath("$.uploadDateTime").value(DEFAULT_UPLOAD_DATE_TIME.toString()));
    }

    @Test
    @Transactional
    void getBranchProfilesByIdFiltering() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        Long id = branchProfile.getId();

        defaultBranchProfileShouldBeFound("id.equals=" + id);
        defaultBranchProfileShouldNotBeFound("id.notEquals=" + id);

        defaultBranchProfileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBranchProfileShouldNotBeFound("id.greaterThan=" + id);

        defaultBranchProfileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBranchProfileShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBranchProfilesBySeasonIsEqualToSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where season equals to DEFAULT_SEASON
        defaultBranchProfileShouldBeFound("season.equals=" + DEFAULT_SEASON);

        // Get all the branchProfileList where season equals to UPDATED_SEASON
        defaultBranchProfileShouldNotBeFound("season.equals=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllBranchProfilesBySeasonIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where season not equals to DEFAULT_SEASON
        defaultBranchProfileShouldNotBeFound("season.notEquals=" + DEFAULT_SEASON);

        // Get all the branchProfileList where season not equals to UPDATED_SEASON
        defaultBranchProfileShouldBeFound("season.notEquals=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllBranchProfilesBySeasonIsInShouldWork() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where season in DEFAULT_SEASON or UPDATED_SEASON
        defaultBranchProfileShouldBeFound("season.in=" + DEFAULT_SEASON + "," + UPDATED_SEASON);

        // Get all the branchProfileList where season equals to UPDATED_SEASON
        defaultBranchProfileShouldNotBeFound("season.in=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllBranchProfilesBySeasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where season is not null
        defaultBranchProfileShouldBeFound("season.specified=true");

        // Get all the branchProfileList where season is null
        defaultBranchProfileShouldNotBeFound("season.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchProfilesBySeasonContainsSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where season contains DEFAULT_SEASON
        defaultBranchProfileShouldBeFound("season.contains=" + DEFAULT_SEASON);

        // Get all the branchProfileList where season contains UPDATED_SEASON
        defaultBranchProfileShouldNotBeFound("season.contains=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllBranchProfilesBySeasonNotContainsSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where season does not contain DEFAULT_SEASON
        defaultBranchProfileShouldNotBeFound("season.doesNotContain=" + DEFAULT_SEASON);

        // Get all the branchProfileList where season does not contain UPDATED_SEASON
        defaultBranchProfileShouldBeFound("season.doesNotContain=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByTypicalDayIsEqualToSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where typicalDay equals to DEFAULT_TYPICAL_DAY
        defaultBranchProfileShouldBeFound("typicalDay.equals=" + DEFAULT_TYPICAL_DAY);

        // Get all the branchProfileList where typicalDay equals to UPDATED_TYPICAL_DAY
        defaultBranchProfileShouldNotBeFound("typicalDay.equals=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByTypicalDayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where typicalDay not equals to DEFAULT_TYPICAL_DAY
        defaultBranchProfileShouldNotBeFound("typicalDay.notEquals=" + DEFAULT_TYPICAL_DAY);

        // Get all the branchProfileList where typicalDay not equals to UPDATED_TYPICAL_DAY
        defaultBranchProfileShouldBeFound("typicalDay.notEquals=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByTypicalDayIsInShouldWork() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where typicalDay in DEFAULT_TYPICAL_DAY or UPDATED_TYPICAL_DAY
        defaultBranchProfileShouldBeFound("typicalDay.in=" + DEFAULT_TYPICAL_DAY + "," + UPDATED_TYPICAL_DAY);

        // Get all the branchProfileList where typicalDay equals to UPDATED_TYPICAL_DAY
        defaultBranchProfileShouldNotBeFound("typicalDay.in=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByTypicalDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where typicalDay is not null
        defaultBranchProfileShouldBeFound("typicalDay.specified=true");

        // Get all the branchProfileList where typicalDay is null
        defaultBranchProfileShouldNotBeFound("typicalDay.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchProfilesByTypicalDayContainsSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where typicalDay contains DEFAULT_TYPICAL_DAY
        defaultBranchProfileShouldBeFound("typicalDay.contains=" + DEFAULT_TYPICAL_DAY);

        // Get all the branchProfileList where typicalDay contains UPDATED_TYPICAL_DAY
        defaultBranchProfileShouldNotBeFound("typicalDay.contains=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByTypicalDayNotContainsSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where typicalDay does not contain DEFAULT_TYPICAL_DAY
        defaultBranchProfileShouldNotBeFound("typicalDay.doesNotContain=" + DEFAULT_TYPICAL_DAY);

        // Get all the branchProfileList where typicalDay does not contain UPDATED_TYPICAL_DAY
        defaultBranchProfileShouldBeFound("typicalDay.doesNotContain=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByModeIsEqualToSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where mode equals to DEFAULT_MODE
        defaultBranchProfileShouldBeFound("mode.equals=" + DEFAULT_MODE);

        // Get all the branchProfileList where mode equals to UPDATED_MODE
        defaultBranchProfileShouldNotBeFound("mode.equals=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByModeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where mode not equals to DEFAULT_MODE
        defaultBranchProfileShouldNotBeFound("mode.notEquals=" + DEFAULT_MODE);

        // Get all the branchProfileList where mode not equals to UPDATED_MODE
        defaultBranchProfileShouldBeFound("mode.notEquals=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByModeIsInShouldWork() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where mode in DEFAULT_MODE or UPDATED_MODE
        defaultBranchProfileShouldBeFound("mode.in=" + DEFAULT_MODE + "," + UPDATED_MODE);

        // Get all the branchProfileList where mode equals to UPDATED_MODE
        defaultBranchProfileShouldNotBeFound("mode.in=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByModeIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where mode is not null
        defaultBranchProfileShouldBeFound("mode.specified=true");

        // Get all the branchProfileList where mode is null
        defaultBranchProfileShouldNotBeFound("mode.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchProfilesByModeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where mode is greater than or equal to DEFAULT_MODE
        defaultBranchProfileShouldBeFound("mode.greaterThanOrEqual=" + DEFAULT_MODE);

        // Get all the branchProfileList where mode is greater than or equal to UPDATED_MODE
        defaultBranchProfileShouldNotBeFound("mode.greaterThanOrEqual=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByModeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where mode is less than or equal to DEFAULT_MODE
        defaultBranchProfileShouldBeFound("mode.lessThanOrEqual=" + DEFAULT_MODE);

        // Get all the branchProfileList where mode is less than or equal to SMALLER_MODE
        defaultBranchProfileShouldNotBeFound("mode.lessThanOrEqual=" + SMALLER_MODE);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByModeIsLessThanSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where mode is less than DEFAULT_MODE
        defaultBranchProfileShouldNotBeFound("mode.lessThan=" + DEFAULT_MODE);

        // Get all the branchProfileList where mode is less than UPDATED_MODE
        defaultBranchProfileShouldBeFound("mode.lessThan=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByModeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where mode is greater than DEFAULT_MODE
        defaultBranchProfileShouldNotBeFound("mode.greaterThan=" + DEFAULT_MODE);

        // Get all the branchProfileList where mode is greater than SMALLER_MODE
        defaultBranchProfileShouldBeFound("mode.greaterThan=" + SMALLER_MODE);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByTimeIntervalIsEqualToSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where timeInterval equals to DEFAULT_TIME_INTERVAL
        defaultBranchProfileShouldBeFound("timeInterval.equals=" + DEFAULT_TIME_INTERVAL);

        // Get all the branchProfileList where timeInterval equals to UPDATED_TIME_INTERVAL
        defaultBranchProfileShouldNotBeFound("timeInterval.equals=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByTimeIntervalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where timeInterval not equals to DEFAULT_TIME_INTERVAL
        defaultBranchProfileShouldNotBeFound("timeInterval.notEquals=" + DEFAULT_TIME_INTERVAL);

        // Get all the branchProfileList where timeInterval not equals to UPDATED_TIME_INTERVAL
        defaultBranchProfileShouldBeFound("timeInterval.notEquals=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByTimeIntervalIsInShouldWork() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where timeInterval in DEFAULT_TIME_INTERVAL or UPDATED_TIME_INTERVAL
        defaultBranchProfileShouldBeFound("timeInterval.in=" + DEFAULT_TIME_INTERVAL + "," + UPDATED_TIME_INTERVAL);

        // Get all the branchProfileList where timeInterval equals to UPDATED_TIME_INTERVAL
        defaultBranchProfileShouldNotBeFound("timeInterval.in=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByTimeIntervalIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where timeInterval is not null
        defaultBranchProfileShouldBeFound("timeInterval.specified=true");

        // Get all the branchProfileList where timeInterval is null
        defaultBranchProfileShouldNotBeFound("timeInterval.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchProfilesByTimeIntervalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where timeInterval is greater than or equal to DEFAULT_TIME_INTERVAL
        defaultBranchProfileShouldBeFound("timeInterval.greaterThanOrEqual=" + DEFAULT_TIME_INTERVAL);

        // Get all the branchProfileList where timeInterval is greater than or equal to UPDATED_TIME_INTERVAL
        defaultBranchProfileShouldNotBeFound("timeInterval.greaterThanOrEqual=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByTimeIntervalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where timeInterval is less than or equal to DEFAULT_TIME_INTERVAL
        defaultBranchProfileShouldBeFound("timeInterval.lessThanOrEqual=" + DEFAULT_TIME_INTERVAL);

        // Get all the branchProfileList where timeInterval is less than or equal to SMALLER_TIME_INTERVAL
        defaultBranchProfileShouldNotBeFound("timeInterval.lessThanOrEqual=" + SMALLER_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByTimeIntervalIsLessThanSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where timeInterval is less than DEFAULT_TIME_INTERVAL
        defaultBranchProfileShouldNotBeFound("timeInterval.lessThan=" + DEFAULT_TIME_INTERVAL);

        // Get all the branchProfileList where timeInterval is less than UPDATED_TIME_INTERVAL
        defaultBranchProfileShouldBeFound("timeInterval.lessThan=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByTimeIntervalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where timeInterval is greater than DEFAULT_TIME_INTERVAL
        defaultBranchProfileShouldNotBeFound("timeInterval.greaterThan=" + DEFAULT_TIME_INTERVAL);

        // Get all the branchProfileList where timeInterval is greater than SMALLER_TIME_INTERVAL
        defaultBranchProfileShouldBeFound("timeInterval.greaterThan=" + SMALLER_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByUploadDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where uploadDateTime equals to DEFAULT_UPLOAD_DATE_TIME
        defaultBranchProfileShouldBeFound("uploadDateTime.equals=" + DEFAULT_UPLOAD_DATE_TIME);

        // Get all the branchProfileList where uploadDateTime equals to UPDATED_UPLOAD_DATE_TIME
        defaultBranchProfileShouldNotBeFound("uploadDateTime.equals=" + UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByUploadDateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where uploadDateTime not equals to DEFAULT_UPLOAD_DATE_TIME
        defaultBranchProfileShouldNotBeFound("uploadDateTime.notEquals=" + DEFAULT_UPLOAD_DATE_TIME);

        // Get all the branchProfileList where uploadDateTime not equals to UPDATED_UPLOAD_DATE_TIME
        defaultBranchProfileShouldBeFound("uploadDateTime.notEquals=" + UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByUploadDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where uploadDateTime in DEFAULT_UPLOAD_DATE_TIME or UPDATED_UPLOAD_DATE_TIME
        defaultBranchProfileShouldBeFound("uploadDateTime.in=" + DEFAULT_UPLOAD_DATE_TIME + "," + UPDATED_UPLOAD_DATE_TIME);

        // Get all the branchProfileList where uploadDateTime equals to UPDATED_UPLOAD_DATE_TIME
        defaultBranchProfileShouldNotBeFound("uploadDateTime.in=" + UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllBranchProfilesByUploadDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        // Get all the branchProfileList where uploadDateTime is not null
        defaultBranchProfileShouldBeFound("uploadDateTime.specified=true");

        // Get all the branchProfileList where uploadDateTime is null
        defaultBranchProfileShouldNotBeFound("uploadDateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchProfilesByInputFileIsEqualToSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);
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
        branchProfile.setInputFile(inputFile);
        branchProfileRepository.saveAndFlush(branchProfile);
        Long inputFileId = inputFile.getId();

        // Get all the branchProfileList where inputFile equals to inputFileId
        defaultBranchProfileShouldBeFound("inputFileId.equals=" + inputFileId);

        // Get all the branchProfileList where inputFile equals to (inputFileId + 1)
        defaultBranchProfileShouldNotBeFound("inputFileId.equals=" + (inputFileId + 1));
    }

    @Test
    @Transactional
    void getAllBranchProfilesByBranchElValIsEqualToSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);
        BranchElVal branchElVal;
        if (TestUtil.findAll(em, BranchElVal.class).isEmpty()) {
            branchElVal = BranchElValResourceIT.createEntity(em);
            em.persist(branchElVal);
            em.flush();
        } else {
            branchElVal = TestUtil.findAll(em, BranchElVal.class).get(0);
        }
        em.persist(branchElVal);
        em.flush();
        branchProfile.addBranchElVal(branchElVal);
        branchProfileRepository.saveAndFlush(branchProfile);
        Long branchElValId = branchElVal.getId();

        // Get all the branchProfileList where branchElVal equals to branchElValId
        defaultBranchProfileShouldBeFound("branchElValId.equals=" + branchElValId);

        // Get all the branchProfileList where branchElVal equals to (branchElValId + 1)
        defaultBranchProfileShouldNotBeFound("branchElValId.equals=" + (branchElValId + 1));
    }

    @Test
    @Transactional
    void getAllBranchProfilesByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);
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
        branchProfile.setNetwork(network);
        branchProfileRepository.saveAndFlush(branchProfile);
        Long networkId = network.getId();

        // Get all the branchProfileList where network equals to networkId
        defaultBranchProfileShouldBeFound("networkId.equals=" + networkId);

        // Get all the branchProfileList where network equals to (networkId + 1)
        defaultBranchProfileShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBranchProfileShouldBeFound(String filter) throws Exception {
        restBranchProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branchProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].season").value(hasItem(DEFAULT_SEASON)))
            .andExpect(jsonPath("$.[*].typicalDay").value(hasItem(DEFAULT_TYPICAL_DAY)))
            .andExpect(jsonPath("$.[*].mode").value(hasItem(DEFAULT_MODE)))
            .andExpect(jsonPath("$.[*].timeInterval").value(hasItem(DEFAULT_TIME_INTERVAL.doubleValue())))
            .andExpect(jsonPath("$.[*].uploadDateTime").value(hasItem(DEFAULT_UPLOAD_DATE_TIME.toString())));

        // Check, that the count call also returns 1
        restBranchProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBranchProfileShouldNotBeFound(String filter) throws Exception {
        restBranchProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBranchProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBranchProfile() throws Exception {
        // Get the branchProfile
        restBranchProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBranchProfile() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        int databaseSizeBeforeUpdate = branchProfileRepository.findAll().size();

        // Update the branchProfile
        BranchProfile updatedBranchProfile = branchProfileRepository.findById(branchProfile.getId()).get();
        // Disconnect from session so that the updates on updatedBranchProfile are not directly saved in db
        em.detach(updatedBranchProfile);
        updatedBranchProfile
            .season(UPDATED_SEASON)
            .typicalDay(UPDATED_TYPICAL_DAY)
            .mode(UPDATED_MODE)
            .timeInterval(UPDATED_TIME_INTERVAL)
            .uploadDateTime(UPDATED_UPLOAD_DATE_TIME);
        BranchProfileDTO branchProfileDTO = branchProfileMapper.toDto(updatedBranchProfile);

        restBranchProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, branchProfileDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchProfileDTO))
            )
            .andExpect(status().isOk());

        // Validate the BranchProfile in the database
        List<BranchProfile> branchProfileList = branchProfileRepository.findAll();
        assertThat(branchProfileList).hasSize(databaseSizeBeforeUpdate);
        BranchProfile testBranchProfile = branchProfileList.get(branchProfileList.size() - 1);
        assertThat(testBranchProfile.getSeason()).isEqualTo(UPDATED_SEASON);
        assertThat(testBranchProfile.getTypicalDay()).isEqualTo(UPDATED_TYPICAL_DAY);
        assertThat(testBranchProfile.getMode()).isEqualTo(UPDATED_MODE);
        assertThat(testBranchProfile.getTimeInterval()).isEqualTo(UPDATED_TIME_INTERVAL);
        assertThat(testBranchProfile.getUploadDateTime()).isEqualTo(UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void putNonExistingBranchProfile() throws Exception {
        int databaseSizeBeforeUpdate = branchProfileRepository.findAll().size();
        branchProfile.setId(count.incrementAndGet());

        // Create the BranchProfile
        BranchProfileDTO branchProfileDTO = branchProfileMapper.toDto(branchProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, branchProfileDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchProfile in the database
        List<BranchProfile> branchProfileList = branchProfileRepository.findAll();
        assertThat(branchProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBranchProfile() throws Exception {
        int databaseSizeBeforeUpdate = branchProfileRepository.findAll().size();
        branchProfile.setId(count.incrementAndGet());

        // Create the BranchProfile
        BranchProfileDTO branchProfileDTO = branchProfileMapper.toDto(branchProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchProfile in the database
        List<BranchProfile> branchProfileList = branchProfileRepository.findAll();
        assertThat(branchProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBranchProfile() throws Exception {
        int databaseSizeBeforeUpdate = branchProfileRepository.findAll().size();
        branchProfile.setId(count.incrementAndGet());

        // Create the BranchProfile
        BranchProfileDTO branchProfileDTO = branchProfileMapper.toDto(branchProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchProfileMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BranchProfile in the database
        List<BranchProfile> branchProfileList = branchProfileRepository.findAll();
        assertThat(branchProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBranchProfileWithPatch() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        int databaseSizeBeforeUpdate = branchProfileRepository.findAll().size();

        // Update the branchProfile using partial update
        BranchProfile partialUpdatedBranchProfile = new BranchProfile();
        partialUpdatedBranchProfile.setId(branchProfile.getId());

        partialUpdatedBranchProfile
            .season(UPDATED_SEASON)
            .mode(UPDATED_MODE)
            .timeInterval(UPDATED_TIME_INTERVAL)
            .uploadDateTime(UPDATED_UPLOAD_DATE_TIME);

        restBranchProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBranchProfile.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBranchProfile))
            )
            .andExpect(status().isOk());

        // Validate the BranchProfile in the database
        List<BranchProfile> branchProfileList = branchProfileRepository.findAll();
        assertThat(branchProfileList).hasSize(databaseSizeBeforeUpdate);
        BranchProfile testBranchProfile = branchProfileList.get(branchProfileList.size() - 1);
        assertThat(testBranchProfile.getSeason()).isEqualTo(UPDATED_SEASON);
        assertThat(testBranchProfile.getTypicalDay()).isEqualTo(DEFAULT_TYPICAL_DAY);
        assertThat(testBranchProfile.getMode()).isEqualTo(UPDATED_MODE);
        assertThat(testBranchProfile.getTimeInterval()).isEqualTo(UPDATED_TIME_INTERVAL);
        assertThat(testBranchProfile.getUploadDateTime()).isEqualTo(UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void fullUpdateBranchProfileWithPatch() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        int databaseSizeBeforeUpdate = branchProfileRepository.findAll().size();

        // Update the branchProfile using partial update
        BranchProfile partialUpdatedBranchProfile = new BranchProfile();
        partialUpdatedBranchProfile.setId(branchProfile.getId());

        partialUpdatedBranchProfile
            .season(UPDATED_SEASON)
            .typicalDay(UPDATED_TYPICAL_DAY)
            .mode(UPDATED_MODE)
            .timeInterval(UPDATED_TIME_INTERVAL)
            .uploadDateTime(UPDATED_UPLOAD_DATE_TIME);

        restBranchProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBranchProfile.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBranchProfile))
            )
            .andExpect(status().isOk());

        // Validate the BranchProfile in the database
        List<BranchProfile> branchProfileList = branchProfileRepository.findAll();
        assertThat(branchProfileList).hasSize(databaseSizeBeforeUpdate);
        BranchProfile testBranchProfile = branchProfileList.get(branchProfileList.size() - 1);
        assertThat(testBranchProfile.getSeason()).isEqualTo(UPDATED_SEASON);
        assertThat(testBranchProfile.getTypicalDay()).isEqualTo(UPDATED_TYPICAL_DAY);
        assertThat(testBranchProfile.getMode()).isEqualTo(UPDATED_MODE);
        assertThat(testBranchProfile.getTimeInterval()).isEqualTo(UPDATED_TIME_INTERVAL);
        assertThat(testBranchProfile.getUploadDateTime()).isEqualTo(UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingBranchProfile() throws Exception {
        int databaseSizeBeforeUpdate = branchProfileRepository.findAll().size();
        branchProfile.setId(count.incrementAndGet());

        // Create the BranchProfile
        BranchProfileDTO branchProfileDTO = branchProfileMapper.toDto(branchProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, branchProfileDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(branchProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchProfile in the database
        List<BranchProfile> branchProfileList = branchProfileRepository.findAll();
        assertThat(branchProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBranchProfile() throws Exception {
        int databaseSizeBeforeUpdate = branchProfileRepository.findAll().size();
        branchProfile.setId(count.incrementAndGet());

        // Create the BranchProfile
        BranchProfileDTO branchProfileDTO = branchProfileMapper.toDto(branchProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(branchProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchProfile in the database
        List<BranchProfile> branchProfileList = branchProfileRepository.findAll();
        assertThat(branchProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBranchProfile() throws Exception {
        int databaseSizeBeforeUpdate = branchProfileRepository.findAll().size();
        branchProfile.setId(count.incrementAndGet());

        // Create the BranchProfile
        BranchProfileDTO branchProfileDTO = branchProfileMapper.toDto(branchProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchProfileMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(branchProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BranchProfile in the database
        List<BranchProfile> branchProfileList = branchProfileRepository.findAll();
        assertThat(branchProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBranchProfile() throws Exception {
        // Initialize the database
        branchProfileRepository.saveAndFlush(branchProfile);

        int databaseSizeBeforeDelete = branchProfileRepository.findAll().size();

        // Delete the branchProfile
        restBranchProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, branchProfile.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BranchProfile> branchProfileList = branchProfileRepository.findAll();
        assertThat(branchProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
