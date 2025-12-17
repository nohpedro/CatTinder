package com.example.preferences.service;

import com.example.preferences.dto.PreferenceUserDTO;
import com.example.preferences.exception.PreferenceNotFoundException;
import com.example.preferences.model.PreferenceUser;
import com.example.preferences.repository.PreferenceUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PreferenceUserServiceImpl implements PreferenceUserService {

    private final PreferenceUserRepository userRepository;

    public PreferenceUserServiceImpl(PreferenceUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public PreferenceUser addPreferenceToUser(PreferenceUserDTO dto) {
        // Verificar duplicado
        userRepository.findByUserIdAndPreferenceOptionId(dto.getUserId(), dto.getPreferenceOptionId())
                .ifPresent(u -> { throw new RuntimeException("El usuario ya tiene esta preferencia"); });

        PreferenceUser pu = new PreferenceUser();
        pu.setUserId(dto.getUserId());
        pu.setPreferenceOptionId(dto.getPreferenceOptionId());
        return userRepository.save(pu);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PreferenceUser> getPreferencesByUser(Long userId) {
        return userRepository.findByUserId(userId);
    }

    @Transactional
    @Override
    public void removePreferenceFromUser(Long userId, Long optionId) {
        // Usando query nativa del repository
        userRepository.deleteByUserIdAndPreferenceOptionId(userId, optionId);
    }
}
