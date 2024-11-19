package com.example.payment.transaction;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.payment.account.AccountRepository;
import com.example.payment.account.domain.Account;
import com.example.payment.account.exception.NotEqualAccountUserException;
import com.example.payment.account.exception.NotExistAccountException;
import com.example.payment.global.exception.NotMatchPasswordException;
import com.example.payment.member.entity.Member;
import com.example.payment.transaction.domain.Transaction;
import com.example.payment.transaction.domain.TransactionResult;
import com.example.payment.transaction.domain.TransactionType;
import com.example.payment.transaction.dto.TransactionDto;
import com.example.payment.transaction.dto.request.TransactionCancelRequest;
import com.example.payment.transaction.dto.request.TransactionRequest;
import com.example.payment.transaction.exception.NotMatchTransactionAccountException;
import com.example.payment.transaction.exception.NotMatchTransactionAmountException;
import com.example.payment.transaction.exception.NotUseAccountException;
import com.example.payment.transaction.exception.OldTransactionOrderException;
import com.example.payment.transfer.exception.NotEnoughWithdrawalMoney;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    TransactionService transactionService;

    @Test
    @DisplayName("잔액 사용 성공")
    void 잔액_사용_성공() throws Exception {
        //given
        TransactionRequest request = new TransactionRequest("abc@abc.com", "1234567891", "1234",
                BigDecimal.valueOf(10000));

        Member member = Member.builder()
                .email("abc@abc.com")
                .nickName("abc")
                .password("1234")
                .accounts(new ArrayList<>())
                .build();

        Account account = Account.builder()
                .accountNumber("1234567891")
                .balance(BigDecimal.valueOf(100000))
                .member(member)
                .password("1234")
                .build();

        member.getAccounts().add(account);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.USE)
                .amountAfterTransaction(account.getBalance().subtract(request.amount()))
                .transactionResult(TransactionResult.SUCCESS)
                .account(account)
                .amount(request.amount())
                .transactedAt(LocalDateTime.now())
                .build();

        //when
        when(accountRepository.getByAccountNumber(anyString())).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        //then
        TransactionDto transactionDto = transactionService.transactionUse(request);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(transactionDto.transactionId()).isEqualTo(transaction.getId());
            softAssertions.assertThat(transactionDto.transactedAt()).isEqualTo(transaction.getTransactedAt());
            softAssertions.assertThat(transactionDto.transactionResult()).isEqualTo(transaction.getTransactionResult());
            softAssertions.assertThat(transactionDto.amount()).isEqualTo(transaction.getAmount());
            softAssertions.assertThat(transactionDto.amountAfterTransaction()).isEqualTo(transaction.getAmountAfterTransaction());
        });
    }

    @Test
    @DisplayName("계좌가 존재 하지 않아 잔액 사용에 실패")
    void 계좌가_존재하지_않아_잔액_사용에_실패() throws Exception {
        //given
        TransactionRequest request = new TransactionRequest("abc@abc.com", "1234567891", "1234",
                BigDecimal.valueOf(10000));

        Member member = Member.builder()
                .email("abc@abc.com")
                .nickName("abc")
                .password("1234")
                .accounts(new ArrayList<>())
                .build();

        Account account = Account.builder()
                .accountNumber("1234567891")
                .balance(BigDecimal.valueOf(100000))
                .member(member)
                .password("1234")
                .build();

        member.getAccounts().add(account);

        //when
        when(accountRepository.getByAccountNumber(anyString())).thenThrow(NotExistAccountException.class);

        //then
        Assertions.assertThatThrownBy(() -> transactionService.transactionUse(request))
                .isInstanceOf(NotExistAccountException.class);
    }

    @Test
    @DisplayName("계정이 일치 하지 않아 잔액 사용 실패")
    void 계정이_일치_하지_않아_잔액_사용_실패() throws Exception {
        //given
        TransactionRequest request = new TransactionRequest("abc@abc.com", "1234567891", "1234",
                BigDecimal.valueOf(10000));

        Member member = Member.builder()
                .email("bcd@bcd.com")
                .nickName("abc")
                .password("1234")
                .accounts(new ArrayList<>())
                .build();

        Account account = Account.builder()
                .accountNumber("1234567891")
                .balance(BigDecimal.valueOf(100000))
                .member(member)
                .password("1234")
                .build();

        member.getAccounts().add(account);

        //when
        when(accountRepository.getByAccountNumber(anyString())).thenReturn(account);

        //then
        Assertions.assertThatThrownBy(() -> transactionService.transactionUse(request))
                .isInstanceOf(NotEqualAccountUserException.class);
    }

    @Test
    @DisplayName("비밀번호가 일치 하지 않아 잔액 사용 실패")
    void 비밀번호가_일치_하지_않아_잔액_사용_실패() throws Exception {
        //given
        TransactionRequest request = new TransactionRequest("abc@abc.com", "1234567891", "1235",
                BigDecimal.valueOf(10000));

        Member member = Member.builder()
                .email("abc@abc.com")
                .nickName("abc")
                .password("1234")
                .accounts(new ArrayList<>())
                .build();

        Account account = Account.builder()
                .accountNumber("1234567891")
                .balance(BigDecimal.valueOf(100000))
                .member(member)
                .password("1234")
                .build();

        member.getAccounts().add(account);

        //when
        when(accountRepository.getByAccountNumber(anyString())).thenReturn(account);

        //then
        Assertions.assertThatThrownBy(() -> transactionService.transactionUse(request))
                .isInstanceOf(NotMatchPasswordException.class);
    }

    @Test
    @DisplayName("잔액이 부족해 잔액 사용 실패")
    void 잔액이_부족해_잔액_사용_실패() throws Exception {
        //given
        TransactionRequest request = new TransactionRequest("abc@abc.com", "1234567891", "1234",
                BigDecimal.valueOf(10000));

        Member member = Member.builder()
                .email("abc@abc.com")
                .nickName("abc")
                .password("1234")
                .accounts(new ArrayList<>())
                .build();

        Account account = Account.builder()
                .accountNumber("1234567891")
                .balance(BigDecimal.valueOf(0))
                .member(member)
                .password("1234")
                .build();

        member.getAccounts().add(account);

        //when
        when(accountRepository.getByAccountNumber(anyString())).thenReturn(account);

        //then
        Assertions.assertThatThrownBy(() -> transactionService.transactionUse(request))
                .isInstanceOf(NotEnoughWithdrawalMoney.class);
    }

    @Test
    @DisplayName("이미 해지된 계정으로 인해 잔액 사용 실패")
    void 이미_해지된_계정으로_인해_잔액_사용_실패() throws Exception {
        //given
        TransactionRequest request = new TransactionRequest("abc@abc.com", "1234567891", "1234",
                BigDecimal.valueOf(10000));

        Member member = Member.builder()
                .email("abc@abc.com")
                .nickName("abc")
                .password("1234")
                .accounts(new ArrayList<>())
                .build();

        Account account = Account.builder()
                .accountNumber("1234567891")
                .balance(BigDecimal.valueOf(0))
                .member(member)
                .password("1234")
                .build();

        account.updateUnregisteredAt();

        member.getAccounts().add(account);

        //when
        when(accountRepository.getByAccountNumber(anyString())).thenReturn(account);

        //then
        Assertions.assertThatThrownBy(() -> transactionService.transactionUse(request))
                .isInstanceOf(NotUseAccountException.class);
    }

    @Test
    @DisplayName("잔액사용 실패시 잔금 실패 내용 저장")
    void 잔액_사용_실패시_잔금_실패_내용_저장() throws Exception {
        //given
        TransactionRequest request = new TransactionRequest("abc@abc.com", "1234567891", "1234",
                BigDecimal.valueOf(10000));

        Member member = Member.builder()
                .email("abc@abc.com")
                .nickName("abc")
                .password("1234")
                .accounts(new ArrayList<>())
                .build();

        Account account = Account.builder()
                .accountNumber("1234567891")
                .balance(BigDecimal.valueOf(0))
                .member(member)
                .password("1234")
                .build();

        member.getAccounts().add(account);

        //when
        when(accountRepository.getByAccountNumber(anyString())).thenReturn(account);

        //then
        assertThatThrownBy(() -> transactionService.transactionUse(request))
                .isInstanceOf(NotEnoughWithdrawalMoney.class);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("잔액 사용 취소")
    void 잔액_사용_취소_성공() throws Exception{
        //given
        TransactionRequest request = new TransactionRequest("abc@abc.com", "1234567891", "1234",
                BigDecimal.valueOf(10000));

        TransactionCancelRequest cancelRequest = new TransactionCancelRequest(
                1L,"1234567891",10000L
        );

        Member member = Member.builder()
                .email("abc@abc.com")
                .nickName("abc")
                .password("1234")
                .accounts(new ArrayList<>())
                .build();

        Account account = Account.builder()
                .accountNumber("1234567891")
                .balance(BigDecimal.valueOf(100000))
                .member(member)
                .password("1234")
                .build();

        member.getAccounts().add(account);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.USE)
                .amountAfterTransaction(account.getBalance().subtract(request.amount()))
                .transactionResult(TransactionResult.SUCCESS)
                .account(account)
                .amount(request.amount())
                .transactedAt(LocalDateTime.now())
                .build();

        Transaction cancelTransaction = Transaction.builder()
                .transactionType(TransactionType.CANCEL)
                .amountAfterTransaction(account.getBalance())
                .transactionResult(TransactionResult.SUCCESS)
                .account(account)
                .amount(BigDecimal.valueOf(cancelRequest.amount()))
                .transactedAt(LocalDateTime.now())
                .build();

        //when
        when(accountRepository.getByAccountNumber(anyString())).thenReturn(account);
        when(transactionRepository.findById(any(Long.class))).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(cancelTransaction);
        //then
        TransactionDto cancelTransactionDto = transactionService.transactionCancel(cancelRequest);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(cancelTransactionDto.transactionId()).isEqualTo(cancelTransaction.getId());
            softAssertions.assertThat(cancelTransactionDto.transactedAt()).isEqualTo(cancelTransaction.getTransactedAt());
            softAssertions.assertThat(cancelTransactionDto.transactionResult()).isEqualTo(cancelTransaction.getTransactionResult());
            softAssertions.assertThat(cancelTransactionDto.amount()).isEqualTo(cancelTransaction.getAmount());
            softAssertions.assertThat(cancelTransactionDto.amountAfterTransaction()).isEqualTo(cancelTransaction.getAmountAfterTransaction());
        });
    }

    @Test
    @DisplayName("계좌가 일치 하지 않아 잔액 사용 취소 실패")
    void 계좌가_일치_하지_않아_잔액_사용_취소_실패() throws Exception{
        //given
        TransactionRequest request = new TransactionRequest("abc@abc.com", "12345678876", "1234",
                BigDecimal.valueOf(10000));

        TransactionCancelRequest cancelRequest = new TransactionCancelRequest(
                1L,"1234567891",10000L
        );

        Member member = Member.builder()
                .email("abc@abc.com")
                .nickName("abc")
                .password("1234")
                .accounts(new ArrayList<>())
                .build();

        Account account = Account.builder()
                .accountNumber("234567891")
                .balance(BigDecimal.valueOf(100000))
                .member(member)
                .password("1234")
                .build();

        member.getAccounts().add(account);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.USE)
                .amountAfterTransaction(account.getBalance().subtract(request.amount()))
                .transactionResult(TransactionResult.SUCCESS)
                .account(account)
                .amount(request.amount())
                .transactedAt(LocalDateTime.now())
                .build();
        //when
        when(accountRepository.getByAccountNumber(anyString())).thenReturn(account);
        when(transactionRepository.findById(any(Long.class))).thenReturn(Optional.of(transaction));

        //then
        assertThatThrownBy(() -> transactionService.transactionCancel(cancelRequest))
                .isInstanceOf(NotMatchTransactionAccountException.class);
    }

    @Test
    @DisplayName("취소한 잔액이 일치 하지 않아 잔액 사용 취소 실패")
    void 취소한_잔액이_일치_하지_않아_잔액_사용_취소_실패() throws Exception{
        //given
        TransactionRequest request = new TransactionRequest("abc@abc.com", "12345678876", "1234",
                BigDecimal.valueOf(10000));

        TransactionCancelRequest cancelRequest = new TransactionCancelRequest(
                1L,"234567891",20000L
        );

        Member member = Member.builder()
                .email("abc@abc.com")
                .nickName("abc")
                .password("1234")
                .accounts(new ArrayList<>())
                .build();

        Account account = Account.builder()
                .accountNumber("234567891")
                .balance(BigDecimal.valueOf(100000))
                .member(member)
                .password("1234")
                .build();

        member.getAccounts().add(account);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.USE)
                .amountAfterTransaction(account.getBalance().subtract(request.amount()))
                .transactionResult(TransactionResult.SUCCESS)
                .account(account)
                .amount(BigDecimal.valueOf(10000L))
                .transactedAt(LocalDateTime.now())
                .build();
        //when
        when(accountRepository.getByAccountNumber(anyString())).thenReturn(account);
        when(transactionRepository.findById(any(Long.class))).thenReturn(Optional.of(transaction));

        //then
        assertThatThrownBy(() -> transactionService.transactionCancel(cancelRequest))
                .isInstanceOf(NotMatchTransactionAmountException.class);
    }

    @Test
    @DisplayName("거래 내역이 1년이 지나 잔액 사용 취소 실패")
    void 거래_내역이_1년이_지나_잔액_사용_취소_실패() throws Exception{
        //given
        TransactionRequest request = new TransactionRequest("abc@abc.com", "12345678876", "1234",
                BigDecimal.valueOf(10000));

        TransactionCancelRequest cancelRequest = new TransactionCancelRequest(
                1L,"234567891",10000L
        );

        Member member = Member.builder()
                .email("abc@abc.com")
                .nickName("abc")
                .password("1234")
                .accounts(new ArrayList<>())
                .build();

        Account account = Account.builder()
                .accountNumber("234567891")
                .balance(BigDecimal.valueOf(100000))
                .member(member)
                .password("1234")
                .build();

        member.getAccounts().add(account);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.USE)
                .amountAfterTransaction(account.getBalance().subtract(request.amount()))
                .transactionResult(TransactionResult.SUCCESS)
                .account(account)
                .amount(BigDecimal.valueOf(10000L))
                .transactedAt(LocalDateTime.of(2023,1,12,0,0,0))
                .build();
        //when
        when(accountRepository.getByAccountNumber(anyString())).thenReturn(account);
        when(transactionRepository.findById(any(Long.class))).thenReturn(Optional.of(transaction));

        //then
        assertThatThrownBy(() -> transactionService.transactionCancel(cancelRequest))
                .isInstanceOf(OldTransactionOrderException.class);
    }
}
