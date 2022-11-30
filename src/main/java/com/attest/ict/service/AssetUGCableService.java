package com.attest.ict.service;

import com.attest.ict.service.dto.AssetUGCableDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.AssetUGCable}.
 */
public interface AssetUGCableService {
    /**
     * Save a assetUGCable.
     *
     * @param assetUGCableDTO the entity to save.
     * @return the persisted entity.
     */
    AssetUGCableDTO save(AssetUGCableDTO assetUGCableDTO);

    /**
     * Partially updates a assetUGCable.
     *
     * @param assetUGCableDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AssetUGCableDTO> partialUpdate(AssetUGCableDTO assetUGCableDTO);

    /**
     * Get all the assetUGCables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssetUGCableDTO> findAll(Pageable pageable);

    /**
     * Get the "id" assetUGCable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssetUGCableDTO> findOne(Long id);

    /**
     * Delete the "id" assetUGCable.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
