package com.example.confgms.dto;

public class configDto {
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
        this.darkmode = darkmode;
        this.showInfo = showInfo;
        this.showOnlineStatus = showOnlineStatus;
        this.showNotifications = showNotifications;
        this.userid = userid;
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
    public void setShowInfo(Boolean showInfo) { this.showInfo = showInfo; }
    public void setShowOnlineStatus(Boolean showOnlineStatus) { this.showOnlineStatus = showOnlineStatus; }
    public void setDarkmode(Boolean Darkmode) { this.darkmode=Darkmode; }
    public void setShowNotifications(Boolean showNotifications) { this.showNotifications=showNotifications; }
}
