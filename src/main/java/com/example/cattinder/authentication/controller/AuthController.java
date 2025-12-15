package com.example.cattinder.authentication.controller;

import com.example.cattinder.authentication.dto.LoginRequest;
import com.example.cattinder.authentication.dto.LoginResponse;
import com.example.cattinder.authentication.dto.RefreshTokenRequest;
import com.example.cattinder.authentication.dto.RefreshTokenResponse;
import com.example.cattinder.authentication.dto.RegisterRequest;
import com.example.cattinder.authentication.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "APIs for user authentication and token management")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with email and password"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User registered successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "\"User registered successfully\"")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"error\": \"Validation failed\", \"message\": {\"email\": \"Email should be valid\", \"password\": \"Password must be at least 6 characters long\"}}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User already exists",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"error\": \"Authentication failed\", \"message\": \"User already exists with email: juan@example.com\"}"
                            )
                    )
            )
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User registration details",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RegisterRequest.class),
                            examples = @ExampleObject(
                                    value = "{\"name\": \"Juan Pérez\", \"email\": \"juan@example.com\", \"password\": \"password123\"}"
                            )
                    )
            )
            @Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @Operation(
            summary = "User login",
            description = "Authenticates user and returns JWT tokens for authorization"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"accessToken\": \"eyJhbGciOiJIUzI1NiJ9...\", \"refreshToken\": \"eyJhbGciOiJIUzI1NiJ9...\", \"message\": \"Login successful\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"error\": \"Authentication failed\", \"message\": \"Invalid email or password\"}"
                            )
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User login credentials",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(
                                    value = "{\"email\": \"juan@example.com\", \"password\": \"password123\"}"
                            )
                    )
            )
            @Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token using a valid refresh token"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token refreshed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RefreshTokenResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"accessToken\": \"eyJhbGciOiJIUzI1NiJ9...\", \"refreshToken\": \"eyJhbGciOiJIUzI1NiJ9...\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid refresh token",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"error\": \"Token validation failed\", \"message\": \"Invalid refresh token\"}"
                            )
                    )
            )
    })
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Refresh token details",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RefreshTokenRequest.class),
                            examples = @ExampleObject(
                                    value = "{\"refreshToken\": \"eyJhbGciOiJIUzI1NiJ9...\"}"
                            )
                    )
            )
            @Valid @RequestBody RefreshTokenRequest request) {
        RefreshTokenResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

}