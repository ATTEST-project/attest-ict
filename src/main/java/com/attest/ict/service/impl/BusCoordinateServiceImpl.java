package com.attest.ict.service.impl;

import com.attest.ict.domain.BusCoordinate;
import com.attest.ict.repository.BusCoordinateRepository;
import com.attest.ict.service.BusCoordinateService;
import com.attest.ict.service.dto.BusCoordinateDTO;
import com.attest.ict.service.mapper.BusCoordinateMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BusCoordinate}.
 */
@Service
@Transactional
public class BusCoordinateServiceImpl implements BusCoordinateService {

    private final Logger log = LoggerFactory.getLogger(BusCoordinateServiceImpl.class);

    private final BusCoordinateRepository busCoordinateRepository;

    private final BusCoordinateMapper busCoordinateMapper;

    public BusCoordinateServiceImpl(BusCoordinateRepository busCoordinateRepository, BusCoordinateMapper busCoordinateMapper) {
        this.busCoordinateRepository = busCoordinateRepository;
        this.busCoordinateMapper = busCoordinateMapper;
    }

    @Override
    public BusCoordinateDTO save(BusCoordinateDTO busCoordinateDTO) {
        log.debug("Request to save BusCoordinate : {}", busCoordinateDTO);
        BusCoordinate busCoordinate = busCoordinateMapper.toEntity(busCoordinateDTO);
        busCoordinate = busCoordinateRepository.save(busCoordinate);
        return busCoordinateMapper.toDto(busCoordinate);
    }

    @Override
    public Optional<BusCoordinateDTO> partialUpdate(BusCoordinateDTO busCoordinateDTO) {
        log.debug("Request to partially update BusCoordinate : {}", busCoordinateDTO);

        return busCoordinateRepository
            .findById(busCoordinateDTO.getId())
            .map(existingBusCoordinate -> {
                busCoordinateMapper.partialUpdate(existingBusCoordinate, busCoordinateDTO);

                return existingBusCoordinate;
            })
            .map(busCoordinateRepository::save)
            .map(busCoordinateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BusCoordinateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BusCoordinates");
        return busCoordinateRepository.findAll(pageable).map(busCoordinateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BusCoordinateDTO> findOne(Long id) {
        log.debug("Request to get BusCoordinate : {}", id);
        return busCoordinateRepository.findById(id).map(busCoordinateMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BusCoordinate : {}", id);
        busCoordinateRepository.deleteById(id);
    }
}
