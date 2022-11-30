package com.attest.ict.service;

import com.attest.ict.service.dto.BillingConsumptionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.BillingConsumption}.
 */
public interface BillingConsumptionService {
    /**
     * Save a billingConsumption.
     *
     * @param billingConsumptionDTO the entity to save.
     * @return the persisted entity.
     */
    BillingConsumptionDTO save(BillingConsumptionDTO billingConsumptionDTO);

    /**
     * Partially updates a billingConsumption.
     *
     * @param billingConsumptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BillingConsumptionDTO> partialUpdate(BillingConsumptionDTO billingConsumptionDTO);

    /**
     * Get all the billingConsumptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BillingConsumptionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" billingConsumption.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BillingConsumptionDTO> findOne(Long id);

    /**
     * Delete the "id" billingConsumption.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
