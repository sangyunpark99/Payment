package com.example.payment.account.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountDto(String email, String accountNumber, BigDecimal balance, LocalDateTime registeredAt, LocalDateTime unregisteredAt) {

    public static AccountDto from(final String email, final String accountNumber, final BigDecimal balance, LocalDateTime registeredAt, LocalDateTime unregisteredAt){
        return new AccountDto(email, accountNumber, balance, registeredAt, unregisteredAt);
    }
}
