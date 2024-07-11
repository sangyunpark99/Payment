package com.example.payment.account;

import com.example.payment.account.dto.request.AccountCreateRequest;
import com.example.payment.account.dto.response.AccountCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountCreateResponse> createAccount(@Valid @RequestBody AccountCreateRequest request) {
        final AccountCreateResponse response = AccountCreateResponse.getAccountCreateResponse(accountService.createAccount(request));
        return ResponseEntity.ok(response);
    }
}
