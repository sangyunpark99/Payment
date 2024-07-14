package com.example.payment.account.exception;

import com.example.payment.global.error.ErrorCode;

public class NotExistAccountException extends RuntimeException {

    public NotExistAccountException() {
        super(ErrorCode.NOT_EXIST_USER.getMessage());
    }
}
