// package com.example.sp_adm.service;

// import com.example.sp_adm.model.Student;
// import java.util.List;
// import java.util.Optional;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// @Service
// public class SuperadminServiceImpl implements SuperadminService {

//     private final StudentService studentService;

//     @Autowired
//     public SuperadminServiceImpl(StudentService studentService) {
//         this.studentService = studentService;
//     }

//     @Override
//     public Optional<Student> getStudentById(Long id) {
//         return studentService.getStudentById(id);
//     }

//     @Override
//     public List<Student> getStudentsByBranch(String branch) {
//         return studentService.getStudentsByBranch(branch);
//     }

//     @Override
//     public List<Student> getStudentsByUsername(String username) {
//         return studentService.getStudentsByUsername(username);
//     }

//     @Override
//     public List<Student> filterStudents(String branch, String username, Long id) {
//         return studentService.filterStudents(branch, username, id);
//     }
// }

package com.example.sp_adm.service;

import com.example.sp_adm.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuperadminServiceImpl implements SuperadminService {

    @Autowired
    private StudentService studentService;

    @Override
    public List<Student> fetchAllStudents() {
        return studentService.getAllStudents();
    }

    @Override
    public Student fetchStudentById(Long id) {
        return studentService.getStudentById(id);
    }
}
