package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.InputFile;
import com.attest.ict.repository.InputFileRepository;
import com.attest.ict.service.criteria.InputFileCriteria;
import com.attest.ict.service.dto.InputFileDTO;
import com.attest.ict.service.mapper.InputFileMapper;
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
 * Service for executing complex queries for {@link InputFile} entities in the database.
 * The main input is a {@link InputFileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InputFileDTO} or a {@link Page} of {@link InputFileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InputFileQueryService extends QueryService<InputFile> {

    private final Logger log = LoggerFactory.getLogger(InputFileQueryService.class);

    private final InputFileRepository inputFileRepository;

    private final InputFileMapper inputFileMapper;

    public InputFileQueryService(InputFileRepository inputFileRepository, InputFileMapper inputFileMapper) {
        this.inputFileRepository = inputFileRepository;
        this.inputFileMapper = inputFileMapper;
    }

    /**
     * Return a {@link List} of {@link InputFileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InputFileDTO> findByCriteria(InputFileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InputFile> specification = createSpecification(criteria);
        return inputFileMapper.toDto(inputFileRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InputFileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InputFileDTO> findByCriteria(InputFileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InputFile> specification = createSpecification(criteria);
        return inputFileRepository.findAll(specification, page).map(inputFileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InputFileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InputFile> specification = createSpecification(criteria);
        return inputFileRepository.count(specification);
    }

    /**
     * Function to convert {@link InputFileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InputFile> createSpecification(InputFileCriteria criteria) {
        Specification<InputFile> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InputFile_.id));
            }
            if (criteria.getFileName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileName(), InputFile_.fileName));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), InputFile_.description));
            }
            if (criteria.getUploadTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUploadTime(), InputFile_.uploadTime));
            }
            if (criteria.getToolId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getToolId(), root -> root.join(InputFile_.tool, JoinType.LEFT).get(Tool_.id))
                    );
            }
            if (criteria.getGenProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGenProfileId(),
                            root -> root.join(InputFile_.genProfile, JoinType.LEFT).get(GenProfile_.id)
                        )
                    );
            }
            if (criteria.getFlexProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFlexProfileId(),
                            root -> root.join(InputFile_.flexProfile, JoinType.LEFT).get(FlexProfile_.id)
                        )
                    );
            }
            if (criteria.getLoadProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLoadProfileId(),
                            root -> root.join(InputFile_.loadProfile, JoinType.LEFT).get(LoadProfile_.id)
                        )
                    );
            }
            if (criteria.getTransfProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransfProfileId(),
                            root -> root.join(InputFile_.transfProfile, JoinType.LEFT).get(TransfProfile_.id)
                        )
                    );
            }
            if (criteria.getBranchProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBranchProfileId(),
                            root -> root.join(InputFile_.branchProfile, JoinType.LEFT).get(BranchProfile_.id)
                        )
                    );
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNetworkId(), root -> root.join(InputFile_.network, JoinType.LEFT).get(Network_.id))
                    );
            }
            if (criteria.getSimulationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSimulationId(),
                            root -> root.join(InputFile_.simulations, JoinType.LEFT).get(Simulation_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
