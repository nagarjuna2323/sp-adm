package com.example.sp_adm.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    // Assigned to manager/admin
    private Long assignedToId;
    private String assignedToRole; // "MANAGER" or "ADMIN"
    
    private Long assignedById;     // Who assigned it (superadmin/manager)

    private String status; // e.g., "ASSIGNED", "IN_PROGRESS", "DONE"

    private String assignedByRole;

}
