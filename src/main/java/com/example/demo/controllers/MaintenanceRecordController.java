package com.example.demo.controllers;

import com.example.demo.exceptions.InternalServerErrorException;
import com.example.demo.models.MaintenanceRecord;
import com.example.demo.responses.GenericResponse;
import com.example.demo.services.MaintenanceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<GenericResponse> getAllMaintenanceRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String keyword) {
        try {
            GenericResponse response = maintenanceRecordService.getAllMaintenanceRecords(page, size, sortBy, sortOrder, keyword);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceRecord> getMaintenanceRecordById(@PathVariable int id) {
        try {
            Optional<MaintenanceRecord> maintenanceRecord = maintenanceRecordService.getMaintenanceRecordById(id);
            return maintenanceRecord.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<MaintenanceRecord> createMaintenanceRecord(@RequestBody MaintenanceRecord maintenanceRecord) {
        try {
            MaintenanceRecord newMaintenanceRecord = maintenanceRecordService.saveMaintenanceRecord(maintenanceRecord);
            return new ResponseEntity<>(newMaintenanceRecord, HttpStatus.CREATED);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenanceRecord(@PathVariable int id) {
        try {
            maintenanceRecordService.deleteMaintenanceRecord(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
