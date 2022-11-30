package com.attest.ict.service;

import com.attest.ict.service.dto.BranchProfileDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.BranchProfile}.
 */
public interface BranchProfileService {
    /**
     * Save a branchProfile.
     *
     * @param branchProfileDTO the entity to save.
     * @return the persisted entity.
     */
    BranchProfileDTO save(BranchProfileDTO branchProfileDTO);

    /**
     * Partially updates a branchProfile.
     *
     * @param branchProfileDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BranchProfileDTO> partialUpdate(BranchProfileDTO branchProfileDTO);

    /**
     * Get all the branchProfiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BranchProfileDTO> findAll(Pageable pageable);

    /**
     * Get the "id" branchProfile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BranchProfileDTO> findOne(Long id);

    /**
     * Delete the "id" branchProfile.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
