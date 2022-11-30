package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.BillingConsumption;
import com.attest.ict.repository.BillingConsumptionRepository;
import com.attest.ict.service.criteria.BillingConsumptionCriteria;
import com.attest.ict.service.dto.BillingConsumptionDTO;
import com.attest.ict.service.mapper.BillingConsumptionMapper;
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
 * Service for executing complex queries for {@link BillingConsumption} entities in the database.
 * The main input is a {@link BillingConsumptionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BillingConsumptionDTO} or a {@link Page} of {@link BillingConsumptionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BillingConsumptionQueryService extends QueryService<BillingConsumption> {

    private final Logger log = LoggerFactory.getLogger(BillingConsumptionQueryService.class);

    private final BillingConsumptionRepository billingConsumptionRepository;

    private final BillingConsumptionMapper billingConsumptionMapper;

    public BillingConsumptionQueryService(
        BillingConsumptionRepository billingConsumptionRepository,
        BillingConsumptionMapper billingConsumptionMapper
    ) {
        this.billingConsumptionRepository = billingConsumptionRepository;
        this.billingConsumptionMapper = billingConsumptionMapper;
    }

    /**
     * Return a {@link List} of {@link BillingConsumptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BillingConsumptionDTO> findByCriteria(BillingConsumptionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BillingConsumption> specification = createSpecification(criteria);
        return billingConsumptionMapper.toDto(billingConsumptionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BillingConsumptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BillingConsumptionDTO> findByCriteria(BillingConsumptionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BillingConsumption> specification = createSpecification(criteria);
        return billingConsumptionRepository.findAll(specification, page).map(billingConsumptionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BillingConsumptionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BillingConsumption> specification = createSpecification(criteria);
        return billingConsumptionRepository.count(specification);
    }

    /**
     * Function to convert {@link BillingConsumptionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BillingConsumption> createSpecification(BillingConsumptionCriteria criteria) {
        Specification<BillingConsumption> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BillingConsumption_.id));
            }
            if (criteria.getBusNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBusNum(), BillingConsumption_.busNum));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), BillingConsumption_.type));
            }
            if (criteria.getTotalEnergyConsumption() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getTotalEnergyConsumption(), BillingConsumption_.totalEnergyConsumption)
                    );
            }
            if (criteria.getUnitOfMeasure() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUnitOfMeasure(), BillingConsumption_.unitOfMeasure));
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNetworkId(),
                            root -> root.join(BillingConsumption_.network, JoinType.LEFT).get(Network_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
