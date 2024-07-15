package com.example.payment.transfer.exception;

import com.example.payment.global.error.ErrorCode;

public class NotEnoughWithdrawalMoney extends RuntimeException{
    public NotEnoughWithdrawalMoney() {
        super(ErrorCode.NOT_ENOUGH_WITHDRAWAL_MONEY.getMessage());
    }
}
