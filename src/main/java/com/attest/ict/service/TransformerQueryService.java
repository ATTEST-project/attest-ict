package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.Transformer;
import com.attest.ict.repository.TransformerRepository;
import com.attest.ict.service.criteria.TransformerCriteria;
import com.attest.ict.service.dto.TransformerDTO;
import com.attest.ict.service.mapper.TransformerMapper;
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
 * Service for executing complex queries for {@link Transformer} entities in the database.
 * The main input is a {@link TransformerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TransformerDTO} or a {@link Page} of {@link TransformerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransformerQueryService extends QueryService<Transformer> {

    private final Logger log = LoggerFactory.getLogger(TransformerQueryService.class);

    private final TransformerRepository transformerRepository;

    private final TransformerMapper transformerMapper;

    public TransformerQueryService(TransformerRepository transformerRepository, TransformerMapper transformerMapper) {
        this.transformerRepository = transformerRepository;
        this.transformerMapper = transformerMapper;
    }

    /**
     * Return a {@link List} of {@link TransformerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransformerDTO> findByCriteria(TransformerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Transformer> specification = createSpecification(criteria);
        return transformerMapper.toDto(transformerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TransformerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransformerDTO> findByCriteria(TransformerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Transformer> specification = createSpecification(criteria);
        return transformerRepository.findAll(specification, page).map(transformerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransformerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Transformer> specification = createSpecification(criteria);
        return transformerRepository.count(specification);
    }

    /**
     * Function to convert {@link TransformerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Transformer> createSpecification(TransformerCriteria criteria) {
        Specification<Transformer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Transformer_.id));
            }
            if (criteria.getFbus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFbus(), Transformer_.fbus));
            }
            if (criteria.getTbus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTbus(), Transformer_.tbus));
            }
            if (criteria.getMin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMin(), Transformer_.min));
            }
            if (criteria.getMax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMax(), Transformer_.max));
            }
            if (criteria.getTotalTaps() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalTaps(), Transformer_.totalTaps));
            }
            if (criteria.getTap() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTap(), Transformer_.tap));
            }
            if (criteria.getManufactureYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getManufactureYear(), Transformer_.manufactureYear));
            }
            if (criteria.getCommissioningYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCommissioningYear(), Transformer_.commissioningYear));
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNetworkId(), root -> root.join(Transformer_.network, JoinType.LEFT).get(Network_.id))
                    );
            }
        }
        return specification;
    }
}
