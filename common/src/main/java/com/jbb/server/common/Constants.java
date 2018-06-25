package com.jbb.server.common;

import java.nio.charset.Charset;
import java.sql.Timestamp;

/**
 * @Type Constants.java
 * @Desc
 * @author VincentTang
 * @date 2017年10月30日 下午12:03:36
 * @version
 */
public class Constants {
    public static final String UTF8 = "UTF-8";
    public static final Charset UTF8_CHARSET = Charset.forName(UTF8);
    public static final String API_KEY = "API_KEY";

    // Last system date 2030-01-01 00:00:00
    public static final long LAST_SYSTEM_MILLIS = 1893427200000L;
    public static final Timestamp LAST_SYSTEM_TIMESTAMP = new Timestamp(LAST_SYSTEM_MILLIS);

    // Seconds in one day
    public static final int ONE_DAY_SECONDS = 24 * 3600;
    // Milliseconds in one day
    public static final long ONE_DAY_MILLIS = ONE_DAY_SECONDS * 1000L;
    public static final long ONE_HOUR_MILLIS = 3600000L;

    // Standard value delimiter
    public static final char DELIMITER = ':';

    // Event Log Name
    public static String Event_LOG_EVENT_DEVICE_NAME = "device";
    public static String Event_LOG_EVENT_DEVICE_ACTION_ACTIVATE = "activate";

    // Event Log User
    public static String Event_LOG_EVENT_USER_EVENT = "user";
    public static String Event_LOG_EVENT_USER_ACTION_LOGIN = "login";
    public static String Event_LOG_EVENT_USER_ACTION_REGISTER = "register";
    public static String Event_LOG_EVENT_USER_ACTION_UPDATE = "update";
    public static String Event_LOG_EVENT_USER_LABEL_PALTFORM = "platform";
    //推送给平安赠险
    public static String Event_LOG_EVENT_USER_ACTION_POST_TO_PA = "post2pa";

    // Iou Type
    public static final int IOU_CATEGORY_HALL = 1; // 大厅
    public static final int IOU_CATEGORY_FOLLOW = 2; // 用户关注的
    public static final int IOU_CATEGORY_PUBLISY = 3; // 用户发布的
    public static final int IOU_CATEGORY_LEND = 4; // 用户出借的
    public static final int IOU_CATEGORY_LENDER_FILL = 5; // 出借人补借条
    public static final int IOU_CATEGORY_BORROWER_FILL = 6; // 借款人补借条
    public static final int IOU_CATEGORY_IOUS_LENDER = 7; // 出借人借出的借条
    public static final int IOU_CATEGORY_IOUS_BORROWER = 8; // 借款人借入的借条
    public static final int IOU_CATEGORY_IOUS_PUBLISH = 9; // 借款人发布的借条

    // Iou Intend status
    public static final int IOU_INTEND_YES = 0; // 有意向
    public static final int IOU_INTEND_REJECT = 1; // 借款人拒绝
    public static final int IOU_INTEND_NO = 2; // 取消意向
    public static final int IOU_INTEND_APPROVE = 3; // 借款人同意
    public static final int IOU_INTEND_MODIFY_APPROVE = 4; // 意向人同意修改
    public static final int IOU_INTEND_MODIFY_REJECT = 5; // 意向人拒绝修改

    // User property name
    public static final String SESAME_CREDIT_SCORE = "sesameCreditScore";
    public static final String CHANNEL_SOURCE_ID = "channel_source_id";
    public static final String FILTER_EXPRESSION = "filterExpression";
    public static final String SIGNIN_PLATFROM = "signin-platform";
    
    
    //User Priv name
    public static final String USER_PRIV_QQ = "qq";
    public static final String USER_PRIV_WECHAT = "wechat";
    public static final String USER_PRIV_INFO = "info";
    public static final String USER_PRIV_PHONE = "phone";
    
    // User property ReadAt value
    public static final String USER_P_READAT = "readAt_";
    public static final String USER_P_READAT_HALL = "readAt_hall";
    public static final String USER_P_READAT_PUBLISH = "readAt_publish";
    public static final String USER_P_READAT_FOLLOW = "readAt_follow";
    public static final String USER_P_READAT_LEND = "readAt_lend";
    
    //iou
    public static final int IOU_STATISTIC_EFFECT = 1;
    public static final int IOU_STATISTIC_EFFECT_WITHOUT_COMPLETED = 2;
    public static final int[] IOU_STATUS_EFFECT = {1,5,6,7,8,9,10,11,12,13,14};
    public static final int[] IOU_STATUS_EFFECT_WITHOUT_COMPLETED = {1,5,6,7,8,10,11,12,13};
    public static final int[] IOU_STATUS_INEFFECT = {0,2,3,4,20,30};
    public static final int[] IOU_STATUS_INEFFECT_EXCLUDE_FILL = {0,2,3,4};
    public static final int IOU_STATUS_LENDER_FILL = 30;
    public static final int IOU_STATUS_BORROWER_FILL = 20;
    
    //iou
    public static final String PRODUCT_AUTH="auth";
    public static final String PRODUCT_IOU="iou";
    
    //User Verify Type
    public static final String VERIFY_TYPE_REALNAME="realName";
    public static final String VERIFY_TYPE_VIDEO="video";
    public static final String VERIFY_TYPE_MOBILE="mobile";
    
    
    //JBB MGT
    public static final String OSS_BUCKET_TD_PRELOAN="jbb-td-preloan";
    public static final String OSS_BUCKET_CLUB_PRELOAN="jbb-club-preloan";
    public static final String OSS_BUCKET_YX_PRELOAN="jbb-yx-report";
    
    //JBB MGT PERMISSONS
    public static final int MGT_P_SYSADMIN =1;
    public static final int MGT_P_ORGADMIN =2;
    public static final int MGT_P_MGTRPORT =3;
    public static final int MGT_P_USERREPORT =4;
    public static final int MGT_P_RECHARGE =5;
    public static final int MGT_P_ACCSETTING =6;
    public static final int MGT_P_CHANNEL =7;
    public static final int MGT_P_MGTCHANNEL =8;
    public static final int MGT_P_DATAFLOW =9;
    public static final int MGT_P_ASSIGN =10;
    public static final int MGT_P_TELEMARKETING =11;
    public static final int MGT_P_PRE_CHECK =12;
    public static final int MGT_P_FIN_CHECK =13;
    public static final int MGT_P_IOU =14;
    public static final int MGT_P_BILL =15;
    public static final int MGT_P_LOAN_MGT =16;
    public static final int MGT_P_COLLECTION =17;
    
    //JBB CLUB notify
    public static final String NOTIFY_TYPE_ACQUIRE = "ACQUIRE";
    public static final String NOTIFY_TYPE_REPORT = "REPORT";
    
    public static final String NOTIFY_EVENT_CREATED = "CREATED";
    public static final String NOTIFY_EVENT_SUCCESS = "SUCCESS";
    public static final String NOTIFY_EVENT_FAILURE = "FAILURE";
    public static final String NOTIFY_EVENT_TIMEOUT = "TIMEOUT";
    
    //LOAN STATUS
    public static final int STATUS_FINISH_7 = 7;
    public static final int STATUS_FINISH_8 = 8;
    
    //JBB Organization
    public static final int JBB_ORG = 1;
    
    //PLATFOMRS
    public static final int PLATFORM_JBB = 1;

    //channel Url
    public static final String CHANNELURL = "http://h5.finrisk.cn/login?s=";
}
