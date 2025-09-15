package com.cknoe.backend_springboot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ThirdPartyApi {

    private final WebClient webClient;

    public ThirdPartyApi(@Value("${LOGO_DEV_KEY}") String logoDevKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://img.logo.dev")
                .defaultUriVariables(Map.of("token", logoDevKey))
                .filter((request, next) -> {
                    System.out.println("Full URI: " + request.url());
                    return next.exchange(request);
                })
                .build();
    }

    @GetMapping("/logodev")
    public ResponseEntity<byte[]> getLogo(@RequestParam String domain) {
        return webClient.get()
                .uri("/{domain}?token={token}", Map.of("domain", domain))
                .exchangeToMono(response -> handleResponse(response))
                .block();
    }

    private Mono<ResponseEntity<byte[]>> handleResponse(ClientResponse response) {
        HttpStatusCode status = response.statusCode();

        if (status.value() == 200) {
            return response.bodyToMono(byte[].class)
                    .map(body -> {
                        if (body == null || body.length == 0) {
                            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                        }
                        return ResponseEntity.ok()
                                .contentType(response.headers().contentType().orElse(MediaType.IMAGE_JPEG))
                                .body(body);
                    });
        } else if (status.value() == 202) {
            return Mono.just(ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(("Auto-generated logo").getBytes()));
        } else if (status.is4xxClientError()) {
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(("Logodev error: " + status.value()).getBytes()));
        } else {
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(("Logodev server error: " + status.value()).getBytes()));
        }
    }
}
