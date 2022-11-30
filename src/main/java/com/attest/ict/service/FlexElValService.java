package com.attest.ict.service;

import com.attest.ict.service.dto.FlexElValDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.FlexElVal}.
 */
public interface FlexElValService {
    /**
     * Save a flexElVal.
     *
     * @param flexElValDTO the entity to save.
     * @return the persisted entity.
     */
    FlexElValDTO save(FlexElValDTO flexElValDTO);

    /**
     * Partially updates a flexElVal.
     *
     * @param flexElValDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FlexElValDTO> partialUpdate(FlexElValDTO flexElValDTO);

    /**
     * Get all the flexElVals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FlexElValDTO> findAll(Pageable pageable);

    /**
     * Get the "id" flexElVal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FlexElValDTO> findOne(Long id);

    /**
     * Delete the "id" flexElVal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
