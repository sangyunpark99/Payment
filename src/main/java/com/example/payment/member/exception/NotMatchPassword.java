package com.example.payment.member.exception;

import com.example.payment.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotMatchPassword extends RuntimeException{
    private String message;

    public NotMatchPassword() {
        this.message = ErrorCode.NOT_MATCH_PASSWORD.getMessage();
    }
}
