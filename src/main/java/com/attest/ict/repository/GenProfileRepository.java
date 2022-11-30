package com.attest.ict.repository;

import com.attest.ict.domain.GenProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GenProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GenProfileRepository extends JpaRepository<GenProfile, Long>, JpaSpecificationExecutor<GenProfile> {
    // @Query(value = "select * from gen_profile gp where gp.network_id=:networkId", nativeQuery = true)

    List<GenProfile> findByNetworkId(String networkId);

    Optional<GenProfile> findByNetworkIdAndSeasonAndTypicalDayAndModeAndTimeInterval(
        Long id,
        String season,
        String typicalDay,
        Integer mode,
        Double timeInterval
    );

    Optional<GenProfile> findByNetworkIdAndInputFileId(Long inputFileId, Long networkId);

    Optional<GenProfile> findByInputFileId(Long inputFileId);
}
