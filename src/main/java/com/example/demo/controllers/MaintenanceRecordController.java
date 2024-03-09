package com.example.demo.controllers;

import com.example.demo.models.MaintenanceRecord;
import com.example.demo.services.MaintenanceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/maintenance-records")
public class MaintenanceRecordController {
    private final MaintenanceRecordService maintenanceRecordService;

    @Autowired
    public MaintenanceRecordController(MaintenanceRecordService maintenanceRecordService) {
        this.maintenanceRecordService = maintenanceRecordService;
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceRecord>> getAllMaintenanceRecords() {
        List<MaintenanceRecord> maintenanceRecords = maintenanceRecordService.getAllMaintenanceRecords();
        return new ResponseEntity<>(maintenanceRecords, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceRecord> getMaintenanceRecordById(@PathVariable int id) {
        Optional<MaintenanceRecord> maintenanceRecord = maintenanceRecordService.getMaintenanceRecordById(id);
        return maintenanceRecord.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<MaintenanceRecord> createMaintenanceRecord(@RequestBody MaintenanceRecord maintenanceRecord) {
        MaintenanceRecord newMaintenanceRecord = maintenanceRecordService.saveMaintenanceRecord(maintenanceRecord);
        return new ResponseEntity<>(newMaintenanceRecord, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenanceRecord(@PathVariable int id) {
        maintenanceRecordService.deleteMaintenanceRecord(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
