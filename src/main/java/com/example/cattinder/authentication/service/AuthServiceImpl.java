package com.example.cattinder.authentication.service;

import com.example.cattinder.authentication.dto.LoginRequest;
import com.example.cattinder.authentication.dto.LoginResponse;
import com.example.cattinder.authentication.dto.RefreshTokenRequest;
import com.example.cattinder.authentication.dto.RefreshTokenResponse;
import com.example.cattinder.authentication.dto.RegisterRequest;
import com.example.cattinder.authentication.exception.InvalidCredentialsException;
import com.example.cattinder.authentication.exception.TokenValidationException;
import com.example.cattinder.authentication.model.AuthUser;
import com.example.cattinder.authentication.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthServiceImpl implements AuthService {

    private final ConcurrentHashMap<String, String> refreshTokens = new ConcurrentHashMap<>();

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil,
                           UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidCredentialsException("User already exists with email: " + request.getEmail());
        }

        AuthUser user = new AuthUser(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getName()
        );

        userRepository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            AuthUser user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new InvalidCredentialsException("User not found"));

            String accessToken = jwtUtil.generateToken(user.getEmail());
            String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

            refreshTokens.put(user.getEmail(), refreshToken);

            return new LoginResponse(accessToken, refreshToken, "Login successful");
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        try {
            String email = jwtUtil.extractUsername(request.getRefreshToken());

            if (!jwtUtil.validateToken(request.getRefreshToken(), email) ||
                    !refreshTokens.containsKey(email) ||
                    !refreshTokens.get(email).equals(request.getRefreshToken())) {
                throw new TokenValidationException("Invalid refresh token");
            }

            String newAccessToken = jwtUtil.generateToken(email);
            String newRefreshToken = jwtUtil.generateRefreshToken(email);

            refreshTokens.put(email, newRefreshToken);

            return new RefreshTokenResponse(newAccessToken, newRefreshToken);
        } catch (Exception e) {
            throw new TokenValidationException("Failed to refresh token: " + e.getMessage());
        }
    }
}