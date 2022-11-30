package com.attest.ict.repository;

import com.attest.ict.domain.BusCoordinate;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BusCoordinate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusCoordinateRepository extends JpaRepository<BusCoordinate, Long>, JpaSpecificationExecutor<BusCoordinate> {
    @Query(value = "select bc.* from bus_coordinate bc join bus b on b.id = bc.bus_id where b.network_id=:networkId", nativeQuery = true)
    List<BusCoordinate> findByNetworkId(@Param("networkId") Long networkId);
}
