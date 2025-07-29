package com.example.sp_adm.controller;  

import com.example.sp_adm.model.Student;
import com.example.sp_adm.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.sp_adm.dto.StudentProfileUpdateRequest;




@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseEntity<List<Student>> fetchAllStudents() {
        return ResponseEntity.ok(studentService.fetchAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> fetchStudentById(@PathVariable Long id) {
        Student student = studentService.fetchStudentById(id);
        return student != null ?
            ResponseEntity.ok(student) :
            ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/profile")
public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody StudentProfileUpdateRequest request) {
    try {
        Student updatedStudent = studentService.updateStudentProfile(id, request);
        return ResponseEntity.ok(updatedStudent);
    } catch (RuntimeException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
}

}
