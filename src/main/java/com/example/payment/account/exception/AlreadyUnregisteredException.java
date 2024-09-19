package com.example.payment.account.exception;

import com.example.payment.global.error.ErrorCode;

public class AlreadyUnregisteredException extends RuntimeException {
    public AlreadyUnregisteredException() {
        super(ErrorCode.ALREADY_UNREGISTERED.getMessage());
    }
}
