package com.example.payment.transaction;

import com.example.payment.transaction.dto.TransactionDto;
import com.example.payment.transaction.dto.request.TransactionCancelRequest;
import com.example.payment.transaction.dto.request.TransactionRequest;
import com.example.payment.transaction.dto.response.TransactionCancelResponse;
import com.example.payment.transaction.dto.response.TransactionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> transaction(@RequestBody @Valid TransactionRequest request) {
        TransactionDto transactionDto = transactionService.transactionUse(request);
        return ResponseEntity.ok(TransactionResponse.fromDto(transactionDto));
    }

    @PostMapping("/cancel")
    public ResponseEntity<TransactionDto> transactionCancel(@RequestBody @Valid TransactionCancelRequest request) {
        TransactionDto transactionDto = transactionService.transactionCancel(request);
        TransactionCancelResponse response = TransactionCancelResponse.fromDto(transactionDto);
        System.out.println(response.transactedAt());
        return ResponseEntity.ok(transactionDto);
    }
}
