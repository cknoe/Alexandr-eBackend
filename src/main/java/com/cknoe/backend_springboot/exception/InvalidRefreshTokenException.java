package com.cknoe.backend_springboot.exception;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException() {
        super("Invalid Refresh Token");
    }
}
