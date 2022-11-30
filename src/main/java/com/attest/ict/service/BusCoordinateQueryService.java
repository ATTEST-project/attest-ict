package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.BusCoordinate;
import com.attest.ict.repository.BusCoordinateRepository;
import com.attest.ict.service.criteria.BusCoordinateCriteria;
import com.attest.ict.service.dto.BusCoordinateDTO;
import com.attest.ict.service.mapper.BusCoordinateMapper;
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
 * Service for executing complex queries for {@link BusCoordinate} entities in the database.
 * The main input is a {@link BusCoordinateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BusCoordinateDTO} or a {@link Page} of {@link BusCoordinateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BusCoordinateQueryService extends QueryService<BusCoordinate> {

    private final Logger log = LoggerFactory.getLogger(BusCoordinateQueryService.class);

    private final BusCoordinateRepository busCoordinateRepository;

    private final BusCoordinateMapper busCoordinateMapper;

    public BusCoordinateQueryService(BusCoordinateRepository busCoordinateRepository, BusCoordinateMapper busCoordinateMapper) {
        this.busCoordinateRepository = busCoordinateRepository;
        this.busCoordinateMapper = busCoordinateMapper;
    }

    /**
     * Return a {@link List} of {@link BusCoordinateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BusCoordinateDTO> findByCriteria(BusCoordinateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BusCoordinate> specification = createSpecification(criteria);
        return busCoordinateMapper.toDto(busCoordinateRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BusCoordinateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BusCoordinateDTO> findByCriteria(BusCoordinateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BusCoordinate> specification = createSpecification(criteria);
        return busCoordinateRepository.findAll(specification, page).map(busCoordinateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BusCoordinateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BusCoordinate> specification = createSpecification(criteria);
        return busCoordinateRepository.count(specification);
    }

    /**
     * Function to convert {@link BusCoordinateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BusCoordinate> createSpecification(BusCoordinateCriteria criteria) {
        Specification<BusCoordinate> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BusCoordinate_.id));
            }
            if (criteria.getX() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getX(), BusCoordinate_.x));
            }
            if (criteria.getY() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getY(), BusCoordinate_.y));
            }
            if (criteria.getBusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBusId(), root -> root.join(BusCoordinate_.bus, JoinType.LEFT).get(Bus_.id))
                    );
            }
        }
        return specification;
    }
}
