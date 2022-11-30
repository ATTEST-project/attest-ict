package com.attest.ict.service;

import com.attest.ict.domain.Transformer;
import com.attest.ict.service.dto.TransformerDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.Transformer}.
 */
public interface TransformerService {
    /**
     * Save a transformer.
     *
     * @param transformerDTO the entity to save.
     * @return the persisted entity.
     */
    TransformerDTO save(TransformerDTO transformerDTO);

    /**
     * Partially updates a transformer.
     *
     * @param transformerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransformerDTO> partialUpdate(TransformerDTO transformerDTO);

    /**
     * Get all the transformers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransformerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" transformer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransformerDTO> findOne(Long id);

    /**
     * Delete the "id" transformer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
