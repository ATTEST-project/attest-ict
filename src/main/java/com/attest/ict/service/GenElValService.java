package com.attest.ict.service;

import com.attest.ict.service.dto.GenElValDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.GenElVal}.
 */
public interface GenElValService {
    /**
     * Save a genElVal.
     *
     * @param genElValDTO the entity to save.
     * @return the persisted entity.
     */
    GenElValDTO save(GenElValDTO genElValDTO);

    /**
     * Partially updates a genElVal.
     *
     * @param genElValDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GenElValDTO> partialUpdate(GenElValDTO genElValDTO);

    /**
     * Get all the genElVals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GenElValDTO> findAll(Pageable pageable);

    /**
     * Get the "id" genElVal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GenElValDTO> findOne(Long id);

    /**
     * Delete the "id" genElVal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
