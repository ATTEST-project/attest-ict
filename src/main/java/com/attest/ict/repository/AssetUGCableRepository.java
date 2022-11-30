package com.attest.ict.repository;

import com.attest.ict.domain.AssetUGCable;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AssetUGCable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssetUGCableRepository extends JpaRepository<AssetUGCable, Long>, JpaSpecificationExecutor<AssetUGCable> {
    /*****  
    @Query(value = "select * from asset_ug_cable au where au.network_id=:networkId", nativeQuery = true)
    List<AssetUGCable> findByNetworkId(@Param("networkId") String networkId);

    @Query(value = "select * from asset_ug_cable au where au.network_id=:networkId and au.sectionLabel=:section", nativeQuery = true)
    AssetUGCable findByNetworkIdAndSectionLabel(@Param("networkId") String networkId, @Param("section") String section);
  ****/

    Optional<AssetUGCable> findByNetworkId(String networkId);

    AssetUGCable findByNetworkIdAndSectionLabel(String networkId, String sectionLabel);
}
