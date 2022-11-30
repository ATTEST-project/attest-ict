package com.attest.ict.service.impl;

import com.attest.ict.domain.Branch;
import com.attest.ict.repository.BranchRepository;
import com.attest.ict.service.BranchService;
import com.attest.ict.service.dto.BranchDTO;
import com.attest.ict.service.mapper.BranchMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Branch}.
 */
@Service
@Transactional
public class BranchServiceImpl implements BranchService {

    private final Logger log = LoggerFactory.getLogger(BranchServiceImpl.class);

    private final BranchRepository branchRepository;

    private final BranchMapper branchMapper;

    public BranchServiceImpl(BranchRepository branchRepository, BranchMapper branchMapper) {
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
    }

    @Override
    public BranchDTO save(BranchDTO branchDTO) {
        log.debug("Request to save Branch : {}", branchDTO);
        Branch branch = branchMapper.toEntity(branchDTO);
        branch = branchRepository.save(branch);
        return branchMapper.toDto(branch);
    }

    @Override
    public Optional<BranchDTO> partialUpdate(BranchDTO branchDTO) {
        log.debug("Request to partially update Branch : {}", branchDTO);

        return branchRepository
            .findById(branchDTO.getId())
            .map(existingBranch -> {
                branchMapper.partialUpdate(existingBranch, branchDTO);

                return existingBranch;
            })
            .map(branchRepository::save)
            .map(branchMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BranchDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Branches");
        return branchRepository.findAll(pageable).map(branchMapper::toDto);
    }

    /**
     *  Get all the branches where BranchExtension is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BranchDTO> findAllWhereBranchExtensionIsNull() {
        log.debug("Request to get all branches where BranchExtension is null");
        return StreamSupport
            .stream(branchRepository.findAll().spliterator(), false)
            .filter(branch -> branch.getBranchExtension() == null)
            .map(branchMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BranchDTO> findOne(Long id) {
        log.debug("Request to get Branch : {}", id);
        return branchRepository.findById(id).map(branchMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Branch : {}", id);
        branchRepository.deleteById(id);
    }

    //Start Custom methods
    @Override
    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    @Override
    public List<Branch> getBranchesByNetworkId(Long networkId) {
        return branchRepository.findByNetworkId(networkId);
    }

    @Override
    public List<Branch> saveAll(List<Branch> branches) {
        return branchRepository.saveAll(branches);
    }
}
