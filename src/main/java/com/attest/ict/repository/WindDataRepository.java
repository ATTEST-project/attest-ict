package com.attest.ict.repository;

import com.attest.ict.domain.WindData;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WindData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WindDataRepository extends JpaRepository<WindData, Long>, JpaSpecificationExecutor<WindData> {}
