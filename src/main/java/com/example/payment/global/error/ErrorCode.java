package com.example.payment.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOT_EXIST_USER("존재하지 않는 사용자입니다."),
    ALREADY_EXISTED_USER("이미 가입한 유저입니다."),
    NOT_EXIST_ACCOUNT("존재하지 않는 계좌입니다."),
    NOT_USE_ACCOUNT("해지된 계좌입니다."),
    ALREADY_EXISTED_FIVE_ACCOUNT("이미 5계의 계좌가 존재합니다."),
    ALREADY_EXISTED_BALANCE("계좌에 잔액이 존재합니다."),
    ALREADY_UNREGISTERED("이미 해지된 계좌입니다."),
    NOT_EQUAL_ACCOUNT_USER("입력한 계정과 계좌의 주인 계정이 일치하지 않습니다."),
    NOT_MATCH_PASSWORD("비밀번호가 일치하지 않습니다."),
    CHANE_PASSWORD_SUCCESS("비밀번호 변경이 완료되었습니다."),
    DELETE_MEMBER_SUCCESS("계정 삭제가 완료되었습니다."),

    NOT_ENOUGH_WITHDRAWAL_MONEY("이체에 필요한 돈이 부족합니다.");

    private final String message;
}
