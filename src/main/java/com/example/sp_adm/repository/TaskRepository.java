package com.example.sp_adm.repository;

import com.example.sp_adm.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedToIdAndAssignedToRole(Long assignedToId, String assignedToRole);
    List<Task> findByAssignedByIdAndAssignedByRole(Long assignedById, String assignedByRole);
    // Add more custom queries as needed
}
