package com.cknoe.backend_springboot.dto.userDTOs;

import com.cknoe.backend_springboot.entity.AppUser;

public record UserDTO(
        Long id,
        String username,
        String role) {

    public static UserDTO fromEntity(AppUser user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getRole());
    }

    public static AppUser toEntity(UserDTO userDTO) {
        AppUser user = new AppUser();
        user.setUsername(userDTO.username());
        user.setRole(userDTO.role());
        return user;
    }
}
