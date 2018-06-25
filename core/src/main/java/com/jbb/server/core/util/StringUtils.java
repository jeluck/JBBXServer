package com.jbb.server.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Random;
public class StringUtils {

	public static String filterHtml(String input) {
		if (input == null) {
			return null;
		}
		if (input.length() == 0) {
			return input;
		}
		input = input.replaceAll("&", "&amp;");
		input = input.replaceAll("<", "&lt;");
		input = input.replaceAll(">", "&gt;");
		input = input.replaceAll("'", "&#39;");
		input = input.replaceAll("\"", "&quot;");
		return input.replaceAll("\n", "<br>");
	}

	public static boolean isNull(String args) {
		if (args == null || args.length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isValidEmail(String email) {
		Pattern pattern = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
		return pattern.matcher(email).find();
	}

	public static boolean isValidUsername(String username) {
		boolean flag = true;

		// 长度不能小于4
		if (username.length() < 4)
			flag = false;

		if (flag) {
			// 只能为小写字母和数字
			Pattern pattern = Pattern.compile("^[a-z0-9]+$");
			flag = pattern.matcher(username).matches();
		}
		if (flag) {
			// 第一个字符不能为数字
			Pattern pattern = Pattern.compile("^[a-zA-Z]");
			flag = pattern.matcher(username).find();
		}
		return flag;
	}

	/**
	 * 生成随即密码
	 * 
	 * @param pwd_len
	 *            生成的密码的总长度
	 * @return 密码的字符串
	 */
	public static String generateRandomString(int pwd_len) {
		// 35是因为数组是从0开始的，26个字母+10个数字
		final int maxNum = 36;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0',
				'1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			// 生成随机数，取绝对值，防止生成负数，

			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1

			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}

		return pwd.toString();
	}

	/**
	 * 随机指定范围内N个不重复的数 最简单最基本的方法
	 * 
	 * @param min指定范围最小值
	 * @param max指定范围最大值
	 * @param n随机数个数
	 */
	public static int[] randomCommon(int min, int max, int n) {
		if (n > (max - min + 1) || max < min) {
			return null;
		}
		int[] result = new int[n];
		int count = 0;
		while (count < n) {
			int num = (int) (Math.random() * (max - min)) + min;
			boolean flag = true;
			for (int j = 0; j < n; j++) {
				if (num == result[j]) {
					flag = false;
					break;
				}
			}
			if (flag) {
				result[count] = num;
				count++;
			}
		}
		return result;
	}

	/** 产生一个随机的字符串 */
	public static String getRandomString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int num = random.nextInt(62);
			buf.append(str.charAt(num));
		}
		return buf.toString();
	}
	/*
	 * 生成随机数数字
	 */
	/** 产生一个随机的字符串 */
	public static String getRandomnum(int length) {
		String str = "0123456789";
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int num = random.nextInt(10);
			buf.append(str.charAt(num));
		}
		return buf.toString();
	}
	

	/** 去除字符串中的回车、换行符、制表符 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	//判断字符串是否是数字（包括一个小数点）负数
	  public static boolean isNumericzidai(String str) {
	        /*Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");*/
	        Pattern pattern = Pattern.compile("^[+-]?\\d+(\\.\\d+)?$");
	        Matcher isNum = pattern.matcher(str);
	        if (!isNum.matches()) {
	            return false;
	        }
	        return true;
	    }
	  //判断是否为手机号
	  public static boolean isMobile(String mobiles) {
		    Pattern p = Pattern.compile("^1(3|5|7|8|4)\\d{9}");
		    Matcher m = p.matcher(mobiles);
		    return m.matches();
		}
	public static void main(String[] args) {
		/*Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		System.out.println(calendar.getTime());*/
		/*HashMap<String, Integer> map =new HashMap<String, Integer>();
		map.put("aa",1);
		map.put("bb",2);
		System.out.println(map.get("cc"));*/
		//BigDecimal num=new BigDecimal("12345678901238456").setScale(2,BigDecimal.ROUND_HALF_UP);
		System.out.println(isNull(""));
		System.out.println(MD5.MD5Encode("123456"));
		System.out.println(StringUtils.generateRandomString(64));
		System.out.println(StringUtils.generateRandomString(64));
		System.out.println(StringUtils.generateRandomString(64));
		System.out.println(StringUtils.generateRandomString(64));
		System.out.println(StringUtils.generateRandomString(64));
        System.out.println(StringUtils.generateRandomString(64));
	}
	
}
