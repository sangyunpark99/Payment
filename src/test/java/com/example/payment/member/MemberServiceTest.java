package com.example.payment.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.payment.member.dto.request.MemberCreateRequest;
import com.example.payment.member.entity.Member;
import org.assertj.core.api.Assertions;
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
    void 회원_등록_성공() {
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
        Assertions.assertThat(id).isEqualTo(member.getId());
    }

}
