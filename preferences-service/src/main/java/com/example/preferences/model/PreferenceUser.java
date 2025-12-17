package com.example.preferences.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(
        name = "preference_users",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "preference_option_id"}),
        indexes = {
                @Index(columnList = "user_id"),
                @Index(columnList = "preference_option_id")
        }
)
public class PreferenceUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El userId es obligatorio")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull(message = "El preferenceOptionId es obligatorio")
    @Column(name = "preference_option_id", nullable = false)
    private Long preferenceOptionId;

    // Constructor vacío requerido por JPA
    public PreferenceUser() {}

    // Constructor completo
    public PreferenceUser(Long id, Long userId, Long preferenceOptionId) {
        this.id = id;
        this.userId = userId;
        this.preferenceOptionId = preferenceOptionId;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getPreferenceOptionId() { return preferenceOptionId; }
    public void setPreferenceOptionId(Long preferenceOptionId) {
        this.preferenceOptionId = preferenceOptionId;
    }
}
