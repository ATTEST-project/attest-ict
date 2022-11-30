package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Branch;
import com.attest.ict.domain.BranchExtension;
import com.attest.ict.repository.BranchExtensionRepository;
import com.attest.ict.service.criteria.BranchExtensionCriteria;
import com.attest.ict.service.dto.BranchExtensionDTO;
import com.attest.ict.service.mapper.BranchExtensionMapper;
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
 * Integration tests for the {@link BranchExtensionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BranchExtensionResourceIT {

    private static final Double DEFAULT_STEP_SIZE = 1D;
    private static final Double UPDATED_STEP_SIZE = 2D;
    private static final Double SMALLER_STEP_SIZE = 1D - 1D;

    private static final Double DEFAULT_ACT_TAP = 1D;
    private static final Double UPDATED_ACT_TAP = 2D;
    private static final Double SMALLER_ACT_TAP = 1D - 1D;

    private static final Double DEFAULT_MIN_TAP = 1D;
    private static final Double UPDATED_MIN_TAP = 2D;
    private static final Double SMALLER_MIN_TAP = 1D - 1D;

    private static final Double DEFAULT_MAX_TAP = 1D;
    private static final Double UPDATED_MAX_TAP = 2D;
    private static final Double SMALLER_MAX_TAP = 1D - 1D;

    private static final Double DEFAULT_NORMAL_TAP = 1D;
    private static final Double UPDATED_NORMAL_TAP = 2D;
    private static final Double SMALLER_NORMAL_TAP = 1D - 1D;

    private static final Double DEFAULT_NOMINAL_RATIO = 1D;
    private static final Double UPDATED_NOMINAL_RATIO = 2D;
    private static final Double SMALLER_NOMINAL_RATIO = 1D - 1D;

    private static final Double DEFAULT_R_IP = 1D;
    private static final Double UPDATED_R_IP = 2D;
    private static final Double SMALLER_R_IP = 1D - 1D;

    private static final Double DEFAULT_R_N = 1D;
    private static final Double UPDATED_R_N = 2D;
    private static final Double SMALLER_R_N = 1D - 1D;

    private static final Double DEFAULT_R_0 = 1D;
    private static final Double UPDATED_R_0 = 2D;
    private static final Double SMALLER_R_0 = 1D - 1D;

    private static final Double DEFAULT_X_0 = 1D;
    private static final Double UPDATED_X_0 = 2D;
    private static final Double SMALLER_X_0 = 1D - 1D;

    private static final Double DEFAULT_B_0 = 1D;
    private static final Double UPDATED_B_0 = 2D;
    private static final Double SMALLER_B_0 = 1D - 1D;

    private static final Double DEFAULT_LENGTH = 1D;
    private static final Double UPDATED_LENGTH = 2D;
    private static final Double SMALLER_LENGTH = 1D - 1D;

    private static final Integer DEFAULT_NORM_STAT = 1;
    private static final Integer UPDATED_NORM_STAT = 2;
    private static final Integer SMALLER_NORM_STAT = 1 - 1;

    private static final Double DEFAULT_G = 1D;
    private static final Double UPDATED_G = 2D;
    private static final Double SMALLER_G = 1D - 1D;

    private static final String DEFAULT_M_RID = "AAAAAAAAAA";
    private static final String UPDATED_M_RID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/branch-extensions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BranchExtensionRepository branchExtensionRepository;

    @Autowired
    private BranchExtensionMapper branchExtensionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBranchExtensionMockMvc;

    private BranchExtension branchExtension;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BranchExtension createEntity(EntityManager em) {
        BranchExtension branchExtension = new BranchExtension()
            .stepSize(DEFAULT_STEP_SIZE)
            .actTap(DEFAULT_ACT_TAP)
            .minTap(DEFAULT_MIN_TAP)
            .maxTap(DEFAULT_MAX_TAP)
            .normalTap(DEFAULT_NORMAL_TAP)
            .nominalRatio(DEFAULT_NOMINAL_RATIO)
            .rIp(DEFAULT_R_IP)
            .rN(DEFAULT_R_N)
            .r0(DEFAULT_R_0)
            .x0(DEFAULT_X_0)
            .b0(DEFAULT_B_0)
            .length(DEFAULT_LENGTH)
            .normStat(DEFAULT_NORM_STAT)
            .g(DEFAULT_G)
            .mRid(DEFAULT_M_RID);
        return branchExtension;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BranchExtension createUpdatedEntity(EntityManager em) {
        BranchExtension branchExtension = new BranchExtension()
            .stepSize(UPDATED_STEP_SIZE)
            .actTap(UPDATED_ACT_TAP)
            .minTap(UPDATED_MIN_TAP)
            .maxTap(UPDATED_MAX_TAP)
            .normalTap(UPDATED_NORMAL_TAP)
            .nominalRatio(UPDATED_NOMINAL_RATIO)
            .rIp(UPDATED_R_IP)
            .rN(UPDATED_R_N)
            .r0(UPDATED_R_0)
            .x0(UPDATED_X_0)
            .b0(UPDATED_B_0)
            .length(UPDATED_LENGTH)
            .normStat(UPDATED_NORM_STAT)
            .g(UPDATED_G)
            .mRid(UPDATED_M_RID);
        return branchExtension;
    }

    @BeforeEach
    public void initTest() {
        branchExtension = createEntity(em);
    }

    @Test
    @Transactional
    void createBranchExtension() throws Exception {
        int databaseSizeBeforeCreate = branchExtensionRepository.findAll().size();
        // Create the BranchExtension
        BranchExtensionDTO branchExtensionDTO = branchExtensionMapper.toDto(branchExtension);
        restBranchExtensionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchExtensionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BranchExtension in the database
        List<BranchExtension> branchExtensionList = branchExtensionRepository.findAll();
        assertThat(branchExtensionList).hasSize(databaseSizeBeforeCreate + 1);
        BranchExtension testBranchExtension = branchExtensionList.get(branchExtensionList.size() - 1);
        assertThat(testBranchExtension.getStepSize()).isEqualTo(DEFAULT_STEP_SIZE);
        assertThat(testBranchExtension.getActTap()).isEqualTo(DEFAULT_ACT_TAP);
        assertThat(testBranchExtension.getMinTap()).isEqualTo(DEFAULT_MIN_TAP);
        assertThat(testBranchExtension.getMaxTap()).isEqualTo(DEFAULT_MAX_TAP);
        assertThat(testBranchExtension.getNormalTap()).isEqualTo(DEFAULT_NORMAL_TAP);
        assertThat(testBranchExtension.getNominalRatio()).isEqualTo(DEFAULT_NOMINAL_RATIO);
        assertThat(testBranchExtension.getrIp()).isEqualTo(DEFAULT_R_IP);
        assertThat(testBranchExtension.getrN()).isEqualTo(DEFAULT_R_N);
        assertThat(testBranchExtension.getr0()).isEqualTo(DEFAULT_R_0);
        assertThat(testBranchExtension.getx0()).isEqualTo(DEFAULT_X_0);
        assertThat(testBranchExtension.getb0()).isEqualTo(DEFAULT_B_0);
        assertThat(testBranchExtension.getLength()).isEqualTo(DEFAULT_LENGTH);
        assertThat(testBranchExtension.getNormStat()).isEqualTo(DEFAULT_NORM_STAT);
        assertThat(testBranchExtension.getG()).isEqualTo(DEFAULT_G);
        assertThat(testBranchExtension.getmRid()).isEqualTo(DEFAULT_M_RID);
    }

    @Test
    @Transactional
    void createBranchExtensionWithExistingId() throws Exception {
        // Create the BranchExtension with an existing ID
        branchExtension.setId(1L);
        BranchExtensionDTO branchExtensionDTO = branchExtensionMapper.toDto(branchExtension);

        int databaseSizeBeforeCreate = branchExtensionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBranchExtensionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchExtensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchExtension in the database
        List<BranchExtension> branchExtensionList = branchExtensionRepository.findAll();
        assertThat(branchExtensionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBranchExtensions() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList
        restBranchExtensionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branchExtension.getId().intValue())))
            .andExpect(jsonPath("$.[*].stepSize").value(hasItem(DEFAULT_STEP_SIZE.doubleValue())))
            .andExpect(jsonPath("$.[*].actTap").value(hasItem(DEFAULT_ACT_TAP.doubleValue())))
            .andExpect(jsonPath("$.[*].minTap").value(hasItem(DEFAULT_MIN_TAP.doubleValue())))
            .andExpect(jsonPath("$.[*].maxTap").value(hasItem(DEFAULT_MAX_TAP.doubleValue())))
            .andExpect(jsonPath("$.[*].normalTap").value(hasItem(DEFAULT_NORMAL_TAP.doubleValue())))
            .andExpect(jsonPath("$.[*].nominalRatio").value(hasItem(DEFAULT_NOMINAL_RATIO.doubleValue())))
            .andExpect(jsonPath("$.[*].rIp").value(hasItem(DEFAULT_R_IP.doubleValue())))
            .andExpect(jsonPath("$.[*].rN").value(hasItem(DEFAULT_R_N.doubleValue())))
            .andExpect(jsonPath("$.[*].r0").value(hasItem(DEFAULT_R_0.doubleValue())))
            .andExpect(jsonPath("$.[*].x0").value(hasItem(DEFAULT_X_0.doubleValue())))
            .andExpect(jsonPath("$.[*].b0").value(hasItem(DEFAULT_B_0.doubleValue())))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH.doubleValue())))
            .andExpect(jsonPath("$.[*].normStat").value(hasItem(DEFAULT_NORM_STAT)))
            .andExpect(jsonPath("$.[*].g").value(hasItem(DEFAULT_G.doubleValue())))
            .andExpect(jsonPath("$.[*].mRid").value(hasItem(DEFAULT_M_RID)));
    }

    @Test
    @Transactional
    void getBranchExtension() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get the branchExtension
        restBranchExtensionMockMvc
            .perform(get(ENTITY_API_URL_ID, branchExtension.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(branchExtension.getId().intValue()))
            .andExpect(jsonPath("$.stepSize").value(DEFAULT_STEP_SIZE.doubleValue()))
            .andExpect(jsonPath("$.actTap").value(DEFAULT_ACT_TAP.doubleValue()))
            .andExpect(jsonPath("$.minTap").value(DEFAULT_MIN_TAP.doubleValue()))
            .andExpect(jsonPath("$.maxTap").value(DEFAULT_MAX_TAP.doubleValue()))
            .andExpect(jsonPath("$.normalTap").value(DEFAULT_NORMAL_TAP.doubleValue()))
            .andExpect(jsonPath("$.nominalRatio").value(DEFAULT_NOMINAL_RATIO.doubleValue()))
            .andExpect(jsonPath("$.rIp").value(DEFAULT_R_IP.doubleValue()))
            .andExpect(jsonPath("$.rN").value(DEFAULT_R_N.doubleValue()))
            .andExpect(jsonPath("$.r0").value(DEFAULT_R_0.doubleValue()))
            .andExpect(jsonPath("$.x0").value(DEFAULT_X_0.doubleValue()))
            .andExpect(jsonPath("$.b0").value(DEFAULT_B_0.doubleValue()))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH.doubleValue()))
            .andExpect(jsonPath("$.normStat").value(DEFAULT_NORM_STAT))
            .andExpect(jsonPath("$.g").value(DEFAULT_G.doubleValue()))
            .andExpect(jsonPath("$.mRid").value(DEFAULT_M_RID));
    }

    @Test
    @Transactional
    void getBranchExtensionsByIdFiltering() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        Long id = branchExtension.getId();

        defaultBranchExtensionShouldBeFound("id.equals=" + id);
        defaultBranchExtensionShouldNotBeFound("id.notEquals=" + id);

        defaultBranchExtensionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBranchExtensionShouldNotBeFound("id.greaterThan=" + id);

        defaultBranchExtensionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBranchExtensionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByStepSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where stepSize equals to DEFAULT_STEP_SIZE
        defaultBranchExtensionShouldBeFound("stepSize.equals=" + DEFAULT_STEP_SIZE);

        // Get all the branchExtensionList where stepSize equals to UPDATED_STEP_SIZE
        defaultBranchExtensionShouldNotBeFound("stepSize.equals=" + UPDATED_STEP_SIZE);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByStepSizeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where stepSize not equals to DEFAULT_STEP_SIZE
        defaultBranchExtensionShouldNotBeFound("stepSize.notEquals=" + DEFAULT_STEP_SIZE);

        // Get all the branchExtensionList where stepSize not equals to UPDATED_STEP_SIZE
        defaultBranchExtensionShouldBeFound("stepSize.notEquals=" + UPDATED_STEP_SIZE);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByStepSizeIsInShouldWork() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where stepSize in DEFAULT_STEP_SIZE or UPDATED_STEP_SIZE
        defaultBranchExtensionShouldBeFound("stepSize.in=" + DEFAULT_STEP_SIZE + "," + UPDATED_STEP_SIZE);

        // Get all the branchExtensionList where stepSize equals to UPDATED_STEP_SIZE
        defaultBranchExtensionShouldNotBeFound("stepSize.in=" + UPDATED_STEP_SIZE);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByStepSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where stepSize is not null
        defaultBranchExtensionShouldBeFound("stepSize.specified=true");

        // Get all the branchExtensionList where stepSize is null
        defaultBranchExtensionShouldNotBeFound("stepSize.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByStepSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where stepSize is greater than or equal to DEFAULT_STEP_SIZE
        defaultBranchExtensionShouldBeFound("stepSize.greaterThanOrEqual=" + DEFAULT_STEP_SIZE);

        // Get all the branchExtensionList where stepSize is greater than or equal to UPDATED_STEP_SIZE
        defaultBranchExtensionShouldNotBeFound("stepSize.greaterThanOrEqual=" + UPDATED_STEP_SIZE);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByStepSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where stepSize is less than or equal to DEFAULT_STEP_SIZE
        defaultBranchExtensionShouldBeFound("stepSize.lessThanOrEqual=" + DEFAULT_STEP_SIZE);

        // Get all the branchExtensionList where stepSize is less than or equal to SMALLER_STEP_SIZE
        defaultBranchExtensionShouldNotBeFound("stepSize.lessThanOrEqual=" + SMALLER_STEP_SIZE);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByStepSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where stepSize is less than DEFAULT_STEP_SIZE
        defaultBranchExtensionShouldNotBeFound("stepSize.lessThan=" + DEFAULT_STEP_SIZE);

        // Get all the branchExtensionList where stepSize is less than UPDATED_STEP_SIZE
        defaultBranchExtensionShouldBeFound("stepSize.lessThan=" + UPDATED_STEP_SIZE);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByStepSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where stepSize is greater than DEFAULT_STEP_SIZE
        defaultBranchExtensionShouldNotBeFound("stepSize.greaterThan=" + DEFAULT_STEP_SIZE);

        // Get all the branchExtensionList where stepSize is greater than SMALLER_STEP_SIZE
        defaultBranchExtensionShouldBeFound("stepSize.greaterThan=" + SMALLER_STEP_SIZE);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByActTapIsEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where actTap equals to DEFAULT_ACT_TAP
        defaultBranchExtensionShouldBeFound("actTap.equals=" + DEFAULT_ACT_TAP);

        // Get all the branchExtensionList where actTap equals to UPDATED_ACT_TAP
        defaultBranchExtensionShouldNotBeFound("actTap.equals=" + UPDATED_ACT_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByActTapIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where actTap not equals to DEFAULT_ACT_TAP
        defaultBranchExtensionShouldNotBeFound("actTap.notEquals=" + DEFAULT_ACT_TAP);

        // Get all the branchExtensionList where actTap not equals to UPDATED_ACT_TAP
        defaultBranchExtensionShouldBeFound("actTap.notEquals=" + UPDATED_ACT_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByActTapIsInShouldWork() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where actTap in DEFAULT_ACT_TAP or UPDATED_ACT_TAP
        defaultBranchExtensionShouldBeFound("actTap.in=" + DEFAULT_ACT_TAP + "," + UPDATED_ACT_TAP);

        // Get all the branchExtensionList where actTap equals to UPDATED_ACT_TAP
        defaultBranchExtensionShouldNotBeFound("actTap.in=" + UPDATED_ACT_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByActTapIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where actTap is not null
        defaultBranchExtensionShouldBeFound("actTap.specified=true");

        // Get all the branchExtensionList where actTap is null
        defaultBranchExtensionShouldNotBeFound("actTap.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByActTapIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where actTap is greater than or equal to DEFAULT_ACT_TAP
        defaultBranchExtensionShouldBeFound("actTap.greaterThanOrEqual=" + DEFAULT_ACT_TAP);

        // Get all the branchExtensionList where actTap is greater than or equal to UPDATED_ACT_TAP
        defaultBranchExtensionShouldNotBeFound("actTap.greaterThanOrEqual=" + UPDATED_ACT_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByActTapIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where actTap is less than or equal to DEFAULT_ACT_TAP
        defaultBranchExtensionShouldBeFound("actTap.lessThanOrEqual=" + DEFAULT_ACT_TAP);

        // Get all the branchExtensionList where actTap is less than or equal to SMALLER_ACT_TAP
        defaultBranchExtensionShouldNotBeFound("actTap.lessThanOrEqual=" + SMALLER_ACT_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByActTapIsLessThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where actTap is less than DEFAULT_ACT_TAP
        defaultBranchExtensionShouldNotBeFound("actTap.lessThan=" + DEFAULT_ACT_TAP);

        // Get all the branchExtensionList where actTap is less than UPDATED_ACT_TAP
        defaultBranchExtensionShouldBeFound("actTap.lessThan=" + UPDATED_ACT_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByActTapIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where actTap is greater than DEFAULT_ACT_TAP
        defaultBranchExtensionShouldNotBeFound("actTap.greaterThan=" + DEFAULT_ACT_TAP);

        // Get all the branchExtensionList where actTap is greater than SMALLER_ACT_TAP
        defaultBranchExtensionShouldBeFound("actTap.greaterThan=" + SMALLER_ACT_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByMinTapIsEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where minTap equals to DEFAULT_MIN_TAP
        defaultBranchExtensionShouldBeFound("minTap.equals=" + DEFAULT_MIN_TAP);

        // Get all the branchExtensionList where minTap equals to UPDATED_MIN_TAP
        defaultBranchExtensionShouldNotBeFound("minTap.equals=" + UPDATED_MIN_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByMinTapIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where minTap not equals to DEFAULT_MIN_TAP
        defaultBranchExtensionShouldNotBeFound("minTap.notEquals=" + DEFAULT_MIN_TAP);

        // Get all the branchExtensionList where minTap not equals to UPDATED_MIN_TAP
        defaultBranchExtensionShouldBeFound("minTap.notEquals=" + UPDATED_MIN_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByMinTapIsInShouldWork() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where minTap in DEFAULT_MIN_TAP or UPDATED_MIN_TAP
        defaultBranchExtensionShouldBeFound("minTap.in=" + DEFAULT_MIN_TAP + "," + UPDATED_MIN_TAP);

        // Get all the branchExtensionList where minTap equals to UPDATED_MIN_TAP
        defaultBranchExtensionShouldNotBeFound("minTap.in=" + UPDATED_MIN_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByMinTapIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where minTap is not null
        defaultBranchExtensionShouldBeFound("minTap.specified=true");

        // Get all the branchExtensionList where minTap is null
        defaultBranchExtensionShouldNotBeFound("minTap.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByMinTapIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where minTap is greater than or equal to DEFAULT_MIN_TAP
        defaultBranchExtensionShouldBeFound("minTap.greaterThanOrEqual=" + DEFAULT_MIN_TAP);

        // Get all the branchExtensionList where minTap is greater than or equal to UPDATED_MIN_TAP
        defaultBranchExtensionShouldNotBeFound("minTap.greaterThanOrEqual=" + UPDATED_MIN_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByMinTapIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where minTap is less than or equal to DEFAULT_MIN_TAP
        defaultBranchExtensionShouldBeFound("minTap.lessThanOrEqual=" + DEFAULT_MIN_TAP);

        // Get all the branchExtensionList where minTap is less than or equal to SMALLER_MIN_TAP
        defaultBranchExtensionShouldNotBeFound("minTap.lessThanOrEqual=" + SMALLER_MIN_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByMinTapIsLessThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where minTap is less than DEFAULT_MIN_TAP
        defaultBranchExtensionShouldNotBeFound("minTap.lessThan=" + DEFAULT_MIN_TAP);

        // Get all the branchExtensionList where minTap is less than UPDATED_MIN_TAP
        defaultBranchExtensionShouldBeFound("minTap.lessThan=" + UPDATED_MIN_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByMinTapIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where minTap is greater than DEFAULT_MIN_TAP
        defaultBranchExtensionShouldNotBeFound("minTap.greaterThan=" + DEFAULT_MIN_TAP);

        // Get all the branchExtensionList where minTap is greater than SMALLER_MIN_TAP
        defaultBranchExtensionShouldBeFound("minTap.greaterThan=" + SMALLER_MIN_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByMaxTapIsEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where maxTap equals to DEFAULT_MAX_TAP
        defaultBranchExtensionShouldBeFound("maxTap.equals=" + DEFAULT_MAX_TAP);

        // Get all the branchExtensionList where maxTap equals to UPDATED_MAX_TAP
        defaultBranchExtensionShouldNotBeFound("maxTap.equals=" + UPDATED_MAX_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByMaxTapIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where maxTap not equals to DEFAULT_MAX_TAP
        defaultBranchExtensionShouldNotBeFound("maxTap.notEquals=" + DEFAULT_MAX_TAP);

        // Get all the branchExtensionList where maxTap not equals to UPDATED_MAX_TAP
        defaultBranchExtensionShouldBeFound("maxTap.notEquals=" + UPDATED_MAX_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByMaxTapIsInShouldWork() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where maxTap in DEFAULT_MAX_TAP or UPDATED_MAX_TAP
        defaultBranchExtensionShouldBeFound("maxTap.in=" + DEFAULT_MAX_TAP + "," + UPDATED_MAX_TAP);

        // Get all the branchExtensionList where maxTap equals to UPDATED_MAX_TAP
        defaultBranchExtensionShouldNotBeFound("maxTap.in=" + UPDATED_MAX_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByMaxTapIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where maxTap is not null
        defaultBranchExtensionShouldBeFound("maxTap.specified=true");

        // Get all the branchExtensionList where maxTap is null
        defaultBranchExtensionShouldNotBeFound("maxTap.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByMaxTapIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where maxTap is greater than or equal to DEFAULT_MAX_TAP
        defaultBranchExtensionShouldBeFound("maxTap.greaterThanOrEqual=" + DEFAULT_MAX_TAP);

        // Get all the branchExtensionList where maxTap is greater than or equal to UPDATED_MAX_TAP
        defaultBranchExtensionShouldNotBeFound("maxTap.greaterThanOrEqual=" + UPDATED_MAX_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByMaxTapIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where maxTap is less than or equal to DEFAULT_MAX_TAP
        defaultBranchExtensionShouldBeFound("maxTap.lessThanOrEqual=" + DEFAULT_MAX_TAP);

        // Get all the branchExtensionList where maxTap is less than or equal to SMALLER_MAX_TAP
        defaultBranchExtensionShouldNotBeFound("maxTap.lessThanOrEqual=" + SMALLER_MAX_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByMaxTapIsLessThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where maxTap is less than DEFAULT_MAX_TAP
        defaultBranchExtensionShouldNotBeFound("maxTap.lessThan=" + DEFAULT_MAX_TAP);

        // Get all the branchExtensionList where maxTap is less than UPDATED_MAX_TAP
        defaultBranchExtensionShouldBeFound("maxTap.lessThan=" + UPDATED_MAX_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByMaxTapIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where maxTap is greater than DEFAULT_MAX_TAP
        defaultBranchExtensionShouldNotBeFound("maxTap.greaterThan=" + DEFAULT_MAX_TAP);

        // Get all the branchExtensionList where maxTap is greater than SMALLER_MAX_TAP
        defaultBranchExtensionShouldBeFound("maxTap.greaterThan=" + SMALLER_MAX_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNormalTapIsEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where normalTap equals to DEFAULT_NORMAL_TAP
        defaultBranchExtensionShouldBeFound("normalTap.equals=" + DEFAULT_NORMAL_TAP);

        // Get all the branchExtensionList where normalTap equals to UPDATED_NORMAL_TAP
        defaultBranchExtensionShouldNotBeFound("normalTap.equals=" + UPDATED_NORMAL_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNormalTapIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where normalTap not equals to DEFAULT_NORMAL_TAP
        defaultBranchExtensionShouldNotBeFound("normalTap.notEquals=" + DEFAULT_NORMAL_TAP);

        // Get all the branchExtensionList where normalTap not equals to UPDATED_NORMAL_TAP
        defaultBranchExtensionShouldBeFound("normalTap.notEquals=" + UPDATED_NORMAL_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNormalTapIsInShouldWork() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where normalTap in DEFAULT_NORMAL_TAP or UPDATED_NORMAL_TAP
        defaultBranchExtensionShouldBeFound("normalTap.in=" + DEFAULT_NORMAL_TAP + "," + UPDATED_NORMAL_TAP);

        // Get all the branchExtensionList where normalTap equals to UPDATED_NORMAL_TAP
        defaultBranchExtensionShouldNotBeFound("normalTap.in=" + UPDATED_NORMAL_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNormalTapIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where normalTap is not null
        defaultBranchExtensionShouldBeFound("normalTap.specified=true");

        // Get all the branchExtensionList where normalTap is null
        defaultBranchExtensionShouldNotBeFound("normalTap.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNormalTapIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where normalTap is greater than or equal to DEFAULT_NORMAL_TAP
        defaultBranchExtensionShouldBeFound("normalTap.greaterThanOrEqual=" + DEFAULT_NORMAL_TAP);

        // Get all the branchExtensionList where normalTap is greater than or equal to UPDATED_NORMAL_TAP
        defaultBranchExtensionShouldNotBeFound("normalTap.greaterThanOrEqual=" + UPDATED_NORMAL_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNormalTapIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where normalTap is less than or equal to DEFAULT_NORMAL_TAP
        defaultBranchExtensionShouldBeFound("normalTap.lessThanOrEqual=" + DEFAULT_NORMAL_TAP);

        // Get all the branchExtensionList where normalTap is less than or equal to SMALLER_NORMAL_TAP
        defaultBranchExtensionShouldNotBeFound("normalTap.lessThanOrEqual=" + SMALLER_NORMAL_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNormalTapIsLessThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where normalTap is less than DEFAULT_NORMAL_TAP
        defaultBranchExtensionShouldNotBeFound("normalTap.lessThan=" + DEFAULT_NORMAL_TAP);

        // Get all the branchExtensionList where normalTap is less than UPDATED_NORMAL_TAP
        defaultBranchExtensionShouldBeFound("normalTap.lessThan=" + UPDATED_NORMAL_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNormalTapIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where normalTap is greater than DEFAULT_NORMAL_TAP
        defaultBranchExtensionShouldNotBeFound("normalTap.greaterThan=" + DEFAULT_NORMAL_TAP);

        // Get all the branchExtensionList where normalTap is greater than SMALLER_NORMAL_TAP
        defaultBranchExtensionShouldBeFound("normalTap.greaterThan=" + SMALLER_NORMAL_TAP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNominalRatioIsEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where nominalRatio equals to DEFAULT_NOMINAL_RATIO
        defaultBranchExtensionShouldBeFound("nominalRatio.equals=" + DEFAULT_NOMINAL_RATIO);

        // Get all the branchExtensionList where nominalRatio equals to UPDATED_NOMINAL_RATIO
        defaultBranchExtensionShouldNotBeFound("nominalRatio.equals=" + UPDATED_NOMINAL_RATIO);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNominalRatioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where nominalRatio not equals to DEFAULT_NOMINAL_RATIO
        defaultBranchExtensionShouldNotBeFound("nominalRatio.notEquals=" + DEFAULT_NOMINAL_RATIO);

        // Get all the branchExtensionList where nominalRatio not equals to UPDATED_NOMINAL_RATIO
        defaultBranchExtensionShouldBeFound("nominalRatio.notEquals=" + UPDATED_NOMINAL_RATIO);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNominalRatioIsInShouldWork() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where nominalRatio in DEFAULT_NOMINAL_RATIO or UPDATED_NOMINAL_RATIO
        defaultBranchExtensionShouldBeFound("nominalRatio.in=" + DEFAULT_NOMINAL_RATIO + "," + UPDATED_NOMINAL_RATIO);

        // Get all the branchExtensionList where nominalRatio equals to UPDATED_NOMINAL_RATIO
        defaultBranchExtensionShouldNotBeFound("nominalRatio.in=" + UPDATED_NOMINAL_RATIO);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNominalRatioIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where nominalRatio is not null
        defaultBranchExtensionShouldBeFound("nominalRatio.specified=true");

        // Get all the branchExtensionList where nominalRatio is null
        defaultBranchExtensionShouldNotBeFound("nominalRatio.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNominalRatioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where nominalRatio is greater than or equal to DEFAULT_NOMINAL_RATIO
        defaultBranchExtensionShouldBeFound("nominalRatio.greaterThanOrEqual=" + DEFAULT_NOMINAL_RATIO);

        // Get all the branchExtensionList where nominalRatio is greater than or equal to UPDATED_NOMINAL_RATIO
        defaultBranchExtensionShouldNotBeFound("nominalRatio.greaterThanOrEqual=" + UPDATED_NOMINAL_RATIO);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNominalRatioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where nominalRatio is less than or equal to DEFAULT_NOMINAL_RATIO
        defaultBranchExtensionShouldBeFound("nominalRatio.lessThanOrEqual=" + DEFAULT_NOMINAL_RATIO);

        // Get all the branchExtensionList where nominalRatio is less than or equal to SMALLER_NOMINAL_RATIO
        defaultBranchExtensionShouldNotBeFound("nominalRatio.lessThanOrEqual=" + SMALLER_NOMINAL_RATIO);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNominalRatioIsLessThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where nominalRatio is less than DEFAULT_NOMINAL_RATIO
        defaultBranchExtensionShouldNotBeFound("nominalRatio.lessThan=" + DEFAULT_NOMINAL_RATIO);

        // Get all the branchExtensionList where nominalRatio is less than UPDATED_NOMINAL_RATIO
        defaultBranchExtensionShouldBeFound("nominalRatio.lessThan=" + UPDATED_NOMINAL_RATIO);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNominalRatioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where nominalRatio is greater than DEFAULT_NOMINAL_RATIO
        defaultBranchExtensionShouldNotBeFound("nominalRatio.greaterThan=" + DEFAULT_NOMINAL_RATIO);

        // Get all the branchExtensionList where nominalRatio is greater than SMALLER_NOMINAL_RATIO
        defaultBranchExtensionShouldBeFound("nominalRatio.greaterThan=" + SMALLER_NOMINAL_RATIO);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByrIpIsEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where rIp equals to DEFAULT_R_IP
        defaultBranchExtensionShouldBeFound("rIp.equals=" + DEFAULT_R_IP);

        // Get all the branchExtensionList where rIp equals to UPDATED_R_IP
        defaultBranchExtensionShouldNotBeFound("rIp.equals=" + UPDATED_R_IP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByrIpIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where rIp not equals to DEFAULT_R_IP
        defaultBranchExtensionShouldNotBeFound("rIp.notEquals=" + DEFAULT_R_IP);

        // Get all the branchExtensionList where rIp not equals to UPDATED_R_IP
        defaultBranchExtensionShouldBeFound("rIp.notEquals=" + UPDATED_R_IP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByrIpIsInShouldWork() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where rIp in DEFAULT_R_IP or UPDATED_R_IP
        defaultBranchExtensionShouldBeFound("rIp.in=" + DEFAULT_R_IP + "," + UPDATED_R_IP);

        // Get all the branchExtensionList where rIp equals to UPDATED_R_IP
        defaultBranchExtensionShouldNotBeFound("rIp.in=" + UPDATED_R_IP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByrIpIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where rIp is not null
        defaultBranchExtensionShouldBeFound("rIp.specified=true");

        // Get all the branchExtensionList where rIp is null
        defaultBranchExtensionShouldNotBeFound("rIp.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByrIpIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where rIp is greater than or equal to DEFAULT_R_IP
        defaultBranchExtensionShouldBeFound("rIp.greaterThanOrEqual=" + DEFAULT_R_IP);

        // Get all the branchExtensionList where rIp is greater than or equal to UPDATED_R_IP
        defaultBranchExtensionShouldNotBeFound("rIp.greaterThanOrEqual=" + UPDATED_R_IP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByrIpIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where rIp is less than or equal to DEFAULT_R_IP
        defaultBranchExtensionShouldBeFound("rIp.lessThanOrEqual=" + DEFAULT_R_IP);

        // Get all the branchExtensionList where rIp is less than or equal to SMALLER_R_IP
        defaultBranchExtensionShouldNotBeFound("rIp.lessThanOrEqual=" + SMALLER_R_IP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByrIpIsLessThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where rIp is less than DEFAULT_R_IP
        defaultBranchExtensionShouldNotBeFound("rIp.lessThan=" + DEFAULT_R_IP);

        // Get all the branchExtensionList where rIp is less than UPDATED_R_IP
        defaultBranchExtensionShouldBeFound("rIp.lessThan=" + UPDATED_R_IP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByrIpIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where rIp is greater than DEFAULT_R_IP
        defaultBranchExtensionShouldNotBeFound("rIp.greaterThan=" + DEFAULT_R_IP);

        // Get all the branchExtensionList where rIp is greater than SMALLER_R_IP
        defaultBranchExtensionShouldBeFound("rIp.greaterThan=" + SMALLER_R_IP);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByrNIsEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where rN equals to DEFAULT_R_N
        defaultBranchExtensionShouldBeFound("rN.equals=" + DEFAULT_R_N);

        // Get all the branchExtensionList where rN equals to UPDATED_R_N
        defaultBranchExtensionShouldNotBeFound("rN.equals=" + UPDATED_R_N);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByrNIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where rN not equals to DEFAULT_R_N
        defaultBranchExtensionShouldNotBeFound("rN.notEquals=" + DEFAULT_R_N);

        // Get all the branchExtensionList where rN not equals to UPDATED_R_N
        defaultBranchExtensionShouldBeFound("rN.notEquals=" + UPDATED_R_N);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByrNIsInShouldWork() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where rN in DEFAULT_R_N or UPDATED_R_N
        defaultBranchExtensionShouldBeFound("rN.in=" + DEFAULT_R_N + "," + UPDATED_R_N);

        // Get all the branchExtensionList where rN equals to UPDATED_R_N
        defaultBranchExtensionShouldNotBeFound("rN.in=" + UPDATED_R_N);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByrNIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where rN is not null
        defaultBranchExtensionShouldBeFound("rN.specified=true");

        // Get all the branchExtensionList where rN is null
        defaultBranchExtensionShouldNotBeFound("rN.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByrNIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where rN is greater than or equal to DEFAULT_R_N
        defaultBranchExtensionShouldBeFound("rN.greaterThanOrEqual=" + DEFAULT_R_N);

        // Get all the branchExtensionList where rN is greater than or equal to UPDATED_R_N
        defaultBranchExtensionShouldNotBeFound("rN.greaterThanOrEqual=" + UPDATED_R_N);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByrNIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where rN is less than or equal to DEFAULT_R_N
        defaultBranchExtensionShouldBeFound("rN.lessThanOrEqual=" + DEFAULT_R_N);

        // Get all the branchExtensionList where rN is less than or equal to SMALLER_R_N
        defaultBranchExtensionShouldNotBeFound("rN.lessThanOrEqual=" + SMALLER_R_N);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByrNIsLessThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where rN is less than DEFAULT_R_N
        defaultBranchExtensionShouldNotBeFound("rN.lessThan=" + DEFAULT_R_N);

        // Get all the branchExtensionList where rN is less than UPDATED_R_N
        defaultBranchExtensionShouldBeFound("rN.lessThan=" + UPDATED_R_N);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByrNIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where rN is greater than DEFAULT_R_N
        defaultBranchExtensionShouldNotBeFound("rN.greaterThan=" + DEFAULT_R_N);

        // Get all the branchExtensionList where rN is greater than SMALLER_R_N
        defaultBranchExtensionShouldBeFound("rN.greaterThan=" + SMALLER_R_N);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByr0IsEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where r0 equals to DEFAULT_R_0
        defaultBranchExtensionShouldBeFound("r0.equals=" + DEFAULT_R_0);

        // Get all the branchExtensionList where r0 equals to UPDATED_R_0
        defaultBranchExtensionShouldNotBeFound("r0.equals=" + UPDATED_R_0);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByr0IsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where r0 not equals to DEFAULT_R_0
        defaultBranchExtensionShouldNotBeFound("r0.notEquals=" + DEFAULT_R_0);

        // Get all the branchExtensionList where r0 not equals to UPDATED_R_0
        defaultBranchExtensionShouldBeFound("r0.notEquals=" + UPDATED_R_0);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByr0IsInShouldWork() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where r0 in DEFAULT_R_0 or UPDATED_R_0
        defaultBranchExtensionShouldBeFound("r0.in=" + DEFAULT_R_0 + "," + UPDATED_R_0);

        // Get all the branchExtensionList where r0 equals to UPDATED_R_0
        defaultBranchExtensionShouldNotBeFound("r0.in=" + UPDATED_R_0);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByr0IsNullOrNotNull() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where r0 is not null
        defaultBranchExtensionShouldBeFound("r0.specified=true");

        // Get all the branchExtensionList where r0 is null
        defaultBranchExtensionShouldNotBeFound("r0.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByr0IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where r0 is greater than or equal to DEFAULT_R_0
        defaultBranchExtensionShouldBeFound("r0.greaterThanOrEqual=" + DEFAULT_R_0);

        // Get all the branchExtensionList where r0 is greater than or equal to UPDATED_R_0
        defaultBranchExtensionShouldNotBeFound("r0.greaterThanOrEqual=" + UPDATED_R_0);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByr0IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where r0 is less than or equal to DEFAULT_R_0
        defaultBranchExtensionShouldBeFound("r0.lessThanOrEqual=" + DEFAULT_R_0);

        // Get all the branchExtensionList where r0 is less than or equal to SMALLER_R_0
        defaultBranchExtensionShouldNotBeFound("r0.lessThanOrEqual=" + SMALLER_R_0);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByr0IsLessThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where r0 is less than DEFAULT_R_0
        defaultBranchExtensionShouldNotBeFound("r0.lessThan=" + DEFAULT_R_0);

        // Get all the branchExtensionList where r0 is less than UPDATED_R_0
        defaultBranchExtensionShouldBeFound("r0.lessThan=" + UPDATED_R_0);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByr0IsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where r0 is greater than DEFAULT_R_0
        defaultBranchExtensionShouldNotBeFound("r0.greaterThan=" + DEFAULT_R_0);

        // Get all the branchExtensionList where r0 is greater than SMALLER_R_0
        defaultBranchExtensionShouldBeFound("r0.greaterThan=" + SMALLER_R_0);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByx0IsEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where x0 equals to DEFAULT_X_0
        defaultBranchExtensionShouldBeFound("x0.equals=" + DEFAULT_X_0);

        // Get all the branchExtensionList where x0 equals to UPDATED_X_0
        defaultBranchExtensionShouldNotBeFound("x0.equals=" + UPDATED_X_0);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByx0IsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where x0 not equals to DEFAULT_X_0
        defaultBranchExtensionShouldNotBeFound("x0.notEquals=" + DEFAULT_X_0);

        // Get all the branchExtensionList where x0 not equals to UPDATED_X_0
        defaultBranchExtensionShouldBeFound("x0.notEquals=" + UPDATED_X_0);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByx0IsInShouldWork() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where x0 in DEFAULT_X_0 or UPDATED_X_0
        defaultBranchExtensionShouldBeFound("x0.in=" + DEFAULT_X_0 + "," + UPDATED_X_0);

        // Get all the branchExtensionList where x0 equals to UPDATED_X_0
        defaultBranchExtensionShouldNotBeFound("x0.in=" + UPDATED_X_0);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByx0IsNullOrNotNull() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where x0 is not null
        defaultBranchExtensionShouldBeFound("x0.specified=true");

        // Get all the branchExtensionList where x0 is null
        defaultBranchExtensionShouldNotBeFound("x0.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByx0IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where x0 is greater than or equal to DEFAULT_X_0
        defaultBranchExtensionShouldBeFound("x0.greaterThanOrEqual=" + DEFAULT_X_0);

        // Get all the branchExtensionList where x0 is greater than or equal to UPDATED_X_0
        defaultBranchExtensionShouldNotBeFound("x0.greaterThanOrEqual=" + UPDATED_X_0);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByx0IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where x0 is less than or equal to DEFAULT_X_0
        defaultBranchExtensionShouldBeFound("x0.lessThanOrEqual=" + DEFAULT_X_0);

        // Get all the branchExtensionList where x0 is less than or equal to SMALLER_X_0
        defaultBranchExtensionShouldNotBeFound("x0.lessThanOrEqual=" + SMALLER_X_0);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByx0IsLessThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where x0 is less than DEFAULT_X_0
        defaultBranchExtensionShouldNotBeFound("x0.lessThan=" + DEFAULT_X_0);

        // Get all the branchExtensionList where x0 is less than UPDATED_X_0
        defaultBranchExtensionShouldBeFound("x0.lessThan=" + UPDATED_X_0);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByx0IsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where x0 is greater than DEFAULT_X_0
        defaultBranchExtensionShouldNotBeFound("x0.greaterThan=" + DEFAULT_X_0);

        // Get all the branchExtensionList where x0 is greater than SMALLER_X_0
        defaultBranchExtensionShouldBeFound("x0.greaterThan=" + SMALLER_X_0);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByb0IsEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where b0 equals to DEFAULT_B_0
        defaultBranchExtensionShouldBeFound("b0.equals=" + DEFAULT_B_0);

        // Get all the branchExtensionList where b0 equals to UPDATED_B_0
        defaultBranchExtensionShouldNotBeFound("b0.equals=" + UPDATED_B_0);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByb0IsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where b0 not equals to DEFAULT_B_0
        defaultBranchExtensionShouldNotBeFound("b0.notEquals=" + DEFAULT_B_0);

        // Get all the branchExtensionList where b0 not equals to UPDATED_B_0
        defaultBranchExtensionShouldBeFound("b0.notEquals=" + UPDATED_B_0);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByb0IsInShouldWork() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where b0 in DEFAULT_B_0 or UPDATED_B_0
        defaultBranchExtensionShouldBeFound("b0.in=" + DEFAULT_B_0 + "," + UPDATED_B_0);

        // Get all the branchExtensionList where b0 equals to UPDATED_B_0
        defaultBranchExtensionShouldNotBeFound("b0.in=" + UPDATED_B_0);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByb0IsNullOrNotNull() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where b0 is not null
        defaultBranchExtensionShouldBeFound("b0.specified=true");

        // Get all the branchExtensionList where b0 is null
        defaultBranchExtensionShouldNotBeFound("b0.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByb0IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where b0 is greater than or equal to DEFAULT_B_0
        defaultBranchExtensionShouldBeFound("b0.greaterThanOrEqual=" + DEFAULT_B_0);

        // Get all the branchExtensionList where b0 is greater than or equal to UPDATED_B_0
        defaultBranchExtensionShouldNotBeFound("b0.greaterThanOrEqual=" + UPDATED_B_0);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByb0IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where b0 is less than or equal to DEFAULT_B_0
        defaultBranchExtensionShouldBeFound("b0.lessThanOrEqual=" + DEFAULT_B_0);

        // Get all the branchExtensionList where b0 is less than or equal to SMALLER_B_0
        defaultBranchExtensionShouldNotBeFound("b0.lessThanOrEqual=" + SMALLER_B_0);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByb0IsLessThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where b0 is less than DEFAULT_B_0
        defaultBranchExtensionShouldNotBeFound("b0.lessThan=" + DEFAULT_B_0);

        // Get all the branchExtensionList where b0 is less than UPDATED_B_0
        defaultBranchExtensionShouldBeFound("b0.lessThan=" + UPDATED_B_0);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByb0IsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where b0 is greater than DEFAULT_B_0
        defaultBranchExtensionShouldNotBeFound("b0.greaterThan=" + DEFAULT_B_0);

        // Get all the branchExtensionList where b0 is greater than SMALLER_B_0
        defaultBranchExtensionShouldBeFound("b0.greaterThan=" + SMALLER_B_0);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByLengthIsEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where length equals to DEFAULT_LENGTH
        defaultBranchExtensionShouldBeFound("length.equals=" + DEFAULT_LENGTH);

        // Get all the branchExtensionList where length equals to UPDATED_LENGTH
        defaultBranchExtensionShouldNotBeFound("length.equals=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByLengthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where length not equals to DEFAULT_LENGTH
        defaultBranchExtensionShouldNotBeFound("length.notEquals=" + DEFAULT_LENGTH);

        // Get all the branchExtensionList where length not equals to UPDATED_LENGTH
        defaultBranchExtensionShouldBeFound("length.notEquals=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByLengthIsInShouldWork() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where length in DEFAULT_LENGTH or UPDATED_LENGTH
        defaultBranchExtensionShouldBeFound("length.in=" + DEFAULT_LENGTH + "," + UPDATED_LENGTH);

        // Get all the branchExtensionList where length equals to UPDATED_LENGTH
        defaultBranchExtensionShouldNotBeFound("length.in=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByLengthIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where length is not null
        defaultBranchExtensionShouldBeFound("length.specified=true");

        // Get all the branchExtensionList where length is null
        defaultBranchExtensionShouldNotBeFound("length.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByLengthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where length is greater than or equal to DEFAULT_LENGTH
        defaultBranchExtensionShouldBeFound("length.greaterThanOrEqual=" + DEFAULT_LENGTH);

        // Get all the branchExtensionList where length is greater than or equal to UPDATED_LENGTH
        defaultBranchExtensionShouldNotBeFound("length.greaterThanOrEqual=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByLengthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where length is less than or equal to DEFAULT_LENGTH
        defaultBranchExtensionShouldBeFound("length.lessThanOrEqual=" + DEFAULT_LENGTH);

        // Get all the branchExtensionList where length is less than or equal to SMALLER_LENGTH
        defaultBranchExtensionShouldNotBeFound("length.lessThanOrEqual=" + SMALLER_LENGTH);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByLengthIsLessThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where length is less than DEFAULT_LENGTH
        defaultBranchExtensionShouldNotBeFound("length.lessThan=" + DEFAULT_LENGTH);

        // Get all the branchExtensionList where length is less than UPDATED_LENGTH
        defaultBranchExtensionShouldBeFound("length.lessThan=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByLengthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where length is greater than DEFAULT_LENGTH
        defaultBranchExtensionShouldNotBeFound("length.greaterThan=" + DEFAULT_LENGTH);

        // Get all the branchExtensionList where length is greater than SMALLER_LENGTH
        defaultBranchExtensionShouldBeFound("length.greaterThan=" + SMALLER_LENGTH);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNormStatIsEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where normStat equals to DEFAULT_NORM_STAT
        defaultBranchExtensionShouldBeFound("normStat.equals=" + DEFAULT_NORM_STAT);

        // Get all the branchExtensionList where normStat equals to UPDATED_NORM_STAT
        defaultBranchExtensionShouldNotBeFound("normStat.equals=" + UPDATED_NORM_STAT);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNormStatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where normStat not equals to DEFAULT_NORM_STAT
        defaultBranchExtensionShouldNotBeFound("normStat.notEquals=" + DEFAULT_NORM_STAT);

        // Get all the branchExtensionList where normStat not equals to UPDATED_NORM_STAT
        defaultBranchExtensionShouldBeFound("normStat.notEquals=" + UPDATED_NORM_STAT);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNormStatIsInShouldWork() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where normStat in DEFAULT_NORM_STAT or UPDATED_NORM_STAT
        defaultBranchExtensionShouldBeFound("normStat.in=" + DEFAULT_NORM_STAT + "," + UPDATED_NORM_STAT);

        // Get all the branchExtensionList where normStat equals to UPDATED_NORM_STAT
        defaultBranchExtensionShouldNotBeFound("normStat.in=" + UPDATED_NORM_STAT);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNormStatIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where normStat is not null
        defaultBranchExtensionShouldBeFound("normStat.specified=true");

        // Get all the branchExtensionList where normStat is null
        defaultBranchExtensionShouldNotBeFound("normStat.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNormStatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where normStat is greater than or equal to DEFAULT_NORM_STAT
        defaultBranchExtensionShouldBeFound("normStat.greaterThanOrEqual=" + DEFAULT_NORM_STAT);

        // Get all the branchExtensionList where normStat is greater than or equal to UPDATED_NORM_STAT
        defaultBranchExtensionShouldNotBeFound("normStat.greaterThanOrEqual=" + UPDATED_NORM_STAT);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNormStatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where normStat is less than or equal to DEFAULT_NORM_STAT
        defaultBranchExtensionShouldBeFound("normStat.lessThanOrEqual=" + DEFAULT_NORM_STAT);

        // Get all the branchExtensionList where normStat is less than or equal to SMALLER_NORM_STAT
        defaultBranchExtensionShouldNotBeFound("normStat.lessThanOrEqual=" + SMALLER_NORM_STAT);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNormStatIsLessThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where normStat is less than DEFAULT_NORM_STAT
        defaultBranchExtensionShouldNotBeFound("normStat.lessThan=" + DEFAULT_NORM_STAT);

        // Get all the branchExtensionList where normStat is less than UPDATED_NORM_STAT
        defaultBranchExtensionShouldBeFound("normStat.lessThan=" + UPDATED_NORM_STAT);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByNormStatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where normStat is greater than DEFAULT_NORM_STAT
        defaultBranchExtensionShouldNotBeFound("normStat.greaterThan=" + DEFAULT_NORM_STAT);

        // Get all the branchExtensionList where normStat is greater than SMALLER_NORM_STAT
        defaultBranchExtensionShouldBeFound("normStat.greaterThan=" + SMALLER_NORM_STAT);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByGIsEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where g equals to DEFAULT_G
        defaultBranchExtensionShouldBeFound("g.equals=" + DEFAULT_G);

        // Get all the branchExtensionList where g equals to UPDATED_G
        defaultBranchExtensionShouldNotBeFound("g.equals=" + UPDATED_G);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByGIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where g not equals to DEFAULT_G
        defaultBranchExtensionShouldNotBeFound("g.notEquals=" + DEFAULT_G);

        // Get all the branchExtensionList where g not equals to UPDATED_G
        defaultBranchExtensionShouldBeFound("g.notEquals=" + UPDATED_G);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByGIsInShouldWork() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where g in DEFAULT_G or UPDATED_G
        defaultBranchExtensionShouldBeFound("g.in=" + DEFAULT_G + "," + UPDATED_G);

        // Get all the branchExtensionList where g equals to UPDATED_G
        defaultBranchExtensionShouldNotBeFound("g.in=" + UPDATED_G);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByGIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where g is not null
        defaultBranchExtensionShouldBeFound("g.specified=true");

        // Get all the branchExtensionList where g is null
        defaultBranchExtensionShouldNotBeFound("g.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByGIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where g is greater than or equal to DEFAULT_G
        defaultBranchExtensionShouldBeFound("g.greaterThanOrEqual=" + DEFAULT_G);

        // Get all the branchExtensionList where g is greater than or equal to UPDATED_G
        defaultBranchExtensionShouldNotBeFound("g.greaterThanOrEqual=" + UPDATED_G);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByGIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where g is less than or equal to DEFAULT_G
        defaultBranchExtensionShouldBeFound("g.lessThanOrEqual=" + DEFAULT_G);

        // Get all the branchExtensionList where g is less than or equal to SMALLER_G
        defaultBranchExtensionShouldNotBeFound("g.lessThanOrEqual=" + SMALLER_G);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByGIsLessThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where g is less than DEFAULT_G
        defaultBranchExtensionShouldNotBeFound("g.lessThan=" + DEFAULT_G);

        // Get all the branchExtensionList where g is less than UPDATED_G
        defaultBranchExtensionShouldBeFound("g.lessThan=" + UPDATED_G);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByGIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where g is greater than DEFAULT_G
        defaultBranchExtensionShouldNotBeFound("g.greaterThan=" + DEFAULT_G);

        // Get all the branchExtensionList where g is greater than SMALLER_G
        defaultBranchExtensionShouldBeFound("g.greaterThan=" + SMALLER_G);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsBymRidIsEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where mRid equals to DEFAULT_M_RID
        defaultBranchExtensionShouldBeFound("mRid.equals=" + DEFAULT_M_RID);

        // Get all the branchExtensionList where mRid equals to UPDATED_M_RID
        defaultBranchExtensionShouldNotBeFound("mRid.equals=" + UPDATED_M_RID);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsBymRidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where mRid not equals to DEFAULT_M_RID
        defaultBranchExtensionShouldNotBeFound("mRid.notEquals=" + DEFAULT_M_RID);

        // Get all the branchExtensionList where mRid not equals to UPDATED_M_RID
        defaultBranchExtensionShouldBeFound("mRid.notEquals=" + UPDATED_M_RID);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsBymRidIsInShouldWork() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where mRid in DEFAULT_M_RID or UPDATED_M_RID
        defaultBranchExtensionShouldBeFound("mRid.in=" + DEFAULT_M_RID + "," + UPDATED_M_RID);

        // Get all the branchExtensionList where mRid equals to UPDATED_M_RID
        defaultBranchExtensionShouldNotBeFound("mRid.in=" + UPDATED_M_RID);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsBymRidIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where mRid is not null
        defaultBranchExtensionShouldBeFound("mRid.specified=true");

        // Get all the branchExtensionList where mRid is null
        defaultBranchExtensionShouldNotBeFound("mRid.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchExtensionsBymRidContainsSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where mRid contains DEFAULT_M_RID
        defaultBranchExtensionShouldBeFound("mRid.contains=" + DEFAULT_M_RID);

        // Get all the branchExtensionList where mRid contains UPDATED_M_RID
        defaultBranchExtensionShouldNotBeFound("mRid.contains=" + UPDATED_M_RID);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsBymRidNotContainsSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        // Get all the branchExtensionList where mRid does not contain DEFAULT_M_RID
        defaultBranchExtensionShouldNotBeFound("mRid.doesNotContain=" + DEFAULT_M_RID);

        // Get all the branchExtensionList where mRid does not contain UPDATED_M_RID
        defaultBranchExtensionShouldBeFound("mRid.doesNotContain=" + UPDATED_M_RID);
    }

    @Test
    @Transactional
    void getAllBranchExtensionsByBranchIsEqualToSomething() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);
        Branch branch;
        if (TestUtil.findAll(em, Branch.class).isEmpty()) {
            branch = BranchResourceIT.createEntity(em);
            em.persist(branch);
            em.flush();
        } else {
            branch = TestUtil.findAll(em, Branch.class).get(0);
        }
        em.persist(branch);
        em.flush();
        branchExtension.setBranch(branch);
        branchExtensionRepository.saveAndFlush(branchExtension);
        Long branchId = branch.getId();

        // Get all the branchExtensionList where branch equals to branchId
        defaultBranchExtensionShouldBeFound("branchId.equals=" + branchId);

        // Get all the branchExtensionList where branch equals to (branchId + 1)
        defaultBranchExtensionShouldNotBeFound("branchId.equals=" + (branchId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBranchExtensionShouldBeFound(String filter) throws Exception {
        restBranchExtensionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branchExtension.getId().intValue())))
            .andExpect(jsonPath("$.[*].stepSize").value(hasItem(DEFAULT_STEP_SIZE.doubleValue())))
            .andExpect(jsonPath("$.[*].actTap").value(hasItem(DEFAULT_ACT_TAP.doubleValue())))
            .andExpect(jsonPath("$.[*].minTap").value(hasItem(DEFAULT_MIN_TAP.doubleValue())))
            .andExpect(jsonPath("$.[*].maxTap").value(hasItem(DEFAULT_MAX_TAP.doubleValue())))
            .andExpect(jsonPath("$.[*].normalTap").value(hasItem(DEFAULT_NORMAL_TAP.doubleValue())))
            .andExpect(jsonPath("$.[*].nominalRatio").value(hasItem(DEFAULT_NOMINAL_RATIO.doubleValue())))
            .andExpect(jsonPath("$.[*].rIp").value(hasItem(DEFAULT_R_IP.doubleValue())))
            .andExpect(jsonPath("$.[*].rN").value(hasItem(DEFAULT_R_N.doubleValue())))
            .andExpect(jsonPath("$.[*].r0").value(hasItem(DEFAULT_R_0.doubleValue())))
            .andExpect(jsonPath("$.[*].x0").value(hasItem(DEFAULT_X_0.doubleValue())))
            .andExpect(jsonPath("$.[*].b0").value(hasItem(DEFAULT_B_0.doubleValue())))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH.doubleValue())))
            .andExpect(jsonPath("$.[*].normStat").value(hasItem(DEFAULT_NORM_STAT)))
            .andExpect(jsonPath("$.[*].g").value(hasItem(DEFAULT_G.doubleValue())))
            .andExpect(jsonPath("$.[*].mRid").value(hasItem(DEFAULT_M_RID)));

        // Check, that the count call also returns 1
        restBranchExtensionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBranchExtensionShouldNotBeFound(String filter) throws Exception {
        restBranchExtensionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBranchExtensionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBranchExtension() throws Exception {
        // Get the branchExtension
        restBranchExtensionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBranchExtension() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        int databaseSizeBeforeUpdate = branchExtensionRepository.findAll().size();

        // Update the branchExtension
        BranchExtension updatedBranchExtension = branchExtensionRepository.findById(branchExtension.getId()).get();
        // Disconnect from session so that the updates on updatedBranchExtension are not directly saved in db
        em.detach(updatedBranchExtension);
        updatedBranchExtension
            .stepSize(UPDATED_STEP_SIZE)
            .actTap(UPDATED_ACT_TAP)
            .minTap(UPDATED_MIN_TAP)
            .maxTap(UPDATED_MAX_TAP)
            .normalTap(UPDATED_NORMAL_TAP)
            .nominalRatio(UPDATED_NOMINAL_RATIO)
            .rIp(UPDATED_R_IP)
            .rN(UPDATED_R_N)
            .r0(UPDATED_R_0)
            .x0(UPDATED_X_0)
            .b0(UPDATED_B_0)
            .length(UPDATED_LENGTH)
            .normStat(UPDATED_NORM_STAT)
            .g(UPDATED_G)
            .mRid(UPDATED_M_RID);
        BranchExtensionDTO branchExtensionDTO = branchExtensionMapper.toDto(updatedBranchExtension);

        restBranchExtensionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, branchExtensionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchExtensionDTO))
            )
            .andExpect(status().isOk());

        // Validate the BranchExtension in the database
        List<BranchExtension> branchExtensionList = branchExtensionRepository.findAll();
        assertThat(branchExtensionList).hasSize(databaseSizeBeforeUpdate);
        BranchExtension testBranchExtension = branchExtensionList.get(branchExtensionList.size() - 1);
        assertThat(testBranchExtension.getStepSize()).isEqualTo(UPDATED_STEP_SIZE);
        assertThat(testBranchExtension.getActTap()).isEqualTo(UPDATED_ACT_TAP);
        assertThat(testBranchExtension.getMinTap()).isEqualTo(UPDATED_MIN_TAP);
        assertThat(testBranchExtension.getMaxTap()).isEqualTo(UPDATED_MAX_TAP);
        assertThat(testBranchExtension.getNormalTap()).isEqualTo(UPDATED_NORMAL_TAP);
        assertThat(testBranchExtension.getNominalRatio()).isEqualTo(UPDATED_NOMINAL_RATIO);
        assertThat(testBranchExtension.getrIp()).isEqualTo(UPDATED_R_IP);
        assertThat(testBranchExtension.getrN()).isEqualTo(UPDATED_R_N);
        assertThat(testBranchExtension.getr0()).isEqualTo(UPDATED_R_0);
        assertThat(testBranchExtension.getx0()).isEqualTo(UPDATED_X_0);
        assertThat(testBranchExtension.getb0()).isEqualTo(UPDATED_B_0);
        assertThat(testBranchExtension.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testBranchExtension.getNormStat()).isEqualTo(UPDATED_NORM_STAT);
        assertThat(testBranchExtension.getG()).isEqualTo(UPDATED_G);
        assertThat(testBranchExtension.getmRid()).isEqualTo(UPDATED_M_RID);
    }

    @Test
    @Transactional
    void putNonExistingBranchExtension() throws Exception {
        int databaseSizeBeforeUpdate = branchExtensionRepository.findAll().size();
        branchExtension.setId(count.incrementAndGet());

        // Create the BranchExtension
        BranchExtensionDTO branchExtensionDTO = branchExtensionMapper.toDto(branchExtension);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchExtensionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, branchExtensionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchExtensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchExtension in the database
        List<BranchExtension> branchExtensionList = branchExtensionRepository.findAll();
        assertThat(branchExtensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBranchExtension() throws Exception {
        int databaseSizeBeforeUpdate = branchExtensionRepository.findAll().size();
        branchExtension.setId(count.incrementAndGet());

        // Create the BranchExtension
        BranchExtensionDTO branchExtensionDTO = branchExtensionMapper.toDto(branchExtension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchExtensionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchExtensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchExtension in the database
        List<BranchExtension> branchExtensionList = branchExtensionRepository.findAll();
        assertThat(branchExtensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBranchExtension() throws Exception {
        int databaseSizeBeforeUpdate = branchExtensionRepository.findAll().size();
        branchExtension.setId(count.incrementAndGet());

        // Create the BranchExtension
        BranchExtensionDTO branchExtensionDTO = branchExtensionMapper.toDto(branchExtension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchExtensionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchExtensionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BranchExtension in the database
        List<BranchExtension> branchExtensionList = branchExtensionRepository.findAll();
        assertThat(branchExtensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBranchExtensionWithPatch() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        int databaseSizeBeforeUpdate = branchExtensionRepository.findAll().size();

        // Update the branchExtension using partial update
        BranchExtension partialUpdatedBranchExtension = new BranchExtension();
        partialUpdatedBranchExtension.setId(branchExtension.getId());

        partialUpdatedBranchExtension
            .stepSize(UPDATED_STEP_SIZE)
            .actTap(UPDATED_ACT_TAP)
            .minTap(UPDATED_MIN_TAP)
            .normalTap(UPDATED_NORMAL_TAP)
            .nominalRatio(UPDATED_NOMINAL_RATIO)
            .rIp(UPDATED_R_IP)
            .rN(UPDATED_R_N)
            .r0(UPDATED_R_0)
            .x0(UPDATED_X_0)
            .length(UPDATED_LENGTH)
            .g(UPDATED_G)
            .mRid(UPDATED_M_RID);

        restBranchExtensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBranchExtension.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBranchExtension))
            )
            .andExpect(status().isOk());

        // Validate the BranchExtension in the database
        List<BranchExtension> branchExtensionList = branchExtensionRepository.findAll();
        assertThat(branchExtensionList).hasSize(databaseSizeBeforeUpdate);
        BranchExtension testBranchExtension = branchExtensionList.get(branchExtensionList.size() - 1);
        assertThat(testBranchExtension.getStepSize()).isEqualTo(UPDATED_STEP_SIZE);
        assertThat(testBranchExtension.getActTap()).isEqualTo(UPDATED_ACT_TAP);
        assertThat(testBranchExtension.getMinTap()).isEqualTo(UPDATED_MIN_TAP);
        assertThat(testBranchExtension.getMaxTap()).isEqualTo(DEFAULT_MAX_TAP);
        assertThat(testBranchExtension.getNormalTap()).isEqualTo(UPDATED_NORMAL_TAP);
        assertThat(testBranchExtension.getNominalRatio()).isEqualTo(UPDATED_NOMINAL_RATIO);
        assertThat(testBranchExtension.getrIp()).isEqualTo(UPDATED_R_IP);
        assertThat(testBranchExtension.getrN()).isEqualTo(UPDATED_R_N);
        assertThat(testBranchExtension.getr0()).isEqualTo(UPDATED_R_0);
        assertThat(testBranchExtension.getx0()).isEqualTo(UPDATED_X_0);
        assertThat(testBranchExtension.getb0()).isEqualTo(DEFAULT_B_0);
        assertThat(testBranchExtension.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testBranchExtension.getNormStat()).isEqualTo(DEFAULT_NORM_STAT);
        assertThat(testBranchExtension.getG()).isEqualTo(UPDATED_G);
        assertThat(testBranchExtension.getmRid()).isEqualTo(UPDATED_M_RID);
    }

    @Test
    @Transactional
    void fullUpdateBranchExtensionWithPatch() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        int databaseSizeBeforeUpdate = branchExtensionRepository.findAll().size();

        // Update the branchExtension using partial update
        BranchExtension partialUpdatedBranchExtension = new BranchExtension();
        partialUpdatedBranchExtension.setId(branchExtension.getId());

        partialUpdatedBranchExtension
            .stepSize(UPDATED_STEP_SIZE)
            .actTap(UPDATED_ACT_TAP)
            .minTap(UPDATED_MIN_TAP)
            .maxTap(UPDATED_MAX_TAP)
            .normalTap(UPDATED_NORMAL_TAP)
            .nominalRatio(UPDATED_NOMINAL_RATIO)
            .rIp(UPDATED_R_IP)
            .rN(UPDATED_R_N)
            .r0(UPDATED_R_0)
            .x0(UPDATED_X_0)
            .b0(UPDATED_B_0)
            .length(UPDATED_LENGTH)
            .normStat(UPDATED_NORM_STAT)
            .g(UPDATED_G)
            .mRid(UPDATED_M_RID);

        restBranchExtensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBranchExtension.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBranchExtension))
            )
            .andExpect(status().isOk());

        // Validate the BranchExtension in the database
        List<BranchExtension> branchExtensionList = branchExtensionRepository.findAll();
        assertThat(branchExtensionList).hasSize(databaseSizeBeforeUpdate);
        BranchExtension testBranchExtension = branchExtensionList.get(branchExtensionList.size() - 1);
        assertThat(testBranchExtension.getStepSize()).isEqualTo(UPDATED_STEP_SIZE);
        assertThat(testBranchExtension.getActTap()).isEqualTo(UPDATED_ACT_TAP);
        assertThat(testBranchExtension.getMinTap()).isEqualTo(UPDATED_MIN_TAP);
        assertThat(testBranchExtension.getMaxTap()).isEqualTo(UPDATED_MAX_TAP);
        assertThat(testBranchExtension.getNormalTap()).isEqualTo(UPDATED_NORMAL_TAP);
        assertThat(testBranchExtension.getNominalRatio()).isEqualTo(UPDATED_NOMINAL_RATIO);
        assertThat(testBranchExtension.getrIp()).isEqualTo(UPDATED_R_IP);
        assertThat(testBranchExtension.getrN()).isEqualTo(UPDATED_R_N);
        assertThat(testBranchExtension.getr0()).isEqualTo(UPDATED_R_0);
        assertThat(testBranchExtension.getx0()).isEqualTo(UPDATED_X_0);
        assertThat(testBranchExtension.getb0()).isEqualTo(UPDATED_B_0);
        assertThat(testBranchExtension.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testBranchExtension.getNormStat()).isEqualTo(UPDATED_NORM_STAT);
        assertThat(testBranchExtension.getG()).isEqualTo(UPDATED_G);
        assertThat(testBranchExtension.getmRid()).isEqualTo(UPDATED_M_RID);
    }

    @Test
    @Transactional
    void patchNonExistingBranchExtension() throws Exception {
        int databaseSizeBeforeUpdate = branchExtensionRepository.findAll().size();
        branchExtension.setId(count.incrementAndGet());

        // Create the BranchExtension
        BranchExtensionDTO branchExtensionDTO = branchExtensionMapper.toDto(branchExtension);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchExtensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, branchExtensionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(branchExtensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchExtension in the database
        List<BranchExtension> branchExtensionList = branchExtensionRepository.findAll();
        assertThat(branchExtensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBranchExtension() throws Exception {
        int databaseSizeBeforeUpdate = branchExtensionRepository.findAll().size();
        branchExtension.setId(count.incrementAndGet());

        // Create the BranchExtension
        BranchExtensionDTO branchExtensionDTO = branchExtensionMapper.toDto(branchExtension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchExtensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(branchExtensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchExtension in the database
        List<BranchExtension> branchExtensionList = branchExtensionRepository.findAll();
        assertThat(branchExtensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBranchExtension() throws Exception {
        int databaseSizeBeforeUpdate = branchExtensionRepository.findAll().size();
        branchExtension.setId(count.incrementAndGet());

        // Create the BranchExtension
        BranchExtensionDTO branchExtensionDTO = branchExtensionMapper.toDto(branchExtension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchExtensionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(branchExtensionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BranchExtension in the database
        List<BranchExtension> branchExtensionList = branchExtensionRepository.findAll();
        assertThat(branchExtensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBranchExtension() throws Exception {
        // Initialize the database
        branchExtensionRepository.saveAndFlush(branchExtension);

        int databaseSizeBeforeDelete = branchExtensionRepository.findAll().size();

        // Delete the branchExtension
        restBranchExtensionMockMvc
            .perform(delete(ENTITY_API_URL_ID, branchExtension.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BranchExtension> branchExtensionList = branchExtensionRepository.findAll();
        assertThat(branchExtensionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
