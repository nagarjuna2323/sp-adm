package com.example.sp_adm.service;

import com.example.sp_adm.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SuperadminServiceImpl implements SuperadminService {

    private final StudentService studentService;

    @Override
    public List<Student> fetchAllStudents() {
        return studentService.fetchAllStudents();
    }

    @Override
    public Student fetchStudentById(Long id) {
        return studentService.fetchStudentById(id);
    }

    @Override
    public Optional<Student> fetchStudentByUsername(String username) {
        return studentService.fetchStudentByUsername(username);
    }

    @Override
    public List<Student> fetchStudentsByBranch(String branch) {
        return studentService.fetchStudentsByBranch(branch);
    }
}
