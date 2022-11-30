package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.Topology;
import com.attest.ict.repository.TopologyRepository;
import com.attest.ict.service.criteria.TopologyCriteria;
import com.attest.ict.service.dto.TopologyDTO;
import com.attest.ict.service.mapper.TopologyMapper;
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
 * Service for executing complex queries for {@link Topology} entities in the database.
 * The main input is a {@link TopologyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TopologyDTO} or a {@link Page} of {@link TopologyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TopologyQueryService extends QueryService<Topology> {

    private final Logger log = LoggerFactory.getLogger(TopologyQueryService.class);

    private final TopologyRepository topologyRepository;

    private final TopologyMapper topologyMapper;

    public TopologyQueryService(TopologyRepository topologyRepository, TopologyMapper topologyMapper) {
        this.topologyRepository = topologyRepository;
        this.topologyMapper = topologyMapper;
    }

    /**
     * Return a {@link List} of {@link TopologyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TopologyDTO> findByCriteria(TopologyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Topology> specification = createSpecification(criteria);
        return topologyMapper.toDto(topologyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TopologyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TopologyDTO> findByCriteria(TopologyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Topology> specification = createSpecification(criteria);
        return topologyRepository.findAll(specification, page).map(topologyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TopologyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Topology> specification = createSpecification(criteria);
        return topologyRepository.count(specification);
    }

    /**
     * Function to convert {@link TopologyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Topology> createSpecification(TopologyCriteria criteria) {
        Specification<Topology> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Topology_.id));
            }
            if (criteria.getPowerLineBranch() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPowerLineBranch(), Topology_.powerLineBranch));
            }
            if (criteria.getp1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getp1(), Topology_.p1));
            }
            if (criteria.getp2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getp2(), Topology_.p2));
            }
            if (criteria.getPowerLineBranchParentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPowerLineBranchParentId(),
                            root -> root.join(Topology_.powerLineBranchParent, JoinType.LEFT).get(TopologyBus_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
