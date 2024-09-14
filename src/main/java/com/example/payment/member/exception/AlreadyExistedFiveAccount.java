package com.example.payment.member.exception;

import com.example.payment.global.error.ErrorCode;

public class AlreadyExistedFiveAccount extends RuntimeException {
    public AlreadyExistedFiveAccount() {
        super(ErrorCode.ALREADY_EXISTED_FIVE_ACCOUNT.getMessage());
    }
}
