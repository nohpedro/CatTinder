package com.example.preferences.repository;

import com.example.preferences.model.PreferenceOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PreferenceOptionRepository extends JpaRepository<PreferenceOption, Long> {

    // Buscar por nombre de preferencia
    List<PreferenceOption> findByName(String name);

    // Ejemplo de query nativa: buscar todas las opciones activas (si agregas un flag active)
    @Query(value = "SELECT * FROM preference_options", nativeQuery = true)
    List<PreferenceOption> findAllOptions();
}
