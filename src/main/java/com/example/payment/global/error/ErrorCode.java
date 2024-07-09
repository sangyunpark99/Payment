package com.example.payment.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOT_EXIST_USER("존재하지 않는 사용자입니다."),
    CHANE_PASSWORD_SUCCESS("비밀번호 변경이 완료되었습니다.");

    private final String message;
}
