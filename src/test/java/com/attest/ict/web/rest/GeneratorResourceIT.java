package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.GenCost;
import com.attest.ict.domain.GenElVal;
import com.attest.ict.domain.GenTag;
import com.attest.ict.domain.Generator;
import com.attest.ict.domain.GeneratorExtension;
import com.attest.ict.domain.Network;
import com.attest.ict.repository.GeneratorRepository;
import com.attest.ict.service.criteria.GeneratorCriteria;
import com.attest.ict.service.dto.GeneratorDTO;
import com.attest.ict.service.mapper.GeneratorMapper;
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
 * Integration tests for the {@link GeneratorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GeneratorResourceIT {

    private static final Long DEFAULT_BUS_NUM = 1L;
    private static final Long UPDATED_BUS_NUM = 2L;
    private static final Long SMALLER_BUS_NUM = 1L - 1L;

    private static final Double DEFAULT_PG = 1D;
    private static final Double UPDATED_PG = 2D;
    private static final Double SMALLER_PG = 1D - 1D;

    private static final Double DEFAULT_QG = 1D;
    private static final Double UPDATED_QG = 2D;
    private static final Double SMALLER_QG = 1D - 1D;

    private static final Double DEFAULT_QMAX = 1D;
    private static final Double UPDATED_QMAX = 2D;
    private static final Double SMALLER_QMAX = 1D - 1D;

    private static final Double DEFAULT_QMIN = 1D;
    private static final Double UPDATED_QMIN = 2D;
    private static final Double SMALLER_QMIN = 1D - 1D;

    private static final Double DEFAULT_VG = 1D;
    private static final Double UPDATED_VG = 2D;
    private static final Double SMALLER_VG = 1D - 1D;

    private static final Double DEFAULT_M_BASE = 1D;
    private static final Double UPDATED_M_BASE = 2D;
    private static final Double SMALLER_M_BASE = 1D - 1D;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;
    private static final Integer SMALLER_STATUS = 1 - 1;

    private static final Double DEFAULT_PMAX = 1D;
    private static final Double UPDATED_PMAX = 2D;
    private static final Double SMALLER_PMAX = 1D - 1D;

    private static final Double DEFAULT_PMIN = 1D;
    private static final Double UPDATED_PMIN = 2D;
    private static final Double SMALLER_PMIN = 1D - 1D;

    private static final Double DEFAULT_PC_1 = 1D;
    private static final Double UPDATED_PC_1 = 2D;
    private static final Double SMALLER_PC_1 = 1D - 1D;

    private static final Double DEFAULT_PC_2 = 1D;
    private static final Double UPDATED_PC_2 = 2D;
    private static final Double SMALLER_PC_2 = 1D - 1D;

    private static final Double DEFAULT_QC_1_MIN = 1D;
    private static final Double UPDATED_QC_1_MIN = 2D;
    private static final Double SMALLER_QC_1_MIN = 1D - 1D;

    private static final Double DEFAULT_QC_1_MAX = 1D;
    private static final Double UPDATED_QC_1_MAX = 2D;
    private static final Double SMALLER_QC_1_MAX = 1D - 1D;

    private static final Double DEFAULT_QC_2_MIN = 1D;
    private static final Double UPDATED_QC_2_MIN = 2D;
    private static final Double SMALLER_QC_2_MIN = 1D - 1D;

    private static final Double DEFAULT_QC_2_MAX = 1D;
    private static final Double UPDATED_QC_2_MAX = 2D;
    private static final Double SMALLER_QC_2_MAX = 1D - 1D;

    private static final Double DEFAULT_RAMP_AGC = 1D;
    private static final Double UPDATED_RAMP_AGC = 2D;
    private static final Double SMALLER_RAMP_AGC = 1D - 1D;

    private static final Double DEFAULT_RAMP_10 = 1D;
    private static final Double UPDATED_RAMP_10 = 2D;
    private static final Double SMALLER_RAMP_10 = 1D - 1D;

    private static final Double DEFAULT_RAMP_30 = 1D;
    private static final Double UPDATED_RAMP_30 = 2D;
    private static final Double SMALLER_RAMP_30 = 1D - 1D;

    private static final Double DEFAULT_RAMP_Q = 1D;
    private static final Double UPDATED_RAMP_Q = 2D;
    private static final Double SMALLER_RAMP_Q = 1D - 1D;

    private static final Long DEFAULT_APF = 1L;
    private static final Long UPDATED_APF = 2L;
    private static final Long SMALLER_APF = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/generators";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GeneratorRepository generatorRepository;

    @Autowired
    private GeneratorMapper generatorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGeneratorMockMvc;

    private Generator generator;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Generator createEntity(EntityManager em) {
        Generator generator = new Generator()
            .busNum(DEFAULT_BUS_NUM)
            .pg(DEFAULT_PG)
            .qg(DEFAULT_QG)
            .qmax(DEFAULT_QMAX)
            .qmin(DEFAULT_QMIN)
            .vg(DEFAULT_VG)
            .mBase(DEFAULT_M_BASE)
            .status(DEFAULT_STATUS)
            .pmax(DEFAULT_PMAX)
            .pmin(DEFAULT_PMIN)
            .pc1(DEFAULT_PC_1)
            .pc2(DEFAULT_PC_2)
            .qc1min(DEFAULT_QC_1_MIN)
            .qc1max(DEFAULT_QC_1_MAX)
            .qc2min(DEFAULT_QC_2_MIN)
            .qc2max(DEFAULT_QC_2_MAX)
            .rampAgc(DEFAULT_RAMP_AGC)
            .ramp10(DEFAULT_RAMP_10)
            .ramp30(DEFAULT_RAMP_30)
            .rampQ(DEFAULT_RAMP_Q)
            .apf(DEFAULT_APF);
        return generator;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Generator createUpdatedEntity(EntityManager em) {
        Generator generator = new Generator()
            .busNum(UPDATED_BUS_NUM)
            .pg(UPDATED_PG)
            .qg(UPDATED_QG)
            .qmax(UPDATED_QMAX)
            .qmin(UPDATED_QMIN)
            .vg(UPDATED_VG)
            .mBase(UPDATED_M_BASE)
            .status(UPDATED_STATUS)
            .pmax(UPDATED_PMAX)
            .pmin(UPDATED_PMIN)
            .pc1(UPDATED_PC_1)
            .pc2(UPDATED_PC_2)
            .qc1min(UPDATED_QC_1_MIN)
            .qc1max(UPDATED_QC_1_MAX)
            .qc2min(UPDATED_QC_2_MIN)
            .qc2max(UPDATED_QC_2_MAX)
            .rampAgc(UPDATED_RAMP_AGC)
            .ramp10(UPDATED_RAMP_10)
            .ramp30(UPDATED_RAMP_30)
            .rampQ(UPDATED_RAMP_Q)
            .apf(UPDATED_APF);
        return generator;
    }

    @BeforeEach
    public void initTest() {
        generator = createEntity(em);
    }

    @Test
    @Transactional
    void createGenerator() throws Exception {
        int databaseSizeBeforeCreate = generatorRepository.findAll().size();
        // Create the Generator
        GeneratorDTO generatorDTO = generatorMapper.toDto(generator);
        restGeneratorMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generatorDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Generator in the database
        List<Generator> generatorList = generatorRepository.findAll();
        assertThat(generatorList).hasSize(databaseSizeBeforeCreate + 1);
        Generator testGenerator = generatorList.get(generatorList.size() - 1);
        assertThat(testGenerator.getBusNum()).isEqualTo(DEFAULT_BUS_NUM);
        assertThat(testGenerator.getPg()).isEqualTo(DEFAULT_PG);
        assertThat(testGenerator.getQg()).isEqualTo(DEFAULT_QG);
        assertThat(testGenerator.getQmax()).isEqualTo(DEFAULT_QMAX);
        assertThat(testGenerator.getQmin()).isEqualTo(DEFAULT_QMIN);
        assertThat(testGenerator.getVg()).isEqualTo(DEFAULT_VG);
        assertThat(testGenerator.getmBase()).isEqualTo(DEFAULT_M_BASE);
        assertThat(testGenerator.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testGenerator.getPmax()).isEqualTo(DEFAULT_PMAX);
        assertThat(testGenerator.getPmin()).isEqualTo(DEFAULT_PMIN);
        assertThat(testGenerator.getPc1()).isEqualTo(DEFAULT_PC_1);
        assertThat(testGenerator.getPc2()).isEqualTo(DEFAULT_PC_2);
        assertThat(testGenerator.getQc1min()).isEqualTo(DEFAULT_QC_1_MIN);
        assertThat(testGenerator.getQc1max()).isEqualTo(DEFAULT_QC_1_MAX);
        assertThat(testGenerator.getQc2min()).isEqualTo(DEFAULT_QC_2_MIN);
        assertThat(testGenerator.getQc2max()).isEqualTo(DEFAULT_QC_2_MAX);
        assertThat(testGenerator.getRampAgc()).isEqualTo(DEFAULT_RAMP_AGC);
        assertThat(testGenerator.getRamp10()).isEqualTo(DEFAULT_RAMP_10);
        assertThat(testGenerator.getRamp30()).isEqualTo(DEFAULT_RAMP_30);
        assertThat(testGenerator.getRampQ()).isEqualTo(DEFAULT_RAMP_Q);
        assertThat(testGenerator.getApf()).isEqualTo(DEFAULT_APF);
    }

    @Test
    @Transactional
    void createGeneratorWithExistingId() throws Exception {
        // Create the Generator with an existing ID
        generator.setId(1L);
        GeneratorDTO generatorDTO = generatorMapper.toDto(generator);

        int databaseSizeBeforeCreate = generatorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeneratorMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Generator in the database
        List<Generator> generatorList = generatorRepository.findAll();
        assertThat(generatorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGenerators() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList
        restGeneratorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(generator.getId().intValue())))
            .andExpect(jsonPath("$.[*].busNum").value(hasItem(DEFAULT_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].pg").value(hasItem(DEFAULT_PG.doubleValue())))
            .andExpect(jsonPath("$.[*].qg").value(hasItem(DEFAULT_QG.doubleValue())))
            .andExpect(jsonPath("$.[*].qmax").value(hasItem(DEFAULT_QMAX.doubleValue())))
            .andExpect(jsonPath("$.[*].qmin").value(hasItem(DEFAULT_QMIN.doubleValue())))
            .andExpect(jsonPath("$.[*].vg").value(hasItem(DEFAULT_VG.doubleValue())))
            .andExpect(jsonPath("$.[*].mBase").value(hasItem(DEFAULT_M_BASE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].pmax").value(hasItem(DEFAULT_PMAX.doubleValue())))
            .andExpect(jsonPath("$.[*].pmin").value(hasItem(DEFAULT_PMIN.doubleValue())))
            .andExpect(jsonPath("$.[*].pc1").value(hasItem(DEFAULT_PC_1.doubleValue())))
            .andExpect(jsonPath("$.[*].pc2").value(hasItem(DEFAULT_PC_2.doubleValue())))
            .andExpect(jsonPath("$.[*].qc1min").value(hasItem(DEFAULT_QC_1_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].qc1max").value(hasItem(DEFAULT_QC_1_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].qc2min").value(hasItem(DEFAULT_QC_2_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].qc2max").value(hasItem(DEFAULT_QC_2_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].rampAgc").value(hasItem(DEFAULT_RAMP_AGC.doubleValue())))
            .andExpect(jsonPath("$.[*].ramp10").value(hasItem(DEFAULT_RAMP_10.doubleValue())))
            .andExpect(jsonPath("$.[*].ramp30").value(hasItem(DEFAULT_RAMP_30.doubleValue())))
            .andExpect(jsonPath("$.[*].rampQ").value(hasItem(DEFAULT_RAMP_Q.doubleValue())))
            .andExpect(jsonPath("$.[*].apf").value(hasItem(DEFAULT_APF.intValue())));
    }

    @Test
    @Transactional
    void getGenerator() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get the generator
        restGeneratorMockMvc
            .perform(get(ENTITY_API_URL_ID, generator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(generator.getId().intValue()))
            .andExpect(jsonPath("$.busNum").value(DEFAULT_BUS_NUM.intValue()))
            .andExpect(jsonPath("$.pg").value(DEFAULT_PG.doubleValue()))
            .andExpect(jsonPath("$.qg").value(DEFAULT_QG.doubleValue()))
            .andExpect(jsonPath("$.qmax").value(DEFAULT_QMAX.doubleValue()))
            .andExpect(jsonPath("$.qmin").value(DEFAULT_QMIN.doubleValue()))
            .andExpect(jsonPath("$.vg").value(DEFAULT_VG.doubleValue()))
            .andExpect(jsonPath("$.mBase").value(DEFAULT_M_BASE.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.pmax").value(DEFAULT_PMAX.doubleValue()))
            .andExpect(jsonPath("$.pmin").value(DEFAULT_PMIN.doubleValue()))
            .andExpect(jsonPath("$.pc1").value(DEFAULT_PC_1.doubleValue()))
            .andExpect(jsonPath("$.pc2").value(DEFAULT_PC_2.doubleValue()))
            .andExpect(jsonPath("$.qc1min").value(DEFAULT_QC_1_MIN.doubleValue()))
            .andExpect(jsonPath("$.qc1max").value(DEFAULT_QC_1_MAX.doubleValue()))
            .andExpect(jsonPath("$.qc2min").value(DEFAULT_QC_2_MIN.doubleValue()))
            .andExpect(jsonPath("$.qc2max").value(DEFAULT_QC_2_MAX.doubleValue()))
            .andExpect(jsonPath("$.rampAgc").value(DEFAULT_RAMP_AGC.doubleValue()))
            .andExpect(jsonPath("$.ramp10").value(DEFAULT_RAMP_10.doubleValue()))
            .andExpect(jsonPath("$.ramp30").value(DEFAULT_RAMP_30.doubleValue()))
            .andExpect(jsonPath("$.rampQ").value(DEFAULT_RAMP_Q.doubleValue()))
            .andExpect(jsonPath("$.apf").value(DEFAULT_APF.intValue()));
    }

    @Test
    @Transactional
    void getGeneratorsByIdFiltering() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        Long id = generator.getId();

        defaultGeneratorShouldBeFound("id.equals=" + id);
        defaultGeneratorShouldNotBeFound("id.notEquals=" + id);

        defaultGeneratorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGeneratorShouldNotBeFound("id.greaterThan=" + id);

        defaultGeneratorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGeneratorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGeneratorsByBusNumIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where busNum equals to DEFAULT_BUS_NUM
        defaultGeneratorShouldBeFound("busNum.equals=" + DEFAULT_BUS_NUM);

        // Get all the generatorList where busNum equals to UPDATED_BUS_NUM
        defaultGeneratorShouldNotBeFound("busNum.equals=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllGeneratorsByBusNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where busNum not equals to DEFAULT_BUS_NUM
        defaultGeneratorShouldNotBeFound("busNum.notEquals=" + DEFAULT_BUS_NUM);

        // Get all the generatorList where busNum not equals to UPDATED_BUS_NUM
        defaultGeneratorShouldBeFound("busNum.notEquals=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllGeneratorsByBusNumIsInShouldWork() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where busNum in DEFAULT_BUS_NUM or UPDATED_BUS_NUM
        defaultGeneratorShouldBeFound("busNum.in=" + DEFAULT_BUS_NUM + "," + UPDATED_BUS_NUM);

        // Get all the generatorList where busNum equals to UPDATED_BUS_NUM
        defaultGeneratorShouldNotBeFound("busNum.in=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllGeneratorsByBusNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where busNum is not null
        defaultGeneratorShouldBeFound("busNum.specified=true");

        // Get all the generatorList where busNum is null
        defaultGeneratorShouldNotBeFound("busNum.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorsByBusNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where busNum is greater than or equal to DEFAULT_BUS_NUM
        defaultGeneratorShouldBeFound("busNum.greaterThanOrEqual=" + DEFAULT_BUS_NUM);

        // Get all the generatorList where busNum is greater than or equal to UPDATED_BUS_NUM
        defaultGeneratorShouldNotBeFound("busNum.greaterThanOrEqual=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllGeneratorsByBusNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where busNum is less than or equal to DEFAULT_BUS_NUM
        defaultGeneratorShouldBeFound("busNum.lessThanOrEqual=" + DEFAULT_BUS_NUM);

        // Get all the generatorList where busNum is less than or equal to SMALLER_BUS_NUM
        defaultGeneratorShouldNotBeFound("busNum.lessThanOrEqual=" + SMALLER_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllGeneratorsByBusNumIsLessThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where busNum is less than DEFAULT_BUS_NUM
        defaultGeneratorShouldNotBeFound("busNum.lessThan=" + DEFAULT_BUS_NUM);

        // Get all the generatorList where busNum is less than UPDATED_BUS_NUM
        defaultGeneratorShouldBeFound("busNum.lessThan=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllGeneratorsByBusNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where busNum is greater than DEFAULT_BUS_NUM
        defaultGeneratorShouldNotBeFound("busNum.greaterThan=" + DEFAULT_BUS_NUM);

        // Get all the generatorList where busNum is greater than SMALLER_BUS_NUM
        defaultGeneratorShouldBeFound("busNum.greaterThan=" + SMALLER_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPgIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pg equals to DEFAULT_PG
        defaultGeneratorShouldBeFound("pg.equals=" + DEFAULT_PG);

        // Get all the generatorList where pg equals to UPDATED_PG
        defaultGeneratorShouldNotBeFound("pg.equals=" + UPDATED_PG);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPgIsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pg not equals to DEFAULT_PG
        defaultGeneratorShouldNotBeFound("pg.notEquals=" + DEFAULT_PG);

        // Get all the generatorList where pg not equals to UPDATED_PG
        defaultGeneratorShouldBeFound("pg.notEquals=" + UPDATED_PG);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPgIsInShouldWork() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pg in DEFAULT_PG or UPDATED_PG
        defaultGeneratorShouldBeFound("pg.in=" + DEFAULT_PG + "," + UPDATED_PG);

        // Get all the generatorList where pg equals to UPDATED_PG
        defaultGeneratorShouldNotBeFound("pg.in=" + UPDATED_PG);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPgIsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pg is not null
        defaultGeneratorShouldBeFound("pg.specified=true");

        // Get all the generatorList where pg is null
        defaultGeneratorShouldNotBeFound("pg.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorsByPgIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pg is greater than or equal to DEFAULT_PG
        defaultGeneratorShouldBeFound("pg.greaterThanOrEqual=" + DEFAULT_PG);

        // Get all the generatorList where pg is greater than or equal to UPDATED_PG
        defaultGeneratorShouldNotBeFound("pg.greaterThanOrEqual=" + UPDATED_PG);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPgIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pg is less than or equal to DEFAULT_PG
        defaultGeneratorShouldBeFound("pg.lessThanOrEqual=" + DEFAULT_PG);

        // Get all the generatorList where pg is less than or equal to SMALLER_PG
        defaultGeneratorShouldNotBeFound("pg.lessThanOrEqual=" + SMALLER_PG);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPgIsLessThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pg is less than DEFAULT_PG
        defaultGeneratorShouldNotBeFound("pg.lessThan=" + DEFAULT_PG);

        // Get all the generatorList where pg is less than UPDATED_PG
        defaultGeneratorShouldBeFound("pg.lessThan=" + UPDATED_PG);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPgIsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pg is greater than DEFAULT_PG
        defaultGeneratorShouldNotBeFound("pg.greaterThan=" + DEFAULT_PG);

        // Get all the generatorList where pg is greater than SMALLER_PG
        defaultGeneratorShouldBeFound("pg.greaterThan=" + SMALLER_PG);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQgIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qg equals to DEFAULT_QG
        defaultGeneratorShouldBeFound("qg.equals=" + DEFAULT_QG);

        // Get all the generatorList where qg equals to UPDATED_QG
        defaultGeneratorShouldNotBeFound("qg.equals=" + UPDATED_QG);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQgIsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qg not equals to DEFAULT_QG
        defaultGeneratorShouldNotBeFound("qg.notEquals=" + DEFAULT_QG);

        // Get all the generatorList where qg not equals to UPDATED_QG
        defaultGeneratorShouldBeFound("qg.notEquals=" + UPDATED_QG);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQgIsInShouldWork() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qg in DEFAULT_QG or UPDATED_QG
        defaultGeneratorShouldBeFound("qg.in=" + DEFAULT_QG + "," + UPDATED_QG);

        // Get all the generatorList where qg equals to UPDATED_QG
        defaultGeneratorShouldNotBeFound("qg.in=" + UPDATED_QG);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQgIsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qg is not null
        defaultGeneratorShouldBeFound("qg.specified=true");

        // Get all the generatorList where qg is null
        defaultGeneratorShouldNotBeFound("qg.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorsByQgIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qg is greater than or equal to DEFAULT_QG
        defaultGeneratorShouldBeFound("qg.greaterThanOrEqual=" + DEFAULT_QG);

        // Get all the generatorList where qg is greater than or equal to UPDATED_QG
        defaultGeneratorShouldNotBeFound("qg.greaterThanOrEqual=" + UPDATED_QG);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQgIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qg is less than or equal to DEFAULT_QG
        defaultGeneratorShouldBeFound("qg.lessThanOrEqual=" + DEFAULT_QG);

        // Get all the generatorList where qg is less than or equal to SMALLER_QG
        defaultGeneratorShouldNotBeFound("qg.lessThanOrEqual=" + SMALLER_QG);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQgIsLessThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qg is less than DEFAULT_QG
        defaultGeneratorShouldNotBeFound("qg.lessThan=" + DEFAULT_QG);

        // Get all the generatorList where qg is less than UPDATED_QG
        defaultGeneratorShouldBeFound("qg.lessThan=" + UPDATED_QG);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQgIsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qg is greater than DEFAULT_QG
        defaultGeneratorShouldNotBeFound("qg.greaterThan=" + DEFAULT_QG);

        // Get all the generatorList where qg is greater than SMALLER_QG
        defaultGeneratorShouldBeFound("qg.greaterThan=" + SMALLER_QG);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQmaxIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qmax equals to DEFAULT_QMAX
        defaultGeneratorShouldBeFound("qmax.equals=" + DEFAULT_QMAX);

        // Get all the generatorList where qmax equals to UPDATED_QMAX
        defaultGeneratorShouldNotBeFound("qmax.equals=" + UPDATED_QMAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQmaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qmax not equals to DEFAULT_QMAX
        defaultGeneratorShouldNotBeFound("qmax.notEquals=" + DEFAULT_QMAX);

        // Get all the generatorList where qmax not equals to UPDATED_QMAX
        defaultGeneratorShouldBeFound("qmax.notEquals=" + UPDATED_QMAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQmaxIsInShouldWork() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qmax in DEFAULT_QMAX or UPDATED_QMAX
        defaultGeneratorShouldBeFound("qmax.in=" + DEFAULT_QMAX + "," + UPDATED_QMAX);

        // Get all the generatorList where qmax equals to UPDATED_QMAX
        defaultGeneratorShouldNotBeFound("qmax.in=" + UPDATED_QMAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQmaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qmax is not null
        defaultGeneratorShouldBeFound("qmax.specified=true");

        // Get all the generatorList where qmax is null
        defaultGeneratorShouldNotBeFound("qmax.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorsByQmaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qmax is greater than or equal to DEFAULT_QMAX
        defaultGeneratorShouldBeFound("qmax.greaterThanOrEqual=" + DEFAULT_QMAX);

        // Get all the generatorList where qmax is greater than or equal to UPDATED_QMAX
        defaultGeneratorShouldNotBeFound("qmax.greaterThanOrEqual=" + UPDATED_QMAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQmaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qmax is less than or equal to DEFAULT_QMAX
        defaultGeneratorShouldBeFound("qmax.lessThanOrEqual=" + DEFAULT_QMAX);

        // Get all the generatorList where qmax is less than or equal to SMALLER_QMAX
        defaultGeneratorShouldNotBeFound("qmax.lessThanOrEqual=" + SMALLER_QMAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQmaxIsLessThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qmax is less than DEFAULT_QMAX
        defaultGeneratorShouldNotBeFound("qmax.lessThan=" + DEFAULT_QMAX);

        // Get all the generatorList where qmax is less than UPDATED_QMAX
        defaultGeneratorShouldBeFound("qmax.lessThan=" + UPDATED_QMAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQmaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qmax is greater than DEFAULT_QMAX
        defaultGeneratorShouldNotBeFound("qmax.greaterThan=" + DEFAULT_QMAX);

        // Get all the generatorList where qmax is greater than SMALLER_QMAX
        defaultGeneratorShouldBeFound("qmax.greaterThan=" + SMALLER_QMAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQminIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qmin equals to DEFAULT_QMIN
        defaultGeneratorShouldBeFound("qmin.equals=" + DEFAULT_QMIN);

        // Get all the generatorList where qmin equals to UPDATED_QMIN
        defaultGeneratorShouldNotBeFound("qmin.equals=" + UPDATED_QMIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQminIsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qmin not equals to DEFAULT_QMIN
        defaultGeneratorShouldNotBeFound("qmin.notEquals=" + DEFAULT_QMIN);

        // Get all the generatorList where qmin not equals to UPDATED_QMIN
        defaultGeneratorShouldBeFound("qmin.notEquals=" + UPDATED_QMIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQminIsInShouldWork() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qmin in DEFAULT_QMIN or UPDATED_QMIN
        defaultGeneratorShouldBeFound("qmin.in=" + DEFAULT_QMIN + "," + UPDATED_QMIN);

        // Get all the generatorList where qmin equals to UPDATED_QMIN
        defaultGeneratorShouldNotBeFound("qmin.in=" + UPDATED_QMIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQminIsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qmin is not null
        defaultGeneratorShouldBeFound("qmin.specified=true");

        // Get all the generatorList where qmin is null
        defaultGeneratorShouldNotBeFound("qmin.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorsByQminIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qmin is greater than or equal to DEFAULT_QMIN
        defaultGeneratorShouldBeFound("qmin.greaterThanOrEqual=" + DEFAULT_QMIN);

        // Get all the generatorList where qmin is greater than or equal to UPDATED_QMIN
        defaultGeneratorShouldNotBeFound("qmin.greaterThanOrEqual=" + UPDATED_QMIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQminIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qmin is less than or equal to DEFAULT_QMIN
        defaultGeneratorShouldBeFound("qmin.lessThanOrEqual=" + DEFAULT_QMIN);

        // Get all the generatorList where qmin is less than or equal to SMALLER_QMIN
        defaultGeneratorShouldNotBeFound("qmin.lessThanOrEqual=" + SMALLER_QMIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQminIsLessThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qmin is less than DEFAULT_QMIN
        defaultGeneratorShouldNotBeFound("qmin.lessThan=" + DEFAULT_QMIN);

        // Get all the generatorList where qmin is less than UPDATED_QMIN
        defaultGeneratorShouldBeFound("qmin.lessThan=" + UPDATED_QMIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQminIsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qmin is greater than DEFAULT_QMIN
        defaultGeneratorShouldNotBeFound("qmin.greaterThan=" + DEFAULT_QMIN);

        // Get all the generatorList where qmin is greater than SMALLER_QMIN
        defaultGeneratorShouldBeFound("qmin.greaterThan=" + SMALLER_QMIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByVgIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where vg equals to DEFAULT_VG
        defaultGeneratorShouldBeFound("vg.equals=" + DEFAULT_VG);

        // Get all the generatorList where vg equals to UPDATED_VG
        defaultGeneratorShouldNotBeFound("vg.equals=" + UPDATED_VG);
    }

    @Test
    @Transactional
    void getAllGeneratorsByVgIsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where vg not equals to DEFAULT_VG
        defaultGeneratorShouldNotBeFound("vg.notEquals=" + DEFAULT_VG);

        // Get all the generatorList where vg not equals to UPDATED_VG
        defaultGeneratorShouldBeFound("vg.notEquals=" + UPDATED_VG);
    }

    @Test
    @Transactional
    void getAllGeneratorsByVgIsInShouldWork() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where vg in DEFAULT_VG or UPDATED_VG
        defaultGeneratorShouldBeFound("vg.in=" + DEFAULT_VG + "," + UPDATED_VG);

        // Get all the generatorList where vg equals to UPDATED_VG
        defaultGeneratorShouldNotBeFound("vg.in=" + UPDATED_VG);
    }

    @Test
    @Transactional
    void getAllGeneratorsByVgIsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where vg is not null
        defaultGeneratorShouldBeFound("vg.specified=true");

        // Get all the generatorList where vg is null
        defaultGeneratorShouldNotBeFound("vg.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorsByVgIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where vg is greater than or equal to DEFAULT_VG
        defaultGeneratorShouldBeFound("vg.greaterThanOrEqual=" + DEFAULT_VG);

        // Get all the generatorList where vg is greater than or equal to UPDATED_VG
        defaultGeneratorShouldNotBeFound("vg.greaterThanOrEqual=" + UPDATED_VG);
    }

    @Test
    @Transactional
    void getAllGeneratorsByVgIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where vg is less than or equal to DEFAULT_VG
        defaultGeneratorShouldBeFound("vg.lessThanOrEqual=" + DEFAULT_VG);

        // Get all the generatorList where vg is less than or equal to SMALLER_VG
        defaultGeneratorShouldNotBeFound("vg.lessThanOrEqual=" + SMALLER_VG);
    }

    @Test
    @Transactional
    void getAllGeneratorsByVgIsLessThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where vg is less than DEFAULT_VG
        defaultGeneratorShouldNotBeFound("vg.lessThan=" + DEFAULT_VG);

        // Get all the generatorList where vg is less than UPDATED_VG
        defaultGeneratorShouldBeFound("vg.lessThan=" + UPDATED_VG);
    }

    @Test
    @Transactional
    void getAllGeneratorsByVgIsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where vg is greater than DEFAULT_VG
        defaultGeneratorShouldNotBeFound("vg.greaterThan=" + DEFAULT_VG);

        // Get all the generatorList where vg is greater than SMALLER_VG
        defaultGeneratorShouldBeFound("vg.greaterThan=" + SMALLER_VG);
    }

    @Test
    @Transactional
    void getAllGeneratorsBymBaseIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where mBase equals to DEFAULT_M_BASE
        defaultGeneratorShouldBeFound("mBase.equals=" + DEFAULT_M_BASE);

        // Get all the generatorList where mBase equals to UPDATED_M_BASE
        defaultGeneratorShouldNotBeFound("mBase.equals=" + UPDATED_M_BASE);
    }

    @Test
    @Transactional
    void getAllGeneratorsBymBaseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where mBase not equals to DEFAULT_M_BASE
        defaultGeneratorShouldNotBeFound("mBase.notEquals=" + DEFAULT_M_BASE);

        // Get all the generatorList where mBase not equals to UPDATED_M_BASE
        defaultGeneratorShouldBeFound("mBase.notEquals=" + UPDATED_M_BASE);
    }

    @Test
    @Transactional
    void getAllGeneratorsBymBaseIsInShouldWork() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where mBase in DEFAULT_M_BASE or UPDATED_M_BASE
        defaultGeneratorShouldBeFound("mBase.in=" + DEFAULT_M_BASE + "," + UPDATED_M_BASE);

        // Get all the generatorList where mBase equals to UPDATED_M_BASE
        defaultGeneratorShouldNotBeFound("mBase.in=" + UPDATED_M_BASE);
    }

    @Test
    @Transactional
    void getAllGeneratorsBymBaseIsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where mBase is not null
        defaultGeneratorShouldBeFound("mBase.specified=true");

        // Get all the generatorList where mBase is null
        defaultGeneratorShouldNotBeFound("mBase.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorsBymBaseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where mBase is greater than or equal to DEFAULT_M_BASE
        defaultGeneratorShouldBeFound("mBase.greaterThanOrEqual=" + DEFAULT_M_BASE);

        // Get all the generatorList where mBase is greater than or equal to UPDATED_M_BASE
        defaultGeneratorShouldNotBeFound("mBase.greaterThanOrEqual=" + UPDATED_M_BASE);
    }

    @Test
    @Transactional
    void getAllGeneratorsBymBaseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where mBase is less than or equal to DEFAULT_M_BASE
        defaultGeneratorShouldBeFound("mBase.lessThanOrEqual=" + DEFAULT_M_BASE);

        // Get all the generatorList where mBase is less than or equal to SMALLER_M_BASE
        defaultGeneratorShouldNotBeFound("mBase.lessThanOrEqual=" + SMALLER_M_BASE);
    }

    @Test
    @Transactional
    void getAllGeneratorsBymBaseIsLessThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where mBase is less than DEFAULT_M_BASE
        defaultGeneratorShouldNotBeFound("mBase.lessThan=" + DEFAULT_M_BASE);

        // Get all the generatorList where mBase is less than UPDATED_M_BASE
        defaultGeneratorShouldBeFound("mBase.lessThan=" + UPDATED_M_BASE);
    }

    @Test
    @Transactional
    void getAllGeneratorsBymBaseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where mBase is greater than DEFAULT_M_BASE
        defaultGeneratorShouldNotBeFound("mBase.greaterThan=" + DEFAULT_M_BASE);

        // Get all the generatorList where mBase is greater than SMALLER_M_BASE
        defaultGeneratorShouldBeFound("mBase.greaterThan=" + SMALLER_M_BASE);
    }

    @Test
    @Transactional
    void getAllGeneratorsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where status equals to DEFAULT_STATUS
        defaultGeneratorShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the generatorList where status equals to UPDATED_STATUS
        defaultGeneratorShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGeneratorsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where status not equals to DEFAULT_STATUS
        defaultGeneratorShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the generatorList where status not equals to UPDATED_STATUS
        defaultGeneratorShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGeneratorsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultGeneratorShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the generatorList where status equals to UPDATED_STATUS
        defaultGeneratorShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGeneratorsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where status is not null
        defaultGeneratorShouldBeFound("status.specified=true");

        // Get all the generatorList where status is null
        defaultGeneratorShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorsByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where status is greater than or equal to DEFAULT_STATUS
        defaultGeneratorShouldBeFound("status.greaterThanOrEqual=" + DEFAULT_STATUS);

        // Get all the generatorList where status is greater than or equal to UPDATED_STATUS
        defaultGeneratorShouldNotBeFound("status.greaterThanOrEqual=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGeneratorsByStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where status is less than or equal to DEFAULT_STATUS
        defaultGeneratorShouldBeFound("status.lessThanOrEqual=" + DEFAULT_STATUS);

        // Get all the generatorList where status is less than or equal to SMALLER_STATUS
        defaultGeneratorShouldNotBeFound("status.lessThanOrEqual=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllGeneratorsByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where status is less than DEFAULT_STATUS
        defaultGeneratorShouldNotBeFound("status.lessThan=" + DEFAULT_STATUS);

        // Get all the generatorList where status is less than UPDATED_STATUS
        defaultGeneratorShouldBeFound("status.lessThan=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGeneratorsByStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where status is greater than DEFAULT_STATUS
        defaultGeneratorShouldNotBeFound("status.greaterThan=" + DEFAULT_STATUS);

        // Get all the generatorList where status is greater than SMALLER_STATUS
        defaultGeneratorShouldBeFound("status.greaterThan=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPmaxIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pmax equals to DEFAULT_PMAX
        defaultGeneratorShouldBeFound("pmax.equals=" + DEFAULT_PMAX);

        // Get all the generatorList where pmax equals to UPDATED_PMAX
        defaultGeneratorShouldNotBeFound("pmax.equals=" + UPDATED_PMAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPmaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pmax not equals to DEFAULT_PMAX
        defaultGeneratorShouldNotBeFound("pmax.notEquals=" + DEFAULT_PMAX);

        // Get all the generatorList where pmax not equals to UPDATED_PMAX
        defaultGeneratorShouldBeFound("pmax.notEquals=" + UPDATED_PMAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPmaxIsInShouldWork() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pmax in DEFAULT_PMAX or UPDATED_PMAX
        defaultGeneratorShouldBeFound("pmax.in=" + DEFAULT_PMAX + "," + UPDATED_PMAX);

        // Get all the generatorList where pmax equals to UPDATED_PMAX
        defaultGeneratorShouldNotBeFound("pmax.in=" + UPDATED_PMAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPmaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pmax is not null
        defaultGeneratorShouldBeFound("pmax.specified=true");

        // Get all the generatorList where pmax is null
        defaultGeneratorShouldNotBeFound("pmax.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorsByPmaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pmax is greater than or equal to DEFAULT_PMAX
        defaultGeneratorShouldBeFound("pmax.greaterThanOrEqual=" + DEFAULT_PMAX);

        // Get all the generatorList where pmax is greater than or equal to UPDATED_PMAX
        defaultGeneratorShouldNotBeFound("pmax.greaterThanOrEqual=" + UPDATED_PMAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPmaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pmax is less than or equal to DEFAULT_PMAX
        defaultGeneratorShouldBeFound("pmax.lessThanOrEqual=" + DEFAULT_PMAX);

        // Get all the generatorList where pmax is less than or equal to SMALLER_PMAX
        defaultGeneratorShouldNotBeFound("pmax.lessThanOrEqual=" + SMALLER_PMAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPmaxIsLessThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pmax is less than DEFAULT_PMAX
        defaultGeneratorShouldNotBeFound("pmax.lessThan=" + DEFAULT_PMAX);

        // Get all the generatorList where pmax is less than UPDATED_PMAX
        defaultGeneratorShouldBeFound("pmax.lessThan=" + UPDATED_PMAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPmaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pmax is greater than DEFAULT_PMAX
        defaultGeneratorShouldNotBeFound("pmax.greaterThan=" + DEFAULT_PMAX);

        // Get all the generatorList where pmax is greater than SMALLER_PMAX
        defaultGeneratorShouldBeFound("pmax.greaterThan=" + SMALLER_PMAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPminIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pmin equals to DEFAULT_PMIN
        defaultGeneratorShouldBeFound("pmin.equals=" + DEFAULT_PMIN);

        // Get all the generatorList where pmin equals to UPDATED_PMIN
        defaultGeneratorShouldNotBeFound("pmin.equals=" + UPDATED_PMIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPminIsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pmin not equals to DEFAULT_PMIN
        defaultGeneratorShouldNotBeFound("pmin.notEquals=" + DEFAULT_PMIN);

        // Get all the generatorList where pmin not equals to UPDATED_PMIN
        defaultGeneratorShouldBeFound("pmin.notEquals=" + UPDATED_PMIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPminIsInShouldWork() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pmin in DEFAULT_PMIN or UPDATED_PMIN
        defaultGeneratorShouldBeFound("pmin.in=" + DEFAULT_PMIN + "," + UPDATED_PMIN);

        // Get all the generatorList where pmin equals to UPDATED_PMIN
        defaultGeneratorShouldNotBeFound("pmin.in=" + UPDATED_PMIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPminIsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pmin is not null
        defaultGeneratorShouldBeFound("pmin.specified=true");

        // Get all the generatorList where pmin is null
        defaultGeneratorShouldNotBeFound("pmin.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorsByPminIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pmin is greater than or equal to DEFAULT_PMIN
        defaultGeneratorShouldBeFound("pmin.greaterThanOrEqual=" + DEFAULT_PMIN);

        // Get all the generatorList where pmin is greater than or equal to UPDATED_PMIN
        defaultGeneratorShouldNotBeFound("pmin.greaterThanOrEqual=" + UPDATED_PMIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPminIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pmin is less than or equal to DEFAULT_PMIN
        defaultGeneratorShouldBeFound("pmin.lessThanOrEqual=" + DEFAULT_PMIN);

        // Get all the generatorList where pmin is less than or equal to SMALLER_PMIN
        defaultGeneratorShouldNotBeFound("pmin.lessThanOrEqual=" + SMALLER_PMIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPminIsLessThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pmin is less than DEFAULT_PMIN
        defaultGeneratorShouldNotBeFound("pmin.lessThan=" + DEFAULT_PMIN);

        // Get all the generatorList where pmin is less than UPDATED_PMIN
        defaultGeneratorShouldBeFound("pmin.lessThan=" + UPDATED_PMIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPminIsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pmin is greater than DEFAULT_PMIN
        defaultGeneratorShouldNotBeFound("pmin.greaterThan=" + DEFAULT_PMIN);

        // Get all the generatorList where pmin is greater than SMALLER_PMIN
        defaultGeneratorShouldBeFound("pmin.greaterThan=" + SMALLER_PMIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPc1IsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pc1 equals to DEFAULT_PC_1
        defaultGeneratorShouldBeFound("pc1.equals=" + DEFAULT_PC_1);

        // Get all the generatorList where pc1 equals to UPDATED_PC_1
        defaultGeneratorShouldNotBeFound("pc1.equals=" + UPDATED_PC_1);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPc1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pc1 not equals to DEFAULT_PC_1
        defaultGeneratorShouldNotBeFound("pc1.notEquals=" + DEFAULT_PC_1);

        // Get all the generatorList where pc1 not equals to UPDATED_PC_1
        defaultGeneratorShouldBeFound("pc1.notEquals=" + UPDATED_PC_1);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPc1IsInShouldWork() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pc1 in DEFAULT_PC_1 or UPDATED_PC_1
        defaultGeneratorShouldBeFound("pc1.in=" + DEFAULT_PC_1 + "," + UPDATED_PC_1);

        // Get all the generatorList where pc1 equals to UPDATED_PC_1
        defaultGeneratorShouldNotBeFound("pc1.in=" + UPDATED_PC_1);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPc1IsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pc1 is not null
        defaultGeneratorShouldBeFound("pc1.specified=true");

        // Get all the generatorList where pc1 is null
        defaultGeneratorShouldNotBeFound("pc1.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorsByPc1IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pc1 is greater than or equal to DEFAULT_PC_1
        defaultGeneratorShouldBeFound("pc1.greaterThanOrEqual=" + DEFAULT_PC_1);

        // Get all the generatorList where pc1 is greater than or equal to UPDATED_PC_1
        defaultGeneratorShouldNotBeFound("pc1.greaterThanOrEqual=" + UPDATED_PC_1);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPc1IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pc1 is less than or equal to DEFAULT_PC_1
        defaultGeneratorShouldBeFound("pc1.lessThanOrEqual=" + DEFAULT_PC_1);

        // Get all the generatorList where pc1 is less than or equal to SMALLER_PC_1
        defaultGeneratorShouldNotBeFound("pc1.lessThanOrEqual=" + SMALLER_PC_1);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPc1IsLessThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pc1 is less than DEFAULT_PC_1
        defaultGeneratorShouldNotBeFound("pc1.lessThan=" + DEFAULT_PC_1);

        // Get all the generatorList where pc1 is less than UPDATED_PC_1
        defaultGeneratorShouldBeFound("pc1.lessThan=" + UPDATED_PC_1);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPc1IsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pc1 is greater than DEFAULT_PC_1
        defaultGeneratorShouldNotBeFound("pc1.greaterThan=" + DEFAULT_PC_1);

        // Get all the generatorList where pc1 is greater than SMALLER_PC_1
        defaultGeneratorShouldBeFound("pc1.greaterThan=" + SMALLER_PC_1);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPc2IsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pc2 equals to DEFAULT_PC_2
        defaultGeneratorShouldBeFound("pc2.equals=" + DEFAULT_PC_2);

        // Get all the generatorList where pc2 equals to UPDATED_PC_2
        defaultGeneratorShouldNotBeFound("pc2.equals=" + UPDATED_PC_2);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPc2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pc2 not equals to DEFAULT_PC_2
        defaultGeneratorShouldNotBeFound("pc2.notEquals=" + DEFAULT_PC_2);

        // Get all the generatorList where pc2 not equals to UPDATED_PC_2
        defaultGeneratorShouldBeFound("pc2.notEquals=" + UPDATED_PC_2);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPc2IsInShouldWork() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pc2 in DEFAULT_PC_2 or UPDATED_PC_2
        defaultGeneratorShouldBeFound("pc2.in=" + DEFAULT_PC_2 + "," + UPDATED_PC_2);

        // Get all the generatorList where pc2 equals to UPDATED_PC_2
        defaultGeneratorShouldNotBeFound("pc2.in=" + UPDATED_PC_2);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPc2IsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pc2 is not null
        defaultGeneratorShouldBeFound("pc2.specified=true");

        // Get all the generatorList where pc2 is null
        defaultGeneratorShouldNotBeFound("pc2.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorsByPc2IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pc2 is greater than or equal to DEFAULT_PC_2
        defaultGeneratorShouldBeFound("pc2.greaterThanOrEqual=" + DEFAULT_PC_2);

        // Get all the generatorList where pc2 is greater than or equal to UPDATED_PC_2
        defaultGeneratorShouldNotBeFound("pc2.greaterThanOrEqual=" + UPDATED_PC_2);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPc2IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pc2 is less than or equal to DEFAULT_PC_2
        defaultGeneratorShouldBeFound("pc2.lessThanOrEqual=" + DEFAULT_PC_2);

        // Get all the generatorList where pc2 is less than or equal to SMALLER_PC_2
        defaultGeneratorShouldNotBeFound("pc2.lessThanOrEqual=" + SMALLER_PC_2);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPc2IsLessThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pc2 is less than DEFAULT_PC_2
        defaultGeneratorShouldNotBeFound("pc2.lessThan=" + DEFAULT_PC_2);

        // Get all the generatorList where pc2 is less than UPDATED_PC_2
        defaultGeneratorShouldBeFound("pc2.lessThan=" + UPDATED_PC_2);
    }

    @Test
    @Transactional
    void getAllGeneratorsByPc2IsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where pc2 is greater than DEFAULT_PC_2
        defaultGeneratorShouldNotBeFound("pc2.greaterThan=" + DEFAULT_PC_2);

        // Get all the generatorList where pc2 is greater than SMALLER_PC_2
        defaultGeneratorShouldBeFound("pc2.greaterThan=" + SMALLER_PC_2);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc1minIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc1min equals to DEFAULT_QC_1_MIN
        defaultGeneratorShouldBeFound("qc1min.equals=" + DEFAULT_QC_1_MIN);

        // Get all the generatorList where qc1min equals to UPDATED_QC_1_MIN
        defaultGeneratorShouldNotBeFound("qc1min.equals=" + UPDATED_QC_1_MIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc1minIsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc1min not equals to DEFAULT_QC_1_MIN
        defaultGeneratorShouldNotBeFound("qc1min.notEquals=" + DEFAULT_QC_1_MIN);

        // Get all the generatorList where qc1min not equals to UPDATED_QC_1_MIN
        defaultGeneratorShouldBeFound("qc1min.notEquals=" + UPDATED_QC_1_MIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc1minIsInShouldWork() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc1min in DEFAULT_QC_1_MIN or UPDATED_QC_1_MIN
        defaultGeneratorShouldBeFound("qc1min.in=" + DEFAULT_QC_1_MIN + "," + UPDATED_QC_1_MIN);

        // Get all the generatorList where qc1min equals to UPDATED_QC_1_MIN
        defaultGeneratorShouldNotBeFound("qc1min.in=" + UPDATED_QC_1_MIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc1minIsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc1min is not null
        defaultGeneratorShouldBeFound("qc1min.specified=true");

        // Get all the generatorList where qc1min is null
        defaultGeneratorShouldNotBeFound("qc1min.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc1minIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc1min is greater than or equal to DEFAULT_QC_1_MIN
        defaultGeneratorShouldBeFound("qc1min.greaterThanOrEqual=" + DEFAULT_QC_1_MIN);

        // Get all the generatorList where qc1min is greater than or equal to UPDATED_QC_1_MIN
        defaultGeneratorShouldNotBeFound("qc1min.greaterThanOrEqual=" + UPDATED_QC_1_MIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc1minIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc1min is less than or equal to DEFAULT_QC_1_MIN
        defaultGeneratorShouldBeFound("qc1min.lessThanOrEqual=" + DEFAULT_QC_1_MIN);

        // Get all the generatorList where qc1min is less than or equal to SMALLER_QC_1_MIN
        defaultGeneratorShouldNotBeFound("qc1min.lessThanOrEqual=" + SMALLER_QC_1_MIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc1minIsLessThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc1min is less than DEFAULT_QC_1_MIN
        defaultGeneratorShouldNotBeFound("qc1min.lessThan=" + DEFAULT_QC_1_MIN);

        // Get all the generatorList where qc1min is less than UPDATED_QC_1_MIN
        defaultGeneratorShouldBeFound("qc1min.lessThan=" + UPDATED_QC_1_MIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc1minIsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc1min is greater than DEFAULT_QC_1_MIN
        defaultGeneratorShouldNotBeFound("qc1min.greaterThan=" + DEFAULT_QC_1_MIN);

        // Get all the generatorList where qc1min is greater than SMALLER_QC_1_MIN
        defaultGeneratorShouldBeFound("qc1min.greaterThan=" + SMALLER_QC_1_MIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc1maxIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc1max equals to DEFAULT_QC_1_MAX
        defaultGeneratorShouldBeFound("qc1max.equals=" + DEFAULT_QC_1_MAX);

        // Get all the generatorList where qc1max equals to UPDATED_QC_1_MAX
        defaultGeneratorShouldNotBeFound("qc1max.equals=" + UPDATED_QC_1_MAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc1maxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc1max not equals to DEFAULT_QC_1_MAX
        defaultGeneratorShouldNotBeFound("qc1max.notEquals=" + DEFAULT_QC_1_MAX);

        // Get all the generatorList where qc1max not equals to UPDATED_QC_1_MAX
        defaultGeneratorShouldBeFound("qc1max.notEquals=" + UPDATED_QC_1_MAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc1maxIsInShouldWork() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc1max in DEFAULT_QC_1_MAX or UPDATED_QC_1_MAX
        defaultGeneratorShouldBeFound("qc1max.in=" + DEFAULT_QC_1_MAX + "," + UPDATED_QC_1_MAX);

        // Get all the generatorList where qc1max equals to UPDATED_QC_1_MAX
        defaultGeneratorShouldNotBeFound("qc1max.in=" + UPDATED_QC_1_MAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc1maxIsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc1max is not null
        defaultGeneratorShouldBeFound("qc1max.specified=true");

        // Get all the generatorList where qc1max is null
        defaultGeneratorShouldNotBeFound("qc1max.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc1maxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc1max is greater than or equal to DEFAULT_QC_1_MAX
        defaultGeneratorShouldBeFound("qc1max.greaterThanOrEqual=" + DEFAULT_QC_1_MAX);

        // Get all the generatorList where qc1max is greater than or equal to UPDATED_QC_1_MAX
        defaultGeneratorShouldNotBeFound("qc1max.greaterThanOrEqual=" + UPDATED_QC_1_MAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc1maxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc1max is less than or equal to DEFAULT_QC_1_MAX
        defaultGeneratorShouldBeFound("qc1max.lessThanOrEqual=" + DEFAULT_QC_1_MAX);

        // Get all the generatorList where qc1max is less than or equal to SMALLER_QC_1_MAX
        defaultGeneratorShouldNotBeFound("qc1max.lessThanOrEqual=" + SMALLER_QC_1_MAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc1maxIsLessThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc1max is less than DEFAULT_QC_1_MAX
        defaultGeneratorShouldNotBeFound("qc1max.lessThan=" + DEFAULT_QC_1_MAX);

        // Get all the generatorList where qc1max is less than UPDATED_QC_1_MAX
        defaultGeneratorShouldBeFound("qc1max.lessThan=" + UPDATED_QC_1_MAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc1maxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc1max is greater than DEFAULT_QC_1_MAX
        defaultGeneratorShouldNotBeFound("qc1max.greaterThan=" + DEFAULT_QC_1_MAX);

        // Get all the generatorList where qc1max is greater than SMALLER_QC_1_MAX
        defaultGeneratorShouldBeFound("qc1max.greaterThan=" + SMALLER_QC_1_MAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc2minIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc2min equals to DEFAULT_QC_2_MIN
        defaultGeneratorShouldBeFound("qc2min.equals=" + DEFAULT_QC_2_MIN);

        // Get all the generatorList where qc2min equals to UPDATED_QC_2_MIN
        defaultGeneratorShouldNotBeFound("qc2min.equals=" + UPDATED_QC_2_MIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc2minIsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc2min not equals to DEFAULT_QC_2_MIN
        defaultGeneratorShouldNotBeFound("qc2min.notEquals=" + DEFAULT_QC_2_MIN);

        // Get all the generatorList where qc2min not equals to UPDATED_QC_2_MIN
        defaultGeneratorShouldBeFound("qc2min.notEquals=" + UPDATED_QC_2_MIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc2minIsInShouldWork() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc2min in DEFAULT_QC_2_MIN or UPDATED_QC_2_MIN
        defaultGeneratorShouldBeFound("qc2min.in=" + DEFAULT_QC_2_MIN + "," + UPDATED_QC_2_MIN);

        // Get all the generatorList where qc2min equals to UPDATED_QC_2_MIN
        defaultGeneratorShouldNotBeFound("qc2min.in=" + UPDATED_QC_2_MIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc2minIsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc2min is not null
        defaultGeneratorShouldBeFound("qc2min.specified=true");

        // Get all the generatorList where qc2min is null
        defaultGeneratorShouldNotBeFound("qc2min.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc2minIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc2min is greater than or equal to DEFAULT_QC_2_MIN
        defaultGeneratorShouldBeFound("qc2min.greaterThanOrEqual=" + DEFAULT_QC_2_MIN);

        // Get all the generatorList where qc2min is greater than or equal to UPDATED_QC_2_MIN
        defaultGeneratorShouldNotBeFound("qc2min.greaterThanOrEqual=" + UPDATED_QC_2_MIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc2minIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc2min is less than or equal to DEFAULT_QC_2_MIN
        defaultGeneratorShouldBeFound("qc2min.lessThanOrEqual=" + DEFAULT_QC_2_MIN);

        // Get all the generatorList where qc2min is less than or equal to SMALLER_QC_2_MIN
        defaultGeneratorShouldNotBeFound("qc2min.lessThanOrEqual=" + SMALLER_QC_2_MIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc2minIsLessThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc2min is less than DEFAULT_QC_2_MIN
        defaultGeneratorShouldNotBeFound("qc2min.lessThan=" + DEFAULT_QC_2_MIN);

        // Get all the generatorList where qc2min is less than UPDATED_QC_2_MIN
        defaultGeneratorShouldBeFound("qc2min.lessThan=" + UPDATED_QC_2_MIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc2minIsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc2min is greater than DEFAULT_QC_2_MIN
        defaultGeneratorShouldNotBeFound("qc2min.greaterThan=" + DEFAULT_QC_2_MIN);

        // Get all the generatorList where qc2min is greater than SMALLER_QC_2_MIN
        defaultGeneratorShouldBeFound("qc2min.greaterThan=" + SMALLER_QC_2_MIN);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc2maxIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc2max equals to DEFAULT_QC_2_MAX
        defaultGeneratorShouldBeFound("qc2max.equals=" + DEFAULT_QC_2_MAX);

        // Get all the generatorList where qc2max equals to UPDATED_QC_2_MAX
        defaultGeneratorShouldNotBeFound("qc2max.equals=" + UPDATED_QC_2_MAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc2maxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc2max not equals to DEFAULT_QC_2_MAX
        defaultGeneratorShouldNotBeFound("qc2max.notEquals=" + DEFAULT_QC_2_MAX);

        // Get all the generatorList where qc2max not equals to UPDATED_QC_2_MAX
        defaultGeneratorShouldBeFound("qc2max.notEquals=" + UPDATED_QC_2_MAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc2maxIsInShouldWork() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc2max in DEFAULT_QC_2_MAX or UPDATED_QC_2_MAX
        defaultGeneratorShouldBeFound("qc2max.in=" + DEFAULT_QC_2_MAX + "," + UPDATED_QC_2_MAX);

        // Get all the generatorList where qc2max equals to UPDATED_QC_2_MAX
        defaultGeneratorShouldNotBeFound("qc2max.in=" + UPDATED_QC_2_MAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc2maxIsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc2max is not null
        defaultGeneratorShouldBeFound("qc2max.specified=true");

        // Get all the generatorList where qc2max is null
        defaultGeneratorShouldNotBeFound("qc2max.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc2maxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc2max is greater than or equal to DEFAULT_QC_2_MAX
        defaultGeneratorShouldBeFound("qc2max.greaterThanOrEqual=" + DEFAULT_QC_2_MAX);

        // Get all the generatorList where qc2max is greater than or equal to UPDATED_QC_2_MAX
        defaultGeneratorShouldNotBeFound("qc2max.greaterThanOrEqual=" + UPDATED_QC_2_MAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc2maxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc2max is less than or equal to DEFAULT_QC_2_MAX
        defaultGeneratorShouldBeFound("qc2max.lessThanOrEqual=" + DEFAULT_QC_2_MAX);

        // Get all the generatorList where qc2max is less than or equal to SMALLER_QC_2_MAX
        defaultGeneratorShouldNotBeFound("qc2max.lessThanOrEqual=" + SMALLER_QC_2_MAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc2maxIsLessThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc2max is less than DEFAULT_QC_2_MAX
        defaultGeneratorShouldNotBeFound("qc2max.lessThan=" + DEFAULT_QC_2_MAX);

        // Get all the generatorList where qc2max is less than UPDATED_QC_2_MAX
        defaultGeneratorShouldBeFound("qc2max.lessThan=" + UPDATED_QC_2_MAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByQc2maxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where qc2max is greater than DEFAULT_QC_2_MAX
        defaultGeneratorShouldNotBeFound("qc2max.greaterThan=" + DEFAULT_QC_2_MAX);

        // Get all the generatorList where qc2max is greater than SMALLER_QC_2_MAX
        defaultGeneratorShouldBeFound("qc2max.greaterThan=" + SMALLER_QC_2_MAX);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRampAgcIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where rampAgc equals to DEFAULT_RAMP_AGC
        defaultGeneratorShouldBeFound("rampAgc.equals=" + DEFAULT_RAMP_AGC);

        // Get all the generatorList where rampAgc equals to UPDATED_RAMP_AGC
        defaultGeneratorShouldNotBeFound("rampAgc.equals=" + UPDATED_RAMP_AGC);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRampAgcIsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where rampAgc not equals to DEFAULT_RAMP_AGC
        defaultGeneratorShouldNotBeFound("rampAgc.notEquals=" + DEFAULT_RAMP_AGC);

        // Get all the generatorList where rampAgc not equals to UPDATED_RAMP_AGC
        defaultGeneratorShouldBeFound("rampAgc.notEquals=" + UPDATED_RAMP_AGC);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRampAgcIsInShouldWork() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where rampAgc in DEFAULT_RAMP_AGC or UPDATED_RAMP_AGC
        defaultGeneratorShouldBeFound("rampAgc.in=" + DEFAULT_RAMP_AGC + "," + UPDATED_RAMP_AGC);

        // Get all the generatorList where rampAgc equals to UPDATED_RAMP_AGC
        defaultGeneratorShouldNotBeFound("rampAgc.in=" + UPDATED_RAMP_AGC);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRampAgcIsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where rampAgc is not null
        defaultGeneratorShouldBeFound("rampAgc.specified=true");

        // Get all the generatorList where rampAgc is null
        defaultGeneratorShouldNotBeFound("rampAgc.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorsByRampAgcIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where rampAgc is greater than or equal to DEFAULT_RAMP_AGC
        defaultGeneratorShouldBeFound("rampAgc.greaterThanOrEqual=" + DEFAULT_RAMP_AGC);

        // Get all the generatorList where rampAgc is greater than or equal to UPDATED_RAMP_AGC
        defaultGeneratorShouldNotBeFound("rampAgc.greaterThanOrEqual=" + UPDATED_RAMP_AGC);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRampAgcIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where rampAgc is less than or equal to DEFAULT_RAMP_AGC
        defaultGeneratorShouldBeFound("rampAgc.lessThanOrEqual=" + DEFAULT_RAMP_AGC);

        // Get all the generatorList where rampAgc is less than or equal to SMALLER_RAMP_AGC
        defaultGeneratorShouldNotBeFound("rampAgc.lessThanOrEqual=" + SMALLER_RAMP_AGC);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRampAgcIsLessThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where rampAgc is less than DEFAULT_RAMP_AGC
        defaultGeneratorShouldNotBeFound("rampAgc.lessThan=" + DEFAULT_RAMP_AGC);

        // Get all the generatorList where rampAgc is less than UPDATED_RAMP_AGC
        defaultGeneratorShouldBeFound("rampAgc.lessThan=" + UPDATED_RAMP_AGC);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRampAgcIsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where rampAgc is greater than DEFAULT_RAMP_AGC
        defaultGeneratorShouldNotBeFound("rampAgc.greaterThan=" + DEFAULT_RAMP_AGC);

        // Get all the generatorList where rampAgc is greater than SMALLER_RAMP_AGC
        defaultGeneratorShouldBeFound("rampAgc.greaterThan=" + SMALLER_RAMP_AGC);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRamp10IsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where ramp10 equals to DEFAULT_RAMP_10
        defaultGeneratorShouldBeFound("ramp10.equals=" + DEFAULT_RAMP_10);

        // Get all the generatorList where ramp10 equals to UPDATED_RAMP_10
        defaultGeneratorShouldNotBeFound("ramp10.equals=" + UPDATED_RAMP_10);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRamp10IsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where ramp10 not equals to DEFAULT_RAMP_10
        defaultGeneratorShouldNotBeFound("ramp10.notEquals=" + DEFAULT_RAMP_10);

        // Get all the generatorList where ramp10 not equals to UPDATED_RAMP_10
        defaultGeneratorShouldBeFound("ramp10.notEquals=" + UPDATED_RAMP_10);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRamp10IsInShouldWork() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where ramp10 in DEFAULT_RAMP_10 or UPDATED_RAMP_10
        defaultGeneratorShouldBeFound("ramp10.in=" + DEFAULT_RAMP_10 + "," + UPDATED_RAMP_10);

        // Get all the generatorList where ramp10 equals to UPDATED_RAMP_10
        defaultGeneratorShouldNotBeFound("ramp10.in=" + UPDATED_RAMP_10);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRamp10IsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where ramp10 is not null
        defaultGeneratorShouldBeFound("ramp10.specified=true");

        // Get all the generatorList where ramp10 is null
        defaultGeneratorShouldNotBeFound("ramp10.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorsByRamp10IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where ramp10 is greater than or equal to DEFAULT_RAMP_10
        defaultGeneratorShouldBeFound("ramp10.greaterThanOrEqual=" + DEFAULT_RAMP_10);

        // Get all the generatorList where ramp10 is greater than or equal to UPDATED_RAMP_10
        defaultGeneratorShouldNotBeFound("ramp10.greaterThanOrEqual=" + UPDATED_RAMP_10);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRamp10IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where ramp10 is less than or equal to DEFAULT_RAMP_10
        defaultGeneratorShouldBeFound("ramp10.lessThanOrEqual=" + DEFAULT_RAMP_10);

        // Get all the generatorList where ramp10 is less than or equal to SMALLER_RAMP_10
        defaultGeneratorShouldNotBeFound("ramp10.lessThanOrEqual=" + SMALLER_RAMP_10);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRamp10IsLessThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where ramp10 is less than DEFAULT_RAMP_10
        defaultGeneratorShouldNotBeFound("ramp10.lessThan=" + DEFAULT_RAMP_10);

        // Get all the generatorList where ramp10 is less than UPDATED_RAMP_10
        defaultGeneratorShouldBeFound("ramp10.lessThan=" + UPDATED_RAMP_10);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRamp10IsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where ramp10 is greater than DEFAULT_RAMP_10
        defaultGeneratorShouldNotBeFound("ramp10.greaterThan=" + DEFAULT_RAMP_10);

        // Get all the generatorList where ramp10 is greater than SMALLER_RAMP_10
        defaultGeneratorShouldBeFound("ramp10.greaterThan=" + SMALLER_RAMP_10);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRamp30IsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where ramp30 equals to DEFAULT_RAMP_30
        defaultGeneratorShouldBeFound("ramp30.equals=" + DEFAULT_RAMP_30);

        // Get all the generatorList where ramp30 equals to UPDATED_RAMP_30
        defaultGeneratorShouldNotBeFound("ramp30.equals=" + UPDATED_RAMP_30);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRamp30IsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where ramp30 not equals to DEFAULT_RAMP_30
        defaultGeneratorShouldNotBeFound("ramp30.notEquals=" + DEFAULT_RAMP_30);

        // Get all the generatorList where ramp30 not equals to UPDATED_RAMP_30
        defaultGeneratorShouldBeFound("ramp30.notEquals=" + UPDATED_RAMP_30);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRamp30IsInShouldWork() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where ramp30 in DEFAULT_RAMP_30 or UPDATED_RAMP_30
        defaultGeneratorShouldBeFound("ramp30.in=" + DEFAULT_RAMP_30 + "," + UPDATED_RAMP_30);

        // Get all the generatorList where ramp30 equals to UPDATED_RAMP_30
        defaultGeneratorShouldNotBeFound("ramp30.in=" + UPDATED_RAMP_30);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRamp30IsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where ramp30 is not null
        defaultGeneratorShouldBeFound("ramp30.specified=true");

        // Get all the generatorList where ramp30 is null
        defaultGeneratorShouldNotBeFound("ramp30.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorsByRamp30IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where ramp30 is greater than or equal to DEFAULT_RAMP_30
        defaultGeneratorShouldBeFound("ramp30.greaterThanOrEqual=" + DEFAULT_RAMP_30);

        // Get all the generatorList where ramp30 is greater than or equal to UPDATED_RAMP_30
        defaultGeneratorShouldNotBeFound("ramp30.greaterThanOrEqual=" + UPDATED_RAMP_30);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRamp30IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where ramp30 is less than or equal to DEFAULT_RAMP_30
        defaultGeneratorShouldBeFound("ramp30.lessThanOrEqual=" + DEFAULT_RAMP_30);

        // Get all the generatorList where ramp30 is less than or equal to SMALLER_RAMP_30
        defaultGeneratorShouldNotBeFound("ramp30.lessThanOrEqual=" + SMALLER_RAMP_30);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRamp30IsLessThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where ramp30 is less than DEFAULT_RAMP_30
        defaultGeneratorShouldNotBeFound("ramp30.lessThan=" + DEFAULT_RAMP_30);

        // Get all the generatorList where ramp30 is less than UPDATED_RAMP_30
        defaultGeneratorShouldBeFound("ramp30.lessThan=" + UPDATED_RAMP_30);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRamp30IsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where ramp30 is greater than DEFAULT_RAMP_30
        defaultGeneratorShouldNotBeFound("ramp30.greaterThan=" + DEFAULT_RAMP_30);

        // Get all the generatorList where ramp30 is greater than SMALLER_RAMP_30
        defaultGeneratorShouldBeFound("ramp30.greaterThan=" + SMALLER_RAMP_30);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRampQIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where rampQ equals to DEFAULT_RAMP_Q
        defaultGeneratorShouldBeFound("rampQ.equals=" + DEFAULT_RAMP_Q);

        // Get all the generatorList where rampQ equals to UPDATED_RAMP_Q
        defaultGeneratorShouldNotBeFound("rampQ.equals=" + UPDATED_RAMP_Q);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRampQIsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where rampQ not equals to DEFAULT_RAMP_Q
        defaultGeneratorShouldNotBeFound("rampQ.notEquals=" + DEFAULT_RAMP_Q);

        // Get all the generatorList where rampQ not equals to UPDATED_RAMP_Q
        defaultGeneratorShouldBeFound("rampQ.notEquals=" + UPDATED_RAMP_Q);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRampQIsInShouldWork() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where rampQ in DEFAULT_RAMP_Q or UPDATED_RAMP_Q
        defaultGeneratorShouldBeFound("rampQ.in=" + DEFAULT_RAMP_Q + "," + UPDATED_RAMP_Q);

        // Get all the generatorList where rampQ equals to UPDATED_RAMP_Q
        defaultGeneratorShouldNotBeFound("rampQ.in=" + UPDATED_RAMP_Q);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRampQIsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where rampQ is not null
        defaultGeneratorShouldBeFound("rampQ.specified=true");

        // Get all the generatorList where rampQ is null
        defaultGeneratorShouldNotBeFound("rampQ.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorsByRampQIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where rampQ is greater than or equal to DEFAULT_RAMP_Q
        defaultGeneratorShouldBeFound("rampQ.greaterThanOrEqual=" + DEFAULT_RAMP_Q);

        // Get all the generatorList where rampQ is greater than or equal to UPDATED_RAMP_Q
        defaultGeneratorShouldNotBeFound("rampQ.greaterThanOrEqual=" + UPDATED_RAMP_Q);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRampQIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where rampQ is less than or equal to DEFAULT_RAMP_Q
        defaultGeneratorShouldBeFound("rampQ.lessThanOrEqual=" + DEFAULT_RAMP_Q);

        // Get all the generatorList where rampQ is less than or equal to SMALLER_RAMP_Q
        defaultGeneratorShouldNotBeFound("rampQ.lessThanOrEqual=" + SMALLER_RAMP_Q);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRampQIsLessThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where rampQ is less than DEFAULT_RAMP_Q
        defaultGeneratorShouldNotBeFound("rampQ.lessThan=" + DEFAULT_RAMP_Q);

        // Get all the generatorList where rampQ is less than UPDATED_RAMP_Q
        defaultGeneratorShouldBeFound("rampQ.lessThan=" + UPDATED_RAMP_Q);
    }

    @Test
    @Transactional
    void getAllGeneratorsByRampQIsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where rampQ is greater than DEFAULT_RAMP_Q
        defaultGeneratorShouldNotBeFound("rampQ.greaterThan=" + DEFAULT_RAMP_Q);

        // Get all the generatorList where rampQ is greater than SMALLER_RAMP_Q
        defaultGeneratorShouldBeFound("rampQ.greaterThan=" + SMALLER_RAMP_Q);
    }

    @Test
    @Transactional
    void getAllGeneratorsByApfIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where apf equals to DEFAULT_APF
        defaultGeneratorShouldBeFound("apf.equals=" + DEFAULT_APF);

        // Get all the generatorList where apf equals to UPDATED_APF
        defaultGeneratorShouldNotBeFound("apf.equals=" + UPDATED_APF);
    }

    @Test
    @Transactional
    void getAllGeneratorsByApfIsNotEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where apf not equals to DEFAULT_APF
        defaultGeneratorShouldNotBeFound("apf.notEquals=" + DEFAULT_APF);

        // Get all the generatorList where apf not equals to UPDATED_APF
        defaultGeneratorShouldBeFound("apf.notEquals=" + UPDATED_APF);
    }

    @Test
    @Transactional
    void getAllGeneratorsByApfIsInShouldWork() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where apf in DEFAULT_APF or UPDATED_APF
        defaultGeneratorShouldBeFound("apf.in=" + DEFAULT_APF + "," + UPDATED_APF);

        // Get all the generatorList where apf equals to UPDATED_APF
        defaultGeneratorShouldNotBeFound("apf.in=" + UPDATED_APF);
    }

    @Test
    @Transactional
    void getAllGeneratorsByApfIsNullOrNotNull() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where apf is not null
        defaultGeneratorShouldBeFound("apf.specified=true");

        // Get all the generatorList where apf is null
        defaultGeneratorShouldNotBeFound("apf.specified=false");
    }

    @Test
    @Transactional
    void getAllGeneratorsByApfIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where apf is greater than or equal to DEFAULT_APF
        defaultGeneratorShouldBeFound("apf.greaterThanOrEqual=" + DEFAULT_APF);

        // Get all the generatorList where apf is greater than or equal to UPDATED_APF
        defaultGeneratorShouldNotBeFound("apf.greaterThanOrEqual=" + UPDATED_APF);
    }

    @Test
    @Transactional
    void getAllGeneratorsByApfIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where apf is less than or equal to DEFAULT_APF
        defaultGeneratorShouldBeFound("apf.lessThanOrEqual=" + DEFAULT_APF);

        // Get all the generatorList where apf is less than or equal to SMALLER_APF
        defaultGeneratorShouldNotBeFound("apf.lessThanOrEqual=" + SMALLER_APF);
    }

    @Test
    @Transactional
    void getAllGeneratorsByApfIsLessThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where apf is less than DEFAULT_APF
        defaultGeneratorShouldNotBeFound("apf.lessThan=" + DEFAULT_APF);

        // Get all the generatorList where apf is less than UPDATED_APF
        defaultGeneratorShouldBeFound("apf.lessThan=" + UPDATED_APF);
    }

    @Test
    @Transactional
    void getAllGeneratorsByApfIsGreaterThanSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        // Get all the generatorList where apf is greater than DEFAULT_APF
        defaultGeneratorShouldNotBeFound("apf.greaterThan=" + DEFAULT_APF);

        // Get all the generatorList where apf is greater than SMALLER_APF
        defaultGeneratorShouldBeFound("apf.greaterThan=" + SMALLER_APF);
    }

    @Test
    @Transactional
    void getAllGeneratorsByGenElValIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);
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
        generator.addGenElVal(genElVal);
        generatorRepository.saveAndFlush(generator);
        Long genElValId = genElVal.getId();

        // Get all the generatorList where genElVal equals to genElValId
        defaultGeneratorShouldBeFound("genElValId.equals=" + genElValId);

        // Get all the generatorList where genElVal equals to (genElValId + 1)
        defaultGeneratorShouldNotBeFound("genElValId.equals=" + (genElValId + 1));
    }

    @Test
    @Transactional
    void getAllGeneratorsByGeneratorExtensionIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);
        GeneratorExtension generatorExtension;
        if (TestUtil.findAll(em, GeneratorExtension.class).isEmpty()) {
            generatorExtension = GeneratorExtensionResourceIT.createEntity(em);
            em.persist(generatorExtension);
            em.flush();
        } else {
            generatorExtension = TestUtil.findAll(em, GeneratorExtension.class).get(0);
        }
        em.persist(generatorExtension);
        em.flush();
        generator.setGeneratorExtension(generatorExtension);
        generatorExtension.setGenerator(generator);
        generatorRepository.saveAndFlush(generator);
        Long generatorExtensionId = generatorExtension.getId();

        // Get all the generatorList where generatorExtension equals to generatorExtensionId
        defaultGeneratorShouldBeFound("generatorExtensionId.equals=" + generatorExtensionId);

        // Get all the generatorList where generatorExtension equals to (generatorExtensionId + 1)
        defaultGeneratorShouldNotBeFound("generatorExtensionId.equals=" + (generatorExtensionId + 1));
    }

    @Test
    @Transactional
    void getAllGeneratorsByGenTagIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);
        GenTag genTag;
        if (TestUtil.findAll(em, GenTag.class).isEmpty()) {
            genTag = GenTagResourceIT.createEntity(em);
            em.persist(genTag);
            em.flush();
        } else {
            genTag = TestUtil.findAll(em, GenTag.class).get(0);
        }
        em.persist(genTag);
        em.flush();
        generator.setGenTag(genTag);
        genTag.setGenerator(generator);
        generatorRepository.saveAndFlush(generator);
        Long genTagId = genTag.getId();

        // Get all the generatorList where genTag equals to genTagId
        defaultGeneratorShouldBeFound("genTagId.equals=" + genTagId);

        // Get all the generatorList where genTag equals to (genTagId + 1)
        defaultGeneratorShouldNotBeFound("genTagId.equals=" + (genTagId + 1));
    }

    @Test
    @Transactional
    void getAllGeneratorsByGenCostIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);
        GenCost genCost;
        if (TestUtil.findAll(em, GenCost.class).isEmpty()) {
            genCost = GenCostResourceIT.createEntity(em);
            em.persist(genCost);
            em.flush();
        } else {
            genCost = TestUtil.findAll(em, GenCost.class).get(0);
        }
        em.persist(genCost);
        em.flush();
        generator.setGenCost(genCost);
        genCost.setGenerator(generator);
        generatorRepository.saveAndFlush(generator);
        Long genCostId = genCost.getId();

        // Get all the generatorList where genCost equals to genCostId
        defaultGeneratorShouldBeFound("genCostId.equals=" + genCostId);

        // Get all the generatorList where genCost equals to (genCostId + 1)
        defaultGeneratorShouldNotBeFound("genCostId.equals=" + (genCostId + 1));
    }

    @Test
    @Transactional
    void getAllGeneratorsByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);
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
        generator.setNetwork(network);
        generatorRepository.saveAndFlush(generator);
        Long networkId = network.getId();

        // Get all the generatorList where network equals to networkId
        defaultGeneratorShouldBeFound("networkId.equals=" + networkId);

        // Get all the generatorList where network equals to (networkId + 1)
        defaultGeneratorShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGeneratorShouldBeFound(String filter) throws Exception {
        restGeneratorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(generator.getId().intValue())))
            .andExpect(jsonPath("$.[*].busNum").value(hasItem(DEFAULT_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].pg").value(hasItem(DEFAULT_PG.doubleValue())))
            .andExpect(jsonPath("$.[*].qg").value(hasItem(DEFAULT_QG.doubleValue())))
            .andExpect(jsonPath("$.[*].qmax").value(hasItem(DEFAULT_QMAX.doubleValue())))
            .andExpect(jsonPath("$.[*].qmin").value(hasItem(DEFAULT_QMIN.doubleValue())))
            .andExpect(jsonPath("$.[*].vg").value(hasItem(DEFAULT_VG.doubleValue())))
            .andExpect(jsonPath("$.[*].mBase").value(hasItem(DEFAULT_M_BASE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].pmax").value(hasItem(DEFAULT_PMAX.doubleValue())))
            .andExpect(jsonPath("$.[*].pmin").value(hasItem(DEFAULT_PMIN.doubleValue())))
            .andExpect(jsonPath("$.[*].pc1").value(hasItem(DEFAULT_PC_1.doubleValue())))
            .andExpect(jsonPath("$.[*].pc2").value(hasItem(DEFAULT_PC_2.doubleValue())))
            .andExpect(jsonPath("$.[*].qc1min").value(hasItem(DEFAULT_QC_1_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].qc1max").value(hasItem(DEFAULT_QC_1_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].qc2min").value(hasItem(DEFAULT_QC_2_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].qc2max").value(hasItem(DEFAULT_QC_2_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].rampAgc").value(hasItem(DEFAULT_RAMP_AGC.doubleValue())))
            .andExpect(jsonPath("$.[*].ramp10").value(hasItem(DEFAULT_RAMP_10.doubleValue())))
            .andExpect(jsonPath("$.[*].ramp30").value(hasItem(DEFAULT_RAMP_30.doubleValue())))
            .andExpect(jsonPath("$.[*].rampQ").value(hasItem(DEFAULT_RAMP_Q.doubleValue())))
            .andExpect(jsonPath("$.[*].apf").value(hasItem(DEFAULT_APF.intValue())));

        // Check, that the count call also returns 1
        restGeneratorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGeneratorShouldNotBeFound(String filter) throws Exception {
        restGeneratorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGeneratorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGenerator() throws Exception {
        // Get the generator
        restGeneratorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGenerator() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        int databaseSizeBeforeUpdate = generatorRepository.findAll().size();

        // Update the generator
        Generator updatedGenerator = generatorRepository.findById(generator.getId()).get();
        // Disconnect from session so that the updates on updatedGenerator are not directly saved in db
        em.detach(updatedGenerator);
        updatedGenerator
            .busNum(UPDATED_BUS_NUM)
            .pg(UPDATED_PG)
            .qg(UPDATED_QG)
            .qmax(UPDATED_QMAX)
            .qmin(UPDATED_QMIN)
            .vg(UPDATED_VG)
            .mBase(UPDATED_M_BASE)
            .status(UPDATED_STATUS)
            .pmax(UPDATED_PMAX)
            .pmin(UPDATED_PMIN)
            .pc1(UPDATED_PC_1)
            .pc2(UPDATED_PC_2)
            .qc1min(UPDATED_QC_1_MIN)
            .qc1max(UPDATED_QC_1_MAX)
            .qc2min(UPDATED_QC_2_MIN)
            .qc2max(UPDATED_QC_2_MAX)
            .rampAgc(UPDATED_RAMP_AGC)
            .ramp10(UPDATED_RAMP_10)
            .ramp30(UPDATED_RAMP_30)
            .rampQ(UPDATED_RAMP_Q)
            .apf(UPDATED_APF);
        GeneratorDTO generatorDTO = generatorMapper.toDto(updatedGenerator);

        restGeneratorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, generatorDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generatorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Generator in the database
        List<Generator> generatorList = generatorRepository.findAll();
        assertThat(generatorList).hasSize(databaseSizeBeforeUpdate);
        Generator testGenerator = generatorList.get(generatorList.size() - 1);
        assertThat(testGenerator.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testGenerator.getPg()).isEqualTo(UPDATED_PG);
        assertThat(testGenerator.getQg()).isEqualTo(UPDATED_QG);
        assertThat(testGenerator.getQmax()).isEqualTo(UPDATED_QMAX);
        assertThat(testGenerator.getQmin()).isEqualTo(UPDATED_QMIN);
        assertThat(testGenerator.getVg()).isEqualTo(UPDATED_VG);
        assertThat(testGenerator.getmBase()).isEqualTo(UPDATED_M_BASE);
        assertThat(testGenerator.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testGenerator.getPmax()).isEqualTo(UPDATED_PMAX);
        assertThat(testGenerator.getPmin()).isEqualTo(UPDATED_PMIN);
        assertThat(testGenerator.getPc1()).isEqualTo(UPDATED_PC_1);
        assertThat(testGenerator.getPc2()).isEqualTo(UPDATED_PC_2);
        assertThat(testGenerator.getQc1min()).isEqualTo(UPDATED_QC_1_MIN);
        assertThat(testGenerator.getQc1max()).isEqualTo(UPDATED_QC_1_MAX);
        assertThat(testGenerator.getQc2min()).isEqualTo(UPDATED_QC_2_MIN);
        assertThat(testGenerator.getQc2max()).isEqualTo(UPDATED_QC_2_MAX);
        assertThat(testGenerator.getRampAgc()).isEqualTo(UPDATED_RAMP_AGC);
        assertThat(testGenerator.getRamp10()).isEqualTo(UPDATED_RAMP_10);
        assertThat(testGenerator.getRamp30()).isEqualTo(UPDATED_RAMP_30);
        assertThat(testGenerator.getRampQ()).isEqualTo(UPDATED_RAMP_Q);
        assertThat(testGenerator.getApf()).isEqualTo(UPDATED_APF);
    }

    @Test
    @Transactional
    void putNonExistingGenerator() throws Exception {
        int databaseSizeBeforeUpdate = generatorRepository.findAll().size();
        generator.setId(count.incrementAndGet());

        // Create the Generator
        GeneratorDTO generatorDTO = generatorMapper.toDto(generator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeneratorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, generatorDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Generator in the database
        List<Generator> generatorList = generatorRepository.findAll();
        assertThat(generatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGenerator() throws Exception {
        int databaseSizeBeforeUpdate = generatorRepository.findAll().size();
        generator.setId(count.incrementAndGet());

        // Create the Generator
        GeneratorDTO generatorDTO = generatorMapper.toDto(generator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneratorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Generator in the database
        List<Generator> generatorList = generatorRepository.findAll();
        assertThat(generatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGenerator() throws Exception {
        int databaseSizeBeforeUpdate = generatorRepository.findAll().size();
        generator.setId(count.incrementAndGet());

        // Create the Generator
        GeneratorDTO generatorDTO = generatorMapper.toDto(generator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneratorMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generatorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Generator in the database
        List<Generator> generatorList = generatorRepository.findAll();
        assertThat(generatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGeneratorWithPatch() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        int databaseSizeBeforeUpdate = generatorRepository.findAll().size();

        // Update the generator using partial update
        Generator partialUpdatedGenerator = new Generator();
        partialUpdatedGenerator.setId(generator.getId());

        partialUpdatedGenerator
            .pg(UPDATED_PG)
            .qg(UPDATED_QG)
            .qmin(UPDATED_QMIN)
            .vg(UPDATED_VG)
            .pc1(UPDATED_PC_1)
            .qc2min(UPDATED_QC_2_MIN)
            .ramp30(UPDATED_RAMP_30)
            .rampQ(UPDATED_RAMP_Q)
            .apf(UPDATED_APF);

        restGeneratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenerator.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenerator))
            )
            .andExpect(status().isOk());

        // Validate the Generator in the database
        List<Generator> generatorList = generatorRepository.findAll();
        assertThat(generatorList).hasSize(databaseSizeBeforeUpdate);
        Generator testGenerator = generatorList.get(generatorList.size() - 1);
        assertThat(testGenerator.getBusNum()).isEqualTo(DEFAULT_BUS_NUM);
        assertThat(testGenerator.getPg()).isEqualTo(UPDATED_PG);
        assertThat(testGenerator.getQg()).isEqualTo(UPDATED_QG);
        assertThat(testGenerator.getQmax()).isEqualTo(DEFAULT_QMAX);
        assertThat(testGenerator.getQmin()).isEqualTo(UPDATED_QMIN);
        assertThat(testGenerator.getVg()).isEqualTo(UPDATED_VG);
        assertThat(testGenerator.getmBase()).isEqualTo(DEFAULT_M_BASE);
        assertThat(testGenerator.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testGenerator.getPmax()).isEqualTo(DEFAULT_PMAX);
        assertThat(testGenerator.getPmin()).isEqualTo(DEFAULT_PMIN);
        assertThat(testGenerator.getPc1()).isEqualTo(UPDATED_PC_1);
        assertThat(testGenerator.getPc2()).isEqualTo(DEFAULT_PC_2);
        assertThat(testGenerator.getQc1min()).isEqualTo(DEFAULT_QC_1_MIN);
        assertThat(testGenerator.getQc1max()).isEqualTo(DEFAULT_QC_1_MAX);
        assertThat(testGenerator.getQc2min()).isEqualTo(UPDATED_QC_2_MIN);
        assertThat(testGenerator.getQc2max()).isEqualTo(DEFAULT_QC_2_MAX);
        assertThat(testGenerator.getRampAgc()).isEqualTo(DEFAULT_RAMP_AGC);
        assertThat(testGenerator.getRamp10()).isEqualTo(DEFAULT_RAMP_10);
        assertThat(testGenerator.getRamp30()).isEqualTo(UPDATED_RAMP_30);
        assertThat(testGenerator.getRampQ()).isEqualTo(UPDATED_RAMP_Q);
        assertThat(testGenerator.getApf()).isEqualTo(UPDATED_APF);
    }

    @Test
    @Transactional
    void fullUpdateGeneratorWithPatch() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        int databaseSizeBeforeUpdate = generatorRepository.findAll().size();

        // Update the generator using partial update
        Generator partialUpdatedGenerator = new Generator();
        partialUpdatedGenerator.setId(generator.getId());

        partialUpdatedGenerator
            .busNum(UPDATED_BUS_NUM)
            .pg(UPDATED_PG)
            .qg(UPDATED_QG)
            .qmax(UPDATED_QMAX)
            .qmin(UPDATED_QMIN)
            .vg(UPDATED_VG)
            .mBase(UPDATED_M_BASE)
            .status(UPDATED_STATUS)
            .pmax(UPDATED_PMAX)
            .pmin(UPDATED_PMIN)
            .pc1(UPDATED_PC_1)
            .pc2(UPDATED_PC_2)
            .qc1min(UPDATED_QC_1_MIN)
            .qc1max(UPDATED_QC_1_MAX)
            .qc2min(UPDATED_QC_2_MIN)
            .qc2max(UPDATED_QC_2_MAX)
            .rampAgc(UPDATED_RAMP_AGC)
            .ramp10(UPDATED_RAMP_10)
            .ramp30(UPDATED_RAMP_30)
            .rampQ(UPDATED_RAMP_Q)
            .apf(UPDATED_APF);

        restGeneratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenerator.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenerator))
            )
            .andExpect(status().isOk());

        // Validate the Generator in the database
        List<Generator> generatorList = generatorRepository.findAll();
        assertThat(generatorList).hasSize(databaseSizeBeforeUpdate);
        Generator testGenerator = generatorList.get(generatorList.size() - 1);
        assertThat(testGenerator.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testGenerator.getPg()).isEqualTo(UPDATED_PG);
        assertThat(testGenerator.getQg()).isEqualTo(UPDATED_QG);
        assertThat(testGenerator.getQmax()).isEqualTo(UPDATED_QMAX);
        assertThat(testGenerator.getQmin()).isEqualTo(UPDATED_QMIN);
        assertThat(testGenerator.getVg()).isEqualTo(UPDATED_VG);
        assertThat(testGenerator.getmBase()).isEqualTo(UPDATED_M_BASE);
        assertThat(testGenerator.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testGenerator.getPmax()).isEqualTo(UPDATED_PMAX);
        assertThat(testGenerator.getPmin()).isEqualTo(UPDATED_PMIN);
        assertThat(testGenerator.getPc1()).isEqualTo(UPDATED_PC_1);
        assertThat(testGenerator.getPc2()).isEqualTo(UPDATED_PC_2);
        assertThat(testGenerator.getQc1min()).isEqualTo(UPDATED_QC_1_MIN);
        assertThat(testGenerator.getQc1max()).isEqualTo(UPDATED_QC_1_MAX);
        assertThat(testGenerator.getQc2min()).isEqualTo(UPDATED_QC_2_MIN);
        assertThat(testGenerator.getQc2max()).isEqualTo(UPDATED_QC_2_MAX);
        assertThat(testGenerator.getRampAgc()).isEqualTo(UPDATED_RAMP_AGC);
        assertThat(testGenerator.getRamp10()).isEqualTo(UPDATED_RAMP_10);
        assertThat(testGenerator.getRamp30()).isEqualTo(UPDATED_RAMP_30);
        assertThat(testGenerator.getRampQ()).isEqualTo(UPDATED_RAMP_Q);
        assertThat(testGenerator.getApf()).isEqualTo(UPDATED_APF);
    }

    @Test
    @Transactional
    void patchNonExistingGenerator() throws Exception {
        int databaseSizeBeforeUpdate = generatorRepository.findAll().size();
        generator.setId(count.incrementAndGet());

        // Create the Generator
        GeneratorDTO generatorDTO = generatorMapper.toDto(generator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeneratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, generatorDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(generatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Generator in the database
        List<Generator> generatorList = generatorRepository.findAll();
        assertThat(generatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGenerator() throws Exception {
        int databaseSizeBeforeUpdate = generatorRepository.findAll().size();
        generator.setId(count.incrementAndGet());

        // Create the Generator
        GeneratorDTO generatorDTO = generatorMapper.toDto(generator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(generatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Generator in the database
        List<Generator> generatorList = generatorRepository.findAll();
        assertThat(generatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGenerator() throws Exception {
        int databaseSizeBeforeUpdate = generatorRepository.findAll().size();
        generator.setId(count.incrementAndGet());

        // Create the Generator
        GeneratorDTO generatorDTO = generatorMapper.toDto(generator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneratorMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(generatorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Generator in the database
        List<Generator> generatorList = generatorRepository.findAll();
        assertThat(generatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGenerator() throws Exception {
        // Initialize the database
        generatorRepository.saveAndFlush(generator);

        int databaseSizeBeforeDelete = generatorRepository.findAll().size();

        // Delete the generator
        restGeneratorMockMvc
            .perform(delete(ENTITY_API_URL_ID, generator.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Generator> generatorList = generatorRepository.findAll();
        assertThat(generatorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
