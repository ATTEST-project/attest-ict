package com.attest.ict.service.impl;

import com.attest.ict.domain.BusExtension;
import com.attest.ict.repository.BusExtensionRepository;
import com.attest.ict.service.BusExtensionService;
import com.attest.ict.service.dto.BusExtensionDTO;
import com.attest.ict.service.mapper.BusExtensionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BusExtension}.
 */
@Service
@Transactional
public class BusExtensionServiceImpl implements BusExtensionService {

    private final Logger log = LoggerFactory.getLogger(BusExtensionServiceImpl.class);

    private final BusExtensionRepository busExtensionRepository;

    private final BusExtensionMapper busExtensionMapper;

    public BusExtensionServiceImpl(BusExtensionRepository busExtensionRepository, BusExtensionMapper busExtensionMapper) {
        this.busExtensionRepository = busExtensionRepository;
        this.busExtensionMapper = busExtensionMapper;
    }

    @Override
    public BusExtensionDTO save(BusExtensionDTO busExtensionDTO) {
        log.debug("Request to save BusExtension : {}", busExtensionDTO);
        BusExtension busExtension = busExtensionMapper.toEntity(busExtensionDTO);
        busExtension = busExtensionRepository.save(busExtension);
        return busExtensionMapper.toDto(busExtension);
    }

    @Override
    public Optional<BusExtensionDTO> partialUpdate(BusExtensionDTO busExtensionDTO) {
        log.debug("Request to partially update BusExtension : {}", busExtensionDTO);

        return busExtensionRepository
            .findById(busExtensionDTO.getId())
            .map(existingBusExtension -> {
                busExtensionMapper.partialUpdate(existingBusExtension, busExtensionDTO);

                return existingBusExtension;
            })
            .map(busExtensionRepository::save)
            .map(busExtensionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BusExtensionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BusExtensions");
        return busExtensionRepository.findAll(pageable).map(busExtensionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BusExtensionDTO> findOne(Long id) {
        log.debug("Request to get BusExtension : {}", id);
        return busExtensionRepository.findById(id).map(busExtensionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BusExtension : {}", id);
        busExtensionRepository.deleteById(id);
    }
}
