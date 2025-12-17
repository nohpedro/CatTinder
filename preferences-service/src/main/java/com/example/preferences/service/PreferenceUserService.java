package com.example.preferences.service;

import com.example.preferences.dto.PreferenceUserDTO;
import com.example.preferences.model.PreferenceUser;

import java.util.List;

public interface PreferenceUserService {

    // Agregar una opción de preferencia a un usuario
    PreferenceUser addPreferenceToUser(PreferenceUserDTO dto);

    // Listar todas las preferencias de un usuario
    List<PreferenceUser> getPreferencesByUser(Long userId);

    // Eliminar una preferencia específica de un usuario
    void removePreferenceFromUser(Long userId, Long optionId);
}
