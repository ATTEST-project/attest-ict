package com.attest.ict.service.impl;

import com.attest.ict.domain.BranchProfile;
import com.attest.ict.repository.BranchProfileRepository;
import com.attest.ict.service.BranchProfileService;
import com.attest.ict.service.dto.BranchProfileDTO;
import com.attest.ict.service.mapper.BranchProfileMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BranchProfile}.
 */
@Service
@Transactional
public class BranchProfileServiceImpl implements BranchProfileService {

    private final Logger log = LoggerFactory.getLogger(BranchProfileServiceImpl.class);

    private final BranchProfileRepository branchProfileRepository;

    private final BranchProfileMapper branchProfileMapper;

    public BranchProfileServiceImpl(BranchProfileRepository branchProfileRepository, BranchProfileMapper branchProfileMapper) {
        this.branchProfileRepository = branchProfileRepository;
        this.branchProfileMapper = branchProfileMapper;
    }

    @Override
    public BranchProfileDTO save(BranchProfileDTO branchProfileDTO) {
        log.debug("Request to save BranchProfile : {}", branchProfileDTO);
        BranchProfile branchProfile = branchProfileMapper.toEntity(branchProfileDTO);
        branchProfile = branchProfileRepository.save(branchProfile);
        return branchProfileMapper.toDto(branchProfile);
    }

    @Override
    public Optional<BranchProfileDTO> partialUpdate(BranchProfileDTO branchProfileDTO) {
        log.debug("Request to partially update BranchProfile : {}", branchProfileDTO);

        return branchProfileRepository
            .findById(branchProfileDTO.getId())
            .map(existingBranchProfile -> {
                branchProfileMapper.partialUpdate(existingBranchProfile, branchProfileDTO);

                return existingBranchProfile;
            })
            .map(branchProfileRepository::save)
            .map(branchProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BranchProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BranchProfiles");
        return branchProfileRepository.findAll(pageable).map(branchProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BranchProfileDTO> findOne(Long id) {
        log.debug("Request to get BranchProfile : {}", id);
        return branchProfileRepository.findById(id).map(branchProfileMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BranchProfile : {}", id);
        branchProfileRepository.deleteById(id);
    }
}
