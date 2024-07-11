package com.example.payment.account;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.payment.account.dto.AccountDto;
import com.example.payment.account.dto.request.AccountCreateRequest;
import com.example.payment.account.dto.response.AccountCreateResponse;
import com.example.payment.global.error.ControllerAdvice;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
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

@WebMvcTest(controllers = {AccountController.class})
public class AccountControllerTest {

    @MockBean
    AccountService accountService;

    @MockBean
    AccountController accountController;

    @MockBean
    AccountCreateResponse accountCreateResponse;

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
}
