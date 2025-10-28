package com.example.users.repository;

import com.example.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Derived Query: Spring construye el query automáticamente por el nombre
    Optional<User> findByEmail(String email);

    // Native Query: evidencia de una consulta personalizada a la BD
    @Query(value = "SELECT * FROM users WHERE active = true", nativeQuery = true)
    List<User> findAllActiveUsers();
}
