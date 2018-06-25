package com.jbb.server.shared.rs;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.TimeZone;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.jbb.server.common.Constants;
import com.jbb.server.common.exception.MissingParameterException;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.CommonUtil;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;

public class Util {
    private static final DatatypeFactory datatypeFactory;
    
    static {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new Error(e);
        }
    }
    
    public static Calendar parseDateTime(String timeStr, TimeZone defaultTimezone)
    throws WrongParameterValueException {
        if (timeStr == null) return null;

        try {
            long millis = Long.parseLong(timeStr);
            checkTimestampValue(millis);
            Calendar cal = DateUtil.getCurrentCalendar(defaultTimezone);
            cal.setTimeInMillis(millis);
            return cal;
        } catch (NumberFormatException ee) {
            Calendar cal = parseXsdDateTime(timeStr, defaultTimezone);
            checkTimestampValue(cal.getTimeInMillis());
            return cal;
        }
    }

    public static Timestamp parseTimestamp(String timeStr, TimeZone defaultTimezone)
    throws WrongParameterValueException {
        if (timeStr == null) return null;

        try {
            long millis = Long.parseLong(timeStr);
            checkTimestampValue(millis);
            return new Timestamp(millis / 1000L * 1000L);
        } catch (NumberFormatException ee) {
            Calendar cal = parseXsdDateTime(timeStr, defaultTimezone);
            long millis = cal.getTimeInMillis();
            checkTimestampValue(millis);
            return new Timestamp(millis);
        }
    }

    private static void checkTimestampValue(long time) {
        if ((time <= 0) || (time > Constants.LAST_SYSTEM_MILLIS)) {
            throw new WrongParameterValueException("Wrong date value " + time);
        }
    }
    
    private static Calendar parseXsdDateTime(String lexicalXSDTime, TimeZone defaultTimezone)
    throws WrongParameterValueException {
        try {
            // try to parse as xsd:dateTime format
            XMLGregorianCalendar xmlCal = datatypeFactory.newXMLGregorianCalendar(lexicalXSDTime);

            if (xmlCal.getTimezone() == DatatypeConstants.FIELD_UNDEFINED) {
                if (defaultTimezone == null) {
                    throw new WrongParameterValueException("Missing time zone in date " + lexicalXSDTime);
                }
                
                return xmlCal.toGregorianCalendar(defaultTimezone, null, null);
            } else {
                return xmlCal.toGregorianCalendar();
            }
        } catch (IllegalArgumentException e) {
            throw new WrongParameterValueException("Invalid date format " + lexicalXSDTime);
        }
    }
    
    public static String printDateTime(Timestamp date, Calendar cal) {
        if (cal == null) return null;

        cal.clear();
        cal.setTime(date);
        return DatatypeConverter.printDateTime(cal);
    }
    
    public static String printDateTime(long time, Calendar cal) {
        if (cal == null) return null;

        cal.clear();
        cal.setTimeInMillis(time);
        return DatatypeConverter.printDateTime(cal);
    }
    
    public static String printDateTime(Timestamp ts) {
        return ts == null ? null : printDateTime(ts, Calendar.getInstance());
    }
    
    public static Long getTimeMs(Timestamp ts) {
        return ts == null ? 0L : ts.getTime();
    }

    public static void checkRequiredParam(String name, String value) {
        if (StringUtil.isEmpty(value)) {
            throw new MissingParameterException("Missing " + name);
        }
    }

    public static void checkRequiredParam(String name, Object[] value) {
        if (CommonUtil.isNullOrEmpty(value)) {
            throw new MissingParameterException("Missing " + name);
        }
    }

    public static void checkRequiredParam(String name, Collection<?> value) {
        if (CommonUtil.isNullOrEmpty(value)) {
            throw new MissingParameterException("Missing " + name);
        }
    }

    public static void checkRequiredParam(String name, byte[] value) {
        if (CommonUtil.isNullOrEmpty(value)) {
            throw new MissingParameterException("Missing " + name);
        }
    }

    public static void checkRequiredParam(String name, int value) {
        if (value == 0) {
            throw new MissingParameterException("Missing " + name);
        }
    }

    public static void checkRequiredParam(String name, Object value) {
        if (value == null) {
            throw new MissingParameterException("Missing " + name);
        }
    }

    public static void validateParam(String name, String value, int maxLen) {
        if (StringUtil.isEmpty(value)) {
            throw new MissingParameterException("Missing " + name);
        }
        
        if (value.length() > maxLen) {
            throw new WrongParameterValueException("Too long value for " + name);
        }
        
        if (StringUtil.checkSurrogateCharacters(value)) {
            throw new WrongParameterValueException("Unsupported characters in " + name);
        }
    }
    

  

}
