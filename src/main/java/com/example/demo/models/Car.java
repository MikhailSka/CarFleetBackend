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
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String make;
    private String model;
    private int year;
    @Column(name = "registration_number")
    private String registrationNumber;
    private String status;
    private int distance;
    @Column(name = "minute_cost")
    private BigDecimal minuteCost;
    @Column(name = "kilometer_cost")
    private BigDecimal kilometerCost;
    private boolean available; // New field to indicate availability for rental

    // Getter and setter for the 'available' field
    public boolean isAvailable() {
        return this.available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

}

