package cn.plateform.utils;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期处理
 */
@Component
public class DateUtil {

    public static final String DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_YMDHM = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_HMS = "HH:mm:ss";
    public static final String DATE_FORMAT_HM = "HH:mm";
    public static final long ONE_DAY_MILLS = 3600000 * 24;

    /**
     * 日期转化为自定义格式时间字符串
     * @param date
     * @param format
     * @return
     */
    public static String dateToFormatString(Date date,String format){
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        return format1.format(date);
    }

    /**
     * 时间字符串转化为日期
     * @param date
     * @param format
     * @return
     */
    public static Date formatStringToDate(String date,String format){
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        try {
            return format1.parse(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.toString());
        }
    }

    /**
     * 计算个两日期的天数 --参数string
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static int getDaysBetween(String startDate, String endDate)
            throws ParseException {
        int dayGap = 0;
        if (startDate != null && startDate.length() > 0 && endDate != null
                && endDate.length() > 0) {
            Date end = formatStringToDate(endDate, DATE_FORMAT_YMD);
            Date start = formatStringToDate(startDate, DATE_FORMAT_YMD);
            dayGap = getDaysBetween(start, end);
        }
        return dayGap;
    }

    /**
     * 计算个两日期的天数 --参数date
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getDaysGapOfDates(Date startDate, Date endDate) {
        int date = 0;
        if (startDate != null && endDate != null) {
            date = getDaysBetween(startDate, endDate);
        }
        return date;
    }

    private static int getDaysBetween(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / ONE_DAY_MILLS);
    }

    /**
     * 计算两个日期之间的年份差距
     * @param firstDate
     * @param secondDate
     * @return
     */
    public static int getYearGapOfDates(Date firstDate, Date secondDate){
        if (firstDate == null || secondDate == null) {
            return 0;
        }
        Calendar helpCalendar = Calendar.getInstance();
        helpCalendar.setTime(firstDate);
        int firstYear = helpCalendar.get(Calendar.YEAR);
        helpCalendar.setTime(secondDate);
        int secondYear = helpCalendar.get(Calendar.YEAR);
        return secondYear - firstYear;
    }

    /**
     * 计算两个日期之间的月份差距
     * @param firstDate
     * @param secondDate
     * @return
     */
    public static int getMonthGapOfDates(Date firstDate, Date secondDate) {
        if (firstDate == null || secondDate == null) {
            return 0;
        }
        return (int) ((secondDate.getTime() - firstDate.getTime())
                / ONE_DAY_MILLS / 30);
    }

    /**
     * 根据指定日期，增加减少天数
     * @param date
     * @param amount
     * @return
     */
    public static Date addOrDecDays(Date date,int amount){
        return add(date,Calendar.DAY_OF_MONTH,amount);
    }

    /**
     *  * 根据指定的日期，类型，增加或减少数量
     *  *
     *  * @param date
     *  * @param calendarField
     *  * @param amount
     *  * @return
     *  
     */
    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    /**
     *获取当天的开始时间
     */
    public static Date getNowDayBegin(){
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_WEEK,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }

    /**
     * 获取当天的结束时间
     * @return
     */
    public static Date getNowDayEnd(){
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_WEEK,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        return calendar.getTime();
    }

    /**
     * 获取本周开始时间
     * @return
     */
    @SuppressWarnings("unused")
    public static Date getNowWeekBegin(){
        Date date = new Date();
        if (date==null){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK);//[1,2,3,4,5,6,7]周天，周一。。。。
        if (week==1){
            week +=7;
        }
        calendar.add(Calendar.DATE,2-week);
        return getDayStartTime(calendar.getTime());
    }

    /**
     * 获取本周的结束时间
     * @return
     */
    public static Date getNowWeekEnd(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getNowWeekBegin());
        calendar.add(Calendar.DAY_OF_WEEK,6);
        return getDayEndTime(calendar.getTime());
    }

    /**
     * 获取本月开始时间
     * @return
     */
    public static Date getNowMonthBegin(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(),getNowMonth()-1,1);
        return getDayStartTime(calendar.getTime());
    }

    /**
     * 获取本月结束时间
     * @return
     */
    public static Date getNowMonthEnd(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(),getNowMonth()-1,1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(),getNowMonth()-1,day);
        return getDayEndTime(calendar.getTime());
    }

    /**
     * 获取本年的开始时间
     * @return
     */
    public static Date getNowYearBegin(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        return getDayStartTime(cal.getTime());
    }

    /**
     * 获取本年的结束时间
     * @return
     */
    public static Date getNowYearEnd(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);
        return getDayEndTime(cal.getTime());
    }

    /**
     * 获取莫个日期的开始时间
     * @param date
     * @return
     */
    public static Timestamp getDayStartTime(Date date){
        Calendar calendar = Calendar.getInstance();
        if (date!=null){
            calendar.setTime(date);
        }
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONDAY),calendar.get(Calendar.DAY_OF_MONTH),0,0,0);
        calendar.set(Calendar.MILLISECOND,0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取莫个日期的结束时间
     * @param date
     * @return
     */
    public static Timestamp getDayEndTime(Date date){
        Calendar calendar = Calendar.getInstance();
        if (date!=null){
            calendar.setTime(date);
        }
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONDAY),calendar.get(Calendar.DAY_OF_MONTH),23,59,59);
        calendar.set(Calendar.MILLISECOND,999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取本年是哪一年
     * @return
     */
    public static Integer getNowYear(){
        Date date = new Date();
        GregorianCalendar calendar = (GregorianCalendar) Calendar.getInstance();
        calendar.setTime(date);
        return Integer.valueOf(calendar.get(1));
    }

    /**
     * 获取本月是那一月
     * @return
     */
    public static Integer getNowMonth(){
        Date date = new Date();
        GregorianCalendar calendar = (GregorianCalendar) Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(2)+1;
    }


}
