package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.Network;
import com.attest.ict.repository.NetworkRepository;
import com.attest.ict.service.criteria.NetworkCriteria;
import com.attest.ict.service.dto.NetworkDTO;
import com.attest.ict.service.mapper.NetworkMapper;
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
 * Service for executing complex queries for {@link Network} entities in the database.
 * The main input is a {@link NetworkCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NetworkDTO} or a {@link Page} of {@link NetworkDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NetworkQueryService extends QueryService<Network> {

    private final Logger log = LoggerFactory.getLogger(NetworkQueryService.class);

    private final NetworkRepository networkRepository;

    private final NetworkMapper networkMapper;

    public NetworkQueryService(NetworkRepository networkRepository, NetworkMapper networkMapper) {
        this.networkRepository = networkRepository;
        this.networkMapper = networkMapper;
    }

    /**
     * Return a {@link List} of {@link NetworkDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NetworkDTO> findByCriteria(NetworkCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Network> specification = createSpecification(criteria);
        return networkMapper.toDto(networkRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NetworkDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NetworkDTO> findByCriteria(NetworkCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Network> specification = createSpecification(criteria);
        return networkRepository.findAll(specification, page).map(networkMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NetworkCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Network> specification = createSpecification(criteria);
        return networkRepository.count(specification);
    }

    /**
     * Function to convert {@link NetworkCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Network> createSpecification(NetworkCriteria criteria) {
        Specification<Network> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Network_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Network_.name));
            }
            if (criteria.getMpcName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMpcName(), Network_.mpcName));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), Network_.country));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Network_.type));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Network_.description));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), Network_.isDeleted));
            }
            if (criteria.getNetworkDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNetworkDate(), Network_.networkDate));
            }
            if (criteria.getVersion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVersion(), Network_.version));
            }
            if (criteria.getCreationDateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDateTime(), Network_.creationDateTime));
            }
            if (criteria.getUpdateDateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdateDateTime(), Network_.updateDateTime));
            }
            if (criteria.getBusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBusId(), root -> root.join(Network_.buses, JoinType.LEFT).get(Bus_.id))
                    );
            }
            if (criteria.getGeneratorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGeneratorId(),
                            root -> root.join(Network_.generators, JoinType.LEFT).get(Generator_.id)
                        )
                    );
            }
            if (criteria.getBranchId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBranchId(), root -> root.join(Network_.branches, JoinType.LEFT).get(Branch_.id))
                    );
            }
            if (criteria.getStorageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getStorageId(), root -> root.join(Network_.storages, JoinType.LEFT).get(Storage_.id))
                    );
            }
            if (criteria.getTransformerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransformerId(),
                            root -> root.join(Network_.transformers, JoinType.LEFT).get(Transformer_.id)
                        )
                    );
            }
            if (criteria.getCapacitorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCapacitorId(),
                            root -> root.join(Network_.capacitors, JoinType.LEFT).get(CapacitorBankData_.id)
                        )
                    );
            }
            if (criteria.getInputFileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInputFileId(),
                            root -> root.join(Network_.inputFiles, JoinType.LEFT).get(InputFile_.id)
                        )
                    );
            }
            if (criteria.getAssetUgCableId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetUgCableId(),
                            root -> root.join(Network_.assetUgCables, JoinType.LEFT).get(AssetUGCable_.id)
                        )
                    );
            }
            if (criteria.getAssetTransformerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetTransformerId(),
                            root -> root.join(Network_.assetTransformers, JoinType.LEFT).get(AssetTransformer_.id)
                        )
                    );
            }
            if (criteria.getBillingConsumptionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBillingConsumptionId(),
                            root -> root.join(Network_.billingConsumptions, JoinType.LEFT).get(BillingConsumption_.id)
                        )
                    );
            }
            if (criteria.getBillingDerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBillingDerId(),
                            root -> root.join(Network_.billingDers, JoinType.LEFT).get(BillingDer_.id)
                        )
                    );
            }
            if (criteria.getLineCableId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLineCableId(),
                            root -> root.join(Network_.lineCables, JoinType.LEFT).get(LineCable_.id)
                        )
                    );
            }
            if (criteria.getGenProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGenProfileId(),
                            root -> root.join(Network_.genProfiles, JoinType.LEFT).get(GenProfile_.id)
                        )
                    );
            }
            if (criteria.getLoadProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLoadProfileId(),
                            root -> root.join(Network_.loadProfiles, JoinType.LEFT).get(LoadProfile_.id)
                        )
                    );
            }
            if (criteria.getFlexProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFlexProfileId(),
                            root -> root.join(Network_.flexProfiles, JoinType.LEFT).get(FlexProfile_.id)
                        )
                    );
            }
            if (criteria.getTransfProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransfProfileId(),
                            root -> root.join(Network_.transfProfiles, JoinType.LEFT).get(TransfProfile_.id)
                        )
                    );
            }
            if (criteria.getBranchProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBranchProfileId(),
                            root -> root.join(Network_.branchProfiles, JoinType.LEFT).get(BranchProfile_.id)
                        )
                    );
            }
            if (criteria.getTopologyBusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTopologyBusId(),
                            root -> root.join(Network_.topologyBuses, JoinType.LEFT).get(TopologyBus_.id)
                        )
                    );
            }
            if (criteria.getDsoTsoConnectionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDsoTsoConnectionId(),
                            root -> root.join(Network_.dsoTsoConnections, JoinType.LEFT).get(DsoTsoConnection_.id)
                        )
                    );
            }
            if (criteria.getBaseMVAId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBaseMVAId(), root -> root.join(Network_.baseMVA, JoinType.LEFT).get(BaseMVA_.id))
                    );
            }
            if (criteria.getVoltageLevelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getVoltageLevelId(),
                            root -> root.join(Network_.voltageLevel, JoinType.LEFT).get(VoltageLevel_.id)
                        )
                    );
            }
            if (criteria.getSimulationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSimulationId(),
                            root -> root.join(Network_.simulations, JoinType.LEFT).get(Simulation_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
