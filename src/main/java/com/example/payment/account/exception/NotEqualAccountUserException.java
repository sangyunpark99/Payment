package com.example.payment.account.exception;

import com.example.payment.global.error.ErrorCode;

public class NotEqualAccountUserException extends RuntimeException{
    public NotEqualAccountUserException() {
        super(ErrorCode.NOT_EQUAL_ACCOUNT_USER.getMessage());
    }
}
