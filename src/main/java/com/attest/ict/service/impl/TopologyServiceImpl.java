package com.attest.ict.service.impl;

import com.attest.ict.domain.Topology;
import com.attest.ict.repository.TopologyRepository;
import com.attest.ict.service.TopologyService;
import com.attest.ict.service.dto.TopologyDTO;
import com.attest.ict.service.mapper.TopologyMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Topology}.
 */
@Service
@Transactional
public class TopologyServiceImpl implements TopologyService {

    private final Logger log = LoggerFactory.getLogger(TopologyServiceImpl.class);

    private final TopologyRepository topologyRepository;

    private final TopologyMapper topologyMapper;

    public TopologyServiceImpl(TopologyRepository topologyRepository, TopologyMapper topologyMapper) {
        this.topologyRepository = topologyRepository;
        this.topologyMapper = topologyMapper;
    }

    @Override
    public TopologyDTO save(TopologyDTO topologyDTO) {
        log.debug("Request to save Topology : {}", topologyDTO);
        Topology topology = topologyMapper.toEntity(topologyDTO);
        topology = topologyRepository.save(topology);
        return topologyMapper.toDto(topology);
    }

    @Override
    public Optional<TopologyDTO> partialUpdate(TopologyDTO topologyDTO) {
        log.debug("Request to partially update Topology : {}", topologyDTO);

        return topologyRepository
            .findById(topologyDTO.getId())
            .map(existingTopology -> {
                topologyMapper.partialUpdate(existingTopology, topologyDTO);

                return existingTopology;
            })
            .map(topologyRepository::save)
            .map(topologyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TopologyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Topologies");
        return topologyRepository.findAll(pageable).map(topologyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TopologyDTO> findOne(Long id) {
        log.debug("Request to get Topology : {}", id);
        return topologyRepository.findById(id).map(topologyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Topology : {}", id);
        topologyRepository.deleteById(id);
    }

    @Override
    public void saveAllTopologies(List<Topology> topologies) {
        topologyRepository.saveAll(topologies);
    }

    @Override
    public List<String> getPointsOfPLB(String plb) {
        List<String> points = topologyRepository.getPointsOfPLB(plb);
        return points;
    }
}
