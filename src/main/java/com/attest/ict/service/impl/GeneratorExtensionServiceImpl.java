package com.attest.ict.service.impl;

import com.attest.ict.domain.GeneratorExtension;
import com.attest.ict.repository.GeneratorExtensionRepository;
import com.attest.ict.service.GeneratorExtensionService;
import com.attest.ict.service.dto.GeneratorExtensionDTO;
import com.attest.ict.service.mapper.GeneratorExtensionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GeneratorExtension}.
 */
@Service
@Transactional
public class GeneratorExtensionServiceImpl implements GeneratorExtensionService {

    private final Logger log = LoggerFactory.getLogger(GeneratorExtensionServiceImpl.class);

    private final GeneratorExtensionRepository generatorExtensionRepository;

    private final GeneratorExtensionMapper generatorExtensionMapper;

    public GeneratorExtensionServiceImpl(
        GeneratorExtensionRepository generatorExtensionRepository,
        GeneratorExtensionMapper generatorExtensionMapper
    ) {
        this.generatorExtensionRepository = generatorExtensionRepository;
        this.generatorExtensionMapper = generatorExtensionMapper;
    }

    @Override
    public GeneratorExtensionDTO save(GeneratorExtensionDTO generatorExtensionDTO) {
        log.debug("Request to save GeneratorExtension : {}", generatorExtensionDTO);
        GeneratorExtension generatorExtension = generatorExtensionMapper.toEntity(generatorExtensionDTO);
        generatorExtension = generatorExtensionRepository.save(generatorExtension);
        return generatorExtensionMapper.toDto(generatorExtension);
    }

    @Override
    public Optional<GeneratorExtensionDTO> partialUpdate(GeneratorExtensionDTO generatorExtensionDTO) {
        log.debug("Request to partially update GeneratorExtension : {}", generatorExtensionDTO);

        return generatorExtensionRepository
            .findById(generatorExtensionDTO.getId())
            .map(existingGeneratorExtension -> {
                generatorExtensionMapper.partialUpdate(existingGeneratorExtension, generatorExtensionDTO);

                return existingGeneratorExtension;
            })
            .map(generatorExtensionRepository::save)
            .map(generatorExtensionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GeneratorExtensionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GeneratorExtensions");
        return generatorExtensionRepository.findAll(pageable).map(generatorExtensionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GeneratorExtensionDTO> findOne(Long id) {
        log.debug("Request to get GeneratorExtension : {}", id);
        return generatorExtensionRepository.findById(id).map(generatorExtensionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GeneratorExtension : {}", id);
        generatorExtensionRepository.deleteById(id);
    }
}
