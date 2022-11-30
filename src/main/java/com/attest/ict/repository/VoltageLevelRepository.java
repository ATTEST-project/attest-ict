package com.attest.ict.repository;

import com.attest.ict.domain.VoltageLevel;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the VoltageLevel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoltageLevelRepository extends JpaRepository<VoltageLevel, Long>, JpaSpecificationExecutor<VoltageLevel> {
    //@Query(value = "select * from voltage_level vl where vl.network_id=:networkId", nativeQuery = true)
    VoltageLevel findByNetworkId(@Param("networkId") Long networkId);
}
