package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.FlexElVal;
import com.attest.ict.repository.FlexElValRepository;
import com.attest.ict.service.criteria.FlexElValCriteria;
import com.attest.ict.service.dto.FlexElValDTO;
import com.attest.ict.service.mapper.FlexElValMapper;
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
 * Service for executing complex queries for {@link FlexElVal} entities in the database.
 * The main input is a {@link FlexElValCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FlexElValDTO} or a {@link Page} of {@link FlexElValDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FlexElValQueryService extends QueryService<FlexElVal> {

    private final Logger log = LoggerFactory.getLogger(FlexElValQueryService.class);

    private final FlexElValRepository flexElValRepository;

    private final FlexElValMapper flexElValMapper;

    public FlexElValQueryService(FlexElValRepository flexElValRepository, FlexElValMapper flexElValMapper) {
        this.flexElValRepository = flexElValRepository;
        this.flexElValMapper = flexElValMapper;
    }

    /**
     * Return a {@link List} of {@link FlexElValDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FlexElValDTO> findByCriteria(FlexElValCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FlexElVal> specification = createSpecification(criteria);
        return flexElValMapper.toDto(flexElValRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FlexElValDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FlexElValDTO> findByCriteria(FlexElValCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FlexElVal> specification = createSpecification(criteria);
        return flexElValRepository.findAll(specification, page).map(flexElValMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FlexElValCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FlexElVal> specification = createSpecification(criteria);
        return flexElValRepository.count(specification);
    }

    /**
     * Function to convert {@link FlexElValCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FlexElVal> createSpecification(FlexElValCriteria criteria) {
        Specification<FlexElVal> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FlexElVal_.id));
            }
            if (criteria.getBusNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBusNum(), FlexElVal_.busNum));
            }
            if (criteria.getHour() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHour(), FlexElVal_.hour));
            }
            if (criteria.getMin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMin(), FlexElVal_.min));
            }
            if (criteria.getPfmaxUp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPfmaxUp(), FlexElVal_.pfmaxUp));
            }
            if (criteria.getPfmaxDn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPfmaxDn(), FlexElVal_.pfmaxDn));
            }
            if (criteria.getQfmaxUp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQfmaxUp(), FlexElVal_.qfmaxUp));
            }
            if (criteria.getQfmaxDn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQfmaxDn(), FlexElVal_.qfmaxDn));
            }
            if (criteria.getFlexProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFlexProfileId(),
                            root -> root.join(FlexElVal_.flexProfile, JoinType.LEFT).get(FlexProfile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
