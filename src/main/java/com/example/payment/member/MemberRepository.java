package com.example.payment.member;

import com.example.payment.member.entity.Member;
import com.example.payment.member.exception.NotExistMemberException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(final String email);

    default Member getById(final Long id) {
        return findById(id)
                .orElseThrow(NotExistMemberException::new);
    }

    default Member getByEmail(final String email){
        return findByEmail(email)
                .orElseThrow(NotExistMemberException::new);
    }
}
