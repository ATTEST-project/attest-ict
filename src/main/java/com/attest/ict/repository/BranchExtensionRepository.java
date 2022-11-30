package com.attest.ict.repository;

import com.attest.ict.domain.BranchExtension;
import java.util.List;
import javax.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BranchExtension entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BranchExtensionRepository extends JpaRepository<BranchExtension, Long>, JpaSpecificationExecutor<BranchExtension> {
    @Query(
        value = "select be.* from branch_extension be join branch b on b.id = be.branch_id where b.network_id=:networkId",
        nativeQuery = true
    )
    List<BranchExtension> getBranchExtensionsByNetworkId(@Param("networkId") Long networkId);

    @Query(
        value = " SELECT be.* " +
        " FROM branch_extension be JOIN branch b on b.id = be.branch_id " +
        " WHERE b.network_id=:networkId " +
        " AND be.length >:lengthMin order by b.id",
        nativeQuery = true
    )
    List<BranchExtension> findByNetworkIdAndLengthGreatherThanOrderbyBranchId(
        @Param("networkId") Long networkId,
        @Param("lengthMin") Double lengthMin
    );

    @Query(
        value = " SELECT be.* " +
        " FROM branch_extension be JOIN branch b on b.id = be.branch_id " +
        " WHERE b.network_id=:networkId " +
        " order by b.id",
        nativeQuery = true
    )
    List<BranchExtension> findByNetworkIdOrderByBranchId(@Param("networkId") Long networkId);

    @Query(
        value = " SELECT be.id as branch_extension_id, be.branch_id , b.fbus , b.tbus, be.length " +
        " FROM branch_extension be " +
        " JOIN branch b on b.id = be.branch_id " +
        " WHERE b.network_id=:networkId " +
        " AND be.length >:lengthMin " +
        " ORDER BY b.id ",
        nativeQuery = true
    )
    List<Tuple> findLengthsByNetworkIdAndLengthGreatherThanOrderbyBranchId(
        @Param("networkId") Long networkId,
        @Param("lengthMin") Double lengthMin
    );

    @Query(
        value = "   SELECT be.id as branch_extension_id, be.branch_id , b.fbus , b.tbus, be.length " +
        " FROM branch_extension be " +
        " JOIN branch b on b.id = be.branch_id " +
        " WHERE b.network_id=:networkId " +
        " ORDER BY b.id ",
        nativeQuery = true
    )
    List<Tuple> findLengthsByNetworkId(@Param("networkId") Long networkId);
}
