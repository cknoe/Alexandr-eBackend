package com.cknoe.backend_springboot.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cknoe.backend_springboot.dto.UserRegisterDTO;
import com.cknoe.backend_springboot.response.AuthResponse;
import com.cknoe.backend_springboot.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody UserRegisterDTO request,
            HttpServletResponse response) {
        AuthResponse tokens = authService.login(request.username(), request.password());
        response.addHeader(HttpHeaders.SET_COOKIE, getResponseCookie(tokens.refreshToken()).toString());
        return ResponseEntity.ok(new AuthResponse(tokens.token(), null));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRegisterDTO input,
            HttpServletResponse response) {
        AuthResponse tokens = authService.register(input);
        response.addHeader(HttpHeaders.SET_COOKIE, getResponseCookie(tokens.refreshToken()).toString());
        return ResponseEntity.ok(new AuthResponse(tokens.token(), null));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response) {
        AuthResponse tokens = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(new AuthResponse(tokens.token(), null));
    }

    private ResponseCookie getResponseCookie(String token) {
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/refresh-token")
                .maxAge(7 * 24 * 60 * 60)
                .build();
    }
}