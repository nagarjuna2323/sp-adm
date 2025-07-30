package com.example.sp_adm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "students")
@Getter @Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private String branch;

private String fullName; 
private LocalDate dateOfBirth;
private String gender;
private String mobileNumber;
private String address;
private String city;
private String state;
private String pinCode;
private String degree;
private Integer yearOfStudy;
private Integer currentSemester;
private Double cgpa;
private Integer admissionYear;
private String internshipCompany;
private String internshipDuration;
private String skills;
private String projects;

}
