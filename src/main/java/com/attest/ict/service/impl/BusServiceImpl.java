package com.attest.ict.service.impl;

import com.attest.ict.domain.Bus;
import com.attest.ict.repository.BusRepository;
import com.attest.ict.service.BusService;
import com.attest.ict.service.dto.BusDTO;
import com.attest.ict.service.mapper.BusMapper;
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
 * Service Implementation for managing {@link Bus}.
 */
@Service
@Transactional
public class BusServiceImpl implements BusService {

    private final Logger log = LoggerFactory.getLogger(BusServiceImpl.class);

    private final BusRepository busRepository;

    private final BusMapper busMapper;

    public BusServiceImpl(BusRepository busRepository, BusMapper busMapper) {
        this.busRepository = busRepository;
        this.busMapper = busMapper;
    }

    @Override
    public BusDTO save(BusDTO busDTO) {
        log.debug("Request to save Bus : {}", busDTO);
        Bus bus = busMapper.toEntity(busDTO);
        bus = busRepository.save(bus);
        return busMapper.toDto(bus);
    }

    @Override
    public Optional<BusDTO> partialUpdate(BusDTO busDTO) {
        log.debug("Request to partially update Bus : {}", busDTO);

        return busRepository
            .findById(busDTO.getId())
            .map(existingBus -> {
                busMapper.partialUpdate(existingBus, busDTO);

                return existingBus;
            })
            .map(busRepository::save)
            .map(busMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Buses");
        return busRepository.findAll(pageable).map(busMapper::toDto);
    }

    /**
     *  Get all the buses where BusName is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BusDTO> findAllWhereBusNameIsNull() {
        log.debug("Request to get all buses where BusName is null");
        return StreamSupport
            .stream(busRepository.findAll().spliterator(), false)
            .filter(bus -> bus.getBusName() == null)
            .map(busMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the buses where BusExtension is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BusDTO> findAllWhereBusExtensionIsNull() {
        log.debug("Request to get all buses where BusExtension is null");
        return StreamSupport
            .stream(busRepository.findAll().spliterator(), false)
            .filter(bus -> bus.getBusExtension() == null)
            .map(busMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the buses where BusCoordinate is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BusDTO> findAllWhereBusCoordinateIsNull() {
        log.debug("Request to get all buses where BusCoordinate is null");
        return StreamSupport
            .stream(busRepository.findAll().spliterator(), false)
            .filter(bus -> bus.getBusCoordinate() == null)
            .map(busMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BusDTO> findOne(Long id) {
        log.debug("Request to get Bus : {}", id);
        return busRepository.findById(id).map(busMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Bus : {}", id);
        busRepository.deleteById(id);
    }

    //====== Start Custom Methods

    @Override
    public List<Bus> getBusesByNetworkId(Long networkId) {
        return busRepository.findByNetworkId(networkId);
    }

    @Override
    public List<Bus> getLoadsByNetworkId(Long networkId, double minP, double minQ) {
        return busRepository.findByNetworkIdAndNotActivePowerReactivePower(networkId, minP, minQ);
    }

    @Override
    public Optional<Bus> findByBusNumAndNetworkName(Long busNum, String networkName) {
        return busRepository.findByBusNumAndNetworkName(busNum, networkName);
    }

    @Override
    public Bus findByBusNumAndNetworkId(Long busNum, Long networkId) {
        return busRepository.findByBusNumAndNetworkId(busNum, networkId);
    }

    @Override
    public List<Bus> saveAll(List<Bus> buses) {
        return busRepository.saveAll(buses);
    }
}
