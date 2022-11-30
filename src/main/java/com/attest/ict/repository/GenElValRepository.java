package com.attest.ict.repository;

import com.attest.ict.custom.query.GenCustomQuery;
import com.attest.ict.domain.GenElVal;
import java.util.List;
import javax.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GenElVal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GenElValRepository extends JpaRepository<GenElVal, Long>, JpaSpecificationExecutor<GenElVal> {
    List<GenElVal> findByGenProfileNetworkId(@Param("networkId") Long networkId);

    List<GenElVal> findByGenProfileId(Long id);

    @Query(value = GenCustomQuery.GEN_P_Q_GROUP_BY_SEASON, nativeQuery = true)
    List<Tuple> findGenPQGroupBySeason(@Param("networkId") Long networkId, @Param("genId") Long genId, @Param("mode") List mode);

    @Query(value = GenCustomQuery.GEN_P_Q_GROUP_BY_SEASON_FOR_TYPICAL_DAY, nativeQuery = true)
    List<Tuple> findGenPQGroupBySeasonForTypicalDay(
        @Param("networkId") Long networkId,
        @Param("genId") Long genId,
        @Param("typicalDay") String typicalDay,
        @Param("mode") List mode
    );

    @Query(value = GenCustomQuery.GEN_P_Q_GROUP_BY_TYPICAL_DAY, nativeQuery = true)
    List<Tuple> findGenPQGroupByTypicalDay(@Param("networkId") Long networkId, @Param("genId") Long genId, @Param("mode") List mode);

    @Query(value = GenCustomQuery.GEN_P_Q_GROUP_BY_TYPICAL_DAY_FOR_SEASON, nativeQuery = true)
    List<Tuple> findGenPQGroupByTypicalDayForSeason(
        @Param("networkId") Long networkId,
        @Param("genId") Long genId,
        @Param("season") String season,
        @Param("mode") List mode
    );
}
