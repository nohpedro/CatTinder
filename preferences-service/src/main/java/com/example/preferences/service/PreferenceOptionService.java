package com.example.preferences.service;

import com.example.preferences.dto.PreferenceOptionDTO;
import com.example.preferences.model.PreferenceOption;

import java.util.List;

public interface PreferenceOptionService {

    // Crear una nueva opción de preferencia
    PreferenceOption createOption(PreferenceOptionDTO dto);

    // Listar todas las opciones de preferencia
    List<PreferenceOption> getAllOptions();

    // Actualizar una opción existente
    PreferenceOption updateOption(Long id, PreferenceOptionDTO dto);

    // Eliminar una opción por id
    void deleteOption(Long id);
}
