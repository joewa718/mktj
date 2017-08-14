package com.mktj.cn.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by lije09 on 2016/12/14.
 */
public class DateUtil {
    public static final DateTimeFormatter DATE_FORMAT_MM_YYYY = DateTimeFormatter.ofPattern("MMyyyy");
    public static final DateTimeFormatter DATE_FORMAT_MM_DD_YYYY = DateTimeFormatter.ofPattern("MMddyyyy");
    public static final DateTimeFormatter DATE_FORMAT_YYYY_MM = DateTimeFormatter.ofPattern("yyyyMM");

    private DateUtil() {
    }

    public static String generateTodayStr() {
        StringBuilder sb = new StringBuilder();
        LocalDate today = LocalDate.now();
        return sb.append(today.getYear())
                .append(today.getMonth().getValue())
                .append(today.getDayOfMonth())
                .toString();
    }


    public static String findYearMonthStringByToday() {
        int year;
        int month;
        String date;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        date = year + "" + (month < 10 ? "0" + month : month);
        return date;
    }

    public static Integer findYearMonthIntByToday() {
        int year;
        int month;
        String date;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        date = year + "" + (month < 10 ? "0" + month : month);
        return Integer.valueOf(date);
    }

    public static String addSymbol(Object str, String c) {
        if (str == null) return null;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.insert(4, c);
        return sb.toString();
    }

    public static Integer removeSymbol(String str, String c) {
        if (str == null) return null;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.replace(4, 5, c);
        return Integer.valueOf(sb.toString());
    }


    public static Integer addMinusMonth(Integer date1Int, Integer value) {
        String date1Str = addSymbol(date1Int, "-");
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM");
        Date date1 = null;
        try {
            date1 = format1.parse(date1Str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date1);
        cal.add(Calendar.MONTH, value);
        Date date2 = cal.getTime();
        SimpleDateFormat format2 = new SimpleDateFormat("yyyyMM");
        String date2Str = format2.format(date2);
        return Integer.valueOf(date2Str);
    }

    public static List<Integer> getYTDDate() {
        List<Integer> list = new ArrayList<Integer>();
        Calendar cal = Calendar.getInstance();
        int x = cal.get(Calendar.YEAR);
        int y = cal.get(Calendar.MONTH) + 1;
        int z = cal.get(Calendar.DATE);
        String strY = null;
        String strZ = null;
        String beginStr = null;
        String endStr = null;
        strY = y >= 10 ? String.valueOf(y) : ("0" + y);
        strZ = z >= 10 ? String.valueOf(z) : ("0" + z);
        beginStr = x + "01";
        endStr = x + strY;
        Integer beginDate = Integer.valueOf(beginStr);
        Integer endDate = Integer.valueOf(endStr);
        while (beginDate <= endDate) {
            list.add(beginDate);
            beginDate = addMinusMonth(beginDate, 1);
        }

        return list;
    }

    public static List<Integer> getYTDDateByEndDate(Integer endIndex) {
        Integer endYear = Integer.valueOf(String.valueOf(endIndex).substring(0, 4));
        Integer endMonthly = Integer.valueOf(String.valueOf(endIndex).substring(4, 6));
        List<Integer> list = new ArrayList<Integer>();
        Calendar cal = Calendar.getInstance();
        int x = endYear;
//	        int y = cal.get(Calendar.MONTH) + 1;
        int y = endMonthly;
        int z = cal.get(Calendar.DATE);
        String strY = null;
        String strZ = null;
        String beginStr = null;
        String endStr = null;
        strY = y >= 10 ? String.valueOf(y) : ("0" + y);
        strZ = z >= 10 ? String.valueOf(z) : ("0" + z);
        beginStr = x + "01";
        endStr = x + strY;
        Integer beginDate = Integer.valueOf(beginStr);
        Integer endDate = Integer.valueOf(endStr);
        while (beginDate <= endDate) {
            list.add(beginDate);
            beginDate = addMinusMonth(beginDate, 1);
        }

        return list;
    }

    public static List<Integer[]> getYTDDate(Integer beginIndex, Integer endIndex) {
        Integer beginYear = Integer.valueOf(String.valueOf(beginIndex).substring(0, 4));
        Integer endYear = Integer.valueOf(String.valueOf(endIndex).substring(0, 4));
        Integer endMonthly = Integer.valueOf(String.valueOf(endIndex).substring(4, 6));
        List<Integer[]> list = new ArrayList<Integer[]>();

        while (beginYear <= endYear) {
            Calendar cal = Calendar.getInstance();
            int x = beginYear;
            int y = endMonthly;
            int z = cal.get(Calendar.DATE);
            String strY = null;
            String strZ = null;
            String beginStr = null;
            String endStr = null;
            strY = y >= 10 ? String.valueOf(y) : ("0" + y);
            strZ = z >= 10 ? String.valueOf(z) : ("0" + z);
            beginStr = x + "01";
            endStr = x + strY;
            Integer beginDate = Integer.valueOf(beginStr);
            Integer endDate = Integer.valueOf(endStr);
            if (beginDate < beginIndex) {
                beginYear++;
                continue;
            }
            List<Integer> arrlist = new ArrayList<Integer>();
            while (beginDate <= endDate) {
                arrlist.add(beginDate);
                beginDate = addMinusMonth(beginDate, 1);
            }
            Integer[] a = new Integer[arrlist.size()];
            list.add(arrlist.toArray(a));
            beginYear++;
        }


        return list;
    }


    public static List<Integer[]> getMATDate(Integer beginIndex, Integer endIndex,
                                             Integer period) {
        List<Integer[]> list = new ArrayList<Integer[]>();
        Integer endDate = endIndex;
        Integer beginDate = addMinusMonth(endDate, -(period - 1));
        while (beginDate >= beginIndex) {
            Integer[] arr = new Integer[period];
            Integer temp = beginDate;
            for (int i = 0; i < arr.length; i++) {
                if (temp > endDate) break;
                arr[i] = temp;
                temp = addMinusMonth(temp, 1);
            }
            list.add(arr);
            endDate = addMinusMonth(beginDate, -1);
            beginDate = addMinusMonth(endDate, -(period - 1));
        }
        return list;
    }

    public static List<Integer[]> getSpecifiedMATDate(Integer beginIndex, Integer endIndex,
                                                      Integer period, Integer count) {
        List<Integer[]> list = new ArrayList<Integer[]>();
        Integer endDate = endIndex;
        Integer beginDate = addMinusMonth(endDate, -(period - 1));
        while ((beginDate >= beginIndex) && count > 0) {
            Integer[] arr = new Integer[period];
            Integer temp = beginDate;
            for (int i = 0; i < arr.length; i++) {
                if (temp > endDate) break;
                arr[i] = temp;
                temp = addMinusMonth(temp, 1);
            }
            list.add(arr);
            endDate = addMinusMonth(beginDate, -1);
            beginDate = addMinusMonth(endDate, -(period - 1));
            count--;
        }
        return list;
    }

    public static List<Integer[]> getSpecifiedYTDDate(Integer beginIndex,
                                                      Integer endIndex, Integer count) {
        Integer beginYear = Integer.valueOf(String.valueOf(beginIndex).substring(0, 4));
        Integer endYear = Integer.valueOf(String.valueOf(endIndex).substring(0, 4));
        Integer endMonthly = Integer.valueOf(String.valueOf(endIndex).substring(4, 6));
        List<Integer[]> list = new ArrayList<Integer[]>();

        while ((beginYear <= endYear) && count > 0) {
            Calendar cal = Calendar.getInstance();
            int x = endYear;
//	            int y = cal.get(Calendar.MONTH) + 1;
            int y = endMonthly;
            int z = cal.get(Calendar.DATE);
            String strY = null;
            String strZ = null;
            String beginStr = null;
            String endStr = null;
            strY = y >= 10 ? String.valueOf(y) : ("0" + y);
            strZ = z >= 10 ? String.valueOf(z) : ("0" + z);
            beginStr = x + "01";
            endStr = x + strY;
            Integer beginDate = Integer.valueOf(beginStr);
            Integer endDate = Integer.valueOf(endStr);
            if (beginDate < beginIndex) {
                endYear--;
                count--;
                continue;
            }
            List<Integer> arrlist = new ArrayList<Integer>();
            while (beginDate <= endDate) {
                arrlist.add(beginDate);
                beginDate = addMinusMonth(beginDate, 1);
            }
            Integer[] a = new Integer[arrlist.size()];
            list.add(arrlist.toArray(a));
            endYear--;
            count--;
        }


        return list;
    }

    public static String toFullDatetimeString(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateToString(date, format);
    }

    public static String dateToString(Date date, DateFormat _format) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (_format != null) {
            format = _format;
        }
        return format.format(date);
    }

    public static List<Integer> getDatePeriod(Integer beginDate, Integer endDate) {
        List<Integer> list = new ArrayList<Integer>();
        if (beginDate == null || endDate == null) return list;
        while (beginDate <= endDate) {
            list.add(beginDate);
            beginDate = addMinusMonth(beginDate, 1);
        }

        return list;
    }

    public static Date toDate(String format, String date) {
        Date resDate = null;
        try {
            resDate = new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resDate;
    }

}
