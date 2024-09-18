package com.example.payment.account.dto.response;

import com.example.payment.account.dto.AccountDto;
import jakarta.validation.constraints.NotBlank;

public record AccountDetailResponse(

        @NotBlank
        String accountNumber,

        @NotBlank
        String balance
) {

    public static AccountDetailResponse to(AccountDto accountDto) {
        return new AccountDetailResponse(accountDto.accountNumber(),
                String.format("%d", accountDto.balance().toBigInteger()));
    }
}
