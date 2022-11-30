package com.attest.ict.service.impl;

import com.attest.ict.domain.GenProfile;
import com.attest.ict.repository.GenProfileRepository;
import com.attest.ict.service.GenProfileService;
import com.attest.ict.service.dto.GenProfileDTO;
import com.attest.ict.service.mapper.GenProfileMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GenProfile}.
 */
@Service
@Transactional
public class GenProfileServiceImpl implements GenProfileService {

    private final Logger log = LoggerFactory.getLogger(GenProfileServiceImpl.class);

    private final GenProfileRepository genProfileRepository;

    private final GenProfileMapper genProfileMapper;

    public GenProfileServiceImpl(GenProfileRepository genProfileRepository, GenProfileMapper genProfileMapper) {
        this.genProfileRepository = genProfileRepository;
        this.genProfileMapper = genProfileMapper;
    }

    @Override
    public GenProfileDTO save(GenProfileDTO genProfileDTO) {
        log.debug("Request to save GenProfile : {}", genProfileDTO);
        GenProfile genProfile = genProfileMapper.toEntity(genProfileDTO);
        genProfile = genProfileRepository.save(genProfile);
        return genProfileMapper.toDto(genProfile);
    }

    @Override
    public Optional<GenProfileDTO> partialUpdate(GenProfileDTO genProfileDTO) {
        log.debug("Request to partially update GenProfile : {}", genProfileDTO);

        return genProfileRepository
            .findById(genProfileDTO.getId())
            .map(existingGenProfile -> {
                genProfileMapper.partialUpdate(existingGenProfile, genProfileDTO);

                return existingGenProfile;
            })
            .map(genProfileRepository::save)
            .map(genProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GenProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GenProfiles");
        return genProfileRepository.findAll(pageable).map(genProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GenProfileDTO> findOne(Long id) {
        log.debug("Request to get GenProfile : {}", id);
        return genProfileRepository.findById(id).map(genProfileMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GenProfile : {}", id);
        genProfileRepository.deleteById(id);
    }
}
