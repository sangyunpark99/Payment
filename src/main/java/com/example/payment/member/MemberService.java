package com.example.payment.member;

import com.example.payment.member.dto.MemberDto;
import com.example.payment.member.dto.request.MemberCreateRequest;
import com.example.payment.member.dto.request.MemberDeleteRequest;
import com.example.payment.member.dto.request.PasswordUpdateRequest;
import com.example.payment.member.entity.Member;
import com.example.payment.global.exception.NotMatchPasswordException;
import com.example.payment.member.exception.AlreadyExistedUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Long createMember(final MemberCreateRequest request) {

        Member member = request.toEntity();

        if(checkAlreadyExistedUser(member)) {
            throw new AlreadyExistedUserException();
        }

        return memberRepository.save(member).getId();
    }

    @Transactional(readOnly = true)
    public MemberDto findMember(final Long id) {
        final Member member = memberRepository.getById(id);
        return MemberDto.of(member);
    }

    @Transactional
    public void changePassword(final PasswordUpdateRequest request) {
        final String email = request.email();
        final Member member = memberRepository.getByEmail(email);
        member.updatePassword(request.password());
    }

    @Transactional
    public void deleteMember(final MemberDeleteRequest request) {

        final Member member = memberRepository.getByEmail(request.email());

        if (!member.getPassword().equals(request.password())) {
            throw new NotMatchPasswordException();
        }
        memberRepository.delete(member);
    }

    private boolean checkAlreadyExistedUser(Member member) {
        return memberRepository.findByEmail(member.getEmail()).isPresent();
    }
}
