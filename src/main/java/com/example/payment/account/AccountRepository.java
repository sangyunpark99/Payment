package com.example.payment.account;

import com.example.payment.account.entity.Account;
import com.example.payment.account.exception.NotExistAccountException;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByAccountNumber(final String accountNumber);

    Optional<Account> findByAccountNumber(final String accountNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.accountNumber = :accountNumber")
    Optional<Account> findByAccountNumberForUpdate(@Param("accountNumber") String accountNumber);

    default Account getByAccountNumber(final String accountNumber) {
        return findByAccountNumber(accountNumber).orElseThrow(NotExistAccountException::new);
    }

    default Account getByAccountNumberForUpdate(final String accountNumber) {
        return findByAccountNumberForUpdate(accountNumber).orElseThrow(NotExistAccountException::new);
    }
}
