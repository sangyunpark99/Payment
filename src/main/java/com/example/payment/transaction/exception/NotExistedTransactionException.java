package com.example.payment.transaction.exception;

import com.example.payment.global.error.ErrorCode;

public class NotExistedTransactionException extends RuntimeException{
    public NotExistedTransactionException() {
        super(ErrorCode.NOT_EXISTED_TRANSACTION.getMessage());
    }
}
