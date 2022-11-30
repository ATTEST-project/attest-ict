package com.attest.ict.service.impl;

import com.attest.ict.domain.VoltageLevel;
import com.attest.ict.repository.VoltageLevelRepository;
import com.attest.ict.service.VoltageLevelService;
import com.attest.ict.service.dto.VoltageLevelDTO;
import com.attest.ict.service.mapper.VoltageLevelMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link VoltageLevel}.
 */
@Service
@Transactional
public class VoltageLevelServiceImpl implements VoltageLevelService {

    private final Logger log = LoggerFactory.getLogger(VoltageLevelServiceImpl.class);

    private final VoltageLevelRepository voltageLevelRepository;

    private final VoltageLevelMapper voltageLevelMapper;

    public VoltageLevelServiceImpl(VoltageLevelRepository voltageLevelRepository, VoltageLevelMapper voltageLevelMapper) {
        this.voltageLevelRepository = voltageLevelRepository;
        this.voltageLevelMapper = voltageLevelMapper;
    }

    @Override
    public VoltageLevelDTO save(VoltageLevelDTO voltageLevelDTO) {
        log.debug("Request to save VoltageLevel : {}", voltageLevelDTO);
        VoltageLevel voltageLevel = voltageLevelMapper.toEntity(voltageLevelDTO);
        voltageLevel = voltageLevelRepository.save(voltageLevel);
        return voltageLevelMapper.toDto(voltageLevel);
    }

    @Override
    public Optional<VoltageLevelDTO> partialUpdate(VoltageLevelDTO voltageLevelDTO) {
        log.debug("Request to partially update VoltageLevel : {}", voltageLevelDTO);

        return voltageLevelRepository
            .findById(voltageLevelDTO.getId())
            .map(existingVoltageLevel -> {
                voltageLevelMapper.partialUpdate(existingVoltageLevel, voltageLevelDTO);

                return existingVoltageLevel;
            })
            .map(voltageLevelRepository::save)
            .map(voltageLevelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VoltageLevelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VoltageLevels");
        return voltageLevelRepository.findAll(pageable).map(voltageLevelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VoltageLevelDTO> findOne(Long id) {
        log.debug("Request to get VoltageLevel : {}", id);
        return voltageLevelRepository.findById(id).map(voltageLevelMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VoltageLevel : {}", id);
        voltageLevelRepository.deleteById(id);
    }

    //====== Start Custom Methods

    @Override
    public List<VoltageLevel> getAllVLevels() {
        return voltageLevelRepository.findAll();
    }

    @Override
    public VoltageLevel getVLevelsByNetworkId(Long networkId) {
        return voltageLevelRepository.findByNetworkId(networkId);
    }
}
