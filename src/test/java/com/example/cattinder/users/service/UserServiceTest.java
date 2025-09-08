package com.example.cattinder.users.service;

import com.example.cattinder.users.dto.UserDTO;
import com.example.cattinder.users.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl();
    }

    @Test
    void testCreateUser() {
        UserDTO dto = new UserDTO("Rodrigo", "rodrigo@email.com");
        User user = userService.createUser(dto);

        assertEquals("Rodrigo", user.getName());
        assertEquals("rodrigo@email.com", user.getEmail());
        assertNotNull(user.getId());
    }
}
