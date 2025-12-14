package com.example.confgms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "Config",
        description = "Preferencias de visualización y notificaciones del usuario",
        example = "{\"userid\":\"u123\",\"darkmode\":true,\"showInfo\":true,\"showOnlineStatus\":false,\"showNotifications\":true}"
)
@JsonPropertyOrder({"userid","darkmode","showInfo","showOnlineStatus","showNotifications"})
public class configDto {

    @Schema(description = "Identificador del usuario propietario de la configuración", example = "u123")
    @JsonProperty("userid")
    @NotBlank(message = "userid es requerido")
    private String userid;

    @Schema(description = "Tema oscuro habilitado", example = "true")
    @JsonProperty("darkmode")
    private boolean darkmode;

    @Schema(description = "Mostrar información adicional del perfil", example = "true")
    @JsonProperty("showInfo")
    private boolean showInfo;

    @Schema(description = "Mostrar estado en línea", example = "false")
    @JsonProperty("showOnlineStatus")
    private boolean showOnlineStatus;

    @Schema(description = "Mostrar notificaciones", example = "true")
    @JsonProperty("showNotifications")
    private boolean showNotifications;

    // Constructor por defecto (como en MatchDto)
    public configDto() {}

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

    // Getters (mismos nombres/firmas que pasaste)
    public Boolean getDarkmode() { return darkmode; }
    public Boolean getShowInfo() { return showInfo; }
    public Boolean getShowOnlineStatus() { return showOnlineStatus; }
    public Boolean getShowNotifications() { return showNotifications; }

    // Setters (mismos nombres/firmas que pasaste)
    public void setUserid(String userid) { this.userid = userid; }
    public void setShowInfo(Boolean showInfo) { this.showInfo = showInfo; }
    public void setShowOnlineStatus(Boolean showOnlineStatus) { this.showOnlineStatus = showOnlineStatus; }
    public void setDarkmode(Boolean Darkmode) { this.darkmode = Darkmode; }
    public void setShowNotifications(Boolean showNotifications) { this.showNotifications = showNotifications; }
}
