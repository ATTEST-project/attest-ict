package com.attest.ict.repository;

import com.attest.ict.domain.LoadProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LoadProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoadProfileRepository extends JpaRepository<LoadProfile, Long>, JpaSpecificationExecutor<LoadProfile> {
    Optional<LoadProfile> findByNetworkIdAndSeasonAndTypicalDayAndModeAndTimeInterval(
        Long networId,
        String season,
        String typicalDay,
        Integer mode,
        Double timeInterval
    );

    Optional<LoadProfile> findByNetworkIdAndInputFileId(Long networId, Long inputFileId);

    Optional<LoadProfile> findByInputFileId(Long inputFileId);
}
