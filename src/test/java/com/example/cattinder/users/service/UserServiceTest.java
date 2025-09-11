package com.example.cattinder.users.service;

import com.example.cattinder.users.dto.UserDTO;
import com.example.cattinder.users.exception.UserNotFoundException;
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
        UserDTO dto = new UserDTO("Rodrigo", "rodrigo@email.com", true);
        User user = userService.createUser(dto);

        assertEquals("Rodrigo", user.getName());
        assertEquals("rodrigo@email.com", user.getEmail());
        assertTrue(user.isActive());
        assertNotNull(user.getId());
    }

    @Test
    void testUpdateUser() {
        UserDTO dto = new UserDTO("Rodrigo", "rodrigo@email.com", false);
        User user = userService.createUser(dto);

        UserDTO updateDto = new UserDTO("Rodo", "rodo@email.com", true);
        User updatedUser = userService.updateUser(user.getId(), updateDto);

        assertEquals("Rodoys", updatedUser.getName());
        assertEquals("rodo@email.com", updatedUser.getEmail());
        assertTrue(user.isActive());
    }

    @Test
    void testToggleUserStatus() {
        UserDTO dto = new UserDTO("Rodrigo", "rodrigo@email.com", false);
        User user = userService.createUser(dto);

        User toggledUser = userService.toggleUserStatus(user.getId(), false);
        assertFalse(toggledUser.isActive());

        toggledUser = userService.toggleUserStatus(user.getId(), true);
        assertTrue(toggledUser.isActive());
    }

    @Test
    void testGetUserByEmailNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("no@existe.com"));
    }

    @Test
    void testCountUsers() {
        long initialCount = userService.countUsers();
        userService.createUser(new UserDTO("Rodrigo", "rodrigo@email.com", true));
        assertEquals(initialCount + 1, userService.countUsers());
    }
}
