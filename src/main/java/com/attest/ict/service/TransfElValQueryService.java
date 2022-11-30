package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.TransfElVal;
import com.attest.ict.repository.TransfElValRepository;
import com.attest.ict.service.criteria.TransfElValCriteria;
import com.attest.ict.service.dto.TransfElValDTO;
import com.attest.ict.service.mapper.TransfElValMapper;
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
 * Service for executing complex queries for {@link TransfElVal} entities in the database.
 * The main input is a {@link TransfElValCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TransfElValDTO} or a {@link Page} of {@link TransfElValDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransfElValQueryService extends QueryService<TransfElVal> {

    private final Logger log = LoggerFactory.getLogger(TransfElValQueryService.class);

    private final TransfElValRepository transfElValRepository;

    private final TransfElValMapper transfElValMapper;

    public TransfElValQueryService(TransfElValRepository transfElValRepository, TransfElValMapper transfElValMapper) {
        this.transfElValRepository = transfElValRepository;
        this.transfElValMapper = transfElValMapper;
    }

    /**
     * Return a {@link List} of {@link TransfElValDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransfElValDTO> findByCriteria(TransfElValCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TransfElVal> specification = createSpecification(criteria);
        return transfElValMapper.toDto(transfElValRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TransfElValDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransfElValDTO> findByCriteria(TransfElValCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TransfElVal> specification = createSpecification(criteria);
        return transfElValRepository.findAll(specification, page).map(transfElValMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransfElValCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TransfElVal> specification = createSpecification(criteria);
        return transfElValRepository.count(specification);
    }

    /**
     * Function to convert {@link TransfElValCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TransfElVal> createSpecification(TransfElValCriteria criteria) {
        Specification<TransfElVal> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TransfElVal_.id));
            }
            if (criteria.getHour() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHour(), TransfElVal_.hour));
            }
            if (criteria.getMin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMin(), TransfElVal_.min));
            }
            if (criteria.getTapRatio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTapRatio(), TransfElVal_.tapRatio));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), TransfElVal_.status));
            }
            if (criteria.getTrasfIdOnSubst() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTrasfIdOnSubst(), TransfElVal_.trasfIdOnSubst));
            }
            if (criteria.getNominalVoltage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNominalVoltage(), TransfElVal_.nominalVoltage));
            }
            if (criteria.getTransfProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransfProfileId(),
                            root -> root.join(TransfElVal_.transfProfile, JoinType.LEFT).get(TransfProfile_.id)
                        )
                    );
            }
            if (criteria.getBranchId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBranchId(), root -> root.join(TransfElVal_.branch, JoinType.LEFT).get(Branch_.id))
                    );
            }
        }
        return specification;
    }
}
