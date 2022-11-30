package com.attest.ict.service.impl;

import com.attest.ict.domain.StorageCost;
import com.attest.ict.repository.StorageCostRepository;
import com.attest.ict.service.StorageCostService;
import com.attest.ict.service.dto.StorageCostDTO;
import com.attest.ict.service.mapper.StorageCostMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StorageCost}.
 */
@Service
@Transactional
public class StorageCostServiceImpl implements StorageCostService {

    private final Logger log = LoggerFactory.getLogger(StorageCostServiceImpl.class);

    private final StorageCostRepository storageCostRepository;

    private final StorageCostMapper storageCostMapper;

    public StorageCostServiceImpl(StorageCostRepository storageCostRepository, StorageCostMapper storageCostMapper) {
        this.storageCostRepository = storageCostRepository;
        this.storageCostMapper = storageCostMapper;
    }

    @Override
    public StorageCostDTO save(StorageCostDTO storageCostDTO) {
        log.debug("Request to save StorageCost : {}", storageCostDTO);
        StorageCost storageCost = storageCostMapper.toEntity(storageCostDTO);
        storageCost = storageCostRepository.save(storageCost);
        return storageCostMapper.toDto(storageCost);
    }

    @Override
    public Optional<StorageCostDTO> partialUpdate(StorageCostDTO storageCostDTO) {
        log.debug("Request to partially update StorageCost : {}", storageCostDTO);

        return storageCostRepository
            .findById(storageCostDTO.getId())
            .map(existingStorageCost -> {
                storageCostMapper.partialUpdate(existingStorageCost, storageCostDTO);

                return existingStorageCost;
            })
            .map(storageCostRepository::save)
            .map(storageCostMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StorageCostDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StorageCosts");
        return storageCostRepository.findAll(pageable).map(storageCostMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StorageCostDTO> findOne(Long id) {
        log.debug("Request to get StorageCost : {}", id);
        return storageCostRepository.findById(id).map(storageCostMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete StorageCost : {}", id);
        storageCostRepository.deleteById(id);
    }
}
