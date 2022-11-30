package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.ProtectionTool;
import com.attest.ict.repository.ProtectionToolRepository;
import com.attest.ict.service.criteria.ProtectionToolCriteria;
import com.attest.ict.service.dto.ProtectionToolDTO;
import com.attest.ict.service.mapper.ProtectionToolMapper;
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
 * Service for executing complex queries for {@link ProtectionTool} entities in the database.
 * The main input is a {@link ProtectionToolCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProtectionToolDTO} or a {@link Page} of {@link ProtectionToolDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProtectionToolQueryService extends QueryService<ProtectionTool> {

    private final Logger log = LoggerFactory.getLogger(ProtectionToolQueryService.class);

    private final ProtectionToolRepository protectionToolRepository;

    private final ProtectionToolMapper protectionToolMapper;

    public ProtectionToolQueryService(ProtectionToolRepository protectionToolRepository, ProtectionToolMapper protectionToolMapper) {
        this.protectionToolRepository = protectionToolRepository;
        this.protectionToolMapper = protectionToolMapper;
    }

    /**
     * Return a {@link List} of {@link ProtectionToolDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProtectionToolDTO> findByCriteria(ProtectionToolCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProtectionTool> specification = createSpecification(criteria);
        return protectionToolMapper.toDto(protectionToolRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProtectionToolDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProtectionToolDTO> findByCriteria(ProtectionToolCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProtectionTool> specification = createSpecification(criteria);
        return protectionToolRepository.findAll(specification, page).map(protectionToolMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProtectionToolCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProtectionTool> specification = createSpecification(criteria);
        return protectionToolRepository.count(specification);
    }

    /**
     * Function to convert {@link ProtectionToolCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProtectionTool> createSpecification(ProtectionToolCriteria criteria) {
        Specification<ProtectionTool> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProtectionTool_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), ProtectionTool_.type));
            }
            if (criteria.getBranchId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBranchId(), root -> root.join(ProtectionTool_.branch, JoinType.LEFT).get(Branch_.id))
                    );
            }
            if (criteria.getBusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBusId(), root -> root.join(ProtectionTool_.bus, JoinType.LEFT).get(Bus_.id))
                    );
            }
        }
        return specification;
    }
}
