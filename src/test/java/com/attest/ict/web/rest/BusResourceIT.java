package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.BusCoordinate;
import com.attest.ict.domain.BusExtension;
import com.attest.ict.domain.BusName;
import com.attest.ict.domain.LoadElVal;
import com.attest.ict.domain.Network;
import com.attest.ict.repository.BusRepository;
import com.attest.ict.service.criteria.BusCriteria;
import com.attest.ict.service.dto.BusDTO;
import com.attest.ict.service.mapper.BusMapper;
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
 * Integration tests for the {@link BusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BusResourceIT {

    private static final Long DEFAULT_BUS_NUM = 1L;
    private static final Long UPDATED_BUS_NUM = 2L;
    private static final Long SMALLER_BUS_NUM = 1L - 1L;

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;
    private static final Integer SMALLER_TYPE = 1 - 1;

    private static final Double DEFAULT_ACTIVE_POWER = 1D;
    private static final Double UPDATED_ACTIVE_POWER = 2D;
    private static final Double SMALLER_ACTIVE_POWER = 1D - 1D;

    private static final Double DEFAULT_REACTIVE_POWER = 1D;
    private static final Double UPDATED_REACTIVE_POWER = 2D;
    private static final Double SMALLER_REACTIVE_POWER = 1D - 1D;

    private static final Double DEFAULT_CONDUCTANCE = 1D;
    private static final Double UPDATED_CONDUCTANCE = 2D;
    private static final Double SMALLER_CONDUCTANCE = 1D - 1D;

    private static final Double DEFAULT_SUSCEPTANCE = 1D;
    private static final Double UPDATED_SUSCEPTANCE = 2D;
    private static final Double SMALLER_SUSCEPTANCE = 1D - 1D;

    private static final Long DEFAULT_AREA = 1L;
    private static final Long UPDATED_AREA = 2L;
    private static final Long SMALLER_AREA = 1L - 1L;

    private static final Double DEFAULT_VM = 1D;
    private static final Double UPDATED_VM = 2D;
    private static final Double SMALLER_VM = 1D - 1D;

    private static final Double DEFAULT_VA = 1D;
    private static final Double UPDATED_VA = 2D;
    private static final Double SMALLER_VA = 1D - 1D;

    private static final Double DEFAULT_BASE_KV = 1D;
    private static final Double UPDATED_BASE_KV = 2D;
    private static final Double SMALLER_BASE_KV = 1D - 1D;

    private static final Long DEFAULT_ZONE = 1L;
    private static final Long UPDATED_ZONE = 2L;
    private static final Long SMALLER_ZONE = 1L - 1L;

    private static final Double DEFAULT_VMAX = 1D;
    private static final Double UPDATED_VMAX = 2D;
    private static final Double SMALLER_VMAX = 1D - 1D;

    private static final Double DEFAULT_VMIN = 1D;
    private static final Double UPDATED_VMIN = 2D;
    private static final Double SMALLER_VMIN = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/buses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private BusMapper busMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusMockMvc;

    private Bus bus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bus createEntity(EntityManager em) {
        Bus bus = new Bus()
            .busNum(DEFAULT_BUS_NUM)
            .type(DEFAULT_TYPE)
            .activePower(DEFAULT_ACTIVE_POWER)
            .reactivePower(DEFAULT_REACTIVE_POWER)
            .conductance(DEFAULT_CONDUCTANCE)
            .susceptance(DEFAULT_SUSCEPTANCE)
            .area(DEFAULT_AREA)
            .vm(DEFAULT_VM)
            .va(DEFAULT_VA)
            .baseKv(DEFAULT_BASE_KV)
            .zone(DEFAULT_ZONE)
            .vmax(DEFAULT_VMAX)
            .vmin(DEFAULT_VMIN);
        return bus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bus createUpdatedEntity(EntityManager em) {
        Bus bus = new Bus()
            .busNum(UPDATED_BUS_NUM)
            .type(UPDATED_TYPE)
            .activePower(UPDATED_ACTIVE_POWER)
            .reactivePower(UPDATED_REACTIVE_POWER)
            .conductance(UPDATED_CONDUCTANCE)
            .susceptance(UPDATED_SUSCEPTANCE)
            .area(UPDATED_AREA)
            .vm(UPDATED_VM)
            .va(UPDATED_VA)
            .baseKv(UPDATED_BASE_KV)
            .zone(UPDATED_ZONE)
            .vmax(UPDATED_VMAX)
            .vmin(UPDATED_VMIN);
        return bus;
    }

    @BeforeEach
    public void initTest() {
        bus = createEntity(em);
    }

    @Test
    @Transactional
    void createBus() throws Exception {
        int databaseSizeBeforeCreate = busRepository.findAll().size();
        // Create the Bus
        BusDTO busDTO = busMapper.toDto(bus);
        restBusMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(busDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Bus in the database
        List<Bus> busList = busRepository.findAll();
        assertThat(busList).hasSize(databaseSizeBeforeCreate + 1);
        Bus testBus = busList.get(busList.size() - 1);
        assertThat(testBus.getBusNum()).isEqualTo(DEFAULT_BUS_NUM);
        assertThat(testBus.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testBus.getActivePower()).isEqualTo(DEFAULT_ACTIVE_POWER);
        assertThat(testBus.getReactivePower()).isEqualTo(DEFAULT_REACTIVE_POWER);
        assertThat(testBus.getConductance()).isEqualTo(DEFAULT_CONDUCTANCE);
        assertThat(testBus.getSusceptance()).isEqualTo(DEFAULT_SUSCEPTANCE);
        assertThat(testBus.getArea()).isEqualTo(DEFAULT_AREA);
        assertThat(testBus.getVm()).isEqualTo(DEFAULT_VM);
        assertThat(testBus.getVa()).isEqualTo(DEFAULT_VA);
        assertThat(testBus.getBaseKv()).isEqualTo(DEFAULT_BASE_KV);
        assertThat(testBus.getZone()).isEqualTo(DEFAULT_ZONE);
        assertThat(testBus.getVmax()).isEqualTo(DEFAULT_VMAX);
        assertThat(testBus.getVmin()).isEqualTo(DEFAULT_VMIN);
    }

    @Test
    @Transactional
    void createBusWithExistingId() throws Exception {
        // Create the Bus with an existing ID
        bus.setId(1L);
        BusDTO busDTO = busMapper.toDto(bus);

        int databaseSizeBeforeCreate = busRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(busDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bus in the database
        List<Bus> busList = busRepository.findAll();
        assertThat(busList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBuses() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList
        restBusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bus.getId().intValue())))
            .andExpect(jsonPath("$.[*].busNum").value(hasItem(DEFAULT_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].activePower").value(hasItem(DEFAULT_ACTIVE_POWER.doubleValue())))
            .andExpect(jsonPath("$.[*].reactivePower").value(hasItem(DEFAULT_REACTIVE_POWER.doubleValue())))
            .andExpect(jsonPath("$.[*].conductance").value(hasItem(DEFAULT_CONDUCTANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].susceptance").value(hasItem(DEFAULT_SUSCEPTANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA.intValue())))
            .andExpect(jsonPath("$.[*].vm").value(hasItem(DEFAULT_VM.doubleValue())))
            .andExpect(jsonPath("$.[*].va").value(hasItem(DEFAULT_VA.doubleValue())))
            .andExpect(jsonPath("$.[*].baseKv").value(hasItem(DEFAULT_BASE_KV.doubleValue())))
            .andExpect(jsonPath("$.[*].zone").value(hasItem(DEFAULT_ZONE.intValue())))
            .andExpect(jsonPath("$.[*].vmax").value(hasItem(DEFAULT_VMAX.doubleValue())))
            .andExpect(jsonPath("$.[*].vmin").value(hasItem(DEFAULT_VMIN.doubleValue())));
    }

    @Test
    @Transactional
    void getBus() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get the bus
        restBusMockMvc
            .perform(get(ENTITY_API_URL_ID, bus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bus.getId().intValue()))
            .andExpect(jsonPath("$.busNum").value(DEFAULT_BUS_NUM.intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.activePower").value(DEFAULT_ACTIVE_POWER.doubleValue()))
            .andExpect(jsonPath("$.reactivePower").value(DEFAULT_REACTIVE_POWER.doubleValue()))
            .andExpect(jsonPath("$.conductance").value(DEFAULT_CONDUCTANCE.doubleValue()))
            .andExpect(jsonPath("$.susceptance").value(DEFAULT_SUSCEPTANCE.doubleValue()))
            .andExpect(jsonPath("$.area").value(DEFAULT_AREA.intValue()))
            .andExpect(jsonPath("$.vm").value(DEFAULT_VM.doubleValue()))
            .andExpect(jsonPath("$.va").value(DEFAULT_VA.doubleValue()))
            .andExpect(jsonPath("$.baseKv").value(DEFAULT_BASE_KV.doubleValue()))
            .andExpect(jsonPath("$.zone").value(DEFAULT_ZONE.intValue()))
            .andExpect(jsonPath("$.vmax").value(DEFAULT_VMAX.doubleValue()))
            .andExpect(jsonPath("$.vmin").value(DEFAULT_VMIN.doubleValue()));
    }

    @Test
    @Transactional
    void getBusesByIdFiltering() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        Long id = bus.getId();

        defaultBusShouldBeFound("id.equals=" + id);
        defaultBusShouldNotBeFound("id.notEquals=" + id);

        defaultBusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBusShouldNotBeFound("id.greaterThan=" + id);

        defaultBusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBusShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBusesByBusNumIsEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where busNum equals to DEFAULT_BUS_NUM
        defaultBusShouldBeFound("busNum.equals=" + DEFAULT_BUS_NUM);

        // Get all the busList where busNum equals to UPDATED_BUS_NUM
        defaultBusShouldNotBeFound("busNum.equals=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllBusesByBusNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where busNum not equals to DEFAULT_BUS_NUM
        defaultBusShouldNotBeFound("busNum.notEquals=" + DEFAULT_BUS_NUM);

        // Get all the busList where busNum not equals to UPDATED_BUS_NUM
        defaultBusShouldBeFound("busNum.notEquals=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllBusesByBusNumIsInShouldWork() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where busNum in DEFAULT_BUS_NUM or UPDATED_BUS_NUM
        defaultBusShouldBeFound("busNum.in=" + DEFAULT_BUS_NUM + "," + UPDATED_BUS_NUM);

        // Get all the busList where busNum equals to UPDATED_BUS_NUM
        defaultBusShouldNotBeFound("busNum.in=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllBusesByBusNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where busNum is not null
        defaultBusShouldBeFound("busNum.specified=true");

        // Get all the busList where busNum is null
        defaultBusShouldNotBeFound("busNum.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByBusNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where busNum is greater than or equal to DEFAULT_BUS_NUM
        defaultBusShouldBeFound("busNum.greaterThanOrEqual=" + DEFAULT_BUS_NUM);

        // Get all the busList where busNum is greater than or equal to UPDATED_BUS_NUM
        defaultBusShouldNotBeFound("busNum.greaterThanOrEqual=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllBusesByBusNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where busNum is less than or equal to DEFAULT_BUS_NUM
        defaultBusShouldBeFound("busNum.lessThanOrEqual=" + DEFAULT_BUS_NUM);

        // Get all the busList where busNum is less than or equal to SMALLER_BUS_NUM
        defaultBusShouldNotBeFound("busNum.lessThanOrEqual=" + SMALLER_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllBusesByBusNumIsLessThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where busNum is less than DEFAULT_BUS_NUM
        defaultBusShouldNotBeFound("busNum.lessThan=" + DEFAULT_BUS_NUM);

        // Get all the busList where busNum is less than UPDATED_BUS_NUM
        defaultBusShouldBeFound("busNum.lessThan=" + UPDATED_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllBusesByBusNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where busNum is greater than DEFAULT_BUS_NUM
        defaultBusShouldNotBeFound("busNum.greaterThan=" + DEFAULT_BUS_NUM);

        // Get all the busList where busNum is greater than SMALLER_BUS_NUM
        defaultBusShouldBeFound("busNum.greaterThan=" + SMALLER_BUS_NUM);
    }

    @Test
    @Transactional
    void getAllBusesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where type equals to DEFAULT_TYPE
        defaultBusShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the busList where type equals to UPDATED_TYPE
        defaultBusShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBusesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where type not equals to DEFAULT_TYPE
        defaultBusShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the busList where type not equals to UPDATED_TYPE
        defaultBusShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBusesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultBusShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the busList where type equals to UPDATED_TYPE
        defaultBusShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBusesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where type is not null
        defaultBusShouldBeFound("type.specified=true");

        // Get all the busList where type is null
        defaultBusShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where type is greater than or equal to DEFAULT_TYPE
        defaultBusShouldBeFound("type.greaterThanOrEqual=" + DEFAULT_TYPE);

        // Get all the busList where type is greater than or equal to UPDATED_TYPE
        defaultBusShouldNotBeFound("type.greaterThanOrEqual=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBusesByTypeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where type is less than or equal to DEFAULT_TYPE
        defaultBusShouldBeFound("type.lessThanOrEqual=" + DEFAULT_TYPE);

        // Get all the busList where type is less than or equal to SMALLER_TYPE
        defaultBusShouldNotBeFound("type.lessThanOrEqual=" + SMALLER_TYPE);
    }

    @Test
    @Transactional
    void getAllBusesByTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where type is less than DEFAULT_TYPE
        defaultBusShouldNotBeFound("type.lessThan=" + DEFAULT_TYPE);

        // Get all the busList where type is less than UPDATED_TYPE
        defaultBusShouldBeFound("type.lessThan=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBusesByTypeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where type is greater than DEFAULT_TYPE
        defaultBusShouldNotBeFound("type.greaterThan=" + DEFAULT_TYPE);

        // Get all the busList where type is greater than SMALLER_TYPE
        defaultBusShouldBeFound("type.greaterThan=" + SMALLER_TYPE);
    }

    @Test
    @Transactional
    void getAllBusesByActivePowerIsEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where activePower equals to DEFAULT_ACTIVE_POWER
        defaultBusShouldBeFound("activePower.equals=" + DEFAULT_ACTIVE_POWER);

        // Get all the busList where activePower equals to UPDATED_ACTIVE_POWER
        defaultBusShouldNotBeFound("activePower.equals=" + UPDATED_ACTIVE_POWER);
    }

    @Test
    @Transactional
    void getAllBusesByActivePowerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where activePower not equals to DEFAULT_ACTIVE_POWER
        defaultBusShouldNotBeFound("activePower.notEquals=" + DEFAULT_ACTIVE_POWER);

        // Get all the busList where activePower not equals to UPDATED_ACTIVE_POWER
        defaultBusShouldBeFound("activePower.notEquals=" + UPDATED_ACTIVE_POWER);
    }

    @Test
    @Transactional
    void getAllBusesByActivePowerIsInShouldWork() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where activePower in DEFAULT_ACTIVE_POWER or UPDATED_ACTIVE_POWER
        defaultBusShouldBeFound("activePower.in=" + DEFAULT_ACTIVE_POWER + "," + UPDATED_ACTIVE_POWER);

        // Get all the busList where activePower equals to UPDATED_ACTIVE_POWER
        defaultBusShouldNotBeFound("activePower.in=" + UPDATED_ACTIVE_POWER);
    }

    @Test
    @Transactional
    void getAllBusesByActivePowerIsNullOrNotNull() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where activePower is not null
        defaultBusShouldBeFound("activePower.specified=true");

        // Get all the busList where activePower is null
        defaultBusShouldNotBeFound("activePower.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByActivePowerIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where activePower is greater than or equal to DEFAULT_ACTIVE_POWER
        defaultBusShouldBeFound("activePower.greaterThanOrEqual=" + DEFAULT_ACTIVE_POWER);

        // Get all the busList where activePower is greater than or equal to UPDATED_ACTIVE_POWER
        defaultBusShouldNotBeFound("activePower.greaterThanOrEqual=" + UPDATED_ACTIVE_POWER);
    }

    @Test
    @Transactional
    void getAllBusesByActivePowerIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where activePower is less than or equal to DEFAULT_ACTIVE_POWER
        defaultBusShouldBeFound("activePower.lessThanOrEqual=" + DEFAULT_ACTIVE_POWER);

        // Get all the busList where activePower is less than or equal to SMALLER_ACTIVE_POWER
        defaultBusShouldNotBeFound("activePower.lessThanOrEqual=" + SMALLER_ACTIVE_POWER);
    }

    @Test
    @Transactional
    void getAllBusesByActivePowerIsLessThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where activePower is less than DEFAULT_ACTIVE_POWER
        defaultBusShouldNotBeFound("activePower.lessThan=" + DEFAULT_ACTIVE_POWER);

        // Get all the busList where activePower is less than UPDATED_ACTIVE_POWER
        defaultBusShouldBeFound("activePower.lessThan=" + UPDATED_ACTIVE_POWER);
    }

    @Test
    @Transactional
    void getAllBusesByActivePowerIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where activePower is greater than DEFAULT_ACTIVE_POWER
        defaultBusShouldNotBeFound("activePower.greaterThan=" + DEFAULT_ACTIVE_POWER);

        // Get all the busList where activePower is greater than SMALLER_ACTIVE_POWER
        defaultBusShouldBeFound("activePower.greaterThan=" + SMALLER_ACTIVE_POWER);
    }

    @Test
    @Transactional
    void getAllBusesByReactivePowerIsEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where reactivePower equals to DEFAULT_REACTIVE_POWER
        defaultBusShouldBeFound("reactivePower.equals=" + DEFAULT_REACTIVE_POWER);

        // Get all the busList where reactivePower equals to UPDATED_REACTIVE_POWER
        defaultBusShouldNotBeFound("reactivePower.equals=" + UPDATED_REACTIVE_POWER);
    }

    @Test
    @Transactional
    void getAllBusesByReactivePowerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where reactivePower not equals to DEFAULT_REACTIVE_POWER
        defaultBusShouldNotBeFound("reactivePower.notEquals=" + DEFAULT_REACTIVE_POWER);

        // Get all the busList where reactivePower not equals to UPDATED_REACTIVE_POWER
        defaultBusShouldBeFound("reactivePower.notEquals=" + UPDATED_REACTIVE_POWER);
    }

    @Test
    @Transactional
    void getAllBusesByReactivePowerIsInShouldWork() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where reactivePower in DEFAULT_REACTIVE_POWER or UPDATED_REACTIVE_POWER
        defaultBusShouldBeFound("reactivePower.in=" + DEFAULT_REACTIVE_POWER + "," + UPDATED_REACTIVE_POWER);

        // Get all the busList where reactivePower equals to UPDATED_REACTIVE_POWER
        defaultBusShouldNotBeFound("reactivePower.in=" + UPDATED_REACTIVE_POWER);
    }

    @Test
    @Transactional
    void getAllBusesByReactivePowerIsNullOrNotNull() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where reactivePower is not null
        defaultBusShouldBeFound("reactivePower.specified=true");

        // Get all the busList where reactivePower is null
        defaultBusShouldNotBeFound("reactivePower.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByReactivePowerIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where reactivePower is greater than or equal to DEFAULT_REACTIVE_POWER
        defaultBusShouldBeFound("reactivePower.greaterThanOrEqual=" + DEFAULT_REACTIVE_POWER);

        // Get all the busList where reactivePower is greater than or equal to UPDATED_REACTIVE_POWER
        defaultBusShouldNotBeFound("reactivePower.greaterThanOrEqual=" + UPDATED_REACTIVE_POWER);
    }

    @Test
    @Transactional
    void getAllBusesByReactivePowerIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where reactivePower is less than or equal to DEFAULT_REACTIVE_POWER
        defaultBusShouldBeFound("reactivePower.lessThanOrEqual=" + DEFAULT_REACTIVE_POWER);

        // Get all the busList where reactivePower is less than or equal to SMALLER_REACTIVE_POWER
        defaultBusShouldNotBeFound("reactivePower.lessThanOrEqual=" + SMALLER_REACTIVE_POWER);
    }

    @Test
    @Transactional
    void getAllBusesByReactivePowerIsLessThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where reactivePower is less than DEFAULT_REACTIVE_POWER
        defaultBusShouldNotBeFound("reactivePower.lessThan=" + DEFAULT_REACTIVE_POWER);

        // Get all the busList where reactivePower is less than UPDATED_REACTIVE_POWER
        defaultBusShouldBeFound("reactivePower.lessThan=" + UPDATED_REACTIVE_POWER);
    }

    @Test
    @Transactional
    void getAllBusesByReactivePowerIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where reactivePower is greater than DEFAULT_REACTIVE_POWER
        defaultBusShouldNotBeFound("reactivePower.greaterThan=" + DEFAULT_REACTIVE_POWER);

        // Get all the busList where reactivePower is greater than SMALLER_REACTIVE_POWER
        defaultBusShouldBeFound("reactivePower.greaterThan=" + SMALLER_REACTIVE_POWER);
    }

    @Test
    @Transactional
    void getAllBusesByConductanceIsEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where conductance equals to DEFAULT_CONDUCTANCE
        defaultBusShouldBeFound("conductance.equals=" + DEFAULT_CONDUCTANCE);

        // Get all the busList where conductance equals to UPDATED_CONDUCTANCE
        defaultBusShouldNotBeFound("conductance.equals=" + UPDATED_CONDUCTANCE);
    }

    @Test
    @Transactional
    void getAllBusesByConductanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where conductance not equals to DEFAULT_CONDUCTANCE
        defaultBusShouldNotBeFound("conductance.notEquals=" + DEFAULT_CONDUCTANCE);

        // Get all the busList where conductance not equals to UPDATED_CONDUCTANCE
        defaultBusShouldBeFound("conductance.notEquals=" + UPDATED_CONDUCTANCE);
    }

    @Test
    @Transactional
    void getAllBusesByConductanceIsInShouldWork() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where conductance in DEFAULT_CONDUCTANCE or UPDATED_CONDUCTANCE
        defaultBusShouldBeFound("conductance.in=" + DEFAULT_CONDUCTANCE + "," + UPDATED_CONDUCTANCE);

        // Get all the busList where conductance equals to UPDATED_CONDUCTANCE
        defaultBusShouldNotBeFound("conductance.in=" + UPDATED_CONDUCTANCE);
    }

    @Test
    @Transactional
    void getAllBusesByConductanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where conductance is not null
        defaultBusShouldBeFound("conductance.specified=true");

        // Get all the busList where conductance is null
        defaultBusShouldNotBeFound("conductance.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByConductanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where conductance is greater than or equal to DEFAULT_CONDUCTANCE
        defaultBusShouldBeFound("conductance.greaterThanOrEqual=" + DEFAULT_CONDUCTANCE);

        // Get all the busList where conductance is greater than or equal to UPDATED_CONDUCTANCE
        defaultBusShouldNotBeFound("conductance.greaterThanOrEqual=" + UPDATED_CONDUCTANCE);
    }

    @Test
    @Transactional
    void getAllBusesByConductanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where conductance is less than or equal to DEFAULT_CONDUCTANCE
        defaultBusShouldBeFound("conductance.lessThanOrEqual=" + DEFAULT_CONDUCTANCE);

        // Get all the busList where conductance is less than or equal to SMALLER_CONDUCTANCE
        defaultBusShouldNotBeFound("conductance.lessThanOrEqual=" + SMALLER_CONDUCTANCE);
    }

    @Test
    @Transactional
    void getAllBusesByConductanceIsLessThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where conductance is less than DEFAULT_CONDUCTANCE
        defaultBusShouldNotBeFound("conductance.lessThan=" + DEFAULT_CONDUCTANCE);

        // Get all the busList where conductance is less than UPDATED_CONDUCTANCE
        defaultBusShouldBeFound("conductance.lessThan=" + UPDATED_CONDUCTANCE);
    }

    @Test
    @Transactional
    void getAllBusesByConductanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where conductance is greater than DEFAULT_CONDUCTANCE
        defaultBusShouldNotBeFound("conductance.greaterThan=" + DEFAULT_CONDUCTANCE);

        // Get all the busList where conductance is greater than SMALLER_CONDUCTANCE
        defaultBusShouldBeFound("conductance.greaterThan=" + SMALLER_CONDUCTANCE);
    }

    @Test
    @Transactional
    void getAllBusesBySusceptanceIsEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where susceptance equals to DEFAULT_SUSCEPTANCE
        defaultBusShouldBeFound("susceptance.equals=" + DEFAULT_SUSCEPTANCE);

        // Get all the busList where susceptance equals to UPDATED_SUSCEPTANCE
        defaultBusShouldNotBeFound("susceptance.equals=" + UPDATED_SUSCEPTANCE);
    }

    @Test
    @Transactional
    void getAllBusesBySusceptanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where susceptance not equals to DEFAULT_SUSCEPTANCE
        defaultBusShouldNotBeFound("susceptance.notEquals=" + DEFAULT_SUSCEPTANCE);

        // Get all the busList where susceptance not equals to UPDATED_SUSCEPTANCE
        defaultBusShouldBeFound("susceptance.notEquals=" + UPDATED_SUSCEPTANCE);
    }

    @Test
    @Transactional
    void getAllBusesBySusceptanceIsInShouldWork() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where susceptance in DEFAULT_SUSCEPTANCE or UPDATED_SUSCEPTANCE
        defaultBusShouldBeFound("susceptance.in=" + DEFAULT_SUSCEPTANCE + "," + UPDATED_SUSCEPTANCE);

        // Get all the busList where susceptance equals to UPDATED_SUSCEPTANCE
        defaultBusShouldNotBeFound("susceptance.in=" + UPDATED_SUSCEPTANCE);
    }

    @Test
    @Transactional
    void getAllBusesBySusceptanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where susceptance is not null
        defaultBusShouldBeFound("susceptance.specified=true");

        // Get all the busList where susceptance is null
        defaultBusShouldNotBeFound("susceptance.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesBySusceptanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where susceptance is greater than or equal to DEFAULT_SUSCEPTANCE
        defaultBusShouldBeFound("susceptance.greaterThanOrEqual=" + DEFAULT_SUSCEPTANCE);

        // Get all the busList where susceptance is greater than or equal to UPDATED_SUSCEPTANCE
        defaultBusShouldNotBeFound("susceptance.greaterThanOrEqual=" + UPDATED_SUSCEPTANCE);
    }

    @Test
    @Transactional
    void getAllBusesBySusceptanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where susceptance is less than or equal to DEFAULT_SUSCEPTANCE
        defaultBusShouldBeFound("susceptance.lessThanOrEqual=" + DEFAULT_SUSCEPTANCE);

        // Get all the busList where susceptance is less than or equal to SMALLER_SUSCEPTANCE
        defaultBusShouldNotBeFound("susceptance.lessThanOrEqual=" + SMALLER_SUSCEPTANCE);
    }

    @Test
    @Transactional
    void getAllBusesBySusceptanceIsLessThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where susceptance is less than DEFAULT_SUSCEPTANCE
        defaultBusShouldNotBeFound("susceptance.lessThan=" + DEFAULT_SUSCEPTANCE);

        // Get all the busList where susceptance is less than UPDATED_SUSCEPTANCE
        defaultBusShouldBeFound("susceptance.lessThan=" + UPDATED_SUSCEPTANCE);
    }

    @Test
    @Transactional
    void getAllBusesBySusceptanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where susceptance is greater than DEFAULT_SUSCEPTANCE
        defaultBusShouldNotBeFound("susceptance.greaterThan=" + DEFAULT_SUSCEPTANCE);

        // Get all the busList where susceptance is greater than SMALLER_SUSCEPTANCE
        defaultBusShouldBeFound("susceptance.greaterThan=" + SMALLER_SUSCEPTANCE);
    }

    @Test
    @Transactional
    void getAllBusesByAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where area equals to DEFAULT_AREA
        defaultBusShouldBeFound("area.equals=" + DEFAULT_AREA);

        // Get all the busList where area equals to UPDATED_AREA
        defaultBusShouldNotBeFound("area.equals=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllBusesByAreaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where area not equals to DEFAULT_AREA
        defaultBusShouldNotBeFound("area.notEquals=" + DEFAULT_AREA);

        // Get all the busList where area not equals to UPDATED_AREA
        defaultBusShouldBeFound("area.notEquals=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllBusesByAreaIsInShouldWork() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where area in DEFAULT_AREA or UPDATED_AREA
        defaultBusShouldBeFound("area.in=" + DEFAULT_AREA + "," + UPDATED_AREA);

        // Get all the busList where area equals to UPDATED_AREA
        defaultBusShouldNotBeFound("area.in=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllBusesByAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where area is not null
        defaultBusShouldBeFound("area.specified=true");

        // Get all the busList where area is null
        defaultBusShouldNotBeFound("area.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where area is greater than or equal to DEFAULT_AREA
        defaultBusShouldBeFound("area.greaterThanOrEqual=" + DEFAULT_AREA);

        // Get all the busList where area is greater than or equal to UPDATED_AREA
        defaultBusShouldNotBeFound("area.greaterThanOrEqual=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllBusesByAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where area is less than or equal to DEFAULT_AREA
        defaultBusShouldBeFound("area.lessThanOrEqual=" + DEFAULT_AREA);

        // Get all the busList where area is less than or equal to SMALLER_AREA
        defaultBusShouldNotBeFound("area.lessThanOrEqual=" + SMALLER_AREA);
    }

    @Test
    @Transactional
    void getAllBusesByAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where area is less than DEFAULT_AREA
        defaultBusShouldNotBeFound("area.lessThan=" + DEFAULT_AREA);

        // Get all the busList where area is less than UPDATED_AREA
        defaultBusShouldBeFound("area.lessThan=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllBusesByAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where area is greater than DEFAULT_AREA
        defaultBusShouldNotBeFound("area.greaterThan=" + DEFAULT_AREA);

        // Get all the busList where area is greater than SMALLER_AREA
        defaultBusShouldBeFound("area.greaterThan=" + SMALLER_AREA);
    }

    @Test
    @Transactional
    void getAllBusesByVmIsEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vm equals to DEFAULT_VM
        defaultBusShouldBeFound("vm.equals=" + DEFAULT_VM);

        // Get all the busList where vm equals to UPDATED_VM
        defaultBusShouldNotBeFound("vm.equals=" + UPDATED_VM);
    }

    @Test
    @Transactional
    void getAllBusesByVmIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vm not equals to DEFAULT_VM
        defaultBusShouldNotBeFound("vm.notEquals=" + DEFAULT_VM);

        // Get all the busList where vm not equals to UPDATED_VM
        defaultBusShouldBeFound("vm.notEquals=" + UPDATED_VM);
    }

    @Test
    @Transactional
    void getAllBusesByVmIsInShouldWork() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vm in DEFAULT_VM or UPDATED_VM
        defaultBusShouldBeFound("vm.in=" + DEFAULT_VM + "," + UPDATED_VM);

        // Get all the busList where vm equals to UPDATED_VM
        defaultBusShouldNotBeFound("vm.in=" + UPDATED_VM);
    }

    @Test
    @Transactional
    void getAllBusesByVmIsNullOrNotNull() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vm is not null
        defaultBusShouldBeFound("vm.specified=true");

        // Get all the busList where vm is null
        defaultBusShouldNotBeFound("vm.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByVmIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vm is greater than or equal to DEFAULT_VM
        defaultBusShouldBeFound("vm.greaterThanOrEqual=" + DEFAULT_VM);

        // Get all the busList where vm is greater than or equal to UPDATED_VM
        defaultBusShouldNotBeFound("vm.greaterThanOrEqual=" + UPDATED_VM);
    }

    @Test
    @Transactional
    void getAllBusesByVmIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vm is less than or equal to DEFAULT_VM
        defaultBusShouldBeFound("vm.lessThanOrEqual=" + DEFAULT_VM);

        // Get all the busList where vm is less than or equal to SMALLER_VM
        defaultBusShouldNotBeFound("vm.lessThanOrEqual=" + SMALLER_VM);
    }

    @Test
    @Transactional
    void getAllBusesByVmIsLessThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vm is less than DEFAULT_VM
        defaultBusShouldNotBeFound("vm.lessThan=" + DEFAULT_VM);

        // Get all the busList where vm is less than UPDATED_VM
        defaultBusShouldBeFound("vm.lessThan=" + UPDATED_VM);
    }

    @Test
    @Transactional
    void getAllBusesByVmIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vm is greater than DEFAULT_VM
        defaultBusShouldNotBeFound("vm.greaterThan=" + DEFAULT_VM);

        // Get all the busList where vm is greater than SMALLER_VM
        defaultBusShouldBeFound("vm.greaterThan=" + SMALLER_VM);
    }

    @Test
    @Transactional
    void getAllBusesByVaIsEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where va equals to DEFAULT_VA
        defaultBusShouldBeFound("va.equals=" + DEFAULT_VA);

        // Get all the busList where va equals to UPDATED_VA
        defaultBusShouldNotBeFound("va.equals=" + UPDATED_VA);
    }

    @Test
    @Transactional
    void getAllBusesByVaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where va not equals to DEFAULT_VA
        defaultBusShouldNotBeFound("va.notEquals=" + DEFAULT_VA);

        // Get all the busList where va not equals to UPDATED_VA
        defaultBusShouldBeFound("va.notEquals=" + UPDATED_VA);
    }

    @Test
    @Transactional
    void getAllBusesByVaIsInShouldWork() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where va in DEFAULT_VA or UPDATED_VA
        defaultBusShouldBeFound("va.in=" + DEFAULT_VA + "," + UPDATED_VA);

        // Get all the busList where va equals to UPDATED_VA
        defaultBusShouldNotBeFound("va.in=" + UPDATED_VA);
    }

    @Test
    @Transactional
    void getAllBusesByVaIsNullOrNotNull() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where va is not null
        defaultBusShouldBeFound("va.specified=true");

        // Get all the busList where va is null
        defaultBusShouldNotBeFound("va.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByVaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where va is greater than or equal to DEFAULT_VA
        defaultBusShouldBeFound("va.greaterThanOrEqual=" + DEFAULT_VA);

        // Get all the busList where va is greater than or equal to UPDATED_VA
        defaultBusShouldNotBeFound("va.greaterThanOrEqual=" + UPDATED_VA);
    }

    @Test
    @Transactional
    void getAllBusesByVaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where va is less than or equal to DEFAULT_VA
        defaultBusShouldBeFound("va.lessThanOrEqual=" + DEFAULT_VA);

        // Get all the busList where va is less than or equal to SMALLER_VA
        defaultBusShouldNotBeFound("va.lessThanOrEqual=" + SMALLER_VA);
    }

    @Test
    @Transactional
    void getAllBusesByVaIsLessThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where va is less than DEFAULT_VA
        defaultBusShouldNotBeFound("va.lessThan=" + DEFAULT_VA);

        // Get all the busList where va is less than UPDATED_VA
        defaultBusShouldBeFound("va.lessThan=" + UPDATED_VA);
    }

    @Test
    @Transactional
    void getAllBusesByVaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where va is greater than DEFAULT_VA
        defaultBusShouldNotBeFound("va.greaterThan=" + DEFAULT_VA);

        // Get all the busList where va is greater than SMALLER_VA
        defaultBusShouldBeFound("va.greaterThan=" + SMALLER_VA);
    }

    @Test
    @Transactional
    void getAllBusesByBaseKvIsEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where baseKv equals to DEFAULT_BASE_KV
        defaultBusShouldBeFound("baseKv.equals=" + DEFAULT_BASE_KV);

        // Get all the busList where baseKv equals to UPDATED_BASE_KV
        defaultBusShouldNotBeFound("baseKv.equals=" + UPDATED_BASE_KV);
    }

    @Test
    @Transactional
    void getAllBusesByBaseKvIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where baseKv not equals to DEFAULT_BASE_KV
        defaultBusShouldNotBeFound("baseKv.notEquals=" + DEFAULT_BASE_KV);

        // Get all the busList where baseKv not equals to UPDATED_BASE_KV
        defaultBusShouldBeFound("baseKv.notEquals=" + UPDATED_BASE_KV);
    }

    @Test
    @Transactional
    void getAllBusesByBaseKvIsInShouldWork() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where baseKv in DEFAULT_BASE_KV or UPDATED_BASE_KV
        defaultBusShouldBeFound("baseKv.in=" + DEFAULT_BASE_KV + "," + UPDATED_BASE_KV);

        // Get all the busList where baseKv equals to UPDATED_BASE_KV
        defaultBusShouldNotBeFound("baseKv.in=" + UPDATED_BASE_KV);
    }

    @Test
    @Transactional
    void getAllBusesByBaseKvIsNullOrNotNull() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where baseKv is not null
        defaultBusShouldBeFound("baseKv.specified=true");

        // Get all the busList where baseKv is null
        defaultBusShouldNotBeFound("baseKv.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByBaseKvIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where baseKv is greater than or equal to DEFAULT_BASE_KV
        defaultBusShouldBeFound("baseKv.greaterThanOrEqual=" + DEFAULT_BASE_KV);

        // Get all the busList where baseKv is greater than or equal to UPDATED_BASE_KV
        defaultBusShouldNotBeFound("baseKv.greaterThanOrEqual=" + UPDATED_BASE_KV);
    }

    @Test
    @Transactional
    void getAllBusesByBaseKvIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where baseKv is less than or equal to DEFAULT_BASE_KV
        defaultBusShouldBeFound("baseKv.lessThanOrEqual=" + DEFAULT_BASE_KV);

        // Get all the busList where baseKv is less than or equal to SMALLER_BASE_KV
        defaultBusShouldNotBeFound("baseKv.lessThanOrEqual=" + SMALLER_BASE_KV);
    }

    @Test
    @Transactional
    void getAllBusesByBaseKvIsLessThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where baseKv is less than DEFAULT_BASE_KV
        defaultBusShouldNotBeFound("baseKv.lessThan=" + DEFAULT_BASE_KV);

        // Get all the busList where baseKv is less than UPDATED_BASE_KV
        defaultBusShouldBeFound("baseKv.lessThan=" + UPDATED_BASE_KV);
    }

    @Test
    @Transactional
    void getAllBusesByBaseKvIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where baseKv is greater than DEFAULT_BASE_KV
        defaultBusShouldNotBeFound("baseKv.greaterThan=" + DEFAULT_BASE_KV);

        // Get all the busList where baseKv is greater than SMALLER_BASE_KV
        defaultBusShouldBeFound("baseKv.greaterThan=" + SMALLER_BASE_KV);
    }

    @Test
    @Transactional
    void getAllBusesByZoneIsEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where zone equals to DEFAULT_ZONE
        defaultBusShouldBeFound("zone.equals=" + DEFAULT_ZONE);

        // Get all the busList where zone equals to UPDATED_ZONE
        defaultBusShouldNotBeFound("zone.equals=" + UPDATED_ZONE);
    }

    @Test
    @Transactional
    void getAllBusesByZoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where zone not equals to DEFAULT_ZONE
        defaultBusShouldNotBeFound("zone.notEquals=" + DEFAULT_ZONE);

        // Get all the busList where zone not equals to UPDATED_ZONE
        defaultBusShouldBeFound("zone.notEquals=" + UPDATED_ZONE);
    }

    @Test
    @Transactional
    void getAllBusesByZoneIsInShouldWork() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where zone in DEFAULT_ZONE or UPDATED_ZONE
        defaultBusShouldBeFound("zone.in=" + DEFAULT_ZONE + "," + UPDATED_ZONE);

        // Get all the busList where zone equals to UPDATED_ZONE
        defaultBusShouldNotBeFound("zone.in=" + UPDATED_ZONE);
    }

    @Test
    @Transactional
    void getAllBusesByZoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where zone is not null
        defaultBusShouldBeFound("zone.specified=true");

        // Get all the busList where zone is null
        defaultBusShouldNotBeFound("zone.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByZoneIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where zone is greater than or equal to DEFAULT_ZONE
        defaultBusShouldBeFound("zone.greaterThanOrEqual=" + DEFAULT_ZONE);

        // Get all the busList where zone is greater than or equal to UPDATED_ZONE
        defaultBusShouldNotBeFound("zone.greaterThanOrEqual=" + UPDATED_ZONE);
    }

    @Test
    @Transactional
    void getAllBusesByZoneIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where zone is less than or equal to DEFAULT_ZONE
        defaultBusShouldBeFound("zone.lessThanOrEqual=" + DEFAULT_ZONE);

        // Get all the busList where zone is less than or equal to SMALLER_ZONE
        defaultBusShouldNotBeFound("zone.lessThanOrEqual=" + SMALLER_ZONE);
    }

    @Test
    @Transactional
    void getAllBusesByZoneIsLessThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where zone is less than DEFAULT_ZONE
        defaultBusShouldNotBeFound("zone.lessThan=" + DEFAULT_ZONE);

        // Get all the busList where zone is less than UPDATED_ZONE
        defaultBusShouldBeFound("zone.lessThan=" + UPDATED_ZONE);
    }

    @Test
    @Transactional
    void getAllBusesByZoneIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where zone is greater than DEFAULT_ZONE
        defaultBusShouldNotBeFound("zone.greaterThan=" + DEFAULT_ZONE);

        // Get all the busList where zone is greater than SMALLER_ZONE
        defaultBusShouldBeFound("zone.greaterThan=" + SMALLER_ZONE);
    }

    @Test
    @Transactional
    void getAllBusesByVmaxIsEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vmax equals to DEFAULT_VMAX
        defaultBusShouldBeFound("vmax.equals=" + DEFAULT_VMAX);

        // Get all the busList where vmax equals to UPDATED_VMAX
        defaultBusShouldNotBeFound("vmax.equals=" + UPDATED_VMAX);
    }

    @Test
    @Transactional
    void getAllBusesByVmaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vmax not equals to DEFAULT_VMAX
        defaultBusShouldNotBeFound("vmax.notEquals=" + DEFAULT_VMAX);

        // Get all the busList where vmax not equals to UPDATED_VMAX
        defaultBusShouldBeFound("vmax.notEquals=" + UPDATED_VMAX);
    }

    @Test
    @Transactional
    void getAllBusesByVmaxIsInShouldWork() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vmax in DEFAULT_VMAX or UPDATED_VMAX
        defaultBusShouldBeFound("vmax.in=" + DEFAULT_VMAX + "," + UPDATED_VMAX);

        // Get all the busList where vmax equals to UPDATED_VMAX
        defaultBusShouldNotBeFound("vmax.in=" + UPDATED_VMAX);
    }

    @Test
    @Transactional
    void getAllBusesByVmaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vmax is not null
        defaultBusShouldBeFound("vmax.specified=true");

        // Get all the busList where vmax is null
        defaultBusShouldNotBeFound("vmax.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByVmaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vmax is greater than or equal to DEFAULT_VMAX
        defaultBusShouldBeFound("vmax.greaterThanOrEqual=" + DEFAULT_VMAX);

        // Get all the busList where vmax is greater than or equal to UPDATED_VMAX
        defaultBusShouldNotBeFound("vmax.greaterThanOrEqual=" + UPDATED_VMAX);
    }

    @Test
    @Transactional
    void getAllBusesByVmaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vmax is less than or equal to DEFAULT_VMAX
        defaultBusShouldBeFound("vmax.lessThanOrEqual=" + DEFAULT_VMAX);

        // Get all the busList where vmax is less than or equal to SMALLER_VMAX
        defaultBusShouldNotBeFound("vmax.lessThanOrEqual=" + SMALLER_VMAX);
    }

    @Test
    @Transactional
    void getAllBusesByVmaxIsLessThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vmax is less than DEFAULT_VMAX
        defaultBusShouldNotBeFound("vmax.lessThan=" + DEFAULT_VMAX);

        // Get all the busList where vmax is less than UPDATED_VMAX
        defaultBusShouldBeFound("vmax.lessThan=" + UPDATED_VMAX);
    }

    @Test
    @Transactional
    void getAllBusesByVmaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vmax is greater than DEFAULT_VMAX
        defaultBusShouldNotBeFound("vmax.greaterThan=" + DEFAULT_VMAX);

        // Get all the busList where vmax is greater than SMALLER_VMAX
        defaultBusShouldBeFound("vmax.greaterThan=" + SMALLER_VMAX);
    }

    @Test
    @Transactional
    void getAllBusesByVminIsEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vmin equals to DEFAULT_VMIN
        defaultBusShouldBeFound("vmin.equals=" + DEFAULT_VMIN);

        // Get all the busList where vmin equals to UPDATED_VMIN
        defaultBusShouldNotBeFound("vmin.equals=" + UPDATED_VMIN);
    }

    @Test
    @Transactional
    void getAllBusesByVminIsNotEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vmin not equals to DEFAULT_VMIN
        defaultBusShouldNotBeFound("vmin.notEquals=" + DEFAULT_VMIN);

        // Get all the busList where vmin not equals to UPDATED_VMIN
        defaultBusShouldBeFound("vmin.notEquals=" + UPDATED_VMIN);
    }

    @Test
    @Transactional
    void getAllBusesByVminIsInShouldWork() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vmin in DEFAULT_VMIN or UPDATED_VMIN
        defaultBusShouldBeFound("vmin.in=" + DEFAULT_VMIN + "," + UPDATED_VMIN);

        // Get all the busList where vmin equals to UPDATED_VMIN
        defaultBusShouldNotBeFound("vmin.in=" + UPDATED_VMIN);
    }

    @Test
    @Transactional
    void getAllBusesByVminIsNullOrNotNull() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vmin is not null
        defaultBusShouldBeFound("vmin.specified=true");

        // Get all the busList where vmin is null
        defaultBusShouldNotBeFound("vmin.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByVminIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vmin is greater than or equal to DEFAULT_VMIN
        defaultBusShouldBeFound("vmin.greaterThanOrEqual=" + DEFAULT_VMIN);

        // Get all the busList where vmin is greater than or equal to UPDATED_VMIN
        defaultBusShouldNotBeFound("vmin.greaterThanOrEqual=" + UPDATED_VMIN);
    }

    @Test
    @Transactional
    void getAllBusesByVminIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vmin is less than or equal to DEFAULT_VMIN
        defaultBusShouldBeFound("vmin.lessThanOrEqual=" + DEFAULT_VMIN);

        // Get all the busList where vmin is less than or equal to SMALLER_VMIN
        defaultBusShouldNotBeFound("vmin.lessThanOrEqual=" + SMALLER_VMIN);
    }

    @Test
    @Transactional
    void getAllBusesByVminIsLessThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vmin is less than DEFAULT_VMIN
        defaultBusShouldNotBeFound("vmin.lessThan=" + DEFAULT_VMIN);

        // Get all the busList where vmin is less than UPDATED_VMIN
        defaultBusShouldBeFound("vmin.lessThan=" + UPDATED_VMIN);
    }

    @Test
    @Transactional
    void getAllBusesByVminIsGreaterThanSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList where vmin is greater than DEFAULT_VMIN
        defaultBusShouldNotBeFound("vmin.greaterThan=" + DEFAULT_VMIN);

        // Get all the busList where vmin is greater than SMALLER_VMIN
        defaultBusShouldBeFound("vmin.greaterThan=" + SMALLER_VMIN);
    }

    @Test
    @Transactional
    void getAllBusesByLoadELValIsEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);
        LoadElVal loadELVal;
        if (TestUtil.findAll(em, LoadElVal.class).isEmpty()) {
            loadELVal = LoadElValResourceIT.createEntity(em);
            em.persist(loadELVal);
            em.flush();
        } else {
            loadELVal = TestUtil.findAll(em, LoadElVal.class).get(0);
        }
        em.persist(loadELVal);
        em.flush();
        bus.addLoadELVal(loadELVal);
        busRepository.saveAndFlush(bus);
        Long loadELValId = loadELVal.getId();

        // Get all the busList where loadELVal equals to loadELValId
        defaultBusShouldBeFound("loadELValId.equals=" + loadELValId);

        // Get all the busList where loadELVal equals to (loadELValId + 1)
        defaultBusShouldNotBeFound("loadELValId.equals=" + (loadELValId + 1));
    }

    @Test
    @Transactional
    void getAllBusesByBusNameIsEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);
        BusName busName;
        if (TestUtil.findAll(em, BusName.class).isEmpty()) {
            busName = BusNameResourceIT.createEntity(em);
            em.persist(busName);
            em.flush();
        } else {
            busName = TestUtil.findAll(em, BusName.class).get(0);
        }
        em.persist(busName);
        em.flush();
        bus.setBusName(busName);
        busName.setBus(bus);
        busRepository.saveAndFlush(bus);
        Long busNameId = busName.getId();

        // Get all the busList where busName equals to busNameId
        defaultBusShouldBeFound("busNameId.equals=" + busNameId);

        // Get all the busList where busName equals to (busNameId + 1)
        defaultBusShouldNotBeFound("busNameId.equals=" + (busNameId + 1));
    }

    @Test
    @Transactional
    void getAllBusesByBusExtensionIsEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);
        BusExtension busExtension;
        if (TestUtil.findAll(em, BusExtension.class).isEmpty()) {
            busExtension = BusExtensionResourceIT.createEntity(em);
            em.persist(busExtension);
            em.flush();
        } else {
            busExtension = TestUtil.findAll(em, BusExtension.class).get(0);
        }
        em.persist(busExtension);
        em.flush();
        bus.setBusExtension(busExtension);
        busExtension.setBus(bus);
        busRepository.saveAndFlush(bus);
        Long busExtensionId = busExtension.getId();

        // Get all the busList where busExtension equals to busExtensionId
        defaultBusShouldBeFound("busExtensionId.equals=" + busExtensionId);

        // Get all the busList where busExtension equals to (busExtensionId + 1)
        defaultBusShouldNotBeFound("busExtensionId.equals=" + (busExtensionId + 1));
    }

    @Test
    @Transactional
    void getAllBusesByBusCoordinateIsEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);
        BusCoordinate busCoordinate;
        if (TestUtil.findAll(em, BusCoordinate.class).isEmpty()) {
            busCoordinate = BusCoordinateResourceIT.createEntity(em);
            em.persist(busCoordinate);
            em.flush();
        } else {
            busCoordinate = TestUtil.findAll(em, BusCoordinate.class).get(0);
        }
        em.persist(busCoordinate);
        em.flush();
        bus.setBusCoordinate(busCoordinate);
        busCoordinate.setBus(bus);
        busRepository.saveAndFlush(bus);
        Long busCoordinateId = busCoordinate.getId();

        // Get all the busList where busCoordinate equals to busCoordinateId
        defaultBusShouldBeFound("busCoordinateId.equals=" + busCoordinateId);

        // Get all the busList where busCoordinate equals to (busCoordinateId + 1)
        defaultBusShouldNotBeFound("busCoordinateId.equals=" + (busCoordinateId + 1));
    }

    @Test
    @Transactional
    void getAllBusesByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);
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
        bus.setNetwork(network);
        busRepository.saveAndFlush(bus);
        Long networkId = network.getId();

        // Get all the busList where network equals to networkId
        defaultBusShouldBeFound("networkId.equals=" + networkId);

        // Get all the busList where network equals to (networkId + 1)
        defaultBusShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBusShouldBeFound(String filter) throws Exception {
        restBusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bus.getId().intValue())))
            .andExpect(jsonPath("$.[*].busNum").value(hasItem(DEFAULT_BUS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].activePower").value(hasItem(DEFAULT_ACTIVE_POWER.doubleValue())))
            .andExpect(jsonPath("$.[*].reactivePower").value(hasItem(DEFAULT_REACTIVE_POWER.doubleValue())))
            .andExpect(jsonPath("$.[*].conductance").value(hasItem(DEFAULT_CONDUCTANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].susceptance").value(hasItem(DEFAULT_SUSCEPTANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA.intValue())))
            .andExpect(jsonPath("$.[*].vm").value(hasItem(DEFAULT_VM.doubleValue())))
            .andExpect(jsonPath("$.[*].va").value(hasItem(DEFAULT_VA.doubleValue())))
            .andExpect(jsonPath("$.[*].baseKv").value(hasItem(DEFAULT_BASE_KV.doubleValue())))
            .andExpect(jsonPath("$.[*].zone").value(hasItem(DEFAULT_ZONE.intValue())))
            .andExpect(jsonPath("$.[*].vmax").value(hasItem(DEFAULT_VMAX.doubleValue())))
            .andExpect(jsonPath("$.[*].vmin").value(hasItem(DEFAULT_VMIN.doubleValue())));

        // Check, that the count call also returns 1
        restBusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBusShouldNotBeFound(String filter) throws Exception {
        restBusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBus() throws Exception {
        // Get the bus
        restBusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBus() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        int databaseSizeBeforeUpdate = busRepository.findAll().size();

        // Update the bus
        Bus updatedBus = busRepository.findById(bus.getId()).get();
        // Disconnect from session so that the updates on updatedBus are not directly saved in db
        em.detach(updatedBus);
        updatedBus
            .busNum(UPDATED_BUS_NUM)
            .type(UPDATED_TYPE)
            .activePower(UPDATED_ACTIVE_POWER)
            .reactivePower(UPDATED_REACTIVE_POWER)
            .conductance(UPDATED_CONDUCTANCE)
            .susceptance(UPDATED_SUSCEPTANCE)
            .area(UPDATED_AREA)
            .vm(UPDATED_VM)
            .va(UPDATED_VA)
            .baseKv(UPDATED_BASE_KV)
            .zone(UPDATED_ZONE)
            .vmax(UPDATED_VMAX)
            .vmin(UPDATED_VMIN);
        BusDTO busDTO = busMapper.toDto(updatedBus);

        restBusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, busDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busDTO))
            )
            .andExpect(status().isOk());

        // Validate the Bus in the database
        List<Bus> busList = busRepository.findAll();
        assertThat(busList).hasSize(databaseSizeBeforeUpdate);
        Bus testBus = busList.get(busList.size() - 1);
        assertThat(testBus.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testBus.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBus.getActivePower()).isEqualTo(UPDATED_ACTIVE_POWER);
        assertThat(testBus.getReactivePower()).isEqualTo(UPDATED_REACTIVE_POWER);
        assertThat(testBus.getConductance()).isEqualTo(UPDATED_CONDUCTANCE);
        assertThat(testBus.getSusceptance()).isEqualTo(UPDATED_SUSCEPTANCE);
        assertThat(testBus.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testBus.getVm()).isEqualTo(UPDATED_VM);
        assertThat(testBus.getVa()).isEqualTo(UPDATED_VA);
        assertThat(testBus.getBaseKv()).isEqualTo(UPDATED_BASE_KV);
        assertThat(testBus.getZone()).isEqualTo(UPDATED_ZONE);
        assertThat(testBus.getVmax()).isEqualTo(UPDATED_VMAX);
        assertThat(testBus.getVmin()).isEqualTo(UPDATED_VMIN);
    }

    @Test
    @Transactional
    void putNonExistingBus() throws Exception {
        int databaseSizeBeforeUpdate = busRepository.findAll().size();
        bus.setId(count.incrementAndGet());

        // Create the Bus
        BusDTO busDTO = busMapper.toDto(bus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, busDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bus in the database
        List<Bus> busList = busRepository.findAll();
        assertThat(busList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBus() throws Exception {
        int databaseSizeBeforeUpdate = busRepository.findAll().size();
        bus.setId(count.incrementAndGet());

        // Create the Bus
        BusDTO busDTO = busMapper.toDto(bus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(busDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bus in the database
        List<Bus> busList = busRepository.findAll();
        assertThat(busList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBus() throws Exception {
        int databaseSizeBeforeUpdate = busRepository.findAll().size();
        bus.setId(count.incrementAndGet());

        // Create the Bus
        BusDTO busDTO = busMapper.toDto(bus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(busDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bus in the database
        List<Bus> busList = busRepository.findAll();
        assertThat(busList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBusWithPatch() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        int databaseSizeBeforeUpdate = busRepository.findAll().size();

        // Update the bus using partial update
        Bus partialUpdatedBus = new Bus();
        partialUpdatedBus.setId(bus.getId());

        partialUpdatedBus.conductance(UPDATED_CONDUCTANCE).area(UPDATED_AREA).vm(UPDATED_VM);

        restBusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBus.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBus))
            )
            .andExpect(status().isOk());

        // Validate the Bus in the database
        List<Bus> busList = busRepository.findAll();
        assertThat(busList).hasSize(databaseSizeBeforeUpdate);
        Bus testBus = busList.get(busList.size() - 1);
        assertThat(testBus.getBusNum()).isEqualTo(DEFAULT_BUS_NUM);
        assertThat(testBus.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testBus.getActivePower()).isEqualTo(DEFAULT_ACTIVE_POWER);
        assertThat(testBus.getReactivePower()).isEqualTo(DEFAULT_REACTIVE_POWER);
        assertThat(testBus.getConductance()).isEqualTo(UPDATED_CONDUCTANCE);
        assertThat(testBus.getSusceptance()).isEqualTo(DEFAULT_SUSCEPTANCE);
        assertThat(testBus.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testBus.getVm()).isEqualTo(UPDATED_VM);
        assertThat(testBus.getVa()).isEqualTo(DEFAULT_VA);
        assertThat(testBus.getBaseKv()).isEqualTo(DEFAULT_BASE_KV);
        assertThat(testBus.getZone()).isEqualTo(DEFAULT_ZONE);
        assertThat(testBus.getVmax()).isEqualTo(DEFAULT_VMAX);
        assertThat(testBus.getVmin()).isEqualTo(DEFAULT_VMIN);
    }

    @Test
    @Transactional
    void fullUpdateBusWithPatch() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        int databaseSizeBeforeUpdate = busRepository.findAll().size();

        // Update the bus using partial update
        Bus partialUpdatedBus = new Bus();
        partialUpdatedBus.setId(bus.getId());

        partialUpdatedBus
            .busNum(UPDATED_BUS_NUM)
            .type(UPDATED_TYPE)
            .activePower(UPDATED_ACTIVE_POWER)
            .reactivePower(UPDATED_REACTIVE_POWER)
            .conductance(UPDATED_CONDUCTANCE)
            .susceptance(UPDATED_SUSCEPTANCE)
            .area(UPDATED_AREA)
            .vm(UPDATED_VM)
            .va(UPDATED_VA)
            .baseKv(UPDATED_BASE_KV)
            .zone(UPDATED_ZONE)
            .vmax(UPDATED_VMAX)
            .vmin(UPDATED_VMIN);

        restBusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBus.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBus))
            )
            .andExpect(status().isOk());

        // Validate the Bus in the database
        List<Bus> busList = busRepository.findAll();
        assertThat(busList).hasSize(databaseSizeBeforeUpdate);
        Bus testBus = busList.get(busList.size() - 1);
        assertThat(testBus.getBusNum()).isEqualTo(UPDATED_BUS_NUM);
        assertThat(testBus.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBus.getActivePower()).isEqualTo(UPDATED_ACTIVE_POWER);
        assertThat(testBus.getReactivePower()).isEqualTo(UPDATED_REACTIVE_POWER);
        assertThat(testBus.getConductance()).isEqualTo(UPDATED_CONDUCTANCE);
        assertThat(testBus.getSusceptance()).isEqualTo(UPDATED_SUSCEPTANCE);
        assertThat(testBus.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testBus.getVm()).isEqualTo(UPDATED_VM);
        assertThat(testBus.getVa()).isEqualTo(UPDATED_VA);
        assertThat(testBus.getBaseKv()).isEqualTo(UPDATED_BASE_KV);
        assertThat(testBus.getZone()).isEqualTo(UPDATED_ZONE);
        assertThat(testBus.getVmax()).isEqualTo(UPDATED_VMAX);
        assertThat(testBus.getVmin()).isEqualTo(UPDATED_VMIN);
    }

    @Test
    @Transactional
    void patchNonExistingBus() throws Exception {
        int databaseSizeBeforeUpdate = busRepository.findAll().size();
        bus.setId(count.incrementAndGet());

        // Create the Bus
        BusDTO busDTO = busMapper.toDto(bus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, busDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(busDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bus in the database
        List<Bus> busList = busRepository.findAll();
        assertThat(busList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBus() throws Exception {
        int databaseSizeBeforeUpdate = busRepository.findAll().size();
        bus.setId(count.incrementAndGet());

        // Create the Bus
        BusDTO busDTO = busMapper.toDto(bus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(busDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bus in the database
        List<Bus> busList = busRepository.findAll();
        assertThat(busList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBus() throws Exception {
        int databaseSizeBeforeUpdate = busRepository.findAll().size();
        bus.setId(count.incrementAndGet());

        // Create the Bus
        BusDTO busDTO = busMapper.toDto(bus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(busDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bus in the database
        List<Bus> busList = busRepository.findAll();
        assertThat(busList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBus() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        int databaseSizeBeforeDelete = busRepository.findAll().size();

        // Delete the bus
        restBusMockMvc
            .perform(delete(ENTITY_API_URL_ID, bus.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bus> busList = busRepository.findAll();
        assertThat(busList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
