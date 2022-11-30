package com.attest.ict.repository;

import com.attest.ict.domain.DsoTsoConnection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DsoTsoConnection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DsoTsoConnectionRepository extends JpaRepository<DsoTsoConnection, Long>, JpaSpecificationExecutor<DsoTsoConnection> {}
