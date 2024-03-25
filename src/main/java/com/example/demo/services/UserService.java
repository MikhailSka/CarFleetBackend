package com.example.demo.services;

import com.example.demo.exceptions.InternalServerErrorException;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.responses.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public GenericResponse getAllUsers(int page, int size, String sortBy, String sortOrder, String keyword) {
        try {
            Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<User> userPage;
            if (keyword != null && !keyword.isEmpty()) {
                userPage = userRepository.searchUsersByKeyword(keyword, pageable);
            } else {
                userPage = userRepository.findAll(pageable);
            }

            List<User> users = userPage.getContent();
            long totalUsers = userPage.getTotalElements(); // This is how you get the total count

            return new GenericResponse(users, totalUsers);
        } catch (Exception e) {
            // Log the exception
            throw new InternalServerErrorException("An error occurred while retrieving users");
        }
    }


    public Optional<User> getUserById(int id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            // Log the exception
            throw new InternalServerErrorException("An error occurred while retrieving the user by id: " + id);
        }
    }

    public User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            // Log the exception
            throw new InternalServerErrorException("An error occurred while saving the user");
        }
    }

    public void deleteUser(int id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            // Log the exception
            throw new InternalServerErrorException("An error occurred while deleting the user with id: " + id);
        }
    }

    public Optional<User> getUserByUsername(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            // Log the exception
            throw new InternalServerErrorException("An error occurred while retrieving the user by username: " + username);
        }
    }
}
