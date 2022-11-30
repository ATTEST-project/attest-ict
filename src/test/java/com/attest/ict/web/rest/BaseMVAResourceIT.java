package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.BaseMVA;
import com.attest.ict.domain.Network;
import com.attest.ict.repository.BaseMVARepository;
import com.attest.ict.service.criteria.BaseMVACriteria;
import com.attest.ict.service.dto.BaseMVADTO;
import com.attest.ict.service.mapper.BaseMVAMapper;
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
 * Integration tests for the {@link BaseMVAResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BaseMVAResourceIT {

    private static final Double DEFAULT_BASE_MVA = 1D;
    private static final Double UPDATED_BASE_MVA = 2D;
    private static final Double SMALLER_BASE_MVA = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/base-mvas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BaseMVARepository baseMVARepository;

    @Autowired
    private BaseMVAMapper baseMVAMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBaseMVAMockMvc;

    private BaseMVA baseMVA;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BaseMVA createEntity(EntityManager em) {
        BaseMVA baseMVA = new BaseMVA().baseMva(DEFAULT_BASE_MVA);
        return baseMVA;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BaseMVA createUpdatedEntity(EntityManager em) {
        BaseMVA baseMVA = new BaseMVA().baseMva(UPDATED_BASE_MVA);
        return baseMVA;
    }

    @BeforeEach
    public void initTest() {
        baseMVA = createEntity(em);
    }

    @Test
    @Transactional
    void createBaseMVA() throws Exception {
        int databaseSizeBeforeCreate = baseMVARepository.findAll().size();
        // Create the BaseMVA
        BaseMVADTO baseMVADTO = baseMVAMapper.toDto(baseMVA);
        restBaseMVAMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(baseMVADTO))
            )
            .andExpect(status().isCreated());

        // Validate the BaseMVA in the database
        List<BaseMVA> baseMVAList = baseMVARepository.findAll();
        assertThat(baseMVAList).hasSize(databaseSizeBeforeCreate + 1);
        BaseMVA testBaseMVA = baseMVAList.get(baseMVAList.size() - 1);
        assertThat(testBaseMVA.getBaseMva()).isEqualTo(DEFAULT_BASE_MVA);
    }

    @Test
    @Transactional
    void createBaseMVAWithExistingId() throws Exception {
        // Create the BaseMVA with an existing ID
        baseMVA.setId(1L);
        BaseMVADTO baseMVADTO = baseMVAMapper.toDto(baseMVA);

        int databaseSizeBeforeCreate = baseMVARepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBaseMVAMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(baseMVADTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaseMVA in the database
        List<BaseMVA> baseMVAList = baseMVARepository.findAll();
        assertThat(baseMVAList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBaseMVAS() throws Exception {
        // Initialize the database
        baseMVARepository.saveAndFlush(baseMVA);

        // Get all the baseMVAList
        restBaseMVAMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(baseMVA.getId().intValue())))
            .andExpect(jsonPath("$.[*].baseMva").value(hasItem(DEFAULT_BASE_MVA.doubleValue())));
    }

    @Test
    @Transactional
    void getBaseMVA() throws Exception {
        // Initialize the database
        baseMVARepository.saveAndFlush(baseMVA);

        // Get the baseMVA
        restBaseMVAMockMvc
            .perform(get(ENTITY_API_URL_ID, baseMVA.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(baseMVA.getId().intValue()))
            .andExpect(jsonPath("$.baseMva").value(DEFAULT_BASE_MVA.doubleValue()));
    }

    @Test
    @Transactional
    void getBaseMVASByIdFiltering() throws Exception {
        // Initialize the database
        baseMVARepository.saveAndFlush(baseMVA);

        Long id = baseMVA.getId();

        defaultBaseMVAShouldBeFound("id.equals=" + id);
        defaultBaseMVAShouldNotBeFound("id.notEquals=" + id);

        defaultBaseMVAShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBaseMVAShouldNotBeFound("id.greaterThan=" + id);

        defaultBaseMVAShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBaseMVAShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBaseMVASByBaseMvaIsEqualToSomething() throws Exception {
        // Initialize the database
        baseMVARepository.saveAndFlush(baseMVA);

        // Get all the baseMVAList where baseMva equals to DEFAULT_BASE_MVA
        defaultBaseMVAShouldBeFound("baseMva.equals=" + DEFAULT_BASE_MVA);

        // Get all the baseMVAList where baseMva equals to UPDATED_BASE_MVA
        defaultBaseMVAShouldNotBeFound("baseMva.equals=" + UPDATED_BASE_MVA);
    }

    @Test
    @Transactional
    void getAllBaseMVASByBaseMvaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        baseMVARepository.saveAndFlush(baseMVA);

        // Get all the baseMVAList where baseMva not equals to DEFAULT_BASE_MVA
        defaultBaseMVAShouldNotBeFound("baseMva.notEquals=" + DEFAULT_BASE_MVA);

        // Get all the baseMVAList where baseMva not equals to UPDATED_BASE_MVA
        defaultBaseMVAShouldBeFound("baseMva.notEquals=" + UPDATED_BASE_MVA);
    }

    @Test
    @Transactional
    void getAllBaseMVASByBaseMvaIsInShouldWork() throws Exception {
        // Initialize the database
        baseMVARepository.saveAndFlush(baseMVA);

        // Get all the baseMVAList where baseMva in DEFAULT_BASE_MVA or UPDATED_BASE_MVA
        defaultBaseMVAShouldBeFound("baseMva.in=" + DEFAULT_BASE_MVA + "," + UPDATED_BASE_MVA);

        // Get all the baseMVAList where baseMva equals to UPDATED_BASE_MVA
        defaultBaseMVAShouldNotBeFound("baseMva.in=" + UPDATED_BASE_MVA);
    }

    @Test
    @Transactional
    void getAllBaseMVASByBaseMvaIsNullOrNotNull() throws Exception {
        // Initialize the database
        baseMVARepository.saveAndFlush(baseMVA);

        // Get all the baseMVAList where baseMva is not null
        defaultBaseMVAShouldBeFound("baseMva.specified=true");

        // Get all the baseMVAList where baseMva is null
        defaultBaseMVAShouldNotBeFound("baseMva.specified=false");
    }

    @Test
    @Transactional
    void getAllBaseMVASByBaseMvaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        baseMVARepository.saveAndFlush(baseMVA);

        // Get all the baseMVAList where baseMva is greater than or equal to DEFAULT_BASE_MVA
        defaultBaseMVAShouldBeFound("baseMva.greaterThanOrEqual=" + DEFAULT_BASE_MVA);

        // Get all the baseMVAList where baseMva is greater than or equal to UPDATED_BASE_MVA
        defaultBaseMVAShouldNotBeFound("baseMva.greaterThanOrEqual=" + UPDATED_BASE_MVA);
    }

    @Test
    @Transactional
    void getAllBaseMVASByBaseMvaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        baseMVARepository.saveAndFlush(baseMVA);

        // Get all the baseMVAList where baseMva is less than or equal to DEFAULT_BASE_MVA
        defaultBaseMVAShouldBeFound("baseMva.lessThanOrEqual=" + DEFAULT_BASE_MVA);

        // Get all the baseMVAList where baseMva is less than or equal to SMALLER_BASE_MVA
        defaultBaseMVAShouldNotBeFound("baseMva.lessThanOrEqual=" + SMALLER_BASE_MVA);
    }

    @Test
    @Transactional
    void getAllBaseMVASByBaseMvaIsLessThanSomething() throws Exception {
        // Initialize the database
        baseMVARepository.saveAndFlush(baseMVA);

        // Get all the baseMVAList where baseMva is less than DEFAULT_BASE_MVA
        defaultBaseMVAShouldNotBeFound("baseMva.lessThan=" + DEFAULT_BASE_MVA);

        // Get all the baseMVAList where baseMva is less than UPDATED_BASE_MVA
        defaultBaseMVAShouldBeFound("baseMva.lessThan=" + UPDATED_BASE_MVA);
    }

    @Test
    @Transactional
    void getAllBaseMVASByBaseMvaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        baseMVARepository.saveAndFlush(baseMVA);

        // Get all the baseMVAList where baseMva is greater than DEFAULT_BASE_MVA
        defaultBaseMVAShouldNotBeFound("baseMva.greaterThan=" + DEFAULT_BASE_MVA);

        // Get all the baseMVAList where baseMva is greater than SMALLER_BASE_MVA
        defaultBaseMVAShouldBeFound("baseMva.greaterThan=" + SMALLER_BASE_MVA);
    }

    @Test
    @Transactional
    void getAllBaseMVASByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        baseMVARepository.saveAndFlush(baseMVA);
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
        baseMVA.setNetwork(network);
        baseMVARepository.saveAndFlush(baseMVA);
        Long networkId = network.getId();

        // Get all the baseMVAList where network equals to networkId
        defaultBaseMVAShouldBeFound("networkId.equals=" + networkId);

        // Get all the baseMVAList where network equals to (networkId + 1)
        defaultBaseMVAShouldNotBeFound("networkId.equals=" + (networkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBaseMVAShouldBeFound(String filter) throws Exception {
        restBaseMVAMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(baseMVA.getId().intValue())))
            .andExpect(jsonPath("$.[*].baseMva").value(hasItem(DEFAULT_BASE_MVA.doubleValue())));

        // Check, that the count call also returns 1
        restBaseMVAMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBaseMVAShouldNotBeFound(String filter) throws Exception {
        restBaseMVAMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBaseMVAMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBaseMVA() throws Exception {
        // Get the baseMVA
        restBaseMVAMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBaseMVA() throws Exception {
        // Initialize the database
        baseMVARepository.saveAndFlush(baseMVA);

        int databaseSizeBeforeUpdate = baseMVARepository.findAll().size();

        // Update the baseMVA
        BaseMVA updatedBaseMVA = baseMVARepository.findById(baseMVA.getId()).get();
        // Disconnect from session so that the updates on updatedBaseMVA are not directly saved in db
        em.detach(updatedBaseMVA);
        updatedBaseMVA.baseMva(UPDATED_BASE_MVA);
        BaseMVADTO baseMVADTO = baseMVAMapper.toDto(updatedBaseMVA);

        restBaseMVAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, baseMVADTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(baseMVADTO))
            )
            .andExpect(status().isOk());

        // Validate the BaseMVA in the database
        List<BaseMVA> baseMVAList = baseMVARepository.findAll();
        assertThat(baseMVAList).hasSize(databaseSizeBeforeUpdate);
        BaseMVA testBaseMVA = baseMVAList.get(baseMVAList.size() - 1);
        assertThat(testBaseMVA.getBaseMva()).isEqualTo(UPDATED_BASE_MVA);
    }

    @Test
    @Transactional
    void putNonExistingBaseMVA() throws Exception {
        int databaseSizeBeforeUpdate = baseMVARepository.findAll().size();
        baseMVA.setId(count.incrementAndGet());

        // Create the BaseMVA
        BaseMVADTO baseMVADTO = baseMVAMapper.toDto(baseMVA);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBaseMVAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, baseMVADTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(baseMVADTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaseMVA in the database
        List<BaseMVA> baseMVAList = baseMVARepository.findAll();
        assertThat(baseMVAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBaseMVA() throws Exception {
        int databaseSizeBeforeUpdate = baseMVARepository.findAll().size();
        baseMVA.setId(count.incrementAndGet());

        // Create the BaseMVA
        BaseMVADTO baseMVADTO = baseMVAMapper.toDto(baseMVA);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaseMVAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(baseMVADTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaseMVA in the database
        List<BaseMVA> baseMVAList = baseMVARepository.findAll();
        assertThat(baseMVAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBaseMVA() throws Exception {
        int databaseSizeBeforeUpdate = baseMVARepository.findAll().size();
        baseMVA.setId(count.incrementAndGet());

        // Create the BaseMVA
        BaseMVADTO baseMVADTO = baseMVAMapper.toDto(baseMVA);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaseMVAMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(baseMVADTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BaseMVA in the database
        List<BaseMVA> baseMVAList = baseMVARepository.findAll();
        assertThat(baseMVAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBaseMVAWithPatch() throws Exception {
        // Initialize the database
        baseMVARepository.saveAndFlush(baseMVA);

        int databaseSizeBeforeUpdate = baseMVARepository.findAll().size();

        // Update the baseMVA using partial update
        BaseMVA partialUpdatedBaseMVA = new BaseMVA();
        partialUpdatedBaseMVA.setId(baseMVA.getId());

        restBaseMVAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBaseMVA.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBaseMVA))
            )
            .andExpect(status().isOk());

        // Validate the BaseMVA in the database
        List<BaseMVA> baseMVAList = baseMVARepository.findAll();
        assertThat(baseMVAList).hasSize(databaseSizeBeforeUpdate);
        BaseMVA testBaseMVA = baseMVAList.get(baseMVAList.size() - 1);
        assertThat(testBaseMVA.getBaseMva()).isEqualTo(DEFAULT_BASE_MVA);
    }

    @Test
    @Transactional
    void fullUpdateBaseMVAWithPatch() throws Exception {
        // Initialize the database
        baseMVARepository.saveAndFlush(baseMVA);

        int databaseSizeBeforeUpdate = baseMVARepository.findAll().size();

        // Update the baseMVA using partial update
        BaseMVA partialUpdatedBaseMVA = new BaseMVA();
        partialUpdatedBaseMVA.setId(baseMVA.getId());

        partialUpdatedBaseMVA.baseMva(UPDATED_BASE_MVA);

        restBaseMVAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBaseMVA.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBaseMVA))
            )
            .andExpect(status().isOk());

        // Validate the BaseMVA in the database
        List<BaseMVA> baseMVAList = baseMVARepository.findAll();
        assertThat(baseMVAList).hasSize(databaseSizeBeforeUpdate);
        BaseMVA testBaseMVA = baseMVAList.get(baseMVAList.size() - 1);
        assertThat(testBaseMVA.getBaseMva()).isEqualTo(UPDATED_BASE_MVA);
    }

    @Test
    @Transactional
    void patchNonExistingBaseMVA() throws Exception {
        int databaseSizeBeforeUpdate = baseMVARepository.findAll().size();
        baseMVA.setId(count.incrementAndGet());

        // Create the BaseMVA
        BaseMVADTO baseMVADTO = baseMVAMapper.toDto(baseMVA);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBaseMVAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, baseMVADTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(baseMVADTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaseMVA in the database
        List<BaseMVA> baseMVAList = baseMVARepository.findAll();
        assertThat(baseMVAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBaseMVA() throws Exception {
        int databaseSizeBeforeUpdate = baseMVARepository.findAll().size();
        baseMVA.setId(count.incrementAndGet());

        // Create the BaseMVA
        BaseMVADTO baseMVADTO = baseMVAMapper.toDto(baseMVA);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaseMVAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(baseMVADTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaseMVA in the database
        List<BaseMVA> baseMVAList = baseMVARepository.findAll();
        assertThat(baseMVAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBaseMVA() throws Exception {
        int databaseSizeBeforeUpdate = baseMVARepository.findAll().size();
        baseMVA.setId(count.incrementAndGet());

        // Create the BaseMVA
        BaseMVADTO baseMVADTO = baseMVAMapper.toDto(baseMVA);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaseMVAMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(baseMVADTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BaseMVA in the database
        List<BaseMVA> baseMVAList = baseMVARepository.findAll();
        assertThat(baseMVAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBaseMVA() throws Exception {
        // Initialize the database
        baseMVARepository.saveAndFlush(baseMVA);

        int databaseSizeBeforeDelete = baseMVARepository.findAll().size();

        // Delete the baseMVA
        restBaseMVAMockMvc
            .perform(delete(ENTITY_API_URL_ID, baseMVA.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BaseMVA> baseMVAList = baseMVARepository.findAll();
        assertThat(baseMVAList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
