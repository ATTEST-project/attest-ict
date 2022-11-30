package com.attest.ict.service.impl;

import com.attest.ict.domain.Transformer;
import com.attest.ict.repository.TransformerRepository;
import com.attest.ict.service.TransformerService;
import com.attest.ict.service.dto.TransformerDTO;
import com.attest.ict.service.mapper.TransformerMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Transformer}.
 */
@Service
@Transactional
public class TransformerServiceImpl implements TransformerService {

    private final Logger log = LoggerFactory.getLogger(TransformerServiceImpl.class);

    private final TransformerRepository transformerRepository;

    private final TransformerMapper transformerMapper;

    public TransformerServiceImpl(TransformerRepository transformerRepository, TransformerMapper transformerMapper) {
        this.transformerRepository = transformerRepository;
        this.transformerMapper = transformerMapper;
    }

    @Override
    public TransformerDTO save(TransformerDTO transformerDTO) {
        log.debug("Request to save Transformer : {}", transformerDTO);
        Transformer transformer = transformerMapper.toEntity(transformerDTO);
        transformer = transformerRepository.save(transformer);
        return transformerMapper.toDto(transformer);
    }

    @Override
    public Optional<TransformerDTO> partialUpdate(TransformerDTO transformerDTO) {
        log.debug("Request to partially update Transformer : {}", transformerDTO);

        return transformerRepository
            .findById(transformerDTO.getId())
            .map(existingTransformer -> {
                transformerMapper.partialUpdate(existingTransformer, transformerDTO);

                return existingTransformer;
            })
            .map(transformerRepository::save)
            .map(transformerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransformerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Transformers");
        return transformerRepository.findAll(pageable).map(transformerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransformerDTO> findOne(Long id) {
        log.debug("Request to get Transformer : {}", id);
        return transformerRepository.findById(id).map(transformerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Transformer : {}", id);
        transformerRepository.deleteById(id);
    }
}
