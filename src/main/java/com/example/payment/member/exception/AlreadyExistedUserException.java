package com.example.payment.member.exception;

import com.example.payment.global.error.ErrorCode;

public class AlreadyExistedUserException extends RuntimeException{
    public AlreadyExistedUserException() {
        super(ErrorCode.ALREADY_EXISTED_USER.getMessage());
    }
}
