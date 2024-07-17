package com.example.payment.transferHistory;

import com.example.payment.transferHistory.entity.TransferHistory;
import java.math.BigDecimal;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class TransferHistoryRepositoryTest {

    @Autowired
    TransferHistoryRepository transferHistoryRepository;

    @Test
    @DisplayName("계좌 이체 내역을 저장합니다.")
    void 계좌_이체_내역을_저장합니다() throws Exception{

        //given
        TransferHistory transferHistory = TransferHistory.builder()
                .withdrawalAccountNumber("0123456789")
                .depositAccountNumber("1234567890")
                .transferAmount(BigDecimal.valueOf(10000))
                .amountAfterWithdrawal(BigDecimal.valueOf(0))
                .amountAfterDeposit(BigDecimal.valueOf(20000))
                .build();

        //when
        TransferHistory savedTransferHistory = transferHistoryRepository.save(transferHistory);

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(savedTransferHistory.getWithdrawalAccountNumber()).isEqualTo(transferHistory.getWithdrawalAccountNumber());
            softAssertions.assertThat(savedTransferHistory.getDepositAccountNumber()).isEqualTo(transferHistory.getDepositAccountNumber());
            softAssertions.assertThat(savedTransferHistory.getTransferAmount()).isEqualTo(transferHistory.getTransferAmount());
            softAssertions.assertThat(savedTransferHistory.getAmountAfterDeposit()).isEqualTo(transferHistory.getAmountAfterDeposit());
            softAssertions.assertThat(savedTransferHistory.getAmountAfterWithdrawal()).isEqualTo(transferHistory.getAmountAfterWithdrawal());
        });
    }
}
