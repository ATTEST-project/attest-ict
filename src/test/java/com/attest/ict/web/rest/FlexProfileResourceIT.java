package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.FlexCost;
import com.attest.ict.domain.FlexElVal;
import com.attest.ict.domain.FlexProfile;
import com.attest.ict.domain.InputFile;
import com.attest.ict.domain.Network;
import com.attest.ict.repository.FlexProfileRepository;
import com.attest.ict.service.criteria.FlexProfileCriteria;
import com.attest.ict.service.dto.FlexProfileDTO;
import com.attest.ict.service.mapper.FlexProfileMapper;
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
 * Integration tests for the {@link FlexProfileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FlexProfileResourceIT {

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

    private static final String ENTITY_API_URL = "/api/flex-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FlexProfileRepository flexProfileRepository;

    @Autowired
    private FlexProfileMapper flexProfileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFlexProfileMockMvc;

    private FlexProfile flexProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlexProfile createEntity(EntityManager em) {
        FlexProfile flexProfile = new FlexProfile()
            .season(DEFAULT_SEASON)
            .typicalDay(DEFAULT_TYPICAL_DAY)
            .mode(DEFAULT_MODE)
            .timeInterval(DEFAULT_TIME_INTERVAL)
            .uploadDateTime(DEFAULT_UPLOAD_DATE_TIME);
        return flexProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlexProfile createUpdatedEntity(EntityManager em) {
        FlexProfile flexProfile = new FlexProfile()
            .season(UPDATED_SEASON)
            .typicalDay(UPDATED_TYPICAL_DAY)
            .mode(UPDATED_MODE)
            .timeInterval(UPDATED_TIME_INTERVAL)
            .uploadDateTime(UPDATED_UPLOAD_DATE_TIME);
        return flexProfile;
    }

    @BeforeEach
    public void initTest() {
        flexProfile = createEntity(em);
    }

    @Test
    @Transactional
    void createFlexProfile() throws Exception {
        int databaseSizeBeforeCreate = flexProfileRepository.findAll().size();
        // Create the FlexProfile
        FlexProfileDTO flexProfileDTO = flexProfileMapper.toDto(flexProfile);
        restFlexProfileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexProfileDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FlexProfile in the database
        List<FlexProfile> flexProfileList = flexProfileRepository.findAll();
        assertThat(flexProfileList).hasSize(databaseSizeBeforeCreate + 1);
        FlexProfile testFlexProfile = flexProfileList.get(flexProfileList.size() - 1);
        assertThat(testFlexProfile.getSeason()).isEqualTo(DEFAULT_SEASON);
        assertThat(testFlexProfile.getTypicalDay()).isEqualTo(DEFAULT_TYPICAL_DAY);
        assertThat(testFlexProfile.getMode()).isEqualTo(DEFAULT_MODE);
        assertThat(testFlexProfile.getTimeInterval()).isEqualTo(DEFAULT_TIME_INTERVAL);
        assertThat(testFlexProfile.getUploadDateTime()).isEqualTo(DEFAULT_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void createFlexProfileWithExistingId() throws Exception {
        // Create the FlexProfile with an existing ID
        flexProfile.setId(1L);
        FlexProfileDTO flexProfileDTO = flexProfileMapper.toDto(flexProfile);

        int databaseSizeBeforeCreate = flexProfileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlexProfileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexProfile in the database
        List<FlexProfile> flexProfileList = flexProfileRepository.findAll();
        assertThat(flexProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFlexProfiles() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList
        restFlexProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flexProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].season").value(hasItem(DEFAULT_SEASON)))
            .andExpect(jsonPath("$.[*].typicalDay").value(hasItem(DEFAULT_TYPICAL_DAY)))
            .andExpect(jsonPath("$.[*].mode").value(hasItem(DEFAULT_MODE)))
            .andExpect(jsonPath("$.[*].timeInterval").value(hasItem(DEFAULT_TIME_INTERVAL.doubleValue())))
            .andExpect(jsonPath("$.[*].uploadDateTime").value(hasItem(DEFAULT_UPLOAD_DATE_TIME.toString())));
    }

    @Test
    @Transactional
    void getFlexProfile() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get the flexProfile
        restFlexProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, flexProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flexProfile.getId().intValue()))
            .andExpect(jsonPath("$.season").value(DEFAULT_SEASON))
            .andExpect(jsonPath("$.typicalDay").value(DEFAULT_TYPICAL_DAY))
            .andExpect(jsonPath("$.mode").value(DEFAULT_MODE))
            .andExpect(jsonPath("$.timeInterval").value(DEFAULT_TIME_INTERVAL.doubleValue()))
            .andExpect(jsonPath("$.uploadDateTime").value(DEFAULT_UPLOAD_DATE_TIME.toString()));
    }

    @Test
    @Transactional
    void getFlexProfilesByIdFiltering() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        Long id = flexProfile.getId();

        defaultFlexProfileShouldBeFound("id.equals=" + id);
        defaultFlexProfileShouldNotBeFound("id.notEquals=" + id);

        defaultFlexProfileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFlexProfileShouldNotBeFound("id.greaterThan=" + id);

        defaultFlexProfileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFlexProfileShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFlexProfilesBySeasonIsEqualToSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where season equals to DEFAULT_SEASON
        defaultFlexProfileShouldBeFound("season.equals=" + DEFAULT_SEASON);

        // Get all the flexProfileList where season equals to UPDATED_SEASON
        defaultFlexProfileShouldNotBeFound("season.equals=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllFlexProfilesBySeasonIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where season not equals to DEFAULT_SEASON
        defaultFlexProfileShouldNotBeFound("season.notEquals=" + DEFAULT_SEASON);

        // Get all the flexProfileList where season not equals to UPDATED_SEASON
        defaultFlexProfileShouldBeFound("season.notEquals=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllFlexProfilesBySeasonIsInShouldWork() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where season in DEFAULT_SEASON or UPDATED_SEASON
        defaultFlexProfileShouldBeFound("season.in=" + DEFAULT_SEASON + "," + UPDATED_SEASON);

        // Get all the flexProfileList where season equals to UPDATED_SEASON
        defaultFlexProfileShouldNotBeFound("season.in=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllFlexProfilesBySeasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where season is not null
        defaultFlexProfileShouldBeFound("season.specified=true");

        // Get all the flexProfileList where season is null
        defaultFlexProfileShouldNotBeFound("season.specified=false");
    }

    @Test
    @Transactional
    void getAllFlexProfilesBySeasonContainsSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where season contains DEFAULT_SEASON
        defaultFlexProfileShouldBeFound("season.contains=" + DEFAULT_SEASON);

        // Get all the flexProfileList where season contains UPDATED_SEASON
        defaultFlexProfileShouldNotBeFound("season.contains=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllFlexProfilesBySeasonNotContainsSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where season does not contain DEFAULT_SEASON
        defaultFlexProfileShouldNotBeFound("season.doesNotContain=" + DEFAULT_SEASON);

        // Get all the flexProfileList where season does not contain UPDATED_SEASON
        defaultFlexProfileShouldBeFound("season.doesNotContain=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByTypicalDayIsEqualToSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where typicalDay equals to DEFAULT_TYPICAL_DAY
        defaultFlexProfileShouldBeFound("typicalDay.equals=" + DEFAULT_TYPICAL_DAY);

        // Get all the flexProfileList where typicalDay equals to UPDATED_TYPICAL_DAY
        defaultFlexProfileShouldNotBeFound("typicalDay.equals=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByTypicalDayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where typicalDay not equals to DEFAULT_TYPICAL_DAY
        defaultFlexProfileShouldNotBeFound("typicalDay.notEquals=" + DEFAULT_TYPICAL_DAY);

        // Get all the flexProfileList where typicalDay not equals to UPDATED_TYPICAL_DAY
        defaultFlexProfileShouldBeFound("typicalDay.notEquals=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByTypicalDayIsInShouldWork() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where typicalDay in DEFAULT_TYPICAL_DAY or UPDATED_TYPICAL_DAY
        defaultFlexProfileShouldBeFound("typicalDay.in=" + DEFAULT_TYPICAL_DAY + "," + UPDATED_TYPICAL_DAY);

        // Get all the flexProfileList where typicalDay equals to UPDATED_TYPICAL_DAY
        defaultFlexProfileShouldNotBeFound("typicalDay.in=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByTypicalDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where typicalDay is not null
        defaultFlexProfileShouldBeFound("typicalDay.specified=true");

        // Get all the flexProfileList where typicalDay is null
        defaultFlexProfileShouldNotBeFound("typicalDay.specified=false");
    }

    @Test
    @Transactional
    void getAllFlexProfilesByTypicalDayContainsSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where typicalDay contains DEFAULT_TYPICAL_DAY
        defaultFlexProfileShouldBeFound("typicalDay.contains=" + DEFAULT_TYPICAL_DAY);

        // Get all the flexProfileList where typicalDay contains UPDATED_TYPICAL_DAY
        defaultFlexProfileShouldNotBeFound("typicalDay.contains=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByTypicalDayNotContainsSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where typicalDay does not contain DEFAULT_TYPICAL_DAY
        defaultFlexProfileShouldNotBeFound("typicalDay.doesNotContain=" + DEFAULT_TYPICAL_DAY);

        // Get all the flexProfileList where typicalDay does not contain UPDATED_TYPICAL_DAY
        defaultFlexProfileShouldBeFound("typicalDay.doesNotContain=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByModeIsEqualToSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where mode equals to DEFAULT_MODE
        defaultFlexProfileShouldBeFound("mode.equals=" + DEFAULT_MODE);

        // Get all the flexProfileList where mode equals to UPDATED_MODE
        defaultFlexProfileShouldNotBeFound("mode.equals=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByModeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where mode not equals to DEFAULT_MODE
        defaultFlexProfileShouldNotBeFound("mode.notEquals=" + DEFAULT_MODE);

        // Get all the flexProfileList where mode not equals to UPDATED_MODE
        defaultFlexProfileShouldBeFound("mode.notEquals=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByModeIsInShouldWork() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where mode in DEFAULT_MODE or UPDATED_MODE
        defaultFlexProfileShouldBeFound("mode.in=" + DEFAULT_MODE + "," + UPDATED_MODE);

        // Get all the flexProfileList where mode equals to UPDATED_MODE
        defaultFlexProfileShouldNotBeFound("mode.in=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByModeIsNullOrNotNull() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where mode is not null
        defaultFlexProfileShouldBeFound("mode.specified=true");

        // Get all the flexProfileList where mode is null
        defaultFlexProfileShouldNotBeFound("mode.specified=false");
    }

    @Test
    @Transactional
    void getAllFlexProfilesByModeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where mode is greater than or equal to DEFAULT_MODE
        defaultFlexProfileShouldBeFound("mode.greaterThanOrEqual=" + DEFAULT_MODE);

        // Get all the flexProfileList where mode is greater than or equal to UPDATED_MODE
        defaultFlexProfileShouldNotBeFound("mode.greaterThanOrEqual=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByModeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where mode is less than or equal to DEFAULT_MODE
        defaultFlexProfileShouldBeFound("mode.lessThanOrEqual=" + DEFAULT_MODE);

        // Get all the flexProfileList where mode is less than or equal to SMALLER_MODE
        defaultFlexProfileShouldNotBeFound("mode.lessThanOrEqual=" + SMALLER_MODE);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByModeIsLessThanSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where mode is less than DEFAULT_MODE
        defaultFlexProfileShouldNotBeFound("mode.lessThan=" + DEFAULT_MODE);

        // Get all the flexProfileList where mode is less than UPDATED_MODE
        defaultFlexProfileShouldBeFound("mode.lessThan=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByModeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where mode is greater than DEFAULT_MODE
        defaultFlexProfileShouldNotBeFound("mode.greaterThan=" + DEFAULT_MODE);

        // Get all the flexProfileList where mode is greater than SMALLER_MODE
        defaultFlexProfileShouldBeFound("mode.greaterThan=" + SMALLER_MODE);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByTimeIntervalIsEqualToSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where timeInterval equals to DEFAULT_TIME_INTERVAL
        defaultFlexProfileShouldBeFound("timeInterval.equals=" + DEFAULT_TIME_INTERVAL);

        // Get all the flexProfileList where timeInterval equals to UPDATED_TIME_INTERVAL
        defaultFlexProfileShouldNotBeFound("timeInterval.equals=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByTimeIntervalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where timeInterval not equals to DEFAULT_TIME_INTERVAL
        defaultFlexProfileShouldNotBeFound("timeInterval.notEquals=" + DEFAULT_TIME_INTERVAL);

        // Get all the flexProfileList where timeInterval not equals to UPDATED_TIME_INTERVAL
        defaultFlexProfileShouldBeFound("timeInterval.notEquals=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByTimeIntervalIsInShouldWork() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where timeInterval in DEFAULT_TIME_INTERVAL or UPDATED_TIME_INTERVAL
        defaultFlexProfileShouldBeFound("timeInterval.in=" + DEFAULT_TIME_INTERVAL + "," + UPDATED_TIME_INTERVAL);

        // Get all the flexProfileList where timeInterval equals to UPDATED_TIME_INTERVAL
        defaultFlexProfileShouldNotBeFound("timeInterval.in=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByTimeIntervalIsNullOrNotNull() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where timeInterval is not null
        defaultFlexProfileShouldBeFound("timeInterval.specified=true");

        // Get all the flexProfileList where timeInterval is null
        defaultFlexProfileShouldNotBeFound("timeInterval.specified=false");
    }

    @Test
    @Transactional
    void getAllFlexProfilesByTimeIntervalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where timeInterval is greater than or equal to DEFAULT_TIME_INTERVAL
        defaultFlexProfileShouldBeFound("timeInterval.greaterThanOrEqual=" + DEFAULT_TIME_INTERVAL);

        // Get all the flexProfileList where timeInterval is greater than or equal to UPDATED_TIME_INTERVAL
        defaultFlexProfileShouldNotBeFound("timeInterval.greaterThanOrEqual=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByTimeIntervalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where timeInterval is less than or equal to DEFAULT_TIME_INTERVAL
        defaultFlexProfileShouldBeFound("timeInterval.lessThanOrEqual=" + DEFAULT_TIME_INTERVAL);

        // Get all the flexProfileList where timeInterval is less than or equal to SMALLER_TIME_INTERVAL
        defaultFlexProfileShouldNotBeFound("timeInterval.lessThanOrEqual=" + SMALLER_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByTimeIntervalIsLessThanSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where timeInterval is less than DEFAULT_TIME_INTERVAL
        defaultFlexProfileShouldNotBeFound("timeInterval.lessThan=" + DEFAULT_TIME_INTERVAL);

        // Get all the flexProfileList where timeInterval is less than UPDATED_TIME_INTERVAL
        defaultFlexProfileShouldBeFound("timeInterval.lessThan=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByTimeIntervalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where timeInterval is greater than DEFAULT_TIME_INTERVAL
        defaultFlexProfileShouldNotBeFound("timeInterval.greaterThan=" + DEFAULT_TIME_INTERVAL);

        // Get all the flexProfileList where timeInterval is greater than SMALLER_TIME_INTERVAL
        defaultFlexProfileShouldBeFound("timeInterval.greaterThan=" + SMALLER_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByUploadDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where uploadDateTime equals to DEFAULT_UPLOAD_DATE_TIME
        defaultFlexProfileShouldBeFound("uploadDateTime.equals=" + DEFAULT_UPLOAD_DATE_TIME);

        // Get all the flexProfileList where uploadDateTime equals to UPDATED_UPLOAD_DATE_TIME
        defaultFlexProfileShouldNotBeFound("uploadDateTime.equals=" + UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByUploadDateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where uploadDateTime not equals to DEFAULT_UPLOAD_DATE_TIME
        defaultFlexProfileShouldNotBeFound("uploadDateTime.notEquals=" + DEFAULT_UPLOAD_DATE_TIME);

        // Get all the flexProfileList where uploadDateTime not equals to UPDATED_UPLOAD_DATE_TIME
        defaultFlexProfileShouldBeFound("uploadDateTime.notEquals=" + UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByUploadDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where uploadDateTime in DEFAULT_UPLOAD_DATE_TIME or UPDATED_UPLOAD_DATE_TIME
        defaultFlexProfileShouldBeFound("uploadDateTime.in=" + DEFAULT_UPLOAD_DATE_TIME + "," + UPDATED_UPLOAD_DATE_TIME);

        // Get all the flexProfileList where uploadDateTime equals to UPDATED_UPLOAD_DATE_TIME
        defaultFlexProfileShouldNotBeFound("uploadDateTime.in=" + UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllFlexProfilesByUploadDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        // Get all the flexProfileList where uploadDateTime is not null
        defaultFlexProfileShouldBeFound("uploadDateTime.specified=true");

        // Get all the flexProfileList where uploadDateTime is null
        defaultFlexProfileShouldNotBeFound("uploadDateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllFlexProfilesByInputFileIsEqualToSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);
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
        flexProfile.setInputFile(inputFile);
        flexProfileRepository.saveAndFlush(flexProfile);
        Long inputFileId = inputFile.getId();

        // Get all the flexProfileList where inputFile equals to inputFileId
        defaultFlexProfileShouldBeFound("inputFileId.equals=" + inputFileId);

        // Get all the flexProfileList where inputFile equals to (inputFileId + 1)
        defaultFlexProfileShouldNotBeFound("inputFileId.equals=" + (inputFileId + 1));
    }

    @Test
    @Transactional
    void getAllFlexProfilesByFlexElValIsEqualToSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);
        FlexElVal flexElVal;
        if (TestUtil.findAll(em, FlexElVal.class).isEmpty()) {
            flexElVal = FlexElValResourceIT.createEntity(em);
            em.persist(flexElVal);
            em.flush();
        } else {
            flexElVal = TestUtil.findAll(em, FlexElVal.class).get(0);
        }
        em.persist(flexElVal);
        em.flush();
        flexProfile.addFlexElVal(flexElVal);
        flexProfileRepository.saveAndFlush(flexProfile);
        Long flexElValId = flexElVal.getId();

        // Get all the flexProfileList where flexElVal equals to flexElValId
        defaultFlexProfileShouldBeFound("flexElValId.equals=" + flexElValId);

        // Get all the flexProfileList where flexElVal equals to (flexElValId + 1)
        defaultFlexProfileShouldNotBeFound("flexElValId.equals=" + (flexElValId + 1));
    }

    @Test
    @Transactional
    void getAllFlexProfilesByFlexCostIsEqualToSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);
        FlexCost flexCost;
        if (TestUtil.findAll(em, FlexCost.class).isEmpty()) {
            flexCost = FlexCostResourceIT.createEntity(em);
            em.persist(flexCost);
            em.flush();
        } else {
            flexCost = TestUtil.findAll(em, FlexCost.class).get(0);
        }
        em.persist(flexCost);
        em.flush();
        flexProfile.addFlexCost(flexCost);
        flexProfileRepository.saveAndFlush(flexProfile);
        Long flexCostId = flexCost.getId();

        // Get all the flexProfileList where flexCost equals to flexCostId
        defaultFlexProfileShouldBeFound("flexCostId.equals=" + flexCostId);

        // Get all the flexProfileList where flexCost equals to (flexCostId + 1)
        defaultFlexProfileShouldNotBeFound("flexCostId.equals=" + (flexCostId + 1));
    }

    @Test
    @Transactional
    void getAllFlexProfilesByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);
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
        flexProfile.setNetwork(network);
        flexProfileRepository.saveAndFlush(flexProfile);
        Long networkId = network.getId();

        // Get all the flexProfileList where network equals to networkId
        defaultFlexProfileShouldBeFound("networkId.equals=" + networkId);

        // Get all the flexProfileList where network equals to (networkId + 1)
        defaultFlexProfileShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFlexProfileShouldBeFound(String filter) throws Exception {
        restFlexProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flexProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].season").value(hasItem(DEFAULT_SEASON)))
            .andExpect(jsonPath("$.[*].typicalDay").value(hasItem(DEFAULT_TYPICAL_DAY)))
            .andExpect(jsonPath("$.[*].mode").value(hasItem(DEFAULT_MODE)))
            .andExpect(jsonPath("$.[*].timeInterval").value(hasItem(DEFAULT_TIME_INTERVAL.doubleValue())))
            .andExpect(jsonPath("$.[*].uploadDateTime").value(hasItem(DEFAULT_UPLOAD_DATE_TIME.toString())));

        // Check, that the count call also returns 1
        restFlexProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFlexProfileShouldNotBeFound(String filter) throws Exception {
        restFlexProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFlexProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFlexProfile() throws Exception {
        // Get the flexProfile
        restFlexProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFlexProfile() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        int databaseSizeBeforeUpdate = flexProfileRepository.findAll().size();

        // Update the flexProfile
        FlexProfile updatedFlexProfile = flexProfileRepository.findById(flexProfile.getId()).get();
        // Disconnect from session so that the updates on updatedFlexProfile are not directly saved in db
        em.detach(updatedFlexProfile);
        updatedFlexProfile
            .season(UPDATED_SEASON)
            .typicalDay(UPDATED_TYPICAL_DAY)
            .mode(UPDATED_MODE)
            .timeInterval(UPDATED_TIME_INTERVAL)
            .uploadDateTime(UPDATED_UPLOAD_DATE_TIME);
        FlexProfileDTO flexProfileDTO = flexProfileMapper.toDto(updatedFlexProfile);

        restFlexProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flexProfileDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexProfileDTO))
            )
            .andExpect(status().isOk());

        // Validate the FlexProfile in the database
        List<FlexProfile> flexProfileList = flexProfileRepository.findAll();
        assertThat(flexProfileList).hasSize(databaseSizeBeforeUpdate);
        FlexProfile testFlexProfile = flexProfileList.get(flexProfileList.size() - 1);
        assertThat(testFlexProfile.getSeason()).isEqualTo(UPDATED_SEASON);
        assertThat(testFlexProfile.getTypicalDay()).isEqualTo(UPDATED_TYPICAL_DAY);
        assertThat(testFlexProfile.getMode()).isEqualTo(UPDATED_MODE);
        assertThat(testFlexProfile.getTimeInterval()).isEqualTo(UPDATED_TIME_INTERVAL);
        assertThat(testFlexProfile.getUploadDateTime()).isEqualTo(UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void putNonExistingFlexProfile() throws Exception {
        int databaseSizeBeforeUpdate = flexProfileRepository.findAll().size();
        flexProfile.setId(count.incrementAndGet());

        // Create the FlexProfile
        FlexProfileDTO flexProfileDTO = flexProfileMapper.toDto(flexProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlexProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flexProfileDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexProfile in the database
        List<FlexProfile> flexProfileList = flexProfileRepository.findAll();
        assertThat(flexProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFlexProfile() throws Exception {
        int databaseSizeBeforeUpdate = flexProfileRepository.findAll().size();
        flexProfile.setId(count.incrementAndGet());

        // Create the FlexProfile
        FlexProfileDTO flexProfileDTO = flexProfileMapper.toDto(flexProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexProfile in the database
        List<FlexProfile> flexProfileList = flexProfileRepository.findAll();
        assertThat(flexProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFlexProfile() throws Exception {
        int databaseSizeBeforeUpdate = flexProfileRepository.findAll().size();
        flexProfile.setId(count.incrementAndGet());

        // Create the FlexProfile
        FlexProfileDTO flexProfileDTO = flexProfileMapper.toDto(flexProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexProfileMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlexProfile in the database
        List<FlexProfile> flexProfileList = flexProfileRepository.findAll();
        assertThat(flexProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFlexProfileWithPatch() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        int databaseSizeBeforeUpdate = flexProfileRepository.findAll().size();

        // Update the flexProfile using partial update
        FlexProfile partialUpdatedFlexProfile = new FlexProfile();
        partialUpdatedFlexProfile.setId(flexProfile.getId());

        partialUpdatedFlexProfile.uploadDateTime(UPDATED_UPLOAD_DATE_TIME);

        restFlexProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlexProfile.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlexProfile))
            )
            .andExpect(status().isOk());

        // Validate the FlexProfile in the database
        List<FlexProfile> flexProfileList = flexProfileRepository.findAll();
        assertThat(flexProfileList).hasSize(databaseSizeBeforeUpdate);
        FlexProfile testFlexProfile = flexProfileList.get(flexProfileList.size() - 1);
        assertThat(testFlexProfile.getSeason()).isEqualTo(DEFAULT_SEASON);
        assertThat(testFlexProfile.getTypicalDay()).isEqualTo(DEFAULT_TYPICAL_DAY);
        assertThat(testFlexProfile.getMode()).isEqualTo(DEFAULT_MODE);
        assertThat(testFlexProfile.getTimeInterval()).isEqualTo(DEFAULT_TIME_INTERVAL);
        assertThat(testFlexProfile.getUploadDateTime()).isEqualTo(UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void fullUpdateFlexProfileWithPatch() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        int databaseSizeBeforeUpdate = flexProfileRepository.findAll().size();

        // Update the flexProfile using partial update
        FlexProfile partialUpdatedFlexProfile = new FlexProfile();
        partialUpdatedFlexProfile.setId(flexProfile.getId());

        partialUpdatedFlexProfile
            .season(UPDATED_SEASON)
            .typicalDay(UPDATED_TYPICAL_DAY)
            .mode(UPDATED_MODE)
            .timeInterval(UPDATED_TIME_INTERVAL)
            .uploadDateTime(UPDATED_UPLOAD_DATE_TIME);

        restFlexProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlexProfile.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlexProfile))
            )
            .andExpect(status().isOk());

        // Validate the FlexProfile in the database
        List<FlexProfile> flexProfileList = flexProfileRepository.findAll();
        assertThat(flexProfileList).hasSize(databaseSizeBeforeUpdate);
        FlexProfile testFlexProfile = flexProfileList.get(flexProfileList.size() - 1);
        assertThat(testFlexProfile.getSeason()).isEqualTo(UPDATED_SEASON);
        assertThat(testFlexProfile.getTypicalDay()).isEqualTo(UPDATED_TYPICAL_DAY);
        assertThat(testFlexProfile.getMode()).isEqualTo(UPDATED_MODE);
        assertThat(testFlexProfile.getTimeInterval()).isEqualTo(UPDATED_TIME_INTERVAL);
        assertThat(testFlexProfile.getUploadDateTime()).isEqualTo(UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingFlexProfile() throws Exception {
        int databaseSizeBeforeUpdate = flexProfileRepository.findAll().size();
        flexProfile.setId(count.incrementAndGet());

        // Create the FlexProfile
        FlexProfileDTO flexProfileDTO = flexProfileMapper.toDto(flexProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlexProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, flexProfileDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flexProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexProfile in the database
        List<FlexProfile> flexProfileList = flexProfileRepository.findAll();
        assertThat(flexProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFlexProfile() throws Exception {
        int databaseSizeBeforeUpdate = flexProfileRepository.findAll().size();
        flexProfile.setId(count.incrementAndGet());

        // Create the FlexProfile
        FlexProfileDTO flexProfileDTO = flexProfileMapper.toDto(flexProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flexProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexProfile in the database
        List<FlexProfile> flexProfileList = flexProfileRepository.findAll();
        assertThat(flexProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFlexProfile() throws Exception {
        int databaseSizeBeforeUpdate = flexProfileRepository.findAll().size();
        flexProfile.setId(count.incrementAndGet());

        // Create the FlexProfile
        FlexProfileDTO flexProfileDTO = flexProfileMapper.toDto(flexProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexProfileMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flexProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlexProfile in the database
        List<FlexProfile> flexProfileList = flexProfileRepository.findAll();
        assertThat(flexProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFlexProfile() throws Exception {
        // Initialize the database
        flexProfileRepository.saveAndFlush(flexProfile);

        int databaseSizeBeforeDelete = flexProfileRepository.findAll().size();

        // Delete the flexProfile
        restFlexProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, flexProfile.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FlexProfile> flexProfileList = flexProfileRepository.findAll();
        assertThat(flexProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
