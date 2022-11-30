package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Branch;
import com.attest.ict.domain.BranchElVal;
import com.attest.ict.domain.BranchExtension;
import com.attest.ict.domain.Network;
import com.attest.ict.domain.TransfElVal;
import com.attest.ict.repository.BranchRepository;
import com.attest.ict.service.criteria.BranchCriteria;
import com.attest.ict.service.dto.BranchDTO;
import com.attest.ict.service.mapper.BranchMapper;
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
 * Integration tests for the {@link BranchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BranchResourceIT {

    private static final Long DEFAULT_FBUS = 1L;
    private static final Long UPDATED_FBUS = 2L;
    private static final Long SMALLER_FBUS = 1L - 1L;

    private static final Long DEFAULT_TBUS = 1L;
    private static final Long UPDATED_TBUS = 2L;
    private static final Long SMALLER_TBUS = 1L - 1L;

    private static final Double DEFAULT_R = 1D;
    private static final Double UPDATED_R = 2D;
    private static final Double SMALLER_R = 1D - 1D;

    private static final Double DEFAULT_X = 1D;
    private static final Double UPDATED_X = 2D;
    private static final Double SMALLER_X = 1D - 1D;

    private static final Double DEFAULT_B = 1D;
    private static final Double UPDATED_B = 2D;
    private static final Double SMALLER_B = 1D - 1D;

    private static final Double DEFAULT_RATEA = 1D;
    private static final Double UPDATED_RATEA = 2D;
    private static final Double SMALLER_RATEA = 1D - 1D;

    private static final Double DEFAULT_RATEB = 1D;
    private static final Double UPDATED_RATEB = 2D;
    private static final Double SMALLER_RATEB = 1D - 1D;

    private static final Double DEFAULT_RATEC = 1D;
    private static final Double UPDATED_RATEC = 2D;
    private static final Double SMALLER_RATEC = 1D - 1D;

    private static final Double DEFAULT_TAP_RATIO = 1D;
    private static final Double UPDATED_TAP_RATIO = 2D;
    private static final Double SMALLER_TAP_RATIO = 1D - 1D;

    private static final Double DEFAULT_ANGLE = 1D;
    private static final Double UPDATED_ANGLE = 2D;
    private static final Double SMALLER_ANGLE = 1D - 1D;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;
    private static final Integer SMALLER_STATUS = 1 - 1;

    private static final Integer DEFAULT_ANGMIN = 1;
    private static final Integer UPDATED_ANGMIN = 2;
    private static final Integer SMALLER_ANGMIN = 1 - 1;

    private static final Integer DEFAULT_ANGMAX = 1;
    private static final Integer UPDATED_ANGMAX = 2;
    private static final Integer SMALLER_ANGMAX = 1 - 1;

    private static final String ENTITY_API_URL = "/api/branches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BranchMapper branchMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBranchMockMvc;

    private Branch branch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Branch createEntity(EntityManager em) {
        Branch branch = new Branch()
            .fbus(DEFAULT_FBUS)
            .tbus(DEFAULT_TBUS)
            .r(DEFAULT_R)
            .x(DEFAULT_X)
            .b(DEFAULT_B)
            .ratea(DEFAULT_RATEA)
            .rateb(DEFAULT_RATEB)
            .ratec(DEFAULT_RATEC)
            .tapRatio(DEFAULT_TAP_RATIO)
            .angle(DEFAULT_ANGLE)
            .status(DEFAULT_STATUS)
            .angmin(DEFAULT_ANGMIN)
            .angmax(DEFAULT_ANGMAX);
        return branch;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Branch createUpdatedEntity(EntityManager em) {
        Branch branch = new Branch()
            .fbus(UPDATED_FBUS)
            .tbus(UPDATED_TBUS)
            .r(UPDATED_R)
            .x(UPDATED_X)
            .b(UPDATED_B)
            .ratea(UPDATED_RATEA)
            .rateb(UPDATED_RATEB)
            .ratec(UPDATED_RATEC)
            .tapRatio(UPDATED_TAP_RATIO)
            .angle(UPDATED_ANGLE)
            .status(UPDATED_STATUS)
            .angmin(UPDATED_ANGMIN)
            .angmax(UPDATED_ANGMAX);
        return branch;
    }

    @BeforeEach
    public void initTest() {
        branch = createEntity(em);
    }

    @Test
    @Transactional
    void createBranch() throws Exception {
        int databaseSizeBeforeCreate = branchRepository.findAll().size();
        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);
        restBranchMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeCreate + 1);
        Branch testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getFbus()).isEqualTo(DEFAULT_FBUS);
        assertThat(testBranch.getTbus()).isEqualTo(DEFAULT_TBUS);
        assertThat(testBranch.getR()).isEqualTo(DEFAULT_R);
        assertThat(testBranch.getX()).isEqualTo(DEFAULT_X);
        assertThat(testBranch.getB()).isEqualTo(DEFAULT_B);
        assertThat(testBranch.getRatea()).isEqualTo(DEFAULT_RATEA);
        assertThat(testBranch.getRateb()).isEqualTo(DEFAULT_RATEB);
        assertThat(testBranch.getRatec()).isEqualTo(DEFAULT_RATEC);
        assertThat(testBranch.getTapRatio()).isEqualTo(DEFAULT_TAP_RATIO);
        assertThat(testBranch.getAngle()).isEqualTo(DEFAULT_ANGLE);
        assertThat(testBranch.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBranch.getAngmin()).isEqualTo(DEFAULT_ANGMIN);
        assertThat(testBranch.getAngmax()).isEqualTo(DEFAULT_ANGMAX);
    }

    @Test
    @Transactional
    void createBranchWithExistingId() throws Exception {
        // Create the Branch with an existing ID
        branch.setId(1L);
        BranchDTO branchDTO = branchMapper.toDto(branch);

        int databaseSizeBeforeCreate = branchRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBranchMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBranches() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList
        restBranchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branch.getId().intValue())))
            .andExpect(jsonPath("$.[*].fbus").value(hasItem(DEFAULT_FBUS.intValue())))
            .andExpect(jsonPath("$.[*].tbus").value(hasItem(DEFAULT_TBUS.intValue())))
            .andExpect(jsonPath("$.[*].r").value(hasItem(DEFAULT_R.doubleValue())))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X.doubleValue())))
            .andExpect(jsonPath("$.[*].b").value(hasItem(DEFAULT_B.doubleValue())))
            .andExpect(jsonPath("$.[*].ratea").value(hasItem(DEFAULT_RATEA.doubleValue())))
            .andExpect(jsonPath("$.[*].rateb").value(hasItem(DEFAULT_RATEB.doubleValue())))
            .andExpect(jsonPath("$.[*].ratec").value(hasItem(DEFAULT_RATEC.doubleValue())))
            .andExpect(jsonPath("$.[*].tapRatio").value(hasItem(DEFAULT_TAP_RATIO.doubleValue())))
            .andExpect(jsonPath("$.[*].angle").value(hasItem(DEFAULT_ANGLE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].angmin").value(hasItem(DEFAULT_ANGMIN)))
            .andExpect(jsonPath("$.[*].angmax").value(hasItem(DEFAULT_ANGMAX)));
    }

    @Test
    @Transactional
    void getBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get the branch
        restBranchMockMvc
            .perform(get(ENTITY_API_URL_ID, branch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(branch.getId().intValue()))
            .andExpect(jsonPath("$.fbus").value(DEFAULT_FBUS.intValue()))
            .andExpect(jsonPath("$.tbus").value(DEFAULT_TBUS.intValue()))
            .andExpect(jsonPath("$.r").value(DEFAULT_R.doubleValue()))
            .andExpect(jsonPath("$.x").value(DEFAULT_X.doubleValue()))
            .andExpect(jsonPath("$.b").value(DEFAULT_B.doubleValue()))
            .andExpect(jsonPath("$.ratea").value(DEFAULT_RATEA.doubleValue()))
            .andExpect(jsonPath("$.rateb").value(DEFAULT_RATEB.doubleValue()))
            .andExpect(jsonPath("$.ratec").value(DEFAULT_RATEC.doubleValue()))
            .andExpect(jsonPath("$.tapRatio").value(DEFAULT_TAP_RATIO.doubleValue()))
            .andExpect(jsonPath("$.angle").value(DEFAULT_ANGLE.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.angmin").value(DEFAULT_ANGMIN))
            .andExpect(jsonPath("$.angmax").value(DEFAULT_ANGMAX));
    }

    @Test
    @Transactional
    void getBranchesByIdFiltering() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        Long id = branch.getId();

        defaultBranchShouldBeFound("id.equals=" + id);
        defaultBranchShouldNotBeFound("id.notEquals=" + id);

        defaultBranchShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBranchShouldNotBeFound("id.greaterThan=" + id);

        defaultBranchShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBranchShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBranchesByFbusIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where fbus equals to DEFAULT_FBUS
        defaultBranchShouldBeFound("fbus.equals=" + DEFAULT_FBUS);

        // Get all the branchList where fbus equals to UPDATED_FBUS
        defaultBranchShouldNotBeFound("fbus.equals=" + UPDATED_FBUS);
    }

    @Test
    @Transactional
    void getAllBranchesByFbusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where fbus not equals to DEFAULT_FBUS
        defaultBranchShouldNotBeFound("fbus.notEquals=" + DEFAULT_FBUS);

        // Get all the branchList where fbus not equals to UPDATED_FBUS
        defaultBranchShouldBeFound("fbus.notEquals=" + UPDATED_FBUS);
    }

    @Test
    @Transactional
    void getAllBranchesByFbusIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where fbus in DEFAULT_FBUS or UPDATED_FBUS
        defaultBranchShouldBeFound("fbus.in=" + DEFAULT_FBUS + "," + UPDATED_FBUS);

        // Get all the branchList where fbus equals to UPDATED_FBUS
        defaultBranchShouldNotBeFound("fbus.in=" + UPDATED_FBUS);
    }

    @Test
    @Transactional
    void getAllBranchesByFbusIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where fbus is not null
        defaultBranchShouldBeFound("fbus.specified=true");

        // Get all the branchList where fbus is null
        defaultBranchShouldNotBeFound("fbus.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByFbusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where fbus is greater than or equal to DEFAULT_FBUS
        defaultBranchShouldBeFound("fbus.greaterThanOrEqual=" + DEFAULT_FBUS);

        // Get all the branchList where fbus is greater than or equal to UPDATED_FBUS
        defaultBranchShouldNotBeFound("fbus.greaterThanOrEqual=" + UPDATED_FBUS);
    }

    @Test
    @Transactional
    void getAllBranchesByFbusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where fbus is less than or equal to DEFAULT_FBUS
        defaultBranchShouldBeFound("fbus.lessThanOrEqual=" + DEFAULT_FBUS);

        // Get all the branchList where fbus is less than or equal to SMALLER_FBUS
        defaultBranchShouldNotBeFound("fbus.lessThanOrEqual=" + SMALLER_FBUS);
    }

    @Test
    @Transactional
    void getAllBranchesByFbusIsLessThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where fbus is less than DEFAULT_FBUS
        defaultBranchShouldNotBeFound("fbus.lessThan=" + DEFAULT_FBUS);

        // Get all the branchList where fbus is less than UPDATED_FBUS
        defaultBranchShouldBeFound("fbus.lessThan=" + UPDATED_FBUS);
    }

    @Test
    @Transactional
    void getAllBranchesByFbusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where fbus is greater than DEFAULT_FBUS
        defaultBranchShouldNotBeFound("fbus.greaterThan=" + DEFAULT_FBUS);

        // Get all the branchList where fbus is greater than SMALLER_FBUS
        defaultBranchShouldBeFound("fbus.greaterThan=" + SMALLER_FBUS);
    }

    @Test
    @Transactional
    void getAllBranchesByTbusIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where tbus equals to DEFAULT_TBUS
        defaultBranchShouldBeFound("tbus.equals=" + DEFAULT_TBUS);

        // Get all the branchList where tbus equals to UPDATED_TBUS
        defaultBranchShouldNotBeFound("tbus.equals=" + UPDATED_TBUS);
    }

    @Test
    @Transactional
    void getAllBranchesByTbusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where tbus not equals to DEFAULT_TBUS
        defaultBranchShouldNotBeFound("tbus.notEquals=" + DEFAULT_TBUS);

        // Get all the branchList where tbus not equals to UPDATED_TBUS
        defaultBranchShouldBeFound("tbus.notEquals=" + UPDATED_TBUS);
    }

    @Test
    @Transactional
    void getAllBranchesByTbusIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where tbus in DEFAULT_TBUS or UPDATED_TBUS
        defaultBranchShouldBeFound("tbus.in=" + DEFAULT_TBUS + "," + UPDATED_TBUS);

        // Get all the branchList where tbus equals to UPDATED_TBUS
        defaultBranchShouldNotBeFound("tbus.in=" + UPDATED_TBUS);
    }

    @Test
    @Transactional
    void getAllBranchesByTbusIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where tbus is not null
        defaultBranchShouldBeFound("tbus.specified=true");

        // Get all the branchList where tbus is null
        defaultBranchShouldNotBeFound("tbus.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByTbusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where tbus is greater than or equal to DEFAULT_TBUS
        defaultBranchShouldBeFound("tbus.greaterThanOrEqual=" + DEFAULT_TBUS);

        // Get all the branchList where tbus is greater than or equal to UPDATED_TBUS
        defaultBranchShouldNotBeFound("tbus.greaterThanOrEqual=" + UPDATED_TBUS);
    }

    @Test
    @Transactional
    void getAllBranchesByTbusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where tbus is less than or equal to DEFAULT_TBUS
        defaultBranchShouldBeFound("tbus.lessThanOrEqual=" + DEFAULT_TBUS);

        // Get all the branchList where tbus is less than or equal to SMALLER_TBUS
        defaultBranchShouldNotBeFound("tbus.lessThanOrEqual=" + SMALLER_TBUS);
    }

    @Test
    @Transactional
    void getAllBranchesByTbusIsLessThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where tbus is less than DEFAULT_TBUS
        defaultBranchShouldNotBeFound("tbus.lessThan=" + DEFAULT_TBUS);

        // Get all the branchList where tbus is less than UPDATED_TBUS
        defaultBranchShouldBeFound("tbus.lessThan=" + UPDATED_TBUS);
    }

    @Test
    @Transactional
    void getAllBranchesByTbusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where tbus is greater than DEFAULT_TBUS
        defaultBranchShouldNotBeFound("tbus.greaterThan=" + DEFAULT_TBUS);

        // Get all the branchList where tbus is greater than SMALLER_TBUS
        defaultBranchShouldBeFound("tbus.greaterThan=" + SMALLER_TBUS);
    }

    @Test
    @Transactional
    void getAllBranchesByRIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where r equals to DEFAULT_R
        defaultBranchShouldBeFound("r.equals=" + DEFAULT_R);

        // Get all the branchList where r equals to UPDATED_R
        defaultBranchShouldNotBeFound("r.equals=" + UPDATED_R);
    }

    @Test
    @Transactional
    void getAllBranchesByRIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where r not equals to DEFAULT_R
        defaultBranchShouldNotBeFound("r.notEquals=" + DEFAULT_R);

        // Get all the branchList where r not equals to UPDATED_R
        defaultBranchShouldBeFound("r.notEquals=" + UPDATED_R);
    }

    @Test
    @Transactional
    void getAllBranchesByRIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where r in DEFAULT_R or UPDATED_R
        defaultBranchShouldBeFound("r.in=" + DEFAULT_R + "," + UPDATED_R);

        // Get all the branchList where r equals to UPDATED_R
        defaultBranchShouldNotBeFound("r.in=" + UPDATED_R);
    }

    @Test
    @Transactional
    void getAllBranchesByRIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where r is not null
        defaultBranchShouldBeFound("r.specified=true");

        // Get all the branchList where r is null
        defaultBranchShouldNotBeFound("r.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where r is greater than or equal to DEFAULT_R
        defaultBranchShouldBeFound("r.greaterThanOrEqual=" + DEFAULT_R);

        // Get all the branchList where r is greater than or equal to UPDATED_R
        defaultBranchShouldNotBeFound("r.greaterThanOrEqual=" + UPDATED_R);
    }

    @Test
    @Transactional
    void getAllBranchesByRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where r is less than or equal to DEFAULT_R
        defaultBranchShouldBeFound("r.lessThanOrEqual=" + DEFAULT_R);

        // Get all the branchList where r is less than or equal to SMALLER_R
        defaultBranchShouldNotBeFound("r.lessThanOrEqual=" + SMALLER_R);
    }

    @Test
    @Transactional
    void getAllBranchesByRIsLessThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where r is less than DEFAULT_R
        defaultBranchShouldNotBeFound("r.lessThan=" + DEFAULT_R);

        // Get all the branchList where r is less than UPDATED_R
        defaultBranchShouldBeFound("r.lessThan=" + UPDATED_R);
    }

    @Test
    @Transactional
    void getAllBranchesByRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where r is greater than DEFAULT_R
        defaultBranchShouldNotBeFound("r.greaterThan=" + DEFAULT_R);

        // Get all the branchList where r is greater than SMALLER_R
        defaultBranchShouldBeFound("r.greaterThan=" + SMALLER_R);
    }

    @Test
    @Transactional
    void getAllBranchesByXIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where x equals to DEFAULT_X
        defaultBranchShouldBeFound("x.equals=" + DEFAULT_X);

        // Get all the branchList where x equals to UPDATED_X
        defaultBranchShouldNotBeFound("x.equals=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllBranchesByXIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where x not equals to DEFAULT_X
        defaultBranchShouldNotBeFound("x.notEquals=" + DEFAULT_X);

        // Get all the branchList where x not equals to UPDATED_X
        defaultBranchShouldBeFound("x.notEquals=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllBranchesByXIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where x in DEFAULT_X or UPDATED_X
        defaultBranchShouldBeFound("x.in=" + DEFAULT_X + "," + UPDATED_X);

        // Get all the branchList where x equals to UPDATED_X
        defaultBranchShouldNotBeFound("x.in=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllBranchesByXIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where x is not null
        defaultBranchShouldBeFound("x.specified=true");

        // Get all the branchList where x is null
        defaultBranchShouldNotBeFound("x.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByXIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where x is greater than or equal to DEFAULT_X
        defaultBranchShouldBeFound("x.greaterThanOrEqual=" + DEFAULT_X);

        // Get all the branchList where x is greater than or equal to UPDATED_X
        defaultBranchShouldNotBeFound("x.greaterThanOrEqual=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllBranchesByXIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where x is less than or equal to DEFAULT_X
        defaultBranchShouldBeFound("x.lessThanOrEqual=" + DEFAULT_X);

        // Get all the branchList where x is less than or equal to SMALLER_X
        defaultBranchShouldNotBeFound("x.lessThanOrEqual=" + SMALLER_X);
    }

    @Test
    @Transactional
    void getAllBranchesByXIsLessThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where x is less than DEFAULT_X
        defaultBranchShouldNotBeFound("x.lessThan=" + DEFAULT_X);

        // Get all the branchList where x is less than UPDATED_X
        defaultBranchShouldBeFound("x.lessThan=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllBranchesByXIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where x is greater than DEFAULT_X
        defaultBranchShouldNotBeFound("x.greaterThan=" + DEFAULT_X);

        // Get all the branchList where x is greater than SMALLER_X
        defaultBranchShouldBeFound("x.greaterThan=" + SMALLER_X);
    }

    @Test
    @Transactional
    void getAllBranchesByBIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where b equals to DEFAULT_B
        defaultBranchShouldBeFound("b.equals=" + DEFAULT_B);

        // Get all the branchList where b equals to UPDATED_B
        defaultBranchShouldNotBeFound("b.equals=" + UPDATED_B);
    }

    @Test
    @Transactional
    void getAllBranchesByBIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where b not equals to DEFAULT_B
        defaultBranchShouldNotBeFound("b.notEquals=" + DEFAULT_B);

        // Get all the branchList where b not equals to UPDATED_B
        defaultBranchShouldBeFound("b.notEquals=" + UPDATED_B);
    }

    @Test
    @Transactional
    void getAllBranchesByBIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where b in DEFAULT_B or UPDATED_B
        defaultBranchShouldBeFound("b.in=" + DEFAULT_B + "," + UPDATED_B);

        // Get all the branchList where b equals to UPDATED_B
        defaultBranchShouldNotBeFound("b.in=" + UPDATED_B);
    }

    @Test
    @Transactional
    void getAllBranchesByBIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where b is not null
        defaultBranchShouldBeFound("b.specified=true");

        // Get all the branchList where b is null
        defaultBranchShouldNotBeFound("b.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByBIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where b is greater than or equal to DEFAULT_B
        defaultBranchShouldBeFound("b.greaterThanOrEqual=" + DEFAULT_B);

        // Get all the branchList where b is greater than or equal to UPDATED_B
        defaultBranchShouldNotBeFound("b.greaterThanOrEqual=" + UPDATED_B);
    }

    @Test
    @Transactional
    void getAllBranchesByBIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where b is less than or equal to DEFAULT_B
        defaultBranchShouldBeFound("b.lessThanOrEqual=" + DEFAULT_B);

        // Get all the branchList where b is less than or equal to SMALLER_B
        defaultBranchShouldNotBeFound("b.lessThanOrEqual=" + SMALLER_B);
    }

    @Test
    @Transactional
    void getAllBranchesByBIsLessThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where b is less than DEFAULT_B
        defaultBranchShouldNotBeFound("b.lessThan=" + DEFAULT_B);

        // Get all the branchList where b is less than UPDATED_B
        defaultBranchShouldBeFound("b.lessThan=" + UPDATED_B);
    }

    @Test
    @Transactional
    void getAllBranchesByBIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where b is greater than DEFAULT_B
        defaultBranchShouldNotBeFound("b.greaterThan=" + DEFAULT_B);

        // Get all the branchList where b is greater than SMALLER_B
        defaultBranchShouldBeFound("b.greaterThan=" + SMALLER_B);
    }

    @Test
    @Transactional
    void getAllBranchesByRateaIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where ratea equals to DEFAULT_RATEA
        defaultBranchShouldBeFound("ratea.equals=" + DEFAULT_RATEA);

        // Get all the branchList where ratea equals to UPDATED_RATEA
        defaultBranchShouldNotBeFound("ratea.equals=" + UPDATED_RATEA);
    }

    @Test
    @Transactional
    void getAllBranchesByRateaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where ratea not equals to DEFAULT_RATEA
        defaultBranchShouldNotBeFound("ratea.notEquals=" + DEFAULT_RATEA);

        // Get all the branchList where ratea not equals to UPDATED_RATEA
        defaultBranchShouldBeFound("ratea.notEquals=" + UPDATED_RATEA);
    }

    @Test
    @Transactional
    void getAllBranchesByRateaIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where ratea in DEFAULT_RATEA or UPDATED_RATEA
        defaultBranchShouldBeFound("ratea.in=" + DEFAULT_RATEA + "," + UPDATED_RATEA);

        // Get all the branchList where ratea equals to UPDATED_RATEA
        defaultBranchShouldNotBeFound("ratea.in=" + UPDATED_RATEA);
    }

    @Test
    @Transactional
    void getAllBranchesByRateaIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where ratea is not null
        defaultBranchShouldBeFound("ratea.specified=true");

        // Get all the branchList where ratea is null
        defaultBranchShouldNotBeFound("ratea.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByRateaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where ratea is greater than or equal to DEFAULT_RATEA
        defaultBranchShouldBeFound("ratea.greaterThanOrEqual=" + DEFAULT_RATEA);

        // Get all the branchList where ratea is greater than or equal to UPDATED_RATEA
        defaultBranchShouldNotBeFound("ratea.greaterThanOrEqual=" + UPDATED_RATEA);
    }

    @Test
    @Transactional
    void getAllBranchesByRateaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where ratea is less than or equal to DEFAULT_RATEA
        defaultBranchShouldBeFound("ratea.lessThanOrEqual=" + DEFAULT_RATEA);

        // Get all the branchList where ratea is less than or equal to SMALLER_RATEA
        defaultBranchShouldNotBeFound("ratea.lessThanOrEqual=" + SMALLER_RATEA);
    }

    @Test
    @Transactional
    void getAllBranchesByRateaIsLessThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where ratea is less than DEFAULT_RATEA
        defaultBranchShouldNotBeFound("ratea.lessThan=" + DEFAULT_RATEA);

        // Get all the branchList where ratea is less than UPDATED_RATEA
        defaultBranchShouldBeFound("ratea.lessThan=" + UPDATED_RATEA);
    }

    @Test
    @Transactional
    void getAllBranchesByRateaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where ratea is greater than DEFAULT_RATEA
        defaultBranchShouldNotBeFound("ratea.greaterThan=" + DEFAULT_RATEA);

        // Get all the branchList where ratea is greater than SMALLER_RATEA
        defaultBranchShouldBeFound("ratea.greaterThan=" + SMALLER_RATEA);
    }

    @Test
    @Transactional
    void getAllBranchesByRatebIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where rateb equals to DEFAULT_RATEB
        defaultBranchShouldBeFound("rateb.equals=" + DEFAULT_RATEB);

        // Get all the branchList where rateb equals to UPDATED_RATEB
        defaultBranchShouldNotBeFound("rateb.equals=" + UPDATED_RATEB);
    }

    @Test
    @Transactional
    void getAllBranchesByRatebIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where rateb not equals to DEFAULT_RATEB
        defaultBranchShouldNotBeFound("rateb.notEquals=" + DEFAULT_RATEB);

        // Get all the branchList where rateb not equals to UPDATED_RATEB
        defaultBranchShouldBeFound("rateb.notEquals=" + UPDATED_RATEB);
    }

    @Test
    @Transactional
    void getAllBranchesByRatebIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where rateb in DEFAULT_RATEB or UPDATED_RATEB
        defaultBranchShouldBeFound("rateb.in=" + DEFAULT_RATEB + "," + UPDATED_RATEB);

        // Get all the branchList where rateb equals to UPDATED_RATEB
        defaultBranchShouldNotBeFound("rateb.in=" + UPDATED_RATEB);
    }

    @Test
    @Transactional
    void getAllBranchesByRatebIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where rateb is not null
        defaultBranchShouldBeFound("rateb.specified=true");

        // Get all the branchList where rateb is null
        defaultBranchShouldNotBeFound("rateb.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByRatebIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where rateb is greater than or equal to DEFAULT_RATEB
        defaultBranchShouldBeFound("rateb.greaterThanOrEqual=" + DEFAULT_RATEB);

        // Get all the branchList where rateb is greater than or equal to UPDATED_RATEB
        defaultBranchShouldNotBeFound("rateb.greaterThanOrEqual=" + UPDATED_RATEB);
    }

    @Test
    @Transactional
    void getAllBranchesByRatebIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where rateb is less than or equal to DEFAULT_RATEB
        defaultBranchShouldBeFound("rateb.lessThanOrEqual=" + DEFAULT_RATEB);

        // Get all the branchList where rateb is less than or equal to SMALLER_RATEB
        defaultBranchShouldNotBeFound("rateb.lessThanOrEqual=" + SMALLER_RATEB);
    }

    @Test
    @Transactional
    void getAllBranchesByRatebIsLessThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where rateb is less than DEFAULT_RATEB
        defaultBranchShouldNotBeFound("rateb.lessThan=" + DEFAULT_RATEB);

        // Get all the branchList where rateb is less than UPDATED_RATEB
        defaultBranchShouldBeFound("rateb.lessThan=" + UPDATED_RATEB);
    }

    @Test
    @Transactional
    void getAllBranchesByRatebIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where rateb is greater than DEFAULT_RATEB
        defaultBranchShouldNotBeFound("rateb.greaterThan=" + DEFAULT_RATEB);

        // Get all the branchList where rateb is greater than SMALLER_RATEB
        defaultBranchShouldBeFound("rateb.greaterThan=" + SMALLER_RATEB);
    }

    @Test
    @Transactional
    void getAllBranchesByRatecIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where ratec equals to DEFAULT_RATEC
        defaultBranchShouldBeFound("ratec.equals=" + DEFAULT_RATEC);

        // Get all the branchList where ratec equals to UPDATED_RATEC
        defaultBranchShouldNotBeFound("ratec.equals=" + UPDATED_RATEC);
    }

    @Test
    @Transactional
    void getAllBranchesByRatecIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where ratec not equals to DEFAULT_RATEC
        defaultBranchShouldNotBeFound("ratec.notEquals=" + DEFAULT_RATEC);

        // Get all the branchList where ratec not equals to UPDATED_RATEC
        defaultBranchShouldBeFound("ratec.notEquals=" + UPDATED_RATEC);
    }

    @Test
    @Transactional
    void getAllBranchesByRatecIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where ratec in DEFAULT_RATEC or UPDATED_RATEC
        defaultBranchShouldBeFound("ratec.in=" + DEFAULT_RATEC + "," + UPDATED_RATEC);

        // Get all the branchList where ratec equals to UPDATED_RATEC
        defaultBranchShouldNotBeFound("ratec.in=" + UPDATED_RATEC);
    }

    @Test
    @Transactional
    void getAllBranchesByRatecIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where ratec is not null
        defaultBranchShouldBeFound("ratec.specified=true");

        // Get all the branchList where ratec is null
        defaultBranchShouldNotBeFound("ratec.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByRatecIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where ratec is greater than or equal to DEFAULT_RATEC
        defaultBranchShouldBeFound("ratec.greaterThanOrEqual=" + DEFAULT_RATEC);

        // Get all the branchList where ratec is greater than or equal to UPDATED_RATEC
        defaultBranchShouldNotBeFound("ratec.greaterThanOrEqual=" + UPDATED_RATEC);
    }

    @Test
    @Transactional
    void getAllBranchesByRatecIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where ratec is less than or equal to DEFAULT_RATEC
        defaultBranchShouldBeFound("ratec.lessThanOrEqual=" + DEFAULT_RATEC);

        // Get all the branchList where ratec is less than or equal to SMALLER_RATEC
        defaultBranchShouldNotBeFound("ratec.lessThanOrEqual=" + SMALLER_RATEC);
    }

    @Test
    @Transactional
    void getAllBranchesByRatecIsLessThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where ratec is less than DEFAULT_RATEC
        defaultBranchShouldNotBeFound("ratec.lessThan=" + DEFAULT_RATEC);

        // Get all the branchList where ratec is less than UPDATED_RATEC
        defaultBranchShouldBeFound("ratec.lessThan=" + UPDATED_RATEC);
    }

    @Test
    @Transactional
    void getAllBranchesByRatecIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where ratec is greater than DEFAULT_RATEC
        defaultBranchShouldNotBeFound("ratec.greaterThan=" + DEFAULT_RATEC);

        // Get all the branchList where ratec is greater than SMALLER_RATEC
        defaultBranchShouldBeFound("ratec.greaterThan=" + SMALLER_RATEC);
    }

    @Test
    @Transactional
    void getAllBranchesByTapRatioIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where tapRatio equals to DEFAULT_TAP_RATIO
        defaultBranchShouldBeFound("tapRatio.equals=" + DEFAULT_TAP_RATIO);

        // Get all the branchList where tapRatio equals to UPDATED_TAP_RATIO
        defaultBranchShouldNotBeFound("tapRatio.equals=" + UPDATED_TAP_RATIO);
    }

    @Test
    @Transactional
    void getAllBranchesByTapRatioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where tapRatio not equals to DEFAULT_TAP_RATIO
        defaultBranchShouldNotBeFound("tapRatio.notEquals=" + DEFAULT_TAP_RATIO);

        // Get all the branchList where tapRatio not equals to UPDATED_TAP_RATIO
        defaultBranchShouldBeFound("tapRatio.notEquals=" + UPDATED_TAP_RATIO);
    }

    @Test
    @Transactional
    void getAllBranchesByTapRatioIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where tapRatio in DEFAULT_TAP_RATIO or UPDATED_TAP_RATIO
        defaultBranchShouldBeFound("tapRatio.in=" + DEFAULT_TAP_RATIO + "," + UPDATED_TAP_RATIO);

        // Get all the branchList where tapRatio equals to UPDATED_TAP_RATIO
        defaultBranchShouldNotBeFound("tapRatio.in=" + UPDATED_TAP_RATIO);
    }

    @Test
    @Transactional
    void getAllBranchesByTapRatioIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where tapRatio is not null
        defaultBranchShouldBeFound("tapRatio.specified=true");

        // Get all the branchList where tapRatio is null
        defaultBranchShouldNotBeFound("tapRatio.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByTapRatioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where tapRatio is greater than or equal to DEFAULT_TAP_RATIO
        defaultBranchShouldBeFound("tapRatio.greaterThanOrEqual=" + DEFAULT_TAP_RATIO);

        // Get all the branchList where tapRatio is greater than or equal to UPDATED_TAP_RATIO
        defaultBranchShouldNotBeFound("tapRatio.greaterThanOrEqual=" + UPDATED_TAP_RATIO);
    }

    @Test
    @Transactional
    void getAllBranchesByTapRatioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where tapRatio is less than or equal to DEFAULT_TAP_RATIO
        defaultBranchShouldBeFound("tapRatio.lessThanOrEqual=" + DEFAULT_TAP_RATIO);

        // Get all the branchList where tapRatio is less than or equal to SMALLER_TAP_RATIO
        defaultBranchShouldNotBeFound("tapRatio.lessThanOrEqual=" + SMALLER_TAP_RATIO);
    }

    @Test
    @Transactional
    void getAllBranchesByTapRatioIsLessThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where tapRatio is less than DEFAULT_TAP_RATIO
        defaultBranchShouldNotBeFound("tapRatio.lessThan=" + DEFAULT_TAP_RATIO);

        // Get all the branchList where tapRatio is less than UPDATED_TAP_RATIO
        defaultBranchShouldBeFound("tapRatio.lessThan=" + UPDATED_TAP_RATIO);
    }

    @Test
    @Transactional
    void getAllBranchesByTapRatioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where tapRatio is greater than DEFAULT_TAP_RATIO
        defaultBranchShouldNotBeFound("tapRatio.greaterThan=" + DEFAULT_TAP_RATIO);

        // Get all the branchList where tapRatio is greater than SMALLER_TAP_RATIO
        defaultBranchShouldBeFound("tapRatio.greaterThan=" + SMALLER_TAP_RATIO);
    }

    @Test
    @Transactional
    void getAllBranchesByAngleIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angle equals to DEFAULT_ANGLE
        defaultBranchShouldBeFound("angle.equals=" + DEFAULT_ANGLE);

        // Get all the branchList where angle equals to UPDATED_ANGLE
        defaultBranchShouldNotBeFound("angle.equals=" + UPDATED_ANGLE);
    }

    @Test
    @Transactional
    void getAllBranchesByAngleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angle not equals to DEFAULT_ANGLE
        defaultBranchShouldNotBeFound("angle.notEquals=" + DEFAULT_ANGLE);

        // Get all the branchList where angle not equals to UPDATED_ANGLE
        defaultBranchShouldBeFound("angle.notEquals=" + UPDATED_ANGLE);
    }

    @Test
    @Transactional
    void getAllBranchesByAngleIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angle in DEFAULT_ANGLE or UPDATED_ANGLE
        defaultBranchShouldBeFound("angle.in=" + DEFAULT_ANGLE + "," + UPDATED_ANGLE);

        // Get all the branchList where angle equals to UPDATED_ANGLE
        defaultBranchShouldNotBeFound("angle.in=" + UPDATED_ANGLE);
    }

    @Test
    @Transactional
    void getAllBranchesByAngleIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angle is not null
        defaultBranchShouldBeFound("angle.specified=true");

        // Get all the branchList where angle is null
        defaultBranchShouldNotBeFound("angle.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByAngleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angle is greater than or equal to DEFAULT_ANGLE
        defaultBranchShouldBeFound("angle.greaterThanOrEqual=" + DEFAULT_ANGLE);

        // Get all the branchList where angle is greater than or equal to UPDATED_ANGLE
        defaultBranchShouldNotBeFound("angle.greaterThanOrEqual=" + UPDATED_ANGLE);
    }

    @Test
    @Transactional
    void getAllBranchesByAngleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angle is less than or equal to DEFAULT_ANGLE
        defaultBranchShouldBeFound("angle.lessThanOrEqual=" + DEFAULT_ANGLE);

        // Get all the branchList where angle is less than or equal to SMALLER_ANGLE
        defaultBranchShouldNotBeFound("angle.lessThanOrEqual=" + SMALLER_ANGLE);
    }

    @Test
    @Transactional
    void getAllBranchesByAngleIsLessThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angle is less than DEFAULT_ANGLE
        defaultBranchShouldNotBeFound("angle.lessThan=" + DEFAULT_ANGLE);

        // Get all the branchList where angle is less than UPDATED_ANGLE
        defaultBranchShouldBeFound("angle.lessThan=" + UPDATED_ANGLE);
    }

    @Test
    @Transactional
    void getAllBranchesByAngleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angle is greater than DEFAULT_ANGLE
        defaultBranchShouldNotBeFound("angle.greaterThan=" + DEFAULT_ANGLE);

        // Get all the branchList where angle is greater than SMALLER_ANGLE
        defaultBranchShouldBeFound("angle.greaterThan=" + SMALLER_ANGLE);
    }

    @Test
    @Transactional
    void getAllBranchesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where status equals to DEFAULT_STATUS
        defaultBranchShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the branchList where status equals to UPDATED_STATUS
        defaultBranchShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBranchesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where status not equals to DEFAULT_STATUS
        defaultBranchShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the branchList where status not equals to UPDATED_STATUS
        defaultBranchShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBranchesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultBranchShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the branchList where status equals to UPDATED_STATUS
        defaultBranchShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBranchesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where status is not null
        defaultBranchShouldBeFound("status.specified=true");

        // Get all the branchList where status is null
        defaultBranchShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where status is greater than or equal to DEFAULT_STATUS
        defaultBranchShouldBeFound("status.greaterThanOrEqual=" + DEFAULT_STATUS);

        // Get all the branchList where status is greater than or equal to UPDATED_STATUS
        defaultBranchShouldNotBeFound("status.greaterThanOrEqual=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBranchesByStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where status is less than or equal to DEFAULT_STATUS
        defaultBranchShouldBeFound("status.lessThanOrEqual=" + DEFAULT_STATUS);

        // Get all the branchList where status is less than or equal to SMALLER_STATUS
        defaultBranchShouldNotBeFound("status.lessThanOrEqual=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllBranchesByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where status is less than DEFAULT_STATUS
        defaultBranchShouldNotBeFound("status.lessThan=" + DEFAULT_STATUS);

        // Get all the branchList where status is less than UPDATED_STATUS
        defaultBranchShouldBeFound("status.lessThan=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBranchesByStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where status is greater than DEFAULT_STATUS
        defaultBranchShouldNotBeFound("status.greaterThan=" + DEFAULT_STATUS);

        // Get all the branchList where status is greater than SMALLER_STATUS
        defaultBranchShouldBeFound("status.greaterThan=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllBranchesByAngminIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angmin equals to DEFAULT_ANGMIN
        defaultBranchShouldBeFound("angmin.equals=" + DEFAULT_ANGMIN);

        // Get all the branchList where angmin equals to UPDATED_ANGMIN
        defaultBranchShouldNotBeFound("angmin.equals=" + UPDATED_ANGMIN);
    }

    @Test
    @Transactional
    void getAllBranchesByAngminIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angmin not equals to DEFAULT_ANGMIN
        defaultBranchShouldNotBeFound("angmin.notEquals=" + DEFAULT_ANGMIN);

        // Get all the branchList where angmin not equals to UPDATED_ANGMIN
        defaultBranchShouldBeFound("angmin.notEquals=" + UPDATED_ANGMIN);
    }

    @Test
    @Transactional
    void getAllBranchesByAngminIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angmin in DEFAULT_ANGMIN or UPDATED_ANGMIN
        defaultBranchShouldBeFound("angmin.in=" + DEFAULT_ANGMIN + "," + UPDATED_ANGMIN);

        // Get all the branchList where angmin equals to UPDATED_ANGMIN
        defaultBranchShouldNotBeFound("angmin.in=" + UPDATED_ANGMIN);
    }

    @Test
    @Transactional
    void getAllBranchesByAngminIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angmin is not null
        defaultBranchShouldBeFound("angmin.specified=true");

        // Get all the branchList where angmin is null
        defaultBranchShouldNotBeFound("angmin.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByAngminIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angmin is greater than or equal to DEFAULT_ANGMIN
        defaultBranchShouldBeFound("angmin.greaterThanOrEqual=" + DEFAULT_ANGMIN);

        // Get all the branchList where angmin is greater than or equal to UPDATED_ANGMIN
        defaultBranchShouldNotBeFound("angmin.greaterThanOrEqual=" + UPDATED_ANGMIN);
    }

    @Test
    @Transactional
    void getAllBranchesByAngminIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angmin is less than or equal to DEFAULT_ANGMIN
        defaultBranchShouldBeFound("angmin.lessThanOrEqual=" + DEFAULT_ANGMIN);

        // Get all the branchList where angmin is less than or equal to SMALLER_ANGMIN
        defaultBranchShouldNotBeFound("angmin.lessThanOrEqual=" + SMALLER_ANGMIN);
    }

    @Test
    @Transactional
    void getAllBranchesByAngminIsLessThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angmin is less than DEFAULT_ANGMIN
        defaultBranchShouldNotBeFound("angmin.lessThan=" + DEFAULT_ANGMIN);

        // Get all the branchList where angmin is less than UPDATED_ANGMIN
        defaultBranchShouldBeFound("angmin.lessThan=" + UPDATED_ANGMIN);
    }

    @Test
    @Transactional
    void getAllBranchesByAngminIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angmin is greater than DEFAULT_ANGMIN
        defaultBranchShouldNotBeFound("angmin.greaterThan=" + DEFAULT_ANGMIN);

        // Get all the branchList where angmin is greater than SMALLER_ANGMIN
        defaultBranchShouldBeFound("angmin.greaterThan=" + SMALLER_ANGMIN);
    }

    @Test
    @Transactional
    void getAllBranchesByAngmaxIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angmax equals to DEFAULT_ANGMAX
        defaultBranchShouldBeFound("angmax.equals=" + DEFAULT_ANGMAX);

        // Get all the branchList where angmax equals to UPDATED_ANGMAX
        defaultBranchShouldNotBeFound("angmax.equals=" + UPDATED_ANGMAX);
    }

    @Test
    @Transactional
    void getAllBranchesByAngmaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angmax not equals to DEFAULT_ANGMAX
        defaultBranchShouldNotBeFound("angmax.notEquals=" + DEFAULT_ANGMAX);

        // Get all the branchList where angmax not equals to UPDATED_ANGMAX
        defaultBranchShouldBeFound("angmax.notEquals=" + UPDATED_ANGMAX);
    }

    @Test
    @Transactional
    void getAllBranchesByAngmaxIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angmax in DEFAULT_ANGMAX or UPDATED_ANGMAX
        defaultBranchShouldBeFound("angmax.in=" + DEFAULT_ANGMAX + "," + UPDATED_ANGMAX);

        // Get all the branchList where angmax equals to UPDATED_ANGMAX
        defaultBranchShouldNotBeFound("angmax.in=" + UPDATED_ANGMAX);
    }

    @Test
    @Transactional
    void getAllBranchesByAngmaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angmax is not null
        defaultBranchShouldBeFound("angmax.specified=true");

        // Get all the branchList where angmax is null
        defaultBranchShouldNotBeFound("angmax.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByAngmaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angmax is greater than or equal to DEFAULT_ANGMAX
        defaultBranchShouldBeFound("angmax.greaterThanOrEqual=" + DEFAULT_ANGMAX);

        // Get all the branchList where angmax is greater than or equal to UPDATED_ANGMAX
        defaultBranchShouldNotBeFound("angmax.greaterThanOrEqual=" + UPDATED_ANGMAX);
    }

    @Test
    @Transactional
    void getAllBranchesByAngmaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angmax is less than or equal to DEFAULT_ANGMAX
        defaultBranchShouldBeFound("angmax.lessThanOrEqual=" + DEFAULT_ANGMAX);

        // Get all the branchList where angmax is less than or equal to SMALLER_ANGMAX
        defaultBranchShouldNotBeFound("angmax.lessThanOrEqual=" + SMALLER_ANGMAX);
    }

    @Test
    @Transactional
    void getAllBranchesByAngmaxIsLessThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angmax is less than DEFAULT_ANGMAX
        defaultBranchShouldNotBeFound("angmax.lessThan=" + DEFAULT_ANGMAX);

        // Get all the branchList where angmax is less than UPDATED_ANGMAX
        defaultBranchShouldBeFound("angmax.lessThan=" + UPDATED_ANGMAX);
    }

    @Test
    @Transactional
    void getAllBranchesByAngmaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where angmax is greater than DEFAULT_ANGMAX
        defaultBranchShouldNotBeFound("angmax.greaterThan=" + DEFAULT_ANGMAX);

        // Get all the branchList where angmax is greater than SMALLER_ANGMAX
        defaultBranchShouldBeFound("angmax.greaterThan=" + SMALLER_ANGMAX);
    }

    @Test
    @Transactional
    void getAllBranchesByTransfElValIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);
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
        branch.addTransfElVal(transfElVal);
        branchRepository.saveAndFlush(branch);
        Long transfElValId = transfElVal.getId();

        // Get all the branchList where transfElVal equals to transfElValId
        defaultBranchShouldBeFound("transfElValId.equals=" + transfElValId);

        // Get all the branchList where transfElVal equals to (transfElValId + 1)
        defaultBranchShouldNotBeFound("transfElValId.equals=" + (transfElValId + 1));
    }

    @Test
    @Transactional
    void getAllBranchesByBranchElValIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);
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
        branch.addBranchElVal(branchElVal);
        branchRepository.saveAndFlush(branch);
        Long branchElValId = branchElVal.getId();

        // Get all the branchList where branchElVal equals to branchElValId
        defaultBranchShouldBeFound("branchElValId.equals=" + branchElValId);

        // Get all the branchList where branchElVal equals to (branchElValId + 1)
        defaultBranchShouldNotBeFound("branchElValId.equals=" + (branchElValId + 1));
    }

    @Test
    @Transactional
    void getAllBranchesByBranchExtensionIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);
        BranchExtension branchExtension;
        if (TestUtil.findAll(em, BranchExtension.class).isEmpty()) {
            branchExtension = BranchExtensionResourceIT.createEntity(em);
            em.persist(branchExtension);
            em.flush();
        } else {
            branchExtension = TestUtil.findAll(em, BranchExtension.class).get(0);
        }
        em.persist(branchExtension);
        em.flush();
        branch.setBranchExtension(branchExtension);
        branchExtension.setBranch(branch);
        branchRepository.saveAndFlush(branch);
        Long branchExtensionId = branchExtension.getId();

        // Get all the branchList where branchExtension equals to branchExtensionId
        defaultBranchShouldBeFound("branchExtensionId.equals=" + branchExtensionId);

        // Get all the branchList where branchExtension equals to (branchExtensionId + 1)
        defaultBranchShouldNotBeFound("branchExtensionId.equals=" + (branchExtensionId + 1));
    }

    @Test
    @Transactional
    void getAllBranchesByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);
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
        branch.setNetwork(network);
        branchRepository.saveAndFlush(branch);
        Long networkId = network.getId();

        // Get all the branchList where network equals to networkId
        defaultBranchShouldBeFound("networkId.equals=" + networkId);

        // Get all the branchList where network equals to (networkId + 1)
        defaultBranchShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBranchShouldBeFound(String filter) throws Exception {
        restBranchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branch.getId().intValue())))
            .andExpect(jsonPath("$.[*].fbus").value(hasItem(DEFAULT_FBUS.intValue())))
            .andExpect(jsonPath("$.[*].tbus").value(hasItem(DEFAULT_TBUS.intValue())))
            .andExpect(jsonPath("$.[*].r").value(hasItem(DEFAULT_R.doubleValue())))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X.doubleValue())))
            .andExpect(jsonPath("$.[*].b").value(hasItem(DEFAULT_B.doubleValue())))
            .andExpect(jsonPath("$.[*].ratea").value(hasItem(DEFAULT_RATEA.doubleValue())))
            .andExpect(jsonPath("$.[*].rateb").value(hasItem(DEFAULT_RATEB.doubleValue())))
            .andExpect(jsonPath("$.[*].ratec").value(hasItem(DEFAULT_RATEC.doubleValue())))
            .andExpect(jsonPath("$.[*].tapRatio").value(hasItem(DEFAULT_TAP_RATIO.doubleValue())))
            .andExpect(jsonPath("$.[*].angle").value(hasItem(DEFAULT_ANGLE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].angmin").value(hasItem(DEFAULT_ANGMIN)))
            .andExpect(jsonPath("$.[*].angmax").value(hasItem(DEFAULT_ANGMAX)));

        // Check, that the count call also returns 1
        restBranchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBranchShouldNotBeFound(String filter) throws Exception {
        restBranchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBranchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBranch() throws Exception {
        // Get the branch
        restBranchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        int databaseSizeBeforeUpdate = branchRepository.findAll().size();

        // Update the branch
        Branch updatedBranch = branchRepository.findById(branch.getId()).get();
        // Disconnect from session so that the updates on updatedBranch are not directly saved in db
        em.detach(updatedBranch);
        updatedBranch
            .fbus(UPDATED_FBUS)
            .tbus(UPDATED_TBUS)
            .r(UPDATED_R)
            .x(UPDATED_X)
            .b(UPDATED_B)
            .ratea(UPDATED_RATEA)
            .rateb(UPDATED_RATEB)
            .ratec(UPDATED_RATEC)
            .tapRatio(UPDATED_TAP_RATIO)
            .angle(UPDATED_ANGLE)
            .status(UPDATED_STATUS)
            .angmin(UPDATED_ANGMIN)
            .angmax(UPDATED_ANGMAX);
        BranchDTO branchDTO = branchMapper.toDto(updatedBranch);

        restBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, branchDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchDTO))
            )
            .andExpect(status().isOk());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
        Branch testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getFbus()).isEqualTo(UPDATED_FBUS);
        assertThat(testBranch.getTbus()).isEqualTo(UPDATED_TBUS);
        assertThat(testBranch.getR()).isEqualTo(UPDATED_R);
        assertThat(testBranch.getX()).isEqualTo(UPDATED_X);
        assertThat(testBranch.getB()).isEqualTo(UPDATED_B);
        assertThat(testBranch.getRatea()).isEqualTo(UPDATED_RATEA);
        assertThat(testBranch.getRateb()).isEqualTo(UPDATED_RATEB);
        assertThat(testBranch.getRatec()).isEqualTo(UPDATED_RATEC);
        assertThat(testBranch.getTapRatio()).isEqualTo(UPDATED_TAP_RATIO);
        assertThat(testBranch.getAngle()).isEqualTo(UPDATED_ANGLE);
        assertThat(testBranch.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBranch.getAngmin()).isEqualTo(UPDATED_ANGMIN);
        assertThat(testBranch.getAngmax()).isEqualTo(UPDATED_ANGMAX);
    }

    @Test
    @Transactional
    void putNonExistingBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branch.setId(count.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, branchDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branch.setId(count.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branch.setId(count.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBranchWithPatch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        int databaseSizeBeforeUpdate = branchRepository.findAll().size();

        // Update the branch using partial update
        Branch partialUpdatedBranch = new Branch();
        partialUpdatedBranch.setId(branch.getId());

        partialUpdatedBranch.tbus(UPDATED_TBUS).r(UPDATED_R).b(UPDATED_B).ratea(UPDATED_RATEA).rateb(UPDATED_RATEB).status(UPDATED_STATUS);

        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBranch.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBranch))
            )
            .andExpect(status().isOk());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
        Branch testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getFbus()).isEqualTo(DEFAULT_FBUS);
        assertThat(testBranch.getTbus()).isEqualTo(UPDATED_TBUS);
        assertThat(testBranch.getR()).isEqualTo(UPDATED_R);
        assertThat(testBranch.getX()).isEqualTo(DEFAULT_X);
        assertThat(testBranch.getB()).isEqualTo(UPDATED_B);
        assertThat(testBranch.getRatea()).isEqualTo(UPDATED_RATEA);
        assertThat(testBranch.getRateb()).isEqualTo(UPDATED_RATEB);
        assertThat(testBranch.getRatec()).isEqualTo(DEFAULT_RATEC);
        assertThat(testBranch.getTapRatio()).isEqualTo(DEFAULT_TAP_RATIO);
        assertThat(testBranch.getAngle()).isEqualTo(DEFAULT_ANGLE);
        assertThat(testBranch.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBranch.getAngmin()).isEqualTo(DEFAULT_ANGMIN);
        assertThat(testBranch.getAngmax()).isEqualTo(DEFAULT_ANGMAX);
    }

    @Test
    @Transactional
    void fullUpdateBranchWithPatch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        int databaseSizeBeforeUpdate = branchRepository.findAll().size();

        // Update the branch using partial update
        Branch partialUpdatedBranch = new Branch();
        partialUpdatedBranch.setId(branch.getId());

        partialUpdatedBranch
            .fbus(UPDATED_FBUS)
            .tbus(UPDATED_TBUS)
            .r(UPDATED_R)
            .x(UPDATED_X)
            .b(UPDATED_B)
            .ratea(UPDATED_RATEA)
            .rateb(UPDATED_RATEB)
            .ratec(UPDATED_RATEC)
            .tapRatio(UPDATED_TAP_RATIO)
            .angle(UPDATED_ANGLE)
            .status(UPDATED_STATUS)
            .angmin(UPDATED_ANGMIN)
            .angmax(UPDATED_ANGMAX);

        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBranch.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBranch))
            )
            .andExpect(status().isOk());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
        Branch testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getFbus()).isEqualTo(UPDATED_FBUS);
        assertThat(testBranch.getTbus()).isEqualTo(UPDATED_TBUS);
        assertThat(testBranch.getR()).isEqualTo(UPDATED_R);
        assertThat(testBranch.getX()).isEqualTo(UPDATED_X);
        assertThat(testBranch.getB()).isEqualTo(UPDATED_B);
        assertThat(testBranch.getRatea()).isEqualTo(UPDATED_RATEA);
        assertThat(testBranch.getRateb()).isEqualTo(UPDATED_RATEB);
        assertThat(testBranch.getRatec()).isEqualTo(UPDATED_RATEC);
        assertThat(testBranch.getTapRatio()).isEqualTo(UPDATED_TAP_RATIO);
        assertThat(testBranch.getAngle()).isEqualTo(UPDATED_ANGLE);
        assertThat(testBranch.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBranch.getAngmin()).isEqualTo(UPDATED_ANGMIN);
        assertThat(testBranch.getAngmax()).isEqualTo(UPDATED_ANGMAX);
    }

    @Test
    @Transactional
    void patchNonExistingBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branch.setId(count.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, branchDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(branchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branch.setId(count.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(branchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branch.setId(count.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(branchDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        int databaseSizeBeforeDelete = branchRepository.findAll().size();

        // Delete the branch
        restBranchMockMvc
            .perform(delete(ENTITY_API_URL_ID, branch.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
