package com.attest.ict.service.impl;

import com.attest.ict.domain.FlexProfile;
import com.attest.ict.repository.FlexProfileRepository;
import com.attest.ict.service.FlexProfileService;
import com.attest.ict.service.dto.FlexProfileDTO;
import com.attest.ict.service.mapper.FlexProfileMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FlexProfile}.
 */
@Service
@Transactional
public class FlexProfileServiceImpl implements FlexProfileService {

    private final Logger log = LoggerFactory.getLogger(FlexProfileServiceImpl.class);

    private final FlexProfileRepository flexProfileRepository;

    private final FlexProfileMapper flexProfileMapper;

    public FlexProfileServiceImpl(FlexProfileRepository flexProfileRepository, FlexProfileMapper flexProfileMapper) {
        this.flexProfileRepository = flexProfileRepository;
        this.flexProfileMapper = flexProfileMapper;
    }

    @Override
    public FlexProfileDTO save(FlexProfileDTO flexProfileDTO) {
        log.debug("Request to save FlexProfile : {}", flexProfileDTO);
        FlexProfile flexProfile = flexProfileMapper.toEntity(flexProfileDTO);
        flexProfile = flexProfileRepository.save(flexProfile);
        return flexProfileMapper.toDto(flexProfile);
    }

    @Override
    public Optional<FlexProfileDTO> partialUpdate(FlexProfileDTO flexProfileDTO) {
        log.debug("Request to partially update FlexProfile : {}", flexProfileDTO);

        return flexProfileRepository
            .findById(flexProfileDTO.getId())
            .map(existingFlexProfile -> {
                flexProfileMapper.partialUpdate(existingFlexProfile, flexProfileDTO);

                return existingFlexProfile;
            })
            .map(flexProfileRepository::save)
            .map(flexProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FlexProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FlexProfiles");
        return flexProfileRepository.findAll(pageable).map(flexProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FlexProfileDTO> findOne(Long id) {
        log.debug("Request to get FlexProfile : {}", id);
        return flexProfileRepository.findById(id).map(flexProfileMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FlexProfile : {}", id);
        flexProfileRepository.deleteById(id);
    }
}
