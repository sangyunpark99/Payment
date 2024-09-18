package com.example.payment.transfer;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.payment.account.AccountRepository;
import com.example.payment.account.domain.Account;
import com.example.payment.member.MemberRepository;
import com.example.payment.member.entity.Member;
import com.example.payment.transfer.dto.reqeust.TransferRequest;
import com.example.payment.transferHistory.TransferHistoryRepository;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TransferConcurrencyTest {

    @Autowired
    private TransferService transferService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferHistoryRepository transferHistoryRepository;

    @BeforeEach
    public void init() {

        Member member = Member.builder()
                .email("abc@abc.com")
                .nickName("abc")
                .password("abc123")
                .build();

        memberRepository.save(member);

        Account accountA = Account.builder()
                .accountNumber("1234567890")
                .balance(BigDecimal.valueOf(10000))
                .password("1234")
                .member(member)
                .build();

        Account accountB = Account.builder()
                .accountNumber("2345678901")
                .balance(BigDecimal.valueOf(0))
                .password("1235")
                .member(member)
                .build();

        Account accountC = Account.builder()
                .accountNumber("3456789012")
                .balance(BigDecimal.valueOf(0))
                .password("1236")
                .member(member)
                .build();

        accountRepository.save(accountA);
        accountRepository.save(accountB);
        accountRepository.save(accountC);
    }

    @Test
    @DisplayName("A계좌 이중이체 발생 문제")
    void A계좌_이중이체_발생_문제() throws Exception{

        TransferRequest requestToAccountB = new TransferRequest("1234567890", "2345678901", BigDecimal.valueOf(10000), "1234");
        TransferRequest requestToAccountC = new TransferRequest("1234567890", "3456789012", BigDecimal.valueOf(10000),"1234");

        //given
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        //when
        transferTo(executorService, latch, requestToAccountB);
        transferTo(executorService, latch, requestToAccountC);

        latch.await();

        //then
        Account accountB = accountRepository.getByAccountNumber("2345678901");
        Account accountC = accountRepository.getByAccountNumber("3456789012");

        long count = transferHistoryRepository.count();

        if(accountB.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            assertThat(accountB.getBalance().toBigInteger()).isEqualTo(BigInteger.valueOf(10000));
            assertThat(accountC.getBalance().toBigInteger()).isEqualTo(BigInteger.valueOf(0));
        }else {
            assertThat(accountB.getBalance().toBigInteger()).isEqualTo(BigInteger.valueOf(0));
            assertThat(accountC.getBalance().toBigInteger()).isEqualTo(BigInteger.valueOf(10000));
        }

        assertThat(count).isEqualTo(1);
    }

    private void transferTo(ExecutorService executorService, CountDownLatch latch, TransferRequest request) {
        executorService.submit(() -> {
            try {
                transferService.transfer(request);
            } finally {
                latch.countDown();
            }
        });
    }
}
