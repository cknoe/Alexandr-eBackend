package com.cknoe.backend_springboot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cknoe.backend_springboot.dto.OpenGraphResponseDTO;
import com.cknoe.backend_springboot.service.external.LogoDevService;
import com.cknoe.backend_springboot.service.external.OpenGraphService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class ThirdPartyController {

    private final LogoDevService logoDevService;
    private final OpenGraphService openGraphService;

    public ThirdPartyController(LogoDevService logoDevService, OpenGraphService openGraphService) {
        this.logoDevService = logoDevService;
        this.openGraphService = openGraphService;
    }

    @GetMapping("/logodev")
    public Mono<ResponseEntity<byte[]>> getLogo(@RequestParam String domain) {
        return logoDevService.fetchLogo(domain);
    }

    @GetMapping("/opengraph")
    public OpenGraphResponseDTO getOpenGraph(@RequestParam String url) {
        return openGraphService.fetch(url);
    }
}
