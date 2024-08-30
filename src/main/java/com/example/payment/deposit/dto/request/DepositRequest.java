package com.example.payment.deposit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record DepositRequest(
        @NotBlank
        String account,

        @NotBlank
        String password,

        @NotNull
        BigDecimal amount
) {
}
