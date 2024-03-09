package com.example.demo.models;

import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String make;
    private String model;
    private int year;
    private String registrationNumber;
    private String status;
    private int distance;
    private BigDecimal minuteCost;
    private BigDecimal kilometerCost;
}

