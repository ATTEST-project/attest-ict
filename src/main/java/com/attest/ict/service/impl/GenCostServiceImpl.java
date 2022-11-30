package com.attest.ict.service.impl;

import com.attest.ict.domain.GenCost;
import com.attest.ict.repository.GenCostRepository;
import com.attest.ict.service.GenCostService;
import com.attest.ict.service.dto.GenCostDTO;
import com.attest.ict.service.mapper.GenCostMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GenCost}.
 */
@Service
@Transactional
public class GenCostServiceImpl implements GenCostService {

    private final Logger log = LoggerFactory.getLogger(GenCostServiceImpl.class);

    private final GenCostRepository genCostRepository;

    private final GenCostMapper genCostMapper;

    public GenCostServiceImpl(GenCostRepository genCostRepository, GenCostMapper genCostMapper) {
        this.genCostRepository = genCostRepository;
        this.genCostMapper = genCostMapper;
    }

    @Override
    public GenCostDTO save(GenCostDTO genCostDTO) {
        log.debug("Request to save GenCost : {}", genCostDTO);
        GenCost genCost = genCostMapper.toEntity(genCostDTO);
        genCost = genCostRepository.save(genCost);
        return genCostMapper.toDto(genCost);
    }

    @Override
    public Optional<GenCostDTO> partialUpdate(GenCostDTO genCostDTO) {
        log.debug("Request to partially update GenCost : {}", genCostDTO);

        return genCostRepository
            .findById(genCostDTO.getId())
            .map(existingGenCost -> {
                genCostMapper.partialUpdate(existingGenCost, genCostDTO);

                return existingGenCost;
            })
            .map(genCostRepository::save)
            .map(genCostMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GenCostDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GenCosts");
        return genCostRepository.findAll(pageable).map(genCostMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GenCostDTO> findOne(Long id) {
        log.debug("Request to get GenCost : {}", id);
        return genCostRepository.findById(id).map(genCostMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GenCost : {}", id);
        genCostRepository.deleteById(id);
    }
}
