package com.example.preferences.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PreferenceOptionDTO {

    @NotBlank(message = "El nombre de la preferencia es obligatorio")
    @Size(max = 50, message = "El nombre de la preferencia no puede exceder 50 caracteres")
    private String name;

    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String description;

    public PreferenceOptionDTO() {}

    public PreferenceOptionDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
