package com.attest.ict.repository;

import com.attest.ict.domain.Simulation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Simulation entity.
 */
@Repository
public interface SimulationRepository extends JpaRepository<Simulation, Long>, JpaSpecificationExecutor<Simulation> {
    @Query(
        value = "select distinct simulation from Simulation simulation left join fetch simulation.inputFiles",
        countQuery = "select count(distinct simulation) from Simulation simulation"
    )
    Page<Simulation> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct simulation from Simulation simulation left join fetch simulation.inputFiles")
    List<Simulation> findAllWithEagerRelationships();

    @Query("select simulation from Simulation simulation left join fetch simulation.inputFiles where simulation.id =:id")
    Optional<Simulation> findOneWithEagerRelationships(@Param("id") Long id);

    Optional<Simulation> findByUuid(UUID uuid);
}
