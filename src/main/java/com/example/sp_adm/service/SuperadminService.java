package com.example.sp_adm.service;

import com.example.sp_adm.model.Student;
import java.util.List;

public interface SuperadminService {
    List<Student> fetchAllStudents();
    Student fetchStudentById(Long id);
}
