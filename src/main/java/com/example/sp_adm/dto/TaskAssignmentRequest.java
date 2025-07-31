package com.example.sp_adm.dto;

import lombok.Data;

@Data
public class TaskAssignmentRequest {
    private String description;
    private Long assignedToId; // user ID
}
