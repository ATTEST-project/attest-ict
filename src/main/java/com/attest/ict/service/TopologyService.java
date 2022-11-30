package com.attest.ict.service;

import com.attest.ict.domain.Topology;
import com.attest.ict.service.dto.TopologyDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.Topology}.
 */
public interface TopologyService {
    /**
     * Save a topology.
     *
     * @param topologyDTO the entity to save.
     * @return the persisted entity.
     */
    TopologyDTO save(TopologyDTO topologyDTO);

    /**
     * Partially updates a topology.
     *
     * @param topologyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TopologyDTO> partialUpdate(TopologyDTO topologyDTO);

    /**
     * Get all the topologies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TopologyDTO> findAll(Pageable pageable);

    /**
     * Get the "id" topology.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TopologyDTO> findOne(Long id);

    /**
     * Delete the "id" topology.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    //======= Start Custom Methods

    void saveAllTopologies(List<Topology> topologies);

    List<String> getPointsOfPLB(String plb);
}
