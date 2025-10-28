package com.example.confgms.dominio.entidad;


import jakarta.persistence.*;

@Entity
@Table(name = "config")
public class confEntity {

    @Id
    @Column(name = "userid", nullable = false, length = 100)
    private String userid;

    @Column(nullable = false)
    private boolean darkmode;

    @Column(nullable = false)
    private boolean showInfo;

    @Column(nullable = false)
    private boolean showOnlineStatus;

    @Column(nullable = false)
    private boolean showNotifications;

    public confEntity() {}

    public confEntity(String userid, boolean darkmode, boolean showInfo,
                        boolean showOnlineStatus, boolean showNotifications) {
        this.userid = userid;
        this.darkmode = darkmode;
        this.showInfo = showInfo;
        this.showOnlineStatus = showOnlineStatus;
        this.showNotifications = showNotifications;
    }

    // Getters/Setters
    public String getUserid() { return userid; }
    public void setUserid(String userid) { this.userid = userid; }

    public boolean isDarkmode() { return darkmode; }
    public void setDarkmode(boolean darkmode) { this.darkmode = darkmode; }

    public boolean isShowInfo() { return showInfo; }
    public void setShowInfo(boolean showInfo) { this.showInfo = showInfo; }

    public boolean isShowOnlineStatus() { return showOnlineStatus; }
    public void setShowOnlineStatus(boolean showOnlineStatus) { this.showOnlineStatus = showOnlineStatus; }

    public boolean isShowNotifications() { return showNotifications; }
    public void setShowNotifications(boolean showNotifications) { this.showNotifications = showNotifications; }
}
