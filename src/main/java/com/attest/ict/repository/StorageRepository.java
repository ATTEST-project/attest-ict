package com.attest.ict.repository;

import com.attest.ict.domain.Storage;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Storage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StorageRepository extends JpaRepository<Storage, Long>, JpaSpecificationExecutor<Storage> {
    // @Query(value = "SELECT * FROM storage s where s.network_id=:network_id", nativeQuery = true)
    //List<Storage> getStoragesByNetworkId(@Param("network_id") String network_id);
    List<Storage> findByNetworkId(@Param("network_id") Long network_id);
}
