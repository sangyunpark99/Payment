package com.example.payment.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.payment.member.dto.MemberDto;
import com.example.payment.member.dto.request.MemberCreateRequest;
import com.example.payment.member.dto.request.MemberDeleteRequest;
import com.example.payment.member.dto.request.PasswordUpdateRequest;
import com.example.payment.member.entity.Member;
import com.example.payment.member.exception.NotExistMemberException;
import com.example.payment.member.exception.NotMatchPasswordException;
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
    void 회원을_조회한다() throws Exception {
        //given
        final Long id = 1L;
        final Member member = Member.builder()
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

    @Test
    @DisplayName("회원의 비밀번호를 변경한다.")
    void 회원의_비밀번호를_변경한다() throws Exception {
        //given
        final Member member = Member.builder()
                .email("abc@abc.com")
                .password("abc123")
                .nickName("abc")
                .build();

        final PasswordUpdateRequest request = new PasswordUpdateRequest("abc@abc.com", "abc124");

        //when
        when(memberRepository.getByEmail(anyString())).thenReturn(member);
        memberService.changePassword(request);

        //then
        assertThat(request.password()).isEqualTo(member.getPassword());
    }

    @Test
    @DisplayName("회원을 삭제한다.")
    void 회원을_삭제한다() throws Exception {
        //given
        final Member member = Member.builder()
                .email("abc@abc.com")
                .password("abc123")
                .nickName("abc")
                .build();

        final MemberDeleteRequest request = new MemberDeleteRequest("abc@abc.com", "abc123", "abc");

        //when
        when(memberRepository.getByEmail(anyString())).thenReturn(member);
        memberService.deleteMember(request);

        //then
        verify(memberRepository, times(1)).delete(member);
    }

    @Test
    @DisplayName("존재하지 않은 회원을 삭제하여 실패한다.")
    void 존재하지_않은_회원을_삭제하여_실패한다() throws Exception {
        //given
        final MemberDeleteRequest request = new MemberDeleteRequest("abc@abc.com", "abc123", "abc");

        //when
        when(memberRepository.getByEmail(anyString())).thenThrow(NotExistMemberException.class);

        //then
        assertThatThrownBy(() -> memberService.deleteMember(request))
                .isInstanceOf(NotExistMemberException.class);

        verify(memberRepository, never()).delete(any(Member.class));
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않아 삭제를 실패한다.")
    void 비밀번호가_일치하지_않아_삭제를_실패한다() throws Exception {
        //given
        final MemberDeleteRequest request = new MemberDeleteRequest("abc@abc.com", "abc123", "abc");
        final Member member = Member.builder()
                .email("abc@abc.com")
                .password("abc124")
                .nickName("abc")
                .build();

        //when
        when(memberRepository.getByEmail(anyString())).thenReturn(member);

        //then
        assertThatThrownBy(() -> memberService.deleteMember(request))
                .isInstanceOf(NotMatchPasswordException.class);

        verify(memberRepository, never()).delete(any(Member.class));
    }
}
