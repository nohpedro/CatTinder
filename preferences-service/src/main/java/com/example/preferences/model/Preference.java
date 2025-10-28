package com.example.preferences.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "preferences")
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación lógica con User (por ahora guardamos solo el ID del usuario)
    @NotNull(message = "El userId es obligatorio")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotBlank(message = "La materia (subject) es obligatoria")
    @Column(nullable = false)
    private String subject;

    @NotBlank(message = "El estilo de estudio es obligatorio")
    @Column(name = "study_style", nullable = false)
    private String studyStyle;

    @NotBlank(message = "La disponibilidad es obligatoria")
    @Column(nullable = false)
    private String availability;

    // Constructor vacío requerido por JPA
    public Preference() {}

    // Constructor completo (útil para tests)
    public Preference(Long id, Long userId, String subject, String studyStyle, String availability) {
        this.id = id;
        this.userId = userId;
        this.subject = subject;
        this.studyStyle = studyStyle;
        this.availability = availability;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getStudyStyle() { return studyStyle; }
    public void setStudyStyle(String studyStyle) { this.studyStyle = studyStyle; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
}
