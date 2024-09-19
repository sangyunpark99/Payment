package com.example.payment.account.dto.response;

import com.example.payment.account.dto.AccountDto;
import java.time.LocalDateTime;

public record AccountDeleteResponse(
        String email,
        String accountNumber,
        LocalDateTime unregisteredAt
) {
    public static AccountDeleteResponse to(AccountDto accountDto) {
        return new AccountDeleteResponse(accountDto.email(), accountDto.accountNumber(), accountDto.unregisteredAt());
    }
}
