package com.attest.ict.repository;

import com.attest.ict.domain.GeneratorExtension;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GeneratorExtension entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeneratorExtensionRepository
    extends JpaRepository<GeneratorExtension, Long>, JpaSpecificationExecutor<GeneratorExtension> {
    @Query(
        value = "select ge.* from generator_extension ge join generator g on g.id = ge.generator_id where g.network_id=:networkId",
        nativeQuery = true
    )
    List<GeneratorExtension> getGeneratorExtensionsByNetworkId(@Param("networkId") Long networkId);
}
