package com.attest.ict.repository;

import com.attest.ict.domain.LineCable;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LineCable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LineCableRepository extends JpaRepository<LineCable, Long>, JpaSpecificationExecutor<LineCable> {
    List<LineCable> findAll();
    List<LineCable> findByNetworkId(String networkId);
}
