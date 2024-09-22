package com.example.payment.transaction;

import com.example.payment.account.AccountRepository;
import com.example.payment.account.domain.Account;
import com.example.payment.account.domain.AccountStatus;
import com.example.payment.account.exception.AlreadyUnregisteredException;
import com.example.payment.account.exception.NotEqualAccountUserException;
import com.example.payment.global.exception.NotMatchPasswordException;
import com.example.payment.transaction.domain.Transaction;
import com.example.payment.transaction.domain.TransactionResult;
import com.example.payment.transaction.domain.TransactionType;
import com.example.payment.transaction.dto.TransactionDto;
import com.example.payment.transaction.dto.request.TransactionRequest;
import com.example.payment.transaction.exception.NotUseAccountException;
import com.example.payment.transfer.exception.NotEnoughWithdrawalMoney;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional(noRollbackFor = {
            NotMatchPasswordException.class,NotEqualAccountUserException.class, NotEnoughWithdrawalMoney.class
    })
    public TransactionDto transaction(final TransactionRequest request) {

        Account account = null;

        try {
            // 계좌 존재여부 검사
            account = accountRepository.getByAccountNumber(request.accountNumber());

            // 계정 일치 여부 검사
            if(!account.getMember().getEmail().equals(request.email())) {
                throw new NotEqualAccountUserException();
            }

            // 비밀번호 일치 검사
            if(!request.password().equals(account.getPassword())) {
                throw new NotMatchPasswordException();
            }

            // 계좌 해지 여부 검사
            if(account.getStatus() == AccountStatus.NOT_IN_USE) {
                throw new NotUseAccountException();
            }

            // 잔액 검사
            if(!checkTransactionAmount(account, request.amount())) {
                throw new NotEnoughWithdrawalMoney();
            }
            // 잔액 차감
            final BigDecimal remainAmount = account.getBalance().subtract(request.amount());
            account.updateBalance(remainAmount);

            // transaction를 성공한 경우
            Transaction transaction = Transaction.builder()
                    .transactionType(TransactionType.USE)
                    .amountAfterTransaction(remainAmount)
                    .transactionResult(TransactionResult.SUCCESS)
                    .account(account)
                    .amount(request.amount())
                    .transactedAt(LocalDateTime.now())
                    .build();

            return TransactionDto.fromEntity(transactionRepository.save(transaction));

        }catch(NotMatchPasswordException | NotEqualAccountUserException | NotEnoughWithdrawalMoney e) {
            Transaction transaction = Transaction.builder()
                    .transactionType(TransactionType.USE)
                    .amountAfterTransaction(account.getBalance())
                    .transactionResult(TransactionResult.FAIL)
                    .account(account)
                    .amount(request.amount())
                    .transactedAt(LocalDateTime.now())
                    .build();

            // transaction를 실패한 경우
            transactionRepository.save(transaction);

            throw e;
        }
    }

    private boolean checkTransactionAmount(final Account account, final BigDecimal transactionAmount) {
        // a.compareTo(b) : a가 b보다 큰 경 1을 return
        // 계좌에 있는 잔액이 더 큰 경우, true를 리턴
        return account.getBalance().compareTo(transactionAmount) != -1;
    }
}
