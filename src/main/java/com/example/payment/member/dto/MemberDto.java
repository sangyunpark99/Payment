package com.example.payment.member.dto;

import com.example.payment.member.entity.Member;

public record MemberDto(Long id, String email, String nickname) {

    public static MemberDto of(Member member){
        return new MemberDto(member.getId(), member.getEmail(), member.getNickName());
    }
}
