package com.attest.ict.service;

import com.attest.ict.service.dto.GenProfileDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.GenProfile}.
 */
public interface GenProfileService {
    /**
     * Save a genProfile.
     *
     * @param genProfileDTO the entity to save.
     * @return the persisted entity.
     */
    GenProfileDTO save(GenProfileDTO genProfileDTO);

    /**
     * Partially updates a genProfile.
     *
     * @param genProfileDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GenProfileDTO> partialUpdate(GenProfileDTO genProfileDTO);

    /**
     * Get all the genProfiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GenProfileDTO> findAll(Pageable pageable);

    /**
     * Get the "id" genProfile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GenProfileDTO> findOne(Long id);

    /**
     * Delete the "id" genProfile.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
