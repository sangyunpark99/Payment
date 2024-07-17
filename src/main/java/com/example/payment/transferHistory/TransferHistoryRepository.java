package com.example.payment.transferHistory;

import com.example.payment.transferHistory.entity.TransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferHistoryRepository extends JpaRepository<TransferHistory, Long> {

}
