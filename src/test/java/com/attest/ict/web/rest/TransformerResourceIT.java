package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Network;
import com.attest.ict.domain.Transformer;
import com.attest.ict.repository.TransformerRepository;
import com.attest.ict.service.criteria.TransformerCriteria;
import com.attest.ict.service.dto.TransformerDTO;
import com.attest.ict.service.mapper.TransformerMapper;
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
 * Integration tests for the {@link TransformerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransformerResourceIT {

    private static final Long DEFAULT_FBUS = 1L;
    private static final Long UPDATED_FBUS = 2L;
    private static final Long SMALLER_FBUS = 1L - 1L;

    private static final Long DEFAULT_TBUS = 1L;
    private static final Long UPDATED_TBUS = 2L;
    private static final Long SMALLER_TBUS = 1L - 1L;

    private static final Double DEFAULT_MIN = 1D;
    private static final Double UPDATED_MIN = 2D;
    private static final Double SMALLER_MIN = 1D - 1D;

    private static final Double DEFAULT_MAX = 1D;
    private static final Double UPDATED_MAX = 2D;
    private static final Double SMALLER_MAX = 1D - 1D;

    private static final Integer DEFAULT_TOTAL_TAPS = 1;
    private static final Integer UPDATED_TOTAL_TAPS = 2;
    private static final Integer SMALLER_TOTAL_TAPS = 1 - 1;

    private static final Integer DEFAULT_TAP = 1;
    private static final Integer UPDATED_TAP = 2;
    private static final Integer SMALLER_TAP = 1 - 1;

    private static final Integer DEFAULT_MANUFACTURE_YEAR = 1;
    private static final Integer UPDATED_MANUFACTURE_YEAR = 2;
    private static final Integer SMALLER_MANUFACTURE_YEAR = 1 - 1;

    private static final Integer DEFAULT_COMMISSIONING_YEAR = 1;
    private static final Integer UPDATED_COMMISSIONING_YEAR = 2;
    private static final Integer SMALLER_COMMISSIONING_YEAR = 1 - 1;

    private static final String ENTITY_API_URL = "/api/transformers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransformerRepository transformerRepository;

    @Autowired
    private TransformerMapper transformerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransformerMockMvc;

    private Transformer transformer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transformer createEntity(EntityManager em) {
        Transformer transformer = new Transformer()
            .fbus(DEFAULT_FBUS)
            .tbus(DEFAULT_TBUS)
            .min(DEFAULT_MIN)
            .max(DEFAULT_MAX)
            .totalTaps(DEFAULT_TOTAL_TAPS)
            .tap(DEFAULT_TAP)
            .manufactureYear(DEFAULT_MANUFACTURE_YEAR)
            .commissioningYear(DEFAULT_COMMISSIONING_YEAR);
        return transformer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transformer createUpdatedEntity(EntityManager em) {
        Transformer transformer = new Transformer()
            .fbus(UPDATED_FBUS)
            .tbus(UPDATED_TBUS)
            .min(UPDATED_MIN)
            .max(UPDATED_MAX)
            .totalTaps(UPDATED_TOTAL_TAPS)
            .tap(UPDATED_TAP)
            .manufactureYear(UPDATED_MANUFACTURE_YEAR)
            .commissioningYear(UPDATED_COMMISSIONING_YEAR);
        return transformer;
    }

    @BeforeEach
    public void initTest() {
        transformer = createEntity(em);
    }

    @Test
    @Transactional
    void createTransformer() throws Exception {
        int databaseSizeBeforeCreate = transformerRepository.findAll().size();
        // Create the Transformer
        TransformerDTO transformerDTO = transformerMapper.toDto(transformer);
        restTransformerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transformerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Transformer in the database
        List<Transformer> transformerList = transformerRepository.findAll();
        assertThat(transformerList).hasSize(databaseSizeBeforeCreate + 1);
        Transformer testTransformer = transformerList.get(transformerList.size() - 1);
        assertThat(testTransformer.getFbus()).isEqualTo(DEFAULT_FBUS);
        assertThat(testTransformer.getTbus()).isEqualTo(DEFAULT_TBUS);
        assertThat(testTransformer.getMin()).isEqualTo(DEFAULT_MIN);
        assertThat(testTransformer.getMax()).isEqualTo(DEFAULT_MAX);
        assertThat(testTransformer.getTotalTaps()).isEqualTo(DEFAULT_TOTAL_TAPS);
        assertThat(testTransformer.getTap()).isEqualTo(DEFAULT_TAP);
        assertThat(testTransformer.getManufactureYear()).isEqualTo(DEFAULT_MANUFACTURE_YEAR);
        assertThat(testTransformer.getCommissioningYear()).isEqualTo(DEFAULT_COMMISSIONING_YEAR);
    }

    @Test
    @Transactional
    void createTransformerWithExistingId() throws Exception {
        // Create the Transformer with an existing ID
        transformer.setId(1L);
        TransformerDTO transformerDTO = transformerMapper.toDto(transformer);

        int databaseSizeBeforeCreate = transformerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransformerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transformerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transformer in the database
        List<Transformer> transformerList = transformerRepository.findAll();
        assertThat(transformerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTransformers() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList
        restTransformerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transformer.getId().intValue())))
            .andExpect(jsonPath("$.[*].fbus").value(hasItem(DEFAULT_FBUS.intValue())))
            .andExpect(jsonPath("$.[*].tbus").value(hasItem(DEFAULT_TBUS.intValue())))
            .andExpect(jsonPath("$.[*].min").value(hasItem(DEFAULT_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].max").value(hasItem(DEFAULT_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].totalTaps").value(hasItem(DEFAULT_TOTAL_TAPS)))
            .andExpect(jsonPath("$.[*].tap").value(hasItem(DEFAULT_TAP)))
            .andExpect(jsonPath("$.[*].manufactureYear").value(hasItem(DEFAULT_MANUFACTURE_YEAR)))
            .andExpect(jsonPath("$.[*].commissioningYear").value(hasItem(DEFAULT_COMMISSIONING_YEAR)));
    }

    @Test
    @Transactional
    void getTransformer() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get the transformer
        restTransformerMockMvc
            .perform(get(ENTITY_API_URL_ID, transformer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transformer.getId().intValue()))
            .andExpect(jsonPath("$.fbus").value(DEFAULT_FBUS.intValue()))
            .andExpect(jsonPath("$.tbus").value(DEFAULT_TBUS.intValue()))
            .andExpect(jsonPath("$.min").value(DEFAULT_MIN.doubleValue()))
            .andExpect(jsonPath("$.max").value(DEFAULT_MAX.doubleValue()))
            .andExpect(jsonPath("$.totalTaps").value(DEFAULT_TOTAL_TAPS))
            .andExpect(jsonPath("$.tap").value(DEFAULT_TAP))
            .andExpect(jsonPath("$.manufactureYear").value(DEFAULT_MANUFACTURE_YEAR))
            .andExpect(jsonPath("$.commissioningYear").value(DEFAULT_COMMISSIONING_YEAR));
    }

    @Test
    @Transactional
    void getTransformersByIdFiltering() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        Long id = transformer.getId();

        defaultTransformerShouldBeFound("id.equals=" + id);
        defaultTransformerShouldNotBeFound("id.notEquals=" + id);

        defaultTransformerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTransformerShouldNotBeFound("id.greaterThan=" + id);

        defaultTransformerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTransformerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransformersByFbusIsEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where fbus equals to DEFAULT_FBUS
        defaultTransformerShouldBeFound("fbus.equals=" + DEFAULT_FBUS);

        // Get all the transformerList where fbus equals to UPDATED_FBUS
        defaultTransformerShouldNotBeFound("fbus.equals=" + UPDATED_FBUS);
    }

    @Test
    @Transactional
    void getAllTransformersByFbusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where fbus not equals to DEFAULT_FBUS
        defaultTransformerShouldNotBeFound("fbus.notEquals=" + DEFAULT_FBUS);

        // Get all the transformerList where fbus not equals to UPDATED_FBUS
        defaultTransformerShouldBeFound("fbus.notEquals=" + UPDATED_FBUS);
    }

    @Test
    @Transactional
    void getAllTransformersByFbusIsInShouldWork() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where fbus in DEFAULT_FBUS or UPDATED_FBUS
        defaultTransformerShouldBeFound("fbus.in=" + DEFAULT_FBUS + "," + UPDATED_FBUS);

        // Get all the transformerList where fbus equals to UPDATED_FBUS
        defaultTransformerShouldNotBeFound("fbus.in=" + UPDATED_FBUS);
    }

    @Test
    @Transactional
    void getAllTransformersByFbusIsNullOrNotNull() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where fbus is not null
        defaultTransformerShouldBeFound("fbus.specified=true");

        // Get all the transformerList where fbus is null
        defaultTransformerShouldNotBeFound("fbus.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformersByFbusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where fbus is greater than or equal to DEFAULT_FBUS
        defaultTransformerShouldBeFound("fbus.greaterThanOrEqual=" + DEFAULT_FBUS);

        // Get all the transformerList where fbus is greater than or equal to UPDATED_FBUS
        defaultTransformerShouldNotBeFound("fbus.greaterThanOrEqual=" + UPDATED_FBUS);
    }

    @Test
    @Transactional
    void getAllTransformersByFbusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where fbus is less than or equal to DEFAULT_FBUS
        defaultTransformerShouldBeFound("fbus.lessThanOrEqual=" + DEFAULT_FBUS);

        // Get all the transformerList where fbus is less than or equal to SMALLER_FBUS
        defaultTransformerShouldNotBeFound("fbus.lessThanOrEqual=" + SMALLER_FBUS);
    }

    @Test
    @Transactional
    void getAllTransformersByFbusIsLessThanSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where fbus is less than DEFAULT_FBUS
        defaultTransformerShouldNotBeFound("fbus.lessThan=" + DEFAULT_FBUS);

        // Get all the transformerList where fbus is less than UPDATED_FBUS
        defaultTransformerShouldBeFound("fbus.lessThan=" + UPDATED_FBUS);
    }

    @Test
    @Transactional
    void getAllTransformersByFbusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where fbus is greater than DEFAULT_FBUS
        defaultTransformerShouldNotBeFound("fbus.greaterThan=" + DEFAULT_FBUS);

        // Get all the transformerList where fbus is greater than SMALLER_FBUS
        defaultTransformerShouldBeFound("fbus.greaterThan=" + SMALLER_FBUS);
    }

    @Test
    @Transactional
    void getAllTransformersByTbusIsEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where tbus equals to DEFAULT_TBUS
        defaultTransformerShouldBeFound("tbus.equals=" + DEFAULT_TBUS);

        // Get all the transformerList where tbus equals to UPDATED_TBUS
        defaultTransformerShouldNotBeFound("tbus.equals=" + UPDATED_TBUS);
    }

    @Test
    @Transactional
    void getAllTransformersByTbusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where tbus not equals to DEFAULT_TBUS
        defaultTransformerShouldNotBeFound("tbus.notEquals=" + DEFAULT_TBUS);

        // Get all the transformerList where tbus not equals to UPDATED_TBUS
        defaultTransformerShouldBeFound("tbus.notEquals=" + UPDATED_TBUS);
    }

    @Test
    @Transactional
    void getAllTransformersByTbusIsInShouldWork() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where tbus in DEFAULT_TBUS or UPDATED_TBUS
        defaultTransformerShouldBeFound("tbus.in=" + DEFAULT_TBUS + "," + UPDATED_TBUS);

        // Get all the transformerList where tbus equals to UPDATED_TBUS
        defaultTransformerShouldNotBeFound("tbus.in=" + UPDATED_TBUS);
    }

    @Test
    @Transactional
    void getAllTransformersByTbusIsNullOrNotNull() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where tbus is not null
        defaultTransformerShouldBeFound("tbus.specified=true");

        // Get all the transformerList where tbus is null
        defaultTransformerShouldNotBeFound("tbus.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformersByTbusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where tbus is greater than or equal to DEFAULT_TBUS
        defaultTransformerShouldBeFound("tbus.greaterThanOrEqual=" + DEFAULT_TBUS);

        // Get all the transformerList where tbus is greater than or equal to UPDATED_TBUS
        defaultTransformerShouldNotBeFound("tbus.greaterThanOrEqual=" + UPDATED_TBUS);
    }

    @Test
    @Transactional
    void getAllTransformersByTbusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where tbus is less than or equal to DEFAULT_TBUS
        defaultTransformerShouldBeFound("tbus.lessThanOrEqual=" + DEFAULT_TBUS);

        // Get all the transformerList where tbus is less than or equal to SMALLER_TBUS
        defaultTransformerShouldNotBeFound("tbus.lessThanOrEqual=" + SMALLER_TBUS);
    }

    @Test
    @Transactional
    void getAllTransformersByTbusIsLessThanSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where tbus is less than DEFAULT_TBUS
        defaultTransformerShouldNotBeFound("tbus.lessThan=" + DEFAULT_TBUS);

        // Get all the transformerList where tbus is less than UPDATED_TBUS
        defaultTransformerShouldBeFound("tbus.lessThan=" + UPDATED_TBUS);
    }

    @Test
    @Transactional
    void getAllTransformersByTbusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where tbus is greater than DEFAULT_TBUS
        defaultTransformerShouldNotBeFound("tbus.greaterThan=" + DEFAULT_TBUS);

        // Get all the transformerList where tbus is greater than SMALLER_TBUS
        defaultTransformerShouldBeFound("tbus.greaterThan=" + SMALLER_TBUS);
    }

    @Test
    @Transactional
    void getAllTransformersByMinIsEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where min equals to DEFAULT_MIN
        defaultTransformerShouldBeFound("min.equals=" + DEFAULT_MIN);

        // Get all the transformerList where min equals to UPDATED_MIN
        defaultTransformerShouldNotBeFound("min.equals=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllTransformersByMinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where min not equals to DEFAULT_MIN
        defaultTransformerShouldNotBeFound("min.notEquals=" + DEFAULT_MIN);

        // Get all the transformerList where min not equals to UPDATED_MIN
        defaultTransformerShouldBeFound("min.notEquals=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllTransformersByMinIsInShouldWork() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where min in DEFAULT_MIN or UPDATED_MIN
        defaultTransformerShouldBeFound("min.in=" + DEFAULT_MIN + "," + UPDATED_MIN);

        // Get all the transformerList where min equals to UPDATED_MIN
        defaultTransformerShouldNotBeFound("min.in=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllTransformersByMinIsNullOrNotNull() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where min is not null
        defaultTransformerShouldBeFound("min.specified=true");

        // Get all the transformerList where min is null
        defaultTransformerShouldNotBeFound("min.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformersByMinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where min is greater than or equal to DEFAULT_MIN
        defaultTransformerShouldBeFound("min.greaterThanOrEqual=" + DEFAULT_MIN);

        // Get all the transformerList where min is greater than or equal to UPDATED_MIN
        defaultTransformerShouldNotBeFound("min.greaterThanOrEqual=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllTransformersByMinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where min is less than or equal to DEFAULT_MIN
        defaultTransformerShouldBeFound("min.lessThanOrEqual=" + DEFAULT_MIN);

        // Get all the transformerList where min is less than or equal to SMALLER_MIN
        defaultTransformerShouldNotBeFound("min.lessThanOrEqual=" + SMALLER_MIN);
    }

    @Test
    @Transactional
    void getAllTransformersByMinIsLessThanSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where min is less than DEFAULT_MIN
        defaultTransformerShouldNotBeFound("min.lessThan=" + DEFAULT_MIN);

        // Get all the transformerList where min is less than UPDATED_MIN
        defaultTransformerShouldBeFound("min.lessThan=" + UPDATED_MIN);
    }

    @Test
    @Transactional
    void getAllTransformersByMinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where min is greater than DEFAULT_MIN
        defaultTransformerShouldNotBeFound("min.greaterThan=" + DEFAULT_MIN);

        // Get all the transformerList where min is greater than SMALLER_MIN
        defaultTransformerShouldBeFound("min.greaterThan=" + SMALLER_MIN);
    }

    @Test
    @Transactional
    void getAllTransformersByMaxIsEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where max equals to DEFAULT_MAX
        defaultTransformerShouldBeFound("max.equals=" + DEFAULT_MAX);

        // Get all the transformerList where max equals to UPDATED_MAX
        defaultTransformerShouldNotBeFound("max.equals=" + UPDATED_MAX);
    }

    @Test
    @Transactional
    void getAllTransformersByMaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where max not equals to DEFAULT_MAX
        defaultTransformerShouldNotBeFound("max.notEquals=" + DEFAULT_MAX);

        // Get all the transformerList where max not equals to UPDATED_MAX
        defaultTransformerShouldBeFound("max.notEquals=" + UPDATED_MAX);
    }

    @Test
    @Transactional
    void getAllTransformersByMaxIsInShouldWork() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where max in DEFAULT_MAX or UPDATED_MAX
        defaultTransformerShouldBeFound("max.in=" + DEFAULT_MAX + "," + UPDATED_MAX);

        // Get all the transformerList where max equals to UPDATED_MAX
        defaultTransformerShouldNotBeFound("max.in=" + UPDATED_MAX);
    }

    @Test
    @Transactional
    void getAllTransformersByMaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where max is not null
        defaultTransformerShouldBeFound("max.specified=true");

        // Get all the transformerList where max is null
        defaultTransformerShouldNotBeFound("max.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformersByMaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where max is greater than or equal to DEFAULT_MAX
        defaultTransformerShouldBeFound("max.greaterThanOrEqual=" + DEFAULT_MAX);

        // Get all the transformerList where max is greater than or equal to UPDATED_MAX
        defaultTransformerShouldNotBeFound("max.greaterThanOrEqual=" + UPDATED_MAX);
    }

    @Test
    @Transactional
    void getAllTransformersByMaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where max is less than or equal to DEFAULT_MAX
        defaultTransformerShouldBeFound("max.lessThanOrEqual=" + DEFAULT_MAX);

        // Get all the transformerList where max is less than or equal to SMALLER_MAX
        defaultTransformerShouldNotBeFound("max.lessThanOrEqual=" + SMALLER_MAX);
    }

    @Test
    @Transactional
    void getAllTransformersByMaxIsLessThanSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where max is less than DEFAULT_MAX
        defaultTransformerShouldNotBeFound("max.lessThan=" + DEFAULT_MAX);

        // Get all the transformerList where max is less than UPDATED_MAX
        defaultTransformerShouldBeFound("max.lessThan=" + UPDATED_MAX);
    }

    @Test
    @Transactional
    void getAllTransformersByMaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where max is greater than DEFAULT_MAX
        defaultTransformerShouldNotBeFound("max.greaterThan=" + DEFAULT_MAX);

        // Get all the transformerList where max is greater than SMALLER_MAX
        defaultTransformerShouldBeFound("max.greaterThan=" + SMALLER_MAX);
    }

    @Test
    @Transactional
    void getAllTransformersByTotalTapsIsEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where totalTaps equals to DEFAULT_TOTAL_TAPS
        defaultTransformerShouldBeFound("totalTaps.equals=" + DEFAULT_TOTAL_TAPS);

        // Get all the transformerList where totalTaps equals to UPDATED_TOTAL_TAPS
        defaultTransformerShouldNotBeFound("totalTaps.equals=" + UPDATED_TOTAL_TAPS);
    }

    @Test
    @Transactional
    void getAllTransformersByTotalTapsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where totalTaps not equals to DEFAULT_TOTAL_TAPS
        defaultTransformerShouldNotBeFound("totalTaps.notEquals=" + DEFAULT_TOTAL_TAPS);

        // Get all the transformerList where totalTaps not equals to UPDATED_TOTAL_TAPS
        defaultTransformerShouldBeFound("totalTaps.notEquals=" + UPDATED_TOTAL_TAPS);
    }

    @Test
    @Transactional
    void getAllTransformersByTotalTapsIsInShouldWork() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where totalTaps in DEFAULT_TOTAL_TAPS or UPDATED_TOTAL_TAPS
        defaultTransformerShouldBeFound("totalTaps.in=" + DEFAULT_TOTAL_TAPS + "," + UPDATED_TOTAL_TAPS);

        // Get all the transformerList where totalTaps equals to UPDATED_TOTAL_TAPS
        defaultTransformerShouldNotBeFound("totalTaps.in=" + UPDATED_TOTAL_TAPS);
    }

    @Test
    @Transactional
    void getAllTransformersByTotalTapsIsNullOrNotNull() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where totalTaps is not null
        defaultTransformerShouldBeFound("totalTaps.specified=true");

        // Get all the transformerList where totalTaps is null
        defaultTransformerShouldNotBeFound("totalTaps.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformersByTotalTapsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where totalTaps is greater than or equal to DEFAULT_TOTAL_TAPS
        defaultTransformerShouldBeFound("totalTaps.greaterThanOrEqual=" + DEFAULT_TOTAL_TAPS);

        // Get all the transformerList where totalTaps is greater than or equal to UPDATED_TOTAL_TAPS
        defaultTransformerShouldNotBeFound("totalTaps.greaterThanOrEqual=" + UPDATED_TOTAL_TAPS);
    }

    @Test
    @Transactional
    void getAllTransformersByTotalTapsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where totalTaps is less than or equal to DEFAULT_TOTAL_TAPS
        defaultTransformerShouldBeFound("totalTaps.lessThanOrEqual=" + DEFAULT_TOTAL_TAPS);

        // Get all the transformerList where totalTaps is less than or equal to SMALLER_TOTAL_TAPS
        defaultTransformerShouldNotBeFound("totalTaps.lessThanOrEqual=" + SMALLER_TOTAL_TAPS);
    }

    @Test
    @Transactional
    void getAllTransformersByTotalTapsIsLessThanSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where totalTaps is less than DEFAULT_TOTAL_TAPS
        defaultTransformerShouldNotBeFound("totalTaps.lessThan=" + DEFAULT_TOTAL_TAPS);

        // Get all the transformerList where totalTaps is less than UPDATED_TOTAL_TAPS
        defaultTransformerShouldBeFound("totalTaps.lessThan=" + UPDATED_TOTAL_TAPS);
    }

    @Test
    @Transactional
    void getAllTransformersByTotalTapsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where totalTaps is greater than DEFAULT_TOTAL_TAPS
        defaultTransformerShouldNotBeFound("totalTaps.greaterThan=" + DEFAULT_TOTAL_TAPS);

        // Get all the transformerList where totalTaps is greater than SMALLER_TOTAL_TAPS
        defaultTransformerShouldBeFound("totalTaps.greaterThan=" + SMALLER_TOTAL_TAPS);
    }

    @Test
    @Transactional
    void getAllTransformersByTapIsEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where tap equals to DEFAULT_TAP
        defaultTransformerShouldBeFound("tap.equals=" + DEFAULT_TAP);

        // Get all the transformerList where tap equals to UPDATED_TAP
        defaultTransformerShouldNotBeFound("tap.equals=" + UPDATED_TAP);
    }

    @Test
    @Transactional
    void getAllTransformersByTapIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where tap not equals to DEFAULT_TAP
        defaultTransformerShouldNotBeFound("tap.notEquals=" + DEFAULT_TAP);

        // Get all the transformerList where tap not equals to UPDATED_TAP
        defaultTransformerShouldBeFound("tap.notEquals=" + UPDATED_TAP);
    }

    @Test
    @Transactional
    void getAllTransformersByTapIsInShouldWork() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where tap in DEFAULT_TAP or UPDATED_TAP
        defaultTransformerShouldBeFound("tap.in=" + DEFAULT_TAP + "," + UPDATED_TAP);

        // Get all the transformerList where tap equals to UPDATED_TAP
        defaultTransformerShouldNotBeFound("tap.in=" + UPDATED_TAP);
    }

    @Test
    @Transactional
    void getAllTransformersByTapIsNullOrNotNull() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where tap is not null
        defaultTransformerShouldBeFound("tap.specified=true");

        // Get all the transformerList where tap is null
        defaultTransformerShouldNotBeFound("tap.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformersByTapIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where tap is greater than or equal to DEFAULT_TAP
        defaultTransformerShouldBeFound("tap.greaterThanOrEqual=" + DEFAULT_TAP);

        // Get all the transformerList where tap is greater than or equal to UPDATED_TAP
        defaultTransformerShouldNotBeFound("tap.greaterThanOrEqual=" + UPDATED_TAP);
    }

    @Test
    @Transactional
    void getAllTransformersByTapIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where tap is less than or equal to DEFAULT_TAP
        defaultTransformerShouldBeFound("tap.lessThanOrEqual=" + DEFAULT_TAP);

        // Get all the transformerList where tap is less than or equal to SMALLER_TAP
        defaultTransformerShouldNotBeFound("tap.lessThanOrEqual=" + SMALLER_TAP);
    }

    @Test
    @Transactional
    void getAllTransformersByTapIsLessThanSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where tap is less than DEFAULT_TAP
        defaultTransformerShouldNotBeFound("tap.lessThan=" + DEFAULT_TAP);

        // Get all the transformerList where tap is less than UPDATED_TAP
        defaultTransformerShouldBeFound("tap.lessThan=" + UPDATED_TAP);
    }

    @Test
    @Transactional
    void getAllTransformersByTapIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where tap is greater than DEFAULT_TAP
        defaultTransformerShouldNotBeFound("tap.greaterThan=" + DEFAULT_TAP);

        // Get all the transformerList where tap is greater than SMALLER_TAP
        defaultTransformerShouldBeFound("tap.greaterThan=" + SMALLER_TAP);
    }

    @Test
    @Transactional
    void getAllTransformersByManufactureYearIsEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where manufactureYear equals to DEFAULT_MANUFACTURE_YEAR
        defaultTransformerShouldBeFound("manufactureYear.equals=" + DEFAULT_MANUFACTURE_YEAR);

        // Get all the transformerList where manufactureYear equals to UPDATED_MANUFACTURE_YEAR
        defaultTransformerShouldNotBeFound("manufactureYear.equals=" + UPDATED_MANUFACTURE_YEAR);
    }

    @Test
    @Transactional
    void getAllTransformersByManufactureYearIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where manufactureYear not equals to DEFAULT_MANUFACTURE_YEAR
        defaultTransformerShouldNotBeFound("manufactureYear.notEquals=" + DEFAULT_MANUFACTURE_YEAR);

        // Get all the transformerList where manufactureYear not equals to UPDATED_MANUFACTURE_YEAR
        defaultTransformerShouldBeFound("manufactureYear.notEquals=" + UPDATED_MANUFACTURE_YEAR);
    }

    @Test
    @Transactional
    void getAllTransformersByManufactureYearIsInShouldWork() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where manufactureYear in DEFAULT_MANUFACTURE_YEAR or UPDATED_MANUFACTURE_YEAR
        defaultTransformerShouldBeFound("manufactureYear.in=" + DEFAULT_MANUFACTURE_YEAR + "," + UPDATED_MANUFACTURE_YEAR);

        // Get all the transformerList where manufactureYear equals to UPDATED_MANUFACTURE_YEAR
        defaultTransformerShouldNotBeFound("manufactureYear.in=" + UPDATED_MANUFACTURE_YEAR);
    }

    @Test
    @Transactional
    void getAllTransformersByManufactureYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where manufactureYear is not null
        defaultTransformerShouldBeFound("manufactureYear.specified=true");

        // Get all the transformerList where manufactureYear is null
        defaultTransformerShouldNotBeFound("manufactureYear.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformersByManufactureYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where manufactureYear is greater than or equal to DEFAULT_MANUFACTURE_YEAR
        defaultTransformerShouldBeFound("manufactureYear.greaterThanOrEqual=" + DEFAULT_MANUFACTURE_YEAR);

        // Get all the transformerList where manufactureYear is greater than or equal to UPDATED_MANUFACTURE_YEAR
        defaultTransformerShouldNotBeFound("manufactureYear.greaterThanOrEqual=" + UPDATED_MANUFACTURE_YEAR);
    }

    @Test
    @Transactional
    void getAllTransformersByManufactureYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where manufactureYear is less than or equal to DEFAULT_MANUFACTURE_YEAR
        defaultTransformerShouldBeFound("manufactureYear.lessThanOrEqual=" + DEFAULT_MANUFACTURE_YEAR);

        // Get all the transformerList where manufactureYear is less than or equal to SMALLER_MANUFACTURE_YEAR
        defaultTransformerShouldNotBeFound("manufactureYear.lessThanOrEqual=" + SMALLER_MANUFACTURE_YEAR);
    }

    @Test
    @Transactional
    void getAllTransformersByManufactureYearIsLessThanSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where manufactureYear is less than DEFAULT_MANUFACTURE_YEAR
        defaultTransformerShouldNotBeFound("manufactureYear.lessThan=" + DEFAULT_MANUFACTURE_YEAR);

        // Get all the transformerList where manufactureYear is less than UPDATED_MANUFACTURE_YEAR
        defaultTransformerShouldBeFound("manufactureYear.lessThan=" + UPDATED_MANUFACTURE_YEAR);
    }

    @Test
    @Transactional
    void getAllTransformersByManufactureYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where manufactureYear is greater than DEFAULT_MANUFACTURE_YEAR
        defaultTransformerShouldNotBeFound("manufactureYear.greaterThan=" + DEFAULT_MANUFACTURE_YEAR);

        // Get all the transformerList where manufactureYear is greater than SMALLER_MANUFACTURE_YEAR
        defaultTransformerShouldBeFound("manufactureYear.greaterThan=" + SMALLER_MANUFACTURE_YEAR);
    }

    @Test
    @Transactional
    void getAllTransformersByCommissioningYearIsEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where commissioningYear equals to DEFAULT_COMMISSIONING_YEAR
        defaultTransformerShouldBeFound("commissioningYear.equals=" + DEFAULT_COMMISSIONING_YEAR);

        // Get all the transformerList where commissioningYear equals to UPDATED_COMMISSIONING_YEAR
        defaultTransformerShouldNotBeFound("commissioningYear.equals=" + UPDATED_COMMISSIONING_YEAR);
    }

    @Test
    @Transactional
    void getAllTransformersByCommissioningYearIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where commissioningYear not equals to DEFAULT_COMMISSIONING_YEAR
        defaultTransformerShouldNotBeFound("commissioningYear.notEquals=" + DEFAULT_COMMISSIONING_YEAR);

        // Get all the transformerList where commissioningYear not equals to UPDATED_COMMISSIONING_YEAR
        defaultTransformerShouldBeFound("commissioningYear.notEquals=" + UPDATED_COMMISSIONING_YEAR);
    }

    @Test
    @Transactional
    void getAllTransformersByCommissioningYearIsInShouldWork() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where commissioningYear in DEFAULT_COMMISSIONING_YEAR or UPDATED_COMMISSIONING_YEAR
        defaultTransformerShouldBeFound("commissioningYear.in=" + DEFAULT_COMMISSIONING_YEAR + "," + UPDATED_COMMISSIONING_YEAR);

        // Get all the transformerList where commissioningYear equals to UPDATED_COMMISSIONING_YEAR
        defaultTransformerShouldNotBeFound("commissioningYear.in=" + UPDATED_COMMISSIONING_YEAR);
    }

    @Test
    @Transactional
    void getAllTransformersByCommissioningYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where commissioningYear is not null
        defaultTransformerShouldBeFound("commissioningYear.specified=true");

        // Get all the transformerList where commissioningYear is null
        defaultTransformerShouldNotBeFound("commissioningYear.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformersByCommissioningYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where commissioningYear is greater than or equal to DEFAULT_COMMISSIONING_YEAR
        defaultTransformerShouldBeFound("commissioningYear.greaterThanOrEqual=" + DEFAULT_COMMISSIONING_YEAR);

        // Get all the transformerList where commissioningYear is greater than or equal to UPDATED_COMMISSIONING_YEAR
        defaultTransformerShouldNotBeFound("commissioningYear.greaterThanOrEqual=" + UPDATED_COMMISSIONING_YEAR);
    }

    @Test
    @Transactional
    void getAllTransformersByCommissioningYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where commissioningYear is less than or equal to DEFAULT_COMMISSIONING_YEAR
        defaultTransformerShouldBeFound("commissioningYear.lessThanOrEqual=" + DEFAULT_COMMISSIONING_YEAR);

        // Get all the transformerList where commissioningYear is less than or equal to SMALLER_COMMISSIONING_YEAR
        defaultTransformerShouldNotBeFound("commissioningYear.lessThanOrEqual=" + SMALLER_COMMISSIONING_YEAR);
    }

    @Test
    @Transactional
    void getAllTransformersByCommissioningYearIsLessThanSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where commissioningYear is less than DEFAULT_COMMISSIONING_YEAR
        defaultTransformerShouldNotBeFound("commissioningYear.lessThan=" + DEFAULT_COMMISSIONING_YEAR);

        // Get all the transformerList where commissioningYear is less than UPDATED_COMMISSIONING_YEAR
        defaultTransformerShouldBeFound("commissioningYear.lessThan=" + UPDATED_COMMISSIONING_YEAR);
    }

    @Test
    @Transactional
    void getAllTransformersByCommissioningYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        // Get all the transformerList where commissioningYear is greater than DEFAULT_COMMISSIONING_YEAR
        defaultTransformerShouldNotBeFound("commissioningYear.greaterThan=" + DEFAULT_COMMISSIONING_YEAR);

        // Get all the transformerList where commissioningYear is greater than SMALLER_COMMISSIONING_YEAR
        defaultTransformerShouldBeFound("commissioningYear.greaterThan=" + SMALLER_COMMISSIONING_YEAR);
    }

    @Test
    @Transactional
    void getAllTransformersByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);
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
        transformer.setNetwork(network);
        transformerRepository.saveAndFlush(transformer);
        Long networkId = network.getId();

        // Get all the transformerList where network equals to networkId
        defaultTransformerShouldBeFound("networkId.equals=" + networkId);

        // Get all the transformerList where network equals to (networkId + 1)
        defaultTransformerShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransformerShouldBeFound(String filter) throws Exception {
        restTransformerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transformer.getId().intValue())))
            .andExpect(jsonPath("$.[*].fbus").value(hasItem(DEFAULT_FBUS.intValue())))
            .andExpect(jsonPath("$.[*].tbus").value(hasItem(DEFAULT_TBUS.intValue())))
            .andExpect(jsonPath("$.[*].min").value(hasItem(DEFAULT_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].max").value(hasItem(DEFAULT_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].totalTaps").value(hasItem(DEFAULT_TOTAL_TAPS)))
            .andExpect(jsonPath("$.[*].tap").value(hasItem(DEFAULT_TAP)))
            .andExpect(jsonPath("$.[*].manufactureYear").value(hasItem(DEFAULT_MANUFACTURE_YEAR)))
            .andExpect(jsonPath("$.[*].commissioningYear").value(hasItem(DEFAULT_COMMISSIONING_YEAR)));

        // Check, that the count call also returns 1
        restTransformerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransformerShouldNotBeFound(String filter) throws Exception {
        restTransformerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransformerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransformer() throws Exception {
        // Get the transformer
        restTransformerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTransformer() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        int databaseSizeBeforeUpdate = transformerRepository.findAll().size();

        // Update the transformer
        Transformer updatedTransformer = transformerRepository.findById(transformer.getId()).get();
        // Disconnect from session so that the updates on updatedTransformer are not directly saved in db
        em.detach(updatedTransformer);
        updatedTransformer
            .fbus(UPDATED_FBUS)
            .tbus(UPDATED_TBUS)
            .min(UPDATED_MIN)
            .max(UPDATED_MAX)
            .totalTaps(UPDATED_TOTAL_TAPS)
            .tap(UPDATED_TAP)
            .manufactureYear(UPDATED_MANUFACTURE_YEAR)
            .commissioningYear(UPDATED_COMMISSIONING_YEAR);
        TransformerDTO transformerDTO = transformerMapper.toDto(updatedTransformer);

        restTransformerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transformerDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transformerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Transformer in the database
        List<Transformer> transformerList = transformerRepository.findAll();
        assertThat(transformerList).hasSize(databaseSizeBeforeUpdate);
        Transformer testTransformer = transformerList.get(transformerList.size() - 1);
        assertThat(testTransformer.getFbus()).isEqualTo(UPDATED_FBUS);
        assertThat(testTransformer.getTbus()).isEqualTo(UPDATED_TBUS);
        assertThat(testTransformer.getMin()).isEqualTo(UPDATED_MIN);
        assertThat(testTransformer.getMax()).isEqualTo(UPDATED_MAX);
        assertThat(testTransformer.getTotalTaps()).isEqualTo(UPDATED_TOTAL_TAPS);
        assertThat(testTransformer.getTap()).isEqualTo(UPDATED_TAP);
        assertThat(testTransformer.getManufactureYear()).isEqualTo(UPDATED_MANUFACTURE_YEAR);
        assertThat(testTransformer.getCommissioningYear()).isEqualTo(UPDATED_COMMISSIONING_YEAR);
    }

    @Test
    @Transactional
    void putNonExistingTransformer() throws Exception {
        int databaseSizeBeforeUpdate = transformerRepository.findAll().size();
        transformer.setId(count.incrementAndGet());

        // Create the Transformer
        TransformerDTO transformerDTO = transformerMapper.toDto(transformer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransformerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transformerDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transformerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transformer in the database
        List<Transformer> transformerList = transformerRepository.findAll();
        assertThat(transformerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransformer() throws Exception {
        int databaseSizeBeforeUpdate = transformerRepository.findAll().size();
        transformer.setId(count.incrementAndGet());

        // Create the Transformer
        TransformerDTO transformerDTO = transformerMapper.toDto(transformer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transformerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transformer in the database
        List<Transformer> transformerList = transformerRepository.findAll();
        assertThat(transformerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransformer() throws Exception {
        int databaseSizeBeforeUpdate = transformerRepository.findAll().size();
        transformer.setId(count.incrementAndGet());

        // Create the Transformer
        TransformerDTO transformerDTO = transformerMapper.toDto(transformer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformerMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transformerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transformer in the database
        List<Transformer> transformerList = transformerRepository.findAll();
        assertThat(transformerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransformerWithPatch() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        int databaseSizeBeforeUpdate = transformerRepository.findAll().size();

        // Update the transformer using partial update
        Transformer partialUpdatedTransformer = new Transformer();
        partialUpdatedTransformer.setId(transformer.getId());

        partialUpdatedTransformer.fbus(UPDATED_FBUS).tbus(UPDATED_TBUS).manufactureYear(UPDATED_MANUFACTURE_YEAR);

        restTransformerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransformer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransformer))
            )
            .andExpect(status().isOk());

        // Validate the Transformer in the database
        List<Transformer> transformerList = transformerRepository.findAll();
        assertThat(transformerList).hasSize(databaseSizeBeforeUpdate);
        Transformer testTransformer = transformerList.get(transformerList.size() - 1);
        assertThat(testTransformer.getFbus()).isEqualTo(UPDATED_FBUS);
        assertThat(testTransformer.getTbus()).isEqualTo(UPDATED_TBUS);
        assertThat(testTransformer.getMin()).isEqualTo(DEFAULT_MIN);
        assertThat(testTransformer.getMax()).isEqualTo(DEFAULT_MAX);
        assertThat(testTransformer.getTotalTaps()).isEqualTo(DEFAULT_TOTAL_TAPS);
        assertThat(testTransformer.getTap()).isEqualTo(DEFAULT_TAP);
        assertThat(testTransformer.getManufactureYear()).isEqualTo(UPDATED_MANUFACTURE_YEAR);
        assertThat(testTransformer.getCommissioningYear()).isEqualTo(DEFAULT_COMMISSIONING_YEAR);
    }

    @Test
    @Transactional
    void fullUpdateTransformerWithPatch() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        int databaseSizeBeforeUpdate = transformerRepository.findAll().size();

        // Update the transformer using partial update
        Transformer partialUpdatedTransformer = new Transformer();
        partialUpdatedTransformer.setId(transformer.getId());

        partialUpdatedTransformer
            .fbus(UPDATED_FBUS)
            .tbus(UPDATED_TBUS)
            .min(UPDATED_MIN)
            .max(UPDATED_MAX)
            .totalTaps(UPDATED_TOTAL_TAPS)
            .tap(UPDATED_TAP)
            .manufactureYear(UPDATED_MANUFACTURE_YEAR)
            .commissioningYear(UPDATED_COMMISSIONING_YEAR);

        restTransformerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransformer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransformer))
            )
            .andExpect(status().isOk());

        // Validate the Transformer in the database
        List<Transformer> transformerList = transformerRepository.findAll();
        assertThat(transformerList).hasSize(databaseSizeBeforeUpdate);
        Transformer testTransformer = transformerList.get(transformerList.size() - 1);
        assertThat(testTransformer.getFbus()).isEqualTo(UPDATED_FBUS);
        assertThat(testTransformer.getTbus()).isEqualTo(UPDATED_TBUS);
        assertThat(testTransformer.getMin()).isEqualTo(UPDATED_MIN);
        assertThat(testTransformer.getMax()).isEqualTo(UPDATED_MAX);
        assertThat(testTransformer.getTotalTaps()).isEqualTo(UPDATED_TOTAL_TAPS);
        assertThat(testTransformer.getTap()).isEqualTo(UPDATED_TAP);
        assertThat(testTransformer.getManufactureYear()).isEqualTo(UPDATED_MANUFACTURE_YEAR);
        assertThat(testTransformer.getCommissioningYear()).isEqualTo(UPDATED_COMMISSIONING_YEAR);
    }

    @Test
    @Transactional
    void patchNonExistingTransformer() throws Exception {
        int databaseSizeBeforeUpdate = transformerRepository.findAll().size();
        transformer.setId(count.incrementAndGet());

        // Create the Transformer
        TransformerDTO transformerDTO = transformerMapper.toDto(transformer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransformerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transformerDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transformerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transformer in the database
        List<Transformer> transformerList = transformerRepository.findAll();
        assertThat(transformerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransformer() throws Exception {
        int databaseSizeBeforeUpdate = transformerRepository.findAll().size();
        transformer.setId(count.incrementAndGet());

        // Create the Transformer
        TransformerDTO transformerDTO = transformerMapper.toDto(transformer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transformerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transformer in the database
        List<Transformer> transformerList = transformerRepository.findAll();
        assertThat(transformerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransformer() throws Exception {
        int databaseSizeBeforeUpdate = transformerRepository.findAll().size();
        transformer.setId(count.incrementAndGet());

        // Create the Transformer
        TransformerDTO transformerDTO = transformerMapper.toDto(transformer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transformerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transformer in the database
        List<Transformer> transformerList = transformerRepository.findAll();
        assertThat(transformerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransformer() throws Exception {
        // Initialize the database
        transformerRepository.saveAndFlush(transformer);

        int databaseSizeBeforeDelete = transformerRepository.findAll().size();

        // Delete the transformer
        restTransformerMockMvc
            .perform(delete(ENTITY_API_URL_ID, transformer.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Transformer> transformerList = transformerRepository.findAll();
        assertThat(transformerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
