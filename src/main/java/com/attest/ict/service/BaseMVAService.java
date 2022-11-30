package com.attest.ict.service;

import com.attest.ict.domain.BaseMVA;
import com.attest.ict.service.dto.BaseMVADTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.BaseMVA}.
 */
public interface BaseMVAService {
    /**
     * Save a baseMVA.
     *
     * @param baseMVADTO the entity to save.
     * @return the persisted entity.
     */
    BaseMVADTO save(BaseMVADTO baseMVADTO);

    /**
     * Partially updates a baseMVA.
     *
     * @param baseMVADTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BaseMVADTO> partialUpdate(BaseMVADTO baseMVADTO);

    /**
     * Get all the baseMVAS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BaseMVADTO> findAll(Pageable pageable);

    /**
     * Get the "id" baseMVA.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BaseMVADTO> findOne(Long id);

    /**
     * Delete the "id" baseMVA.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    //======== Start Custom Methods
    List<BaseMVA> getAllBaseMVA();

    BaseMVA getBaseMVAByNetworkId(Long networkId);

    List<BaseMVA> saveAll(List<BaseMVA> list);
}
