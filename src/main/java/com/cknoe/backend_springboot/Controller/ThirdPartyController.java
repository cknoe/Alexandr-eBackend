package com.cknoe.backend_springboot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cknoe.backend_springboot.service.external.LogoDevService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class ThirdPartyController {

    private final LogoDevService logoDevService;

    public ThirdPartyController(LogoDevService logoDevService) {
        this.logoDevService = logoDevService;
    }

    @GetMapping("/logodev")
    public Mono<ResponseEntity<byte[]>> getLogo(@RequestParam String domain) {
        return logoDevService.fetchLogo(domain);
    }
}
