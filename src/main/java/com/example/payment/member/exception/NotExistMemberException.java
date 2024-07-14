package com.example.payment.member.exception;

import com.example.payment.global.error.ErrorCode;

public class NotExistMemberException extends RuntimeException {
    public NotExistMemberException() {
        super(ErrorCode.NOT_EXIST_USER.getMessage());
    }
}
