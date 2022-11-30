package com.attest.ict.service;

import com.attest.ict.domain.TopologyBus;
import com.attest.ict.service.dto.TopologyBusDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.TopologyBus}.
 */
public interface TopologyBusService {
    /**
     * Save a topologyBus.
     *
     * @param topologyBusDTO the entity to save.
     * @return the persisted entity.
     */
    TopologyBusDTO save(TopologyBusDTO topologyBusDTO);

    /**
     * Partially updates a topologyBus.
     *
     * @param topologyBusDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TopologyBusDTO> partialUpdate(TopologyBusDTO topologyBusDTO);

    /**
     * Get all the topologyBuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TopologyBusDTO> findAll(Pageable pageable);

    /**
     * Get the "id" topologyBus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TopologyBusDTO> findOne(Long id);

    /**
     * Delete the "id" topologyBus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    //======= Start Custom Methods

    void saveAllTopologyBuses(List<TopologyBus> topologyBuses);

    List<String> getPLBNames();

    List<String> getPLBNamesByNetworkId(String networkId);
}
