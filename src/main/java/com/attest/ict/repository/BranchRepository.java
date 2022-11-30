package com.attest.ict.repository;

import com.attest.ict.domain.Branch;
import com.attest.ict.domain.Bus;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Branch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BranchRepository extends JpaRepository<Branch, Long>, JpaSpecificationExecutor<Branch> {
    @Query(
        value = "SELECT branch_id,angle, angmax,angmin, b,fbus,length,r,r0,ratea,rateb,ratec,ratio,status,tbus,x,x0, network_id FROM branch r INNER JOIN network ON (network.datetime>:startDate AND network.datetime<:endDate)",
        nativeQuery = true
    )
    List<Branch> findBranchByNetworkTime(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //@Query(value="SELECT b.* FROM branch b join network n on b.network_id=n.network_id and n.network_id=:networkId", nativeQuery = true)
    List<Branch> findByNetworkId(@Param("networkId") Long networkId);

    @Query(value = "SELECT * FROM branch b where b.branch_id=:branchId", nativeQuery = true)
    Branch findBranchById(@Param("branchId") long branchId);

    //@Query(value = "SELECT * FROM branch b where b.network_id=:network_id", nativeQuery = true)
    //List<Branch> getBranchesByNetworkId(@Param("network_id") Long network_id);

    List<Branch> findByFbusAndTbusAndNetworkIdOrderByIdAsc(
        @Param("fbus") Long fBus,
        @Param("tbus") Long tBus,
        @Param("networkId") Long networkId
    );
}
