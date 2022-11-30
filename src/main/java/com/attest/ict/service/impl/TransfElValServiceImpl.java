package com.attest.ict.service.impl;

import com.attest.ict.domain.TransfElVal;
import com.attest.ict.repository.TransfElValRepository;
import com.attest.ict.service.TransfElValService;
import com.attest.ict.service.dto.TransfElValDTO;
import com.attest.ict.service.mapper.TransfElValMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TransfElVal}.
 */
@Service
@Transactional
public class TransfElValServiceImpl implements TransfElValService {

    private final Logger log = LoggerFactory.getLogger(TransfElValServiceImpl.class);

    private final TransfElValRepository transfElValRepository;

    private final TransfElValMapper transfElValMapper;

    public TransfElValServiceImpl(TransfElValRepository transfElValRepository, TransfElValMapper transfElValMapper) {
        this.transfElValRepository = transfElValRepository;
        this.transfElValMapper = transfElValMapper;
    }

    @Override
    public TransfElValDTO save(TransfElValDTO transfElValDTO) {
        log.debug("Request to save TransfElVal : {}", transfElValDTO);
        TransfElVal transfElVal = transfElValMapper.toEntity(transfElValDTO);
        transfElVal = transfElValRepository.save(transfElVal);
        return transfElValMapper.toDto(transfElVal);
    }

    @Override
    public Optional<TransfElValDTO> partialUpdate(TransfElValDTO transfElValDTO) {
        log.debug("Request to partially update TransfElVal : {}", transfElValDTO);

        return transfElValRepository
            .findById(transfElValDTO.getId())
            .map(existingTransfElVal -> {
                transfElValMapper.partialUpdate(existingTransfElVal, transfElValDTO);

                return existingTransfElVal;
            })
            .map(transfElValRepository::save)
            .map(transfElValMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransfElValDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransfElVals");
        return transfElValRepository.findAll(pageable).map(transfElValMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransfElValDTO> findOne(Long id) {
        log.debug("Request to get TransfElVal : {}", id);
        return transfElValRepository.findById(id).map(transfElValMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransfElVal : {}", id);
        transfElValRepository.deleteById(id);
    }
}
