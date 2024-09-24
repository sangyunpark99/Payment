package com.example.payment.transaction.dto.response;

import com.example.payment.transaction.domain.TransactionResult;
import com.example.payment.transaction.dto.TransactionDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionCancelResponse(
        String accountNumber,
        TransactionResult transactionResult,
        Long transactionId,
        BigDecimal amount,
        LocalDateTime transactedAt
) {

    public static TransactionCancelResponse fromDto(TransactionDto transactionDto) {
        return new TransactionCancelResponse(
                transactionDto.accountNumber(),
                transactionDto.transactionResult(),
                transactionDto.transactionId(),
                transactionDto.amount(),
                transactionDto.transactedAt()
        );
    }
}
