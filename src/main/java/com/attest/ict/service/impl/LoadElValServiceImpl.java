package com.attest.ict.service.impl;

import com.attest.ict.domain.LoadElVal;
import com.attest.ict.repository.LoadElValRepository;
import com.attest.ict.service.LoadElValService;
import com.attest.ict.service.dto.LoadElValDTO;
import com.attest.ict.service.mapper.LoadElValMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LoadElVal}.
 */
@Service
@Transactional
public class LoadElValServiceImpl implements LoadElValService {

    private final Logger log = LoggerFactory.getLogger(LoadElValServiceImpl.class);

    private final LoadElValRepository loadElValRepository;

    private final LoadElValMapper loadElValMapper;

    public LoadElValServiceImpl(LoadElValRepository loadElValRepository, LoadElValMapper loadElValMapper) {
        this.loadElValRepository = loadElValRepository;
        this.loadElValMapper = loadElValMapper;
    }

    @Override
    public LoadElValDTO save(LoadElValDTO loadElValDTO) {
        log.debug("Request to save LoadElVal : {}", loadElValDTO);
        LoadElVal loadElVal = loadElValMapper.toEntity(loadElValDTO);
        loadElVal = loadElValRepository.save(loadElVal);
        return loadElValMapper.toDto(loadElVal);
    }

    @Override
    public Optional<LoadElValDTO> partialUpdate(LoadElValDTO loadElValDTO) {
        log.debug("Request to partially update LoadElVal : {}", loadElValDTO);

        return loadElValRepository
            .findById(loadElValDTO.getId())
            .map(existingLoadElVal -> {
                loadElValMapper.partialUpdate(existingLoadElVal, loadElValDTO);

                return existingLoadElVal;
            })
            .map(loadElValRepository::save)
            .map(loadElValMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoadElValDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LoadElVals");
        return loadElValRepository.findAll(pageable).map(loadElValMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoadElValDTO> findOne(Long id) {
        log.debug("Request to get LoadElVal : {}", id);
        return loadElValRepository.findById(id).map(loadElValMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LoadElVal : {}", id);
        loadElValRepository.deleteById(id);
    }

    //======= Start Custom Methods
    @Override
    public List<LoadElVal> getAllLoadElVals() {
        List<LoadElVal> loadElVars = loadElValRepository.findAll();
        return loadElVars;
    }

    @Override
    public List<LoadElVal> getLoadElValsByNetworkId(Long networkId) {
        List<LoadElVal> loadElVars = loadElValRepository.findByLoadProfileNetworkId(networkId);
        return loadElVars;
    }
}
