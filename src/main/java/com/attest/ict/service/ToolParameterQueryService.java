package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.ToolParameter;
import com.attest.ict.repository.ToolParameterRepository;
import com.attest.ict.service.criteria.ToolParameterCriteria;
import com.attest.ict.service.dto.ToolParameterDTO;
import com.attest.ict.service.mapper.ToolParameterMapper;
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
 * Service for executing complex queries for {@link ToolParameter} entities in the database.
 * The main input is a {@link ToolParameterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ToolParameterDTO} or a {@link Page} of {@link ToolParameterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ToolParameterQueryService extends QueryService<ToolParameter> {

    private final Logger log = LoggerFactory.getLogger(ToolParameterQueryService.class);

    private final ToolParameterRepository toolParameterRepository;

    private final ToolParameterMapper toolParameterMapper;

    public ToolParameterQueryService(ToolParameterRepository toolParameterRepository, ToolParameterMapper toolParameterMapper) {
        this.toolParameterRepository = toolParameterRepository;
        this.toolParameterMapper = toolParameterMapper;
    }

    /**
     * Return a {@link List} of {@link ToolParameterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ToolParameterDTO> findByCriteria(ToolParameterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ToolParameter> specification = createSpecification(criteria);
        return toolParameterMapper.toDto(toolParameterRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ToolParameterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ToolParameterDTO> findByCriteria(ToolParameterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ToolParameter> specification = createSpecification(criteria);
        return toolParameterRepository.findAll(specification, page).map(toolParameterMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ToolParameterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ToolParameter> specification = createSpecification(criteria);
        return toolParameterRepository.count(specification);
    }

    /**
     * Function to convert {@link ToolParameterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ToolParameter> createSpecification(ToolParameterCriteria criteria) {
        Specification<ToolParameter> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ToolParameter_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ToolParameter_.name));
            }
            if (criteria.getDefaultValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDefaultValue(), ToolParameter_.defaultValue));
            }
            if (criteria.getIsEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsEnabled(), ToolParameter_.isEnabled));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ToolParameter_.description));
            }
            if (criteria.getLastUpdate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastUpdate(), ToolParameter_.lastUpdate));
            }
            if (criteria.getToolId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getToolId(), root -> root.join(ToolParameter_.tool, JoinType.LEFT).get(Tool_.id))
                    );
            }
        }
        return specification;
    }
}
