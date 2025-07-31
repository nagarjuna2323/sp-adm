package com.example.sp_adm.repository;

import com.example.sp_adm.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Optional<Manager> findByUsername(String username);
    Optional<Manager> findByEmail(String email);
}
