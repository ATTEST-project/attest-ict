package com.attest.ict.service;

import com.attest.ict.service.dto.GenCostDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.GenCost}.
 */
public interface GenCostService {
    /**
     * Save a genCost.
     *
     * @param genCostDTO the entity to save.
     * @return the persisted entity.
     */
    GenCostDTO save(GenCostDTO genCostDTO);

    /**
     * Partially updates a genCost.
     *
     * @param genCostDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GenCostDTO> partialUpdate(GenCostDTO genCostDTO);

    /**
     * Get all the genCosts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GenCostDTO> findAll(Pageable pageable);

    /**
     * Get the "id" genCost.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GenCostDTO> findOne(Long id);

    /**
     * Delete the "id" genCost.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
