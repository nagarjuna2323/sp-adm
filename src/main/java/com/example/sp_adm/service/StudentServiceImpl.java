package com.example.sp_adm.service;

import com.example.sp_adm.model.Student;
import com.example.sp_adm.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.sp_adm.dto.StudentProfileUpdateRequest;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public List<Student> fetchAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student fetchStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
    }

    @Override
    public Optional<Student> fetchStudentByUsername(String username) {
        return studentRepository.findByUsername(username);
    }

    @Override
    public List<Student> fetchStudentsByBranch(String branch) {
        return studentRepository.findByBranchIgnoreCase(branch);
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
public Student updateStudentProfile(Long id, StudentProfileUpdateRequest request) {
    Student student = studentRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Student not found with id " + id));

    // Update fields (add null checks if you want partial updates)
    student.setFullName(request.getFullName());
   // student.setDateOfBirth(request.getDateOfBirth());
    student.setGender(request.getGender());
    //student.setEmail(request.getEmail());
    student.setMobileNumber(request.getMobileNumber());
    student.setAddress(request.getAddress());
    student.setCity(request.getCity());
    student.setState(request.getState());
    student.setPinCode(request.getPinCode());

    //student.setBranch(request.getBranch());
    student.setDegree(request.getDegree());
    student.setYearOfStudy(request.getYearOfStudy());
    student.setCurrentSemester(request.getCurrentSemester());
    student.setCgpa(request.getCgpa());
    student.setAdmissionYear(request.getAdmissionYear());

    student.setInternshipCompany(request.getInternshipCompany());
    student.setInternshipDuration(request.getInternshipDuration());
    student.setSkills(request.getSkills());
    student.setProjects(request.getProjects());

    return studentRepository.save(student);
}

}
