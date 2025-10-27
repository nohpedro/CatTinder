package com.example.cattinder.users.preference.repository;

import com.example.cattinder.users.preference.model.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {

    // Derived query: Spring genera "SELECT * FROM preferences WHERE user_id = ?"
    List<Preference> findByUserId(Long userId);

    // Native query: ejemplo para la rúbrica
    // Trae todas las preferencias de un usuario filtrando por disponibilidad
    @Query(
            value = "SELECT * FROM preferences WHERE user_id = :userId AND availability = :availability",
            nativeQuery = true
    )
    List<Preference> findByUserIdAndAvailabilityNative(Long userId, String availability);
}
