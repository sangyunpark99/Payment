package com.example.payment.transaction;

import com.example.payment.account.AccountRepository;
import com.example.payment.account.domain.Account;
import com.example.payment.account.domain.AccountStatus;
import com.example.payment.account.exception.NotEqualAccountUserException;
import com.example.payment.account.exception.NotExistAccountException;
import com.example.payment.global.exception.NotMatchPasswordException;
import com.example.payment.transaction.domain.Transaction;
import com.example.payment.transaction.domain.TransactionResult;
import com.example.payment.transaction.domain.TransactionType;
import com.example.payment.transaction.dto.TransactionDto;
import com.example.payment.transaction.dto.request.TransactionCancelRequest;
import com.example.payment.transaction.dto.request.TransactionRequest;
import com.example.payment.transaction.exception.NotExistedTransactionException;
import com.example.payment.transaction.exception.NotMatchTransactionAccountException;
import com.example.payment.transaction.exception.NotMatchTransactionAmountException;
import com.example.payment.transaction.exception.NotUseAccountException;
import com.example.payment.transaction.exception.OldTransactionOrderException;
import com.example.payment.transfer.exception.NotEnoughWithdrawalMoney;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional(noRollbackFor = {
            NotMatchPasswordException.class,NotEqualAccountUserException.class, NotEnoughWithdrawalMoney.class
    })
    public TransactionDto transactionUse(final TransactionRequest request) {

        Account account = accountRepository.getByAccountNumber(request.accountNumber());

        try {

            validateTransactionUse(request, account);

            // 잔액 차감
            account.subtractBalance(request.amount());

            // transaction를 성공한 경우
            Transaction transaction = Transaction.builder()
                    .transactionType(TransactionType.USE)
                    .amountAfterTransaction(account.getBalance())
                    .transactionResult(TransactionResult.SUCCESS)
                    .account(account)
                    .amount(request.amount())
                    .transactedAt(LocalDateTime.now())
                    .build();

            return TransactionDto.fromEntity(transactionRepository.save(transaction));

        }catch(NotMatchPasswordException | NotEqualAccountUserException | NotEnoughWithdrawalMoney | NotExistAccountException e) {
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

    @Transactional(noRollbackFor = {
            NotMatchPasswordException.class,NotEqualAccountUserException.class, NotEnoughWithdrawalMoney.class
    })
    public TransactionDto transactionCancel(final TransactionCancelRequest request){ // 취소가 되도 로그를 기록

        Account account = accountRepository.getByAccountNumber(request.accountNumber());

        try {

            Transaction transaction = transactionRepository.findById(request.transactionId()).orElseThrow(() -> new NotExistedTransactionException());

            validateTransactionCancel(request, transaction, account);

            account.cancelBalance(BigDecimal.valueOf(request.amount()));

            Transaction cancelTransaction = Transaction.builder()
                    .transactionType(TransactionType.CANCEL)
                    .amountAfterTransaction(account.getBalance())
                    .transactionResult(TransactionResult.SUCCESS)
                    .account(account)
                    .amount(BigDecimal.valueOf(request.amount()))
                    .transactedAt(LocalDateTime.now())
                    .build();

            return TransactionDto.fromEntity(transactionRepository.save(cancelTransaction));

        } catch (NotMatchTransactionAccountException | NotMatchTransactionAmountException | OldTransactionOrderException |
                 NotExistAccountException e) {

            Transaction cancelTransaction = Transaction.builder()
                    .transactionType(TransactionType.CANCEL)
                    .amountAfterTransaction(account.getBalance())
                    .transactionResult(TransactionResult.FAIL)
                    .account(account)
                    .amount(BigDecimal.valueOf(request.amount()))
                    .transactedAt(LocalDateTime.now())
                    .build();

            transactionRepository.save(cancelTransaction);

            throw e;
        }
    }


    private void validateTransactionUse(final TransactionRequest request, final Account account) {
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
    }

    private void validateTransactionCancel(final TransactionCancelRequest request, final Transaction transaction, final Account account) {

        if(!Objects.equals(transaction.getAccount().getAccountNumber(), request.accountNumber())) { // 거래 계좌가 일치하지 않은 경우
            throw new NotMatchTransactionAccountException();
        }

        if(!Objects.equals(transaction.getAmount().setScale(0), BigDecimal.valueOf(request.amount()))) { // 거래 잔액과 취소 요청 잔액이 일치하지 않은 경우
            throw new NotMatchTransactionAmountException();
        }

        if(transaction.getTransactedAt().isBefore(LocalDateTime.now().minusYears(1))) { //거래를 한 후, 1년이 경과한 경우
            throw new OldTransactionOrderException();
        }
    }

    private boolean checkTransactionAmount(final Account account, final BigDecimal transactionAmount) {
        // a.compareTo(b) : a가 b보다 큰 경 1을 return
        // 계좌에 있는 잔액이 더 큰 경우, true를 리턴
        return account.getBalance().compareTo(transactionAmount) == 1;
    }
}
