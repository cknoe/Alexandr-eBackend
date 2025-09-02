package com.cknoe.backend_springboot;

public class CardNotFoundException extends RuntimeException {
    CardNotFoundException(Long id) {
        super("Could not find card " + id);
    }
}
