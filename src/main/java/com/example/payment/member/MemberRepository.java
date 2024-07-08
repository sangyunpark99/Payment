package com.example.payment.member;

import com.example.payment.member.entity.Member;
import com.example.payment.member.exception.NotExistMemberException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {

    default Member getById(final Long id) {
        return findById(id)
                .orElseThrow(NotExistMemberException::new);
    }
}
