package com.example.preferences.exception;

public class PreferenceNotFoundException extends RuntimeException {
    public PreferenceNotFoundException(String message) {
        super(message);
    }
}
