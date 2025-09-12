package com.cknoe.backend_springboot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cknoe.backend_springboot.dto.RegisterRequestDTO;
import com.cknoe.backend_springboot.entity.AppUser;
import com.cknoe.backend_springboot.repository.AppUserRepository;
import com.cknoe.backend_springboot.security.config.SecurityConfig;
import com.cknoe.backend_springboot.security.jwt.JwtUtil;
import com.cknoe.backend_springboot.service.AppUserDetailsService;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
            AppUserDetailsService userDetailsService,
            JwtUtil jwtUtil,
            AppUserRepository appUserRepository,
            SecurityConfig securityConfig) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = securityConfig.passwordEncoder();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

        String jwt = jwtUtil.generateToken(userDetails);
        String jwtRefresh = jwtUtil.generateRefreshToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt, jwtRefresh, null));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequestDTO request) {

        if (appUserRepository.findByUsername(request.username()).isPresent()) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, null, "Username already exists"));
        }

        AppUser user = new AppUser();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole("USER");

        appUserRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

        String jwt = jwtUtil.generateToken(userDetails);
        String jwtRefresh = jwtUtil.generateRefreshToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(jwt, jwtRefresh, null));
    }

    @PostMapping("refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@CookieValue("refreshToken") String refreshToken) {
        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, "Invalid refresh token"));
        }

        UserDetails userDetails = userDetailsService
                .loadUserByUsername(jwtUtil.extractUsernameFromRefresh(refreshToken));

        String newToken = jwtUtil.generateToken(userDetails);
        String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(newToken, newRefreshToken, null));
    }
}

record AuthRequest(String username, String password) {
}

record AuthResponse(String token, String refreshToken, String error) {
}

record ErrorResponse(String error) {
}