package com.example.preferences.service;

import com.example.preferences.dto.PreferenceDTO;
import com.example.preferences.model.Preference;

import java.util.List;

public interface PreferenceService {

    Preference createPreference(PreferenceDTO dto);

    List<Preference> getPreferencesByUserId(Long userId);

    // Nuevo método para exponer la native query (por ejemplo "todas las prefs del user X con disponibilidad 'noche'")
    List<Preference> getPreferencesByUserIdAndAvailability(Long userId, String availability);

    Preference updatePreference(Long id, PreferenceDTO dto);

    void deletePreference(Long id);
}
