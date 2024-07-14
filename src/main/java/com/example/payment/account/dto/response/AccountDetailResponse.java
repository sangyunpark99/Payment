package com.example.payment.account.dto.response;

import com.example.payment.account.dto.AccountDto;
import jakarta.validation.constraints.NotBlank;

public record AccountDetailResponse(
        @NotBlank
        String balance
) {

    public static AccountDetailResponse to(AccountDto accountDto) {
        return new AccountDetailResponse(String.format("%d", accountDto.balance().toBigInteger()));
    }
}
