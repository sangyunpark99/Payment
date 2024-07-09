package com.example.payment.member;

import com.example.payment.member.dto.request.MemberCreateRequest;
import com.example.payment.member.entity.Member;
import com.example.payment.member.exception.NotExistMemberException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
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
    void 멤버를_저장한_후_조회한다() throws Exception{
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

    @Test
    @DisplayName("존재하지 않는 멤버를 아이디 조회할 경우, 예외를 던진다.")
    void 존재하지_않는_멤버를_조회할_경우_예외를_던진다() throws Exception{
        //given
        final Long id = 1L;

        // when & then
        Assertions.assertThatThrownBy(() -> memberRepository.getById(id))
                .isInstanceOf(NotExistMemberException.class);
    }

    @Test
    @DisplayName("멤버를 저장한 후, 이메일로 조회한다.")
    void 멤버를_저장한후_이메일로_조회한다() throws Exception{
        //given
        final Member member = new MemberCreateRequest("abc@abc.com", "abc123", "abc").toEntity();

        //when
        final Member savedMember = memberRepository.save(member);

        em.flush();
        em.clear();

        //then
        final Member foundMember = memberRepository.findByEmail(savedMember.getEmail()).get();

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(foundMember.getId()).isEqualTo(savedMember.getId());
            softAssertions.assertThat(foundMember.getEmail()).isEqualTo(savedMember.getEmail());
            softAssertions.assertThat(foundMember.getPassword()).isEqualTo(savedMember.getPassword());
            softAssertions.assertThat(foundMember.getNickName()).isEqualTo(savedMember.getNickName());
        });
    }

    @Test
    @DisplayName("존재하지 않는 멤버를 이메일로 조회할 경우, 예외를 던진다.")
    void 존재하지_않는_멤버를_이메일로_조회할_경우_예외를_던진다() throws Exception{
        //given
        final String email = "abc@abc.com";

        // when & then
        Assertions.assertThatThrownBy(() -> memberRepository.getByEmail(email))
                .isInstanceOf(NotExistMemberException.class);
    }
}
