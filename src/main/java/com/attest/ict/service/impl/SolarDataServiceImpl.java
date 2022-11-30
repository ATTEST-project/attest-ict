package com.attest.ict.service.impl;

import com.attest.ict.domain.SolarData;
import com.attest.ict.repository.SolarDataRepository;
import com.attest.ict.service.SolarDataService;
import com.attest.ict.service.dto.SolarDataDTO;
import com.attest.ict.service.mapper.SolarDataMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SolarData}.
 */
@Service
@Transactional
public class SolarDataServiceImpl implements SolarDataService {

    private final Logger log = LoggerFactory.getLogger(SolarDataServiceImpl.class);

    private final SolarDataRepository solarDataRepository;

    private final SolarDataMapper solarDataMapper;

    public SolarDataServiceImpl(SolarDataRepository solarDataRepository, SolarDataMapper solarDataMapper) {
        this.solarDataRepository = solarDataRepository;
        this.solarDataMapper = solarDataMapper;
    }

    @Override
    public SolarDataDTO save(SolarDataDTO solarDataDTO) {
        log.debug("Request to save SolarData : {}", solarDataDTO);
        SolarData solarData = solarDataMapper.toEntity(solarDataDTO);
        solarData = solarDataRepository.save(solarData);
        return solarDataMapper.toDto(solarData);
    }

    @Override
    public Optional<SolarDataDTO> partialUpdate(SolarDataDTO solarDataDTO) {
        log.debug("Request to partially update SolarData : {}", solarDataDTO);

        return solarDataRepository
            .findById(solarDataDTO.getId())
            .map(existingSolarData -> {
                solarDataMapper.partialUpdate(existingSolarData, solarDataDTO);

                return existingSolarData;
            })
            .map(solarDataRepository::save)
            .map(solarDataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolarDataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SolarData");
        return solarDataRepository.findAll(pageable).map(solarDataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SolarDataDTO> findOne(Long id) {
        log.debug("Request to get SolarData : {}", id);
        return solarDataRepository.findById(id).map(solarDataMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SolarData : {}", id);
        solarDataRepository.deleteById(id);
    }
}
