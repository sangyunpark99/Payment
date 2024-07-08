package com.example.payment.member;

import com.example.payment.member.dto.request.MemberCreateRequest;
import com.example.payment.member.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("멤버를 저장한 후 조회한다.")
    void 멤버를_저장한_조회한다() throws Exception{
        //given
        final Member member = new MemberCreateRequest("abc@abc.com", "abc123", "abc").toEntity();

        //when
        final Member savedMember = memberRepository.save(member);

        em.flush();
        em.clear();

        //then
        final Member foundMember = memberRepository.findById(savedMember.getId()).get();

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(foundMember.getId()).isEqualTo(savedMember.getId());
            softAssertions.assertThat(foundMember.getEmail()).isEqualTo(savedMember.getEmail());
            softAssertions.assertThat(foundMember.getPassword()).isEqualTo(savedMember.getPassword());
            softAssertions.assertThat(foundMember.getNickName()).isEqualTo(savedMember.getNickName());
        });
    }
}
