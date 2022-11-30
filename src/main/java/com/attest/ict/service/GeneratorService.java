package com.attest.ict.service;

import com.attest.ict.domain.Generator;
import com.attest.ict.service.dto.GeneratorDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.attest.ict.domain.Generator}.
 */
public interface GeneratorService {
    /**
     * Save a generator.
     *
     * @param generatorDTO the entity to save.
     * @return the persisted entity.
     */
    GeneratorDTO save(GeneratorDTO generatorDTO);

    /**
     * Partially updates a generator.
     *
     * @param generatorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GeneratorDTO> partialUpdate(GeneratorDTO generatorDTO);

    /**
     * Get all the generators.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GeneratorDTO> findAll(Pageable pageable);
    /**
     * Get all the GeneratorDTO where GeneratorExtension is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<GeneratorDTO> findAllWhereGeneratorExtensionIsNull();
    /**
     * Get all the GeneratorDTO where GenTag is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<GeneratorDTO> findAllWhereGenTagIsNull();
    /**
     * Get all the GeneratorDTO where GenCost is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<GeneratorDTO> findAllWhereGenCostIsNull();

    /**
     * Get the "id" generator.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GeneratorDTO> findOne(Long id);

    /**
     * Delete the "id" generator.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    //======= Start Custom Methods

    List<Generator> getAllGenerators();

    List<Generator> findByNetworkId(Long networkId);

    List<Generator> saveAll(List<Generator> generators);

    List<Generator> findByNetworkName(String networkName);

    //One bus more than one generator (e.g: HR_Dx_05_2020)
    List<Generator> findByBusNumAndNetworkName(Long busNum, String networkName);

    //One bus more than one generator
    List<Generator> findByBusNumAndNetworkId(Long busNum, Long networkId);

    Optional<Generator> findByIdAndNetworkId(Long genId, Long networkId);
}
