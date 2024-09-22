package com.example.payment.transaction;

import com.example.payment.transaction.dto.TransactionDto;
import com.example.payment.transaction.dto.request.TransactionRequest;
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
        TransactionDto transactionDto = transactionService.transaction(request);
        return ResponseEntity.ok(TransactionResponse.fromDto(transactionDto));
    }


}
