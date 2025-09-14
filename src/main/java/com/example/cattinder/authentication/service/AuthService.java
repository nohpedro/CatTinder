package com.example.cattinder.authentication.service;

import com.example.cattinder.authentication.dto.LoginRequest;
import com.example.cattinder.authentication.dto.LoginResponse;
import com.example.cattinder.authentication.dto.RefreshTokenRequest;
import com.example.cattinder.authentication.dto.RefreshTokenResponse;
import com.example.cattinder.authentication.dto.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    RefreshTokenResponse refreshToken(RefreshTokenRequest request);
}