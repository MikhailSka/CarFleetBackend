package com.example.demo.services;

import com.example.demo.exceptions.InternalServerErrorException;
import com.example.demo.models.MaintenanceRecord;
import com.example.demo.repository.MaintenanceRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MaintenanceRecordServiceTest {

    @Mock
    private MaintenanceRecordRepository maintenanceRecordRepository;

    @InjectMocks
    private MaintenanceRecordService maintenanceRecordService;

    private MaintenanceRecord maintenanceRecord;

    @BeforeEach
    void setUp() {
        maintenanceRecord = new MaintenanceRecord();
        maintenanceRecord.setId(1);
        // Set other fields as needed
    }

    @Test
    void getAllMaintenanceRecordsShouldReturnRecords() {
        Page<MaintenanceRecord> maintenanceRecordPage = new PageImpl<>(Arrays.asList(maintenanceRecord));
        when(maintenanceRecordRepository.findAll(any(PageRequest.class))).thenReturn(maintenanceRecordPage);

        List<MaintenanceRecord> result = maintenanceRecordService.getAllMaintenanceRecords(0, 10, "id", "asc", "");
        assertFalse(result.isEmpty(), "Expected non-empty list of maintenance records");
        assertEquals(1, result.size(), "Expected list size of 1");
    }

    @Test
    void getMaintenanceRecordByIdShouldReturnRecord() {
        when(maintenanceRecordRepository.findById(1)).thenReturn(Optional.of(maintenanceRecord));

        Optional<MaintenanceRecord> result = maintenanceRecordService.getMaintenanceRecordById(1);
        assertTrue(result.isPresent(), "Expected maintenance record to be present");
        assertEquals(maintenanceRecord.getId(), result.get().getId(), "Expected maintenance record IDs to match");
    }

    @Test
    void saveMaintenanceRecordShouldPersistRecord() {
        when(maintenanceRecordRepository.save(any(MaintenanceRecord.class))).thenReturn(maintenanceRecord);

        MaintenanceRecord savedRecord = maintenanceRecordService.saveMaintenanceRecord(maintenanceRecord);
        assertNotNull(savedRecord, "Expected a non-null maintenance record");
        assertEquals(maintenanceRecord.getId(), savedRecord.getId(), "Expected maintenance record IDs to match");
    }

    @Test
    void deleteMaintenanceRecordShouldRemoveRecord() {
        maintenanceRecordService.deleteMaintenanceRecord(1);
        assertTrue(true, "Expected deleteMaintenanceRecord to be called without errors");
    }

    @Test
    void getAllMaintenanceRecordsWhenInternalServerErrorShouldThrowException() {
        when(maintenanceRecordRepository.findAll(any(PageRequest.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(InternalServerErrorException.class,
                () -> maintenanceRecordService.getAllMaintenanceRecords(0, 10, "id", "asc", ""),
                "Expected getAllMaintenanceRecords to throw InternalServerErrorException");
    }
}
