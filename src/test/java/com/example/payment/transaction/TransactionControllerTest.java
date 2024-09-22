package com.example.payment.transaction;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.payment.transaction.domain.TransactionResult;
import com.example.payment.transaction.dto.TransactionDto;
import com.example.payment.transaction.dto.request.TransactionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;
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

@WebMvcTest(controllers = TransactionController.class)
public class TransactionControllerTest {

    @MockBean
    TransactionService transactionService;

    @MockBean
    TransactionController transactionController;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("잔액 사용을 성공한다.")
    void 잔액_사용_성공한다() throws Exception{
        //given
        TransactionRequest request = new TransactionRequest("abc@abc.com", "1234567891", "1234", BigDecimal.valueOf(10000));
        TransactionDto transactionDto = new TransactionDto(1L, "1234567891", TransactionResult.SUCCESS, BigDecimal.valueOf(10000),
                LocalDateTime.now(), BigDecimal.valueOf(0));

        //when
        when(transactionService.transaction(request)).thenReturn(transactionDto);

        //then
        mockMvc.perform(post("/api/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andDo(print()).andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTransferRequests")
    @DisplayName("필수 필드를 입력하지 않아서 잔액 사용을 실패한다.")
    void 필수_필드를_입력하지_않아서_잔액사용을_실패한다(TransactionRequest request) throws Exception{
        //then
        mockMvc.perform(post("/api/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }


    private static Stream<Arguments> provideInvalidTransferRequests() {
        return Stream.of(
                Arguments.of(new TransactionRequest(null, "1234567891", "1234",BigDecimal.valueOf(10000))),
                Arguments.of(new TransactionRequest("abc@abc.com", null, "1234",BigDecimal.valueOf(10000))),
                Arguments.of(new TransactionRequest("abc@abc.com", "1234567891", null,BigDecimal.valueOf(10000))),
                Arguments.of(new TransactionRequest("abc@abc.com", "1234567891", "1234",null))
        );
    }
}
