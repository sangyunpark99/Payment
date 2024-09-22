package com.example.payment.transaction.exception;

import com.example.payment.global.error.ErrorCode;

public class NotUseAccountException extends RuntimeException{
    public NotUseAccountException() {
        super(ErrorCode.NOT_USE_ACCOUNT.getMessage());
    }
}
