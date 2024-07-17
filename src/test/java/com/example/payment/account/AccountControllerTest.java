package com.example.payment.account;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.payment.account.dto.AccountDto;
import com.example.payment.account.dto.request.AccountCreateRequest;
import com.example.payment.global.error.ControllerAdvice;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest(controllers = AccountController.class)
public class AccountControllerTest {

    @MockBean
    AccountService accountService;

    @MockBean
    AccountController accountController;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .setControllerAdvice(new ControllerAdvice())
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("계좌 생성을 성공한다.")
    void 회원가입을_성공한다() throws Exception {
        //given
        final AccountCreateRequest request = new AccountCreateRequest("abc@abc.com", "abc123", "1234");
        final AccountDto accountDto = new AccountDto("1234567891", BigDecimal.ZERO);

        //when
        when(accountService.createAccount(any(AccountCreateRequest.class))).thenReturn(accountDto);

        //then
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidAccountCreateRequest")
    @DisplayName("올바른 요청을 보내지 않아, 계좌 생성을 실패한다.")
    void 계좌_비밀번호를_4글자_입력하지_않아_계좌_생성을_실패한다() throws Exception {
        //given
        final AccountCreateRequest request = new AccountCreateRequest("abc@abc.com", "abc123", "123");
        final AccountDto accountDto = new AccountDto("1234567891", BigDecimal.ZERO);

        //when
        when(accountService.createAccount(any(AccountCreateRequest.class))).thenReturn(accountDto);

        //then
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> provideInvalidAccountCreateRequest() {
        return Stream.of(
                Arguments.of(new AccountCreateRequest("abc@abc.com", null, "1234")),
                Arguments.of(new AccountCreateRequest(null, "abc123", "1234")),
                Arguments.of(new AccountCreateRequest("abc@abc.com", "abc123", null)),
                Arguments.of(new AccountCreateRequest("abcabc.com", "abc123", null)),
                Arguments.of(new AccountCreateRequest("abc@abc.com", "abc123", "123"))
        );
    }

    @Test
    @DisplayName("계좌의 잔액 확인을 성공한다.")
    void 계좌의_잔액_확인을_성공한다() throws Exception{
        //given
        final String accountNumber = "1234567891";
        final AccountDto accountDto = new AccountDto(accountNumber, BigDecimal.ZERO);

        //when
        when(accountService.getBalance(anyString())).thenReturn(accountDto);

        //then
        mockMvc.perform(get("/api/accounts/details?accountNumber=1234567891")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("유저가 생성한 계좌들에 대한 목록을 반환받는다.")
    void 유저가_생성한_계좌들에_대한_목록을_반환받는다() throws Exception{
        //given
        List<AccountDto> accounts = new ArrayList<>();

        accounts.add(new AccountDto("1234567891", BigDecimal.ZERO));
        accounts.add(new AccountDto("1456789123", BigDecimal.ZERO));
        accounts.add(new AccountDto("2322323233", BigDecimal.ZERO));

        //when
        when(accountService.getAccounts(anyString())).thenReturn(accounts);

        //then
        mockMvc.perform(get("/api/accounts?email=abc@abc.com")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("존재하지 않는 계좌들에 대한 목록을 반환받지 못한다.")
    void 존재하지_않는_계좌들에_대한_목록을_반환받지_못한다() throws Exception{
        //given
        List<AccountDto> accounts = new ArrayList<>();

        accounts.add(new AccountDto("1234567891", BigDecimal.ZERO));
        accounts.add(new AccountDto("1456789123", BigDecimal.ZERO));
        accounts.add(new AccountDto("2322323233", BigDecimal.ZERO));

        //when, then
        mockMvc.perform(get("/api/accounts?email")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }
}