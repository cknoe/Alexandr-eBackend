package com.cknoe.backend_springboot.dto;

import com.cknoe.backend_springboot.entity.AppUser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequestDTO(
        @NotBlank String username,
        @NotNull String password) {

    public static AppUser toEntity(RegisterRequestDTO registerRequestDTO) {
        AppUser user = new AppUser();
        user.setUsername(registerRequestDTO.username());
        user.setPassword(registerRequestDTO.password());
        user.setRole("USER");
        return user;
    }
}