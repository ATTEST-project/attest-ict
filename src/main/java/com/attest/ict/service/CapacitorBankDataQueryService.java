package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.CapacitorBankData;
import com.attest.ict.repository.CapacitorBankDataRepository;
import com.attest.ict.service.criteria.CapacitorBankDataCriteria;
import com.attest.ict.service.dto.CapacitorBankDataDTO;
import com.attest.ict.service.mapper.CapacitorBankDataMapper;
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
 * Service for executing complex queries for {@link CapacitorBankData} entities in the database.
 * The main input is a {@link CapacitorBankDataCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CapacitorBankDataDTO} or a {@link Page} of {@link CapacitorBankDataDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CapacitorBankDataQueryService extends QueryService<CapacitorBankData> {

    private final Logger log = LoggerFactory.getLogger(CapacitorBankDataQueryService.class);

    private final CapacitorBankDataRepository capacitorBankDataRepository;

    private final CapacitorBankDataMapper capacitorBankDataMapper;

    public CapacitorBankDataQueryService(
        CapacitorBankDataRepository capacitorBankDataRepository,
        CapacitorBankDataMapper capacitorBankDataMapper
    ) {
        this.capacitorBankDataRepository = capacitorBankDataRepository;
        this.capacitorBankDataMapper = capacitorBankDataMapper;
    }

    /**
     * Return a {@link List} of {@link CapacitorBankDataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CapacitorBankDataDTO> findByCriteria(CapacitorBankDataCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CapacitorBankData> specification = createSpecification(criteria);
        return capacitorBankDataMapper.toDto(capacitorBankDataRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CapacitorBankDataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CapacitorBankDataDTO> findByCriteria(CapacitorBankDataCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CapacitorBankData> specification = createSpecification(criteria);
        return capacitorBankDataRepository.findAll(specification, page).map(capacitorBankDataMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CapacitorBankDataCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CapacitorBankData> specification = createSpecification(criteria);
        return capacitorBankDataRepository.count(specification);
    }

    /**
     * Function to convert {@link CapacitorBankDataCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CapacitorBankData> createSpecification(CapacitorBankDataCriteria criteria) {
        Specification<CapacitorBankData> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CapacitorBankData_.id));
            }
            if (criteria.getBusNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBusNum(), CapacitorBankData_.busNum));
            }
            if (criteria.getNodeId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNodeId(), CapacitorBankData_.nodeId));
            }
            if (criteria.getBankId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBankId(), CapacitorBankData_.bankId));
            }
            if (criteria.getQnom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQnom(), CapacitorBankData_.qnom));
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNetworkId(),
                            root -> root.join(CapacitorBankData_.network, JoinType.LEFT).get(Network_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
