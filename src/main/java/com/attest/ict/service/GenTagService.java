package com.attest.ict.service;

import com.attest.ict.service.dto.GenTagDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.GenTag}.
 */
public interface GenTagService {
    /**
     * Save a genTag.
     *
     * @param genTagDTO the entity to save.
     * @return the persisted entity.
     */
    GenTagDTO save(GenTagDTO genTagDTO);

    /**
     * Partially updates a genTag.
     *
     * @param genTagDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GenTagDTO> partialUpdate(GenTagDTO genTagDTO);

    /**
     * Get all the genTags.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GenTagDTO> findAll(Pageable pageable);

    /**
     * Get the "id" genTag.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GenTagDTO> findOne(Long id);

    /**
     * Delete the "id" genTag.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
