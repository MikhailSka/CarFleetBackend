package com.example.demo.services;

import com.example.demo.exceptions.InternalServerErrorException;
import com.example.demo.models.MaintenanceRecord;
import com.example.demo.repository.MaintenanceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public List<MaintenanceRecord> getAllMaintenanceRecords(int page, int size, String sortBy, String sortOrder, String keyword) {
        try {
            Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            if (keyword != null && !keyword.isEmpty()) {
                Page<MaintenanceRecord> maintenanceRecordPage = maintenanceRecordRepository.findByDescriptionContainingIgnoreCase(keyword, pageable);
                return maintenanceRecordPage.getContent();
            } else {
                Page<MaintenanceRecord> maintenanceRecordPage = maintenanceRecordRepository.findAll(pageable);
                return maintenanceRecordPage.getContent();
            }
        } catch (Exception e) {
            // Log the exception
            throw new InternalServerErrorException("An error occurred while retrieving maintenance records");
        }
    }

    public Optional<MaintenanceRecord> getMaintenanceRecordById(int id) {
        try {
            return maintenanceRecordRepository.findById(id);
        } catch (Exception e) {
            // Log the exception
            throw new InternalServerErrorException("An error occurred while retrieving the maintenance record by id: " + id);
        }
    }

    public MaintenanceRecord saveMaintenanceRecord(MaintenanceRecord maintenanceRecord) {
        try {
            return maintenanceRecordRepository.save(maintenanceRecord);
        } catch (Exception e) {
            // Log the exception
            throw new InternalServerErrorException("An error occurred while saving the maintenance record");
        }
    }

    public void deleteMaintenanceRecord(int id) {
        try {
            maintenanceRecordRepository.deleteById(id);
        } catch (Exception e) {
            // Log the exception
            throw new InternalServerErrorException("An error occurred while deleting the maintenance record with id: " + id);
        }
    }
}
