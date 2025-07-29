package com.example.sp_adm.repository;

import com.example.sp_adm.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUsername(String username);
        List<Student> findByBranchIgnoreCase(String branch);

}
