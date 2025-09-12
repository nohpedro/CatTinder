package com.example.cattinder.authentication.controller;

import com.example.cattinder.authentication.dto.*;
import com.example.cattinder.authentication.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) { this.authService = authService; }

    @Operation(summary = "Login user")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        LoginResponse resp = authService.login(req);
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Register user (in-memory)")
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest req) {
        LoginResponse resp = authService.register(req);
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Refresh access token given refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest req) {
        RefreshTokenResponse r = authService.refreshToken(req);
        return ResponseEntity.ok(r);
    }

    @Operation(summary = "Logout (invalidate refresh token)")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody RefreshTokenRequest req) {
        authService.logout(req.getRefreshToken());
        return ResponseEntity.ok().build();
    }

    // Example protected endpoint
    @Operation(summary = "Protected ping")
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}
