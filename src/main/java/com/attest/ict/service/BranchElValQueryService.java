package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.BranchElVal;
import com.attest.ict.repository.BranchElValRepository;
import com.attest.ict.service.criteria.BranchElValCriteria;
import com.attest.ict.service.dto.BranchElValDTO;
import com.attest.ict.service.mapper.BranchElValMapper;
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
 * Service for executing complex queries for {@link BranchElVal} entities in the database.
 * The main input is a {@link BranchElValCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BranchElValDTO} or a {@link Page} of {@link BranchElValDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BranchElValQueryService extends QueryService<BranchElVal> {

    private final Logger log = LoggerFactory.getLogger(BranchElValQueryService.class);

    private final BranchElValRepository branchElValRepository;

    private final BranchElValMapper branchElValMapper;

    public BranchElValQueryService(BranchElValRepository branchElValRepository, BranchElValMapper branchElValMapper) {
        this.branchElValRepository = branchElValRepository;
        this.branchElValMapper = branchElValMapper;
    }

    /**
     * Return a {@link List} of {@link BranchElValDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BranchElValDTO> findByCriteria(BranchElValCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BranchElVal> specification = createSpecification(criteria);
        return branchElValMapper.toDto(branchElValRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BranchElValDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BranchElValDTO> findByCriteria(BranchElValCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BranchElVal> specification = createSpecification(criteria);
        return branchElValRepository.findAll(specification, page).map(branchElValMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BranchElValCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BranchElVal> specification = createSpecification(criteria);
        return branchElValRepository.count(specification);
    }

    /**
     * Function to convert {@link BranchElValCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BranchElVal> createSpecification(BranchElValCriteria criteria) {
        Specification<BranchElVal> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BranchElVal_.id));
            }
            if (criteria.getHour() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHour(), BranchElVal_.hour));
            }
            if (criteria.getMin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMin(), BranchElVal_.min));
            }
            if (criteria.getP() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getP(), BranchElVal_.p));
            }
            if (criteria.getQ() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQ(), BranchElVal_.q));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), BranchElVal_.status));
            }
            if (criteria.getBranchIdOnSubst() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBranchIdOnSubst(), BranchElVal_.branchIdOnSubst));
            }
            if (criteria.getNominalVoltage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNominalVoltage(), BranchElVal_.nominalVoltage));
            }
            if (criteria.getBranchId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBranchId(), root -> root.join(BranchElVal_.branch, JoinType.LEFT).get(Branch_.id))
                    );
            }
            if (criteria.getBranchProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBranchProfileId(),
                            root -> root.join(BranchElVal_.branchProfile, JoinType.LEFT).get(BranchProfile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
