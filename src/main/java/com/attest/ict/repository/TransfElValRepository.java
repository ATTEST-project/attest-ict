package com.attest.ict.repository;

import com.attest.ict.domain.LoadElVal;
import com.attest.ict.domain.TransfElVal;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TransfElVal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransfElValRepository extends JpaRepository<TransfElVal, Long>, JpaSpecificationExecutor<TransfElVal> {
    List<TransfElVal> findByTransfProfileId(Long transfProfileId);
}
