package com.cknoe.backend_springboot.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cknoe.backend_springboot.dto.UserChangeDTO;
import com.cknoe.backend_springboot.dto.UserDTO;
import com.cknoe.backend_springboot.entity.AppUser;
import com.cknoe.backend_springboot.exception.UserNotFoundException;
import com.cknoe.backend_springboot.repository.AppUserRepository;
import com.cknoe.backend_springboot.security.jwt.JwtUtil;

@RestController
public class AppUserController {

    private final AppUserRepository appUserRepository;
    private final JwtUtil jwtUtil;

    public AppUserController(AppUserRepository appUserRepository, JwtUtil jwtUtil) {
        this.appUserRepository = appUserRepository;
        this.jwtUtil = jwtUtil;
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
    UserChangeDTO replaceUser(@RequestBody UserDTO newUserDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        AppUser newUser = UserDTO.toEntity(newUserDTO);
        AppUser updatedUser = appUserRepository.findByUsername(userDetails.getUsername())
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    return appUserRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));
        ;
        String newJwt = jwtUtil.generateToken(updatedUser.getUsername());
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
