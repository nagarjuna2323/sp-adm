package com.example.sp_adm.controller;

import com.example.sp_adm.model.Superadmin;
import com.example.sp_adm.service.SuperadminService;
import com.example.sp_adm.model.Student;
import com.example.sp_adm.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/superadmin/api")
public class SuperadminController {

    @Autowired
    private AuthService authService;

     @Autowired
    private SuperadminService superadminService;

    // Get own details (get ID from JWT token)
    @GetMapping("/user-details")
    public ResponseEntity<?> getUserDetails(HttpServletRequest request) {
        // Extract user id and role from SecurityContext
        var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String username = authentication.getName();

        // We assume only superadmin role can access here
        var authorities = authentication.getAuthorities();
        boolean isSuperadmin = authorities.stream()
                .anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_SUPERADMIN"));
        if (!isSuperadmin) {
            return ResponseEntity.status(403).body("Access denied");
        }

        try {
            // find by username
            Superadmin user = authService.getSuperadminByUsername(username);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Superadmin not found");
        }
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(superadminService.fetchAllStudents());
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(superadminService.fetchStudentById(id));
    }
}
