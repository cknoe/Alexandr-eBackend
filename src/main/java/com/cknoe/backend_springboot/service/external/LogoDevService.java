package com.cknoe.backend_springboot.service.external;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class LogoDevService {

    private final WebClient webClient;

    public LogoDevService(
            WebClient.Builder builder,
            @Value("${LOGO_DEV_KEY}") String token) {
        this.webClient = builder
                .baseUrl("https://img.logo.dev")
                .defaultUriVariables(Map.of("token", token))
                .build();
    }

    public Mono<ResponseEntity<byte[]>> fetchLogo(String domain) {
        return webClient.get()
                .uri("/{domain}?token={token}&fallback=404",
                        Map.of("domain", domain))
                .exchangeToMono(this::handleResponse);
    }

    private Mono<ResponseEntity<byte[]>> handleResponse(ClientResponse response) {
        HttpStatusCode status = response.statusCode();

        if (status.is2xxSuccessful()) {
            return response.bodyToMono(byte[].class)
                    .map(body -> ResponseEntity.ok()
                            .contentType(
                                    response.headers()
                                            .contentType()
                                            .orElse(MediaType.IMAGE_JPEG))
                            .body(body));
        }

        return Mono.just(
                ResponseEntity.status(status)
                        .body(("Error " + status.value()).getBytes()));
    }
}