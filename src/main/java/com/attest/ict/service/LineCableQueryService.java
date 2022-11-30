package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.LineCable;
import com.attest.ict.repository.LineCableRepository;
import com.attest.ict.service.criteria.LineCableCriteria;
import com.attest.ict.service.dto.LineCableDTO;
import com.attest.ict.service.mapper.LineCableMapper;
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
 * Service for executing complex queries for {@link LineCable} entities in the database.
 * The main input is a {@link LineCableCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LineCableDTO} or a {@link Page} of {@link LineCableDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LineCableQueryService extends QueryService<LineCable> {

    private final Logger log = LoggerFactory.getLogger(LineCableQueryService.class);

    private final LineCableRepository lineCableRepository;

    private final LineCableMapper lineCableMapper;

    public LineCableQueryService(LineCableRepository lineCableRepository, LineCableMapper lineCableMapper) {
        this.lineCableRepository = lineCableRepository;
        this.lineCableMapper = lineCableMapper;
    }

    /**
     * Return a {@link List} of {@link LineCableDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LineCableDTO> findByCriteria(LineCableCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LineCable> specification = createSpecification(criteria);
        return lineCableMapper.toDto(lineCableRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LineCableDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LineCableDTO> findByCriteria(LineCableCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LineCable> specification = createSpecification(criteria);
        return lineCableRepository.findAll(specification, page).map(lineCableMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LineCableCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LineCable> specification = createSpecification(criteria);
        return lineCableRepository.count(specification);
    }

    /**
     * Function to convert {@link LineCableCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LineCable> createSpecification(LineCableCriteria criteria) {
        Specification<LineCable> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LineCable_.id));
            }
            if (criteria.getFbus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFbus(), LineCable_.fbus));
            }
            if (criteria.getTbus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTbus(), LineCable_.tbus));
            }
            if (criteria.getLengthKm() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLengthKm(), LineCable_.lengthKm));
            }
            if (criteria.getTypeOfInstallation() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getTypeOfInstallation(), LineCable_.typeOfInstallation));
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNetworkId(), root -> root.join(LineCable_.network, JoinType.LEFT).get(Network_.id))
                    );
            }
        }
        return specification;
    }
}
