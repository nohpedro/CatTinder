package com.example.cattinder.users.preference.service;

import com.example.cattinder.users.preference.dto.PreferenceDTO;
import com.example.cattinder.users.preference.exception.PreferenceNotFoundException;
import com.example.cattinder.users.preference.model.Preference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class PreferenceServiceImpl implements PreferenceService {

    private final List<Preference> preferences = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    @Override
    public Preference createPreference(PreferenceDTO dto) {
        Preference preference = new Preference(
                counter.incrementAndGet(),
                dto.getUserId(),
                dto.getSubject(),
                dto.getStudyStyle(),
                dto.getAvailability()
        );
        preferences.add(preference);
        return preference;
    }

    @Override
    public List<Preference> getPreferencesByUserId(Long userId) {
        return preferences.stream()
                .filter(p -> p.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Preference updatePreference(Long id, PreferenceDTO dto) {
        Preference preference = preferences.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new PreferenceNotFoundException("Preferencia no encontrada con id: " + id));

        preference.setSubject(dto.getSubject());
        preference.setStudyStyle(dto.getStudyStyle());
        preference.setAvailability(dto.getAvailability());
        return preference;
    }

    @Override
    public void deletePreference(Long id) {
        Preference preference = preferences.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new PreferenceNotFoundException("Preferencia no encontrada con id: " + id));

        preferences.remove(preference);
    }
}
