package com.example.preferences.service;

import com.example.preferences.dto.PreferenceOptionDTO;
import com.example.preferences.exception.PreferenceNotFoundException;
import com.example.preferences.model.PreferenceOption;
import com.example.preferences.repository.PreferenceOptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PreferenceOptionServiceImpl implements PreferenceOptionService {

    private final PreferenceOptionRepository optionRepository;

    public PreferenceOptionServiceImpl(PreferenceOptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    @Transactional
    @Override
    public PreferenceOption createOption(PreferenceOptionDTO dto) {
        PreferenceOption option = new PreferenceOption();
        option.setName(dto.getName());
        option.setDescription(dto.getDescription());
        return optionRepository.save(option);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PreferenceOption> getAllOptions() {
        // Usamos la query nativa obligatoria del repository
        return optionRepository.findAllOptions();
    }

    @Transactional
    @Override
    public PreferenceOption updateOption(Long id, PreferenceOptionDTO dto) {
        PreferenceOption existing = optionRepository.findById(id)
                .orElseThrow(() -> new PreferenceNotFoundException("Opción no encontrada con id: " + id));
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        return optionRepository.save(existing);
    }

    @Transactional
    @Override
    public void deleteOption(Long id) {
        PreferenceOption existing = optionRepository.findById(id)
                .orElseThrow(() -> new PreferenceNotFoundException("Opción no encontrada con id: " + id));
        optionRepository.delete(existing);
    }
}
