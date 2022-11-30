package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.GeneratorExtension;
import com.attest.ict.repository.GeneratorExtensionRepository;
import com.attest.ict.service.criteria.GeneratorExtensionCriteria;
import com.attest.ict.service.dto.GeneratorExtensionDTO;
import com.attest.ict.service.mapper.GeneratorExtensionMapper;
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
 * Service for executing complex queries for {@link GeneratorExtension} entities in the database.
 * The main input is a {@link GeneratorExtensionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GeneratorExtensionDTO} or a {@link Page} of {@link GeneratorExtensionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GeneratorExtensionQueryService extends QueryService<GeneratorExtension> {

    private final Logger log = LoggerFactory.getLogger(GeneratorExtensionQueryService.class);

    private final GeneratorExtensionRepository generatorExtensionRepository;

    private final GeneratorExtensionMapper generatorExtensionMapper;

    public GeneratorExtensionQueryService(
        GeneratorExtensionRepository generatorExtensionRepository,
        GeneratorExtensionMapper generatorExtensionMapper
    ) {
        this.generatorExtensionRepository = generatorExtensionRepository;
        this.generatorExtensionMapper = generatorExtensionMapper;
    }

    /**
     * Return a {@link List} of {@link GeneratorExtensionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GeneratorExtensionDTO> findByCriteria(GeneratorExtensionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GeneratorExtension> specification = createSpecification(criteria);
        return generatorExtensionMapper.toDto(generatorExtensionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GeneratorExtensionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GeneratorExtensionDTO> findByCriteria(GeneratorExtensionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GeneratorExtension> specification = createSpecification(criteria);
        return generatorExtensionRepository.findAll(specification, page).map(generatorExtensionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GeneratorExtensionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GeneratorExtension> specification = createSpecification(criteria);
        return generatorExtensionRepository.count(specification);
    }

    /**
     * Function to convert {@link GeneratorExtensionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GeneratorExtension> createSpecification(GeneratorExtensionCriteria criteria) {
        Specification<GeneratorExtension> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GeneratorExtension_.id));
            }
            if (criteria.getIdGen() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIdGen(), GeneratorExtension_.idGen));
            }
            if (criteria.getStatusCurt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatusCurt(), GeneratorExtension_.statusCurt));
            }
            if (criteria.getDgType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDgType(), GeneratorExtension_.dgType));
            }
            if (criteria.getGeneratorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGeneratorId(),
                            root -> root.join(GeneratorExtension_.generator, JoinType.LEFT).get(Generator_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
