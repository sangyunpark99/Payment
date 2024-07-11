package com.example.payment.account;

import com.example.payment.account.dto.AccountDto;
import com.example.payment.account.dto.request.AccountCreateRequest;
import com.example.payment.account.entity.Account;
import com.example.payment.account.utils.AccountUtils;
import com.example.payment.member.MemberRepository;
import com.example.payment.member.entity.Member;
import com.example.payment.member.exception.NotMatchPassword;
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

    @Transactional
    public AccountDto createAccount(AccountCreateRequest request) {
        final Member member = memberRepository.getByEmail(request.email());

        if(!request.password().equals(member.getPassword())) {
            throw new NotMatchPassword();
        }

        String accountNumber;
        do {
            accountNumber = AccountUtils.generateAccountNumber();
        }
        while (accountRepository.existsByAccountNumber(accountNumber));
        final Account account = request.toEntity(member, accountNumber);
        final Account savedAccount = accountRepository.save(account);

        return new AccountDto(savedAccount.getAccountNumber(), savedAccount.getBalance());
    }
}
