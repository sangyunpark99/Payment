package com.example.payment.account;

import com.example.payment.account.entity.Account;
import com.example.payment.account.exception.NotExistAccountException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByAccountNumber(final String accountNumber);

    Optional<Account> findByAccountNumber(final String accountNumber);

    default Account getByAccountNumber(final String accountNumber) {
        return findByAccountNumber(accountNumber).orElseThrow(NotExistAccountException::new);
    }
}
