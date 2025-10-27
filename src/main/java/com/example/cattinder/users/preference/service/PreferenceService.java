package com.example.cattinder.users.preference.service;

import com.example.cattinder.users.preference.dto.PreferenceDTO;
import com.example.cattinder.users.preference.model.Preference;

import java.util.List;

public interface PreferenceService {

    Preference createPreference(PreferenceDTO dto);

    List<Preference> getPreferencesByUserId(Long userId);

    // Nuevo método para exponer la native query (por ejemplo "todas las prefs del user X con disponibilidad 'noche'")
    List<Preference> getPreferencesByUserIdAndAvailability(Long userId, String availability);

    Preference updatePreference(Long id, PreferenceDTO dto);

    void deletePreference(Long id);
}
