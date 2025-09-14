package com.example.cattinder.authentication.service;

import com.example.cattinder.authentication.dto.LoginRequest;
import com.example.cattinder.authentication.dto.LoginResponse;
import com.example.cattinder.authentication.dto.RefreshTokenRequest;
import com.example.cattinder.authentication.dto.RefreshTokenResponse;
import com.example.cattinder.authentication.dto.RegisterRequest;
import com.example.cattinder.authentication.exception.InvalidCredentialsException;
import com.example.cattinder.authentication.exception.TokenValidationException;
import com.example.cattinder.authentication.model.AuthUser;
import com.example.cattinder.authentication.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final List<AuthUser> users = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong(1);

    public AuthServiceImpl(JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;

        // Usuario de prueba
        users.add(new AuthUser(counter.getAndIncrement(),
                "admin@cattinder.com",
                passwordEncoder.encode("123456"),
                "ADMIN",
                true));
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        AuthUser user = users.stream()
                .filter(u -> u.getEmail().equals(request.getEmail()))
                .findFirst()
                .orElseThrow(() -> new InvalidCredentialsException("Usuario o contraseña inválidos"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Usuario o contraseña inválidos");
        }

        String accessToken = jwtUtil.generateToken(user.getEmail());
        String refreshToken = jwtUtil.generateToken(user.getEmail());
        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String email;
        try {
            email = jwtUtil.extractEmail(request.getRefreshToken());
        } catch (Exception e) {
            throw new TokenValidationException("Refresh token inválido");
        }

        if (!jwtUtil.validateToken(request.getRefreshToken(), email)) {
            throw new TokenValidationException("Refresh token inválido o expirado");
        }

        String accessToken = jwtUtil.generateToken(email);
        return new RefreshTokenResponse(accessToken);
    }

    @Override
    public LoginResponse register(RegisterRequest request) {
        boolean exists = users.stream().anyMatch(u -> u.getEmail().equals(request.getEmail()));
        if (exists) {
            throw new InvalidCredentialsException("El usuario ya existe");
        }

        AuthUser user = new AuthUser(counter.getAndIncrement(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getRole(),
                true);
        users.add(user);

        String accessToken = jwtUtil.generateToken(user.getEmail());
        String refreshToken = jwtUtil.generateToken(user.getEmail());
        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    public void logout(String email) {
        // Logout simbólico
        System.out.println("Usuario " + email + " ha cerrado sesión (logout simbólico).");
    }
}
