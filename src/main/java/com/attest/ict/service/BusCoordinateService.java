package com.attest.ict.service;

import com.attest.ict.service.dto.BusCoordinateDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.BusCoordinate}.
 */
public interface BusCoordinateService {
    /**
     * Save a busCoordinate.
     *
     * @param busCoordinateDTO the entity to save.
     * @return the persisted entity.
     */
    BusCoordinateDTO save(BusCoordinateDTO busCoordinateDTO);

    /**
     * Partially updates a busCoordinate.
     *
     * @param busCoordinateDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BusCoordinateDTO> partialUpdate(BusCoordinateDTO busCoordinateDTO);

    /**
     * Get all the busCoordinates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BusCoordinateDTO> findAll(Pageable pageable);

    /**
     * Get the "id" busCoordinate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BusCoordinateDTO> findOne(Long id);

    /**
     * Delete the "id" busCoordinate.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
