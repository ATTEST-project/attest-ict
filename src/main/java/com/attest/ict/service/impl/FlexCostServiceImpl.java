package com.attest.ict.service.impl;

import com.attest.ict.domain.FlexCost;
import com.attest.ict.repository.FlexCostRepository;
import com.attest.ict.service.FlexCostService;
import com.attest.ict.service.dto.FlexCostDTO;
import com.attest.ict.service.mapper.FlexCostMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FlexCost}.
 */
@Service
@Transactional
public class FlexCostServiceImpl implements FlexCostService {

    private final Logger log = LoggerFactory.getLogger(FlexCostServiceImpl.class);

    private final FlexCostRepository flexCostRepository;

    private final FlexCostMapper flexCostMapper;

    public FlexCostServiceImpl(FlexCostRepository flexCostRepository, FlexCostMapper flexCostMapper) {
        this.flexCostRepository = flexCostRepository;
        this.flexCostMapper = flexCostMapper;
    }

    @Override
    public FlexCostDTO save(FlexCostDTO flexCostDTO) {
        log.debug("Request to save FlexCost : {}", flexCostDTO);
        FlexCost flexCost = flexCostMapper.toEntity(flexCostDTO);
        flexCost = flexCostRepository.save(flexCost);
        return flexCostMapper.toDto(flexCost);
    }

    @Override
    public Optional<FlexCostDTO> partialUpdate(FlexCostDTO flexCostDTO) {
        log.debug("Request to partially update FlexCost : {}", flexCostDTO);

        return flexCostRepository
            .findById(flexCostDTO.getId())
            .map(existingFlexCost -> {
                flexCostMapper.partialUpdate(existingFlexCost, flexCostDTO);

                return existingFlexCost;
            })
            .map(flexCostRepository::save)
            .map(flexCostMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FlexCostDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FlexCosts");
        return flexCostRepository.findAll(pageable).map(flexCostMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FlexCostDTO> findOne(Long id) {
        log.debug("Request to get FlexCost : {}", id);
        return flexCostRepository.findById(id).map(flexCostMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FlexCost : {}", id);
        flexCostRepository.deleteById(id);
    }
}
