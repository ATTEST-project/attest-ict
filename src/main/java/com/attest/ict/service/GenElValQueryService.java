package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.GenElVal;
import com.attest.ict.repository.GenElValRepository;
import com.attest.ict.service.criteria.GenElValCriteria;
import com.attest.ict.service.dto.GenElValDTO;
import com.attest.ict.service.mapper.GenElValMapper;
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
 * Service for executing complex queries for {@link GenElVal} entities in the database.
 * The main input is a {@link GenElValCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GenElValDTO} or a {@link Page} of {@link GenElValDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GenElValQueryService extends QueryService<GenElVal> {

    private final Logger log = LoggerFactory.getLogger(GenElValQueryService.class);

    private final GenElValRepository genElValRepository;

    private final GenElValMapper genElValMapper;

    public GenElValQueryService(GenElValRepository genElValRepository, GenElValMapper genElValMapper) {
        this.genElValRepository = genElValRepository;
        this.genElValMapper = genElValMapper;
    }

    /**
     * Return a {@link List} of {@link GenElValDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GenElValDTO> findByCriteria(GenElValCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GenElVal> specification = createSpecification(criteria);
        return genElValMapper.toDto(genElValRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GenElValDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GenElValDTO> findByCriteria(GenElValCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GenElVal> specification = createSpecification(criteria);
        return genElValRepository.findAll(specification, page).map(genElValMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GenElValCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GenElVal> specification = createSpecification(criteria);
        return genElValRepository.count(specification);
    }

    /**
     * Function to convert {@link GenElValCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GenElVal> createSpecification(GenElValCriteria criteria) {
        Specification<GenElVal> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GenElVal_.id));
            }
            if (criteria.getHour() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHour(), GenElVal_.hour));
            }
            if (criteria.getMin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMin(), GenElVal_.min));
            }
            if (criteria.getP() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getP(), GenElVal_.p));
            }
            if (criteria.getQ() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQ(), GenElVal_.q));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), GenElVal_.status));
            }
            if (criteria.getVoltageMagnitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVoltageMagnitude(), GenElVal_.voltageMagnitude));
            }
            if (criteria.getGenIdOnSubst() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGenIdOnSubst(), GenElVal_.genIdOnSubst));
            }
            if (criteria.getNominalVoltage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNominalVoltage(), GenElVal_.nominalVoltage));
            }
            if (criteria.getGenProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGenProfileId(),
                            root -> root.join(GenElVal_.genProfile, JoinType.LEFT).get(GenProfile_.id)
                        )
                    );
            }
            if (criteria.getGeneratorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGeneratorId(),
                            root -> root.join(GenElVal_.generator, JoinType.LEFT).get(Generator_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
