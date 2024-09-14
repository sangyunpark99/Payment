package com.example.payment.account.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AccountDto {

    private String email;
    private String accountNumber;
    private BigDecimal balance;
    private LocalDateTime registeredAt;
    private LocalDateTime unregisteredAt;

    public AccountDto(String email, String accountNumber, BigDecimal balance, LocalDateTime registeredAt, LocalDateTime unregisteredAt) {
        this.email = email;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.registeredAt = registeredAt;
        this.unregisteredAt = unregisteredAt;
    }

    public static AccountDto from(final String email, final String accountNumber, final BigDecimal balance, LocalDateTime registeredAt, LocalDateTime unregisteredAt){
        return new AccountDto(email, accountNumber, balance, registeredAt, unregisteredAt);
    }
}
