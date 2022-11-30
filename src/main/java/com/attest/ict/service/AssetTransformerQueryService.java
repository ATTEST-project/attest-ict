package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.AssetTransformer;
import com.attest.ict.repository.AssetTransformerRepository;
import com.attest.ict.service.criteria.AssetTransformerCriteria;
import com.attest.ict.service.dto.AssetTransformerDTO;
import com.attest.ict.service.mapper.AssetTransformerMapper;
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
 * Service for executing complex queries for {@link AssetTransformer} entities in the database.
 * The main input is a {@link AssetTransformerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssetTransformerDTO} or a {@link Page} of {@link AssetTransformerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssetTransformerQueryService extends QueryService<AssetTransformer> {

    private final Logger log = LoggerFactory.getLogger(AssetTransformerQueryService.class);

    private final AssetTransformerRepository assetTransformerRepository;

    private final AssetTransformerMapper assetTransformerMapper;

    public AssetTransformerQueryService(
        AssetTransformerRepository assetTransformerRepository,
        AssetTransformerMapper assetTransformerMapper
    ) {
        this.assetTransformerRepository = assetTransformerRepository;
        this.assetTransformerMapper = assetTransformerMapper;
    }

    /**
     * Return a {@link List} of {@link AssetTransformerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssetTransformerDTO> findByCriteria(AssetTransformerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AssetTransformer> specification = createSpecification(criteria);
        return assetTransformerMapper.toDto(assetTransformerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssetTransformerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetTransformerDTO> findByCriteria(AssetTransformerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssetTransformer> specification = createSpecification(criteria);
        return assetTransformerRepository.findAll(specification, page).map(assetTransformerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssetTransformerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AssetTransformer> specification = createSpecification(criteria);
        return assetTransformerRepository.count(specification);
    }

    /**
     * Function to convert {@link AssetTransformerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssetTransformer> createSpecification(AssetTransformerCriteria criteria) {
        Specification<AssetTransformer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AssetTransformer_.id));
            }
            if (criteria.getBusNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBusNum(), AssetTransformer_.busNum));
            }
            if (criteria.getVoltageRatio() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVoltageRatio(), AssetTransformer_.voltageRatio));
            }
            if (criteria.getInsulationMedium() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getInsulationMedium(), AssetTransformer_.insulationMedium));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), AssetTransformer_.type));
            }
            if (criteria.getIndoorOutdoor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIndoorOutdoor(), AssetTransformer_.indoorOutdoor));
            }
            if (criteria.getAnnualMaxLoadKva() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAnnualMaxLoadKva(), AssetTransformer_.annualMaxLoadKva));
            }
            if (criteria.getAge() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAge(), AssetTransformer_.age));
            }
            if (criteria.getExternalCondition() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getExternalCondition(), AssetTransformer_.externalCondition));
            }
            if (criteria.getRatingKva() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRatingKva(), AssetTransformer_.ratingKva));
            }
            if (criteria.getNumConnectedCustomers() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getNumConnectedCustomers(), AssetTransformer_.numConnectedCustomers)
                    );
            }
            if (criteria.getNumSensitiveCustomers() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getNumSensitiveCustomers(), AssetTransformer_.numSensitiveCustomers)
                    );
            }
            if (criteria.getBackupSupply() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBackupSupply(), AssetTransformer_.backupSupply));
            }
            if (criteria.getCostOfFailureEuro() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCostOfFailureEuro(), AssetTransformer_.costOfFailureEuro));
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNetworkId(),
                            root -> root.join(AssetTransformer_.network, JoinType.LEFT).get(Network_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
