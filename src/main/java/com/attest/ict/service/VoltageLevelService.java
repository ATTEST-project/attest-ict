package com.attest.ict.service;

import com.attest.ict.domain.VoltageLevel;
import com.attest.ict.service.dto.VoltageLevelDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.VoltageLevel}.
 */
public interface VoltageLevelService {
    /**
     * Save a voltageLevel.
     *
     * @param voltageLevelDTO the entity to save.
     * @return the persisted entity.
     */
    VoltageLevelDTO save(VoltageLevelDTO voltageLevelDTO);

    /**
     * Partially updates a voltageLevel.
     *
     * @param voltageLevelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VoltageLevelDTO> partialUpdate(VoltageLevelDTO voltageLevelDTO);

    /**
     * Get all the voltageLevels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VoltageLevelDTO> findAll(Pageable pageable);

    /**
     * Get the "id" voltageLevel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VoltageLevelDTO> findOne(Long id);

    /**
     * Delete the "id" voltageLevel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    //Start Custom Methods

    //TODO remove
    List<VoltageLevel> getAllVLevels();

    VoltageLevel getVLevelsByNetworkId(Long networkId);
}
