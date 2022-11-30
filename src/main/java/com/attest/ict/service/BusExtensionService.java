package com.attest.ict.service;

import com.attest.ict.service.dto.BusExtensionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.BusExtension}.
 */
public interface BusExtensionService {
    /**
     * Save a busExtension.
     *
     * @param busExtensionDTO the entity to save.
     * @return the persisted entity.
     */
    BusExtensionDTO save(BusExtensionDTO busExtensionDTO);

    /**
     * Partially updates a busExtension.
     *
     * @param busExtensionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BusExtensionDTO> partialUpdate(BusExtensionDTO busExtensionDTO);

    /**
     * Get all the busExtensions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BusExtensionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" busExtension.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BusExtensionDTO> findOne(Long id);

    /**
     * Delete the "id" busExtension.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
