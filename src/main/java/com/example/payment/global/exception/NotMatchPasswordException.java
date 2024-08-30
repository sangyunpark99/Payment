package com.example.payment.global.exception;

import com.example.payment.global.error.ErrorCode;

public class NotMatchPasswordException extends RuntimeException{
    public NotMatchPasswordException() {
        super(ErrorCode.NOT_MATCH_PASSWORD.getMessage());
    }
}
