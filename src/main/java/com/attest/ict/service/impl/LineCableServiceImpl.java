package com.attest.ict.service.impl;

import com.attest.ict.domain.LineCable;
import com.attest.ict.repository.LineCableRepository;
import com.attest.ict.service.LineCableService;
import com.attest.ict.service.dto.LineCableDTO;
import com.attest.ict.service.mapper.LineCableMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LineCable}.
 */
@Service
@Transactional
public class LineCableServiceImpl implements LineCableService {

    private final Logger log = LoggerFactory.getLogger(LineCableServiceImpl.class);

    private final LineCableRepository lineCableRepository;

    private final LineCableMapper lineCableMapper;

    public LineCableServiceImpl(LineCableRepository lineCableRepository, LineCableMapper lineCableMapper) {
        this.lineCableRepository = lineCableRepository;
        this.lineCableMapper = lineCableMapper;
    }

    @Override
    public LineCableDTO save(LineCableDTO lineCableDTO) {
        log.debug("Request to save LineCable : {}", lineCableDTO);
        LineCable lineCable = lineCableMapper.toEntity(lineCableDTO);
        lineCable = lineCableRepository.save(lineCable);
        return lineCableMapper.toDto(lineCable);
    }

    @Override
    public Optional<LineCableDTO> partialUpdate(LineCableDTO lineCableDTO) {
        log.debug("Request to partially update LineCable : {}", lineCableDTO);

        return lineCableRepository
            .findById(lineCableDTO.getId())
            .map(existingLineCable -> {
                lineCableMapper.partialUpdate(existingLineCable, lineCableDTO);

                return existingLineCable;
            })
            .map(lineCableRepository::save)
            .map(lineCableMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LineCableDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LineCables");
        return lineCableRepository.findAll(pageable).map(lineCableMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LineCableDTO> findOne(Long id) {
        log.debug("Request to get LineCable : {}", id);
        return lineCableRepository.findById(id).map(lineCableMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LineCable : {}", id);
        lineCableRepository.deleteById(id);
    }
}
