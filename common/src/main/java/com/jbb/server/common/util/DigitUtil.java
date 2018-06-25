package com.jbb.server.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DigitUtil {

    /**
     * 验证整数（正整数和负整数）
     * 
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDigit(int digit) {
        String digit1 = Integer.toString(digit);
        String regex = "\\-?[1-9]\\d+";
        return Pattern.matches(regex, digit1);
    }

}
