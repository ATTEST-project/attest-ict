package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.BusExtension;
import com.attest.ict.repository.BusExtensionRepository;
import com.attest.ict.service.criteria.BusExtensionCriteria;
import com.attest.ict.service.dto.BusExtensionDTO;
import com.attest.ict.service.mapper.BusExtensionMapper;
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
 * Integration tests for the {@link BusExtensionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BusExtensionResourceIT {

    private static final Integer DEFAULT_HAS_GEN = 1;
    private static final Integer UPDATED_HAS_GEN = 2;
    private static final Integer SMALLER_HAS_GEN = 1 - 1;

    private static final Integer DEFAULT_IS_LOAD = 1;
    private static final Integer UPDATED_IS_LOAD = 2;
    private static final Integer SMALLER_IS_LOAD = 1 - 1;

    private static final Double DEFAULT_SNOM_MVA = 1D;
    private static final Double UPDATED_SNOM_MVA = 2D;
    private static final Double SMALLER_SNOM_MVA = 1D - 1D;

    private static final Double DEFAULT_SX = 1D;
    private static final Double UPDATED_SX = 2D;
    private static final Double SMALLER_SX = 1D - 1D;

    private static final Double DEFAULT_SY = 1D;
    private static final Double UPDATED_SY = 2D;
    private static final Double SMALLER_SY = 1D - 1D;

    private static final Double DEFAULT_GX = 1D;
    private static final Double UPDATED_GX = 2D;
    private static final Double SMALLER_GX = 1D - 1D;

    private static final Double DEFAULT_GY = 1D;
    private static final Double UPDATED_GY = 2D;
    private static final Double SMALLER_GY = 1D - 1D;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;
    private static final Integer SMALLER_STATUS = 1 - 1;

    private static final Integer DEFAULT_INCREMENT_COST = 1;
    private static final Integer UPDATED_INCREMENT_COST = 2;
    private static final Integer SMALLER_INCREMENT_COST = 1 - 1;

    private static final Integer DEFAULT_DECREMENT_COST = 1;
    private static final Integer UPDATED_DECREMENT_COST = 2;
    private static final Integer SMALLER_DECREMENT_COST = 1 - 1;

    private static final String DEFAULT_M_RID = "AAAAAAAAAA";
    private static final String UPDATED_M_RID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bus-extensions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BusExtensionRepository busExtensionRepository;

    @Autowired
    private BusExtensionMapper busExtensionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusExtensionMockMvc;

    private BusExtension busExtension;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusExtension createEntity(EntityManager em) {
        BusExtension busExtension = new BusExtension()
            .hasGen(DEFAULT_HAS_GEN)
            .isLoad(DEFAULT_IS_LOAD)
            .snomMva(DEFAULT_SNOM_MVA)
            .sx(DEFAULT_SX)
            .sy(DEFAULT_SY)
            .gx(DEFAULT_GX)
            .gy(DEFAULT_GY)
            .status(DEFAULT_STATUS)
            .incrementCost(DEFAULT_INCREMENT_COST)
            .decrementCost(DEFAULT_DECREMENT_COST)
            .mRid(DEFAULT_M_RID);
        return busExtension;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusExtension createUpdatedEntity(EntityManager em) {
        BusExtension busExtension = new BusExtension()
            .hasGen(UPDATED_HAS_GEN)
            .isLoad(UPDATED_IS_LOAD)
            .snomMva(UPDATED_SNOM_MVA)
            .sx(UPDATED_SX)
            .sy(UPDATED_SY)
            .gx(UPDATED_GX)
            .gy(UPDATED_GY)
            .status(UPDATED_STATUS)
            .incrementCost(UPDATED_INCREMENT_COST)
            .decrementCost(UPDATED_DECREMENT_COST)
            .mRid(UPDATED_M_RID);
        return busExtension;
    }

    @BeforeEach
    public void initTest() {
        busExtension = createEntity(em);
    }

    @Test
    @Transactional
    void createBusExtension() throws Exception {
        int databaseSizeBeforeCreate = busExtensionRepository.findAll().size();
        // Create the BusExtension
        BusExtensionDTO busExtensionDTO = busExtensionMapper.toDto(busExtension);
        restBusExtensionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busExtensionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BusExtension in the database
        List<BusExtension> busExtensionList = busExtensionRepository.findAll();
        assertThat(busExtensionList).hasSize(databaseSizeBeforeCreate + 1);
        BusExtension testBusExtension = busExtensionList.get(busExtensionList.size() - 1);
        assertThat(testBusExtension.getHasGen()).isEqualTo(DEFAULT_HAS_GEN);
        assertThat(testBusExtension.getIsLoad()).isEqualTo(DEFAULT_IS_LOAD);
        assertThat(testBusExtension.getSnomMva()).isEqualTo(DEFAULT_SNOM_MVA);
        assertThat(testBusExtension.getSx()).isEqualTo(DEFAULT_SX);
        assertThat(testBusExtension.getSy()).isEqualTo(DEFAULT_SY);
        assertThat(testBusExtension.getGx()).isEqualTo(DEFAULT_GX);
        assertThat(testBusExtension.getGy()).isEqualTo(DEFAULT_GY);
        assertThat(testBusExtension.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBusExtension.getIncrementCost()).isEqualTo(DEFAULT_INCREMENT_COST);
        assertThat(testBusExtension.getDecrementCost()).isEqualTo(DEFAULT_DECREMENT_COST);
        assertThat(testBusExtension.getmRid()).isEqualTo(DEFAULT_M_RID);
    }

    @Test
    @Transactional
    void createBusExtensionWithExistingId() throws Exception {
        // Create the BusExtension with an existing ID
        busExtension.setId(1L);
        BusExtensionDTO busExtensionDTO = busExtensionMapper.toDto(busExtension);

        int databaseSizeBeforeCreate = busExtensionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusExtensionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busExtensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusExtension in the database
        List<BusExtension> busExtensionList = busExtensionRepository.findAll();
        assertThat(busExtensionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBusExtensions() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList
        restBusExtensionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(busExtension.getId().intValue())))
            .andExpect(jsonPath("$.[*].hasGen").value(hasItem(DEFAULT_HAS_GEN)))
            .andExpect(jsonPath("$.[*].isLoad").value(hasItem(DEFAULT_IS_LOAD)))
            .andExpect(jsonPath("$.[*].snomMva").value(hasItem(DEFAULT_SNOM_MVA.doubleValue())))
            .andExpect(jsonPath("$.[*].sx").value(hasItem(DEFAULT_SX.doubleValue())))
            .andExpect(jsonPath("$.[*].sy").value(hasItem(DEFAULT_SY.doubleValue())))
            .andExpect(jsonPath("$.[*].gx").value(hasItem(DEFAULT_GX.doubleValue())))
            .andExpect(jsonPath("$.[*].gy").value(hasItem(DEFAULT_GY.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].incrementCost").value(hasItem(DEFAULT_INCREMENT_COST)))
            .andExpect(jsonPath("$.[*].decrementCost").value(hasItem(DEFAULT_DECREMENT_COST)))
            .andExpect(jsonPath("$.[*].mRid").value(hasItem(DEFAULT_M_RID)));
    }

    @Test
    @Transactional
    void getBusExtension() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get the busExtension
        restBusExtensionMockMvc
            .perform(get(ENTITY_API_URL_ID, busExtension.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(busExtension.getId().intValue()))
            .andExpect(jsonPath("$.hasGen").value(DEFAULT_HAS_GEN))
            .andExpect(jsonPath("$.isLoad").value(DEFAULT_IS_LOAD))
            .andExpect(jsonPath("$.snomMva").value(DEFAULT_SNOM_MVA.doubleValue()))
            .andExpect(jsonPath("$.sx").value(DEFAULT_SX.doubleValue()))
            .andExpect(jsonPath("$.sy").value(DEFAULT_SY.doubleValue()))
            .andExpect(jsonPath("$.gx").value(DEFAULT_GX.doubleValue()))
            .andExpect(jsonPath("$.gy").value(DEFAULT_GY.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.incrementCost").value(DEFAULT_INCREMENT_COST))
            .andExpect(jsonPath("$.decrementCost").value(DEFAULT_DECREMENT_COST))
            .andExpect(jsonPath("$.mRid").value(DEFAULT_M_RID));
    }

    @Test
    @Transactional
    void getBusExtensionsByIdFiltering() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        Long id = busExtension.getId();

        defaultBusExtensionShouldBeFound("id.equals=" + id);
        defaultBusExtensionShouldNotBeFound("id.notEquals=" + id);

        defaultBusExtensionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBusExtensionShouldNotBeFound("id.greaterThan=" + id);

        defaultBusExtensionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBusExtensionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByHasGenIsEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where hasGen equals to DEFAULT_HAS_GEN
        defaultBusExtensionShouldBeFound("hasGen.equals=" + DEFAULT_HAS_GEN);

        // Get all the busExtensionList where hasGen equals to UPDATED_HAS_GEN
        defaultBusExtensionShouldNotBeFound("hasGen.equals=" + UPDATED_HAS_GEN);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByHasGenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where hasGen not equals to DEFAULT_HAS_GEN
        defaultBusExtensionShouldNotBeFound("hasGen.notEquals=" + DEFAULT_HAS_GEN);

        // Get all the busExtensionList where hasGen not equals to UPDATED_HAS_GEN
        defaultBusExtensionShouldBeFound("hasGen.notEquals=" + UPDATED_HAS_GEN);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByHasGenIsInShouldWork() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where hasGen in DEFAULT_HAS_GEN or UPDATED_HAS_GEN
        defaultBusExtensionShouldBeFound("hasGen.in=" + DEFAULT_HAS_GEN + "," + UPDATED_HAS_GEN);

        // Get all the busExtensionList where hasGen equals to UPDATED_HAS_GEN
        defaultBusExtensionShouldNotBeFound("hasGen.in=" + UPDATED_HAS_GEN);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByHasGenIsNullOrNotNull() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where hasGen is not null
        defaultBusExtensionShouldBeFound("hasGen.specified=true");

        // Get all the busExtensionList where hasGen is null
        defaultBusExtensionShouldNotBeFound("hasGen.specified=false");
    }

    @Test
    @Transactional
    void getAllBusExtensionsByHasGenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where hasGen is greater than or equal to DEFAULT_HAS_GEN
        defaultBusExtensionShouldBeFound("hasGen.greaterThanOrEqual=" + DEFAULT_HAS_GEN);

        // Get all the busExtensionList where hasGen is greater than or equal to UPDATED_HAS_GEN
        defaultBusExtensionShouldNotBeFound("hasGen.greaterThanOrEqual=" + UPDATED_HAS_GEN);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByHasGenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where hasGen is less than or equal to DEFAULT_HAS_GEN
        defaultBusExtensionShouldBeFound("hasGen.lessThanOrEqual=" + DEFAULT_HAS_GEN);

        // Get all the busExtensionList where hasGen is less than or equal to SMALLER_HAS_GEN
        defaultBusExtensionShouldNotBeFound("hasGen.lessThanOrEqual=" + SMALLER_HAS_GEN);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByHasGenIsLessThanSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where hasGen is less than DEFAULT_HAS_GEN
        defaultBusExtensionShouldNotBeFound("hasGen.lessThan=" + DEFAULT_HAS_GEN);

        // Get all the busExtensionList where hasGen is less than UPDATED_HAS_GEN
        defaultBusExtensionShouldBeFound("hasGen.lessThan=" + UPDATED_HAS_GEN);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByHasGenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where hasGen is greater than DEFAULT_HAS_GEN
        defaultBusExtensionShouldNotBeFound("hasGen.greaterThan=" + DEFAULT_HAS_GEN);

        // Get all the busExtensionList where hasGen is greater than SMALLER_HAS_GEN
        defaultBusExtensionShouldBeFound("hasGen.greaterThan=" + SMALLER_HAS_GEN);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByIsLoadIsEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where isLoad equals to DEFAULT_IS_LOAD
        defaultBusExtensionShouldBeFound("isLoad.equals=" + DEFAULT_IS_LOAD);

        // Get all the busExtensionList where isLoad equals to UPDATED_IS_LOAD
        defaultBusExtensionShouldNotBeFound("isLoad.equals=" + UPDATED_IS_LOAD);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByIsLoadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where isLoad not equals to DEFAULT_IS_LOAD
        defaultBusExtensionShouldNotBeFound("isLoad.notEquals=" + DEFAULT_IS_LOAD);

        // Get all the busExtensionList where isLoad not equals to UPDATED_IS_LOAD
        defaultBusExtensionShouldBeFound("isLoad.notEquals=" + UPDATED_IS_LOAD);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByIsLoadIsInShouldWork() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where isLoad in DEFAULT_IS_LOAD or UPDATED_IS_LOAD
        defaultBusExtensionShouldBeFound("isLoad.in=" + DEFAULT_IS_LOAD + "," + UPDATED_IS_LOAD);

        // Get all the busExtensionList where isLoad equals to UPDATED_IS_LOAD
        defaultBusExtensionShouldNotBeFound("isLoad.in=" + UPDATED_IS_LOAD);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByIsLoadIsNullOrNotNull() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where isLoad is not null
        defaultBusExtensionShouldBeFound("isLoad.specified=true");

        // Get all the busExtensionList where isLoad is null
        defaultBusExtensionShouldNotBeFound("isLoad.specified=false");
    }

    @Test
    @Transactional
    void getAllBusExtensionsByIsLoadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where isLoad is greater than or equal to DEFAULT_IS_LOAD
        defaultBusExtensionShouldBeFound("isLoad.greaterThanOrEqual=" + DEFAULT_IS_LOAD);

        // Get all the busExtensionList where isLoad is greater than or equal to UPDATED_IS_LOAD
        defaultBusExtensionShouldNotBeFound("isLoad.greaterThanOrEqual=" + UPDATED_IS_LOAD);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByIsLoadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where isLoad is less than or equal to DEFAULT_IS_LOAD
        defaultBusExtensionShouldBeFound("isLoad.lessThanOrEqual=" + DEFAULT_IS_LOAD);

        // Get all the busExtensionList where isLoad is less than or equal to SMALLER_IS_LOAD
        defaultBusExtensionShouldNotBeFound("isLoad.lessThanOrEqual=" + SMALLER_IS_LOAD);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByIsLoadIsLessThanSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where isLoad is less than DEFAULT_IS_LOAD
        defaultBusExtensionShouldNotBeFound("isLoad.lessThan=" + DEFAULT_IS_LOAD);

        // Get all the busExtensionList where isLoad is less than UPDATED_IS_LOAD
        defaultBusExtensionShouldBeFound("isLoad.lessThan=" + UPDATED_IS_LOAD);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByIsLoadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where isLoad is greater than DEFAULT_IS_LOAD
        defaultBusExtensionShouldNotBeFound("isLoad.greaterThan=" + DEFAULT_IS_LOAD);

        // Get all the busExtensionList where isLoad is greater than SMALLER_IS_LOAD
        defaultBusExtensionShouldBeFound("isLoad.greaterThan=" + SMALLER_IS_LOAD);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySnomMvaIsEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where snomMva equals to DEFAULT_SNOM_MVA
        defaultBusExtensionShouldBeFound("snomMva.equals=" + DEFAULT_SNOM_MVA);

        // Get all the busExtensionList where snomMva equals to UPDATED_SNOM_MVA
        defaultBusExtensionShouldNotBeFound("snomMva.equals=" + UPDATED_SNOM_MVA);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySnomMvaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where snomMva not equals to DEFAULT_SNOM_MVA
        defaultBusExtensionShouldNotBeFound("snomMva.notEquals=" + DEFAULT_SNOM_MVA);

        // Get all the busExtensionList where snomMva not equals to UPDATED_SNOM_MVA
        defaultBusExtensionShouldBeFound("snomMva.notEquals=" + UPDATED_SNOM_MVA);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySnomMvaIsInShouldWork() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where snomMva in DEFAULT_SNOM_MVA or UPDATED_SNOM_MVA
        defaultBusExtensionShouldBeFound("snomMva.in=" + DEFAULT_SNOM_MVA + "," + UPDATED_SNOM_MVA);

        // Get all the busExtensionList where snomMva equals to UPDATED_SNOM_MVA
        defaultBusExtensionShouldNotBeFound("snomMva.in=" + UPDATED_SNOM_MVA);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySnomMvaIsNullOrNotNull() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where snomMva is not null
        defaultBusExtensionShouldBeFound("snomMva.specified=true");

        // Get all the busExtensionList where snomMva is null
        defaultBusExtensionShouldNotBeFound("snomMva.specified=false");
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySnomMvaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where snomMva is greater than or equal to DEFAULT_SNOM_MVA
        defaultBusExtensionShouldBeFound("snomMva.greaterThanOrEqual=" + DEFAULT_SNOM_MVA);

        // Get all the busExtensionList where snomMva is greater than or equal to UPDATED_SNOM_MVA
        defaultBusExtensionShouldNotBeFound("snomMva.greaterThanOrEqual=" + UPDATED_SNOM_MVA);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySnomMvaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where snomMva is less than or equal to DEFAULT_SNOM_MVA
        defaultBusExtensionShouldBeFound("snomMva.lessThanOrEqual=" + DEFAULT_SNOM_MVA);

        // Get all the busExtensionList where snomMva is less than or equal to SMALLER_SNOM_MVA
        defaultBusExtensionShouldNotBeFound("snomMva.lessThanOrEqual=" + SMALLER_SNOM_MVA);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySnomMvaIsLessThanSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where snomMva is less than DEFAULT_SNOM_MVA
        defaultBusExtensionShouldNotBeFound("snomMva.lessThan=" + DEFAULT_SNOM_MVA);

        // Get all the busExtensionList where snomMva is less than UPDATED_SNOM_MVA
        defaultBusExtensionShouldBeFound("snomMva.lessThan=" + UPDATED_SNOM_MVA);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySnomMvaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where snomMva is greater than DEFAULT_SNOM_MVA
        defaultBusExtensionShouldNotBeFound("snomMva.greaterThan=" + DEFAULT_SNOM_MVA);

        // Get all the busExtensionList where snomMva is greater than SMALLER_SNOM_MVA
        defaultBusExtensionShouldBeFound("snomMva.greaterThan=" + SMALLER_SNOM_MVA);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySxIsEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where sx equals to DEFAULT_SX
        defaultBusExtensionShouldBeFound("sx.equals=" + DEFAULT_SX);

        // Get all the busExtensionList where sx equals to UPDATED_SX
        defaultBusExtensionShouldNotBeFound("sx.equals=" + UPDATED_SX);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where sx not equals to DEFAULT_SX
        defaultBusExtensionShouldNotBeFound("sx.notEquals=" + DEFAULT_SX);

        // Get all the busExtensionList where sx not equals to UPDATED_SX
        defaultBusExtensionShouldBeFound("sx.notEquals=" + UPDATED_SX);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySxIsInShouldWork() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where sx in DEFAULT_SX or UPDATED_SX
        defaultBusExtensionShouldBeFound("sx.in=" + DEFAULT_SX + "," + UPDATED_SX);

        // Get all the busExtensionList where sx equals to UPDATED_SX
        defaultBusExtensionShouldNotBeFound("sx.in=" + UPDATED_SX);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySxIsNullOrNotNull() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where sx is not null
        defaultBusExtensionShouldBeFound("sx.specified=true");

        // Get all the busExtensionList where sx is null
        defaultBusExtensionShouldNotBeFound("sx.specified=false");
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where sx is greater than or equal to DEFAULT_SX
        defaultBusExtensionShouldBeFound("sx.greaterThanOrEqual=" + DEFAULT_SX);

        // Get all the busExtensionList where sx is greater than or equal to UPDATED_SX
        defaultBusExtensionShouldNotBeFound("sx.greaterThanOrEqual=" + UPDATED_SX);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where sx is less than or equal to DEFAULT_SX
        defaultBusExtensionShouldBeFound("sx.lessThanOrEqual=" + DEFAULT_SX);

        // Get all the busExtensionList where sx is less than or equal to SMALLER_SX
        defaultBusExtensionShouldNotBeFound("sx.lessThanOrEqual=" + SMALLER_SX);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySxIsLessThanSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where sx is less than DEFAULT_SX
        defaultBusExtensionShouldNotBeFound("sx.lessThan=" + DEFAULT_SX);

        // Get all the busExtensionList where sx is less than UPDATED_SX
        defaultBusExtensionShouldBeFound("sx.lessThan=" + UPDATED_SX);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where sx is greater than DEFAULT_SX
        defaultBusExtensionShouldNotBeFound("sx.greaterThan=" + DEFAULT_SX);

        // Get all the busExtensionList where sx is greater than SMALLER_SX
        defaultBusExtensionShouldBeFound("sx.greaterThan=" + SMALLER_SX);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySyIsEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where sy equals to DEFAULT_SY
        defaultBusExtensionShouldBeFound("sy.equals=" + DEFAULT_SY);

        // Get all the busExtensionList where sy equals to UPDATED_SY
        defaultBusExtensionShouldNotBeFound("sy.equals=" + UPDATED_SY);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where sy not equals to DEFAULT_SY
        defaultBusExtensionShouldNotBeFound("sy.notEquals=" + DEFAULT_SY);

        // Get all the busExtensionList where sy not equals to UPDATED_SY
        defaultBusExtensionShouldBeFound("sy.notEquals=" + UPDATED_SY);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySyIsInShouldWork() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where sy in DEFAULT_SY or UPDATED_SY
        defaultBusExtensionShouldBeFound("sy.in=" + DEFAULT_SY + "," + UPDATED_SY);

        // Get all the busExtensionList where sy equals to UPDATED_SY
        defaultBusExtensionShouldNotBeFound("sy.in=" + UPDATED_SY);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySyIsNullOrNotNull() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where sy is not null
        defaultBusExtensionShouldBeFound("sy.specified=true");

        // Get all the busExtensionList where sy is null
        defaultBusExtensionShouldNotBeFound("sy.specified=false");
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where sy is greater than or equal to DEFAULT_SY
        defaultBusExtensionShouldBeFound("sy.greaterThanOrEqual=" + DEFAULT_SY);

        // Get all the busExtensionList where sy is greater than or equal to UPDATED_SY
        defaultBusExtensionShouldNotBeFound("sy.greaterThanOrEqual=" + UPDATED_SY);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where sy is less than or equal to DEFAULT_SY
        defaultBusExtensionShouldBeFound("sy.lessThanOrEqual=" + DEFAULT_SY);

        // Get all the busExtensionList where sy is less than or equal to SMALLER_SY
        defaultBusExtensionShouldNotBeFound("sy.lessThanOrEqual=" + SMALLER_SY);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySyIsLessThanSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where sy is less than DEFAULT_SY
        defaultBusExtensionShouldNotBeFound("sy.lessThan=" + DEFAULT_SY);

        // Get all the busExtensionList where sy is less than UPDATED_SY
        defaultBusExtensionShouldBeFound("sy.lessThan=" + UPDATED_SY);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBySyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where sy is greater than DEFAULT_SY
        defaultBusExtensionShouldNotBeFound("sy.greaterThan=" + DEFAULT_SY);

        // Get all the busExtensionList where sy is greater than SMALLER_SY
        defaultBusExtensionShouldBeFound("sy.greaterThan=" + SMALLER_SY);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByGxIsEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where gx equals to DEFAULT_GX
        defaultBusExtensionShouldBeFound("gx.equals=" + DEFAULT_GX);

        // Get all the busExtensionList where gx equals to UPDATED_GX
        defaultBusExtensionShouldNotBeFound("gx.equals=" + UPDATED_GX);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByGxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where gx not equals to DEFAULT_GX
        defaultBusExtensionShouldNotBeFound("gx.notEquals=" + DEFAULT_GX);

        // Get all the busExtensionList where gx not equals to UPDATED_GX
        defaultBusExtensionShouldBeFound("gx.notEquals=" + UPDATED_GX);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByGxIsInShouldWork() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where gx in DEFAULT_GX or UPDATED_GX
        defaultBusExtensionShouldBeFound("gx.in=" + DEFAULT_GX + "," + UPDATED_GX);

        // Get all the busExtensionList where gx equals to UPDATED_GX
        defaultBusExtensionShouldNotBeFound("gx.in=" + UPDATED_GX);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByGxIsNullOrNotNull() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where gx is not null
        defaultBusExtensionShouldBeFound("gx.specified=true");

        // Get all the busExtensionList where gx is null
        defaultBusExtensionShouldNotBeFound("gx.specified=false");
    }

    @Test
    @Transactional
    void getAllBusExtensionsByGxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where gx is greater than or equal to DEFAULT_GX
        defaultBusExtensionShouldBeFound("gx.greaterThanOrEqual=" + DEFAULT_GX);

        // Get all the busExtensionList where gx is greater than or equal to UPDATED_GX
        defaultBusExtensionShouldNotBeFound("gx.greaterThanOrEqual=" + UPDATED_GX);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByGxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where gx is less than or equal to DEFAULT_GX
        defaultBusExtensionShouldBeFound("gx.lessThanOrEqual=" + DEFAULT_GX);

        // Get all the busExtensionList where gx is less than or equal to SMALLER_GX
        defaultBusExtensionShouldNotBeFound("gx.lessThanOrEqual=" + SMALLER_GX);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByGxIsLessThanSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where gx is less than DEFAULT_GX
        defaultBusExtensionShouldNotBeFound("gx.lessThan=" + DEFAULT_GX);

        // Get all the busExtensionList where gx is less than UPDATED_GX
        defaultBusExtensionShouldBeFound("gx.lessThan=" + UPDATED_GX);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByGxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where gx is greater than DEFAULT_GX
        defaultBusExtensionShouldNotBeFound("gx.greaterThan=" + DEFAULT_GX);

        // Get all the busExtensionList where gx is greater than SMALLER_GX
        defaultBusExtensionShouldBeFound("gx.greaterThan=" + SMALLER_GX);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByGyIsEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where gy equals to DEFAULT_GY
        defaultBusExtensionShouldBeFound("gy.equals=" + DEFAULT_GY);

        // Get all the busExtensionList where gy equals to UPDATED_GY
        defaultBusExtensionShouldNotBeFound("gy.equals=" + UPDATED_GY);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByGyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where gy not equals to DEFAULT_GY
        defaultBusExtensionShouldNotBeFound("gy.notEquals=" + DEFAULT_GY);

        // Get all the busExtensionList where gy not equals to UPDATED_GY
        defaultBusExtensionShouldBeFound("gy.notEquals=" + UPDATED_GY);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByGyIsInShouldWork() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where gy in DEFAULT_GY or UPDATED_GY
        defaultBusExtensionShouldBeFound("gy.in=" + DEFAULT_GY + "," + UPDATED_GY);

        // Get all the busExtensionList where gy equals to UPDATED_GY
        defaultBusExtensionShouldNotBeFound("gy.in=" + UPDATED_GY);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByGyIsNullOrNotNull() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where gy is not null
        defaultBusExtensionShouldBeFound("gy.specified=true");

        // Get all the busExtensionList where gy is null
        defaultBusExtensionShouldNotBeFound("gy.specified=false");
    }

    @Test
    @Transactional
    void getAllBusExtensionsByGyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where gy is greater than or equal to DEFAULT_GY
        defaultBusExtensionShouldBeFound("gy.greaterThanOrEqual=" + DEFAULT_GY);

        // Get all the busExtensionList where gy is greater than or equal to UPDATED_GY
        defaultBusExtensionShouldNotBeFound("gy.greaterThanOrEqual=" + UPDATED_GY);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByGyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where gy is less than or equal to DEFAULT_GY
        defaultBusExtensionShouldBeFound("gy.lessThanOrEqual=" + DEFAULT_GY);

        // Get all the busExtensionList where gy is less than or equal to SMALLER_GY
        defaultBusExtensionShouldNotBeFound("gy.lessThanOrEqual=" + SMALLER_GY);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByGyIsLessThanSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where gy is less than DEFAULT_GY
        defaultBusExtensionShouldNotBeFound("gy.lessThan=" + DEFAULT_GY);

        // Get all the busExtensionList where gy is less than UPDATED_GY
        defaultBusExtensionShouldBeFound("gy.lessThan=" + UPDATED_GY);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByGyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where gy is greater than DEFAULT_GY
        defaultBusExtensionShouldNotBeFound("gy.greaterThan=" + DEFAULT_GY);

        // Get all the busExtensionList where gy is greater than SMALLER_GY
        defaultBusExtensionShouldBeFound("gy.greaterThan=" + SMALLER_GY);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where status equals to DEFAULT_STATUS
        defaultBusExtensionShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the busExtensionList where status equals to UPDATED_STATUS
        defaultBusExtensionShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where status not equals to DEFAULT_STATUS
        defaultBusExtensionShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the busExtensionList where status not equals to UPDATED_STATUS
        defaultBusExtensionShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultBusExtensionShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the busExtensionList where status equals to UPDATED_STATUS
        defaultBusExtensionShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where status is not null
        defaultBusExtensionShouldBeFound("status.specified=true");

        // Get all the busExtensionList where status is null
        defaultBusExtensionShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllBusExtensionsByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where status is greater than or equal to DEFAULT_STATUS
        defaultBusExtensionShouldBeFound("status.greaterThanOrEqual=" + DEFAULT_STATUS);

        // Get all the busExtensionList where status is greater than or equal to UPDATED_STATUS
        defaultBusExtensionShouldNotBeFound("status.greaterThanOrEqual=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where status is less than or equal to DEFAULT_STATUS
        defaultBusExtensionShouldBeFound("status.lessThanOrEqual=" + DEFAULT_STATUS);

        // Get all the busExtensionList where status is less than or equal to SMALLER_STATUS
        defaultBusExtensionShouldNotBeFound("status.lessThanOrEqual=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where status is less than DEFAULT_STATUS
        defaultBusExtensionShouldNotBeFound("status.lessThan=" + DEFAULT_STATUS);

        // Get all the busExtensionList where status is less than UPDATED_STATUS
        defaultBusExtensionShouldBeFound("status.lessThan=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where status is greater than DEFAULT_STATUS
        defaultBusExtensionShouldNotBeFound("status.greaterThan=" + DEFAULT_STATUS);

        // Get all the busExtensionList where status is greater than SMALLER_STATUS
        defaultBusExtensionShouldBeFound("status.greaterThan=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByIncrementCostIsEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where incrementCost equals to DEFAULT_INCREMENT_COST
        defaultBusExtensionShouldBeFound("incrementCost.equals=" + DEFAULT_INCREMENT_COST);

        // Get all the busExtensionList where incrementCost equals to UPDATED_INCREMENT_COST
        defaultBusExtensionShouldNotBeFound("incrementCost.equals=" + UPDATED_INCREMENT_COST);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByIncrementCostIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where incrementCost not equals to DEFAULT_INCREMENT_COST
        defaultBusExtensionShouldNotBeFound("incrementCost.notEquals=" + DEFAULT_INCREMENT_COST);

        // Get all the busExtensionList where incrementCost not equals to UPDATED_INCREMENT_COST
        defaultBusExtensionShouldBeFound("incrementCost.notEquals=" + UPDATED_INCREMENT_COST);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByIncrementCostIsInShouldWork() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where incrementCost in DEFAULT_INCREMENT_COST or UPDATED_INCREMENT_COST
        defaultBusExtensionShouldBeFound("incrementCost.in=" + DEFAULT_INCREMENT_COST + "," + UPDATED_INCREMENT_COST);

        // Get all the busExtensionList where incrementCost equals to UPDATED_INCREMENT_COST
        defaultBusExtensionShouldNotBeFound("incrementCost.in=" + UPDATED_INCREMENT_COST);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByIncrementCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where incrementCost is not null
        defaultBusExtensionShouldBeFound("incrementCost.specified=true");

        // Get all the busExtensionList where incrementCost is null
        defaultBusExtensionShouldNotBeFound("incrementCost.specified=false");
    }

    @Test
    @Transactional
    void getAllBusExtensionsByIncrementCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where incrementCost is greater than or equal to DEFAULT_INCREMENT_COST
        defaultBusExtensionShouldBeFound("incrementCost.greaterThanOrEqual=" + DEFAULT_INCREMENT_COST);

        // Get all the busExtensionList where incrementCost is greater than or equal to UPDATED_INCREMENT_COST
        defaultBusExtensionShouldNotBeFound("incrementCost.greaterThanOrEqual=" + UPDATED_INCREMENT_COST);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByIncrementCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where incrementCost is less than or equal to DEFAULT_INCREMENT_COST
        defaultBusExtensionShouldBeFound("incrementCost.lessThanOrEqual=" + DEFAULT_INCREMENT_COST);

        // Get all the busExtensionList where incrementCost is less than or equal to SMALLER_INCREMENT_COST
        defaultBusExtensionShouldNotBeFound("incrementCost.lessThanOrEqual=" + SMALLER_INCREMENT_COST);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByIncrementCostIsLessThanSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where incrementCost is less than DEFAULT_INCREMENT_COST
        defaultBusExtensionShouldNotBeFound("incrementCost.lessThan=" + DEFAULT_INCREMENT_COST);

        // Get all the busExtensionList where incrementCost is less than UPDATED_INCREMENT_COST
        defaultBusExtensionShouldBeFound("incrementCost.lessThan=" + UPDATED_INCREMENT_COST);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByIncrementCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where incrementCost is greater than DEFAULT_INCREMENT_COST
        defaultBusExtensionShouldNotBeFound("incrementCost.greaterThan=" + DEFAULT_INCREMENT_COST);

        // Get all the busExtensionList where incrementCost is greater than SMALLER_INCREMENT_COST
        defaultBusExtensionShouldBeFound("incrementCost.greaterThan=" + SMALLER_INCREMENT_COST);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByDecrementCostIsEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where decrementCost equals to DEFAULT_DECREMENT_COST
        defaultBusExtensionShouldBeFound("decrementCost.equals=" + DEFAULT_DECREMENT_COST);

        // Get all the busExtensionList where decrementCost equals to UPDATED_DECREMENT_COST
        defaultBusExtensionShouldNotBeFound("decrementCost.equals=" + UPDATED_DECREMENT_COST);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByDecrementCostIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where decrementCost not equals to DEFAULT_DECREMENT_COST
        defaultBusExtensionShouldNotBeFound("decrementCost.notEquals=" + DEFAULT_DECREMENT_COST);

        // Get all the busExtensionList where decrementCost not equals to UPDATED_DECREMENT_COST
        defaultBusExtensionShouldBeFound("decrementCost.notEquals=" + UPDATED_DECREMENT_COST);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByDecrementCostIsInShouldWork() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where decrementCost in DEFAULT_DECREMENT_COST or UPDATED_DECREMENT_COST
        defaultBusExtensionShouldBeFound("decrementCost.in=" + DEFAULT_DECREMENT_COST + "," + UPDATED_DECREMENT_COST);

        // Get all the busExtensionList where decrementCost equals to UPDATED_DECREMENT_COST
        defaultBusExtensionShouldNotBeFound("decrementCost.in=" + UPDATED_DECREMENT_COST);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByDecrementCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where decrementCost is not null
        defaultBusExtensionShouldBeFound("decrementCost.specified=true");

        // Get all the busExtensionList where decrementCost is null
        defaultBusExtensionShouldNotBeFound("decrementCost.specified=false");
    }

    @Test
    @Transactional
    void getAllBusExtensionsByDecrementCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where decrementCost is greater than or equal to DEFAULT_DECREMENT_COST
        defaultBusExtensionShouldBeFound("decrementCost.greaterThanOrEqual=" + DEFAULT_DECREMENT_COST);

        // Get all the busExtensionList where decrementCost is greater than or equal to UPDATED_DECREMENT_COST
        defaultBusExtensionShouldNotBeFound("decrementCost.greaterThanOrEqual=" + UPDATED_DECREMENT_COST);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByDecrementCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where decrementCost is less than or equal to DEFAULT_DECREMENT_COST
        defaultBusExtensionShouldBeFound("decrementCost.lessThanOrEqual=" + DEFAULT_DECREMENT_COST);

        // Get all the busExtensionList where decrementCost is less than or equal to SMALLER_DECREMENT_COST
        defaultBusExtensionShouldNotBeFound("decrementCost.lessThanOrEqual=" + SMALLER_DECREMENT_COST);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByDecrementCostIsLessThanSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where decrementCost is less than DEFAULT_DECREMENT_COST
        defaultBusExtensionShouldNotBeFound("decrementCost.lessThan=" + DEFAULT_DECREMENT_COST);

        // Get all the busExtensionList where decrementCost is less than UPDATED_DECREMENT_COST
        defaultBusExtensionShouldBeFound("decrementCost.lessThan=" + UPDATED_DECREMENT_COST);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByDecrementCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where decrementCost is greater than DEFAULT_DECREMENT_COST
        defaultBusExtensionShouldNotBeFound("decrementCost.greaterThan=" + DEFAULT_DECREMENT_COST);

        // Get all the busExtensionList where decrementCost is greater than SMALLER_DECREMENT_COST
        defaultBusExtensionShouldBeFound("decrementCost.greaterThan=" + SMALLER_DECREMENT_COST);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBymRidIsEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where mRid equals to DEFAULT_M_RID
        defaultBusExtensionShouldBeFound("mRid.equals=" + DEFAULT_M_RID);

        // Get all the busExtensionList where mRid equals to UPDATED_M_RID
        defaultBusExtensionShouldNotBeFound("mRid.equals=" + UPDATED_M_RID);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBymRidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where mRid not equals to DEFAULT_M_RID
        defaultBusExtensionShouldNotBeFound("mRid.notEquals=" + DEFAULT_M_RID);

        // Get all the busExtensionList where mRid not equals to UPDATED_M_RID
        defaultBusExtensionShouldBeFound("mRid.notEquals=" + UPDATED_M_RID);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBymRidIsInShouldWork() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where mRid in DEFAULT_M_RID or UPDATED_M_RID
        defaultBusExtensionShouldBeFound("mRid.in=" + DEFAULT_M_RID + "," + UPDATED_M_RID);

        // Get all the busExtensionList where mRid equals to UPDATED_M_RID
        defaultBusExtensionShouldNotBeFound("mRid.in=" + UPDATED_M_RID);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBymRidIsNullOrNotNull() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where mRid is not null
        defaultBusExtensionShouldBeFound("mRid.specified=true");

        // Get all the busExtensionList where mRid is null
        defaultBusExtensionShouldNotBeFound("mRid.specified=false");
    }

    @Test
    @Transactional
    void getAllBusExtensionsBymRidContainsSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where mRid contains DEFAULT_M_RID
        defaultBusExtensionShouldBeFound("mRid.contains=" + DEFAULT_M_RID);

        // Get all the busExtensionList where mRid contains UPDATED_M_RID
        defaultBusExtensionShouldNotBeFound("mRid.contains=" + UPDATED_M_RID);
    }

    @Test
    @Transactional
    void getAllBusExtensionsBymRidNotContainsSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        // Get all the busExtensionList where mRid does not contain DEFAULT_M_RID
        defaultBusExtensionShouldNotBeFound("mRid.doesNotContain=" + DEFAULT_M_RID);

        // Get all the busExtensionList where mRid does not contain UPDATED_M_RID
        defaultBusExtensionShouldBeFound("mRid.doesNotContain=" + UPDATED_M_RID);
    }

    @Test
    @Transactional
    void getAllBusExtensionsByBusIsEqualToSomething() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);
        Bus bus;
        if (TestUtil.findAll(em, Bus.class).isEmpty()) {
            bus = BusResourceIT.createEntity(em);
            em.persist(bus);
            em.flush();
        } else {
            bus = TestUtil.findAll(em, Bus.class).get(0);
        }
        em.persist(bus);
        em.flush();
        busExtension.setBus(bus);
        busExtensionRepository.saveAndFlush(busExtension);
        Long busId = bus.getId();

        // Get all the busExtensionList where bus equals to busId
        defaultBusExtensionShouldBeFound("busId.equals=" + busId);

        // Get all the busExtensionList where bus equals to (busId + 1)
        defaultBusExtensionShouldNotBeFound("busId.equals=" + (busId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBusExtensionShouldBeFound(String filter) throws Exception {
        restBusExtensionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(busExtension.getId().intValue())))
            .andExpect(jsonPath("$.[*].hasGen").value(hasItem(DEFAULT_HAS_GEN)))
            .andExpect(jsonPath("$.[*].isLoad").value(hasItem(DEFAULT_IS_LOAD)))
            .andExpect(jsonPath("$.[*].snomMva").value(hasItem(DEFAULT_SNOM_MVA.doubleValue())))
            .andExpect(jsonPath("$.[*].sx").value(hasItem(DEFAULT_SX.doubleValue())))
            .andExpect(jsonPath("$.[*].sy").value(hasItem(DEFAULT_SY.doubleValue())))
            .andExpect(jsonPath("$.[*].gx").value(hasItem(DEFAULT_GX.doubleValue())))
            .andExpect(jsonPath("$.[*].gy").value(hasItem(DEFAULT_GY.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].incrementCost").value(hasItem(DEFAULT_INCREMENT_COST)))
            .andExpect(jsonPath("$.[*].decrementCost").value(hasItem(DEFAULT_DECREMENT_COST)))
            .andExpect(jsonPath("$.[*].mRid").value(hasItem(DEFAULT_M_RID)));

        // Check, that the count call also returns 1
        restBusExtensionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBusExtensionShouldNotBeFound(String filter) throws Exception {
        restBusExtensionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBusExtensionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBusExtension() throws Exception {
        // Get the busExtension
        restBusExtensionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBusExtension() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        int databaseSizeBeforeUpdate = busExtensionRepository.findAll().size();

        // Update the busExtension
        BusExtension updatedBusExtension = busExtensionRepository.findById(busExtension.getId()).get();
        // Disconnect from session so that the updates on updatedBusExtension are not directly saved in db
        em.detach(updatedBusExtension);
        updatedBusExtension
            .hasGen(UPDATED_HAS_GEN)
            .isLoad(UPDATED_IS_LOAD)
            .snomMva(UPDATED_SNOM_MVA)
            .sx(UPDATED_SX)
            .sy(UPDATED_SY)
            .gx(UPDATED_GX)
            .gy(UPDATED_GY)
            .status(UPDATED_STATUS)
            .incrementCost(UPDATED_INCREMENT_COST)
            .decrementCost(UPDATED_DECREMENT_COST)
            .mRid(UPDATED_M_RID);
        BusExtensionDTO busExtensionDTO = busExtensionMapper.toDto(updatedBusExtension);

        restBusExtensionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, busExtensionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busExtensionDTO))
            )
            .andExpect(status().isOk());

        // Validate the BusExtension in the database
        List<BusExtension> busExtensionList = busExtensionRepository.findAll();
        assertThat(busExtensionList).hasSize(databaseSizeBeforeUpdate);
        BusExtension testBusExtension = busExtensionList.get(busExtensionList.size() - 1);
        assertThat(testBusExtension.getHasGen()).isEqualTo(UPDATED_HAS_GEN);
        assertThat(testBusExtension.getIsLoad()).isEqualTo(UPDATED_IS_LOAD);
        assertThat(testBusExtension.getSnomMva()).isEqualTo(UPDATED_SNOM_MVA);
        assertThat(testBusExtension.getSx()).isEqualTo(UPDATED_SX);
        assertThat(testBusExtension.getSy()).isEqualTo(UPDATED_SY);
        assertThat(testBusExtension.getGx()).isEqualTo(UPDATED_GX);
        assertThat(testBusExtension.getGy()).isEqualTo(UPDATED_GY);
        assertThat(testBusExtension.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBusExtension.getIncrementCost()).isEqualTo(UPDATED_INCREMENT_COST);
        assertThat(testBusExtension.getDecrementCost()).isEqualTo(UPDATED_DECREMENT_COST);
        assertThat(testBusExtension.getmRid()).isEqualTo(UPDATED_M_RID);
    }

    @Test
    @Transactional
    void putNonExistingBusExtension() throws Exception {
        int databaseSizeBeforeUpdate = busExtensionRepository.findAll().size();
        busExtension.setId(count.incrementAndGet());

        // Create the BusExtension
        BusExtensionDTO busExtensionDTO = busExtensionMapper.toDto(busExtension);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusExtensionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, busExtensionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busExtensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusExtension in the database
        List<BusExtension> busExtensionList = busExtensionRepository.findAll();
        assertThat(busExtensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBusExtension() throws Exception {
        int databaseSizeBeforeUpdate = busExtensionRepository.findAll().size();
        busExtension.setId(count.incrementAndGet());

        // Create the BusExtension
        BusExtensionDTO busExtensionDTO = busExtensionMapper.toDto(busExtension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusExtensionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busExtensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusExtension in the database
        List<BusExtension> busExtensionList = busExtensionRepository.findAll();
        assertThat(busExtensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBusExtension() throws Exception {
        int databaseSizeBeforeUpdate = busExtensionRepository.findAll().size();
        busExtension.setId(count.incrementAndGet());

        // Create the BusExtension
        BusExtensionDTO busExtensionDTO = busExtensionMapper.toDto(busExtension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusExtensionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busExtensionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusExtension in the database
        List<BusExtension> busExtensionList = busExtensionRepository.findAll();
        assertThat(busExtensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBusExtensionWithPatch() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        int databaseSizeBeforeUpdate = busExtensionRepository.findAll().size();

        // Update the busExtension using partial update
        BusExtension partialUpdatedBusExtension = new BusExtension();
        partialUpdatedBusExtension.setId(busExtension.getId());

        partialUpdatedBusExtension
            .isLoad(UPDATED_IS_LOAD)
            .sx(UPDATED_SX)
            .sy(UPDATED_SY)
            .gx(UPDATED_GX)
            .status(UPDATED_STATUS)
            .incrementCost(UPDATED_INCREMENT_COST)
            .mRid(UPDATED_M_RID);

        restBusExtensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusExtension.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusExtension))
            )
            .andExpect(status().isOk());

        // Validate the BusExtension in the database
        List<BusExtension> busExtensionList = busExtensionRepository.findAll();
        assertThat(busExtensionList).hasSize(databaseSizeBeforeUpdate);
        BusExtension testBusExtension = busExtensionList.get(busExtensionList.size() - 1);
        assertThat(testBusExtension.getHasGen()).isEqualTo(DEFAULT_HAS_GEN);
        assertThat(testBusExtension.getIsLoad()).isEqualTo(UPDATED_IS_LOAD);
        assertThat(testBusExtension.getSnomMva()).isEqualTo(DEFAULT_SNOM_MVA);
        assertThat(testBusExtension.getSx()).isEqualTo(UPDATED_SX);
        assertThat(testBusExtension.getSy()).isEqualTo(UPDATED_SY);
        assertThat(testBusExtension.getGx()).isEqualTo(UPDATED_GX);
        assertThat(testBusExtension.getGy()).isEqualTo(DEFAULT_GY);
        assertThat(testBusExtension.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBusExtension.getIncrementCost()).isEqualTo(UPDATED_INCREMENT_COST);
        assertThat(testBusExtension.getDecrementCost()).isEqualTo(DEFAULT_DECREMENT_COST);
        assertThat(testBusExtension.getmRid()).isEqualTo(UPDATED_M_RID);
    }

    @Test
    @Transactional
    void fullUpdateBusExtensionWithPatch() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        int databaseSizeBeforeUpdate = busExtensionRepository.findAll().size();

        // Update the busExtension using partial update
        BusExtension partialUpdatedBusExtension = new BusExtension();
        partialUpdatedBusExtension.setId(busExtension.getId());

        partialUpdatedBusExtension
            .hasGen(UPDATED_HAS_GEN)
            .isLoad(UPDATED_IS_LOAD)
            .snomMva(UPDATED_SNOM_MVA)
            .sx(UPDATED_SX)
            .sy(UPDATED_SY)
            .gx(UPDATED_GX)
            .gy(UPDATED_GY)
            .status(UPDATED_STATUS)
            .incrementCost(UPDATED_INCREMENT_COST)
            .decrementCost(UPDATED_DECREMENT_COST)
            .mRid(UPDATED_M_RID);

        restBusExtensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusExtension.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusExtension))
            )
            .andExpect(status().isOk());

        // Validate the BusExtension in the database
        List<BusExtension> busExtensionList = busExtensionRepository.findAll();
        assertThat(busExtensionList).hasSize(databaseSizeBeforeUpdate);
        BusExtension testBusExtension = busExtensionList.get(busExtensionList.size() - 1);
        assertThat(testBusExtension.getHasGen()).isEqualTo(UPDATED_HAS_GEN);
        assertThat(testBusExtension.getIsLoad()).isEqualTo(UPDATED_IS_LOAD);
        assertThat(testBusExtension.getSnomMva()).isEqualTo(UPDATED_SNOM_MVA);
        assertThat(testBusExtension.getSx()).isEqualTo(UPDATED_SX);
        assertThat(testBusExtension.getSy()).isEqualTo(UPDATED_SY);
        assertThat(testBusExtension.getGx()).isEqualTo(UPDATED_GX);
        assertThat(testBusExtension.getGy()).isEqualTo(UPDATED_GY);
        assertThat(testBusExtension.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBusExtension.getIncrementCost()).isEqualTo(UPDATED_INCREMENT_COST);
        assertThat(testBusExtension.getDecrementCost()).isEqualTo(UPDATED_DECREMENT_COST);
        assertThat(testBusExtension.getmRid()).isEqualTo(UPDATED_M_RID);
    }

    @Test
    @Transactional
    void patchNonExistingBusExtension() throws Exception {
        int databaseSizeBeforeUpdate = busExtensionRepository.findAll().size();
        busExtension.setId(count.incrementAndGet());

        // Create the BusExtension
        BusExtensionDTO busExtensionDTO = busExtensionMapper.toDto(busExtension);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusExtensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, busExtensionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(busExtensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusExtension in the database
        List<BusExtension> busExtensionList = busExtensionRepository.findAll();
        assertThat(busExtensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBusExtension() throws Exception {
        int databaseSizeBeforeUpdate = busExtensionRepository.findAll().size();
        busExtension.setId(count.incrementAndGet());

        // Create the BusExtension
        BusExtensionDTO busExtensionDTO = busExtensionMapper.toDto(busExtension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusExtensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(busExtensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusExtension in the database
        List<BusExtension> busExtensionList = busExtensionRepository.findAll();
        assertThat(busExtensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBusExtension() throws Exception {
        int databaseSizeBeforeUpdate = busExtensionRepository.findAll().size();
        busExtension.setId(count.incrementAndGet());

        // Create the BusExtension
        BusExtensionDTO busExtensionDTO = busExtensionMapper.toDto(busExtension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusExtensionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(busExtensionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusExtension in the database
        List<BusExtension> busExtensionList = busExtensionRepository.findAll();
        assertThat(busExtensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBusExtension() throws Exception {
        // Initialize the database
        busExtensionRepository.saveAndFlush(busExtension);

        int databaseSizeBeforeDelete = busExtensionRepository.findAll().size();

        // Delete the busExtension
        restBusExtensionMockMvc
            .perform(delete(ENTITY_API_URL_ID, busExtension.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BusExtension> busExtensionList = busExtensionRepository.findAll();
        assertThat(busExtensionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
