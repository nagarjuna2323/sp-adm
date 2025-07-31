package com.example.sp_adm.service;

import com.example.sp_adm.model.Manager;
import com.example.sp_adm.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {
    private final ManagerRepository managerRepo;

    @Override
    public Manager getManagerByUsername(String username) {
        return managerRepo.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Manager not found"));
    }
}
