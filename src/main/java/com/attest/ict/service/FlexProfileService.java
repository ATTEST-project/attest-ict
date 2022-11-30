package com.attest.ict.service;

import com.attest.ict.service.dto.FlexProfileDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.FlexProfile}.
 */
public interface FlexProfileService {
    /**
     * Save a flexProfile.
     *
     * @param flexProfileDTO the entity to save.
     * @return the persisted entity.
     */
    FlexProfileDTO save(FlexProfileDTO flexProfileDTO);

    /**
     * Partially updates a flexProfile.
     *
     * @param flexProfileDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FlexProfileDTO> partialUpdate(FlexProfileDTO flexProfileDTO);

    /**
     * Get all the flexProfiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FlexProfileDTO> findAll(Pageable pageable);

    /**
     * Get the "id" flexProfile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FlexProfileDTO> findOne(Long id);

    /**
     * Delete the "id" flexProfile.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
