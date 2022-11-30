package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.AssetUGCable;
import com.attest.ict.repository.AssetUGCableRepository;
import com.attest.ict.service.criteria.AssetUGCableCriteria;
import com.attest.ict.service.dto.AssetUGCableDTO;
import com.attest.ict.service.mapper.AssetUGCableMapper;
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
 * Service for executing complex queries for {@link AssetUGCable} entities in the database.
 * The main input is a {@link AssetUGCableCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssetUGCableDTO} or a {@link Page} of {@link AssetUGCableDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssetUGCableQueryService extends QueryService<AssetUGCable> {

    private final Logger log = LoggerFactory.getLogger(AssetUGCableQueryService.class);

    private final AssetUGCableRepository assetUGCableRepository;

    private final AssetUGCableMapper assetUGCableMapper;

    public AssetUGCableQueryService(AssetUGCableRepository assetUGCableRepository, AssetUGCableMapper assetUGCableMapper) {
        this.assetUGCableRepository = assetUGCableRepository;
        this.assetUGCableMapper = assetUGCableMapper;
    }

    /**
     * Return a {@link List} of {@link AssetUGCableDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssetUGCableDTO> findByCriteria(AssetUGCableCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AssetUGCable> specification = createSpecification(criteria);
        return assetUGCableMapper.toDto(assetUGCableRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssetUGCableDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetUGCableDTO> findByCriteria(AssetUGCableCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssetUGCable> specification = createSpecification(criteria);
        return assetUGCableRepository.findAll(specification, page).map(assetUGCableMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssetUGCableCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AssetUGCable> specification = createSpecification(criteria);
        return assetUGCableRepository.count(specification);
    }

    /**
     * Function to convert {@link AssetUGCableCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssetUGCable> createSpecification(AssetUGCableCriteria criteria) {
        Specification<AssetUGCable> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AssetUGCable_.id));
            }
            if (criteria.getSectionLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSectionLabel(), AssetUGCable_.sectionLabel));
            }
            if (criteria.getCircuitId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCircuitId(), AssetUGCable_.circuitId));
            }
            if (criteria.getConductorCrossSectionalArea() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getConductorCrossSectionalArea(), AssetUGCable_.conductorCrossSectionalArea)
                    );
            }
            if (criteria.getSheathMaterial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSheathMaterial(), AssetUGCable_.sheathMaterial));
            }
            if (criteria.getDesignVoltage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDesignVoltage(), AssetUGCable_.designVoltage));
            }
            if (criteria.getOperatingVoltage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOperatingVoltage(), AssetUGCable_.operatingVoltage));
            }
            if (criteria.getInsulationTypeSheath() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getInsulationTypeSheath(), AssetUGCable_.insulationTypeSheath));
            }
            if (criteria.getConductorMaterial() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getConductorMaterial(), AssetUGCable_.conductorMaterial));
            }
            if (criteria.getAge() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAge(), AssetUGCable_.age));
            }
            if (criteria.getFaultHistory() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFaultHistory(), AssetUGCable_.faultHistory));
            }
            if (criteria.getLengthOfCableSectionMeters() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getLengthOfCableSectionMeters(), AssetUGCable_.lengthOfCableSectionMeters)
                    );
            }
            if (criteria.getSectionRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSectionRating(), AssetUGCable_.sectionRating));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), AssetUGCable_.type));
            }
            if (criteria.getNumberOfCores() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumberOfCores(), AssetUGCable_.numberOfCores));
            }
            if (criteria.getNetPerformanceCostOfFailureEuro() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getNetPerformanceCostOfFailureEuro(),
                            AssetUGCable_.netPerformanceCostOfFailureEuro
                        )
                    );
            }
            if (criteria.getRepairTimeHour() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRepairTimeHour(), AssetUGCable_.repairTimeHour));
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNetworkId(),
                            root -> root.join(AssetUGCable_.network, JoinType.LEFT).get(Network_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
