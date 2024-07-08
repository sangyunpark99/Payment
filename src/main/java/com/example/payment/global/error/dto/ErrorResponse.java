package com.example.payment.global.error.dto;

public record ErrorResponse(String message) {

    public ErrorResponse(final String message) {
        this.message = message;
    }
}
