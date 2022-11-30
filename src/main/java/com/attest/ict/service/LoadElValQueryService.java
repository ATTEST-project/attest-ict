package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.LoadElVal;
import com.attest.ict.repository.LoadElValRepository;
import com.attest.ict.service.criteria.LoadElValCriteria;
import com.attest.ict.service.dto.LoadElValDTO;
import com.attest.ict.service.mapper.LoadElValMapper;
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
 * Service for executing complex queries for {@link LoadElVal} entities in the database.
 * The main input is a {@link LoadElValCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LoadElValDTO} or a {@link Page} of {@link LoadElValDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LoadElValQueryService extends QueryService<LoadElVal> {

    private final Logger log = LoggerFactory.getLogger(LoadElValQueryService.class);

    private final LoadElValRepository loadElValRepository;

    private final LoadElValMapper loadElValMapper;

    public LoadElValQueryService(LoadElValRepository loadElValRepository, LoadElValMapper loadElValMapper) {
        this.loadElValRepository = loadElValRepository;
        this.loadElValMapper = loadElValMapper;
    }

    /**
     * Return a {@link List} of {@link LoadElValDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LoadElValDTO> findByCriteria(LoadElValCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LoadElVal> specification = createSpecification(criteria);
        return loadElValMapper.toDto(loadElValRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LoadElValDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LoadElValDTO> findByCriteria(LoadElValCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LoadElVal> specification = createSpecification(criteria);
        return loadElValRepository.findAll(specification, page).map(loadElValMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LoadElValCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LoadElVal> specification = createSpecification(criteria);
        return loadElValRepository.count(specification);
    }

    /**
     * Function to convert {@link LoadElValCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LoadElVal> createSpecification(LoadElValCriteria criteria) {
        Specification<LoadElVal> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LoadElVal_.id));
            }
            if (criteria.getHour() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHour(), LoadElVal_.hour));
            }
            if (criteria.getMin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMin(), LoadElVal_.min));
            }
            if (criteria.getP() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getP(), LoadElVal_.p));
            }
            if (criteria.getQ() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQ(), LoadElVal_.q));
            }
            if (criteria.getLoadIdOnSubst() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLoadIdOnSubst(), LoadElVal_.loadIdOnSubst));
            }
            if (criteria.getNominalVoltage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNominalVoltage(), LoadElVal_.nominalVoltage));
            }
            if (criteria.getLoadProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLoadProfileId(),
                            root -> root.join(LoadElVal_.loadProfile, JoinType.LEFT).get(LoadProfile_.id)
                        )
                    );
            }
            if (criteria.getBusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBusId(), root -> root.join(LoadElVal_.bus, JoinType.LEFT).get(Bus_.id))
                    );
            }
        }
        return specification;
    }
}
