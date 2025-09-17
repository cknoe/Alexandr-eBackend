package com.cknoe.backend_springboot.response;

public record AuthResponse(String token, String refreshToken) {
}