package com.attest.ict.service;

import com.attest.ict.service.dto.LineCableDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.LineCable}.
 */
public interface LineCableService {
    /**
     * Save a lineCable.
     *
     * @param lineCableDTO the entity to save.
     * @return the persisted entity.
     */
    LineCableDTO save(LineCableDTO lineCableDTO);

    /**
     * Partially updates a lineCable.
     *
     * @param lineCableDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LineCableDTO> partialUpdate(LineCableDTO lineCableDTO);

    /**
     * Get all the lineCables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LineCableDTO> findAll(Pageable pageable);

    /**
     * Get the "id" lineCable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LineCableDTO> findOne(Long id);

    /**
     * Delete the "id" lineCable.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
