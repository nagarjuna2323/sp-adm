package com.example.sp_adm.service;

import com.example.sp_adm.model.Manager;

public interface ManagerService {
    Manager getManagerByUsername(String username);
}
