package com.example.payment.transfer;

import com.example.payment.account.AccountRepository;
import com.example.payment.account.entity.Account;
import com.example.payment.member.exception.NotMatchPasswordException;
import com.example.payment.transfer.dto.reqeust.TransferRequest;
import com.example.payment.transfer.exception.NotEnoughWithdrawalMoney;
import com.example.payment.transferHistory.TransferHistoryRepository;
import com.example.payment.transferHistory.entity.TransferHistory;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferHistoryRepository transferHistoryRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public void transfer(final TransferRequest request) {

        final String withdrawalAccountNumber = request.withdrawalAccountNumber();
        final String depositAccountNumber = request.depositAccountNumber();
        final BigDecimal transferAmount = request.transferAmount();

        // 1. 계좌의 존재 여부를 확인한다.
        Account withdrawalAccount = accountRepository.getByAccountNumber(withdrawalAccountNumber);
        Account depositAccount = accountRepository.getByAccountNumber(depositAccountNumber);

        // 2. 비밀번호 일치여부를 확인한다.
        if(!checkAccountPassword(request.accountPassword(), withdrawalAccount.getPassword())){
            throw new NotMatchPasswordException();
        }

        // 3. 출금 게좌에 출금 액수만큼 빼준다.
        if(!checkWithdrawalMoney(withdrawalAccount, transferAmount)) {
            throw new NotEnoughWithdrawalMoney();
        }
        final BigDecimal amountAfterWithdrawal =  depositAccount.getBalance().subtract(transferAmount);
        withdrawalAccount.updateBalance(amountAfterWithdrawal);

        // 4. 입금 계좌에 입금 액수만큼 더해준다.
        final BigDecimal amountAfterDeposit =  depositAccount.getBalance().add(transferAmount);
        depositAccount.updateBalance(amountAfterDeposit);

        // 5. 이체 내역을 기록한다.
        TransferHistory transferHistory = TransferHistory.builder()
                .amountAfterWithdrawal(amountAfterWithdrawal)
                .amountAfterDeposit(amountAfterDeposit)
                .transferAmount(transferAmount)
                .depositAccountNumber(depositAccountNumber)
                .withdrawalAccountNumber(withdrawalAccountNumber)
                .build();

        transferHistoryRepository.save(transferHistory);
    }

    /**
     * a.compareTo(b) : a가 b보다 큰 경우 1을 return 한다.
     */
    private boolean checkWithdrawalMoney(final Account withdrawlAccount, final BigDecimal transferAmount) {
        // a.compareTo(b) : a가 b보다 큰 경 1을 return
        return withdrawlAccount.getBalance().compareTo(transferAmount) == 1;
    }

    private boolean checkAccountPassword(final String requestPassword, final String accountPassword) {
        return requestPassword.equals(accountPassword);
    }
}
