package com.attest.ict.repository;

import com.attest.ict.domain.Task;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Task entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    @Query("select task from Task task where task.user.login = ?#{principal.preferredUsername}")
    List<Task> findByUserIsCurrentUser();

    @Query(value = "SELECT * FROM tasks t where t.id=:taskId", nativeQuery = true)
    Task findTaskById(@Param("taskId") long taskId);

    @Query(value = "SELECT * FROM tasks t where t.user_id=:userId", nativeQuery = true)
    List<Task> findTaskByUserId(@Param("userId") String userId);
}
