package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.SolarData;
import com.attest.ict.repository.SolarDataRepository;
import com.attest.ict.service.criteria.SolarDataCriteria;
import com.attest.ict.service.dto.SolarDataDTO;
import com.attest.ict.service.mapper.SolarDataMapper;
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
 * Service for executing complex queries for {@link SolarData} entities in the database.
 * The main input is a {@link SolarDataCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SolarDataDTO} or a {@link Page} of {@link SolarDataDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SolarDataQueryService extends QueryService<SolarData> {

    private final Logger log = LoggerFactory.getLogger(SolarDataQueryService.class);

    private final SolarDataRepository solarDataRepository;

    private final SolarDataMapper solarDataMapper;

    public SolarDataQueryService(SolarDataRepository solarDataRepository, SolarDataMapper solarDataMapper) {
        this.solarDataRepository = solarDataRepository;
        this.solarDataMapper = solarDataMapper;
    }

    /**
     * Return a {@link List} of {@link SolarDataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SolarDataDTO> findByCriteria(SolarDataCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SolarData> specification = createSpecification(criteria);
        return solarDataMapper.toDto(solarDataRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SolarDataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SolarDataDTO> findByCriteria(SolarDataCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SolarData> specification = createSpecification(criteria);
        return solarDataRepository.findAll(specification, page).map(solarDataMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SolarDataCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SolarData> specification = createSpecification(criteria);
        return solarDataRepository.count(specification);
    }

    /**
     * Function to convert {@link SolarDataCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SolarData> createSpecification(SolarDataCriteria criteria) {
        Specification<SolarData> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SolarData_.id));
            }
            if (criteria.getP() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getP(), SolarData_.p));
            }
            if (criteria.getHour() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHour(), SolarData_.hour));
            }
        }
        return specification;
    }
}
