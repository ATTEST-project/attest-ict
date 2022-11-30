package com.attest.ict.service.impl;

import com.attest.ict.domain.AssetTransformer;
import com.attest.ict.repository.AssetTransformerRepository;
import com.attest.ict.service.AssetTransformerService;
import com.attest.ict.service.dto.AssetTransformerDTO;
import com.attest.ict.service.mapper.AssetTransformerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssetTransformer}.
 */
@Service
@Transactional
public class AssetTransformerServiceImpl implements AssetTransformerService {

    private final Logger log = LoggerFactory.getLogger(AssetTransformerServiceImpl.class);

    private final AssetTransformerRepository assetTransformerRepository;

    private final AssetTransformerMapper assetTransformerMapper;

    public AssetTransformerServiceImpl(
        AssetTransformerRepository assetTransformerRepository,
        AssetTransformerMapper assetTransformerMapper
    ) {
        this.assetTransformerRepository = assetTransformerRepository;
        this.assetTransformerMapper = assetTransformerMapper;
    }

    @Override
    public AssetTransformerDTO save(AssetTransformerDTO assetTransformerDTO) {
        log.debug("Request to save AssetTransformer : {}", assetTransformerDTO);
        AssetTransformer assetTransformer = assetTransformerMapper.toEntity(assetTransformerDTO);
        assetTransformer = assetTransformerRepository.save(assetTransformer);
        return assetTransformerMapper.toDto(assetTransformer);
    }

    @Override
    public Optional<AssetTransformerDTO> partialUpdate(AssetTransformerDTO assetTransformerDTO) {
        log.debug("Request to partially update AssetTransformer : {}", assetTransformerDTO);

        return assetTransformerRepository
            .findById(assetTransformerDTO.getId())
            .map(existingAssetTransformer -> {
                assetTransformerMapper.partialUpdate(existingAssetTransformer, assetTransformerDTO);

                return existingAssetTransformer;
            })
            .map(assetTransformerRepository::save)
            .map(assetTransformerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetTransformerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetTransformers");
        return assetTransformerRepository.findAll(pageable).map(assetTransformerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetTransformerDTO> findOne(Long id) {
        log.debug("Request to get AssetTransformer : {}", id);
        return assetTransformerRepository.findById(id).map(assetTransformerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetTransformer : {}", id);
        assetTransformerRepository.deleteById(id);
    }
}
