package com.attest.ict.repository;

import com.attest.ict.domain.CapacitorBankData;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CapacitorBankData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CapacitorBankDataRepository extends JpaRepository<CapacitorBankData, Long>, JpaSpecificationExecutor<CapacitorBankData> {
    // @Query(value = "select * from capacitor_bank_data cbd where cbd.network_id=:networkId", nativeQuery = true)
    List<CapacitorBankData> findByNetworkId(@Param("networkId") Long networkId);
}
