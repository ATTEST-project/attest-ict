package com.attest.ict.service.impl;

import com.attest.ict.custom.model.projection.NetworkProjection;
import com.attest.ict.domain.Network;
import com.attest.ict.repository.NetworkRepository;
import com.attest.ict.service.NetworkService;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.mapper.NetworkMapper;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Network}.
 */
@Service
@Transactional
public class NetworkServiceImpl implements NetworkService {

    private final Logger log = LoggerFactory.getLogger(NetworkServiceImpl.class);

    private final NetworkRepository networkRepository;

    private final NetworkMapper networkMapper;

    public NetworkServiceImpl(NetworkRepository networkRepository, NetworkMapper networkMapper) {
        this.networkRepository = networkRepository;
        this.networkMapper = networkMapper;
    }

    @Override
    public NetworkDTO save(NetworkDTO networkDTO) {
        log.debug("Request to save Network : {}", networkDTO);
        Network network = networkMapper.toEntity(networkDTO);
        network = networkRepository.save(network);
        return networkMapper.toDto(network);
    }

    @Override
    public Optional<NetworkDTO> partialUpdate(NetworkDTO networkDTO) {
        log.debug("Request to partially update Network : {}", networkDTO);

        return networkRepository
            .findById(networkDTO.getId())
            .map(existingNetwork -> {
                networkMapper.partialUpdate(existingNetwork, networkDTO);

                return existingNetwork;
            })
            .map(networkRepository::save)
            .map(networkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NetworkDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Networks");
        return networkRepository.findAll(pageable).map(networkMapper::toDto);
    }

    /**
     *  Get all the networks where BaseMVA is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<NetworkDTO> findAllWhereBaseMVAIsNull() {
        log.debug("Request to get all networks where BaseMVA is null");
        return StreamSupport
            .stream(networkRepository.findAll().spliterator(), false)
            .filter(network -> network.getBaseMVA() == null)
            .map(networkMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the networks where VoltageLevel is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<NetworkDTO> findAllWhereVoltageLevelIsNull() {
        log.debug("Request to get all networks where VoltageLevel is null");
        return StreamSupport
            .stream(networkRepository.findAll().spliterator(), false)
            .filter(network -> network.getVoltageLevel() == null)
            .map(networkMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NetworkDTO> findOne(Long id) {
        log.debug("Request to get Network : {}", id);
        return networkRepository.findById(id).map(networkMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Network : {}", id);
        networkRepository.deleteById(id);
    }

    // =======  Start Custom Method

    @Override
    public Optional<NetworkDTO> findByName(String networkName) {
        log.debug("Request to get Network : {}", networkName);
        return networkRepository.findByName(networkName).map(networkMapper::toDto);
    }

    @Override
    public List<Network> findNetworkByNameBetweenDate(String networkName, Date startDate, Date endDate) {
        return networkRepository.findNetworksByName(networkName, startDate, endDate);
    }

    @Override
    public List<Long> getNetworkId(String networkName, Date startDate, Date endDate) {
        return networkRepository.findNetworkId(networkName, startDate, endDate);
    }

    @Override
    public Long getNetworkIdByName(String networkName) {
        return networkRepository.findNetworkIdByName(networkName);
    }

    @Override
    public List<Long> getNetworkIds() {
        return networkRepository.findNetworkIds();
    }

    @Override
    public List<Object> getAllNetworksPlain() {
        return networkRepository.findAllNetworksPlain();
    }

    @Override
    public List<String> getAllNetworkNames() {
        return networkRepository.getAllNetworkNames();
    }

    @Override
    public Optional<Network> findNetworkByName(String networkName) {
        return networkRepository.findByName(networkName);
    }

    @Override
    public Optional<Network> findById(Long networkId) {
        return networkRepository.findById(networkId);
    }

    @Override
    public void saveNetwork_v2(Network network) {
        networkRepository.save(network);
    }

    @Override
    public void deleteGenTagsByNetworkId(Long networkId) {
        networkRepository.deleteGenTagsByNetworkId(networkId);
    }

    @Override
    public void deleteGenCostsByNetworkId(Long networkId) {
        networkRepository.deleteGenCostsByNetworkId(networkId);
    }

    @Override
    public void deleteGeneratorsByNetworkId(Long networkId) {
        networkRepository.deleteGeneratorsByNetworkId(networkId);
    }

    public void deleteBranchesByNetworkId(Long networkId) {
        networkRepository.deleteBranchesByNetworkId(networkId);
    }

    public void deleteBusNamesByNetworkId(Long networkId) {
        networkRepository.deleteBusNamesByNetworkId(networkId);
    }

    public void deleteBusCoordinatesByNetworkId(Long networkId) {
        networkRepository.deleteBusCoordinatesByNetworkId(networkId);
    }

    public void deleteBaseMVAByNetworkId(Long networkId) {
        networkRepository.deleteBaseMVAByNetworkId(networkId);
    }

    public void deleteLoadElVarsByNetworkId(Long networkId) {
        networkRepository.deleteLoadElVarsByNetworkId(networkId);
    }

    public void deleteBusesByNetworkId(Long networkId) {
        networkRepository.deleteBusesByNetworkId(networkId);
    }

    public void deleteTopologyByNetworkId(Long networkId) {
        networkRepository.deleteTopologyByNetworkId(networkId);
    }

    public void deleteTopologyBusesByNetworkId(Long networkId) {
        networkRepository.deleteTopologyBusesByNetworkId(networkId);
    }

    public void deleteLoadsByNetworkId(Long networkId) {
        networkRepository.deleteLoadsByNetworkId(networkId);
    }

    public void deleteStoragesByNetworkId(Long networkId) {
        networkRepository.deleteStoragesByNetworkId(networkId);
    }

    public void deleteTransformersByNetworkId(Long networkId) {
        networkRepository.deleteTransformersByNetworkId(networkId);
    }

    public void deleteCapacitorsByNetworkId(Long networkId) {
        networkRepository.deleteCapacitorsByNetworkId(networkId);
    }

    public void deleteFilesByNetworkId(Long networkId) {
        networkRepository.deleteFilesByNetworkId(networkId);
    }

    public void deleteNDFilesByNetworkId(Long networkId) {
        networkRepository.deleteNDFilesByNetworkId(networkId);
    }

    public void deleteOutputFilesByNetworkId(Long networkId) {
        networkRepository.deleteOutputFilesByNetworkId(networkId);
    }

    public void deleteNetworksById(Long networkId) {
        networkRepository.deleteNetworksById(networkId);
    }

    public List<Network> getAllNetworks() {
        return networkRepository.findAll();
    }

    public List<NetworkProjection> getNetworks() {
        return networkRepository.getNetworksBy();
    }
}
