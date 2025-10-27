package com.example.cattinder.users.preference.service;

import com.example.cattinder.users.preference.dto.PreferenceDTO;
import com.example.cattinder.users.preference.exception.PreferenceNotFoundException;
import com.example.cattinder.users.preference.model.Preference;
import com.example.cattinder.users.preference.repository.PreferenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PreferenceServiceImpl implements PreferenceService {

    private final PreferenceRepository preferenceRepository;

    public PreferenceServiceImpl(PreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    @Transactional
    @Override
    public Preference createPreference(PreferenceDTO dto) {
        Preference pref = new Preference();
        pref.setUserId(dto.getUserId());
        pref.setSubject(dto.getSubject());
        pref.setStudyStyle(dto.getStudyStyle());
        pref.setAvailability(dto.getAvailability());

        // INSERT en BD
        return preferenceRepository.save(pref);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Preference> getPreferencesByUserId(Long userId) {
        // SELECT * FROM preferences WHERE user_id = ?
        return preferenceRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Preference> getPreferencesByUserIdAndAvailability(Long userId, String availability) {
        // SELECT * FROM preferences WHERE user_id = :userId AND availability = :availability
        return preferenceRepository.findByUserIdAndAvailabilityNative(userId, availability);
    }

    @Transactional
    @Override
    public Preference updatePreference(Long id, PreferenceDTO dto) {
        Preference existing = preferenceRepository.findById(id)
                .orElseThrow(() ->
                        new PreferenceNotFoundException("Preferencia no encontrada con id: " + id));

        existing.setSubject(dto.getSubject());
        existing.setStudyStyle(dto.getStudyStyle());
        existing.setAvailability(dto.getAvailability());

        // UPDATE en BD
        return preferenceRepository.save(existing);
    }

    @Transactional
    @Override
    public void deletePreference(Long id) {
        Preference existing = preferenceRepository.findById(id)
                .orElseThrow(() ->
                        new PreferenceNotFoundException("Preferencia no encontrada con id: " + id));

        // DELETE en BD
        preferenceRepository.delete(existing);
    }
}
