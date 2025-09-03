package com.cknoe.backend_springboot.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cknoe.backend_springboot.entity.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}