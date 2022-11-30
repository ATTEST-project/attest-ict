package com.attest.ict.service;

import com.attest.ict.service.dto.WindDataDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.WindData}.
 */
public interface WindDataService {
    /**
     * Save a windData.
     *
     * @param windDataDTO the entity to save.
     * @return the persisted entity.
     */
    WindDataDTO save(WindDataDTO windDataDTO);

    /**
     * Partially updates a windData.
     *
     * @param windDataDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WindDataDTO> partialUpdate(WindDataDTO windDataDTO);

    /**
     * Get all the windData.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WindDataDTO> findAll(Pageable pageable);

    /**
     * Get the "id" windData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WindDataDTO> findOne(Long id);

    /**
     * Delete the "id" windData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
