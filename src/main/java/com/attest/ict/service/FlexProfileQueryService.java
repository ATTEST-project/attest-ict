package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.FlexProfile;
import com.attest.ict.repository.FlexProfileRepository;
import com.attest.ict.service.criteria.FlexProfileCriteria;
import com.attest.ict.service.dto.FlexProfileDTO;
import com.attest.ict.service.mapper.FlexProfileMapper;
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
 * Service for executing complex queries for {@link FlexProfile} entities in the database.
 * The main input is a {@link FlexProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FlexProfileDTO} or a {@link Page} of {@link FlexProfileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FlexProfileQueryService extends QueryService<FlexProfile> {

    private final Logger log = LoggerFactory.getLogger(FlexProfileQueryService.class);

    private final FlexProfileRepository flexProfileRepository;

    private final FlexProfileMapper flexProfileMapper;

    public FlexProfileQueryService(FlexProfileRepository flexProfileRepository, FlexProfileMapper flexProfileMapper) {
        this.flexProfileRepository = flexProfileRepository;
        this.flexProfileMapper = flexProfileMapper;
    }

    /**
     * Return a {@link List} of {@link FlexProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FlexProfileDTO> findByCriteria(FlexProfileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FlexProfile> specification = createSpecification(criteria);
        return flexProfileMapper.toDto(flexProfileRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FlexProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FlexProfileDTO> findByCriteria(FlexProfileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FlexProfile> specification = createSpecification(criteria);
        return flexProfileRepository.findAll(specification, page).map(flexProfileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FlexProfileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FlexProfile> specification = createSpecification(criteria);
        return flexProfileRepository.count(specification);
    }

    /**
     * Function to convert {@link FlexProfileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FlexProfile> createSpecification(FlexProfileCriteria criteria) {
        Specification<FlexProfile> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FlexProfile_.id));
            }
            if (criteria.getSeason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSeason(), FlexProfile_.season));
            }
            if (criteria.getTypicalDay() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTypicalDay(), FlexProfile_.typicalDay));
            }
            if (criteria.getMode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMode(), FlexProfile_.mode));
            }
            if (criteria.getTimeInterval() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeInterval(), FlexProfile_.timeInterval));
            }
            if (criteria.getUploadDateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUploadDateTime(), FlexProfile_.uploadDateTime));
            }
            if (criteria.getInputFileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInputFileId(),
                            root -> root.join(FlexProfile_.inputFile, JoinType.LEFT).get(InputFile_.id)
                        )
                    );
            }
            if (criteria.getFlexElValId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFlexElValId(),
                            root -> root.join(FlexProfile_.flexElVals, JoinType.LEFT).get(FlexElVal_.id)
                        )
                    );
            }
            if (criteria.getFlexCostId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFlexCostId(),
                            root -> root.join(FlexProfile_.flexCosts, JoinType.LEFT).get(FlexCost_.id)
                        )
                    );
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNetworkId(), root -> root.join(FlexProfile_.network, JoinType.LEFT).get(Network_.id))
                    );
            }
        }
        return specification;
    }
}
