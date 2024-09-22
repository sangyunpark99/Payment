package com.example.payment.transaction.dto;

import com.example.payment.transaction.domain.Transaction;
import com.example.payment.transaction.domain.TransactionResult;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record TransactionDto(
        Long transactionId,
        String accountNumber,
        TransactionResult transactionResult,
        BigDecimal amount,
        LocalDateTime transactedAt,
        BigDecimal amountAfterTransaction
) {

    public static TransactionDto fromEntity(Transaction transaction) {
        return TransactionDto.builder()
                .transactionId(transaction.getId())
                .transactedAt(transaction.getTransactedAt())
                .accountNumber(transaction.getAccount().getAccountNumber())
                .amount(transaction.getAmount())
                .transactionResult(transaction.getTransactionResult())
                .amountAfterTransaction(transaction.getAmountAfterTransaction())
                .build();
    }
}
