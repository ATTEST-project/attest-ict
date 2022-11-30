package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.FlexCost;
import com.attest.ict.repository.FlexCostRepository;
import com.attest.ict.service.criteria.FlexCostCriteria;
import com.attest.ict.service.dto.FlexCostDTO;
import com.attest.ict.service.mapper.FlexCostMapper;
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
 * Service for executing complex queries for {@link FlexCost} entities in the database.
 * The main input is a {@link FlexCostCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FlexCostDTO} or a {@link Page} of {@link FlexCostDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FlexCostQueryService extends QueryService<FlexCost> {

    private final Logger log = LoggerFactory.getLogger(FlexCostQueryService.class);

    private final FlexCostRepository flexCostRepository;

    private final FlexCostMapper flexCostMapper;

    public FlexCostQueryService(FlexCostRepository flexCostRepository, FlexCostMapper flexCostMapper) {
        this.flexCostRepository = flexCostRepository;
        this.flexCostMapper = flexCostMapper;
    }

    /**
     * Return a {@link List} of {@link FlexCostDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FlexCostDTO> findByCriteria(FlexCostCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FlexCost> specification = createSpecification(criteria);
        return flexCostMapper.toDto(flexCostRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FlexCostDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FlexCostDTO> findByCriteria(FlexCostCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FlexCost> specification = createSpecification(criteria);
        return flexCostRepository.findAll(specification, page).map(flexCostMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FlexCostCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FlexCost> specification = createSpecification(criteria);
        return flexCostRepository.count(specification);
    }

    /**
     * Function to convert {@link FlexCostCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FlexCost> createSpecification(FlexCostCriteria criteria) {
        Specification<FlexCost> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FlexCost_.id));
            }
            if (criteria.getBusNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBusNum(), FlexCost_.busNum));
            }
            if (criteria.getModel() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModel(), FlexCost_.model));
            }
            if (criteria.getnCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getnCost(), FlexCost_.nCost));
            }
            if (criteria.getCostPr() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCostPr(), FlexCost_.costPr));
            }
            if (criteria.getCostQr() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCostQr(), FlexCost_.costQr));
            }
            if (criteria.getCostPf() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCostPf(), FlexCost_.costPf));
            }
            if (criteria.getCostQf() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCostQf(), FlexCost_.costQf));
            }
            if (criteria.getFlexProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFlexProfileId(),
                            root -> root.join(FlexCost_.flexProfile, JoinType.LEFT).get(FlexProfile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
