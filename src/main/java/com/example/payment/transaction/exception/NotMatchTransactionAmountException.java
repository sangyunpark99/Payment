package com.example.payment.transaction.exception;

import com.example.payment.global.error.ErrorCode;

public class NotMatchTransactionAmountException extends RuntimeException{

    public NotMatchTransactionAmountException() {
        super(ErrorCode.NOT_MATCH_TRANSACTION_AMOUNT.getMessage());
    }
}
