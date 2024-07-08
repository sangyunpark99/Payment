package com.example.payment.member;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.payment.global.error.ControllerAdvice;
import com.example.payment.member.dto.request.MemberCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest(controllers = {MemberController.class})
public class MemberControllerTest {

    @MockBean
    MemberService memberService;

    @MockBean
    MemberController memberController;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController)
                .setControllerAdvice(ControllerAdvice.class)
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("회원가입을 성공한다.")
    void 회원가입을_성공한다() throws Exception {
        //given
        final MemberCreateRequest request = new MemberCreateRequest("abc@abc.com", "abc123", "abc");
        final Long id = 1L;

        //when
        when(memberService.createMember(request)).thenReturn(id);

        //then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입을 잘못된 이메일 형식의 요청으로 실패한다.")
    void 회원가입을_잘못된_이메일_형식의_요청으로_실패한다() throws Exception{
        //given
        final MemberCreateRequest request = new MemberCreateRequest("abcabc.com", "abc123", "abc");

        //when, then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입을 올바르지 않은 형식의 요청으로 실패한다.")
    void 회원가입을_올바르지_않은_형식의_요청으로_실패한다() throws Exception{
        //given
        final MemberCreateRequest request = new MemberCreateRequest("abcabc.com", "abc123", "abc");

        //when, then
        mockMvc.perform(get("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입을 GET 메서드 요청으로 실패한다.")
    void 회원가입을_GET_메서드_요청으로_실패한다() throws Exception{
        //given
        final MemberCreateRequest request = new MemberCreateRequest("abcabc.com", "abc123", "abc");

        //when, then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }
}
