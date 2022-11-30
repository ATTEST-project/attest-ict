package com.attest.ict.service.impl;

import com.attest.ict.domain.TopologyBus;
import com.attest.ict.repository.TopologyBusRepository;
import com.attest.ict.service.TopologyBusService;
import com.attest.ict.service.dto.TopologyBusDTO;
import com.attest.ict.service.mapper.TopologyBusMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TopologyBus}.
 */
@Service
@Transactional
public class TopologyBusServiceImpl implements TopologyBusService {

    private final Logger log = LoggerFactory.getLogger(TopologyBusServiceImpl.class);

    private final TopologyBusRepository topologyBusRepository;

    private final TopologyBusMapper topologyBusMapper;

    public TopologyBusServiceImpl(TopologyBusRepository topologyBusRepository, TopologyBusMapper topologyBusMapper) {
        this.topologyBusRepository = topologyBusRepository;
        this.topologyBusMapper = topologyBusMapper;
    }

    @Override
    public TopologyBusDTO save(TopologyBusDTO topologyBusDTO) {
        log.debug("Request to save TopologyBus : {}", topologyBusDTO);
        TopologyBus topologyBus = topologyBusMapper.toEntity(topologyBusDTO);
        topologyBus = topologyBusRepository.save(topologyBus);
        return topologyBusMapper.toDto(topologyBus);
    }

    @Override
    public Optional<TopologyBusDTO> partialUpdate(TopologyBusDTO topologyBusDTO) {
        log.debug("Request to partially update TopologyBus : {}", topologyBusDTO);

        return topologyBusRepository
            .findById(topologyBusDTO.getId())
            .map(existingTopologyBus -> {
                topologyBusMapper.partialUpdate(existingTopologyBus, topologyBusDTO);

                return existingTopologyBus;
            })
            .map(topologyBusRepository::save)
            .map(topologyBusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TopologyBusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TopologyBuses");
        return topologyBusRepository.findAll(pageable).map(topologyBusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TopologyBusDTO> findOne(Long id) {
        log.debug("Request to get TopologyBus : {}", id);
        return topologyBusRepository.findById(id).map(topologyBusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TopologyBus : {}", id);
        topologyBusRepository.deleteById(id);
    }

    @Override
    public void saveAllTopologyBuses(List<TopologyBus> topologyBuses) {
        topologyBusRepository.saveAll(topologyBuses);
    }

    @Override
    public List<String> getPLBNames() {
        List<String> plbNames = topologyBusRepository.getPLBNames();
        return plbNames;
    }

    @Override
    public List<String> getPLBNamesByNetworkId(String networkId) {
        List<String> plbNames = topologyBusRepository.getPLBNamesByNetworkId(networkId);
        return plbNames;
    }
}
