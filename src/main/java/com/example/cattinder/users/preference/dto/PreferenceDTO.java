package com.example.cattinder.users.preference.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PreferenceDTO {
    @NotNull(message = "El userId es obligatorio")
    private Long userId;

    @NotBlank(message = "La materia es obligatoria")
    private String subject;

    @NotBlank(message = "El estilo de estudio es obligatorio")
    private String studyStyle;

    @NotBlank(message = "La disponibilidad es obligatoria")
    private String availability;

    public PreferenceDTO() {}

    public PreferenceDTO(Long userId, String subject, String studyStyle, String availability) {
        this.userId = userId;
        this.subject = subject;
        this.studyStyle = studyStyle;
        this.availability = availability;
    }

    // Getters & Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getStudyStyle() { return studyStyle; }
    public void setStudyStyle(String studyStyle) { this.studyStyle = studyStyle; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
}
