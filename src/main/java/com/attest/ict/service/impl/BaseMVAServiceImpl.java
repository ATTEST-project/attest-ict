package com.attest.ict.service.impl;

import com.attest.ict.domain.BaseMVA;
import com.attest.ict.repository.BaseMVARepository;
import com.attest.ict.service.BaseMVAService;
import com.attest.ict.service.dto.BaseMVADTO;
import com.attest.ict.service.mapper.BaseMVAMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BaseMVA}.
 */
@Service
@Transactional
public class BaseMVAServiceImpl implements BaseMVAService {

    private final Logger log = LoggerFactory.getLogger(BaseMVAServiceImpl.class);

    private final BaseMVARepository baseMVARepository;

    private final BaseMVAMapper baseMVAMapper;

    public BaseMVAServiceImpl(BaseMVARepository baseMVARepository, BaseMVAMapper baseMVAMapper) {
        this.baseMVARepository = baseMVARepository;
        this.baseMVAMapper = baseMVAMapper;
    }

    @Override
    public BaseMVADTO save(BaseMVADTO baseMVADTO) {
        log.debug("Request to save BaseMVA : {}", baseMVADTO);
        BaseMVA baseMVA = baseMVAMapper.toEntity(baseMVADTO);
        baseMVA = baseMVARepository.save(baseMVA);
        return baseMVAMapper.toDto(baseMVA);
    }

    @Override
    public Optional<BaseMVADTO> partialUpdate(BaseMVADTO baseMVADTO) {
        log.debug("Request to partially update BaseMVA : {}", baseMVADTO);

        return baseMVARepository
            .findById(baseMVADTO.getId())
            .map(existingBaseMVA -> {
                baseMVAMapper.partialUpdate(existingBaseMVA, baseMVADTO);

                return existingBaseMVA;
            })
            .map(baseMVARepository::save)
            .map(baseMVAMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BaseMVADTO> findAll(Pageable pageable) {
        log.debug("Request to get all BaseMVAS");
        return baseMVARepository.findAll(pageable).map(baseMVAMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BaseMVADTO> findOne(Long id) {
        log.debug("Request to get BaseMVA : {}", id);
        return baseMVARepository.findById(id).map(baseMVAMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BaseMVA : {}", id);
        baseMVARepository.deleteById(id);
    }

    //Start Custom Methods
    @Override
    public List<BaseMVA> getAllBaseMVA() {
        return baseMVARepository.findAll();
    }

    @Override
    public BaseMVA getBaseMVAByNetworkId(Long networkId) {
        return baseMVARepository.findByNetworkId(networkId);
    }

    @Override
    public List<BaseMVA> saveAll(List<BaseMVA> list) {
        return baseMVARepository.saveAll(list);
    }
}
