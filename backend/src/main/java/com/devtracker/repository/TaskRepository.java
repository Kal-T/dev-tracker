package com.devtracker.repository;

import com.devtracker.entity.Task;
import com.devtracker.entity.TaskStatus;
import com.devtracker.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task> {

    List<Task> findByOwner(User owner);

    List<Task> findByOwnerAndStatus(User owner, TaskStatus status);

    @Query("SELECT t.status, COUNT(t) FROM Task t GROUP BY t.status")
    List<Object[]> countTasksByStatus();

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Task t SET t.status = :status, t.updatedAt = CURRENT_TIMESTAMP WHERE t.id IN :ids")
    int bulkUpdateStatus(@Param("ids") List<UUID> ids, @Param("status") TaskStatus status);

    @Query("SELECT t FROM Task t WHERE t.createdAt < :createdAt ORDER BY t.createdAt DESC")
    List<Task> findTasksBefore(@Param("createdAt") LocalDateTime createdAt, Pageable pageable);
}
