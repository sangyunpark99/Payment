package com.example.payment.transaction.exception;

import com.example.payment.global.error.ErrorCode;

public class OldTransactionOrderException extends RuntimeException{
    public OldTransactionOrderException() {
        super(ErrorCode.OLD_TRANSACTION_ORDER.getMessage());
    }
}
