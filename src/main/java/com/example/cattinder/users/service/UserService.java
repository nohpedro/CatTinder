package com.example.cattinder.users.service;

import com.example.cattinder.users.dto.UserDTO;
import com.example.cattinder.users.model.User;

import java.util.List;

public interface UserService {
    User createUser(UserDTO userDTO);
    List<User> getAllUsers();
    User getUserById(Long id);

    // Nuevos métodos para mejorar el microservicio
    User updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    User getUserByEmail(String email);
    User toggleUserStatus(Long id, boolean active);
    Long countUsers();
}
