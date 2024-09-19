package com.example.payment.account.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public record AccountDeleteRequest(
        @NotBlank @Email
        String email,

        @NotBlank
        String accountNumber,

        @NotBlank @Length(min = 4, max = 4)
        String password
) {
}
