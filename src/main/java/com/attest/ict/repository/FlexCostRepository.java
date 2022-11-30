package com.attest.ict.repository;

import com.attest.ict.domain.FlexCost;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FlexCost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlexCostRepository extends JpaRepository<FlexCost, Long>, JpaSpecificationExecutor<FlexCost> {
    @Query(
        value = "select fc.* from flex_cost fc join flex_profile fp on fp.flex_profile_id = fc.flex_profile_id where fc.network_id=:networkId",
        nativeQuery = true
    )
    Optional<FlexCost> findByNetworkId(@Param("networkId") String networkId);
}
