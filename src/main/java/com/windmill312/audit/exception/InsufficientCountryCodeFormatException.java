package com.windmill312.audit.exception;

public class InsufficientCountryCodeFormatException extends RuntimeException {
    public InsufficientCountryCodeFormatException(String message) {
        super(message);
    }
}