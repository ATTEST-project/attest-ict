package com.attest.ict.repository;

import com.attest.ict.custom.model.projection.NetworkProjection;
import com.attest.ict.domain.InputFile;
import com.attest.ict.domain.Network;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data SQL repository for the Network entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NetworkRepository extends JpaRepository<Network, Long>, JpaSpecificationExecutor<Network> {
    Optional<Network> findByName(@Param("networkName") String networkName);

    //Network findNetworkByName(@Param("networkName") String networkName);

    Optional<Network> findById(@Param("networkId") String networkId);

    //Network findNetworkByNetworkId( String networkId);

    @Modifying
    @Query(value = "DELETE FROM network WHERE network_id=:networkId", nativeQuery = true)
    @Transactional(rollbackFor = Exception.class)
    void deleteNetworksById(@Param("networkId") Long networkId);

    @Modifying
    @Query(value = "DELETE FROM generator WHERE network_id=:networkId", nativeQuery = true)
    @Transactional(rollbackFor = Exception.class)
    void deleteGeneratorsByNetworkId(@Param("networkId") Long networkId);

    @Modifying
    @Query(value = "DELETE FROM branch WHERE network_id=:networkId", nativeQuery = true)
    @Transactional(rollbackFor = Exception.class)
    void deleteBranchesByNetworkId(@Param("networkId") Long networkId);

    @Modifying
    @Query(value = "DELETE FROM bus WHERE network_id=:networkId", nativeQuery = true)
    @Transactional(rollbackFor = Exception.class)
    void deleteBusesByNetworkId(@Param("networkId") Long networkId);

    @Query(
        value = "SELECT * FROM network r WHERE (name=:networkName AND network_date>:startDate AND network_date<:endDate)",
        nativeQuery = true
    )
    List<Network> findNetworksByName(
        @Param("networkName") String networkName,
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate
    );

    @Query(
        value = "SELECT network_id FROM network r WHERE (name=:networkName AND network_date>:startDate AND network_date<:endDate)",
        nativeQuery = true
    )
    List<Long> findNetworkId(@Param("networkName") String networkName, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "SELECT network_id FROM network r", nativeQuery = true)
    List<Long> findNetworkIds();

    @Query(value = "SELECT network_id FROM network r WHERE name=:networkName", nativeQuery = true)
    Long findNetworkIdByName(@Param("networkName") String networkName);

    @Query(value = "SELECT network_id, network_date, name FROM network r", nativeQuery = true)
    List<Object> findAllNetworksPlain();

    @Query(value = "select n.name as name from Network n")
    List<String> getNetworksNames();

    // only necessary fields using projection
    List<NetworkProjection> getNetworksBy();

    void save(Optional<? extends Network> network);

    @Modifying
    @Query(value = "DELETE FROM topology_bus WHERE network_id=:networkId", nativeQuery = true)
    @Transactional(rollbackFor = Exception.class)
    void deleteTopologyBusesByNetworkId(@Param("networkId") Long networkId);

    @Modifying
    @Query(
        value = "delete t " +
        "from topology t join topology_bus tb on t.power_line_branch_parent = tb.power_line_branch " +
        "where tb.network_id=:networkId",
        nativeQuery = true
    )
    @Transactional
    void deleteTopologyByNetworkId(@Param("networkId") Long networkId);

    @Modifying
    @Query(
        value = "delete bn " + "from bus_names bn join bus b on bn.bus_id=b.bus_id " + "where b.network_id=:networkId",
        nativeQuery = true
    )
    @Transactional
    void deleteBusNamesByNetworkId(@Param("networkId") Long networkId);

    @Modifying
    @Query(
        value = "delete bc " + "from bus_coordinates bc join bus b on bc.bus_id=b.bus_id " + "where b.network_id=:networkId",
        nativeQuery = true
    )
    @Transactional
    void deleteBusCoordinatesByNetworkId(@Param("networkId") Long networkId);

    @Modifying
    @Query(
        value = "delete gt " + "from gen_tags gt join generator g on gt.gen_id=g.id " + "where g.network_id=:networkId",
        nativeQuery = true
    )
    @Transactional(rollbackFor = Exception.class)
    void deleteGenTagsByNetworkId(@Param("networkId") Long networkId);

    @Modifying
    @Query(
        value = "delete gc " + "from gen_cost gc join generator g on gc.gen_id=g.id " + "where g.network_id=:networkId",
        nativeQuery = true
    )
    @Transactional(rollbackFor = Exception.class)
    void deleteGenCostsByNetworkId(@Param("networkId") Long networkId);

    @Modifying
    @Query(value = "DELETE FROM base_mva bm WHERE bm.network_id=:networkId", nativeQuery = true)
    @Transactional(rollbackFor = Exception.class)
    void deleteBaseMVAByNetworkId(@Param("networkId") Long networkId);

    @Modifying
    @Query(value = "DELETE FROM input_files f WHERE f.network_id=:networkId", nativeQuery = true)
    @Transactional(rollbackFor = Exception.class)
    void deleteFilesByNetworkId(@Param("networkId") Long networkId);

    @Modifying
    @Query(value = "DELETE FROM network_display_files f WHERE f.network_id=:networkId", nativeQuery = true)
    @Transactional(rollbackFor = Exception.class)
    void deleteNDFilesByNetworkId(@Param("networkId") Long networkId);

    @Modifying
    @Query(value = "DELETE FROM output_files f WHERE f.network_id=:networkId", nativeQuery = true)
    @Transactional(rollbackFor = Exception.class)
    void deleteOutputFilesByNetworkId(@Param("networkId") Long networkId);

    @Modifying
    @Query(value = "DELETE FROM loads l WHERE l.network_id=:networkId", nativeQuery = true)
    @Transactional(rollbackFor = Exception.class)
    void deleteLoadsByNetworkId(@Param("networkId") Long networkId);

    @Modifying
    @Query(value = "DELETE FROM storage s WHERE s.network_id=:networkId", nativeQuery = true)
    @Transactional(rollbackFor = Exception.class)
    void deleteStoragesByNetworkId(@Param("networkId") Long networkId);

    @Modifying
    @Query(value = "DELETE FROM transformer t WHERE t.network_id=:networkId", nativeQuery = true)
    @Transactional(rollbackFor = Exception.class)
    void deleteTransformersByNetworkId(@Param("networkId") Long networkId);

    @Modifying
    @Query(value = "DELETE FROM capacitor_bank_data cbd WHERE cbd.network_id=:networkId", nativeQuery = true)
    @Transactional(rollbackFor = Exception.class)
    void deleteCapacitorsByNetworkId(@Param("networkId") Long networkId);

    @Modifying
    @Query(
        value = "delete lev " + "from load_el_vars lev join bus b on lev.bus_id = b.bus_id " + "where b.network_id=:networkId",
        nativeQuery = true
    )
    @Transactional(rollbackFor = Exception.class)
    void deleteLoadElVarsByNetworkId(@Param("networkId") Long networkId);

    @Query(value = "select n.name from network n", nativeQuery = true)
    List<String> getAllNetworkNames();
}
