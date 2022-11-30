package com.attest.ict.service.impl;

import com.attest.ict.domain.TransfProfile;
import com.attest.ict.repository.TransfProfileRepository;
import com.attest.ict.service.TransfProfileService;
import com.attest.ict.service.dto.TransfProfileDTO;
import com.attest.ict.service.mapper.TransfProfileMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TransfProfile}.
 */
@Service
@Transactional
public class TransfProfileServiceImpl implements TransfProfileService {

    private final Logger log = LoggerFactory.getLogger(TransfProfileServiceImpl.class);

    private final TransfProfileRepository transfProfileRepository;

    private final TransfProfileMapper transfProfileMapper;

    public TransfProfileServiceImpl(TransfProfileRepository transfProfileRepository, TransfProfileMapper transfProfileMapper) {
        this.transfProfileRepository = transfProfileRepository;
        this.transfProfileMapper = transfProfileMapper;
    }

    @Override
    public TransfProfileDTO save(TransfProfileDTO transfProfileDTO) {
        log.debug("Request to save TransfProfile : {}", transfProfileDTO);
        TransfProfile transfProfile = transfProfileMapper.toEntity(transfProfileDTO);
        transfProfile = transfProfileRepository.save(transfProfile);
        return transfProfileMapper.toDto(transfProfile);
    }

    @Override
    public Optional<TransfProfileDTO> partialUpdate(TransfProfileDTO transfProfileDTO) {
        log.debug("Request to partially update TransfProfile : {}", transfProfileDTO);

        return transfProfileRepository
            .findById(transfProfileDTO.getId())
            .map(existingTransfProfile -> {
                transfProfileMapper.partialUpdate(existingTransfProfile, transfProfileDTO);

                return existingTransfProfile;
            })
            .map(transfProfileRepository::save)
            .map(transfProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransfProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransfProfiles");
        return transfProfileRepository.findAll(pageable).map(transfProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransfProfileDTO> findOne(Long id) {
        log.debug("Request to get TransfProfile : {}", id);
        return transfProfileRepository.findById(id).map(transfProfileMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransfProfile : {}", id);
        transfProfileRepository.deleteById(id);
    }
}
