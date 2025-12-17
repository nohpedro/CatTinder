package com.example.preferences.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PreferenceUserDTO {

    @NotNull(message = "El userId es obligatorio")
    @Positive(message = "El userId debe ser positivo")
    private Long userId;

    @NotNull(message = "El preferenceOptionId es obligatorio")
    @Positive(message = "El preferenceOptionId debe ser positivo")
    private Long preferenceOptionId;

    public PreferenceUserDTO() {}

    public PreferenceUserDTO(Long userId, Long preferenceOptionId) {
        this.userId = userId;
        this.preferenceOptionId = preferenceOptionId;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getPreferenceOptionId() { return preferenceOptionId; }
    public void setPreferenceOptionId(Long preferenceOptionId) { this.preferenceOptionId = preferenceOptionId; }
}
