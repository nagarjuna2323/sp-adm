package com.example.sp_adm.service;

import com.example.sp_adm.model.Admin;
import com.example.sp_adm.model.Student;
import com.example.sp_adm.model.Superadmin;
import com.example.sp_adm.repository.AdminRepository;
import com.example.sp_adm.repository.StudentRepository;
import com.example.sp_adm.dto.AuthResponse;
import com.example.sp_adm.repository.SuperadminRepository;
import com.example.sp_adm.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private SuperadminRepository superadminRepo;

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    public String registerSuperadmin(String username, String password, String email) {
        if (superadminRepo.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists for Superadmin");
        }
        Superadmin user = new Superadmin();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        superadminRepo.save(user);
        return "Superadmin registered successfully!";
    }

    public String registerAdmin(String username, String password, String email) {
        if (adminRepo.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists for Admin");
        }
        Admin user = new Admin();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        adminRepo.save(user);
        return "Admin registered successfully!";
    }

    public String registerStudent(String username, String password, String email) {
        if (studentRepo.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists for Student");
        }
        Student user = new Student();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        studentRepo.save(user);
        return "Student registered successfully!";
    }

    public AuthResponse login(String username, String rawPassword) {
        // Check superadmin
        Optional<Superadmin> sa = superadminRepo.findByUsername(username);
        if (sa.isPresent() && passwordEncoder.matches(rawPassword, sa.get().getPassword())) {
            String token = jwtUtils.generateJwtToken(sa.get().getId(), username, "superadmin");
            return new AuthResponse(token, "superadmin");
        }

        // Check admin
        Optional<Admin> admin = adminRepo.findByUsername(username);
        if (admin.isPresent() && passwordEncoder.matches(rawPassword, admin.get().getPassword())) {
            String token = jwtUtils.generateJwtToken(admin.get().getId(), username, "admin");
            return new AuthResponse(token, "admin");
        }

        // Check student
        Optional<Student> student = studentRepo.findByUsername(username);
        if (student.isPresent() && passwordEncoder.matches(rawPassword, student.get().getPassword())) {
            String token = jwtUtils.generateJwtToken(student.get().getId(), username, "student");
            return new AuthResponse(token, "student");
        }

        throw new RuntimeException("Invalid username or password");
    }

    // Fetch superadmin details by id
    public Superadmin getSuperadminById(Long id) {
        return superadminRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Superadmin not found"));
    }

    
    public Superadmin getSuperadminByUsername(String username) {
    return superadminRepo.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Superadmin not found"));
}
}
