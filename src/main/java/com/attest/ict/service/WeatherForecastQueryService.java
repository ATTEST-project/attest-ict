package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.WeatherForecast;
import com.attest.ict.repository.WeatherForecastRepository;
import com.attest.ict.service.criteria.WeatherForecastCriteria;
import com.attest.ict.service.dto.WeatherForecastDTO;
import com.attest.ict.service.mapper.WeatherForecastMapper;
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
 * Service for executing complex queries for {@link WeatherForecast} entities in the database.
 * The main input is a {@link WeatherForecastCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WeatherForecastDTO} or a {@link Page} of {@link WeatherForecastDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WeatherForecastQueryService extends QueryService<WeatherForecast> {

    private final Logger log = LoggerFactory.getLogger(WeatherForecastQueryService.class);

    private final WeatherForecastRepository weatherForecastRepository;

    private final WeatherForecastMapper weatherForecastMapper;

    public WeatherForecastQueryService(WeatherForecastRepository weatherForecastRepository, WeatherForecastMapper weatherForecastMapper) {
        this.weatherForecastRepository = weatherForecastRepository;
        this.weatherForecastMapper = weatherForecastMapper;
    }

    /**
     * Return a {@link List} of {@link WeatherForecastDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WeatherForecastDTO> findByCriteria(WeatherForecastCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WeatherForecast> specification = createSpecification(criteria);
        return weatherForecastMapper.toDto(weatherForecastRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WeatherForecastDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WeatherForecastDTO> findByCriteria(WeatherForecastCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WeatherForecast> specification = createSpecification(criteria);
        return weatherForecastRepository.findAll(specification, page).map(weatherForecastMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WeatherForecastCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WeatherForecast> specification = createSpecification(criteria);
        return weatherForecastRepository.count(specification);
    }

    /**
     * Function to convert {@link WeatherForecastCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WeatherForecast> createSpecification(WeatherForecastCriteria criteria) {
        Specification<WeatherForecast> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WeatherForecast_.id));
            }
            if (criteria.getSolarProfile() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSolarProfile(), WeatherForecast_.solarProfile));
            }
            if (criteria.getOutsideTemp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOutsideTemp(), WeatherForecast_.outsideTemp));
            }
        }
        return specification;
    }
}
