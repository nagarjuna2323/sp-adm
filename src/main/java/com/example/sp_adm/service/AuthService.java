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
import com.example.sp_adm.dto.RegisterRequest;
import org.springframework.transaction.annotation.Transactional;
import com.example.sp_adm.model.Manager;
import com.example.sp_adm.repository.ManagerRepository;

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
    private ManagerRepository managerRepo;

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

    public String registerManager(String username, String password, String email, String fullName) {
        if (managerRepo.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists for Manager");
        }
        Manager manager = new Manager();
        manager.setUsername(username);
        manager.setPassword(passwordEncoder.encode(password));
        manager.setEmail(email);
        manager.setFullName(fullName);
        managerRepo.save(manager);
        return "Manager registered successfully!";
    }

    public String registerAdmin(String username, String password, String email, String fullName, Long managerId) {
        if (adminRepo.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists for Admin");
         }
    //     Admin user = new Admin();
    //     user.setUsername(username);
    //     user.setPassword(passwordEncoder.encode(password));
    //     user.setEmail(email);
    //     user.setFullName(fullName);
    //     user.setManagerId(managerId);
    //     adminRepo.save(user);
    //     return "Admin registered successfully!";
    // }
            // Long managerId = request.getManagerId();
        if (managerId == null) {
            throw new RuntimeException("Manager ID is required to create Admin");
        }
        if (!managerRepo.existsById(managerId)) {
            throw new RuntimeException("Manager not found with ID: " + managerId);  
        }
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setEmail(email);
        admin.setFullName(fullName);
        admin.setManagerId(managerId);   // set the manager link!
        adminRepo.save(admin);
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

        // check manager
        Optional<Manager> manager = managerRepo.findByUsername(username);
    if (manager.isPresent() && passwordEncoder.matches(rawPassword, manager.get().getPassword())) {
        String token = jwtUtils.generateJwtToken(manager.get().getId(), username, "manager");
        return new AuthResponse(token, "manager");
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

 @Transactional
    public void createUser(RegisterRequest request) {
        String role = request.getRole();
        if (role == null) {
            throw new IllegalArgumentException("Role must be specified for user creation");
        }

        switch (role.toUpperCase()) {
            case "STUDENT":
                if (studentRepo.findByUsername(request.getUsername()).isPresent()) {
                    throw new RuntimeException("Username already exists for Student");
                }
                Student student = new Student();
                student.setUsername(request.getUsername());
                student.setPassword(passwordEncoder.encode(request.getPassword()));
                student.setEmail(request.getEmail());
                student.setFullName(request.getFullName());
                student.setBranch(request.getBranch());
                student.setDegree(request.getDegree());
                studentRepo.save(student);
                break;

            case "MANAGER":
                if (managerRepo.findByUsername(request.getUsername()).isPresent()) {
                    throw new RuntimeException("Manager username already exists");
                }
                Manager manager = new Manager();
                manager.setUsername(request.getUsername());
                manager.setPassword(passwordEncoder.encode(request.getPassword()));
                manager.setEmail(request.getEmail());
                manager.setFullName(request.getFullName());
                managerRepo.save(manager);
                break;


            case "ADMIN":
                if (adminRepo.findByUsername(request.getUsername()).isPresent()) {
                    throw new RuntimeException("Username already exists for Admin");
                }
                Admin admin = new Admin();
                admin.setUsername(request.getUsername());
                admin.setPassword(passwordEncoder.encode(request.getPassword()));
                admin.setEmail(request.getEmail());
                admin.setFullName(request.getFullName());
                adminRepo.save(admin);
                break;

            case "SUPERADMIN":
                if (superadminRepo.findByUsername(request.getUsername()).isPresent()) {
                    throw new RuntimeException("Username already exists for Superadmin");
                }
                Superadmin superadmin = new Superadmin();
                superadmin.setUsername(request.getUsername());
                superadmin.setPassword(passwordEncoder.encode(request.getPassword()));
                superadmin.setEmail(request.getEmail());
                superadmin.setFullName(request.getFullName());
                superadminRepo.save(superadmin);
                break;

            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

     public void deleteUserByIdAndRole(String role, Long id) {
        switch (role.toUpperCase()) {
            case "STUDENT":
                if (!studentRepo.existsById(id))
                    throw new RuntimeException("Student not found with id: " + id);
                studentRepo.deleteById(id);
                break;

            case "ADMIN":
                if (!adminRepo.existsById(id))
                    throw new RuntimeException("Admin not found with id: " + id);
                adminRepo.deleteById(id);
                break;

            case "SUPERADMIN":
                if (!superadminRepo.existsById(id))
                    throw new RuntimeException("Superadmin not found with id: " + id);
                superadminRepo.deleteById(id);
                break;

            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}
