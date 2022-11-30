package com.attest.ict.service.impl;

import com.attest.ict.domain.GenElVal;
import com.attest.ict.repository.GenElValRepository;
import com.attest.ict.service.GenElValService;
import com.attest.ict.service.dto.GenElValDTO;
import com.attest.ict.service.mapper.GenElValMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GenElVal}.
 */
@Service
@Transactional
public class GenElValServiceImpl implements GenElValService {

    private final Logger log = LoggerFactory.getLogger(GenElValServiceImpl.class);

    private final GenElValRepository genElValRepository;

    private final GenElValMapper genElValMapper;

    public GenElValServiceImpl(GenElValRepository genElValRepository, GenElValMapper genElValMapper) {
        this.genElValRepository = genElValRepository;
        this.genElValMapper = genElValMapper;
    }

    @Override
    public GenElValDTO save(GenElValDTO genElValDTO) {
        log.debug("Request to save GenElVal : {}", genElValDTO);
        GenElVal genElVal = genElValMapper.toEntity(genElValDTO);
        genElVal = genElValRepository.save(genElVal);
        return genElValMapper.toDto(genElVal);
    }

    @Override
    public Optional<GenElValDTO> partialUpdate(GenElValDTO genElValDTO) {
        log.debug("Request to partially update GenElVal : {}", genElValDTO);

        return genElValRepository
            .findById(genElValDTO.getId())
            .map(existingGenElVal -> {
                genElValMapper.partialUpdate(existingGenElVal, genElValDTO);

                return existingGenElVal;
            })
            .map(genElValRepository::save)
            .map(genElValMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GenElValDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GenElVals");
        return genElValRepository.findAll(pageable).map(genElValMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GenElValDTO> findOne(Long id) {
        log.debug("Request to get GenElVal : {}", id);
        return genElValRepository.findById(id).map(genElValMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GenElVal : {}", id);
        genElValRepository.deleteById(id);
    }
}
