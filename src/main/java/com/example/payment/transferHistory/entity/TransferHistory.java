package com.example.payment.transferHistory.entity;

import com.example.payment.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    private String withdrawalAccountNumber;

    @Column(length = 10)
    private String depositAccountNumber;

    private BigDecimal transferAmount;

    private BigDecimal amountAfterWithdrawal;

    private BigDecimal amountAfterDeposit;

    @Builder
    public TransferHistory(
            final String withdrawalAccountNumber,
            final String depositAccountNumber,
            final BigDecimal transferAmount,
            final BigDecimal amountAfterWithdrawal,
            final BigDecimal amountAfterDeposit
    ) {
        this.withdrawalAccountNumber = withdrawalAccountNumber;
        this.depositAccountNumber = depositAccountNumber;
        this.transferAmount = transferAmount;
        this.amountAfterWithdrawal = amountAfterWithdrawal;
        this.amountAfterDeposit = amountAfterDeposit;
    }
}
