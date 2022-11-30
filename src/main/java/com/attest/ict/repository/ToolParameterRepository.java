package com.attest.ict.repository;

import com.attest.ict.domain.ToolParameter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ToolParameter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ToolParameterRepository extends JpaRepository<ToolParameter, Long>, JpaSpecificationExecutor<ToolParameter> {}
