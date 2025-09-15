package com.cknoe.backend_springboot.dto;

import com.cknoe.backend_springboot.entity.AppUser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterRequestDTO(
        @NotBlank @Pattern(regexp = "^[A-Za-z0-9_]{1,30}$", message = "Username must be 1 to 30 characters : letters, numbers or underscore only") String username,
        @NotNull String password) {

    public static AppUser toEntity(RegisterRequestDTO registerRequestDTO) {
        AppUser user = new AppUser();
        user.setUsername(registerRequestDTO.username());
        user.setPassword(registerRequestDTO.password());
        user.setRole("USER");
        return user;
    }
}