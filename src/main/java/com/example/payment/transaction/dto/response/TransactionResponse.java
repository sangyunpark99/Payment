package com.example.payment.transaction.dto.response;

import com.example.payment.transaction.dto.TransactionDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(

        Long transactionId,
        String accountNumber,
        String transactionResult,
        BigDecimal amount,
        LocalDateTime transactedAt
) {

    public static TransactionResponse fromDto(TransactionDto transactionDto) {
        return new TransactionResponse(
                transactionDto.transactionId(),
                transactionDto.accountNumber(),
                transactionDto.transactionResult().name(),
                transactionDto.amount(),
                transactionDto.transactedAt()
        );
    }
}
