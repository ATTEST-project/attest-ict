package com.attest.ict.service.impl;

import com.attest.ict.domain.BranchElVal;
import com.attest.ict.repository.BranchElValRepository;
import com.attest.ict.service.BranchElValService;
import com.attest.ict.service.dto.BranchElValDTO;
import com.attest.ict.service.mapper.BranchElValMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BranchElVal}.
 */
@Service
@Transactional
public class BranchElValServiceImpl implements BranchElValService {

    private final Logger log = LoggerFactory.getLogger(BranchElValServiceImpl.class);

    private final BranchElValRepository branchElValRepository;

    private final BranchElValMapper branchElValMapper;

    public BranchElValServiceImpl(BranchElValRepository branchElValRepository, BranchElValMapper branchElValMapper) {
        this.branchElValRepository = branchElValRepository;
        this.branchElValMapper = branchElValMapper;
    }

    @Override
    public BranchElValDTO save(BranchElValDTO branchElValDTO) {
        log.debug("Request to save BranchElVal : {}", branchElValDTO);
        BranchElVal branchElVal = branchElValMapper.toEntity(branchElValDTO);
        branchElVal = branchElValRepository.save(branchElVal);
        return branchElValMapper.toDto(branchElVal);
    }

    @Override
    public Optional<BranchElValDTO> partialUpdate(BranchElValDTO branchElValDTO) {
        log.debug("Request to partially update BranchElVal : {}", branchElValDTO);

        return branchElValRepository
            .findById(branchElValDTO.getId())
            .map(existingBranchElVal -> {
                branchElValMapper.partialUpdate(existingBranchElVal, branchElValDTO);

                return existingBranchElVal;
            })
            .map(branchElValRepository::save)
            .map(branchElValMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BranchElValDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BranchElVals");
        return branchElValRepository.findAll(pageable).map(branchElValMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BranchElValDTO> findOne(Long id) {
        log.debug("Request to get BranchElVal : {}", id);
        return branchElValRepository.findById(id).map(branchElValMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BranchElVal : {}", id);
        branchElValRepository.deleteById(id);
    }
}
