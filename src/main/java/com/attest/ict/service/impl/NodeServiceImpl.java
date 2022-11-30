package com.attest.ict.service.impl;

import com.attest.ict.domain.Node;
import com.attest.ict.repository.NodeRepository;
import com.attest.ict.service.NodeService;
import com.attest.ict.service.dto.NodeDTO;
import com.attest.ict.service.mapper.NodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Node}.
 */
@Service
@Transactional
public class NodeServiceImpl implements NodeService {

    private final Logger log = LoggerFactory.getLogger(NodeServiceImpl.class);

    private final NodeRepository nodeRepository;

    private final NodeMapper nodeMapper;

    public NodeServiceImpl(NodeRepository nodeRepository, NodeMapper nodeMapper) {
        this.nodeRepository = nodeRepository;
        this.nodeMapper = nodeMapper;
    }

    @Override
    public NodeDTO save(NodeDTO nodeDTO) {
        log.debug("Request to save Node : {}", nodeDTO);
        Node node = nodeMapper.toEntity(nodeDTO);
        node = nodeRepository.save(node);
        return nodeMapper.toDto(node);
    }

    @Override
    public Optional<NodeDTO> partialUpdate(NodeDTO nodeDTO) {
        log.debug("Request to partially update Node : {}", nodeDTO);

        return nodeRepository
            .findById(nodeDTO.getId())
            .map(existingNode -> {
                nodeMapper.partialUpdate(existingNode, nodeDTO);

                return existingNode;
            })
            .map(nodeRepository::save)
            .map(nodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Nodes");
        return nodeRepository.findAll(pageable).map(nodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NodeDTO> findOne(Long id) {
        log.debug("Request to get Node : {}", id);
        return nodeRepository.findById(id).map(nodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Node : {}", id);
        nodeRepository.deleteById(id);
    }
}
