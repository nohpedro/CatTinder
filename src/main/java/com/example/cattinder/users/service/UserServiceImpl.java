package com.example.cattinder.users.service;

import com.example.cattinder.users.dto.UserDTO;
import com.example.cattinder.users.exception.UserNotFoundException;
import com.example.cattinder.users.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserServiceImpl implements UserService {

    private final List<User> users = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    @Override
    public User createUser(UserDTO userDTO) {
        User user = new User(counter.incrementAndGet(), userDTO.getName(), userDTO.getEmail(), true);
        users.add(user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    @Override
    public User getUserById(Long id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: " + id));
    }

    @Override
    public User updateUser(Long id, UserDTO userDTO) {
        User user = getUserById(id);
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);
        users.remove(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + email));
    }

    @Override
    public User toggleUserStatus(Long id, boolean active) {
        User user = getUserById(id);
        user.setActive(active);
        return user;
    }

    @Override
    public Long countUsers() {
        return (long) users.size();
    }
}
