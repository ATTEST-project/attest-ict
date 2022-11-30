package com.attest.ict.service;

import com.attest.ict.domain.BranchExtension;
import com.attest.ict.service.dto.BranchExtensionDTO;
import com.attest.ict.service.dto.custom.BranchCustomDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.BranchExtension}.
 */
public interface BranchExtensionService {
    /**
     * Save a branchExtension.
     *
     * @param branchExtensionDTO the entity to save.
     * @return the persisted entity.
     */
    BranchExtensionDTO save(BranchExtensionDTO branchExtensionDTO);

    /**
     * Partially updates a branchExtension.
     *
     * @param branchExtensionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BranchExtensionDTO> partialUpdate(BranchExtensionDTO branchExtensionDTO);

    /**
     * Get all the branchExtensions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BranchExtensionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" branchExtension.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BranchExtensionDTO> findOne(Long id);

    /**
     * Delete the "id" branchExtension.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    //Start Custom methods
    List<BranchExtension> findByNetworkIdAndLengthGreatherThen(Long networkId, Double legthMin);

    List<BranchExtension> findByNetworkId(Long networkId);

    List<BranchCustomDTO> findLengthByNetworkId(Long networkId);

    List<BranchCustomDTO> findLengthByNetworkIdAndLengthGreatherThen(Long networkId, Double lengthMin);
}
