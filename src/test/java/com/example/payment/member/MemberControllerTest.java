package com.example.payment.member;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.payment.global.error.ControllerAdvice;
import com.example.payment.member.dto.request.MemberCreateRequest;
import com.example.payment.member.dto.request.MemberDeleteRequest;
import com.example.payment.member.dto.request.PasswordUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
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
                .setControllerAdvice(new ControllerAdvice())
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("회원가입을 성공한다.")
    void 회원가입을_성공한다() throws Exception {
        //given
        final MemberCreateRequest request = new MemberCreateRequest("abc@abc.com", "abc123", "abc");

        //then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidMemberCreateRequest")
    @DisplayName("필수 필드를 입력하지 않아서 회원가입을 실패한다.")
    void 필수_필드를_입력하지_않아서_회원가입을_실패한다(MemberCreateRequest request) throws Exception {
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> provideInvalidMemberCreateRequest() {
        return Stream.of(
                Arguments.of(new MemberCreateRequest("abc@abc.com", null, "abc")),
                Arguments.of(new MemberCreateRequest(null, "abc123", "abc")),
                Arguments.of(new MemberCreateRequest("abc@abc.com", "abc123", null))
        );
    }

    @Test
    @DisplayName("회원가입을 잘못된 이메일 형식의 요청으로 실패한다.")
    void 회원가입을_잘못된_이메일_형식의_요청으로_실패한다() throws Exception {
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
    void 회원가입을_올바르지_않은_형식의_요청으로_실패한다() throws Exception {
        //given
        final MemberCreateRequest request = new MemberCreateRequest("abcabc.com", "abc123", "abc");

        //when, then
        mockMvc.perform(get("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("회원가입을 GET 메서드 요청으로 실패한다.")
    void 회원가입을_GET_메서드_요청으로_실패한다() throws Exception {
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
    @DisplayName("회원정보 조회를 성공한다.")
    void 회원정보_조회를_성공한다() throws Exception {

        //when, then
        mockMvc.perform(get("/api/members/1")
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("존재하지 않는 회원정보를 조회해 조회를 실패한다.")
    void 회원정보_조회를_실패한다() throws Exception {

        //when, then
        mockMvc.perform(get("/api/members/asdf")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("비밀번호 수정을 성공한다.")
    void 비밀번호_수정을_성공한다() throws Exception {
        //given
        final PasswordUpdateRequest request = new PasswordUpdateRequest("abc@abc.com", "abc124");

        //when, then
        mockMvc.perform(put("/api/members/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원탈퇴를 성공한다.")
    void 회원탈퇴를_성공한다() throws Exception {
        //given
        final MemberDeleteRequest request = new MemberDeleteRequest("abc@abc.com", "abc123", "박상윤");

        //then
        mockMvc.perform(delete("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidMemberDeleteRequests")
    @DisplayName("필수 필드를 입력하지 않아서 회원탈퇴를 실패한다.")
    void 필수_필드를_입력하지_않아서_회원탈퇴를_실패한다(MemberDeleteRequest request) throws Exception {
        mockMvc.perform(delete("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> provideInvalidMemberDeleteRequests() {
        return Stream.of(
                Arguments.of(new MemberDeleteRequest("abc@abc.com", null, "abc")),
                Arguments.of(new MemberDeleteRequest(null, "abc123", "abc")),
                Arguments.of(new MemberDeleteRequest("abc@abc.com", "abc123", null))
        );
    }
}