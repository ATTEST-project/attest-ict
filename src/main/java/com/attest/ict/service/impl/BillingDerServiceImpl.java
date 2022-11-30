package com.attest.ict.service.impl;

import com.attest.ict.domain.BillingDer;
import com.attest.ict.repository.BillingDerRepository;
import com.attest.ict.service.BillingDerService;
import com.attest.ict.service.dto.BillingDerDTO;
import com.attest.ict.service.mapper.BillingDerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BillingDer}.
 */
@Service
@Transactional
public class BillingDerServiceImpl implements BillingDerService {

    private final Logger log = LoggerFactory.getLogger(BillingDerServiceImpl.class);

    private final BillingDerRepository billingDerRepository;

    private final BillingDerMapper billingDerMapper;

    public BillingDerServiceImpl(BillingDerRepository billingDerRepository, BillingDerMapper billingDerMapper) {
        this.billingDerRepository = billingDerRepository;
        this.billingDerMapper = billingDerMapper;
    }

    @Override
    public BillingDerDTO save(BillingDerDTO billingDerDTO) {
        log.debug("Request to save BillingDer : {}", billingDerDTO);
        BillingDer billingDer = billingDerMapper.toEntity(billingDerDTO);
        billingDer = billingDerRepository.save(billingDer);
        return billingDerMapper.toDto(billingDer);
    }

    @Override
    public Optional<BillingDerDTO> partialUpdate(BillingDerDTO billingDerDTO) {
        log.debug("Request to partially update BillingDer : {}", billingDerDTO);

        return billingDerRepository
            .findById(billingDerDTO.getId())
            .map(existingBillingDer -> {
                billingDerMapper.partialUpdate(existingBillingDer, billingDerDTO);

                return existingBillingDer;
            })
            .map(billingDerRepository::save)
            .map(billingDerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BillingDerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BillingDers");
        return billingDerRepository.findAll(pageable).map(billingDerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BillingDerDTO> findOne(Long id) {
        log.debug("Request to get BillingDer : {}", id);
        return billingDerRepository.findById(id).map(billingDerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BillingDer : {}", id);
        billingDerRepository.deleteById(id);
    }
}
