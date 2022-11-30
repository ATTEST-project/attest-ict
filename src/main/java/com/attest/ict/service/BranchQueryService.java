package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.Branch;
import com.attest.ict.repository.BranchRepository;
import com.attest.ict.service.criteria.BranchCriteria;
import com.attest.ict.service.dto.BranchDTO;
import com.attest.ict.service.mapper.BranchMapper;
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
 * Service for executing complex queries for {@link Branch} entities in the database.
 * The main input is a {@link BranchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BranchDTO} or a {@link Page} of {@link BranchDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BranchQueryService extends QueryService<Branch> {

    private final Logger log = LoggerFactory.getLogger(BranchQueryService.class);

    private final BranchRepository branchRepository;

    private final BranchMapper branchMapper;

    public BranchQueryService(BranchRepository branchRepository, BranchMapper branchMapper) {
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
    }

    /**
     * Return a {@link List} of {@link BranchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BranchDTO> findByCriteria(BranchCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Branch> specification = createSpecification(criteria);
        return branchMapper.toDto(branchRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BranchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BranchDTO> findByCriteria(BranchCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Branch> specification = createSpecification(criteria);
        return branchRepository.findAll(specification, page).map(branchMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BranchCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Branch> specification = createSpecification(criteria);
        return branchRepository.count(specification);
    }

    /**
     * Function to convert {@link BranchCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Branch> createSpecification(BranchCriteria criteria) {
        Specification<Branch> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Branch_.id));
            }
            if (criteria.getFbus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFbus(), Branch_.fbus));
            }
            if (criteria.getTbus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTbus(), Branch_.tbus));
            }
            if (criteria.getR() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getR(), Branch_.r));
            }
            if (criteria.getX() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getX(), Branch_.x));
            }
            if (criteria.getB() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getB(), Branch_.b));
            }
            if (criteria.getRatea() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRatea(), Branch_.ratea));
            }
            if (criteria.getRateb() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRateb(), Branch_.rateb));
            }
            if (criteria.getRatec() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRatec(), Branch_.ratec));
            }
            if (criteria.getTapRatio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTapRatio(), Branch_.tapRatio));
            }
            if (criteria.getAngle() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAngle(), Branch_.angle));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), Branch_.status));
            }
            if (criteria.getAngmin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAngmin(), Branch_.angmin));
            }
            if (criteria.getAngmax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAngmax(), Branch_.angmax));
            }
            if (criteria.getTransfElValId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransfElValId(),
                            root -> root.join(Branch_.transfElVals, JoinType.LEFT).get(TransfElVal_.id)
                        )
                    );
            }
            if (criteria.getBranchElValId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBranchElValId(),
                            root -> root.join(Branch_.branchElVals, JoinType.LEFT).get(BranchElVal_.id)
                        )
                    );
            }
            if (criteria.getBranchExtensionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBranchExtensionId(),
                            root -> root.join(Branch_.branchExtension, JoinType.LEFT).get(BranchExtension_.id)
                        )
                    );
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNetworkId(), root -> root.join(Branch_.network, JoinType.LEFT).get(Network_.id))
                    );
            }
        }
        return specification;
    }
}
