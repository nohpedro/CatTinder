package com.example.users.service;

import com.example.users.dto.UserDTO;
import com.example.users.dto.UserProfileDTO;
import com.example.users.model.User;

import java.util.List;

public interface UserService {

    User createUser(UserDTO userDTO);

    List<User> getAllUsers();

    User getUserById(Long id);

    User updateUser(Long id, UserDTO userDTO);

    void deleteUser(Long id);

    User getUserByEmail(String email);

    User toggleUserStatus(Long id, boolean active);

    Long countUsers();

    List<User> getAllActiveUsers();

    // -----------------------
    // NUEVOS MÉTODOS PARA PERFIL
    // -----------------------
    UserProfileDTO getUserProfile(Long id);

    UserProfileDTO updateUserProfile(Long id, UserProfileDTO profileDTO);
}
