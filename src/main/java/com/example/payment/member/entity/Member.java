package com.example.payment.member.entity;

import com.example.payment.account.entity.Account;
import com.example.payment.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "members")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String nickName;

    @OneToMany(mappedBy = "member")
    private List<Account> accounts = new ArrayList<>();

    @Builder
    public Member(final String email, final String password, final String nickName, final List<Account> accounts) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.accounts = accounts;
    }

    public void updatePassword(final String password) {
        this.password = password;
    }
}
