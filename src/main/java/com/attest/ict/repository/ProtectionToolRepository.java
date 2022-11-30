package com.attest.ict.repository;

import com.attest.ict.domain.ProtectionTool;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProtectionTool entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProtectionToolRepository extends JpaRepository<ProtectionTool, Long>, JpaSpecificationExecutor<ProtectionTool> {}
