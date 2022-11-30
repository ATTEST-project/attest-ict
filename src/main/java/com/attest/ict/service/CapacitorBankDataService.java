package com.attest.ict.service;

import com.attest.ict.domain.CapacitorBankData;
import com.attest.ict.service.dto.CapacitorBankDataDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.CapacitorBankData}.
 */
public interface CapacitorBankDataService {
    /**
     * Save a capacitorBankData.
     *
     * @param capacitorBankDataDTO the entity to save.
     * @return the persisted entity.
     */
    CapacitorBankDataDTO save(CapacitorBankDataDTO capacitorBankDataDTO);

    /**
     * Partially updates a capacitorBankData.
     *
     * @param capacitorBankDataDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CapacitorBankDataDTO> partialUpdate(CapacitorBankDataDTO capacitorBankDataDTO);

    /**
     * Get all the capacitorBankData.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CapacitorBankDataDTO> findAll(Pageable pageable);

    /**
     * Get the "id" capacitorBankData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CapacitorBankDataDTO> findOne(Long id);

    /**
     * Delete the "id" capacitorBankData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    //=== Start Custom Methods
    List<CapacitorBankData> getAllCapacitors();

    List<CapacitorBankData> getCapacitorsByNetworkId(Long networkId);
}
