package com.example.payment.account.dto.request;

import com.example.payment.account.entity.Account;
import com.example.payment.member.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import org.hibernate.validator.constraints.Length;

public record AccountCreateRequest(
        @NotBlank @Email
        String email,

        @NotBlank
        String password,

        @NotBlank @Length(min = 4, max = 4)
        String accountPassword
) {

    public Account toEntity(final Member member, final String accountNumber) {
        Account account = Account.builder()
                .member(member)
                .balance(BigDecimal.ZERO)
                .accountNumber(accountNumber)
                .password(accountPassword)
                .build();

        return account;
    }
}
