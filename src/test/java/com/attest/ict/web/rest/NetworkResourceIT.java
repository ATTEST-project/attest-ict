package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.AssetTransformer;
import com.attest.ict.domain.AssetUGCable;
import com.attest.ict.domain.BaseMVA;
import com.attest.ict.domain.BillingConsumption;
import com.attest.ict.domain.BillingDer;
import com.attest.ict.domain.Branch;
import com.attest.ict.domain.BranchProfile;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.CapacitorBankData;
import com.attest.ict.domain.DsoTsoConnection;
import com.attest.ict.domain.FlexProfile;
import com.attest.ict.domain.GenProfile;
import com.attest.ict.domain.Generator;
import com.attest.ict.domain.InputFile;
import com.attest.ict.domain.LineCable;
import com.attest.ict.domain.LoadProfile;
import com.attest.ict.domain.Network;
import com.attest.ict.domain.Simulation;
import com.attest.ict.domain.Storage;
import com.attest.ict.domain.TopologyBus;
import com.attest.ict.domain.TransfProfile;
import com.attest.ict.domain.Transformer;
import com.attest.ict.domain.VoltageLevel;
import com.attest.ict.repository.NetworkRepository;
import com.attest.ict.service.criteria.NetworkCriteria;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.mapper.NetworkMapper;
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
 * Integration tests for the {@link NetworkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NetworkResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MPC_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MPC_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final Instant DEFAULT_NETWORK_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_NETWORK_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;
    private static final Integer SMALLER_VERSION = 1 - 1;

    private static final Instant DEFAULT_CREATION_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/networks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NetworkRepository networkRepository;

    @Autowired
    private NetworkMapper networkMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNetworkMockMvc;

    private Network network;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Network createEntity(EntityManager em) {
        Network network = new Network()
            .name(DEFAULT_NAME)
            .mpcName(DEFAULT_MPC_NAME)
            .country(DEFAULT_COUNTRY)
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .isDeleted(DEFAULT_IS_DELETED)
            .networkDate(DEFAULT_NETWORK_DATE)
            .version(DEFAULT_VERSION)
            .creationDateTime(DEFAULT_CREATION_DATE_TIME)
            .updateDateTime(DEFAULT_UPDATE_DATE_TIME);
        return network;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Network createUpdatedEntity(EntityManager em) {
        Network network = new Network()
            .name(UPDATED_NAME)
            .mpcName(UPDATED_MPC_NAME)
            .country(UPDATED_COUNTRY)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .isDeleted(UPDATED_IS_DELETED)
            .networkDate(UPDATED_NETWORK_DATE)
            .version(UPDATED_VERSION)
            .creationDateTime(UPDATED_CREATION_DATE_TIME)
            .updateDateTime(UPDATED_UPDATE_DATE_TIME);
        return network;
    }

    @BeforeEach
    public void initTest() {
        network = createEntity(em);
    }

    @Test
    @Transactional
    void createNetwork() throws Exception {
        int databaseSizeBeforeCreate = networkRepository.findAll().size();
        // Create the Network
        NetworkDTO networkDTO = networkMapper.toDto(network);
        restNetworkMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(networkDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Network in the database
        List<Network> networkList = networkRepository.findAll();
        assertThat(networkList).hasSize(databaseSizeBeforeCreate + 1);
        Network testNetwork = networkList.get(networkList.size() - 1);
        assertThat(testNetwork.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNetwork.getMpcName()).isEqualTo(DEFAULT_MPC_NAME);
        assertThat(testNetwork.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testNetwork.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testNetwork.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testNetwork.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testNetwork.getNetworkDate()).isEqualTo(DEFAULT_NETWORK_DATE);
        assertThat(testNetwork.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testNetwork.getCreationDateTime()).isEqualTo(DEFAULT_CREATION_DATE_TIME);
        assertThat(testNetwork.getUpdateDateTime()).isEqualTo(DEFAULT_UPDATE_DATE_TIME);
    }

    @Test
    @Transactional
    void createNetworkWithExistingId() throws Exception {
        // Create the Network with an existing ID
        network.setId(1L);
        NetworkDTO networkDTO = networkMapper.toDto(network);

        int databaseSizeBeforeCreate = networkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNetworkMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(networkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Network in the database
        List<Network> networkList = networkRepository.findAll();
        assertThat(networkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = networkRepository.findAll().size();
        // set the field null
        network.setIsDeleted(null);

        // Create the Network, which fails.
        NetworkDTO networkDTO = networkMapper.toDto(network);

        restNetworkMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(networkDTO))
            )
            .andExpect(status().isBadRequest());

        List<Network> networkList = networkRepository.findAll();
        assertThat(networkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNetworks() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList
        restNetworkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(network.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].mpcName").value(hasItem(DEFAULT_MPC_NAME)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].networkDate").value(hasItem(DEFAULT_NETWORK_DATE.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].creationDateTime").value(hasItem(DEFAULT_CREATION_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateDateTime").value(hasItem(DEFAULT_UPDATE_DATE_TIME.toString())));
    }

    @Test
    @Transactional
    void getNetwork() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get the network
        restNetworkMockMvc
            .perform(get(ENTITY_API_URL_ID, network.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(network.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.mpcName").value(DEFAULT_MPC_NAME))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.networkDate").value(DEFAULT_NETWORK_DATE.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.creationDateTime").value(DEFAULT_CREATION_DATE_TIME.toString()))
            .andExpect(jsonPath("$.updateDateTime").value(DEFAULT_UPDATE_DATE_TIME.toString()));
    }

    @Test
    @Transactional
    void getNetworksByIdFiltering() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        Long id = network.getId();

        defaultNetworkShouldBeFound("id.equals=" + id);
        defaultNetworkShouldNotBeFound("id.notEquals=" + id);

        defaultNetworkShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNetworkShouldNotBeFound("id.greaterThan=" + id);

        defaultNetworkShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNetworkShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNetworksByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where name equals to DEFAULT_NAME
        defaultNetworkShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the networkList where name equals to UPDATED_NAME
        defaultNetworkShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNetworksByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where name not equals to DEFAULT_NAME
        defaultNetworkShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the networkList where name not equals to UPDATED_NAME
        defaultNetworkShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNetworksByNameIsInShouldWork() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where name in DEFAULT_NAME or UPDATED_NAME
        defaultNetworkShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the networkList where name equals to UPDATED_NAME
        defaultNetworkShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNetworksByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where name is not null
        defaultNetworkShouldBeFound("name.specified=true");

        // Get all the networkList where name is null
        defaultNetworkShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllNetworksByNameContainsSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where name contains DEFAULT_NAME
        defaultNetworkShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the networkList where name contains UPDATED_NAME
        defaultNetworkShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNetworksByNameNotContainsSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where name does not contain DEFAULT_NAME
        defaultNetworkShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the networkList where name does not contain UPDATED_NAME
        defaultNetworkShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNetworksByMpcNameIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where mpcName equals to DEFAULT_MPC_NAME
        defaultNetworkShouldBeFound("mpcName.equals=" + DEFAULT_MPC_NAME);

        // Get all the networkList where mpcName equals to UPDATED_MPC_NAME
        defaultNetworkShouldNotBeFound("mpcName.equals=" + UPDATED_MPC_NAME);
    }

    @Test
    @Transactional
    void getAllNetworksByMpcNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where mpcName not equals to DEFAULT_MPC_NAME
        defaultNetworkShouldNotBeFound("mpcName.notEquals=" + DEFAULT_MPC_NAME);

        // Get all the networkList where mpcName not equals to UPDATED_MPC_NAME
        defaultNetworkShouldBeFound("mpcName.notEquals=" + UPDATED_MPC_NAME);
    }

    @Test
    @Transactional
    void getAllNetworksByMpcNameIsInShouldWork() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where mpcName in DEFAULT_MPC_NAME or UPDATED_MPC_NAME
        defaultNetworkShouldBeFound("mpcName.in=" + DEFAULT_MPC_NAME + "," + UPDATED_MPC_NAME);

        // Get all the networkList where mpcName equals to UPDATED_MPC_NAME
        defaultNetworkShouldNotBeFound("mpcName.in=" + UPDATED_MPC_NAME);
    }

    @Test
    @Transactional
    void getAllNetworksByMpcNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where mpcName is not null
        defaultNetworkShouldBeFound("mpcName.specified=true");

        // Get all the networkList where mpcName is null
        defaultNetworkShouldNotBeFound("mpcName.specified=false");
    }

    @Test
    @Transactional
    void getAllNetworksByMpcNameContainsSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where mpcName contains DEFAULT_MPC_NAME
        defaultNetworkShouldBeFound("mpcName.contains=" + DEFAULT_MPC_NAME);

        // Get all the networkList where mpcName contains UPDATED_MPC_NAME
        defaultNetworkShouldNotBeFound("mpcName.contains=" + UPDATED_MPC_NAME);
    }

    @Test
    @Transactional
    void getAllNetworksByMpcNameNotContainsSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where mpcName does not contain DEFAULT_MPC_NAME
        defaultNetworkShouldNotBeFound("mpcName.doesNotContain=" + DEFAULT_MPC_NAME);

        // Get all the networkList where mpcName does not contain UPDATED_MPC_NAME
        defaultNetworkShouldBeFound("mpcName.doesNotContain=" + UPDATED_MPC_NAME);
    }

    @Test
    @Transactional
    void getAllNetworksByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where country equals to DEFAULT_COUNTRY
        defaultNetworkShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the networkList where country equals to UPDATED_COUNTRY
        defaultNetworkShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllNetworksByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where country not equals to DEFAULT_COUNTRY
        defaultNetworkShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the networkList where country not equals to UPDATED_COUNTRY
        defaultNetworkShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllNetworksByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultNetworkShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the networkList where country equals to UPDATED_COUNTRY
        defaultNetworkShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllNetworksByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where country is not null
        defaultNetworkShouldBeFound("country.specified=true");

        // Get all the networkList where country is null
        defaultNetworkShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllNetworksByCountryContainsSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where country contains DEFAULT_COUNTRY
        defaultNetworkShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the networkList where country contains UPDATED_COUNTRY
        defaultNetworkShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllNetworksByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where country does not contain DEFAULT_COUNTRY
        defaultNetworkShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the networkList where country does not contain UPDATED_COUNTRY
        defaultNetworkShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllNetworksByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where type equals to DEFAULT_TYPE
        defaultNetworkShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the networkList where type equals to UPDATED_TYPE
        defaultNetworkShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllNetworksByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where type not equals to DEFAULT_TYPE
        defaultNetworkShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the networkList where type not equals to UPDATED_TYPE
        defaultNetworkShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllNetworksByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultNetworkShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the networkList where type equals to UPDATED_TYPE
        defaultNetworkShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllNetworksByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where type is not null
        defaultNetworkShouldBeFound("type.specified=true");

        // Get all the networkList where type is null
        defaultNetworkShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllNetworksByTypeContainsSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where type contains DEFAULT_TYPE
        defaultNetworkShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the networkList where type contains UPDATED_TYPE
        defaultNetworkShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllNetworksByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where type does not contain DEFAULT_TYPE
        defaultNetworkShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the networkList where type does not contain UPDATED_TYPE
        defaultNetworkShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllNetworksByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where description equals to DEFAULT_DESCRIPTION
        defaultNetworkShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the networkList where description equals to UPDATED_DESCRIPTION
        defaultNetworkShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNetworksByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where description not equals to DEFAULT_DESCRIPTION
        defaultNetworkShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the networkList where description not equals to UPDATED_DESCRIPTION
        defaultNetworkShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNetworksByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultNetworkShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the networkList where description equals to UPDATED_DESCRIPTION
        defaultNetworkShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNetworksByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where description is not null
        defaultNetworkShouldBeFound("description.specified=true");

        // Get all the networkList where description is null
        defaultNetworkShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllNetworksByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where description contains DEFAULT_DESCRIPTION
        defaultNetworkShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the networkList where description contains UPDATED_DESCRIPTION
        defaultNetworkShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNetworksByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where description does not contain DEFAULT_DESCRIPTION
        defaultNetworkShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the networkList where description does not contain UPDATED_DESCRIPTION
        defaultNetworkShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNetworksByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where isDeleted equals to DEFAULT_IS_DELETED
        defaultNetworkShouldBeFound("isDeleted.equals=" + DEFAULT_IS_DELETED);

        // Get all the networkList where isDeleted equals to UPDATED_IS_DELETED
        defaultNetworkShouldNotBeFound("isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllNetworksByIsDeletedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where isDeleted not equals to DEFAULT_IS_DELETED
        defaultNetworkShouldNotBeFound("isDeleted.notEquals=" + DEFAULT_IS_DELETED);

        // Get all the networkList where isDeleted not equals to UPDATED_IS_DELETED
        defaultNetworkShouldBeFound("isDeleted.notEquals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllNetworksByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where isDeleted in DEFAULT_IS_DELETED or UPDATED_IS_DELETED
        defaultNetworkShouldBeFound("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED);

        // Get all the networkList where isDeleted equals to UPDATED_IS_DELETED
        defaultNetworkShouldNotBeFound("isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllNetworksByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where isDeleted is not null
        defaultNetworkShouldBeFound("isDeleted.specified=true");

        // Get all the networkList where isDeleted is null
        defaultNetworkShouldNotBeFound("isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllNetworksByNetworkDateIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where networkDate equals to DEFAULT_NETWORK_DATE
        defaultNetworkShouldBeFound("networkDate.equals=" + DEFAULT_NETWORK_DATE);

        // Get all the networkList where networkDate equals to UPDATED_NETWORK_DATE
        defaultNetworkShouldNotBeFound("networkDate.equals=" + UPDATED_NETWORK_DATE);
    }

    @Test
    @Transactional
    void getAllNetworksByNetworkDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where networkDate not equals to DEFAULT_NETWORK_DATE
        defaultNetworkShouldNotBeFound("networkDate.notEquals=" + DEFAULT_NETWORK_DATE);

        // Get all the networkList where networkDate not equals to UPDATED_NETWORK_DATE
        defaultNetworkShouldBeFound("networkDate.notEquals=" + UPDATED_NETWORK_DATE);
    }

    @Test
    @Transactional
    void getAllNetworksByNetworkDateIsInShouldWork() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where networkDate in DEFAULT_NETWORK_DATE or UPDATED_NETWORK_DATE
        defaultNetworkShouldBeFound("networkDate.in=" + DEFAULT_NETWORK_DATE + "," + UPDATED_NETWORK_DATE);

        // Get all the networkList where networkDate equals to UPDATED_NETWORK_DATE
        defaultNetworkShouldNotBeFound("networkDate.in=" + UPDATED_NETWORK_DATE);
    }

    @Test
    @Transactional
    void getAllNetworksByNetworkDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where networkDate is not null
        defaultNetworkShouldBeFound("networkDate.specified=true");

        // Get all the networkList where networkDate is null
        defaultNetworkShouldNotBeFound("networkDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNetworksByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where version equals to DEFAULT_VERSION
        defaultNetworkShouldBeFound("version.equals=" + DEFAULT_VERSION);

        // Get all the networkList where version equals to UPDATED_VERSION
        defaultNetworkShouldNotBeFound("version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllNetworksByVersionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where version not equals to DEFAULT_VERSION
        defaultNetworkShouldNotBeFound("version.notEquals=" + DEFAULT_VERSION);

        // Get all the networkList where version not equals to UPDATED_VERSION
        defaultNetworkShouldBeFound("version.notEquals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllNetworksByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where version in DEFAULT_VERSION or UPDATED_VERSION
        defaultNetworkShouldBeFound("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION);

        // Get all the networkList where version equals to UPDATED_VERSION
        defaultNetworkShouldNotBeFound("version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllNetworksByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where version is not null
        defaultNetworkShouldBeFound("version.specified=true");

        // Get all the networkList where version is null
        defaultNetworkShouldNotBeFound("version.specified=false");
    }

    @Test
    @Transactional
    void getAllNetworksByVersionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where version is greater than or equal to DEFAULT_VERSION
        defaultNetworkShouldBeFound("version.greaterThanOrEqual=" + DEFAULT_VERSION);

        // Get all the networkList where version is greater than or equal to UPDATED_VERSION
        defaultNetworkShouldNotBeFound("version.greaterThanOrEqual=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllNetworksByVersionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where version is less than or equal to DEFAULT_VERSION
        defaultNetworkShouldBeFound("version.lessThanOrEqual=" + DEFAULT_VERSION);

        // Get all the networkList where version is less than or equal to SMALLER_VERSION
        defaultNetworkShouldNotBeFound("version.lessThanOrEqual=" + SMALLER_VERSION);
    }

    @Test
    @Transactional
    void getAllNetworksByVersionIsLessThanSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where version is less than DEFAULT_VERSION
        defaultNetworkShouldNotBeFound("version.lessThan=" + DEFAULT_VERSION);

        // Get all the networkList where version is less than UPDATED_VERSION
        defaultNetworkShouldBeFound("version.lessThan=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllNetworksByVersionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where version is greater than DEFAULT_VERSION
        defaultNetworkShouldNotBeFound("version.greaterThan=" + DEFAULT_VERSION);

        // Get all the networkList where version is greater than SMALLER_VERSION
        defaultNetworkShouldBeFound("version.greaterThan=" + SMALLER_VERSION);
    }

    @Test
    @Transactional
    void getAllNetworksByCreationDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where creationDateTime equals to DEFAULT_CREATION_DATE_TIME
        defaultNetworkShouldBeFound("creationDateTime.equals=" + DEFAULT_CREATION_DATE_TIME);

        // Get all the networkList where creationDateTime equals to UPDATED_CREATION_DATE_TIME
        defaultNetworkShouldNotBeFound("creationDateTime.equals=" + UPDATED_CREATION_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllNetworksByCreationDateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where creationDateTime not equals to DEFAULT_CREATION_DATE_TIME
        defaultNetworkShouldNotBeFound("creationDateTime.notEquals=" + DEFAULT_CREATION_DATE_TIME);

        // Get all the networkList where creationDateTime not equals to UPDATED_CREATION_DATE_TIME
        defaultNetworkShouldBeFound("creationDateTime.notEquals=" + UPDATED_CREATION_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllNetworksByCreationDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where creationDateTime in DEFAULT_CREATION_DATE_TIME or UPDATED_CREATION_DATE_TIME
        defaultNetworkShouldBeFound("creationDateTime.in=" + DEFAULT_CREATION_DATE_TIME + "," + UPDATED_CREATION_DATE_TIME);

        // Get all the networkList where creationDateTime equals to UPDATED_CREATION_DATE_TIME
        defaultNetworkShouldNotBeFound("creationDateTime.in=" + UPDATED_CREATION_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllNetworksByCreationDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where creationDateTime is not null
        defaultNetworkShouldBeFound("creationDateTime.specified=true");

        // Get all the networkList where creationDateTime is null
        defaultNetworkShouldNotBeFound("creationDateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllNetworksByUpdateDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where updateDateTime equals to DEFAULT_UPDATE_DATE_TIME
        defaultNetworkShouldBeFound("updateDateTime.equals=" + DEFAULT_UPDATE_DATE_TIME);

        // Get all the networkList where updateDateTime equals to UPDATED_UPDATE_DATE_TIME
        defaultNetworkShouldNotBeFound("updateDateTime.equals=" + UPDATED_UPDATE_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllNetworksByUpdateDateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where updateDateTime not equals to DEFAULT_UPDATE_DATE_TIME
        defaultNetworkShouldNotBeFound("updateDateTime.notEquals=" + DEFAULT_UPDATE_DATE_TIME);

        // Get all the networkList where updateDateTime not equals to UPDATED_UPDATE_DATE_TIME
        defaultNetworkShouldBeFound("updateDateTime.notEquals=" + UPDATED_UPDATE_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllNetworksByUpdateDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where updateDateTime in DEFAULT_UPDATE_DATE_TIME or UPDATED_UPDATE_DATE_TIME
        defaultNetworkShouldBeFound("updateDateTime.in=" + DEFAULT_UPDATE_DATE_TIME + "," + UPDATED_UPDATE_DATE_TIME);

        // Get all the networkList where updateDateTime equals to UPDATED_UPDATE_DATE_TIME
        defaultNetworkShouldNotBeFound("updateDateTime.in=" + UPDATED_UPDATE_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllNetworksByUpdateDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        // Get all the networkList where updateDateTime is not null
        defaultNetworkShouldBeFound("updateDateTime.specified=true");

        // Get all the networkList where updateDateTime is null
        defaultNetworkShouldNotBeFound("updateDateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllNetworksByBusIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
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
        network.addBus(bus);
        networkRepository.saveAndFlush(network);
        Long busId = bus.getId();

        // Get all the networkList where bus equals to busId
        defaultNetworkShouldBeFound("busId.equals=" + busId);

        // Get all the networkList where bus equals to (busId + 1)
        defaultNetworkShouldNotBeFound("busId.equals=" + (busId + 1));
    }

    @Test
    @Transactional
    void getAllNetworksByGeneratorIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
        Generator generator;
        if (TestUtil.findAll(em, Generator.class).isEmpty()) {
            generator = GeneratorResourceIT.createEntity(em);
            em.persist(generator);
            em.flush();
        } else {
            generator = TestUtil.findAll(em, Generator.class).get(0);
        }
        em.persist(generator);
        em.flush();
        network.addGenerator(generator);
        networkRepository.saveAndFlush(network);
        Long generatorId = generator.getId();

        // Get all the networkList where generator equals to generatorId
        defaultNetworkShouldBeFound("generatorId.equals=" + generatorId);

        // Get all the networkList where generator equals to (generatorId + 1)
        defaultNetworkShouldNotBeFound("generatorId.equals=" + (generatorId + 1));
    }

    @Test
    @Transactional
    void getAllNetworksByBranchIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
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
        network.addBranch(branch);
        networkRepository.saveAndFlush(network);
        Long branchId = branch.getId();

        // Get all the networkList where branch equals to branchId
        defaultNetworkShouldBeFound("branchId.equals=" + branchId);

        // Get all the networkList where branch equals to (branchId + 1)
        defaultNetworkShouldNotBeFound("branchId.equals=" + (branchId + 1));
    }

    @Test
    @Transactional
    void getAllNetworksByStorageIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
        Storage storage;
        if (TestUtil.findAll(em, Storage.class).isEmpty()) {
            storage = StorageResourceIT.createEntity(em);
            em.persist(storage);
            em.flush();
        } else {
            storage = TestUtil.findAll(em, Storage.class).get(0);
        }
        em.persist(storage);
        em.flush();
        network.addStorage(storage);
        networkRepository.saveAndFlush(network);
        Long storageId = storage.getId();

        // Get all the networkList where storage equals to storageId
        defaultNetworkShouldBeFound("storageId.equals=" + storageId);

        // Get all the networkList where storage equals to (storageId + 1)
        defaultNetworkShouldNotBeFound("storageId.equals=" + (storageId + 1));
    }

    @Test
    @Transactional
    void getAllNetworksByTransformerIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
        Transformer transformer;
        if (TestUtil.findAll(em, Transformer.class).isEmpty()) {
            transformer = TransformerResourceIT.createEntity(em);
            em.persist(transformer);
            em.flush();
        } else {
            transformer = TestUtil.findAll(em, Transformer.class).get(0);
        }
        em.persist(transformer);
        em.flush();
        network.addTransformer(transformer);
        networkRepository.saveAndFlush(network);
        Long transformerId = transformer.getId();

        // Get all the networkList where transformer equals to transformerId
        defaultNetworkShouldBeFound("transformerId.equals=" + transformerId);

        // Get all the networkList where transformer equals to (transformerId + 1)
        defaultNetworkShouldNotBeFound("transformerId.equals=" + (transformerId + 1));
    }

    @Test
    @Transactional
    void getAllNetworksByCapacitorIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
        CapacitorBankData capacitor;
        if (TestUtil.findAll(em, CapacitorBankData.class).isEmpty()) {
            capacitor = CapacitorBankDataResourceIT.createEntity(em);
            em.persist(capacitor);
            em.flush();
        } else {
            capacitor = TestUtil.findAll(em, CapacitorBankData.class).get(0);
        }
        em.persist(capacitor);
        em.flush();
        network.addCapacitor(capacitor);
        networkRepository.saveAndFlush(network);
        Long capacitorId = capacitor.getId();

        // Get all the networkList where capacitor equals to capacitorId
        defaultNetworkShouldBeFound("capacitorId.equals=" + capacitorId);

        // Get all the networkList where capacitor equals to (capacitorId + 1)
        defaultNetworkShouldNotBeFound("capacitorId.equals=" + (capacitorId + 1));
    }

    @Test
    @Transactional
    void getAllNetworksByInputFileIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
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
        network.addInputFile(inputFile);
        networkRepository.saveAndFlush(network);
        Long inputFileId = inputFile.getId();

        // Get all the networkList where inputFile equals to inputFileId
        defaultNetworkShouldBeFound("inputFileId.equals=" + inputFileId);

        // Get all the networkList where inputFile equals to (inputFileId + 1)
        defaultNetworkShouldNotBeFound("inputFileId.equals=" + (inputFileId + 1));
    }

    @Test
    @Transactional
    void getAllNetworksByAssetUgCableIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
        AssetUGCable assetUgCable;
        if (TestUtil.findAll(em, AssetUGCable.class).isEmpty()) {
            assetUgCable = AssetUGCableResourceIT.createEntity(em);
            em.persist(assetUgCable);
            em.flush();
        } else {
            assetUgCable = TestUtil.findAll(em, AssetUGCable.class).get(0);
        }
        em.persist(assetUgCable);
        em.flush();
        network.addAssetUgCable(assetUgCable);
        networkRepository.saveAndFlush(network);
        Long assetUgCableId = assetUgCable.getId();

        // Get all the networkList where assetUgCable equals to assetUgCableId
        defaultNetworkShouldBeFound("assetUgCableId.equals=" + assetUgCableId);

        // Get all the networkList where assetUgCable equals to (assetUgCableId + 1)
        defaultNetworkShouldNotBeFound("assetUgCableId.equals=" + (assetUgCableId + 1));
    }

    @Test
    @Transactional
    void getAllNetworksByAssetTransformerIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
        AssetTransformer assetTransformer;
        if (TestUtil.findAll(em, AssetTransformer.class).isEmpty()) {
            assetTransformer = AssetTransformerResourceIT.createEntity(em);
            em.persist(assetTransformer);
            em.flush();
        } else {
            assetTransformer = TestUtil.findAll(em, AssetTransformer.class).get(0);
        }
        em.persist(assetTransformer);
        em.flush();
        network.addAssetTransformer(assetTransformer);
        networkRepository.saveAndFlush(network);
        Long assetTransformerId = assetTransformer.getId();

        // Get all the networkList where assetTransformer equals to assetTransformerId
        defaultNetworkShouldBeFound("assetTransformerId.equals=" + assetTransformerId);

        // Get all the networkList where assetTransformer equals to (assetTransformerId + 1)
        defaultNetworkShouldNotBeFound("assetTransformerId.equals=" + (assetTransformerId + 1));
    }

    @Test
    @Transactional
    void getAllNetworksByBillingConsumptionIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
        BillingConsumption billingConsumption;
        if (TestUtil.findAll(em, BillingConsumption.class).isEmpty()) {
            billingConsumption = BillingConsumptionResourceIT.createEntity(em);
            em.persist(billingConsumption);
            em.flush();
        } else {
            billingConsumption = TestUtil.findAll(em, BillingConsumption.class).get(0);
        }
        em.persist(billingConsumption);
        em.flush();
        network.addBillingConsumption(billingConsumption);
        networkRepository.saveAndFlush(network);
        Long billingConsumptionId = billingConsumption.getId();

        // Get all the networkList where billingConsumption equals to billingConsumptionId
        defaultNetworkShouldBeFound("billingConsumptionId.equals=" + billingConsumptionId);

        // Get all the networkList where billingConsumption equals to (billingConsumptionId + 1)
        defaultNetworkShouldNotBeFound("billingConsumptionId.equals=" + (billingConsumptionId + 1));
    }

    @Test
    @Transactional
    void getAllNetworksByBillingDerIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
        BillingDer billingDer;
        if (TestUtil.findAll(em, BillingDer.class).isEmpty()) {
            billingDer = BillingDerResourceIT.createEntity(em);
            em.persist(billingDer);
            em.flush();
        } else {
            billingDer = TestUtil.findAll(em, BillingDer.class).get(0);
        }
        em.persist(billingDer);
        em.flush();
        network.addBillingDer(billingDer);
        networkRepository.saveAndFlush(network);
        Long billingDerId = billingDer.getId();

        // Get all the networkList where billingDer equals to billingDerId
        defaultNetworkShouldBeFound("billingDerId.equals=" + billingDerId);

        // Get all the networkList where billingDer equals to (billingDerId + 1)
        defaultNetworkShouldNotBeFound("billingDerId.equals=" + (billingDerId + 1));
    }

    @Test
    @Transactional
    void getAllNetworksByLineCableIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
        LineCable lineCable;
        if (TestUtil.findAll(em, LineCable.class).isEmpty()) {
            lineCable = LineCableResourceIT.createEntity(em);
            em.persist(lineCable);
            em.flush();
        } else {
            lineCable = TestUtil.findAll(em, LineCable.class).get(0);
        }
        em.persist(lineCable);
        em.flush();
        network.addLineCable(lineCable);
        networkRepository.saveAndFlush(network);
        Long lineCableId = lineCable.getId();

        // Get all the networkList where lineCable equals to lineCableId
        defaultNetworkShouldBeFound("lineCableId.equals=" + lineCableId);

        // Get all the networkList where lineCable equals to (lineCableId + 1)
        defaultNetworkShouldNotBeFound("lineCableId.equals=" + (lineCableId + 1));
    }

    @Test
    @Transactional
    void getAllNetworksByGenProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
        GenProfile genProfile;
        if (TestUtil.findAll(em, GenProfile.class).isEmpty()) {
            genProfile = GenProfileResourceIT.createEntity(em);
            em.persist(genProfile);
            em.flush();
        } else {
            genProfile = TestUtil.findAll(em, GenProfile.class).get(0);
        }
        em.persist(genProfile);
        em.flush();
        network.addGenProfile(genProfile);
        networkRepository.saveAndFlush(network);
        Long genProfileId = genProfile.getId();

        // Get all the networkList where genProfile equals to genProfileId
        defaultNetworkShouldBeFound("genProfileId.equals=" + genProfileId);

        // Get all the networkList where genProfile equals to (genProfileId + 1)
        defaultNetworkShouldNotBeFound("genProfileId.equals=" + (genProfileId + 1));
    }

    @Test
    @Transactional
    void getAllNetworksByLoadProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
        LoadProfile loadProfile;
        if (TestUtil.findAll(em, LoadProfile.class).isEmpty()) {
            loadProfile = LoadProfileResourceIT.createEntity(em);
            em.persist(loadProfile);
            em.flush();
        } else {
            loadProfile = TestUtil.findAll(em, LoadProfile.class).get(0);
        }
        em.persist(loadProfile);
        em.flush();
        network.addLoadProfile(loadProfile);
        networkRepository.saveAndFlush(network);
        Long loadProfileId = loadProfile.getId();

        // Get all the networkList where loadProfile equals to loadProfileId
        defaultNetworkShouldBeFound("loadProfileId.equals=" + loadProfileId);

        // Get all the networkList where loadProfile equals to (loadProfileId + 1)
        defaultNetworkShouldNotBeFound("loadProfileId.equals=" + (loadProfileId + 1));
    }

    @Test
    @Transactional
    void getAllNetworksByFlexProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
        FlexProfile flexProfile;
        if (TestUtil.findAll(em, FlexProfile.class).isEmpty()) {
            flexProfile = FlexProfileResourceIT.createEntity(em);
            em.persist(flexProfile);
            em.flush();
        } else {
            flexProfile = TestUtil.findAll(em, FlexProfile.class).get(0);
        }
        em.persist(flexProfile);
        em.flush();
        network.addFlexProfile(flexProfile);
        networkRepository.saveAndFlush(network);
        Long flexProfileId = flexProfile.getId();

        // Get all the networkList where flexProfile equals to flexProfileId
        defaultNetworkShouldBeFound("flexProfileId.equals=" + flexProfileId);

        // Get all the networkList where flexProfile equals to (flexProfileId + 1)
        defaultNetworkShouldNotBeFound("flexProfileId.equals=" + (flexProfileId + 1));
    }

    @Test
    @Transactional
    void getAllNetworksByTransfProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
        TransfProfile transfProfile;
        if (TestUtil.findAll(em, TransfProfile.class).isEmpty()) {
            transfProfile = TransfProfileResourceIT.createEntity(em);
            em.persist(transfProfile);
            em.flush();
        } else {
            transfProfile = TestUtil.findAll(em, TransfProfile.class).get(0);
        }
        em.persist(transfProfile);
        em.flush();
        network.addTransfProfile(transfProfile);
        networkRepository.saveAndFlush(network);
        Long transfProfileId = transfProfile.getId();

        // Get all the networkList where transfProfile equals to transfProfileId
        defaultNetworkShouldBeFound("transfProfileId.equals=" + transfProfileId);

        // Get all the networkList where transfProfile equals to (transfProfileId + 1)
        defaultNetworkShouldNotBeFound("transfProfileId.equals=" + (transfProfileId + 1));
    }

    @Test
    @Transactional
    void getAllNetworksByBranchProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
        BranchProfile branchProfile;
        if (TestUtil.findAll(em, BranchProfile.class).isEmpty()) {
            branchProfile = BranchProfileResourceIT.createEntity(em);
            em.persist(branchProfile);
            em.flush();
        } else {
            branchProfile = TestUtil.findAll(em, BranchProfile.class).get(0);
        }
        em.persist(branchProfile);
        em.flush();
        network.addBranchProfile(branchProfile);
        networkRepository.saveAndFlush(network);
        Long branchProfileId = branchProfile.getId();

        // Get all the networkList where branchProfile equals to branchProfileId
        defaultNetworkShouldBeFound("branchProfileId.equals=" + branchProfileId);

        // Get all the networkList where branchProfile equals to (branchProfileId + 1)
        defaultNetworkShouldNotBeFound("branchProfileId.equals=" + (branchProfileId + 1));
    }

    @Test
    @Transactional
    void getAllNetworksByTopologyBusIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
        TopologyBus topologyBus;
        if (TestUtil.findAll(em, TopologyBus.class).isEmpty()) {
            topologyBus = TopologyBusResourceIT.createEntity(em);
            em.persist(topologyBus);
            em.flush();
        } else {
            topologyBus = TestUtil.findAll(em, TopologyBus.class).get(0);
        }
        em.persist(topologyBus);
        em.flush();
        network.addTopologyBus(topologyBus);
        networkRepository.saveAndFlush(network);
        Long topologyBusId = topologyBus.getId();

        // Get all the networkList where topologyBus equals to topologyBusId
        defaultNetworkShouldBeFound("topologyBusId.equals=" + topologyBusId);

        // Get all the networkList where topologyBus equals to (topologyBusId + 1)
        defaultNetworkShouldNotBeFound("topologyBusId.equals=" + (topologyBusId + 1));
    }

    @Test
    @Transactional
    void getAllNetworksByDsoTsoConnectionIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
        DsoTsoConnection dsoTsoConnection;
        if (TestUtil.findAll(em, DsoTsoConnection.class).isEmpty()) {
            dsoTsoConnection = DsoTsoConnectionResourceIT.createEntity(em);
            em.persist(dsoTsoConnection);
            em.flush();
        } else {
            dsoTsoConnection = TestUtil.findAll(em, DsoTsoConnection.class).get(0);
        }
        em.persist(dsoTsoConnection);
        em.flush();
        network.addDsoTsoConnection(dsoTsoConnection);
        networkRepository.saveAndFlush(network);
        Long dsoTsoConnectionId = dsoTsoConnection.getId();

        // Get all the networkList where dsoTsoConnection equals to dsoTsoConnectionId
        defaultNetworkShouldBeFound("dsoTsoConnectionId.equals=" + dsoTsoConnectionId);

        // Get all the networkList where dsoTsoConnection equals to (dsoTsoConnectionId + 1)
        defaultNetworkShouldNotBeFound("dsoTsoConnectionId.equals=" + (dsoTsoConnectionId + 1));
    }

    @Test
    @Transactional
    void getAllNetworksByBaseMVAIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
        BaseMVA baseMVA;
        if (TestUtil.findAll(em, BaseMVA.class).isEmpty()) {
            baseMVA = BaseMVAResourceIT.createEntity(em);
            em.persist(baseMVA);
            em.flush();
        } else {
            baseMVA = TestUtil.findAll(em, BaseMVA.class).get(0);
        }
        em.persist(baseMVA);
        em.flush();
        network.setBaseMVA(baseMVA);
        baseMVA.setNetwork(network);
        networkRepository.saveAndFlush(network);
        Long baseMVAId = baseMVA.getId();

        // Get all the networkList where baseMVA equals to baseMVAId
        defaultNetworkShouldBeFound("baseMVAId.equals=" + baseMVAId);

        // Get all the networkList where baseMVA equals to (baseMVAId + 1)
        defaultNetworkShouldNotBeFound("baseMVAId.equals=" + (baseMVAId + 1));
    }

    @Test
    @Transactional
    void getAllNetworksByVoltageLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
        VoltageLevel voltageLevel;
        if (TestUtil.findAll(em, VoltageLevel.class).isEmpty()) {
            voltageLevel = VoltageLevelResourceIT.createEntity(em);
            em.persist(voltageLevel);
            em.flush();
        } else {
            voltageLevel = TestUtil.findAll(em, VoltageLevel.class).get(0);
        }
        em.persist(voltageLevel);
        em.flush();
        network.setVoltageLevel(voltageLevel);
        voltageLevel.setNetwork(network);
        networkRepository.saveAndFlush(network);
        Long voltageLevelId = voltageLevel.getId();

        // Get all the networkList where voltageLevel equals to voltageLevelId
        defaultNetworkShouldBeFound("voltageLevelId.equals=" + voltageLevelId);

        // Get all the networkList where voltageLevel equals to (voltageLevelId + 1)
        defaultNetworkShouldNotBeFound("voltageLevelId.equals=" + (voltageLevelId + 1));
    }

    @Test
    @Transactional
    void getAllNetworksBySimulationIsEqualToSomething() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);
        Simulation simulation;
        if (TestUtil.findAll(em, Simulation.class).isEmpty()) {
            simulation = SimulationResourceIT.createEntity(em);
            em.persist(simulation);
            em.flush();
        } else {
            simulation = TestUtil.findAll(em, Simulation.class).get(0);
        }
        em.persist(simulation);
        em.flush();
        network.addSimulation(simulation);
        networkRepository.saveAndFlush(network);
        Long simulationId = simulation.getId();

        // Get all the networkList where simulation equals to simulationId
        defaultNetworkShouldBeFound("simulationId.equals=" + simulationId);

        // Get all the networkList where simulation equals to (simulationId + 1)
        defaultNetworkShouldNotBeFound("simulationId.equals=" + (simulationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNetworkShouldBeFound(String filter) throws Exception {
        restNetworkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(network.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].mpcName").value(hasItem(DEFAULT_MPC_NAME)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].networkDate").value(hasItem(DEFAULT_NETWORK_DATE.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].creationDateTime").value(hasItem(DEFAULT_CREATION_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateDateTime").value(hasItem(DEFAULT_UPDATE_DATE_TIME.toString())));

        // Check, that the count call also returns 1
        restNetworkMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNetworkShouldNotBeFound(String filter) throws Exception {
        restNetworkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNetworkMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNetwork() throws Exception {
        // Get the network
        restNetworkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNetwork() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        int databaseSizeBeforeUpdate = networkRepository.findAll().size();

        // Update the network
        Network updatedNetwork = networkRepository.findById(network.getId()).get();
        // Disconnect from session so that the updates on updatedNetwork are not directly saved in db
        em.detach(updatedNetwork);
        updatedNetwork
            .name(UPDATED_NAME)
            .mpcName(UPDATED_MPC_NAME)
            .country(UPDATED_COUNTRY)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .isDeleted(UPDATED_IS_DELETED)
            .networkDate(UPDATED_NETWORK_DATE)
            .version(UPDATED_VERSION)
            .creationDateTime(UPDATED_CREATION_DATE_TIME)
            .updateDateTime(UPDATED_UPDATE_DATE_TIME);
        NetworkDTO networkDTO = networkMapper.toDto(updatedNetwork);

        restNetworkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, networkDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(networkDTO))
            )
            .andExpect(status().isOk());

        // Validate the Network in the database
        List<Network> networkList = networkRepository.findAll();
        assertThat(networkList).hasSize(databaseSizeBeforeUpdate);
        Network testNetwork = networkList.get(networkList.size() - 1);
        assertThat(testNetwork.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNetwork.getMpcName()).isEqualTo(UPDATED_MPC_NAME);
        assertThat(testNetwork.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testNetwork.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testNetwork.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testNetwork.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testNetwork.getNetworkDate()).isEqualTo(UPDATED_NETWORK_DATE);
        assertThat(testNetwork.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testNetwork.getCreationDateTime()).isEqualTo(UPDATED_CREATION_DATE_TIME);
        assertThat(testNetwork.getUpdateDateTime()).isEqualTo(UPDATED_UPDATE_DATE_TIME);
    }

    @Test
    @Transactional
    void putNonExistingNetwork() throws Exception {
        int databaseSizeBeforeUpdate = networkRepository.findAll().size();
        network.setId(count.incrementAndGet());

        // Create the Network
        NetworkDTO networkDTO = networkMapper.toDto(network);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNetworkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, networkDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(networkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Network in the database
        List<Network> networkList = networkRepository.findAll();
        assertThat(networkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNetwork() throws Exception {
        int databaseSizeBeforeUpdate = networkRepository.findAll().size();
        network.setId(count.incrementAndGet());

        // Create the Network
        NetworkDTO networkDTO = networkMapper.toDto(network);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNetworkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(networkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Network in the database
        List<Network> networkList = networkRepository.findAll();
        assertThat(networkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNetwork() throws Exception {
        int databaseSizeBeforeUpdate = networkRepository.findAll().size();
        network.setId(count.incrementAndGet());

        // Create the Network
        NetworkDTO networkDTO = networkMapper.toDto(network);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNetworkMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(networkDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Network in the database
        List<Network> networkList = networkRepository.findAll();
        assertThat(networkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNetworkWithPatch() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        int databaseSizeBeforeUpdate = networkRepository.findAll().size();

        // Update the network using partial update
        Network partialUpdatedNetwork = new Network();
        partialUpdatedNetwork.setId(network.getId());

        partialUpdatedNetwork
            .mpcName(UPDATED_MPC_NAME)
            .type(UPDATED_TYPE)
            .isDeleted(UPDATED_IS_DELETED)
            .updateDateTime(UPDATED_UPDATE_DATE_TIME);

        restNetworkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNetwork.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNetwork))
            )
            .andExpect(status().isOk());

        // Validate the Network in the database
        List<Network> networkList = networkRepository.findAll();
        assertThat(networkList).hasSize(databaseSizeBeforeUpdate);
        Network testNetwork = networkList.get(networkList.size() - 1);
        assertThat(testNetwork.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNetwork.getMpcName()).isEqualTo(UPDATED_MPC_NAME);
        assertThat(testNetwork.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testNetwork.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testNetwork.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testNetwork.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testNetwork.getNetworkDate()).isEqualTo(DEFAULT_NETWORK_DATE);
        assertThat(testNetwork.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testNetwork.getCreationDateTime()).isEqualTo(DEFAULT_CREATION_DATE_TIME);
        assertThat(testNetwork.getUpdateDateTime()).isEqualTo(UPDATED_UPDATE_DATE_TIME);
    }

    @Test
    @Transactional
    void fullUpdateNetworkWithPatch() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        int databaseSizeBeforeUpdate = networkRepository.findAll().size();

        // Update the network using partial update
        Network partialUpdatedNetwork = new Network();
        partialUpdatedNetwork.setId(network.getId());

        partialUpdatedNetwork
            .name(UPDATED_NAME)
            .mpcName(UPDATED_MPC_NAME)
            .country(UPDATED_COUNTRY)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .isDeleted(UPDATED_IS_DELETED)
            .networkDate(UPDATED_NETWORK_DATE)
            .version(UPDATED_VERSION)
            .creationDateTime(UPDATED_CREATION_DATE_TIME)
            .updateDateTime(UPDATED_UPDATE_DATE_TIME);

        restNetworkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNetwork.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNetwork))
            )
            .andExpect(status().isOk());

        // Validate the Network in the database
        List<Network> networkList = networkRepository.findAll();
        assertThat(networkList).hasSize(databaseSizeBeforeUpdate);
        Network testNetwork = networkList.get(networkList.size() - 1);
        assertThat(testNetwork.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNetwork.getMpcName()).isEqualTo(UPDATED_MPC_NAME);
        assertThat(testNetwork.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testNetwork.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testNetwork.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testNetwork.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testNetwork.getNetworkDate()).isEqualTo(UPDATED_NETWORK_DATE);
        assertThat(testNetwork.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testNetwork.getCreationDateTime()).isEqualTo(UPDATED_CREATION_DATE_TIME);
        assertThat(testNetwork.getUpdateDateTime()).isEqualTo(UPDATED_UPDATE_DATE_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingNetwork() throws Exception {
        int databaseSizeBeforeUpdate = networkRepository.findAll().size();
        network.setId(count.incrementAndGet());

        // Create the Network
        NetworkDTO networkDTO = networkMapper.toDto(network);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNetworkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, networkDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(networkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Network in the database
        List<Network> networkList = networkRepository.findAll();
        assertThat(networkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNetwork() throws Exception {
        int databaseSizeBeforeUpdate = networkRepository.findAll().size();
        network.setId(count.incrementAndGet());

        // Create the Network
        NetworkDTO networkDTO = networkMapper.toDto(network);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNetworkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(networkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Network in the database
        List<Network> networkList = networkRepository.findAll();
        assertThat(networkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNetwork() throws Exception {
        int databaseSizeBeforeUpdate = networkRepository.findAll().size();
        network.setId(count.incrementAndGet());

        // Create the Network
        NetworkDTO networkDTO = networkMapper.toDto(network);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNetworkMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(networkDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Network in the database
        List<Network> networkList = networkRepository.findAll();
        assertThat(networkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNetwork() throws Exception {
        // Initialize the database
        networkRepository.saveAndFlush(network);

        int databaseSizeBeforeDelete = networkRepository.findAll().size();

        // Delete the network
        restNetworkMockMvc
            .perform(delete(ENTITY_API_URL_ID, network.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Network> networkList = networkRepository.findAll();
        assertThat(networkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
