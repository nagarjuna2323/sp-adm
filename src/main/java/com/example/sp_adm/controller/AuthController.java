package com.example.sp_adm.controller;

import com.example.sp_adm.dto.AuthResponse;
import com.example.sp_adm.dto.LoginRequest;
import com.example.sp_adm.dto.RegisterRequest;
import com.example.sp_adm.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register/superadmin")
    public ResponseEntity<?> registerSuperadmin(@RequestBody RegisterRequest request) {
        try {
            String msg = authService.registerSuperadmin(request.getUsername(), request.getPassword(), request.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(msg);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest request) {
        try {
            String msg = authService.registerAdmin(request.getUsername(), request.getPassword(), request.getEmail(), request.getFullName(), request.getManagerId());
            return ResponseEntity.status(HttpStatus.CREATED).body(msg);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@RequestBody RegisterRequest request) {
        try {
            String msg = authService.registerStudent(request.getUsername(), request.getPassword(), request.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(msg);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse resp = authService.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

     @PostMapping("/register/manager")
    public ResponseEntity<?> registerManager(@RequestBody RegisterRequest request) {
        try {
            String result = authService.registerManager(
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                request.getFullName()
            );
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
