package com.example.sp_adm.service;

import com.example.sp_adm.dto.TaskAssignmentRequest;
import com.example.sp_adm.model.Task;
import java.util.List;

public interface TaskService {
    void assignTaskToManager(Long superadminId, TaskAssignmentRequest request); // Role "SUPERADMIN"
    void assignTaskToAdmin(Long managerId, TaskAssignmentRequest request);      // Role "MANAGER"
    List<Task> getTasksAssignedTo(Long userId, String role); // fetch tasks assigned to this user
    List<Task> getTasksAssignedBy(Long userId, String role); // fetch tasks assigned by this user
    
}
