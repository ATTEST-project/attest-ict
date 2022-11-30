package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.Simulation;
import com.attest.ict.repository.SimulationRepository;
import com.attest.ict.service.criteria.SimulationCriteria;
import com.attest.ict.service.dto.SimulationDTO;
import com.attest.ict.service.mapper.SimulationMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Simulation} entities in the database.
 * The main input is a {@link SimulationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SimulationDTO} or a {@link Page} of {@link SimulationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SimulationQueryService extends QueryService<Simulation> {

    private final Logger log = LoggerFactory.getLogger(SimulationQueryService.class);

    private final SimulationRepository simulationRepository;

    private final SimulationMapper simulationMapper;

    public SimulationQueryService(SimulationRepository simulationRepository, SimulationMapper simulationMapper) {
        this.simulationRepository = simulationRepository;
        this.simulationMapper = simulationMapper;
    }

    /**
     * Return a {@link List} of {@link SimulationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SimulationDTO> findByCriteria(SimulationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Simulation> specification = createSpecification(criteria);
        return simulationMapper.toDto(simulationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SimulationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SimulationDTO> findByCriteria(SimulationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Simulation> specification = createSpecification(criteria);
        return simulationRepository.findAll(specification, page).map(simulationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SimulationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Simulation> specification = createSpecification(criteria);
        return simulationRepository.count(specification);
    }

    /**
     * Function to convert {@link SimulationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Simulation> createSpecification(SimulationCriteria criteria) {
        Specification<Simulation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Simulation_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), Simulation_.uuid));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Simulation_.description));
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNetworkId(), root -> root.join(Simulation_.network, JoinType.LEFT).get(Network_.id))
                    );
            }
            if (criteria.getInputFileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInputFileId(),
                            root -> root.join(Simulation_.inputFiles, JoinType.LEFT).get(InputFile_.id)
                        )
                    );
            }
            if (criteria.getTaskId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTaskId(), root -> root.join(Simulation_.task, JoinType.LEFT).get(Task_.id))
                    );
            }
            if (criteria.getOutputFileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOutputFileId(),
                            root -> root.join(Simulation_.outputFiles, JoinType.LEFT).get(OutputFile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
