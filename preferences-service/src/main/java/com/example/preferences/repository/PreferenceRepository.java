package com.example.preferences.repository;

import com.example.preferences.model.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {

    // Derived query: todas las configuraciones únicas de un usuario
    List<Preference> findByUserId(Long userId);

    // Native query: por ejemplo buscar por disponibilidad
    @Query(value = "SELECT * FROM preferences WHERE user_id = :userId AND availability = :availability", nativeQuery = true)
    List<Preference> findByUserIdAndAvailabilityNative(Long userId, String availability);
}
