package com.jbb.server.core.domain;

import java.util.EnumMap;

public enum LcCmdType {
    WECHATREQ("请求交换微信", 101), WECHATAPPROVE("同意交换微信", 102), WECHATREJECT("拒绝交换微信", 103), QQREQ("请求交换QQ", 104),
    QQAPPROVE("同意交换QQ", 105), QQREJECT("拒绝交换QQ", 106), INFOREQ("请求交换用户信息", 107), INFOAPPROVE("同意交换用户信息", 108),
    INFOREJECT("拒绝交换用户信息", 109), PHONEREQ("请求交换电话", 110), PHONEAPPROVE("同意交换交换电话", 111), PHONEREJECT("拒绝交换电话", 112),
    IOUINFO("借条信息", 113), QQINFO("QQ信息", 114), WECHATINFO("微信信息", 115), USERINFO("个人资料", 116),
    PHONEINFO("电话信息", 117);

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private LcCmdType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (LcCmdType type : LcCmdType.values()) {
            if (type.getIndex() == index) {
                return type.name;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public static LcCmdType getType(int index) {
        for (LcCmdType type : LcCmdType.values()) {
            if (type.getIndex() == index) {
                return type;
            }
        }
        return null;
    }

    public static EnumMap<LcCmdType, String[]> cmdTypeMap = new EnumMap<LcCmdType, String[]>(LcCmdType.class);

    static {

        String[] reqQQMsg = {"请求交换QQ已发送", "对方请求同您交换QQ"};
        cmdTypeMap.put(QQREQ, reqQQMsg);
        String[] reqWechatMsg = {"请求交换微信已发送", "对方请求同您交换微信"};
        cmdTypeMap.put(WECHATREQ, reqWechatMsg);
        String[] reqInfoMsg = {"请求交换个人资料已发送", "对方请求同您交换个人资料"};
        cmdTypeMap.put(INFOREQ, reqInfoMsg);
        String[] reqPhoneMsg = {"请求交换电话已发送", "对方请求同您交换电话"};
        cmdTypeMap.put(PHONEREQ, reqPhoneMsg);
        String[] approveQQMsg = {"同意对方交换QQ", "对方已同意交换QQ"};
        cmdTypeMap.put(QQAPPROVE, approveQQMsg);
        String[] approveWechatMsg = {"同意对方交换微信", "对方已同意交换微信"};
        cmdTypeMap.put(WECHATAPPROVE, approveWechatMsg);
        String[] approveInfoMsg = {"同意对方交换个人资料", "对方已同意交换个人资料"};
        cmdTypeMap.put(INFOAPPROVE, approveInfoMsg);
        String[] approvePhoneMsg = {"同意对方交换电话", "对方已同意交换电话"};
        cmdTypeMap.put(PHONEAPPROVE, approvePhoneMsg);
        String[] rejectQQMsg = {"拒绝对方交换QQ", "对方拒绝交换QQ"};
        cmdTypeMap.put(QQREJECT, rejectQQMsg);
        String[] rejectWechatMsg = {"拒绝对方交换微信", "对方拒绝交换微信"};
        cmdTypeMap.put(WECHATREJECT, rejectWechatMsg);
        String[] rejectInfoMsg = {"拒绝对方交换个人资料", "对方拒绝交换个人资料"};
        cmdTypeMap.put(INFOREJECT, rejectInfoMsg);
        String[] rejectPhoneMsg = {"拒绝对方交换电话", "对方拒绝交换电话"};
        cmdTypeMap.put(PHONEREJECT, rejectPhoneMsg);
    }

}