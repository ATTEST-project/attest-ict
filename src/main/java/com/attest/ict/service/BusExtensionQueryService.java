package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.BusExtension;
import com.attest.ict.repository.BusExtensionRepository;
import com.attest.ict.service.criteria.BusExtensionCriteria;
import com.attest.ict.service.dto.BusExtensionDTO;
import com.attest.ict.service.mapper.BusExtensionMapper;
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
 * Service for executing complex queries for {@link BusExtension} entities in the database.
 * The main input is a {@link BusExtensionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BusExtensionDTO} or a {@link Page} of {@link BusExtensionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BusExtensionQueryService extends QueryService<BusExtension> {

    private final Logger log = LoggerFactory.getLogger(BusExtensionQueryService.class);

    private final BusExtensionRepository busExtensionRepository;

    private final BusExtensionMapper busExtensionMapper;

    public BusExtensionQueryService(BusExtensionRepository busExtensionRepository, BusExtensionMapper busExtensionMapper) {
        this.busExtensionRepository = busExtensionRepository;
        this.busExtensionMapper = busExtensionMapper;
    }

    /**
     * Return a {@link List} of {@link BusExtensionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BusExtensionDTO> findByCriteria(BusExtensionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BusExtension> specification = createSpecification(criteria);
        return busExtensionMapper.toDto(busExtensionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BusExtensionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BusExtensionDTO> findByCriteria(BusExtensionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BusExtension> specification = createSpecification(criteria);
        return busExtensionRepository.findAll(specification, page).map(busExtensionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BusExtensionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BusExtension> specification = createSpecification(criteria);
        return busExtensionRepository.count(specification);
    }

    /**
     * Function to convert {@link BusExtensionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BusExtension> createSpecification(BusExtensionCriteria criteria) {
        Specification<BusExtension> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BusExtension_.id));
            }
            if (criteria.getHasGen() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHasGen(), BusExtension_.hasGen));
            }
            if (criteria.getIsLoad() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIsLoad(), BusExtension_.isLoad));
            }
            if (criteria.getSnomMva() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSnomMva(), BusExtension_.snomMva));
            }
            if (criteria.getSx() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSx(), BusExtension_.sx));
            }
            if (criteria.getSy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSy(), BusExtension_.sy));
            }
            if (criteria.getGx() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGx(), BusExtension_.gx));
            }
            if (criteria.getGy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGy(), BusExtension_.gy));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), BusExtension_.status));
            }
            if (criteria.getIncrementCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIncrementCost(), BusExtension_.incrementCost));
            }
            if (criteria.getDecrementCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDecrementCost(), BusExtension_.decrementCost));
            }
            if (criteria.getmRid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getmRid(), BusExtension_.mRid));
            }
            if (criteria.getBusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBusId(), root -> root.join(BusExtension_.bus, JoinType.LEFT).get(Bus_.id))
                    );
            }
        }
        return specification;
    }
}
