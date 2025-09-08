package com.example.swipeservice.docs.schemas;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SwipeCreatedResponse",
        description = "Respuesta al registrar un swipe; incluye matchId si hubo reciprocidad")
public class SwipeCreatedResponse {

    @Schema(example = "¡Hay match!", description = "Mensaje de estado")
    private String mensaje;

    @Schema(example = "f4f2a1b0-9c8d-7e6f-5a43-210fedcba987",
            description = "Id del match si se generó; null si no hubo match",
            nullable = true)
    private String matchId;

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getMatchId() { return matchId; }
    public void setMatchId(String matchId) { this.matchId = matchId; }
}
