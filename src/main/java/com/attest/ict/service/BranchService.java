package com.attest.ict.service;

import com.attest.ict.domain.Branch;
import com.attest.ict.service.dto.BranchDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.Branch}.
 */
public interface BranchService {
    /**
     * Save a branch.
     *
     * @param branchDTO the entity to save.
     * @return the persisted entity.
     */
    BranchDTO save(BranchDTO branchDTO);

    /**
     * Partially updates a branch.
     *
     * @param branchDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BranchDTO> partialUpdate(BranchDTO branchDTO);

    /**
     * Get all the branches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BranchDTO> findAll(Pageable pageable);
    /**
     * Get all the BranchDTO where BranchExtension is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<BranchDTO> findAllWhereBranchExtensionIsNull();

    /**
     * Get the "id" branch.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BranchDTO> findOne(Long id);

    /**
     * Delete the "id" branch.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    //======= Start Customm Methods
    List<Branch> getAllBranches();

    List<Branch> getBranchesByNetworkId(Long networkId);

    List<Branch> saveAll(List<Branch> branches);
}
