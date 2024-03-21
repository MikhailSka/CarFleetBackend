package com.example.demo.repository;

import com.example.demo.models.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {
    @Query("SELECT t FROM Trip t WHERE UPPER(CONCAT(" +
            "t.car.make, ' ', " +
            "t.car.model, ' ', " +
            "t.user.username, ' ', " +
            "t.startTime, ' ', " +
            "t.endTime, ' ', " +
            "CAST(t.startDistance AS string), ' ', " +
            "CAST(t.endDistance AS string), ' ', " +
            "CAST(t.tripCost AS string)" +
            ")) LIKE %:keyword%")
    Page<Trip> searchTripsByKeyword(String keyword, Pageable pageable);
}
