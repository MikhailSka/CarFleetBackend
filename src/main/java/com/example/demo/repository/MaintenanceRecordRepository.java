package com.example.demo.repository;

import com.example.demo.models.MaintenanceRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Integer> {
    @Query("SELECT m FROM MaintenanceRecord m WHERE UPPER(CONCAT(" +
            "m.description, ' ', " +
            "CAST(m.cost AS string), ' ', " +
            "m.car.make, ' ', " +
            "m.car.model, ' ', " +
            "CAST(m.date AS string)" +
            ")) LIKE %:keyword%")
    Page<MaintenanceRecord> searchMaintenanceRecordsByKeyword(String keyword, Pageable pageable);
}
