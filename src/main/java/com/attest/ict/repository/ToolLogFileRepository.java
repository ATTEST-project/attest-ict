package com.attest.ict.repository;

import com.attest.ict.domain.ToolLogFile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ToolLogFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ToolLogFileRepository extends JpaRepository<ToolLogFile, Long>, JpaSpecificationExecutor<ToolLogFile> {
    //@Query(value = "select tlf.* from tool_log_file tlf where tlf.task_id=:taskId", nativeQuery = true)
    //ToolLogFile getToolLogFileByTaskId(long taskId);
    Optional<ToolLogFile> findByTaskId(@Param("taskId") Long taskId);
}
