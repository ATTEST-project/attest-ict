package com.attest.ict.service;

import com.attest.ict.service.dto.ToolParameterDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.ToolParameter}.
 */
public interface ToolParameterService {
    /**
     * Save a toolParameter.
     *
     * @param toolParameterDTO the entity to save.
     * @return the persisted entity.
     */
    ToolParameterDTO save(ToolParameterDTO toolParameterDTO);

    /**
     * Partially updates a toolParameter.
     *
     * @param toolParameterDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ToolParameterDTO> partialUpdate(ToolParameterDTO toolParameterDTO);

    /**
     * Get all the toolParameters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ToolParameterDTO> findAll(Pageable pageable);

    /**
     * Get the "id" toolParameter.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ToolParameterDTO> findOne(Long id);

    /**
     * Delete the "id" toolParameter.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
