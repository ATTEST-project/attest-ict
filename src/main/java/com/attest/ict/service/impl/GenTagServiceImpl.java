package com.attest.ict.service.impl;

import com.attest.ict.domain.GenTag;
import com.attest.ict.repository.GenTagRepository;
import com.attest.ict.service.GenTagService;
import com.attest.ict.service.dto.GenTagDTO;
import com.attest.ict.service.mapper.GenTagMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GenTag}.
 */
@Service
@Transactional
public class GenTagServiceImpl implements GenTagService {

    private final Logger log = LoggerFactory.getLogger(GenTagServiceImpl.class);

    private final GenTagRepository genTagRepository;

    private final GenTagMapper genTagMapper;

    public GenTagServiceImpl(GenTagRepository genTagRepository, GenTagMapper genTagMapper) {
        this.genTagRepository = genTagRepository;
        this.genTagMapper = genTagMapper;
    }

    @Override
    public GenTagDTO save(GenTagDTO genTagDTO) {
        log.debug("Request to save GenTag : {}", genTagDTO);
        GenTag genTag = genTagMapper.toEntity(genTagDTO);
        genTag = genTagRepository.save(genTag);
        return genTagMapper.toDto(genTag);
    }

    @Override
    public Optional<GenTagDTO> partialUpdate(GenTagDTO genTagDTO) {
        log.debug("Request to partially update GenTag : {}", genTagDTO);

        return genTagRepository
            .findById(genTagDTO.getId())
            .map(existingGenTag -> {
                genTagMapper.partialUpdate(existingGenTag, genTagDTO);

                return existingGenTag;
            })
            .map(genTagRepository::save)
            .map(genTagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GenTagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GenTags");
        return genTagRepository.findAll(pageable).map(genTagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GenTagDTO> findOne(Long id) {
        log.debug("Request to get GenTag : {}", id);
        return genTagRepository.findById(id).map(genTagMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GenTag : {}", id);
        genTagRepository.deleteById(id);
    }
}
