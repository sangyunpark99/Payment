package com.example.payment.member.exception;

import com.example.payment.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotExistMemberException extends RuntimeException {
    private String message;

    public NotExistMemberException() {
        this.message = ErrorCode.NOT_EXIST_USER.getMessage();
    }
}
