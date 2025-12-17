package com.example.preferences.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(
        name = "preferences",
        indexes = {
                @Index(columnList = "user_id")
        }
)
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación lógica con users-service
    @NotNull(message = "El userId es obligatorio")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull(message = "El studyStyle es obligatorio")
    @Column(name = "study_style", nullable = false)
    private String studyStyle;

    @NotNull(message = "La availability es obligatoria")
    @Column(name = "availability", nullable = false)
    private String availability;

    // Constructor vacío requerido por JPA
    public Preference() {
    }

    // Constructor completo (opcional, útil para tests)
    public Preference(Long id, Long userId, String studyStyle, String availability) {
        this.id = id;
        this.userId = userId;
        this.studyStyle = studyStyle;
        this.availability = availability;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStudyStyle() {
        return studyStyle;
    }

    public void setStudyStyle(String studyStyle) {
        this.studyStyle = studyStyle;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
