package com.attest.ict.repository;

import com.attest.ict.domain.GenCost;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GenCost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GenCostRepository extends JpaRepository<GenCost, Long>, JpaSpecificationExecutor<GenCost> {
    @Query(
        value = "select gc.* from gen_cost gc join generator g on g.id = gc.generator_id where g.network_id=:networkId",
        nativeQuery = true
    )
    List<GenCost> getGenCostsByNetworkId(@Param("networkId") Long networkId);
}
