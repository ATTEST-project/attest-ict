package com.attest.ict.repository;

import com.attest.ict.domain.SolarData;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SolarData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SolarDataRepository extends JpaRepository<SolarData, Long>, JpaSpecificationExecutor<SolarData> {}
