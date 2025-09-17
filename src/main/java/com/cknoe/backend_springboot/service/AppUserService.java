package com.cknoe.backend_springboot.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cknoe.backend_springboot.dto.UserInputDTO;
import com.cknoe.backend_springboot.entity.AppUser;
import com.cknoe.backend_springboot.exception.UserNotFoundException;
import com.cknoe.backend_springboot.repository.AppUserRepository;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser getCurrentUser(String username) {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public AppUser getUserById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public AppUser updateCurrentUser(String username, UserInputDTO dto) {
        return appUserRepository.findByUsername(username)
                .map(user -> {
                    user.setUsername(dto.username());
                    if (!dto.password().isEmpty()) {
                        user.setPassword(passwordEncoder.encode(dto.password()));
                    }
                    return appUserRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public void deleteCurrentUser(String username) {
        AppUser user = getCurrentUser(username);
        appUserRepository.deleteById(user.getId());
    }
}
