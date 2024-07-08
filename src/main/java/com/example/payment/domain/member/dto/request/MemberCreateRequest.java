package com.example.payment.domain.member.dto.request;

import com.example.payment.domain.member.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberCreateRequest(
        @NotBlank @Email
        String email,

        @NotBlank
        String password,

        @NotBlank
        String name
) {
        public Member toEntity() {
                return Member.builder()
                        .email(this.email)
                        .password(this.password)
                        .nickName(this.name)
                        .build();
        }
}
