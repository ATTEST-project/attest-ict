package com.attest.ict.service;

import com.attest.ict.service.dto.SimulationDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Interface for managing {@link com.attest.ict.domain.Simulation}.
 */
public interface SimulationService {
    /**
     * Save a simulation.
     *
     * @param simulationDTO the entity to save.
     * @return the persisted entity.
     */
    SimulationDTO save(SimulationDTO simulationDTO);

    /**
     * Partially updates a simulation.
     *
     * @param simulationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SimulationDTO> partialUpdate(SimulationDTO simulationDTO);

    /**
     * Get all the simulations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SimulationDTO> findAll(Pageable pageable);
    /**
     * Get all the SimulationDTO where Task is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<SimulationDTO> findAllWhereTaskIsNull();

    /**
     * Get all the simulations with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SimulationDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" simulation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SimulationDTO> findOne(Long id);

    /**
     * Delete the "id" simulation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    //Custom Implementation
    @Transactional(readOnly = true)
    Optional<SimulationDTO> findByUuid(String uuidString);
}
