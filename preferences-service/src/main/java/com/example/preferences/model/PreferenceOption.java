package com.example.preferences.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(
        name = "preference_options",
        indexes = {@Index(columnList = "name")}
)
public class PreferenceOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la preferencia es obligatorio")
    @Size(max = 50, message = "El nombre de la preferencia no puede exceder 50 caracteres")
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    @Column(length = 255)
    private String description;

    // Constructor vacío requerido por JPA
    public PreferenceOption() {}

    // Constructor completo
    public PreferenceOption(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
