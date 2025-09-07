package com.example.swipeservice.dto;

public class SwipeResponse {
    private String mensaje;
    private String matchId;

    public SwipeResponse(String mensaje, String matchId) {
        this.mensaje = mensaje;
        this.matchId = matchId;
    }
    public String getMensaje() { return mensaje; }
    public String getMatchId() { return matchId; }
}
