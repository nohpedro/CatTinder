package com.example.authentication.controller;

import com.example.authentication.dto.RegisterRequest;
import com.example.authentication.dto.AuthUserInfo;
import com.example.authentication.entity.User;
import com.example.authentication.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        if (userService.userExists(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        if (userService.emailExists(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }

        // Convert RegisterRequest to User entity
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());

        User registeredUser = userService.registerUser(user);
        
        // Convert back to RegisterRequest for response (without password)
        RegisterRequest response = new RegisterRequest(
            registeredUser.getId(),
            registeredUser.getUsername(),
            registeredUser.getEmail(),
            registeredUser.getCreatedAt(),
            registeredUser.getUpdatedAt()
        );

        return ResponseEntity.ok(response);
    }


    @GetMapping("/user/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        RegisterRequest userResponse = new RegisterRequest(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );

        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user info from Keycloak token")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return ResponseEntity.badRequest().body("No authentication token found");
        }

        // Extraer información del token JWT de Keycloak
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");
        String name = jwt.getClaimAsString("name");

        // Buscar usuario en la base de datos local si existe
        Optional<User> userOptional = userService.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            RegisterRequest userResponse = new RegisterRequest(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
            );
            return ResponseEntity.ok(userResponse);
        } else {
            // Usuario autenticado en Keycloak pero no registrado localmente
            return ResponseEntity.ok(new AuthUserInfo(username, email, name));
        }
    }
}