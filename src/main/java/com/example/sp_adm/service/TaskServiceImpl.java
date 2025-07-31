package com.example.sp_adm.service;

import com.example.sp_adm.dto.TaskAssignmentRequest;
import com.example.sp_adm.model.Task;
import com.example.sp_adm.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    // Assign a task to a manager (Superadmin action)
    @Transactional
    public void assignTaskToManager(Long superadminId, TaskAssignmentRequest request) {
        Task task = new Task();
        task.setDescription(request.getDescription());
        task.setAssignedToId(request.getAssignedToId());
        task.setAssignedToRole("MANAGER");
        task.setAssignedById(superadminId);
        task.setAssignedByRole("SUPERADMIN");
        task.setStatus("ASSIGNED");
        taskRepository.save(task);
    }

    // Assign a task to an admin (Manager action)
    @Transactional
    public void assignTaskToAdmin(Long managerId, TaskAssignmentRequest request) {
        Task task = new Task();
        task.setDescription(request.getDescription());
        task.setAssignedToId(request.getAssignedToId());
        task.setAssignedToRole("ADMIN");
        task.setAssignedById(managerId);
        task.setAssignedByRole("MANAGER");
        task.setStatus("ASSIGNED");
        taskRepository.save(task);
    }

    // All tasks assigned to a user
    public List<Task> getTasksAssignedTo(Long userId, String role) {
        return taskRepository.findByAssignedToIdAndAssignedToRole(userId, role);
    }

    // All tasks assigned by a user (e.g., all tasks a manager assigned to admins)
    public List<Task> getTasksAssignedBy(Long userId, String role) {
        return taskRepository.findByAssignedByIdAndAssignedByRole(userId, role);
    }
}
