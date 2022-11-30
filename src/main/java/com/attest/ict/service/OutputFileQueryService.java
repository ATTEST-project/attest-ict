package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.OutputFile;
import com.attest.ict.repository.OutputFileRepository;
import com.attest.ict.service.criteria.OutputFileCriteria;
import com.attest.ict.service.dto.OutputFileDTO;
import com.attest.ict.service.mapper.OutputFileMapper;
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
 * Service for executing complex queries for {@link OutputFile} entities in the database.
 * The main input is a {@link OutputFileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OutputFileDTO} or a {@link Page} of {@link OutputFileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OutputFileQueryService extends QueryService<OutputFile> {

    private final Logger log = LoggerFactory.getLogger(OutputFileQueryService.class);

    private final OutputFileRepository outputFileRepository;

    private final OutputFileMapper outputFileMapper;

    public OutputFileQueryService(OutputFileRepository outputFileRepository, OutputFileMapper outputFileMapper) {
        this.outputFileRepository = outputFileRepository;
        this.outputFileMapper = outputFileMapper;
    }

    /**
     * Return a {@link List} of {@link OutputFileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OutputFileDTO> findByCriteria(OutputFileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OutputFile> specification = createSpecification(criteria);
        return outputFileMapper.toDto(outputFileRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OutputFileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OutputFileDTO> findByCriteria(OutputFileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OutputFile> specification = createSpecification(criteria);
        return outputFileRepository.findAll(specification, page).map(outputFileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OutputFileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OutputFile> specification = createSpecification(criteria);
        return outputFileRepository.count(specification);
    }

    /**
     * Function to convert {@link OutputFileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OutputFile> createSpecification(OutputFileCriteria criteria) {
        Specification<OutputFile> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OutputFile_.id));
            }
            if (criteria.getFileName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileName(), OutputFile_.fileName));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), OutputFile_.description));
            }
            if (criteria.getUploadTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUploadTime(), OutputFile_.uploadTime));
            }
            if (criteria.getToolId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getToolId(), root -> root.join(OutputFile_.tool, JoinType.LEFT).get(Tool_.id))
                    );
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNetworkId(), root -> root.join(OutputFile_.network, JoinType.LEFT).get(Network_.id))
                    );
            }
            if (criteria.getSimulationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSimulationId(),
                            root -> root.join(OutputFile_.simulation, JoinType.LEFT).get(Simulation_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
