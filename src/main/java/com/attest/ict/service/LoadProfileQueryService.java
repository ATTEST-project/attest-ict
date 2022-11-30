package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.LoadProfile;
import com.attest.ict.repository.LoadProfileRepository;
import com.attest.ict.service.criteria.LoadProfileCriteria;
import com.attest.ict.service.dto.LoadProfileDTO;
import com.attest.ict.service.mapper.LoadProfileMapper;
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
 * Service for executing complex queries for {@link LoadProfile} entities in the database.
 * The main input is a {@link LoadProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LoadProfileDTO} or a {@link Page} of {@link LoadProfileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LoadProfileQueryService extends QueryService<LoadProfile> {

    private final Logger log = LoggerFactory.getLogger(LoadProfileQueryService.class);

    private final LoadProfileRepository loadProfileRepository;

    private final LoadProfileMapper loadProfileMapper;

    public LoadProfileQueryService(LoadProfileRepository loadProfileRepository, LoadProfileMapper loadProfileMapper) {
        this.loadProfileRepository = loadProfileRepository;
        this.loadProfileMapper = loadProfileMapper;
    }

    /**
     * Return a {@link List} of {@link LoadProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LoadProfileDTO> findByCriteria(LoadProfileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LoadProfile> specification = createSpecification(criteria);
        return loadProfileMapper.toDto(loadProfileRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LoadProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LoadProfileDTO> findByCriteria(LoadProfileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LoadProfile> specification = createSpecification(criteria);
        return loadProfileRepository.findAll(specification, page).map(loadProfileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LoadProfileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LoadProfile> specification = createSpecification(criteria);
        return loadProfileRepository.count(specification);
    }

    /**
     * Function to convert {@link LoadProfileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LoadProfile> createSpecification(LoadProfileCriteria criteria) {
        Specification<LoadProfile> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LoadProfile_.id));
            }
            if (criteria.getSeason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSeason(), LoadProfile_.season));
            }
            if (criteria.getTypicalDay() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTypicalDay(), LoadProfile_.typicalDay));
            }
            if (criteria.getMode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMode(), LoadProfile_.mode));
            }
            if (criteria.getTimeInterval() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeInterval(), LoadProfile_.timeInterval));
            }
            if (criteria.getUploadDateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUploadDateTime(), LoadProfile_.uploadDateTime));
            }
            if (criteria.getInputFileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInputFileId(),
                            root -> root.join(LoadProfile_.inputFile, JoinType.LEFT).get(InputFile_.id)
                        )
                    );
            }
            if (criteria.getLoadElValId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLoadElValId(),
                            root -> root.join(LoadProfile_.loadElVals, JoinType.LEFT).get(LoadElVal_.id)
                        )
                    );
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNetworkId(), root -> root.join(LoadProfile_.network, JoinType.LEFT).get(Network_.id))
                    );
            }
        }
        return specification;
    }
}
