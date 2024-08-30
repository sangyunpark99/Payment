package com.example.payment.deposit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.payment.deposit.dto.request.DepositRequest;
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

@WebMvcTest(controllers = DepositController.class)
public class DepositControllerTest {

    @MockBean
    DepositController depositController;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(depositController)
                .setControllerAdvice(new ControllerAdvice())
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("예금을 성공한다.")
    void 예금을_성공한다() throws Exception{
        //given
        final DepositRequest request = new DepositRequest("1234567891", "1234", BigDecimal.valueOf(10000));

        //then
        mockMvc.perform(post("/api/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDepositRequest")
    @DisplayName("올바른 요청을 보내지 않아, 예금을 실패한다.")
    void 계좌_비밀번호를_4글자_입력하지_않아_계좌_생성을_실패한다(DepositRequest request) throws Exception {

        //then
        mockMvc.perform(post("/api/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> provideInvalidDepositRequest() {
        return Stream.of(
                Arguments.of(new DepositRequest(null, "1234", BigDecimal.valueOf(10000))),
                Arguments.of(new DepositRequest("1234567891", null, BigDecimal.valueOf(10000))),
                Arguments.of(new DepositRequest("1234567891", "1234", null))
        );
    }
}
