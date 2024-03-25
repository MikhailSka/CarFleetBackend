package com.example.demo.controllers;

import com.example.demo.exceptions.InternalServerErrorException;
import com.example.demo.models.User;
import com.example.demo.responses.GenericResponse;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<GenericResponse> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String keyword) {
        try {
            GenericResponse response = userService.getAllUsers(page, size, sortBy, sortOrder, keyword);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        try {
            Optional<User> user = userService.getUserById(id);
            return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User newUser = userService.saveUser(user);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            User newUser = userService.saveUser(user);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User loginUser) {
        try {
            Optional<User> existingUser = userService.getUserByUsername(loginUser.getUsername());
            if (existingUser.isPresent()) {
                if (existingUser.get().getPassword().equals(loginUser.getPassword())) {
                    return new ResponseEntity<>(existingUser.get(), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}