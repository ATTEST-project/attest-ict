package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.GenCost;
import com.attest.ict.repository.GenCostRepository;
import com.attest.ict.service.criteria.GenCostCriteria;
import com.attest.ict.service.dto.GenCostDTO;
import com.attest.ict.service.mapper.GenCostMapper;
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
 * Service for executing complex queries for {@link GenCost} entities in the database.
 * The main input is a {@link GenCostCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GenCostDTO} or a {@link Page} of {@link GenCostDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GenCostQueryService extends QueryService<GenCost> {

    private final Logger log = LoggerFactory.getLogger(GenCostQueryService.class);

    private final GenCostRepository genCostRepository;

    private final GenCostMapper genCostMapper;

    public GenCostQueryService(GenCostRepository genCostRepository, GenCostMapper genCostMapper) {
        this.genCostRepository = genCostRepository;
        this.genCostMapper = genCostMapper;
    }

    /**
     * Return a {@link List} of {@link GenCostDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GenCostDTO> findByCriteria(GenCostCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GenCost> specification = createSpecification(criteria);
        return genCostMapper.toDto(genCostRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GenCostDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GenCostDTO> findByCriteria(GenCostCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GenCost> specification = createSpecification(criteria);
        return genCostRepository.findAll(specification, page).map(genCostMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GenCostCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GenCost> specification = createSpecification(criteria);
        return genCostRepository.count(specification);
    }

    /**
     * Function to convert {@link GenCostCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GenCost> createSpecification(GenCostCriteria criteria) {
        Specification<GenCost> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GenCost_.id));
            }
            if (criteria.getModel() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModel(), GenCost_.model));
            }
            if (criteria.getStartup() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartup(), GenCost_.startup));
            }
            if (criteria.getShutdown() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShutdown(), GenCost_.shutdown));
            }
            if (criteria.getnCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getnCost(), GenCost_.nCost));
            }
            if (criteria.getCostPF() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCostPF(), GenCost_.costPF));
            }
            if (criteria.getCostQF() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCostQF(), GenCost_.costQF));
            }
            if (criteria.getGeneratorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGeneratorId(),
                            root -> root.join(GenCost_.generator, JoinType.LEFT).get(Generator_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
