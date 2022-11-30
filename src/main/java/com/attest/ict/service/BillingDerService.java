package com.attest.ict.service;

import com.attest.ict.service.dto.BillingDerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.BillingDer}.
 */
public interface BillingDerService {
    /**
     * Save a billingDer.
     *
     * @param billingDerDTO the entity to save.
     * @return the persisted entity.
     */
    BillingDerDTO save(BillingDerDTO billingDerDTO);

    /**
     * Partially updates a billingDer.
     *
     * @param billingDerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BillingDerDTO> partialUpdate(BillingDerDTO billingDerDTO);

    /**
     * Get all the billingDers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BillingDerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" billingDer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BillingDerDTO> findOne(Long id);

    /**
     * Delete the "id" billingDer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
