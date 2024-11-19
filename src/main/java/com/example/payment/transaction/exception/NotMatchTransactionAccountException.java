package com.example.payment.transaction.exception;

import com.example.payment.global.error.ErrorCode;

public class NotMatchTransactionAccountException extends RuntimeException{

    public NotMatchTransactionAccountException() {
        super(ErrorCode.NOT_MATCH_TRANSACTION_ACCOUNT.getMessage());
    }
}
