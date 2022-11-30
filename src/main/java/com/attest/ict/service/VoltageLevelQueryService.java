package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.VoltageLevel;
import com.attest.ict.repository.VoltageLevelRepository;
import com.attest.ict.service.criteria.VoltageLevelCriteria;
import com.attest.ict.service.dto.VoltageLevelDTO;
import com.attest.ict.service.mapper.VoltageLevelMapper;
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
 * Service for executing complex queries for {@link VoltageLevel} entities in the database.
 * The main input is a {@link VoltageLevelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VoltageLevelDTO} or a {@link Page} of {@link VoltageLevelDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VoltageLevelQueryService extends QueryService<VoltageLevel> {

    private final Logger log = LoggerFactory.getLogger(VoltageLevelQueryService.class);

    private final VoltageLevelRepository voltageLevelRepository;

    private final VoltageLevelMapper voltageLevelMapper;

    public VoltageLevelQueryService(VoltageLevelRepository voltageLevelRepository, VoltageLevelMapper voltageLevelMapper) {
        this.voltageLevelRepository = voltageLevelRepository;
        this.voltageLevelMapper = voltageLevelMapper;
    }

    /**
     * Return a {@link List} of {@link VoltageLevelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VoltageLevelDTO> findByCriteria(VoltageLevelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<VoltageLevel> specification = createSpecification(criteria);
        return voltageLevelMapper.toDto(voltageLevelRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VoltageLevelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VoltageLevelDTO> findByCriteria(VoltageLevelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<VoltageLevel> specification = createSpecification(criteria);
        return voltageLevelRepository.findAll(specification, page).map(voltageLevelMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VoltageLevelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<VoltageLevel> specification = createSpecification(criteria);
        return voltageLevelRepository.count(specification);
    }

    /**
     * Function to convert {@link VoltageLevelCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<VoltageLevel> createSpecification(VoltageLevelCriteria criteria) {
        Specification<VoltageLevel> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), VoltageLevel_.id));
            }
            if (criteria.getv1() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getv1(), VoltageLevel_.v1));
            }
            if (criteria.getv2() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getv2(), VoltageLevel_.v2));
            }
            if (criteria.getv3() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getv3(), VoltageLevel_.v3));
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNetworkId(),
                            root -> root.join(VoltageLevel_.network, JoinType.LEFT).get(Network_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
