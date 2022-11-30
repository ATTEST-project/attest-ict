package com.attest.ict.repository;

import com.attest.ict.domain.Bus;
import com.attest.ict.domain.Generator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Bus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusRepository extends JpaRepository<Bus, Long>, JpaSpecificationExecutor<Bus> {
    @Query(
        value = "SELECT r.* FROM bus r INNER JOIN network ON (network.network_date>:startDate AND network.network_date<:endDate)",
        nativeQuery = true
    )
    List<Bus> findBusByNetworkTime(Date startDate, Date endDate);

    //@Query(value="SELECT b.* FROM bus b join network n on b.network_id=n.network_id and n.network_id=:networkId", nativeQuery = true)
    List<Bus> findByNetworkId(Long networkId);

    @Query(value = "SELECT * FROM bus b where b.bus_num=:busNum and b.network_id=:networkId", nativeQuery = true)
    Bus findByBusNumAndNetworkId(@Param("busNum") Long busNum, @Param("networkId") Long networkId);

    @Query(value = "SELECT * FROM bus b where b.network_id=:network_id", nativeQuery = true)
    List<Bus> getBusesByNetworkId(@Param("network_id") Long network_id);

    @Query(value = "select b.* from bus b order by b.bus_id desc limit 1", nativeQuery = true)
    Bus findLastBus();

    @Query(
        value = "SELECT * FROM bus b where  b.network_id=:network_id and ( b.active_power <> :minP OR b.reactive_power <> :minQ ) ",
        nativeQuery = true
    )
    List<Bus> findByNetworkIdAndNotActivePowerReactivePower(
        @Param("network_id") Long network_id,
        @Param("minP") double minP,
        @Param("minQ") double minQ
    );

    Optional<Bus> findByBusNumAndNetworkName(Long busNum, String networkName);
}
