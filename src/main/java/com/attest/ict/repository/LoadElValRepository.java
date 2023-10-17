package com.attest.ict.repository;

import com.attest.ict.custom.query.LoadCustomQuery;
import com.attest.ict.domain.LoadElVal;
import java.util.List;
import javax.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LoadElVal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoadElValRepository extends JpaRepository<LoadElVal, Long>, JpaSpecificationExecutor<LoadElVal> {
    List<LoadElVal> findAll();
    /*
    @Query(value = "select lev.* from load_el_val lev join bus b on b.bus_id = lev.bus_id where b.network_id=:networkId", nativeQuery = true)
    List<LoadElVal> getLoadElValsByNetworkId(@Param("networkId") Long networkId);

    @Query(value = "select lev.* from load_el_val lev join bus b on b.bus_id = lev.bus_id where b.network_id=:networkId and lev.type='P'", nativeQuery = true)
    List<LoadElVal> getLoadElValsPByNetworkId(@Param("networkId") Long networkId);

    @Query(value = "select lev.* from load_el_val lev join bus b on b.bus_id = lev.bus_id where b.network_id=:networkId and lev.type='Q'", nativeQuery = true)
    List<LoadElVal> getLoadElValsQByNetworkId(@Param("networkId") Long networkId);
 */
    //Find all profile linked to one networkId
    List<LoadElVal> findByLoadProfileNetworkId(@Param("networkId") Long networkId);

    @Query(value = LoadCustomQuery.LOAD_P_Q_GROUP_BY_SEASON, nativeQuery = true)
    List<Tuple> findLoadPQGroupBySeason(@Param("networkId") Long networkId, @Param("busId") Long busId);

    @Query(value = LoadCustomQuery.LOAD_P_Q_GROUP_BY_SEASON_FOR_TYPICAL_DAY, nativeQuery = true)
    List<Tuple> findLoadPQGroupBySeasonForTypicalDay(
        @Param("networkId") Long networkId,
        @Param("busId") Long busId,
        @Param("typicalDay") String typicalDay
    );

    @Query(value = LoadCustomQuery.LOAD_P_Q_GROUP_BY_TYPICAL_DAY, nativeQuery = true)
    List<Tuple> findLoadPQGroupByTypicalDay(@Param("networkId") Long networkId, @Param("busId") Long busId);

    @Query(value = LoadCustomQuery.LOAD_P_Q_GROUP_BY_TYPICAL_DAY_FOR_SEASON, nativeQuery = true)
    List<Tuple> findLoadPQGroupByTypicalDayForSeason(
        @Param("networkId") Long networkId,
        @Param("busId") Long busId,
        @Param("season") String season
    );
}
