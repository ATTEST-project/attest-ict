package com.attest.ict.service;

import com.attest.ict.service.dto.TransfElValDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.TransfElVal}.
 */
public interface TransfElValService {
    /**
     * Save a transfElVal.
     *
     * @param transfElValDTO the entity to save.
     * @return the persisted entity.
     */
    TransfElValDTO save(TransfElValDTO transfElValDTO);

    /**
     * Partially updates a transfElVal.
     *
     * @param transfElValDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransfElValDTO> partialUpdate(TransfElValDTO transfElValDTO);

    /**
     * Get all the transfElVals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransfElValDTO> findAll(Pageable pageable);

    /**
     * Get the "id" transfElVal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransfElValDTO> findOne(Long id);

    /**
     * Delete the "id" transfElVal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
