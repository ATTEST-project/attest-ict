package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.WindData;
import com.attest.ict.repository.WindDataRepository;
import com.attest.ict.service.criteria.WindDataCriteria;
import com.attest.ict.service.dto.WindDataDTO;
import com.attest.ict.service.mapper.WindDataMapper;
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
 * Service for executing complex queries for {@link WindData} entities in the database.
 * The main input is a {@link WindDataCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WindDataDTO} or a {@link Page} of {@link WindDataDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WindDataQueryService extends QueryService<WindData> {

    private final Logger log = LoggerFactory.getLogger(WindDataQueryService.class);

    private final WindDataRepository windDataRepository;

    private final WindDataMapper windDataMapper;

    public WindDataQueryService(WindDataRepository windDataRepository, WindDataMapper windDataMapper) {
        this.windDataRepository = windDataRepository;
        this.windDataMapper = windDataMapper;
    }

    /**
     * Return a {@link List} of {@link WindDataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WindDataDTO> findByCriteria(WindDataCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WindData> specification = createSpecification(criteria);
        return windDataMapper.toDto(windDataRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WindDataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WindDataDTO> findByCriteria(WindDataCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WindData> specification = createSpecification(criteria);
        return windDataRepository.findAll(specification, page).map(windDataMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WindDataCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WindData> specification = createSpecification(criteria);
        return windDataRepository.count(specification);
    }

    /**
     * Function to convert {@link WindDataCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WindData> createSpecification(WindDataCriteria criteria) {
        Specification<WindData> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WindData_.id));
            }
            if (criteria.getWindSpeed() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWindSpeed(), WindData_.windSpeed));
            }
            if (criteria.getHour() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHour(), WindData_.hour));
            }
        }
        return specification;
    }
}
