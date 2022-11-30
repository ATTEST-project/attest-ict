package com.attest.ict.repository;

import com.attest.ict.domain.BaseMVA;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BaseMVA entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BaseMVARepository extends JpaRepository<BaseMVA, Long>, JpaSpecificationExecutor<BaseMVA> {
    //@Query(value = "select * from base_mva where network_id=:networkId", nativeQuery = true)
    BaseMVA findByNetworkId(@Param("networkId") Long networkId);
}
