package com.example.preferences.service;

import com.example.preferences.dto.PreferenceDTO;
import com.example.preferences.model.Preference;

public interface PreferenceService {

    // Crear o actualizar configuración única de usuario
    Preference createOrUpdateSettings(PreferenceDTO dto);

    // Obtener configuración de usuario
    Preference getSettingsByUser(Long userId);
}
