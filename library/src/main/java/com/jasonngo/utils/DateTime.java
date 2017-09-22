package com.jasonngo.utils;

import android.text.TextUtils;
import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by tuanhuynh on 7/25/15.
 */
public class DateTime {

    // 2017-06-05T20:28:26.000+10:00
    public static final String FORMAT_TIME_ISO = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static StringBuilder formatBuilder = new StringBuilder();
    private static Formatter formatter = new Formatter(formatBuilder, Locale.getDefault());

    /**
     * Return an ISO 8601 combined date and time string for current date/time
     *
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss'Z'"
     */
    public static String getISO8601StringForCurrentDate() {
        return getISO8601StringForDate(new Date(), Locale.getDefault());
    }

    /**
     * Return an ISO 8601 combined date and time string for specified date/time
     *
     * @param date Date
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss'Z'"
     */
    public static String getISO8601StringForDate(Date date, Locale locale) {
        DateFormat dateFormat = new SimpleDateFormat(FORMAT_TIME_ISO, locale);
        return dateFormat.format(date);
    }

    public static String getISO8601StringForDate(String strDate, Locale locale) {
        Date date = getDateUTC(strDate);
        DateFormat dateFormat = new SimpleDateFormat(FORMAT_TIME_ISO, locale);
        return dateFormat.format(date);
    }

    public static Date getISO8601Date(String strDate) {
        DateFormat dateFormat = new SimpleDateFormat(FORMAT_TIME_ISO, Locale.getDefault());
        try {
            return dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static long getUniTimeFrom(int date) {
        long realDate = date * 24 * 60 * 60 * 1000;
        return System.currentTimeMillis() - realDate;
    }

    /**
     * Private constructor: class cannot be instantiated
     */
    private DateTime() {
    }

    /**
     * Return an Date with timezone default
     *
     * @param timestamp
     * @param pattern
     * @return String with format pattern
     */
    public static String getDateTime(long timestamp, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        try {
            if (String.valueOf(timestamp).length() < 13)
                timestamp = timestamp * 1000;
            Date date = new Date(timestamp);
            return sdf.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sdf.format(new Date());
    }


    public static String getDateTime(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        try {
            return sdf.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sdf.format(new Date());
    }
    /**
     * Return an Date from string
     *
     * @return Date
     */
    public static Date getDate(String strDate) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(getMillisecond(strDate));
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            Date currentTimeZone = calendar.getTime();
            return currentTimeZone;
        } catch (Exception e) {
        }
        return null;
    }

    public static Date convertStringToDate(String strDate, String dateFormat) {
        DateFormat readFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
        Date date = new Date();
        try {
            if(!TextUtils.isEmpty(strDate))
                date = readFormat.parse(strDate);
        } catch (ParseException ex) {
        }
        return date;
    }

//    public static String convertStringToMilliSecond(long milliTime, String pattern){
//        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//        Date date = new Date();
//        if (milliTime != 0)
//            date = new Date(milliTime*1000);
//        return sdf.format(date);
//    }

    /**
     * Return an Date from string
     *
     * @return Date
     */
    public static Date getDateUTC(String strDate) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getTimeZone("UTC");
            calendar.setTimeInMillis(getMillisecond(strDate));
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            return calendar.getTime();
        } catch (Exception e) {
        }
        return null;
    }

    public static long getMillisecond(String dateString) {
        return getMillisecond("HH:mm MMMM dd, yyyy", dateString);
    }

    // Get millisecond
    public static long getMillisecond(String pattern, String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        try {
            Date date = formatter.parse(dateString);
            long milli = date.getTime();
            return milli;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Formats the specified milliseconds to a human readable format
     * in the form of (Hours : Minutes : Seconds).  If the specified
     * milliseconds is less than 0 the resulting format will be
     * "--:--" to represent an unknown time
     *
     * @param milliseconds The time in milliseconds to format
     * @return The human readable time
     */
    public static String formatMs(long milliseconds) {
        if (milliseconds < 0) {
            return "--:--";
        }

        long seconds = (milliseconds % DateUtils.MINUTE_IN_MILLIS) / DateUtils.SECOND_IN_MILLIS;
        long minutes = (milliseconds % DateUtils.HOUR_IN_MILLIS) / DateUtils.MINUTE_IN_MILLIS;
        long hours = (milliseconds % DateUtils.DAY_IN_MILLIS) / DateUtils.HOUR_IN_MILLIS;

        formatBuilder.setLength(0);
        if (hours > 0) {
            return formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        }

        return formatter.format("%02d:%02d", minutes, seconds).toString();
    }

}