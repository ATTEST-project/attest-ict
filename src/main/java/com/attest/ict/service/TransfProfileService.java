package com.attest.ict.service;

import com.attest.ict.service.dto.TransfProfileDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.TransfProfile}.
 */
public interface TransfProfileService {
    /**
     * Save a transfProfile.
     *
     * @param transfProfileDTO the entity to save.
     * @return the persisted entity.
     */
    TransfProfileDTO save(TransfProfileDTO transfProfileDTO);

    /**
     * Partially updates a transfProfile.
     *
     * @param transfProfileDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransfProfileDTO> partialUpdate(TransfProfileDTO transfProfileDTO);

    /**
     * Get all the transfProfiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransfProfileDTO> findAll(Pageable pageable);

    /**
     * Get the "id" transfProfile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransfProfileDTO> findOne(Long id);

    /**
     * Delete the "id" transfProfile.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
