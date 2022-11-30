package com.attest.ict.service;

import com.attest.ict.service.dto.GeneratorExtensionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.GeneratorExtension}.
 */
public interface GeneratorExtensionService {
    /**
     * Save a generatorExtension.
     *
     * @param generatorExtensionDTO the entity to save.
     * @return the persisted entity.
     */
    GeneratorExtensionDTO save(GeneratorExtensionDTO generatorExtensionDTO);

    /**
     * Partially updates a generatorExtension.
     *
     * @param generatorExtensionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GeneratorExtensionDTO> partialUpdate(GeneratorExtensionDTO generatorExtensionDTO);

    /**
     * Get all the generatorExtensions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GeneratorExtensionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" generatorExtension.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GeneratorExtensionDTO> findOne(Long id);

    /**
     * Delete the "id" generatorExtension.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
