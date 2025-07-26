package com.example.sp_adm.repository;

import com.example.sp_adm.model.Superadmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuperadminRepository extends JpaRepository<Superadmin, Long> {
    Optional<Superadmin> findByUsername(String username);
}
