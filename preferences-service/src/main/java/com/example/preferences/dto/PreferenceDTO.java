package com.example.preferences.dto;

import jakarta.validation.constraints.NotNull;

public class PreferenceDTO {

    @NotNull(message = "El userId es obligatorio")
    private Long userId;

    @NotNull(message = "El studyStyle es obligatorio")
    private String studyStyle;

    @NotNull(message = "La availability es obligatoria")
    private String availability;

    public PreferenceDTO() {}

    public PreferenceDTO(Long userId, String studyStyle, String availability) {
        this.userId = userId;
        this.studyStyle = studyStyle;
        this.availability = availability;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getStudyStyle() { return studyStyle; }
    public void setStudyStyle(String studyStyle) { this.studyStyle = studyStyle; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
}
