package com.example.cattinder.users.service;

import com.example.cattinder.users.dto.UserDTO;
import com.example.cattinder.users.model.User;

import java.util.List;

public interface UserService {
    User createUser(UserDTO userDTO);
    List<User> getAllUsers();
    User getUserById(Long id);
}
