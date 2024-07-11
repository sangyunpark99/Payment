package com.example.payment.account.utils;

import java.security.SecureRandom;

public class AccountUtils {

    private static final SecureRandom random = new SecureRandom();
    private static final int ACCOUNT_NUMBER_LENGTH = 10;

    public static String generateAccountNumber(){
        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
            int value = random.nextInt(10);
            accountNumber.append(value);
        }

        return accountNumber.toString();
    }
}
