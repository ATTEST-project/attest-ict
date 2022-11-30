package com.attest.ict.repository;

import com.attest.ict.domain.StorageCost;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StorageCost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StorageCostRepository extends JpaRepository<StorageCost, Long>, JpaSpecificationExecutor<StorageCost> {}
