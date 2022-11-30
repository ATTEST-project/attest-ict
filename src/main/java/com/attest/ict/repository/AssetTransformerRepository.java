package com.attest.ict.repository;

import com.attest.ict.domain.AssetTransformer;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AssetTransformer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssetTransformerRepository extends JpaRepository<AssetTransformer, Long>, JpaSpecificationExecutor<AssetTransformer> {
    /*  @Query(value = "select * from asset_transformer at where at.network_id=:networkId", nativeQuery = true)
     List<AssetTransformer> findByNetworkId(@Param("networkId") String networkId);

     @Query(value = "select * from asset_transformer at where at.network_id=:networkId and at.busNum=:busNum", nativeQuery = true)
     AssetTransformer findByNetworkIdAndBusNum(@Param("networkId") String networkId, @Param("busNum") long busNum);
     */

    Optional<AssetTransformer> findByNetworkId(String networkId);
    AssetTransformer findByNetworkIdAndBusNum(String networkId, Long busNum);
}
