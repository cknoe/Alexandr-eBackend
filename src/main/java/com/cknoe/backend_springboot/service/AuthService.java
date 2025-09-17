package com.cknoe.backend_springboot.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cknoe.backend_springboot.dto.UserRegisterDTO;
import com.cknoe.backend_springboot.entity.AppUser;
import com.cknoe.backend_springboot.exception.InvalidRefreshTokenException;
import com.cknoe.backend_springboot.exception.UsernameAlreadyExistsException;
import com.cknoe.backend_springboot.repository.AppUserRepository;
import com.cknoe.backend_springboot.response.AuthResponse;
import com.cknoe.backend_springboot.security.jwt.JwtUtil;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final AppUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(AppUserRepository appUserRepository,
            AppUserDetailsService userDetailsService,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.appUserRepository = appUserRepository;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse login(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String jwt = jwtUtil.generateToken(userDetails);
        String jwtRefresh = jwtUtil.generateRefreshToken(userDetails);
        return new AuthResponse(jwt, jwtRefresh);
    }

    public AuthResponse register(UserRegisterDTO input) {
        if (appUserRepository.findByUsername(input.username()).isPresent()) {
            throw new UsernameAlreadyExistsException(input.username());
        }
        AppUser user = new AppUser();
        user.setUsername(input.username());
        user.setPassword(passwordEncoder.encode(input.password()));
        user.setRole("USER");
        appUserRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(input.username());
        return new AuthResponse(jwtUtil.generateToken(userDetails),
                jwtUtil.generateRefreshToken(userDetails));
    }

    public AuthResponse refreshToken(String refreshToken) {
        System.out.println("refreshToken = " + refreshToken);
        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }
        UserDetails userDetails = userDetailsService
                .loadUserByUsername(jwtUtil.extractUsernameFromRefresh(refreshToken));
        return new AuthResponse(jwtUtil.generateToken(userDetails), null);
    }
}
