package com.attest.ict.service.impl;

import com.attest.ict.domain.ToolParameter;
import com.attest.ict.repository.ToolParameterRepository;
import com.attest.ict.service.ToolParameterService;
import com.attest.ict.service.dto.ToolParameterDTO;
import com.attest.ict.service.mapper.ToolParameterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ToolParameter}.
 */
@Service
@Transactional
public class ToolParameterServiceImpl implements ToolParameterService {

    private final Logger log = LoggerFactory.getLogger(ToolParameterServiceImpl.class);

    private final ToolParameterRepository toolParameterRepository;

    private final ToolParameterMapper toolParameterMapper;

    public ToolParameterServiceImpl(ToolParameterRepository toolParameterRepository, ToolParameterMapper toolParameterMapper) {
        this.toolParameterRepository = toolParameterRepository;
        this.toolParameterMapper = toolParameterMapper;
    }

    @Override
    public ToolParameterDTO save(ToolParameterDTO toolParameterDTO) {
        log.debug("Request to save ToolParameter : {}", toolParameterDTO);
        ToolParameter toolParameter = toolParameterMapper.toEntity(toolParameterDTO);
        toolParameter = toolParameterRepository.save(toolParameter);
        return toolParameterMapper.toDto(toolParameter);
    }

    @Override
    public Optional<ToolParameterDTO> partialUpdate(ToolParameterDTO toolParameterDTO) {
        log.debug("Request to partially update ToolParameter : {}", toolParameterDTO);

        return toolParameterRepository
            .findById(toolParameterDTO.getId())
            .map(existingToolParameter -> {
                toolParameterMapper.partialUpdate(existingToolParameter, toolParameterDTO);

                return existingToolParameter;
            })
            .map(toolParameterRepository::save)
            .map(toolParameterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ToolParameterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ToolParameters");
        return toolParameterRepository.findAll(pageable).map(toolParameterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ToolParameterDTO> findOne(Long id) {
        log.debug("Request to get ToolParameter : {}", id);
        return toolParameterRepository.findById(id).map(toolParameterMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ToolParameter : {}", id);
        toolParameterRepository.deleteById(id);
    }
}
