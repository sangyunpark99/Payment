package com.example.payment.account.dto.response;

import com.example.payment.account.dto.AccountDto;
import java.math.BigDecimal;

public record AccountCreateResponse(String accountNumber, BigDecimal balance) {

    public static AccountCreateResponse to(AccountDto accountDto) {
        return new AccountCreateResponse(accountDto.getAccountNumber(),
                accountDto.getBalance());
    }
}
