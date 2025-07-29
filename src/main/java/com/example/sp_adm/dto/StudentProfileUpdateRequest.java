package com.example.sp_adm.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class StudentProfileUpdateRequest {
    // Personal
    private String fullName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String gender;
    //private String email;
    private String mobileNumber;
    private String address;
    private String city;
    private String state;
    private String pinCode;

    // Educational
    //private String branch;
    private String degree;
    private Integer yearOfStudy;
    private Integer currentSemester;
    private Double cgpa;
    private Integer admissionYear;

    // Professional
    private String internshipCompany;
    private String internshipDuration;
    private String skills;
    private String projects;
}
