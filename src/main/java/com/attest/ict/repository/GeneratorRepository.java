package com.attest.ict.repository;

import com.attest.ict.domain.Generator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Generator entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeneratorRepository extends JpaRepository<Generator, Long>, JpaSpecificationExecutor<Generator> {
    @Query(
        value = "SELECT r.id,apf, bus,m_base, p_max,p_min,pc1,pc2,pg,qc1max,qc1min,qc2max,qc2min,qg,qmax,qmin,ramp_10,ramp_30, ramp_agc, ramp_q, status, vg, network_id FROM generator r INNER JOIN network ON (network.datetime>:startDate AND network.datetime<:endDate)",
        nativeQuery = true
    )
    List<Generator> findGeneratorByNetworkTime(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // @Query(value="SELECT g.* FROM generator g join network n on g.network_id=n.network_id and n.network_id=:networkId", nativeQuery = true)
    List<Generator> findByNetworkIdOrderByIdAsc(@Param("networkId") Long networkId);

    /* @Query(value = "SELECT g.* FROM generator g where g.network_id=:network_id", nativeQuery = true)
    List<Generator> getGeneratorsByNetworkId(@Param("network_id") Long network_id);*/

    List<Generator> findByNetworkName(@Param("networkName") String networkName);

    List<Generator> findByBusNumAndNetworkName(@Param("busNum") Long busNum, @Param("networkName") String networkName);

    List<Generator> findByBusNumAndNetworkIdOrderByIdAsc(@Param("busNum") Long busNum, @Param("netwotkId") Long networkId);

    Optional<Generator> findByIdAndNetworkId(@Param("genId") Long genId, @Param("netwotkId") Long networkId);

    List<Generator> findByNetworkIdAndPg(@Param("networkId") Long networkId, @Param("gg") Double pg);
}
