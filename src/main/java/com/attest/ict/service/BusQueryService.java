package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.Bus;
import com.attest.ict.repository.BusRepository;
import com.attest.ict.service.criteria.BusCriteria;
import com.attest.ict.service.dto.BusDTO;
import com.attest.ict.service.mapper.BusMapper;
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
 * Service for executing complex queries for {@link Bus} entities in the database.
 * The main input is a {@link BusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BusDTO} or a {@link Page} of {@link BusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BusQueryService extends QueryService<Bus> {

    private final Logger log = LoggerFactory.getLogger(BusQueryService.class);

    private final BusRepository busRepository;

    private final BusMapper busMapper;

    public BusQueryService(BusRepository busRepository, BusMapper busMapper) {
        this.busRepository = busRepository;
        this.busMapper = busMapper;
    }

    /**
     * Return a {@link List} of {@link BusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BusDTO> findByCriteria(BusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Bus> specification = createSpecification(criteria);
        return busMapper.toDto(busRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BusDTO> findByCriteria(BusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Bus> specification = createSpecification(criteria);
        return busRepository.findAll(specification, page).map(busMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Bus> specification = createSpecification(criteria);
        return busRepository.count(specification);
    }

    /**
     * Function to convert {@link BusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Bus> createSpecification(BusCriteria criteria) {
        Specification<Bus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Bus_.id));
            }
            if (criteria.getBusNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBusNum(), Bus_.busNum));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getType(), Bus_.type));
            }
            if (criteria.getActivePower() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActivePower(), Bus_.activePower));
            }
            if (criteria.getReactivePower() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReactivePower(), Bus_.reactivePower));
            }
            if (criteria.getConductance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getConductance(), Bus_.conductance));
            }
            if (criteria.getSusceptance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSusceptance(), Bus_.susceptance));
            }
            if (criteria.getArea() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getArea(), Bus_.area));
            }
            if (criteria.getVm() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVm(), Bus_.vm));
            }
            if (criteria.getVa() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVa(), Bus_.va));
            }
            if (criteria.getBaseKv() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBaseKv(), Bus_.baseKv));
            }
            if (criteria.getZone() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getZone(), Bus_.zone));
            }
            if (criteria.getVmax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVmax(), Bus_.vmax));
            }
            if (criteria.getVmin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVmin(), Bus_.vmin));
            }
            if (criteria.getLoadELValId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLoadELValId(), root -> root.join(Bus_.loadELVals, JoinType.LEFT).get(LoadElVal_.id))
                    );
            }
            if (criteria.getBusNameId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBusNameId(), root -> root.join(Bus_.busName, JoinType.LEFT).get(BusName_.id))
                    );
            }
            if (criteria.getBusExtensionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBusExtensionId(),
                            root -> root.join(Bus_.busExtension, JoinType.LEFT).get(BusExtension_.id)
                        )
                    );
            }
            if (criteria.getBusCoordinateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBusCoordinateId(),
                            root -> root.join(Bus_.busCoordinate, JoinType.LEFT).get(BusCoordinate_.id)
                        )
                    );
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNetworkId(), root -> root.join(Bus_.network, JoinType.LEFT).get(Network_.id))
                    );
            }
        }
        return specification;
    }
}
