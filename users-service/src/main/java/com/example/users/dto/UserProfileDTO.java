package com.example.users.dto;

public class UserProfileDTO {

    private String name;
    private String email;
    private Boolean active;

    private String bio;
    private String career;

    private String instagramUrl;
    private String linkedinUrl;
    private String xUrl;

    private Boolean visible;

    // Constructores
    public UserProfileDTO() {}

    public UserProfileDTO(String name, String email, Boolean active, String bio,
                          String career, String instagramUrl, String linkedinUrl,
                          String xUrl, Boolean visible) {
        this.name = name;
        this.email = email;
        this.active = active;
        this.bio = bio;
        this.career = career;
        this.instagramUrl = instagramUrl;
        this.linkedinUrl = linkedinUrl;
        this.xUrl = xUrl;
        this.visible = visible;
    }

    // Getters y setters
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
}
