package com.example.payment.deposit;

import static org.mockito.Mockito.when;

import com.example.payment.account.AccountRepository;
import com.example.payment.account.entity.Account;
import com.example.payment.deposit.dto.request.DepositRequest;
import com.example.payment.global.exception.NotMatchPasswordException;
import com.example.payment.member.entity.Member;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DepositServiceTest {

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    DepositService depositService;

    @Test
    @DisplayName("돈을 예금한다.")
    void 돈을_예금한다() throws Exception{
        //given
        final DepositRequest request = new DepositRequest("1234567891","1234", BigDecimal.valueOf(10000));
        final Member member = new Member("abc@abc.com", "abc123","abc",new ArrayList<>());
        final Account account = new Account(member,"1234567891",BigDecimal.ZERO, "1234");

        //when
        when(accountRepository.getByAccountNumberForUpdate(request.account())).thenReturn(account);

        //then
        depositService.deposit(request);
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않아 돈을 예금하지 못한다.")
    void 비밀번호가_일치하지_않아_돈을_예금하지_못한다() throws Exception{
        //given
        final DepositRequest request = new DepositRequest("1234567891","1235", BigDecimal.valueOf(10000));
        final Member member = new Member("abc@abc.com", "abc123","abc",new ArrayList<>());
        final Account account = new Account(member,"1234567891",BigDecimal.ZERO, "1234");

        //when
        when(accountRepository.getByAccountNumberForUpdate(request.account())).thenReturn(account);

        //then
        Assertions.assertThatThrownBy(() -> depositService.deposit(request)).isInstanceOf(NotMatchPasswordException.class);
    }
}
