package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.BranchExtension;
import com.attest.ict.repository.BranchExtensionRepository;
import com.attest.ict.service.criteria.BranchExtensionCriteria;
import com.attest.ict.service.dto.BranchExtensionDTO;
import com.attest.ict.service.mapper.BranchExtensionMapper;
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
 * Service for executing complex queries for {@link BranchExtension} entities in the database.
 * The main input is a {@link BranchExtensionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BranchExtensionDTO} or a {@link Page} of {@link BranchExtensionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BranchExtensionQueryService extends QueryService<BranchExtension> {

    private final Logger log = LoggerFactory.getLogger(BranchExtensionQueryService.class);

    private final BranchExtensionRepository branchExtensionRepository;

    private final BranchExtensionMapper branchExtensionMapper;

    public BranchExtensionQueryService(BranchExtensionRepository branchExtensionRepository, BranchExtensionMapper branchExtensionMapper) {
        this.branchExtensionRepository = branchExtensionRepository;
        this.branchExtensionMapper = branchExtensionMapper;
    }

    /**
     * Return a {@link List} of {@link BranchExtensionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BranchExtensionDTO> findByCriteria(BranchExtensionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BranchExtension> specification = createSpecification(criteria);
        return branchExtensionMapper.toDto(branchExtensionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BranchExtensionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BranchExtensionDTO> findByCriteria(BranchExtensionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BranchExtension> specification = createSpecification(criteria);
        return branchExtensionRepository.findAll(specification, page).map(branchExtensionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BranchExtensionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BranchExtension> specification = createSpecification(criteria);
        return branchExtensionRepository.count(specification);
    }

    /**
     * Function to convert {@link BranchExtensionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BranchExtension> createSpecification(BranchExtensionCriteria criteria) {
        Specification<BranchExtension> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BranchExtension_.id));
            }
            if (criteria.getStepSize() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStepSize(), BranchExtension_.stepSize));
            }
            if (criteria.getActTap() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActTap(), BranchExtension_.actTap));
            }
            if (criteria.getMinTap() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinTap(), BranchExtension_.minTap));
            }
            if (criteria.getMaxTap() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxTap(), BranchExtension_.maxTap));
            }
            if (criteria.getNormalTap() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNormalTap(), BranchExtension_.normalTap));
            }
            if (criteria.getNominalRatio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNominalRatio(), BranchExtension_.nominalRatio));
            }
            if (criteria.getrIp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getrIp(), BranchExtension_.rIp));
            }
            if (criteria.getrN() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getrN(), BranchExtension_.rN));
            }
            if (criteria.getr0() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getr0(), BranchExtension_.r0));
            }
            if (criteria.getx0() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getx0(), BranchExtension_.x0));
            }
            if (criteria.getb0() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getb0(), BranchExtension_.b0));
            }
            if (criteria.getLength() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLength(), BranchExtension_.length));
            }
            if (criteria.getNormStat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNormStat(), BranchExtension_.normStat));
            }
            if (criteria.getG() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getG(), BranchExtension_.g));
            }
            if (criteria.getmRid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getmRid(), BranchExtension_.mRid));
            }
            if (criteria.getBranchId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBranchId(),
                            root -> root.join(BranchExtension_.branch, JoinType.LEFT).get(Branch_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
