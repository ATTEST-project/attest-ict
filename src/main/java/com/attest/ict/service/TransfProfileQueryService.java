package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.TransfProfile;
import com.attest.ict.repository.TransfProfileRepository;
import com.attest.ict.service.criteria.TransfProfileCriteria;
import com.attest.ict.service.dto.TransfProfileDTO;
import com.attest.ict.service.mapper.TransfProfileMapper;
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
 * Service for executing complex queries for {@link TransfProfile} entities in the database.
 * The main input is a {@link TransfProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TransfProfileDTO} or a {@link Page} of {@link TransfProfileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransfProfileQueryService extends QueryService<TransfProfile> {

    private final Logger log = LoggerFactory.getLogger(TransfProfileQueryService.class);

    private final TransfProfileRepository transfProfileRepository;

    private final TransfProfileMapper transfProfileMapper;

    public TransfProfileQueryService(TransfProfileRepository transfProfileRepository, TransfProfileMapper transfProfileMapper) {
        this.transfProfileRepository = transfProfileRepository;
        this.transfProfileMapper = transfProfileMapper;
    }

    /**
     * Return a {@link List} of {@link TransfProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransfProfileDTO> findByCriteria(TransfProfileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TransfProfile> specification = createSpecification(criteria);
        return transfProfileMapper.toDto(transfProfileRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TransfProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransfProfileDTO> findByCriteria(TransfProfileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TransfProfile> specification = createSpecification(criteria);
        return transfProfileRepository.findAll(specification, page).map(transfProfileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransfProfileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TransfProfile> specification = createSpecification(criteria);
        return transfProfileRepository.count(specification);
    }

    /**
     * Function to convert {@link TransfProfileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TransfProfile> createSpecification(TransfProfileCriteria criteria) {
        Specification<TransfProfile> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TransfProfile_.id));
            }
            if (criteria.getSeason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSeason(), TransfProfile_.season));
            }
            if (criteria.getTypicalDay() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTypicalDay(), TransfProfile_.typicalDay));
            }
            if (criteria.getMode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMode(), TransfProfile_.mode));
            }
            if (criteria.getTimeInterval() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeInterval(), TransfProfile_.timeInterval));
            }
            if (criteria.getUploadDateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUploadDateTime(), TransfProfile_.uploadDateTime));
            }
            if (criteria.getInputFileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInputFileId(),
                            root -> root.join(TransfProfile_.inputFile, JoinType.LEFT).get(InputFile_.id)
                        )
                    );
            }
            if (criteria.getTransfElValId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransfElValId(),
                            root -> root.join(TransfProfile_.transfElVals, JoinType.LEFT).get(TransfElVal_.id)
                        )
                    );
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNetworkId(),
                            root -> root.join(TransfProfile_.network, JoinType.LEFT).get(Network_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
