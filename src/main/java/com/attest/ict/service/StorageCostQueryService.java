package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.StorageCost;
import com.attest.ict.repository.StorageCostRepository;
import com.attest.ict.service.criteria.StorageCostCriteria;
import com.attest.ict.service.dto.StorageCostDTO;
import com.attest.ict.service.mapper.StorageCostMapper;
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
 * Service for executing complex queries for {@link StorageCost} entities in the database.
 * The main input is a {@link StorageCostCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StorageCostDTO} or a {@link Page} of {@link StorageCostDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StorageCostQueryService extends QueryService<StorageCost> {

    private final Logger log = LoggerFactory.getLogger(StorageCostQueryService.class);

    private final StorageCostRepository storageCostRepository;

    private final StorageCostMapper storageCostMapper;

    public StorageCostQueryService(StorageCostRepository storageCostRepository, StorageCostMapper storageCostMapper) {
        this.storageCostRepository = storageCostRepository;
        this.storageCostMapper = storageCostMapper;
    }

    /**
     * Return a {@link List} of {@link StorageCostDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StorageCostDTO> findByCriteria(StorageCostCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StorageCost> specification = createSpecification(criteria);
        return storageCostMapper.toDto(storageCostRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StorageCostDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StorageCostDTO> findByCriteria(StorageCostCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StorageCost> specification = createSpecification(criteria);
        return storageCostRepository.findAll(specification, page).map(storageCostMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StorageCostCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StorageCost> specification = createSpecification(criteria);
        return storageCostRepository.count(specification);
    }

    /**
     * Function to convert {@link StorageCostCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StorageCost> createSpecification(StorageCostCriteria criteria) {
        Specification<StorageCost> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StorageCost_.id));
            }
            if (criteria.getBusNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBusNum(), StorageCost_.busNum));
            }
            if (criteria.getCostA() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCostA(), StorageCost_.costA));
            }
            if (criteria.getCostB() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCostB(), StorageCost_.costB));
            }
            if (criteria.getCostC() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCostC(), StorageCost_.costC));
            }
        }
        return specification;
    }
}
