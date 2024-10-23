package com.example.payment.transaction.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record TransactionCancelRequest(

        @NotNull(message = "거래 아이디는 필수로 입력해야 합니다.")
        Long transactionId,

        @NotBlank(message = "출금 계좌 번호는 필수로 입력해야 합니다.") @Length(min = 10, max = 10)
        String accountNumber,

        @NotNull(message = "취소할 금액은 필수로 입력해야 합니다.") @DecimalMin("1")
        Long amount

) {
}