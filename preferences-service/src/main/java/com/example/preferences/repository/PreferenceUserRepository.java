package com.example.preferences.repository;

import com.example.preferences.model.PreferenceUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PreferenceUserRepository extends JpaRepository<PreferenceUser, Long> {

    // Todas las preferencias de un usuario
    List<PreferenceUser> findByUserId(Long userId);

    // Verificar si el usuario ya tiene cierta preferencia
    Optional<PreferenceUser> findByUserIdAndPreferenceOptionId(Long userId, Long preferenceOptionId);

    // Eliminar una preferencia de un usuario usando query nativa
    @Modifying
    @Query(value = "DELETE FROM preference_users WHERE user_id = :userId AND preference_option_id = :preferenceOptionId", nativeQuery = true)
    void deleteByUserIdAndPreferenceOptionId(Long userId, Long preferenceOptionId);
}
