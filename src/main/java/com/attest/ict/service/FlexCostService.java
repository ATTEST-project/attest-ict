package com.attest.ict.service;

import com.attest.ict.service.dto.FlexCostDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.FlexCost}.
 */
public interface FlexCostService {
    /**
     * Save a flexCost.
     *
     * @param flexCostDTO the entity to save.
     * @return the persisted entity.
     */
    FlexCostDTO save(FlexCostDTO flexCostDTO);

    /**
     * Partially updates a flexCost.
     *
     * @param flexCostDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FlexCostDTO> partialUpdate(FlexCostDTO flexCostDTO);

    /**
     * Get all the flexCosts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FlexCostDTO> findAll(Pageable pageable);

    /**
     * Get the "id" flexCost.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FlexCostDTO> findOne(Long id);

    /**
     * Delete the "id" flexCost.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
