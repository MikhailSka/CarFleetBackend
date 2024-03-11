package com.example.demo.models;

import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "maintenanceRecords")
public class MaintenanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;
    private Date date;
    private String description;
    private BigDecimal cost;
}
