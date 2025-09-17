package com.cknoe.backend_springboot.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.cknoe.backend_springboot.dto.userDTOs.UserChangeDTO;
import com.cknoe.backend_springboot.dto.userDTOs.UserDTO;
import com.cknoe.backend_springboot.dto.userDTOs.UserInputDTO;
import com.cknoe.backend_springboot.entity.AppUser;
import com.cknoe.backend_springboot.security.jwt.JwtUtil;
import com.cknoe.backend_springboot.service.AppUserDetailsService;
import com.cknoe.backend_springboot.service.AppUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class AppUserController {

    private final AppUserService appUserService;
    private final AppUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public AppUserController(AppUserService appUserService,
            AppUserDetailsService userDetailsService,
            JwtUtil jwtUtil) {
        this.appUserService = appUserService;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public UserDTO getMe(@AuthenticationPrincipal UserDetails userDetails) {
        return UserDTO.fromEntity(appUserService.getCurrentUser(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return UserDTO.fromEntity(appUserService.getUserById(id));
    }

    @PutMapping
    public UserChangeDTO replaceUser(@Valid @RequestBody UserInputDTO userInputDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        AppUser updatedUser = appUserService.updateCurrentUser(userDetails.getUsername(), userInputDTO);

        String newJwt = jwtUtil.generateToken(
                userDetailsService.loadUserByUsername(updatedUser.getUsername()));

        return new UserChangeDTO(UserDTO.fromEntity(updatedUser), newJwt);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        appUserService.deleteCurrentUser(userDetails.getUsername());
    }
}
