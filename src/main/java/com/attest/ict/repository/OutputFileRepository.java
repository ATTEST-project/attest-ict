package com.attest.ict.repository;

import com.attest.ict.custom.query.ToolResultsCustomQuery;
import com.attest.ict.domain.OutputFile;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import javax.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OutputFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OutputFileRepository extends JpaRepository<OutputFile, Long>, JpaSpecificationExecutor<OutputFile> {
    @Query(
        value = "select f.* from output_file f where " +
        "f.network_id=:networkId and (f.file_name like 'fdr%' or f.file_name like 'v_sc%')",
        nativeQuery = true
    )
    List<OutputFile> getPlotsPNGByNetworkId(@Param("networkId") Long networkId);

    @Query(
        value = "select f.* from output_file f where " +
        "f.network_id=:networkId and f.file_name = concat('fdr',:feeder,'_sc',:scenario,'.png') " +
        "order by f.upload_time desc " +
        "limit 1",
        nativeQuery = true
    )
    OutputFile getFdrPlotOfNetworkByFdrAndSc(
        @Param("networkId") Long networkId,
        @Param("feeder") String feeder,
        @Param("scenario") String scenario
    );

    @Query(
        value = "select f.* from output_file f where " +
        "f.network_id=:networkId and f.file_name = concat('v_sc_',:scenario,'.png') " +
        "order by f.upload_time desc " +
        "limit 1",
        nativeQuery = true
    )
    OutputFile getVPlotOfNetworkBySc(@Param("networkId") Long networkId, @Param("scenario") String scenario);

    Optional<OutputFile> findTopByNetworkIdAndFileNameAndToolNameOrderByUploadTimeDesc(Long networkId, String fileName, String toolName);

    List<OutputFile> findBySimulationId(Long simulationId);

    @Query(value = ToolResultsCustomQuery.TOOL_RESULTS, nativeQuery = true)
    List<Tuple> findToolResults(
        @Param("networkId") Long networkId,
        @Param("toolId") Long toolId,
        @Param("fileName") String fileName,
        @Param("dateTimeEnd") Instant dateTimeEnd
    );
}
