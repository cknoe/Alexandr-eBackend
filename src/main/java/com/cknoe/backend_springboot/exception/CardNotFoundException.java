package com.cknoe.backend_springboot.exception;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(Long id) {
        super("Could not find card " + id);
    }
}
