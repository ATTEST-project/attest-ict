package com.attest.ict.service;

import com.attest.ict.domain.ProtectionTool;
import com.attest.ict.service.dto.ProtectionToolDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.ProtectionTool}.
 */
public interface ProtectionToolService {
    /**
     * Save a protectionTool.
     *
     * @param protectionToolDTO the entity to save.
     * @return the persisted entity.
     */
    ProtectionToolDTO save(ProtectionToolDTO protectionToolDTO);

    /**
     * Partially updates a protectionTool.
     *
     * @param protectionToolDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProtectionToolDTO> partialUpdate(ProtectionToolDTO protectionToolDTO);

    /**
     * Get all the protectionTools.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProtectionToolDTO> findAll(Pageable pageable);

    /**
     * Get the "id" protectionTool.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProtectionToolDTO> findOne(Long id);

    /**
     * Delete the "id" protectionTool.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    //Start Custom Methods
    void saveAllProtectionTools(List<ProtectionTool> protectionTools);
}
