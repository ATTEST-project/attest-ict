package com.attest.ict.service;

import com.attest.ict.service.dto.StorageCostDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.StorageCost}.
 */
public interface StorageCostService {
    /**
     * Save a storageCost.
     *
     * @param storageCostDTO the entity to save.
     * @return the persisted entity.
     */
    StorageCostDTO save(StorageCostDTO storageCostDTO);

    /**
     * Partially updates a storageCost.
     *
     * @param storageCostDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StorageCostDTO> partialUpdate(StorageCostDTO storageCostDTO);

    /**
     * Get all the storageCosts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StorageCostDTO> findAll(Pageable pageable);

    /**
     * Get the "id" storageCost.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StorageCostDTO> findOne(Long id);

    /**
     * Delete the "id" storageCost.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
