package com.attest.ict.service;

import com.attest.ict.domain.*; // for static metamodels
import com.attest.ict.domain.Generator;
import com.attest.ict.repository.GeneratorRepository;
import com.attest.ict.service.criteria.GeneratorCriteria;
import com.attest.ict.service.dto.GeneratorDTO;
import com.attest.ict.service.mapper.GeneratorMapper;
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
 * Service for executing complex queries for {@link Generator} entities in the database.
 * The main input is a {@link GeneratorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GeneratorDTO} or a {@link Page} of {@link GeneratorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GeneratorQueryService extends QueryService<Generator> {

    private final Logger log = LoggerFactory.getLogger(GeneratorQueryService.class);

    private final GeneratorRepository generatorRepository;

    private final GeneratorMapper generatorMapper;

    public GeneratorQueryService(GeneratorRepository generatorRepository, GeneratorMapper generatorMapper) {
        this.generatorRepository = generatorRepository;
        this.generatorMapper = generatorMapper;
    }

    /**
     * Return a {@link List} of {@link GeneratorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GeneratorDTO> findByCriteria(GeneratorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Generator> specification = createSpecification(criteria);
        return generatorMapper.toDto(generatorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GeneratorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GeneratorDTO> findByCriteria(GeneratorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Generator> specification = createSpecification(criteria);
        return generatorRepository.findAll(specification, page).map(generatorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GeneratorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Generator> specification = createSpecification(criteria);
        return generatorRepository.count(specification);
    }

    /**
     * Function to convert {@link GeneratorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Generator> createSpecification(GeneratorCriteria criteria) {
        Specification<Generator> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Generator_.id));
            }
            if (criteria.getBusNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBusNum(), Generator_.busNum));
            }
            if (criteria.getPg() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPg(), Generator_.pg));
            }
            if (criteria.getQg() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQg(), Generator_.qg));
            }
            if (criteria.getQmax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQmax(), Generator_.qmax));
            }
            if (criteria.getQmin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQmin(), Generator_.qmin));
            }
            if (criteria.getVg() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVg(), Generator_.vg));
            }
            if (criteria.getmBase() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getmBase(), Generator_.mBase));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), Generator_.status));
            }
            if (criteria.getPmax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPmax(), Generator_.pmax));
            }
            if (criteria.getPmin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPmin(), Generator_.pmin));
            }
            if (criteria.getPc1() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPc1(), Generator_.pc1));
            }
            if (criteria.getPc2() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPc2(), Generator_.pc2));
            }
            if (criteria.getQc1min() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQc1min(), Generator_.qc1min));
            }
            if (criteria.getQc1max() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQc1max(), Generator_.qc1max));
            }
            if (criteria.getQc2min() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQc2min(), Generator_.qc2min));
            }
            if (criteria.getQc2max() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQc2max(), Generator_.qc2max));
            }
            if (criteria.getRampAgc() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRampAgc(), Generator_.rampAgc));
            }
            if (criteria.getRamp10() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRamp10(), Generator_.ramp10));
            }
            if (criteria.getRamp30() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRamp30(), Generator_.ramp30));
            }
            if (criteria.getRampQ() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRampQ(), Generator_.rampQ));
            }
            if (criteria.getApf() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getApf(), Generator_.apf));
            }
            if (criteria.getGenElValId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGenElValId(),
                            root -> root.join(Generator_.genElVals, JoinType.LEFT).get(GenElVal_.id)
                        )
                    );
            }
            if (criteria.getGeneratorExtensionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGeneratorExtensionId(),
                            root -> root.join(Generator_.generatorExtension, JoinType.LEFT).get(GeneratorExtension_.id)
                        )
                    );
            }
            if (criteria.getGenTagId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGenTagId(), root -> root.join(Generator_.genTag, JoinType.LEFT).get(GenTag_.id))
                    );
            }
            if (criteria.getGenCostId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGenCostId(), root -> root.join(Generator_.genCost, JoinType.LEFT).get(GenCost_.id))
                    );
            }
            if (criteria.getNetworkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNetworkId(), root -> root.join(Generator_.network, JoinType.LEFT).get(Network_.id))
                    );
            }
        }
        return specification;
    }
}
