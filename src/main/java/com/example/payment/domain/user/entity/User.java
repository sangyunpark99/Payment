package com.example.payment.domain.user.entity;

import com.example.payment.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class User extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String email;

    private String password;

    private String name;
}
