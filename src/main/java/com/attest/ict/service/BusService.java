package com.attest.ict.service;

import com.attest.ict.domain.Bus;
import com.attest.ict.domain.Generator;
import com.attest.ict.service.dto.BusDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.Bus}.
 */
public interface BusService {
    /**
     * Save a bus.
     *
     * @param busDTO the entity to save.
     * @return the persisted entity.
     */
    BusDTO save(BusDTO busDTO);

    /**
     * Partially updates a bus.
     *
     * @param busDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BusDTO> partialUpdate(BusDTO busDTO);

    /**
     * Get all the buses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BusDTO> findAll(Pageable pageable);
    /**
     * Get all the BusDTO where BusName is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<BusDTO> findAllWhereBusNameIsNull();
    /**
     * Get all the BusDTO where BusExtension is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<BusDTO> findAllWhereBusExtensionIsNull();
    /**
     * Get all the BusDTO where BusCoordinate is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<BusDTO> findAllWhereBusCoordinateIsNull();

    /**
     * Get the "id" bus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BusDTO> findOne(Long id);

    /**
     * Delete the "id" bus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    //=== Start Custom Methods

    /**
     * Get the network's buses
     *
     * @param networkId the id of the network.
     * @return list of buses
     */
    List<Bus> getBusesByNetworkId(Long networkId);

    /**
     * Get the network's load
     *
     * @param networkId the id of the network.
     * @return list of buses with anctive and reactive power <> than 0
     */
    List<Bus> getLoadsByNetworkId(Long networkId, double minP, double minQ);

    Optional<Bus> findByBusNumAndNetworkName(Long busNum, String networkName);

    Bus findByBusNumAndNetworkId(Long busNum, Long networkId);

    List<Bus> saveAll(List<Bus> buses);
}
