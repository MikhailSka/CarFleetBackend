package com.example.demo.controllers;

import com.example.demo.config.TestSecurityConfig;
import com.example.demo.models.MaintenanceRecord;
import com.example.demo.responses.GenericResponse;
import com.example.demo.services.MaintenanceRecordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MaintenanceRecordController.class)
@Import(TestSecurityConfig.class)
public class MaintenanceRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MaintenanceRecordService maintenanceRecordService;

    @Autowired
    private ObjectMapper objectMapper;

    private MaintenanceRecord maintenanceRecord;

    @BeforeEach
    void setUp() {
        maintenanceRecord = new MaintenanceRecord();
        maintenanceRecord.setId(1);
        // Initialize other necessary fields of maintenanceRecord
    }

    @Test
    void getAllMaintenanceRecordsShouldReturnRecords() throws Exception {
        MaintenanceRecord maintenanceRecord = new MaintenanceRecord(); // Initialize your maintenanceRecord with test data
        maintenanceRecord.setId(1); // And other necessary properties

        GenericResponse maintenanceRecordResponse = new GenericResponse(Collections.singletonList(maintenanceRecord), 1);
        given(maintenanceRecordService.getAllMaintenanceRecords(0, 10, "id", "asc", null)).willReturn(maintenanceRecordResponse);

        mockMvc.perform(get("/api/maintenance-records")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("sortOrder", "asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].id").value(maintenanceRecord.getId()))
                .andExpect(jsonPath("$.total").value(1));
    }

    @Test
    void getMaintenanceRecordByIdShouldReturnRecord() throws Exception {
        given(maintenanceRecordService.getMaintenanceRecordById(1)).willReturn(Optional.of(maintenanceRecord));

        mockMvc.perform(get("/api/maintenance-records/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(maintenanceRecord.getId()));
    }

    @Test
    void getMaintenanceRecordByIdWhenNotFoundShouldReturnNotFound() throws Exception {
        given(maintenanceRecordService.getMaintenanceRecordById(1)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/maintenance-records/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void createMaintenanceRecordShouldReturnCreated() throws Exception {
        given(maintenanceRecordService.saveMaintenanceRecord(any(MaintenanceRecord.class))).willReturn(maintenanceRecord);

        mockMvc.perform(post("/api/maintenance-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(maintenanceRecord)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(maintenanceRecord.getId()));
    }

    @Test
    void deleteMaintenanceRecordShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/maintenance-records/{id}", maintenanceRecord.getId()))
                .andExpect(status().isNoContent());
    }
}

