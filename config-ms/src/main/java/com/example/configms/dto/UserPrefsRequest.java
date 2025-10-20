package com.example.configms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserPrefsRequest {

  @NotBlank
  @Size(max = 64)
  private String userId;

  private Boolean darkMode;
  private Boolean showOnlineStatus;
  private Boolean pushNotifications;
  private Boolean showInfo;

  public UserPrefsRequest() {}

  public String getUserId() { return userId; }
  public void setUserId(String userId) { this.userId = userId; }

  public Boolean getDarkMode() { return darkMode; }
  public void setDarkMode(Boolean darkMode) { this.darkMode = darkMode; }

  public Boolean getShowOnlineStatus() { return showOnlineStatus; }
  public void setShowOnlineStatus(Boolean showOnlineStatus) { this.showOnlineStatus = showOnlineStatus; }

  public Boolean getPushNotifications() { return pushNotifications; }
  public void setPushNotifications(Boolean pushNotifications) { this.pushNotifications = pushNotifications; }

  public Boolean getShowInfo() { return showInfo; }
  public void setShowInfo(Boolean showInfo) { this.showInfo = showInfo; }
}
