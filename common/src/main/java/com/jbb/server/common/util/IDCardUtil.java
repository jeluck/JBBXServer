package com.jbb.server.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IDCardUtil {

    public static int ID_CARD_LEN = 18;

    public static String SEX_F = "女";
    public static String SEX_M = "男";

    /**
     * 每位加权因子
     */
    private static int POWER[] = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static String CHECK_CODE[] = {"1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2", "1"};

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    public static boolean validate(String idcard) {
        if (!checkLengthAndNum(idcard)) {
            return false;
        }
        if (getBirthday(idcard) == null) {
            return false;
        }
        if (!checkCode(idcard)) {
            return false;
        }
        return true;
    }

    private static boolean checkCode(String idcard) {
        String idcard17 = idcard.substring(0, 17);
        String idcard18Code = idcard.substring(17, 18);
        char c[] = idcard17.toCharArray();

        int bit[] = converCharToInt(c);
        int sum17 = getPowerSum(bit);
        String checkCode = CHECK_CODE[sum17 % 11];

        return checkCode.equals(idcard18Code.toLowerCase());
    }

    public static String getCode(String idcard) {
        return idcard.substring(0, 6);
    }
    
    public static String getProvinceCode(String idcard) {
        return idcard.substring(0, 2);
    }

    
    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     * 
     * @param bit
     * @return
     */
    private static int getPowerSum(int[] bit) {

        int sum = 0;

        if (POWER.length != bit.length) {
            return sum;
        }

        for (int i = 0; i < bit.length; i++) {
            for (int j = 0; j < POWER.length; j++) {
                if (i == j) {
                    sum = sum + bit[i] * POWER[j];
                }
            }
        }
        return sum;
    }

    /**
     * 将字符数组转为整型数组
     * 
     * @param c
     * @return
     * @throws NumberFormatException
     */
    private static int[] converCharToInt(char[] c) throws NumberFormatException {
        int[] a = new int[c.length];
        int k = 0;
        for (char temp : c) {
            a[k++] = Integer.parseInt(String.valueOf(temp));
        }
        return a;
    }

    private static boolean checkLengthAndNum(String idcard) {

        if (StringUtil.isEmpty(idcard)) {
            return false;

        }
        if (idcard.length() != ID_CARD_LEN) {
            return false;
        }
        // 检查前17位数字
        String idcard17 = idcard.substring(0, 17);
        if (!isDigital(idcard17)) {
            return false;
        }

        return true;
    }

    private static boolean isDigital(String str) {
        return str.matches("^[0-9]*$");
    }

    public static Date getBirthday(String idcard) {

        String birthday = idcard.substring(6, 14);

        try {
            Date birthDate = sdf.parse(birthday);
            String tmpDate = sdf.format(birthDate);
            if (!tmpDate.equals(birthday)) {// 出生年月日不正确
                return null;
            }
            return birthDate;

        } catch (ParseException e1) {
            return null;
        }
    }

    public static int calculateAge(String idcard) {
        String birthYear = idcard.substring(6, 10);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return year - Integer.valueOf(birthYear);
    }

    public static String calculateSex(String idcard) {
        String sexStr = idcard.substring(16, 17);
        int sex = Integer.valueOf(sexStr);
        return sex % 2 == 1 ? SEX_M : SEX_F;
    }

    public static boolean isBelongToProvince(String idCard, String[] provinces) {
        if (provinces == null || provinces.length == 0 || idCard == null || idCard.length() != 18) {
            return false;
        }
        String provinceCode = idCard.substring(0, 2);
        for (int i = 0; i < provinces.length; i++) {
            if (provinceCode.equals(provinces[i])) {
                return true;
            }
        }
        return false;
    }

}
