package com.example.payment.account.exception;

import com.example.payment.global.error.ErrorCode;

public class AlreadyExistedBalanceException extends RuntimeException{
    public AlreadyExistedBalanceException() {
        super(ErrorCode.ALREADY_EXISTED_BALANCE.getMessage());
    }
}
