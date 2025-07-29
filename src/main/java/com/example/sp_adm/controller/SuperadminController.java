// package com.example.sp_adm.controller;

// import com.example.sp_adm.model.Superadmin;
// import com.example.sp_adm.service.SuperadminService;
// import com.example.sp_adm.model.Student;
// import com.example.sp_adm.service.AuthService;
// import com.example.sp_adm.service.StudentService;
// import jakarta.servlet.http.HttpServletRequest;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.security.access.prepost.PreAuthorize;

// import java.util.List;

// @RestController
// @RequestMapping("/superadmin/api")
// public class SuperadminController {

//     @Autowired
//     private AuthService authService;

//      @Autowired
//     private SuperadminService superadminService;

//     // Get own details (get ID from JWT token)
//     @GetMapping("/user-details")
//     public ResponseEntity<?> getUserDetails(HttpServletRequest request) {
//         // Extract user id and role from SecurityContext
//         var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

//         if (authentication == null || !authentication.isAuthenticated()) {
//             return ResponseEntity.status(401).body("Unauthorized");
//         }

//         String username = authentication.getName();

//         // We assume only superadmin role can access here
//         var authorities = authentication.getAuthorities();
//         boolean isSuperadmin = authorities.stream()
//                 .anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_SUPERADMIN"));
//         if (!isSuperadmin) {
//             return ResponseEntity.status(403).body("Access denied");
//         }

//         try {
//             // find by username
//             Superadmin user = authService.getSuperadminByUsername(username);
//             return ResponseEntity.ok(user);
//         } catch (Exception e) {
//             return ResponseEntity.status(404).body("Superadmin not found");
//         }
//     }

//      @PreAuthorize("hasRole('SUPERADMIN')")
//     @GetMapping("/students")
//     public ResponseEntity<List<Student>> getAllStudents() {
//         return ResponseEntity.ok(superadminService.fetchAllStudents());
//     }

//      @PreAuthorize("hasRole('SUPERADMIN')")
//     @GetMapping("/students/{id}")
//     public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
//         return ResponseEntity.ok(superadminService.fetchStudentById(id));
//     }

//      @PreAuthorize("hasRole('SUPERADMIN')")
//     @GetMapping("/students/username/{username}")
//     public ResponseEntity<Student> getStudentByUsername(@PathVariable String username) {
//         return ResponseEntity.ok(superadminService.fetchStudentByUsername(username));
//     }

//      @PreAuthorize("hasRole('SUPERADMIN')")
//     @GetMapping("/students/branch/{branch}")
//     public ResponseEntity<List<Student>> getStudentsByBranch(@PathVariable String branch) {
//         return ResponseEntity.ok(superadminService.fetchStudentsByBranch(branch));
//     }
// }

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

import java.util.List;

@RestController
@RequestMapping("/superadmin/api")
@RequiredArgsConstructor
public class SuperadminController {

    private final AuthService authService;
    private final SuperadminService superadminService;

    // Get own superadmin user details
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
    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(superadminService.fetchAllStudents());
    }

    // Get student by ID
    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(superadminService.fetchStudentById(id));
    }

    // Get student by username
    @GetMapping("/students/username/{username}")
    public ResponseEntity<Student> getStudentByUsername(@PathVariable String username) {
        return superadminService.fetchStudentByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get students by branch
    @GetMapping("/students/branch/{branch}")
    public ResponseEntity<List<Student>> getStudentsByBranch(@PathVariable String branch) {
        return ResponseEntity.ok(superadminService.fetchStudentsByBranch(branch));
    }
}
