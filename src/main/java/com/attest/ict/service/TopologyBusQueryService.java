package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.TopologyBus;
import com.attest.ict.repository.TopologyBusRepository;
import com.attest.ict.service.criteria.TopologyBusCriteria;
import com.attest.ict.service.dto.TopologyBusDTO;
import com.attest.ict.service.mapper.TopologyBusMapper;
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
 * Service for executing complex queries for {@link TopologyBus} entities in the database.
 * The main input is a {@link TopologyBusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TopologyBusDTO} or a {@link Page} of {@link TopologyBusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TopologyBusQueryService extends QueryService<TopologyBus> {

    private final Logger log = LoggerFactory.getLogger(TopologyBusQueryService.class);

    private final TopologyBusRepository topologyBusRepository;

    private final TopologyBusMapper topologyBusMapper;

    public TopologyBusQueryService(TopologyBusRepository topologyBusRepository, TopologyBusMapper topologyBusMapper) {
        this.topologyBusRepository = topologyBusRepository;
        this.topologyBusMapper = topologyBusMapper;
    }

    /**
     * Return a {@link List} of {@link TopologyBusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TopologyBusDTO> findByCriteria(TopologyBusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TopologyBus> specification = createSpecification(criteria);
        return topologyBusMapper.toDto(topologyBusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TopologyBusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TopologyBusDTO> findByCriteria(TopologyBusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TopologyBus> specification = createSpecification(criteria);
        return topologyBusRepository.findAll(specification, page).map(topologyBusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TopologyBusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TopologyBus> specification = createSpecification(criteria);
        return topologyBusRepository.count(specification);
    }

    /**
     * Function to convert {@link TopologyBusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TopologyBus> createSpecification(TopologyBusCriteria criteria) {
        Specification<TopologyBus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TopologyBus_.id));
            }
            if (criteria.getPowerLineBranch() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPowerLineBranch(), TopologyBus_.powerLineBranch));
            }
            if (criteria.getBusName1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBusName1(), TopologyBus_.busName1));
            }
            if (criteria.getBusName2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBusName2(), TopologyBus_.busName2));
            }
            if (criteria.getTopologyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTopologyId(),
                            root -> root.join(TopologyBus_.topologies, JoinType.LEFT).get(Topology_.id)
                        )
                    );
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNetworkId(), root -> root.join(TopologyBus_.network, JoinType.LEFT).get(Network_.id))
                    );
            }
        }
        return specification;
    }
}
