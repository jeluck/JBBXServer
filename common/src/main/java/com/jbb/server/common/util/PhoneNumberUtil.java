package com.jbb.server.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jbb.server.common.PropertyManager;

public class PhoneNumberUtil {

    private static Pattern pattern = null;
    static {
        String regStr = PropertyManager.getProperty("jbb.phonenumber.reg");
        pattern = Pattern.compile(regStr);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {

        if (StringUtil.isEmpty(phoneNumber) || phoneNumber.length() != 11) {
            return false;
        }

        Matcher mat = pattern.matcher(phoneNumber);
        return mat.matches();
    }
  
}
