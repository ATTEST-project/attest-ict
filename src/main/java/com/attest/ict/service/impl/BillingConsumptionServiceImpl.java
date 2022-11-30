package com.attest.ict.service.impl;

import com.attest.ict.domain.BillingConsumption;
import com.attest.ict.repository.BillingConsumptionRepository;
import com.attest.ict.service.BillingConsumptionService;
import com.attest.ict.service.dto.BillingConsumptionDTO;
import com.attest.ict.service.mapper.BillingConsumptionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BillingConsumption}.
 */
@Service
@Transactional
public class BillingConsumptionServiceImpl implements BillingConsumptionService {

    private final Logger log = LoggerFactory.getLogger(BillingConsumptionServiceImpl.class);

    private final BillingConsumptionRepository billingConsumptionRepository;

    private final BillingConsumptionMapper billingConsumptionMapper;

    public BillingConsumptionServiceImpl(
        BillingConsumptionRepository billingConsumptionRepository,
        BillingConsumptionMapper billingConsumptionMapper
    ) {
        this.billingConsumptionRepository = billingConsumptionRepository;
        this.billingConsumptionMapper = billingConsumptionMapper;
    }

    @Override
    public BillingConsumptionDTO save(BillingConsumptionDTO billingConsumptionDTO) {
        log.debug("Request to save BillingConsumption : {}", billingConsumptionDTO);
        BillingConsumption billingConsumption = billingConsumptionMapper.toEntity(billingConsumptionDTO);
        billingConsumption = billingConsumptionRepository.save(billingConsumption);
        return billingConsumptionMapper.toDto(billingConsumption);
    }

    @Override
    public Optional<BillingConsumptionDTO> partialUpdate(BillingConsumptionDTO billingConsumptionDTO) {
        log.debug("Request to partially update BillingConsumption : {}", billingConsumptionDTO);

        return billingConsumptionRepository
            .findById(billingConsumptionDTO.getId())
            .map(existingBillingConsumption -> {
                billingConsumptionMapper.partialUpdate(existingBillingConsumption, billingConsumptionDTO);

                return existingBillingConsumption;
            })
            .map(billingConsumptionRepository::save)
            .map(billingConsumptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BillingConsumptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BillingConsumptions");
        return billingConsumptionRepository.findAll(pageable).map(billingConsumptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BillingConsumptionDTO> findOne(Long id) {
        log.debug("Request to get BillingConsumption : {}", id);
        return billingConsumptionRepository.findById(id).map(billingConsumptionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BillingConsumption : {}", id);
        billingConsumptionRepository.deleteById(id);
    }
}
