package com.attest.ict.service.impl;

import com.attest.ict.domain.WindData;
import com.attest.ict.repository.WindDataRepository;
import com.attest.ict.service.WindDataService;
import com.attest.ict.service.dto.WindDataDTO;
import com.attest.ict.service.mapper.WindDataMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WindData}.
 */
@Service
@Transactional
public class WindDataServiceImpl implements WindDataService {

    private final Logger log = LoggerFactory.getLogger(WindDataServiceImpl.class);

    private final WindDataRepository windDataRepository;

    private final WindDataMapper windDataMapper;

    public WindDataServiceImpl(WindDataRepository windDataRepository, WindDataMapper windDataMapper) {
        this.windDataRepository = windDataRepository;
        this.windDataMapper = windDataMapper;
    }

    @Override
    public WindDataDTO save(WindDataDTO windDataDTO) {
        log.debug("Request to save WindData : {}", windDataDTO);
        WindData windData = windDataMapper.toEntity(windDataDTO);
        windData = windDataRepository.save(windData);
        return windDataMapper.toDto(windData);
    }

    @Override
    public Optional<WindDataDTO> partialUpdate(WindDataDTO windDataDTO) {
        log.debug("Request to partially update WindData : {}", windDataDTO);

        return windDataRepository
            .findById(windDataDTO.getId())
            .map(existingWindData -> {
                windDataMapper.partialUpdate(existingWindData, windDataDTO);

                return existingWindData;
            })
            .map(windDataRepository::save)
            .map(windDataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WindDataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WindData");
        return windDataRepository.findAll(pageable).map(windDataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WindDataDTO> findOne(Long id) {
        log.debug("Request to get WindData : {}", id);
        return windDataRepository.findById(id).map(windDataMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WindData : {}", id);
        windDataRepository.deleteById(id);
    }
}
