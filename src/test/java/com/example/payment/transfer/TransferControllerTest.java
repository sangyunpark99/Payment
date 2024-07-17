package com.example.payment.transfer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.payment.global.error.ControllerAdvice;
import com.example.payment.transfer.dto.reqeust.TransferRequest;
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

@WebMvcTest(controllers = TransferController.class)
public class TransferControllerTest {

    @MockBean
    TransferService transferService;

    @MockBean
    TransferController transferController;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(transferController)
                .setControllerAdvice(new ControllerAdvice())
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("계좌 이체에 성공한다.")
    void 계좌_이체에_성공한다() throws Exception{

        //given
        final TransferRequest request = new TransferRequest("0123456789", "1234567890", BigDecimal.valueOf(10000),"1234");

        //then
        mockMvc.perform(post("/api/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isOk());

    }

    @ParameterizedTest
    @MethodSource("provideInvalidTransferRequests")
    @DisplayName("필수 필드를 입력하지 않아서 계좌 이체를 실패한다.")
    void 필수_필드를_입력하지_않아서_회원탈퇴를_실패한다(TransferRequest request) throws Exception{
        //then
        mockMvc.perform(post("/api/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }


    private static Stream<Arguments> provideInvalidTransferRequests() {
        return Stream.of(
                Arguments.of(new TransferRequest(null, "1234567890", BigDecimal.valueOf(10000),"1234")),
                Arguments.of(new TransferRequest("1234567890", null, BigDecimal.valueOf(10000),"1234")),
                Arguments.of(new TransferRequest("0123456789", "1234567890", null,"1234")),
                Arguments.of(new TransferRequest(null, "1234567890", BigDecimal.valueOf(10000),null)),
                Arguments.of(new TransferRequest(null, "1234567890", BigDecimal.valueOf(0),"1234"))
        );
    }
}
