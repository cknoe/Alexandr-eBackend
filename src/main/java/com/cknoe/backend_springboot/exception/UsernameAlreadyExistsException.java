package com.cknoe.backend_springboot.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super("Username already exists : " + username);
    }
}
