package com.ultimatestorytelling.backend.exception;

public enum ErrorCode {

    POST_NOT_FOUND("Posts not found");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {

        return message;
    }
}
