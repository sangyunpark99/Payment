package com.example.payment.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberDeleteRequest(
        @NotBlank @Email
        String email,

        @NotBlank
        String password,

        @NotBlank
        String nickname
) {
}