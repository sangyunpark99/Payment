package com.example.payment.account.dto;

import java.math.BigDecimal;

public record AccountDto(String accountNumber, BigDecimal balance) {
}
