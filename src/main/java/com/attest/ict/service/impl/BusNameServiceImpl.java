package com.attest.ict.service.impl;

import com.attest.ict.domain.BusName;
import com.attest.ict.repository.BusNameRepository;
import com.attest.ict.service.BusNameService;
import com.attest.ict.service.dto.BusNameDTO;
import com.attest.ict.service.mapper.BusNameMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BusName}.
 */
@Service
@Transactional
public class BusNameServiceImpl implements BusNameService {

    private final Logger log = LoggerFactory.getLogger(BusNameServiceImpl.class);

    private final BusNameRepository busNameRepository;

    private final BusNameMapper busNameMapper;

    public BusNameServiceImpl(BusNameRepository busNameRepository, BusNameMapper busNameMapper) {
        this.busNameRepository = busNameRepository;
        this.busNameMapper = busNameMapper;
    }

    @Override
    public BusNameDTO save(BusNameDTO busNameDTO) {
        log.debug("Request to save BusName : {}", busNameDTO);
        BusName busName = busNameMapper.toEntity(busNameDTO);
        busName = busNameRepository.save(busName);
        return busNameMapper.toDto(busName);
    }

    @Override
    public Optional<BusNameDTO> partialUpdate(BusNameDTO busNameDTO) {
        log.debug("Request to partially update BusName : {}", busNameDTO);

        return busNameRepository
            .findById(busNameDTO.getId())
            .map(existingBusName -> {
                busNameMapper.partialUpdate(existingBusName, busNameDTO);

                return existingBusName;
            })
            .map(busNameRepository::save)
            .map(busNameMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BusNameDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BusNames");
        return busNameRepository.findAll(pageable).map(busNameMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BusNameDTO> findOne(Long id) {
        log.debug("Request to get BusName : {}", id);
        return busNameRepository.findById(id).map(busNameMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BusName : {}", id);
        busNameRepository.deleteById(id);
    }
}
