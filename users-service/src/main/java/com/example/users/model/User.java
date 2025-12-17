package com.example.users.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String name;

    @Email(message = "El email debe ser válido")
    @NotBlank(message = "El email es obligatorio")
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull(message = "El estado activo es obligatorio")
    @Column(nullable = false)
    private Boolean active;

    // NUEVOS CAMPOS OPCIONALES
    @Column(length = 500)
    private String bio;

    @Column
    private String career;

    @Column
    private String instagramUrl;

    @Column
    private String linkedinUrl;

    @Column
    private String xUrl;

    @Column(nullable = false)
    private Boolean visible = true;

    // Constructores
    public User() {}

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.active = true;
    }

    public User(Long id, String name, String email, Boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.active = active;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getCareer() { return career; }
    public void setCareer(String career) { this.career = career; }

    public String getInstagramUrl() { return instagramUrl; }
    public void setInstagramUrl(String instagramUrl) { this.instagramUrl = instagramUrl; }

    public String getLinkedinUrl() { return linkedinUrl; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }

    public String getXUrl() { return xUrl; }
    public void setXUrl(String xUrl) { this.xUrl = xUrl; }

    public Boolean getVisible() { return visible; }
    public void setVisible(Boolean visible) { this.visible = visible; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                ", bio='" + bio + '\'' +
                ", career='" + career + '\'' +
                ", instagramUrl='" + instagramUrl + '\'' +
                ", linkedinUrl='" + linkedinUrl + '\'' +
                ", xUrl='" + xUrl + '\'' +
                ", visible=" + visible +
                '}';
    }
}
