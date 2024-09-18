package com.example.payment.account;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.payment.account.domain.Account;
import com.example.payment.account.dto.AccountDto;
import com.example.payment.account.dto.request.AccountCreateRequest;
import com.example.payment.account.dto.request.AccountDeleteRequest;
import com.example.payment.account.exception.AlreadyExistedBalanceException;
import com.example.payment.account.exception.AlreadyUnregisteredException;
import com.example.payment.account.exception.NotEqualAccountUserException;
import com.example.payment.member.MemberRepository;
import com.example.payment.member.entity.Member;
import com.example.payment.member.exception.AlreadyExistedFiveAccount;
import com.example.payment.member.exception.NotExistMemberException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
        assertThatThrownBy(() ->accountService.createAccount(request))
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

    @Test
    @DisplayName("계좌 생성시, 이미 계좌를 5개 보유한다면, 계좌 생성을 실패한다.")
    void 계좌_생성시_이미_계좌를_5개_보유한다면_계좌_생성을_실패한다() throws Exception{
        //given

        final Member member = Member.builder()
                .email("abc@abc.com")
                .password("abc123")
                .nickName("abc")
                .accounts(new ArrayList<>())
                .build();

        final Account account1 = Account
                .builder()
                .accountNumber("1234567891")
                .balance(BigDecimal.ZERO)
                .member(member)
                .password("1234")
                .build();

        final Account account2 = Account
                .builder()
                .accountNumber("1234567892")
                .balance(BigDecimal.ZERO)
                .member(member)
                .password("1234")
                .build();

        final Account account3 = Account
                .builder()
                .accountNumber("1234567893")
                .balance(BigDecimal.ZERO)
                .member(member)
                .password("1234")
                .build();

        final Account account4 = Account
                .builder()
                .accountNumber("1234567894")
                .balance(BigDecimal.ZERO)
                .member(member)
                .password("1234")
                .build();

        final Account account5 = Account
                .builder()
                .accountNumber("1234567895")
                .balance(BigDecimal.ZERO)
                .member(member)
                .password("1234")
                .build();

        member.getAccounts().add(account1);
        member.getAccounts().add(account2);
        member.getAccounts().add(account3);
        member.getAccounts().add(account4);
        member.getAccounts().add(account5);

        final AccountCreateRequest request = new AccountCreateRequest("abc@abc.com", "abc123", "1234");

        //when
        when(memberRepository.getByEmail(member.getEmail())).thenReturn(member);

        //then
        assertThatThrownBy(() -> accountService.createAccount(request)).isInstanceOf(AlreadyExistedFiveAccount.class);
    }

    @Test
    @DisplayName("계좌번호로 계좌의 잔액을 가져온다.")
    void 계좌의_잔액을_가져온다() throws Exception{
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

        final String accountNumber = "01234567891";

        //when
        when(accountRepository.getByAccountNumber(accountNumber)).thenReturn(account);

        //then
        final AccountDto accountDto = accountService.getBalance(accountNumber);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(accountDto.accountNumber()).isEqualTo(account.getAccountNumber());
            softAssertions.assertThat(accountDto.balance()).isEqualTo(account.getBalance());
        });
    }

    @Test
    @DisplayName("이메일로 계좌의 정보를 가져온다.")
    void 이메일로_계좌의_정보를_가져온다() throws Exception{
        //given
        final Account account1 = Account.builder().accountNumber("123456781").password("1234").balance(BigDecimal.ZERO).build();
        final Account account2 = Account.builder().accountNumber("123456781").password("1234").balance(BigDecimal.ZERO).build();
        final Account account3 = Account.builder().accountNumber("123456781").password("1234").balance(BigDecimal.ZERO).build();

        final List<Account> accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);
        accounts.add(account3);

        final Member member = Member.builder()
                .email("abc@abc.com")
                .accounts(accounts)
                .password("abc123")
                .nickName("abc")
                .build();

        //when
        when(memberRepository.getByEmail(anyString())).thenReturn(member);

        //then
        List<AccountDto> resultAccounts = accountService.getAccounts(member.getEmail());

        assertThat(resultAccounts.size()).isEqualTo(3);

        System.out.println(resultAccounts.get(0));

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(resultAccounts.get(0).accountNumber()).isEqualTo("123456781");
            softAssertions.assertThat(resultAccounts.get(0).balance()).isEqualTo(BigDecimal.ZERO);
            softAssertions.assertThat(resultAccounts.get(1).accountNumber()).isEqualTo("123456781");
            softAssertions.assertThat(resultAccounts.get(1).balance()).isEqualTo(BigDecimal.ZERO);
            softAssertions.assertThat(resultAccounts.get(2).accountNumber()).isEqualTo("123456781");
            softAssertions.assertThat(resultAccounts.get(2).balance()).isEqualTo(BigDecimal.ZERO);
        });
    }

    @Test
    @DisplayName("계좌를 해지한다.")
    void 계좌를_해지한다() throws Exception{
        //given
        final AccountDeleteRequest request = new AccountDeleteRequest("abc@abc.com", "1234567891", "1234");

        List<Account> accounts = new ArrayList<>();
        final Member member = new Member("abc@abc.com", "1234", "abc", accounts);
        final Account account = new Account(member, "1234567891", BigDecimal.ZERO, "1234");
        member.getAccounts().add(account);

        //when
        when(accountRepository.getByAccountNumber(anyString())).thenReturn(account);

        //then
        final AccountDto accountDto = accountService.deleteAccount(request);

        assertThat(accountDto.unregisteredAt()).isEqualTo(account.getUnregisteredAt());
    }

    @Test
    @DisplayName("잘못된 이메일 정보로 계좌 해지를 실패한다.")
    void 잘못된_이메일_정보로_계좌_해지를_실패한다() throws Exception{
        //given
        final AccountDeleteRequest request = new AccountDeleteRequest("abc@abc.com", "1234567891", "1234");

        List<Account> accounts = new ArrayList<>();
        final Member member = new Member("cba@cba.com", "1234", "cba", accounts);
        final Account account = new Account(member, "1234567891", BigDecimal.ZERO, "1234");
        member.getAccounts().add(account);

        //when
        when(accountRepository.getByAccountNumber(anyString())).thenReturn(account);

        //then
        assertThatThrownBy(() -> accountService.deleteAccount(request)).isInstanceOf(
                NotEqualAccountUserException.class);
    }

    @Test
    @DisplayName("이미 해지된 계좌로 계좌 해지를 실패한다.")
    void 이미_해지된_계좌로_계좌_해지를_실패한다() throws Exception{
        //given
        final AccountDeleteRequest request = new AccountDeleteRequest("abc@abc.com", "1234567891", "1234");

        List<Account> accounts = new ArrayList<>();
        final Member member = new Member("abc@abc.com", "1234", "cba", accounts);
        final Account account = new Account(member, "1234567891", BigDecimal.ZERO, "1234");
        member.getAccounts().add(account);

        account.updateUnregisteredAt();

        //when
        when(accountRepository.getByAccountNumber(anyString())).thenReturn(account);

        //then
        assertThatThrownBy(() -> accountService.deleteAccount(request)).isInstanceOf(
                AlreadyUnregisteredException.class);
    }

    @Test
    @DisplayName("계좌 잔액이 0원이 아니여서 계좌 해지를 실패한다.")
    void 계좌_잔액이_0원이_아니여서_계좌_해지를_실패한다() throws Exception{
        //given
        final AccountDeleteRequest request = new AccountDeleteRequest("abc@abc.com", "1234567891", "1234");

        List<Account> accounts = new ArrayList<>();
        final Member member = new Member("abc@abc.com", "1234", "cba", accounts);
        final Account account = new Account(member, "1234567891", BigDecimal.ONE, "1234");
        member.getAccounts().add(account);

        //when
        when(accountRepository.getByAccountNumber(anyString())).thenReturn(account);

        //then
        assertThatThrownBy(() -> accountService.deleteAccount(request)).isInstanceOf(
                AlreadyExistedBalanceException.class);
    }
}