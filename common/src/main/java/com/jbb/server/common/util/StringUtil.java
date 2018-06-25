package com.jbb.server.common.util;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import com.jbb.server.common.Constants;

/**
 * StringUtil
 * 
 * @author VincentTang
 * @date 2017年12月20日
 */
public final class StringUtil {

    public static final int UNLIMITED_SPLIT = -1;

    private static ConcurrentLinkedQueue<SecureRandom> secureRandomPool = new ConcurrentLinkedQueue<>();
    private static String STR_NUMBERS = "123456789";

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    public static synchronized String generateIoUId() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE);
        int s = c.get(Calendar.SECOND);
        int ms = c.get(Calendar.MILLISECOND);
        return "JBB" + sdf.format(c.getTime()) + String.format("%08d",((hour * 3600 + m * 60 + s) * 1000 + ms)) + StringUtil.getRandomnum(3);
    }

    public static String printDateTime(Timestamp date, Calendar cal) {
        if (cal == null)
            return null;

        cal.clear();
        cal.setTime(date);
        return DatatypeConverter.printDateTime(cal);
    }

    /**
     * 获取数字随机串
     * 
     * @param length
     * @return
     */
    public static String getRandomnum(int length) {

        Random random = new Random();
        int size = STR_NUMBERS.length();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(size);
            buf.append(STR_NUMBERS.charAt(num));
        }
        return buf.toString();
    }

    /**
     * 检查string是否为空
     * 
     * @param arg 字符串
     * @return 是否为空串
     */
    public static boolean isEmpty(String arg) {
        return (arg == null) || (arg.trim().length() == 0);
    }

    public static String notNull(final String parameter, final String defaultValue) {
        String s = trimToNull(parameter);
        return s == null ? defaultValue : s;
    }

    public static String notNull(final Object s) {
        if (s == null)
            return "";

        return String.valueOf(s).trim();
    }

    /**
     * 如果原串不为空，返回原串；如果原串为空，返回替换的串。
     * 
     * @param source 源字符串
     * @param replace 替换的字符串
     * @return 如果原串不为空，返回原串；如果原串为空，返回替换的串。
     */
    public static String nvl(String source, String replace) {
        return source == null ? replace : source.trim();
    }

    /**
     * Trim 字符串
     * 
     * @return 如果字符串为空或者长度为0，返回null, 其它情况返回 trim()字符串。
     */
    public static String trimToNull(String source) {
        if (source == null)
            return null;

        String res = source.trim();
        return res.length() == 0 ? null : res;
    }

    /**
     * Replaces a pattern within a string with another
     * 
     * @param source the original string
     * @param pattern the pattern to be replaced
     * @param replace the new pattern
     * @return the replaced string
     */
    public static String replace(String source, String pattern, String replace) {
        String res;
        final int patternLen = pattern.length();

        if (source == null) {
            res = null;
        } else if (patternLen == 0) {
            res = source;
        } else {
            int found = source.indexOf(pattern, 0);

            if (found == -1) {
                // in most cases nothing to replace
                res = source;
            } else {
                StringBuilder sb = new StringBuilder(source.length() << 1);
                int start = 0;

                if (replace == null)
                    replace = "";

                do {
                    sb.append(source.substring(start, found));
                    sb.append(replace);
                    start = found + patternLen;
                    found = source.indexOf(pattern, start);
                } while (found != -1);

                sb.append(source.substring(start));
                res = sb.toString();
            }
        }

        return res;
    }

    /**
     * Replaces a pattern within a string with another
     * 
     * @param source the original StringBuilder
     * @param pattern the pattern to be replaced
     * @param replace the new pattern
     * @return the replaced StringBuilder
     */
    public static StringBuilder replace(StringBuilder source, String pattern, String replace) {
        final int patternLen = pattern.length();
        final int replaceLen = replace.length();
        int index = 0;

        while ((index = source.indexOf(pattern, index)) != -1) {
            source.replace(index, index + patternLen, replace);
            index += replaceLen;
        }

        return source;
    }

    /**
     * Replace a list of values to another in a string
     * 
     * @param str the string
     * @param encode values to replace
     * @param decode values to be used
     * @return a new string with replaced values or the original string
     */
    public static String replace(String str, String[] encode, String[] decode) {
        if (str == null)
            return null;

        StringBuilder res = null;
        int len = encode.length > decode.length ? encode.length : decode.length;

        for (int i = 0; i < len; i++) {
            if ((encode[i] == null) || (encode[i].length() == 0) || (decode[i] == null))
                continue;

            int ind = (res == null) ? str.indexOf(encode[i]) : res.indexOf(encode[i]);

            while (ind >= 0) {
                if (res == null)
                    res = new StringBuilder(str);

                res.replace(ind, ind + encode[i].length(), decode[i]);
                ind = res.indexOf(encode[i], ind + decode[i].length());
            }
        }

        return (res == null) ? str : res.toString();
    }

    /**
     * Replace patterns like [i] to provided values
     * 
     * @param str the string
     * @param replace values to be used
     * @return a new string with replaced values or the original string
     */
    public static String replaceIndexedPattern(String str, String[] replace) {
        if (str == null)
            return null;

        StringBuilder res = null;
        int len = replace.length;

        for (int i = 0; i < len; i++) {
            if (replace[i] == null)
                continue;

            String encode = "[" + i + ']';
            int ind = (res == null) ? str.indexOf(encode) : res.indexOf(encode);

            while (ind >= 0) {
                if (res == null)
                    res = new StringBuilder(str);

                res.replace(ind, ind + encode.length(), replace[i]);
                ind = res.indexOf(encode, ind + replace[i].length());
            }
        }

        return (res == null) ? str : res.toString();
    }

    /**
     * Remove a list of values from a string
     * 
     * @param str the string
     * @param remove values to remove
     * @return a new string with replaced values or the original string
     */
    public static String remove(String str, String[] remove) {
        if (str == null)
            return null;

        StringBuilder res = null;

        for (String aRemove : remove) {
            int ind = (res == null) ? str.indexOf(aRemove) : res.indexOf(aRemove);

            while (ind >= 0) {
                if (res == null)
                    res = new StringBuilder(str);

                res.delete(ind, ind + aRemove.length());
                ind = res.indexOf(aRemove, ind);
            }
        }

        return (res == null) ? str : res.toString();
    }

    public static String[] split(String source, char delimiter) {
        return split(source, delimiter, UNLIMITED_SPLIT);
    }

    /**
     * Split this string around the delimiter
     * 
     * @param source the string to be splitted
     * @param delimiter the delimiting character
     * @param limit the maximum amount of substrings (use UNLIMITED_SPLIT for unlimited)
     * @return the array of substrings
     */
    public static String[] split(String source, char delimiter, int limit) {
        if (source == null)
            return new String[0];

        int found = source.indexOf(delimiter);
        if (found < 0)
            return new String[] {source};

        final int sourceLen = source.length();
        final int len = (limit > 0) && (limit < sourceLen) ? limit : sourceLen + 1;
        int[] ind = new int[len + 1];
        int resLen = 1;

        ind[0] = -1;

        while ((found >= 0) && (resLen < len)) {
            ind[resLen] = found;
            resLen++;
            found = source.indexOf(delimiter, found + 1);
        }
        ind[resLen] = sourceLen;

        String[] res = new String[resLen];

        for (int i = 0; i < resLen; i++) {
            if (ind[i] + 1 == ind[i + 1])
                res[i] = "";
            else
                res[i] = source.substring(ind[i] + 1, ind[i + 1]);
        }

        return res;
    }

    public static int[] parseIntArray(String source, char delimiter, int radix, int defaultValue) {
        String[] valueStrArr = split(source, delimiter, StringUtil.UNLIMITED_SPLIT);

        int[] values = new int[valueStrArr.length];

        for (int i = 0; i < valueStrArr.length; i++) {
            if (isEmpty(valueStrArr[i])) {
                values[i] = defaultValue;
            } else {
                try {
                    values[i] = Integer.parseInt(valueStrArr[i].trim(), radix);
                } catch (NumberFormatException nfe) {
                    values[i] = defaultValue;
                }
            }
        }

        return values;
    }

    public static Integer[] parseIntegerArray(String source, char delimiter, int radix, int defaultValue) {
        String[] valueStrArr = split(source, delimiter, StringUtil.UNLIMITED_SPLIT);

        Integer[] values = new Integer[valueStrArr.length];

        for (int i = 0; i < valueStrArr.length; i++) {
            if (isEmpty(valueStrArr[i])) {
                values[i] = defaultValue;
            } else {
                try {
                    values[i] = Integer.valueOf(valueStrArr[i].trim(), radix);
                } catch (NumberFormatException nfe) {
                    values[i] = defaultValue;
                }
            }
        }

        return values;
    }

    public static int charCount(String str, char ch) {
        if (str == null)
            return 0;

        int cnt = 0;
        for (int i = str.length() - 1; i >= 0; i--) {
            if (str.charAt(i) == ch)
                cnt++;
        }

        return cnt;
    }

    /**
     * Concatenate several strings separated by a delimiter
     * 
     * @param str string array
     * @param delimiter string delimiter
     * @return a new concatenated string
     */
    public static String concatenate(String[] str, char delimiter) {
        if ((str == null) || (str.length == 0))
            return "";
        if (str.length == 1)
            return str[0];

        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < str.length; i++) {
            if (i > 0)
                buf.append(delimiter);
            if (str[i] != null)
                buf.append(str[i]);
        }

        return buf.toString();
    }

    /**
     * Concatenate several strings separated by a delimiter
     * 
     * @param str string array
     * @param delimiter string delimiter
     * @return a new concatenated string
     */
    public static String concatenate(String[] str, char delimiter, int maxLen) {
        StringBuilder buf = new StringBuilder();
        for (String s : str) {
            int buflen = buf.length();
            int slen = (s == null) ? 0 : s.length();
            if (maxLen > 0) {
                if ((buflen == 0 ? slen : buflen + 1 + slen) > maxLen) {
                    break;
                }
            }
            if (buflen > 0)
                buf.append(delimiter);
            if (slen > 0)
                buf.append(s);
        }
        return buf.length() > 0 ? buf.toString() : null;
    }

    /**
     * Concatenate several integers into string separated by a delimiter
     * 
     * @param val array
     * @param delimiter delimiter
     * @return a new concatenated string
     */
    public static String concatenate(int[] val, char delimiter) {
        StringBuilder buf = new StringBuilder();

        if (val != null) {
            for (int i = 0; i < val.length; i++) {
                if (i > 0)
                    buf.append(delimiter);
                buf.append(val[i]);
            }
        }

        return buf.toString();
    }

    /**
     * Concatenate byte array into string separated by a delimiter
     * 
     * @param val array
     * @param delimiter delimiter
     * @return a new concatenated string
     */
    public static String concatenate(byte[] val, char delimiter) {
        StringBuilder buf = new StringBuilder();

        if (val != null) {
            for (int i = 0; i < val.length; i++) {
                if (i > 0)
                    buf.append(delimiter);
                buf.append(val[i]);
            }
        }

        return buf.toString();
    }

    /**
     * Concatenate several short integers into string separated by a delimiter
     * 
     * @param val array
     * @param delimiter delimiter
     * @return a new concatenated string
     */
    public static String concatenate(short[] val, char delimiter) {
        StringBuilder buf = new StringBuilder();

        if (val != null) {
            for (int i = 0; i < val.length; i++) {
                if (i > 0)
                    buf.append(delimiter);
                buf.append(val[i]);
            }
        }

        return buf.toString();
    }

    /**
     * Concatenate long integers into string separated by a delimiter
     * 
     * @param val array
     * @param delimiter delimiter
     * @return a new concatenated string
     */
    public static String concatenate(long[] val, char delimiter) {
        StringBuilder buf = new StringBuilder();

        if (val != null) {
            for (int i = 0; i < val.length; i++) {
                if (i > 0)
                    buf.append(delimiter);
                buf.append(val[i]);
            }
        }

        return buf.toString();
    }

    public static String concatenate(Object[] val, char delimiter) {
        StringBuilder buf = new StringBuilder();

        if (val != null) {
            for (int i = 0; i < val.length; i++) {
                if (i > 0)
                    buf.append(delimiter);
                buf.append(val[i]);
            }
        }

        return buf.toString();
    }

    /**
     * Concatenate several strings separated by a delimiter
     * 
     * @param str strings list
     * @param delimiter string delimiter
     * @return a new concatenated string
     */
    public static String concatenate(List<String> str, char delimiter) {
        StringBuilder buf = new StringBuilder();

        if (str != null) {
            for (String s : str) {
                if (s != null)
                    buf.append(s);
                buf.append(delimiter);
            }
        }

        return buf.toString();
    }

    /**
     * Converts a string of HEX numbers to a string of ASCII symbols
     * 
     * @param hexString HEX string
     * @return ASCII representation for the input
     */
    static String hexToAscii(String hexString) {
        if ((hexString.length() % 2) != 0)
            return null;

        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < hexString.length(); i += 2) {
            String hexToken = hexString.substring(i, i + 2);
            buffer.append((char)Integer.parseInt(hexToken, 16));
        }

        return buffer.toString();
    }

    /**
     * Convers a string of ASCII simbols to a string of HEX numbers
     * 
     * @param asciiString ASCII string
     * @return HEX string
     */
    static String asciiToHex(String asciiString) {
        if (asciiString == null)
            return null;

        StringBuilder output = new StringBuilder();
        for (int i = 0; i < asciiString.length(); i++) {
            int b = asciiString.charAt(i) & 0xff;
            if (b < 0x10)
                output.append("0");
            output.append(Integer.toHexString(b));
        }
        return output.toString();
    }

    // All possible chars for representing a number as a String
    private static final char[] DIGITS =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    private static final int LOWER_CASE_RADIX = 36;

    /**
     * Return a randomly generated string containing decimals and letters
     * 
     * @param length output string length
     * @return a random characters string
     */
    public static String randomAlphaNum(int length) {
        return new String(randomAlphaNumArray(length, DIGITS.length));
    }

    /**
     * Return a randomly generated lower case string containing decimals and letters
     * 
     * @param length output string length
     * @return a random characters string
     */
    public static String randomLowerCaseAlphaNum(int length) {
        return new String(randomAlphaNumArray(length, LOWER_CASE_RADIX));
    }

    /**
     * Return a randomly generated char array containing decimals and letters
     * 
     * @param length output string length
     * @return a random characters array
     */
    static char[] randomAlphaNumArray(int length, int radix) {
        char[] buffer = new char[length];
        for (int i = 0; i < length; i++) {
            buffer[i] = DIGITS[(int)(Math.random() * radix)];
        }
        return buffer;
    }

    /**
     * Generate secure random URL safe string
     * 
     * @param size random byte array size
     * @return Base64 encoded URL safe random value
     */
    public static String secureRandom(int size) {
        byte[] rndValue = new byte[size];
        SecureRandom random = secureRandomPool.poll();
        if (random == null)
            random = new SecureRandom();
        random.nextBytes(rndValue);
        secureRandomPool.add(random);
        return CodecUtil.toBase64URLSafeString(rndValue);
    }

    /**
     * Generate secure random string
     * 
     * @param size random byte array size
     * @param minSize minimum result length
     * @return Base64 encoded random value
     */
    public static String secureRandom(int size, int minSize) {
        byte[] rndValue = new byte[size];
        SecureRandom random = secureRandomPool.poll();
        if (random == null)
            random = new SecureRandom();
        random.nextBytes(rndValue);
        secureRandomPool.add(random);
        return CodecUtil.toBase64String(rndValue, minSize);
    }

    public static String replaceWildcard(String value) {
        return value == null ? null : value.replace('*', '%');
    }

    public static String truncateBytes(String value, int size) {
        if (value == null)
            return null;
        if (size <= 0)
            return "";

        if (value.length() > size)
            value = value.substring(0, size);

        try {
            for (int l = value.length() - 1; l > 0; l--) {
                byte[] bytes = value.getBytes(Constants.UTF8);
                if (bytes.length <= size)
                    break;
                value = value.substring(0, l); // remove last character
            }
        } catch (UnsupportedEncodingException e) {
            // ignore
        }

        return value;
    }

    private static final Pattern SURROGATE_OUTLIER = Pattern.compile("[^\\u0000-\\uFFFF]");

    public static String removeSurrogateCharacters(String value) {
        if (value == null)
            return null;

        int len = value.length();
        if (len == 0)
            return value;

        Matcher matcher = SURROGATE_OUTLIER.matcher(value);

        boolean found = matcher.find();
        if (found) {
            StringBuilder sb = new StringBuilder();
            int lastEnd = 0;

            do {
                int start = matcher.start();
                if (lastEnd < start)
                    sb.append(value.subSequence(lastEnd, start));
                lastEnd = matcher.end();
                found = matcher.find();
            } while (found);

            if (lastEnd < len)
                sb.append(value.subSequence(lastEnd, len));

            return sb.toString();
        }

        return value;
    }

    public static String clearToNull(String value) {
        if (value == null)
            return null;

        String res = removeSurrogateCharacters(value).trim();
        return res.length() == 0 ? null : res;
    }

    public static String clearToNull(String value, int maxSize) {
        if (value == null)
            return null;

        String res = removeSurrogateCharacters(value).trim();
        return res.length() == 0 ? null : truncateBytes(res, maxSize);
    }

    public static boolean checkSurrogateCharacters(String value) {
        return value != null && SURROGATE_OUTLIER.matcher(value).find();
    }

    public static String prepareStringField(String value, int size) {
        return value == null ? null : truncateBytes(removeSurrogateCharacters(value), size);
    }

    public static int compareVersions(String v0, String v1) {
        if (v0 == null)
            return v1 == null ? 0 : -1;
        if (v1 == null)
            return 1;

        char dot = '.';
        int v0Start = 0;
        int v1Start = 0;
        boolean found0 = true;
        boolean found1 = true;
        int res = 0;

        while (found0 && found1 && (res == 0)) {
            String part0, part1;

            int v0End = v0.indexOf(dot, v0Start);
            if (v0End < 0) {
                part0 = v0.substring(v0Start);
                found0 = false;
            } else if (v0Start == v0End) {
                part0 = "0";
                v0Start++;
            } else {
                part0 = v0.substring(v0Start, v0End);
                v0Start = v0End + 1;
            }

            int v1End = v1.indexOf(dot, v1Start);
            if (v1End < 0) {
                part1 = v1.substring(v1Start);
                found1 = false;
            } else if (v1Start == v1End) {
                part1 = "0";
                v1Start++;
            } else {
                part1 = v1.substring(v1Start, v1End);
                v1Start = v1End + 1;
            }

            try {
                res = Integer.parseInt(part0, 16) - Integer.parseInt(part1, 16);
            } catch (NumberFormatException e) {
                res = part0.compareTo(part1);
            }
        }

        return res != 0 ? res : !found0 && !found1 ? 0 : found0 ? 1 : -1;
    }

    public static int checkSum(String str) {
        int sum = 0;
        for (char aCh : str.toCharArray()) {
            sum += aCh;
        }
        return sum;
    }

    public static int parseIntVal(String value) {
        if ((value != null) && (value.length() > 0)) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                // nothing to do
            }
        }
        return 0;
    }
}
