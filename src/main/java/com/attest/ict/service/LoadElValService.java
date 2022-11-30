package com.attest.ict.service;

import com.attest.ict.domain.LoadElVal;
import com.attest.ict.service.dto.LoadElValDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.LoadElVal}.
 */
public interface LoadElValService {
    /**
     * Save a loadElVal.
     *
     * @param loadElValDTO the entity to save.
     * @return the persisted entity.
     */
    LoadElValDTO save(LoadElValDTO loadElValDTO);

    /**
     * Partially updates a loadElVal.
     *
     * @param loadElValDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LoadElValDTO> partialUpdate(LoadElValDTO loadElValDTO);

    /**
     * Get all the loadElVals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LoadElValDTO> findAll(Pageable pageable);

    /**
     * Get the "id" loadElVal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LoadElValDTO> findOne(Long id);

    /**
     * Delete the "id" loadElVal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    //====== Start Custom Methods
    List<LoadElVal> getAllLoadElVals();

    List<LoadElVal> getLoadElValsByNetworkId(Long networkId);
}
