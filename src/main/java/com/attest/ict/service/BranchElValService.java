package com.attest.ict.service;

import com.attest.ict.service.dto.BranchElValDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.BranchElVal}.
 */
public interface BranchElValService {
    /**
     * Save a branchElVal.
     *
     * @param branchElValDTO the entity to save.
     * @return the persisted entity.
     */
    BranchElValDTO save(BranchElValDTO branchElValDTO);

    /**
     * Partially updates a branchElVal.
     *
     * @param branchElValDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BranchElValDTO> partialUpdate(BranchElValDTO branchElValDTO);

    /**
     * Get all the branchElVals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BranchElValDTO> findAll(Pageable pageable);

    /**
     * Get the "id" branchElVal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BranchElValDTO> findOne(Long id);

    /**
     * Delete the "id" branchElVal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
