package com.example.demo.controllers;


import com.example.demo.models.User;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = userService.saveUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        // Check if the username is already taken
        Optional<User> existingUser = userService.getUserByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // Username already exists
        }

        // Save the new user
        User newUser = userService.saveUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User loginUser) {
        // Find the user by username
        Optional<User> existingUser = userService.getUserByUsername(loginUser.getUsername());
        if (existingUser.isPresent()) {
            // Check if the password matches
            if (existingUser.get().getPassword().equals(loginUser.getPassword())) {
                return new ResponseEntity<>(existingUser.get(), HttpStatus.OK); // Login successful
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Incorrect password
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // User not found
        }
    }
}
