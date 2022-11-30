package com.attest.ict.service.impl;

import com.attest.ict.domain.ProtectionTool;
import com.attest.ict.repository.ProtectionToolRepository;
import com.attest.ict.service.ProtectionToolService;
import com.attest.ict.service.dto.ProtectionToolDTO;
import com.attest.ict.service.mapper.ProtectionToolMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProtectionTool}.
 */
@Service
@Transactional
public class ProtectionToolServiceImpl implements ProtectionToolService {

    private final Logger log = LoggerFactory.getLogger(ProtectionToolServiceImpl.class);

    private final ProtectionToolRepository protectionToolRepository;

    private final ProtectionToolMapper protectionToolMapper;

    public ProtectionToolServiceImpl(ProtectionToolRepository protectionToolRepository, ProtectionToolMapper protectionToolMapper) {
        this.protectionToolRepository = protectionToolRepository;
        this.protectionToolMapper = protectionToolMapper;
    }

    @Override
    public ProtectionToolDTO save(ProtectionToolDTO protectionToolDTO) {
        log.debug("Request to save ProtectionTool : {}", protectionToolDTO);
        ProtectionTool protectionTool = protectionToolMapper.toEntity(protectionToolDTO);
        protectionTool = protectionToolRepository.save(protectionTool);
        return protectionToolMapper.toDto(protectionTool);
    }

    @Override
    public Optional<ProtectionToolDTO> partialUpdate(ProtectionToolDTO protectionToolDTO) {
        log.debug("Request to partially update ProtectionTool : {}", protectionToolDTO);

        return protectionToolRepository
            .findById(protectionToolDTO.getId())
            .map(existingProtectionTool -> {
                protectionToolMapper.partialUpdate(existingProtectionTool, protectionToolDTO);

                return existingProtectionTool;
            })
            .map(protectionToolRepository::save)
            .map(protectionToolMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProtectionToolDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProtectionTools");
        return protectionToolRepository.findAll(pageable).map(protectionToolMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProtectionToolDTO> findOne(Long id) {
        log.debug("Request to get ProtectionTool : {}", id);
        return protectionToolRepository.findById(id).map(protectionToolMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProtectionTool : {}", id);
        protectionToolRepository.deleteById(id);
    }

    //====== Start Custom Methods

    @Override
    public void saveAllProtectionTools(List<ProtectionTool> protectionTools) {
        protectionToolRepository.saveAll(protectionTools);
    }
}
