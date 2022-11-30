package com.attest.ict.service.impl;

import com.attest.ict.domain.BranchExtension;
import com.attest.ict.repository.BranchExtensionRepository;
import com.attest.ict.service.BranchExtensionService;
import com.attest.ict.service.dto.BranchExtensionDTO;
import com.attest.ict.service.dto.custom.BranchCustomDTO;
import com.attest.ict.service.mapper.BranchExtensionMapper;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BranchExtension}.
 */
@Service
@Transactional
public class BranchExtensionServiceImpl implements BranchExtensionService {

    private final Logger log = LoggerFactory.getLogger(BranchExtensionServiceImpl.class);

    private final BranchExtensionRepository branchExtensionRepository;

    private final BranchExtensionMapper branchExtensionMapper;

    public BranchExtensionServiceImpl(BranchExtensionRepository branchExtensionRepository, BranchExtensionMapper branchExtensionMapper) {
        this.branchExtensionRepository = branchExtensionRepository;
        this.branchExtensionMapper = branchExtensionMapper;
    }

    @Override
    public BranchExtensionDTO save(BranchExtensionDTO branchExtensionDTO) {
        log.debug("Request to save BranchExtension : {}", branchExtensionDTO);
        BranchExtension branchExtension = branchExtensionMapper.toEntity(branchExtensionDTO);
        branchExtension = branchExtensionRepository.save(branchExtension);
        return branchExtensionMapper.toDto(branchExtension);
    }

    @Override
    public Optional<BranchExtensionDTO> partialUpdate(BranchExtensionDTO branchExtensionDTO) {
        log.debug("Request to partially update BranchExtension : {}", branchExtensionDTO);

        return branchExtensionRepository
            .findById(branchExtensionDTO.getId())
            .map(existingBranchExtension -> {
                branchExtensionMapper.partialUpdate(existingBranchExtension, branchExtensionDTO);

                return existingBranchExtension;
            })
            .map(branchExtensionRepository::save)
            .map(branchExtensionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BranchExtensionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BranchExtensions");
        return branchExtensionRepository.findAll(pageable).map(branchExtensionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BranchExtensionDTO> findOne(Long id) {
        log.debug("Request to get BranchExtension : {}", id);
        return branchExtensionRepository.findById(id).map(branchExtensionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BranchExtension : {}", id);
        branchExtensionRepository.deleteById(id);
    }

    @Override
    public List<BranchExtension> findByNetworkIdAndLengthGreatherThen(Long networkId, Double lengthMin) {
        log.debug("Request to obtain BranchExtensions for networkId: {} with length greatherThan: {} ", networkId, lengthMin);
        return branchExtensionRepository.findByNetworkIdAndLengthGreatherThanOrderbyBranchId(networkId, lengthMin);
    }

    @Override
    public List<BranchExtension> findByNetworkId(@Param("networkId") Long networkId) {
        log.debug("Request to obtain BranchExtensions  for networkId: {} ", networkId);
        return branchExtensionRepository.findByNetworkIdOrderByBranchId(networkId);
    }

    @Override
    public List<BranchCustomDTO> findLengthByNetworkId(@Param("networkId") Long networkId) {
        log.debug("Request to obtain branch's length for networkId: {} ", networkId);
        List<Tuple> lenghts = branchExtensionRepository.findLengthsByNetworkId(networkId);
        List<BranchCustomDTO> branchDtoList = lenghts
            .stream()
            .map(t ->
                new BranchCustomDTO(
                    t.get(0, BigInteger.class), // branchExtensionId
                    t.get(1, BigInteger.class), // branchId
                    t.get(2, BigInteger.class), //fromBus
                    t.get(3, BigInteger.class), //toBus
                    t.get(4, Double.class) //kmLength
                )
            )
            .collect(Collectors.toList());
        return branchDtoList;
    }

    @Override
    public List<BranchCustomDTO> findLengthByNetworkIdAndLengthGreatherThen(Long networkId, Double lengthMin) {
        log.debug("Request to obtain branch's length for networkId: {} with length greatherThan: {} ", networkId, lengthMin);
        List<Tuple> lenghts = branchExtensionRepository.findLengthsByNetworkIdAndLengthGreatherThanOrderbyBranchId(networkId, lengthMin);
        List<BranchCustomDTO> branchDtoList = lenghts
            .stream()
            .map(t ->
                new BranchCustomDTO(
                    t.get(0, BigInteger.class), //branchExtensionId
                    t.get(1, BigInteger.class), //branchId
                    t.get(2, BigInteger.class), //fromBus
                    t.get(3, BigInteger.class), //toBus
                    t.get(4, Double.class) //kmLength
                )
            )
            .collect(Collectors.toList());
        return branchDtoList;
    }
}
