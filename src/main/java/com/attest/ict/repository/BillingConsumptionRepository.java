package com.attest.ict.repository;

import com.attest.ict.domain.BillingConsumption;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BillingConsumption entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillingConsumptionRepository
    extends JpaRepository<BillingConsumption, Long>, JpaSpecificationExecutor<BillingConsumption> {
    Optional<BillingConsumption> findByNetworkId(String networkId);

    BillingConsumption findByNetworkIdAndBusNum(String networkId, Long busNum);
}
