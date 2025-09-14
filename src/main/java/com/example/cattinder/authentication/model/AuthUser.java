package com.example.cattinder.authentication.model;

public class AuthUser {

    private Long id;
    private String email;
    private String password; // encriptado
    private String role;
    private boolean active;

    // Constructor vacío (necesario para Spring y serialización)
    public AuthUser() {}

    // Constructor completo
    public AuthUser(Long id, String email, String password, String role, boolean active) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.active = active;
    }

    // Getters y setters manuales
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

