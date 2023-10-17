package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.Task;
import com.attest.ict.repository.TaskRepository;
import com.attest.ict.service.criteria.TaskCriteria;
import com.attest.ict.service.dto.TaskDTO;
import com.attest.ict.service.mapper.TaskMapper;
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
 * Service for executing complex queries for {@link Task} entities in the database.
 * The main input is a {@link TaskCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TaskDTO} or a {@link Page} of {@link TaskDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TaskQueryService extends QueryService<Task> {

    private final Logger log = LoggerFactory.getLogger(TaskQueryService.class);

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    public TaskQueryService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    /**
     * Return a {@link List} of {@link TaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TaskDTO> findByCriteria(TaskCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Task> specification = createSpecification(criteria);
        return taskMapper.toDto(taskRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TaskDTO> findByCriteria(TaskCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Task> specification = createSpecification(criteria);
        return taskRepository.findAll(specification, page).map(taskMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TaskCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Task> specification = createSpecification(criteria);
        return taskRepository.count(specification);
    }

    /**
     * Function to convert {@link TaskCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Task> createSpecification(TaskCriteria criteria) {
        Specification<Task> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Task_.id));
            }
            if (criteria.getTaskStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTaskStatus(), Task_.taskStatus));
            }
            if (criteria.getInfo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInfo(), Task_.info));
            }
            if (criteria.getDateTimeStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateTimeStart(), Task_.dateTimeStart));
            }
            if (criteria.getDateTimeEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateTimeEnd(), Task_.dateTimeEnd));
            }
            if (criteria.getToolLogFileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getToolLogFileId(),
                            root -> root.join(Task_.toolLogFile, JoinType.LEFT).get(ToolLogFile_.id)
                        )
                    );
            }
            if (criteria.getSimulationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSimulationId(),
                            root -> root.join(Task_.simulation, JoinType.LEFT).get(Simulation_.id)
                        )
                    );
            }
            if (criteria.getToolId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getToolId(), root -> root.join(Task_.tool, JoinType.LEFT).get(Tool_.id)));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getUserId(), root -> root.join(Task_.user, JoinType.LEFT).get(User_.id)));
            }

            //20231003 add custom criteria for filtering by toolNum
            if (criteria.getToolNum() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getToolNum(), root -> root.join(Task_.tool, JoinType.LEFT).get(Tool_.num))
                    );
            }
        }
        return specification;
    }
}
