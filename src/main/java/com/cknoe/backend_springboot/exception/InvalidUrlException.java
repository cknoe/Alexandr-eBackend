package com.cknoe.backend_springboot.exception;

public class InvalidUrlException extends RuntimeException {
    public InvalidUrlException(String url, String exMessage) {
        super("Invalid URL " + url + " with message : " + exMessage);
    }
}