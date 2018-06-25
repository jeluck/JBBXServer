package com.jbb.server.common.util;

public class UTF8Util {

    public static boolean isUtf8(byte[] bytes) {
        if (bytes != null && bytes.length > 0) {
            boolean foundStartByte = false;
            int requireByte = 0;
            for (int i = 0; i < bytes.length; i++) {
                byte current = bytes[i];
                // 当前字节小于128，标准ASCII码范围
                if ((current & 0x80) == 0x00) {
                    if (foundStartByte) {
                        foundStartByte = false;
                        requireByte = 0;
                    }
                    continue;
                    // 当前以0x110开头，标记2字节编码开始，后面需紧跟1个0x10开头字节
                } else if ((current & 0xC0) == 0xC0) {
                    foundStartByte = true;
                    requireByte = 1;
                    // 当前以0x1110开头，标记3字节编码开始，后面需紧跟2个0x10开头字节
                } else if ((current & 0xE0) == 0xE0) {
                    foundStartByte = true;
                    requireByte = 2;
                    // 当前以0x11110开头，标记4字节编码开始，后面需紧跟3个0x10开头字节
                } else if ((current & 0xF0) == 0xF0) {
                    foundStartByte = true;
                    requireByte = 3;
                    // 当前以0x111110开头，标记5字节编码开始，后面需紧跟4个0x10开头字节
                } else if ((current & 0xE8) == 0xE8) {
                    foundStartByte = true;
                    requireByte = 4;
                    // 当前以0x1111110开头，标记6字节编码开始，后面需紧跟5个0x10开头字节
                } else if ((current & 0xEC) == 0xEC) {
                    foundStartByte = true;
                    requireByte = 5;
                    // 当前以0x10开头，判断是否满足utf8编码规则
                } else if ((current & 0x80) == 0x80) {
                    if (foundStartByte) {
                        requireByte--;
                        // 出现多个0x10开头字节，个数满足，发现utf8编码字符，直接返回
                        if (requireByte == 0) {
                            return true;
                        }
                        // 虽然经当前以0x10开头，但前一字节不是以0x110|0x1110|0x11110肯定不是utf8编码，直接返回
                    } else {
                        return false;
                    }
                    // 发现0x8000~0xC000之间字节，肯定不是utf8编码
                } else {
                    return false;
                }
            }
        }
        return false;
    }
}
