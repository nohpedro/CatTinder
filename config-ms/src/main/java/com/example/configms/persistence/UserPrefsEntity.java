package com.example.configms.persistence;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "user_prefs", indexes = {
    @Index(name = "idx_user_prefs_user_id", columnList = "userId", unique = true)
})
public class UserPrefsEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 64)
  private String userId;

  @Column
  private Boolean darkMode;

  @Column
  private Boolean showOnlineStatus;

  @Column
  private Boolean pushNotifications;

  @Column
  private Boolean showInfo;

  @Column(nullable = false)
  private Instant updatedAt;

  @PrePersist @PreUpdate
  void touch() { this.updatedAt = Instant.now(); }

  // getters/setters
  public Long getId() { return id; } public void setId(Long id) { this.id = id; }
  public String getUserId() { return userId; } public void setUserId(String userId) { this.userId = userId; }
  public Boolean getDarkMode() { return darkMode; } public void setDarkMode(Boolean darkMode) { this.darkMode = darkMode; }
  public Boolean getShowOnlineStatus() { return showOnlineStatus; } public void setShowOnlineStatus(Boolean showOnlineStatus) { this.showOnlineStatus = showOnlineStatus; }
  public Boolean getPushNotifications() { return pushNotifications; } public void setPushNotifications(Boolean pushNotifications) { this.pushNotifications = pushNotifications; }
  public Boolean getShowInfo() { return showInfo; } public void setShowInfo(Boolean showInfo) { this.showInfo = showInfo; }
  public Instant getUpdatedAt() { return updatedAt; } public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
