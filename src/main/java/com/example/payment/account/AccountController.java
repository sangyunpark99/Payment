package com.example.payment.account;

import com.example.payment.account.dto.request.AccountCreateRequest;
import com.example.payment.account.dto.response.AccountCreateResponse;
import com.example.payment.account.dto.response.AccountDetailResponse;
import com.example.payment.account.dto.response.AccountsDetailsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountCreateResponse> createAccount(@Valid @RequestBody final AccountCreateRequest request) {
        final AccountCreateResponse response = AccountCreateResponse.to(accountService.createAccount(request));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/details")
    public ResponseEntity<AccountDetailResponse> getAccountDetail(@RequestParam("accountNumber") final String accountNumber) {
        final AccountDetailResponse response = AccountDetailResponse.to(accountService.getBalance(accountNumber));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<AccountsDetailsResponse> getAccounts(@RequestParam("email") final String email) {
        final AccountsDetailsResponse response = new AccountsDetailsResponse(accountService.getAccounts(email));
        return ResponseEntity.ok(response);
    }
}
