package com.example.cattinder.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class RefreshTokenRequest {
    @NotBlank(message = "Refresh token is required")

    @NotBlank(message = "Refresh token is required")
    @Schema(description = "Refresh token obtained from login", example = "eyJhbGciOiJIUzI1NiJ9...", required = true)
    private String refreshToken;


    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}