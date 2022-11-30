package com.attest.ict.repository;

import com.attest.ict.domain.Topology;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Topology entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TopologyRepository extends JpaRepository<Topology, Long>, JpaSpecificationExecutor<Topology> {
    @Query(
        value = "select t.p1, t.p2\n" +
        "from topology t join topology_buses tb on t.power_line_branch_parent = tb.power_line_branch\n" +
        "where tb.power_line_branch=:plb",
        nativeQuery = true
    )
    List<String> getPointsOfPLB(String plb);

    @Query(value = "select t.power_line_branch_parent, t.p1, t.p2\n" + "from topology t", nativeQuery = true)
    List<String> getAllPoints();

    @Query(value = "select t.power_line_branch_parent\n" + "from topology t", nativeQuery = true)
    List<String> getPLBNames();
}
