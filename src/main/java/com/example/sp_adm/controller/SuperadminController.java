package com.example.sp_adm.controller;

import com.example.sp_adm.model.Student;
import com.example.sp_adm.model.Superadmin;
import com.example.sp_adm.service.AuthService;
import com.example.sp_adm.service.SuperadminService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.sp_adm.dto.RegisterRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import java.lang.IllegalArgumentException;
import com.example.sp_adm.dto.TaskAssignmentRequest;
import com.example.sp_adm.service.TaskService;
import java.security.Principal;
import com.example.sp_adm.service.ManagerService;



import java.util.List;

@RestController
@RequestMapping("/superadmin/api")
@RequiredArgsConstructor
public class SuperadminController {

    private final AuthService authService;
    private final SuperadminService superadminService;
    private final TaskService taskService;
    private final ManagerService managerService;

    // Get own superadmin user details
    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/user-details")
    public ResponseEntity<?> getUserDetails(HttpServletRequest request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String username = authentication.getName();

        try {
            Superadmin user = authService.getSuperadminByUsername(username);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Superadmin not found");
        }
    }

    // Get all students
    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(superadminService.fetchAllStudents());
    }

    // Get student by ID
    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(superadminService.fetchStudentById(id));
    }

    // Get student by username
    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/students/username/{username}")
    public ResponseEntity<Student> getStudentByUsername(@PathVariable String username) {
        return superadminService.fetchStudentByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    } 

    // Get students by branch
    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/students/branch/{branch}")
    public ResponseEntity<List<Student>> getStudentsByBranch(@PathVariable String branch) {
        return ResponseEntity.ok(superadminService.fetchStudentsByBranch(branch));
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody RegisterRequest request) {
        // Optionally add a "role" field to RegisterRequest for type
        authService.createUser(request); // Implement logic for admin/student/superadmin
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @DeleteMapping("/users/{role}/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String role, @PathVariable Long id) {
        try {
            authService.deleteUserByIdAndRole(role, id);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }

        @PreAuthorize("hasRole('SUPERADMIN')")
        @PostMapping("/tasks/assign-to-manager")
        public ResponseEntity<?> assignTaskToManager(@RequestBody TaskAssignmentRequest request, Principal principal) {
            String username = principal.getName();
            Long superadminId = authService.getSuperadminByUsername(username).getId();
            taskService.assignTaskToManager(superadminId, request);
                return ResponseEntity.ok("Task assigned to manager");
}

}

