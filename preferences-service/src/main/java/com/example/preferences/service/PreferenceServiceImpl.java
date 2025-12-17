package com.example.preferences.service;

import com.example.preferences.dto.PreferenceDTO;
import com.example.preferences.exception.PreferenceNotFoundException;
import com.example.preferences.model.Preference;
import com.example.preferences.repository.PreferenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PreferenceServiceImpl implements PreferenceService {

    private final PreferenceRepository preferenceRepository;

    public PreferenceServiceImpl(PreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    @Transactional
    @Override
    public Preference createOrUpdateSettings(PreferenceDTO dto) {
        Preference settings = preferenceRepository.findByUserId(dto.getUserId())
                .stream().findFirst().orElse(new Preference());
        settings.setUserId(dto.getUserId());
        settings.setStudyStyle(dto.getStudyStyle());
        settings.setAvailability(dto.getAvailability());
        return preferenceRepository.save(settings);
    }

    @Transactional(readOnly = true)
    @Override
    public Preference getSettingsByUser(Long userId) {
        return preferenceRepository.findByUserId(userId)
                .stream().findFirst()
                .orElseThrow(() -> new PreferenceNotFoundException("Settings de usuario no encontrados"));
    }
}
