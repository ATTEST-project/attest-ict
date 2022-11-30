package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.Storage;
import com.attest.ict.repository.StorageRepository;
import com.attest.ict.service.criteria.StorageCriteria;
import com.attest.ict.service.dto.StorageDTO;
import com.attest.ict.service.mapper.StorageMapper;
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
 * Service for executing complex queries for {@link Storage} entities in the database.
 * The main input is a {@link StorageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StorageDTO} or a {@link Page} of {@link StorageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StorageQueryService extends QueryService<Storage> {

    private final Logger log = LoggerFactory.getLogger(StorageQueryService.class);

    private final StorageRepository storageRepository;

    private final StorageMapper storageMapper;

    public StorageQueryService(StorageRepository storageRepository, StorageMapper storageMapper) {
        this.storageRepository = storageRepository;
        this.storageMapper = storageMapper;
    }

    /**
     * Return a {@link List} of {@link StorageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StorageDTO> findByCriteria(StorageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Storage> specification = createSpecification(criteria);
        return storageMapper.toDto(storageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StorageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StorageDTO> findByCriteria(StorageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Storage> specification = createSpecification(criteria);
        return storageRepository.findAll(specification, page).map(storageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StorageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Storage> specification = createSpecification(criteria);
        return storageRepository.count(specification);
    }

    /**
     * Function to convert {@link StorageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Storage> createSpecification(StorageCriteria criteria) {
        Specification<Storage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Storage_.id));
            }
            if (criteria.getBusNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBusNum(), Storage_.busNum));
            }
            if (criteria.getPs() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPs(), Storage_.ps));
            }
            if (criteria.getQs() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQs(), Storage_.qs));
            }
            if (criteria.getEnergy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEnergy(), Storage_.energy));
            }
            if (criteria.geteRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.geteRating(), Storage_.eRating));
            }
            if (criteria.getChargeRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getChargeRating(), Storage_.chargeRating));
            }
            if (criteria.getDischargeRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDischargeRating(), Storage_.dischargeRating));
            }
            if (criteria.getChargeEfficiency() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getChargeEfficiency(), Storage_.chargeEfficiency));
            }
            if (criteria.getThermalRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getThermalRating(), Storage_.thermalRating));
            }
            if (criteria.getQmin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQmin(), Storage_.qmin));
            }
            if (criteria.getQmax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQmax(), Storage_.qmax));
            }
            if (criteria.getR() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getR(), Storage_.r));
            }
            if (criteria.getX() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getX(), Storage_.x));
            }
            if (criteria.getpLoss() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getpLoss(), Storage_.pLoss));
            }
            if (criteria.getqLoss() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getqLoss(), Storage_.qLoss));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), Storage_.status));
            }
            if (criteria.getSocInitial() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSocInitial(), Storage_.socInitial));
            }
            if (criteria.getSocMin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSocMin(), Storage_.socMin));
            }
            if (criteria.getSocMax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSocMax(), Storage_.socMax));
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNetworkId(), root -> root.join(Storage_.network, JoinType.LEFT).get(Network_.id))
                    );
            }
        }
        return specification;
    }
}
