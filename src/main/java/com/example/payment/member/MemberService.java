package com.example.payment.member;

import com.example.payment.member.dto.MemberDto;
import com.example.payment.member.dto.request.MemberCreateRequest;
import com.example.payment.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Long createMember(final MemberCreateRequest request) {
         return memberRepository.save(request.toEntity()).getId();
    }

    public MemberDto findMember(final Long id) {
        final Member member = memberRepository.getById(id);
        return MemberDto.of(member);
    }
}
