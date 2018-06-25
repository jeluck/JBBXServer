package com.jbb.server.common.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * DateUtil 日期操作工具类
 * 
 * @author VincentTang
 * @date 2017年12月20日
 */
public class DateUtil {
    public static final long HOUR_MILLSECONDES = 3600000L; // 24*3600*1000
    public static final long DAY_MILLSECONDES = 86400000L; // 24*3600*1000

    private static DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

    public static Timestamp calTimestamp(long currentTimeMillis, long offset) {
        return new Timestamp(currentTimeMillis + offset);
    }

    public static Timestamp getCurrentTimeStamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Calendar getCurrentCalendar() {
        return Calendar.getInstance();
    }

    /**
     * @return current time without milliseconds
     */
    public static long getTodayStartCurrentTime() {
        long ms = System.currentTimeMillis();
        return ms - (ms % DAY_MILLSECONDES);
    }

    public static long getTodayStartTs() {
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = Calendar.getInstance(tz);
        cal.setTime(new Date(System.currentTimeMillis()));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static Long nvl(Long source, Long replace) {
        return source == null ? replace : source;
    }

    /**
     * @return current time without milliseconds
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * @return current time in seconds
     */
    public static long getCurrentTimeSec() {
        return System.currentTimeMillis() / 1000L;
    }

    public static Calendar getCurrentCalendar(TimeZone tz) {
        if (tz == null)
            tz = TimeZone.getDefault();
        return Calendar.getInstance(tz);
    }

    public static long getStartTsOfDay(long ts) {
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = Calendar.getInstance(tz);
        cal.setTime(new Date(ts));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static int calDays(long ts1, long ts2) {
        long tsStart1 = getStartTsOfDay(ts1);
        long tsStart2 = getStartTsOfDay(ts2);
        return (int)((tsStart2 - tsStart1) / DAY_MILLSECONDES);
    }

    public static Timestamp parseStr(String time) {
        try {
            return new Timestamp(format.parse(time).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            // nothing todo
        }
        return null;
    }
    
    public static Timestamp parseStrnew(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return new Timestamp(sdf.parse(time).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            // nothing todo
        }
        return null;
    }
    
    public static String getNumDateString(int day,String date_s) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date=sdf.parse(date_s);
        Calendar now = Calendar.getInstance();  
        now.setTime(date);  
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day); 
        return sdf.format(now.getTime());  
    }
    
    /**
     * 获取当前月的第一天
     * @return
     */
    public static LocalDate getlastDayOfThisMonth() {
        LocalDate today = LocalDate.now();
        LocalDate lastDayOfThisMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        return lastDayOfThisMonth;
    }
    
    /**
     * 获取当前月的最后一天
     * @return
     */
    public static LocalDate getfirstDayOfThisMonth() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfThisMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        return firstDayOfThisMonth;
    }
    
    /**
     * 获取当前周的周一
     * @return
     */
    public static LocalDate getNowWeekMonday() {
        LocalDate today = LocalDate.now();
        LocalDate nowWeekMonday = today.with(DayOfWeek.MONDAY);
        return nowWeekMonday;
    }
    
    /**
     * 获取当前周的周末
     * @return
     */
    public static LocalDate getNowWeekSunday() {
        LocalDate today = LocalDate.now();
        LocalDate nowWeekSunday = today.with(DayOfWeek.SUNDAY);
        return nowWeekSunday;
    }
    

}
