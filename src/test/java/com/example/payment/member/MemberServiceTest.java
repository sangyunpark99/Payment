package com.example.payment.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.example.payment.member.dto.MemberDto;
import com.example.payment.member.dto.request.MemberCreateRequest;
import com.example.payment.member.entity.Member;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    MemberService memberService;

    @Test
    @DisplayName("회원을 등록한다.")
    void 회원을_등록한다() {
        // given
        final Member member = Member.builder()
                .id(1L)
                .email("abc@abc.com")
                .password("abc123")
                .nickName("abc")
                .build();

        final MemberCreateRequest request = new MemberCreateRequest("abc@abc.com", "abc123", "abc");

        // when
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        final Long id = memberService.createMember(request);

        // then
        assertThat(id).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("회원을 조회한다.")
    void 회원을_조회한다() throws Exception{
        //given
        final Long id = 1L;
        final Member member = Member.builder()
                .id(1L)
                .email("abc@abc.com")
                .password("abc123")
                .nickName("abc")
                .build();

        //when
        when(memberRepository.getById(anyLong())).thenReturn(member);

        final MemberDto memberDto = memberService.findMember(id);

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(memberDto.id()).isEqualTo(member.getId());
            softAssertions.assertThat(memberDto.email()).isEqualTo(member.getEmail());
            softAssertions.assertThat(memberDto.nickname()).isEqualTo(member.getNickName());
        });

    }
}
