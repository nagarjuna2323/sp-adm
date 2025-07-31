package com.example.sp_adm.controller;

import com.example.sp_adm.dto.TaskAssignmentRequest;
import com.example.sp_adm.model.Task;
import com.example.sp_adm.service.ManagerService;
import com.example.sp_adm.service.TaskService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/manager/api")
@RequiredArgsConstructor
public class ManagerController {

    private final TaskService taskService;
    private final ManagerService managerService;

    // Manager assigns task to admin
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/tasks/assign-to-admin")
    public ResponseEntity<?> assignTaskToAdmin(@RequestBody TaskAssignmentRequest request, Principal principal) {
        String username = principal.getName();
        Long managerId = managerService.getManagerByUsername(username).getId();

        taskService.assignTaskToAdmin(managerId, request);
        return ResponseEntity.ok("Task assigned to admin");
    }

    // // Get tasks assigned to this manager
    // @PreAuthorize("hasRole('MANAGER')")
    // @GetMapping("/tasks")
    // public ResponseEntity<List<Task>> getTasksAssignedToManager(Principal principal) {
    //     String username = principal.getName();
    //     Long managerId = managerService.getManagerByUsername(username).getId();

    //     List<Task> tasks = taskService.getTasksAssignedTo(managerId, "MANAGER");
    //     return ResponseEntity.ok(tasks);
    // }

    // Additional endpoints can be added here...

}
