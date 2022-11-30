package com.attest.ict.service.impl;

import com.attest.ict.domain.Generator;
import com.attest.ict.repository.GeneratorRepository;
import com.attest.ict.service.GeneratorService;
import com.attest.ict.service.dto.GeneratorDTO;
import com.attest.ict.service.mapper.GeneratorMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Generator}.
 */
@Service
@Transactional
public class GeneratorServiceImpl implements GeneratorService {

    private final Logger log = LoggerFactory.getLogger(GeneratorServiceImpl.class);

    private final GeneratorRepository generatorRepository;

    private final GeneratorMapper generatorMapper;

    public GeneratorServiceImpl(GeneratorRepository generatorRepository, GeneratorMapper generatorMapper) {
        this.generatorRepository = generatorRepository;
        this.generatorMapper = generatorMapper;
    }

    @Override
    public GeneratorDTO save(GeneratorDTO generatorDTO) {
        log.debug("Request to save Generator : {}", generatorDTO);
        Generator generator = generatorMapper.toEntity(generatorDTO);
        generator = generatorRepository.save(generator);
        return generatorMapper.toDto(generator);
    }

    @Override
    public Optional<GeneratorDTO> partialUpdate(GeneratorDTO generatorDTO) {
        log.debug("Request to partially update Generator : {}", generatorDTO);

        return generatorRepository
            .findById(generatorDTO.getId())
            .map(existingGenerator -> {
                generatorMapper.partialUpdate(existingGenerator, generatorDTO);

                return existingGenerator;
            })
            .map(generatorRepository::save)
            .map(generatorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GeneratorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Generators");
        return generatorRepository.findAll(pageable).map(generatorMapper::toDto);
    }

    /**
     *  Get all the generators where GeneratorExtension is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GeneratorDTO> findAllWhereGeneratorExtensionIsNull() {
        log.debug("Request to get all generators where GeneratorExtension is null");
        return StreamSupport
            .stream(generatorRepository.findAll().spliterator(), false)
            .filter(generator -> generator.getGeneratorExtension() == null)
            .map(generatorMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the generators where GenTag is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GeneratorDTO> findAllWhereGenTagIsNull() {
        log.debug("Request to get all generators where GenTag is null");
        return StreamSupport
            .stream(generatorRepository.findAll().spliterator(), false)
            .filter(generator -> generator.getGenTag() == null)
            .map(generatorMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the generators where GenCost is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GeneratorDTO> findAllWhereGenCostIsNull() {
        log.debug("Request to get all generators where GenCost is null");
        return StreamSupport
            .stream(generatorRepository.findAll().spliterator(), false)
            .filter(generator -> generator.getGenCost() == null)
            .map(generatorMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GeneratorDTO> findOne(Long id) {
        log.debug("Request to get Generator : {}", id);
        return generatorRepository.findById(id).map(generatorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Generator : {}", id);
        generatorRepository.deleteById(id);
    }

    //======= Start Custom Model

    @Override
    public List<Generator> getAllGenerators() {
        return generatorRepository.findAll();
    }

    @Override
    public List<Generator> findByNetworkId(Long networkId) {
        return generatorRepository.findByNetworkIdOrderByIdAsc(networkId);
    }

    @Override
    public List<Generator> saveAll(List<Generator> generators) {
        return generatorRepository.saveAll(generators);
    }

    @Override
    public List<Generator> findByNetworkName(String networkName) {
        return generatorRepository.findByNetworkName(networkName);
    }

    @Override
    public List<Generator> findByBusNumAndNetworkName(Long busNum, String networkName) {
        return generatorRepository.findByBusNumAndNetworkName(busNum, networkName);
    }

    @Override
    public List<Generator> findByBusNumAndNetworkId(Long busNum, Long networkId) {
        return generatorRepository.findByBusNumAndNetworkIdOrderByIdAsc(busNum, networkId);
    }

    @Override
    public Optional<Generator> findByIdAndNetworkId(Long genId, Long networkId) {
        return generatorRepository.findByIdAndNetworkId(genId, networkId);
    }
}
