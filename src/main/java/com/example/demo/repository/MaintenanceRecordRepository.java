package com.example.demo.repository;

import com.example.demo.models.MaintenanceRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Integer> {
    Page<MaintenanceRecord> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);
}