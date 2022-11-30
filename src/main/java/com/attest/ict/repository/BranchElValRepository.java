package com.attest.ict.repository;

import com.attest.ict.domain.BranchElVal;
import com.attest.ict.domain.LoadElVal;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BranchElVal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BranchElValRepository extends JpaRepository<BranchElVal, Long>, JpaSpecificationExecutor<BranchElVal> {
    List<BranchElVal> findByBranchProfileId(Long branchProfileId);
}
