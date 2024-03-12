package com.example.demo.repository;

import com.example.demo.models.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    Page<Car> findByModelContainingIgnoreCase(String model, Pageable pageable);
}