package com.example.demo.services;

import com.example.demo.models.MaintenanceRecord;
import com.example.demo.repository.MaintenanceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MaintenanceRecordService {
    private final MaintenanceRecordRepository maintenanceRecordRepository;

    @Autowired
    public MaintenanceRecordService(MaintenanceRecordRepository maintenanceRecordRepository) {
        this.maintenanceRecordRepository = maintenanceRecordRepository;
    }

    public List<MaintenanceRecord> getAllMaintenanceRecords() {
        return maintenanceRecordRepository.findAll();
    }

    public Optional<MaintenanceRecord> getMaintenanceRecordById(int id) {
        return maintenanceRecordRepository.findById(id);
    }

    public MaintenanceRecord saveMaintenanceRecord(MaintenanceRecord maintenanceRecord) {
        return maintenanceRecordRepository.save(maintenanceRecord);
    }

    public void deleteMaintenanceRecord(int id) {
        maintenanceRecordRepository.deleteById(id);
    }
}