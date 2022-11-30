package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.BranchProfile;
import com.attest.ict.repository.BranchProfileRepository;
import com.attest.ict.service.criteria.BranchProfileCriteria;
import com.attest.ict.service.dto.BranchProfileDTO;
import com.attest.ict.service.mapper.BranchProfileMapper;
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
 * Service for executing complex queries for {@link BranchProfile} entities in the database.
 * The main input is a {@link BranchProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BranchProfileDTO} or a {@link Page} of {@link BranchProfileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BranchProfileQueryService extends QueryService<BranchProfile> {

    private final Logger log = LoggerFactory.getLogger(BranchProfileQueryService.class);

    private final BranchProfileRepository branchProfileRepository;

    private final BranchProfileMapper branchProfileMapper;

    public BranchProfileQueryService(BranchProfileRepository branchProfileRepository, BranchProfileMapper branchProfileMapper) {
        this.branchProfileRepository = branchProfileRepository;
        this.branchProfileMapper = branchProfileMapper;
    }

    /**
     * Return a {@link List} of {@link BranchProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BranchProfileDTO> findByCriteria(BranchProfileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BranchProfile> specification = createSpecification(criteria);
        return branchProfileMapper.toDto(branchProfileRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BranchProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BranchProfileDTO> findByCriteria(BranchProfileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BranchProfile> specification = createSpecification(criteria);
        return branchProfileRepository.findAll(specification, page).map(branchProfileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BranchProfileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BranchProfile> specification = createSpecification(criteria);
        return branchProfileRepository.count(specification);
    }

    /**
     * Function to convert {@link BranchProfileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BranchProfile> createSpecification(BranchProfileCriteria criteria) {
        Specification<BranchProfile> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BranchProfile_.id));
            }
            if (criteria.getSeason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSeason(), BranchProfile_.season));
            }
            if (criteria.getTypicalDay() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTypicalDay(), BranchProfile_.typicalDay));
            }
            if (criteria.getMode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMode(), BranchProfile_.mode));
            }
            if (criteria.getTimeInterval() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeInterval(), BranchProfile_.timeInterval));
            }
            if (criteria.getUploadDateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUploadDateTime(), BranchProfile_.uploadDateTime));
            }
            if (criteria.getInputFileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInputFileId(),
                            root -> root.join(BranchProfile_.inputFile, JoinType.LEFT).get(InputFile_.id)
                        )
                    );
            }
            if (criteria.getBranchElValId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBranchElValId(),
                            root -> root.join(BranchProfile_.branchElVals, JoinType.LEFT).get(BranchElVal_.id)
                        )
                    );
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNetworkId(),
                            root -> root.join(BranchProfile_.network, JoinType.LEFT).get(Network_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
