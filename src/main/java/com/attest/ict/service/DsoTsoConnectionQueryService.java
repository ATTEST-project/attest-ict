package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.DsoTsoConnection;
import com.attest.ict.repository.DsoTsoConnectionRepository;
import com.attest.ict.service.criteria.DsoTsoConnectionCriteria;
import com.attest.ict.service.dto.DsoTsoConnectionDTO;
import com.attest.ict.service.mapper.DsoTsoConnectionMapper;
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
 * Service for executing complex queries for {@link DsoTsoConnection} entities in the database.
 * The main input is a {@link DsoTsoConnectionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DsoTsoConnectionDTO} or a {@link Page} of {@link DsoTsoConnectionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DsoTsoConnectionQueryService extends QueryService<DsoTsoConnection> {

    private final Logger log = LoggerFactory.getLogger(DsoTsoConnectionQueryService.class);

    private final DsoTsoConnectionRepository dsoTsoConnectionRepository;

    private final DsoTsoConnectionMapper dsoTsoConnectionMapper;

    public DsoTsoConnectionQueryService(
        DsoTsoConnectionRepository dsoTsoConnectionRepository,
        DsoTsoConnectionMapper dsoTsoConnectionMapper
    ) {
        this.dsoTsoConnectionRepository = dsoTsoConnectionRepository;
        this.dsoTsoConnectionMapper = dsoTsoConnectionMapper;
    }

    /**
     * Return a {@link List} of {@link DsoTsoConnectionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DsoTsoConnectionDTO> findByCriteria(DsoTsoConnectionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DsoTsoConnection> specification = createSpecification(criteria);
        return dsoTsoConnectionMapper.toDto(dsoTsoConnectionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DsoTsoConnectionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DsoTsoConnectionDTO> findByCriteria(DsoTsoConnectionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DsoTsoConnection> specification = createSpecification(criteria);
        return dsoTsoConnectionRepository.findAll(specification, page).map(dsoTsoConnectionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DsoTsoConnectionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DsoTsoConnection> specification = createSpecification(criteria);
        return dsoTsoConnectionRepository.count(specification);
    }

    /**
     * Function to convert {@link DsoTsoConnectionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DsoTsoConnection> createSpecification(DsoTsoConnectionCriteria criteria) {
        Specification<DsoTsoConnection> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DsoTsoConnection_.id));
            }
            if (criteria.getTsoNetworkName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTsoNetworkName(), DsoTsoConnection_.tsoNetworkName));
            }
            if (criteria.getDsoBusNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDsoBusNum(), DsoTsoConnection_.dsoBusNum));
            }
            if (criteria.getTsoBusNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTsoBusNum(), DsoTsoConnection_.tsoBusNum));
            }
            if (criteria.getDsoNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDsoNetworkId(),
                            root -> root.join(DsoTsoConnection_.dsoNetwork, JoinType.LEFT).get(Network_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
