package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.BusName;
import com.attest.ict.repository.BusNameRepository;
import com.attest.ict.service.criteria.BusNameCriteria;
import com.attest.ict.service.dto.BusNameDTO;
import com.attest.ict.service.mapper.BusNameMapper;
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
 * Service for executing complex queries for {@link BusName} entities in the database.
 * The main input is a {@link BusNameCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BusNameDTO} or a {@link Page} of {@link BusNameDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BusNameQueryService extends QueryService<BusName> {

    private final Logger log = LoggerFactory.getLogger(BusNameQueryService.class);

    private final BusNameRepository busNameRepository;

    private final BusNameMapper busNameMapper;

    public BusNameQueryService(BusNameRepository busNameRepository, BusNameMapper busNameMapper) {
        this.busNameRepository = busNameRepository;
        this.busNameMapper = busNameMapper;
    }

    /**
     * Return a {@link List} of {@link BusNameDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BusNameDTO> findByCriteria(BusNameCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BusName> specification = createSpecification(criteria);
        return busNameMapper.toDto(busNameRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BusNameDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BusNameDTO> findByCriteria(BusNameCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BusName> specification = createSpecification(criteria);
        return busNameRepository.findAll(specification, page).map(busNameMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BusNameCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BusName> specification = createSpecification(criteria);
        return busNameRepository.count(specification);
    }

    /**
     * Function to convert {@link BusNameCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BusName> createSpecification(BusNameCriteria criteria) {
        Specification<BusName> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BusName_.id));
            }
            if (criteria.getBusName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBusName(), BusName_.busName));
            }
            if (criteria.getBusId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getBusId(), root -> root.join(BusName_.bus, JoinType.LEFT).get(Bus_.id)));
            }
        }
        return specification;
    }
}
