package com.example.payment.account;

import com.example.payment.account.domain.Account;
import com.example.payment.account.domain.AccountStatus;
import com.example.payment.account.dto.AccountDto;
import com.example.payment.account.dto.request.AccountCreateRequest;
import com.example.payment.account.dto.request.AccountDeleteRequest;
import com.example.payment.account.exception.AlreadyUnregisteredException;
import com.example.payment.account.exception.AlreadyExistedBalanceException;
import com.example.payment.account.exception.NotEqualAccountUserException;
import com.example.payment.account.utils.AccountUtils;
import com.example.payment.global.exception.NotMatchPasswordException;
import com.example.payment.member.MemberRepository;
import com.example.payment.member.entity.Member;
import com.example.payment.member.exception.AlreadyExistedFiveAccount;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;

    private static final Integer MAX_ACCOUNT_VALUE = 5;

    @Transactional
    public AccountDto createAccount(final AccountCreateRequest request) {
        final Member member = memberRepository.getByEmail(request.email());

        if (!request.password().equals(member.getPassword())) {
            throw new NotMatchPasswordException();
        }

        if (member.getAccounts().size() >= MAX_ACCOUNT_VALUE) {
            throw new AlreadyExistedFiveAccount();
        }

        String accountNumber;
        do {
            accountNumber = AccountUtils.generateAccountNumber();
        }
        while (accountRepository.existsByAccountNumber(accountNumber));
        final Account account = request.toEntity(member, accountNumber);
        final Account savedAccount = accountRepository.save(account);

        return AccountDto.from(member.getEmail(),
                savedAccount.getAccountNumber(), account.getBalance(),
                account.getRegisteredAt(),null);
    }

    @Transactional
    public AccountDto deleteAccount(final AccountDeleteRequest request) {

        final Account account = accountRepository.getByAccountNumber(request.accountNumber());
        final Member member = account.getMember();

        if(!member.getEmail().equals(request.email())) {
            throw new NotEqualAccountUserException();
        }

        if(account.getStatus().equals(AccountStatus.NOT_IN_USE)) {
            throw new AlreadyUnregisteredException();
        }

        if(account.getBalance().compareTo(BigDecimal.ZERO) == 1) {
            throw new AlreadyExistedBalanceException();
        }

        account.updateUnregisteredAt();

        return AccountDto.from(member.getEmail(), account.getAccountNumber(), null, null, account.getUnregisteredAt());
    }

    @Transactional(readOnly = true)
    public AccountDto getBalance(final String accountNumber) {
        final Account account = accountRepository.getByAccountNumber(
                accountNumber);

        return AccountDto.from(null, account.getAccountNumber(),
                account.getBalance(), account.getRegisteredAt(),null);
    }

    @Transactional(readOnly = true)
    public List<AccountDto> getAccounts(final String email) {
        final Member member = memberRepository.getByEmail(email);

        return member.getAccounts().stream()
                .map(account -> AccountDto.from(email,
                        account.getAccountNumber(), account.getBalance(),
                        account.getRegisteredAt(),null)).collect(
                        Collectors.toList());
    }
}
