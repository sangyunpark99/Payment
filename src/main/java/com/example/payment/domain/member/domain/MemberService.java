package com.example.payment.domain.member.domain;

import com.example.payment.domain.member.dto.request.MemberCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Long createMember(MemberCreateRequest request) {
         return memberRepository.save(request.toEntity()).getId();
    }
}
