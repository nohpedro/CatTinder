package com.example.configms.model;

import java.time.Instant;

public class UserPrefs {
  private String userId;
  private Boolean darkMode;
  private Boolean showOnlineStatus;
  private Boolean pushNotifications;
  private Boolean showInfo;
  private Instant updatedAt;

  public UserPrefs() { }

  public UserPrefs(String userId, Boolean darkMode, Boolean showOnlineStatus,
                   Boolean pushNotifications, Boolean showInfo, Instant updatedAt) {
    this.userId = userId;
    this.darkMode = darkMode;
    this.showOnlineStatus = showOnlineStatus;
    this.pushNotifications = pushNotifications;
    this.showInfo = showInfo;
    this.updatedAt = updatedAt;
  }

  public String getUserId() { 
    return userId; }

  public void setUserId(String userId) { 
    this.userId = userId; }

  public Boolean getDarkMode() { 
    return darkMode; }

  public void setDarkMode(Boolean darkMode) { 
    this.darkMode = darkMode; }

  public Boolean getShowOnlineStatus() { 
    return showOnlineStatus; }

  public void setShowOnlineStatus(Boolean showOnlineStatus) { 
    this.showOnlineStatus = showOnlineStatus; }

  public Boolean getPushNotifications() { 
    return pushNotifications; }

  public void setPushNotifications(Boolean pushNotifications) { 
    this.pushNotifications = pushNotifications; }

  public Boolean getShowInfo() { 
    return showInfo; }

  public void setShowInfo(Boolean showInfo) { 
    this.showInfo = showInfo; }

  public Instant getUpdatedAt() { 
    return updatedAt; }
    
  public void setUpdatedAt(Instant updatedAt) { 
    this.updatedAt = updatedAt; }
}