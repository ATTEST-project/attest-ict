package com.attest.ict.repository;

import com.attest.ict.domain.GenProfile;
import com.attest.ict.domain.TransfProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TransfProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransfProfileRepository extends JpaRepository<TransfProfile, Long>, JpaSpecificationExecutor<TransfProfile> {
    Optional<TransfProfile> findByNetworkIdAndInputFileId(Long inputFileId, Long networkId);

    Optional<TransfProfile> findByInputFileId(Long inputFileId);
}
