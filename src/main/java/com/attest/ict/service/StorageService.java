package com.attest.ict.service;

import com.attest.ict.domain.Storage;
import com.attest.ict.service.dto.StorageDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.Storage}.
 */
public interface StorageService {
    /**
     * Save a storage.
     *
     * @param storageDTO the entity to save.
     * @return the persisted entity.
     */
    StorageDTO save(StorageDTO storageDTO);

    /**
     * Partially updates a storage.
     *
     * @param storageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StorageDTO> partialUpdate(StorageDTO storageDTO);

    /**
     * Get all the storages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StorageDTO> findAll(Pageable pageable);

    /**
     * Get the "id" storage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StorageDTO> findOne(Long id);

    /**
     * Delete the "id" storage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    //== Start Custom Methods
    List<Storage> getStoragesByNetworkId(Long networkId);
}
