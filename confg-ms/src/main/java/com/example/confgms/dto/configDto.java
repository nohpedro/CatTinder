package com.example.confgms.dto;
import jakarta.validation.constraints.NotBlank;
public class configDto {
    @NotBlank(message = "userid es requerido")
    private String userid;
    private boolean darkmode;
    private boolean showInfo;
    private boolean showOnlineStatus;
    private boolean showNotifications;

    public configDto(String userid,
                     boolean darkmode,
                     boolean showInfo,
                     boolean showOnlineStatus,
                     boolean showNotifications) {
        this.userid = userid;
        this.darkmode = darkmode;
        this.showInfo = showInfo;
        this.showOnlineStatus = showOnlineStatus;
        this.showNotifications = showNotifications;

    }

    public String getUserid() {
        return userid;
    }

    //Getters
    public Boolean getDarkmode() {return darkmode;}
    public Boolean getShowInfo() { return showInfo; }
    public Boolean getShowOnlineStatus() { return showOnlineStatus; }
    public Boolean getShowNotifications() { return showNotifications; }
    //Setters
    public void setUserid(String userid) { this.userid = userid; }
    public void setShowInfo(Boolean showInfo) { this.showInfo = showInfo; }
    public void setShowOnlineStatus(Boolean showOnlineStatus) { this.showOnlineStatus = showOnlineStatus; }
    public void setDarkmode(Boolean Darkmode) { this.darkmode=Darkmode; }
    public void setShowNotifications(Boolean showNotifications) { this.showNotifications=showNotifications; }
}
