package com.attest.ict.service;

import com.attest.ict.custom.model.projection.NetworkProjection;
import com.attest.ict.domain.Network;
import com.attest.ict.service.dto.NetworkDTO;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.Network}.
 */
public interface NetworkService {
    /**
     * Save a network.
     *
     * @param networkDTO the entity to save.
     * @return the persisted entity.
     */
    NetworkDTO save(NetworkDTO networkDTO);

    /**
     * Partially updates a network.
     *
     * @param networkDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NetworkDTO> partialUpdate(NetworkDTO networkDTO);

    /**
     * Get all the networks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NetworkDTO> findAll(Pageable pageable);
    /**
     * Get all the NetworkDTO where BaseMVA is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<NetworkDTO> findAllWhereBaseMVAIsNull();
    /**
     * Get all the NetworkDTO where VoltageLevel is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<NetworkDTO> findAllWhereVoltageLevelIsNull();

    /**
     * Get the "id" network.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NetworkDTO> findOne(Long id);

    /**
     * Delete the "id" network.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    // =======  Start Custom Method
    /**
     * Get the "networkName" networkName.
     *
     * @param networkName the name of the entity.
     * @return the entity.
     */
    Optional<NetworkDTO> findByName(String networkName);

    /**
     * Get the list of network by  networkName from startDate to endDate
     *
     * @param networkName the name of the entity.
     * @return the entity.
     */
    List<Network> findNetworkByNameBetweenDate(String networkName, Date startDate, Date endDate);

    List<Long> getNetworkId(String networkName, Date startDate, Date endDate);

    Long getNetworkIdByName(String networkName);

    List<Long> getNetworkIds();

    List<Object> getAllNetworksPlain();

    List<String> getAllNetworkNames();

    Optional<Network> findNetworkByName(String networkName);

    Optional<Network> findById(Long networkId);

    void saveNetwork_v2(Network network);

    void deleteGenTagsByNetworkId(Long networkId);

    void deleteGenCostsByNetworkId(Long networkId);

    void deleteGeneratorsByNetworkId(Long networkId);

    void deleteBranchesByNetworkId(Long networkId);

    void deleteBusNamesByNetworkId(Long networkId);

    void deleteBusCoordinatesByNetworkId(Long networkId);

    void deleteBaseMVAByNetworkId(Long networkId);

    void deleteLoadElVarsByNetworkId(Long networkId);

    void deleteBusesByNetworkId(Long networkId);

    void deleteTopologyByNetworkId(Long networkId);

    void deleteTopologyBusesByNetworkId(Long networkId);

    void deleteLoadsByNetworkId(Long networkId);

    void deleteStoragesByNetworkId(Long networkId);

    void deleteTransformersByNetworkId(Long networkId);

    void deleteCapacitorsByNetworkId(Long networkId);

    void deleteFilesByNetworkId(Long networkId);

    void deleteNDFilesByNetworkId(Long networkId);

    void deleteOutputFilesByNetworkId(Long networkId);

    void deleteNetworksById(Long networkId);

    List<Network> getAllNetworks();

    List<NetworkProjection> getNetworks();
}
