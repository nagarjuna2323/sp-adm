package com.example.sp_adm.service;

import com.example.sp_adm.model.Student;
import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    Student getStudentById(Long id);
}
