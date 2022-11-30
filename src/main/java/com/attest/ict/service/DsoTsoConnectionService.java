package com.attest.ict.service;

import com.attest.ict.service.dto.DsoTsoConnectionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.DsoTsoConnection}.
 */
public interface DsoTsoConnectionService {
    /**
     * Save a dsoTsoConnection.
     *
     * @param dsoTsoConnectionDTO the entity to save.
     * @return the persisted entity.
     */
    DsoTsoConnectionDTO save(DsoTsoConnectionDTO dsoTsoConnectionDTO);

    /**
     * Partially updates a dsoTsoConnection.
     *
     * @param dsoTsoConnectionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DsoTsoConnectionDTO> partialUpdate(DsoTsoConnectionDTO dsoTsoConnectionDTO);

    /**
     * Get all the dsoTsoConnections.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DsoTsoConnectionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" dsoTsoConnection.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DsoTsoConnectionDTO> findOne(Long id);

    /**
     * Delete the "id" dsoTsoConnection.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
