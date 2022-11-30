package com.attest.ict.repository;

import com.attest.ict.domain.TopologyBus;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TopologyBus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TopologyBusRepository extends JpaRepository<TopologyBus, Long>, JpaSpecificationExecutor<TopologyBus> {
    @Query(value = "select tb.power_line_branch\n" + "from topology_buses tb", nativeQuery = true)
    List<String> getPLBNames();

    @Query(value = "select tb.*\n" + "from topology_buses tb\n" + "where tb.power_line_branch=:plb", nativeQuery = true)
    TopologyBus findTopologyBusesByPLB(String plb);

    @Query(value = "select tb.power_line_branch from topology_buses tb where tb.network_id=:networkId", nativeQuery = true)
    List<String> getPLBNamesByNetworkId(@Param("networkId") String networkId);
}
