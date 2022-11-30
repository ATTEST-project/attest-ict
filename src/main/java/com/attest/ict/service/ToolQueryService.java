package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.Tool;
import com.attest.ict.repository.ToolRepository;
import com.attest.ict.service.criteria.ToolCriteria;
import com.attest.ict.service.dto.ToolDTO;
import com.attest.ict.service.mapper.ToolMapper;
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
 * Service for executing complex queries for {@link Tool} entities in the database.
 * The main input is a {@link ToolCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ToolDTO} or a {@link Page} of {@link ToolDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ToolQueryService extends QueryService<Tool> {

    private final Logger log = LoggerFactory.getLogger(ToolQueryService.class);

    private final ToolRepository toolRepository;

    private final ToolMapper toolMapper;

    public ToolQueryService(ToolRepository toolRepository, ToolMapper toolMapper) {
        this.toolRepository = toolRepository;
        this.toolMapper = toolMapper;
    }

    /**
     * Return a {@link List} of {@link ToolDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ToolDTO> findByCriteria(ToolCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Tool> specification = createSpecification(criteria);
        return toolMapper.toDto(toolRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ToolDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ToolDTO> findByCriteria(ToolCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tool> specification = createSpecification(criteria);
        return toolRepository.findAll(specification, page).map(toolMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ToolCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Tool> specification = createSpecification(criteria);
        return toolRepository.count(specification);
    }

    /**
     * Function to convert {@link ToolCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Tool> createSpecification(ToolCriteria criteria) {
        Specification<Tool> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Tool_.id));
            }
            if (criteria.getWorkPackage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWorkPackage(), Tool_.workPackage));
            }
            if (criteria.getNum() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNum(), Tool_.num));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Tool_.name));
            }
            if (criteria.getPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPath(), Tool_.path));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Tool_.description));
            }
            if (criteria.getInputFileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getInputFileId(), root -> root.join(Tool_.inputFiles, JoinType.LEFT).get(InputFile_.id))
                    );
            }
            if (criteria.getOutputFileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOutputFileId(),
                            root -> root.join(Tool_.outputFiles, JoinType.LEFT).get(OutputFile_.id)
                        )
                    );
            }
            if (criteria.getTaskId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTaskId(), root -> root.join(Tool_.tasks, JoinType.LEFT).get(Task_.id))
                    );
            }
            if (criteria.getParameterId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParameterId(),
                            root -> root.join(Tool_.parameters, JoinType.LEFT).get(ToolParameter_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
