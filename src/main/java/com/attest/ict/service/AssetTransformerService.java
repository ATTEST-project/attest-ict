package com.attest.ict.service;

import com.attest.ict.service.dto.AssetTransformerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.AssetTransformer}.
 */
public interface AssetTransformerService {
    /**
     * Save a assetTransformer.
     *
     * @param assetTransformerDTO the entity to save.
     * @return the persisted entity.
     */
    AssetTransformerDTO save(AssetTransformerDTO assetTransformerDTO);

    /**
     * Partially updates a assetTransformer.
     *
     * @param assetTransformerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AssetTransformerDTO> partialUpdate(AssetTransformerDTO assetTransformerDTO);

    /**
     * Get all the assetTransformers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssetTransformerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" assetTransformer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssetTransformerDTO> findOne(Long id);

    /**
     * Delete the "id" assetTransformer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
