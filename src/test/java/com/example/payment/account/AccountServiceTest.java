package com.example.payment.account;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.payment.account.dto.AccountDto;
import com.example.payment.account.dto.request.AccountCreateRequest;
import com.example.payment.account.entity.Account;
import com.example.payment.member.MemberRepository;
import com.example.payment.member.entity.Member;
import com.example.payment.member.exception.NotExistMemberException;
import java.math.BigDecimal;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AccountService accountService;

    @Test
    @DisplayName("계좌를 등록한다.")
    void 계좌를_등록한다() throws Exception{
        //given
        final Member member = Member.builder()
                .email("abc@abc.com")
                .password("abc123")
                .nickName("abc")
                .build();

        final Account account = Account
                .builder()
                .accountNumber("1234567891")
                .balance(BigDecimal.ZERO)
                .member(member)
                .password("1234")
                .build();

        final AccountCreateRequest request = new AccountCreateRequest("abc@abc.com", "abc123", "1234");

        //when
        when(memberRepository.getByEmail(member.getEmail())).thenReturn(member);
        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        //then
        final AccountDto accountDto = accountService.createAccount(request);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(accountDto.accountNumber()).isEqualTo(account.getAccountNumber());
            softAssertions.assertThat(accountDto.balance()).isEqualTo(account.getBalance());
        });
    }

    @Test
    @DisplayName("존재하지 않는 계정으로 계좌를 생성시 계좌 등록을 실패한다.")
    void 존재하지_않는_계정으로_계좌를_생성시에_계좌_등록을_실패한다() throws Exception{
        //given
        final Member member = Member.builder()
                .email("abc@abc.com")
                .password("abc123")
                .nickName("abc")
                .build();

        final AccountCreateRequest request = new AccountCreateRequest("abc@abc.com", "abc123", "1234");

        //when
        when(memberRepository.getByEmail(member.getEmail())).thenThrow(new NotExistMemberException());

        //then

        Assertions.assertThatThrownBy(() ->accountService.createAccount(request))
                .isInstanceOf(NotExistMemberException.class);
    }

    @Test
    @DisplayName("계좌 생성시, 이미 계좌가 존재하면, 랜덤한 계좌 번호를 다시 생성한다.")
    public void 계좌_생성시_이미_계좌가_존재하면_랜덤한_계좌_번호를_다시_생성한다() {
        //given
        final Member member = Member.builder()
                .email("abc@abc.com")
                .password("abc123")
                .nickName("abc")
                .build();

        final Account account = Account
                .builder()
                .accountNumber("1234567891")
                .balance(BigDecimal.ZERO)
                .member(member)
                .password("1234")
                .build();

        final AccountCreateRequest request = new AccountCreateRequest("abc@abc.com", "abc123", "1234");

        //when
        when(memberRepository.getByEmail(member.getEmail())).thenReturn(member);
        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(true).thenReturn(false); // 처음엔 true, 마지막엔 false
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        accountService.createAccount(request);

        // then
        verify(accountRepository, times(2)).existsByAccountNumber(anyString());
    }
}