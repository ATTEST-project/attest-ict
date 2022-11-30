package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.GenProfile;
import com.attest.ict.repository.GenProfileRepository;
import com.attest.ict.service.criteria.GenProfileCriteria;
import com.attest.ict.service.dto.GenProfileDTO;
import com.attest.ict.service.mapper.GenProfileMapper;
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
 * Service for executing complex queries for {@link GenProfile} entities in the database.
 * The main input is a {@link GenProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GenProfileDTO} or a {@link Page} of {@link GenProfileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GenProfileQueryService extends QueryService<GenProfile> {

    private final Logger log = LoggerFactory.getLogger(GenProfileQueryService.class);

    private final GenProfileRepository genProfileRepository;

    private final GenProfileMapper genProfileMapper;

    public GenProfileQueryService(GenProfileRepository genProfileRepository, GenProfileMapper genProfileMapper) {
        this.genProfileRepository = genProfileRepository;
        this.genProfileMapper = genProfileMapper;
    }

    /**
     * Return a {@link List} of {@link GenProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GenProfileDTO> findByCriteria(GenProfileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GenProfile> specification = createSpecification(criteria);
        return genProfileMapper.toDto(genProfileRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GenProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GenProfileDTO> findByCriteria(GenProfileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GenProfile> specification = createSpecification(criteria);
        return genProfileRepository.findAll(specification, page).map(genProfileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GenProfileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GenProfile> specification = createSpecification(criteria);
        return genProfileRepository.count(specification);
    }

    /**
     * Function to convert {@link GenProfileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GenProfile> createSpecification(GenProfileCriteria criteria) {
        Specification<GenProfile> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GenProfile_.id));
            }
            if (criteria.getSeason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSeason(), GenProfile_.season));
            }
            if (criteria.getTypicalDay() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTypicalDay(), GenProfile_.typicalDay));
            }
            if (criteria.getMode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMode(), GenProfile_.mode));
            }
            if (criteria.getTimeInterval() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeInterval(), GenProfile_.timeInterval));
            }
            if (criteria.getUploadDateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUploadDateTime(), GenProfile_.uploadDateTime));
            }
            if (criteria.getInputFileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInputFileId(),
                            root -> root.join(GenProfile_.inputFile, JoinType.LEFT).get(InputFile_.id)
                        )
                    );
            }
            if (criteria.getGenElValId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGenElValId(),
                            root -> root.join(GenProfile_.genElVals, JoinType.LEFT).get(GenElVal_.id)
                        )
                    );
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNetworkId(), root -> root.join(GenProfile_.network, JoinType.LEFT).get(Network_.id))
                    );
            }
        }
        return specification;
    }
}
