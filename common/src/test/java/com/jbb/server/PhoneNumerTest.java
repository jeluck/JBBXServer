package com.jbb.server;

import com.jbb.server.common.util.PhoneNumberUtil;

public class PhoneNumerTest {

    public static void main(String[] args) {

        boolean flag = PhoneNumberUtil.isValidPhoneNumber("13511111234");
        System.out.println(flag);

        flag = PhoneNumberUtil.isValidPhoneNumber(null);
        System.out.println(flag);

        flag = PhoneNumberUtil.isValidPhoneNumber("18777269858");
        System.out.println(flag);

    }

}
