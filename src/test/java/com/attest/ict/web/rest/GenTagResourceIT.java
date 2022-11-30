package com.attest.ict.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.attest.ict.IntegrationTest;
import com.attest.ict.domain.GenTag;
import com.attest.ict.domain.Generator;
import com.attest.ict.repository.GenTagRepository;
import com.attest.ict.service.criteria.GenTagCriteria;
import com.attest.ict.service.dto.GenTagDTO;
import com.attest.ict.service.mapper.GenTagMapper;
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
 * Integration tests for the {@link GenTagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GenTagResourceIT {

    private static final String DEFAULT_GEN_TAG = "AAAAAAAAAA";
    private static final String UPDATED_GEN_TAG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/gen-tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GenTagRepository genTagRepository;

    @Autowired
    private GenTagMapper genTagMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGenTagMockMvc;

    private GenTag genTag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GenTag createEntity(EntityManager em) {
        GenTag genTag = new GenTag().genTag(DEFAULT_GEN_TAG);
        return genTag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GenTag createUpdatedEntity(EntityManager em) {
        GenTag genTag = new GenTag().genTag(UPDATED_GEN_TAG);
        return genTag;
    }

    @BeforeEach
    public void initTest() {
        genTag = createEntity(em);
    }

    @Test
    @Transactional
    void createGenTag() throws Exception {
        int databaseSizeBeforeCreate = genTagRepository.findAll().size();
        // Create the GenTag
        GenTagDTO genTagDTO = genTagMapper.toDto(genTag);
        restGenTagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genTagDTO))
            )
            .andExpect(status().isCreated());

        // Validate the GenTag in the database
        List<GenTag> genTagList = genTagRepository.findAll();
        assertThat(genTagList).hasSize(databaseSizeBeforeCreate + 1);
        GenTag testGenTag = genTagList.get(genTagList.size() - 1);
        assertThat(testGenTag.getGenTag()).isEqualTo(DEFAULT_GEN_TAG);
    }

    @Test
    @Transactional
    void createGenTagWithExistingId() throws Exception {
        // Create the GenTag with an existing ID
        genTag.setId(1L);
        GenTagDTO genTagDTO = genTagMapper.toDto(genTag);

        int databaseSizeBeforeCreate = genTagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGenTagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenTag in the database
        List<GenTag> genTagList = genTagRepository.findAll();
        assertThat(genTagList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGenTags() throws Exception {
        // Initialize the database
        genTagRepository.saveAndFlush(genTag);

        // Get all the genTagList
        restGenTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].genTag").value(hasItem(DEFAULT_GEN_TAG)));
    }

    @Test
    @Transactional
    void getGenTag() throws Exception {
        // Initialize the database
        genTagRepository.saveAndFlush(genTag);

        // Get the genTag
        restGenTagMockMvc
            .perform(get(ENTITY_API_URL_ID, genTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(genTag.getId().intValue()))
            .andExpect(jsonPath("$.genTag").value(DEFAULT_GEN_TAG));
    }

    @Test
    @Transactional
    void getGenTagsByIdFiltering() throws Exception {
        // Initialize the database
        genTagRepository.saveAndFlush(genTag);

        Long id = genTag.getId();

        defaultGenTagShouldBeFound("id.equals=" + id);
        defaultGenTagShouldNotBeFound("id.notEquals=" + id);

        defaultGenTagShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGenTagShouldNotBeFound("id.greaterThan=" + id);

        defaultGenTagShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGenTagShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGenTagsByGenTagIsEqualToSomething() throws Exception {
        // Initialize the database
        genTagRepository.saveAndFlush(genTag);

        // Get all the genTagList where genTag equals to DEFAULT_GEN_TAG
        defaultGenTagShouldBeFound("genTag.equals=" + DEFAULT_GEN_TAG);

        // Get all the genTagList where genTag equals to UPDATED_GEN_TAG
        defaultGenTagShouldNotBeFound("genTag.equals=" + UPDATED_GEN_TAG);
    }

    @Test
    @Transactional
    void getAllGenTagsByGenTagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genTagRepository.saveAndFlush(genTag);

        // Get all the genTagList where genTag not equals to DEFAULT_GEN_TAG
        defaultGenTagShouldNotBeFound("genTag.notEquals=" + DEFAULT_GEN_TAG);

        // Get all the genTagList where genTag not equals to UPDATED_GEN_TAG
        defaultGenTagShouldBeFound("genTag.notEquals=" + UPDATED_GEN_TAG);
    }

    @Test
    @Transactional
    void getAllGenTagsByGenTagIsInShouldWork() throws Exception {
        // Initialize the database
        genTagRepository.saveAndFlush(genTag);

        // Get all the genTagList where genTag in DEFAULT_GEN_TAG or UPDATED_GEN_TAG
        defaultGenTagShouldBeFound("genTag.in=" + DEFAULT_GEN_TAG + "," + UPDATED_GEN_TAG);

        // Get all the genTagList where genTag equals to UPDATED_GEN_TAG
        defaultGenTagShouldNotBeFound("genTag.in=" + UPDATED_GEN_TAG);
    }

    @Test
    @Transactional
    void getAllGenTagsByGenTagIsNullOrNotNull() throws Exception {
        // Initialize the database
        genTagRepository.saveAndFlush(genTag);

        // Get all the genTagList where genTag is not null
        defaultGenTagShouldBeFound("genTag.specified=true");

        // Get all the genTagList where genTag is null
        defaultGenTagShouldNotBeFound("genTag.specified=false");
    }

    @Test
    @Transactional
    void getAllGenTagsByGenTagContainsSomething() throws Exception {
        // Initialize the database
        genTagRepository.saveAndFlush(genTag);

        // Get all the genTagList where genTag contains DEFAULT_GEN_TAG
        defaultGenTagShouldBeFound("genTag.contains=" + DEFAULT_GEN_TAG);

        // Get all the genTagList where genTag contains UPDATED_GEN_TAG
        defaultGenTagShouldNotBeFound("genTag.contains=" + UPDATED_GEN_TAG);
    }

    @Test
    @Transactional
    void getAllGenTagsByGenTagNotContainsSomething() throws Exception {
        // Initialize the database
        genTagRepository.saveAndFlush(genTag);

        // Get all the genTagList where genTag does not contain DEFAULT_GEN_TAG
        defaultGenTagShouldNotBeFound("genTag.doesNotContain=" + DEFAULT_GEN_TAG);

        // Get all the genTagList where genTag does not contain UPDATED_GEN_TAG
        defaultGenTagShouldBeFound("genTag.doesNotContain=" + UPDATED_GEN_TAG);
    }

    @Test
    @Transactional
    void getAllGenTagsByGeneratorIsEqualToSomething() throws Exception {
        // Initialize the database
        genTagRepository.saveAndFlush(genTag);
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
        genTag.setGenerator(generator);
        genTagRepository.saveAndFlush(genTag);
        Long generatorId = generator.getId();

        // Get all the genTagList where generator equals to generatorId
        defaultGenTagShouldBeFound("generatorId.equals=" + generatorId);

        // Get all the genTagList where generator equals to (generatorId + 1)
        defaultGenTagShouldNotBeFound("generatorId.equals=" + (generatorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGenTagShouldBeFound(String filter) throws Exception {
        restGenTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].genTag").value(hasItem(DEFAULT_GEN_TAG)));

        // Check, that the count call also returns 1
        restGenTagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGenTagShouldNotBeFound(String filter) throws Exception {
        restGenTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGenTagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGenTag() throws Exception {
        // Get the genTag
        restGenTagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGenTag() throws Exception {
        // Initialize the database
        genTagRepository.saveAndFlush(genTag);

        int databaseSizeBeforeUpdate = genTagRepository.findAll().size();

        // Update the genTag
        GenTag updatedGenTag = genTagRepository.findById(genTag.getId()).get();
        // Disconnect from session so that the updates on updatedGenTag are not directly saved in db
        em.detach(updatedGenTag);
        updatedGenTag.genTag(UPDATED_GEN_TAG);
        GenTagDTO genTagDTO = genTagMapper.toDto(updatedGenTag);

        restGenTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, genTagDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genTagDTO))
            )
            .andExpect(status().isOk());

        // Validate the GenTag in the database
        List<GenTag> genTagList = genTagRepository.findAll();
        assertThat(genTagList).hasSize(databaseSizeBeforeUpdate);
        GenTag testGenTag = genTagList.get(genTagList.size() - 1);
        assertThat(testGenTag.getGenTag()).isEqualTo(UPDATED_GEN_TAG);
    }

    @Test
    @Transactional
    void putNonExistingGenTag() throws Exception {
        int databaseSizeBeforeUpdate = genTagRepository.findAll().size();
        genTag.setId(count.incrementAndGet());

        // Create the GenTag
        GenTagDTO genTagDTO = genTagMapper.toDto(genTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, genTagDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenTag in the database
        List<GenTag> genTagList = genTagRepository.findAll();
        assertThat(genTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGenTag() throws Exception {
        int databaseSizeBeforeUpdate = genTagRepository.findAll().size();
        genTag.setId(count.incrementAndGet());

        // Create the GenTag
        GenTagDTO genTagDTO = genTagMapper.toDto(genTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenTag in the database
        List<GenTag> genTagList = genTagRepository.findAll();
        assertThat(genTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGenTag() throws Exception {
        int databaseSizeBeforeUpdate = genTagRepository.findAll().size();
        genTag.setId(count.incrementAndGet());

        // Create the GenTag
        GenTagDTO genTagDTO = genTagMapper.toDto(genTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenTagMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genTagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GenTag in the database
        List<GenTag> genTagList = genTagRepository.findAll();
        assertThat(genTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGenTagWithPatch() throws Exception {
        // Initialize the database
        genTagRepository.saveAndFlush(genTag);

        int databaseSizeBeforeUpdate = genTagRepository.findAll().size();

        // Update the genTag using partial update
        GenTag partialUpdatedGenTag = new GenTag();
        partialUpdatedGenTag.setId(genTag.getId());

        partialUpdatedGenTag.genTag(UPDATED_GEN_TAG);

        restGenTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenTag.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenTag))
            )
            .andExpect(status().isOk());

        // Validate the GenTag in the database
        List<GenTag> genTagList = genTagRepository.findAll();
        assertThat(genTagList).hasSize(databaseSizeBeforeUpdate);
        GenTag testGenTag = genTagList.get(genTagList.size() - 1);
        assertThat(testGenTag.getGenTag()).isEqualTo(UPDATED_GEN_TAG);
    }

    @Test
    @Transactional
    void fullUpdateGenTagWithPatch() throws Exception {
        // Initialize the database
        genTagRepository.saveAndFlush(genTag);

        int databaseSizeBeforeUpdate = genTagRepository.findAll().size();

        // Update the genTag using partial update
        GenTag partialUpdatedGenTag = new GenTag();
        partialUpdatedGenTag.setId(genTag.getId());

        partialUpdatedGenTag.genTag(UPDATED_GEN_TAG);

        restGenTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenTag.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenTag))
            )
            .andExpect(status().isOk());

        // Validate the GenTag in the database
        List<GenTag> genTagList = genTagRepository.findAll();
        assertThat(genTagList).hasSize(databaseSizeBeforeUpdate);
        GenTag testGenTag = genTagList.get(genTagList.size() - 1);
        assertThat(testGenTag.getGenTag()).isEqualTo(UPDATED_GEN_TAG);
    }

    @Test
    @Transactional
    void patchNonExistingGenTag() throws Exception {
        int databaseSizeBeforeUpdate = genTagRepository.findAll().size();
        genTag.setId(count.incrementAndGet());

        // Create the GenTag
        GenTagDTO genTagDTO = genTagMapper.toDto(genTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, genTagDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenTag in the database
        List<GenTag> genTagList = genTagRepository.findAll();
        assertThat(genTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGenTag() throws Exception {
        int databaseSizeBeforeUpdate = genTagRepository.findAll().size();
        genTag.setId(count.incrementAndGet());

        // Create the GenTag
        GenTagDTO genTagDTO = genTagMapper.toDto(genTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenTag in the database
        List<GenTag> genTagList = genTagRepository.findAll();
        assertThat(genTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGenTag() throws Exception {
        int databaseSizeBeforeUpdate = genTagRepository.findAll().size();
        genTag.setId(count.incrementAndGet());

        // Create the GenTag
        GenTagDTO genTagDTO = genTagMapper.toDto(genTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenTagMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genTagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GenTag in the database
        List<GenTag> genTagList = genTagRepository.findAll();
        assertThat(genTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGenTag() throws Exception {
        // Initialize the database
        genTagRepository.saveAndFlush(genTag);

        int databaseSizeBeforeDelete = genTagRepository.findAll().size();

        // Delete the genTag
        restGenTagMockMvc
            .perform(delete(ENTITY_API_URL_ID, genTag.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GenTag> genTagList = genTagRepository.findAll();
        assertThat(genTagList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
