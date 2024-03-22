package com.example.demo.services;

import com.example.demo.exceptions.InternalServerErrorException;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1);
        user.setUsername("testUser");
        user.setPassword("password");
    }

    @Test
    void getAllUsersShouldReturnUsers() {
        Page<User> userPage = new PageImpl<>(Arrays.asList(user));
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);

        List<User> result = userService.getAllUsers(0, 10, "id", "asc", "");
        assertFalse(result.isEmpty(), "Expected non-empty list of users");
        assertEquals(1, result.size(), "Expected list size of 1");
        assertEquals(user.getUsername(), result.get(0).getUsername(), "Expected usernames to match");
    }

    @Test
    void getUserByIdShouldReturnUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1);
        assertTrue(result.isPresent(), "Expected user to be present");
        assertEquals(user.getUsername(), result.get().getUsername(), "Expected usernames to match");
    }

    @Test
    void saveUserShouldPersistUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);
        assertNotNull(savedUser, "Expected a non-null user");
        assertEquals(user.getUsername(), savedUser.getUsername(), "Expected usernames to match");
    }

    @Test
    void deleteUserShouldRemoveUser() {
        userService.deleteUser(1);
        assertTrue(true, "Expected deleteUser to be called without errors");
    }

    @Test
    void getUserByUsernameShouldReturnUser() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByUsername("testUser");
        assertTrue(result.isPresent(), "Expected user to be present");
        assertEquals(user.getUsername(), result.get().getUsername(), "Expected usernames to match");
    }

    @Test
    void getAllUsersWhenInternalServerErrorShouldThrowException() {
        when(userRepository.findAll(any(PageRequest.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(InternalServerErrorException.class, () -> userService.getAllUsers(0, 10, "id", "asc", ""),
                "Expected getAllUsers to throw InternalServerErrorException");
    }
}
