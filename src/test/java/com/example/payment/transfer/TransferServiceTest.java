package com.example.payment.transfer;

import static org.mockito.Mockito.when;

import com.example.payment.account.AccountRepository;
import com.example.payment.account.entity.Account;
import com.example.payment.account.exception.NotExistAccountException;
import com.example.payment.member.exception.NotMatchPasswordException;
import com.example.payment.transfer.dto.reqeust.TransferRequest;
import com.example.payment.transfer.exception.NotEnoughWithdrawalMoney;
import com.example.payment.transferHistory.TransferHistoryRepository;
import java.math.BigDecimal;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @Mock
    TransferHistoryRepository transferHistoryRepository;

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    TransferService transferService;

    @Test
    @DisplayName("A계좌에서 B계좌로 이체한후, 이체내역을 기록한다.")
    void A계좌에서_B계좌로_이체한후_이체내역을_기록한다() throws Exception {
        //given
        final String withdrawalAccountNumber = "0123456789";
        final String depositAccountNumber = "123456789";
        final BigDecimal transferAmount = BigDecimal.valueOf(10000);
        final String accountNumber = "1234";

        final TransferRequest request = new TransferRequest(withdrawalAccountNumber, depositAccountNumber,
                transferAmount, accountNumber);

        final Account withdrawalAccount = Account.builder()
                .accountNumber(withdrawalAccountNumber)
                .balance(BigDecimal.valueOf(10000))
                .password("1234")
                .build();

        final Account depositAccount = Account.builder()
                .accountNumber(depositAccountNumber)
                .balance(BigDecimal.valueOf(10000))
                .password("1235")
                .build();

        //when
        when(accountRepository.getByAccountNumber(withdrawalAccountNumber)).thenReturn(withdrawalAccount);
        when(accountRepository.getByAccountNumber(depositAccountNumber)).thenReturn(depositAccount);

        //then
        transferService.transfer(request);
    }

    @Test
    @DisplayName("A계좌가 존재하지 않아 A계좌에서 B계좌로 이체를 실패한다")
    void A계좌가_존재하지_않아_A계좌에서_B계좌로_이체를_실패한다() throws Exception {
        //given
        final String withdrawalAccountNumber = "0123456789";
        final String depositAccountNumber = "123456789";
        final BigDecimal transferAmount = BigDecimal.valueOf(10000);
        final String accountNumber = "1234";

        final TransferRequest request = new TransferRequest(withdrawalAccountNumber, depositAccountNumber,
                transferAmount, accountNumber);

        //when
        when(accountRepository.getByAccountNumber(withdrawalAccountNumber)).thenThrow(new NotExistAccountException());

        //then
        Assertions.assertThatThrownBy(() -> transferService.transfer(request)).isInstanceOf(NotExistAccountException.class);
    }

    @Test
    @DisplayName("B계좌가 존재하지 않아 A계좌에서 B계좌로 이체를 실패한다")
    void B계좌가_존재하지_않아_A계좌에서_B계좌로_이체를_실패한다() throws Exception {
        //given
        final String withdrawalAccountNumber = "0123456789";
        final String depositAccountNumber = "123456789";
        final BigDecimal transferAmount = BigDecimal.valueOf(10000);
        final String accountNumber = "1234";

        final TransferRequest request = new TransferRequest(withdrawalAccountNumber, depositAccountNumber,
                transferAmount, accountNumber);

        final Account withdrawalAccount = Account.builder()
                .accountNumber(withdrawalAccountNumber)
                .balance(BigDecimal.valueOf(10000))
                .password("1234")
                .build();

        //when
        when(accountRepository.getByAccountNumber(withdrawalAccountNumber)).thenReturn(withdrawalAccount);
        when(accountRepository.getByAccountNumber(depositAccountNumber)).thenThrow(new NotExistAccountException());

        //then
        Assertions.assertThatThrownBy(() -> transferService.transfer(request)).isInstanceOf(NotExistAccountException.class);
    }

    @Test
    @DisplayName("A계좌에 돈이 충분하지 않아 A계좌에서 B계좌로 이체를 실패한다")
    void A계좌에_돈이_충분하지_않아_A계좌에서_B계좌로_이체를_실패한다() throws Exception {
        //given
        final String withdrawalAccountNumber = "0123456789";
        final String depositAccountNumber = "123456789";
        final BigDecimal transferAmount = BigDecimal.valueOf(10000);
        final String accountNumber = "1234";

        final TransferRequest request = new TransferRequest(withdrawalAccountNumber, depositAccountNumber,
                transferAmount, accountNumber);

        final Account withdrawalAccount = Account.builder()
                .accountNumber(withdrawalAccountNumber)
                .balance(BigDecimal.ZERO)
                .password("1234")
                .build  ();

        final Account depositAccount = Account.builder()
                .accountNumber(depositAccountNumber)
                .balance(BigDecimal.valueOf(10000))
                .password("1235")
                .build();

        //when
        when(accountRepository.getByAccountNumber(withdrawalAccountNumber)).thenReturn(withdrawalAccount);
        when(accountRepository.getByAccountNumber(depositAccountNumber)).thenReturn(depositAccount);

        //then
        Assertions.assertThatThrownBy(() -> transferService.transfer(request)).isInstanceOf(NotEnoughWithdrawalMoney.class);
    }

    @Test
    @DisplayName("A계좌의 비밀번호를 틀려 A계좌에서 B계좌로 이체를 실패한다")
    void A계좌의_비밀번호를_틀려_A계좌에서_B계좌로_이체를_실패한다() throws Exception {
        //given
        final String withdrawalAccountNumber = "0123456789";
        final String depositAccountNumber = "123456789";
        final BigDecimal transferAmount = BigDecimal.valueOf(10000);
        final String accountNumber = "1235";

        final TransferRequest request = new TransferRequest(withdrawalAccountNumber, depositAccountNumber,
                transferAmount, accountNumber);

        final Account withdrawalAccount = Account.builder()
                .accountNumber(withdrawalAccountNumber)
                .balance(BigDecimal.ZERO)
                .password("1234")
                .build  ();

        //when
        when(accountRepository.getByAccountNumber(withdrawalAccountNumber)).thenReturn(withdrawalAccount);

        //then
        Assertions.assertThatThrownBy(() -> transferService.transfer(request)).isInstanceOf(NotMatchPasswordException.class);
    }
}
