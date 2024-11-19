package com.example.payment.transaction.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import org.hibernate.validator.constraints.Length;

public record TransactionRequest(

        @NotBlank(message = "사용자 이메일은 필수로 입력해야 합니다.")
        String email,

        @NotBlank(message = "출금 계좌 번호는 필수로 입력해야 합니다.") @Length(min = 10, max = 10)
        String accountNumber,

        @NotBlank(message = "계좌의 비밀번호는 필수로 입력해야 합니다.")
        String password,

        @NotNull(message = "이체할 금액은 필수로 입력해야 합니다.") @DecimalMin("1")
        BigDecimal amount

) {
}
