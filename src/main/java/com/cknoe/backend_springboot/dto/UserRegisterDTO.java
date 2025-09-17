package com.cknoe.backend_springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRegisterDTO(
        @NotBlank @Pattern(regexp = "^[A-Za-z0-9_]{1,30}$", message = "Username must be 1 to 30 characters : letters, numbers or underscore only") String username,
        @NotBlank String password) {
}