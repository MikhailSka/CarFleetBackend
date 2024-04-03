package com.example.demo.repository;

import com.example.demo.models.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    @Query("SELECT c FROM Car c WHERE UPPER(CONCAT(" +
            "c.make, ' ', " +
            "c.model, ' ', " +
            "c.registrationNumber, ' ', " +
            "c.status, ' ', " +
            "CAST(c.year AS string), ' ', " +
            "CAST(c.distance AS string), ' ', " +
            "CAST(c.minuteCost AS string), ' ', " +
            "CAST(c.kilometerCost AS string)" +
            ")) LIKE %:keyword%")
    Page<Car> searchCarsByKeyword(String keyword, Pageable pageable);

    List<Car> findByAvailableTrue();
}

