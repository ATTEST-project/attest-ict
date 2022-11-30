package com.attest.ict.service;

import com.attest.ict.service.dto.SolarDataDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.SolarData}.
 */
public interface SolarDataService {
    /**
     * Save a solarData.
     *
     * @param solarDataDTO the entity to save.
     * @return the persisted entity.
     */
    SolarDataDTO save(SolarDataDTO solarDataDTO);

    /**
     * Partially updates a solarData.
     *
     * @param solarDataDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SolarDataDTO> partialUpdate(SolarDataDTO solarDataDTO);

    /**
     * Get all the solarData.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SolarDataDTO> findAll(Pageable pageable);

    /**
     * Get the "id" solarData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SolarDataDTO> findOne(Long id);

    /**
     * Delete the "id" solarData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
