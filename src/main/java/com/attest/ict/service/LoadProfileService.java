package com.attest.ict.service;

import com.attest.ict.service.dto.LoadProfileDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.LoadProfile}.
 */
public interface LoadProfileService {
    /**
     * Save a loadProfile.
     *
     * @param loadProfileDTO the entity to save.
     * @return the persisted entity.
     */
    LoadProfileDTO save(LoadProfileDTO loadProfileDTO);

    /**
     * Partially updates a loadProfile.
     *
     * @param loadProfileDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LoadProfileDTO> partialUpdate(LoadProfileDTO loadProfileDTO);

    /**
     * Get all the loadProfiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LoadProfileDTO> findAll(Pageable pageable);

    /**
     * Get the "id" loadProfile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LoadProfileDTO> findOne(Long id);

    /**
     * Delete the "id" loadProfile.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
