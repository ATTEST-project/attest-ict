package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.InputFile;
import com.attest.ict.domain.Network;
import com.attest.ict.domain.TransfElVal;
import com.attest.ict.domain.TransfProfile;
import com.attest.ict.repository.TransfProfileRepository;
import com.attest.ict.service.criteria.TransfProfileCriteria;
import com.attest.ict.service.dto.TransfProfileDTO;
import com.attest.ict.service.mapper.TransfProfileMapper;
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
 * Integration tests for the {@link TransfProfileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransfProfileResourceIT {

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

    private static final String ENTITY_API_URL = "/api/transf-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransfProfileRepository transfProfileRepository;

    @Autowired
    private TransfProfileMapper transfProfileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransfProfileMockMvc;

    private TransfProfile transfProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransfProfile createEntity(EntityManager em) {
        TransfProfile transfProfile = new TransfProfile()
            .season(DEFAULT_SEASON)
            .typicalDay(DEFAULT_TYPICAL_DAY)
            .mode(DEFAULT_MODE)
            .timeInterval(DEFAULT_TIME_INTERVAL)
            .uploadDateTime(DEFAULT_UPLOAD_DATE_TIME);
        return transfProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransfProfile createUpdatedEntity(EntityManager em) {
        TransfProfile transfProfile = new TransfProfile()
            .season(UPDATED_SEASON)
            .typicalDay(UPDATED_TYPICAL_DAY)
            .mode(UPDATED_MODE)
            .timeInterval(UPDATED_TIME_INTERVAL)
            .uploadDateTime(UPDATED_UPLOAD_DATE_TIME);
        return transfProfile;
    }

    @BeforeEach
    public void initTest() {
        transfProfile = createEntity(em);
    }

    @Test
    @Transactional
    void createTransfProfile() throws Exception {
        int databaseSizeBeforeCreate = transfProfileRepository.findAll().size();
        // Create the TransfProfile
        TransfProfileDTO transfProfileDTO = transfProfileMapper.toDto(transfProfile);
        restTransfProfileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transfProfileDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TransfProfile in the database
        List<TransfProfile> transfProfileList = transfProfileRepository.findAll();
        assertThat(transfProfileList).hasSize(databaseSizeBeforeCreate + 1);
        TransfProfile testTransfProfile = transfProfileList.get(transfProfileList.size() - 1);
        assertThat(testTransfProfile.getSeason()).isEqualTo(DEFAULT_SEASON);
        assertThat(testTransfProfile.getTypicalDay()).isEqualTo(DEFAULT_TYPICAL_DAY);
        assertThat(testTransfProfile.getMode()).isEqualTo(DEFAULT_MODE);
        assertThat(testTransfProfile.getTimeInterval()).isEqualTo(DEFAULT_TIME_INTERVAL);
        assertThat(testTransfProfile.getUploadDateTime()).isEqualTo(DEFAULT_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void createTransfProfileWithExistingId() throws Exception {
        // Create the TransfProfile with an existing ID
        transfProfile.setId(1L);
        TransfProfileDTO transfProfileDTO = transfProfileMapper.toDto(transfProfile);

        int databaseSizeBeforeCreate = transfProfileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransfProfileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transfProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransfProfile in the database
        List<TransfProfile> transfProfileList = transfProfileRepository.findAll();
        assertThat(transfProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTransfProfiles() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList
        restTransfProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transfProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].season").value(hasItem(DEFAULT_SEASON)))
            .andExpect(jsonPath("$.[*].typicalDay").value(hasItem(DEFAULT_TYPICAL_DAY)))
            .andExpect(jsonPath("$.[*].mode").value(hasItem(DEFAULT_MODE)))
            .andExpect(jsonPath("$.[*].timeInterval").value(hasItem(DEFAULT_TIME_INTERVAL.doubleValue())))
            .andExpect(jsonPath("$.[*].uploadDateTime").value(hasItem(DEFAULT_UPLOAD_DATE_TIME.toString())));
    }

    @Test
    @Transactional
    void getTransfProfile() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get the transfProfile
        restTransfProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, transfProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transfProfile.getId().intValue()))
            .andExpect(jsonPath("$.season").value(DEFAULT_SEASON))
            .andExpect(jsonPath("$.typicalDay").value(DEFAULT_TYPICAL_DAY))
            .andExpect(jsonPath("$.mode").value(DEFAULT_MODE))
            .andExpect(jsonPath("$.timeInterval").value(DEFAULT_TIME_INTERVAL.doubleValue()))
            .andExpect(jsonPath("$.uploadDateTime").value(DEFAULT_UPLOAD_DATE_TIME.toString()));
    }

    @Test
    @Transactional
    void getTransfProfilesByIdFiltering() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        Long id = transfProfile.getId();

        defaultTransfProfileShouldBeFound("id.equals=" + id);
        defaultTransfProfileShouldNotBeFound("id.notEquals=" + id);

        defaultTransfProfileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTransfProfileShouldNotBeFound("id.greaterThan=" + id);

        defaultTransfProfileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTransfProfileShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransfProfilesBySeasonIsEqualToSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where season equals to DEFAULT_SEASON
        defaultTransfProfileShouldBeFound("season.equals=" + DEFAULT_SEASON);

        // Get all the transfProfileList where season equals to UPDATED_SEASON
        defaultTransfProfileShouldNotBeFound("season.equals=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllTransfProfilesBySeasonIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where season not equals to DEFAULT_SEASON
        defaultTransfProfileShouldNotBeFound("season.notEquals=" + DEFAULT_SEASON);

        // Get all the transfProfileList where season not equals to UPDATED_SEASON
        defaultTransfProfileShouldBeFound("season.notEquals=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllTransfProfilesBySeasonIsInShouldWork() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where season in DEFAULT_SEASON or UPDATED_SEASON
        defaultTransfProfileShouldBeFound("season.in=" + DEFAULT_SEASON + "," + UPDATED_SEASON);

        // Get all the transfProfileList where season equals to UPDATED_SEASON
        defaultTransfProfileShouldNotBeFound("season.in=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllTransfProfilesBySeasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where season is not null
        defaultTransfProfileShouldBeFound("season.specified=true");

        // Get all the transfProfileList where season is null
        defaultTransfProfileShouldNotBeFound("season.specified=false");
    }

    @Test
    @Transactional
    void getAllTransfProfilesBySeasonContainsSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where season contains DEFAULT_SEASON
        defaultTransfProfileShouldBeFound("season.contains=" + DEFAULT_SEASON);

        // Get all the transfProfileList where season contains UPDATED_SEASON
        defaultTransfProfileShouldNotBeFound("season.contains=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllTransfProfilesBySeasonNotContainsSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where season does not contain DEFAULT_SEASON
        defaultTransfProfileShouldNotBeFound("season.doesNotContain=" + DEFAULT_SEASON);

        // Get all the transfProfileList where season does not contain UPDATED_SEASON
        defaultTransfProfileShouldBeFound("season.doesNotContain=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByTypicalDayIsEqualToSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where typicalDay equals to DEFAULT_TYPICAL_DAY
        defaultTransfProfileShouldBeFound("typicalDay.equals=" + DEFAULT_TYPICAL_DAY);

        // Get all the transfProfileList where typicalDay equals to UPDATED_TYPICAL_DAY
        defaultTransfProfileShouldNotBeFound("typicalDay.equals=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByTypicalDayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where typicalDay not equals to DEFAULT_TYPICAL_DAY
        defaultTransfProfileShouldNotBeFound("typicalDay.notEquals=" + DEFAULT_TYPICAL_DAY);

        // Get all the transfProfileList where typicalDay not equals to UPDATED_TYPICAL_DAY
        defaultTransfProfileShouldBeFound("typicalDay.notEquals=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByTypicalDayIsInShouldWork() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where typicalDay in DEFAULT_TYPICAL_DAY or UPDATED_TYPICAL_DAY
        defaultTransfProfileShouldBeFound("typicalDay.in=" + DEFAULT_TYPICAL_DAY + "," + UPDATED_TYPICAL_DAY);

        // Get all the transfProfileList where typicalDay equals to UPDATED_TYPICAL_DAY
        defaultTransfProfileShouldNotBeFound("typicalDay.in=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByTypicalDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where typicalDay is not null
        defaultTransfProfileShouldBeFound("typicalDay.specified=true");

        // Get all the transfProfileList where typicalDay is null
        defaultTransfProfileShouldNotBeFound("typicalDay.specified=false");
    }

    @Test
    @Transactional
    void getAllTransfProfilesByTypicalDayContainsSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where typicalDay contains DEFAULT_TYPICAL_DAY
        defaultTransfProfileShouldBeFound("typicalDay.contains=" + DEFAULT_TYPICAL_DAY);

        // Get all the transfProfileList where typicalDay contains UPDATED_TYPICAL_DAY
        defaultTransfProfileShouldNotBeFound("typicalDay.contains=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByTypicalDayNotContainsSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where typicalDay does not contain DEFAULT_TYPICAL_DAY
        defaultTransfProfileShouldNotBeFound("typicalDay.doesNotContain=" + DEFAULT_TYPICAL_DAY);

        // Get all the transfProfileList where typicalDay does not contain UPDATED_TYPICAL_DAY
        defaultTransfProfileShouldBeFound("typicalDay.doesNotContain=" + UPDATED_TYPICAL_DAY);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByModeIsEqualToSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where mode equals to DEFAULT_MODE
        defaultTransfProfileShouldBeFound("mode.equals=" + DEFAULT_MODE);

        // Get all the transfProfileList where mode equals to UPDATED_MODE
        defaultTransfProfileShouldNotBeFound("mode.equals=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByModeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where mode not equals to DEFAULT_MODE
        defaultTransfProfileShouldNotBeFound("mode.notEquals=" + DEFAULT_MODE);

        // Get all the transfProfileList where mode not equals to UPDATED_MODE
        defaultTransfProfileShouldBeFound("mode.notEquals=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByModeIsInShouldWork() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where mode in DEFAULT_MODE or UPDATED_MODE
        defaultTransfProfileShouldBeFound("mode.in=" + DEFAULT_MODE + "," + UPDATED_MODE);

        // Get all the transfProfileList where mode equals to UPDATED_MODE
        defaultTransfProfileShouldNotBeFound("mode.in=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByModeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where mode is not null
        defaultTransfProfileShouldBeFound("mode.specified=true");

        // Get all the transfProfileList where mode is null
        defaultTransfProfileShouldNotBeFound("mode.specified=false");
    }

    @Test
    @Transactional
    void getAllTransfProfilesByModeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where mode is greater than or equal to DEFAULT_MODE
        defaultTransfProfileShouldBeFound("mode.greaterThanOrEqual=" + DEFAULT_MODE);

        // Get all the transfProfileList where mode is greater than or equal to UPDATED_MODE
        defaultTransfProfileShouldNotBeFound("mode.greaterThanOrEqual=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByModeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where mode is less than or equal to DEFAULT_MODE
        defaultTransfProfileShouldBeFound("mode.lessThanOrEqual=" + DEFAULT_MODE);

        // Get all the transfProfileList where mode is less than or equal to SMALLER_MODE
        defaultTransfProfileShouldNotBeFound("mode.lessThanOrEqual=" + SMALLER_MODE);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByModeIsLessThanSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where mode is less than DEFAULT_MODE
        defaultTransfProfileShouldNotBeFound("mode.lessThan=" + DEFAULT_MODE);

        // Get all the transfProfileList where mode is less than UPDATED_MODE
        defaultTransfProfileShouldBeFound("mode.lessThan=" + UPDATED_MODE);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByModeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where mode is greater than DEFAULT_MODE
        defaultTransfProfileShouldNotBeFound("mode.greaterThan=" + DEFAULT_MODE);

        // Get all the transfProfileList where mode is greater than SMALLER_MODE
        defaultTransfProfileShouldBeFound("mode.greaterThan=" + SMALLER_MODE);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByTimeIntervalIsEqualToSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where timeInterval equals to DEFAULT_TIME_INTERVAL
        defaultTransfProfileShouldBeFound("timeInterval.equals=" + DEFAULT_TIME_INTERVAL);

        // Get all the transfProfileList where timeInterval equals to UPDATED_TIME_INTERVAL
        defaultTransfProfileShouldNotBeFound("timeInterval.equals=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByTimeIntervalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where timeInterval not equals to DEFAULT_TIME_INTERVAL
        defaultTransfProfileShouldNotBeFound("timeInterval.notEquals=" + DEFAULT_TIME_INTERVAL);

        // Get all the transfProfileList where timeInterval not equals to UPDATED_TIME_INTERVAL
        defaultTransfProfileShouldBeFound("timeInterval.notEquals=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByTimeIntervalIsInShouldWork() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where timeInterval in DEFAULT_TIME_INTERVAL or UPDATED_TIME_INTERVAL
        defaultTransfProfileShouldBeFound("timeInterval.in=" + DEFAULT_TIME_INTERVAL + "," + UPDATED_TIME_INTERVAL);

        // Get all the transfProfileList where timeInterval equals to UPDATED_TIME_INTERVAL
        defaultTransfProfileShouldNotBeFound("timeInterval.in=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByTimeIntervalIsNullOrNotNull() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where timeInterval is not null
        defaultTransfProfileShouldBeFound("timeInterval.specified=true");

        // Get all the transfProfileList where timeInterval is null
        defaultTransfProfileShouldNotBeFound("timeInterval.specified=false");
    }

    @Test
    @Transactional
    void getAllTransfProfilesByTimeIntervalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where timeInterval is greater than or equal to DEFAULT_TIME_INTERVAL
        defaultTransfProfileShouldBeFound("timeInterval.greaterThanOrEqual=" + DEFAULT_TIME_INTERVAL);

        // Get all the transfProfileList where timeInterval is greater than or equal to UPDATED_TIME_INTERVAL
        defaultTransfProfileShouldNotBeFound("timeInterval.greaterThanOrEqual=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByTimeIntervalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where timeInterval is less than or equal to DEFAULT_TIME_INTERVAL
        defaultTransfProfileShouldBeFound("timeInterval.lessThanOrEqual=" + DEFAULT_TIME_INTERVAL);

        // Get all the transfProfileList where timeInterval is less than or equal to SMALLER_TIME_INTERVAL
        defaultTransfProfileShouldNotBeFound("timeInterval.lessThanOrEqual=" + SMALLER_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByTimeIntervalIsLessThanSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where timeInterval is less than DEFAULT_TIME_INTERVAL
        defaultTransfProfileShouldNotBeFound("timeInterval.lessThan=" + DEFAULT_TIME_INTERVAL);

        // Get all the transfProfileList where timeInterval is less than UPDATED_TIME_INTERVAL
        defaultTransfProfileShouldBeFound("timeInterval.lessThan=" + UPDATED_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByTimeIntervalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where timeInterval is greater than DEFAULT_TIME_INTERVAL
        defaultTransfProfileShouldNotBeFound("timeInterval.greaterThan=" + DEFAULT_TIME_INTERVAL);

        // Get all the transfProfileList where timeInterval is greater than SMALLER_TIME_INTERVAL
        defaultTransfProfileShouldBeFound("timeInterval.greaterThan=" + SMALLER_TIME_INTERVAL);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByUploadDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where uploadDateTime equals to DEFAULT_UPLOAD_DATE_TIME
        defaultTransfProfileShouldBeFound("uploadDateTime.equals=" + DEFAULT_UPLOAD_DATE_TIME);

        // Get all the transfProfileList where uploadDateTime equals to UPDATED_UPLOAD_DATE_TIME
        defaultTransfProfileShouldNotBeFound("uploadDateTime.equals=" + UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByUploadDateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where uploadDateTime not equals to DEFAULT_UPLOAD_DATE_TIME
        defaultTransfProfileShouldNotBeFound("uploadDateTime.notEquals=" + DEFAULT_UPLOAD_DATE_TIME);

        // Get all the transfProfileList where uploadDateTime not equals to UPDATED_UPLOAD_DATE_TIME
        defaultTransfProfileShouldBeFound("uploadDateTime.notEquals=" + UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByUploadDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where uploadDateTime in DEFAULT_UPLOAD_DATE_TIME or UPDATED_UPLOAD_DATE_TIME
        defaultTransfProfileShouldBeFound("uploadDateTime.in=" + DEFAULT_UPLOAD_DATE_TIME + "," + UPDATED_UPLOAD_DATE_TIME);

        // Get all the transfProfileList where uploadDateTime equals to UPDATED_UPLOAD_DATE_TIME
        defaultTransfProfileShouldNotBeFound("uploadDateTime.in=" + UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllTransfProfilesByUploadDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        // Get all the transfProfileList where uploadDateTime is not null
        defaultTransfProfileShouldBeFound("uploadDateTime.specified=true");

        // Get all the transfProfileList where uploadDateTime is null
        defaultTransfProfileShouldNotBeFound("uploadDateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllTransfProfilesByInputFileIsEqualToSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);
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
        transfProfile.setInputFile(inputFile);
        transfProfileRepository.saveAndFlush(transfProfile);
        Long inputFileId = inputFile.getId();

        // Get all the transfProfileList where inputFile equals to inputFileId
        defaultTransfProfileShouldBeFound("inputFileId.equals=" + inputFileId);

        // Get all the transfProfileList where inputFile equals to (inputFileId + 1)
        defaultTransfProfileShouldNotBeFound("inputFileId.equals=" + (inputFileId + 1));
    }

    @Test
    @Transactional
    void getAllTransfProfilesByTransfElValIsEqualToSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);
        TransfElVal transfElVal;
        if (TestUtil.findAll(em, TransfElVal.class).isEmpty()) {
            transfElVal = TransfElValResourceIT.createEntity(em);
            em.persist(transfElVal);
            em.flush();
        } else {
            transfElVal = TestUtil.findAll(em, TransfElVal.class).get(0);
        }
        em.persist(transfElVal);
        em.flush();
        transfProfile.addTransfElVal(transfElVal);
        transfProfileRepository.saveAndFlush(transfProfile);
        Long transfElValId = transfElVal.getId();

        // Get all the transfProfileList where transfElVal equals to transfElValId
        defaultTransfProfileShouldBeFound("transfElValId.equals=" + transfElValId);

        // Get all the transfProfileList where transfElVal equals to (transfElValId + 1)
        defaultTransfProfileShouldNotBeFound("transfElValId.equals=" + (transfElValId + 1));
    }

    @Test
    @Transactional
    void getAllTransfProfilesByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);
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
        transfProfile.setNetwork(network);
        transfProfileRepository.saveAndFlush(transfProfile);
        Long networkId = network.getId();

        // Get all the transfProfileList where network equals to networkId
        defaultTransfProfileShouldBeFound("networkId.equals=" + networkId);

        // Get all the transfProfileList where network equals to (networkId + 1)
        defaultTransfProfileShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransfProfileShouldBeFound(String filter) throws Exception {
        restTransfProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transfProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].season").value(hasItem(DEFAULT_SEASON)))
            .andExpect(jsonPath("$.[*].typicalDay").value(hasItem(DEFAULT_TYPICAL_DAY)))
            .andExpect(jsonPath("$.[*].mode").value(hasItem(DEFAULT_MODE)))
            .andExpect(jsonPath("$.[*].timeInterval").value(hasItem(DEFAULT_TIME_INTERVAL.doubleValue())))
            .andExpect(jsonPath("$.[*].uploadDateTime").value(hasItem(DEFAULT_UPLOAD_DATE_TIME.toString())));

        // Check, that the count call also returns 1
        restTransfProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransfProfileShouldNotBeFound(String filter) throws Exception {
        restTransfProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransfProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransfProfile() throws Exception {
        // Get the transfProfile
        restTransfProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTransfProfile() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        int databaseSizeBeforeUpdate = transfProfileRepository.findAll().size();

        // Update the transfProfile
        TransfProfile updatedTransfProfile = transfProfileRepository.findById(transfProfile.getId()).get();
        // Disconnect from session so that the updates on updatedTransfProfile are not directly saved in db
        em.detach(updatedTransfProfile);
        updatedTransfProfile
            .season(UPDATED_SEASON)
            .typicalDay(UPDATED_TYPICAL_DAY)
            .mode(UPDATED_MODE)
            .timeInterval(UPDATED_TIME_INTERVAL)
            .uploadDateTime(UPDATED_UPLOAD_DATE_TIME);
        TransfProfileDTO transfProfileDTO = transfProfileMapper.toDto(updatedTransfProfile);

        restTransfProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transfProfileDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transfProfileDTO))
            )
            .andExpect(status().isOk());

        // Validate the TransfProfile in the database
        List<TransfProfile> transfProfileList = transfProfileRepository.findAll();
        assertThat(transfProfileList).hasSize(databaseSizeBeforeUpdate);
        TransfProfile testTransfProfile = transfProfileList.get(transfProfileList.size() - 1);
        assertThat(testTransfProfile.getSeason()).isEqualTo(UPDATED_SEASON);
        assertThat(testTransfProfile.getTypicalDay()).isEqualTo(UPDATED_TYPICAL_DAY);
        assertThat(testTransfProfile.getMode()).isEqualTo(UPDATED_MODE);
        assertThat(testTransfProfile.getTimeInterval()).isEqualTo(UPDATED_TIME_INTERVAL);
        assertThat(testTransfProfile.getUploadDateTime()).isEqualTo(UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void putNonExistingTransfProfile() throws Exception {
        int databaseSizeBeforeUpdate = transfProfileRepository.findAll().size();
        transfProfile.setId(count.incrementAndGet());

        // Create the TransfProfile
        TransfProfileDTO transfProfileDTO = transfProfileMapper.toDto(transfProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransfProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transfProfileDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transfProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransfProfile in the database
        List<TransfProfile> transfProfileList = transfProfileRepository.findAll();
        assertThat(transfProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransfProfile() throws Exception {
        int databaseSizeBeforeUpdate = transfProfileRepository.findAll().size();
        transfProfile.setId(count.incrementAndGet());

        // Create the TransfProfile
        TransfProfileDTO transfProfileDTO = transfProfileMapper.toDto(transfProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransfProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transfProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransfProfile in the database
        List<TransfProfile> transfProfileList = transfProfileRepository.findAll();
        assertThat(transfProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransfProfile() throws Exception {
        int databaseSizeBeforeUpdate = transfProfileRepository.findAll().size();
        transfProfile.setId(count.incrementAndGet());

        // Create the TransfProfile
        TransfProfileDTO transfProfileDTO = transfProfileMapper.toDto(transfProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransfProfileMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transfProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransfProfile in the database
        List<TransfProfile> transfProfileList = transfProfileRepository.findAll();
        assertThat(transfProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransfProfileWithPatch() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        int databaseSizeBeforeUpdate = transfProfileRepository.findAll().size();

        // Update the transfProfile using partial update
        TransfProfile partialUpdatedTransfProfile = new TransfProfile();
        partialUpdatedTransfProfile.setId(transfProfile.getId());

        partialUpdatedTransfProfile.season(UPDATED_SEASON).mode(UPDATED_MODE).timeInterval(UPDATED_TIME_INTERVAL);

        restTransfProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransfProfile.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransfProfile))
            )
            .andExpect(status().isOk());

        // Validate the TransfProfile in the database
        List<TransfProfile> transfProfileList = transfProfileRepository.findAll();
        assertThat(transfProfileList).hasSize(databaseSizeBeforeUpdate);
        TransfProfile testTransfProfile = transfProfileList.get(transfProfileList.size() - 1);
        assertThat(testTransfProfile.getSeason()).isEqualTo(UPDATED_SEASON);
        assertThat(testTransfProfile.getTypicalDay()).isEqualTo(DEFAULT_TYPICAL_DAY);
        assertThat(testTransfProfile.getMode()).isEqualTo(UPDATED_MODE);
        assertThat(testTransfProfile.getTimeInterval()).isEqualTo(UPDATED_TIME_INTERVAL);
        assertThat(testTransfProfile.getUploadDateTime()).isEqualTo(DEFAULT_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void fullUpdateTransfProfileWithPatch() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        int databaseSizeBeforeUpdate = transfProfileRepository.findAll().size();

        // Update the transfProfile using partial update
        TransfProfile partialUpdatedTransfProfile = new TransfProfile();
        partialUpdatedTransfProfile.setId(transfProfile.getId());

        partialUpdatedTransfProfile
            .season(UPDATED_SEASON)
            .typicalDay(UPDATED_TYPICAL_DAY)
            .mode(UPDATED_MODE)
            .timeInterval(UPDATED_TIME_INTERVAL)
            .uploadDateTime(UPDATED_UPLOAD_DATE_TIME);

        restTransfProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransfProfile.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransfProfile))
            )
            .andExpect(status().isOk());

        // Validate the TransfProfile in the database
        List<TransfProfile> transfProfileList = transfProfileRepository.findAll();
        assertThat(transfProfileList).hasSize(databaseSizeBeforeUpdate);
        TransfProfile testTransfProfile = transfProfileList.get(transfProfileList.size() - 1);
        assertThat(testTransfProfile.getSeason()).isEqualTo(UPDATED_SEASON);
        assertThat(testTransfProfile.getTypicalDay()).isEqualTo(UPDATED_TYPICAL_DAY);
        assertThat(testTransfProfile.getMode()).isEqualTo(UPDATED_MODE);
        assertThat(testTransfProfile.getTimeInterval()).isEqualTo(UPDATED_TIME_INTERVAL);
        assertThat(testTransfProfile.getUploadDateTime()).isEqualTo(UPDATED_UPLOAD_DATE_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingTransfProfile() throws Exception {
        int databaseSizeBeforeUpdate = transfProfileRepository.findAll().size();
        transfProfile.setId(count.incrementAndGet());

        // Create the TransfProfile
        TransfProfileDTO transfProfileDTO = transfProfileMapper.toDto(transfProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransfProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transfProfileDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transfProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransfProfile in the database
        List<TransfProfile> transfProfileList = transfProfileRepository.findAll();
        assertThat(transfProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransfProfile() throws Exception {
        int databaseSizeBeforeUpdate = transfProfileRepository.findAll().size();
        transfProfile.setId(count.incrementAndGet());

        // Create the TransfProfile
        TransfProfileDTO transfProfileDTO = transfProfileMapper.toDto(transfProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransfProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transfProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransfProfile in the database
        List<TransfProfile> transfProfileList = transfProfileRepository.findAll();
        assertThat(transfProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransfProfile() throws Exception {
        int databaseSizeBeforeUpdate = transfProfileRepository.findAll().size();
        transfProfile.setId(count.incrementAndGet());

        // Create the TransfProfile
        TransfProfileDTO transfProfileDTO = transfProfileMapper.toDto(transfProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransfProfileMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transfProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransfProfile in the database
        List<TransfProfile> transfProfileList = transfProfileRepository.findAll();
        assertThat(transfProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransfProfile() throws Exception {
        // Initialize the database
        transfProfileRepository.saveAndFlush(transfProfile);

        int databaseSizeBeforeDelete = transfProfileRepository.findAll().size();

        // Delete the transfProfile
        restTransfProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, transfProfile.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TransfProfile> transfProfileList = transfProfileRepository.findAll();
        assertThat(transfProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
