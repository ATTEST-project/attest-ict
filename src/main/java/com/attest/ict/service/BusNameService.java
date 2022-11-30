package com.attest.ict.service;

import com.attest.ict.service.dto.BusNameDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.BusName}.
 */
public interface BusNameService {
    /**
     * Save a busName.
     *
     * @param busNameDTO the entity to save.
     * @return the persisted entity.
     */
    BusNameDTO save(BusNameDTO busNameDTO);

    /**
     * Partially updates a busName.
     *
     * @param busNameDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BusNameDTO> partialUpdate(BusNameDTO busNameDTO);

    /**
     * Get all the busNames.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BusNameDTO> findAll(Pageable pageable);

    /**
     * Get the "id" busName.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BusNameDTO> findOne(Long id);

    /**
     * Delete the "id" busName.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
