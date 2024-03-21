package com.example.demo.repository;

import com.example.demo.models.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE UPPER(CONCAT(" +
            "u.username, ' ', " +
            "u.role, ' ', " +
            "u.email, ' ', " +
            "u.phone, ' ', " +
            "u.status" +
            ")) LIKE %:keyword%")
    Page<User> searchUsersByKeyword(String keyword, Pageable pageable);
}
