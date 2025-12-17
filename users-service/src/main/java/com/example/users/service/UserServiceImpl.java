package com.example.users.service;

import com.example.users.dto.UserDTO;
import com.example.users.dto.UserProfileDTO;
import com.example.users.exception.UserNotFoundException;
import com.example.users.model.User;
import com.example.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        if (userDTO.getActive() != null) {
            user.setActive(userDTO.getActive());
        } else {
            user.setActive(true);
        }

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
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

        // Validar email único
        if(userDTO.getEmail() != null && !userDTO.getEmail().equals(existing.getEmail())) {
            userRepository.findByEmail(userDTO.getEmail())
                    .filter(u -> !u.getId().equals(id))
                    .ifPresent(u -> {
                        throw new IllegalArgumentException("Email ya existe en otro usuario");
                    });
            existing.setEmail(userDTO.getEmail());
        }

        if(userDTO.getName() != null) existing.setName(userDTO.getName());
        if(userDTO.getActive() != null) existing.setActive(userDTO.getActive());

        return userRepository.save(existing);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        User existing = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Usuario no encontrado con id: " + id));

        userRepository.delete(existing);
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
        return userRepository.save(existing);
    }

    @Transactional(readOnly = true)
    @Override
    public Long countUsers() {
        return userRepository.count();
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllActiveUsers() {
        return userRepository.findAllActiveUsers();
    }

    // -----------------------
    // MÉTODOS PARA PERFIL
    // -----------------------

    @Transactional(readOnly = true)
    @Override
    public UserProfileDTO getUserProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: " + id));

        return new UserProfileDTO(
                user.getName(),
                user.getEmail(),
                user.getActive(),
                user.getBio(),
                user.getCareer(),
                user.getInstagramUrl(),
                user.getLinkedinUrl(),
                user.getXUrl(),
                user.getVisible()
        );
    }

    @Transactional
    @Override
    public UserProfileDTO updateUserProfile(Long id, UserProfileDTO profileDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: " + id));

        // Validar email único
        if(profileDTO.getEmail() != null && !profileDTO.getEmail().equals(user.getEmail())) {
            userRepository.findByEmail(profileDTO.getEmail())
                    .filter(u -> !u.getId().equals(id))
                    .ifPresent(u -> {
                        throw new IllegalArgumentException("Email ya existe en otro usuario");
                    });
            user.setEmail(profileDTO.getEmail());
        }

        if (profileDTO.getName() != null) user.setName(profileDTO.getName());
        if (profileDTO.getActive() != null) user.setActive(profileDTO.getActive());
        if (profileDTO.getBio() != null) user.setBio(profileDTO.getBio());
        if (profileDTO.getCareer() != null) user.setCareer(profileDTO.getCareer());
        if (profileDTO.getInstagramUrl() != null) user.setInstagramUrl(profileDTO.getInstagramUrl());
        if (profileDTO.getLinkedinUrl() != null) user.setLinkedinUrl(profileDTO.getLinkedinUrl());
        if (profileDTO.getXUrl() != null) user.setXUrl(profileDTO.getXUrl());
        if (profileDTO.getVisible() != null) user.setVisible(profileDTO.getVisible());

        User updated = userRepository.save(user);

        return new UserProfileDTO(
                updated.getName(),
                updated.getEmail(),
                updated.getActive(),
                updated.getBio(),
                updated.getCareer(),
                updated.getInstagramUrl(),
                updated.getLinkedinUrl(),
                updated.getXUrl(),
                updated.getVisible()
        );
    }
}
