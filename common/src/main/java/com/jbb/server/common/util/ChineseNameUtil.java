 package com.jbb.server.common.util;

import java.util.regex.Pattern;

public class ChineseNameUtil {
     public static boolean checkChinese(String chinese) {
         String regex = "^([\\u4e00-\\u9fa5]){2,7}$";
         return Pattern.matches(regex, chinese);
     }

}
