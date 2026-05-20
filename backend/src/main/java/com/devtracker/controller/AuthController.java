package com.devtracker.controller;

import com.devtracker.dto.request.LoginRequest;
import com.devtracker.dto.request.RegisterRequest;
import com.devtracker.dto.response.TokenResponse;
import com.devtracker.entity.User;
import com.devtracker.exception.ResourceNotFoundException;
import com.devtracker.repository.UserRepository;
import com.devtracker.security.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${app.security.cookie-secure:false}")
    private boolean cookieSecure;

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm missing", e);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletResponse response
    ) {
        log.info("Processing user registration for: {}", request.email());

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("An account with this email address already exists.");
        }

        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .displayName(request.displayName())
                .build();

        User savedUser = userRepository.save(user);
        
        String accessToken = jwtService.generateAccessToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);

        // Secure Refresh Token Rotation setup
        savedUser.setRefreshTokenHash(hashToken(refreshToken));
        userRepository.save(savedUser);

        // Issue Refresh Token in secure HTTP-Only Cookie
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/api/auth")
                .maxAge(7 * 24 * 60 * 60) // 7 days
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new ResponseEntity<>(new TokenResponse(accessToken), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        log.info("Processing login request for: {}", request.email());

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Store new rotated refresh token hash
        user.setRefreshTokenHash(hashToken(refreshToken));
        userRepository.save(user);

        // Issue Refresh Token in secure HTTP-Only Cookie
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/api/auth")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(new TokenResponse(accessToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(
            @CookieValue(name = "refreshToken", required = false) String incomingToken,
            HttpServletResponse response
    ) {
        log.info("Processing token refresh request");

        if (incomingToken == null) {
            throw new IllegalArgumentException("Missing refresh token cookie.");
        }

        String email = jwtService.extractEmail(incomingToken);
        if (email == null || jwtService.isTokenExpired(incomingToken)) {
            throw new IllegalArgumentException("Invalid or expired refresh token.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User associated with token not found."));

        // Validate refresh token hash for secure rotation checks (mitigates theft replay attacks)
        String incomingHash = hashToken(incomingToken);
        if (user.getRefreshTokenHash() == null || !user.getRefreshTokenHash().equals(incomingHash)) {
            log.warn("Replay attack detected or token invalidated! Invalidating all active tokens for user: {}", email);
            user.setRefreshTokenHash(null);
            userRepository.save(user);

            // Clear the obsolete cookie on replay attack detection
            ResponseCookie clearCookie = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true)
                    .secure(cookieSecure)
                    .path("/api/auth")
                    .maxAge(0)
                    .sameSite("Strict")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, clearCookie.toString());

            throw new IllegalArgumentException("Replay attack or obsolete token detected. Please login again.");
        }

        // Generate rotated pair
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        user.setRefreshTokenHash(hashToken(newRefreshToken));
        userRepository.save(user);

        // Issue new rotated Refresh Token in secure Cookie
        ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/api/auth")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(new TokenResponse(newAccessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletResponse response
    ) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            try {
                String email = jwtService.extractEmail(jwt);
                if (email != null) {
                    User user = userRepository.findByEmail(email).orElse(null);
                    if (user != null) {
                        log.info("Invalidating server-side refresh token for user: {}", email);
                        user.setRefreshTokenHash(null);
                        userRepository.save(user);
                    }
                }
            } catch (Exception e) {
                log.debug("Logout token extraction parsed but failed or expired: {}", e.getMessage());
            }
        }

        // Invalidate and delete refresh token cookie from the browser
        ResponseCookie clearCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/api/auth")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, clearCookie.toString());

        return ResponseEntity.noContent().build();
    }
}
