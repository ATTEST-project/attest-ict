package com.attest.ict.repository;

import com.attest.ict.domain.FlexElVal;
import com.attest.ict.domain.LoadElVal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FlexElVal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlexElValRepository extends JpaRepository<FlexElVal, Long>, JpaSpecificationExecutor<FlexElVal> {
    @Query(
        value = "select fev.* from flex_el_val fev join flex_profile fp on fp.flex_profile_id = fev.flex_profile_id where fev.network_id=:networkId",
        nativeQuery = true
    )
    Optional<FlexElVal> findByNetworkId(@Param("networkId") String networkId);

    //Find all profile linked to one networkId
    List<FlexElVal> findByFlexProfileNetworkId(@Param("networkId") Long networkId);

    List<FlexElVal> findByFlexProfileId(Long loadProfileId);
}
