package com.cknoe.backend_springboot.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cknoe.backend_springboot.dto.RegisterRequestDTO;
import com.cknoe.backend_springboot.dto.UserChangeDTO;
import com.cknoe.backend_springboot.dto.UserDTO;
import com.cknoe.backend_springboot.entity.AppUser;
import com.cknoe.backend_springboot.exception.UserNotFoundException;
import com.cknoe.backend_springboot.repository.AppUserRepository;
import com.cknoe.backend_springboot.security.config.SecurityConfig;
import com.cknoe.backend_springboot.security.jwt.JwtUtil;
import com.cknoe.backend_springboot.service.AppUserDetailsService;

import jakarta.validation.Valid;

@RestController
public class AppUserController {

    private final AppUserRepository appUserRepository;
    private final AppUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AppUserController(AppUserRepository appUserRepository, JwtUtil jwtUtil,
            AppUserDetailsService userDetailsService, SecurityConfig securityConfig) {
        this.appUserRepository = appUserRepository;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = securityConfig.passwordEncoder();
    }

    @GetMapping("/users")
    UserDTO getMe(@AuthenticationPrincipal UserDetails userDetails) {
        AppUser user = appUserRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));
        return UserDTO.fromEntity(user);
    }

    @GetMapping("/users/{id}")
    UserDTO getUserById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        AppUser user = appUserRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return UserDTO.fromEntity(user);
    }

    @PutMapping("/users")
    UserChangeDTO replaceUser(@Valid @RequestBody RegisterRequestDTO registerRequestDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        AppUser newUser = RegisterRequestDTO.toEntity(registerRequestDTO);
        AppUser updatedUser = appUserRepository.findByUsername(userDetails.getUsername())
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    if (!newUser.getPassword().equals("")) {
                        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
                    }

                    return appUserRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));
        ;
        String newJwt = jwtUtil.generateToken(userDetailsService.loadUserByUsername(updatedUser.getUsername()));
        UserDTO userDTO = UserDTO.fromEntity(updatedUser);
        return new UserChangeDTO(userDTO, newJwt);
    }

    @DeleteMapping("/users")
    void deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        appUserRepository.deleteById(user.getId());
    }

    /*
     * TO-DO :
     * Create admin controller(s?)
     */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("admin/users")
    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("admin/users/{id}")
    void deleteUserById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        appUserRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("admin/users/{id}")
    UserDTO replaceUserById(@RequestBody UserDTO newUserDTO, @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        AppUser newUser = UserDTO.toEntity(newUserDTO);
        AppUser updatedUser = appUserRepository.findById(id)
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    user.setRole(newUser.getRole());
                    return appUserRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(id));
        ;
        return UserDTO.fromEntity(updatedUser);
    }
}
