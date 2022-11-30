package com.attest.ict.repository;

import com.attest.ict.domain.BusExtension;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BusExtension entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusExtensionRepository extends JpaRepository<BusExtension, Long>, JpaSpecificationExecutor<BusExtension> {
    @Query(value = "select be.* from bus_extension be join bus b on b.id = be.bus_id where b.network_id=:networkId", nativeQuery = true)
    List<BusExtension> getBusExtensionsByNetworkId(@Param("networkId") Long networkId);
}
