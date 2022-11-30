package com.attest.ict.service.impl;

import com.attest.ict.domain.DsoTsoConnection;
import com.attest.ict.repository.DsoTsoConnectionRepository;
import com.attest.ict.service.DsoTsoConnectionService;
import com.attest.ict.service.dto.DsoTsoConnectionDTO;
import com.attest.ict.service.mapper.DsoTsoConnectionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DsoTsoConnection}.
 */
@Service
@Transactional
public class DsoTsoConnectionServiceImpl implements DsoTsoConnectionService {

    private final Logger log = LoggerFactory.getLogger(DsoTsoConnectionServiceImpl.class);

    private final DsoTsoConnectionRepository dsoTsoConnectionRepository;

    private final DsoTsoConnectionMapper dsoTsoConnectionMapper;

    public DsoTsoConnectionServiceImpl(
        DsoTsoConnectionRepository dsoTsoConnectionRepository,
        DsoTsoConnectionMapper dsoTsoConnectionMapper
    ) {
        this.dsoTsoConnectionRepository = dsoTsoConnectionRepository;
        this.dsoTsoConnectionMapper = dsoTsoConnectionMapper;
    }

    @Override
    public DsoTsoConnectionDTO save(DsoTsoConnectionDTO dsoTsoConnectionDTO) {
        log.debug("Request to save DsoTsoConnection : {}", dsoTsoConnectionDTO);
        DsoTsoConnection dsoTsoConnection = dsoTsoConnectionMapper.toEntity(dsoTsoConnectionDTO);
        dsoTsoConnection = dsoTsoConnectionRepository.save(dsoTsoConnection);
        return dsoTsoConnectionMapper.toDto(dsoTsoConnection);
    }

    @Override
    public Optional<DsoTsoConnectionDTO> partialUpdate(DsoTsoConnectionDTO dsoTsoConnectionDTO) {
        log.debug("Request to partially update DsoTsoConnection : {}", dsoTsoConnectionDTO);

        return dsoTsoConnectionRepository
            .findById(dsoTsoConnectionDTO.getId())
            .map(existingDsoTsoConnection -> {
                dsoTsoConnectionMapper.partialUpdate(existingDsoTsoConnection, dsoTsoConnectionDTO);

                return existingDsoTsoConnection;
            })
            .map(dsoTsoConnectionRepository::save)
            .map(dsoTsoConnectionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DsoTsoConnectionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DsoTsoConnections");
        return dsoTsoConnectionRepository.findAll(pageable).map(dsoTsoConnectionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DsoTsoConnectionDTO> findOne(Long id) {
        log.debug("Request to get DsoTsoConnection : {}", id);
        return dsoTsoConnectionRepository.findById(id).map(dsoTsoConnectionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DsoTsoConnection : {}", id);
        dsoTsoConnectionRepository.deleteById(id);
    }
}
