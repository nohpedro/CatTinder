package com.example.cattinder.users.preference.service;

import com.example.cattinder.users.preference.dto.PreferenceDTO;
import com.example.cattinder.users.preference.model.Preference;

import java.util.List;

public interface PreferenceService {
    Preference createPreference(PreferenceDTO dto);
    List<Preference> getPreferencesByUserId(Long userId);
    Preference updatePreference(Long id, PreferenceDTO dto);
    void deletePreference(Long id);
}
