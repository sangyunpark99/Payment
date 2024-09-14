package com.example.payment.account.dto.response;

import com.example.payment.account.dto.AccountDto;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AccountDeleteResponse {

    private String email;
    private String accountNumber;
    private LocalDateTime unregisteredAt;

    public AccountDeleteResponse(String email, String accountNumber,
                                 LocalDateTime unregisteredAt) {
        this.email = email;
        this.accountNumber = accountNumber;
        this.unregisteredAt = unregisteredAt;
    }

    public static AccountDeleteResponse to(AccountDto accountDto) {
        return new AccountDeleteResponse(accountDto.getEmail(), accountDto.getAccountNumber(), accountDto.getUnregisteredAt());
    }
}
