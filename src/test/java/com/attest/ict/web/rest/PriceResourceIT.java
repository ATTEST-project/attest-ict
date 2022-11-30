package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.Price;
import com.attest.ict.repository.PriceRepository;
import com.attest.ict.service.criteria.PriceCriteria;
import com.attest.ict.service.dto.PriceDTO;
import com.attest.ict.service.mapper.PriceMapper;
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
 * Integration tests for the {@link PriceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PriceResourceIT {

    private static final Double DEFAULT_ELECTRICITY_ENERGY = 1D;
    private static final Double UPDATED_ELECTRICITY_ENERGY = 2D;
    private static final Double SMALLER_ELECTRICITY_ENERGY = 1D - 1D;

    private static final Double DEFAULT_GAS_ENERGY = 1D;
    private static final Double UPDATED_GAS_ENERGY = 2D;
    private static final Double SMALLER_GAS_ENERGY = 1D - 1D;

    private static final Double DEFAULT_SECONDARY_BAND = 1D;
    private static final Double UPDATED_SECONDARY_BAND = 2D;
    private static final Double SMALLER_SECONDARY_BAND = 1D - 1D;

    private static final Double DEFAULT_SECONDARY_UP = 1D;
    private static final Double UPDATED_SECONDARY_UP = 2D;
    private static final Double SMALLER_SECONDARY_UP = 1D - 1D;

    private static final Double DEFAULT_SECONDARY_DOWN = 1D;
    private static final Double UPDATED_SECONDARY_DOWN = 2D;
    private static final Double SMALLER_SECONDARY_DOWN = 1D - 1D;

    private static final Double DEFAULT_SECONDARY_RATIO_UP = 1D;
    private static final Double UPDATED_SECONDARY_RATIO_UP = 2D;
    private static final Double SMALLER_SECONDARY_RATIO_UP = 1D - 1D;

    private static final Double DEFAULT_SECONDARY_RATIO_DOWN = 1D;
    private static final Double UPDATED_SECONDARY_RATIO_DOWN = 2D;
    private static final Double SMALLER_SECONDARY_RATIO_DOWN = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/prices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private PriceMapper priceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPriceMockMvc;

    private Price price;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Price createEntity(EntityManager em) {
        Price price = new Price()
            .electricityEnergy(DEFAULT_ELECTRICITY_ENERGY)
            .gasEnergy(DEFAULT_GAS_ENERGY)
            .secondaryBand(DEFAULT_SECONDARY_BAND)
            .secondaryUp(DEFAULT_SECONDARY_UP)
            .secondaryDown(DEFAULT_SECONDARY_DOWN)
            .secondaryRatioUp(DEFAULT_SECONDARY_RATIO_UP)
            .secondaryRatioDown(DEFAULT_SECONDARY_RATIO_DOWN);
        return price;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Price createUpdatedEntity(EntityManager em) {
        Price price = new Price()
            .electricityEnergy(UPDATED_ELECTRICITY_ENERGY)
            .gasEnergy(UPDATED_GAS_ENERGY)
            .secondaryBand(UPDATED_SECONDARY_BAND)
            .secondaryUp(UPDATED_SECONDARY_UP)
            .secondaryDown(UPDATED_SECONDARY_DOWN)
            .secondaryRatioUp(UPDATED_SECONDARY_RATIO_UP)
            .secondaryRatioDown(UPDATED_SECONDARY_RATIO_DOWN);
        return price;
    }

    @BeforeEach
    public void initTest() {
        price = createEntity(em);
    }

    @Test
    @Transactional
    void createPrice() throws Exception {
        int databaseSizeBeforeCreate = priceRepository.findAll().size();
        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);
        restPriceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeCreate + 1);
        Price testPrice = priceList.get(priceList.size() - 1);
        assertThat(testPrice.getElectricityEnergy()).isEqualTo(DEFAULT_ELECTRICITY_ENERGY);
        assertThat(testPrice.getGasEnergy()).isEqualTo(DEFAULT_GAS_ENERGY);
        assertThat(testPrice.getSecondaryBand()).isEqualTo(DEFAULT_SECONDARY_BAND);
        assertThat(testPrice.getSecondaryUp()).isEqualTo(DEFAULT_SECONDARY_UP);
        assertThat(testPrice.getSecondaryDown()).isEqualTo(DEFAULT_SECONDARY_DOWN);
        assertThat(testPrice.getSecondaryRatioUp()).isEqualTo(DEFAULT_SECONDARY_RATIO_UP);
        assertThat(testPrice.getSecondaryRatioDown()).isEqualTo(DEFAULT_SECONDARY_RATIO_DOWN);
    }

    @Test
    @Transactional
    void createPriceWithExistingId() throws Exception {
        // Create the Price with an existing ID
        price.setId(1L);
        PriceDTO priceDTO = priceMapper.toDto(price);

        int databaseSizeBeforeCreate = priceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPriceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPrices() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList
        restPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(price.getId().intValue())))
            .andExpect(jsonPath("$.[*].electricityEnergy").value(hasItem(DEFAULT_ELECTRICITY_ENERGY.doubleValue())))
            .andExpect(jsonPath("$.[*].gasEnergy").value(hasItem(DEFAULT_GAS_ENERGY.doubleValue())))
            .andExpect(jsonPath("$.[*].secondaryBand").value(hasItem(DEFAULT_SECONDARY_BAND.doubleValue())))
            .andExpect(jsonPath("$.[*].secondaryUp").value(hasItem(DEFAULT_SECONDARY_UP.doubleValue())))
            .andExpect(jsonPath("$.[*].secondaryDown").value(hasItem(DEFAULT_SECONDARY_DOWN.doubleValue())))
            .andExpect(jsonPath("$.[*].secondaryRatioUp").value(hasItem(DEFAULT_SECONDARY_RATIO_UP.doubleValue())))
            .andExpect(jsonPath("$.[*].secondaryRatioDown").value(hasItem(DEFAULT_SECONDARY_RATIO_DOWN.doubleValue())));
    }

    @Test
    @Transactional
    void getPrice() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get the price
        restPriceMockMvc
            .perform(get(ENTITY_API_URL_ID, price.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(price.getId().intValue()))
            .andExpect(jsonPath("$.electricityEnergy").value(DEFAULT_ELECTRICITY_ENERGY.doubleValue()))
            .andExpect(jsonPath("$.gasEnergy").value(DEFAULT_GAS_ENERGY.doubleValue()))
            .andExpect(jsonPath("$.secondaryBand").value(DEFAULT_SECONDARY_BAND.doubleValue()))
            .andExpect(jsonPath("$.secondaryUp").value(DEFAULT_SECONDARY_UP.doubleValue()))
            .andExpect(jsonPath("$.secondaryDown").value(DEFAULT_SECONDARY_DOWN.doubleValue()))
            .andExpect(jsonPath("$.secondaryRatioUp").value(DEFAULT_SECONDARY_RATIO_UP.doubleValue()))
            .andExpect(jsonPath("$.secondaryRatioDown").value(DEFAULT_SECONDARY_RATIO_DOWN.doubleValue()));
    }

    @Test
    @Transactional
    void getPricesByIdFiltering() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        Long id = price.getId();

        defaultPriceShouldBeFound("id.equals=" + id);
        defaultPriceShouldNotBeFound("id.notEquals=" + id);

        defaultPriceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPriceShouldNotBeFound("id.greaterThan=" + id);

        defaultPriceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPriceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPricesByElectricityEnergyIsEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where electricityEnergy equals to DEFAULT_ELECTRICITY_ENERGY
        defaultPriceShouldBeFound("electricityEnergy.equals=" + DEFAULT_ELECTRICITY_ENERGY);

        // Get all the priceList where electricityEnergy equals to UPDATED_ELECTRICITY_ENERGY
        defaultPriceShouldNotBeFound("electricityEnergy.equals=" + UPDATED_ELECTRICITY_ENERGY);
    }

    @Test
    @Transactional
    void getAllPricesByElectricityEnergyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where electricityEnergy not equals to DEFAULT_ELECTRICITY_ENERGY
        defaultPriceShouldNotBeFound("electricityEnergy.notEquals=" + DEFAULT_ELECTRICITY_ENERGY);

        // Get all the priceList where electricityEnergy not equals to UPDATED_ELECTRICITY_ENERGY
        defaultPriceShouldBeFound("electricityEnergy.notEquals=" + UPDATED_ELECTRICITY_ENERGY);
    }

    @Test
    @Transactional
    void getAllPricesByElectricityEnergyIsInShouldWork() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where electricityEnergy in DEFAULT_ELECTRICITY_ENERGY or UPDATED_ELECTRICITY_ENERGY
        defaultPriceShouldBeFound("electricityEnergy.in=" + DEFAULT_ELECTRICITY_ENERGY + "," + UPDATED_ELECTRICITY_ENERGY);

        // Get all the priceList where electricityEnergy equals to UPDATED_ELECTRICITY_ENERGY
        defaultPriceShouldNotBeFound("electricityEnergy.in=" + UPDATED_ELECTRICITY_ENERGY);
    }

    @Test
    @Transactional
    void getAllPricesByElectricityEnergyIsNullOrNotNull() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where electricityEnergy is not null
        defaultPriceShouldBeFound("electricityEnergy.specified=true");

        // Get all the priceList where electricityEnergy is null
        defaultPriceShouldNotBeFound("electricityEnergy.specified=false");
    }

    @Test
    @Transactional
    void getAllPricesByElectricityEnergyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where electricityEnergy is greater than or equal to DEFAULT_ELECTRICITY_ENERGY
        defaultPriceShouldBeFound("electricityEnergy.greaterThanOrEqual=" + DEFAULT_ELECTRICITY_ENERGY);

        // Get all the priceList where electricityEnergy is greater than or equal to UPDATED_ELECTRICITY_ENERGY
        defaultPriceShouldNotBeFound("electricityEnergy.greaterThanOrEqual=" + UPDATED_ELECTRICITY_ENERGY);
    }

    @Test
    @Transactional
    void getAllPricesByElectricityEnergyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where electricityEnergy is less than or equal to DEFAULT_ELECTRICITY_ENERGY
        defaultPriceShouldBeFound("electricityEnergy.lessThanOrEqual=" + DEFAULT_ELECTRICITY_ENERGY);

        // Get all the priceList where electricityEnergy is less than or equal to SMALLER_ELECTRICITY_ENERGY
        defaultPriceShouldNotBeFound("electricityEnergy.lessThanOrEqual=" + SMALLER_ELECTRICITY_ENERGY);
    }

    @Test
    @Transactional
    void getAllPricesByElectricityEnergyIsLessThanSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where electricityEnergy is less than DEFAULT_ELECTRICITY_ENERGY
        defaultPriceShouldNotBeFound("electricityEnergy.lessThan=" + DEFAULT_ELECTRICITY_ENERGY);

        // Get all the priceList where electricityEnergy is less than UPDATED_ELECTRICITY_ENERGY
        defaultPriceShouldBeFound("electricityEnergy.lessThan=" + UPDATED_ELECTRICITY_ENERGY);
    }

    @Test
    @Transactional
    void getAllPricesByElectricityEnergyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where electricityEnergy is greater than DEFAULT_ELECTRICITY_ENERGY
        defaultPriceShouldNotBeFound("electricityEnergy.greaterThan=" + DEFAULT_ELECTRICITY_ENERGY);

        // Get all the priceList where electricityEnergy is greater than SMALLER_ELECTRICITY_ENERGY
        defaultPriceShouldBeFound("electricityEnergy.greaterThan=" + SMALLER_ELECTRICITY_ENERGY);
    }

    @Test
    @Transactional
    void getAllPricesByGasEnergyIsEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where gasEnergy equals to DEFAULT_GAS_ENERGY
        defaultPriceShouldBeFound("gasEnergy.equals=" + DEFAULT_GAS_ENERGY);

        // Get all the priceList where gasEnergy equals to UPDATED_GAS_ENERGY
        defaultPriceShouldNotBeFound("gasEnergy.equals=" + UPDATED_GAS_ENERGY);
    }

    @Test
    @Transactional
    void getAllPricesByGasEnergyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where gasEnergy not equals to DEFAULT_GAS_ENERGY
        defaultPriceShouldNotBeFound("gasEnergy.notEquals=" + DEFAULT_GAS_ENERGY);

        // Get all the priceList where gasEnergy not equals to UPDATED_GAS_ENERGY
        defaultPriceShouldBeFound("gasEnergy.notEquals=" + UPDATED_GAS_ENERGY);
    }

    @Test
    @Transactional
    void getAllPricesByGasEnergyIsInShouldWork() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where gasEnergy in DEFAULT_GAS_ENERGY or UPDATED_GAS_ENERGY
        defaultPriceShouldBeFound("gasEnergy.in=" + DEFAULT_GAS_ENERGY + "," + UPDATED_GAS_ENERGY);

        // Get all the priceList where gasEnergy equals to UPDATED_GAS_ENERGY
        defaultPriceShouldNotBeFound("gasEnergy.in=" + UPDATED_GAS_ENERGY);
    }

    @Test
    @Transactional
    void getAllPricesByGasEnergyIsNullOrNotNull() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where gasEnergy is not null
        defaultPriceShouldBeFound("gasEnergy.specified=true");

        // Get all the priceList where gasEnergy is null
        defaultPriceShouldNotBeFound("gasEnergy.specified=false");
    }

    @Test
    @Transactional
    void getAllPricesByGasEnergyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where gasEnergy is greater than or equal to DEFAULT_GAS_ENERGY
        defaultPriceShouldBeFound("gasEnergy.greaterThanOrEqual=" + DEFAULT_GAS_ENERGY);

        // Get all the priceList where gasEnergy is greater than or equal to UPDATED_GAS_ENERGY
        defaultPriceShouldNotBeFound("gasEnergy.greaterThanOrEqual=" + UPDATED_GAS_ENERGY);
    }

    @Test
    @Transactional
    void getAllPricesByGasEnergyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where gasEnergy is less than or equal to DEFAULT_GAS_ENERGY
        defaultPriceShouldBeFound("gasEnergy.lessThanOrEqual=" + DEFAULT_GAS_ENERGY);

        // Get all the priceList where gasEnergy is less than or equal to SMALLER_GAS_ENERGY
        defaultPriceShouldNotBeFound("gasEnergy.lessThanOrEqual=" + SMALLER_GAS_ENERGY);
    }

    @Test
    @Transactional
    void getAllPricesByGasEnergyIsLessThanSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where gasEnergy is less than DEFAULT_GAS_ENERGY
        defaultPriceShouldNotBeFound("gasEnergy.lessThan=" + DEFAULT_GAS_ENERGY);

        // Get all the priceList where gasEnergy is less than UPDATED_GAS_ENERGY
        defaultPriceShouldBeFound("gasEnergy.lessThan=" + UPDATED_GAS_ENERGY);
    }

    @Test
    @Transactional
    void getAllPricesByGasEnergyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where gasEnergy is greater than DEFAULT_GAS_ENERGY
        defaultPriceShouldNotBeFound("gasEnergy.greaterThan=" + DEFAULT_GAS_ENERGY);

        // Get all the priceList where gasEnergy is greater than SMALLER_GAS_ENERGY
        defaultPriceShouldBeFound("gasEnergy.greaterThan=" + SMALLER_GAS_ENERGY);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryBandIsEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryBand equals to DEFAULT_SECONDARY_BAND
        defaultPriceShouldBeFound("secondaryBand.equals=" + DEFAULT_SECONDARY_BAND);

        // Get all the priceList where secondaryBand equals to UPDATED_SECONDARY_BAND
        defaultPriceShouldNotBeFound("secondaryBand.equals=" + UPDATED_SECONDARY_BAND);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryBandIsNotEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryBand not equals to DEFAULT_SECONDARY_BAND
        defaultPriceShouldNotBeFound("secondaryBand.notEquals=" + DEFAULT_SECONDARY_BAND);

        // Get all the priceList where secondaryBand not equals to UPDATED_SECONDARY_BAND
        defaultPriceShouldBeFound("secondaryBand.notEquals=" + UPDATED_SECONDARY_BAND);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryBandIsInShouldWork() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryBand in DEFAULT_SECONDARY_BAND or UPDATED_SECONDARY_BAND
        defaultPriceShouldBeFound("secondaryBand.in=" + DEFAULT_SECONDARY_BAND + "," + UPDATED_SECONDARY_BAND);

        // Get all the priceList where secondaryBand equals to UPDATED_SECONDARY_BAND
        defaultPriceShouldNotBeFound("secondaryBand.in=" + UPDATED_SECONDARY_BAND);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryBandIsNullOrNotNull() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryBand is not null
        defaultPriceShouldBeFound("secondaryBand.specified=true");

        // Get all the priceList where secondaryBand is null
        defaultPriceShouldNotBeFound("secondaryBand.specified=false");
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryBandIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryBand is greater than or equal to DEFAULT_SECONDARY_BAND
        defaultPriceShouldBeFound("secondaryBand.greaterThanOrEqual=" + DEFAULT_SECONDARY_BAND);

        // Get all the priceList where secondaryBand is greater than or equal to UPDATED_SECONDARY_BAND
        defaultPriceShouldNotBeFound("secondaryBand.greaterThanOrEqual=" + UPDATED_SECONDARY_BAND);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryBandIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryBand is less than or equal to DEFAULT_SECONDARY_BAND
        defaultPriceShouldBeFound("secondaryBand.lessThanOrEqual=" + DEFAULT_SECONDARY_BAND);

        // Get all the priceList where secondaryBand is less than or equal to SMALLER_SECONDARY_BAND
        defaultPriceShouldNotBeFound("secondaryBand.lessThanOrEqual=" + SMALLER_SECONDARY_BAND);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryBandIsLessThanSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryBand is less than DEFAULT_SECONDARY_BAND
        defaultPriceShouldNotBeFound("secondaryBand.lessThan=" + DEFAULT_SECONDARY_BAND);

        // Get all the priceList where secondaryBand is less than UPDATED_SECONDARY_BAND
        defaultPriceShouldBeFound("secondaryBand.lessThan=" + UPDATED_SECONDARY_BAND);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryBandIsGreaterThanSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryBand is greater than DEFAULT_SECONDARY_BAND
        defaultPriceShouldNotBeFound("secondaryBand.greaterThan=" + DEFAULT_SECONDARY_BAND);

        // Get all the priceList where secondaryBand is greater than SMALLER_SECONDARY_BAND
        defaultPriceShouldBeFound("secondaryBand.greaterThan=" + SMALLER_SECONDARY_BAND);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryUpIsEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryUp equals to DEFAULT_SECONDARY_UP
        defaultPriceShouldBeFound("secondaryUp.equals=" + DEFAULT_SECONDARY_UP);

        // Get all the priceList where secondaryUp equals to UPDATED_SECONDARY_UP
        defaultPriceShouldNotBeFound("secondaryUp.equals=" + UPDATED_SECONDARY_UP);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryUpIsNotEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryUp not equals to DEFAULT_SECONDARY_UP
        defaultPriceShouldNotBeFound("secondaryUp.notEquals=" + DEFAULT_SECONDARY_UP);

        // Get all the priceList where secondaryUp not equals to UPDATED_SECONDARY_UP
        defaultPriceShouldBeFound("secondaryUp.notEquals=" + UPDATED_SECONDARY_UP);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryUpIsInShouldWork() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryUp in DEFAULT_SECONDARY_UP or UPDATED_SECONDARY_UP
        defaultPriceShouldBeFound("secondaryUp.in=" + DEFAULT_SECONDARY_UP + "," + UPDATED_SECONDARY_UP);

        // Get all the priceList where secondaryUp equals to UPDATED_SECONDARY_UP
        defaultPriceShouldNotBeFound("secondaryUp.in=" + UPDATED_SECONDARY_UP);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryUpIsNullOrNotNull() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryUp is not null
        defaultPriceShouldBeFound("secondaryUp.specified=true");

        // Get all the priceList where secondaryUp is null
        defaultPriceShouldNotBeFound("secondaryUp.specified=false");
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryUpIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryUp is greater than or equal to DEFAULT_SECONDARY_UP
        defaultPriceShouldBeFound("secondaryUp.greaterThanOrEqual=" + DEFAULT_SECONDARY_UP);

        // Get all the priceList where secondaryUp is greater than or equal to UPDATED_SECONDARY_UP
        defaultPriceShouldNotBeFound("secondaryUp.greaterThanOrEqual=" + UPDATED_SECONDARY_UP);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryUpIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryUp is less than or equal to DEFAULT_SECONDARY_UP
        defaultPriceShouldBeFound("secondaryUp.lessThanOrEqual=" + DEFAULT_SECONDARY_UP);

        // Get all the priceList where secondaryUp is less than or equal to SMALLER_SECONDARY_UP
        defaultPriceShouldNotBeFound("secondaryUp.lessThanOrEqual=" + SMALLER_SECONDARY_UP);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryUpIsLessThanSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryUp is less than DEFAULT_SECONDARY_UP
        defaultPriceShouldNotBeFound("secondaryUp.lessThan=" + DEFAULT_SECONDARY_UP);

        // Get all the priceList where secondaryUp is less than UPDATED_SECONDARY_UP
        defaultPriceShouldBeFound("secondaryUp.lessThan=" + UPDATED_SECONDARY_UP);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryUpIsGreaterThanSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryUp is greater than DEFAULT_SECONDARY_UP
        defaultPriceShouldNotBeFound("secondaryUp.greaterThan=" + DEFAULT_SECONDARY_UP);

        // Get all the priceList where secondaryUp is greater than SMALLER_SECONDARY_UP
        defaultPriceShouldBeFound("secondaryUp.greaterThan=" + SMALLER_SECONDARY_UP);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryDownIsEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryDown equals to DEFAULT_SECONDARY_DOWN
        defaultPriceShouldBeFound("secondaryDown.equals=" + DEFAULT_SECONDARY_DOWN);

        // Get all the priceList where secondaryDown equals to UPDATED_SECONDARY_DOWN
        defaultPriceShouldNotBeFound("secondaryDown.equals=" + UPDATED_SECONDARY_DOWN);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryDownIsNotEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryDown not equals to DEFAULT_SECONDARY_DOWN
        defaultPriceShouldNotBeFound("secondaryDown.notEquals=" + DEFAULT_SECONDARY_DOWN);

        // Get all the priceList where secondaryDown not equals to UPDATED_SECONDARY_DOWN
        defaultPriceShouldBeFound("secondaryDown.notEquals=" + UPDATED_SECONDARY_DOWN);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryDownIsInShouldWork() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryDown in DEFAULT_SECONDARY_DOWN or UPDATED_SECONDARY_DOWN
        defaultPriceShouldBeFound("secondaryDown.in=" + DEFAULT_SECONDARY_DOWN + "," + UPDATED_SECONDARY_DOWN);

        // Get all the priceList where secondaryDown equals to UPDATED_SECONDARY_DOWN
        defaultPriceShouldNotBeFound("secondaryDown.in=" + UPDATED_SECONDARY_DOWN);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryDownIsNullOrNotNull() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryDown is not null
        defaultPriceShouldBeFound("secondaryDown.specified=true");

        // Get all the priceList where secondaryDown is null
        defaultPriceShouldNotBeFound("secondaryDown.specified=false");
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryDownIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryDown is greater than or equal to DEFAULT_SECONDARY_DOWN
        defaultPriceShouldBeFound("secondaryDown.greaterThanOrEqual=" + DEFAULT_SECONDARY_DOWN);

        // Get all the priceList where secondaryDown is greater than or equal to UPDATED_SECONDARY_DOWN
        defaultPriceShouldNotBeFound("secondaryDown.greaterThanOrEqual=" + UPDATED_SECONDARY_DOWN);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryDownIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryDown is less than or equal to DEFAULT_SECONDARY_DOWN
        defaultPriceShouldBeFound("secondaryDown.lessThanOrEqual=" + DEFAULT_SECONDARY_DOWN);

        // Get all the priceList where secondaryDown is less than or equal to SMALLER_SECONDARY_DOWN
        defaultPriceShouldNotBeFound("secondaryDown.lessThanOrEqual=" + SMALLER_SECONDARY_DOWN);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryDownIsLessThanSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryDown is less than DEFAULT_SECONDARY_DOWN
        defaultPriceShouldNotBeFound("secondaryDown.lessThan=" + DEFAULT_SECONDARY_DOWN);

        // Get all the priceList where secondaryDown is less than UPDATED_SECONDARY_DOWN
        defaultPriceShouldBeFound("secondaryDown.lessThan=" + UPDATED_SECONDARY_DOWN);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryDownIsGreaterThanSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryDown is greater than DEFAULT_SECONDARY_DOWN
        defaultPriceShouldNotBeFound("secondaryDown.greaterThan=" + DEFAULT_SECONDARY_DOWN);

        // Get all the priceList where secondaryDown is greater than SMALLER_SECONDARY_DOWN
        defaultPriceShouldBeFound("secondaryDown.greaterThan=" + SMALLER_SECONDARY_DOWN);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryRatioUpIsEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryRatioUp equals to DEFAULT_SECONDARY_RATIO_UP
        defaultPriceShouldBeFound("secondaryRatioUp.equals=" + DEFAULT_SECONDARY_RATIO_UP);

        // Get all the priceList where secondaryRatioUp equals to UPDATED_SECONDARY_RATIO_UP
        defaultPriceShouldNotBeFound("secondaryRatioUp.equals=" + UPDATED_SECONDARY_RATIO_UP);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryRatioUpIsNotEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryRatioUp not equals to DEFAULT_SECONDARY_RATIO_UP
        defaultPriceShouldNotBeFound("secondaryRatioUp.notEquals=" + DEFAULT_SECONDARY_RATIO_UP);

        // Get all the priceList where secondaryRatioUp not equals to UPDATED_SECONDARY_RATIO_UP
        defaultPriceShouldBeFound("secondaryRatioUp.notEquals=" + UPDATED_SECONDARY_RATIO_UP);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryRatioUpIsInShouldWork() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryRatioUp in DEFAULT_SECONDARY_RATIO_UP or UPDATED_SECONDARY_RATIO_UP
        defaultPriceShouldBeFound("secondaryRatioUp.in=" + DEFAULT_SECONDARY_RATIO_UP + "," + UPDATED_SECONDARY_RATIO_UP);

        // Get all the priceList where secondaryRatioUp equals to UPDATED_SECONDARY_RATIO_UP
        defaultPriceShouldNotBeFound("secondaryRatioUp.in=" + UPDATED_SECONDARY_RATIO_UP);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryRatioUpIsNullOrNotNull() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryRatioUp is not null
        defaultPriceShouldBeFound("secondaryRatioUp.specified=true");

        // Get all the priceList where secondaryRatioUp is null
        defaultPriceShouldNotBeFound("secondaryRatioUp.specified=false");
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryRatioUpIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryRatioUp is greater than or equal to DEFAULT_SECONDARY_RATIO_UP
        defaultPriceShouldBeFound("secondaryRatioUp.greaterThanOrEqual=" + DEFAULT_SECONDARY_RATIO_UP);

        // Get all the priceList where secondaryRatioUp is greater than or equal to UPDATED_SECONDARY_RATIO_UP
        defaultPriceShouldNotBeFound("secondaryRatioUp.greaterThanOrEqual=" + UPDATED_SECONDARY_RATIO_UP);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryRatioUpIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryRatioUp is less than or equal to DEFAULT_SECONDARY_RATIO_UP
        defaultPriceShouldBeFound("secondaryRatioUp.lessThanOrEqual=" + DEFAULT_SECONDARY_RATIO_UP);

        // Get all the priceList where secondaryRatioUp is less than or equal to SMALLER_SECONDARY_RATIO_UP
        defaultPriceShouldNotBeFound("secondaryRatioUp.lessThanOrEqual=" + SMALLER_SECONDARY_RATIO_UP);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryRatioUpIsLessThanSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryRatioUp is less than DEFAULT_SECONDARY_RATIO_UP
        defaultPriceShouldNotBeFound("secondaryRatioUp.lessThan=" + DEFAULT_SECONDARY_RATIO_UP);

        // Get all the priceList where secondaryRatioUp is less than UPDATED_SECONDARY_RATIO_UP
        defaultPriceShouldBeFound("secondaryRatioUp.lessThan=" + UPDATED_SECONDARY_RATIO_UP);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryRatioUpIsGreaterThanSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryRatioUp is greater than DEFAULT_SECONDARY_RATIO_UP
        defaultPriceShouldNotBeFound("secondaryRatioUp.greaterThan=" + DEFAULT_SECONDARY_RATIO_UP);

        // Get all the priceList where secondaryRatioUp is greater than SMALLER_SECONDARY_RATIO_UP
        defaultPriceShouldBeFound("secondaryRatioUp.greaterThan=" + SMALLER_SECONDARY_RATIO_UP);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryRatioDownIsEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryRatioDown equals to DEFAULT_SECONDARY_RATIO_DOWN
        defaultPriceShouldBeFound("secondaryRatioDown.equals=" + DEFAULT_SECONDARY_RATIO_DOWN);

        // Get all the priceList where secondaryRatioDown equals to UPDATED_SECONDARY_RATIO_DOWN
        defaultPriceShouldNotBeFound("secondaryRatioDown.equals=" + UPDATED_SECONDARY_RATIO_DOWN);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryRatioDownIsNotEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryRatioDown not equals to DEFAULT_SECONDARY_RATIO_DOWN
        defaultPriceShouldNotBeFound("secondaryRatioDown.notEquals=" + DEFAULT_SECONDARY_RATIO_DOWN);

        // Get all the priceList where secondaryRatioDown not equals to UPDATED_SECONDARY_RATIO_DOWN
        defaultPriceShouldBeFound("secondaryRatioDown.notEquals=" + UPDATED_SECONDARY_RATIO_DOWN);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryRatioDownIsInShouldWork() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryRatioDown in DEFAULT_SECONDARY_RATIO_DOWN or UPDATED_SECONDARY_RATIO_DOWN
        defaultPriceShouldBeFound("secondaryRatioDown.in=" + DEFAULT_SECONDARY_RATIO_DOWN + "," + UPDATED_SECONDARY_RATIO_DOWN);

        // Get all the priceList where secondaryRatioDown equals to UPDATED_SECONDARY_RATIO_DOWN
        defaultPriceShouldNotBeFound("secondaryRatioDown.in=" + UPDATED_SECONDARY_RATIO_DOWN);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryRatioDownIsNullOrNotNull() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryRatioDown is not null
        defaultPriceShouldBeFound("secondaryRatioDown.specified=true");

        // Get all the priceList where secondaryRatioDown is null
        defaultPriceShouldNotBeFound("secondaryRatioDown.specified=false");
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryRatioDownIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryRatioDown is greater than or equal to DEFAULT_SECONDARY_RATIO_DOWN
        defaultPriceShouldBeFound("secondaryRatioDown.greaterThanOrEqual=" + DEFAULT_SECONDARY_RATIO_DOWN);

        // Get all the priceList where secondaryRatioDown is greater than or equal to UPDATED_SECONDARY_RATIO_DOWN
        defaultPriceShouldNotBeFound("secondaryRatioDown.greaterThanOrEqual=" + UPDATED_SECONDARY_RATIO_DOWN);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryRatioDownIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryRatioDown is less than or equal to DEFAULT_SECONDARY_RATIO_DOWN
        defaultPriceShouldBeFound("secondaryRatioDown.lessThanOrEqual=" + DEFAULT_SECONDARY_RATIO_DOWN);

        // Get all the priceList where secondaryRatioDown is less than or equal to SMALLER_SECONDARY_RATIO_DOWN
        defaultPriceShouldNotBeFound("secondaryRatioDown.lessThanOrEqual=" + SMALLER_SECONDARY_RATIO_DOWN);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryRatioDownIsLessThanSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryRatioDown is less than DEFAULT_SECONDARY_RATIO_DOWN
        defaultPriceShouldNotBeFound("secondaryRatioDown.lessThan=" + DEFAULT_SECONDARY_RATIO_DOWN);

        // Get all the priceList where secondaryRatioDown is less than UPDATED_SECONDARY_RATIO_DOWN
        defaultPriceShouldBeFound("secondaryRatioDown.lessThan=" + UPDATED_SECONDARY_RATIO_DOWN);
    }

    @Test
    @Transactional
    void getAllPricesBySecondaryRatioDownIsGreaterThanSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where secondaryRatioDown is greater than DEFAULT_SECONDARY_RATIO_DOWN
        defaultPriceShouldNotBeFound("secondaryRatioDown.greaterThan=" + DEFAULT_SECONDARY_RATIO_DOWN);

        // Get all the priceList where secondaryRatioDown is greater than SMALLER_SECONDARY_RATIO_DOWN
        defaultPriceShouldBeFound("secondaryRatioDown.greaterThan=" + SMALLER_SECONDARY_RATIO_DOWN);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPriceShouldBeFound(String filter) throws Exception {
        restPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(price.getId().intValue())))
            .andExpect(jsonPath("$.[*].electricityEnergy").value(hasItem(DEFAULT_ELECTRICITY_ENERGY.doubleValue())))
            .andExpect(jsonPath("$.[*].gasEnergy").value(hasItem(DEFAULT_GAS_ENERGY.doubleValue())))
            .andExpect(jsonPath("$.[*].secondaryBand").value(hasItem(DEFAULT_SECONDARY_BAND.doubleValue())))
            .andExpect(jsonPath("$.[*].secondaryUp").value(hasItem(DEFAULT_SECONDARY_UP.doubleValue())))
            .andExpect(jsonPath("$.[*].secondaryDown").value(hasItem(DEFAULT_SECONDARY_DOWN.doubleValue())))
            .andExpect(jsonPath("$.[*].secondaryRatioUp").value(hasItem(DEFAULT_SECONDARY_RATIO_UP.doubleValue())))
            .andExpect(jsonPath("$.[*].secondaryRatioDown").value(hasItem(DEFAULT_SECONDARY_RATIO_DOWN.doubleValue())));

        // Check, that the count call also returns 1
        restPriceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPriceShouldNotBeFound(String filter) throws Exception {
        restPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPriceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPrice() throws Exception {
        // Get the price
        restPriceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrice() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        int databaseSizeBeforeUpdate = priceRepository.findAll().size();

        // Update the price
        Price updatedPrice = priceRepository.findById(price.getId()).get();
        // Disconnect from session so that the updates on updatedPrice are not directly saved in db
        em.detach(updatedPrice);
        updatedPrice
            .electricityEnergy(UPDATED_ELECTRICITY_ENERGY)
            .gasEnergy(UPDATED_GAS_ENERGY)
            .secondaryBand(UPDATED_SECONDARY_BAND)
            .secondaryUp(UPDATED_SECONDARY_UP)
            .secondaryDown(UPDATED_SECONDARY_DOWN)
            .secondaryRatioUp(UPDATED_SECONDARY_RATIO_UP)
            .secondaryRatioDown(UPDATED_SECONDARY_RATIO_DOWN);
        PriceDTO priceDTO = priceMapper.toDto(updatedPrice);

        restPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, priceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
        Price testPrice = priceList.get(priceList.size() - 1);
        assertThat(testPrice.getElectricityEnergy()).isEqualTo(UPDATED_ELECTRICITY_ENERGY);
        assertThat(testPrice.getGasEnergy()).isEqualTo(UPDATED_GAS_ENERGY);
        assertThat(testPrice.getSecondaryBand()).isEqualTo(UPDATED_SECONDARY_BAND);
        assertThat(testPrice.getSecondaryUp()).isEqualTo(UPDATED_SECONDARY_UP);
        assertThat(testPrice.getSecondaryDown()).isEqualTo(UPDATED_SECONDARY_DOWN);
        assertThat(testPrice.getSecondaryRatioUp()).isEqualTo(UPDATED_SECONDARY_RATIO_UP);
        assertThat(testPrice.getSecondaryRatioDown()).isEqualTo(UPDATED_SECONDARY_RATIO_DOWN);
    }

    @Test
    @Transactional
    void putNonExistingPrice() throws Exception {
        int databaseSizeBeforeUpdate = priceRepository.findAll().size();
        price.setId(count.incrementAndGet());

        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, priceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrice() throws Exception {
        int databaseSizeBeforeUpdate = priceRepository.findAll().size();
        price.setId(count.incrementAndGet());

        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrice() throws Exception {
        int databaseSizeBeforeUpdate = priceRepository.findAll().size();
        price.setId(count.incrementAndGet());

        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePriceWithPatch() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        int databaseSizeBeforeUpdate = priceRepository.findAll().size();

        // Update the price using partial update
        Price partialUpdatedPrice = new Price();
        partialUpdatedPrice.setId(price.getId());

        partialUpdatedPrice.secondaryDown(UPDATED_SECONDARY_DOWN).secondaryRatioDown(UPDATED_SECONDARY_RATIO_DOWN);

        restPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrice.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrice))
            )
            .andExpect(status().isOk());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
        Price testPrice = priceList.get(priceList.size() - 1);
        assertThat(testPrice.getElectricityEnergy()).isEqualTo(DEFAULT_ELECTRICITY_ENERGY);
        assertThat(testPrice.getGasEnergy()).isEqualTo(DEFAULT_GAS_ENERGY);
        assertThat(testPrice.getSecondaryBand()).isEqualTo(DEFAULT_SECONDARY_BAND);
        assertThat(testPrice.getSecondaryUp()).isEqualTo(DEFAULT_SECONDARY_UP);
        assertThat(testPrice.getSecondaryDown()).isEqualTo(UPDATED_SECONDARY_DOWN);
        assertThat(testPrice.getSecondaryRatioUp()).isEqualTo(DEFAULT_SECONDARY_RATIO_UP);
        assertThat(testPrice.getSecondaryRatioDown()).isEqualTo(UPDATED_SECONDARY_RATIO_DOWN);
    }

    @Test
    @Transactional
    void fullUpdatePriceWithPatch() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        int databaseSizeBeforeUpdate = priceRepository.findAll().size();

        // Update the price using partial update
        Price partialUpdatedPrice = new Price();
        partialUpdatedPrice.setId(price.getId());

        partialUpdatedPrice
            .electricityEnergy(UPDATED_ELECTRICITY_ENERGY)
            .gasEnergy(UPDATED_GAS_ENERGY)
            .secondaryBand(UPDATED_SECONDARY_BAND)
            .secondaryUp(UPDATED_SECONDARY_UP)
            .secondaryDown(UPDATED_SECONDARY_DOWN)
            .secondaryRatioUp(UPDATED_SECONDARY_RATIO_UP)
            .secondaryRatioDown(UPDATED_SECONDARY_RATIO_DOWN);

        restPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrice.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrice))
            )
            .andExpect(status().isOk());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
        Price testPrice = priceList.get(priceList.size() - 1);
        assertThat(testPrice.getElectricityEnergy()).isEqualTo(UPDATED_ELECTRICITY_ENERGY);
        assertThat(testPrice.getGasEnergy()).isEqualTo(UPDATED_GAS_ENERGY);
        assertThat(testPrice.getSecondaryBand()).isEqualTo(UPDATED_SECONDARY_BAND);
        assertThat(testPrice.getSecondaryUp()).isEqualTo(UPDATED_SECONDARY_UP);
        assertThat(testPrice.getSecondaryDown()).isEqualTo(UPDATED_SECONDARY_DOWN);
        assertThat(testPrice.getSecondaryRatioUp()).isEqualTo(UPDATED_SECONDARY_RATIO_UP);
        assertThat(testPrice.getSecondaryRatioDown()).isEqualTo(UPDATED_SECONDARY_RATIO_DOWN);
    }

    @Test
    @Transactional
    void patchNonExistingPrice() throws Exception {
        int databaseSizeBeforeUpdate = priceRepository.findAll().size();
        price.setId(count.incrementAndGet());

        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, priceDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrice() throws Exception {
        int databaseSizeBeforeUpdate = priceRepository.findAll().size();
        price.setId(count.incrementAndGet());

        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrice() throws Exception {
        int databaseSizeBeforeUpdate = priceRepository.findAll().size();
        price.setId(count.incrementAndGet());

        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrice() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        int databaseSizeBeforeDelete = priceRepository.findAll().size();

        // Delete the price
        restPriceMockMvc
            .perform(delete(ENTITY_API_URL_ID, price.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
