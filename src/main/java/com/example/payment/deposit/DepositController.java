package com.example.payment.deposit;

import com.example.payment.deposit.dto.request.DepositRequest;
import com.example.payment.deposit.dto.response.DepositResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deposit")
@RequiredArgsConstructor
public class DepositController {

    private static final String DEPOSIT_SUCCESS = "예금을 완료 하였습니다.";

    private final DepositService depositService;

    @PostMapping
    public ResponseEntity<DepositResponse> deposit(@Valid @RequestBody final DepositRequest request) {
        depositService.deposit(request);
        return ResponseEntity.ok(new DepositResponse(DEPOSIT_SUCCESS));
    }
}
