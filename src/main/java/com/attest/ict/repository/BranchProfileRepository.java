package com.attest.ict.repository;

import com.attest.ict.domain.BranchProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BranchProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BranchProfileRepository extends JpaRepository<BranchProfile, Long>, JpaSpecificationExecutor<BranchProfile> {
    Optional<BranchProfile> findByNetworkIdAndInputFileId(Long networkId, Long inputFileId);

    Optional<BranchProfile> findByInputFileId(Long inputFileId);
}
