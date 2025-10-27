package com.example.cattinder.users.service;

import com.example.cattinder.users.dto.UserDTO;
import com.example.cattinder.users.exception.UserNotFoundException;
import com.example.cattinder.users.model.User;
import com.example.cattinder.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // Inyección por constructor
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        // si el DTO manda active, úsalo. Si no manda, por defecto true:
        if (userDTO.getActive() != null) {
            user.setActive(userDTO.getActive());
        } else {
            user.setActive(true);
        }

        return userRepository.save(user); // INSERT en BD
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll(); // SELECT * FROM users
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Usuario no encontrado con id: " + id));
    }

    @Transactional
    @Override
    public User updateUser(Long id, UserDTO userDTO) {
        User existing = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Usuario no encontrado con id: " + id));

        existing.setName(userDTO.getName());
        existing.setEmail(userDTO.getEmail());

        // si manda active en el DTO, también actualizamos
        if (userDTO.getActive() != null) {
            existing.setActive(userDTO.getActive());
        }

        return userRepository.save(existing); // UPDATE en BD
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        User existing = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Usuario no encontrado con id: " + id));

        userRepository.delete(existing); // DELETE en BD
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("Usuario no encontrado con email: " + email));
    }

    @Transactional
    @Override
    public User toggleUserStatus(Long id, boolean active) {
        User existing = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Usuario no encontrado con id: " + id));

        existing.setActive(active);
        return userRepository.save(existing); // persiste el cambio de estado
    }

    @Transactional(readOnly = true)
    @Override
    public Long countUsers() {
        return userRepository.count();
    }

    // 🔥 EXTRA: usamos la native query findAllActiveUsers()
    @Transactional(readOnly = true)
    @Override
    public List<User> getAllActiveUsers() {
        return userRepository.findAllActiveUsers();
    }
}
