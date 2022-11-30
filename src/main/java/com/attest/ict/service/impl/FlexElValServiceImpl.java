package com.attest.ict.service.impl;

import com.attest.ict.domain.FlexElVal;
import com.attest.ict.repository.FlexElValRepository;
import com.attest.ict.service.FlexElValService;
import com.attest.ict.service.dto.FlexElValDTO;
import com.attest.ict.service.mapper.FlexElValMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FlexElVal}.
 */
@Service
@Transactional
public class FlexElValServiceImpl implements FlexElValService {

    private final Logger log = LoggerFactory.getLogger(FlexElValServiceImpl.class);

    private final FlexElValRepository flexElValRepository;

    private final FlexElValMapper flexElValMapper;

    public FlexElValServiceImpl(FlexElValRepository flexElValRepository, FlexElValMapper flexElValMapper) {
        this.flexElValRepository = flexElValRepository;
        this.flexElValMapper = flexElValMapper;
    }

    @Override
    public FlexElValDTO save(FlexElValDTO flexElValDTO) {
        log.debug("Request to save FlexElVal : {}", flexElValDTO);
        FlexElVal flexElVal = flexElValMapper.toEntity(flexElValDTO);
        flexElVal = flexElValRepository.save(flexElVal);
        return flexElValMapper.toDto(flexElVal);
    }

    @Override
    public Optional<FlexElValDTO> partialUpdate(FlexElValDTO flexElValDTO) {
        log.debug("Request to partially update FlexElVal : {}", flexElValDTO);

        return flexElValRepository
            .findById(flexElValDTO.getId())
            .map(existingFlexElVal -> {
                flexElValMapper.partialUpdate(existingFlexElVal, flexElValDTO);

                return existingFlexElVal;
            })
            .map(flexElValRepository::save)
            .map(flexElValMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FlexElValDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FlexElVals");
        return flexElValRepository.findAll(pageable).map(flexElValMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FlexElValDTO> findOne(Long id) {
        log.debug("Request to get FlexElVal : {}", id);
        return flexElValRepository.findById(id).map(flexElValMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FlexElVal : {}", id);
        flexElValRepository.deleteById(id);
    }
}
