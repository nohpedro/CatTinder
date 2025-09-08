package com.example.cattinder.users.preference.model;

public class Preference {
    private Long id;
    private Long userId; // Relación con usuario
    private String subject;
    private String studyStyle;
    private String availability;

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
