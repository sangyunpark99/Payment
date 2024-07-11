package com.example.payment.account.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AccountUtilsTest {

    private static final int ACCOUNT_NUMBER_LENGTH = 10;

    @Test
    @DisplayName("랜덤한 10자리 계좌를 생성을 성공한다.")
    void 랜덤한_10자리_계좌_생성을_성공한다() throws Exception{
        //given

        //when
        final String account = AccountUtils.generateAccountNumber();

        //then
        Assertions.assertThat(account.length()).isEqualTo(ACCOUNT_NUMBER_LENGTH);
    }
}
