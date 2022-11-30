package com.attest.ict.service.impl;

import com.attest.ict.domain.LoadProfile;
import com.attest.ict.repository.LoadProfileRepository;
import com.attest.ict.service.LoadProfileService;
import com.attest.ict.service.dto.LoadProfileDTO;
import com.attest.ict.service.mapper.LoadProfileMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LoadProfile}.
 */
@Service
@Transactional
public class LoadProfileServiceImpl implements LoadProfileService {

    private final Logger log = LoggerFactory.getLogger(LoadProfileServiceImpl.class);

    private final LoadProfileRepository loadProfileRepository;

    private final LoadProfileMapper loadProfileMapper;

    public LoadProfileServiceImpl(LoadProfileRepository loadProfileRepository, LoadProfileMapper loadProfileMapper) {
        this.loadProfileRepository = loadProfileRepository;
        this.loadProfileMapper = loadProfileMapper;
    }

    @Override
    public LoadProfileDTO save(LoadProfileDTO loadProfileDTO) {
        log.debug("Request to save LoadProfile : {}", loadProfileDTO);
        LoadProfile loadProfile = loadProfileMapper.toEntity(loadProfileDTO);
        loadProfile = loadProfileRepository.save(loadProfile);
        return loadProfileMapper.toDto(loadProfile);
    }

    @Override
    public Optional<LoadProfileDTO> partialUpdate(LoadProfileDTO loadProfileDTO) {
        log.debug("Request to partially update LoadProfile : {}", loadProfileDTO);

        return loadProfileRepository
            .findById(loadProfileDTO.getId())
            .map(existingLoadProfile -> {
                loadProfileMapper.partialUpdate(existingLoadProfile, loadProfileDTO);

                return existingLoadProfile;
            })
            .map(loadProfileRepository::save)
            .map(loadProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoadProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LoadProfiles");
        return loadProfileRepository.findAll(pageable).map(loadProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoadProfileDTO> findOne(Long id) {
        log.debug("Request to get LoadProfile : {}", id);
        return loadProfileRepository.findById(id).map(loadProfileMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LoadProfile : {}", id);
        loadProfileRepository.deleteById(id);
    }
}
