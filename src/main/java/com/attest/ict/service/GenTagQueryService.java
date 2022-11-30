package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.GenTag;
import com.attest.ict.repository.GenTagRepository;
import com.attest.ict.service.criteria.GenTagCriteria;
import com.attest.ict.service.dto.GenTagDTO;
import com.attest.ict.service.mapper.GenTagMapper;
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
 * Service for executing complex queries for {@link GenTag} entities in the database.
 * The main input is a {@link GenTagCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GenTagDTO} or a {@link Page} of {@link GenTagDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GenTagQueryService extends QueryService<GenTag> {

    private final Logger log = LoggerFactory.getLogger(GenTagQueryService.class);

    private final GenTagRepository genTagRepository;

    private final GenTagMapper genTagMapper;

    public GenTagQueryService(GenTagRepository genTagRepository, GenTagMapper genTagMapper) {
        this.genTagRepository = genTagRepository;
        this.genTagMapper = genTagMapper;
    }

    /**
     * Return a {@link List} of {@link GenTagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GenTagDTO> findByCriteria(GenTagCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GenTag> specification = createSpecification(criteria);
        return genTagMapper.toDto(genTagRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GenTagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GenTagDTO> findByCriteria(GenTagCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GenTag> specification = createSpecification(criteria);
        return genTagRepository.findAll(specification, page).map(genTagMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GenTagCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GenTag> specification = createSpecification(criteria);
        return genTagRepository.count(specification);
    }

    /**
     * Function to convert {@link GenTagCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GenTag> createSpecification(GenTagCriteria criteria) {
        Specification<GenTag> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GenTag_.id));
            }
            if (criteria.getGenTag() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGenTag(), GenTag_.genTag));
            }
            if (criteria.getGeneratorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGeneratorId(),
                            root -> root.join(GenTag_.generator, JoinType.LEFT).get(Generator_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
