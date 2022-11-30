package com.attest.ict.repository;

import com.attest.ict.domain.BusName;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BusName entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusNameRepository extends JpaRepository<BusName, Long>, JpaSpecificationExecutor<BusName> {
    @Query(value = "select bn.* from bus_name bn join bus b on b.id = bn.bus_id where b.network_id=:networkId", nativeQuery = true)
    List<BusName> getBusNamesByNetworkId(@Param("networkId") Long networkId);
}
