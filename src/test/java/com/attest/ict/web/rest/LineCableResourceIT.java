package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.LineCable;
import com.attest.ict.domain.Network;
import com.attest.ict.repository.LineCableRepository;
import com.attest.ict.service.criteria.LineCableCriteria;
import com.attest.ict.service.dto.LineCableDTO;
import com.attest.ict.service.mapper.LineCableMapper;
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
 * Integration tests for the {@link LineCableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LineCableResourceIT {

    private static final Long DEFAULT_FBUS = 1L;
    private static final Long UPDATED_FBUS = 2L;
    private static final Long SMALLER_FBUS = 1L - 1L;

    private static final Long DEFAULT_TBUS = 1L;
    private static final Long UPDATED_TBUS = 2L;
    private static final Long SMALLER_TBUS = 1L - 1L;

    private static final Double DEFAULT_LENGTH_KM = 1D;
    private static final Double UPDATED_LENGTH_KM = 2D;
    private static final Double SMALLER_LENGTH_KM = 1D - 1D;

    private static final String DEFAULT_TYPE_OF_INSTALLATION = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_OF_INSTALLATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/line-cables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LineCableRepository lineCableRepository;

    @Autowired
    private LineCableMapper lineCableMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLineCableMockMvc;

    private LineCable lineCable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LineCable createEntity(EntityManager em) {
        LineCable lineCable = new LineCable()
            .fbus(DEFAULT_FBUS)
            .tbus(DEFAULT_TBUS)
            .lengthKm(DEFAULT_LENGTH_KM)
            .typeOfInstallation(DEFAULT_TYPE_OF_INSTALLATION);
        return lineCable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LineCable createUpdatedEntity(EntityManager em) {
        LineCable lineCable = new LineCable()
            .fbus(UPDATED_FBUS)
            .tbus(UPDATED_TBUS)
            .lengthKm(UPDATED_LENGTH_KM)
            .typeOfInstallation(UPDATED_TYPE_OF_INSTALLATION);
        return lineCable;
    }

    @BeforeEach
    public void initTest() {
        lineCable = createEntity(em);
    }

    @Test
    @Transactional
    void createLineCable() throws Exception {
        int databaseSizeBeforeCreate = lineCableRepository.findAll().size();
        // Create the LineCable
        LineCableDTO lineCableDTO = lineCableMapper.toDto(lineCable);
        restLineCableMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineCableDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LineCable in the database
        List<LineCable> lineCableList = lineCableRepository.findAll();
        assertThat(lineCableList).hasSize(databaseSizeBeforeCreate + 1);
        LineCable testLineCable = lineCableList.get(lineCableList.size() - 1);
        assertThat(testLineCable.getFbus()).isEqualTo(DEFAULT_FBUS);
        assertThat(testLineCable.getTbus()).isEqualTo(DEFAULT_TBUS);
        assertThat(testLineCable.getLengthKm()).isEqualTo(DEFAULT_LENGTH_KM);
        assertThat(testLineCable.getTypeOfInstallation()).isEqualTo(DEFAULT_TYPE_OF_INSTALLATION);
    }

    @Test
    @Transactional
    void createLineCableWithExistingId() throws Exception {
        // Create the LineCable with an existing ID
        lineCable.setId(1L);
        LineCableDTO lineCableDTO = lineCableMapper.toDto(lineCable);

        int databaseSizeBeforeCreate = lineCableRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLineCableMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineCableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LineCable in the database
        List<LineCable> lineCableList = lineCableRepository.findAll();
        assertThat(lineCableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLineCables() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList
        restLineCableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lineCable.getId().intValue())))
            .andExpect(jsonPath("$.[*].fbus").value(hasItem(DEFAULT_FBUS.intValue())))
            .andExpect(jsonPath("$.[*].tbus").value(hasItem(DEFAULT_TBUS.intValue())))
            .andExpect(jsonPath("$.[*].lengthKm").value(hasItem(DEFAULT_LENGTH_KM.doubleValue())))
            .andExpect(jsonPath("$.[*].typeOfInstallation").value(hasItem(DEFAULT_TYPE_OF_INSTALLATION)));
    }

    @Test
    @Transactional
    void getLineCable() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get the lineCable
        restLineCableMockMvc
            .perform(get(ENTITY_API_URL_ID, lineCable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lineCable.getId().intValue()))
            .andExpect(jsonPath("$.fbus").value(DEFAULT_FBUS.intValue()))
            .andExpect(jsonPath("$.tbus").value(DEFAULT_TBUS.intValue()))
            .andExpect(jsonPath("$.lengthKm").value(DEFAULT_LENGTH_KM.doubleValue()))
            .andExpect(jsonPath("$.typeOfInstallation").value(DEFAULT_TYPE_OF_INSTALLATION));
    }

    @Test
    @Transactional
    void getLineCablesByIdFiltering() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        Long id = lineCable.getId();

        defaultLineCableShouldBeFound("id.equals=" + id);
        defaultLineCableShouldNotBeFound("id.notEquals=" + id);

        defaultLineCableShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLineCableShouldNotBeFound("id.greaterThan=" + id);

        defaultLineCableShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLineCableShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLineCablesByFbusIsEqualToSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where fbus equals to DEFAULT_FBUS
        defaultLineCableShouldBeFound("fbus.equals=" + DEFAULT_FBUS);

        // Get all the lineCableList where fbus equals to UPDATED_FBUS
        defaultLineCableShouldNotBeFound("fbus.equals=" + UPDATED_FBUS);
    }

    @Test
    @Transactional
    void getAllLineCablesByFbusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where fbus not equals to DEFAULT_FBUS
        defaultLineCableShouldNotBeFound("fbus.notEquals=" + DEFAULT_FBUS);

        // Get all the lineCableList where fbus not equals to UPDATED_FBUS
        defaultLineCableShouldBeFound("fbus.notEquals=" + UPDATED_FBUS);
    }

    @Test
    @Transactional
    void getAllLineCablesByFbusIsInShouldWork() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where fbus in DEFAULT_FBUS or UPDATED_FBUS
        defaultLineCableShouldBeFound("fbus.in=" + DEFAULT_FBUS + "," + UPDATED_FBUS);

        // Get all the lineCableList where fbus equals to UPDATED_FBUS
        defaultLineCableShouldNotBeFound("fbus.in=" + UPDATED_FBUS);
    }

    @Test
    @Transactional
    void getAllLineCablesByFbusIsNullOrNotNull() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where fbus is not null
        defaultLineCableShouldBeFound("fbus.specified=true");

        // Get all the lineCableList where fbus is null
        defaultLineCableShouldNotBeFound("fbus.specified=false");
    }

    @Test
    @Transactional
    void getAllLineCablesByFbusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where fbus is greater than or equal to DEFAULT_FBUS
        defaultLineCableShouldBeFound("fbus.greaterThanOrEqual=" + DEFAULT_FBUS);

        // Get all the lineCableList where fbus is greater than or equal to UPDATED_FBUS
        defaultLineCableShouldNotBeFound("fbus.greaterThanOrEqual=" + UPDATED_FBUS);
    }

    @Test
    @Transactional
    void getAllLineCablesByFbusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where fbus is less than or equal to DEFAULT_FBUS
        defaultLineCableShouldBeFound("fbus.lessThanOrEqual=" + DEFAULT_FBUS);

        // Get all the lineCableList where fbus is less than or equal to SMALLER_FBUS
        defaultLineCableShouldNotBeFound("fbus.lessThanOrEqual=" + SMALLER_FBUS);
    }

    @Test
    @Transactional
    void getAllLineCablesByFbusIsLessThanSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where fbus is less than DEFAULT_FBUS
        defaultLineCableShouldNotBeFound("fbus.lessThan=" + DEFAULT_FBUS);

        // Get all the lineCableList where fbus is less than UPDATED_FBUS
        defaultLineCableShouldBeFound("fbus.lessThan=" + UPDATED_FBUS);
    }

    @Test
    @Transactional
    void getAllLineCablesByFbusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where fbus is greater than DEFAULT_FBUS
        defaultLineCableShouldNotBeFound("fbus.greaterThan=" + DEFAULT_FBUS);

        // Get all the lineCableList where fbus is greater than SMALLER_FBUS
        defaultLineCableShouldBeFound("fbus.greaterThan=" + SMALLER_FBUS);
    }

    @Test
    @Transactional
    void getAllLineCablesByTbusIsEqualToSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where tbus equals to DEFAULT_TBUS
        defaultLineCableShouldBeFound("tbus.equals=" + DEFAULT_TBUS);

        // Get all the lineCableList where tbus equals to UPDATED_TBUS
        defaultLineCableShouldNotBeFound("tbus.equals=" + UPDATED_TBUS);
    }

    @Test
    @Transactional
    void getAllLineCablesByTbusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where tbus not equals to DEFAULT_TBUS
        defaultLineCableShouldNotBeFound("tbus.notEquals=" + DEFAULT_TBUS);

        // Get all the lineCableList where tbus not equals to UPDATED_TBUS
        defaultLineCableShouldBeFound("tbus.notEquals=" + UPDATED_TBUS);
    }

    @Test
    @Transactional
    void getAllLineCablesByTbusIsInShouldWork() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where tbus in DEFAULT_TBUS or UPDATED_TBUS
        defaultLineCableShouldBeFound("tbus.in=" + DEFAULT_TBUS + "," + UPDATED_TBUS);

        // Get all the lineCableList where tbus equals to UPDATED_TBUS
        defaultLineCableShouldNotBeFound("tbus.in=" + UPDATED_TBUS);
    }

    @Test
    @Transactional
    void getAllLineCablesByTbusIsNullOrNotNull() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where tbus is not null
        defaultLineCableShouldBeFound("tbus.specified=true");

        // Get all the lineCableList where tbus is null
        defaultLineCableShouldNotBeFound("tbus.specified=false");
    }

    @Test
    @Transactional
    void getAllLineCablesByTbusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where tbus is greater than or equal to DEFAULT_TBUS
        defaultLineCableShouldBeFound("tbus.greaterThanOrEqual=" + DEFAULT_TBUS);

        // Get all the lineCableList where tbus is greater than or equal to UPDATED_TBUS
        defaultLineCableShouldNotBeFound("tbus.greaterThanOrEqual=" + UPDATED_TBUS);
    }

    @Test
    @Transactional
    void getAllLineCablesByTbusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where tbus is less than or equal to DEFAULT_TBUS
        defaultLineCableShouldBeFound("tbus.lessThanOrEqual=" + DEFAULT_TBUS);

        // Get all the lineCableList where tbus is less than or equal to SMALLER_TBUS
        defaultLineCableShouldNotBeFound("tbus.lessThanOrEqual=" + SMALLER_TBUS);
    }

    @Test
    @Transactional
    void getAllLineCablesByTbusIsLessThanSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where tbus is less than DEFAULT_TBUS
        defaultLineCableShouldNotBeFound("tbus.lessThan=" + DEFAULT_TBUS);

        // Get all the lineCableList where tbus is less than UPDATED_TBUS
        defaultLineCableShouldBeFound("tbus.lessThan=" + UPDATED_TBUS);
    }

    @Test
    @Transactional
    void getAllLineCablesByTbusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where tbus is greater than DEFAULT_TBUS
        defaultLineCableShouldNotBeFound("tbus.greaterThan=" + DEFAULT_TBUS);

        // Get all the lineCableList where tbus is greater than SMALLER_TBUS
        defaultLineCableShouldBeFound("tbus.greaterThan=" + SMALLER_TBUS);
    }

    @Test
    @Transactional
    void getAllLineCablesByLengthKmIsEqualToSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where lengthKm equals to DEFAULT_LENGTH_KM
        defaultLineCableShouldBeFound("lengthKm.equals=" + DEFAULT_LENGTH_KM);

        // Get all the lineCableList where lengthKm equals to UPDATED_LENGTH_KM
        defaultLineCableShouldNotBeFound("lengthKm.equals=" + UPDATED_LENGTH_KM);
    }

    @Test
    @Transactional
    void getAllLineCablesByLengthKmIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where lengthKm not equals to DEFAULT_LENGTH_KM
        defaultLineCableShouldNotBeFound("lengthKm.notEquals=" + DEFAULT_LENGTH_KM);

        // Get all the lineCableList where lengthKm not equals to UPDATED_LENGTH_KM
        defaultLineCableShouldBeFound("lengthKm.notEquals=" + UPDATED_LENGTH_KM);
    }

    @Test
    @Transactional
    void getAllLineCablesByLengthKmIsInShouldWork() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where lengthKm in DEFAULT_LENGTH_KM or UPDATED_LENGTH_KM
        defaultLineCableShouldBeFound("lengthKm.in=" + DEFAULT_LENGTH_KM + "," + UPDATED_LENGTH_KM);

        // Get all the lineCableList where lengthKm equals to UPDATED_LENGTH_KM
        defaultLineCableShouldNotBeFound("lengthKm.in=" + UPDATED_LENGTH_KM);
    }

    @Test
    @Transactional
    void getAllLineCablesByLengthKmIsNullOrNotNull() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where lengthKm is not null
        defaultLineCableShouldBeFound("lengthKm.specified=true");

        // Get all the lineCableList where lengthKm is null
        defaultLineCableShouldNotBeFound("lengthKm.specified=false");
    }

    @Test
    @Transactional
    void getAllLineCablesByLengthKmIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where lengthKm is greater than or equal to DEFAULT_LENGTH_KM
        defaultLineCableShouldBeFound("lengthKm.greaterThanOrEqual=" + DEFAULT_LENGTH_KM);

        // Get all the lineCableList where lengthKm is greater than or equal to UPDATED_LENGTH_KM
        defaultLineCableShouldNotBeFound("lengthKm.greaterThanOrEqual=" + UPDATED_LENGTH_KM);
    }

    @Test
    @Transactional
    void getAllLineCablesByLengthKmIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where lengthKm is less than or equal to DEFAULT_LENGTH_KM
        defaultLineCableShouldBeFound("lengthKm.lessThanOrEqual=" + DEFAULT_LENGTH_KM);

        // Get all the lineCableList where lengthKm is less than or equal to SMALLER_LENGTH_KM
        defaultLineCableShouldNotBeFound("lengthKm.lessThanOrEqual=" + SMALLER_LENGTH_KM);
    }

    @Test
    @Transactional
    void getAllLineCablesByLengthKmIsLessThanSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where lengthKm is less than DEFAULT_LENGTH_KM
        defaultLineCableShouldNotBeFound("lengthKm.lessThan=" + DEFAULT_LENGTH_KM);

        // Get all the lineCableList where lengthKm is less than UPDATED_LENGTH_KM
        defaultLineCableShouldBeFound("lengthKm.lessThan=" + UPDATED_LENGTH_KM);
    }

    @Test
    @Transactional
    void getAllLineCablesByLengthKmIsGreaterThanSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where lengthKm is greater than DEFAULT_LENGTH_KM
        defaultLineCableShouldNotBeFound("lengthKm.greaterThan=" + DEFAULT_LENGTH_KM);

        // Get all the lineCableList where lengthKm is greater than SMALLER_LENGTH_KM
        defaultLineCableShouldBeFound("lengthKm.greaterThan=" + SMALLER_LENGTH_KM);
    }

    @Test
    @Transactional
    void getAllLineCablesByTypeOfInstallationIsEqualToSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where typeOfInstallation equals to DEFAULT_TYPE_OF_INSTALLATION
        defaultLineCableShouldBeFound("typeOfInstallation.equals=" + DEFAULT_TYPE_OF_INSTALLATION);

        // Get all the lineCableList where typeOfInstallation equals to UPDATED_TYPE_OF_INSTALLATION
        defaultLineCableShouldNotBeFound("typeOfInstallation.equals=" + UPDATED_TYPE_OF_INSTALLATION);
    }

    @Test
    @Transactional
    void getAllLineCablesByTypeOfInstallationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where typeOfInstallation not equals to DEFAULT_TYPE_OF_INSTALLATION
        defaultLineCableShouldNotBeFound("typeOfInstallation.notEquals=" + DEFAULT_TYPE_OF_INSTALLATION);

        // Get all the lineCableList where typeOfInstallation not equals to UPDATED_TYPE_OF_INSTALLATION
        defaultLineCableShouldBeFound("typeOfInstallation.notEquals=" + UPDATED_TYPE_OF_INSTALLATION);
    }

    @Test
    @Transactional
    void getAllLineCablesByTypeOfInstallationIsInShouldWork() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where typeOfInstallation in DEFAULT_TYPE_OF_INSTALLATION or UPDATED_TYPE_OF_INSTALLATION
        defaultLineCableShouldBeFound("typeOfInstallation.in=" + DEFAULT_TYPE_OF_INSTALLATION + "," + UPDATED_TYPE_OF_INSTALLATION);

        // Get all the lineCableList where typeOfInstallation equals to UPDATED_TYPE_OF_INSTALLATION
        defaultLineCableShouldNotBeFound("typeOfInstallation.in=" + UPDATED_TYPE_OF_INSTALLATION);
    }

    @Test
    @Transactional
    void getAllLineCablesByTypeOfInstallationIsNullOrNotNull() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where typeOfInstallation is not null
        defaultLineCableShouldBeFound("typeOfInstallation.specified=true");

        // Get all the lineCableList where typeOfInstallation is null
        defaultLineCableShouldNotBeFound("typeOfInstallation.specified=false");
    }

    @Test
    @Transactional
    void getAllLineCablesByTypeOfInstallationContainsSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where typeOfInstallation contains DEFAULT_TYPE_OF_INSTALLATION
        defaultLineCableShouldBeFound("typeOfInstallation.contains=" + DEFAULT_TYPE_OF_INSTALLATION);

        // Get all the lineCableList where typeOfInstallation contains UPDATED_TYPE_OF_INSTALLATION
        defaultLineCableShouldNotBeFound("typeOfInstallation.contains=" + UPDATED_TYPE_OF_INSTALLATION);
    }

    @Test
    @Transactional
    void getAllLineCablesByTypeOfInstallationNotContainsSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        // Get all the lineCableList where typeOfInstallation does not contain DEFAULT_TYPE_OF_INSTALLATION
        defaultLineCableShouldNotBeFound("typeOfInstallation.doesNotContain=" + DEFAULT_TYPE_OF_INSTALLATION);

        // Get all the lineCableList where typeOfInstallation does not contain UPDATED_TYPE_OF_INSTALLATION
        defaultLineCableShouldBeFound("typeOfInstallation.doesNotContain=" + UPDATED_TYPE_OF_INSTALLATION);
    }

    @Test
    @Transactional
    void getAllLineCablesByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);
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
        lineCable.setNetwork(network);
        lineCableRepository.saveAndFlush(lineCable);
        Long networkId = network.getId();

        // Get all the lineCableList where network equals to networkId
        defaultLineCableShouldBeFound("networkId.equals=" + networkId);

        // Get all the lineCableList where network equals to (networkId + 1)
        defaultLineCableShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLineCableShouldBeFound(String filter) throws Exception {
        restLineCableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lineCable.getId().intValue())))
            .andExpect(jsonPath("$.[*].fbus").value(hasItem(DEFAULT_FBUS.intValue())))
            .andExpect(jsonPath("$.[*].tbus").value(hasItem(DEFAULT_TBUS.intValue())))
            .andExpect(jsonPath("$.[*].lengthKm").value(hasItem(DEFAULT_LENGTH_KM.doubleValue())))
            .andExpect(jsonPath("$.[*].typeOfInstallation").value(hasItem(DEFAULT_TYPE_OF_INSTALLATION)));

        // Check, that the count call also returns 1
        restLineCableMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLineCableShouldNotBeFound(String filter) throws Exception {
        restLineCableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLineCableMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLineCable() throws Exception {
        // Get the lineCable
        restLineCableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLineCable() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        int databaseSizeBeforeUpdate = lineCableRepository.findAll().size();

        // Update the lineCable
        LineCable updatedLineCable = lineCableRepository.findById(lineCable.getId()).get();
        // Disconnect from session so that the updates on updatedLineCable are not directly saved in db
        em.detach(updatedLineCable);
        updatedLineCable.fbus(UPDATED_FBUS).tbus(UPDATED_TBUS).lengthKm(UPDATED_LENGTH_KM).typeOfInstallation(UPDATED_TYPE_OF_INSTALLATION);
        LineCableDTO lineCableDTO = lineCableMapper.toDto(updatedLineCable);

        restLineCableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lineCableDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineCableDTO))
            )
            .andExpect(status().isOk());

        // Validate the LineCable in the database
        List<LineCable> lineCableList = lineCableRepository.findAll();
        assertThat(lineCableList).hasSize(databaseSizeBeforeUpdate);
        LineCable testLineCable = lineCableList.get(lineCableList.size() - 1);
        assertThat(testLineCable.getFbus()).isEqualTo(UPDATED_FBUS);
        assertThat(testLineCable.getTbus()).isEqualTo(UPDATED_TBUS);
        assertThat(testLineCable.getLengthKm()).isEqualTo(UPDATED_LENGTH_KM);
        assertThat(testLineCable.getTypeOfInstallation()).isEqualTo(UPDATED_TYPE_OF_INSTALLATION);
    }

    @Test
    @Transactional
    void putNonExistingLineCable() throws Exception {
        int databaseSizeBeforeUpdate = lineCableRepository.findAll().size();
        lineCable.setId(count.incrementAndGet());

        // Create the LineCable
        LineCableDTO lineCableDTO = lineCableMapper.toDto(lineCable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLineCableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lineCableDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineCableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LineCable in the database
        List<LineCable> lineCableList = lineCableRepository.findAll();
        assertThat(lineCableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLineCable() throws Exception {
        int databaseSizeBeforeUpdate = lineCableRepository.findAll().size();
        lineCable.setId(count.incrementAndGet());

        // Create the LineCable
        LineCableDTO lineCableDTO = lineCableMapper.toDto(lineCable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineCableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineCableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LineCable in the database
        List<LineCable> lineCableList = lineCableRepository.findAll();
        assertThat(lineCableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLineCable() throws Exception {
        int databaseSizeBeforeUpdate = lineCableRepository.findAll().size();
        lineCable.setId(count.incrementAndGet());

        // Create the LineCable
        LineCableDTO lineCableDTO = lineCableMapper.toDto(lineCable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineCableMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineCableDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LineCable in the database
        List<LineCable> lineCableList = lineCableRepository.findAll();
        assertThat(lineCableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLineCableWithPatch() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        int databaseSizeBeforeUpdate = lineCableRepository.findAll().size();

        // Update the lineCable using partial update
        LineCable partialUpdatedLineCable = new LineCable();
        partialUpdatedLineCable.setId(lineCable.getId());

        partialUpdatedLineCable.typeOfInstallation(UPDATED_TYPE_OF_INSTALLATION);

        restLineCableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLineCable.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLineCable))
            )
            .andExpect(status().isOk());

        // Validate the LineCable in the database
        List<LineCable> lineCableList = lineCableRepository.findAll();
        assertThat(lineCableList).hasSize(databaseSizeBeforeUpdate);
        LineCable testLineCable = lineCableList.get(lineCableList.size() - 1);
        assertThat(testLineCable.getFbus()).isEqualTo(DEFAULT_FBUS);
        assertThat(testLineCable.getTbus()).isEqualTo(DEFAULT_TBUS);
        assertThat(testLineCable.getLengthKm()).isEqualTo(DEFAULT_LENGTH_KM);
        assertThat(testLineCable.getTypeOfInstallation()).isEqualTo(UPDATED_TYPE_OF_INSTALLATION);
    }

    @Test
    @Transactional
    void fullUpdateLineCableWithPatch() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        int databaseSizeBeforeUpdate = lineCableRepository.findAll().size();

        // Update the lineCable using partial update
        LineCable partialUpdatedLineCable = new LineCable();
        partialUpdatedLineCable.setId(lineCable.getId());

        partialUpdatedLineCable
            .fbus(UPDATED_FBUS)
            .tbus(UPDATED_TBUS)
            .lengthKm(UPDATED_LENGTH_KM)
            .typeOfInstallation(UPDATED_TYPE_OF_INSTALLATION);

        restLineCableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLineCable.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLineCable))
            )
            .andExpect(status().isOk());

        // Validate the LineCable in the database
        List<LineCable> lineCableList = lineCableRepository.findAll();
        assertThat(lineCableList).hasSize(databaseSizeBeforeUpdate);
        LineCable testLineCable = lineCableList.get(lineCableList.size() - 1);
        assertThat(testLineCable.getFbus()).isEqualTo(UPDATED_FBUS);
        assertThat(testLineCable.getTbus()).isEqualTo(UPDATED_TBUS);
        assertThat(testLineCable.getLengthKm()).isEqualTo(UPDATED_LENGTH_KM);
        assertThat(testLineCable.getTypeOfInstallation()).isEqualTo(UPDATED_TYPE_OF_INSTALLATION);
    }

    @Test
    @Transactional
    void patchNonExistingLineCable() throws Exception {
        int databaseSizeBeforeUpdate = lineCableRepository.findAll().size();
        lineCable.setId(count.incrementAndGet());

        // Create the LineCable
        LineCableDTO lineCableDTO = lineCableMapper.toDto(lineCable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLineCableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lineCableDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lineCableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LineCable in the database
        List<LineCable> lineCableList = lineCableRepository.findAll();
        assertThat(lineCableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLineCable() throws Exception {
        int databaseSizeBeforeUpdate = lineCableRepository.findAll().size();
        lineCable.setId(count.incrementAndGet());

        // Create the LineCable
        LineCableDTO lineCableDTO = lineCableMapper.toDto(lineCable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineCableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lineCableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LineCable in the database
        List<LineCable> lineCableList = lineCableRepository.findAll();
        assertThat(lineCableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLineCable() throws Exception {
        int databaseSizeBeforeUpdate = lineCableRepository.findAll().size();
        lineCable.setId(count.incrementAndGet());

        // Create the LineCable
        LineCableDTO lineCableDTO = lineCableMapper.toDto(lineCable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineCableMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lineCableDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LineCable in the database
        List<LineCable> lineCableList = lineCableRepository.findAll();
        assertThat(lineCableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLineCable() throws Exception {
        // Initialize the database
        lineCableRepository.saveAndFlush(lineCable);

        int databaseSizeBeforeDelete = lineCableRepository.findAll().size();

        // Delete the lineCable
        restLineCableMockMvc
            .perform(delete(ENTITY_API_URL_ID, lineCable.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LineCable> lineCableList = lineCableRepository.findAll();
        assertThat(lineCableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
