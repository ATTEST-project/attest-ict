package com.attest.ict.service.impl;

import com.attest.ict.domain.AssetUGCable;
import com.attest.ict.repository.AssetUGCableRepository;
import com.attest.ict.service.AssetUGCableService;
import com.attest.ict.service.dto.AssetUGCableDTO;
import com.attest.ict.service.mapper.AssetUGCableMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssetUGCable}.
 */
@Service
@Transactional
public class AssetUGCableServiceImpl implements AssetUGCableService {

    private final Logger log = LoggerFactory.getLogger(AssetUGCableServiceImpl.class);

    private final AssetUGCableRepository assetUGCableRepository;

    private final AssetUGCableMapper assetUGCableMapper;

    public AssetUGCableServiceImpl(AssetUGCableRepository assetUGCableRepository, AssetUGCableMapper assetUGCableMapper) {
        this.assetUGCableRepository = assetUGCableRepository;
        this.assetUGCableMapper = assetUGCableMapper;
    }

    @Override
    public AssetUGCableDTO save(AssetUGCableDTO assetUGCableDTO) {
        log.debug("Request to save AssetUGCable : {}", assetUGCableDTO);
        AssetUGCable assetUGCable = assetUGCableMapper.toEntity(assetUGCableDTO);
        assetUGCable = assetUGCableRepository.save(assetUGCable);
        return assetUGCableMapper.toDto(assetUGCable);
    }

    @Override
    public Optional<AssetUGCableDTO> partialUpdate(AssetUGCableDTO assetUGCableDTO) {
        log.debug("Request to partially update AssetUGCable : {}", assetUGCableDTO);

        return assetUGCableRepository
            .findById(assetUGCableDTO.getId())
            .map(existingAssetUGCable -> {
                assetUGCableMapper.partialUpdate(existingAssetUGCable, assetUGCableDTO);

                return existingAssetUGCable;
            })
            .map(assetUGCableRepository::save)
            .map(assetUGCableMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetUGCableDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetUGCables");
        return assetUGCableRepository.findAll(pageable).map(assetUGCableMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetUGCableDTO> findOne(Long id) {
        log.debug("Request to get AssetUGCable : {}", id);
        return assetUGCableRepository.findById(id).map(assetUGCableMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetUGCable : {}", id);
        assetUGCableRepository.deleteById(id);
    }
}
