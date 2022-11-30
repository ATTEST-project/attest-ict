package com.attest.ict.repository;

import com.attest.ict.domain.BranchProfile;
import com.attest.ict.domain.FlexProfile;
import com.attest.ict.domain.LoadProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FlexProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlexProfileRepository extends JpaRepository<FlexProfile, Long>, JpaSpecificationExecutor<FlexProfile> {
    List<FlexProfile> findAll();

    List<FlexProfile> findByNetworkId(String networkId);

    List<FlexProfile> findByNetworkIdAndMode(String networkId, Integer mode);

    Optional<FlexProfile> findByNetworkIdAndSeasonAndTypicalDayAndModeAndTimeInterval(
        Long networId,
        String season,
        String typicalDay,
        Integer mode,
        Double timeInterval
    );

    Optional<FlexProfile> findByInputFileId(Long inputFileId);
}
