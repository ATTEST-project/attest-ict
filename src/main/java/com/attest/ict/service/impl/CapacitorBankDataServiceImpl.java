package com.attest.ict.service.impl;

import com.attest.ict.domain.CapacitorBankData;
import com.attest.ict.repository.CapacitorBankDataRepository;
import com.attest.ict.service.CapacitorBankDataService;
import com.attest.ict.service.dto.CapacitorBankDataDTO;
import com.attest.ict.service.mapper.CapacitorBankDataMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CapacitorBankData}.
 */
@Service
@Transactional
public class CapacitorBankDataServiceImpl implements CapacitorBankDataService {

    private final Logger log = LoggerFactory.getLogger(CapacitorBankDataServiceImpl.class);

    private final CapacitorBankDataRepository capacitorBankDataRepository;

    private final CapacitorBankDataMapper capacitorBankDataMapper;

    public CapacitorBankDataServiceImpl(
        CapacitorBankDataRepository capacitorBankDataRepository,
        CapacitorBankDataMapper capacitorBankDataMapper
    ) {
        this.capacitorBankDataRepository = capacitorBankDataRepository;
        this.capacitorBankDataMapper = capacitorBankDataMapper;
    }

    @Override
    public CapacitorBankDataDTO save(CapacitorBankDataDTO capacitorBankDataDTO) {
        log.debug("Request to save CapacitorBankData : {}", capacitorBankDataDTO);
        CapacitorBankData capacitorBankData = capacitorBankDataMapper.toEntity(capacitorBankDataDTO);
        capacitorBankData = capacitorBankDataRepository.save(capacitorBankData);
        return capacitorBankDataMapper.toDto(capacitorBankData);
    }

    @Override
    public Optional<CapacitorBankDataDTO> partialUpdate(CapacitorBankDataDTO capacitorBankDataDTO) {
        log.debug("Request to partially update CapacitorBankData : {}", capacitorBankDataDTO);

        return capacitorBankDataRepository
            .findById(capacitorBankDataDTO.getId())
            .map(existingCapacitorBankData -> {
                capacitorBankDataMapper.partialUpdate(existingCapacitorBankData, capacitorBankDataDTO);

                return existingCapacitorBankData;
            })
            .map(capacitorBankDataRepository::save)
            .map(capacitorBankDataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CapacitorBankDataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CapacitorBankData");
        return capacitorBankDataRepository.findAll(pageable).map(capacitorBankDataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CapacitorBankDataDTO> findOne(Long id) {
        log.debug("Request to get CapacitorBankData : {}", id);
        return capacitorBankDataRepository.findById(id).map(capacitorBankDataMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CapacitorBankData : {}", id);
        capacitorBankDataRepository.deleteById(id);
    }

    //==== Start Custom Methods

    public List<CapacitorBankData> getAllCapacitors() {
        return capacitorBankDataRepository.findAll();
    }

    public List<CapacitorBankData> getCapacitorsByNetworkId(Long networkId) {
        return capacitorBankDataRepository.findByNetworkId(networkId);
    }
}
