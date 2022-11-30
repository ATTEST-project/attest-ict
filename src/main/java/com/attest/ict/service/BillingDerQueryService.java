package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.BillingDer;
import com.attest.ict.repository.BillingDerRepository;
import com.attest.ict.service.criteria.BillingDerCriteria;
import com.attest.ict.service.dto.BillingDerDTO;
import com.attest.ict.service.mapper.BillingDerMapper;
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
 * Service for executing complex queries for {@link BillingDer} entities in the database.
 * The main input is a {@link BillingDerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BillingDerDTO} or a {@link Page} of {@link BillingDerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BillingDerQueryService extends QueryService<BillingDer> {

    private final Logger log = LoggerFactory.getLogger(BillingDerQueryService.class);

    private final BillingDerRepository billingDerRepository;

    private final BillingDerMapper billingDerMapper;

    public BillingDerQueryService(BillingDerRepository billingDerRepository, BillingDerMapper billingDerMapper) {
        this.billingDerRepository = billingDerRepository;
        this.billingDerMapper = billingDerMapper;
    }

    /**
     * Return a {@link List} of {@link BillingDerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BillingDerDTO> findByCriteria(BillingDerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BillingDer> specification = createSpecification(criteria);
        return billingDerMapper.toDto(billingDerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BillingDerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BillingDerDTO> findByCriteria(BillingDerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BillingDer> specification = createSpecification(criteria);
        return billingDerRepository.findAll(specification, page).map(billingDerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BillingDerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BillingDer> specification = createSpecification(criteria);
        return billingDerRepository.count(specification);
    }

    /**
     * Function to convert {@link BillingDerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BillingDer> createSpecification(BillingDerCriteria criteria) {
        Specification<BillingDer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BillingDer_.id));
            }
            if (criteria.getBusNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBusNum(), BillingDer_.busNum));
            }
            if (criteria.getMaxPowerKw() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxPowerKw(), BillingDer_.maxPowerKw));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), BillingDer_.type));
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNetworkId(), root -> root.join(BillingDer_.network, JoinType.LEFT).get(Network_.id))
                    );
            }
        }
        return specification;
    }
}
