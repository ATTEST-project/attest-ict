package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.BaseMVA;
import com.attest.ict.repository.BaseMVARepository;
import com.attest.ict.service.criteria.BaseMVACriteria;
import com.attest.ict.service.dto.BaseMVADTO;
import com.attest.ict.service.mapper.BaseMVAMapper;
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
 * Service for executing complex queries for {@link BaseMVA} entities in the database.
 * The main input is a {@link BaseMVACriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BaseMVADTO} or a {@link Page} of {@link BaseMVADTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BaseMVAQueryService extends QueryService<BaseMVA> {

    private final Logger log = LoggerFactory.getLogger(BaseMVAQueryService.class);

    private final BaseMVARepository baseMVARepository;

    private final BaseMVAMapper baseMVAMapper;

    public BaseMVAQueryService(BaseMVARepository baseMVARepository, BaseMVAMapper baseMVAMapper) {
        this.baseMVARepository = baseMVARepository;
        this.baseMVAMapper = baseMVAMapper;
    }

    /**
     * Return a {@link List} of {@link BaseMVADTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BaseMVADTO> findByCriteria(BaseMVACriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BaseMVA> specification = createSpecification(criteria);
        return baseMVAMapper.toDto(baseMVARepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BaseMVADTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BaseMVADTO> findByCriteria(BaseMVACriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BaseMVA> specification = createSpecification(criteria);
        return baseMVARepository.findAll(specification, page).map(baseMVAMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BaseMVACriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BaseMVA> specification = createSpecification(criteria);
        return baseMVARepository.count(specification);
    }

    /**
     * Function to convert {@link BaseMVACriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BaseMVA> createSpecification(BaseMVACriteria criteria) {
        Specification<BaseMVA> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BaseMVA_.id));
            }
            if (criteria.getBaseMva() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBaseMva(), BaseMVA_.baseMva));
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNetworkId(), root -> root.join(BaseMVA_.network, JoinType.LEFT).get(Network_.id))
                    );
            }
        }
        return specification;
    }
}
