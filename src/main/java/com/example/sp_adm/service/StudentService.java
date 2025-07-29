package com.example.sp_adm.service;

import com.example.sp_adm.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<Student> fetchAllStudents();
    Student fetchStudentById(Long id);
    Optional<Student> fetchStudentByUsername(String username);
    List<Student> fetchStudentsByBranch(String branch);
    Student saveStudent(Student student);
    void deleteStudent(Long id);
}
