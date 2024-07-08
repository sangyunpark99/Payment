package com.example.payment.member.exception;

public class NotExistMemberException extends RuntimeException {

    public NotExistMemberException(final String message) {
        super(message);
    }

    public NotExistMemberException() {
        this("존재하지 않는 회원입니다.");
    }
}
