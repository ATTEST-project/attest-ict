package com.attest.ict.repository;

import com.attest.ict.domain.GenTag;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GenTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GenTagRepository extends JpaRepository<GenTag, Long>, JpaSpecificationExecutor<GenTag> {
    @Query(
        value = "select gt.* from gen_tag gt join generator g on g.id = gt.generator_id where g.network_id=:networkId",
        nativeQuery = true
    )
    List<GenTag> getGenTagsByNetworkId(@Param("networkId") Long networkId);
}
