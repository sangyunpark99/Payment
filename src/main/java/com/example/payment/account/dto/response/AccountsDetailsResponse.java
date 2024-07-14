package com.example.payment.account.dto.response;

import com.example.payment.account.dto.AccountDto;
import java.util.List;

public record AccountsDetailsResponse(List<AccountDto> accounts) {
}
