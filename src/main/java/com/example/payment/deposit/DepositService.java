package com.example.payment.deposit;

import com.example.payment.account.AccountRepository;
import com.example.payment.account.entity.Account;
import com.example.payment.deposit.dto.request.DepositRequest;
import com.example.payment.global.exception.NotMatchPasswordException;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepositService {

    private final AccountRepository accountRepository;

    @Transactional
    public void deposit(DepositRequest request) {
        final String accountNumber = request.account();
        final String password = request.password();
        final BigDecimal amount =  request.amount();

        Account account = accountRepository.getByAccountNumberForUpdate(accountNumber);

        if(!checkPassword(password, account.getPassword())) {
            throw new NotMatchPasswordException();
        }

        account.updateBalance(amount);
    }

    private boolean checkPassword(final String requestPassword, final String accountPassword) {
        return accountPassword.equals(requestPassword);
    }
}

