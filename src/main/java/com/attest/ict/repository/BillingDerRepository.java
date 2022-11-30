package com.attest.ict.repository;

import com.attest.ict.domain.BillingDer;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BillingDer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillingDerRepository extends JpaRepository<BillingDer, Long>, JpaSpecificationExecutor<BillingDer> {
    Optional<BillingDer> findByNetworkId(String networkId);

    BillingDer findByNetworkIdAndBusNum(String networkId, Long busNum);
}
