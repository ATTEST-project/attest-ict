package com.attest.ict.service.impl;

import com.attest.ict.domain.Simulation;
import com.attest.ict.repository.SimulationRepository;
import com.attest.ict.service.SimulationService;
import com.attest.ict.service.dto.SimulationDTO;
import com.attest.ict.service.mapper.SimulationMapper;
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
 * Service Implementation for managing {@link Simulation}.
 */
@Service
@Transactional
public class SimulationServiceImpl implements SimulationService {

    private final Logger log = LoggerFactory.getLogger(SimulationServiceImpl.class);

    private final SimulationRepository simulationRepository;

    private final SimulationMapper simulationMapper;

    public SimulationServiceImpl(SimulationRepository simulationRepository, SimulationMapper simulationMapper) {
        this.simulationRepository = simulationRepository;
        this.simulationMapper = simulationMapper;
    }

    @Override
    public SimulationDTO save(SimulationDTO simulationDTO) {
        log.debug("Request to save Simulation : {}", simulationDTO);
        Simulation simulation = simulationMapper.toEntity(simulationDTO);
        simulation = simulationRepository.save(simulation);
        return simulationMapper.toDto(simulation);
    }

    @Override
    public Optional<SimulationDTO> partialUpdate(SimulationDTO simulationDTO) {
        log.debug("Request to partially update Simulation : {}", simulationDTO);

        return simulationRepository
            .findById(simulationDTO.getId())
            .map(existingSimulation -> {
                simulationMapper.partialUpdate(existingSimulation, simulationDTO);

                return existingSimulation;
            })
            .map(simulationRepository::save)
            .map(simulationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SimulationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Simulations");
        return simulationRepository.findAll(pageable).map(simulationMapper::toDto);
    }

    public Page<SimulationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return simulationRepository.findAllWithEagerRelationships(pageable).map(simulationMapper::toDto);
    }

    /**
     *  Get all the simulations where Task is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SimulationDTO> findAllWhereTaskIsNull() {
        log.debug("Request to get all simulations where Task is null");
        return StreamSupport
            .stream(simulationRepository.findAll().spliterator(), false)
            .filter(simulation -> simulation.getTask() == null)
            .map(simulationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SimulationDTO> findOne(Long id) {
        log.debug("Request to get Simulation : {}", id);
        return simulationRepository.findOneWithEagerRelationships(id).map(simulationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Simulation : {}", id);
        simulationRepository.deleteById(id);
    }
}
