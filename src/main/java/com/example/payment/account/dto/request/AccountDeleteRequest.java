package com.example.payment.account.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AccountDeleteRequest(
        @NotBlank @Email
        String email,

        @NotBlank @Size(min = 10, max = 10)
        String accountNumber,

        @NotBlank
        String password
) {
}
