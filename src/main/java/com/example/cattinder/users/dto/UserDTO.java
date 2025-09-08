package com.example.cattinder.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Email(message = "El email debe ser válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    // Nuevo campo para activar/desactivar usuario
    private Boolean active;

    // Constructores
    public UserDTO() {}

    public UserDTO(String name, String email, Boolean active) {
        this.name = name;
        this.email = email;
        this.active = active;
    }

    // Getters y setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
